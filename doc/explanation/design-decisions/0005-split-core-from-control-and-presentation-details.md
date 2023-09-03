<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

# 5. Split core from control/presentation details

Date: 2023-06-28 (*a posteriori*)

Complements: [3. Split core from solver and dictionary details](0003-split-core-from-solver-and-dictionary-details.md)

## Status

Accepted

## Context

It would be nice if the project core was not coupled to a specific framework. This way, it could
be reused for several applications, e.g. a command-line interface and a graphical user interface.

## Decision

Split control/presentation details from core:

- Make the core (`croiseur`) a library which offers services to be called by a real application. It
  can be seen as the "Service" or the "Use Cases" layer described notably by Bob Martin in
  its [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).
- Define a presentation extension point (`croiseur-spi-presenter`) which can implemented to display
  the output of `croiseur`.
- Core API methods do not return results. Even methods looking semantically like queries (e.g. the
  method for listing the available solvers) does not return anything. Core drives the presentation:
  It forwards its results to the presenter SPI. Potentially a single call to core may lead to
  several calls to presenter by core: It depends on the use-case.

## Consequences

### Core is simpler to reason with

Since it does not manage control/presentation details. It needs no external framework.

### Core is reusable for several applications

Several frontends can be developed around core.

### Core API is unusual

From the caller point of view (typically a "controller"), it looks like:

- Everything is a command;
- The controller is left much less control about the execution.

It forces core library user to:

- Limit the logic in controllers: They are left to merely adapt the events they receive to the core
  API, maybe make the call in a different thread, but not much more.
- In particular, split the control logic from the presentation logic.

This might get difficult when using a framework requiring controllers to return a value to update a
kind of "view". How can a controller return a value when this value is set by a separate presenter
driven by the Use Cases layer?

![Controllers, presenters, view-model](https://plainionist.github.io/assets/clean-architecture/User.Interactor.Flow.png)

This [interesting blog post](https://www.plainionist.net/Implementing-Clean-Architecture-Controller-Presenter/),
from which comes the above figure, exposes with great details the problem. Solution proposed by
the author is to pass to the presenter a callback to set, by side effect, a view-model declared in
the controller. Controller can then return the view-model to the view.

In other words: Controller is given read-only access to the view-model, filled by the presenter,
so that the controller can return it (or some information it contains) to the framework. In the
figure above, you can imagine:

- Having a new dependency (straight black arrow) from controller to view-model;
- Having the flow (curved pink arrow) deviated when reaching the view-model: It does not go
  directly to the view, it passes by the controller that returns it to the view.

This kind of trick is necessary when using frameworks requiring a return value from the controllers
to update a "view", e.g. a Web framework like Spring. Frameworks allowing to bind the view to a
view-model, e.g. a GUI framework like JavaFx, does not require such a trick.
