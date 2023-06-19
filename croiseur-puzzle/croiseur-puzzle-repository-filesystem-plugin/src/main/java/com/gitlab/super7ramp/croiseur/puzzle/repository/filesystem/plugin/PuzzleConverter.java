/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.repository.filesystem.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdCrossword;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdGrid;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdMetadata;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

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
     * Converts the persisted crossword model to the domain crossword model.
     *
     * @param id                      the id of the crossword
     * @param persistedCrosswordModel the persisted crossword
     * @return the domain crossword model
     * @throws PuzzleConversionException if conversion fails
     */
    static SavedPuzzle toDomain(final int id, final XdCrossword persistedCrosswordModel)
            throws PuzzleConversionException {
        final int revision = extractRevision(persistedCrosswordModel.metadata());
        final PuzzleDetails details = toDomain(persistedCrosswordModel.metadata());
        final PuzzleGrid grid = toDomain(persistedCrosswordModel.grid());
        final Puzzle puzzle = new Puzzle(details, grid);
        return new SavedPuzzle(id, puzzle, revision);
    }

    /**
     * Extracts revision information from xd metadata.
     *
     * @param metadata the persisted metadata
     * @return the extracted revision information
     * @throws PuzzleConversionException if revision information does not exist
     */
    private static int extractRevision(final XdMetadata metadata) throws PuzzleConversionException {
        final String value = metadata.otherProperties().get("x-croiseur-revision");
        try {
            return Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            throw new PuzzleConversionException("Cannot read revision", e);
        }
    }

    /**
     * Converts a persisted {@link XdGrid} to a domain {@link PuzzleGrid}.
     *
     * @param persistedGrid the persisted grid
     * @return the domain {@link PuzzleGrid}
     * @throws PuzzleConversionException if conversion fails
     */
    private static PuzzleGrid toDomain(final XdGrid persistedGrid)
            throws PuzzleConversionException {
        if (!persistedGrid.spaces().isEmpty()) {
            throw new PuzzleConversionException(
                    "Cannot convert grid with spaces: This is not supported by Croiseur.");
        }
        final PuzzleGrid.Builder builder = new PuzzleGrid.Builder();
        persistedGrid.blocks().stream().map(PuzzleConverter::toDomain).forEach(builder::shade);
        persistedGrid.filled()
                     .forEach((index, letter) -> builder.fill(toDomain(index), letter.charAt(0)));
        return builder.width(width(persistedGrid)).height(height(persistedGrid)).build();
    }

    /**
     * Converts a persisted {@link XdMetadata} to a domain {@link PuzzleDetails}.
     *
     * @param persistedMetadata the persisted metadata
     * @return the domain {@link PuzzleGrid}
     */
    private static PuzzleDetails toDomain(final XdMetadata persistedMetadata) {
        return new PuzzleDetails(persistedMetadata.title().orElse(""),
                                 persistedMetadata.author().orElse(""),
                                 persistedMetadata.editor().orElse(""),
                                 persistedMetadata.copyright().orElse(""),
                                 persistedMetadata.date());
    }

    /**
     * Converts a persisted {@link XdGrid.Index} to a domain {@link GridPosition}.
     *
     * @param persistedPosition the persisted position
     * @return the domain position
     */
    private static GridPosition toDomain(final XdGrid.Index persistedPosition) {
        return new GridPosition(persistedPosition.column(), persistedPosition.row());
    }

    /**
     * Determines the width (i.e. the number of columns) of the persisted grid.
     *
     * @param persistedGrid the persisted grid
     * @return the width of the persisted grid
     * @throws PuzzleConversionException if grid is empty
     */
    private static int width(final XdGrid persistedGrid) throws PuzzleConversionException {
        return maxDimension(persistedGrid, XdGrid.Index::column);
    }

    /**
     * Determines the height (i.e. the number of rows) of the persisted grid.
     *
     * @param persistedGrid the persisted grid
     * @return the rows of the persisted grid
     * @throws PuzzleConversionException if grid is empty
     */
    private static int height(final XdGrid persistedGrid) throws PuzzleConversionException {
        return maxDimension(persistedGrid, XdGrid.Index::row);
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
                             persistedGrid.nonFilled())
                         .flatMap(Collection::stream)
                         .map(XdGrid.Index::row)
                         .max(Comparator.naturalOrder())
                         .orElseThrow(() -> new PuzzleConversionException("Invalid empty grid"));
    }
}
