/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.spi.clue.ClueProvider;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleEncoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;

/**
 * Web API, frontend to croiseur.
 */
open module com.gitlab.super7ramp.croiseur.web {
    requires com.fasterxml.jackson.databind;
    requires com.gitlab.super7ramp.croiseur;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.web;
    requires spring.webmvc;
    requires java.logging;
    requires jdk.unsupported; // not required by croiseur.web but by spring.aop, at run-time

    // Component loads service providers itself
    uses ClueProvider;
    uses CrosswordSolver;
    uses DictionaryProvider;
    uses PuzzleDecoder;
    uses PuzzleEncoder;
    uses PuzzleRepository;
}