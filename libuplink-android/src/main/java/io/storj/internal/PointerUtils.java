package io.storj.internal;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public class PointerUtils {

    public static Pointer toPointer(String value) {
        Memory pointer = new Memory(value.length() + 1);
        pointer.setString(0, value);
        return pointer;
    }
}
