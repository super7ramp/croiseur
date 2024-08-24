/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Presenter submodule of croiseur-gui.
 * <p>
 * Presents croiseur outputs by updating croiseur-gui-view-model.
 */
module re.belv.croiseur.gui.presenter {
    requires transitive re.belv.croiseur.spi.presenter;
    requires transitive re.belv.croiseur.gui.view.model;
    requires java.logging;
    requires javafx.graphics; // for Platform.runLater()

    exports re.belv.croiseur.gui.presenter;
// The following 'provides' should exist but GuiPresenter has no default constructor: The
// view model it operates on is passed in constructor (for now).
// provides Presenter with GuiPresenter;
}
