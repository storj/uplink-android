package io.storj;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

public class Project implements Closeable {
	
	private io.storj.libuplink.mobile.Project project;
	
	public Project(io.storj.libuplink.mobile.Project project) {
		this.project = project;
	}
	
	public BucketInfo createBucket(String bucketName, BucketConfig config) throws StorjException {
		try {
			io.storj.libuplink.mobile.BucketInfo info = project.createBucket(bucketName, config.internal());
			return new BucketInfo(info);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}
	
	public Bucket openBucket(String bucketName, EncryptionAccess access) throws StorjException {
		try {
			io.storj.libuplink.mobile.Bucket bucket = project.openBucket(bucketName, access.access);
			return new Bucket(bucket);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}
	
	public BucketInfo getBucketInfo(String bucketName) throws StorjException {
		try {
			io.storj.libuplink.mobile.BucketInfo info = project.getBucketInfo(bucketName);
			return new BucketInfo(info);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}

	public Iterable<BucketInfo> listBuckets() throws StorjException {
		return listBuckets(null, 0);
	}

	public Iterable<BucketInfo> listBuckets(String cursor) throws StorjException {
		return listBuckets(cursor, 0);
	}

	public Iterable<BucketInfo> listBuckets(int pageSize) throws StorjException {
		return listBuckets(null, pageSize);
	}
	
	public Iterable<BucketInfo> listBuckets(String cursor, final int pageSize) throws StorjException {
		return new BucketIterator(project, cursor, pageSize);
	}
	
	public void deleteBucket(String bucketName) throws StorjException {
		try {
			project.deleteBucket(bucketName);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}

	@Override
	public void close() throws IOException {
		try {
			project.close();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
