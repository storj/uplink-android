package io.storj;

import io.storj.internal.Uplink;

class ExceptionUtil {

    static void handleError(Uplink.Error.ByReference error) throws StorjException {
        if (error != null) {
            String message = "" + error.code;
            if (error.message != null) {
                message = error.message.getString(0);
                Uplink.INSTANCE.free_error(error);
            }
            throw new StorjException(message);
        }
    }

    static StorjException toStorjException(Exception e) {
        return new StorjException(e);
    }

}
