<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

# 2. Use Java module system

Date: 2023-06-28 (*a posteriori*)

Complements: [3. Split core from solver and dictionary details](0003-split-core-from-solver-and-dictionary-details.md)

## Status

Accepted

## Context

Separation of the project in modules require consistent rules to design them.

The Java module system introduce in Java 9 allows to enforce strong encapsulation of modules:

- It allows to clearly declare an API (by defining exported packages).
- It allows to clearly define dependencies between modules, including runtime-only dependencies.

## Decision

Use the Java module system.

## Consequences

### Every module is clearly described

The `module-info.java` file allows to gather - in a single place - information to quickly:

- Understand the purpose of a module: Read the Javadoc of the module.
- Use a module: Look only at exported classes.
- Build a module: Configure the build system using the declared dependencies.

### Learning is necessary to understand the system

The learning effort mostly happens when creating the first modules though: Once a few example are
correctly set, the concept is very easy to grasp and reproduce.

### Non-modularised external dependencies may become a pain

One needs to be careful when introducing new dependencies, as some non-modularised external
dependencies can be tedious to integrate, notably projects with packages split in multiple
jars.
