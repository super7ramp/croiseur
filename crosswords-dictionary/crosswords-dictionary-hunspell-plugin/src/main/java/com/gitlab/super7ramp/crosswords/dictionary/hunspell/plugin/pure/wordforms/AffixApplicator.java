package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.wordforms;

import java.util.Optional;
import java.util.function.Function;

interface AffixApplicator extends Function<String, Optional<String>> {
    // Marker interface
}