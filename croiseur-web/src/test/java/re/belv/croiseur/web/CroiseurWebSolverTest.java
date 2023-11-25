/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import re.belv.croiseur.common.puzzle.PuzzleGrid;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.emptyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static re.belv.croiseur.common.puzzle.GridPosition.at;
import static re.belv.croiseur.web.matcher.SolverRunJsonMatcher.createdSolverRun;
import static re.belv.croiseur.web.matcher.SolverRunJsonMatcher.interruptedSolverRun;
import static re.belv.croiseur.web.matcher.SolverRunJsonMatcher.runningSolverRun;
import static re.belv.croiseur.web.matcher.SolverRunJsonMatcher.terminatedSolverRun;
import static re.belv.croiseur.web.matcher.SolverRunUriMatcher.isValidSolverRunUri;

/**
 * (Almost) end-to-end tests pertaining to the solver service.
 */
final class CroiseurWebSolverTest extends CroiseurWebTestBase {

    /** A simple grid. */
    private static final PuzzleGrid GRID_SIMPLE = new PuzzleGrid(2, 2, Set.of(), Map.of());

    /** A medium grid. */
    private static final PuzzleGrid GRID_MEDIUM =
            new PuzzleGrid(6, 7, Set.of(at(1, 1), at(5, 1)), Map.of());

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
        postSolverRun(GRID_SIMPLE).andExpect(header().string("Location", isValidSolverRunUri()))
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
        mockMvc.perform(get("/solvers/runs/404").session(mockHttpSession))
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
        postSolverRun(GRID_SIMPLE);
        mockMvc.perform(get("/solvers/runs").session(mockHttpSession))
               .andExpect(status().isOk())
               .andExpect(content().string(createdSolverRun()));
    }

    /**
     * Verifies a GET on "/solvers/runs/{id}" displays the solver run details - not found case.
     *
     * @throws Exception should not happen
     */
    @Test
    void getRun_notFound() throws Exception {
        mockMvc.perform(get("/solvers/runs/404").session(mockHttpSession))
               .andExpect(status().isNotFound());
    }

    /**
     * Verifies a GET on "/solvers/runs/{id}" displays the solver run details - created case.
     *
     * @throws Exception should not happen
     */
    @Test
    void getRun_created() throws Exception {
        final String uri = postSolverRunAndReturnUri(GRID_SIMPLE);
        mockMvc.perform(get(uri).session(mockHttpSession))
               .andExpect(status().isOk())
               .andExpect(content().string(createdSolverRun()));
    }

    /**
     * Verifies a GET on "/solvers/runs/{id}" displays the solver run details - running case.
     *
     * @throws Exception should not happen
     */
    @Test
    void getRun_running() throws Exception {
        final String uri = postSolverRunAndReturnUri(GRID_MEDIUM);
        await(Duration.ofSeconds(3));
        mockMvc.perform(get(uri).session(mockHttpSession))
               .andExpect(status().isOk())
               .andExpect(content().string(runningSolverRun()));
    }

    /**
     * Verifies a GET on "/solvers/runs/{id}" displays the solver run details - terminated case.
     *
     * @throws Exception should not happen
     */
    @Test
    void getRun_terminated() throws Exception {
        final String uri = postSolverRunAndReturnUri(GRID_SIMPLE);
        await(Duration.ofSeconds(5));
        mockMvc.perform(get(uri).session(mockHttpSession))
               .andExpect(status().isOk())
               .andExpect(content().string(terminatedSolverRun()));
    }

    @Test
    @Disabled("timeout not implemented yet")
    void getRun_interrupted() throws Exception {
        final String uri = postSolverRunAndReturnUri(GRID_MEDIUM);
        // Grid is hard: It takes more than 30s to solve, so it is interrupted when performing a GET
        await(Duration.ofSeconds(30));
        mockMvc.perform(get(uri).session(mockHttpSession))
               .andExpect(status().isOk())
               .andExpect(content().string(interruptedSolverRun()));
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clear solver runs stored in session state between each test
        mockMvc.perform(delete("/solvers/runs"))
               .andExpect(status().isOk());
    }

    /**
     * Performs a POST on "/solvers/runs" with the given puzzle grid, creating a solver run
     * resource. Method verifies the return code is 201.
     *
     * @param grid the puzzle grid for which to create a solver run
     * @return the {@link ResultActions} for further verifications
     * @throws Exception should not happen
     */
    private ResultActions postSolverRun(final PuzzleGrid grid) throws Exception {
        final String puzzleGridJson = json.write(grid).getJson();
        return mockMvc.perform(post("/solvers/runs")
                                       .session(mockHttpSession)
                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                       .content(puzzleGridJson))
                      .andExpect(status().isCreated());
    }

    /**
     * Same as {@link #addSolverRun()}, but directly  returns the URI of the created resource.
     * <p>
     * Useful when no further verification is needed.
     *
     * @param grid the puzzle grid for which to create a solver run
     * @return the content of the "Location" field of the response header, or {@code null} if none
     * @throws Exception should not happen
     */
    private String postSolverRunAndReturnUri(final PuzzleGrid grid) throws Exception {
        final MvcResult result = postSolverRun(grid).andReturn();
        return result.getResponse().getHeader("Location");
    }

    /**
     * Sleeps for the given duration.
     *
     * @param duration the sleep duration
     * @throws InterruptedException if interrupted while sleeping
     */
    private void await(final Duration duration) throws InterruptedException {
        Thread.sleep(duration.toMillis());
    }
}
