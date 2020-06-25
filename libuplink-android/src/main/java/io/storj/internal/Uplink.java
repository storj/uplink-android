package io.storj.internal;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;

public interface Uplink extends Library {

    public static final Uplink INSTANCE = Native.load("gojni", Uplink.class);

    public static final int ERROR_UPLOAD_DONE = (int) 0x22;
    public static final int ERROR_INVALID_HANDLE = (int) 0x04;
    public static final int ERROR_OBJECT_NOT_FOUND = (int) 0x21;
    public static final int ERROR_CANCELED = (int) 0x03;
    public static final int ERROR_BUCKET_ALREADY_EXISTS = (int) 0x11;
    public static final int ERROR_BANDWIDTH_LIMIT_EXCEEDED = (int) 0x06;
    public static final int ERROR_BUCKET_NOT_FOUND = (int) 0x13;
    public static final int ERROR_OBJECT_KEY_INVALID = (int) 0x20;
    public static final int ERROR_TOO_MANY_REQUESTS = (int) 0x05;
    public static final int ERROR_BUCKET_NAME_INVALID = (int) 0x10;
    public static final int ERROR_BUCKET_NOT_EMPTY = (int) 0x12;
    public static final int ERROR_INTERNAL = (int) 0x02;

    @Structure.FieldOrder({"_handle"})
    public static class Handle extends Structure {
        public NativeLong _handle;

        public Handle() {
            super();
        }

        public Handle(NativeLong _handle) {
            super();
            this._handle = _handle;
        }

        public static class ByReference extends Handle implements Structure.ByReference {
        }
        public static class ByValue extends Handle implements Structure.ByValue {
        }
    }


