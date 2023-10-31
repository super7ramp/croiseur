/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import java.util.List;

/**
 * A puzzle codec.
 */
public final class PuzzleCodec {

    /** The codec name. */
    private final String name;

    /** The supported file extensions. */
    private final List<String> extensions;

    /**
     * Constructs an instance.
     *
     * @param nameArg       the codec name
     * @param extensionsArg the supported file extensions
     */
    public PuzzleCodec(final String nameArg, final List<String> extensionsArg) {
        name = nameArg;
        extensions = extensionsArg;
    }

    /**
     * The codec name.
     *
     * @return the codec name
     */
    public String name() {
        return name;
    }

    /**
     * The supported file extensions.
     *
     * @return the supported file extensions
     */
    public List<String> extensions() {
        return extensions;
    }
}
