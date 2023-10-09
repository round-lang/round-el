package org.dreamcat.round.el.ast;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.LongPredicate;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.math.MathUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-08-15
 */
@Slf4j
class AnalyzerTest extends NodeTest {

    @Test
    void test() throws Exception {
        BigInteger nano = BigInteger.valueOf(System.nanoTime()).divide(BigInteger.valueOf(1000_000_000));
        System.out.println("nano=" + nano);

        String s = "2021-05-25 00:00:00";
        evalNode(el(6),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s).getTime(),
                "s", s);

        evalNode(el(7), null,
                it -> sqrtAndTrunc(((List<?>) it).get(0)).equals(nano),
                "s", s);
    }

    String el(int lineNo) {
        String expression = expr("el.txt");
        String[] lines = expression.split("\n");
        if (lines.length > lineNo) {
            expression = String.join("\n", Arrays.asList(lines).subList(0, lineNo));
        }
        return expression;
    }

    public BigInteger sqrtAndTrunc(Object v) {
        return MathUtil.sqrt((BigInteger) v).divide(BigInteger.valueOf(1000_000_000));
    }

}
