package io.storj;

import io.storj.internal.Uplink;

class ExceptionUtil {

    static void handleError(Uplink.Error error) throws StorjException{
        if (error != null) {
            throw new StorjException(error.message.getString(0));
        }
    }

    static StorjException toStorjException(Exception e) {
        return new StorjException(e);
    }

}
