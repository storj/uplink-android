package io.storj;

import java.io.Serializable;
import java.util.Date;

public class BucketInfo implements Serializable, Comparable<BucketInfo> {

    private io.storj.libuplink.mobile.BucketInfo info;

    BucketInfo(io.storj.libuplink.mobile.BucketInfo info) {
        this.info = info;
    }

    public String getName() {
        return info.getName();
    }

    public Date getCreated() {
        return new Date(info.getCreated());
    }

    public CipherSuite getPathCipher() {
        return CipherSuite.fromValue(info.getPathCipher());
    }

    public long getSegmentsSize() {
        return info.getSegmentsSize();
    }

    public RedundancyScheme getRedundancyScheme() {
        return new RedundancyScheme(info.getRedundancyScheme());
    }

    public EncryptionParameters getEncryptionParameters() {
        return new EncryptionParameters(info.getEncryptionParameters());
    }

    /**
     * Two BucketInfos are equal if their names are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BucketInfo)) {
            return false;
        }
        BucketInfo that = (BucketInfo) obj;
        return getName().equals(that.getName());
    }

    /**
     * The hash code value of BucketInfo is the hash code value of its name.
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * Two BucketInfos are compared to each other by their names.
     */
    @Override
    public int compareTo(BucketInfo other) {
        return getName().compareTo(other.getName());
    }
}
