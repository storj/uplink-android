package io.storj;

import io.storj.libuplink.mobile.Mobile;

/**
 * Represents an encryption access context. It holds information about how various buckets and
 * objects should be encrypted and decrypted.
 */
public class EncryptionAccess {

    private io.storj.libuplink.mobile.EncryptionAccess access;

    // We need the 'workaround' parameter to avoid strange compiler issue
    // for clients of this library that use the public constructor.
    EncryptionAccess(io.storj.libuplink.mobile.EncryptionAccess access, Object workaround) {
        this.access = access;
    }

    /**
     * Creates new {@link EncryptionAccess} with salted {@link Key}.
     *
     * <p>Salted keys can be created with
     * {@link Key#getSaltedKeyFromPassphrase(Project, String)}.</p>
     *
     * @param saltedKey a salted {@link Key}
     * @throws StorjException in case of error
     * @see Key#getSaltedKeyFromPassphrase(Project, String)
     */
    public EncryptionAccess(Key saltedKey) throws StorjException {
        try {
            this.access = Mobile.newEncryptionAccessWithDefaultKey(saltedKey.getKeyData());
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Merges another {@link EncryptionAccess} into this one.
     *
     * <p>The new settings are kept in cases of conflicting path decryption settings, including if
     * both accesses have a default key.</p>
     *
     * @param other an {@link EncryptionAccess} to merge
     * @throws StorjException in case of error
     */
    public void merge(EncryptionAccess other) throws StorjException {
        try {
            this.access.import_(other.internal());
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Overrides the encryption key for the prefix with encryption key.
     *
     * @param bucket bucket name
     * @param prefix prefix in a bucket
     * @param key    the key
     * @throws StorjException in case of error
     */
    public void overrideEncryptionKey(String bucket, String prefix, Key key) throws StorjException {
        try {
            access.overrideEncryptionKey(bucket, prefix, key.getKeyData());
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Serialize this {@link EncryptionAccess} to base58-encoded {@link String}.
     *
     * @return a {@link String} with serialized encryption access context
     * @throws StorjException in case of error
     */
    public String serialize() throws StorjException {
        try {
            return access.serialize();
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Parses a base58-encoded {@link String} to an {@link EncryptionAccess}.
     *
     * @param serialized a base58-encoded {@link String}
     * @return the parsed {@link EncryptionAccess}
     * @throws StorjException in case of error
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
