package io.storj;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import io.storj.internal.Uplink;

/**
 * An {@link OutputStream} for writing data to an object stored on the Storj network.
 */
public class ObjectOutputStream extends OutputStream {

    private Uplink.Upload.ByReference cUpload;

    /**
     * Used for efficiency by `write(int b)`
     */
    private byte[] buf = new byte[1];

    ObjectOutputStream(Uplink.Upload.ByReference cUpload) {
        this.cUpload = cUpload;
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
     * @param b the <code>byte</code>.
     * @throws IOException if an I/O error occurs. In particular,
     *                     an <code>IOException</code> may be thrown if the
     *                     output stream has been closed.
     */
    @Override
    public void write(int b) throws IOException {
        buf[0] = (byte) b;

        Pointer byteArray = new Memory(1);
        byteArray.write(0, buf, 0, buf.length);
        Uplink.WriteResult.ByValue writeResult = Uplink.INSTANCE.upload_write(this.cUpload, byteArray, new NativeLong(1));
        try {
            ExceptionUtil.handleError(writeResult.error);
        } catch (StorjException e) {
            throw new IOException(e);
        }
        Uplink.INSTANCE.free_write_result(writeResult);
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
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IOException if an I/O error occurs. In particular,
     *                     an <code>IOException</code> is thrown if the output
     *                     stream is closed.
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

        Pointer byteArray = new Memory(len);
        byteArray.write(0, b, off, len);
        Uplink.WriteResult.ByValue writeResult = Uplink.INSTANCE.upload_write(this.cUpload, byteArray, new NativeLong(len));
        try {
            ExceptionUtil.handleError(writeResult.error);
        } catch (StorjException e) {
            throw new IOException(e);
        }
        Uplink.INSTANCE.free_write_result(writeResult);
    }

    /**
     * Closes this input stream and releases any system resources associated
     * with the stream.
     *
     * <p> The <code>close</code> method of <code>InputStream</code> does
     * nothing.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        Uplink.UploadResult.ByValue result = new Uplink.UploadResult.ByValue();
        result.upload = this.cUpload;
        Uplink.INSTANCE.free_upload_result(result);
    }

    public void commit() throws StorjException {
        Uplink.Error.ByReference error = Uplink.INSTANCE.upload_commit(this.cUpload);
        ExceptionUtil.handleError(error);
    }

    public void setCustom(Map<String, String> metadata) throws StorjException {
        if (metadata.isEmpty()){
            return;
        }

        Uplink.CustomMetadataEntry.ByReference singleEntry = new Uplink.CustomMetadataEntry.ByReference();
        Structure[] entries = singleEntry.toArray(metadata.size());
        int i = 0;
        for (Map.Entry<String, String> metadataEntry : metadata.entrySet()) {
            Uplink.CustomMetadataEntry.ByReference entry = (Uplink.CustomMetadataEntry.ByReference) entries[i];
            entry.key = metadataEntry.getKey();
            entry.key_length = new NativeLong(metadataEntry.getKey().length());
            entry.value = metadataEntry.getValue();
            entry.value_length = new NativeLong(metadataEntry.getValue().length());
            i++;
        }

        Uplink.CustomMetadata.ByValue customMetadata = new Uplink.CustomMetadata.ByValue();
        customMetadata.entries = ((Uplink.CustomMetadataEntry.ByReference) entries[0]);
        customMetadata.count = new NativeLong(entries.length);

        Uplink.Error.ByReference error = Uplink.INSTANCE.upload_set_custom_metadata(this.cUpload, customMetadata);
        ExceptionUtil.handleError(error);
    }

    /**
     * Aborts the upload process.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void abort() throws IOException {
        Uplink.Error.ByReference error = Uplink.INSTANCE.upload_abort(this.cUpload);
        try {
            ExceptionUtil.handleError(error);
        } catch (StorjException e) {
            throw new IOException(e);
        }
    }
}
