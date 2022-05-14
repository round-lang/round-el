package org.dreamcat.round.el.lex;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.dreamcat.common.Pair;

/**
 * @author Jerry Will
 * @since 2021-07-08
 */
@Getter
@Setter
@Accessors(chain = true)
public class LexSettings {

    /**
     * use in help information
     */
    protected String name = "expression";
    /**
     * identify the number token as {@link java.math.BigDecimal} or {@link java.math.BigInteger}
     */
    protected boolean enableBigNumber = false;
    /**
     * single line comment
     */
    protected List<String> singleComments = Collections.singletonList("//");
    /**
     * multiple line comment
     */
    protected List<Pair<String, String>> multipleComments = Collections.singletonList(Pair.of("/*", "*/"));
    /**
     * sample char count when throw a wrong syntax exception
     */
    protected int sampleCharCount = 1 << 8; // set to <=0 to disable it
}
