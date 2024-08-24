/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.controller.dictionary;

import java.util.Locale;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import re.belv.croiseur.api.dictionary.DictionaryIdentifier;
import re.belv.croiseur.api.dictionary.DictionaryService;
import re.belv.croiseur.api.dictionary.ListDictionariesRequest;
import re.belv.croiseur.api.dictionary.ListDictionaryEntriesRequest;
import re.belv.croiseur.api.dictionary.SearchDictionaryEntriesRequest;
import re.belv.croiseur.cli.status.Status;

/** "dictionary" subcommand: List and print available dictionaries. */
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
     * @return the error status
     */
    @Command
    int cat(@Parameters(index = "0", paramLabel = "PROVIDER:DICTIONARY") final DictionaryIdentifier dictionaryId) {
        final ListDictionaryEntriesRequest request = ListDictionaryEntriesRequest.of(dictionaryId);
        dictionaryService.listEntries(request);
        return Status.getAndReset();
    }

    /**
     * Displays the default dictionary.
     *
     * @return the error status
     */
    @Command(name = "get-default")
    int getDefault() {
        dictionaryService.getDefaultDictionary();
        return Status.getAndReset();
    }

    /**
     * Greps the dictionary content.
     *
     * @return the error status
     */
    @Command(aliases = {"search"})
    int grep(
            @Parameters(index = "0", paramLabel = "PROVIDER:DICTIONARY") final DictionaryIdentifier dictionaryId,
            @Parameters(index = "1", paramLabel = "PATTERN") final String pattern) {
        final SearchDictionaryEntriesRequest request = SearchDictionaryEntriesRequest.of(dictionaryId, pattern);
        dictionaryService.searchEntries(request);
        return Status.getAndReset();
    }

    /**
     * Lists the available dictionary.
     *
     * @param provider filter on the provider
     * @param locale filter on the locale
     * @return the error status
     */
    @Command(aliases = {"ls"})
    int list(
            @Option(
                            names = {"-p", "--provider"},
                            paramLabel = "PROVIDER")
                    final String provider,
            @Option(
                            names = {"-l", "--locale"},
                            paramLabel = "LOCALE")
                    final Locale locale) {
        final ListDictionariesRequest request = ListDictionariesRequest.of(locale, provider);
        dictionaryService.listDictionaries(request);
        return Status.getAndReset();
    }

    /**
     * Lists the available dictionary providers.
     *
     * @return the error status
     */
    @Command(name = "list-providers")
    int listProviders() {
        dictionaryService.listProviders();
        return Status.getAndReset();
    }
}
