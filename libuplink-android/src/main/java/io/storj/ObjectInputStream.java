package io.storj;

import java.io.IOException;
import java.io.InputStream;

public class ObjectInputStream extends InputStream {

    private io.storj.libuplink.mobile.Reader reader;
    private byte[] buf = new byte[1];

    public ObjectInputStream(Bucket bucket, String objectPath) throws StorjException {
        try {
            this.reader = bucket.internal().newReader(objectPath, new io.storj.libuplink.mobile.ReaderOptions());
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    public ObjectInputStream(Bucket bucket, String objectPath, long off, long len) throws StorjException {
        try {
            this.reader = bucket.internal().newRangeReader(objectPath, off, len, new io.storj.libuplink.mobile.ReaderOptions());
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    @Override
    public int read() throws IOException {
        try {
            int n = reader.read(buf, 0, 1);
            if (n == -1) {
                return n;
            }
            if (n == 1) {
                return buf[0] & 0xff;
            }
            throw new IOException("invalid state");
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (off < 0 || len < 0 || off + len > b.length) {
            throw new IndexOutOfBoundsException();
        }
        try {
            return reader.read(b, off, len);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.reader.close();
        } catch (Exception e) {
            throw new IOException(e);
        }
        super.close();
    }

    public void cancel() {
        this.reader.cancel();
    }
}
