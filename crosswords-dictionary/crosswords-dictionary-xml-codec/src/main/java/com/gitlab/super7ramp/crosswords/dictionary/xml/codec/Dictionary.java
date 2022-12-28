package com.gitlab.super7ramp.crosswords.dictionary.xml.codec;

import java.util.List;

/**
 * A dictionary.
 *
 * @param header the header
 * @param words the words
 */
public record Dictionary(DictionaryHeader header, List<String> words) {
    // TODO add validation
}
