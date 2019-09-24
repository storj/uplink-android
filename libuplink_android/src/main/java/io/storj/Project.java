package io.storj;

import java.io.Closeable;
import java.io.IOException;

public class Project implements Closeable {
	
	private io.storj.libuplink.mobile.Project project;
	
	public Project(io.storj.libuplink.mobile.Project project) {
		this.project = project;
	}
	
	public BucketInfo createBucket(String bucketName, BucketConfig config) throws StorjException {
		try {
			io.storj.libuplink.mobile.BucketInfo info = this.project.createBucket(bucketName, config.internal());
			return new BucketInfo(info);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}
	
	public Bucket openBucket(String bucketName, EncryptionAccess access) throws StorjException {
		try {
			io.storj.libuplink.mobile.Bucket bucket = this.project.openBucket(bucketName, access.access);
			return new Bucket(bucket);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}
	
	public BucketInfo getBucketInfo(String bucketName) throws StorjException {
		try {
			io.storj.libuplink.mobile.BucketInfo info = this.project.getBucketInfo(bucketName);
			return new BucketInfo(info);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}
	
	public BucketList listBuckets(String cursor, int limit) throws StorjException {
		try {
			io.storj.libuplink.mobile.BucketList list = this.project.listBuckets(cursor, limit);
			return new BucketList(list);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}
	
	public void deleteBucket(String bucketName) throws StorjException {
		try {
			this.project.deleteBucket(bucketName);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}

	@Override
	public void close() throws IOException {
		try {
			this.project.close();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
