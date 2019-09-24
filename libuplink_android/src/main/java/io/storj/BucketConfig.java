package io.storj;

public class BucketConfig {
	
	private io.storj.libuplink.mobile.BucketConfig config;

	public CipherSuite getPathCipher() {
		return CipherSuite.fromValue(this.config.getPathCipher());
	}

	public EncryptionParameters getEncryptionParameters() {
		return new EncryptionParameters(this.config.getEncryptionParameters());
	}
	
	io.storj.libuplink.mobile.BucketConfig internal() {
		return this.config;
	}

	private BucketConfig(Builder builder) {
		this.config = new io.storj.libuplink.mobile.BucketConfig();
		if (builder.pathCipher != null) {
			this.config.setPathCipher(builder.pathCipher.getValue());
		}
		if (builder.encryption != null) {
			this.config.setEncryptionParameters(builder.encryption.internal());
		}
		if (builder.redundancy != null) {
			this.config.setRedundancyScheme(builder.redundancy.internal());
		}
		this.config.setSegmentsSize(builder.segmentsSize);
	}

	public static class Builder {

		private CipherSuite pathCipher;
		private EncryptionParameters encryption;
		private RedundancyScheme redundancy;
		private long segmentsSize;

		public Builder setPathCipher(CipherSuite cipher) {
			this.pathCipher = pathCipher;
			return this;
		}

		public Builder setEncryptionParameters(EncryptionParameters encryption) {
			this.encryption = encryption;
			return this;
		}

		public Builder setRedundancyScheme(RedundancyScheme redundancy) {
			this.redundancy = redundancy;
			return this;
		}

		public Builder setSegmentsSize(long size) {
			this.segmentsSize = size;
			return this;
		}

		public BucketConfig build() {
			return new BucketConfig(this);
		}
	}

}
