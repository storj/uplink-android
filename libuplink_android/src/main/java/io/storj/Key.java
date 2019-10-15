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

    public static Key getSaltedKeyFromPassphrase(Project project, String passphrase) throws StorjException {
        try {
            byte[] keyData = project.internal().saltedKeyFromPassphrase(passphrase);
            return new Key(keyData);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

}
