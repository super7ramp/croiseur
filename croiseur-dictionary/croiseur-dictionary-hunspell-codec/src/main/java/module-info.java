/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Library which allows to read Hunspell dictionaries and generate all word forms from them.
 */
module com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec {
    requires transitive com.gitlab.super7ramp.croiseur.common;
    requires com.gitlab.super7ramp.croiseur.dictionary.common;
    requires java.logging;

    exports com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec;
}