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
    cp -r croiseur-common re.belv.croiseur.common
    cp -r croiseur-spi/croiseur-spi-clue re.belv.croiseur.spi.clue
    cp -r croiseur-spi/croiseur-spi-dictionary re.belv.croiseur.spi.dictionary
    cp -r croiseur-spi/croiseur-spi-presenter re.belv.croiseur.spi.presenter
    cp -r croiseur-spi/croiseur-spi-puzzle-codec re.belv.croiseur.spi.puzzle.codec
    cp -r croiseur-spi/croiseur-spi-puzzle-repository re.belv.croiseur.spi.puzzle.repository
    cp -r croiseur-spi/croiseur-spi-solver re.belv.croiseur.spi.solver
    cp -r croiseur re.belv.croiseur
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

modules="re.belv.croiseur.common \
         re.belv.croiseur.spi.clue \
         re.belv.croiseur.spi.dictionary \
         re.belv.croiseur.spi.presenter \
         re.belv.croiseur.spi.puzzle.codec \
         re.belv.croiseur.spi.puzzle.repository \
         re.belv.croiseur.spi.solver \
         re.belv.croiseur"

destdir="javadoc"

prepare
generate
cleanup
