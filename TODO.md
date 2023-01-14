<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## TODO

### Features

#### Allow user to iterate over solutions (usecase + UI + solver)

- What's the best for user?
  1. Add an option to specify the number of solutions: Internally, mark the n-1 solutions found as
     dead-end and continue algorithm
  2. Add an option `--random`: Does shuffling dictionary suffice?
- Not sure what's the best for speed

#### Allow user to stop the solver after completion threshold reached (usecase + UI + probably solver)

- CLI: Add an option `--threshold`
- Implementation:
  - Interrupt solver once threshold reached? Not reliable as solver may already have backtracked
    when interrupted status takes effect
  - Add a decorator on slot iterator so that its `hasNext()` returns `false` when threshold is
    reached? Requires modification in solver

#### Allow user to stop after timeout (UI + maybe usecase + maybe solver)

- CLI: Add an option `--timeout`
- UI only or add it as a real usecase solve option?

#### More dictionaries (dictionary, tools)

- All Hunspell dictionaries available.
- Create gradle task to automate call to tool to convert Hunspell format to XML format.

#### Support UTF8 (solver, dictionary)

- Don't think it's necessary for French, it might be useful for other languages

#### Support output formatter as plugins

- `crosswords-spi-formatter`: Defines the API.
- `crosswords-formatter-ipuz-plugin`: ipuz implementation (json like, use gson?)
- `crosswords-formatter-pdf-plugin`: pdf implementation (check LaTeX packages).

#### Suggest (interesting) definitions

For the words of a solved grid. It can be a funny problem to explore.

- `crosswords-spi-suggestion`: Defines the API.

#### Add special treatment

Allow user-defined treatment on dictionary entries, e.g. reverse order, consonant only, ... I 
think that's what qxw call 'answer treatment'.

### Improvements

#### CLI

- Add some end-to-end tests.
- Enhance text output format: Column and row numbers.

#### GUI

- Allow skinning of the crossword grid: French style, British style, German style, American 
  style, ...
- Add numbers to the grid
- Add a way to choose the solver without bloating the interface
- Implement solver progress presentation
- Implement solver/dictionary errors presentation
- Modal grid edition toolbar (i.e. hide grid edition buttons by default, they take a lot of space)

#### Solver: Ginsberg

##### GridDataBuilder

- Make it more readable
- Should ID creation/incrementation be in GridDataBuilder? => maybe not if id is used for
  backtracking

##### More progress indications

- Find a way to display grid being solved
- More stats

##### Core

- Assess Slot vs. SlotIdentifier usage
- Split pervasive Slot interface

##### CandidateChooser

- Perfs: Check if probe parallelization improves performances with default k and with bigger k

##### Backtrack

Make sure the selected unassigned variable(s) actually solve the issue (i.e. the estimated number of
solutions after unassignment is > 0). See Ginsberg papers.

##### Bugs

* Performance issues with some languages: Solver is way slower with e.g. Spanish dictionary. Is 
  it because the solver doesn't scale when number of words increase? It is because some words are 
  causing problems (invalid characters, spaces)?
* Sometimes, solver marks the grid as impossible but doesn't precise which slot has no candidate 
  left.

#### Solver: Other providers

- pauglb (Crossword Composer):
    - Improve Rust binding code for Crossword Composer
    - Assess what needs to be done in order to be able to pass pre-defined letters
- To try:
    - https://github.com/szunami/xwords-rs
    - https://www.quinapalus.com/qxw.html: Not sure where the solver is

#### Dictionary

##### Hunspell

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

##### XML

There seems to be some duplicates in Spanish dictionary. Should be harmless but still, to clean up.

##### Other providers

- Look for them.
- There is an XML format that is called XDXF which looks interesting but couldn't find a simple
  organised list of dictionaries for them.

##### Common

- Rationalize filtering: Should only ASCII character be taken into account? No accent? See also
  point on UTF8 support.

#### Others

##### Deployment

- Currently, it's possible to use `crosswords-cli` and `crosswords-gui` `installDist` tasks, but
  it's lacking some customization.
- Some deployment ideas:
  - Native deployment
- Create Linux packages (RPM/DEB).

##### READMEs/Documentation

- Every subproject should have one
- Complete them, see TODOs in files directly

##### Naming

- Find a real name for the project. Ideas: `croiseur`, `croiseur-rouge` (because it sounds good), 
  `cr`, `croisette`.