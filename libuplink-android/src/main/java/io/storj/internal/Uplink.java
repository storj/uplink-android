package io.storj.internal;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.PointerByReference;

public class Uplink {

    public static final Awesome INSTANCE = Native.load("uplink", Awesome.class);

    public interface Awesome extends Library {

        @FieldOrder({"skip_peer_ca_whitelist", "peer_ca_whitelist_path"})
        public class TLS extends Structure {
            public boolean skip_peer_ca_whitelist;
            public Pointer peer_ca_whitelist_path;

            public TLS() {
                super();
            }
        }

        @FieldOrder({"tls", "peer_id_version", "max_inline_size", "max_memory", "dial_timeout"})
        public class Volatile extends Structure {
            public TLS tls;
            public Pointer peer_id_version;
            public int max_inline_size;
            public int max_memory;
            public int dial_timeout;

            public Volatile() {
                super();
                this.tls = new TLS();
            }
        }

        @FieldOrder({"Volatile"})
        public class UplinkConfig extends Structure {

            public static class ByReference extends UplinkConfig implements Structure.ByReference {
            }

            public static class ByValue extends UplinkConfig implements Structure.ByValue {
            }

            public Volatile Volatile;

            public UplinkConfig() {
                super();
                this.Volatile = new Volatile();
            }
        }

        @FieldOrder({"content_type", "expires"})
        public class UploadOptions extends Structure {
            public String content_type;
            public long expires;

            public UploadOptions() {
                super();
            }
        }

        @FieldOrder({"cipher_suite", "block_size"})
        public class EncryptionParameters extends Structure {
            public int cipher_suite;
            public int block_size;
        }

        @FieldOrder({"algorithm", "share_size", "required_shares", "repair_shares", "optimal_shares", "total_shares"})
        public class RedundancyScheme extends Structure {
            public int algorithm;
            public int share_size;
            public short required_shares;
            public short repair_shares;
            public short optimal_shares;
            public short total_shares;
        }

        @FieldOrder({"name", "created", "path_cipher", "segment_size", "encryption_parameters", "redundancy_scheme"})
        public class BucketInfo extends Structure {
            public Pointer name;
            public long created;
            public int path_cipher;
            public long segment_size;
            public EncryptionParameters encryption_parameters;
            public RedundancyScheme redundancy_scheme;

            public static class ByReference extends BucketInfo implements Structure.ByReference {

            };
        }

        @FieldOrder({"more", "items", "length"})
        public class BucketList extends Structure {
            public static class ByValue extends BucketList implements Structure.ByValue {
            }

            public byte more;
            public BucketInfo.ByReference items;
            public int length;
        }

        @FieldOrder({"cursor", "direction", "limit"})
        public class BucketListOptions extends Structure {
            public static class ByValue extends BucketListOptions implements Structure.ByValue {
            }

            public Pointer cursor;
            public int direction;
            public long limit;
        }

        Reference.ByValue new_uplink(UplinkConfig cfg, Pointer temp, PointerByReference bufp);

        void close_uplink(Reference.ByValue uplinkRef, PointerByReference err);

        Reference.ByValue parse_api_key(StringPointer p, PointerByReference err);

        Reference.ByValue open_project(Reference.ByValue uplinkRef, Pointer satelliteAddress, Reference.ByValue apiKeyRef, PointerByReference err);

        BucketList.ByValue list_buckets(Reference.ByValue projectRef, BucketListOptions.ByReference listOptions, PointerByReference err);

//        Reference.ByValue open_bucket(Reference.ByValue projectRef, Pointer p1, Pointer, err);

        long new_encryption_access(PointerByReference err);
    }

}
