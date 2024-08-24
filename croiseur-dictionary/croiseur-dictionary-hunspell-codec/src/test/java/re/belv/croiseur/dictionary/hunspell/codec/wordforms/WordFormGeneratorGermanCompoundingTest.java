/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Disabled;

@Disabled("compounding from continuation not implemented")
final class WordFormGeneratorGermanCompoundingTest extends WordFormGeneratorTestCase {

    @Override
    String name() {
        return "germancompounding";
    }

    @Override
    Charset charset() {
        return StandardCharsets.ISO_8859_1;
    }
}
