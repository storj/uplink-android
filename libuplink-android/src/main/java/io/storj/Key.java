package io.storj;

import io.storj.libuplink.mobile.Mobile;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Represents the largest key used by any encryption protocol.
 *
 * @see EncryptionAccess#EncryptionAccess(Key)
 */
public class Key {

    private byte[] keyData;

    /**
     * Derives a salted key for password using the salt.
     *
     * @param password the password
     * @param salt     the salt
     * @return salted key
     * @throws StorjException in case of error
     */
    public static Key deriveEncryptionKey(byte[] password, byte[] salt) throws StorjException {
        try {
            byte[] keyData = Mobile.deriveEncryptionKey(password, salt);
            return new Key(keyData);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    Key(byte[] keyData) {
        this.keyData = keyData;
    }

    Key(String keyData) {
        this.keyData = keyData.getBytes(UTF_8);
    }

    byte[] getKeyData() {
        return keyData;
    }

    /**
     * Returns a key generated from the given passphrase using a stable, project-specific salt.
     *
     * @param project    project which will be used to for the salt
     * @param passphrase human-readable passphrase
     * @return a salted {@link Key}
     * @throws StorjException in case of error
     */
    public static Key getSaltedKeyFromPassphrase(Project project, String passphrase) throws StorjException {
        try {
            byte[] keyData = project.internal().saltedKeyFromPassphrase(passphrase);
            return new Key(keyData);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

}
