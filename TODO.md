# TODO

## Features

### Allow user to define shaded box (CLI)

- CLI: Finish implementing the `--shaded` option.

### Allow user to provide prefilled slots (CLI)

- CLI: Add repeatable options:
  - `--box <coordinate> <character>`
  - `--vertical-slot <start-coordinate> <string>`
  - `--horizontal-slot <start-coordinate> <string>`
- Solver part done (maybe bugs to fix)

### Allow user to stop the solver after completion threshold reached (CLI, maybe solver too)

- Add an option `--threshold` in CLI
- Implementation:
  - Interrupt solver once threshold reached? Not reliable as solver may already have backtracked when interrupted status
    takes effect
  - Add a decorator on slot iterator so that its `hasNext()` returns `false` when threshold is reached? Requires
    modification in solver

### More dictionaries (dictionary, tools)

- All Hunspell dictionaries available.
- Create gradle task to automate call to tool to convert Hunspell format to Java object serialized format
- Create a dictionary-data subproject to store dictionaries

### Support UTF8 (solver, dictionary)

- Don't think it's necessary for French, it might be useful for other languages

## Improvements

### Solver

#### Backtrack

- Likely backtrack and iterator should be coupled more.
- Refactor Backtracker, SlotIterator and History.
- Doc to read:
  - (again) Gashnig algorithms applied for 8-Queens SAP
  - Knuth's fascicle on satisfiability

#### GridDataBuilder

- Make it more readable
- Should ID creation/incrementation be in GridDataBuilder? => maybe not if id is used for backtracking

#### More progress indications

- Remaining slots
- Record stats:
  - Number of dead-ends encountered,
  - Number of assignments,
  - Stats on number of assignments before dead-ends

### Dictionary

- Filtering

### CandidateChooser

- Perfs: Check if probe parallelization improves performances with default k and with bigger k

### Others

#### GraalVM

To test. See if performances are improved - but make backtrack more decent first