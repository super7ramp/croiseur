/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.controller;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.web.model.PuzzleRequestResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Controller of the {@link PuzzleService}.
 */
@RestController
@RequestMapping("/puzzles")
public final class PuzzleController {

    /** The puzzle persistence service that this controller calls. */
    private final PuzzlePersistenceService puzzlePersistenceService;

    /** The puzzle request response model, where the request state is stored. */
    private final PuzzleRequestResponseModel puzzleRequestResponseModel;

    /**
     * Constructs an instance.
     *
     * @param puzzlePersistenceServiceArg   the puzzle persistence service
     * @param puzzleRequestResponseModelArg the puzzle request response model, where the request
     *                                      processing state is stored
     */
    public PuzzleController(final PuzzlePersistenceService puzzlePersistenceServiceArg,
                            final PuzzleRequestResponseModel puzzleRequestResponseModelArg) {
        puzzlePersistenceService = puzzlePersistenceServiceArg;
        puzzleRequestResponseModel = puzzleRequestResponseModelArg;
    }

    /**
     * Lists all saved puzzles.
     *
     * @return all the saved puzzles
     */
    @GetMapping({"", "/"})
    public Iterable<SavedPuzzle> getPuzzles() {
        puzzlePersistenceService.list();
        return puzzleRequestResponseModel.puzzles();
    }

    /**
     * Gets the saved puzzle matching given id.
     *
     * @param id the id of the puzzle to get
     * @return the saved puzzle matching id, if any
     */
    @GetMapping("/{id}")
    public ResponseEntity<SavedPuzzle> getPuzzle(@PathVariable("id") final long id) {
        puzzlePersistenceService.load(id);
        return ResponseEntity.of(puzzleRequestResponseModel.puzzle());
    }

    /**
     * Creates a new puzzle.
     *
     * @param newPuzzle the new puzzle to save
     * @return the URI to saved puzzle
     */
    @PostMapping(value = {"", "/"})
    public ResponseEntity<URI> addPuzzle(@RequestBody final Puzzle newPuzzle) {
        puzzlePersistenceService.save(newPuzzle);
        return puzzleRequestResponseModel.puzzle()
                                         .map(puzzle -> ResponseEntity.created(toUri(puzzle)))
                                         .orElseGet(ResponseEntity::internalServerError)
                                         .build();
    }

    /**
     * Returns the URI of the saved puzzle resource.
     *
     * @param puzzle the saved puzzle
     * @return the URI of the saved puzzle resource
     */
    private URI toUri(final SavedPuzzle puzzle) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(puzzle.id())
                .toUri();
    }

    /**
     * Deletes all saved puzzles.
     *
     * @return 200 if deletion was successful; 500 with body containing details about the error
     * otherwise
     */
    @DeleteMapping({"", "/"})
    public ResponseEntity<String> deleteAll() {
        puzzlePersistenceService.deleteAll();
        return puzzleRequestResponseModel.allPuzzlesDeleted() ? ResponseEntity.ok().build() :
                ResponseEntity.internalServerError().build();
    }

    /**
     * Deletes the saved puzzle matching given id.
     *
     * @param id the id of the puzzle to delete
     * @return 200 if deletion was successful, 404 otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePuzzle(@PathVariable("id") final long id) {
        puzzlePersistenceService.delete(id);
        return puzzleRequestResponseModel.deletedPuzzleIds().stream()
                                         .filter(deletedId -> deletedId.equals(id))
                                         .map(deletedId -> ResponseEntity.ok().build())
                                         .findFirst()
                                         .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
