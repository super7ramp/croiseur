# TODO

## Features

### Allow user to iterate over solutions (CLI, solver)

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

#### CandidateChooser

- Perfs: Check if probe parallelization improves performances with default k and with bigger k

### Dictionary

- Filtering

### Others

#### Add READMEs

- At root
- At subprojects (at least solver and dictionaries)
