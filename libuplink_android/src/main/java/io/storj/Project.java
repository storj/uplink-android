package io.storj;

public class Project implements AutoCloseable {

    private io.storj.libuplink.mobile.Project project;

    public Project(io.storj.libuplink.mobile.Project project) {
        this.project = project;
    }

    public BucketInfo createBucket(String bucketName, BucketOption... options) throws StorjException {
        try {
            io.storj.libuplink.mobile.BucketInfo info = project.createBucket(bucketName, BucketOption.internal(options));
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
            io.storj.libuplink.mobile.BucketInfo bucketInfo = project.getBucketInfo(bucketName);
            return new BucketInfo(bucketInfo);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    public Iterable<BucketInfo> listBuckets(BucketListOption... options) throws StorjException {
        return new BucketIterator(project, options);
    }

    public void deleteBucket(String bucketName) throws StorjException {
        try {
            project.deleteBucket(bucketName);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    @Override
    public void close() throws StorjException {
        try {
            project.close();
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    io.storj.libuplink.mobile.Project internal() {
        return project;
    }

}
