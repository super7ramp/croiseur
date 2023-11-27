/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.spi.clue.ClueProvider;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecoder;
import re.belv.croiseur.spi.puzzle.codec.PuzzleEncoder;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;
import re.belv.croiseur.spi.solver.CrosswordSolver;

/**
 * Web API, frontend to croiseur.
 */
open module re.belv.croiseur.web {
    requires com.fasterxml.jackson.databind;
    requires io.swagger.v3.oas.annotations;
    requires re.belv.croiseur;
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