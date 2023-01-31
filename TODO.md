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

#### Support characters beyond U+FFFF (solver, dictionary)

> (Currently solver assumes that the content of the box can be represented as a single char, i.e. 
> a 16bit value, i.e. a character belonging to `[U+0000, U+FFFF]`. Supplementary characters need 2 
> chars. That's UTF-16.)

- Look for the languages for which it might be useful

#### Support output formatter as plugins

- `croiseur-spi-formatter`: Defines the API.
- `croiseur-formatter-ipuz-plugin`: ipuz implementation (json like, use gson?)
- `croiseur-formatter-pdf-plugin`: pdf implementation (check LaTeX packages).

#### Suggest (interesting) definitions

For the words of a solved grid. It can be a funny problem to explore.

- `croiseur-spi-suggestion`: Defines the API.

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
  - Alternatively: Create a dedicated project 

##### XML

There seems to be some duplicates in Spanish dictionary. Should be harmless but still, to clean up.

##### Other providers

- Look for them.
- There is an XML format that is called XDXF which looks interesting but couldn't find a simple
  organised list of dictionaries for them.

#### Others

##### Deployment

- Currently, it's possible to use `crosswords-cli` and `crosswords-gui` `installDist` tasks, but
  it's lacking some customization.
- Some deployment ideas:
  - Native deployment
- Create Linux packages (RPM/DEB).
- Take a look at jpackage.

##### READMEs/Documentation

- Every subproject should have one
- Complete them, see TODOs in files directly

##### Naming

- Find a real name for the project. Ideas: `croiseur`, `croiseur-rouge` (because it sounds good), 
  `cr`, `croisette`.