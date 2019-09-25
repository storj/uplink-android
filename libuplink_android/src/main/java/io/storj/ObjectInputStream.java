package io.storj;

import java.io.IOException;
import java.io.InputStream;

class ObjectInputStream extends InputStream {

    private io.storj.libuplink.mobile.Reader reader;
    private byte[] buf = new byte[1];

    public ObjectInputStream(Bucket bucket, String objectPath) throws StorjException {
        try {
            this.reader = bucket.internal().newReader(objectPath, new io.storj.libuplink.mobile.ReaderOptions());
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    @Override
    public int read() throws IOException {
        try {
            int n = reader.read(buf);
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
    public void close() throws IOException {
        try {
            this.reader.close();
        } catch (Exception e) {
            throw new IOException(e);
        }
        super.close();
    }
}
