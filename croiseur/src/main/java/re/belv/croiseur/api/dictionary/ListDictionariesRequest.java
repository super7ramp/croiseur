/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.api.dictionary;

import java.util.Locale;
import java.util.Optional;

/** A request to list available dictionaries. */
public interface ListDictionariesRequest {

    /**
     * Creates a new {@link ListDictionaryEntriesRequest}.
     *
     * @param locale the locale to filter dictionaries with or @{code null} if no filter desired
     * @param provider the provider name to filter dictionaries with or {@code null} if no filter desired
     * @return a new {@link ListDictionaryEntriesRequest}
     */
    static ListDictionariesRequest of(final Locale locale, final String provider) {
        return new ListDictionariesRequest() {
            @Override
            public Optional<Locale> locale() {
                return Optional.ofNullable(locale);
            }

            @Override
            public Optional<String> provider() {
                return Optional.ofNullable(provider);
            }
        };
    }

    /**
     * If present, only shows dictionaries for this locale.
     *
     * @return an optional locale
     */
    Optional<Locale> locale();

    /**
     * If present, only shows dictionaries from this provider.
     *
     * @return an optional provider
     */
    Optional<String> provider();
}
