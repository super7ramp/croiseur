/**
 * Library to generate all word forms from Hunspell dictionaries.
 */
module com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec {
    requires transitive com.gitlab.super7ramp.crosswords.common;
    requires com.gitlab.super7ramp.crosswords.dictionary.common;
    exports com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec;

    requires java.logging;
}