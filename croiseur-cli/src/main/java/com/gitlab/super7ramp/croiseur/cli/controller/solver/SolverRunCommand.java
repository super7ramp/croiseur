/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.solver;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.croiseur.api.solver.SolveRequest;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.cli.controller.solver.adapter.SolveRequestImpl;
import com.gitlab.super7ramp.croiseur.cli.controller.solver.parser.GridSize;
import com.gitlab.super7ramp.croiseur.cli.controller.solver.parser.PrefilledBox;
import com.gitlab.super7ramp.croiseur.cli.controller.solver.parser.PrefilledSlot;
import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Random;

/**
 * "solver run" subcommand: Solves a crossword puzzle.
 */
@Command(name = "run", aliases = {"solve"})
public final class SolverRunCommand implements Runnable {

    /** Solver service. */
    private final SolverService solverService;

    /** The name of the solver to use. */
    @Parameters(arity = "0..1")
    private String solver;

    /** The grid dimensions. */
    @Option(names = {"-s", "--size"}, paramLabel = "<INTEGERxINTEGER>", arity = "1", required =
            true)
    private GridSize size;

    /** The identifiers of the dictionary to use for solving. */
    @Option(names = {"-d", "--dictionary", "--dictionaries"}, paramLabel = "<PROVIDER:DICTIONARY>",
            arity = "1..*")
    private DictionaryIdentifier[] dictionaryIds;

    /** The definition of the shaded boxes. */
    @Option(names = {"-B", "--shaded-box", "--shaded-boxes"}, arity = "1..*", paramLabel =
            "<COORDINATE> ")
    private GridPosition[] shadedBoxes = {};

    /** The definition of the pre-filled boxes. */
    @Option(names = {"-b", "--box", "--boxes"}, arity = "1..*", paramLabel = "<(COORDINATE," +
                                                                             "LETTER)> ")
    private PrefilledBox[] prefilledBoxes = {};

    /** The definition of the pre-filled horizontal slots. */
    @Option(names = {"-H", "--horizontal", "--across"}, arity = "1..*", paramLabel = "<" +
                                                                                     "(COORDINATE,WORD)> ")
    private PrefilledSlot[] prefilledHorizontalSlots = {};

    /** The definition of the pre-filled vertical slots. */
    @Option(names = {"-V", "--vertical", "--down"}, arity = "1..*", paramLabel = "<(COORDINATE," +
                                                                                 "WORD)> ")
    private PrefilledSlot[] prefilledVerticalSlots = {};

    /** The randomness source for shuffling dictionary. */
    @Option(names = {"-r", "--random", "--shuffle"}, arity = "0..1", paramLabel = "SEED")
    private Random random;

    /** Flag to show solver progress. */
    @Option(names = {"-p", "--progress"})
    private boolean progress;

    /**
     * Constructs an instance.
     *
     * @param solverServiceArg solver service
     */
    public SolverRunCommand(final SolverService solverServiceArg) {
        solverService = solverServiceArg;
    }

    @Override
    public void run() {
        final SolveRequest request = new SolveRequestImpl(solver, size, shadedBoxes, prefilledBoxes,
                                                          prefilledHorizontalSlots,
                                                          prefilledVerticalSlots, dictionaryIds,
                                                          random, progress);
        solverService.solve(request);
    }

}
