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
 * A simple helper test class managing application runtime during tests.
 * <p>
 * To be extended by actual test classes, e.g.:
 * <pre>{@code
 * final class MyCliTest extends TestRuntime {
 *     @Test
 *     void myTest() {
 *         cli("some", "arguments");
 *         assertEquals("Received arguments: 'some', 'arguments'", out());
 *         assertEquals("", err());
 *         assertEquals(SUCCESS, exitCode());
 *     }
 * }}</pre>
 *
 * @see <a href=https://picocli.info/#_diy_output_capturing>DIY Output Capturing</a> in Picocli
 * documentation
 */
abstract class TestRuntime {

    /** Application exited without error. */
    protected static final int SUCCESS = 0;

    /** Application threw an exception. */
    protected static final int RUNTIME_ERROR = 1;

    /** Application rejected malformed or incomplete command. */
    protected static final int INPUT_ERROR = 2;

    /**
     * The host locale, assumed constant for JVM lifetime. Locale is overridden during test
     * execution for reproducibility and restored afterwards.
     */
    private static final Locale ORIGIN_LOCALE = Locale.getDefault();

    /** Emulated output stream. */
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    /** Emulated error output stream. */
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    /** The application under test. */
    private CroiseurCliApplication app;

    /** The application exit code. */
    private Integer exitCode;

    /**
     * Sets default locale to {@link Locale#ENGLISH} in order to have results independent of
     * system's locale.
     */
    @BeforeAll
    static void setEnglishLocale() {
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
        exitCode = null;
    }

    /**
     * Restores standard system output streams and default locale once tests have finished.
     */
    @AfterAll
    static void tearDown() {
        System.setOut(System.out);
        System.setErr(System.err);
        Locale.setDefault(ORIGIN_LOCALE);
    }

    /**
     * Executes the croiseur-cli command with given arguments.
     *
     * @param args the arguments
     */
    protected final void cli(final String... args) {
        exitCode = app.run(args);
    }

    /**
     * Returns the text written to output stream up to the moment of the call.
     * <p>
     * Note 1: To simplify testing, line endings of returned text are forced to Unix line endings
     * ({@code "\n"}).
     * <p>
     * Note2: The stream is reset when the method returns.
     *
     * @return the text written to output stream up to the moment of the call
     */
    protected final String out() {
        final String lines = out.toString().replace(System.lineSeparator(), "\n");
        out.reset();
        return lines;
    }

    /**
     * Returns the text written to error output stream up to the moment of the call.
     * <p>
     * Note 1: To simplify testing, line endings of returned text are forced to Unix line endings
     * ({@code "\n"}).
     * <p>
     * Note 2: The stream is reset when the method returns.
     *
     * @return the text written to error output stream up to the moment of the call
     */
    protected final String err() {
        final String lines = err.toString().replace(System.lineSeparator(), "\n");
        err.reset();
        return lines;
    }

    /**
     * Returns the last exit code or {@code null} if application hasn't exited at the moment of the
     * call.
     *
     * @return the last exit code  or {@code null} if application hasn't exited at the moment of the
     * call
     */
    protected final Integer exitCode() {
        return exitCode;
    }
}
