package io.storj.internal;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.NativeLong;

public class Uplink {

    public static final Libuplink INSTANCE = Native.load("gojni", Libuplink.class);

    public interface Libuplink extends Library {

        @FieldOrder({"_handle"})
        public class Access extends Structure {
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
        }

        @FieldOrder({"code", "message"})
        public class Error extends Structure {
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
        }

        @FieldOrder({"access", "error"})
        public class AccessResult extends Structure {
            public Access.ByReference access;
            public Error.ByReference error;
            public AccessResult() {
                super();
            }

            public AccessResult(Access.ByReference access, Error.ByReference error) {
                super();
                this.access = access;
                this.error = error;
            }
            public static class ByReference extends AccessResult implements Structure.ByReference {
            };
            public static class ByValue extends AccessResult implements Structure.ByValue {
            };
        }

        @FieldOrder({"string", "error"})
        public class StringResult extends Structure {
            public Pointer string;
            public Error.ByReference error;
            public StringResult() {
                super();
            }

            public StringResult(Pointer string, Error.ByReference error) {
                super();
                this.string = string;
                this.error = error;
            }
            public static class ByReference extends StringResult implements Structure.ByReference {
            };
            public static class ByValue extends StringResult implements Structure.ByValue {
            };
        }


        AccessResult.ByValue parse_access(String serializedAccess);

        StringResult.ByValue access_serialize(Access access);
    }

}
