package io.storj;

import java.io.IOException;
import java.io.OutputStream;

class ObjectOutputStream extends OutputStream {

    private Bucket bucket;
    private io.storj.libuplink.mobile.Writer writer;

    /**
     * This is the default buffer size
     */
    private static final int DEFAULT_BUFFER_SIZE = 512;

    /**
     * This is the internal byte array used for buffering output before
     * writing it.
     */
    private byte[] buf = new byte[DEFAULT_BUFFER_SIZE];

    /**
     * This is the number of bytes that are currently in the buffer and
     * are waiting to be written to the underlying stream. It always points to
     * the index into the buffer where the next byte of data will be stored
     */
    private int count;

    public ObjectOutputStream(Bucket bucket, String objectPath, ObjectUploadOption... options) throws StorjException {
        this.bucket = bucket;
        try {
            this.writer = this.bucket.internal().newWriter(objectPath, ObjectUploadOption.internal(options));
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    @Override
    public void write(int b) throws IOException {
        if (count == buf.length) {
            flush();
        }
        buf[count] = (byte) (b & 0xFF);
        ++count;
    }

    @Override
    public void flush() throws IOException {
        if (count == 0) {
            return;
        }
        try {
            this.writer.write(buf, 0, count);
            this.count = 0;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.flush();
            this.writer.close();
        } catch (Exception e) {
            throw new IOException(e);
        }
        super.close();
    }
}
