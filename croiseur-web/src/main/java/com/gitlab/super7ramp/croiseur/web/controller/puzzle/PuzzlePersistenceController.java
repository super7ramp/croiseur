/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.web.session.model.PuzzleSessionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controller of the {@link PuzzleService}.
 */
@RestController
@RequestMapping("/puzzles")
public final class PuzzlePersistenceController {

    /** The puzzle persistence service that this controller calls. */
    private final PuzzlePersistenceService puzzlePersistenceService;

    /** The puzzle session model, where the session state is stored. */
    private final PuzzleSessionModel puzzleSessionModel;

    /**
     * Constructs an instance.
     *
     * @param puzzlePersistenceServiceArg the puzzle persistence service
     * @param puzzleSessionModelArg       the puzzle session model, where the session state is
     *                                    stored
     */
    public PuzzlePersistenceController(final PuzzlePersistenceService puzzlePersistenceServiceArg,
                                       final PuzzleSessionModel puzzleSessionModelArg) {
        puzzlePersistenceService = puzzlePersistenceServiceArg;
        puzzleSessionModel = puzzleSessionModelArg;
    }

    /**
     * Lists all saved puzzles.
     *
     * @return all the saved puzzles
     */
    @GetMapping({"", "/"})
    public Iterable<SavedPuzzle> getPuzzles() {
        puzzlePersistenceService.list();
        return puzzleSessionModel.puzzles();
    }

    /**
     * Gets the saved puzzle matching given id.
     *
     * @param id the id of the puzzle to get
     * @return the saved puzzle matching id, if any
     */
    @GetMapping("/{id}")
    public ResponseEntity<SavedPuzzle> getPuzzle(@PathVariable("id") final long id) {
        puzzleSessionModel.resetLoadedPuzzle();
        puzzlePersistenceService.load(id);
        final Optional<SavedPuzzle> puzzle = puzzleSessionModel.loadedPuzzle();
        return ResponseEntity.of(puzzle);
    }

    /**
     * Deletes the saved puzzle matching given id.
     *
     * @param id the id of the puzzle to delete
     * @return a string containing "OK" if puzzle has been successfully deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePuzzle(@PathVariable("id") final long id) {
        puzzlePersistenceService.delete(id);
        return puzzleSessionModel.error()
                                 .map(error -> new ResponseEntity<String>(HttpStatus.NOT_FOUND))
                                 .orElseGet(() -> ResponseEntity.ok("OK"));
    }
}
