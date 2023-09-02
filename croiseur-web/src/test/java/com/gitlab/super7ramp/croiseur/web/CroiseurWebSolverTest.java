/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * (Almost) end-to-end tests pertaining to the solver service.
 */
final class CroiseurWebSolverTest extends CroiseurWebTestBase {

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
}
