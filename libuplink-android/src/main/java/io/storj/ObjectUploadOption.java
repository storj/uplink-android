package io.storj;

import java.util.Date;

import io.storj.libuplink.mobile.WriterOptions;

/**
 * Options for uploading objects.
 */
public class ObjectUploadOption {

    private enum Key {
        CONTENT_TYPE,
        ENCRYPTION_PARAMETERS,
        EXPIRES,
        REDUNDANCY_SCHEME
    }

    private Key key;

    private Object value;

    ObjectUploadOption(Key key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Option for the MIME content type of the new object.
     *
     * @param contentType a {@link String} with the content type
     * @return a {@link ObjectUploadOption}
     */
    public static ObjectUploadOption contentType(String contentType) {
        return new ObjectUploadOption(Key.CONTENT_TYPE, contentType);
    }

    /**
     * Option for the {@link EncryptionParameters} for encrypting the new object. If not set, the
     * bucket's defaults will be used.
     *
     * @param encryptionParameters {@link EncryptionParameters}
     * @return a {@link ObjectUploadOption}
     */
    public static ObjectUploadOption encryptionParameters(EncryptionParameters encryptionParameters) {
        return new ObjectUploadOption(Key.ENCRYPTION_PARAMETERS, encryptionParameters);
    }

    /**
     * Option for the expiration date of the new object. If not set, the object will never expire
     * and will persist on the network until deleted explicitly.
     *
     * @param expires the expiration {@link Date}
     * @return a {@link ObjectUploadOption}
     */
    public static ObjectUploadOption expires(Date expires) {
        return new ObjectUploadOption(Key.EXPIRES, expires);
    }

    /**
     * Option for the {@link RedundancyScheme} for encoding the new object into pieces.
     *
     * @param redundancyScheme a {@link RedundancyScheme}
     * @return a {@link ObjectUploadOption}
     */
    public static ObjectUploadOption redundancyScheme(RedundancyScheme redundancyScheme) {
        return new ObjectUploadOption(Key.REDUNDANCY_SCHEME, redundancyScheme);
    }

    static io.storj.libuplink.mobile.WriterOptions internal(ObjectUploadOption... options) {
        io.storj.libuplink.mobile.WriterOptions writerOptions = new WriterOptions();
        for (ObjectUploadOption option : options) {
            if (option.key == Key.CONTENT_TYPE) {
                writerOptions.setContentType(option.value.toString());
            } else if (option.key == Key.ENCRYPTION_PARAMETERS) {
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
