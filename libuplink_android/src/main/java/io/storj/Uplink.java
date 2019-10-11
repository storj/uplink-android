package io.storj;

public class Uplink implements AutoCloseable {
	
	private io.storj.libuplink.mobile.Uplink uplink;
	
	public Uplink(Config config) {
		this.uplink = new io.storj.libuplink.mobile.Uplink(config.internal(), config.getTempDir());
	}
	
	public Project openProject(String satelliteAddress, ApiKey apiKey) throws StorjException {
		try {
			io.storj.libuplink.mobile.Project project = uplink.openProject(satelliteAddress, apiKey.serialize());
			return new Project(project);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}
	
	public void close() throws StorjException {
		try {
			uplink.close();
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}

}
