package com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.aff;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Kind of item parsed in a Hunspell affix file.
 *
 * @see <a href="https://helpmanual.io/man5/hunspell/">man 5 hunspell</a>
 */
enum AffItemKind {

    /** An affix header. */
    AFFIX_HEADER("(PFX|SFX) [^ /]+ [YN] [0-9]+"),
    /** An affix rule. */
    AFFIX_RULE("^(PFX|SFX) [^ /]+ [^ /]+ [^ /]+(/[^ /]+)? [^ /]+$"),
    /** An empty line or a line with only blanks. */
    BLANK("^[ ]*$"),
    /** A comment. */
    COMMENT("^#.*$"),
    /** Compounding option: TODO document. */
    COMPOUNDING_BREAK_HEADER("^BREAK [0-9]+$"),
    /** Compounding option: TODO document. */
    COMPOUNDING_BREAK("^BREAK [^ ]+$"),
    /** General option: Two-fold prefix stripping. */
    GENERAL_COMPLEX_PREFIXES("^COMPLEXPREFIXES$"),
    /** General option: Character encoding of words and morphemes in affix and dictionary files. */
    GENERAL_ENCODING("^SET (UTF-8|ISO8859-1|ISO8859-10|ISO8859-13|ISO8859-15|KOI8-R|KOI8-U|microsoft-cp1251|ISCII-DEVANAGARI)$"),
    /** General option: Flag type. */
    GENERAL_FLAG_TYPE("^FLAG (UTF-8|long|num)$"),
    /** General option: Languages specific code. */
    GENERAL_LANG("^LANG (az_AZ|de_DE|hu_HU|TR_tr)$"),
    /** Other option: TODO document, */
    OTHERS_CIRCUMFIX("^CIRCUMFIX [^ ]+$"),
    /** Other option: TODO document, */
    OTHERS_FORBIDDEN_WORD("^FORBIDDENWORD [^ ]+$"),
    /** Other option: Affix rules can strip full words, not only one less characters, before adding the affixes, */
    OTHERS_FULL_STRIP("^FULLSTRIP$"),
    /** Other option: Input conversion table header. */
    OTHERS_INPUT_CONVERSION_TABLE_HEADER("^ICONV [0-9]+$"),
    /** Other option: Input conversion table entry, used e.g. to convert one type of quote to another one. */
    OTHERS_INPUT_CONVERSION_TABLE_ENTRY("^ICONV [^ ]+ [^ ]+$"),
    /** Other option: TODO document. */
    OTHERS_KEEP_CASE("^KEEPCASE [^ ]+$"),
    /** Other option: TODO document. */
    OTHERS_NEED_AFFIX("^NEEDAFFIX [^ ]+$"),
    /** Other option: Output conversion table header. */
    OTHERS_OUTPUT_CONVERSION_TABLE_HEADER("^OCONV [0-9]+$"),
    /** Other option: Output conversion table entry. */
    OTHERS_OUTPUT_CONVERSION_TABLE_ENTRY("^OCONV [^ ]+ [^ ]+$"),
    /** Other option: Characters specified are meant to extend Hunspell CLI tokenizer. */
    OTHERS_WORD_CHARS("^WORDCHARS [^ ]+$"),
    /** Suggestion option: Neighbor characters, word is suggested by replacing one character by a neighbor character. */
    SUGGESTION_KEY("^KEY [^ \\|]+(\\|[^ \\|]+)*"),
    /** Suggestion option: A map table entry; represents a list of related characters. */
    SUGGESTION_RELATED_CHARACTERS_TABLE_ENTRY("^MAP [^ ]+$"),
    /** Suggestion option: The header of the map table, containing the number of entries. */
    SUGGESTION_RELATED_CHARACTERS_TABLE_HEADER("^MAP [0-9]+$"),
    /** Suggestion option: The header of the replacement table, containing the number of entries. */
    SUGGESTION_REPLACEMENT_TABLE_HEADER("^REP [0-9]+$"),
    /** Suggestion option: A replacement table entry; represents a replacement rule. */
    SUGGESTION_REPLACEMENT_TABLE_ENTRY("^REP [^ ]+ [^ ]+$"),
    /** Option for suggestion: Set number of n-gram suggestions. */
    SUGGESTION_MAX_NGRAM("^MAXNGRAMSUGS [0-9]+$"),
    /** Option for suggestion: Disable split-word suggestions. */
    SUGGESTION_NO_SPLIT("^NOSPLITSUGS$"),
    /** Option for suggestion: Words signed with flags should not be suggested. */
    SUGGESTION_NO_SUGGEST("^NOSUGGEST [^ ]+$"),
    /** Option for suggestion: Suggest when word differs from the right word form by one of these characters */
    SUGGESTION_TRY("^TRY [^ ]+$");

    /** The pattern that identifies a parsed line. */
    private final Pattern identificationPattern;

    /**
     * Constructor.
     *
     * @param pattern the pattern
     */
    AffItemKind(final String pattern) {
        identificationPattern = Pattern.compile(pattern);
    }

    /**
     * Identify the {@link AffItemKind} of the given line.
     *
     * @param line the line to identify
     * @return the {@link AffItemKind} corresponding to this line, if any
     */
    static Optional<AffItemKind> identify(final String line) {
        return Arrays.stream(values())
                .filter(affItemKind -> affItemKind.matches(line))
                .findFirst();
    }

    /**
     * @param line the line to test
     * @return <code>true</code> if the given line is of this {@link AffItemKind}
     */
    private boolean matches(final String line) {
        return identificationPattern.matcher(line).matches();
    }

}