package io.storj;

/**
 * Represents the main entrypoint to the Storj network. An uplink connects to a specific satellite
 * and caches connections and resources, allowing one to create sessions delineated by specific
 * access controls.
 */
public class Uplink {

    // TODO list
    // * free all results on error e.g. error is free but result is not
    //

    private UplinkOption[] options;

    public Uplink(UplinkOption... options) {
        this.options = options;
    }

    /**
     * Returns a {@link Project} handle for the given {@link Access}.
     *
     * @param access a {@link Access}
     * @return a {@link Project} handle
     * @throws StorjException in case of error
     */
    public Project openProject(Access access) throws StorjException {
        io.storj.internal.Uplink.ProjectResult.ByValue result = null;

        io.storj.internal.Uplink.AccessResult.ByValue internalAccess = io.storj.internal.Uplink.INSTANCE.parse_access(access.serializedAccess);
        ExceptionUtil.handleError(internalAccess.error);

        try {
            if (options.length == 0) {
                result = io.storj.internal.Uplink.INSTANCE.open_project(internalAccess.access);
            } else {
                io.storj.internal.Uplink.Config.ByValue config = UplinkOption.internal(options);
                result = io.storj.internal.Uplink.INSTANCE.config_open_project(config, internalAccess.access);
            }
            ExceptionUtil.handleError(result.error);
        } finally {
            io.storj.internal.Uplink.INSTANCE.free_access_result(internalAccess);
        }
        return new Project(result.project);
    }

    public Access requestAccessWithPassphrase(String satelliteAddress, String apiKey, String passphrase) throws StorjException {
        io.storj.internal.Uplink.AccessResult.ByValue result = null;

        if (options.length == 0) {
            result = io.storj.internal.Uplink.INSTANCE.request_access_with_passphrase(satelliteAddress, apiKey, passphrase);
        } else {
            io.storj.internal.Uplink.Config.ByValue config = UplinkOption.internal(options);
            result = io.storj.internal.Uplink.INSTANCE.config_request_access_with_passphrase(config, satelliteAddress, apiKey, passphrase);
        }
        ExceptionUtil.handleError(result.error);

        io.storj.internal.Uplink.StringResult.ByValue stringResult = io.storj.internal.Uplink.INSTANCE.access_serialize(result.access);
        try {
            ExceptionUtil.handleError(stringResult.error);

            return new Access(stringResult.string);
        }finally {
            io.storj.internal.Uplink.INSTANCE.free_access_result(result);
            io.storj.internal.Uplink.INSTANCE.free_string_result(stringResult);
        }
    }

}