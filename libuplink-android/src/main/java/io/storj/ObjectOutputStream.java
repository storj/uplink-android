package io.storj;

import java.io.IOException;
import java.io.OutputStream;

public class ObjectOutputStream extends OutputStream {

    private io.storj.libuplink.mobile.Writer writer;

    /**
     * Used for efficiency by `write(int b)`
     */
    private byte[] buf = new byte[1];

    /**
     * This is the number of bytes that are currently in the buffer and
     * are waiting to be written to the underlying stream. It always points to
     * the index into the buffer where the next byte of data will be stored
     */
    private int count;

    public ObjectOutputStream(Bucket bucket, String objectPath, ObjectUploadOption... options) throws StorjException {
        try {
            this.writer = bucket.internal().newWriter(objectPath, ObjectUploadOption.internal(options));
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    @Override
    public void write(int b) throws IOException {
        buf[0] = (byte) b;
        write(buf, 0, 1);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }

        try {
            writer.write(b, off, len);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.writer.close();
        } catch (Exception e) {
            throw new IOException(e);
        }
        super.close();
    }

    public void cancel() {
        this.writer.cancel();
    }
}
