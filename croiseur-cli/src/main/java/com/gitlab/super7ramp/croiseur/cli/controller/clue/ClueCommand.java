/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.clue;

import com.gitlab.super7ramp.croiseur.api.clue.ClueService;
import com.gitlab.super7ramp.croiseur.api.clue.CreateClueRequest;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The 'clue' command.
 */
@CommandLine.Command(name = "clue", description = "Give crossword clues and list available " +
        "clue providers", synopsisSubcommandLabel = "COMMAND" /* instead of [COMMAND], because
        subcommand is mandatory */)
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

    @CommandLine.Command(name = "list", aliases = {"ls"}, description = "List available clue " +
            "providers")
    void list() {
        clueService.listProviders();
    }


    @CommandLine.Command(name = "get", description = "Get clues for the given words")
    void create(@CommandLine.Parameters(arity = "1..*", paramLabel = "WORD [WORD...]") final String[] words) {
        final CreateClueRequest request = new CreateClueRequest() {
            @Override
            public Optional<String> clueProvider() {
                return Optional.empty();
            }

            @Override
            public List<String> words() {
                return Arrays.asList(words);
            }
        };
        clueService.createClues(request);
    }
}
