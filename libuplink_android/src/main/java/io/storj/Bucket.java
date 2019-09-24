package io.storj;

import java.io.Closeable;
import java.io.IOException;

public class Bucket implements Closeable {
	
	private io.storj.libuplink.mobile.Bucket bucket;

	Bucket(io.storj.libuplink.mobile.Bucket bucket) {
		this.bucket = bucket;
	}
	
	public String getName() {
		return this.bucket.getName();
	}

	@Override
	public void close() throws IOException {
		try {
			this.bucket.close();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
