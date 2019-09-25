package io.storj;

import java.util.Date;

class ObjectInfo {

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ObjectInfo)) {
            return false;
        }
        ObjectInfo that = (ObjectInfo) obj;
        return info.equals(that.info);
    }

    @Override
    public int hashCode() {
        return info.hashCode();
    }
}
