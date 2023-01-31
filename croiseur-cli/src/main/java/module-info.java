/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.cli.presenter.CliPresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;

/**
 * CLI for crossword solving.
 */
module com.gitlab.super7ramp.croiseur.cli {

    // Core library
    requires com.gitlab.super7ramp.croiseur;

    // CLI provides core library with a publisher
    provides Presenter with CliPresenter;

    // Open for reflection to CLI framework
    opens com.gitlab.super7ramp.croiseur.cli to info.picocli;
    opens com.gitlab.super7ramp.croiseur.cli.controller.dictionary to info.picocli;
    opens com.gitlab.super7ramp.croiseur.cli.controller.solver to info.picocli;
    opens com.gitlab.super7ramp.croiseur.cli.controller to info.picocli;

    // CLI framework
    requires info.picocli;

    // Base modules
    requires java.logging;
}