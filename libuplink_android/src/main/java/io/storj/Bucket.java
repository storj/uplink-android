package io.storj;

import java.io.Closeable;
import java.io.IOException;

public class Bucket implements Closeable {
	
	private io.storj.libuplink.mobile.Bucket bucket;

	Bucket(io.storj.libuplink.mobile.Bucket bucket) {
		this.bucket = bucket;
	}
	
	public String getName() {
		return bucket.getName();
	}

	public Iterable<ObjectInfo> listObjects(ListOptions options) throws StorjException {
		return new ObjectIterator(this.bucket, options);
	}

	public void deleteObject(String objectPath) throws StorjException {
		try {
			bucket.deleteObject(objectPath);
		} catch (Exception e) {
			ExceptionUtil.toStorjException(e);
		}
	}

	@Override
	public void close() throws IOException {
		try {
			bucket.close();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
