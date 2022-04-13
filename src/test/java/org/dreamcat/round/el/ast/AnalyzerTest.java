package org.dreamcat.round.el.ast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.LongPredicate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-08-15
 */
@Slf4j
class AnalyzerTest extends NodeTest {

    @Test
    void test() throws Exception {
        LongPredicate nano = it -> it / 10_000_000 == System.nanoTime() / 10_000_000;
        // evalNode(el(5), null,
        //         it -> nano.test((long) it));

        String s = "2021-05-25 00:00:00";
        evalNode(el(6),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s).getTime(),
                "s", s);

        evalNode(el(7), null,
                it -> nano.test((Long) (((List<?>) it).get(0))),
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
}
