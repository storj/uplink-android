package io.storj;

import java.util.Date;

import io.storj.libuplink.mobile.WriterOptions;

public class UploadOption {

    private enum Key {
        CONTENT_TYPE,
        ENCRYPTION_PARAMTERS,
        EXPIRES,
        REDUNDANCY_SCHEME
    }

    private Key key;

    private Object value;

    UploadOption(Key key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static UploadOption contentType(String contentType) {
        return new UploadOption(Key.CONTENT_TYPE, contentType);
    }

    public static UploadOption encryptionParameters(EncryptionParameters encryptionParameters) {
        return new UploadOption(Key.ENCRYPTION_PARAMTERS, encryptionParameters);
    }

    public static UploadOption expires(Date expires) {
        return new UploadOption(Key.EXPIRES, expires);
    }

    public static UploadOption redundancyScheme(RedundancyScheme redundancyScheme) {
        return new UploadOption(Key.REDUNDANCY_SCHEME, redundancyScheme);
    }

    static io.storj.libuplink.mobile.WriterOptions internal(UploadOption... options) {
        io.storj.libuplink.mobile.WriterOptions writerOptions = new WriterOptions();
        for (UploadOption option : options) {
            if (option.key == Key.CONTENT_TYPE) {
                writerOptions.setContentType(option.value.toString());
            } else if (option.key == Key.ENCRYPTION_PARAMTERS) {
                EncryptionParameters parameters = (EncryptionParameters) option.value;
                writerOptions.setEncryptionParameters(parameters.internal());
            } else if (option.key == Key.EXPIRES) {
                Date expires = (Date) option.value;
                writerOptions.setExpires(expires.getTime());
            } else if (option.key == Key.REDUNDANCY_SCHEME) {
                RedundancyScheme scheme = (RedundancyScheme) option.value;
                writerOptions.setRedundancyScheme(scheme.internal());
            }
        }
        return writerOptions;
    }

}
