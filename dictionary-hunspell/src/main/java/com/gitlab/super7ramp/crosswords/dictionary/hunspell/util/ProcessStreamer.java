package com.gitlab.super7ramp.crosswords.dictionary.hunspell.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Manage lifecycle of an external {@link Process} via the {@link Stream} interface.
 */
public final class ProcessStreamer {

    /**
     * Desired input streams.
     */
    private enum InputProcessStream {
        /** Standard input stream (i.e. output of the process). */
        INPUT,
        /** Standard error stream (i.e. error output of the process). */
        ERROR
    }

    /**
     * Wraps an {@link InterruptedException} into an unchecked exception.
     */
    private static class UncheckedInterruptedException extends RuntimeException {

        /**
         * Constructor.
         *
         * @param e the cause
         */
        UncheckedInterruptedException(final InterruptedException e) {
            super(e);
        }
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ProcessStreamer.class.getName());

    /**
     * Time to wait for external process termination after destroy action.
     */
    private static final Duration DESTROY_TIMEOUT = Duration.ofSeconds(3);

    /**
     * The actual error stream.
     */
    private final Stream<String> errorStream;

    /**
     * The actual input stream.
     */
    private final Stream<String> inputStream;

    /**
     * Constructor.
     *
     * @param anInputStream the actual process input stream
     */
    private ProcessStreamer(final Stream<String> anInputStream,
                            final Stream<String> anErrorStream) {
        inputStream = anInputStream;
        errorStream = anErrorStream;
    }

    /**
     * Create a new {@link ProcessStreamer} managing only the {@link #inputStream() input stream}.
     * <p>
     * The underlying process is started when this method returns.
     *
     * @param commands command and its arguments
     * @return a new {@link ProcessStreamer} managing only the {@link #inputStream() input stream}
     */
    public static ProcessStreamer inputOnly(final String... commands) {
        return of(EnumSet.of(InputProcessStream.INPUT), commands);
    }

    /**
     * Create a new {@link ProcessStreamer} managing only the {@link #errorStream() error stream}.
     * <p>
     * The underlying process is started when this method returns.
     *
     * @param commands command and its arguments
     * @return a new {@link ProcessStreamer} managing only the {@link #errorStream() error stream}
     */
    public static ProcessStreamer errorOnly(final String... commands) {
        return of(EnumSet.of(InputProcessStream.ERROR), commands);
    }

    /**
     * Create a new {@link ProcessStreamer} managing both the input and error streams.
     * <p>
     * The underlying process is started when this method returns.
     *
     * @param commands command and its arguments
     * @return a new {@link ProcessStreamer} managing both the input and error streams.
     */
    public static ProcessStreamer of(final String... commands) {
        return of(EnumSet.of(InputProcessStream.ERROR, InputProcessStream.INPUT), commands);
    }

    /**
     * Create a new {@link ProcessStreamer}.
     *
     * @param commands command and its arguments
     * @return a new {@link ProcessStreamer}
     */
    private static ProcessStreamer of(final Set<InputProcessStream> desiredStreams,
                                      final String... commands) {
        final Process process = startProcess(desiredStreams, commands);
        final Stream<String> inputStream = inputStream(process);
        final Stream<String> errorStream = errorStream(process);
        return new ProcessStreamer(inputStream, errorStream);
    }

    /**
     * Start the external process.
     *
     * @param command the command and its arguments
     * @return the launched external process
     */
    private static Process startProcess(final Set<InputProcessStream> desiredStreams,
                                        final String... command) {
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder().command(command);
            if (!desiredStreams.contains(InputProcessStream.INPUT)) {
                processBuilder.redirectInput(ProcessBuilder.Redirect.DISCARD);
            }
            if (!desiredStreams.contains(InputProcessStream.ERROR)) {
                processBuilder.redirectError(ProcessBuilder.Redirect.DISCARD);
            }
            return processBuilder.start();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Stop the given process.
     *
     * @param process the process to stop
     */
    private static void stopProcess(final Process process) {
        if (!process.isAlive()) {
            // Nominal case, input and error streams have been entirely consumed
            return;
        }

        // Short-circuit case: Stopping the process before it has terminated
        LOGGER.info("Stopping external process");

        try {
            process.destroy();
            process.onExit().get(DESTROY_TIMEOUT.toSeconds(), TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new UncheckedInterruptedException(e);
        } catch (final ExecutionException e) {
            LOGGER.warning(() -> "External process failed: " + e.getMessage());
        } catch (final TimeoutException e) {
            LOGGER.warning("External process did not shutdown after " + DESTROY_TIMEOUT + ", " +
                    "hard" + " killing.");
            process.destroyForcibly();
        }

        LOGGER.info("External process terminated");
    }

    /**
     * Get the process input stream.
     *
     * @param process the process to get the input stream from
     * @return the process input stream
     */
    private static Stream<String> inputStream(Process process) {
        final InputStream inputStream = process.getInputStream();
        if (inputStream == null) {
            return Stream.empty();
        }
        return reader(inputStream).lines().onClose(() -> stopProcess(process));
    }

    /**
     * Get the process input stream.
     *
     * @param process the process to get the input stream from
     * @return the process input stream
     */
    private static Stream<String> errorStream(Process process) {
        final InputStream errorStream = process.getErrorStream();
        if (errorStream == null) {
            return Stream.empty();
        }
        return reader(process.getErrorStream()).lines().onClose(() -> stopProcess(process));
    }

    /**
     * Create a {@link BufferedReader} from the given {@link InputStream}.
     *
     * @param is the input stream
     * @return the buffered reader
     */
    private static BufferedReader reader(final InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    /**
     * Get the input stream.
     * <p>
     * If instance is created via {@link #errorOnly}, then this method returns
     * {@link Stream#empty()}.
     * <p>
     * The returned stream will stop the underlying external process upon {@link Stream#close()}.
     * Since the underlying
     * process may not terminate until stream is entirely consumed, consider using the
     * try-with-resources construct if
     * the stream is not meant to be entirely consumed.
     *
     * @return the actual {@link Stream}
     */
    public Stream<String> inputStream() {
        return inputStream;
    }

    /**
     * Get the error stream.
     * <p>
     * If instance is created via {@link #inputOnly}, then this method returns
     * {@link Stream#empty()}.
     * <p>
     * The returned stream will stop the underlying external process upon {@link Stream#close()}.
     * Since the underlying
     * process may not terminate until stream is entirely consumed, consider using the
     * try-with-resources construct if
     * the stream is not meant to be entirely consumed.
     *
     * @return the actual {@link Stream}
     */
    public Stream<String> errorStream() {
        return errorStream;
    }
}
