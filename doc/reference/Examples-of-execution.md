<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Reference: Examples of Execution

### Conventions

The following sequence diagrams aim to give an understanding of the execution inside both
core `croiseur` library and applications using it.

They do not aim to:

- Describe other components internals (e.g. crossword solvers – this may be done in each solver
  project);
- Be precise enough for code generation.

With that in mind, the following conventions apply to all diagrams:

- No specific class name: Only package names are used (and some liberty may be taken to avoid
  mentioning implementation-private packages);
- Happy path only: Error handling is omitted.

### Puzzle Usecases

This section presents examples of execution for usecases related primarily to saved puzzles
management.

#### Import Puzzle

This is the usecase in which user requests to import a puzzle to the puzzle repository.

##### Core Fragment

This is the fragment showing the interactions driven by core library `croiseur`.

```plantuml
group Import Puzzle Usecase
participant croiseur.api.puzzle.importer
participant croiseur.spi.puzzle.codec
participant croiseur.spi.puzzle.repository
participant croiseur.spi.puzzle.presenter

activate croiseur.api.puzzle.importer
croiseur.api.puzzle.importer -> croiseur.api.puzzle.importer : Select decoder
activate croiseur.api.puzzle.importer
deactivate croiseur.api.puzzle.importer

croiseur.api.puzzle.importer -> croiseur.spi.puzzle.codec : Decode puzzle
activate croiseur.spi.puzzle.codec
croiseur.spi.puzzle.codec -> croiseur.api.puzzle.importer : Decoded puzzle
deactivate croiseur.spi.puzzle.codec

croiseur.api.puzzle.importer -> croiseur.spi.puzzle.repository : Save puzzle
activate croiseur.spi.puzzle.repository
croiseur.spi.puzzle.repository -> croiseur.api.puzzle.importer : Saved puzzle
deactivate croiseur.spi.puzzle.repository

croiseur.api.puzzle.importer -> croiseur.spi.puzzle.presenter : Present saved puzzle
activate croiseur.spi.puzzle.presenter
```

#### CLI

Here is the execution of `croiseur-cli` calling the core `croiseur` Import Puzzle usecase.

```plantuml
skinparam responseMessageBelowArrow true

actor User
participant croiseur.cli.controller
participant croiseur.api.puzzle.importer
participant "croiseur.spi.presenter\n(croiseur.cli.presenter)" as croiseur.spi.presenter

User -> croiseur.cli.controller : croiseur-cli puzzle import \\\nexample_file
activate croiseur.cli.controller
croiseur.cli.controller -> croiseur.cli.controller : Adapt request\n(create an input stream)
activate croiseur.cli.controller
deactivate croiseur.cli.controller
croiseur.cli.controller -> croiseur.api.puzzle.importer : Import

activate croiseur.api.puzzle.importer
activate croiseur.spi.presenter
ref over croiseur.api.puzzle.importer, croiseur.spi.presenter : Import Puzzle Usecase\n
croiseur.spi.presenter -> croiseur.spi.presenter : Format saved puzzle
activate croiseur.spi.presenter
deactivate croiseur.spi.presenter

croiseur.spi.presenter -> User : \
Saved puzzle.\n\
\n\
Identifier: 1\n\
Revision: 1\n\
Title: Example Grid\n\
Author: Me\n\
Editor: (No one)\n\
Copyright: Public Domain\n\
Date: 2023-06-19\n\
Grid:\n\
|A|B|C|\n\
|D|E|F|\n\
|G|H|I|

deactivate croiseur.spi.presenter
deactivate croiseur.api.puzzle.importer
deactivate croiseur.cli.controller
```

#### GUI

Here is the execution of `croiseur-gui` calling the core `croiseur` Import Puzzle usecase.

```plantuml
skinparam responseMessageBelowArrow true

actor User
participant croiseur.gui.view
participant croiseur.gui.controller
participant croiseur.gui.view.model
participant croiseur.api.puzzle.importer
participant "croiseur.spi.presenter\n(croiseur.gui.presenter)" as croiseur.spi.presenter

User -> croiseur.gui.view : <img:"image/croiseur-gui-puzzle-import.png"{scale=0.75}>
activate croiseur.gui.view

croiseur.gui.view -> croiseur.gui.controller : Import puzzle
activate croiseur.gui.controller

croiseur.gui.controller -> croiseur.gui.controller : Create import puzzle task
activate croiseur.gui.controller
deactivate croiseur.gui.controller

croiseur.gui.controller ->> croiseur.api.puzzle.importer : Execute import puzzle task
note right
 Asynchronous call: Import puzzle task
 is executed in a background thread
 in order not to freeze the view.
end note
deactivate croiseur.gui.controller
deactivate croiseur.gui.view
activate croiseur.api.puzzle.importer
activate croiseur.spi.presenter

ref over croiseur.api.puzzle.importer, croiseur.spi.presenter : Import Puzzle Usecase\n

croiseur.spi.presenter -> croiseur.spi.presenter : Adapt saved puzzle
activate croiseur.spi.presenter
deactivate croiseur.api.puzzle.importer
deactivate croiseur.spi.presenter

croiseur.spi.presenter ->> croiseur.gui.view.model : Add an element to the puzzle list
note left
 Asynchronous call: View model
 (and thus the view) is updated
 in the application thread.
end note
activate croiseur.gui.view.model
deactivate croiseur.spi.presenter
deactivate croiseur.api.solver

croiseur.gui.view.model -> croiseur.gui.view : Notify update
activate croiseur.gui.view

croiseur.gui.view -> User : <img:"image/croiseur-gui-puzzle-imported.png"{scale=0.75}>

deactivate croiseur.gui.view
deactivate croiseur.gui.view.model
```

