/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** View-model for a puzzle details editor view. */
public final class PuzzleDetailsViewModel {

    /** The puzzle id. */
    private final Property<Long> id;

    /** The puzzle revision. */
    private final Property<Integer> revision;

    /** The various puzzle fields. */
    private final StringProperty title, author, editor, copyright, date;

    /** Constructs an instance. */
    public PuzzleDetailsViewModel() {
        id = new SimpleObjectProperty<>(this, "id");
        revision = new SimpleObjectProperty<>(this, "revision");
        title = new SimpleStringProperty(this, "title", "");
        author = new SimpleStringProperty(this, "author", "");
        editor = new SimpleStringProperty(this, "editor", "");
        copyright = new SimpleStringProperty(this, "copyright", "");
        date = new SimpleStringProperty(this, "date", "");
    }

    /**
     * The puzzle id property.
     *
     * <p>Value can {@code null} if puzzle hasn't been saved yet.
     *
     * @return the puzzle id property
     */
    public Property<Long> idProperty() {
        return id;
    }

    /**
     * Returns the value of the id property.
     *
     * <p>Can be {@code null} if puzzle hasn't been saved yet.
     *
     * @return the value of the id property
     */
    public Long id() {
        return id.getValue();
    }

    /**
     * Sets the value of the id property.
     *
     * @param value the value to set
     */
    public void id(final long value) {
        id.setValue(value);
    }

    /**
     * Returns the value of the revision property.
     *
     * <p>Can be {@code null} if puzzle hasn't been saved yet.
     *
     * @return the value of the revision property
     */
    public Integer revision() {
        return revision.getValue();
    }

    /**
     * Sets the value of the revision property.
     *
     * @param value the value to set
     */
    public void revision(final int value) {
        revision.setValue(value);
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
     * Returns the value of the title property.
     *
     * @return the value of the title property
     */
    public String title() {
        return title.get();
    }

    /**
     * Sets the value of the title property.
     *
     * @param value the value to set
     */
    public void title(final String value) {
        title.set(value);
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
     * @param value the value of the author property.
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
     * Sets the value of copyright property.
     *
     * @param value the value of the copyright property
     */
    public void copyright(final String value) {
        copyright.set(value);
    }

    /**
     * Returns the date property.
     *
     * @return the date property.
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

    /** Resets this model to defaults. */
    public void reset() {
        id.setValue(null);
        revision.setValue(null);
        title.set("");
        author.set("");
        editor.set("");
        copyright.set("");
        date.set("");
    }
}
