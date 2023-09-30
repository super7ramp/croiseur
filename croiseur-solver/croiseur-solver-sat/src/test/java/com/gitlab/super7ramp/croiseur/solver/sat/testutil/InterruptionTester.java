/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.sat.testutil;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test util to verify thread interruption.
 * <p> Usage:
 * <pre>{@code
 * final var interruptionTester = new InterruptionTester(myInterruptibleRunnable);
 * interruptionTester.interruptThread();
 * interruptionTester.assertRunnableThrewInterruptedException();
 * }
 * </pre>
 */
public final class InterruptionTester {

    public interface InterruptibleRunnable {
        void run() throws InterruptedException;
    }

    private final ExecutorService executor;
    private final Future<Boolean> runnableInterrupted;

    /**
     * Constructs an instance. Immediately starts the given interruptible runnable in a dedicated
     * thread.
     *
     * @param runnable the interruptible runnable to test
     */
    public InterruptionTester(final InterruptibleRunnable runnable) {
        executor = Executors.newSingleThreadExecutor();
        runnableInterrupted = executor.submit(() -> {
            try {
                runnable.run();
                return false;
            } catch (final InterruptedException e) {
                return true;
            }
        });
    }

    /**
     * Interrupts the thread in which the interruptible runnable is supposed to run, after the given
     * delay is elapsed.
     *
     * @param delay the delay before interrupting
     */
    public void interruptThreadAfter(final int delay) throws InterruptedException {
        Thread.sleep(delay * 1000L);
        executor.shutdownNow();
    }

    /**
     * Verifies that the interruptible runnable passed at construction has raised an
     * {@link InterruptedException} withing timeout. Will always fail if
     * {@link #interruptThreadAfter} has not been called.
     *
     * @param timeout the timeout (in seconds)
     */
    public void assertRunnableThrewInterruptedExceptionWithin(final int timeout) {
        try {
            assertTrue(runnableInterrupted.get(timeout, TimeUnit.SECONDS),
                       "Runnable terminated without interruption");
        } catch (final ExecutionException | InterruptedException | TimeoutException e) {
            fail("Runnable not interrupted within " + timeout + " seconds: " + e.getMessage());
        }
    }
}
