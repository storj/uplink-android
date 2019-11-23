package io.storj.internal;

import com.sun.jna.NativeLong;
import com.sun.jna.Structure;

@Structure.FieldOrder({ "_handle"})
public class Reference extends Structure {
	public NativeLong _handle;

	public Reference() {
		super();
	}

	public static class ByReference extends Reference implements Structure.ByReference {
		public ByReference() {
			super();
		}
	};

	public static class ByValue extends Reference implements Structure.ByValue {
		public ByValue() {
			super();
		}
	};
}
