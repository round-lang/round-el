package org.dreamcat.round.el;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.dreamcat.round.el.lex.LexSettings;

/**
 * @author Jerry Will
 * @since 2021-07-08
 */
@Getter
@Setter
@Accessors(chain = true)
public class ElSettings extends LexSettings {

    protected boolean enableExtendedFunction = false;
}
