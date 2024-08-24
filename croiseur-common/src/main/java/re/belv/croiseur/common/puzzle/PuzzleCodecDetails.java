/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.common.puzzle;

import java.util.List;
import java.util.Objects;

/**
 * Details about a puzzle codec.
 *
 * @param name the codec name
 * @param description the codec description
 * @param supportedFormats the supported formats under the form of file extensions, e.g. ["*.xd"] or mimetypes
 */
public record PuzzleCodecDetails(String name, String description, List<String> supportedFormats) {

    /**
     * Validates fields
     *
     * @param name the codec name
     * @param description the codec description
     * @param supportedFormats the supported formats under the form of file extensions, e.g. ["*.xd"] or mimetypes
     */
    public PuzzleCodecDetails {
        Objects.requireNonNull(name);
        Objects.requireNonNull(description);
        Objects.requireNonNull(supportedFormats);
    }
}
