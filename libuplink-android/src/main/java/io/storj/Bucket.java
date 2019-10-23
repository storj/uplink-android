package io.storj;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a stateful resource to a bucket in a project. It allows executing operations on the
 * bucket like listing, uploading, downloading, and deleting objects.
 *
 * <p>Make sure to always close the Bucket object after completing work with it. The Bucket class
 * implements the {@link java.lang.AutoCloseable} interface, so it is best to use Bucket objects
 * in try-with-resource blocks:</p>
 *
 * <pre>
 * {@code try (Uplink uplink = new Uplink();
 *       Project project = uplink.openProject(scope);
 *       Bucket bucket = project.openBucket("my-bucket", scope)) {
 *      ...
 *  }
 * }
 * </pre>
 */
public class Bucket implements AutoCloseable {

    private static final int BUFFER_SIZE = 128 * 1024;

    private io.storj.libuplink.mobile.Bucket bucket;

    Bucket(io.storj.libuplink.mobile.Bucket bucket) {
        this.bucket = bucket;
    }

    /**
     * Returns the bucket name.
     *
     * @return the bucket name
     */
    public String getName() {
        return bucket.getName();
    }

    /**
     * Uploads the content to a new object in this bucket.
     *
     * @param objectPath the path of the object to create
     * @param content a byte array of the content to upload
     * @param options options to apply on the new object
     * @throws StorjException if an error occurs during the upload
     */
    public void uploadObject(String objectPath, byte[] content, ObjectUploadOption... options) throws StorjException {
        uploadObject(objectPath, new ByteArrayInputStream(content), options);
    }

    /**
     * Uploads the content to a new object in this bucket.
     *
     * @param objectPath the path of the object to create
     * @param inputStream an {@link InputStream} that supplies the content to upload
     * @param options options to apply on the new object
     * @throws StorjException if an error occurs during the upload
     */
    public void uploadObject(String objectPath, InputStream inputStream, ObjectUploadOption... options) throws StorjException {
        try (OutputStream out = new ObjectOutputStream(this, objectPath, options);
             BufferedInputStream in = new BufferedInputStream(inputStream, BUFFER_SIZE)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new StorjException(e);
        }
    }

    /**
     * Downloads the content of an object from this buckets.
     *
     * @param objectPath the path of the object to download
     * @param outputStream an {@link OutputStream} to receive the downloaded content
     * @throws StorjException if an error occurs during the download
     */
    public void downloadObject(String objectPath, OutputStream outputStream) throws StorjException {
        try (InputStream in = new ObjectInputStream(this, objectPath)) {
            downloadObject(in, outputStream);
        } catch (IOException e) {
            throw new StorjException(e);
        }
    }

    /**
     * Downloads a portion of the content of an object from this buckets.
     *
     * @param objectPath the path of the object to download
     * @param outputStream an {@link OutputStream} to receive the downloaded content
     * @param off the starting offset in bytes
     * @param len the total bytes to download
     * @throws StorjException if an error occurs during the download
     */
    public void downloadObject(String objectPath, OutputStream outputStream, long off, long len) throws StorjException {
        try (InputStream in = new ObjectInputStream(this, objectPath, off, len)) {
            downloadObject(in, outputStream);
        } catch (IOException e) {
            throw new StorjException(e);
        }
    }

    private void downloadObject(InputStream in, OutputStream outputStream) throws StorjException {
        try (BufferedOutputStream out = new BufferedOutputStream(outputStream, BUFFER_SIZE)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new StorjException(e);
        }
    }

    /**
     * Lists the objects in this bucket.
     *
     * @param options an optional list of {@link ObjectListOption}
     * @return an {@link Iterable}&lt;{@link ObjectInfo}&gt;
     * @throws StorjException if an error occurs while retrieving the listing
     */
    public Iterable<ObjectInfo> listObjects(ObjectListOption... options) throws StorjException {
        return new ObjectIterator(this.bucket, options);
    }

    /**
     * Deletes an object from this bucket.
     *
     * @param objectPath the path of the object to delete
     * @throws StorjException if an error occurs during the deletion
     */
    public void deleteObject(String objectPath) throws StorjException {
        try {
            bucket.deleteObject(objectPath);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Closes the bucket and releases the allocated network resources.
     *
     * @throws StorjException if an error occurs while closing
     */
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
