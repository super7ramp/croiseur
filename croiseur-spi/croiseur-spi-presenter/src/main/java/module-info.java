/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Interface definition for presenters.
 */
module re.belv.croiseur.spi.presenter {
    requires transitive re.belv.croiseur.common;
    exports re.belv.croiseur.spi.presenter;
    exports re.belv.croiseur.spi.presenter.clue;
    exports re.belv.croiseur.spi.presenter.dictionary;
    exports re.belv.croiseur.spi.presenter.puzzle;
    exports re.belv.croiseur.spi.presenter.solver;
}