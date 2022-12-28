package com.gitlab.super7ramp.crosswords.dictionary.common.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link BomInputStream}.
 */
final class BomInputStreamTest {

    @Test
    void noBom() throws IOException {
        final byte[] hello = "HELLO".getBytes();
        final String read;

        try (final ByteArrayInputStream bytesStream = new ByteArrayInputStream(hello);
             final BomInputStream filteredStream = new BomInputStream(bytesStream)) {
              read = new String(filteredStream.readAllBytes());
        }

        assertEquals("HELLO", read);
    }

    @Test
    void utf8Bom() throws IOException {
        final byte[] utf8Bom = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
        final byte[] hello = "HELLO".getBytes();
        final byte[] utf8BomAndHello = new byte[utf8Bom.length + hello.length];
        System.arraycopy(utf8Bom, 0, utf8BomAndHello, 0, utf8Bom.length);
        System.arraycopy(hello, 0, utf8BomAndHello, utf8Bom.length, hello.length);
        final String read;

        try (final ByteArrayInputStream bytesStream = new ByteArrayInputStream(utf8BomAndHello);
             final BomInputStream filteredStream = new BomInputStream(bytesStream)) {
            read = new String(filteredStream.readAllBytes());
        }

        assertEquals("HELLO", read);
    }
}
