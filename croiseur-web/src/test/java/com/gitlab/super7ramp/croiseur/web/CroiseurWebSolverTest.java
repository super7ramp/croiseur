/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web;

import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;
import java.util.Set;

import static com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition.at;
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

    /** An example grid used by several tests. */
    private static final PuzzleGrid EXAMPLE_GRID =
            new PuzzleGrid(4, 4, Set.of(at(0, 0)), Map.of(at(1, 0), 'A'));

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
        addSolverRun(EXAMPLE_GRID).andExpect(
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
        mockMvc.perform(get("/solvers/runs/1"))
               .andExpect(status().isNotFound());
    }

    /**
     * Verifies a GET on "/solvers/runs" lists the solvers runs - no run case.
     *
     * @throws Exception should not happen
     */
    @Test
    void listRuns_empty() throws Exception {
        mockMvc.perform(get("/solvers/runs"))
               .andExpect(status().isOk())
               .andExpect(content().json("[]"));
    }

    /**
     * Verifies a GET on "/solvers/runs" lists the solvers runs.
     *
     * @throws Exception should not happen
     */
    @Test
    @Disabled("mockMvc doesn't fill JSESSIONID out-of-the box, so session scope bean does not work")
    void listRuns_notEmpty() throws Exception {
        addSolverRun(EXAMPLE_GRID);
        mockMvc.perform(get("/solvers/runs"))
               .andExpect(status().isOk())
               .andExpect(content().json("[something]"));
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
                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                       .content(puzzleGridJson))
                      .andExpect(status().isCreated());
    }
}
