package io.storj;

public class EncryptionParameters {

	private io.storj.libuplink.mobile.EncryptionParameters params;
	
	public EncryptionParameters(CipherSuite cipher, int blockSize) {
		this.params = new io.storj.libuplink.mobile.EncryptionParameters();
		this.params.setCipherSuite(cipher.getValue());
		this.params.setBlockSize(blockSize);
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
}
