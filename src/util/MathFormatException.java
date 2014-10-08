package util;

public class MathFormatException extends Exception {

    /**
     * Creates a new instance of
     * <code>MathFormatException</code> without detail message.
     */
    public MathFormatException() {
    }

    /**
     * Constructs an instance of
     * <code>MathFormatException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public MathFormatException(String msg) {
        super(msg);
    }
}