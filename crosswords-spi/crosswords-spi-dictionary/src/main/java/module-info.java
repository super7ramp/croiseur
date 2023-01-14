/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Dictionary SPI definition.
 */
module com.gitlab.super7ramp.crosswords.spi.dictionary {
    requires transitive com.gitlab.super7ramp.crosswords.common;
    exports com.gitlab.super7ramp.crosswords.spi.dictionary;
}