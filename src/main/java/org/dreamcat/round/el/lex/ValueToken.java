package org.dreamcat.round.el.lex;

import java.util.Objects;
import lombok.Getter;

/**
 * @author Jerry Will
 * @version 2022-02-02
 */
@Getter
public abstract class ValueToken<T> extends AbstractToken {

    final T value;

    public ValueToken(T value, Type type, String rawToken) {
        super(type, rawToken);
        this.value = value;
    }

    @Override
    public boolean isValue() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof ValueToken) {
            return Objects.equals(value, ((ValueToken<?>) o).value);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
