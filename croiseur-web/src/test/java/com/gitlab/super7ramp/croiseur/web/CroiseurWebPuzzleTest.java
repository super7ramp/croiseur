/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleClues;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition.at;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * (Almost) end-to-end tests pertaining to the puzzle service.
 */
final class CroiseurWebPuzzleTest extends CroiseurWebTestBase {

    /** Some example puzzles. */
    private static final List<Puzzle> EXAMPLE_PUZZLES;

    static {
        final var puzzle1 = new Puzzle(new PuzzleDetails("Puzzle #1",
                                                         "John Doe",
                                                         "(None)",
                                                         "(None)",
                                                         Optional.empty()),
                                       new PuzzleGrid(4, 4, Set.of(at(1, 0)),
                                                      Map.of(at(0, 0), 'A')),
                                       PuzzleClues.empty());
        final var puzzle2 = new Puzzle(new PuzzleDetails("Puzzle #2",
                                                         "Jane Doe",
                                                         "(None)",
                                                         "(None)",
                                                         Optional.empty()),
                                       new PuzzleGrid(3, 6, Set.of(), Map.of()),
                                       PuzzleClues.empty());
        final var puzzle3 = new Puzzle(new PuzzleDetails("Puzzle #3",
                                                         "Antoine B.",
                                                         "(None)",
                                                         "(None)",
                                                         Optional.of(LocalDate.of(2023, 9, 1))),
                                       new PuzzleGrid(6, 7, Set.of(), Map.of()),
                                       PuzzleClues.empty());
        EXAMPLE_PUZZLES = List.of(puzzle1, puzzle2, puzzle3);
    }

    /**
     * Verifies puzzle creation with a POST on "/puzzles".
     *
     * @throws Exception should not happen
     */
    @Test
    void addPuzzle() throws Exception {
        addPuzzle(EXAMPLE_PUZZLES.get(0))
                .andExpect(header().string("Location", endsWith("/puzzles/1")))
                .andExpect(content().string(emptyString()));
    }

    /**
     * Verifies a 404 error is returned when performing a GET on "/puzzles/$id" when puzzle does not
     * exist.
     *
     * @throws Exception should not happen
     */
    @Test
    void getPuzzle_notFound() throws Exception {
        mockMvc.perform(get("/puzzles/1"))
               .andExpect(status().isNotFound());
    }

    /**
     * Verifies puzzle listing with a GET on "/puzzles" - no puzzle case.
     *
     * @throws Exception should not happen
     */
    @Test
    void getPuzzles_empty() throws Exception {
        mockMvc.perform(get("/puzzles"))
               .andExpect(status().isOk())
               .andExpect(content().json("[]"));
    }

    /**
     * Verifies puzzle listing with a GET on "/puzzles".
     *
     * @throws Exception should not happen
     */
    @Test
    void getPuzzles_notEmpty() throws Exception {
        for (final Puzzle puzzle : EXAMPLE_PUZZLES) {
            addPuzzle(puzzle);
        }
        mockMvc.perform(get("/puzzles"))
               .andExpect(status().isOk())
               .andExpect(content().json(
                       """
                       [{
                           "id":1,
                           "data":{
                               "details":{
                                   "title":"Puzzle #1",
                                   "author":"John Doe",
                                   "editor":"(None)",
                                   "copyright":"(None)",
                                   "date":null
                               },
                               "grid":{
                                   "width":4,
                                   "height":4,
                                   "shaded":[{"x":1,"y":0}],
                                   "filled":{"GridPosition{x=0, y=0}":"A"}
                               },
                               "clues":{
                                   "across":[],
                                   "down":[]
                               }
                           },
                           "revision":1
                       },
                       {
                           "id":2,
                           "data":{
                               "details":{
                                   "title":"Puzzle #2",
                                   "author":"Jane Doe",
                                   "editor":"(None)",
                                   "copyright":"(None)",
                                   "date":null
                               },
                               "grid":{
                                   "width":3,
                                   "height":6,
                                   "shaded":[],
                                   "filled":{}
                               },
                               "clues":{
                                   "across":[],
                                   "down":[]
                               }
                           },
                           "revision":1
                       },
                       {
                           "id":3,
                           "data":{
                               "details":{
                                   "title":"Puzzle #3",
                                   "author":"Antoine B.",
                                   "editor":"(None)",
                                   "copyright":"(None)",
                                   "date":"2023-09-01"
                               },
                               "grid":{
                                   "width":6,
                                   "height":7,
                                   "shaded":[],
                                   "filled":{}
                               },
                               "clues":{
                                   "across":[],
                                   "down":[]
                               }
                           },
                           "revision":1
                       }]
                       """));
    }

    /**
     * Verifies single puzzle deletion - puzzle not found case.
     *
     * @throws Exception should not happen
     */
    @Test
    void delete_notFound() throws Exception {
        mockMvc.perform(delete("/puzzles/1"))
               .andExpect(status().isNotFound());
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clear database between each test
        mockMvc.perform(delete("/puzzles"));
    }

    /**
     * Performs a POST on /puzzles with the given puzzle, creating the resource. Method verifies the
     * return code to be 201.
     *
     * @param puzzle the puzzle to save
     * @return the {@link ResultActions} for further verifications
     * @throws Exception should not happen
     */
    private ResultActions addPuzzle(final Puzzle puzzle) throws Exception {
        final String puzzleJson = json.write(puzzle).getJson();
        return mockMvc.perform(post("/puzzles")
                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                       .content(puzzleJson))
                      .andExpect(status().isCreated());
    }
}
