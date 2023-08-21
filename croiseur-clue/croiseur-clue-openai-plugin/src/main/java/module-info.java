/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.clue.openai.plugin.OpenAiClueProvider;
import com.gitlab.super7ramp.croiseur.spi.clue.ClueProvider;

/**
 * A clue provider backed by OpenAI Web API.
 */
module com.gitlab.super7ramp.croiseur.clue.openai.plugin {
    requires com.theokanning.openai;
    requires transitive com.gitlab.super7ramp.croiseur.spi.clue;
    provides ClueProvider with OpenAiClueProvider;
}