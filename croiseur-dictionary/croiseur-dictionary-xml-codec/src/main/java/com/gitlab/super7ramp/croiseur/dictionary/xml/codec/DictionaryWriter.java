/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.xml.codec;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A stateless dictionary writer.
 */
public final class DictionaryWriter {

    /** Private constructor to prevent instantiation. */
    private DictionaryWriter() {
        // Nothing to do.
    }

    /**
     * Writes the given dictionary header using the given writer.
     *
     * @param header the header to write
     * @param writer the writer to use
     * @throws XMLStreamException if write fails
     */
    private static void writeHeader(final DictionaryHeader header, final XMLStreamWriter writer) throws XMLStreamException {
        writeLocale(header.locale(), writer);
        for (final Map.Entry<Locale, String> entry : header.names().entrySet()) {
            writeName(entry.getKey(), entry.getValue(), writer);
        }
        for (final Map.Entry<Locale, String> entry : header.descriptions().entrySet()) {
            writeDescription(entry.getKey(), entry.getValue(), writer);
        }
    }

    /**
     * Writes the given description using the given writer.
     *
     * @param lang        the description locale
     * @param description the description to write
     * @param writer      the writer to use
     * @throws XMLStreamException if write fails
     */
    private static void writeDescription(final Locale lang, final String description,
                                         final XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(ElementNames.DESCRIPTION);
        if (!lang.equals(Locale.ENGLISH)) {
            writer.writeAttribute("xml:lang", lang.toLanguageTag());
        }
        writer.writeCharacters(description);
        writer.writeEndElement();
    }

    /**
     * Writes the given dictionary name using the given writer.
     *
     * @param lang   the dictionary name locale
     * @param name   the dictionary name to write
     * @param writer the writer to use
     * @throws XMLStreamException if write fails
     */
    private static void writeName(final Locale lang, final String name,
                                  final XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(ElementNames.NAME);
        if (!lang.equals(Locale.ENGLISH)) {
            writer.writeAttribute("xml:lang", lang.toLanguageTag());
        }
        writer.writeCharacters(name);
        writer.writeEndElement();
    }

    /**
     * Writes the given dictionary locale using the given writer.
     *
     * @param locale the dictionary locale to write
     * @param writer the writer to use
     * @throws XMLStreamException if write fails
     */
    private static void writeLocale(final Locale locale, final XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(ElementNames.LOCALE);
        writer.writeCharacters(locale.toLanguageTag());
        writer.writeEndElement();
    }

    /**
     * Writes the given word using the given writer.
     *
     * @param word   the word to write
     * @param writer the writer to use
     * @throws XMLStreamException if write fails
     */
    private static void writeWord(final String word, final XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(ElementNames.WORD);
        writer.writeCharacters(word);
        writer.writeEndElement();
    }

    /**
     * Writes the given words using the given writer.
     *
     * @param words  the words to write
     * @param writer the writer to use
     * @throws XMLStreamException if write fails
     */
    private static void writeWords(final List<String> words, final XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(ElementNames.WORDS);
        for (final String word : words) {
            writeWord(word, writer);
        }
        writer.writeEndElement();
    }

    /**
     * Writes the given {@link Dictionary} to the given {@link OutputStream}.
     *
     * @param os         the output stream to write into
     * @param dictionary the dictionary to write
     * @throws DictionaryWriteException if write fails
     */
    public static void write(final OutputStream os, final Dictionary dictionary) throws DictionaryWriteException {
        try (final OutputStream bufferedOutputStream = new BufferedOutputStream(os); final AutocloseableXMLStreamWriter writer = createWriter(bufferedOutputStream)) {

            writer.writeStartDocument();

            writeDictionaryStartElement(writer);
            writeHeader(dictionary.header(), writer);
            writeWords(dictionary.words(), writer);
            writer.writeEndElement();

            writer.writeEndDocument();
            writer.flush();
        } catch (final XMLStreamException | IOException e) {
            throw new DictionaryWriteException(e);
        }
    }

    /**
     * Writes the dictionary start element using the given writer.
     *
     * @param writer the writer to use
     * @throws XMLStreamException if write fails
     */
    private static void writeDictionaryStartElement(final XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("tns", "dictionary", "http://www.example.org/dictionary");
        writer.writeAttribute("xmlns", "", "tns", "http://www.example.org/dictionary");
        writer.writeAttribute("xmlns", "", "xsi", "http://www.w3.org/2001/XMLSchema-instance");
        writer.writeAttribute("xsi", "", "schemaLocation", "http://www.example.org/dictionary" +
                " dictionary.xsd");
    }

    /**
     * Creates the XML writer.
     *
     * @param os the underlying output stream
     * @return the XML writer
     * @throws XMLStreamException if an error occurs during writer construction
     */
    private static AutocloseableXMLStreamWriter createWriter(final OutputStream os) throws XMLStreamException {
        final XMLStreamWriter writer = XMLOutputFactory.newInstance()
                .createXMLStreamWriter(os, Charset.defaultCharset().name());
        return new AutocloseableXMLStreamWriter(writer);
    }
}
