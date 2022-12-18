package com.gitlab.super7ramp.crosswords.cli.controller.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionariesRequest;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionaryEntriesRequest;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Locale;

/**
 * "dictionary" subcommand.
 */
@Command(name = "dictionary", description = "List and print available dictionaries",
        synopsisSubcommandLabel = "COMMAND" // instead of [COMMAND], because subcommand is mandatory
)
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

    @Command(name = "cat", description = "Display dictionary entries")
    void cat(@Parameters(index = "0", paramLabel = "<PROVIDER:DICTIONARY>") final DictionaryIdentifier dictionaryId) {
        final ListDictionaryEntriesRequest request = ListDictionaryEntriesRequest.of(dictionaryId);
        dictionaryService.listEntries(request);
    }

    @Command(name = "get-default", description = "Return the default dictionary")
    void getDefault() {
        dictionaryService.showPreferredDictionary();
    }

    @Command(name = "list", aliases = {"ls"}, description = "List available dictionaries")
    void list(@Option(names = {"-p", "--provider"}) final String provider, @Option(names = {"-l",
            "--locale"}) final Locale locale) {
        final ListDictionariesRequest request = ListDictionariesRequest.of(locale, provider);
        dictionaryService.listDictionaries(request);
    }

    @Command(name = "list-providers", description = "List available dictionary providers")
    void listProviders() {
        dictionaryService.listProviders();
    }

}
