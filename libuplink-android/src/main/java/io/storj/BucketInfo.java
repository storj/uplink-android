package io.storj;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Represents bucket metadata.
 */
public class BucketInfo implements Serializable, Comparable<BucketInfo> {

    private String name;
    private Date created;
    private CipherSuite pathCipher;
    private long segmentsSize;
    private RedundancyScheme redundancy;
    private EncryptionParameters encryption;

    BucketInfo(io.storj.libuplink.mobile.BucketInfo info) {
        this.name = info.getName();
        this.created = new Date(info.getCreated());
        this.pathCipher = CipherSuite.fromValue(info.getPathCipher());
        this.segmentsSize = info.getSegmentsSize();
        this.redundancy = new RedundancyScheme(info.getRedundancyScheme());
        this.encryption = new EncryptionParameters(info.getEncryptionParameters());
    }

    public String getName() {
        return name;
    }

    public Date getCreated() {
        return created;
    }

    public CipherSuite getPathCipher() {
        return pathCipher;
    }

    public long getSegmentsSize() {
        return segmentsSize;
    }

    public RedundancyScheme getRedundancyScheme() {
        return redundancy;
    }

    public EncryptionParameters getEncryptionParameters() {
        return encryption;
    }

    /**
     * Two BucketInfos are equal if their names are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BucketInfo that = (BucketInfo) o;
        return Objects.equals(name, that.name);
    }

    /**
     * The hash code value of BucketInfo is the hash code value of its name.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Two BucketInfos are compared to each other by their names.
     */
    @Override
    public int compareTo(BucketInfo other) {
        return name.compareTo(other.name);
    }
}
