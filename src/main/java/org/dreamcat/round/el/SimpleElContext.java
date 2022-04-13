package org.dreamcat.round.el;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
class SimpleElContext implements ElContext {

    // private final Map<String, String> symbols = new HashMap<>();
    private final Map<String, Object> variables = new HashMap<>();

    @Override
    public Object get(String name) {
        return variables.get(name);
    }

    @Override
    public void set(String name, Object value) {
        variables.put(name, value);
    }

    @Override
    public Set<String> names() {
        return variables.keySet();
    }
}
