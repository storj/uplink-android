package io.storj;

import io.storj.internal.Uplink;

public class UploadOption {

    private enum Key {
        EXPIRES,
    }

    private Key key;

    private Object value;

    UploadOption(Key key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static UploadOption expires(long expires) {
        return new UploadOption(Key.EXPIRES, expires);
    }

    static io.storj.internal.Uplink.UploadOptions.ByReference internal(UploadOption... options) {
        if (options.length==0){
            return null;
        }

        io.storj.internal.Uplink.UploadOptions.ByReference uploadOptions = new Uplink.UploadOptions.ByReference();
        for (UploadOption option : options) {
            if (option.key == Key.EXPIRES) {
                uploadOptions.expires = (long)option.value;
            }
        }

        return uploadOptions;
    }
}
