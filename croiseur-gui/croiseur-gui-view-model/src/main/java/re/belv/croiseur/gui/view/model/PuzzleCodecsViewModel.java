/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/**
 * The available codecs view model.
 */
public final class PuzzleCodecsViewModel {

    /** The available puzzle decoders. */
    private final ListProperty<PuzzleCodec> decoders;

    /** The available puzzle encoders. */
    private final ListProperty<PuzzleCodec> encoders;

    /**
     * Constructs an instance.
     */
    PuzzleCodecsViewModel() {
        decoders = new SimpleListProperty<>(this, "decoders", FXCollections.observableArrayList());
        encoders = new SimpleListProperty<>(this, "encoders", FXCollections.observableArrayList());
    }

    /**
     * The available puzzle decoders.
     *
     * @return the available puzzle decoders
     */
    public ListProperty<PuzzleCodec> decodersProperty() {
        return decoders;
    }

    /**
     * The available puzzle encoders.
     *
     * @return the available puzzle encoders
     */
    public ListProperty<PuzzleCodec> encodersProperty() {
        return encoders;
    }
}
