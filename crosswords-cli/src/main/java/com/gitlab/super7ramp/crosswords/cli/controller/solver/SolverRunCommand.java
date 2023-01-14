/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.cli.controller.solver;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.api.solver.SolveRequest;
import com.gitlab.super7ramp.crosswords.api.solver.SolverService;
import com.gitlab.super7ramp.crosswords.cli.controller.solver.adapter.SolveRequestImpl;
import com.gitlab.super7ramp.crosswords.cli.controller.solver.parser.GridSize;
import com.gitlab.super7ramp.crosswords.cli.controller.solver.parser.PrefilledBox;
import com.gitlab.super7ramp.crosswords.cli.controller.solver.parser.PrefilledSlot;
import com.gitlab.super7ramp.crosswords.common.GridPosition;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * "solver run" subcommand.
 */
@Command(name = "run", aliases = {"solve"}, description = "Solve a crossword puzzle")
public final class SolverRunCommand implements Runnable {

    /** Solver service. */
    private final SolverService solverService;

    @Parameters(arity = "0..1", description = "The name of the solver to use")
    private String solver;

    @Option(names = {"-s", "--size"}, paramLabel = "<INTEGERxINTEGER>", arity = "1", required =
            true, description = "Grid dimensions, e.g. '--size 7x15' for a grid of width 7 and " +
            "height 15")
    private GridSize size;

    @Option(names = {"-d", "--dictionary"}, paramLabel = "<PROVIDER:DICTIONARY>", arity = "1",
            description = "Dictionary identifier")
    private DictionaryIdentifier dictionaryId;

    @Option(names = {"-B", "--shaded-box", "--shaded-boxes"}, arity = "1..*", description =
            "Shaded boxes, e.g. '--shaded-boxes (1,2) (3,4)...'", paramLabel = "<COORDINATE> ")
    private GridPosition[] shadedBoxes = {};

    @Option(names = {"-b", "--box", "--boxes"}, arity = "1..*", description = "Pre-filled boxes, " +
            "e.g. '--boxes ((1,2),A) ((3,4),B)...'", paramLabel = "<(COORDINATE,LETTER)> ")
    private PrefilledBox[] prefilledBoxes = {};

    @Option(names = {"-H", "--horizontal"}, arity = "1..*", description = "Pre-filled horizontal " +
            "slots, e.g. '--horizontal ((0,0),hello) ((5,0),world)...",
            paramLabel = "<(COORDINATE,WORD)> ")
    private PrefilledSlot[] prefilledHorizontalSlots = {};

    @Option(names = {"-V", "--vertical"}, arity = "1..*", description = "Pre-filled vertical " +
            "slots, e.g. '--vertical ((0,0),hello) ((5,0),world)...",
            paramLabel = "<(COORDINATE,WORD)> ")
    private PrefilledSlot[] prefilledVerticalSlots = {};

    @Option(names = {"-p", "--progress"}, description = "Show solver progress")
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
                prefilledHorizontalSlots, prefilledVerticalSlots, dictionaryId, progress);
        solverService.solve(request);
    }

}
