package io.storj;

import io.storj.libuplink.mobile.Mobile;

/**
 * Enumeration of the cipher suites supported by Storj libraries for encrypting the object paths
 * and the data stored on the network.
 */
public enum CipherSuite {
    /**
     * No encryption. If this is used, the data will be unencrypted!
     */
    NONE(Mobile.CipherSuiteEncNull),

    /**
     * AES-GCM 256-bit encryption.
     */
    AESGCM(Mobile.CipherSuiteEncAESGCM),

    /**
     * SecretBox encryption.
     */
    SECRET_BOX(Mobile.CipherSuiteEncSecretBox);

    private byte value;

    CipherSuite(byte value) {
        this.value = value;
    }

    byte getValue() {
        return value;
    }

    static CipherSuite fromValue(byte value) {
        for (CipherSuite v : CipherSuite.values()) {
            if (v.value == value) {
                return v;
            }
        }
        return null;
    }
}
