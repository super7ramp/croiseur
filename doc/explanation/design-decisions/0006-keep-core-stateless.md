<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

# 5. Keep core stateless

Date: 2023-06-28 (*a posteriori*)

## Status

Accepted

## Context

Application will need to manage various states (e.g. solver running state, persisted puzzles).

## Decision

1. Keep core stateless: No cache.
2. If it makes sense for core to control a state (e.g. a puzzle repository), then an extension point
   needs to be defined and implemented outside core.

## Consequences

Core is simpler to reason with: No cache to take care of, no risk of concurrency issue (as long as
the plugins are thread-safe).
