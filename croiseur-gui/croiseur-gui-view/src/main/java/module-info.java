/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * View submodule of croiseur-gui.
 *
 * <p>Widgets of croiseur-gui.
 */
module re.belv.croiseur.gui.view {
    requires transitive re.belv.croiseur.gui.view.model;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;

    exports re.belv.croiseur.gui.view;
    exports re.belv.croiseur.gui.view.javafx.scene.control;

    opens re.belv.croiseur.gui.view to
            javafx.fxml;
    opens re.belv.croiseur.gui.view.javafx.scene.control to
            javafx.fxml;
}
