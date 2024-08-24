/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.clue.openai.plugin;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import re.belv.croiseur.spi.clue.ClueProvider;

/**
 * {@link ClueProvider} implementation for {@link ClueGenerator}.
 */
public final class OpenAiClueProvider implements ClueProvider {

    /** The provider name. */
    private static final String NAME = "OpenAI";

    /** The actual provider, lazily initialized. */
    private ClueGenerator clueGenerator;

    /**
     * Constructs an instance.
     */
    public OpenAiClueProvider() {
        // Nothing to do.
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String description() {
        return ResourceBundle.getBundle("re.belv.croiseur.clue.openai.plugin.Messages")
                .getString("description");
    }

    @Override
    public Map<String, String> define(final Set<String> words) {
        return clueGenerator().generate(words);
    }

    /**
     * Gets the clue generator, creating it on first call.
     *
     * @return the clue generator
     */
    private synchronized ClueGenerator clueGenerator() {
        if (clueGenerator == null) {
            clueGenerator = new ClueGenerator();
        }
        return clueGenerator;
    }
}
