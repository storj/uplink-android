package io.storj.internal;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;

public interface Uplink2 extends Library {

	public static final Uplink2 INSTANCE = Native.load("gojni", Uplink2.class);

	public static final int ERROR_UPLOAD_DONE = (int)0x22;
	public static final int ERROR_INVALID_HANDLE = (int)0x04;
	public static final int ERROR_OBJECT_NOT_FOUND = (int)0x21;
	public static final int ERROR_CANCELED = (int)0x03;
	public static final int ERROR_BUCKET_ALREADY_EXISTS = (int)0x11;
	public static final int ERROR_BANDWIDTH_LIMIT_EXCEEDED = (int)0x06;
	public static final int ERROR_BUCKET_NOT_FOUND = (int)0x13;
	public static final int ERROR_OBJECT_KEY_INVALID = (int)0x20;
	public static final int ERROR_TOO_MANY_REQUESTS = (int)0x05;
	public static final int ERROR_BUCKET_NAME_INVALID = (int)0x10;
	public static final int ERROR_BUCKET_NOT_EMPTY = (int)0x12;
	public static final int ERROR_INTERNAL = (int)0x02;

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
			
		};
		public static class ByValue extends Handle implements Structure.ByValue {
			
		};
	};
	@Structure.FieldOrder({"_handle"})
	public static class Access extends Structure {
		public NativeLong _handle;
		public Access() {
			super();
		}
		public Access(NativeLong _handle) {
			super();
			this._handle = _handle;
		}
		public static class ByReference extends Access implements Structure.ByReference {
			
		};
		public static class ByValue extends Access implements Structure.ByValue {
			
		};
	};
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
			
		};
		public static class ByValue extends Project implements Structure.ByValue {
			
		};
	};
	@Structure.FieldOrder({"user_agent", "dial_timeout_milliseconds", "temp_directory"})
	public static class Config extends Structure {
		public Pointer user_agent;
		public int dial_timeout_milliseconds;
		/** temp_directory specifies where to save data during downloads to use less memory. */
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
			
		};
		public static class ByValue extends Config implements Structure.ByValue {
			
		};
	};
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
			
		};
		public static class ByValue extends Bucket implements Structure.ByValue {
			
		};
	};
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
			
		};
		public static class ByValue extends Error implements Structure.ByValue {
			
		};
	};
	@Structure.FieldOrder({"access", "error"})
	public static class AccessResult extends Structure {
		public Uplink2.Access.ByReference access;
		public Uplink2.Error.ByReference error;
		public AccessResult() {
			super();
		}
		public AccessResult(Uplink2.Access.ByReference access, Uplink2.Error.ByReference error) {
			super();
			this.access = access;
			this.error = error;
		}
		public static class ByReference extends AccessResult implements Structure.ByReference {
			
		};
		public static class ByValue extends AccessResult implements Structure.ByValue {
			
		};
	};
	@Structure.FieldOrder({"project", "error"})
	public static class ProjectResult extends Structure {
		public Uplink2.Project.ByReference project;
		public Uplink2.Error.ByReference error;
		public ProjectResult() {
			super();
		}
		public ProjectResult(Uplink2.Project.ByReference project, Uplink2.Error.ByReference error) {
			super();
			this.project = project;
			this.error = error;
		}
		public static class ByReference extends ProjectResult implements Structure.ByReference {
			
		};
		public static class ByValue extends ProjectResult implements Structure.ByValue {
			
		};
	};
	@Structure.FieldOrder({"bucket", "error"})
	public static class BucketResult extends Structure {
		public Uplink2.Bucket.ByReference bucket;
		public Uplink2.Error.ByReference error;
		public BucketResult() {
			super();
		}
		public BucketResult(Uplink2.Bucket.ByReference bucket, Uplink2.Error.ByReference error) {
			super();
			this.bucket = bucket;
			this.error = error;
		}
		public static class ByReference extends BucketResult implements Structure.ByReference {
			
		};
		public static class ByValue extends BucketResult implements Structure.ByValue {
			
		};
	};
	@Structure.FieldOrder({"string", "error"})
	public static class StringResult extends Structure {
		public Pointer string;
		public Uplink2.Error.ByReference error;
		public StringResult() {
			super();
		}
		public StringResult(Pointer string, Uplink2.Error.ByReference error) {
			super();
			this.string = string;
			this.error = error;
		}
		public static class ByReference extends StringResult implements Structure.ByReference {
			
		};
		public static class ByValue extends StringResult implements Structure.ByValue {
			
		};
	};

	Uplink2.AccessResult.ByValue parse_access(String p0);
	Uplink2.AccessResult.ByValue request_access_with_passphrase(String p0, String p1, String p2);
	Uplink2.StringResult.ByValue access_serialize(Uplink2.Access p0);
	Uplink2.AccessResult.ByValue access_share(Uplink2.Access p0, Uplink2.Permission p1, Uplink2.SharePrefix p2, Uplink2.GoInt p3);
	void free_string_result(Uplink2.StringResult.ByValue p0);
	void free_access_result(Uplink2.AccessResult.ByValue p0);
	Uplink2.BucketResult.ByValue stat_bucket(Uplink2.Project p0, String p1);
	Uplink2.BucketResult.ByValue create_bucket(Uplink2.Project p0, String p1);
	Uplink2.BucketResult.ByValue ensure_bucket(Uplink2.Project p0, String p1);
	Uplink2.BucketResult.ByValue delete_bucket(Uplink2.Project p0, String p1);
	void free_bucket_result(Uplink2.BucketResult.ByValue p0);
	void free_bucket(Uplink2.Bucket p0);
	Uplink2.BucketIterator list_buckets(Uplink2.Project p0, Uplink2.ListBucketsOptions p1);
	Uplink2._Bool bucket_iterator_next(Uplink2.BucketIterator p0);
	Uplink2.Error bucket_iterator_err(Uplink2.BucketIterator p0);
	Uplink2.Bucket bucket_iterator_item(Uplink2.BucketIterator p0);
	void free_bucket_iterator(Uplink2.BucketIterator p0);
	Uplink2.AccessResult.ByValue config_request_access_with_passphrase(Uplink2.Config.ByValue p0, String p1, String p2, String p3);
	Uplink2.ProjectResult.ByValue config_open_project(Uplink2.Config.ByValue p0, Uplink2.Access p1);
	Uplink2.ProjectResult.ByValue open_project(Uplink2.Access p0);
	Uplink2.Error close_project(Uplink2.Project p0);
	void free_project_result(Uplink2.ProjectResult.ByValue p0);

	/** Pointer to unknown (opaque) type */
	public static class BucketIterator extends PointerType {
		public BucketIterator(Pointer address) {
			super(address);
		}
		public BucketIterator() {
			super();
		}
	};
	/** Pointer to unknown (opaque) type */
	public static class _Bool extends PointerType {
		public _Bool(Pointer address) {
			super(address);
		}
		public _Bool() {
			super();
		}
	};
	/** Pointer to unknown (opaque) type */
	public static class Permission extends PointerType {
		public Permission(Pointer address) {
			super(address);
		}
		public Permission() {
			super();
		}
	};
	/** Pointer to unknown (opaque) type */
	public static class ListBucketsOptions extends PointerType {
		public ListBucketsOptions(Pointer address) {
			super(address);
		}
		public ListBucketsOptions() {
			super();
		}
	};
	/** Pointer to unknown (opaque) type */
	public static class GoInt extends PointerType {
		public GoInt(Pointer address) {
			super(address);
		}
		public GoInt() {
			super();
		}
	};
	/** Pointer to unknown (opaque) type */
	public static class SharePrefix extends PointerType {
		public SharePrefix(Pointer address) {
			super(address);
		}
		public SharePrefix() {
			super();
		}
	};
}
