/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

/**
 * Base class for (almost) end-to-end Croiseur Web tests.
 */
@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
abstract class CroiseurWebTestBase {

    /**
     * The host locale, assumed constant for JVM lifetime. Locale is overridden during test
     * execution for reproducibility and restored afterwards.
     */
    private static final Locale ORIGIN_LOCALE = Locale.getDefault();

    /** The "server" entry point. */
    @Autowired
    protected MockMvc mockMvc;

    /** The mocked http session. */
    @Autowired
    protected MockHttpSession mockHttpSession;

    @BeforeAll
    static void setEnglishLocale() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @AfterAll
    static void restoreDefaultLocale() {
        Locale.setDefault(ORIGIN_LOCALE);
    }
}
