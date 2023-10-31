/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.cli.presenter.CliPresenter;
import re.belv.croiseur.spi.presenter.Presenter;

/**
 * Command-line frontend to croiseur.
 */
module re.belv.croiseur.cli {

    requires re.belv.croiseur;     // Core library
    requires info.picocli;         // CLI framework
    requires java.logging;         // Base module

    // CLI provides core library with a presenter
    provides Presenter with CliPresenter;

    // Open for reflection to CLI framework
    opens re.belv.croiseur.cli to info.picocli;
    opens re.belv.croiseur.cli.controller to info.picocli;
    opens re.belv.croiseur.cli.controller.clue to info.picocli;
    opens re.belv.croiseur.cli.controller.dictionary to info.picocli;
    opens re.belv.croiseur.cli.controller.puzzle to info.picocli;
    opens re.belv.croiseur.cli.controller.solver to info.picocli;

}