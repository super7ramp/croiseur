package com.gitlab.super7ramp.crosswords.dictionary.hunspell.external.wordforms;

import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Generate word forms.
 */
public interface WordFormGenerator extends Supplier<Stream<String>> {

    static WordFormGenerator of(final Path affFile, final Path dicFile) {
        return new ExternalWordFormGenerator(affFile, dicFile);
    }

    @Override
    default Stream<String> get() {
        return generate();
    }

    /**
     * Generate all the valid forms.
     *
     * @return all the valid forms
     */
    Stream<String> generate();

}
