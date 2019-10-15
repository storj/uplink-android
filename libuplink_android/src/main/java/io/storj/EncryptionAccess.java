package io.storj;

import io.storj.libuplink.mobile.Mobile;

/**
 * EncryptionAccess represents an encryption access context. It holds information
 * about how various buckets and objects should be encrypted and decrypted.
 */
public class EncryptionAccess {

    private io.storj.libuplink.mobile.EncryptionAccess access;

    EncryptionAccess(io.storj.libuplink.mobile.EncryptionAccess access) {
        this.access = access;
    }

    public EncryptionAccess(Key saltedKey) throws StorjException {
        try {
            this.access = Mobile.newEncryptionAccessWithDefaultKey(saltedKey.getKeyData());
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    public void merge(EncryptionAccess other) throws StorjException {
        try {
            this.access.import_(other.internal());
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    public String serialize() throws StorjException {
        try {
            return access.serialize();
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    public static EncryptionAccess parse(String serialized) throws StorjException {
        try {
            return new EncryptionAccess(Mobile.parseEncryptionAccess(serialized));
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    io.storj.libuplink.mobile.EncryptionAccess internal() {
        return access;
    }
}
