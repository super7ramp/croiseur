/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web;

import org.springframework.boot.SpringApplication;

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
     * Starts Croiseur Web.
     *
     * @param args the start arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(CroiseurWebApplication.class, args);
    }
}
