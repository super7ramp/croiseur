/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;

import java.util.function.Function;

/**
 * An alternative to {@link javafx.scene.control.cell.TextFieldListCell}, which allows to customize
 * prompt text.
 *
 * @param <T> The type of the elements contained within the ListView.
 */
public final class TextFieldListCell<T> extends ListCell<T> {

    /** The main text converter. */
    private final ObjectProperty<StringConverter<T>> textConverter;

    /** The prompt text converter. */
    private final ObjectProperty<Function<T, String>> promptTextConverter;

    /** The text field. */
    private TextField textField;

    /**
     * Creates a TextFieldListCell that provides a {@link TextField} when put into editing mode that
     * allows editing of the cell content. This method will work on any ListView instance,
     * regardless of its generic type. However, to enable this, a {@link StringConverter} must be
     * provided that will convert the given String (from what the user typed in) into an instance of
     * type T. This item will then be passed along to the {@link ListView#onEditCommitProperty()}
     * callback.
     *
     * @param textConverterArg       A {@link StringConverter converter} that can convert the given
     *                               String (from what the user typed in) into an instance of type T
     *                               and vice-versa
     * @param promptTextConverterArg A converter than can convert the given instance of type T into
     *                               a prompt text
     */
    public TextFieldListCell(final StringConverter<T> textConverterArg,
                             final Function<T, String> promptTextConverterArg) {
        textConverter = new SimpleObjectProperty<>(this, "textConverter", textConverterArg);
        promptTextConverter =
                new SimpleObjectProperty<>(this, "promptTextConverter", promptTextConverterArg);
        getStyleClass().add("text-field-list-cell");
    }

    @Override
    public void startEdit() {
        if (!isEditable() || !getListView().isEditable()) {
            return;
        }
        super.startEdit();
        if (isEditing()) {
            if (textField == null) {
                createTextField();
            } else {
                textField.setPromptText(itemPromptText());
                textField.setText(itemText());
            }
            setGraphic(textField);
            textField.selectAll();
            textField.requestFocus();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(textField);
    }

    @Override
    public void updateItem(final T item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            if (textField == null) {
                createTextField();
            } else {
                textField.setText(itemText());
            }
            setGraphic(textField);
        }
    }

    private void createTextField() {
        textField = new TextField();
        textField.setPromptText(itemPromptText());
        textField.setText(itemText());
        textField.setOnAction(event -> {
            final StringConverter<T> converter = textConverter.get();
            if (converter == null) {
                throw new IllegalStateException(
                        "Attempting to convert text input into Object, but provided "
                        + "StringConverter is null. Be sure to set a StringConverter "
                        + "in your cell factory.");
            }
            commitEdit(converter.fromString(textField.getText()));
            event.consume();
        });
        textField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                event.consume();
            }
        });
        /*
         * Default text field behaviour allows edition of the content by a single click, which would
         * bypass the ListCell edit management (i.e. first click -> select cell, second click ->
         * edit).
         *
         * The trick is to set the text field as transparent to the mouse. This way, a single click
         * only selects the list cell. A second click will start edit mode by triggering call to
         * startEdit (which enables text field editing by requesting focus).
         */
        textField.setMouseTransparent(true);
    }

    private String itemText() {
        final StringConverter<T> converter = textConverter.get();
        final T item = getItem();
        final String itemText;
        if (converter == null) {
            itemText = item == null ? "" : item.toString();
        } else {
            itemText = converter.toString(item);
        }
        return itemText;
    }

    private String itemPromptText() {
        final Function<T, String> converter = promptTextConverter.get();
        final T item = getItem();
        final String itemPromptText;
        if (converter == null) {
            itemPromptText = item == null ? "" : item.toString();
        } else {
            itemPromptText = converter.apply(item);
        }
        return itemPromptText;
    }
}
