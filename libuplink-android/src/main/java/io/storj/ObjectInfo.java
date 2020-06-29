package io.storj;

import com.sun.jna.Structure;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.storj.internal.Uplink;

public class ObjectInfo implements Serializable, Comparable<ObjectInfo> {

    private String key;
    private boolean isPrefix;
    private SystemMetadata system;
    private Map<String, String> custom = new HashMap<>();

    ObjectInfo(Uplink.Object object) {
        this.key = object.key;
        this.isPrefix = object.is_prefix == 1;
        this.system = new SystemMetadata(new Date(object.system.created),
                new Date(object.system.expires), object.system.content_length);

        if (object.custom.count.longValue()> 0){
            Structure[] entries = object.custom.entries.toArray(object.custom.count.intValue());
            for (int i = 0; i < entries.length; i++) {
                Uplink.CustomMetadataEntry entry = (Uplink.CustomMetadataEntry) entries[i];
                custom.put(entry.key, entry.value);
            }
        }
    }

    public String getKey() {
        return key;
    }

    public boolean isPrefix() {
        return isPrefix;
    }

    public SystemMetadata getSystem() {
        return system;
    }

    public Map<String, String> getCustom() {
        return custom;
    }

    /**
     * Two {@link ObjectInfo} objects are equal if their names are equal.
     *
     * @return <code>true</code> if this object is the same as the specified object;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectInfo that = (ObjectInfo) o;
        return Objects.equals(key, that.key)
                && isPrefix == that.isPrefix;
//                && Objects.equals(system, that.system)
//                && Objects.equals(custom, that.custom);
    }

    /**
     * The hash code value of {@link ObjectInfo} is the hash code value of its name.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    /**
     * Two {@link ObjectInfo} objects are compared to each other by their prefix flag, bucket,
     * path and version, in this order.
     *
     * @return a negative integer, zero, or a positive integer as this object is less than,
     * equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(ObjectInfo other) {
        int result = Boolean.compare(isPrefix(), other.isPrefix());
        if (result != 0) {
            return result;
        }

        result = getKey().compareTo(other.getKey());
        if (result != 0) {
            return result;
        }


        // TODO compare system metadata and custom
        return 0;
    }
}
