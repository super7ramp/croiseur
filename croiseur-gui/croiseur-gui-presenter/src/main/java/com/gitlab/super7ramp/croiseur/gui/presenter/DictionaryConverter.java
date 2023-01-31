/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.presenter;

import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDescription;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionaryViewModel;

import java.util.Locale;

/**
 * Converts from/to view model types.
 */
final class DictionaryConverter {

    /**
     * Private constructor, static utilities only.
     */
    private DictionaryConverter() {
        // Nothing to do.
    }

    /**
     * Converts a {@link ProvidedDictionaryDescription} into a {@link DictionaryViewModel}.
     *
     * @param providedDictionary a {@link ProvidedDictionaryDescription}
     * @return a {@link DictionaryViewModel}
     */
    static DictionaryViewModel toViewModelType(final ProvidedDictionaryDescription providedDictionary) {
        final String provider = providedDictionary.provider().name();
        final Locale locale = providedDictionary.dictionary().locale();
        final String dictionaryName = providedDictionary.dictionary().name();
        return new DictionaryViewModel(locale, provider, dictionaryName);
    }

}
