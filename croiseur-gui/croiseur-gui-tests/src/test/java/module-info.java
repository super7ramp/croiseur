/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * End-to-end tests of Croiseur GUI.
 */
module com.gitlab.super7ramp.croiseur.gui.tests {
    requires com.gitlab.super7ramp.croiseur.gui;

    requires org.junit.jupiter.api;
    requires org.testfx;
    requires org.testfx.junit5;
    requires javafx.controls;
    requires javafx.fxml;
    opens com.gitlab.super7ramp.croiseur.gui.tests to org.junit.platform.commons, org.testfx.junit5;
}