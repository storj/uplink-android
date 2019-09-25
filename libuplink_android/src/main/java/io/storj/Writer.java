package io.storj;

import java.io.IOException;
import java.io.OutputStream;

class Writer extends OutputStream {

    private io.storj.libuplink.mobile.Writer writer;

    /**
     * This is the default buffer size
     */
    private static final int DEFAULT_BUFFER_SIZE = 512;

    /**
     * This is the internal byte array used for buffering output before
     * writing it.
     */
    private byte[] buf;

    /**
     * This is the number of bytes that are currently in the buffer and
     * are waiting to be written to the underlying stream. It always points to
     * the index into the buffer where the next byte of data will be stored
     */
    private int count;

    Writer(io.storj.libuplink.mobile.Writer writer) {
        this.writer = writer;
        this.buf = new byte[DEFAULT_BUFFER_SIZE];
    }

    @Override
    public void write(int b) throws IOException {
        if (count == buf.length) {
            flush();
        }
         buf[count] = (byte)(b & 0xFF);
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
            this.writer.close();
        } catch (Exception e) {
            throw new IOException(e);
        }
        super.close();
    }
}
