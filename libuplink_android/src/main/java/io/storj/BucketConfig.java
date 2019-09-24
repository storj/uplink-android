package io.storj;

public class BucketConfig {
	
	private io.storj.libuplink.mobile.BucketConfig config;
	
	public BucketConfig(CipherSuite pathCipher, EncryptionParameters encryption, RedundancyScheme redundancy, long segmentsSize) {
		this.config = new io.storj.libuplink.mobile.BucketConfig();
		this.config.setPathCipher(pathCipher.getValue());
		this.config.setEncryptionParameters(encryption.internal());
		this.config.setRedundancyScheme(redundancy.internal());
		this.config.setSegmentsSize(segmentsSize);
	}
	
	io.storj.libuplink.mobile.BucketConfig internal() {
		return this.config;
	}

}
