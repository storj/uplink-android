package io.storj;

/**
 * Represents an access credential to certain resources.
 */
public class ApiKey {

    private io.storj.libuplink.mobile.APIKey apiKey;

    ApiKey(io.storj.libuplink.mobile.APIKey apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Generates a new {@link ApiKey} restricted by the provided {@link Caveat}.
     *
     * @param caveat a {@link Caveat} for restricting the access
     * @return the restricted {@link ApiKey}
     * @throws StorjException in case of error
     */
    public ApiKey restrict(Caveat caveat) throws StorjException {
        try {
            return new ApiKey(this.apiKey.restrict(caveat.internal()));
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Serializes the {@link ApiKey} to base58-encoded {@link String}.
     *
     * @return a {@link String} with the serialized API key
     */
    public String serialize() {
        return apiKey.serialize();
    }

    /**
     * Parses a base58-encoded {@link String} to an {@link ApiKey}.
     *
     * @param serialized a base58-encoded {@link String}
     * @return the parsed {@link ApiKey}
     * @throws StorjException if the serialized string is not a valid API key
     */
    public static ApiKey parse(String serialized) throws StorjException {
        try {
            return new ApiKey(io.storj.libuplink.mobile.Mobile.parseAPIKey(serialized));
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    io.storj.libuplink.mobile.APIKey internal() {
        return apiKey;
    }

}
