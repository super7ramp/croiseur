/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Library which allows to read and write dictionaries in a custom XML format.
 */
module com.gitlab.super7ramp.croiseur.dictionary.xml.codec {
    requires java.logging;
    requires java.xml;
    exports com.gitlab.super7ramp.croiseur.dictionary.xml.codec;
}