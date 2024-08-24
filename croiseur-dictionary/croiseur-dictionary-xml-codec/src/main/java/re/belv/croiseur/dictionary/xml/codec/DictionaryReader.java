/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.xml.codec;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

/**
 * A dictionary reader.
 *
 * <p>It allows to read parts of the dictionary separately and repeatedly:
 *
 * <ul>
 *   <li>The "header" part with {@link #readHeader()}
 *   <li>The "words" part with {@link #readWords()}
 * </ul>
 *
 * This is useful when only description of the dictionary is needed, e.g. to present the dictionary to the user: Calling
 * {@link #readHeader()} is fast as it will not parse the word list at all.
 *
 * <p>Note that in order to support multiple read on the same source, the reader is not bound directly to an
 * {@link InputStream} but to an {@link InputStreamSupplier}. Such a supplier can easily be created from e.g. a URL:
 *
 * <pre>{@code
 * final URL dictionaryUrl = MyReaderTest.class.getResource("/example.xml");
 * final DictionaryReader = new DictionaryReader(dictionaryUrl::openStream);
 * }</pre>
 *
 * <p>The reader will close the stream appropriately when it doesn't need it any more.
 *
 * <p>Finally, note that the results of calls to {@link #readHeader()} and {@link #readWords()} are not cached: Values
 * will be read every time.
 */
public final class DictionaryReader {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(DictionaryReader.class.getName());

    /** The reader dedicated to the header part. */
    private final DictionaryHeaderReader dictionaryHeaderReader;

    /** The reader dedicated to the word list part. */
    private final DictionaryWordsReader dictionaryWordsReader;

    /**
     * Constructs an instance.
     *
     * @param dictionaryStreamArg the dictionary input stream supplier
     */
    public DictionaryReader(final InputStreamSupplier dictionaryStreamArg) {
        final InputStreamSupplier dictionaryStream = Objects.requireNonNull(dictionaryStreamArg);
        final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

        // See
        // https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html#xmlinputfactory-a-stax-parser
        xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");

        xmlInputFactory.setXMLReporter((message, errorType, relatedInformation, location) -> LOGGER.warning(
                "Error " + errorType + " while parsing XML dictionary at location " + location + ": " + message));

        dictionaryHeaderReader = new DictionaryHeaderReader(xmlInputFactory, dictionaryStream);
        dictionaryWordsReader = new DictionaryWordsReader(xmlInputFactory, dictionaryStream);
    }

    /**
     * Reads the header of the dictionary and put them into a {@link DictionaryHeader}.
     *
     * @return the {@link DictionaryHeader}
     * @throws DictionaryReadException if read fails
     */
    public DictionaryHeader readHeader() throws DictionaryReadException {
        final long before = System.currentTimeMillis();
        try {
            return dictionaryHeaderReader.read();
        } catch (final IOException | XMLStreamException e) {
            throw new DictionaryReadException(e);
        } finally {
            LOGGER.fine(() -> "Read header in " + (System.currentTimeMillis() - before) + " ms");
        }
    }

    /**
     * Reads the word list of the dictionary. Output may be very large.
     *
     * @return the words of the dictionary as a {@link Stream}
     * @throws DictionaryReadException if read fails
     */
    public Stream<String> readWords() throws DictionaryReadException {
        final long before = System.currentTimeMillis();
        try {
            return dictionaryWordsReader.read();
        } catch (final IOException | XMLStreamException e) {
            throw new DictionaryReadException(e);
        } finally {
            LOGGER.fine(() -> "Read words in " + (System.currentTimeMillis() - before) + " ms");
        }
    }
}
