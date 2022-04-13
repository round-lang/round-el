package org.dreamcat.round.el.lex;

import org.dreamcat.round.el.ElSettings;
import org.dreamcat.round.el.TestBase;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-08-15
 */
class LexerTest extends TestBase {

    @Test
    void test() {
        String expression = expr("el.txt");
        Lexer lexer = new Lexer(new ElSettings());
        TokenStream stream = lexer.lex(expression);
        while (stream.hasNext()) {
            System.out.println(stream.next());
        }
    }
}
