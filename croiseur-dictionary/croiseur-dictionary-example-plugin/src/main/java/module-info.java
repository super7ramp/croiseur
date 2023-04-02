/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.dictionary.example.plugin.ExampleDictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;

/**
 * An example dictionary provider plugin.
 */
module com.gitlab.super7ramp.croiseur.dictionary.example.plugin {
    requires com.gitlab.super7ramp.croiseur.spi.dictionary;
    provides DictionaryProvider with ExampleDictionaryProvider;
}