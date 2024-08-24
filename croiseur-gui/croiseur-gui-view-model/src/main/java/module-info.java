/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * View-model submodule of croiseur-gui.
 *
 * <p>A model representing the state of the views, observed and displayed by croiseur-gui-view.
 */
module re.belv.croiseur.gui.view.model {
    requires transitive javafx.base; // Requiring only javafx.base for javafx.beans

    exports re.belv.croiseur.gui.view.model;
    exports re.belv.croiseur.gui.view.model.slot;
}
