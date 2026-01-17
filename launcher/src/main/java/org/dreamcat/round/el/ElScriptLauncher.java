package org.dreamcat.round.el;

import org.dreamcat.common.io.FileUtil;
import org.dreamcat.common.json.JSON;
import org.dreamcat.common.util.ExceptionUtil;
import org.dreamcat.common.util.ReflectUtil;

import java.io.File;
import java.math.BigDecimal;

/**
 * @author Jerry Will
 * @version 2026-01-17
 */
public class ElScriptLauncher {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("require <script_file>");
            return;
        }
        File file = new File(args[0]);
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            System.err.println("script file not exists or can't read");
            return;
        }
        String code;
        try {
            code = FileUtil.readAsString(file);
        } catch (Exception e) {
            System.err.println("read script file error: " + ExceptionUtil.getRootCause(e).getMessage());
            return;
        }
        ElEngine engine = ElEngine.getTypicalEngine();

        ElContext context = ElContext.create();
        System.getenv().forEach((k ,v) -> {
            Object value = v;
            try {
                value = new BigDecimal(v);
            } catch (Exception ignored) {}
            context.set(k, value);
        });

        Object result = engine.evaluate(code, context);
        if (result == null || ReflectUtil.isFlat(result.getClass())) {
            System.out.println(result);
            return;
        }
        System.out.println(JSON.stringify(result));
    }
}
