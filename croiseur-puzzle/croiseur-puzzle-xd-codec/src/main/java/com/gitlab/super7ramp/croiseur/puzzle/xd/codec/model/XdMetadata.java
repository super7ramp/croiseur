/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.xd.codec.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class XdMetadata {

    private final Map<String, String> metadata;

    XdMetadata() {
        metadata = new HashMap<>();
    }

    public Map<String,String> all() {
        return Collections.unmodifiableMap(metadata);
    }

    public String author() {
        return metadata.get("Author");
    }

    public String copyright() {
        return metadata.get("Copyright");
    }

    public String editor() {
        return metadata.get("Editor");
    }

    public String title() {
        return metadata.get("Title");
    }

    public String rawDate() {
        return metadata.get("Date");
    }

    public LocalDate date() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
