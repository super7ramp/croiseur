/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.writer;

import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdMetadata;

import java.util.Objects;

/**
 * Encodes {@link XdMetadata} to its textual representation.
 */
final class XdMetadataWriter {

    /**
     * Constructs an instance.
     */
    XdMetadataWriter() {
        // Nothing to do.
    }

    /**
     * Writes the given metadata to a string.
     *
     * @param xdMetadata the metadata
     * @return the written string
     */
    String write(final XdMetadata xdMetadata) {
        Objects.requireNonNull(xdMetadata);
        final StringBuilder sb = new StringBuilder();
        xdMetadata.title().ifPresent(title -> append(sb, "Title", title));
        xdMetadata.author().ifPresent(author -> append(sb, "Author", author));
        xdMetadata.editor().ifPresent(editor -> append(sb, "Editor", editor));
        xdMetadata.copyright().ifPresent(copyright -> append(sb, "Copyright", copyright));
        xdMetadata.date().ifPresent(date -> append(sb, "Date", date.toString()));
        xdMetadata.otherProperties().forEach((key, value) -> append(sb, key, value));
        return sb.toString();
    }

    /**
     * Appends the formatted key-value pair to the given string builder.
     *
     * @param sb the string builder
     * @param key the key
     * @param value the value
     */
    private void append(final StringBuilder sb, final String key, final String value) {
        sb.append(key).append(": ").append(value).append('\n');
    }
}
