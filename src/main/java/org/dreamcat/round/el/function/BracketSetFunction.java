package org.dreamcat.round.el.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.dreamcat.common.util.ArrayUtil;

/**
 * @author Jerry Will
 * @version 2022-04-11
 */
@SuppressWarnings({"unchecked"})
public enum BracketSetFunction implements ElFunction {
    INSTANCE;

    @Override
    public Object invoke(Object... args) {
        Object object = Objects.requireNonNull(args[0]);
        // only int-based index is supported, no pair
        List<Integer> index = evalIndex(Objects.requireNonNull(args[1]));
        Object value = Objects.requireNonNull(args[2]);

        boolean isArray = object.getClass().isArray();
        int n = index.size();
        int i = 0, offset = -1;
        for (; ; ) {
            offset = index.get(i++);
            if (offset < 0) {
                if (isArray) {
                    offset += ArrayUtil.length(object);
                } else {
                    offset += ((List<?>) object).size();
                }
            }
            if (i >= n) break;
            if (isArray) {
                object = ArrayUtil.get(object, offset);
            } else {
                object = ((List<?>) object).get(offset);
            }
        }
        if (isArray) {
            ArrayUtil.set(object, offset, value);
        } else {
            ((List<Object>) object).set(offset, value);
        }
        return value;
    }

    private List<Integer> evalIndex(Object arg1) {
        if (arg1 instanceof Number) {
            return Collections.singletonList(((Number) arg1).intValue());
        }
        List<?> list = (List<?>) arg1;
        List<Integer> offsets = new ArrayList<>(list.size() << 1);
        for (Object elem : list) {
            offsets.addAll(evalIndex(elem));
        }
        return offsets;
    }
}
