/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * View submodule of croiseur-gui.
 * <p>
 * Widgets of croiseur-gui.
 */
module com.gitlab.super7ramp.croiseur.gui.view {
    requires transitive com.gitlab.super7ramp.croiseur.gui.view.model;
    requires javafx.controls;
    requires javafx.fxml;

    exports com.gitlab.super7ramp.croiseur.gui.view.clue;
    exports com.gitlab.super7ramp.croiseur.gui.view.dictionary;
    exports com.gitlab.super7ramp.croiseur.gui.view.javafx.scene.control;
    exports com.gitlab.super7ramp.croiseur.gui.view.puzzle.edition;
    exports com.gitlab.super7ramp.croiseur.gui.view.puzzle.selection;
    exports com.gitlab.super7ramp.croiseur.gui.view.solver;

    opens com.gitlab.super7ramp.croiseur.gui.view.clue to javafx.fxml;
    opens com.gitlab.super7ramp.croiseur.gui.view.dictionary to javafx.fxml;
    opens com.gitlab.super7ramp.croiseur.gui.view.puzzle.selection to javafx.fxml;
    opens com.gitlab.super7ramp.croiseur.gui.view.javafx.scene.control to javafx.fxml;
    opens com.gitlab.super7ramp.croiseur.gui.view.solver to javafx.fxml;
    opens com.gitlab.super7ramp.croiseur.gui.view.puzzle.edition to javafx.fxml;
}