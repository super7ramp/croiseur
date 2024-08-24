/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.controller.solver;

import java.util.Random;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import re.belv.croiseur.api.dictionary.DictionaryIdentifier;
import re.belv.croiseur.api.solver.SolveRequest;
import re.belv.croiseur.api.solver.SolverService;
import re.belv.croiseur.cli.controller.solver.adapter.CliSolveRequest;
import re.belv.croiseur.cli.controller.solver.parser.GridSize;
import re.belv.croiseur.cli.controller.solver.parser.PrefilledBox;
import re.belv.croiseur.cli.controller.solver.parser.PrefilledSlot;
import re.belv.croiseur.cli.status.Status;
import re.belv.croiseur.common.puzzle.GridPosition;

/** "solver run" subcommand: Solves a crossword puzzle. */
@Command(
        name = "run",
        aliases = {"solve"})
public final class SolverRunCommand implements Callable<Integer> {

    /** Solver service. */
    private final SolverService solverService;

    /** The name of the solver to use. */
    @Parameters(arity = "0..1", paramLabel = "SOLVER")
    private String solver;

    /** The grid dimensions. */
    @Option(
            names = {"-s", "--size"},
            paramLabel = "INTEGERxINTEGER",
            arity = "1",
            required = true)
    private GridSize size;

    /** The identifiers of the dictionary to use for solving. */
    @Option(
            names = {"-d", "--dictionary", "--dictionaries"},
            paramLabel = "PROVIDER:DICTIONARY",
            arity = "1..*")
    private DictionaryIdentifier[] dictionaryIds = {};

    /** The definition of the shaded boxes. */
    @Option(
            names = {"-B", "--shaded-box", "--shaded-boxes"},
            arity = "1..*",
            paramLabel = "COORDINATE ")
    private GridPosition[] shadedBoxes = {};

    /** The definition of the pre-filled boxes. */
    @Option(
            names = {"-b", "--box", "--boxes"},
            arity = "1..*",
            paramLabel = "(COORDINATE," + "LETTER)")
    private PrefilledBox[] prefilledBoxes = {};

    /** The definition of the pre-filled horizontal slots. */
    @Option(
            names = {"-H", "--horizontal", "--across"},
            arity = "1..*",
            paramLabel = "(COORDINATE,WORD)")
    private PrefilledSlot[] prefilledHorizontalSlots = {};

    /** The definition of the pre-filled vertical slots. */
    @Option(
            names = {"-V", "--vertical", "--down"},
            arity = "1..*",
            paramLabel = "(COORDINATE," + "WORD)")
    private PrefilledSlot[] prefilledVerticalSlots = {};

    /** The randomness source for shuffling dictionary. */
    @Option(
            names = {"-r", "--random", "--shuffle"},
            arity = "0..1",
            paramLabel = "SEED")
    private Random random;

    /** Flag to show solver progress. */
    @Option(names = {"-p", "--progress"})
    private boolean progress;

    /** Flag to generate clues for result slot words if solver succeeds. */
    @Option(names = {"-c", "--clues"})
    private boolean clues;

    /** Flag to save the grid. */
    @Option(names = {"-S", "--save"})
    private boolean save;

    /**
     * Constructs an instance.
     *
     * @param solverServiceArg solver service
     */
    public SolverRunCommand(final SolverService solverServiceArg) {
        solverService = solverServiceArg;
    }

    @Override
    public Integer call() {
        final SolveRequest request = new CliSolveRequest.Builder()
                .solver(solver)
                .size(size)
                .shadedBoxes(shadedBoxes)
                .prefilledBoxes(prefilledBoxes)
                .prefilledHorizontalSlots(prefilledHorizontalSlots)
                .prefilledVerticalSlots(prefilledVerticalSlots)
                .dictionaryIds(dictionaryIds)
                .random(random)
                .progress(progress)
                .clues(clues)
                .save(save)
                .build();
        solverService.solve(request);
        return Status.getAndReset();
    }
}
