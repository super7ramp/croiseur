/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import java.util.Locale;

/**
 * Key information to identify a dictionary.
 *
 * @param provider dictionary provider
 * @param name dictionary name
 * @param locale dictionary locale
 */
public record DictionaryKey(String provider, String name, Locale locale) {
    // Nothing to add
}
