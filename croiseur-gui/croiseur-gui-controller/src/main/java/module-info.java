/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * GUI controller: Basically transforms events from the view into events for the croiseur library.
 */
module com.gitlab.super7ramp.croiseur.gui.controller {
    requires transitive com.gitlab.super7ramp.croiseur;
    requires transitive com.gitlab.super7ramp.croiseur.gui.view.model;
    exports com.gitlab.super7ramp.croiseur.gui.controller.dictionary;
    exports com.gitlab.super7ramp.croiseur.gui.controller.solver;

    requires java.logging;
    requires javafx.graphics; // for javafx.concurrent
}