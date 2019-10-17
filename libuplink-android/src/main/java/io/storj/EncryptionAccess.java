package io.storj;

import io.storj.libuplink.mobile.Mobile;

/**
 * EncryptionAccess represents an encryption access context. It holds information
 * about how various buckets and objects should be encrypted and decrypted.
 */
public class EncryptionAccess {

    private io.storj.libuplink.mobile.EncryptionAccess access;

    // We need the 'workaround' parameter to avoid strange compiler issue
    // for clients of this library that use the public constructor.
    EncryptionAccess(io.storj.libuplink.mobile.EncryptionAccess access, Object workaround) {
        this.access = access;
    }

    /**
     * Constructs encryption access with salted key.
     *
     * @param saltedKey salted key
     * @throws StorjException
     */
    public EncryptionAccess(Key saltedKey) throws StorjException {
        try {
            this.access = Mobile.newEncryptionAccessWithDefaultKey(saltedKey.getKeyData());
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Merges the other encryption access context into this one. In cases
     * of conflicting path decryption settings (including if both accesses have
     * a default key), the new settings are kept.
     *
     * @param other other encryption access
     * @throws StorjException
     */
    public void merge(EncryptionAccess other) throws StorjException {
        try {
            this.access.import_(other.internal());
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Serialize encryption access to base58 string.
     *
     * @return serialized encryption access
     * @throws StorjException
     */
    public String serialize() throws StorjException {
        try {
            return access.serialize();
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Parses a base58 serialized encryption access into a working one.
     *
     * @param serialized base58 serialized encryption access
     * @return encryption access
     * @throws StorjException
     */
    public static EncryptionAccess parse(String serialized) throws StorjException {
        try {
            return new EncryptionAccess(Mobile.parseEncryptionAccess(serialized), null);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    io.storj.libuplink.mobile.EncryptionAccess internal() {
        return access;
    }
}
