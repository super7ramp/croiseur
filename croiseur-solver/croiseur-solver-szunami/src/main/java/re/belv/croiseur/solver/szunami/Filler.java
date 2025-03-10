/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.szunami;

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

    private record Input(String contents, int width, int height, Iterable<String> words) {}

    private final ObjectMapper om;
    private final Plugin plugin;

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
        try {
            final var input =
                    new Input(crossword.contents(), crossword.width(), crossword.height(), dictionary.words());
            final byte[] serializedInput = om.writeValueAsBytes(input);
            final byte[] serializedOutput = plugin.call("fill", serializedInput);
            return om.readValue(serializedOutput, Result.class);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        } catch (final ExtismFunctionException e) {
            // TODO rename NativePanicException
            throw new NativePanicException(e.getMessage());
        }
    }
}
