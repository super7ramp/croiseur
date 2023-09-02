/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.repository.filesystem.plugin;

import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.PuzzleClues;
import re.belv.croiseur.common.puzzle.PuzzleDetails;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.puzzle.codec.xd.model.XdClue;
import re.belv.croiseur.puzzle.codec.xd.model.XdClues;
import re.belv.croiseur.puzzle.codec.xd.model.XdCrossword;
import re.belv.croiseur.puzzle.codec.xd.model.XdGrid;
import re.belv.croiseur.puzzle.codec.xd.model.XdMetadata;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static re.belv.croiseur.common.puzzle.GridPosition.at;

/**
 * Converts crossword from/to persistence format (xd).
 */
final class PuzzleConverter {

    /** The metadata key for the revision information, in persisted crossword model. */
    private static final String REVISION_METADATA_KEY = "x-croiseur-revision";

    /** Private constructor to prevent instantiate, static methods only. */
    private PuzzleConverter() {
        // Nothing to do.
    }

    /**
     * Converts the persistence crossword model to the domain crossword model.
     *
     * @param id                        the id of the crossword
     * @param persistenceCrosswordModel the persistence crossword model
     * @return the domain crossword model
     * @throws PuzzleConversionException if conversion fails
     */
    static SavedPuzzle toDomain(final long id, final XdCrossword persistenceCrosswordModel)
            throws PuzzleConversionException {
        final int revision = extractRevision(persistenceCrosswordModel.metadata());
        final PuzzleDetails details = toDomain(persistenceCrosswordModel.metadata());
        final PuzzleGrid grid = toDomain(persistenceCrosswordModel.grid());
        final PuzzleClues clues = toDomain(persistenceCrosswordModel.clues());
        final Puzzle puzzle = new Puzzle(details, grid, clues);
        return new SavedPuzzle(id, revision, puzzle);
    }

    /**
     * Converts the domain crossword model to the persistence crossword model.
     *
     * @param puzzle the domain crossword model
     * @return the persistence crossword model
     */
    static XdCrossword toPersistence(final SavedPuzzle puzzle) {
        final XdMetadata metadata = toPersistence(puzzle.details(), puzzle.revision());
        final XdGrid grid = toPersistence(puzzle.grid());
        final XdClues clues = toPersistence(puzzle.grid(), puzzle.clues());
        return new XdCrossword(metadata, grid, clues);
    }

    /**
     * Extracts revision information from {@link XdMetadata}.
     *
     * @param metadata the persisted metadata
     * @return the extracted revision information
     * @throws PuzzleConversionException if revision information does not exist
     */
    private static int extractRevision(final XdMetadata metadata) throws PuzzleConversionException {
        final String value = metadata.otherProperties().get(REVISION_METADATA_KEY);
        try {
            return Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            throw new PuzzleConversionException("Cannot read revision", e);
        }
    }

    /**
     * Converts a persistence grid to a domain grid model
     *
     * @param persistenceGridModel the persistence grid model
     * @return the domain grid model
     * @throws PuzzleConversionException if conversion fails
     */
    private static PuzzleGrid toDomain(final XdGrid persistenceGridModel)
            throws PuzzleConversionException {
        if (!persistenceGridModel.spaces().isEmpty()) {
            throw new PuzzleConversionException(
                    "Cannot convert grid with spaces: This is not supported by Croiseur.");
        }
        final PuzzleGrid.Builder builder = new PuzzleGrid.Builder();
        persistenceGridModel.blocks().stream().map(PuzzleConverter::toDomain)
                            .forEach(builder::shade);
        persistenceGridModel.filled()
                            .forEach((index, letter) -> builder.fill(toDomain(index),
                                                                     letter.charAt(0)));
        return builder.width(width(persistenceGridModel)).height(height(persistenceGridModel))
                      .build();
    }

    /**
     * Converts the persistence metadata model to the domain metadata model
     *
     * @param persistenceMetadataModel the persistence metadata model
     * @return the domain metadata model
     */
    private static PuzzleDetails toDomain(final XdMetadata persistenceMetadataModel) {
        return new PuzzleDetails(persistenceMetadataModel.title().orElse(""),
                                 persistenceMetadataModel.author().orElse(""),
                                 persistenceMetadataModel.editor().orElse(""),
                                 persistenceMetadataModel.copyright().orElse(""),
                                 persistenceMetadataModel.date());
    }

    /**
     * Converts the persistence clues model to the domain clues model.
     *
     * @param persistenceCluesModel the persistence clues model
     * @return the domain clues model
     */
    private static PuzzleClues toDomain(final XdClues persistenceCluesModel) {
        final List<String> acrossClues =
                persistenceCluesModel.acrossClues().stream().map(XdClue::clue).toList();
        final List<String> downClues =
                persistenceCluesModel.downClues().stream().map(XdClue::clue).toList();
        return new PuzzleClues(acrossClues, downClues);
    }

