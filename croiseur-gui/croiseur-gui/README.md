<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-gui

Root module of Croiseur GUI:

- Instantiates other submodules
- Acts as root controller for `croiseur-gui-view` events, stripping them from JavaFx graphics
  information and forwarding them to `croiseur-gui-controller`.