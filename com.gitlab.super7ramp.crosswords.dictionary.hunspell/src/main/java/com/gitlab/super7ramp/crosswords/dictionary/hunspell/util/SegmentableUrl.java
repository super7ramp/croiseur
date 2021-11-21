package com.gitlab.super7ramp.crosswords.dictionary.hunspell.util;

import java.net.URL;

public final class SegmentableUrl {

    private final URL url;

    /**
     * Constructor.
     */
    public SegmentableUrl(final URL anUrl) {
        url = anUrl;
    }

    public String lastPathSegment() {
        final String path = url.getPath();
        if (path.endsWith("/")) {
            return "";
        }
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
