package org.dreamcat.round.el.util;

import org.dreamcat.common.util.ReflectUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2022-05-10
 */
class ElClassUtilTest {

    @Test
    void test() {
        ReflectUtil.retrievePackage("org.dreamcat.round.el")
                .forEach(System.out::println);
    }
}
