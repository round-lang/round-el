package org.dreamcat.round.el.lex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dreamcat.common.Pair;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
@Getter
@RequiredArgsConstructor
public enum OperatorToken implements Token {
    ASSIGN("=", 15, false),
    IDENTIFIER("?", 14), // treat identifier as a very low priority
    OR("||", "or", 13),
    AND("&&", "and", 12),
    BIT_OR("|", 11),
    BIT_XOR("^", 10),
    BIT_AND("&", 9),
    BIT_NOT("~", 8),
    EQ("==", "eq", 7),
    NE("!=", "ne", 7),
    GT(">", "gt", 6),
    GE(">=", "ge", 6),
    LT("<", "lt", 6),
    LE("<=", "le", 6),
    ADD("+", 4),
    SUB("-", 4),
    MUL("*", 3),
    DOUBLE_MUL("**", 3),
    DIV("/", 3),
    REM("%", 3),
    NOT("!", "not", 2),
    BRACKET("[]", 1), // treat [] as a very high priority
    DOT(".", 1),
    DOUBLE_DOT("..", 1),
    DOUBLE_COLON("::", 1),
    ;

    final String raw;
    final String name; // as function name
    final int priority;
    final boolean lefty;

    OperatorToken(String raw, int priority, boolean lefty) {
        this(raw, null, priority, lefty);
    }

    OperatorToken(String raw, String name, int priority) {
        this(raw, name, priority, true);
    }

    OperatorToken(String raw, int priority) {
        this(raw, null, priority);
    }

    @Override
    public Type getType() {
        return Type.OPERATOR;
    }

    @Override
    public boolean isOperator() {
        return true;
    }

    @Override
    public OperatorToken getOperator() {
        return this;
    }

    @Override
    public boolean isDot() {
        return DOT.equals(this);
    }

    public boolean comparePriority(OperatorToken other) {
        int cmp = other.priority - priority;
        if (cmp != 0) return cmp > 0;
        return lefty;
    }

    public boolean isShortCircuit() {
        return equals(AND) || equals(OR);
    }

    public static Pair<OperatorToken, Integer> search(String sql, int offset) {
        char c = sql.charAt(offset);
        int size = sql.length();
        if (c == '.') {
            if (offset < size - 1 && sql.charAt(offset + 1) == '.') {
                return Pair.of(DOUBLE_DOT, offset + 2);
            } else {
                return Pair.of(DOT, offset + 1);
            }
        } else if (c == '+') {
            return Pair.of(ADD, offset + 1);
        } else if (c == '-') {
            return Pair.of(SUB, offset + 1);
        } else if (c == '*') {
            if (offset < size - 1 && sql.charAt(offset + 1) == '*') {
                return Pair.of(DOUBLE_MUL, offset + 2);
            } else {
                return Pair.of(MUL, offset + 1);
            }
        } else if (c == '/') {
            return Pair.of(DIV, offset + 1);
        } else if (c == '%') {
            return Pair.of(REM, offset + 1);
        } else if (c == '=') {
            if (offset < size - 1 && sql.charAt(offset + 1) == '=') {
                return Pair.of(EQ, offset + 2);
            } else {
                return Pair.of(ASSIGN, offset + 1);
            }
        } else if (c == '&') {
            if (offset < size - 1 && sql.charAt(offset + 1) == '&') {
                return Pair.of(AND, offset + 2);
            } else {
                return Pair.of(BIT_AND, offset + 1);
            }
        } else if (c == '|') {
            if (offset < size - 1 && sql.charAt(offset + 1) == '|') {
                return Pair.of(OR, offset + 2);
            } else {
                return Pair.of(BIT_OR, offset + 1);
            }
        } else if (c == '!') {
            if (offset < size - 1 && sql.charAt(offset + 1) == '=') {
                return Pair.of(NE, offset + 2);
            } else {
                return Pair.of(NOT, offset + 1);
            }
        } else if (c == '<') {
            if (offset < size - 1 && sql.charAt(offset + 1) == '=') {
                return Pair.of(LE, offset + 2);
            } else {
                return Pair.of(LT, offset + 1);
            }
        } else if (c == '>') {
            if (offset < size - 1 && sql.charAt(offset + 1) == '=') {
                return Pair.of(GE, offset + 2);
            } else {
                return Pair.of(GT, offset + 1);
            }
        } else if (c == '^') {
            return Pair.of(BIT_XOR, offset + 1);
        } else if (c == '~') {
            return Pair.of(BIT_NOT, offset + 1);
        } else {
            return null;
        }
    }
}
