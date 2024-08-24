/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui;

import javafx.application.Application;

/** Program entry point. */
public final class Main {

    /** Static methods only. */
    private Main() {
        // Nothing to do.
    }

    /**
     * Starts the crossword GUI.
     *
     * @param args the start arguments
     */
    public static void main(final String[] args) {
        Application.launch(CroiseurGuiApplication.class, args);
    }
}
