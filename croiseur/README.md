<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur

`croiseur` is the core library of the project and gives it its name.

This library defines and implements the use-cases of the application. It interacts with various
[service providers](../croiseur-spi) to get the complex jobs (e.g. solving a grid) and/or the
technical jobs (e.g. presenting the result) done.

It can be seen as the "Use Cases" layer coined by
the [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).
