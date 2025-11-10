/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view;

import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import re.belv.croiseur.gui.view.model.ClueViewModel;

/**
 * A clue list cell.
 *
 * <p>It is a list cell inspired by {@link javafx.scene.control.cell.TextFieldListCell} with the following
 * particularities:
 *
 * <ul>
 *   <li>Its label is empty: Everything is in the graphic part;
 *   <li>It displays the cell index in a label on the left;
 *   <li>It displays the clue content in a text area (not a text field) in the middle;
 *   <li>It displays a button on the right, whose action is configurable;
 *   <li>Its appearance can be customized via the {@code .clue-list-cell} CSS class allowing the following properties:
 *       <ul>
 *         <li>{@code -index-format: [roman|arabic]}: Defines the format of the cell index
 *       </ul>
 * </ul>
 */
final class ClueListCell extends ListCell<ClueViewModel> {

    /** The ways to format the cell index. */
    private enum IndexFormat {
        ARABIC,
        ROMAN
    }

    /** Styleable property factory. */
    private static final StyleablePropertyFactory<ClueListCell> FACTORY =
            new StyleablePropertyFactory<>(Labeled.getClassCssMetaData());

    /** The index format CSS metadata. */
    private static final CssMetaData<ClueListCell, IndexFormat> INDEX_FORMAT_CSS_METADATA =
            FACTORY.createEnumCssMetaData(
                    IndexFormat.class, "-index-format", s -> s.indexFormat, IndexFormat.ARABIC, false);

    /** The index format styleable property. */
    private final StyleableObjectProperty<IndexFormat> indexFormat;

    /** Whether fill button shall not be visible, even when cell is selected. */
    private final BooleanProperty hideFillButton;

    /** Whether fill button shall be disabled, even when cell is selected. */
    private final BooleanProperty disableFillButton;

    /** The hbox containing the following label, text field and button. */
    @FXML
    private HBox containerHBox;

    /** The label containing the cell index. */
    @FXML
    private Label indexLabel;

    /** The text area containing the clue. */
    @FXML
    private TextArea textArea;

    /** The fill button. */
    @FXML
    private Button fillButton;

    /** Constructs an instance. */
    ClueListCell() {
        indexFormat =
                new SimpleStyleableObjectProperty<>(INDEX_FORMAT_CSS_METADATA, this, "indexFormat", IndexFormat.ARABIC);
        indexFormat.addListener(observable -> updateItem(getItem(), isEmpty()));

        hideFillButton = new SimpleBooleanProperty(this, "hideFillButton");
        hideFillButton.addListener(o -> updateItem(getItem(), isEmpty()));

        disableFillButton = new SimpleBooleanProperty(this, "disableFillButton");
        disableFillButton.addListener(o -> updateItem(getItem(), isEmpty()));

        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
    }

    /**
     * The "on fill clue button action" property.
     *
     * @return the "on fill clue button action" property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onFillClueButtonActionProperty() {
        return fillButton.onActionProperty();
    }

    /**
     * The "fill clue button disable" property.
     *
     * @return The "fill clue button disable" property
     */
    public BooleanProperty fillClueButtonDisableProperty() {
        return disableFillButton;
    }

    /**
     * The "fill clue button hide" property.
     *
     * <p>Fill button is visible when this property value is {@code false} and the cell is selected.
     *
     * @return the "fill clue button hide" property
     */
    public BooleanProperty fillButtonHideProperty() {
        return hideFillButton;
    }

    @Override
    public void startEdit() {
        if (!isEditable() || !getListView().isEditable()) {
            return;
        }
        super.startEdit();
        if (isEditing()) {
            textArea.setPromptText(itemToPromptText());
            textArea.setText(itemToText());
            textArea.selectAll();
            textArea.requestFocus();
        }
    }

    @Override
    public void updateItem(final ClueViewModel item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            indexLabel.setText(formattedIndex());
            textArea.setPromptText(itemToPromptText());
            textArea.setText(itemToText());
            fillButton.setVisible(isSelected() && !hideFillButton.get());
            fillButton.setDisable(disableFillButton.get() || !item.userContent().isEmpty());
            setGraphic(containerHBox);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(containerHBox);
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    /** Initializes the control after object hierarchy has been loaded from FXML. */
    @FXML
    private void initialize() {
        initializeTextArea();
    }

    /** Initializes text area event handlers. */
    private void initializeTextArea() {
        textArea.addEventFilter(KeyEvent.ANY, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit(itemFromText());
                event.consume();
            }
        });
        textArea.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                event.consume();
            }
        });
    }

    // model <-> view conversion methods

    /**
     * Converts text field text to item model.
     *
     * @return the item model
     */
    private ClueViewModel itemFromText() {
        return new ClueViewModel(textArea.getText());
    }

    /**
     * Converts item model to text.
     *
     * @return the text
     */
    private String itemToText() {
        return getItem().userContent();
    }

    /**
     * Converts the item model to prompt text.
     *
     * @return the prompt text
     */
    private String itemToPromptText() {
        final String itemSystemContent = getItem().systemContent();
        return itemSystemContent.isEmpty()
                ? ResourceBundle.getBundle(ClueListCell.class.getName()).getString("clue.none")
                : itemSystemContent;
    }

    // Index format methods

    /**
     * Returns the formatted cell index.
     *
     * @return the formatted cell index
     */
    private String formattedIndex() {
        final int index = getIndex() + 1;
        return switch (indexFormat.get()) {
                    case ARABIC -> arabicFormat(index);
                    case ROMAN -> romanFormat(index);
                } + ".";
    }

    /**
     * Formats the given index as a String.
     *
     * @param index the index
     * @return the formatted String
     */
    private static String arabicFormat(final int index) {
        return Integer.toString(index);
    }

    /**
     * Formats the given index as a String of roman numbers.
     *
     * @param index the index
     * @return the formatted String
     */
    private static String romanFormat(final int index) {
        // Not the most efficient implementation, but definitely one of the coolest.
        return "I"
                .repeat(index)
                .replace("IIIII", "V")
                .replace("IIII", "IV")
                .replace("VV", "X")
                .replace("VIV", "IX")
                .replace("XXXXX", "L")
                .replace("XXXX", "XL")
                .replace("LL", "C")
                .replace("LXL", "XC")
                .replace("CCCCC", "D")
                .replace("CCCC", "CD")
                .replace("DD", "M")
                .replace("DCD", "CM");
    }
}
