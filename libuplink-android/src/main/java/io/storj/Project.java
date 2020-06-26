package io.storj;

import io.storj.internal.Uplink;

public class Project implements AutoCloseable {

    protected Uplink.Project.ByReference project;

    Project(Uplink.Project.ByReference project) {
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
        Uplink.BucketResult.ByValue deleteBucket = Uplink.INSTANCE.delete_bucket(this.project, bucket);
        ExceptionUtil.handleError(deleteBucket.error);

        BucketInfo bucketInfo = new BucketInfo(deleteBucket.bucket);
        Uplink.INSTANCE.free_bucket_result(deleteBucket);
        return bucketInfo;
    }

    public ObjectInfo statObject(String bucket, String key) throws StorjException {
        Uplink.ObjectResult.ByValue statObject = Uplink.INSTANCE.stat_object(this.project, bucket, key);
        ExceptionUtil.handleError(statObject.error);

        ObjectInfo objectInfo = new ObjectInfo(statObject.object);
        Uplink.INSTANCE.free_object_result(statObject);
        return objectInfo;
    }

    public ObjectInfo deleteObject(String bucket, String key) throws StorjException {
        Uplink.ObjectResult.ByValue statObject = Uplink.INSTANCE.delete_object(this.project, bucket, key);
        ExceptionUtil.handleError(statObject.error);

        ObjectInfo objectInfo = new ObjectInfo(statObject.object);
        Uplink.INSTANCE.free_object_result(statObject);
        return objectInfo;
    }

    public ObjectOutputStream uploadObject(String bucket, String key, UploadOption... options) throws StorjException {
        Uplink.UploadResult.ByValue uploadResult = Uplink.INSTANCE.upload_object(this.project, bucket, key,
                UploadOption.internal(options));
        ExceptionUtil.handleError(uploadResult.error);

        return new ObjectOutputStream(uploadResult.upload);
    }

    public ObjectInputStream downloadObject(String bucket, String key, DownloadOption... options) throws StorjException {
        Uplink.DownloadResult.ByValue downloadResult = Uplink.INSTANCE.download_object(this.project, bucket, key,
                DownloadOption.internal(options));
        ExceptionUtil.handleError(downloadResult.error);

        return new ObjectInputStream(downloadResult.download);
    }

    @Override
    public void close() throws Exception {
        Uplink.Error.ByReference result = Uplink.INSTANCE.close_project(this.project);
        ExceptionUtil.handleError(result);
    }
}
