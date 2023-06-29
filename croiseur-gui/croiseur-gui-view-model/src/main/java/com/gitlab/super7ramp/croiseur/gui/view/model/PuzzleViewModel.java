/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A model for displaying a synthetic view of a crossword puzzle.
 */
public final class PuzzleViewModel {

    /** The puzzle id. Value is {@code null} if puzzle hasn't been saved yet. */
    private final LongProperty id;

    /** The puzzle revision. Value is {@code null} if puzzle hasn't been saved yet. */
    private final IntegerProperty revision;

    /** The puzzle title. */
    private final StringProperty title;

    /** The puzzle author. */
    private final StringProperty author;

    /** The puzzle editor. */
    private final StringProperty editor;

    /** The puzzle copyright. */
    private final StringProperty copyright;

    /** The puzzle creation date. */
    private final StringProperty date;

    /** The puzzle grid. */
    private final ObjectProperty<PuzzleGrid> grid;

    /**
     * Constructs an instance.
     */
    public PuzzleViewModel() {
        id = new SimpleLongProperty(this, "id");
        revision = new SimpleIntegerProperty(this, "revision");
        title = new SimpleStringProperty(this, "title");
        author = new SimpleStringProperty(this, "author");
        editor = new SimpleStringProperty(this, "editor");
        copyright = new SimpleStringProperty(this, "copyright");
        date = new SimpleStringProperty(this, "date");
        grid = new SimpleObjectProperty<>(this, "grid");
    }

    /**
     * Returns the id property.
     *
     * @return the id property
     */
    public LongProperty idProperty() {
        return id;
    }

    /**
     * Returns the value of the id property.
     *
     * @return the value of the id property
     */
    public Long id() {
        return id.get();
    }

    /**
     * Sets the value of the id property.
     *
     * @param value the value to set
     */
    public void id(final long value) {
        id.set(value);
    }

    /**
     * Returns the revision property.
     *
     * @return the revision property
     */
    public IntegerProperty revisionProperty() {
        return revision;
    }

    /**
     * Returns the value of the revision property.
     *
     * @return the value of the revision property
     */
    public Integer revision() {
        return revision.get();
    }

    /**
     * Sets the value of the revision property.
     *
     * @param value the value to set
     */
    public void revision(final int value) {
        revision.set(value);
    }

    /**
     * Returns the title property.
     *
     * @return the title property
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value the value to set
     */
    public void title(final String value) {
        title.setValue(value);
    }

    /**
     * Returns the value of the title property.
     *
     * @return the value of the title property
     */
    public String title() {
        return title.get();
    }

    /**
     * Returns the author property.
     *
     * @return the author property
     */
    public StringProperty authorProperty() {
        return author;
    }

    /**
     * Returns the value of the author property.
     *
     * @return the value of the author property
     */
    public String author() {
        return author.get();
    }

    /**
     * Sets the value of the author property.
     *
     * @param value the value to set
     */
    public void author(final String value) {
        author.set(value);
    }

    /**
     * Returns the editor property.
     *
     * @return the editor property
     */
    public StringProperty editorProperty() {
        return editor;
    }

    /**
     * Returns the value of the editor property.
     *
     * @return the value of the editor property
     */
    public String editor() {
        return editor.get();
    }

    /**
     * Sets the value of the editor property.
     *
     * @param value the value to set
     */
    public void editor(final String value) {
        editor.set(value);
    }

    /**
     * Returns the copyright property.
     *
     * @return the copyright property
     */
    public StringProperty copyrightProperty() {
        return copyright;
    }

    /**
     * Returns the value of the copyright property.
     *
     * @return the value of the copyright property
     */
    public String copyright() {
        return copyright.get();
    }


    /**
     * Sets the value of the copyright property.
     *
     * @param value the value to set
     */
    public void copyright(final String value) {
        copyright.set(value);
    }

    /**
     * Returns the date property.
     *
     * @return the date property
     */
    public StringProperty dateProperty() {
        return date;
    }

    /**
     * Returns the value of the date property.
     *
     * @return the value of the date property
     */
    public String date() {
        return date.get();
    }

    /**
     * Sets the value of the date property.
     *
     * @param value the value to set
     */
    public void date(final String value) {
        date.set(value);
    }

    /**
     * Returns the grid property.
     *
     * @return the grid property
     */
    public ObjectProperty<PuzzleGrid> gridProperty() {
        return grid;
    }

    /**
     * Returns the value of the grid property.
     *
     * @return the value of the grid property
     */
    public PuzzleGrid grid() {
        return grid.get();
    }

    /**
     * Sets the value of the grid property.
     *
     * @param value the value to set
     */
    public void grid(final PuzzleGrid value) {
        grid.set(value);
    }
}
