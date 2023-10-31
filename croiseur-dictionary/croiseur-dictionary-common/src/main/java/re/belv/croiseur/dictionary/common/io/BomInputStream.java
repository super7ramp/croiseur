/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.common.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * An {@link InputStream} filtering Byte Order Mark (BOM) at start of file, if any.
 * <p>
 * Only UTF-8 BOM is supported for now.
 *
 * @see
 * <a href="https://github.com/apache/commons-io/blob/master/src/main/java/org/apache/commons/io/input/BOMInputStream.java">Apache Commons implementation</a>
 */
public final class BomInputStream extends FilterInputStream {

    /** A UTF-7 BOM. */
    private static final byte[] UTF7_BOM = new byte[]{0x2B, 0x2F, 0x76, 0x38, 0x2D};

    /** A UTF-8 BOM. */
    private static final byte[] UTF8_BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

    /** A UTF-16 (little endian) BOM. */
    private static final byte[] UTF_16_LE_BOM = new byte[]{(byte) 0xFF, (byte) 0xFE};

    /** A UTF-16 (big endian) BOM. */
    private static final byte[] UTF_16_BE_BOM = new byte[]{(byte) 0xFE, (byte) 0xFF};

    /** A UTF-32 (little endian) BOM. */
    private static final byte[] UTF_32_LE = new byte[]{(byte) 0xFF, (byte) 0xFE, 0x00, 0x00};

    /** A UTF-32 (big endian) BOM. */
    private static final byte[] UTF_32_BE = new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF};

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
        // TODO other BOMs
    }
}
