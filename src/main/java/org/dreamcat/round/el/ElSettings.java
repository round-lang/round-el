package org.dreamcat.round.el;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.round.el.lex.LexSettings;

/**
 * @author Jerry Will
 * @since 2021-07-08
 */
@Getter
@Setter
@Slf4j
public class ElSettings extends LexSettings {

    protected boolean enableExtendedFunction = false;
}
