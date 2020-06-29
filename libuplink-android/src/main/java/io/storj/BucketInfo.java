package io.storj;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import io.storj.internal.Uplink;

/**
 * Represents bucket metadata.
 */
public class BucketInfo implements Serializable, Comparable<BucketInfo> {

    private String name;
    private Date created;

    BucketInfo(Uplink.Bucket bucket) {
        this.name = bucket.name;
        this.created = new Date(bucket.created);
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
     * Two {@link BucketInfo} objects are equal if their names are equal.
     *
     * @return <code>true</code> if this object is the same as the specified object;
     * <code>false</code> otherwise.
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
     * equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(BucketInfo other) {
        return name.compareTo(other.name);
    }
}