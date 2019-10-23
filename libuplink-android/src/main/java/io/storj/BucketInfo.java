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

    /**
     * Returns the bucket name.
     *
     * @return the bucket name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the time when the bucket was created.
     *
     * @return the creation {@link Date}
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Returns the {@link CipherSuite} used for encrypting all object paths in this bucket.
     *
     * @return the default {@link CipherSuite}
     */
    public CipherSuite getPathCipher() {
        return pathCipher;
    }

    /**
     * Returns the segments size used for uploading new objects to this bucket.
     *
     * @return the size of the segments
     */
    public long getSegmentsSize() {
        return segmentsSize;
    }

    /**
     * Returns the default {@link RedundancyScheme} used for uploading new objects to this bucket.
     *
     * <p>Individual uploads can override the {@link RedundancyScheme} by using the
     * {@link ObjectUploadOption#redundancyScheme(RedundancyScheme)} option.</p>
     *
     * @return the default {@link RedundancyScheme}
     */
    public RedundancyScheme getRedundancyScheme() {
        return redundancy;
    }

    /**
     * Returns the default {@link EncryptionParameters} used for uploading new objects to this
     * bucket.
     *
     * <p>Individual uploads can override the {@link EncryptionParameters} by using the
     * {@link ObjectUploadOption#encryptionParameters(EncryptionParameters)} option.</p>
     *
     * @return the default {@link EncryptionParameters}
     */
    public EncryptionParameters getEncryptionParameters() {
        return encryption;
    }

    /**
     * Two {@link BucketInfo} objects are equal if their names are equal.
     *
     * @return <code>true</code> if this object is the same as the specified object;
     *          <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BucketInfo that = (BucketInfo) o;
        return Objects.equals(name, that.name);
    }

    /**
     * The hash code value of {@link BucketInfo} is the hash code value of its name.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Two {@link BucketInfo} objects are compared to each other by their names.
     *
     * @return a negative integer, zero, or a positive integer as this object is less than,
     *          equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(BucketInfo other) {
        return name.compareTo(other.name);
    }
}
