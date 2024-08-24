/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.xml.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Locale;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.junit.jupiter.api.Test;

/** Tests on {@link DictionaryWriter}. */
final class DictionaryWriterTest {

    private static String formatXml(final String xml) {
        try {
            final Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            final Writer out = new StringWriter();
            t.transform(new StreamSource(new StringReader(xml)), new StreamResult(out));
            return out.toString().replaceAll("\r\n", "\n"); // Force Unix line ending
        } catch (final TransformerException e) {
            throw new AssertionError("Error in test code when formatting XML output", e);
        }
    }

    @Test
    void write() throws DictionaryWriteException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final DictionaryHeader header = new DictionaryHeader.Builder()
                .setLocale(Locale.ENGLISH)
                .addName(Locale.ENGLISH, "Dictionary example")
                .addName(Locale.FRANCE, "Exemple de dictionnaire")
                .addDescription(
                        Locale.ENGLISH, "An example of XML " + "dictionary. Useful for discovery and testing purposes.")
                .addDescription(
                        Locale.FRANCE, "Un exemple de dictionnaire. Utile à des fins de " + "découverte et de tests.")
                .build();
        final List<String> words = List.of("Hello", "World");
        final Dictionary dictionary = new Dictionary(header, words);

        DictionaryWriter.write(outputStream, dictionary);

        assertEquals(
                """
                     <?xml version="1.0" encoding="UTF-8"?>\
                     <tns:dictionary xmlns:tns="http://www.example.org/dictionary" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.example.org/dictionary dictionary.xsd">
                         <locale>en</locale>
                         <name>Dictionary example</name>
                         <name xml:lang="fr-FR">Exemple de dictionnaire</name>
                         <description>An example of XML dictionary. Useful for discovery and testing purposes.</description>
                         <description xml:lang="fr-FR">Un exemple de dictionnaire. Utile à des fins de découverte et de tests.</description>
                         <words>
                             <word>Hello</word>
                             <word>World</word>
                         </words>
                     </tns:dictionary>
                     """,
                formatXml(outputStream.toString()));
    }
}
