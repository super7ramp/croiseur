/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleClues;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * (Almost) end-to-end tests pertaining to the puzzle service.
 */
@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
final class CroiseurWebPuzzleTest {

    /** Some example puzzles. */
    private static final List<Puzzle> EXAMPLE_PUZZLES;

    static {
        final var puzzle1 = new Puzzle(new PuzzleDetails("Puzzle #1",
                                                         "John Doe",
                                                         "(None)",
                                                         "(None)",
                                                         Optional.empty()),
                                       new PuzzleGrid(4, 4, Set.of(), Map.of()),
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

    /** The "server" entry point. */
    @Autowired
    private MockMvc mockMvc;

    /** The test json (un)marshaller. */
    @Autowired
    private JacksonTester<Puzzle> json;

    @Test
    void addPuzzle() throws Exception {
        addPuzzle(EXAMPLE_PUZZLES.get(0));
    }

    @Test
    void getPuzzles_empty() throws Exception {
        mockMvc.perform(get("/puzzles"))
               .andExpect(status().isOk())
               .andExpect(content().string(""));
    }

    @Test
    void getPuzzles_notEmpty() throws Exception {
        for (final Puzzle puzzle : EXAMPLE_PUZZLES) {
            addPuzzle(puzzle);
        }
        // TODO verification
    }

    private void addPuzzle(final Puzzle puzzle) throws Exception {
        final String puzzleJson = json.write(puzzle).getJson();
        mockMvc.perform(post("/puzzles")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(puzzleJson))
               .andExpect(status().isOk())
               .andExpect(content().string("OK"));
    }
}
