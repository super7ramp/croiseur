/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

/**
 * A helper class managing application runtime during tests. To be extended by actual test classes.
 *
 * @see <a href=https://picocli.info/#_diy_output_capturing>DIY Output Capturing</a> in Picocli
 * documentation
 */
abstract class CroiseurCliTestRuntime {

    /** Application exited without error. */
    protected static final int SUCCESS = 0;

    /** Application threw an exception. */
    protected static final int RUNTIME_ERROR = 1;

    /** Application rejected malformed or incomplete command. */
    protected static final int INPUT_ERROR = 2;

    /** Emulated output stream. */
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    /** Emulated error output stream. */
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    /** The application under test. */
    private CroiseurCliApplication app;

    /**
     * Sets default locale to {@link Locale#ENGLISH} in order to have results independent of
     * system's locale.
     */
    @BeforeAll
    public static void setEnglishLocale() {
        Locale.setDefault(Locale.ENGLISH);
    }

    /**
     * Sets up the application and its runtime (i.e. output streams).
     */
    @BeforeEach
    final void setUp() {
        out.reset();
        err.reset();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
        app = new CroiseurCliApplication();
    }

    /**
     * Restores standard system output streams once tests have finished.
     */
    @AfterAll
    static void tearDown() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    /**
     * Executes the croiseur-cli command with given arguments.
     *
     * @param args the arguments
     * @return the error code
     */
    protected final int cli(final String... args) {
        return app.run(args);
    }

    /**
     * Returns the text written to output stream up to the moment of the call.
     *
     * @return the text written to output stream up to the moment of the call
     */
    protected final String out() {
        return out.toString();
    }

    /**
     * Returns the text written to error output stream up to the moment of the call.
     *
     * @return the text written to error output stream up to the moment of the call
     */
    protected final String err() {
        return err.toString();
    }

}
