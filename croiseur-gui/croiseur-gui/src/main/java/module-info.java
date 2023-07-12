/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;

/**
 * Desktop application, frontend to croiseur.
 */
module com.gitlab.super7ramp.croiseur.gui {

    requires com.gitlab.super7ramp.croiseur.gui.controller;
    requires com.gitlab.super7ramp.croiseur.gui.presenter;
    requires com.gitlab.super7ramp.croiseur.gui.view.model;
    requires com.gitlab.super7ramp.croiseur.gui.view;

    // Actual dependency to core library
    requires com.gitlab.super7ramp.croiseur;

    // GUI loads service providers itself
    uses DictionaryProvider;
    uses CrosswordSolver;
    uses PuzzleDecoder;
    uses PuzzleRepository;

    // JavaFX stuff
    requires javafx.controls;
    requires javafx.fxml;
    exports com.gitlab.super7ramp.croiseur.gui to javafx.graphics;
    opens com.gitlab.super7ramp.croiseur.gui to javafx.fxml;
}