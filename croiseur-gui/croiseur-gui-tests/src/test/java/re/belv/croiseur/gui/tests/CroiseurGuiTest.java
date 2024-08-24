/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.tests;

import static org.testfx.framework.junit5.ApplicationTest.launch;

import java.util.Locale;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import re.belv.croiseur.gui.CroiseurGuiApplication;

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

    @AfterAll
    static void tearDown() {
        Locale.setDefault(ORIGIN_LOCALE);
    }
}