    public static class Access extends Handle {
        public static class ByReference extends Access implements Structure.ByReference {
        }
        public static class ByValue extends Access implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"_handle"})
    public static class Project extends Structure {
        public NativeLong _handle;

        public Project() {
            super();
        }

        public Project(NativeLong _handle) {
            super();
            this._handle = _handle;
        }

        public static class ByReference extends Project implements Structure.ByReference {
        }
        public static class ByValue extends Project implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"user_agent", "dial_timeout_milliseconds", "temp_directory"})
    public static class Config extends Structure {
        public Pointer user_agent;
        public int dial_timeout_milliseconds;
        /**
         * temp_directory specifies where to save data during downloads to use less memory.
         */
        public Pointer temp_directory;

        public Config() {
            super();
        }

        public Config(Pointer user_agent, int dial_timeout_milliseconds, Pointer temp_directory) {
            super();
            this.user_agent = user_agent;
            this.dial_timeout_milliseconds = dial_timeout_milliseconds;
            this.temp_directory = temp_directory;
        }

        public static class ByReference extends Config implements Structure.ByReference {
        }
        public static class ByValue extends Config implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"name", "created"})
    public static class Bucket extends Structure {
        public Pointer name;
        public long created;

        public Bucket() {
            super();
        }

        public Bucket(Pointer name, long created) {
            super();
            this.name = name;
            this.created = created;
        }

        public static class ByReference extends Bucket implements Structure.ByReference {
        }
        public static class ByValue extends Bucket implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"code", "message"})
    public static class Error extends Structure {
        public int code;
        public Pointer message;

        public Error() {
            super();
        }

        public Error(int code, Pointer message) {
            super();
            this.code = code;
            this.message = message;
        }

        public static class ByReference extends Error implements Structure.ByReference {
        }
        public static class ByValue extends Error implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"access", "error"})
    public static class AccessResult extends Structure {
        public Uplink.Access.ByReference access;
        public Uplink.Error.ByReference error;

        public AccessResult() {
            super();
        }

        public AccessResult(Uplink.Access.ByReference access, Uplink.Error.ByReference error) {
            super();
            this.access = access;
            this.error = error;
        }

        public static class ByReference extends AccessResult implements Structure.ByReference {
        }
        public static class ByValue extends AccessResult implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"project", "error"})
    public static class ProjectResult extends Structure {
        public Uplink.Project.ByReference project;
        public Uplink.Error.ByReference error;

        public ProjectResult() {
            super();
        }

        public ProjectResult(Uplink.Project.ByReference project, Uplink.Error.ByReference error) {
            super();
            this.project = project;
            this.error = error;
        }

        public static class ByReference extends ProjectResult implements Structure.ByReference {
        }
        public static class ByValue extends ProjectResult implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"bucket", "error"})
    public static class BucketResult extends Structure {
        public Uplink.Bucket.ByReference bucket;
        public Uplink.Error.ByReference error;

        public BucketResult() {
            super();
        }

        public BucketResult(Uplink.Bucket.ByReference bucket, Uplink.Error.ByReference error) {
            super();
            this.bucket = bucket;
            this.error = error;
        }

        public static class ByReference extends BucketResult implements Structure.ByReference {
        }
        public static class ByValue extends BucketResult implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"string", "error"})
    public static class StringResult extends Structure {
        public Pointer string;
        public Uplink.Error.ByReference error;

        public StringResult() {
            super();
        }

        public StringResult(Pointer string, Uplink.Error.ByReference error) {
            super();
            this.string = string;
            this.error = error;
        }

        public static class ByReference extends StringResult implements Structure.ByReference {
        }
        public static class ByValue extends StringResult implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"allow_download", "allow_upload", "allow_list", "allow_delete", "not_before", "not_after"})
    public static class Permission extends Structure {
        public byte allow_download;
        public byte allow_upload;
        public byte allow_list;
        public byte allow_delete;
        /**
         * disabled when 0.
         */
        public long not_before;
        /**
         * disabled when 0.
         */
        public long not_after;

        public Permission() {
            super();
        }

        public Permission(byte allow_download, byte allow_upload, byte allow_list, byte allow_delete, long not_before, long not_after) {
            super();
            this.allow_download = allow_download;
            this.allow_upload = allow_upload;
            this.allow_list = allow_list;
            this.allow_delete = allow_delete;
            this.not_before = not_before;
            this.not_after = not_after;
        }

        public static class ByReference extends Permission implements Structure.ByReference {
        }
        public static class ByValue extends Permission implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"bucket", "prefix"})
    public static class SharePrefix extends Structure {
        public Pointer bucket;
        /**
         * prefix is the prefix of the shared object keys.
         */
        public Pointer prefix;

        public SharePrefix() {
            super();
        }

        public SharePrefix(Pointer bucket, Pointer prefix) {
            super();
            this.bucket = bucket;
            this.prefix = prefix;
        }

        public static class ByReference extends SharePrefix implements Structure.ByReference {
        }
        public static class ByValue extends SharePrefix implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"cursor"})
    public static class ListBucketsOptions extends Structure {
        public Pointer cursor;

        public ListBucketsOptions() {
            super();
        }

        public ListBucketsOptions(Pointer cursor) {
            super();
            this.cursor = cursor;
        }

        public static class ByReference extends ListBucketsOptions implements Structure.ByReference {
        }

        public static class ByValue extends ListBucketsOptions implements Structure.ByValue {
        }
    }

    public static class BucketIterator extends Handle {
        public static class ByReference extends BucketIterator implements Structure.ByReference {
        }
        public static class ByValue extends BucketIterator implements Structure.ByValue {
        }
    };

    Uplink.AccessResult.ByValue parse_access(String p0);

    Uplink.AccessResult.ByValue request_access_with_passphrase(String p0, String p1, String p2);

    Uplink.StringResult.ByValue access_serialize(Uplink.Access p0);

    Uplink.AccessResult.ByValue access_share(Uplink.Access p0, Uplink.Permission p1, Uplink.SharePrefix p2, long p3);

    void free_string_result(Uplink.StringResult.ByValue p0);

    void free_access_result(Uplink.AccessResult.ByValue p0);

    Uplink.BucketResult.ByValue stat_bucket(Uplink.Project p0, String p1);

    Uplink.BucketResult.ByValue create_bucket(Uplink.Project p0, String p1);

    Uplink.BucketResult.ByValue ensure_bucket(Uplink.Project p0, String p1);

    Uplink.BucketResult.ByValue delete_bucket(Uplink.Project p0, String p1);

    void free_bucket_result(Uplink.BucketResult.ByValue p0);

    void free_bucket(Uplink.Bucket p0);

    Uplink.BucketIterator list_buckets(Uplink.Project p0, Uplink.ListBucketsOptions p1);

    boolean bucket_iterator_next(Uplink.BucketIterator p0);

    Uplink.Error bucket_iterator_err(Uplink.BucketIterator p0);

    Uplink.Bucket bucket_iterator_item(Uplink.BucketIterator p0);

    void free_bucket_iterator(Uplink.BucketIterator p0);

    Uplink.AccessResult.ByValue config_request_access_with_passphrase(Uplink.Config.ByValue p0, String p1, String p2, String p3);

    Uplink.ProjectResult.ByValue config_open_project(Uplink.Config.ByValue p0, Uplink.Access p1);

    Uplink.ProjectResult.ByValue open_project(Uplink.Access p0);

    Uplink.Error close_project(Uplink.Project p0);

    void free_project_result(Uplink.ProjectResult.ByValue p0);

    void free_error(Uplink.Error p0);

}
