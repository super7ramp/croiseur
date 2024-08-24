/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view;

import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/** A pane to edit puzzle details. */
public final class PuzzlePane extends Accordion {

    /** The various text fields. No specific validation. */
    @FXML
    private TextField title, author, editor, copyright;

    /** The (creation) date picker. */
    @FXML
    private DatePicker date;

    /** The export button. */
    @FXML
    private Button exportButton;

    /** The back to puzzle selection button. */
    @FXML
    private Button backToSelectionButton;

    /** Constructs an instance. */
    public PuzzlePane() {
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
    }

    /**
     * Returns the title property.
     *
     * @return the title property
     */
    public StringProperty titleProperty() {
        return title.textProperty();
    }

    /**
     * Returns the author property.
     *
     * @return the author property
     */
    public StringProperty authorProperty() {
        return author.textProperty();
    }

    /**
     * Returns the editor property.
     *
     * @return the editor property
     */
    public StringProperty editorProperty() {
        return editor.textProperty();
    }

    /**
     * Returns the copyright property.
     *
     * @return the copyright property
     */
    public StringProperty copyrightProperty() {
        return copyright.textProperty();
    }

    /**
     * Returns the date property.
     *
     * <p>Watch out: Expected date format is {@link DatePicker#converterProperty() DatePicker}'s default one. Supporting
     * a different formatter would require changes in this class (likely exposing date formatter as a property) as well
     * as in client classes (likely bind date formatter property with view model).
     *
     * @return the date property
     */
    public StringProperty dateProperty() {
        return date.getEditor().textProperty();
    }

    /**
     * The export button event handler property.
     *
     * @return the export button event handler property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onExportButtonActionProperty() {
        return exportButton.onActionProperty();
    }

    /**
     * The export button disable property.
     *
     * <p>Allows to disable specifically the export button. Note that global {@link #disableProperty()} takes precedence
     * (i.e. if {@link #disableProperty()} is {@code true} then the button will be effectively disabled).
     *
     * @return the export button disable property
     */
    public BooleanProperty exportButtonDisableProperty() {
        return exportButton.disableProperty();
    }

    /**
     * The back to puzzle selection button event handler property.
     *
     * @return the back to puzzle selection button event handler property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onBackToSelectionButtonActionProperty() {
        return backToSelectionButton.onActionProperty();
    }

    /** Initializes the control after object hierarchy has been loaded from FXML. */
    @FXML
    private void initialize() {
        initializeTitledPanes();
        initializeDatePicker();
    }

    /** Initializes titled panes: Make sure always one titled pane is expanded. */
    private void initializeTitledPanes() {
        expandedPaneProperty().addListener((observable, oldValue, newValue) -> {
            final boolean hasExpanded = getPanes().stream().anyMatch(TitledPane::isExpanded);
            if (!hasExpanded && oldValue != null) {
                Platform.runLater(() -> setExpandedPane(oldValue));
            }
        });
        setExpandedPane(getPanes().get(0));
    }

    /**
     * Initializes date picker: Since text field is not editable (to avoid the validation it would require) and picker
     * does not have a button to reset the value, allow to clear the text field using backspace and delete keys.
     */
    private void initializeDatePicker() {
        date.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            final KeyCode keyCode = e.getCode();
            if (keyCode == KeyCode.BACK_SPACE || keyCode == KeyCode.DELETE) {
                date.getEditor().clear();
                // Clear the value as well: DatePicker caches it, picking the same date after having
                // cleared only the text field would leave the text field unchanged (blank).
                date.setValue(null);
            }
        });
    }
}
