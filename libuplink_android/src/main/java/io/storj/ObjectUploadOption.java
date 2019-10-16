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
     * Content type option.
     *
     * @param contentType if set, gives a MIME content-type for the Object.
     * @return content type object upload option
     */
    public static ObjectUploadOption contentType(String contentType) {
        return new ObjectUploadOption(Key.CONTENT_TYPE, contentType);
    }

    /**
     * Encryption parameters option.
     *
     * @param encryptionParameters determines the cipher suite to use for
     *                             the Object's data encryption. If not set, the Bucket's
     *                             defaults will be used.
     * @return encryption parameters object upload option
     */
    public static ObjectUploadOption encryptionParameters(EncryptionParameters encryptionParameters) {
        return new ObjectUploadOption(Key.ENCRYPTION_PARAMETERS, encryptionParameters);
    }

    /**
     * Expires option.
     *
     * @param expires is the time at which the new Object can expire (be deleted
     *                automatically from storage nodes).
     * @return expires object upload option
     */
    public static ObjectUploadOption expires(Date expires) {
        return new ObjectUploadOption(Key.EXPIRES, expires);
    }

    /**
     * Redundancy scheme option.
     *
     * @param redundancyScheme determines the Reed-Solomon and/or Forward Error Correction
     *                         encoding parameters to be used for this Object.
     * @return redundancy scheme object upload option
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
