package io.storj;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

	public OutputStream newWriter(String objectName, WriterOptions options) throws StorjException {
		try {
			io.storj.libuplink.mobile.Writer writer  = this.bucket.newWriter(objectName, options.internal());
			return new Writer(writer);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}

	public InputStream newReader(String objectName, ReaderOptions options) throws StorjException {
		try {
			io.storj.libuplink.mobile.Reader reader  = this.bucket.newReader(objectName, options.internal());
			return new Reader(reader);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
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
