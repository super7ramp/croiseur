/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.clue.openai.plugin.OpenAiClueProvider;
import re.belv.croiseur.spi.clue.ClueProvider;

/**
 * A clue provider backed by OpenAI Web API.
 */
module re.belv.croiseur.clue.openai.plugin {
    requires com.azure.ai.openai;
    requires transitive re.belv.croiseur.spi.clue;
    provides ClueProvider with OpenAiClueProvider;
}