<!--
SPDX-FileCopyrightText: 2023-2024 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Version 0.11 - Not much but still - not yet released

- clue-openai: Update model to gpt-4o-mini (!169).
- solver-sat: Misc improvements (!187).
- solver-sat: Explain unsat instances (!189).
- solvers: Add benchmarks (!204, #42).
- Reformat source code wih spotless.
- Update dependencies.

## Version 0.10 - Under the hood improvements - 2024/04/21

- GUI: Fix button directions (!171).
- GUI: Make crossword grid thumbnail crisper (!138).
- Shorten rdns name (!136).
- Publish ARM binaries.
- Update dependencies.

## Version 0.9 – Upgrade to Java 21 – 2023/10/26

- Upgrade to Java 21 (#87, !131)
- Upgrade to JavaFx 21 (#88, !132)
- Fix obsolete Javadoc (!134)
- Collapse editor's lateral panes when switching back to welcome screen (!125) 

## Version 0.8 – New SAT Solver – 2023/10/06

- Added a new solver based on Sat4j's default pseudo-boolean solver: Slow and memory intensive,
  for small grids and test purposes only (#48, !127).
- Minor improvements on existing solver (!127, !128).

## Version 0.7.1 – Bugfixes – 2023/08/29

- Increase OpenAI clue plugin http timeout (#86, !122)
- CLI: Fix example command (!123)
- Fix typos in documentation (!123)

## Version 0.7 – Clue SPI – 2023/08/27

- Introduce clue management and generation (#7, !115)
- Fix dictionary path management on Windows (!121)
- CLI: Add examples in help message (!118)
- Publish Javadoc to Gitlab pages (#54, !107)

## Version 0.6 – Puzzle SPI – 2023/07/24

- Allow to save/re-open crosswords (#39, !95)
- Allow to import/export crosswords (#6, !99)
- Add a Conveyor configuration for Croiseur GUI (!89)

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
