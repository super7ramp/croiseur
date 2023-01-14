/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Presenter SPI definition.
 */
module com.gitlab.super7ramp.crosswords.spi.presenter {
    requires transitive com.gitlab.super7ramp.crosswords.common;
    // TODO remove dependency solver SPI
    requires transitive com.gitlab.super7ramp.crosswords.spi.solver;
    exports com.gitlab.super7ramp.crosswords.spi.presenter;
    exports com.gitlab.super7ramp.crosswords.spi.presenter.solver;
    exports com.gitlab.super7ramp.crosswords.spi.presenter.dictionary;
}