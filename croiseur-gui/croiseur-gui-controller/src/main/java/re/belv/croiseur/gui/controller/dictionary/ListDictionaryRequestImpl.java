/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.controller.dictionary;

import java.util.Locale;
import java.util.Optional;
import re.belv.croiseur.api.dictionary.ListDictionariesRequest;

/** Implementation of {@link ListDictionariesRequest}. */
final class ListDictionaryRequestImpl implements ListDictionariesRequest {

    /** Constructs an instance. */
    ListDictionaryRequestImpl() {
        // Nothing to do.
    }

    @Override
    public Optional<Locale> locale() {
        return Optional.empty();
    }

    @Override
    public Optional<String> provider() {
        return Optional.empty();
    }
}
