/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/**
 * The available codecs view model.
 */
public final class PuzzleCodecsViewModel {

    /** The available puzzle decoders. */
    private final ListProperty<PuzzleCodec> decoders;

    // Encoders will be added there when ready.

    /**
     * Constructs an instance.
     */
    PuzzleCodecsViewModel() {
        decoders = new SimpleListProperty<>(this, "decoders", FXCollections.observableArrayList());
    }

    /**
     * The available puzzle decoders.
     *
     * @return the available puzzle decoders
     */
    public ListProperty<PuzzleCodec> decodersProperty() {
        return decoders;
    }

}
