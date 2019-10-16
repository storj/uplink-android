package io.storj;

import java.io.Serializable;
import java.util.Date;

public class ObjectInfo implements Serializable, Comparable<ObjectInfo> {

    private io.storj.libuplink.mobile.ObjectInfo info;

    ObjectInfo(io.storj.libuplink.mobile.ObjectInfo info) {
        this.info = info;
    }

    public int getVersion() {
        return info.getVersion();
    }

    public String getBucket() {
        return info.getBucket();
    }

    public String getPath() {
        return info.getPath();
    }

    public boolean isPrefix() {
        return info.getIsPrefix();
    }

    public long getSize() {
        return info.getSize();
    }

    public String getContentType() {
        return info.getContentType();
    }

    public Date getCreated() {
        return new Date(info.getCreated());
    }

    public Date getModified() {
        return new Date(info.getModified());
    }

    public Date getExpires() {
        return new Date(info.getExpires());
    }

    /**
     * Two ObjectInfos are equal if all their bucket, path, version and prefix flag are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ObjectInfo)) {
            return false;
        }
        ObjectInfo that = (ObjectInfo) obj;
        return getBucket().equals(that.getBucket()) &&
                getPath().equals(that.getPath()) &&
                getVersion() == that.getVersion() &&
                isPrefix() == that.isPrefix();
    }

    /**
     * The hash code value of ObjectInfo is the hash code value of the concatenation of its
     * bucket, path, version and prefix flag.
     */
    @Override
    public int hashCode() {
        String concat = getBucket() + "/" + getPath() + "/" + getVersion() + "/" + isPrefix();
        return concat.hashCode();
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
