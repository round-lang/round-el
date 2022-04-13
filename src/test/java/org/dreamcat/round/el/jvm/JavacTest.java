package org.dreamcat.round.el.jvm;

import java.io.IOException;
import org.dreamcat.common.io.ClassPathUtil;
import org.dreamcat.common.io.FileUtil;
import org.dreamcat.common.text.DollarInterpolation;
import org.dreamcat.common.util.MapUtil;
import org.dreamcat.common.util.StringUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-07-31
 */
class JavacTest {

    @Test
    void huge_dot() throws IOException {
        String t = ClassPathUtil.getResourceAsString("huge_dot.txt");
        String code = StringUtil.repeatJoin("a", 2200, ".");
        String s = DollarInterpolation.format(t, MapUtil.of("code", code));
        FileUtil.writeFrom(System.getenv("HOME") + "/Downloads/HugeDot.java", s);
        System.out.println("javac HugeDot.java");
        System.out.println("javap -p -v HugeDot");
    }

    @Test
    void hug_brace() throws IOException {
        String t = ClassPathUtil.getResourceAsString("huge_brace.txt");

        StringBuilder sb = new StringBuilder();
        int size = 500;
        for (int i = 0; i < size; i++) {
            sb.append("1 + (");
        }
        sb.append("1 + a").append(StringUtil.repeat(')', size));
        String code = sb.toString();

        String s = DollarInterpolation.format(t, MapUtil.of("code", code));
        FileUtil.writeFrom(System.getenv("HOME") + "/Downloads/HugeBrace.java", s);
        System.out.println("javac HugeBrace.java");
        System.out.println("javap -p -v HugeBrace");
    }

}
