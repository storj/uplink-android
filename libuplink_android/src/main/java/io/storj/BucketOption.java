package io.storj;

import io.storj.libuplink.mobile.BucketConfig;

public class BucketOption {

    private enum Key {
        PATH_CIPHER,
        ENCRYPTION_PARAMETERS,
        REDUNDANCY_SCHEME,
        SEGMENTS_SIZE
    }

    private Key key;

    private Object value;

    BucketOption(Key key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static BucketOption pathCipher(CipherSuite pathCipher) {
        return new BucketOption(Key.PATH_CIPHER, pathCipher);
    }

    public static BucketOption encryptionParameters(EncryptionParameters encryptionParameters) {
        return new BucketOption(Key.ENCRYPTION_PARAMETERS, encryptionParameters);
    }

    public static BucketOption redundancyScheme(RedundancyScheme redundancyScheme) {
        return new BucketOption(Key.REDUNDANCY_SCHEME, redundancyScheme);
    }

    public static BucketOption segmentsSize(long segmentsSize) {
        return new BucketOption(Key.SEGMENTS_SIZE, segmentsSize);
    }

    static io.storj.libuplink.mobile.BucketConfig internal(BucketOption... options) {
        io.storj.libuplink.mobile.BucketConfig bucketConfig = new BucketConfig();

        for (BucketOption option : options) {
            if (option.key == Key.PATH_CIPHER) {
                CipherSuite cipherSuite = (CipherSuite) option.value;
                bucketConfig.setPathCipher(cipherSuite.getValue());
            } else if (option.key == Key.ENCRYPTION_PARAMETERS) {
                EncryptionParameters encryptionParameters = (EncryptionParameters) option.value;
                bucketConfig.setEncryptionParameters(encryptionParameters.internal());
            } else if (option.key == Key.REDUNDANCY_SCHEME) {
                RedundancyScheme redundancyScheme = (RedundancyScheme) option.value;
                bucketConfig.setRedundancyScheme(redundancyScheme.internal());
            } else if (option.key == Key.SEGMENTS_SIZE) {
                bucketConfig.setSegmentsSize((Long) option.value);
            }
        }
        return bucketConfig;
    }

}
