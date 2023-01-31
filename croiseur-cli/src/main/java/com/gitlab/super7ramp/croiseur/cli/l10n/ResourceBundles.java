/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.l10n;

import java.util.ResourceBundle;

/**
 * Retrieves {@link ResourceBundle}.
 */
public final class ResourceBundles {

    /**
     * Private constructor to prevent instantiation.
     */
    private ResourceBundles() {
        // Nothing to do.
    }

    /**
     * Retrieves the resource bundle for help messages.
     *
     * @return the resource bundle for help messages.
     */
    public static ResourceBundle helpMessages() {
        return ResourceBundle.getBundle("/l10n/HelpMessages");
    }

    /**
     * Retrieves the resource bundle for application output dictionary messages.
     *
     * @return the resource bundle for application output dictionary messages
     */
    public static ResourceBundle dictionaryMessages() {
        return ResourceBundle.getBundle("/l10n/DictionaryMessages");
    }

    /**
     * Retrieves the resource bundle for application output solver messages.
     *
     * @return the resource bundle for application output solver messages
     */
    public static ResourceBundle solverMessages() {
        return ResourceBundle.getBundle("/l10n/SolverMessages");
    }

}
