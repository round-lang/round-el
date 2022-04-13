package org.dreamcat.round.el.exception;

/**
 * @author Jerry Will
 * @version 2021-07-25
 */
public class UnsupportedOpException extends ExecuteException {

    public UnsupportedOpException() {
        super();
    }

    public UnsupportedOpException(String message) {
        super(message);
    }

    public UnsupportedOpException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedOpException(Throwable cause) {
        super(cause);
    }
}
