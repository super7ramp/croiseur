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
 * Desktop application, frontend to croiseur.
 */
module com.gitlab.super7ramp.croiseur.gui {

    // GUI submodules
    requires com.gitlab.super7ramp.croiseur.gui.controller;
    requires com.gitlab.super7ramp.croiseur.gui.presenter;
    requires com.gitlab.super7ramp.croiseur.gui.view;
    requires com.gitlab.super7ramp.croiseur.gui.view.model;
    // The main dependency: The core library
    requires com.gitlab.super7ramp.croiseur;
    // JavaFX stuff
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    // GUI loads service providers itself
    uses ClueProvider;
    uses CrosswordSolver;
    uses DictionaryProvider;
    uses PuzzleDecoder;
    uses PuzzleEncoder;
    uses PuzzleRepository;

    exports com.gitlab.super7ramp.croiseur.gui;
    opens com.gitlab.super7ramp.croiseur.gui to javafx.fxml;
}