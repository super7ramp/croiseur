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

    exports com.gitlab.super7ramp.croiseur.gui.view;
    exports com.gitlab.super7ramp.croiseur.gui.view.javafx.scene.control;
    opens com.gitlab.super7ramp.croiseur.gui.view to javafx.fxml;
}