package io.storj;

public class Key {

    private byte[] keyData;

    public Key(byte[] keyData){
        this.keyData = keyData;
    }

    public Key(String keyData){
        this.keyData = keyData.getBytes();
    }

    byte[] getKeyData() {
        return keyData;
    }

}
