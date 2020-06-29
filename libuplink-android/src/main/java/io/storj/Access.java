package io.storj;

import io.storj.internal.Uplink;

public class Access {

    protected String serializedAccess;

    protected Access(String serializedAccess) {
        this.serializedAccess = serializedAccess;
    }

    /**
     * Serializes this {@link Access} to base58-encoded {@link String}.
     *
     * @return a {@link String} with serialized Access Grant
     * @throws StorjException in case of error
     */
    public String serialize() throws StorjException {
//        Uplink.StringResult.ByValue result = Uplink.INSTANCE.access_serialize(this.access);
//        ExceptionUtil.handleError(result.error);
//        String serializedAccess = result.string.getString(0);
//        Uplink.INSTANCE.free_string_result(result);
        return this.serializedAccess;
    }

    /**
     * Parses a base58-encoded {@link String} to an {@link Access}.
     *
     * @param serialized a base58-encoded {@link String}
     * @return the parsed {@link Access} Grant
     * @throws StorjException in case of error
     */
    public static Access parse(String serialized) throws StorjException {
        Uplink.AccessResult.ByValue result = Uplink.INSTANCE.parse_access(serialized);
        ExceptionUtil.handleError(result.error);
        Uplink.INSTANCE.free_access_result(result);
        return new Access(serialized);
    }

    public Access share(Permission permission, SharePrefix... prefixes) throws StorjException {
        Uplink.Permission.ByValue cPermission = permission.internal();
        Uplink.AccessResult.ByValue accessResult = null;
        Uplink.AccessResult.ByValue shareResult = null;
        Uplink.StringResult.ByValue stringResult = null;

        Uplink.SharePrefix.ByReference firstPrefix = new Uplink.SharePrefix.ByReference();
        if (prefixes.length > 0) {
            Uplink.SharePrefix.ByReference[] cPrefixes = (Uplink.SharePrefix.ByReference[]) firstPrefix.toArray(prefixes.length);
            int i = 0;
            for (SharePrefix prefix : prefixes) {
                cPrefixes[0].bucket = prefix.getBucket();
                cPrefixes[0].prefix = prefix.getPrefix();
                i++;
            }
            firstPrefix = cPrefixes[0];
        }


        try {
            accessResult = Uplink.INSTANCE.parse_access(this.serializedAccess);
            ExceptionUtil.handleError(accessResult.error);

            shareResult = Uplink.INSTANCE.access_share(accessResult.access, cPermission, firstPrefix, prefixes.length);
            ExceptionUtil.handleError(shareResult.error);

            stringResult = Uplink.INSTANCE.access_serialize(shareResult.access);
            ExceptionUtil.handleError(stringResult.error);

            return new Access(stringResult.string);
        } finally {
            if (accessResult != null) {
                Uplink.INSTANCE.free_access_result(accessResult);
            }
            if (shareResult != null) {
                Uplink.INSTANCE.free_access_result(shareResult);
            }
            if (stringResult != null) {
                Uplink.INSTANCE.free_string_result(stringResult);
            }
        }
    }
}
