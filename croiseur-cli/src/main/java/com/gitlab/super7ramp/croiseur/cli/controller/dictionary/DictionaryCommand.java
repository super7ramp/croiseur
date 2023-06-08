/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.dictionary;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.croiseur.api.dictionary.ListDictionariesRequest;
import com.gitlab.super7ramp.croiseur.api.dictionary.ListDictionaryEntriesRequest;
import com.gitlab.super7ramp.croiseur.api.dictionary.SearchDictionaryEntriesRequest;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Locale;

/**
 * "dictionary" subcommand: List and print available dictionaries.
 */
@Command(name = "dictionary")
public final class DictionaryCommand {

    /** The dictionary service. */
    private final DictionaryService dictionaryService;

    /**
     * Constructor.
     *
     * @param aDictionaryService the dictionary service
     */
    public DictionaryCommand(final DictionaryService aDictionaryService) {
        dictionaryService = aDictionaryService;
    }

    /**
     * Displays dictionary entries.
     *
     * @param dictionaryId the dictionary identifier
     */
    @Command(name = "cat")
    void cat(
            @Parameters(index = "0", paramLabel = "<PROVIDER:DICTIONARY>") final DictionaryIdentifier dictionaryId) {
        final ListDictionaryEntriesRequest request = ListDictionaryEntriesRequest.of(dictionaryId);
        dictionaryService.listEntries(request);
    }

    /**
     * Displays the default dictionary.
     */
    @Command(name = "get-default")
    void getDefault() {
        dictionaryService.getDefaultDictionary();
    }

    /**
     * Greps the dictionary content.
     */
    @Command(name = "grep", aliases = {"search"})
    void grep(
            @Parameters(index = "0", paramLabel = "<PROVIDER:DICTIONARY>") final DictionaryIdentifier dictionaryId,
            @Parameters(index = "1", paramLabel = "<PATTERN>") final String pattern) {
        final SearchDictionaryEntriesRequest request =
                SearchDictionaryEntriesRequest.of(dictionaryId, pattern);
        dictionaryService.searchEntries(request);
    }

    /**
     * Lists the available dictionary.
     *
     * @param provider filter on the provider
     * @param locale   filter on the locale
     */
    @Command(name = "list", aliases = {"ls"})
    void list(@Option(names = {"-p", "--provider"}, paramLabel = "PROVIDER") final String provider,
              @Option(names = {"-l", "--locale"}, paramLabel = "LOCALE") final Locale locale) {
        final ListDictionariesRequest request = ListDictionariesRequest.of(locale, provider);
        dictionaryService.listDictionaries(request);
    }

    /**
     * Lists the available dictionary providers.
     */
    @Command(name = "list-providers")
    void listProviders() {
        dictionaryService.listProviders();
    }

}
