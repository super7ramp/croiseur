import com.gitlab.super7ramp.crosswords.cli.presenter.CliPresenter;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;

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

    // CLI provides core library with a publisher
    provides Presenter with CliPresenter;

    // Open for reflection to CLI framework
    opens com.gitlab.super7ramp.crosswords.cli to info.picocli;
    opens com.gitlab.super7ramp.crosswords.cli.controller.dictionary to info.picocli;
    opens com.gitlab.super7ramp.crosswords.cli.controller.solve to info.picocli;
    opens com.gitlab.super7ramp.crosswords.cli.controller.toplevel to info.picocli;
}