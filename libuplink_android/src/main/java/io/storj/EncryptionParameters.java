package io.storj;

public class EncryptionParameters {

	private io.storj.libuplink.mobile.EncryptionParameters params;

	EncryptionParameters(io.storj.libuplink.mobile.EncryptionParameters params) {
		this.params = params;
	}
	
	public CipherSuite getCipher() {
		return CipherSuite.fromValue(this.params.getCipherSuite());
	}
	
	public int getBlockSize() {
		return this.params.getBlockSize();
	}
	
	io.storj.libuplink.mobile.EncryptionParameters internal() {
		return this.params;
	}

	private EncryptionParameters(Builder builder) {
		this.params = new io.storj.libuplink.mobile.EncryptionParameters();
		if (builder.cipher != null) {
			this.params.setCipherSuite(builder.cipher.getValue());
		}
		this.params.setBlockSize(builder.blockSize);
	}

	public static class Builder {

		private CipherSuite cipher;
		private int blockSize;

		public Builder setCipher(CipherSuite cipher) {
			this.cipher = cipher;
			return this;
		}

		public Builder setBlockSize(int size) {
			this.blockSize = size;
			return this;
		}

		public EncryptionParameters build() {
			return new EncryptionParameters(this);
		}
	}
}
