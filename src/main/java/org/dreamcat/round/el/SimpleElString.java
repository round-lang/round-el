package org.dreamcat.round.el;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dreamcat.round.el.ast.ElNode;
import org.dreamcat.round.el.lex.Lexer;
import org.dreamcat.round.el.lex.TokenStream;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class SimpleElString implements ElString {

    final String expression;
    final ElEngine engine;
    final ElNode root;

    public SimpleElString(String expression, ElEngine engine) {
        Lexer lexer = new Lexer(engine.getSettings());
        TokenStream stream = lexer.lex(expression);

        this.expression = expression;
        this.engine = engine;
        this.root = ElNode.compile(stream);
    }

    @Override
    public ElString optimize(ElContext context) {
        ElNode newRoot = root.copy();
        newRoot.optimize();
        return new SimpleElString(expression, engine, newRoot);
    }

    @Override
    public Object evaluate(ElContext context) {
        return root.evaluate(context, engine);
    }
}
