package com.gitlab.super7ramp.crosswords.gui;

import javafx.application.Application;

/**
 * Program entry point.
 */
public final class Main {

    /**
     * Static methods only.
     */
    private Main() {
        // Nothing to do.
    }

    /**
     * Starts the crossword GUI.
     *
     * @param args the start arguments
     */
    public static void main(String[] args) {
        Application.launch(CrosswordGuiApplication.class, args);
    }

}