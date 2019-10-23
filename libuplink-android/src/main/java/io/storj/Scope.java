package io.storj;

import io.storj.libuplink.mobile.EncryptionRestrictions;

/**
 * Represents all credentials you need to access data on the Storj network:
 * <ul>
 *     <li>Satellite address in the form of "host:port"</li>
 *     <li>{@link ApiKey} to access the satellite</li>
 *     <li>{@link EncryptionAccess} for accessing the encrypted content</li>
 * </ul>
 */
public class Scope {

    private io.storj.libuplink.mobile.Scope scope;

    Scope(io.storj.libuplink.mobile.Scope scope) {
        this.scope = scope;
    }

    /**
     * Creates new {@link Scope}.
     *
     * @param satelliteAddress a satellite address
     * @param apiKey an {@link ApiKey} to access satellite
     * @param encryptionAccess an {@link EncryptionAccess} for accessin the encrypted content
     */
    public Scope(String satelliteAddress, ApiKey apiKey, EncryptionAccess encryptionAccess) {
        this.scope = io.storj.libuplink.mobile.Mobile.newScope(satelliteAddress, apiKey.internal(), encryptionAccess.internal());
    }

    /**
     * Returns the satellite address.
     *
     * @return the satellite address.
     */
    public String getSatelliteAddress() {
        return this.scope.satelliteAddr();
    }

    /**
     * Returns the {@link ApiKey} to access the satellite.
     *
     * @return the {@link ApiKey}
     */
    public ApiKey getApiKey() {
        return new ApiKey(this.scope.apiKey());
    }

    /**
     * Returns the {@link EncryptionAccess} to access the encrypted content.
     *
     * @return the {@link EncryptionAccess}
     */
    public EncryptionAccess getEncryptionAccess() {
        return new EncryptionAccess(this.scope.encryptionAccess(), null);
    }

    /**
     * Restricts this {@link Scope} with a list of {@link EncryptionRestriction}.
     *
     * @param restrictions a list of {@link EncryptionRestriction}
     * @return new restricted {@link Scope}
     * @throws StorjException in case of error
     */
    public Scope restrict(EncryptionRestriction... restrictions) throws StorjException {
        return this.restrict(null, restrictions);
    }

    /**
     * Restricts this {@link Scope} with a {@link Caveat} and a list of
     * {@link EncryptionRestriction}.
     *
     * @param caveat a {@link Caveat}
     * @param restrictions a list of {@link EncryptionRestriction}
     * @return new restricted {@link Scope}
     * @throws StorjException in case of error
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
     * Serializes this {@link Scope} to base58-encoded {@link String}.
     *
     * @return a {@link String} with serialized scope
     * @throws StorjException in case of error
     */
    public String serialize() throws StorjException {
        try {
            return this.scope.serialize();
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Parses a base58-encoded {@link String} to an {@link Scope}.
     *
     * @param serialized a base58-encoded {@link String}
     * @return the parsed {@link Scope}
     * @throws StorjException in case of error
     */
    public static Scope parse(String serialized) throws StorjException {
        try {
            return new Scope(io.storj.libuplink.mobile.Mobile.parseScope(serialized));
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

}
