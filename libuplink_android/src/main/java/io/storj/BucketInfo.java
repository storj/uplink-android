package io.storj;

public class BucketInfo {

	private io.storj.libuplink.mobile.BucketInfo info;

	BucketInfo(io.storj.libuplink.mobile.BucketInfo info) {
		this.info = info;
	}

	public String getName() {
		return info.getName();
	}

	public long getCreated() {
		return info.getCreated();
	}

	public CipherSuite getPathCipher() {
		return CipherSuite.fromValue(info.getPathCipher());
	}

	public long getSegmentsSize() {
		return info.getSegmentsSize();
	}

	public RedundancyScheme getRedundancyScheme() {
		return new RedundancyScheme(info.getRedundancyScheme());
	}

	public EncryptionParameters getEncryptionParameters() {
		return new EncryptionParameters(info.getEncryptionParameters());
	}
}
