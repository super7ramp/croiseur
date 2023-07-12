/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.common.puzzle;

import java.util.List;
import java.util.Objects;

/**
 * Details about a puzzle codec.
 *
 * @param name             the codec name
 * @param description      the codec description
 * @param supportedFormats the formats supported by the codec (ideally the mimetype, or the file
 *                         extension)
 */
public record PuzzleCodecDetails(String name, String description, List<String> supportedFormats) {

    /**
     * Validates fields
     *
     * @param name             the codec name
     * @param description      the codec description
     * @param supportedFormats the mimetypes or the file extensions (in the format "*.extension")
     *                         supported by the codec
     */
    public PuzzleCodecDetails {
        Objects.requireNonNull(name);
        Objects.requireNonNull(description);
        Objects.requireNonNull(supportedFormats);
    }
}
