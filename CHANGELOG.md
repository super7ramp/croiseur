<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Version 0.5 – CLI Improvements – 2023/07/01

- CLI:
    - Fixed help command throwing exception when compiled ahead-of-time (#62, !58)
    - Added missing command description (#61, !59)
    - Added end-to-end tests (#9, !60, !62)
    - Made native tests pass (!61)
    - Configured memory settings (!63)
- GUI: Allowed to fill the current with selected suggestion (!65)
- Documentation:
    - Added architecture decision records (#73, #74, #75, !79)
    - Described components (!64)

## Version 0.4 – GUI Improvements – 2023/06/06

- GUI:
    - Use AtlantaFx theme (#65, !37)
    - Reworked dictionaries pane appearance (#68, !38)
    - Highlight selected slot (#67, !40)
    - Added a "Suggestions" pane indicating feasible words for the selected slot (!42)
    - Changed appearance of the selected box (#72, !42)
    - Allowed to "iterate" over fills found by solver (!54)
    - Present solver progress (#14, !44)
    - Present errors in alert popups (#15, !45)
    - Present dictionary descriptions in tooltips (!39)
- Reduced memory usage (#59, !48, !52, !55)
- Updated and improved documentation (!53)

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
- GUI: Fixed grid update issues (!14).
- CLI: Fixed native build (!13).

## Version 0.1 – A First Experimental Release – 2023/03/13

- Basic dictionary management and solver service, including:
    - 3 different solvers;
    - 6 dictionaries (English ×2, French, German, Italian, Spanish).
- 2 user interfaces: Command Line (CLI) & Desktop (GUI).
- All interfaces are subject to evolutions.
