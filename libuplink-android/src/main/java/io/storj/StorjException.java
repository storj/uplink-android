package io.storj;

/**
 * Signals that an exception occurred when calling a method of the Storj library.
 */
public class StorjException extends Exception {

    private static final long serialVersionUID = 5024346477061996880L;

    /**
     * Constructs a {@link StorjException} with the specified cause and a detail message of
     * <code>(cause==null ? null : cause.toString())</code> (which typically contains the class and
     * detail message of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link Throwable#getCause()} method). (A <code>null</code> value is permitted,
     *              and indicates that the cause is nonexistent or unknown.)
     */
    public StorjException(Throwable cause) {
        super(cause);
    }

    public StorjException(String message) {
        super(message);
    }

}
