package io.storj;

/**
 * Uplink represents the main entrypoint to Storj V3. An Uplink connects to
 * a specific Satellite and caches connections and resources, allowing one to
 * create sessions delineated by specific access controls.
 */
public class Uplink implements AutoCloseable {

    private io.storj.libuplink.mobile.Uplink uplink;

    public Uplink(UplinkOption... options) {
        UplinkOption.UplinkOptions uplinkOptions = UplinkOption.internal(options);
        this.uplink = new io.storj.libuplink.mobile.Uplink(uplinkOptions.config, uplinkOptions.tempDir);
    }

    public Project openProject(String satelliteAddress, ApiKey apiKey) throws StorjException {
        try {
            io.storj.libuplink.mobile.Project project = uplink.openProject(satelliteAddress, apiKey.serialize());
            return new Project(project);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Closes the Uplink. Opened projects, buckets or objects must not be used after calling Close.
     *
     * @throws StorjException
     */
    public void close() throws StorjException {
        try {
            uplink.close();
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

}
