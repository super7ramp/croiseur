#!/bin/sh

# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

#
# Quick and dirty script to generate Javadoc for main modules.
#
# Properly generating Javadoc with the Java module system
# is challenging in the sense that it imposes a directory
# structure with folder names strictly equal to module names.
#
# Croiseur does not respect that structure (yet).
#
# I'm a bit reluctant to switch to this structure because real
# module names are very long which does not look good when
# browsing the sources.
#
# This script does the transformation to the expected structure
# so that the javadoc command can be run. The generated Javadoc
# is created in a "javadoc" folder.
#

function prepare() {
    cp -r croiseur-common com.gitlab.super7ramp.croiseur.common
    cp -r croiseur-spi/croiseur-spi-dictionary com.gitlab.super7ramp.croiseur.spi.dictionary
    cp -r croiseur-spi/croiseur-spi-presenter com.gitlab.super7ramp.croiseur.spi.presenter
    cp -r croiseur-spi/croiseur-spi-puzzle-codec com.gitlab.super7ramp.croiseur.spi.puzzle.codec
    cp -r croiseur-spi/croiseur-spi-puzzle-repository com.gitlab.super7ramp.croiseur.spi.puzzle.repository
    cp -r croiseur-spi/croiseur-spi-solver com.gitlab.super7ramp.croiseur.spi.solver
    cp -r croiseur com.gitlab.super7ramp.croiseur
}

function generate() {
    local sourcefiles=$(find ${modules} -name "*.java")
    javadoc -d "$destdir" \
            -doctitle "<h1>Croiseur Modules</h1>" \
            --module-source-path "/*/src/main/java/" \
            $sourcefiles
}

function cleanup() {
    rm -r $modules
}

modules="com.gitlab.super7ramp.croiseur.common \
         com.gitlab.super7ramp.croiseur.spi.dictionary \
         com.gitlab.super7ramp.croiseur.spi.presenter \
         com.gitlab.super7ramp.croiseur.spi.puzzle.codec \
         com.gitlab.super7ramp.croiseur.spi.puzzle.repository \
         com.gitlab.super7ramp.croiseur.spi.solver \
         com.gitlab.super7ramp.croiseur"

destdir="javadoc"

prepare
generate
cleanup
