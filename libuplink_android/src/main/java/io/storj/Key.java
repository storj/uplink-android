package io.storj;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Key {

    private byte[] keyData;

    public Key(byte[] keyData) {
        this.keyData = keyData;
    }

    public Key(String keyData) {
        this.keyData = keyData.getBytes(UTF_8);
    }

    byte[] getKeyData() {
        return keyData;
    }

}
