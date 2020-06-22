package io.storj;

import io.storj.internal.Uplink;

public class Project implements AutoCloseable {

    protected Uplink.Project.ByReference project;

    protected Project(Uplink.Project.ByReference project) {
        this.project = project;
    }

    @Override
    public void close() throws Exception {
        Uplink.Error result = Uplink.INSTANCE.close_project(this.project);
        ExceptionUtil.handleError(result);
    }
}