    /**
     * Converts a grid position from persistence model to domain model.
     *
     * @param persistenceGridPosition the grid position from persistence model
     * @return the grid position in domain model
     */
    private static GridPosition toDomain(final XdGrid.Index persistenceGridPosition) {
        return new GridPosition(persistenceGridPosition.column(), persistenceGridPosition.row());
    }

    /**
     * Determines the width (i.e. the number of columns) of the persistence grid model.
     *
     * @param persistenceGridModel the persistence grid model
     * @return the width of the grid
     * @throws PuzzleConversionException if grid is empty
     */
    private static int width(final XdGrid persistenceGridModel) throws PuzzleConversionException {
        return maxDimension(persistenceGridModel, XdGrid.Index::column);
    }

    /**
     * Determines the height (i.e. the number of rows) of the persistence grid model.
     *
     * @param persistenceGridModel the persistence grid model
     * @return the height of the grid
     * @throws PuzzleConversionException if grid is empty
     */
    private static int height(final XdGrid persistenceGridModel) throws PuzzleConversionException {
        return maxDimension(persistenceGridModel, XdGrid.Index::row);
    }

    /**
     * Utility method to determine the max dimension (width or height) of the given persisted grid.
     *
     * @param persistedGrid the persisted grid
     * @param dimension     the dimension of the grid for which get the max value
     * @return the max value of the given dimension of the given grid
     * @throws PuzzleConversionException if grid is empty
     */
    private static int maxDimension(final XdGrid persistedGrid,
                                    final Function<XdGrid.Index, Integer> dimension)
            throws PuzzleConversionException {
        return 1 + Stream.of(persistedGrid.blocks(), persistedGrid.filled().keySet(),
                             persistedGrid.nonFilled()).flatMap(Collection::stream).map(dimension)
                         .max(Comparator.naturalOrder())
                         .orElseThrow(() -> new PuzzleConversionException("Invalid empty grid"));
    }

    /**
     * Converts domain metadata model to persistence metadata model.
     *
     * @param details  the domain metadata model
     * @param revision the revision number
     * @return the persistence metadata model
     */
    private static XdMetadata toPersistence(final PuzzleDetails details, final int revision) {
        final XdMetadata.Builder builder = new XdMetadata.Builder();
        if (!details.title().isEmpty()) {
            builder.title(details.title());
        }
        if (!details.author().isEmpty()) {
            builder.author(details.author());
        }
        if (!details.editor().isEmpty()) {
            builder.editor(details.editor());
        }
        if (!details.copyright().isEmpty()) {
            builder.copyright(details.copyright());
        }
        details.date().ifPresent(builder::date);
        builder.otherProperty(REVISION_METADATA_KEY, String.valueOf(revision));
        return builder.build();
    }

    /**
     * Converts a domain grid model to a persistence grid model.
     *
     * @param grid the domain grid model
     * @return the persistence grid model
     */
    private static XdGrid toPersistence(final PuzzleGrid grid) {
        final XdGrid.Builder builder = new XdGrid.Builder();
        for (int row = 0; row < grid.height(); row++) {
            for (int column = 0; column < grid.width(); column++) {
                final Character letter = grid.filled().get(at(column, row));
                if (letter != null) {
                    builder.filled(XdGrid.Index.at(column, row), letter);
                } else if (grid.shaded().contains(at(column, row))) {
                    builder.block(XdGrid.Index.at(column, row));
                } else {
                    builder.nonFilled(XdGrid.Index.at(column, row));
                }
            }
        }
        return builder.build();
    }

    /**
     * Converts a domain clues model to a persistence clues model.
     *
     * @param grid  the domain grid model
     * @param clues the domain clues model
     * @return the persistence clues model
     */
    private static XdClues toPersistence(final PuzzleGrid grid, final PuzzleClues clues) {
        final XdClues.Builder builder = new XdClues.Builder();

        final List<String> acrossSlotsContents = grid.acrossSlotContents();
        final List<String> acrossClues = clues.across();
        for (int i = 0; i < acrossClues.size(); i++) {
            builder.across(i + 1, acrossClues.get(i), acrossSlotsContents.get(i));
        }

        final List<String> downSlotContents = grid.downSlotContents();
        final List<String> downClues = clues.down();
        for (int i = 0; i < downClues.size(); i++) {
            builder.down(i + 1, downClues.get(i), downSlotContents.get(i));
        }

        return builder.build();
    }
}
