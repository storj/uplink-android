package io.storj;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Bucket represents operations you can perform on a bucket like listing, uploading, downloading.
 */
public class Bucket implements AutoCloseable {

    private static final int BUFFER_SIZE = 128 * 1024;

    private io.storj.libuplink.mobile.Bucket bucket;

    Bucket(io.storj.libuplink.mobile.Bucket bucket) {
        this.bucket = bucket;
    }

    public String getName() {
        return bucket.getName();
    }

    public void uploadObject(String objectPath, byte[] content, ObjectUploadOption... options) throws StorjException {
        uploadObject(objectPath, new ByteArrayInputStream(content), options);
    }

    public void uploadObject(String objectPath, InputStream content, ObjectUploadOption... options) throws StorjException {
        try (OutputStream out = new ObjectOutputStream(this, objectPath, options);
             BufferedInputStream in = new BufferedInputStream(content, BUFFER_SIZE)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new StorjException(e);
        }
    }

    public void downloadObject(String objectPath, OutputStream outputStream) throws StorjException {
        try (InputStream in = new ObjectInputStream(this, objectPath);
             BufferedOutputStream out = new BufferedOutputStream(outputStream, BUFFER_SIZE)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new StorjException(e);
        }
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
