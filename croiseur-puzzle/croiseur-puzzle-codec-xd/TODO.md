<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## TODO

- Rebus (multiple letters in a single cell): Not supported by croiseur solvers but shouldn't be too
  complicated to support at least in the codec.
- Special: Optional metadata, whose value is either "shaded" or "circle", indicating a special style
  for letters which are lowercase. So just for presentation purpose.
- Full UTF-8 support: For now, only code points between U+0000 and U+FFFF are supported (a.k.a the
  Basic Multilingual Plane or BMP)
- Format V2 (support for markdown-like headers instead of the 3 new-lines to separate sections)
- Multi-line clues
- Notes section