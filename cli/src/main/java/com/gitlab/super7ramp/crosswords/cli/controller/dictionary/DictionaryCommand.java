package com.gitlab.super7ramp.crosswords.cli.controller.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryUsecase;
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
    private final DictionaryUsecase dictionaryUsecase;

    /**
     * Constructor.
     *
     * @param aDictionaryUsecase the dictionary service
     */
    public DictionaryCommand(final DictionaryUsecase aDictionaryUsecase) {
        dictionaryUsecase = aDictionaryUsecase;
    }

    @Command(name = "providers", description = "List available dictionary providers")
    void providers() {
        dictionaryUsecase.listProviders();
    }

    @Command(name = "cat", description = "Display dictionary entries")
    void cat(@Parameters(index = "0", paramLabel = "<PROVIDER:DICTIONARY>") final DictionaryIdentifier dictionaryId) {
        final ListDictionaryEntriesRequest request = ListDictionaryEntriesRequest.of(dictionaryId);
        dictionaryUsecase.listEntries(request);
    }

    @Command(name = "list", description = "List available dictionaries")
    void list(@Option(names = {"-p", "--provider"}) final String provider, @Option(names = {"-l",
            "--locale"}) final Locale locale) {
        final ListDictionariesRequest request = ListDictionariesRequest.of(locale, provider);
        dictionaryUsecase.listDictionaries(request);
    }

}
