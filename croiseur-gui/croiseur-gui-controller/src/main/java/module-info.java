/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Controller submodule of croiseur-gui.
 * <p>
 * Calls croiseur service upon croiseur-gui-view events.
 */
module com.gitlab.super7ramp.croiseur.gui.controller {
    requires transitive com.gitlab.super7ramp.croiseur;
    requires transitive com.gitlab.super7ramp.croiseur.gui.view.model;
    requires java.logging;
    requires javafx.graphics; // for javafx.concurrent

    exports com.gitlab.super7ramp.croiseur.gui.controller.clue;
    exports com.gitlab.super7ramp.croiseur.gui.controller.dictionary;
    exports com.gitlab.super7ramp.croiseur.gui.controller.puzzle;
    exports com.gitlab.super7ramp.croiseur.gui.controller.solver;
}