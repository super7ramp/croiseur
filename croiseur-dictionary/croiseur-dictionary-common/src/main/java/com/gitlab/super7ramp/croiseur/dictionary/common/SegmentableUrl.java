/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.common;

import java.net.URL;

/**
 * A utility to segment URLs.
 */
// FIXME strange class; probably unnecessary now we don't read dictionaries from jar resources
public final class SegmentableUrl {

    /** The URL to segment. */
    private final URL url;

    /**
     * Constructs an instance.
     *
     * @param anUrl the URL to segment
     */
    public SegmentableUrl(final URL anUrl) {
        url = anUrl;
    }

    /**
     * Returns the last segment of the URL path.
     *
     * @return the last segment of the URL path
     */
    public String lastPathSegment() {
        final String path = url.getPath();
        final String lastPathSegment;
        if (path.endsWith("/")) {
            lastPathSegment = "";
        } else {
            lastPathSegment = path.substring(path.lastIndexOf('/') + 1);
        }
        return lastPathSegment;
    }
}
