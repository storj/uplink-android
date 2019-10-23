package io.storj;

/**
 * Project represents operations you can perform on a project like bucket creating, listing,
 * deleting.
 */
public class Project implements AutoCloseable {

    private io.storj.libuplink.mobile.Project project;

    Project(io.storj.libuplink.mobile.Project project) {
        this.project = project;
    }

    /**
     * Creates a new bucket if authorized.
     *
     * @param bucketName bucket name
     * @param options    set of bucket options
     * @return created bucket info
     * @throws StorjException
     */
    public BucketInfo createBucket(String bucketName, BucketCreateOption... options) throws StorjException {
        try {
            io.storj.libuplink.mobile.BucketInfo info = project.createBucket(bucketName, BucketCreateOption.internal(options));
            return new BucketInfo(info);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Returns a Bucket handle with given scope.
     *
     * @param bucketName bucket name
     * @param scope      scope encryption access will be used do open bucket
     * @return handle to a bucket
     * @throws StorjException
     */
    public Bucket openBucket(String bucketName, Scope scope) throws StorjException {
        return this.openBucket(bucketName, scope.getEncryptionAccess());
    }

    /**
     * Returns a Bucket handle with given encryption access.
     *
     * @param bucketName bucket name
     * @param access     encryption access will be used do open bucket
     * @return handle to a bucket
     * @throws StorjException
     */
    public Bucket openBucket(String bucketName, EncryptionAccess access) throws StorjException {
        try {
            io.storj.libuplink.mobile.Bucket bucket = project.openBucket(bucketName, access.internal());
            return new Bucket(bucket);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Returns info about bucket.
     *
     * @param bucketName bucket name
     * @return bucket info
     * @throws StorjException
     */
    public BucketInfo getBucketInfo(String bucketName) throws StorjException {
        try {
            io.storj.libuplink.mobile.BucketInfo bucketInfo = project.getBucketInfo(bucketName);
            return new BucketInfo(bucketInfo);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Lists buckets with given options
     *
     * @param options set of options
     * @return list of buckets
     * @throws StorjException
     */
    public Iterable<BucketInfo> listBuckets(BucketListOption... options) throws StorjException {
        return new BucketIterator(project, options);
    }

    /**
     * Deletes a bucket if authorized. If the bucket contains any
     * Objects at the time of deletion, they may be lost permanently.
     *
     * @param bucketName bucket name
     * @throws StorjException
     */
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
