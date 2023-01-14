/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * GUI controller: Basically transforms events from the view into events for the crossword library.
 */
module com.gitlab.super7ramp.crosswords.gui.controller {
    requires transitive com.gitlab.super7ramp.crosswords;
    requires transitive com.gitlab.super7ramp.crosswords.gui.view.model;
    exports com.gitlab.super7ramp.crosswords.gui.controller.dictionary;
    exports com.gitlab.super7ramp.crosswords.gui.controller.solver;

    requires java.logging;
    requires javafx.graphics; // for javafx.concurrent
}