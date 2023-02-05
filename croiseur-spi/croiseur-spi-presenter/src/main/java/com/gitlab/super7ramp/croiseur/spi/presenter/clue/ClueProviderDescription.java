/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.presenter.clue;

/**
 * Describes a clue provider.
 *
 * @param name        the clue provider's name
 * @param description a short description
 */
public record ClueProviderDescription(String name, String description) {
    // Nothing to add
}
