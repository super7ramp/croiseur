/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.xd.codec.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The crossword metadata.
 * <p>
 * Metadata are immutable and can only be built using the associated {@link Builder} class.
 */
public final class XdMetadata {

    /**
     * The builder class for {@link XdMetadata}.
     * <p>
     * Usage:
     * <pre>{@code
     * var builder = new XdMetadata.Builder();
     * var xdMetadata = builder.title("An example grid")
     *                         .author("Me Myself")
     *                         .date("2023-06-11")
     *                         .build();
     * }</pre>
     */
    public static final class Builder {

        /** The other properties being built. */
        private final Map<String, String> otherProperties;

        /** The title property, {@code null} by default. */
        private String title;

        /** The author property, {@code null} by default. */
        private String author;

        /** The editor property, {@code null} by default. */
        private String editor;

        /** The copyright property, {@code null} by default. */
        private String copyright;

        /** The date property, {@code null} by default. */
        private LocalDate date;

        /**
         * Constructs a new instance.
         */
        public Builder() {
            otherProperties = new HashMap<>();
        }

        /**
         * Sets the title value.
         * <p>
         * Erases any previously set value.
         *
         * @param titleValue the new value
         * @return this builder
         * @throws NullPointerException if given value is {@code null}
         */
        public Builder title(final String titleValue) {
            title = Objects.requireNonNull(titleValue);
            return this;
        }

        /**
         * Sets the author value.
         * <p>
         * Erases any previously set value.
         *
         * @param authorValue the new value
         * @return this builder
         * @throws NullPointerException if given value is {@code null}
         */
        public Builder author(final String authorValue) {
            author = Objects.requireNonNull(authorValue);
            return this;
        }

        /**
         * Sets the editor value.
         * <p>
         * Erases any previously set value.
         *
         * @param editorValue the new value
         * @return this builder
         * @throws NullPointerException if given value is {@code null}
         */
        public Builder editor(final String editorValue) {
            editor = Objects.requireNonNull(editorValue);
            return this;
        }

        /**
         * Sets the copyright value.
         * <p>
         * Erases any previously set value.
         *
         * @param copyrightValue the new value
         * @return this builder
         * @throws NullPointerException if given value is {@code null}
         */
        public Builder copyright(final String copyrightValue) {
            copyright = Objects.requireNonNull(copyrightValue);
            return this;
        }

        /**
         * Sets the date value.
         * <p>
         * Erases any previously set value.
         *
         * @param dateValue the new value
         * @return this builder
         * @throws NullPointerException if given value is {@code null}
         */
        public Builder date(final LocalDate dateValue) {
            date = Objects.requireNonNull(dateValue);
            return this;
        }

        /**
         * Sets another property, as a String.
         * <p>
         * Erases any previously set value for the given key.
         *
         * @param key the new property key
         * @param value the new property value
         * @return this builder
         * @throws NullPointerException if given key or value is {@code null}
         */
        public Builder otherProperty(final String key, final String value) {
            Objects.requireNonNull(key);
            Objects.requireNonNull(value);
            otherProperties.put(key, value);
            return this;
        }

        /**
         * Builds the metadata.
         * <p>
         * Data will be deep-copied from the builder, thus this builder can be reused.
         *
         * @return the metadata
         */
        public XdMetadata build() {
            return new XdMetadata(this);
        }

        /**
         * Resets this builder.
         */
        public void reset() {
            title = null;
            author = null;
            editor = null;
            copyright = null;
            date = null;
            otherProperties.clear();
        }
    }

    /** Other properties. */
    private final Map<String, String> otherProperties;

    /** The title property. */
    private final String title;

    /** The author property. */
    private final String author;

    /** The editor property. */
    private final String editor;

    /** The copyright property. */
    private final String copyright;

    /** The date property. */
    private final LocalDate date;

    /**
     * Constructs an instance.
     *
     * @param builder the builder
     */
    private XdMetadata(final Builder builder) {
        title = builder.title;
        author = builder.author;
        editor = builder.editor;
        copyright = builder.copyright;
        date = builder.date;
        otherProperties = new HashMap<>(builder.otherProperties);
    }

    /**
     * @return all other, non-standard, properties
     */
    public Map<String, String> otherProperties() {
        return Collections.unmodifiableMap(otherProperties);
    }

    /**
     * @return the value of the "Author" property, if any
     */
    public Optional<String> author() {
        return Optional.ofNullable(author);
    }

    /**
     * @return the value of the "Date" property, if any
     */
    public Optional<LocalDate> date() {
        return Optional.ofNullable(date);
    }

    /**
     * @return the value of the "Copyright" property, if any
     */
    public Optional<String> copyright() {
        return Optional.ofNullable(copyright);
    }

    /**
     * @return the value of the "Editor" property, if any
     */
    public Optional<String> editor() {
        return Optional.ofNullable(editor);
    }

    /**
     * @return the value of the "Title" property, if any
     */
    public Optional<String> title() {
        return Optional.ofNullable(title);
    }

}
