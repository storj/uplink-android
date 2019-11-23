package io.storj.internal;

import com.sun.jna.Memory;

public class StringPointer extends Memory {

	// TODO might be unsafe to do this like that
	public StringPointer(String value) {
		super(value.length() + 1);
		byte[] data = value.getBytes();
        this.write(0, data, 0, data.length);
        this.setByte(data.length, (byte)0);
	}

}
