package io.storj;

/**
 * Represents a restriction to the access of objects based on a path prefix.
 *
 * @see Scope#restrict(EncryptionRestriction...)
 * @see Scope#restrict(Caveat, EncryptionRestriction...)
 */
public class EncryptionRestriction {

    private io.storj.libuplink.mobile.EncryptionRestriction restriction;

    /**
     * Creates new {@link EncryptionRestriction} that restricts the access to all objects within
     * the specified bucket.
     *
     * @param bucket a bucket name
     */
    public EncryptionRestriction(String bucket) {
        this(bucket, "");
    }

    /**
     * Creates new {@link EncryptionRestriction} that restricts the access to all objects with the
     * specified path prefix within the specified bucket.
     *
     * @param bucket a bucket name
     * @param pathPrefix a path prefix
     */
    public EncryptionRestriction(String bucket, String pathPrefix) {
        this.restriction = new io.storj.libuplink.mobile.EncryptionRestriction(bucket, pathPrefix);
    }

    io.storj.libuplink.mobile.EncryptionRestriction internal() {
        return this.restriction;
    }
}
