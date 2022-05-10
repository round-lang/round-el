package org.dreamcat.round.el;

import lombok.Getter;
import lombok.Setter;
import org.dreamcat.round.el.lex.LexSettings;

/**
 * @author Jerry Will
 * @since 2021-07-08
 */
@Getter
@Setter
public class ElSettings extends LexSettings {

    protected boolean enableExtendedFunction = false;
}
