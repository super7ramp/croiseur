/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.presenter;

import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleCodecDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.web.model.PuzzleRequestResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of {@link PuzzlePresenter} for Croiseur Web.
 */
@Component
public final class WebPuzzlePresenter implements PuzzlePresenter {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(WebPuzzlePresenter.class.getName());

    /** The puzzle request response model to update. */
    private final PuzzleRequestResponseModel puzzleRequestResponseModel;

    /**
     * Constructs an instance.
     *
     * @param puzzleRequestResponseModelArg the puzzle request response model to update
     */
    public WebPuzzlePresenter(final PuzzleRequestResponseModel puzzleRequestResponseModelArg) {
        puzzleRequestResponseModel = puzzleRequestResponseModelArg;
    }

    @Override
    public void presentAvailablePuzzles(final List<SavedPuzzle> puzzles) {
        LOGGER.info(() -> "Received available puzzles: " + puzzles);
        puzzleRequestResponseModel.puzzles(puzzles);
    }

    @Override
    public void presentLoadedPuzzle(final SavedPuzzle puzzle) {
        LOGGER.info(() -> "Received loaded puzzle: " + puzzle);
        puzzleRequestResponseModel.puzzle(puzzle);
    }

    @Override
    public void presentPuzzleRepositoryError(final String error) {
        LOGGER.warning(() -> "Received puzzle repository error: " + error);
    }

    @Override
    public void presentSavedPuzzle(final SavedPuzzle puzzle) {
        LOGGER.info(() -> "Received saved puzzle: " + puzzle);
        puzzleRequestResponseModel.puzzle(puzzle);
    }

    @Override
    public void presentDeletedAllPuzzles() {
        LOGGER.info("Received all puzzles deleted notification");
        puzzleRequestResponseModel.allPuzzlesDeleted(true);
    }

    @Override
    public void presentDeletedPuzzle(final long id) {
        LOGGER.info(() -> "Received all puzzle deleted notification for id #" + id);
        puzzleRequestResponseModel.deletedPuzzleId(id);
    }

    @Override
    public void presentPuzzleDecoders(final List<PuzzleCodecDetails> decoders) {
        LOGGER.info(() -> "Received puzzle decoders: " + decoders);
    }

    @Override
    public void presentPuzzleImportError(final String error) {
        LOGGER.warning(() -> "Received puzzle import error: " + error);
    }

    @Override
    public void presentPuzzleEncoders(final List<PuzzleCodecDetails> encoders) {
        LOGGER.info(() -> "Received puzzle encoders: " + encoders);
    }

    @Override
    public void presentPuzzleExportError(final String error) {
        LOGGER.warning(() -> "Received puzzle export error: " + error);
    }

}
