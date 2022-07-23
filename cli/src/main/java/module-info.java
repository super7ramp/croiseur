import com.gitlab.super7ramp.crosswords.cli.publish.TextPublisher;

/**
 * CLI for crossword solving.
 */
module com.gitlab.super7ramp.crosswords.cli {

    // Base modules
    requires java.logging;

    // CLI framework
    requires info.picocli;

    // Core library
    requires com.gitlab.super7ramp.crosswords;

    // GUI provides core library with a publisher
    provides com.gitlab.super7ramp.crosswords.spi.publisher.Publisher with TextPublisher;

    // Open for reflection to CLI framework
    opens com.gitlab.super7ramp.crosswords.cli to info.picocli;
    opens com.gitlab.super7ramp.crosswords.cli.dictionary to info.picocli;
    opens com.gitlab.super7ramp.crosswords.cli.solve to info.picocli;
    opens com.gitlab.super7ramp.crosswords.cli.toplevel to info.picocli;
}