/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.presenter;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleCodecDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.ErrorsViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.GridCoord;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleCodec;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleCodecsViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleDetailsViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleEditionViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleSelectionViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SavedPuzzleViewModel;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import javafx.application.Platform;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * GUI implementation of {@link PuzzlePresenter}.
 */
final class GuiPuzzlePresenter implements PuzzlePresenter {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GuiPuzzlePresenter.class.getName());

    /** The date time formatter - assuming locale is constant during application run-time. */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

    /** The puzzle selection view model. */
    private final PuzzleSelectionViewModel puzzleSelectionViewModel;

    /** The puzzle details edition view model. */
    private final PuzzleDetailsViewModel puzzleDetailsViewModel;

    /** The crossword grid edition view model. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /** The puzzle codecs view model. */
    private final PuzzleCodecsViewModel puzzleCodecsViewModel;

    /** The errors view model. */
    private final ErrorsViewModel errorsViewModel;

    /**
     * Constructs an instance.
     *
     * @param puzzleSelectionViewModelArg the puzzle selection view model
     * @param puzzleEditionViewModelArg   the puzzle edition view model
     * @param puzzleCodecsViewModelArg    the puzzle codecs view model
     * @param errorsViewModelArg          the errors view model
     */
    GuiPuzzlePresenter(final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
                       final PuzzleEditionViewModel puzzleEditionViewModelArg,
                       final PuzzleCodecsViewModel puzzleCodecsViewModelArg,
                       final ErrorsViewModel errorsViewModelArg) {
        puzzleSelectionViewModel = puzzleSelectionViewModelArg;
        puzzleDetailsViewModel = puzzleEditionViewModelArg.puzzleDetailsViewModel();
        crosswordGridViewModel = puzzleEditionViewModelArg.crosswordGridViewModel();
        puzzleCodecsViewModel = puzzleCodecsViewModelArg;
        errorsViewModel = errorsViewModelArg;
    }

    @Override
    public void presentAvailablePuzzles(final List<SavedPuzzle> puzzles) {
        LOGGER.info(() -> "Received available puzzles: " + puzzles);
        final List<SavedPuzzleViewModel> savedPuzzleViewModels =
                puzzles.stream().map(GuiPuzzlePresenter::convertToViewModel).toList();
        Platform.runLater(() -> puzzleSelectionViewModel.availablePuzzlesProperty()
                                                        .setAll(savedPuzzleViewModels));
    }

    @Override
    public void presentLoadedPuzzle(final SavedPuzzle puzzle) {
        LOGGER.info(() -> "Received loaded puzzle: " + puzzle);
        Platform.runLater(() -> {
            fillDetailsViewModelWith(puzzle.id(), puzzle.revision(), puzzle.details());
            fillGridViewModelWith(puzzle.grid());
        });
    }

    @Override
    public void presentPuzzleRepositoryError(final String error) {
        LOGGER.warning(() -> "Received puzzle repository error: " + error);
        Platform.runLater(() -> errorsViewModel.addError(error));
    }

    @Override
    public void presentSavedPuzzle(final SavedPuzzle puzzle) {
        LOGGER.info(() -> "Received saved puzzle: " + puzzle);
        final SavedPuzzleViewModel savedPuzzleViewModel = convertToViewModel(puzzle);
        Platform.runLater(
                () -> puzzleSelectionViewModel.updateAvailablePuzzlesWith(savedPuzzleViewModel));
    }

    @Override
    public void presentDeletedAllPuzzles() {
        Platform.runLater(() -> puzzleSelectionViewModel.availablePuzzlesProperty().clear());
    }

    @Override
    public void presentDeletedPuzzle(final long id) {
        Platform.runLater(() -> puzzleSelectionViewModel.removeAvailablePuzzleWithId(id));
    }

    @Override
    public void presentPuzzleDecoders(final List<PuzzleCodecDetails> decoders) {
        LOGGER.info(() -> "Received puzzle decoders: " + decoders);
        final List<PuzzleCodec> decoderViewModels =
                decoders.stream().map(GuiPuzzlePresenter::convertToViewModel).toList();
        Platform.runLater(() -> puzzleCodecsViewModel.decodersProperty().setAll(decoderViewModels));
    }

    @Override
    public void presentPuzzleImportError(final String error) {
        LOGGER.warning(() -> "Received puzzle import error: " + error);
        Platform.runLater(() -> errorsViewModel.addError(error));
    }

    @Override
    public void presentPuzzleEncoders(final List<PuzzleCodecDetails> encoders) {
        LOGGER.info(() -> "Received puzzle encoders: " + encoders);
        final List<PuzzleCodec> encoderViewModels =
                encoders.stream().map(GuiPuzzlePresenter::convertToViewModel).toList();
        Platform.runLater(() -> puzzleCodecsViewModel.encodersProperty().setAll(encoderViewModels));
    }

    @Override
    public void presentPuzzleExportError(final String error) {
        LOGGER.warning(() -> "Received puzzle export error: " + error);
        Platform.runLater(() -> errorsViewModel.addError(error));
    }

    /**
     * Converts puzzle from domain type to view-model type.
     *
     * @param puzzle the puzzle
     * @return the converted puzzle
     */
    private static SavedPuzzleViewModel convertToViewModel(final SavedPuzzle puzzle) {
        final PuzzleDetails details = puzzle.details();
        final var builder =
                new SavedPuzzleViewModel.Builder().id(puzzle.id()).revision(puzzle.revision())
                                                  .title(details.title())
                                                  .author(details.author())
                                                  .editor(details.editor())
                                                  .copyright(details.copyright())
                                                  .date(details.date()
                                                               .map(DATE_FORMATTER::format)
                                                               .orElse(""))
                                                  .numberOfColumns(puzzle.grid().width())
                                                  .numberOfRows(puzzle.grid().height());
        puzzle.grid().shaded().forEach(pos -> builder.shaded(gridCoordFrom(pos)));
        puzzle.grid().filled()
              .forEach((pos, letter) -> builder.filled(gridCoordFrom(pos), letter));
        return builder.build();
    }

    /**
     * Converts puzzle codec details from domain type to view-model type.
     *
     * @param codecDetails the domain puzzle codec details
     * @return the puzzle codec details converted to view model type
     */
    private static PuzzleCodec convertToViewModel(final PuzzleCodecDetails codecDetails) {
        final String joinedFormats = String.join(", ", codecDetails.supportedFormats());
        final String prettyName = codecDetails.name() + " files (" + joinedFormats + ")";
        return new PuzzleCodec(prettyName, codecDetails.supportedFormats());
    }

    /**
     * Synchronizes the puzzle details view model with given puzzle details.
     *
     * @param id       the puzzle id
     * @param revision the puzzle revision
     * @param details  the other puzzle details
     */
    private void fillDetailsViewModelWith(final long id, final int revision,
                                          final PuzzleDetails details) {
        puzzleDetailsViewModel.id(id);
        puzzleDetailsViewModel.revision(revision);
        puzzleDetailsViewModel.title(details.title());
        puzzleDetailsViewModel.author(details.author());
        puzzleDetailsViewModel.editor(details.editor());
        puzzleDetailsViewModel.copyright(details.copyright());
        details.date().map(DATE_FORMATTER::format).ifPresent(puzzleDetailsViewModel::date);
    }

    /**
     * Synchronizes the grid view model with given puzzle grid.
     *
     * @param grid the puzzle grid
     */
    private void fillGridViewModelWith(final PuzzleGrid grid) {
        crosswordGridViewModel.resizeTo(grid.width(), grid.height());

        final Set<GridCoord> positionsToUpdate =
                new HashSet<>(crosswordGridViewModel.boxesProperty().keySet());

        grid.filled().forEach((position, letter) -> {
            positionsToUpdate.remove(gridCoordFrom(position));
            final CrosswordBoxViewModel box =
                    crosswordGridViewModel.box(gridCoordFrom(position));
            box.lighten();
            box.userContent(String.valueOf(letter));
        });

        grid.shaded().forEach(position -> {
            positionsToUpdate.remove(gridCoordFrom(position));
            crosswordGridViewModel.box(gridCoordFrom(position)).shade();
        });

        positionsToUpdate.stream().map(crosswordGridViewModel::box).forEach(box -> {
            box.lighten();
            box.userContent("");
        });
    }

    private static GridCoord gridCoordFrom(final GridPosition domainPosition) {
        return new GridCoord(domainPosition.x(), domainPosition.y());
    }

}
