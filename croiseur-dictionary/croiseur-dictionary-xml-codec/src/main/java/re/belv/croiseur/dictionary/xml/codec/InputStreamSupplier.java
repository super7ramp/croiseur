/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.xml.codec;

import java.io.IOException;
import java.io.InputStream;

/** Supplies an {@link InputStream}. */
public interface InputStreamSupplier {

    /**
     * Returns an {@link InputStream}.
     *
     * @return an {@link InputStream}
     * @throws IOException if {@link InputStream} cannot be supplied
     */
    InputStream get() throws IOException;
}
