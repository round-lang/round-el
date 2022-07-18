package org.dreamcat.round.el.function;

import static org.dreamcat.round.el.function.ArithmeticOpFunctions.ADD_OP;
import static org.dreamcat.round.el.function.ArithmeticOpFunctions.DIV_OP;
import static org.dreamcat.round.el.function.ArithmeticOpFunctions.DOUBLE_MUL_OP;
import static org.dreamcat.round.el.function.ArithmeticOpFunctions.MUL_OP;
import static org.dreamcat.round.el.function.ArithmeticOpFunctions.REM_OP;
import static org.dreamcat.round.el.function.ArithmeticOpFunctions.SUB_OP;
import static org.dreamcat.round.el.function.BitOpFunctions.BIT_AND_OP;
import static org.dreamcat.round.el.function.BitOpFunctions.BIT_NOT_OP;
import static org.dreamcat.round.el.function.BitOpFunctions.BIT_OR_OP;
import static org.dreamcat.round.el.function.BitOpFunctions.BIT_XOR_OP;
import static org.dreamcat.round.el.function.CompareOpFunctions.EQ_OP;
import static org.dreamcat.round.el.function.CompareOpFunctions.GE_OP;
import static org.dreamcat.round.el.function.CompareOpFunctions.GT_OP;
import static org.dreamcat.round.el.function.CompareOpFunctions.LE_OP;
import static org.dreamcat.round.el.function.CompareOpFunctions.LT_OP;
import static org.dreamcat.round.el.function.CompareOpFunctions.NE_OP;
import static org.dreamcat.round.el.function.LogicOpFunctions.AND_OP;
import static org.dreamcat.round.el.function.LogicOpFunctions.NOT_OP;
import static org.dreamcat.round.el.function.LogicOpFunctions.OR_OP;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.dreamcat.round.el.lex.OperatorToken;

/**
 * @author Jerry Will
 * @version 2021-07-25
 */
public final class ElFunctions {

    private ElFunctions() {
    }

    static final Map<OperatorToken, ElFunction> _operators = new EnumMap<>(OperatorToken.class);
    static final Map<String, ElFunction> _functions = new HashMap<>();

    public static final Map<String, ElFunction> FUNCTIONS = Collections.unmodifiableMap(_functions);
    public static final Map<OperatorToken, ElFunction> OPERATORS = Collections.unmodifiableMap(_operators);

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    static {
        // op
        register(OperatorToken.ADD, ADD_OP);
        register(OperatorToken.SUB, SUB_OP);
        register(OperatorToken.MUL, MUL_OP);
        register(OperatorToken.DOUBLE_MUL, DOUBLE_MUL_OP);
        register(OperatorToken.DIV, DIV_OP);
        register(OperatorToken.REM, REM_OP);

        register(OperatorToken.BIT_AND, BIT_AND_OP);
        register(OperatorToken.BIT_OR, BIT_OR_OP);
        register(OperatorToken.BIT_XOR, BIT_XOR_OP);
        register(OperatorToken.BIT_NOT, BIT_NOT_OP);

        register(OperatorToken.AND, AND_OP);
        register(OperatorToken.OR, OR_OP);
        register(OperatorToken.NOT, NOT_OP);

        register(OperatorToken.EQ, EQ_OP);
        register(OperatorToken.NE, NE_OP);
        register(OperatorToken.LT, LT_OP);
        register(OperatorToken.LE, LE_OP);
        register(OperatorToken.GT, GT_OP);
        register(OperatorToken.GE, GE_OP);

        register(OperatorToken.DOT, DotElFunction.INSTANCE);

        // infix
        register(OperatorToken.AND.getName(), AND_OP);
        register(OperatorToken.OR.getName(), OR_OP);
        register(OperatorToken.NOT.getName(), NOT_OP);

        register(OperatorToken.EQ.getName(), EQ_OP);
        register(OperatorToken.NE.getName(), NE_OP);
        register(OperatorToken.LT.getName(), LT_OP);
        register(OperatorToken.LE.getName(), LE_OP);
        register(OperatorToken.GT.getName(), GT_OP);
        register(OperatorToken.GE.getName(), GE_OP);

        register(InElFunction.KEYWORD, InElFunction.INSTANCE);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    static void register(String name, ElFunction function) {
        _functions.put(name, function);
    }

    static void register(OperatorToken operator, ElFunction function) {
        _operators.put(operator, function);
    }
}
