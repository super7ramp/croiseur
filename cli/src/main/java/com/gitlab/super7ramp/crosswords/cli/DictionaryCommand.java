package com.gitlab.super7ramp.crosswords.cli;

import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;
import com.gitlab.super7ramp.crosswords.dictionary.api.DictionaryLoader;
import com.gitlab.super7ramp.crosswords.dictionary.api.spi.DictionaryProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * "dictionary" subcommand.
 */
@Command(
        name = "dictionary",
        description = "List and print available dictionaries",
        synopsisSubcommandLabel = "COMMAND" // instead of [COMMAND], because subcommand is mandatory
)
final class DictionaryCommand {

    private static final String PROVIDERS_OUTPUT_FORMAT = "%-16s\t%-54s%n";

    private static final String LIST_OUTPUT_FORMAT = "%-16s\t%-16s\t%-16s%n";

    /**
     * Constructor.
     */
    DictionaryCommand() {
        // Nothing to do.
    }

    private static void printDictionaries(final Map<DictionaryProvider, Collection<Dictionary>> dictionariesByProvider) {
        System.out.printf(LIST_OUTPUT_FORMAT, "Provider", "Name", "Locale");
        System.out.printf(LIST_OUTPUT_FORMAT, "--------", "----", "------");
        for (final Map.Entry<DictionaryProvider, Collection<Dictionary>> entry : dictionariesByProvider.entrySet()) {
            final DictionaryProvider provider = entry.getKey();
            final Collection<Dictionary> dictionaries = entry.getValue();

            dictionaries.forEach(dictionary ->
                    System.out.printf(LIST_OUTPUT_FORMAT, provider.name(), dictionary.name(),
                            dictionary.locale().getDisplayName())
            );
        }
    }

    private static Predicate<DictionaryProvider> filterFrom(final String backend) {
        return Optional.ofNullable(backend)
                .map(DictionaryLoader.Search::byProvider)
                .orElseGet(DictionaryLoader.Search::includeAll);
    }

    private static Predicate<Dictionary> filterFrom(final Locale locale) {
        return Optional.ofNullable(locale)
                .map(DictionaryLoader.Search::byLocale)
                .orElseGet(DictionaryLoader.Search::includeAll);
    }

    @Command(name = "providers", description = "List available dictionary providers")
    void providers() {
        final Collection<DictionaryProvider> providers = DictionaryLoader.providers();
        if (providers.isEmpty()) {
            System.out.println("No dictionary provider found.");
        } else {
            System.out.printf(PROVIDERS_OUTPUT_FORMAT, "Provider", "Description");
            System.out.printf(PROVIDERS_OUTPUT_FORMAT, "--------", "-----------");
            providers.forEach(provider ->
                    System.out.printf(PROVIDERS_OUTPUT_FORMAT, provider.name(), provider.description())
            );
        }

    }

    @Command(name = "cat", description = "Display dictionary entries")
    void cat(@Parameters(index = "0", paramLabel = "<[provider:]dictionary>") final String dictionaryId) {

        final String[] parts = dictionaryId.split(":");
        final Optional<String> providerName;
        final String dictionaryName;
        if (parts.length > 1) {
            providerName = Optional.of(parts[0]);
            dictionaryName = parts[1];
        } else {
            providerName = Optional.empty();
            dictionaryName = parts[0];
        }

        final Map<DictionaryProvider, Collection<Dictionary>> dictionaries =
                DictionaryLoader.get(
                        providerName.map(DictionaryLoader.Search::byProvider).orElseGet(DictionaryLoader.Search::includeAll),
                        DictionaryLoader.Search.byName(dictionaryName));

        if (dictionaries.isEmpty()) {
            System.out.println("Dictionary not found.");
        } else if (dictionaries.size() > 1 || dictionaries.values().size() != 1) {
            System.out.println("Ambiguous dictionary name, candidates are:");
            printDictionaries(dictionaries);
        } else {
            final Dictionary dictionary = dictionaries.entrySet().iterator().next().getValue().iterator().next();
            final Iterator<String> wordIterator = dictionary.lookup(word -> true).iterator();

            // As output may be very large, it is likely the output is going to be piped (grep, head, ...). Checking
            // error status on System.out allows detecting broken pipe and fast exit.
            // TODO confirm it actually works, it doesn't seem to be the case when program is launched via gradle
            while (wordIterator.hasNext() && !System.out.checkError()) {
                System.out.println(wordIterator.next());
            }
            //System.err.println("exit");
        }
    }

    @Command(name = "list", description = "List available dictionaries")
    void list(@Option(names = {"-p", "--provider"}) final String providerOption,
              @Option(names = {"-l", "--locale"}) final Locale localeOption) {

        final Map<DictionaryProvider, Collection<Dictionary>> availableDictionaries =
                DictionaryLoader.get(filterFrom(providerOption), filterFrom(localeOption));

        if (availableDictionaries.isEmpty()) {
            System.out.println("No dictionary found.");
        } else {
            printDictionaries(availableDictionaries);
        }
    }

}
