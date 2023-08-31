/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.configuration;

import com.gitlab.super7ramp.croiseur.api.CrosswordService;
import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declares core services as Spring beans.
 * <p>
 * Note: The core {@link CrosswordService} does not use Spring beans to get its dependencies: It
 * uses {@link java.util.ServiceLoader ServiceLoader}. This allows core not to depend on Spring.
 * <p>
 * Both systems are used: Core services are declared as Spring beans here so that they can be used
 * in application controllers while
 * {@link com.gitlab.super7ramp.croiseur.web.presenter.WebPresenter application presenter} is
 * declared as a service loadable via ServiceLoader for consumption by core service.
 */
@Configuration
public class ServiceConfiguration {

    /**
     * The root {@link CrosswordService} bean.
     *
     * @return the crossword service
     */
    @Bean
    public CrosswordService crosswordService() {
        return CrosswordService.create();
    }

    /**
     * The {@link SolverService}.
     *
     * @return the solver service
     */
    @Bean
    public SolverService solverService() {
        return crosswordService().solverService();
    }

    /**
     * The {@link PuzzleService} bean.
     *
     * @return the puzzle service
     */
    @Bean
    public PuzzleService puzzleService() {
        return crosswordService().puzzleService();
    }

    /**
     * The {@link PuzzlePersistenceService}.
     *
     * @return puzzle persistence service
     */
    @Bean
    public PuzzlePersistenceService puzzlePersistenceService() {
        return puzzleService().persistence();
    }
}
