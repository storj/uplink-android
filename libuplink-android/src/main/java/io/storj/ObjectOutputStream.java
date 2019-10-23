package io.storj;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An {@link OutputStream} for writing data to an object stored on the Storj network.
 */
public class ObjectOutputStream extends OutputStream {

    private io.storj.libuplink.mobile.Writer writer;

    /**
     * Used for efficiency by `write(int b)`
     */
    private byte[] buf = new byte[1];

    /**
     * Creates a new {@link ObjectOutputStream} for uploading content to an object.
     *
     * @param bucket the {@link Bucket} to store the object
     * @param objectPath a path to the new object
     * @param options an optional list of {@link ObjectUploadOption}
     * @throws StorjException if an error occurs while establishing the connection
     */
    public ObjectOutputStream(Bucket bucket, String objectPath, ObjectUploadOption... options) throws StorjException {
        try {
            this.writer = bucket.internal().newWriter(objectPath, ObjectUploadOption.internal(options));
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Writes the specified byte to this output stream. The general
     * contract for <code>write</code> is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument <code>b</code>. The 24
     * high-order bits of <code>b</code> are ignored.
     * <p>
     * Subclasses of <code>OutputStream</code> must provide an
     * implementation for this method.
     *
     * @param      b   the <code>byte</code>.
     * @exception  IOException  if an I/O error occurs. In particular,
     *             an <code>IOException</code> may be thrown if the
     *             output stream has been closed.
     */
    @Override
    public void write(int b) throws IOException {
        buf[0] = (byte) b;
        write(buf, 0, 1);
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this output stream.
     * The general contract for <code>write(b, off, len)</code> is that
     * some of the bytes in the array <code>b</code> are written to the
     * output stream in order; element <code>b[off]</code> is the first
     * byte written and <code>b[off+len-1]</code> is the last byte written
     * by this operation.
     * <p>
     * The <code>write</code> method of <code>OutputStream</code> calls
     * the write method of one argument on each of the bytes to be
     * written out. Subclasses are encouraged to override this method and
     * provide a more efficient implementation.
     * <p>
     * If <code>b</code> is <code>null</code>, a
     * <code>NullPointerException</code> is thrown.
     * <p>
     * If <code>off</code> is negative, or <code>len</code> is negative, or
     * <code>off+len</code> is greater than the length of the array
     * <code>b</code>, then an <tt>IndexOutOfBoundsException</tt> is thrown.
     *
     * @param      b     the data.
     * @param      off   the start offset in the data.
     * @param      len   the number of bytes to write.
     * @exception  IOException  if an I/O error occurs. In particular,
     *             an <code>IOException</code> is thrown if the output
     *             stream is closed.
     */
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

    /**
     * Closes this output stream and releases any system resources
     * associated with this stream. The general contract of <code>close</code>
     * is that it closes the output stream. A closed stream cannot perform
     * output operations and cannot be reopened.
     * <p>
     * The <code>close</code> method of <code>OutputStream</code> does nothing.
     *
     * @exception  IOException  if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        try {
            this.writer.close();
        } catch (Exception e) {
            throw new IOException(e);
        }
        super.close();
    }

    /**
     * Cancels the upload process.
     */
    public void cancel() {
        this.writer.cancel();
    }
}
