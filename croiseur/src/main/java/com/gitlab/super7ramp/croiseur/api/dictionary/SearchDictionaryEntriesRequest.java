/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.dictionary;

import java.util.Objects;

/**
 * A request to search a given dictionary for words matching the given expression.
 */
public interface SearchDictionaryEntriesRequest {

    /**
     * Creates a new {@link SearchDictionaryEntriesRequest} from given information.
     *
     * @param dictionaryIdentifier the dictionary identifier
     * @param searchExpression     the search expression; The given expression shall be a valid
     *                             regular expression
     * @return a new {@link SearchDictionaryEntriesRequest}
     */
    static SearchDictionaryEntriesRequest of(final DictionaryIdentifier dictionaryIdentifier,
                                             final String searchExpression) {
        Objects.requireNonNull(dictionaryIdentifier);
        Objects.requireNonNull(searchExpression);
        return new SearchDictionaryEntriesRequest() {

            @Override
            public DictionaryIdentifier dictionaryIdentifier() {
                return dictionaryIdentifier;
            }

            @Override
            public String searchExpression() {
                return searchExpression;
            }
        };
    }

    /**
     * Returns the dictionary identifier.
     *
     * @return the dictionary identifier
     */
    DictionaryIdentifier dictionaryIdentifier();

    /**
     * Returns the search expression.
     * <p>
     * The given expression shall be a valid regular expression.
     *
     * @return the search expression
     * @see java.util.regex.Pattern
     */
    String searchExpression();

}
