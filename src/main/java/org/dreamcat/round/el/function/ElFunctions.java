package org.dreamcat.round.el.function;

import static org.dreamcat.common.util.RandomUtil.choose;
import static org.dreamcat.common.util.RandomUtil.rand;
import static org.dreamcat.common.util.RandomUtil.randi;
import static org.dreamcat.common.util.RandomUtil.uuid32;
import static org.dreamcat.common.util.RandomUtil.uuid36;
import static org.dreamcat.round.el.function.ArithmeticOpFunctions.ADD_OP;
import static org.dreamcat.round.el.function.ArithmeticOpFunctions.DIV_OP;
import static org.dreamcat.round.el.function.ArithmeticOpFunctions.DOUBLE_MUL_OP;
import static org.dreamcat.round.el.function.ArithmeticOpFunctions.MUL_OP;
import static org.dreamcat.round.el.function.ArithmeticOpFunctions.POS_OP;
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

import org.dreamcat.common.Pair;
import org.dreamcat.common.text.InterpolationUtil;
import org.dreamcat.common.util.DateUtil;
import org.dreamcat.common.util.MapUtil;
import org.dreamcat.common.util.RandomUtil;
import org.dreamcat.round.lex.OperatorToken;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    static {
        // op
        register(OperatorToken.ADD, ADD_OP);
        register(OperatorToken.SUB, SUB_OP);
        register(OperatorToken.MUL, MUL_OP);
        register(OperatorToken.DOUBLE_MUL, DOUBLE_MUL_OP);
        register(OperatorToken.DIV, DIV_OP);
        register(OperatorToken.REM, REM_OP);
        register(OperatorToken.DOUBLE_ADD, POS_OP);
        register(OperatorToken.DOUBLE_SUB, POS_OP);

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

    static void register(OperatorToken operator, ElFunction function) {
        _operators.put(operator, function);
    }

    static void register(String name, ElFunction function) {
        _functions.put(name, function);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    static final Map<String, ElFunction> _typical_functions = new HashMap<>();

    public static final Map<String, ElFunction> TYPICAL_FUNCTIONS = Collections.unmodifiableMap(_typical_functions);

    static {
        // print
        registerTypical("print", args -> {
            for (Object arg : args) {
                System.out.print(arg);
            }
            return null;
        });
        registerTypical("printf", args -> {
            if (args.length == 0) throw new IllegalArgumentException("printf: no format string");
            System.out.printf(String.valueOf(args[0]) , (Object[])
                    Arrays.copyOfRange(args, 1, args.length));
            return null;
        });
        registerTypical("println", args -> {
            if (args.length == 0) System.out.println();
            else if (args.length == 1) System.out.println(args[0]);
            else System.out.println(Arrays.toString(args));
            return null;
        });
        registerTypical("interpolate", args -> {
            if (args.length == 0) throw new IllegalArgumentException("interpolate: no format string");
            else if (args.length == 1) return String.valueOf(args[0]);
            return InterpolationUtil.format(String.valueOf(args[0]),
                    MapUtil.of(Arrays.copyOfRange(args, 1, args.length)));
        });
        registerTypical("printi", args -> {
            if (args.length == 0) throw new IllegalArgumentException("printi: no format string");
            else if (args.length == 1) {
                System.out.print(args[0]);
            } else {
                System.out.print(InterpolationUtil.format(String.valueOf(args[0]),
                        MapUtil.of(Arrays.copyOfRange(args, 1, args.length))));
            }
            return null;
        });

        // random
        registerTypical("rand", args -> {
            if (args.length == 0) return rand();
            else if (args.length == 1) return rand(((Number) args[0]).doubleValue());
            else return rand(((Number) args[0]).doubleValue(), ((Number) args[1]).doubleValue());
        });
        registerTypical("randi", args -> {
            if (args.length == 0) return randi(2); // 1 or 0
            else if (args.length == 1) return randi(((Number) args[0]).longValue());
            else return randi(((Number) args[0]).longValue(), ((Number) args[1]).longValue());
        });
        registerTypical("choose", args -> {
            if (args.length != 2) throw new IllegalArgumentException("choose: missing count or chars");
            else return choose(((Number) args[0]).intValue(), String.valueOf(args[1]));
        });
        registerTypical("uuid", args -> UUID.randomUUID());
        registerTypical("uuid32", args -> uuid32());
        registerTypical("uuid36", args -> uuid36());

        // collection ops
        registerTypical("reverse", args -> {
            if (args.length == 0 || !(args[0] instanceof Collection)) {
                throw new IllegalArgumentException("reverse: no collection passed");
            }
            List<?> list = new ArrayList<>((Collection<?>) args[0]);
            Collections.reverse(list);
            return list;
        });

        // time
        registerTypical("to_timestamp", args -> {
            if (args.length == 0) throw new IllegalArgumentException("to_timestamp: no datetime string");
            String arg = String.valueOf(args[0]);
            return DateUtil.parse(arg).getTime();
        });
        registerTypical("to_unix_timestamp", args -> {
            if (args.length == 0) throw new IllegalArgumentException("to_unix_timestamp: no datetime string");
            String arg = String.valueOf(args[0]);
            return DateUtil.parse(arg).getTime() / 1000;
        });
        registerTypical("parse_local_datetime", args -> {
            if (args.length == 0) throw new IllegalArgumentException("parse_local_datetime: no datetime string");
            String arg = String.valueOf(args[0]);
            return DateUtil.parseLocalDateTime(arg);
        });
        registerTypical("date_format", args -> {
            if (args.length == 0) {
                return DateUtil.formatNow();
            }
            Object time = args[0];
            if (time instanceof Date || time instanceof Number || time instanceof String) {
                Date date;
                if (time instanceof Number) {
                    date = new Date(((Number) time).longValue());
                } else if (time instanceof String) {
                    date = DateUtil.parse((String) time);
                } else {
                    date = (Date) time;
                }
                if (args.length >= 2) {
                    SimpleDateFormat fmt = new SimpleDateFormat(String.valueOf(args[1]));
                    return fmt.format(date);
                } else {
                    return DateUtil.format(date);
                }
            } else if (time instanceof TemporalAccessor) {
                if (args.length >= 2) {
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern(String.valueOf(args[1]));
                    return fmt.format((TemporalAccessor) time);
                } else {
                    return DateTimeFormatter.ISO_DATE_TIME.format((TemporalAccessor) time);
                }
            } else if (time == null) {
                return null;
            } else {
                throw new IllegalArgumentException("date_format: unsupported time type " + time.getClass());
            }
        });
    }

    static void registerTypical(String name, ElFunction function) {
        _typical_functions.put(name, function);
    }
}
