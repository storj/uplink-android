package io.storj;

/**
 * ApiKey represents an access credential to certain resources.
 */
public class ApiKey {

    private io.storj.libuplink.mobile.APIKey apiKey;

    ApiKey(io.storj.libuplink.mobile.APIKey apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Generates a new API key restricted by the provided Caveat.
     *
     * @param caveat a Caveat for restricting the access of the API key
     * @return new restricted API key
     * @throws StorjException
     */
    public ApiKey restrict(Caveat caveat) throws StorjException {
        try {
            return new ApiKey(this.apiKey.restrict(caveat.internal()));
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Serializes the API key to a string.
     *
     * @return serialized API key
     */
    public String serialize() {
        return apiKey.serialize();
    }

    /**
     * Parses the API key from the serialized string.
     *
     * @param serialized serialized API key
     * @return the parsed API key
     * @throws StorjException
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
