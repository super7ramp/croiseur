/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.tests.puzzle;

import re.belv.croiseur.api.puzzle.persistence.PuzzlePatch;
import re.belv.croiseur.common.puzzle.ChangedPuzzle;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.PuzzleClues;
import re.belv.croiseur.common.puzzle.PuzzleDetails;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.tests.context.PuzzleRepositorySpy;
import re.belv.croiseur.tests.context.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static re.belv.croiseur.common.puzzle.GridPosition.at;

/**
 * Datatable and parameter types pertaining to puzzle service.
 */
public final class PuzzleTypes {

    /**
     * A pattern matching an <em>id variable</em>.
     * <p>
     * An id variable looks like {@literal $id_0 (or just $id), $id_1, $id_2}. It masks the real
     * value of the record identifier. It allows to avoid scenarios with hard coded values depending
     * on a particular repository id allocation implementation.
     * <p>
     * This pattern offers a capture group on the key of the id variable (i.e. the number 0, 1, 2).
     */
    private static final Pattern ID_VARIABLE_PATTERN = Pattern.compile("\\$id(?:_(1-9)+)?");

    /** The characters separating two clues in a datatable. */
    private static final String CLUE_SEPARATOR = " - ";

    /** The puzzle repository spy. */
    private final PuzzleRepositorySpy puzzleRepositorySpy;

    /**
     * Constructs an instance.
     *
     * @param testContext the test context
     */
    public PuzzleTypes(final TestContext testContext) {
        puzzleRepositorySpy = testContext.puzzleRepositorySpy();
    }

    /**
     * Parses a puzzle id or a <em>puzzle id variable</em>.
     * <p>
     * A puzzle id variable looks like {@literal $id, $id_1, $id_2}. It masks the real value of the
     * identifier of the puzzle in puzzle repository. It allows to avoid scenarios with hard coded
     * values depending on a particular repository id allocation implementation.
     *
     * @param idOrIdVariable id variable
     * @return the value of the id variable; If given string cannot be identified as an id variable,
     * then it will assume a raw id value is given and return its parsed integer value
     */
    @ParameterType(".+")
    public long puzzleId(final String idOrIdVariable) {
        final Matcher idVariableMatcher = ID_VARIABLE_PATTERN.matcher(idOrIdVariable);
        final long id;
        if (idVariableMatcher.matches()) {
            final int variableNumber =
                    idVariableMatcher.groupCount() > 1 ?
                            Integer.parseInt(idVariableMatcher.group(1)) : 0;
            id = puzzleRepositorySpy.idVariableValue(variableNumber).orElseThrow();
        } else {
            id = Integer.parseInt(idOrIdVariable);
        }
        return id;
    }

    @DataTableType
    public ChangedPuzzle changedPuzzle(final Map<String, String> table) {
        final long id = puzzleId(table.get("Id"));
        final Puzzle puzzle = puzzle(table);
        /*
         * Have to fake a SavedPuzzle to bypass the small protection put on ChangedPuzzle
         * (package-private constructor only accessible from SavedPuzzle).
         */
        final SavedPuzzle fakeSavedPuzzle = new SavedPuzzle(id, puzzle, 1);
        return fakeSavedPuzzle.asChangedPuzzle();
    }

    @DataTableType
    public SavedPuzzle savedPuzzle(final Map<String, String> table) {
        final long id = puzzleId(table.get("Id"));
        final int revision = Integer.parseInt(table.get("Revision"));
        final Puzzle puzzle = puzzle(table);
        return new SavedPuzzle(id, puzzle, revision);
    }

    @DataTableType
    public Puzzle puzzle(final Map<String, String> table) {
        final PuzzleDetails details = puzzleDetails(table);
        final PuzzleGrid grid = puzzleGrid(table.get("Grid (rows)"));
        final PuzzleClues clues =
                puzzleClues(table.get("Clues (across)"), table.get("Clues (down)"));
        return new Puzzle(details, grid, clues);
    }

    private PuzzleClues puzzleClues(final String rawAcrossClues, final String rawDownClues) {
        final List<String> acrossClues = splitClues(rawAcrossClues);
        final List<String> downClues = splitClues(rawDownClues);
        return new PuzzleClues(acrossClues, downClues);
    }

    private List<String> splitClues(final String rawClues) {
        final String[] splitClues = Optional.ofNullable(rawClues)
                                            .map(s -> s.split(CLUE_SEPARATOR))
                                            .orElseGet(() -> new String[]{});
        return Arrays.stream(splitClues)
                     .map(clue -> clue.replace("(empty)", ""))
                     .toList();
    }

