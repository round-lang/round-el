package org.dreamcat.round.el.function;

/**
 * @author Jerry Will
 * @version 2021-07-23
 */
@FunctionalInterface
public interface ElFunction {

    Object invoke(Object... args);
}
