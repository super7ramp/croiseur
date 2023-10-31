/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli;

import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A helper test class providing a fluent API over {@link TestRuntime} for writing simple BDD-like
 * tests.
 * <p>
 * To be extended by actual test classes, e.g.:
 * <pre>{@code
 * final class MyFluentCliTest extends FluentTestHelper {
 *     @Test
 *     void myFluentCliTest {
 *         whenOneRunsCli("some", "arguments")
 *             .thenCli().writesToStdOut("Received arguments: 'some', 'arguments'")
 *             .and().doesNotWriteToStdErr()
 *             .and().exitsWithCode(SUCCESS);
 *     }
 * }
 * }</pre>
 */
abstract class FluentTestHelper extends TestRuntime {

    /**
     * A complex assertion on the content of an output stream.
     */
    static final class WriteExpectation {

        /** The output stream name. */
        private final String streamName;

        /** The supplier of the output stream. */
        private final Supplier<String> actual;

        /** The assertions on the content of the output stream. */
        private final List<Executable> assertions;

        /**
         * Constructs an instance.
         *
         * @param actualArg     the provider of the output stream
         * @param streamNameArg the name of the output stream
         */
        private WriteExpectation(final Supplier<String> actualArg, final String streamNameArg) {
            actual = actualArg;
            streamName = streamNameArg;
            assertions = new ArrayList<>();
        }

        /**
         * Adds an assertion on the content of the output being of the given number of lines.
         *
         * @param expected the expected number of lines
         * @return this expectation, for method chaining
         */
        WriteExpectation aTotalOf(final int expected) {
            final Executable assertion =
                    () -> assertEquals(expected, actual.get().lines().count(),
                                       "Number of lines written to " + streamName);
            assertions.add(assertion);
            return this;
        }

        /**
         * Sugar to add after {@link #aTotalOf(int)}, no effect.
         *
         * @return this expectation, for method chaining
         */
        WriteExpectation lines() {
            return this;
        }

        /**
         * Adds an assertion on the content of the output starting with the given string.
         *
         * @param expected the expected start of content
         * @return this expectation, for method chaining
         */
        WriteExpectation startingWith(final String expected) {
            final Executable assertion =
                    () -> assertEquals(expected,
                                       actual.get().substring(0, expected.length()),
                                       "Start of " + streamName);
            assertions.add(assertion);
            return this;
        }

        /**
         * Adds an assertion on the output being empty.
         *
         * @return this expectation, for method chaining
         */
        WriteExpectation nothing() {
            return theLines("");
        }

        /**
         * Adds an assertion on the output matching the given string.
         *
         * @param expected the expected content
         * @return this expectation, for method chaining
         */
        WriteExpectation theLines(final String expected) {
            final Executable assertion = () -> assertEquals(expected, actual.get(), streamName);
            assertions.add(assertion);
            return this;
        }
    }

    /**
     * A builder of JUnit assertions.
     * <p>
     * All assertions are aggregated in a single context and are all executed when
     * {@link #exitsWithCode(int)} or {@link #andThatIsIt()} is called.
     * <p>
     * A failing assertion does not prevent the others from being tested, which gives a complete
     * picture of the result when test fails.
     */
    static final class AssertionBuilder {

        /** The assertions being built. */
        private final List<Executable> assertions;

        /** The supplier of the standard output content. */
        private final Supplier<String> out;

        /** The supplier of the standard error output content. */
        private final Supplier<String> err;

        /** The supplier of the exit code. */
        private final Supplier<Integer> exitCode;

        /**
         * Constructs an instance.
         *
         * @param outSupplier      the supplier of the standard output content
         * @param errSupplier      the supplier of the standard error output content
         * @param exitCodeSupplier the supplier of the exit code
         */
        private AssertionBuilder(final Supplier<String> outSupplier,
                                 final Supplier<String> errSupplier,
                                 final Supplier<Integer> exitCodeSupplier) {
            out = outSupplier;
            err = errSupplier;
            exitCode = exitCodeSupplier;
            assertions = new ArrayList<>();
        }

        /**
         * Adds an assertion on either the standard error output or the standard output matching the
         * given {@link WriteExpectation}.
         *
         * @return this assertion builder, for method chaining
         */
        AssertionBuilder writes(final WriteExpectation writeExpectation) {
            assertions.addAll(writeExpectation.assertions);
            return this;
        }

        /**
         * Adds an assertion on the standard input matching the given string.
         *
         * @param expected the expected content of the standard output
         * @return this assertion builder, for method chaining
         */
        AssertionBuilder writesToStdOut(final String expected) {
            return writes(toStdOut().theLines(expected));
        }

