/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * The GUI view models.
 */
module com.gitlab.super7ramp.croiseur.gui.view.model {
    requires transitive com.gitlab.super7ramp.croiseur.common;
    exports com.gitlab.super7ramp.croiseur.gui.view.model;

    requires transitive javafx.base; // Requiring only javafx.base for javafx.beans
}