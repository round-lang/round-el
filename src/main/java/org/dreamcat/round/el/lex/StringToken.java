package org.dreamcat.round.el.lex;

import lombok.Getter;
import org.dreamcat.common.util.StringUtil;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
@Getter
public class StringToken extends ValueToken<String> {

    final String prefix; // like f'$a ${b + c}'
    final String sep; // single sep or left sep
    final String secondSep; // right sep

    private StringToken(String value, String rawValue, String prefix, String sep, String secondSep) {
        super(value, Type.STRING, rawValue);
        this.prefix = prefix;
        this.sep = sep;
        this.secondSep = secondSep;
    }

    public static StringToken ofSingle(String rawValue) {
        String rawToken = String.format("'%s'", rawValue);
        String value = StringUtil.unescape(rawValue);
        return new StringToken(value, rawToken, null, "'", null);
    }

    public static StringToken ofDouble(String rawValue) {
        String rawToken = String.format("\"%s\"", rawValue);
        String value = StringUtil.unescape(rawValue);
        return new StringToken(value, rawToken, null, "\"", null);
    }

    public static StringToken ofBacktick(String rawValue) {
        String rawToken = String.format("`%s`", rawValue);
        String value = StringUtil.unescape(rawValue);
        return new StringToken(value, rawToken, null, "`", null);
    }

    public static StringToken ofPrefix(String rawValue, String prefix, char left, char right) {
        String rawToken = String.format("%s%s%s%s", prefix, left, rawValue, right);
        String value = StringUtil.unescape(rawValue);
        return new StringToken(value, rawToken, prefix, String.valueOf(left), String.valueOf(right));
    }

    public static StringToken ofSeparator(String rawValue, String left, String right) {
        String rawToken = String.format("%s%s%s", left, rawValue, right);
        String value = StringUtil.unescape(rawValue);
        return new StringToken(value, rawToken, null, left, right);
    }

    public boolean isSingle() {
        return "'".equals(sep);
    }

    public boolean isDouble() {
        return "\"".equals(sep);
    }

    public boolean isBacktick() {
        return "`".equals(sep);
    }
}
