/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Puzzle repository SPI definition.
 */
module com.gitlab.super7ramp.croiseur.spi.puzzle.repository {
    requires transitive com.gitlab.super7ramp.croiseur.common;
    exports com.gitlab.super7ramp.croiseur.spi.puzzle.repository;
}