### Solver Usecases

This section presents examples of execution for usecases related primarily to the crossword solvers.

#### Solve Grid

This is the usecase in which user requests to solve a grid, i.e. to automatically fill the grid.

##### Core Fragment

This is the fragment showing the interactions driven by core library `croiseur`.

```plantuml
group Solve Grid Usecase
participant croiseur.api.solver
participant croiseur.spi.dictionary
participant croiseur.spi.solver
participant croiseur.spi.puzzle.repository
participant croiseur.spi.presenter

activate croiseur.api.solver
croiseur.api.solver -> croiseur.spi.dictionary : Select dictionary (filter on provider and name)

croiseur.api.solver -> croiseur.spi.solver : Select solver (filter on name)

opt Save Grid
  croiseur.api.solver -> croiseur.spi.puzzle.repository : Save grid in new puzzle
  activate croiseur.spi.puzzle.repository
  croiseur.spi.puzzle.repository -> croiseur.api.solver : Saved puzzle
  deactivate croiseur.spi.puzzle.repository
  croiseur.api.solver -> croiseur.spi.presenter : Present saved puzzle
  activate croiseur.spi.presenter
  deactivate croiseur.spi.presenter
end opt

opt Shuffle Dictionary
  croiseur.api.solver -> croiseur.api.solver : Shuffle dictionary
  activate croiseur.api.solver
  deactivate croiseur.api.solver
end opt

croiseur.api.solver -> croiseur.spi.solver : Run solver
activate croiseur.spi.solver
croiseur.spi.solver -> croiseur.api.solver : Solver result
deactivate croiseur.spi.solver

opt Save Grid && Solver Result is a Success
  croiseur.api.solver -> croiseur.spi.puzzle.repository : Save grid in puzzle created above
  activate croiseur.spi.puzzle.repository
  croiseur.spi.puzzle.repository -> croiseur.api.solver : Saved puzzle
  deactivate croiseur.spi.puzzle.repository
  croiseur.api.solver -> croiseur.spi.presenter : Present saved puzzle
  activate croiseur.spi.presenter
  deactivate croiseur.spi.presenter
end opt

croiseur.api.solver -> croiseur.spi.presenter : Present solver result
activate croiseur.spi.presenter
end
```

##### CLI

Here is the execution of `croiseur-cli` calling the core `croiseur` Solve Usecase.

```plantuml
skinparam responseMessageBelowArrow true

actor User
participant croiseur.cli.controller
participant croiseur.api.solver
participant "croiseur.spi.presenter\n(croiseur.cli.presenter)" as croiseur.spi.presenter

User -> croiseur.cli.controller : croiseur-cli solver run \\\n--size 3x3
activate croiseur.cli.controller
croiseur.cli.controller -> croiseur.cli.controller : Adapt request
activate croiseur.cli.controller
deactivate croiseur.cli.controller
croiseur.cli.controller -> croiseur.api.solver : Solve

activate croiseur.api.solver
activate croiseur.spi.presenter
ref over croiseur.api.solver, croiseur.spi.presenter : Solve Grid Usecase\n
croiseur.spi.presenter -> croiseur.spi.presenter : Format result
activate croiseur.spi.presenter
deactivate croiseur.spi.presenter

croiseur.spi.presenter -> User : \
Result: SUCCESS\n\n\
|A|B|C|\n\
|D|E|F|\n\
|G|H|I|

deactivate croiseur.spi.presenter
deactivate croiseur.api.solver
deactivate croiseur.cli.controller
```

##### GUI

Here is the execution of `croiseur-gui` calling the core `croiseur` Solve Usecase.

```plantuml
skinparam responseMessageBelowArrow true

actor User
participant croiseur.gui.view
participant croiseur.gui.controller
participant croiseur.gui.view.model
participant croiseur.api.solver
participant "croiseur.spi.presenter\n(croiseur.gui.presenter)" as croiseur.spi.presenter

User -> croiseur.gui.view : <img:"image/croiseur-gui-solver-solve.png"{scale=0.75}>
activate croiseur.gui.view

croiseur.gui.view -> croiseur.gui.controller : Solve
activate croiseur.gui.controller

croiseur.gui.controller -> croiseur.gui.controller : Create solve task
activate croiseur.gui.controller
croiseur.gui.controller -> croiseur.gui.view.model : Retrieve grid and\nselected dictionaries
deactivate croiseur.gui.controller

croiseur.gui.controller ->> croiseur.api.solver : Execute solve task
note right
 Asynchronous call: Solve task is
 executed in a background thread
 in order not to freeze the view.

 Some elements of the view are
 disabled while the task is
 running though (e.g. the grid
 edition controls) to prevent
 inconsistencies when the task
 finishes.
end note
deactivate croiseur.gui.controller
deactivate croiseur.gui.view
activate croiseur.api.solver
activate croiseur.spi.presenter

ref over croiseur.api.solver, croiseur.spi.presenter : Solve Grid Usecase\n

croiseur.spi.presenter -> croiseur.spi.presenter : Adapt result
activate croiseur.spi.presenter
deactivate croiseur.spi.presenter

croiseur.spi.presenter ->> croiseur.gui.view.model : Update grid
note left
 Asynchronous call: View model
 (and thus the view) is updated
 in the application thread.
end note
activate croiseur.gui.view.model
deactivate croiseur.spi.presenter
deactivate croiseur.api.solver

croiseur.gui.view.model -> croiseur.gui.view : Notify update
activate croiseur.gui.view

croiseur.gui.view -> User : <img:"image/croiseur-gui-solver-solved.png"{scale=0.75}>

deactivate croiseur.gui.view
deactivate croiseur.gui.view.model
```