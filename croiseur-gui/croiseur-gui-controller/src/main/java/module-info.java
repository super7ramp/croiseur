/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Controller submodule of croiseur-gui.
 * <p>
 * Calls croiseur service upon croiseur-gui-view events.
 */
module re.belv.croiseur.gui.controller {
    requires transitive re.belv.croiseur;
    requires transitive re.belv.croiseur.gui.view.model;
    requires java.logging;
    requires javafx.graphics; // for javafx.concurrent

    exports re.belv.croiseur.gui.controller.clue;
    exports re.belv.croiseur.gui.controller.dictionary;
    exports re.belv.croiseur.gui.controller.puzzle;
    exports re.belv.croiseur.gui.controller.solver;
}