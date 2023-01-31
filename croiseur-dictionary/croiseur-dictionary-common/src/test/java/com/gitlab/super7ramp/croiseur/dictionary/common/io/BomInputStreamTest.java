/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.common.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link BomInputStream}.
 */
final class BomInputStreamTest {

    /**
     * Decodes the given byte array using the {@link BomInputStream}.
     *
     * @param encoded the byte array to decode as a character string
     * @return the decoded string
     * @throws IOException if BOM detection fails
     */
    private static String decode(final byte[] encoded) throws IOException {
        try (final ByteArrayInputStream bytesStream = new ByteArrayInputStream(encoded);
             final BomInputStream filteredStream = new BomInputStream(bytesStream)) {
            return new String(filteredStream.readAllBytes());
        }
    }

    /**
     * Concatenates the bom and the content into a new byte array.
     *
     * @param bom     the BOM
     * @param content the encoded content
     * @return a new array containing the bom and the content
     */
    private static byte[] concat(byte[] bom, byte[] content) {
        final byte[] bomAndContent = new byte[bom.length + content.length];
        System.arraycopy(bom, 0, bomAndContent, 0, bom.length);
        System.arraycopy(content, 0, bomAndContent, bom.length, content.length);
        return bomAndContent;
    }

    @Test
    void empty() throws IOException {
        final byte[] empty = "".getBytes();
        final String read = decode(empty);
        assertEquals("", read);
    }

    @Test
    void noBom() throws IOException {
        final byte[] hello = "HELLO".getBytes();
        final String read = decode(hello);
        assertEquals("HELLO", read);
    }

    @Test
    void utf8Bom() throws IOException {
        final byte[] utf8Bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        final byte[] hello = "HELLO".getBytes();
        final byte[] utf8BomAndHello = concat(utf8Bom, hello);

        final String read = decode(utf8BomAndHello);

        assertEquals("HELLO", read);
    }
}
