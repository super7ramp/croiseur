/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.tests;

import com.gitlab.super7ramp.croiseur.gui.CroiseurGuiApplication;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Locale;

import static org.testfx.framework.junit5.ApplicationTest.launch;

/**
 * Base class for Croiseur GUI end-to-end tests.
 */
@ExtendWith(ApplicationExtension.class)
abstract class CroiseurGuiTest {

    /**
     * The host locale, assumed constant for JVM lifetime. Locale is overridden during test
     * execution for reproducibility and restored afterwards.
     */
    private static final Locale ORIGIN_LOCALE = Locale.getDefault();

    @BeforeAll
    static void setup() throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        launch(CroiseurGuiApplication.class);
    }

    @Start
    final void start(final Stage stage) {
        stage.show();
    }

    @AfterAll
    static void tearDown() {
        Locale.setDefault(ORIGIN_LOCALE);
    }
}