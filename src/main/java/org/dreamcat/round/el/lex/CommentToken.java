package org.dreamcat.round.el.lex;

import lombok.Getter;

/**
 * @author Jerry Will
 * @version 2022-01-28
 */
@Getter
public class CommentToken extends AbstractToken {

    private String sep;
    private String startSep;
    private String endSep;

    private CommentToken(String rawToken) {
        super(Type.COMMENT, rawToken);
    }

    public boolean isSingle() {
        return sep != null;
    }

    public boolean isMultiple() {
        return !isSingle();
    }

    public static CommentToken of(String rawToken, String sep) {
        CommentToken token = new CommentToken(rawToken);
        token.sep = sep;
        return token;
    }

    public static CommentToken of(String rawToken, String startSep, String endSep) {
        CommentToken token = new CommentToken(rawToken);
        token.startSep = startSep;
        token.endSep = endSep;
        return token;
    }
}
