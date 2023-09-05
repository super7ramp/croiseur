/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web;

import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition.at;
import static com.gitlab.super7ramp.croiseur.web.matcher.SolverRunJsonMatcher.terminatedSolverRun;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * (Almost) end-to-end tests pertaining to the solver service.
 */
final class CroiseurWebSolverTest extends CroiseurWebTestBase {

    /** Some example grids. */
    private static final List<PuzzleGrid> EXAMPLE_GRIDS;

    static {
        final var puzzleGrid1 = new PuzzleGrid(4, 4, Set.of(at(0, 0)), Map.of(at(1, 0), 'A'));
        final var puzzleGrid2 = new PuzzleGrid(3, 3, Set.of(at(0, 0)), Map.of(at(1, 0), 'A'));
        final var puzzleGrid3 = new PuzzleGrid(2, 2, Set.of(), Map.of());
        EXAMPLE_GRIDS = List.of(puzzleGrid1, puzzleGrid2, puzzleGrid3);
    }

    /** The test json (de)serializer for {@link PuzzleGrid}s. */
    @Autowired
    private JacksonTester<PuzzleGrid> json;

    /**
     * Verifies a GET on "/solvers" lists the available solvers.
     *
     * @throws Exception should not happen
     */
    @Test
    void listSolvers() throws Exception {
        mockMvc.perform(get("/solvers"))
               .andExpect(status().isOk())
               .andExpect(content().json("""
                                         [{
                                             "name":"Ginsberg",
                                             "description":"A crossword solver based on Ginsberg's papers."
                                         }]
                                         """));
    }

    /**
     * Verifies a POST on "/solvers/runs" with a solve request creates a new solver run.
     *
     * @throws Exception should not happen
     */
    @Test
    void addSolverRun() throws Exception {
        addSolverRun(EXAMPLE_GRIDS.get(0)).andExpect(
                                                  header().string("Location", endsWith("/solvers/runs/1")))
                                          .andExpect(content().string(emptyString()));
    }

    /**
     * Verifies a 404 error is returned when performing a GET on "/solvers/runs/$id" when solver run
     * does not exist.
     *
     * @throws Exception should not happen
     */
    @Test
    void getSolverRun_notFound() throws Exception {
        mockMvc.perform(get("/solvers/runs/1").session(mockHttpSession))
               .andExpect(status().isNotFound());
    }

    /**
     * Verifies a GET on "/solvers/runs" lists the solvers runs - no run case.
     *
     * @throws Exception should not happen
     */
    @Test
    void listRuns_empty() throws Exception {
        mockMvc.perform(get("/solvers/runs").session(mockHttpSession))
               .andExpect(status().isOk())
               .andExpect(content().json("[]"));
    }

    /**
     * Verifies a GET on "/solvers/runs" lists the solvers runs.
     *
     * @throws Exception should not happen
     */
    @Test
    void listRuns_notEmpty() throws Exception {
        addSolverRun(EXAMPLE_GRIDS.get(0));
        // TODO add several runs
        mockMvc.perform(get("/solvers/runs").session(mockHttpSession))
               .andExpect(status().isOk())
               .andExpect(content().string(terminatedSolverRun()));
    }

    /**
     * Performs a POST on "/solvers/runs" with the given puzzle grid, creating a solver run
     * resource. Method verifies the return code is 201.
     *
     * @param grid the puzzle grid for which to create a solver run
     * @return the {@link ResultActions} for further verifications
     * @throws Exception should not happen
     */
    private ResultActions addSolverRun(final PuzzleGrid grid) throws Exception {
        final String puzzleGridJson = json.write(grid).getJson();
        return mockMvc.perform(post("/solvers/runs")
                                       .session(mockHttpSession)
                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                       .content(puzzleGridJson))
                      .andExpect(status().isCreated());
    }
}
