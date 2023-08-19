/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleClues;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdClue;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdClues;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdCrossword;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdGrid;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdMetadata;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecodingException;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition.at;

/**
 * Converts crossword from/to the xd crossword model.
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
        final PuzzleClues clues = toDomain(xdModel.clues());
        return new Puzzle(details, grid, clues);
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
     * Converts the xd clues model to the domain clues model.
     *
     * @param persistenceCluesModel the xd clues model
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

    /**
     * Converts the domain crossword model to the xd crossword model.
     *
     * @param domainModel the domain crossword model
     * @return the xd crossword model
     */
    static XdCrossword toXd(final Puzzle domainModel) {
        final XdMetadata metadata = toXd(domainModel.details());
        final XdGrid grid = toXd(domainModel.grid());
        final XdClues clues = toXd(domainModel.grid(), domainModel.clues());
        return new XdCrossword(metadata, grid, clues);
    }

    /**
     * Converts a domain metadata model to xd metadata model.
     *
     * @param details the domain metadata model
     * @return the persistence metadata model
     */
    static XdMetadata toXd(final PuzzleDetails details) {
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
        return builder.build();
    }

    /**
     * Converts a domain grid model to a xd grid model.
     *
     * @param grid the domain grid model
     * @return the xd grid model
     */
    static XdGrid toXd(final PuzzleGrid grid) {
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
     * Converts a domain clues model to a xd clues model.
     *
     * @param grid  the domain grid model
     * @param clues the domain clues model
     * @return the xd clues model
     */
    private static XdClues toXd(final PuzzleGrid grid, final PuzzleClues clues) {
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
