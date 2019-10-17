package io.storj;

import java.io.Serializable;
import java.util.Objects;

/**
 * EncryptionParameters is the cipher suite and parameters used for encryption
 */
public class EncryptionParameters implements Serializable {

    private CipherSuite cipher;
    private int blockSize;

    EncryptionParameters(io.storj.libuplink.mobile.EncryptionParameters params) {
        this.cipher = CipherSuite.fromValue(params.getCipherSuite());
        this.blockSize = params.getBlockSize();
    }

    public CipherSuite getCipher() {
        return cipher;
    }

    public int getBlockSize() {
        return blockSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncryptionParameters that = (EncryptionParameters) o;
        return blockSize == that.blockSize &&
                cipher == that.cipher;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cipher, blockSize);
    }

    io.storj.libuplink.mobile.EncryptionParameters internal() {
        io.storj.libuplink.mobile.EncryptionParameters params = new io.storj.libuplink.mobile.EncryptionParameters();
        if (cipher != null) {
            params.setCipherSuite(cipher.getValue());
        }
        params.setBlockSize(blockSize);
        return params;
    }

    private EncryptionParameters(Builder builder) {
        this.cipher = builder.cipher;
        this.blockSize = builder.blockSize;
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
