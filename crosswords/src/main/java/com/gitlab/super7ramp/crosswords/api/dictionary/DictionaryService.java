package com.gitlab.super7ramp.crosswords.api.dictionary;

/**
 * The dictionary service. Mainly queries on the available dictionaries.
 */
public interface DictionaryService {

    /**
     * Lists available dictionary providers.
     */
    void listProviders();

    /**
     * Lists available dictionaries.
     *
     * @param request the request
     */
    void listDictionaries(final ListDictionariesRequest request);

    /**
     * Lists entries for a dictionary.
     *
     * @param request the request
     */
    void listEntries(final ListDictionaryEntriesRequest request);

    /**
     * Shows the preferred dictionary.
     * <p>
     * The criteria used to compare dictionaries are, by order of preference:
     * <ul>
     *     <li>Locale: Dictionary matching system's locale (language + country) is preferred over
     *     one which doesn't;</li>
     *     <li>Language: Dictionary matching system's language is preferred over one which doesn't;
     *     </li>
     *     <li>Type: Dictionary of type "internal" is preferred over one of another type;</li>
     *     <li>Name: Dictionary whose dictionary identifier string representation is smaller (in
     *     lexicographical sense) is preferred. (This is not a relevant criterion, it is used to
     *     guarantee the dictionary list can be completely sorted, assuming that identifiers are
     *     unique.)</li>
     * </ul>
     */
    void showPreferredDictionary();
}
