package io.storj;

import io.storj.internal.Uplink;

public class DownloadOption {

    private enum Key {
        OFFSET,
        LENGTH,
    }

    private Key key;

    private Object value;

    DownloadOption(Key key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static DownloadOption offset(long offset) {
        return new DownloadOption(Key.OFFSET, offset);
    }


    static io.storj.internal.Uplink.DownloadOptions internal(DownloadOption... options) {
        if (options.length == 0){
            return null;
        }

        io.storj.internal.Uplink.DownloadOptions downloadOptions = new Uplink.DownloadOptions();
        for (DownloadOption option : options) {
            if (option.key == Key.OFFSET) {
                downloadOptions.offset = (long)option.value;
            }else if (option.key == Key.LENGTH) {
                downloadOptions.length = (long)option.value;
            }
        }

        return downloadOptions;
    }
}
