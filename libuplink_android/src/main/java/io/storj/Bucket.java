package io.storj;

public class Bucket implements AutoCloseable {
	
	private io.storj.libuplink.mobile.Bucket bucket;

	Bucket(io.storj.libuplink.mobile.Bucket bucket) {
		this.bucket = bucket;
	}
	
	public String getName() {
		return bucket.getName();
	}

	public Iterable<ObjectInfo> listObjects(ObjectListOption... options) throws StorjException {
		return new ObjectIterator(this.bucket, options);
	}

	public void deleteObject(String objectPath) throws StorjException {
		try {
			bucket.deleteObject(objectPath);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}

	@Override
	public void close() throws StorjException {
		try {
			bucket.close();
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}

	io.storj.libuplink.mobile.Bucket internal() {
		return bucket;
	}

}
