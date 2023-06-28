<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

# 2. Use Java as default language

Date: 2023-06-27

## Status

Accepted

## Context

Project is a crossword filler/editor. Author uses mainly Java.

## Decision

1. Implement components in Java, unless there is a reason to use another language (e.g. a lower
   level language for performance sensitive components).
2. Use Java 17 as reference, which is the Long-Term Support (LTS) version at the decision time.
3. Migrate to next LTSes when they are published and well-supported by tools (IDE, build system).

## Consequences

### Build will be fast(-ish)

Building jar is fast, compared to compiled languages.

### Code will be readable by many

It is assumed that a lot of people understand Java. They will have no problem to read the code.
People who do not understand - or like - Java may be repelled though.

### Slow JVM startup time will be annoying for short-lived executions

E.g. a CLI program just querying the available word lists.

This is not the main use-case though: The main use-case is to edit/fill crosswords, which is
expected to be longer than the JVM startup time.

Also, there are techniques to improve startup time:

- Implement a daemon;
- Compile Ahead-of-Time - some JVMs provide this feature.

### Performance-sensitive components may be harder to get right

E.g. automatically filling a grid, which is a computation intensive operation. Algorithms count a
lot though, not only raw language performance.

Lower-level language (e.g. C, C++, Rust) may be used via JNI.
