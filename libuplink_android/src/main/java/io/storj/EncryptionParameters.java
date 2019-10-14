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

    public static class Builder {

        private CipherSuite cipher;
        private int blockSize;

        public Builder setCipher(CipherSuite cipher) {
            this.cipher = cipher;
            return this;
        }

        public Builder setBlockSize(int size) {
            this.blockSize = size;
            return this;
        }

        public EncryptionParameters build() {
            return new EncryptionParameters(this);
        }
    }
}
