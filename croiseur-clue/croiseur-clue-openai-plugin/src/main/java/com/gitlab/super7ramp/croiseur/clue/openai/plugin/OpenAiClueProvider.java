/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.clue.openai.plugin;

import com.gitlab.super7ramp.croiseur.spi.clue.ClueProvider;

import java.util.List;
import java.util.Map;

/**
 * {@link ClueProvider} implementation for {@link ClueGenerator}.
 */
public final class OpenAiClueProvider implements ClueProvider {

    /** The provider name. */
    private static final String NAME = "OpenAI";

    /** The provider description. */
    private static final String DESCRIPTION = "Clue generator backed by OpenAI's ChatGPT service";

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
        return DESCRIPTION;
    }

    @Override
    public Map<String, String> define(final List<String> words) {
        // TODO Cache generator instance in a lazily evaluated field
        return new ClueGenerator().generate(words);
    }
}
