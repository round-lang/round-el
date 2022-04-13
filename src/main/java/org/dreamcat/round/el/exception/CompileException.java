package org.dreamcat.round.el.exception;

/**
 * @author Jerry Will
 * @since 2021-07-08
 */
public class CompileException extends RoundException {

    public CompileException() {
        super();
    }

    public CompileException(String message) {
        super(message);
    }

    public CompileException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompileException(Throwable cause) {
        super(cause);
    }
}