    @ParameterType(".*")
    public PuzzleGrid puzzleGrid(final String gridRows) {
        if (gridRows.isEmpty()) {
            throw new IllegalArgumentException("Unexpected empty puzzle grid. Check your step.");
        }
        final PuzzleGrid.Builder gridBuilder = new PuzzleGrid.Builder();
        final String[] rows = gridRows.split(",");
        for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
            final String line = rows[rowIndex].trim();
            for (int columnIndex = 0; columnIndex < line.length(); columnIndex++) {
                final char cell = line.charAt(columnIndex);
                final GridPosition position = at(columnIndex, rowIndex);
                if (cell == '#') {
                    gridBuilder.shade(position);
                } else if (cell != ' ' && cell != '.') {
                    gridBuilder.fill(position, cell);
                } // else cell is empty
            }
        }
        return gridBuilder.width(rows[0].length()).height(rows.length).build();
    }

    @DataTableType
    public PuzzleDetails puzzleDetails(final Map<String, String> table) {
        final String author = Optional.ofNullable(table.get("Author")).orElse("");
        final String title = Optional.ofNullable(table.get("Title")).orElse("");
        final String editor = Optional.ofNullable(table.get("Editor")).orElse("");
        final String copyright = Optional.ofNullable(table.get("Copyright")).orElse("");
        final Optional<LocalDate> date = Optional.ofNullable(table.get("Date"))
                                                 .map(dateText -> "$today".equals(dateText) ?
                                                         LocalDate.now() :
                                                         LocalDate.parse(dateText));
        return new PuzzleDetails(title, author, editor, copyright, date);
    }

    @DataTableType
    public PuzzleGrid puzzleGrid(final DataTable table) {
        final PuzzleGrid.Builder gridBuilder = new PuzzleGrid.Builder();
        gridBuilder.height(table.height());
        gridBuilder.width(table.width());
        for (int rowIndex = 0; rowIndex < table.height(); rowIndex++) {
            final List<String> row = table.row(rowIndex);
            for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
                final String cellContent = row.get(columnIndex);
                if (cellContent == null) {
                    // blank
                    continue;
                }
                if (cellContent.length() > 1) {
                    throw new IllegalArgumentException(
                            "Cells should only contain single character, but got " + cellContent);
                }
                if (cellContent.equals("#")) {
                    gridBuilder.shade(new GridPosition(columnIndex, rowIndex));
                } else {
                    gridBuilder.fill(new GridPosition(columnIndex, rowIndex),
                                     cellContent.charAt(0));
                }
            }
        }
        return gridBuilder.build();
    }

    @DataTableType
    public PuzzlePatch puzzlePatch(final Map<String, String> table) {

        final Map<String, String> workTable = new HashMap<>(table);
        final String title = workTable.remove("Title");
        final String author = workTable.remove("Author");
        final String editor = workTable.remove("Editor");
        final String copyright = workTable.remove("Copyright");
        final String date = workTable.remove("Date");
        final String grid = workTable.remove("Grid (rows)");
        final String acrossClues = workTable.remove("Clues (across)");
        final String downClues = workTable.remove("Clues (down)");

        if (!workTable.isEmpty()) {
            throw new IllegalArgumentException(
                    "Unused fields detected while creating puzzle patch: " + workTable +
                    ". Have you misspelled them?");
        }

        return new PuzzlePatch() {
            @Override
            public Optional<String> modifiedTitle() {
                return Optional.ofNullable(title);
            }

            @Override
            public Optional<String> modifiedAuthor() {
                return Optional.ofNullable(author);
            }

            @Override
            public Optional<String> modifiedEditor() {
                return Optional.ofNullable(editor);
            }

            @Override
            public Optional<String> modifiedCopyright() {
                return Optional.ofNullable(copyright);
            }

            @Override
            public Optional<LocalDate> modifiedDate() {
                return Optional.ofNullable(date).map(LocalDate::parse);
            }

            @Override
            public Optional<PuzzleGrid> modifiedGrid() {
                return Optional.ofNullable(grid).map(PuzzleTypes.this::puzzleGrid);
            }

            @Override
            public Optional<List<String>> modifiedAcrossClues() {
                return Optional.ofNullable(acrossClues).map(PuzzleTypes.this::splitClues);
            }

            @Override
            public Optional<List<String>> modifiedDownClues() {
                return Optional.ofNullable(downClues).map(PuzzleTypes.this::splitClues);
            }
        };
    }
}
