<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Reference: Examples of Execution

### Solve Usecase

This is the usecase in which user requests to solve a grid, i.e. to automatically fill the grid.

#### Core Fragment

This is the fragment showing the interactions driven by core library `croiseur`.

```plantuml
group Solve Usecase
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

#### CLI

Here is the execution of `croiseur-cli` calling the core `croiseur` Solve Usecase.

```plantuml
skinparam responseMessageBelowArrow true

actor User
participant croiseur.cli.controller
participant croiseur.api.solver
participant "croiseur.spi.presenter\n(croiseur.cli.presenter)" as croiseur.spi.presenter

User -> croiseur.cli.controller : "croiseur-cli solver run --size 3x3"
activate croiseur.cli.controller
croiseur.cli.controller -> croiseur.cli.controller : Adapt request
activate croiseur.cli.controller
deactivate croiseur.cli.controller
croiseur.cli.controller -> croiseur.api.solver : Solve

activate croiseur.api.solver
activate croiseur.spi.presenter
ref over croiseur.api.solver, croiseur.spi.presenter : Solve Usecase\n
croiseur.spi.presenter -> croiseur.spi.presenter : Format result
activate croiseur.spi.presenter
deactivate croiseur.spi.presenter

croiseur.spi.presenter -> User : "Result: SUCCESS"\n\n| A | B | C |\n| D | E | F |\n| G | H | I |\n
deactivate croiseur.spi.presenter
deactivate croiseur.api.solver
deactivate croiseur.cli.controller
```

#### GUI

Here is the execution of `croiseur-gui` calling the core `croiseur` Solve Usecase.

```plantuml
skinparam responseMessageBelowArrow true

actor User
participant croiseur.gui.view
participant croiseur.gui.controller
participant croiseur.gui.view.model
participant croiseur.api.solver
participant "croiseur.spi.presenter\n(croiseur.gui.presenter)" as croiseur.spi.presenter

User -> croiseur.gui.view : Clicks on solve button 
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
end note
deactivate croiseur.gui.controller
deactivate croiseur.gui.view
activate croiseur.api.solver
activate croiseur.spi.presenter

ref over croiseur.api.solver, croiseur.spi.presenter : Solve Usecase\n

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

croiseur.gui.view -> User : | A | B | C |\n| D | E | F |\n| G | H | I |\n
deactivate croiseur.gui.view
deactivate croiseur.gui.view.model
```