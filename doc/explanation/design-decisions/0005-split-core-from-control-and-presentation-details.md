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

It would be nice if the application core was not coupled to a specific framework. This way, it could
be reused for several applications, e.g. a command-line interface and a graphical user interface.

## Decision

Split control/presentation details from core:

- Make the core (`croiseur`) a library which offers service to be called by a real application. It
  can be seen as the "Use Cases" layer coined by
  the [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).
- Define a presentation extension point (`croiseur-spi-presenter`) which can implemented to display
  the output of `croiseur`.
- Core API methods do not return results. Even methods looking semantically like queries (e.g. the
  method for listing the available solvers) does not return anything. Core drives the presentation:
  It forwards its results to the presenter SPI. Potentially a single call to core may lead to
  several call to presenter by core: It depends on the use-case.[^1]

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

This might get difficult when using a framework requiring controllers to return a value. Maybe
implementing a kind a communication mean between controllers and presenters would work, but no
guarantee that it will look good.

Documentation will be important to reduce confusion.

---
[^1]: See [this interesting blog post](https://www.plainionist.net/Implementing-Clean-Architecture-Controller-Presenter/)
describing another approach: In terms of dependencies, control/presentation layer is well split from
the use-case layer but use-case layer doesn't drive the presentation. Use-case API looks more like
a classic command-query API.
