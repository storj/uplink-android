package io.storj;

public enum ListDirection {
	BEFORE((byte) -2),
	BACKWARD((byte) -1),
	FORWARD((byte) 1),
	AFTER((byte) 2);

	private byte value;

	private ListDirection(byte value) {
		this.value = value;
	}
	
	byte getValue() {
		return value;
	}

	public static ListDirection fromValue(byte value) {
		for (ListDirection v : ListDirection.values()) {
			if (v.value == value) {
				return v;
			}
		}
		return null;
	}
}
