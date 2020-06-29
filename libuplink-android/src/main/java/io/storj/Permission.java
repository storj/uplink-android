package io.storj;

import java.util.Date;

import io.storj.internal.Uplink;

public class Permission {

    private Uplink.Permission.ByValue cPermission;

    Permission(Builder builder) {
        this.cPermission = new Uplink.Permission.ByValue();
        this.cPermission.allow_upload = (byte) (builder.allowUpload ? 1 : 0);
        this.cPermission.allow_download = (byte) (builder.allowDownload ? 1 : 0);
        this.cPermission.allow_list = (byte) (builder.allowList ? 1 : 0);
        this.cPermission.allow_delete = (byte) (builder.allowDelete ? 1 : 0);
        if (builder.notAfter != null) {
            this.cPermission.not_after = builder.notAfter.getTime() / 1000;
        }
        if (builder.notBefore != null) {
            this.cPermission.not_before = builder.notBefore.getTime() / 1000;
        }
    }

    Uplink.Permission.ByValue internal() {
        return this.cPermission;
    }

    /**
     * Builder for {@link Permission} objects.
     */
    public static class Builder {

        private boolean allowDownload;
        private boolean allowUpload;
        private boolean allowList;
        private boolean allowDelete;
        private Date notAfter;
        private Date notBefore;

        /**
         * Determines that downloads are allowed.
         *
         * @return a reference to this object
         */
        public Builder allowDownload() {
            this.allowDownload = true;
            return this;
        }

        /**
         * Determines that uploads are allowed.
         *
         * @return a reference to this object
         */
        public Builder allowUpload() {
            this.allowUpload = true;
            return this;
        }

        /**
         * Determines that listing buckets and objects is allowed.
         *
         * @return a reference to this object
         */
        public Builder allowList() {
            this.allowList = true;
            return this;
        }

        /**
         * Determines if deletes are allowed.
         *
         * @return a reference to this object
         */
        public Builder allowDelete() {
            this.allowDelete = true;
            return this;
        }

        /**
         * Determines the latest date of the permission validity.
         *
         * @param notAfter a {@link Date}
         * @return a reference to this object
         */
        public Builder notAfter(Date notAfter) {
            this.notAfter = notAfter;
            return this;
        }

        /**
         * Determines the earlies date of the permission validity.
         *
         * @param notBefore a {@link Date}
         * @return a reference to this object
         */
        public Builder notBefore(Date notBefore) {
            this.notBefore = notBefore;
            return this;
        }


        /**
         * Creates the new {@link Permission} object from this builder.
         *
         * @return a {@link Permission}
         */
        public Permission build() {
            return new Permission(this);
        }
    }
}
