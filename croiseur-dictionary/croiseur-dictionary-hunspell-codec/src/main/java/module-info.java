/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Library to generate all word forms from Hunspell dictionaries.
 */
module com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec {
    requires transitive com.gitlab.super7ramp.croiseur.common;
    requires com.gitlab.super7ramp.croiseur.dictionary.common;
    exports com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec;

    requires java.logging;
}