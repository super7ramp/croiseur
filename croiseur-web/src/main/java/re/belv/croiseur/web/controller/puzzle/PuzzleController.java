/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.web.controller.puzzle;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import re.belv.croiseur.api.puzzle.PuzzleService;
import re.belv.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.web.model.puzzle.PuzzleRequestResponseModel;

import java.net.URI;

/**
 * Controller of the {@link PuzzleService}.
 */
@RestController
@RequestMapping("/puzzles")
public class PuzzleController {

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

    @GetMapping
    @Operation(description = "Lists all saved puzzles")
    @ApiResponse(responseCode = "200", description = "All the saved puzzles")
    public Iterable<SavedPuzzle> getPuzzles() {
        puzzlePersistenceService.list();
        return puzzleRequestResponseModel.puzzles();
    }

    @GetMapping("/{id}")
    @Operation(description = "Gets the saved puzzle matching the given ID")
    @ApiResponse(responseCode = "200", description = "Puzzle found")
    @ApiResponse(responseCode = "400", description = "Puzzle not found", content = @Content)
    public ResponseEntity<SavedPuzzle> getPuzzle(
            @Parameter(description = "The ID of the puzzle to get") @PathVariable("id")
            final long id) {
        puzzlePersistenceService.load(id);
        return ResponseEntity.of(puzzleRequestResponseModel.puzzle());
    }

    @PostMapping
    @Operation(description = "Saves a new puzzle")
    @ApiResponse(responseCode = "201", description = "The URI to the saved puzzle resource")
    public ResponseEntity<URI> addPuzzle(
            @Parameter(description = "The new puzzle to save")
            @RequestBody final Puzzle newPuzzle) {
        puzzlePersistenceService.save(newPuzzle);
        return puzzleRequestResponseModel.puzzle()
                                         .map(puzzle -> ResponseEntity.created(toUri(puzzle)))
                                         .orElseGet(ResponseEntity::internalServerError)
                                         .build();
    }

    @DeleteMapping
    @Operation(description = "Deletes all saved puzzles")
    @ApiResponse(responseCode = "200", description = "All the puzzles deleted", content = @Content)
    public ResponseEntity<String> deleteAll() {
        puzzlePersistenceService.deleteAll();
        return puzzleRequestResponseModel.allPuzzlesDeleted() ? ResponseEntity.ok().build() :
                ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Deletes the saved puzzle matching the given ID")
    @ApiResponse(responseCode = "200", description = "Puzzle deleted", content = @Content)
    @ApiResponse(responseCode = "404", description = "Puzzle to delete not found")
    public ResponseEntity<Object> deletePuzzle(@PathVariable("id") final long id) {
        puzzlePersistenceService.delete(id);
        return puzzleRequestResponseModel.deletedPuzzleIds().stream()
                                         .filter(deletedId -> deletedId.equals(id))
                                         .map(deletedId -> ResponseEntity.ok().build())
                                         .findFirst()
                                         .orElseGet(() -> ResponseEntity.notFound().build());
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
}
