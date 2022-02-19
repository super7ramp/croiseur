package com.gitlab.super7ramp.crosswords.cli.solve;

import com.gitlab.super7ramp.crosswords.api.solve.SolveRequest;
import com.gitlab.super7ramp.crosswords.api.solve.SolverService;
import com.gitlab.super7ramp.crosswords.cli.dictionary.parsed.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.cli.solve.adapted.SolveRequestImpl;
import com.gitlab.super7ramp.crosswords.cli.solve.parsed.GridSize;
import com.gitlab.super7ramp.crosswords.cli.solve.parsed.PrefilledBox;
import com.gitlab.super7ramp.crosswords.cli.solve.parsed.PrefilledSlot;
import com.gitlab.super7ramp.crosswords.solver.api.GridPosition;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * "solve" subcommand.
 */
@Command(name = "solve", description = "Solve a crossword puzzle")
public final class SolveCommand implements Runnable {

    /** Crossword service. */
    private final SolverService solverService;

    @Option(names = {"-s", "--size"}, paramLabel = "<INTEGERxINTEGER>", arity = "1", required =
            true, description = "Grid dimensions, e.g. '--size 7x15' for a grid of width 7 and" +
            "height 15")
    private GridSize size;

    // TODO allow optional => use default dictionary => sort dictionaries
    //  (prefer system's locale, implement criteria on provider)
    @Option(names = {"-d", "--dictionary"}, paramLabel = "<[PROVIDER:]DICTIONARY>", arity = "1",
            required = true, description = "Dictionary identifier")
    private DictionaryIdentifier dictionaryId;

    @Option(names = {"-B", "--shaded-box", "--shaded-boxes"}, arity = "1..*", description =
            "Shaded boxes, e.g. '--shaded-boxes (1,2) (3,4)...'", paramLabel = "<COORDINATE> ")
    private GridPosition[] shadedBoxes = {};

    @Option(names = {"-b", "--box", "--boxes"}, arity = "1..*", description = "Pre-filled boxes, " +
            "e.g. '--boxes ((1,2),A) ((3,4),B)...'", paramLabel = "<(COORDINATE,LETTER)> ")
    private PrefilledBox[] prefilledBoxes = {};

    @Option(names = {"-H", "--horizontal"}, arity = "1..*", description = "Pre-filled horizontal " +
            "slot(s), e.g. '--horizontal ((0,0),hello) ((5,0),world)...",
            paramLabel = "<(COORDINATE,WORD)> ")
    private PrefilledSlot[] prefilledHorizontalSlots = {};

    @Option(names = {"-V", "--vertical"}, arity = "1..*", description = "Pre-filled vertical " +
            "slot(s), e.g. '--vertical ((0,0),hello) ((5,0),world)...",
            paramLabel = "<(COORDINATE,WORD)> ")
    private PrefilledSlot[] prefilledVerticalSlots = {};

    @Option(names = {"-p", "--progress"}, description = "Show solver progress")
    private boolean progress;

    /**
     * Constructor.
     *
     * @param aSolverService solver service
     */
    public SolveCommand(final SolverService aSolverService) {
        solverService = aSolverService;
    }

    @Override
    public void run() {
        final SolveRequest request = new SolveRequestImpl(size, shadedBoxes, prefilledBoxes,
                prefilledHorizontalSlots, prefilledVerticalSlots, dictionaryId, progress);
        solverService.solve(request);
    }

}
