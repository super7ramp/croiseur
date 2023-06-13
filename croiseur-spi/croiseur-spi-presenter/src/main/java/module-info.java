/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Interface definition for presenters.
 */
module com.gitlab.super7ramp.croiseur.spi.presenter {
    requires transitive com.gitlab.super7ramp.croiseur.common;
    // TODO remove dependency solver SPI
    requires transitive com.gitlab.super7ramp.croiseur.spi.solver;
    exports com.gitlab.super7ramp.croiseur.spi.presenter;
    exports com.gitlab.super7ramp.croiseur.spi.presenter.solver;
    exports com.gitlab.super7ramp.croiseur.spi.presenter.dictionary;
}