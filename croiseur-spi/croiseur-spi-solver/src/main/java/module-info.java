/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Interface definition for solvers.
 */
module com.gitlab.super7ramp.croiseur.spi.solver {
    requires transitive com.gitlab.super7ramp.croiseur.common;
    exports com.gitlab.super7ramp.croiseur.spi.solver;
}