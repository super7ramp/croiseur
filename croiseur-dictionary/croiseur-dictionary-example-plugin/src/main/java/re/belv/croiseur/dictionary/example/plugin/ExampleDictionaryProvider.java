/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.example.plugin;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import re.belv.croiseur.common.dictionary.DictionaryDetails;
import re.belv.croiseur.common.dictionary.DictionaryProviderDetails;
import re.belv.croiseur.spi.dictionary.Dictionary;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;

/** An example dictionary provider plugin. */
public final class ExampleDictionaryProvider implements DictionaryProvider {

    /** An example dictionary, with hard-coded words. */
    private static final class ExampleDictionary implements Dictionary {

        /** Constructs an instance. */
        ExampleDictionary() {
            // Nothing to do.
        }

        @Override
        public DictionaryDetails details() {
            return new DictionaryDetails("Example Dictionary", Locale.ENGLISH, "An example dictionary");
        }

        @Override
        public Set<String> words() {
            return Set.of("Hello", "Word");
        }
    }

    /** Constructs an instance. */
    public ExampleDictionaryProvider() {
        /*
         * It's important that the constructor is public and without arguments: The service
         * loader mechanism used to load plugins requires it.
         */

        /*
         * A real-life dictionary provider would cache dictionary and dictionary entries as
         * fields in this class, instead of creating new instances in methods like in this
         * example. But note that dictionary provider constructor is always called at croiseur
         * startup, even if dictionary does not end up being used. In order to avoid costly and
         * useless reads, one may use the Lazy class from dictionary-common.
         */

        /*
         * If you're planning to provide dictionaries from local filesystem, consider visiting
         * the paths designated by the re.belv.croiseur.dictionary.path system
         * property. You may use dictionary-common's DictionaryPath class for this purpose.
         */
    }

    @Override
    public DictionaryProviderDetails details() {
        return new DictionaryProviderDetails("Example", "An example dictionary provider");
    }

    @Override
    public Collection<Dictionary> get() {
        return List.of(new ExampleDictionary());
    }
}
