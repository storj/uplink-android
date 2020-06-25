package io.storj;

import io.storj.internal.Uplink;

public class Project implements AutoCloseable {

    protected Uplink.Project.ByReference project;

    protected Project(Uplink.Project.ByReference project) {
        this.project = project;
    }

    public BucketInfo statBucket(String name) throws StorjException {
        Uplink.BucketResult.ByValue result = Uplink.INSTANCE.stat_bucket(this.project, name);
        ExceptionUtil.handleError(result.error);

        BucketInfo bucketInfo = new BucketInfo(result.bucket);
        Uplink.INSTANCE.free_bucket_result(result);
        return bucketInfo;
    }

    public BucketInfo createBucket(String name) throws StorjException {
        Uplink.BucketResult.ByValue result = Uplink.INSTANCE.create_bucket(this.project, name);
        ExceptionUtil.handleError(result.error);

        BucketInfo bucketInfo = new BucketInfo(result.bucket);
        Uplink.INSTANCE.free_bucket_result(result);
        return bucketInfo;
    }

    public BucketInfo ensureBucket(String name) throws StorjException {
        Uplink.BucketResult.ByValue result = Uplink.INSTANCE.ensure_bucket(this.project, name);
        ExceptionUtil.handleError(result.error);

        BucketInfo bucketInfo = new BucketInfo(result.bucket);
        Uplink.INSTANCE.free_bucket_result(result);
        return bucketInfo;
    }

    public Buckets listBuckets(BucketListOption... options) {
        return new Buckets(this.project, options);
    }

    public BucketInfo deleteBucket(String bucket) throws StorjException {
        Uplink.BucketResult.ByValue bucketResult = Uplink.INSTANCE.delete_bucket(this.project, bucket);

        BucketInfo bucketInfo = new BucketInfo(bucketResult.bucket);
        Uplink.INSTANCE.free_bucket_result(bucketResult);
        return bucketInfo;
    }

    @Override
    public void close() throws Exception {
        Uplink.Error result = Uplink.INSTANCE.close_project(this.project);
        ExceptionUtil.handleError(result);
    }
}
