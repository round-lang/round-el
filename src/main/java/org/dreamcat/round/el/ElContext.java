package org.dreamcat.round.el;

import java.util.Map;
import java.util.Set;


/**
 * Create by tuke on 2020/10/26
 */
public interface ElContext {

    static ElContext createContext() {
        return new SimpleElContext();
    }

    static ElContext createContext(Map<String, Object> m) {
        SimpleElContext context = new SimpleElContext();
        m.forEach(context::set);
        return context;
    }

    Object get(String name);

    void set(String name, Object value);

    Set<String> names();
}
