package com.gitlab.super7ramp.crosswords.cli.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.crosswords.cli.dictionary.adapted.ListDictionariesRequestImpl;
import com.gitlab.super7ramp.crosswords.cli.dictionary.adapted.ListDictionaryEntriesRequestImpl;
import com.gitlab.super7ramp.crosswords.cli.dictionary.parsed.DictionaryIdentifier;
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

    @Command(name = "providers", description = "List available dictionary providers")
    void providers() {
        dictionaryService.listProviders();
    }

    @Command(name = "cat", description = "Display dictionary entries")
    void cat(@Parameters(index = "0", paramLabel = "<[PROVIDER:]DICTIONARY>") final DictionaryIdentifier dictionaryId) {
        dictionaryService.listEntries(new ListDictionaryEntriesRequestImpl(dictionaryId));
    }

    @Command(name = "list", description = "List available dictionaries")
    void list(@Option(names = {"-p", "--provider"}) final String provider, @Option(names = {"-l",
            "--locale"}) final Locale locale) {
        dictionaryService.listDictionaries(new ListDictionariesRequestImpl(locale, provider));
    }

}
