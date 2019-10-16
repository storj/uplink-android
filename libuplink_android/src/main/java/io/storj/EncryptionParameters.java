package io.storj;

/**
 * EncryptionParameters is the cipher suite and parameters used for encryption
 */
public class EncryptionParameters {

    private io.storj.libuplink.mobile.EncryptionParameters params;

    EncryptionParameters(io.storj.libuplink.mobile.EncryptionParameters params) {
        this.params = params;
    }

    public CipherSuite getCipher() {
        return CipherSuite.fromValue(params.getCipherSuite());
    }

    public int getBlockSize() {
        return params.getBlockSize();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EncryptionParameters)) {
            return false;
        }
        EncryptionParameters that = (EncryptionParameters) obj;
        return params.equals(that.params);
    }

    @Override
    public int hashCode() {
        return params.hashCode();
    }

    io.storj.libuplink.mobile.EncryptionParameters internal() {
        return params;
    }

    private EncryptionParameters(Builder builder) {
        this.params = new io.storj.libuplink.mobile.EncryptionParameters();
        if (builder.cipher != null) {
            this.params.setCipherSuite(builder.cipher.getValue());
        }
        this.params.setBlockSize(builder.blockSize);
    }

    /**
     * Builder for EncryptionParameters.
     */
    public static class Builder {

        private CipherSuite cipher;
        private int blockSize;

        /**
         * CipherSuite specifies the cipher suite to be used for encryption.
         *
         * @param cipher cipher suite
         * @return the builder
         */
        public Builder setCipher(CipherSuite cipher) {
            this.cipher = cipher;
            return this;
        }

        /**
         * BlockSize determines the unit size at which encryption is performed.
         * It is important to distinguish this from the block size used by the
         * cipher suite (probably 128 bits). There is some small overhead for
         * each encryption unit, so BlockSize should not be too small, but
         * smaller sizes yield shorter first-byte latency and better seek times.
         * Note that BlockSize itself is the size of data blocks _after_ they
         * have been encrypted and the authentication overhead has been added.
         * It is not the size of the data blocks to be encrypted.
         *
         * @param size block size in bytes
         * @return the builder
         */
        public Builder setBlockSize(int size) {
            this.blockSize = size;
            return this;
        }

        public EncryptionParameters build() {
            return new EncryptionParameters(this);
        }
    }
}
