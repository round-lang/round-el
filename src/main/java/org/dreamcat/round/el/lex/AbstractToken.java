package org.dreamcat.round.el.lex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
@Getter
@RequiredArgsConstructor
class AbstractToken implements Token {

    final Type type;
    final String rawToken;

    @Override
    public String toString() {
        return this.getRawToken();
    }
}
