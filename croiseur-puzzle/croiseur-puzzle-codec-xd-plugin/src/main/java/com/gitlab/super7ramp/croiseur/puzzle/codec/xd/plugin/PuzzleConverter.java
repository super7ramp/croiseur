/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdCrossword;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdGrid;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdMetadata;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecodingException;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Converts crossword from/to persistence format (xd).
 */
final class PuzzleConverter {

    /** Private constructor to prevent instantiate, static methods only. */
    private PuzzleConverter() {
        // Nothing to do.
    }

    /**
     * Converts the xd crossword model to the domain crossword model.
     *
     * @param xdModel the xd crossword model
     * @return the domain crossword model
     * @throws PuzzleDecodingException if conversion fails
     */
    static Puzzle toDomain(final XdCrossword xdModel) throws PuzzleDecodingException {
        final PuzzleDetails details = toDomain(xdModel.metadata());
        final PuzzleGrid grid = toDomain(xdModel.grid());
        return new Puzzle(details, grid);
    }

    /**
     * Converts a xd grid model to a domain grid model
     *
     * @param persistenceGridModel the xd grid model
     * @return the domain grid model
     * @throws PuzzleDecodingException if conversion fails
     */
    private static PuzzleGrid toDomain(final XdGrid persistenceGridModel)
            throws PuzzleDecodingException {
        if (!persistenceGridModel.spaces().isEmpty()) {
            throw new PuzzleDecodingException(
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
     * Converts the xd metadata model to the domain metadata model
     *
     * @param persistenceMetadataModel the xd metadata model
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
     * Converts a grid position from xd model to domain model.
     *
     * @param persistenceGridPosition the grid position from persistence model
     * @return the grid position in domain model
     */
    private static GridPosition toDomain(final XdGrid.Index persistenceGridPosition) {
        return new GridPosition(persistenceGridPosition.column(), persistenceGridPosition.row());
    }

    /**
     * Determines the width (i.e. the number of columns) of the xd grid model.
     *
     * @param persistenceGridModel the persistence grid model
     * @return the width of the grid
     * @throws PuzzleDecodingException if grid is empty
     */
    private static int width(final XdGrid persistenceGridModel) throws PuzzleDecodingException {
        return maxDimension(persistenceGridModel, XdGrid.Index::column);
    }

    /**
     * Determines the height (i.e. the number of rows) of the xd grid model.
     *
     * @param persistenceGridModel the persistence grid model
     * @return the height of the grid
     * @throws PuzzleDecodingException if grid is empty
     */
    private static int height(final XdGrid persistenceGridModel) throws PuzzleDecodingException {
        return maxDimension(persistenceGridModel, XdGrid.Index::row);
    }

    /**
     * Utility method to determine the max dimension (width or height) of the given persisted grid.
     *
     * @param persistedGrid the persisted grid
     * @param dimension     the dimension of the grid for which get the max value
     * @return the max value of the given dimension of the given grid
     * @throws PuzzleDecodingException if grid is empty
     */
    private static int maxDimension(final XdGrid persistedGrid,
                                    final Function<XdGrid.Index, Integer> dimension)
            throws PuzzleDecodingException {
        return 1 + Stream.of(persistedGrid.blocks(), persistedGrid.filled().keySet(),
                             persistedGrid.nonFilled()).flatMap(Collection::stream).map(dimension)
                         .max(Comparator.naturalOrder())
                         .orElseThrow(() -> new PuzzleDecodingException("Invalid empty grid"));
    }
}
