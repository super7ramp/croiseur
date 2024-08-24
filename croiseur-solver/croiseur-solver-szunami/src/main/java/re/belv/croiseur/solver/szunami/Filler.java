/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.szunami;

/** A solver wrapping szunami's xword-rs filler written in Rust. */
public final class Filler {

    static {
        BundledNativeLibLoader.loadLibrary("xwords_rs_jni");
    }

    /**
     * Fills the given {@link Crossword}.
     *
     * @param crossword the crossword to fill
     * @return the fill {@link Result}, containing either the filled {@link Crossword} or an error message
     * @throws InterruptedException if interrupted while filling
     * @throws NativePanicException if native code panics
     */
    public native Result fill(final Crossword crossword, final Dictionary dictionary) throws InterruptedException;
}
