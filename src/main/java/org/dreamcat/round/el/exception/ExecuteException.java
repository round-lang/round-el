package org.dreamcat.round.el.exception;

/**
 * @author Jerry Will
 * @version 2021-07-24
 */
public class ExecuteException extends RoundException {

    public ExecuteException() {
        super();
    }

    public ExecuteException(String message) {
        super(message);
    }

    public ExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecuteException(Throwable cause) {
        super(cause);
    }
}
