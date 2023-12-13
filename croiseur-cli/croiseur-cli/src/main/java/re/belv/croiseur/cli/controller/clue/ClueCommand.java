/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.controller.clue;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import re.belv.croiseur.api.clue.ClueService;
import re.belv.croiseur.api.clue.GetClueRequest;
import re.belv.croiseur.cli.status.Status;

import java.util.Optional;
import java.util.Set;

/**
 * The 'clue' command.
 */
@Command(name = "clue")
public final class ClueCommand {

    /** The clue service. */
    private final ClueService clueService;

    /**
     * Constructs an instance.
     *
     * @param clueServiceArg the clue service
     */
    public ClueCommand(final ClueService clueServiceArg) {
        clueService = clueServiceArg;
    }

    @Command(name = "list-providers")
    void listProviders() {
        clueService.listProviders();
    }

    @Command
    int get(@Option(names = {"-p", "--provider"}, paramLabel = "PROVIDER") final String provider,
            @Parameters(arity = "1..*", paramLabel = "WORD [WORD...]") final String[] words) {
        final GetClueRequest request = new GetClueRequest() {
            @Override
            public Optional<String> clueProvider() {
                return Optional.ofNullable(provider);
            }

            @Override
            public Set<String> words() {
                return Set.of(words);
            }
        };
        clueService.getClues(request);
        return Status.getAndReset();
    }
}
