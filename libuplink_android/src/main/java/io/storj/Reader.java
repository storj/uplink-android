package io.storj;

import java.io.IOException;
import java.io.InputStream;

class Reader extends InputStream {

    private io.storj.libuplink.mobile.Reader reader;

    Reader(io.storj.libuplink.mobile.Reader reader) {
        this.reader=reader;
    }

    @Override
    public int read() throws IOException {
        byte[] buf = new byte[1];
        try {
            int n = reader.read(buf);
            if (n == -1) {
                return n;
            }
            if (n == 1) {
                return buf[0]& 0xff;
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
