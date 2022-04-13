package org.dreamcat.round.el.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Jerry Will
 * @version 2021-09-07
 */
@Getter
@RequiredArgsConstructor
public class ReturnException extends JumpException {

    final Object value;
}
