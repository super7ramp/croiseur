/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.dictionary;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDescription;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;

import java.util.Locale;
import java.util.Map;

/**
 * Datatable and parameter types pertaining to dictionary service.
 */
public final class DictionaryTypes {

    /**
     * Constructs an instance.
     */
    public DictionaryTypes() {
        // Nothing to do.
    }

    @ParameterType(name = "locale", value = "[^ ]+")
    public Locale locale(final String languageTag) {
        return Locale.forLanguageTag(languageTag);
    }

    @DataTableType
    public DictionaryProviderDescription dictionaryProviderDescription(
            final Map<String, String> entry) {
        return new DictionaryProviderDescription(entry.get("Provider"), entry.get("Description"));
    }

    @DataTableType
    public ProvidedDictionaryDescription providedDictionaryDescription(
            final Map<String, String> entry) {
        final String providerName = entry.get("Provider");
        final DictionaryDescription dictionaryDescription =
                new DictionaryDescription(entry.get("Name"),
                        Locale.forLanguageTag(entry.get("Locale")));
        return new ProvidedDictionaryDescription(providerName, dictionaryDescription);
    }
}
