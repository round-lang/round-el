package org.dreamcat.round.el.comparison;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import ognl.Node;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.jexl3.internal.Engine;
import org.dreamcat.common.eval.EvalContext;
import org.dreamcat.common.eval.EvalEngine;
import org.dreamcat.common.eval.EvalExpression;
import org.dreamcat.common.util.NumberUtil;
import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.el.ElString;

/**
 * @author Jerry Will
 * @since 2021-07-08
 */
public abstract class ElTestBase {

    static final JexlEngine jexlEngine = new Engine();
    static final OgnlContext ognlContextDefault = (OgnlContext) Ognl.createDefaultContext(null);
    static final ExpressRunner expressRunner = new ExpressRunner();
    static final ElEngine elEngine = ElEngine.getEngine();
    static final EvalEngine evalEngine = EvalEngine.getEngine();

    public abstract String getExpression();

    public abstract Map<String, Object> getContext();

    public abstract boolean isNum();

    final JexlExpression jexlExpression;
    final JexlContext jexlContext = new MapContext();

    final Expression aviatorExpression;
    final Map<String, Object> aviatorEnv = new HashMap<>();

    final Node node;
    final Map<String, Object> ognlContext = new HashMap<>();

    final DefaultContext<String, Object> qlExpressContext = new DefaultContext<>();

    final ElString elString;
    final ElContext elContext = ElContext.create();

    EvalExpression evalExpression;
    final EvalContext evalContext = EvalContext.createContext();

    public ElTestBase() {
        String expression = getExpression();
        jexlExpression = jexlEngine.createExpression(expression);
        aviatorExpression = AviatorEvaluator.compile(expression);
        try {
            node = (Node) Ognl.parseExpression(expression);
        } catch (OgnlException e) {
            throw new RuntimeException(e);
        }
        elString = elEngine.compile(expression);
        if (isNum()) {
            evalExpression = evalEngine.createExpression(expression);
        }
        evalExpression = evalEngine.createExpression(expression);

        Map<String, Object> ctx = getContext();
        ctx.forEach(jexlContext::set);
        aviatorEnv.putAll(ctx);
        ognlContext.putAll(ctx);
        qlExpressContext.putAll(ctx);

        ctx.forEach(elContext::set);
        ctx.forEach((k ,v) -> {
            if (!(v instanceof Number)) return;
            Number n = (Number) v;
            if (n instanceof BigDecimal){
                evalContext.set(k, (BigDecimal) n);
            } else if (NumberUtil.isFloatLike(n)){
                evalContext.set(k, n.doubleValue());
            } else {
                evalContext.set(k, n.longValue());
            }
        });
    }

}
