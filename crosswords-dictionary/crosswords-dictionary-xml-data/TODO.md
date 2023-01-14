<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

- Automate generation from Hunspell dictionaries, basically:
    - Refine dictionary tools to be able to set dictionary names and descriptions
    - Refine dictionary tools or xml writer or call another tool to format the generated XML
    - Refine dictionary tools or call another tool to validate the generated XML against XSD
    - Wraps these calls into gradle tasks for each dictionary in hunspell-data folder
    - Once everything works, remove generated XML dictionaries from repository (add to .gitignore)