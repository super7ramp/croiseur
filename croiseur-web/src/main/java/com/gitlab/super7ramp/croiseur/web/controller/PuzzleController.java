/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.controller;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.web.session.model.PuzzleSessionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controller of the {@link PuzzleService}.
 */
@RestController
@RequestMapping("/puzzles")
public final class PuzzleController {

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
    public PuzzleController(final PuzzlePersistenceService puzzlePersistenceServiceArg,
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
     * Creates a new puzzle.
     *
     * @param newPuzzle the new puzzle to save
     * @return the String "OK" if creation was successful
     */
    @PostMapping(value = {"", "/"})
    public ResponseEntity<String> addPuzzle(@RequestBody final Puzzle newPuzzle) {
        puzzlePersistenceService.save(newPuzzle);
        return puzzleSessionModel.error()
                                 .map(error -> ResponseEntity.notFound().<String>build())
                                 .orElseGet(() -> ResponseEntity.ok("OK"));
    }

    /**
     * Deletes all saved puzzles.
     *
     * @return the String "OK" if deletion was successful
     */
    @DeleteMapping({"", "/"})
    public ResponseEntity<String> deleteAll() {
        puzzlePersistenceService.deleteAll();
        return puzzleSessionModel.error()
                                 .map(error -> ResponseEntity.internalServerError().<String>build())
                                 .orElseGet(() -> ResponseEntity.ok("OK"));
    }

    /**
     * Deletes the saved puzzle matching given id.
     *
     * @param id the id of the puzzle to delete
     * @return the string "OK" deletion was successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePuzzle(@PathVariable("id") final long id) {
        puzzlePersistenceService.delete(id);
        return puzzleSessionModel.error()
                                 .map(error -> ResponseEntity.notFound().<String>build())
                                 .orElseGet(() -> ResponseEntity.ok("OK"));
    }
}
