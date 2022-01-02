# TODO

## Features

### Allow user to iterate over solutions (CLI, solver)

- **Watch out backtracking limitations, see Improvements section below.**
- What's the best for user?
  1. Add an option to specify the number of solutions: Internally, mark the n-1 solutions found as
     dead-end and continue algorithm
  2. Add an option `--random`: Does shuffling dictionary suffice?
- Not sure what's the best for speed

### Allow user to stop the solver after completion threshold reached (CLI, maybe solver too)

- Add an option `--threshold` in CLI
- Implementation:
  - Interrupt solver once threshold reached? Not reliable as solver may already have backtracked
    when interrupted status takes effect
  - Add a decorator on slot iterator so that its `hasNext()` returns `false` when threshold is
    reached? Requires modification in solver

### More dictionaries (dictionary, tools)

- All Hunspell dictionaries available.
- Create gradle task to automate call to tool to convert Hunspell format to Java object serialized
  format
- Create a dictionary-data subproject to store dictionaries (maybe different repo, can be integrated
  as git submodule).

### Support UTF8 (solver, dictionary)

- Don't think it's necessary for French, it might be useful for other languages

### Enhance text output format

- Add definition placeholders, column and row numbers.

### Support output format as plugins

- output-format-api: Define API. Relies on solver result so depends on solver-api.
- output-format-xml: xml implementation (jaxb?).
- output-format-json: json implementation (gson?).
- output-format-pdf: pdf implementation (check LaTeX packages).
- output-format-docx: docx implementation (rely on some Apache library).
- Might be in a different repository.

## Improvements

### Solver

#### Backtrack

- Current `DeadEnd` implementation is strange: Shouldn't all variable states be stored to represent
  a deadend?
- Backtrack vs. backjump vs. smarter backjump vs. other
  - **Backtrack**: Remove n-1 th assignment and try to reassign it
  - **Backjump**: Remove assignments until a connected slot is reached; Try to reassign it
  - **Smart backjump**: Same as backjump but ensure the source of the problem is removed (I don't
    understand the difference with backjump)
  - **Currently**: Remove the last assignment but don't necessarily try to re-assign it, just let
    the candidate chooser decide which slot to assign next. Is that the same as dynamic
    backtracking?
- Doc to read:
  - (again) Gashnig algorithms applied for 8-Queens SAP
  - Dynamic Backtracking by Ginsberg
  - Knuth's fascicle on satisfiability

#### GridDataBuilder

- Make it more readable
- Should ID creation/incrementation be in GridDataBuilder? => maybe not if id is used for
  backtracking

#### More progress indications

- Remaining slots
- Record stats:
  - Number of dead-ends encountered,
  - Number of assignments,
  - Stats on number of assignments before dead-ends

#### Core

- Rename to something more relevant. Maybe `sap`.
- Assess Slot vs. SlotIdentifier usage

### Dictionary

- Filtering

### CandidateChooser

- Perfs: Check if probe parallelization improves performances with default k and with bigger k

### Others

#### GraalVM

To test. See if performances are improved - but make backtrack more decent first

#### Add READMEs

- At root
- At subprojects (at least solver and dictionaries)
