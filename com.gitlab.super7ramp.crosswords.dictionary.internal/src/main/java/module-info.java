import com.gitlab.super7ramp.crosswords.dictionary.api.spi.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.dictionary.internal.InternalDictionaryProvider;

/**
 * {@link DictionaryProvider} based on a basic serialized representation.
 */
module com.gitlab.super7ramp.crosswords.dictionary.internal {
    // Base modules
    requires java.logging;

    // Implemented SPIs.
    requires com.gitlab.super7ramp.crosswords.dictionary.api;
    provides DictionaryProvider with InternalDictionaryProvider;
}