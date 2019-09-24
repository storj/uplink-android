package io.storj;

import java.io.Closeable;
import java.io.IOException;

public class Uplink implements Closeable {
	
	private io.storj.libuplink.mobile.Uplink uplink;
	
	public Uplink(Config config) throws StorjException {
		this.uplink = new io.storj.libuplink.mobile.Uplink(config.internal(), config.getTempDir());
	}
	
	public Project openProject(String satelliteAddress, ApiKey apiKey) throws StorjException {
		try {
			io.storj.libuplink.mobile.Project proj = uplink.openProject(satelliteAddress, apiKey.serialize());
			return new Project(proj);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}
	
	public void close() throws IOException {
		try {
			uplink.close();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
