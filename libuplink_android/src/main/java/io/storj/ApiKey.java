package io.storj;

import java.io.Serializable;

/**
 * ApiKey represents an access credential to certain resources.
 */
public class ApiKey implements Serializable {

    private static final long serialVersionUID = 7096529321479799953L;

    private io.storj.libuplink.mobile.APIKey apiKey;

    ApiKey(io.storj.libuplink.mobile.APIKey apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Creates new ApiKey.
     *
     * @param serializedApiKey API key in string format
     * @throws StorjException
     */
    public ApiKey(String serializedApiKey) throws StorjException {
        try {
            this.apiKey = io.storj.libuplink.mobile.Mobile.parseAPIKey(serializedApiKey);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Generates a new ApiKey with the provided Caveat attached.
     *
     * @param caveat
     * @return new restricted ApiKey
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
     * Serializes the ApiKey to a string.
     *
     * @return serialized to string ApiKey
     */
    public String serialize() {
        return apiKey.serialize();
    }

}
