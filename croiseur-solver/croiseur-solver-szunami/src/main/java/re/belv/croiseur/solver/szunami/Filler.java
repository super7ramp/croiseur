/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.szunami;

import com.dylibso.chicory.wasm.ChicoryException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;
import org.extism.sdk.chicory.ExtismFunctionException;
import org.extism.sdk.chicory.Manifest;
import org.extism.sdk.chicory.ManifestWasm;
import org.extism.sdk.chicory.Plugin;

/** A solver wrapping szunami's xword-rs filler written in Rust. */
public final class Filler {

    /**
     * The input passed to the wasm code.
     *
     * @param contents the crossword contents
     * @param width the crossword width
     * @param height the crossword height
     * @param words the dictionary words
     */
    private record Input(String contents, int width, int height, Iterable<String> words) {}

    /** The mapper to serialize/deserialize JSON to/from wasm code. */
    private final ObjectMapper om;

    /** The extism plugin running wasm code. */
    private final Plugin plugin;

    /** Constructs an instance. */
    public Filler() {
        om = new ObjectMapper();
        final URL wasmUrl = Objects.requireNonNull(
                Filler.class.getResource("/xwords_rs_wasm.wasm"), "Failed to locate xwords_rs.wasm");
        final var manifestWasm = ManifestWasm.fromUrl(wasmUrl.toExternalForm()).build();
        final var manifest = Manifest.ofWasms(manifestWasm).build();
        plugin = Plugin.ofManifest(manifest).build();
    }

    /**
     * Fills the given {@link Crossword}.
     *
     * @param crossword the crossword to fill
     * @return the fill {@link Result}, containing either the filled {@link Crossword} or an error message
     * @throws InterruptedException if interrupted while filling
     * @throws NativePanicException if native code panics
     */
    public Result fill(final Crossword crossword, final Dictionary dictionary) throws InterruptedException {
        Objects.requireNonNull(crossword, "Crossword must not be null");
        Objects.requireNonNull(dictionary, "Dictionary must not be null");
        try {
            final var input =
                    new Input(crossword.contents(), crossword.width(), crossword.height(), dictionary.words());
            final byte[] serializedInput = om.writeValueAsBytes(input);
            final byte[] serializedOutput = plugin.call("fill", serializedInput);
            return om.readValue(serializedOutput, Result.class);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        } catch (final ExtismFunctionException e) {
            return Result.err(e.getMessage());
        } catch (final ChicoryException e) {
            if (e.getMessage().contains("Thread interrupted")) {
                final var ie = new InterruptedException("Filler interrupted");
                ie.initCause(e);
                throw ie;
            }
            throw e;
        }
    }
}
