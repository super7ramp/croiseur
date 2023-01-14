<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## TODO

### jni

#### Java part

- `NativeLibLoader` (derived from JavaFX): Licence GPL-2.0-only WITH Classpath-exception-2.0 seems 
  not compatible with GPL-3.0-or-later (the classpath exception doesn't apply here as far as I 
  understand). Rewrite it entirely (and simplify it) or use NativeUtils which is under compatible
  MIT licence.

#### Rust part

- Lifetimes
- Doc
- Actually understand what I'm doing and rewrite it correctly
