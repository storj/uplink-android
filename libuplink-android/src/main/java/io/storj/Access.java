package io.storj;

import io.storj.internal.Uplink2;

public class Access {

    private Uplink2.Access.ByReference access;

    private Access(Uplink2.Access.ByReference access) {
        this.access = access;
    }

    /**
     * Serializes this {@link Access} to base58-encoded {@link String}.
     *
     * @return a {@link String} with serialized Access Grant
     * @throws StorjException in case of error
     */
    public String serialize() throws StorjException {
        Uplink2.StringResult.ByValue result = Uplink2.INSTANCE.access_serialize(this.access);
        if (result.error != null) {
            throw new StorjException(result.error.message.getString(0));
        }
        return result.string.getString(0);
    }

    /**
     * Parses a base58-encoded {@link String} to an {@link Access}.
     *
     * @param serialized a base58-encoded {@link String}
     * @return the parsed {@link Access} Grant
     * @throws StorjException in case of error
     */
    public static Access parse(String serialized) throws StorjException {
        Uplink2.AccessResult.ByValue result = Uplink2.INSTANCE.parse_access(serialized);
        if (result.error != null) {
            throw new StorjException(result.error.message.getString(0));
        }
        return new Access(result.access);
    }

}
