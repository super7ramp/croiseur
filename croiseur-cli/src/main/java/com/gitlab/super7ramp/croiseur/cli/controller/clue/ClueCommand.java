/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.clue;

import com.gitlab.super7ramp.croiseur.api.clue.ClueService;
import com.gitlab.super7ramp.croiseur.api.clue.GetClueRequest;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    void get(@Parameters(arity = "1..*", paramLabel = "WORD [WORD...]") final String[] words) {
        final GetClueRequest request = new GetClueRequest() {
            @Override
            public Optional<String> clueProvider() {
                return Optional.empty();
            }

            @Override
            public List<String> words() {
                return Arrays.asList(words);
            }
        };
        clueService.getClues(request);
    }
}
