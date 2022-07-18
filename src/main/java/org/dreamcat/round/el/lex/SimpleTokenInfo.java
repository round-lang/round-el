package org.dreamcat.round.el.lex;

import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Jerry Will
 * @version 2021-09-07
 */
@Getter
@RequiredArgsConstructor
public class SimpleTokenInfo implements TokenInfo {

    final Token token;
    final int start;
    final int end;
    int line; // 1-based index, for cache purpose
    int col; // 1-based

    public static SimpleTokenInfo of(
            Token token, int start, int end) {
        return new SimpleTokenInfo(token, start, end);
    }

    @Override
    public String toString() {
        return token.getClass().getSimpleName() + "@" + token;
    }
}
