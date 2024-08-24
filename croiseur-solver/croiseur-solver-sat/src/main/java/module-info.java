/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Crossword solver library based on a generic SAT solver.
 */
module re.belv.croiseur.solver.sat {
    requires org.ow2.sat4j.core;
    requires org.ow2.sat4j.pb;

    exports re.belv.croiseur.solver.sat;
}
