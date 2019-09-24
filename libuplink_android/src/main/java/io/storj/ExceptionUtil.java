package io.storj;

class ExceptionUtil {
	
	static StorjException toStorjException(Exception e) {
		return new StorjException(e);
	}

}
