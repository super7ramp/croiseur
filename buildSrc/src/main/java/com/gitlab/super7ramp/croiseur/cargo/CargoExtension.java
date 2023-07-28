/*
 * SPDX-FileCopyrightText: 2021 Arc'blroth
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gitlab.super7ramp.croiseur.cargo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration options for the Cargo wrapper plugin.
 */
public class CargoExtension {
    /**
     * Location of the <code>cargo</code> executable.
     * By default, the plugin will use the Cargo
     * executable located on the path.
     */
    public String cargoCommand = "cargo";

    /**
     * The rust toolchain to use.
     * By default, the plugin will use the default toolchain as
     * <a href="https://rust-lang.github.io/rustup/overrides.html">
     * resolved by rustup
     * </a>.
     */
    public String toolchain = null;

    /**
     * Location of the crate to build.
     * By default, this is the current project directory.
     */
    public String crate = null;

    /**
     * List of the locations of the outputs executables/libraries.
     * Each entry in this list is a pair between a target triple
     * and the name of the output file. Use <code>""</code>
     * (the empty string) to specify the default target.
     */
    public Map<String, String> outputs = new ConcurrentHashMap<>();

    /**
     * Build profile to use. If this is not set to <code>"debug"</code>,
     * the plugin assumes a release profile.
     * By default, this is set to the <code>"debug"</code> release profile.
     */
    public String profile = "debug";

    /**
     * Additional arguments to pass to Cargo.
     */
    public List<String> arguments = new ArrayList<>();

    /**
     * Additional environmental variables to set while
     * launching Cargo.
     */
    public Map<String, String> environment = new ConcurrentHashMap<>();
}