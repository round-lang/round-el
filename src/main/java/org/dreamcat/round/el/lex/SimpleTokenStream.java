package org.dreamcat.round.el.lex;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.dreamcat.round.el.exception.CompileException;

/**
 * @author Jerry Will
 * @version 2021-09-07
 */
@RequiredArgsConstructor
public class SimpleTokenStream implements TokenStream {

    @Getter
    protected final String expression;
    protected final LexSettings settings;
    protected final List<TokenInfo> tokenInfos = new ArrayList<>();
    protected int offset = -1;
    protected int size;
    @Setter
    protected int firstLineNo = 1;
    @Setter
    protected int firstCol = 1;

    public SimpleTokenStream copy() {
        SimpleTokenStream stream = new SimpleTokenStream(expression, settings);
        for (TokenInfo tokenInfo : tokenInfos) {
            stream.add(tokenInfo);
        }
        return stream;
    }

    // only invoke it in a lexer
    public void add(TokenInfo tokenInfo) {
        tokenInfos.add(tokenInfo);
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    @Override
    public TokenInfo getTokenInfo() {
        if (offset >= size || offset < 0) return throwWrongSyntax();
        return computeTokenInfo();
    }

    @Override
    public Token get() {
        return getTokenInfo().getToken();
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    @Override
    public boolean hasNext() {
        return offset < size - 1;
    }

    @Override
    public Token next() {
        if (hasNext()) {
            return tokenInfos.get(++offset).getToken();
        } else {
            return throwWrongSyntax();
        }
    }

    @Override
    public void previous() {
        if (offset > -1) {
            --offset;
        } else {
            throwWrongSyntax();
        }
    }

    @Override
    public TokenStream nextUntil(Token delimiterToken) {
        SimpleTokenStream stream = new SimpleTokenStream(
                expression, settings);
        while (hasNext()) {
            Token token = next();
            if (token.equals(delimiterToken)) {
                break;
            }
            stream.add(getTokenInfo());
        }
        return stream;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    @Override
    public <T> T throwWrongSyntax() {
        if (offset >= size) offset = size - 1;
        else if (offset < 0) offset = 0;

        TokenInfo tokenInfo = computeTokenInfo();
        int halfWidth = settings.sampleCharCount >> 1;
        int start = tokenInfo.getStart() - halfWidth, end = start + halfWidth;

        if (settings.sampleCharCount <= 0) {
            // no sample
            throw new CompileException(String.format(
                    "You has wrong syntax in your %s, at line %d col %d",
                    settings.getName(), tokenInfo.getLine(), tokenInfo.getCol()));
        }
        if (end >= size) {
            start -= (end - size);
            end = size;
        }
        if (start < 0) {
            end += -start;
            start = 0;
        }
        if (end >= size) end = size;

        throw new CompileException(String.format(
                "You has wrong syntax in your %s, at line %d col %d near by: %s",
                settings.getName(), tokenInfo.getLine(), tokenInfo.getCol(),
                expression.substring(start, end)));
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    private TokenInfo computeTokenInfo() {
        SimpleTokenInfo prevTokenInfo = offset > 0 ?
                (SimpleTokenInfo) tokenInfos.get(offset - 1) : null;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) tokenInfos.get(offset);

        int prev = 0;
        // already compute, then use previous token cahce
        if (prevTokenInfo != null && prevTokenInfo.line > 0) {
            prev = prevTokenInfo.getStart();
            tokenInfo.line = prevTokenInfo.line;
            tokenInfo.col = prevTokenInfo.col;
        } else {
            tokenInfo.line = firstLineNo;
            tokenInfo.col = firstCol;
        }

        int start = tokenInfo.getStart();
        for (int i = prev; i < start; i++) {
            char c = expression.charAt(i);
            if (c != '\n') {
                tokenInfo.col++;
            } else {
                tokenInfo.line++;
                tokenInfo.col = 1;
            }
        }
        return tokenInfo;
    }
}
