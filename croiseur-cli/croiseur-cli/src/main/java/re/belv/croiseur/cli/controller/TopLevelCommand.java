/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.controller;

import picocli.CommandLine.Command;

/**
 * The top-level command, which only prints help.
 */
@Command(name = "croiseur-cli")
public final class TopLevelCommand {

    /**
     * Constructs an instance.
     */
    public TopLevelCommand() {
        // Nothing to do.
    }

    /*
     * A non-runnable command is the idiomatic way to do a top-level command requiring
     * subcommands, see https://picocli.info/#_required_subcommands.
     */
}
