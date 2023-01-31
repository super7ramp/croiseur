/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * The views and some controls.
 */
module com.gitlab.super7ramp.croiseur.gui.view {
    requires transitive com.gitlab.super7ramp.croiseur.common;
    requires transitive com.gitlab.super7ramp.croiseur.gui.view.model;
    exports com.gitlab.super7ramp.croiseur.gui.view;
    opens com.gitlab.super7ramp.croiseur.gui.view to javafx.fxml;

    requires javafx.controls;
    requires javafx.fxml;
}