        /**
         * Adds an assertion on the standard output being empty.
         *
         * @return this assertion builder, for chaining
         */
        AssertionBuilder doesNotWriteToStdOut() {
            return writes(toStdOut().nothing());
        }

        /**
         * Adds an assertion on the standard error output matching the given string.
         *
         * @param expected the expected content of the standard error output
         * @return this assertion builder, for chaining
         */
        AssertionBuilder writesToStdErr(final String expected) {
            return writes(toStdErr().theLines(expected));
        }

        /**
         * Adds an assertion on the file at the given path matching the given string.
         *
         * @param expected the expected content of the file at the given path
         * @return this assertion builder, for chaining
         */
        AssertionBuilder writesToFile(final Path filePath, final String expected) {
            return writes(toFile(filePath).theLines(expected));
        }

        /**
         * Adds an assertion on the standard error output being empty.
         *
         * @return this assertion builder, for chaining
         */
        AssertionBuilder doesNotWriteToStdErr() {
            return writes(toStdErr().nothing());
        }

        /**
         * Creates a new {@link WriteExpectation} on the standard output, to be used with
         * {@link AssertionBuilder#writes(WriteExpectation)}.
         *
         * @return a new complex write expectation on standard output
         */
        private WriteExpectation toStdOut() {
            return new WriteExpectation(out, "Standard output");
        }

        /**
         * Creates a new {@link WriteExpectation} on the standard error output, to be used with
         * {@link AssertionBuilder#writes(WriteExpectation)}.
         *
         * @return a new complex write expectation on standard error output
         */
        private WriteExpectation toStdErr() {
            return new WriteExpectation(err, "Standard error");
        }

        /**
         * Creates a new {@link WriteExpectation} on the file at the given path to be used with
         * {@link AssertionBuilder#writes(WriteExpectation)}.
         *
         * @return a new complex write expectation on the file at the given path
         */
        private WriteExpectation toFile(final Path path) {
            final Supplier<String> fileReader = () -> {
                try {
                    return Files.readString(path);
                } catch (final IOException e) {
                    fail("Error while reading " + path, e);
                    return null;
                }
            };
            return new WriteExpectation(fileReader, "File " + path.getFileName().toString());
        }

        /**
         * Sugar between assertion adding methods, no effect.
         *
         * @return this assertion builder, for chaining
         */
        AssertionBuilder and() {
            return this;
        }

        /**
         * Adds an exit code assertion to this assertion builder then executes all assertions added
         * so far.
         *
         * @param expected the expected exit code
         */
        void exitsWithCode(final int expected) {
            final Executable exitCodeAssertion =
                    () -> assertEquals(expected, exitCode.get(), "Exit code");
            assertions.add(exitCodeAssertion);
            andThatIsIt();
        }

        /**
         * Executes all the assertions added so far.
         */
        void andThatIsIt() {
            assertAll(assertions);
        }
    }

    /**
     * Executes the croiseur-cli command with given arguments and verifies it exits with success. A
     * precondition step.
     *
     * @param args the arguments
     * @return this object, for chaining
     */
    protected final FluentTestHelper givenOneHasRunCli(final String... args) {
        cli(args);
        assertEquals("", err());
        assertEquals(SUCCESS, exitCode());
        drainOut();
        drainErr();
        return this;
    }

    /**
     * Executes the croiseur-cli command with given arguments.
     *
     * @param arguments the arguments
     * @return this object, for chaining
     */
    protected final FluentTestHelper whenOneRunsCli(final String... arguments) {
        cli(arguments);
        return this;
    }

    /**
     * Creates a new {@link AssertionBuilder} for chaining assertions.
     *
     * @return a new assertion builder
     */
    protected final AssertionBuilder thenCli() {
        return new AssertionBuilder(this::out, this::err, this::exitCode);
    }

    /**
     * Creates a new {@link WriteExpectation} on the standard output, to be used with
     * {@link AssertionBuilder#writes(WriteExpectation)}.
     *
     * @return a new complex write expectation on standard output
     */
    protected final WriteExpectation toStdOut() {
        return new WriteExpectation(this::out, "Standard output");
    }

    /**
     * Creates a new {@link WriteExpectation} on the standard error output, to be used with
     * {@link AssertionBuilder#writes(WriteExpectation)}.
     *
     * @return a new complex write expectation on standard error output
     */
    protected final WriteExpectation toStdErr() {
        return new WriteExpectation(this::err, "Standard error");
    }
}
