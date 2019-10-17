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

    public String getBucket() {
        return bucket;
    }

    public String getPath() {
        return path;
    }

    public int getVersion() {
        return version;
    }

    public boolean isPrefix() {
        return prefix;
    }

    public long getSize() {
        return size;
    }

    public String getContentType() {
        return contentType;
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
    }

    public Date getExpires() {
        return expires;
    }

    /**
     * Two ObjectInfos are equal if all their bucket, path, version and prefix flag are equal.
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
     * The hash code value of ObjectInfo is the hash code value of the concatenation of its
     * bucket, path, version and prefix flag.
     */
    @Override
    public int hashCode() {
        return Objects.hash(bucket, path, version, prefix);
    }

    /**
     * Two ObjectInfos are compared to each other by their prefix flag, bucket, path and version,
     * in this order.
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
