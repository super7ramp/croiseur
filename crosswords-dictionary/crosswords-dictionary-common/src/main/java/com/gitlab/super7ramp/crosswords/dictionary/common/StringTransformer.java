/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.common;

import java.util.function.UnaryOperator;

/**
 * Applies some changes on an input string.
 */
public interface StringTransformer extends UnaryOperator<String> {
    // Marker interface
}
