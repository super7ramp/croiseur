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
     * Retrieves the resource bundle for application messages.
     *
     * @return the resource bundle for application messages
     */
    public static ResourceBundle messages() {
        return ResourceBundle.getBundle("com.gitlab.super7ramp.croiseur.cli.l10n.Messages");
    }

    /**
     * Retrieves the localised message of given key.
     *
     * @param key the key of the message to retrieve
     * @return the localised message with given key
     */
    public static String $(final String key) {
        return messages().getString(key);
    }
}
