package io.storj;

import io.storj.libuplink.mobile.Mobile;

/**
 * EncryptionAccess represents an encryption access context. It holds information
 * about how various buckets and objects should be encrypted and decrypted.
 */
public class EncryptionAccess {

    io.storj.libuplink.mobile.EncryptionAccess access;

    public static EncryptionAccess withDefaultKey(Key defaultKey) throws StorjException {
        try {
            return new EncryptionAccess(Mobile.newEncryptionAccessWithDefaultKey(defaultKey.getKeyData()));
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    public static EncryptionAccess withRoot(String bucket, String unencryptedPath, String encryptedPath, Key key) throws StorjException {
        try {
            return new EncryptionAccess(Mobile.newEncryptionAccessWithRoot(bucket, unencryptedPath, encryptedPath, key.getKeyData()));
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    EncryptionAccess(io.storj.libuplink.mobile.EncryptionAccess access) {
        this.access = access;
    }

    public EncryptionAccess() {
        this(new io.storj.libuplink.mobile.EncryptionAccess());
    }

    public void setDefaultKey(Key key) throws StorjException {
        try {
            this.access.setDefaultKey(key.getKeyData());
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    public String serialize() throws StorjException {
        try {
            return this.access.serialize();
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }
}
