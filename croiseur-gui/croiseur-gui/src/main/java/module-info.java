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
 * Desktop application, frontend to croiseur.
 */
module re.belv.croiseur.gui {

    // GUI submodules
    requires re.belv.croiseur.gui.controller;
    requires re.belv.croiseur.gui.presenter;
    requires re.belv.croiseur.gui.view;
    requires re.belv.croiseur.gui.view.model;
    // The main dependency: The core library
    requires re.belv.croiseur;
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

    exports re.belv.croiseur.gui;

    opens re.belv.croiseur.gui to
            javafx.fxml;
}
