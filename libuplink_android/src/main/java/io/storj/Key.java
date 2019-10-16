package io.storj;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Key {

    private byte[] keyData;

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
     * Returns a key generated from the given passphrase using a stable, project-specific salt
     *
     * @param project    project which will be used to generate key
     * @param passphrase human readable passphrase
     * @return salted key
     * @throws StorjException
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
