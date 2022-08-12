package org.dreamcat.round.el;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Jerry Will
 * @since 2021-07-08
 */
@Getter
@Setter
@Accessors(chain = true)
public class ElConfig {

    // compile
    private boolean enableBigNumber = true;
    private int sampleCharCount;

    // evaluate
    private boolean enableExtendedFunction = false;
}
