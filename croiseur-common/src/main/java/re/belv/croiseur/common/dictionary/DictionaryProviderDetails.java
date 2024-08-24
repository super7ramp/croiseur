/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.common.dictionary;

/**
 * Details about a dictionary provider.
 *
 * @param name the name of this dictionary provider
 * @param description a short description of this dictionary provider
 */
public record DictionaryProviderDetails(String name, String description) {
    // Nothing to add.
}
