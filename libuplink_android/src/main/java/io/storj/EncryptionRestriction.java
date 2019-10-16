package io.storj;

/**
 * EncryptionRestriction represents a scenario where some set of objects
 * may need to be encrypted/decrypted.
 */
public class EncryptionRestriction {

    private io.storj.libuplink.mobile.EncryptionRestriction restriction;

    public EncryptionRestriction(String bucket) {
        this(bucket, "");
    }

    /**
     * Creates new encryption restriction.
     *
     * @param bucket bucket name
     * @param pathPrefix path prefix
     */
    public EncryptionRestriction(String bucket, String pathPrefix) {
        this.restriction = new io.storj.libuplink.mobile.EncryptionRestriction(bucket, pathPrefix);
    }

    io.storj.libuplink.mobile.EncryptionRestriction internal() {
        return this.restriction;
    }
}
