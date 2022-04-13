package org.dreamcat.round.el.exception;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
public class RoundException extends RuntimeException {

    public RoundException() {
        super();
    }

    public RoundException(String message) {
        super(message);
    }

    public RoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoundException(Throwable cause) {
        super(cause);
    }
}
