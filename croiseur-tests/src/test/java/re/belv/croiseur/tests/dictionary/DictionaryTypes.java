/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.tests.dictionary;

import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import java.util.Locale;
import java.util.Map;
import re.belv.croiseur.common.dictionary.DictionaryDetails;
import re.belv.croiseur.common.dictionary.DictionaryProviderDetails;
import re.belv.croiseur.common.dictionary.ProvidedDictionaryDetails;

/** Datatable and parameter types pertaining to dictionary service. */
public final class DictionaryTypes {

    /** Constructs an instance. */
    public DictionaryTypes() {
        // Nothing to do.
    }

    @ParameterType(name = "locale", value = "[^ ]+")
    public Locale locale(final String languageTag) {
        return Locale.forLanguageTag(languageTag);
    }

    @DataTableType
    public DictionaryProviderDetails dictionaryProviderDetails(final Map<String, String> entry) {
        return new DictionaryProviderDetails(entry.get("Provider"), entry.get("Description"));
    }

    @DataTableType
    public ProvidedDictionaryDetails providedDictionaryDetails(final Map<String, String> entry) {
        final String providerName = entry.get("Provider");
        final DictionaryDetails dictionaryDetails = new DictionaryDetails(
                entry.get("Name"), Locale.forLanguageTag(entry.get("Locale")), entry.get("Description"));
        return new ProvidedDictionaryDetails(providerName, dictionaryDetails);
    }
}
