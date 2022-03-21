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

### Support output formatter as plugins

- formatter-api: Defines the API. Depends on input types so presently on solver result so depends on
  solver-api (one day maybe it will be on crosswords-datatypes)
- formatter-xml: xml implementation (jaxb?).
- formatter-json: json implementation (gson?).
- formatter-pdf: pdf implementation (check LaTeX packages).
- formatter-docx: docx implementation (rely on some Apache library).
- How to combine it with frontend publishers?

### Suggest definitions

For the words of a solved grid. It can be funny a problem to explore.

### Make a GUI

Don't lose yourself, backend is what you know.

## Improvements

### Solver

#### GridDataBuilder

- Make it more readable
- Should ID creation/incrementation be in GridDataBuilder? => maybe not if id is used for
  backtracking

#### More progress indications

- Find a way to display grid being solved
- More stats

#### Core

- Rename to something more relevant. Maybe `sap`.
- Assess Slot vs. SlotIdentifier usage
- Split pervasive Slot interface

#### CandidateChooser

- Perfs: Check if probe parallelization improves performances with default k and with bigger k

#### Backtrack

Make sure the selected unassigned variable(s) actually solve the issue (i.e. the estimated number of
solutions after unassignment is > 0). See Ginsberg papers.

### Dictionary

#### Hunspell

- It would be really nice to have a comprehensive implementation of Hunspell in pure Java, even if
  we wouldn't need all features for crosswords
  - Check (again) if that exists or if someone is working on it or is planning to work on it
  - Assess original Hunspell library and if it is worth trying to work with it using JNI
- Alternatively: Progressively improve the current library.
  - Currently, only French "works" (at least it doesn't fail on read)
  - Try to fix English
  - Then continue with e.g. Spanish or German or Italian
  - Then maybe it's time to take a step back and make a plan about what should be done to have a
    decent library (at least for our context, i.e. just expanding all forms of the dictionary)

#### Binary

- Default lookup paths to improve, currently it relies on InternalDictionaryProvider resource loader
  which is limiting.
- Read:
  - https://www.oracle.com/java/technologies/javase/seccodeguide.html
  - https://docs.oracle.com/en/java/javase/16/core/serialization-filtering1.html#GUID-55BABE96-3048-4A9F-A7E6-781790FF3480

#### Other providers

Look for them

#### Common (maybe solver job after all)

- Rationalize filtering: Should only ASCII character be taken into account? No accent? See also
  point on UTF8 support.

### crosswords

#### Transform publishers into service provider

- Move crosswords' Publisher into a dedicated module publisher-api.
- Implemented by frontends.

### Others

#### Add READMEs

- At root
- At subprojects (at least solver and dictionaries)
