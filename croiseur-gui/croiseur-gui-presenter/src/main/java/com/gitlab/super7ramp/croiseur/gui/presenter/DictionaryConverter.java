/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.presenter;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryDetails;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDetails;
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
     * Converts a {@link ProvidedDictionaryDetails} into a {@link DictionaryViewModel}.
     *
     * @param providedDictionary a {@link ProvidedDictionaryDetails}
     * @return a {@link DictionaryViewModel}
     */
    static DictionaryViewModel toViewModelType(final ProvidedDictionaryDetails providedDictionary) {
        final String providerName = providedDictionary.providerName();
        final DictionaryDetails dictionaryDetails = providedDictionary.dictionary();
        final Locale locale = dictionaryDetails.locale();
        final String dictionaryName = dictionaryDetails.name();
        final String dictionaryDescription = dictionaryDetails.description();
        return new DictionaryViewModel(locale, providerName, dictionaryName, dictionaryDescription);
    }

}
