package io.storj;

import io.storj.libuplink.mobile.Mobile;

public class EncryptionAccess {

    io.storj.libuplink.mobile.EncryptionAccess access;

    public static EncryptionAccess WithDefaultKey(byte[] key) throws StorjException {
        try {
            return new EncryptionAccess(Mobile.newEncryptionAccessWithDefaultKey(key));
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    public static EncryptionAccess WithRoot(String bucket, String unencryptedPath, String encryptedPath, byte[] keyData) throws StorjException {
        try {
            return new EncryptionAccess(Mobile.newEncryptionAccessWithRoot(bucket, unencryptedPath, encryptedPath, keyData));
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

    public void setDefaultKey(byte[] key) throws StorjException {
        try {
            this.access.setDefaultKey(key);
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
