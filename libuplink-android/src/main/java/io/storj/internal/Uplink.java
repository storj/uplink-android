package io.storj.internal;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface Uplink extends Library {

    public static final Uplink INSTANCE = Native.load("gojni", Uplink.class);

    public static final int EOF = -1;
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

    public static class Project extends Handle {
        public static class ByReference extends Project implements Structure.ByReference {
        }

        public static class ByValue extends Project implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"user_agent", "dial_timeout_milliseconds", "temp_directory"})
    public static class Config extends Structure {
        public String user_agent;
        public int dial_timeout_milliseconds;
        /**
         * temp_directory specifies where to save data during downloads to use less memory.
         */
        public String temp_directory;

        public Config() {
            super();
        }

        public Config(String user_agent, int dial_timeout_milliseconds, String temp_directory) {
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
        public String name;
        public long created;

        public Bucket() {
            super();
        }

        public Bucket(String name, long created) {
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
        public String message;

        public Error() {
            super();
        }

        public Error(int code, String message) {
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
        public String cursor;

        public ListBucketsOptions() {
            super();
        }

        public ListBucketsOptions(String cursor) {
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
    }

    public static class Download extends Handle {
        public static class ByReference extends Download implements Structure.ByReference {
        }

        public static class ByValue extends Download implements Structure.ByValue {
        }
    }

    public static class ObjectIterator extends Handle {
        public static class ByReference extends ObjectIterator implements Structure.ByReference {
        }

        public static class ByValue extends ObjectIterator implements Structure.ByValue {
        }
    }

    public static class Upload extends Handle {
        public static class ByReference extends Upload implements Structure.ByReference {
        }

        public static class ByValue extends Upload implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"key", "is_prefix", "system", "custom"})
    public static class Object extends Structure {
        public String key;
        public byte is_prefix;
        public Uplink.SystemMetadata.ByValue system;
        public Uplink.CustomMetadata.ByValue custom;

        public Object() {
            super();
        }

        public Object(String key, byte is_prefix, Uplink.SystemMetadata.ByValue system, Uplink.CustomMetadata.ByValue custom) {
            super();
            this.key = key;
            this.is_prefix = is_prefix;
            this.system = system;
            this.custom = custom;
        }

        public static class ByReference extends Object implements Structure.ByReference {
        }

        public static class ByValue extends Object implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"created", "expires", "content_length"})
    public static class SystemMetadata extends Structure {
        public long created;
        public long expires;
        public long content_length;

        public SystemMetadata() {
            super();
        }

        public SystemMetadata(long created, long expires, long content_length) {
            super();
            this.created = created;
            this.expires = expires;
            this.content_length = content_length;
        }

        public static class ByReference extends SystemMetadata implements Structure.ByReference {
        }

        public static class ByValue extends SystemMetadata implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"key", "key_length", "value", "value_length"})
    public static class CustomMetadataEntry extends Structure {
        public String key;
        public NativeLong key_length;
        public String value;
        public NativeLong value_length;

        public CustomMetadataEntry() {
            super();
        }

        public CustomMetadataEntry(String key, NativeLong key_length, String value, NativeLong value_length) {
            super();
            this.key = key;
            this.key_length = key_length;
            this.value = value;
            this.value_length = value_length;
        }

        public static class ByReference extends CustomMetadataEntry implements Structure.ByReference {
        }

        public static class ByValue extends CustomMetadataEntry implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"entries", "count"})
    public static class CustomMetadata extends Structure {
        public Uplink.CustomMetadataEntry.ByReference entries;
        public NativeLong count;

        public CustomMetadata() {
            super();
        }

        public CustomMetadata(Uplink.CustomMetadataEntry.ByReference entries, NativeLong count) {
            super();
            this.entries = entries;
            this.count = count;
        }

        public static class ByReference extends CustomMetadata implements Structure.ByReference {
        }

        public static class ByValue extends CustomMetadata implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"expires"})
    public static class UploadOptions extends Structure {
        /**
         * When expires is 0 or negative, it means no expiration.
         */
        public long expires;

        public UploadOptions() {
            super();
        }

        public UploadOptions(long expires) {
            super();
            this.expires = expires;
        }

        public static class ByReference extends UploadOptions implements Structure.ByReference {
        }

        public static class ByValue extends UploadOptions implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"offset", "length"})
    public static class DownloadOptions extends Structure {
        public long offset;
        /**
         * When length is negative, it will read until the end of the blob.
         */
        public long length;

        public DownloadOptions() {
            super();
        }

        public DownloadOptions(long offset, long length) {
            super();
            this.offset = offset;
            this.length = length;
        }

        public static class ByReference extends DownloadOptions implements Structure.ByReference {
        }

        public static class ByValue extends DownloadOptions implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"prefix", "cursor", "recursive", "system", "custom"})
    public static class ListObjectsOptions extends Structure {
        public Pointer prefix;
        public Pointer cursor;
        public byte recursive;
        public byte system;
        public byte custom;

        public ListObjectsOptions() {
            super();
        }

        public ListObjectsOptions(Pointer prefix, Pointer cursor, byte recursive, byte system, byte custom) {
            super();
            this.prefix = prefix;
            this.cursor = cursor;
            this.recursive = recursive;
            this.system = system;
            this.custom = custom;
        }

        public static class ByReference extends ListObjectsOptions implements Structure.ByReference {
        }

        public static class ByValue extends ListObjectsOptions implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"object", "error"})
    public static class ObjectResult extends Structure {
        public Uplink.Object.ByReference object;
        public Uplink.Error.ByReference error;

        public ObjectResult() {
            super();
        }

        public ObjectResult(Uplink.Object.ByReference object, Uplink.Error.ByReference error) {
            super();
            this.object = object;
            this.error = error;
        }

        public static class ByReference extends ObjectResult implements Structure.ByReference {
        }

        public static class ByValue extends ObjectResult implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"upload", "error"})
    public static class UploadResult extends Structure {
        public Uplink.Upload.ByReference upload;
        public Uplink.Error.ByReference error;

        public UploadResult() {
            super();
        }

        public UploadResult(Uplink.Upload.ByReference upload, Uplink.Error.ByReference error) {
            super();
            this.upload = upload;
            this.error = error;
        }

        public static class ByReference extends UploadResult implements Structure.ByReference {
        }

        public static class ByValue extends UploadResult implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"download", "error"})
    public static class DownloadResult extends Structure {
        public Uplink.Download.ByReference download;
        public Uplink.Error.ByReference error;

        public DownloadResult() {
            super();
        }

        public DownloadResult(Uplink.Download.ByReference download, Uplink.Error.ByReference error) {
            super();
            this.download = download;
            this.error = error;
        }

        public static class ByReference extends DownloadResult implements Structure.ByReference {
        }

        public static class ByValue extends DownloadResult implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"bytes_written", "error"})
    public static class WriteResult extends Structure {
        public NativeLong bytes_written;
        public Uplink.Error.ByReference error;

        public WriteResult() {
            super();
        }

        public WriteResult(NativeLong bytes_written, Uplink.Error.ByReference error) {
            super();
            this.bytes_written = bytes_written;
            this.error = error;
        }

        public static class ByReference extends WriteResult implements Structure.ByReference {
        }

        public static class ByValue extends WriteResult implements Structure.ByValue {
        }
    }

    @Structure.FieldOrder({"bytes_read", "error"})
    public static class ReadResult extends Structure {
        public NativeLong bytes_read;
        public Uplink.Error.ByReference error;

        public ReadResult() {
            super();
        }

        public ReadResult(NativeLong bytes_read, Uplink.Error.ByReference error) {
            super();
            this.bytes_read = bytes_read;
            this.error = error;
        }

        public static class ByReference extends ReadResult implements Structure.ByReference {
        }

        public static class ByValue extends ReadResult implements Structure.ByValue {
        }
    }

    // access
    Uplink.AccessResult.ByValue parse_access(String p0);

    Uplink.AccessResult.ByValue request_access_with_passphrase(String p0, String p1, String p2);

    Uplink.AccessResult.ByValue config_request_access_with_passphrase(Uplink.Config.ByValue p0, String p1, String p2, String p3);

    Uplink.StringResult.ByValue access_serialize(Uplink.Access p0);

    Uplink.AccessResult.ByValue access_share(Uplink.Access p0, Uplink.Permission p1, Uplink.SharePrefix p2, NativeLong p3);

    void free_access_result(Uplink.AccessResult.ByValue p0);

    // bucket
    Uplink.BucketResult.ByValue stat_bucket(Uplink.Project p0, String p1);

    Uplink.BucketResult.ByValue create_bucket(Uplink.Project p0, String p1);

    Uplink.BucketResult.ByValue ensure_bucket(Uplink.Project p0, String p1);

    Uplink.BucketResult.ByValue delete_bucket(Uplink.Project p0, String p1);

    void free_bucket_result(Uplink.BucketResult.ByValue p0);

    void free_bucket(Uplink.Bucket p0);

    Uplink.BucketIterator.ByReference list_buckets(Uplink.Project.ByReference project, Uplink.ListBucketsOptions.ByReference options);

    boolean bucket_iterator_next(Uplink.BucketIterator p0);

    Uplink.Error.ByReference bucket_iterator_err(Uplink.BucketIterator.ByReference iterator);

    Uplink.Bucket bucket_iterator_item(Uplink.BucketIterator p0);

    void free_bucket_iterator(Uplink.BucketIterator p0);

    // project
    Uplink.ProjectResult.ByValue config_open_project(Uplink.Config.ByValue p0, Uplink.Access p1);

    Uplink.ProjectResult.ByValue open_project(Uplink.Access p0);

    Uplink.Error.ByReference close_project(Uplink.Project.ByReference project);

    void free_project_result(Uplink.ProjectResult.ByValue p0);

    // object
    Uplink.ObjectResult.ByValue stat_object(Uplink.Project.ByReference project, String bucket, String key);

    Uplink.ObjectResult.ByValue delete_object(Uplink.Project.ByReference project, String bucket, String key);

    void free_object_result(Uplink.ObjectResult.ByValue result);

    void free_object(Uplink.Object.ByReference object);

    Uplink.ObjectIterator list_objects(Uplink.Project p0, String p1, Uplink.ListObjectsOptions p2);

    boolean object_iterator_next(Uplink.ObjectIterator p0);

    Uplink.Error object_iterator_err(Uplink.ObjectIterator p0);

    Uplink.Object object_iterator_item(Uplink.ObjectIterator p0);

    void free_object_iterator(Uplink.ObjectIterator p0);

    // upload
    Uplink.UploadResult.ByValue upload_object(Uplink.Project.ByReference project, String bucket, String key, Uplink.UploadOptions options);

    Uplink.WriteResult.ByValue upload_write(Uplink.Upload.ByReference upload, Pointer bytes, NativeLong size);

    Uplink.Error.ByReference upload_commit(Uplink.Upload.ByReference upload);

    Uplink.Error.ByReference upload_abort(Uplink.Upload.ByReference upload);

    Uplink.ObjectResult.ByValue upload_info(Uplink.Upload.ByReference upload);

    Uplink.Error.ByReference upload_set_custom_metadata(Uplink.Upload.ByReference upload, Uplink.CustomMetadata.ByValue p1);

    void free_write_result(Uplink.WriteResult.ByValue p0);

    void free_upload_result(Uplink.UploadResult.ByValue p0);

    // download
    Uplink.DownloadResult.ByValue download_object(Uplink.Project.ByReference project, String bucket, String key, Uplink.DownloadOptions options);

    Uplink.ReadResult.ByValue download_read(Uplink.Download.ByReference download, byte[] bytes, NativeLong size);

    Uplink.ObjectResult.ByValue download_info(Uplink.Download.ByReference download);

    void free_read_result(Uplink.ReadResult.ByValue result);

    Uplink.Error.ByReference close_download(Uplink.Download.ByReference download);

    void free_download_result(Uplink.DownloadResult.ByValue result);

    // other
    void free_string_result(Uplink.StringResult.ByValue result);

    void free_error(Uplink.Error.ByReference error);
}
