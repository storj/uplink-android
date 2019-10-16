package io.storj;

import io.storj.libuplink.mobile.Mobile;

/**
 * CipherSuite specifies one of the encryption suites supported by Storj
 * libraries for encryption of in-network data.
 */
public enum CipherSuite {
    NONE(Mobile.CipherSuiteEncNull),
    AESGCM(Mobile.CipherSuiteEncAESGCM),
    SECRET_BOX(Mobile.CipherSuiteEncSecretBox);

    private byte value;

    CipherSuite(byte value) {
        this.value = value;
    }

    byte getValue() {
        return value;
    }

    public static CipherSuite fromValue(byte value) {
        for (CipherSuite v : CipherSuite.values()) {
            if (v.value == value) {
                return v;
            }
        }
        return null;
    }
}
