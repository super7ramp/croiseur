/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Interface definition for puzzle codecs.
 */
module com.gitlab.super7ramp.croiseur.spi.puzzle.codec {
    requires transitive com.gitlab.super7ramp.croiseur.common;
    exports com.gitlab.super7ramp.croiseur.spi.puzzle.codec;
}