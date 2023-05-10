<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Version 0.3 – Dictionary Improvements – 2023/05/11

- Added a function to search words by pattern in core, usable via CLI (!26)
- Added a function to shuffle dictionaries in core, usable via CLI (!34)
- Made search order of `xwords-rs` more deterministic
- Improved performance of `croiseur-solver-ginsberg` marginally (!22)
- Added and improved documentation (!23, !25, !29, !35)
- Migrated build to Gradle Kotlin DSL (!30)
- Add some dictionary descriptions (!28)

## Version 0.2 – Solver Improvements – 2023/04/01

- Improved `croiseur-solver-ginsberg`:
    - Improved dictionary cache (!15, !16, !20)
        - Raw throughput improved by 2 orders of magnitude thanks to ideas taken from `xwords-rs`
          (trie, pattern cache).
        - Note that at equivalent backtrack numbers, `xwords-rs` is still faster by an order of
          magnitude – other areas of `croiseur-solver-ginsberg` lack optimisations.
    - Improved backtracking: It should be less incorrect (!15).
    - Miscellaneous improvements (!18).
- Improved solver documentation:
    - Added how-to: Plug a solver provider (!6).
    - Added explanations: How do crossword solvers work (!17).
    - Updated solver descriptions (!21).
- gui: Fixed grid update issues (!14).
- cli: Fixed native build (!13).

## Version 0.1 – A First Experimental Release – 2023/03/13

- Basic dictionary management and solver service, including:
    - 3 different solvers;
    - 6 dictionaries (English ×2, French, German, Italian, Spanish).
- 2 user interfaces: Command Line (CLI) & Desktop (GUI).
- All interfaces are subject to evolutions.
