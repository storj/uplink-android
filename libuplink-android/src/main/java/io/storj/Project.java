package io.storj;

/**
 * Represents a stateful resource to a project on a satellite. It allows executing operations on the
 * project like creating, opening, listing, and deleting buckets.
 *
 * <p>Make sure to always close the Project object after completing work with it. The Project class
 * implements the {@link java.lang.AutoCloseable} interface, so it is best to use Project objects
 * in try-with-resource blocks:</p>
 *
 * <pre>
 * {@code try (Uplink uplink = new Uplink();
 *       Project project = uplink.openProject(scope)) {
 *      ...
 *  }
 * }
 * </pre>
 */
public class Project implements AutoCloseable {

    private io.storj.libuplink.mobile.Project project;

    Project(io.storj.libuplink.mobile.Project project) {
        this.project = project;
    }

    /**
     * Creates a new bucket in this project.
     *
     * @param bucketName a bucket name
     * @param options an optional list of {@link BucketCreateOption}
     * @return created bucket info
     * @throws StorjException in case of error
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
     * Returns a {@link Bucket} handle with the encryption access from the given scope.
     *
     * @param bucketName bucket name
     * @param scope a {@link Scope}
     * @return a {@link Bucket} handle
     * @throws StorjException in case of error
     */
    public Bucket openBucket(String bucketName, Scope scope) throws StorjException {
        return this.openBucket(bucketName, scope.getEncryptionAccess());
    }

    /**
     * Returns a {@link Bucket} handle with given encryption access.
     *
     * @param bucketName a bucket name
     * @param access an {@link EncryptionAccess}
     * @return a {@link Bucket} handle
     * @throws StorjException in case of error
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
     * Returns bucket metadata.
     *
     * @param bucketName a bucket name
     * @return a {@link BucketInfo}
     * @throws StorjException in case of error
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
     * Lists the buckets in this project.
     *
     * @param options set of options
     * @return an optional list of {@link BucketListOption}
     * @throws StorjException in case of error
     */
    public Iterable<BucketInfo> listBuckets(BucketListOption... options) throws StorjException {
        return new BucketIterator(project, options);
    }

    /**
     * Deletes a bucket from this project.
     *
     * @param bucketName a bucket name
     * @throws StorjException in case of error
     */
    public void deleteBucket(String bucketName) throws StorjException {
        try {
            project.deleteBucket(bucketName);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Closes the project and releases the allocated network resources.
     *
     * @throws StorjException if an error occurs while closing
     */
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
