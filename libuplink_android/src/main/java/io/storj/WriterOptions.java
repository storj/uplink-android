package io.storj;

public class WriterOptions {

    private io.storj.libuplink.mobile.WriterOptions options;

    private WriterOptions(Builder builder){
        this.options = new io.storj.libuplink.mobile.WriterOptions();
        if (builder.contentType != null) {
            this.options.setContentType(builder.contentType);
        }
        if (builder.encryptionParameters != null) {
            this.options.setEncryptionParameters(builder.encryptionParameters.internal());
        }
        this.options.setExpires(builder.expires);
        if (builder.rs != null) {
            this.options.setRedundancyScheme(builder.rs.internal());
        }
    }

    io.storj.libuplink.mobile.WriterOptions internal() {
        return options;
    }

    public static class Builder {
        private RedundancyScheme rs;
        private String contentType;
        private long expires;
        private EncryptionParameters encryptionParameters;

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setEncryptionParameters(EncryptionParameters encryptionParameters) {
            this.encryptionParameters = encryptionParameters;
            return this;
        }

        public Builder setExpires(long expires) {
            this.expires = expires;
            return this;
        }

        public Builder setRedundancyScheme(RedundancyScheme rs) {
            this.rs = rs;
            return this;
        }

        public WriterOptions build() {
            return new WriterOptions(this);
        }
    }
}
