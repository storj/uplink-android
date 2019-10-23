package io.storj;

import io.storj.libuplink.mobile.BucketConfig;

/**
 * Options for creating new buckets.
 *
 * @see Project#createBucket(String, BucketCreateOption...)
 */
public class BucketCreateOption {

    private enum Key {
        PATH_CIPHER,
        ENCRYPTION_PARAMETERS,
        REDUNDANCY_SCHEME,
        SEGMENTS_SIZE
    }

    private Key key;

    private Object value;

    BucketCreateOption(Key key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Option for setting the {@link CipherSuite} for encrypting all object paths in the bucket.
     *
     * @param pathCipher a {@link CipherSuite}
     * @return a {@link BucketCreateOption}
     */
    public static BucketCreateOption pathCipher(CipherSuite pathCipher) {
        return new BucketCreateOption(Key.PATH_CIPHER, pathCipher);
    }

    /**
     * Option for setting the default {@link EncryptionParameters} used when uploading new objects
     * to the bucket.
     *
     * @param encryptionParameters an {@link EncryptionParameters}
     * @return a {@link BucketCreateOption}
     */
    public static BucketCreateOption encryptionParameters(EncryptionParameters encryptionParameters) {
        return new BucketCreateOption(Key.ENCRYPTION_PARAMETERS, encryptionParameters);
    }

    /**
     * Option for setting the default {@link RedundancyScheme} used when uploading new objects to
     * the bucket.
     *
     * @param redundancyScheme a {@link RedundancyScheme}
     * @return a {@link BucketCreateOption}
     */
    public static BucketCreateOption redundancyScheme(RedundancyScheme redundancyScheme) {
        return new BucketCreateOption(Key.REDUNDANCY_SCHEME, redundancyScheme);
    }

    /**
     * Option for setting the segments size used when uploading new objects in the bucket.
     *
     * @param segmentsSize size of segments
     * @return a {@link BucketCreateOption}
     */
    public static BucketCreateOption segmentsSize(long segmentsSize) {
        return new BucketCreateOption(Key.SEGMENTS_SIZE, segmentsSize);
    }

    static io.storj.libuplink.mobile.BucketConfig internal(BucketCreateOption... options) {
        io.storj.libuplink.mobile.BucketConfig bucketConfig = new BucketConfig();

        for (BucketCreateOption option : options) {
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
