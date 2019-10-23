package io.storj;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Represents object metadata.
 */
public class ObjectInfo implements Serializable, Comparable<ObjectInfo> {

    private String bucket;
    private String path;
    private int version;
    private boolean prefix;
    private long size;
    private String contentType;
    private Date created;
    private Date modified;
    private Date expires;

    ObjectInfo(io.storj.libuplink.mobile.ObjectInfo info) {
        this.bucket = info.getBucket();
        this.path = info.getPath();
        this.version = info.getVersion();
        this.prefix = info.getIsPrefix();
        this.size = info.getSize();
        this.contentType = info.getContentType();
        this.created = new Date(info.getCreated());
        this.modified = new Date(info.getModified());
        this.expires = new Date(info.getExpires());
    }

    /**
     * Returns the bucket that contains this object.
     *
     * @return the bucket name
     */
    public String getBucket() {
        return bucket;
    }

    /**
     * Returns the path to the object relative to the bucket.
     *
     * @return the object path
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the version of the object.
     *
     * @return the object version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Returns if this is a prefix instead of an object.
     *
     * <p>This is useful in non-recursive object listings.</p>
     *
     * @return <code>true</code> if this represents a prefix, or <code>false</code> if this
     *          represents an object
     */
    public boolean isPrefix() {
        return prefix;
    }

    /**
     * Returns the size of the object.
     *
     * <p>This is the size of the decoded and decrypted object, if downloaded from the network.</p>
     *
     * @return the object size
     */
    public long getSize() {
        return size;
    }

    /**
     * Returns the MIME content type of the object.
     *
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Returns the creation time of the object.
     *
     * @return the created {@link Date}
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Returns the time the object was modified.
     *
     * @return the modified {@link Date}
     */
    public Date getModified() {
        return modified;
    }

    /**
     * Returns the time the object expires and will be deleted from the network.
     *
     * @return the expiration {@link Date}
     */
    public Date getExpires() {
        return expires;
    }

    /**
     * Two {@link ObjectInfo} objects are equal if all their bucket, path, version and prefix flag
     * are equal.
     *
     * @return <code>true</code> if this object is the same as the specified object;
     *          <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectInfo that = (ObjectInfo) o;
        return version == that.version &&
                prefix == that.prefix &&
                Objects.equals(bucket, that.bucket) &&
                Objects.equals(path, that.path);
    }

    /**
     * The hash code value of {@link ObjectInfo} is the hash code value of the concatenation of its
     * bucket, path, version and prefix flag.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(bucket, path, version, prefix);
    }

    /**
     * Two {@link ObjectInfo} objects are compared to each other by their prefix flag, bucket,
     * path and version, in this order.
     *
     * @return a negative integer, zero, or a positive integer as this object is less than,
     *          equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(ObjectInfo other) {
        int result = Boolean.compare(isPrefix(), other.isPrefix());
        if (result != 0) {
            return result;
        }

        result = getBucket().compareTo(other.getBucket());
        if (result != 0) {
            return result;
        }

        result = getPath().compareTo(other.getPath());
        if (result != 0) {
            return result;
        }

        return Integer.compare(getVersion(), other.getVersion());
    }
}
