package org.dreamcat.round.el.util;

/**
 * @author Jerry Will
 * @version 2021-08-22
 */
public final class ElClassUtil {

    private ElClassUtil() {
    }

    public static Class<?> findBuiltinClass(String className) {
        try {
            return Class.forName("java.lang." + className);
        } catch (ClassNotFoundException ignore) { // nop
        }
        try {
            return Class.forName("java.util." + className);
        } catch (ClassNotFoundException ignore) { // nop
        }
        return null;
    }

}
