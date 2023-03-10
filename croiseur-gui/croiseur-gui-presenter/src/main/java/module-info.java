/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Implementation of the Presenter SPI.
 */
module com.gitlab.super7ramp.croiseur.gui.presenter {
    requires transitive com.gitlab.super7ramp.croiseur.common;
    requires transitive com.gitlab.super7ramp.croiseur.spi.presenter;
    requires transitive com.gitlab.super7ramp.croiseur.gui.view.model;
    exports com.gitlab.super7ramp.croiseur.gui.presenter;
    // The following 'provides' should exist but GuiPresenter has no default constructor: The
    // view model it operates on is passed in constructor (for now).
    // provides Presenter with GuiPresenter;

    requires java.logging;
    requires javafx.graphics; // for Platform.runLater()
}