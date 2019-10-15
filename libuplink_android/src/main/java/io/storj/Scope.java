package io.storj;

import io.storj.libuplink.mobile.EncryptionRestrictions;

public class Scope {

    private io.storj.libuplink.mobile.Scope scope;

    Scope(io.storj.libuplink.mobile.Scope scope) {
        this.scope = scope;
    }

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

    public Scope restrict(EncryptionRestriction... restrictions) throws StorjException {
        return this.restrict(null, restrictions);
    }

    public Scope restrict(Caveat caveat, EncryptionRestriction... restrictions) throws StorjException{
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

    public String serialize() throws StorjException {
        try {
            return this.scope.serialize();
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    public static Scope parse(String serialized) throws StorjException {
        try {
            return new Scope(io.storj.libuplink.mobile.Mobile.parseScope(serialized));
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

}
