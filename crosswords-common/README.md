<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## crosswords-common

`crosswords-common` is a library providing base data types related to the crossword domain. They
can be used by any other modules as building blocks for their own objects.

Since any modules can depend on it, `crosswords-common` shall ensure backwards compatibility, i.e.
newer versions of `crosswords-common` shall not break modules developed against previous versions of
`crosswords-common`.