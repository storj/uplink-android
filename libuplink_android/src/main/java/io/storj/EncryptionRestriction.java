package io.storj;

public class EncryptionRestriction {

    private io.storj.libuplink.mobile.EncryptionRestriction restriction;

    public EncryptionRestriction(String bucket) {
        this(bucket, "");
    }

    public EncryptionRestriction(String bucket, String pathPrefix) {
        this.restriction = new io.storj.libuplink.mobile.EncryptionRestriction(bucket, pathPrefix);
    }

    io.storj.libuplink.mobile.EncryptionRestriction internal() {
        return this.restriction;
    }
}
