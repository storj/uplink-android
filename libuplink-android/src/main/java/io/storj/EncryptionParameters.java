package io.storj;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the encryption parameters used for encrypting the data on the network.
 */
public class EncryptionParameters implements Serializable {

    private CipherSuite cipher;
    private int blockSize;

    EncryptionParameters(io.storj.libuplink.mobile.EncryptionParameters params) {
        this.cipher = CipherSuite.fromValue(params.getCipherSuite());
        this.blockSize = params.getBlockSize();
    }

    /**
     * Returns the {@link CipherSuite} used for encryption.
     *
     * @return a {@link CipherSuite}
     */
    public CipherSuite getCipher() {
        return cipher;
    }

    /**
     * Returns the block size at which encryption is performed.
     *
     * <p>It is important to distinguish this from the block size used by the
     * cipher suite (probably 128 bits). There is some small overhead for
     * each encryption unit, so the block size should not be too small, but
     * smaller sizes yield shorter first-byte latency and better seek times.
     * Note that the block size itself is the size of data blocks _after_ they
     * have been encrypted and the authentication overhead has been added.
     * It is not the size of the data blocks to be encrypted.</p>
     *
     * @return the block size in bytes
     */
    public int getBlockSize() {
        return blockSize;
    }

    /**
     * Two {@link EncryptionParameters} objects are equal if both their {@link CipherSuite} and
     * block size are equal.
     *
     * @return <code>true</code> if this object is the same as the specified object;
     *          <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncryptionParameters that = (EncryptionParameters) o;
        return blockSize == that.blockSize &&
                cipher == that.cipher;
    }

    /**
     * The hash code value of {@link EncryptionParameters} is the hash code value of its
     * {@link CipherSuite} and block size.
     *
     * @return a hash code value for this object
     */
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
     * Builder for {@link EncryptionParameters} objects.
     */
    public static class Builder {

        private CipherSuite cipher;
        private int blockSize;

        /**
         * Determines the {@link CipherSuite} to use for encryption.
         *
         * @param cipher a {@link CipherSuite}
         * @return a reference to this object
         */
        public Builder setCipher(CipherSuite cipher) {
            this.cipher = cipher;
            return this;
        }

        /**
         * Determines the block size at which encryption is performed.
         *
         * <p>It is important to distinguish this from the block size used by the
         * cipher suite (probably 128 bits). There is some small overhead for
         * each encryption unit, so the block size should not be too small, but
         * smaller sizes yield shorter first-byte latency and better seek times.
         * Note that the block size itself is the size of data blocks _after_ they
         * have been encrypted and the authentication overhead has been added.
         * It is not the size of the data blocks to be encrypted.</p>
         *
         * @param size block size in bytes
         * @return a reference to this object
         */
        public Builder setBlockSize(int size) {
            this.blockSize = size;
            return this;
        }

        /**
         * Creates the new {@link EncryptionParameters} object from this builder.
         *
         * @return an {@link EncryptionParameters}
         */
        public EncryptionParameters build() {
            return new EncryptionParameters(this);
        }
    }
}
