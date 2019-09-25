package io.storj;

public class EncryptionAccess {
	
	io.storj.libuplink.mobile.EncryptionAccess access;

	EncryptionAccess(){
		this.access = new io.storj.libuplink.mobile.EncryptionAccess();
	}

	public void setDefaultKey(byte[] key) throws StorjException {
		try {
			this.access.setDefaultKey(key);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}

	public String serialize() throws StorjException {
		try {
			return this.access.serialize();
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}
}
