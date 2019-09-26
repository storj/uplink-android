package io.storj;

public class BucketConfig {
	
	private io.storj.libuplink.mobile.BucketConfig config;

	public CipherSuite getPathCipher() {
		return CipherSuite.fromValue(config.getPathCipher());
	}

	public EncryptionParameters getEncryptionParameters() {
		return new EncryptionParameters(config.getEncryptionParameters());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BucketConfig)) {
			return false;
		}
		BucketConfig that = (BucketConfig) obj;
		return config.equals(that.config);
	}

	@Override
	public int hashCode() {
		return config.hashCode();
	}
	
	io.storj.libuplink.mobile.BucketConfig internal() {
		return config;
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
			this.pathCipher = cipher;
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
