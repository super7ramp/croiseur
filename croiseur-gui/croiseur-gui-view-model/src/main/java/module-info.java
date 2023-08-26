/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * View-model submodule of croiseur-gui.
 * <p>
 * A model representing the state of the views, observed and displayed by croiseur-gui-view.
 */
module com.gitlab.super7ramp.croiseur.gui.view.model {
    requires transitive javafx.base; // Requiring only javafx.base for javafx.beans
    exports com.gitlab.super7ramp.croiseur.gui.view.model;
    exports com.gitlab.super7ramp.croiseur.gui.view.model.clue;
    exports com.gitlab.super7ramp.croiseur.gui.view.model.dictionary;
    exports com.gitlab.super7ramp.croiseur.gui.view.model.error;
    exports com.gitlab.super7ramp.croiseur.gui.view.model.puzzle.codec;
    exports com.gitlab.super7ramp.croiseur.gui.view.model.puzzle.edition;
    exports com.gitlab.super7ramp.croiseur.gui.view.model.puzzle.edition.slot;
    exports com.gitlab.super7ramp.croiseur.gui.view.model.puzzle.selection;
    exports com.gitlab.super7ramp.croiseur.gui.view.model.solver;
}