/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.clue;

import com.gitlab.super7ramp.croiseur.api.clue.ClueService;
import com.gitlab.super7ramp.croiseur.api.clue.GetClueRequest;
import com.gitlab.super7ramp.croiseur.cli.status.Status;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

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
