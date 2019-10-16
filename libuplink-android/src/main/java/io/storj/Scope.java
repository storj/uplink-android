package io.storj;

import io.storj.libuplink.mobile.EncryptionRestrictions;

/**
 * Scope is a serializable type that represents all of the credentials you need
 * to open a project and some amount of buckets
 */
public class Scope {

    private io.storj.libuplink.mobile.Scope scope;

    Scope(io.storj.libuplink.mobile.Scope scope) {
        this.scope = scope;
    }

    /**
     * Creates new scope.
     *
     * @param satelliteAddress satellite address
     * @param apiKey           API key to access satellite
     * @param encryptionAccess encryption access
     */
    public Scope(String satelliteAddress, ApiKey apiKey, EncryptionAccess encryptionAccess) {
        this.scope = io.storj.libuplink.mobile.Mobile.newScope(satelliteAddress, apiKey.internal(), encryptionAccess.internal());
    }

    public String getSatelliteAddress() {
        return this.scope.satelliteAddr();
    }

    public ApiKey getApiKey() {
        return new ApiKey(this.scope.apiKey());
    }

    public EncryptionAccess getEncryptionAccess() {
        return new EncryptionAccess(this.scope.encryptionAccess());
    }

    /**
     * Restricts access to resources.
     *
     * @param restrictions set of restrictions
     * @return new restricted scope
     * @throws StorjException
     */
    public Scope restrict(EncryptionRestriction... restrictions) throws StorjException {
        return this.restrict(null, restrictions);
    }

    /**
     * Restricts access to resources.
     *
     * @param caveat       caveat
     * @param restrictions set of restrictions
     * @return new restricted scope
     * @throws StorjException
     */
    public Scope restrict(Caveat caveat, EncryptionRestriction... restrictions) throws StorjException {
        ApiKey apiKey = this.getApiKey();
        if (caveat != null) {
            apiKey = apiKey.restrict(caveat);
        }

        EncryptionRestrictions tempRestrictions = new EncryptionRestrictions();
        for (EncryptionRestriction restriction : restrictions) {
            tempRestrictions.add(restriction.internal());
        }

        try {
            io.storj.libuplink.mobile.Scope libScope = scope.encryptionAccess().restrict(this.getSatelliteAddress(), apiKey.internal(), tempRestrictions);
            return new Scope(libScope);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Serializes a scope to a base58-encoded string.
     *
     * @return serialized scope
     * @throws StorjException
     */
    public String serialize() throws StorjException {
        try {
            return this.scope.serialize();
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * ParseScope unmarshals a base58 encoded scope protobuf and decodes
     * the fields into the Scope convenience type. It will return an error if the
     * protobuf is malformed or field validation fails.
     *
     * @param serialized serialized scope
     * @return new scope
     * @throws StorjException
     */
    public static Scope parse(String serialized) throws StorjException {
        try {
            return new Scope(io.storj.libuplink.mobile.Mobile.parseScope(serialized));
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

}
