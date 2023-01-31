/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Tools for manipulating dictionaries.
 */
module com.gitlab.super7ramp.croiseur.dictionary.tools {
    requires com.gitlab.super7ramp.croiseur.dictionary.common;
    requires com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec;
    requires com.gitlab.super7ramp.croiseur.dictionary.xml.codec;
    requires java.logging;

    exports com.gitlab.super7ramp.croiseur.dictionary.tools;
}