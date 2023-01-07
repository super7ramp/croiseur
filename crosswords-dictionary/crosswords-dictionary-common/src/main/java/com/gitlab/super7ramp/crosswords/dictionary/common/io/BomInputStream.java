package com.gitlab.super7ramp.crosswords.dictionary.common.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * An {@link InputStream} filtering Byte Order Mark (BOM) at start of file, if any.
 * <p>
 * Only UTF-8 BOM is supported for now.
 *
 * @see <a href="https://github.com/apache/commons-io/blob/master/src/main/java/org/apache/commons/io/input/BOMInputStream.java">Apache Commons implementation</a>
 */
public final class BomInputStream extends FilterInputStream {

    /** A UTF-8 BOM. */
    private static final byte[] UTF8_BOM = new byte[]{ (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };

    /**
     * Creates a {@code BomInputStream} from the given {@link InputStream}.
     *
     * @param in the underlying input stream
     * @throws IOException if BOM detection fails
     */
    public BomInputStream(final InputStream in) throws IOException {
        super(in);
        in.mark(3);
        final byte[] firstBytes = in.readNBytes(3);
        if (!Arrays.equals(UTF8_BOM, firstBytes)) {
            // Not a UTF-8 BOM, let's reset read cursor to start of stream
            in.reset();
        }
    }
}
