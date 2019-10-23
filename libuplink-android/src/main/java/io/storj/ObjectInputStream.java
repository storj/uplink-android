package io.storj;

import java.io.IOException;
import java.io.InputStream;

/**
 * An {@link InputStream} for reading data from an object stored on the Storj network.
 */
public class ObjectInputStream extends InputStream {

    private io.storj.libuplink.mobile.Reader reader;
    private byte[] buf = new byte[1];

    /**
     * Creates a new {@link ObjectInputStream} for downloading the content of an object.
     *
     * @param bucket the {@link Bucket} that stores the object
     * @param objectPath a path to the object
     * @throws StorjException if an error occurs while establishing the connection
     */
    public ObjectInputStream(Bucket bucket, String objectPath) throws StorjException {
        try {
            this.reader = bucket.internal().newReader(objectPath, new io.storj.libuplink.mobile.ReaderOptions());
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Creates a new {@link ObjectInputStream} for downloading a portion of the content of an
     * object.
     *
     * @param bucket the {@link Bucket} that stores the object
     * @param objectPath a path to the object
     * @param off the starting offset in bytes
     * @param len the total bytes to download
     * @throws StorjException if an error occurs while establishing the connection
     */
    public ObjectInputStream(Bucket bucket, String objectPath, long off, long len) throws StorjException {
        try {
            this.reader = bucket.internal().newRangeReader(objectPath, off, len, new io.storj.libuplink.mobile.ReaderOptions());
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Reads the next byte of data from the input stream. The value byte is
     * returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     *
     * <p> A subclass must provide an implementation of this method.
     *
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
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

    /**
     * Reads up to <code>len</code> bytes of data from the input stream into
     * an array of bytes.  An attempt is made to read as many as
     * <code>len</code> bytes, but a smaller number may be read.
     * The number of bytes actually read is returned as an integer.
     *
     * <p> This method blocks until input data is available, end of file is
     * detected, or an exception is thrown.
     *
     * <p> If <code>len</code> is zero, then no bytes are read and
     * <code>0</code> is returned; otherwise, there is an attempt to read at
     * least one byte. If no byte is available because the stream is at end of
     * file, the value <code>-1</code> is returned; otherwise, at least one
     * byte is read and stored into <code>b</code>.
     *
     * <p> The first byte read is stored into element <code>b[off]</code>, the
     * next one into <code>b[off+1]</code>, and so on. The number of bytes read
     * is, at most, equal to <code>len</code>. Let <i>k</i> be the number of
     * bytes actually read; these bytes will be stored in elements
     * <code>b[off]</code> through <code>b[off+</code><i>k</i><code>-1]</code>,
     * leaving elements <code>b[off+</code><i>k</i><code>]</code> through
     * <code>b[off+len-1]</code> unaffected.
     *
     * <p> In every case, elements <code>b[0]</code> through
     * <code>b[off]</code> and elements <code>b[off+len]</code> through
     * <code>b[b.length-1]</code> are unaffected.
     *
     * <p> The <code>read(b,</code> <code>off,</code> <code>len)</code> method
     * for class <code>InputStream</code> simply calls the method
     * <code>read()</code> repeatedly. If the first such call results in an
     * <code>IOException</code>, that exception is returned from the call to
     * the <code>read(b,</code> <code>off,</code> <code>len)</code> method.  If
     * any subsequent call to <code>read()</code> results in a
     * <code>IOException</code>, the exception is caught and treated as if it
     * were end of file; the bytes read up to that point are stored into
     * <code>b</code> and the number of bytes read before the exception
     * occurred is returned. The default implementation of this method blocks
     * until the requested amount of input data <code>len</code> has been read,
     * end of file is detected, or an exception is thrown. Subclasses are encouraged
     * to provide a more efficient implementation of this method.
     *
     * @param      b     the buffer into which the data is read.
     * @param      off   the start offset in array <code>b</code>
     *                   at which the data is written.
     * @param      len   the maximum number of bytes to read.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end of
     *             the stream has been reached.
     * @exception  IOException If the first byte cannot be read for any reason
     * other than end of file, or if the input stream has been closed, or if
     * some other I/O error occurs.
     * @exception  NullPointerException If <code>b</code> is <code>null</code>.
     * @exception  IndexOutOfBoundsException If <code>off</code> is negative,
     * <code>len</code> is negative, or <code>len</code> is greater than
     * <code>b.length - off</code>
     * @see        java.io.InputStream#read()
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        try {
            return reader.read(b, off, len);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * Closes this input stream and releases any system resources associated
     * with the stream.
     *
     * <p> The <code>close</code> method of <code>InputStream</code> does
     * nothing.
     *
     * @exception  IOException  if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        try {
            this.reader.close();
        } catch (Exception e) {
            throw new IOException(e);
        }
        super.close();
    }

    /**
     * Cancels the download process.
     */
    public void cancel() {
        this.reader.cancel();
    }
}
