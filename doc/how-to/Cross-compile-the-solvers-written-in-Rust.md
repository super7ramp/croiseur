<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## How To: Cross-compile the solvers written in Rust

### Audience

This how-to is aimed at developers desiring to cross-compile the solvers written in Rust so that
they can run on operating systems other than the host operating system.

Only Linux to Windows cross-compilation is documented for now.

It assumes a host running on Linux and the [`rustup`](https://rustup.rs) tool is installed.

### Steps

#### 1. Install Rustup

You probably have already done that, unless you are using a Rust toolchain provided by your
distribution.

##### openSUSE

```sh
zypper install rustup
```

#### 2. Install the Rust targets

```sh
rustup target add x86_64-unknown-linux-gnu x86_64-pc-windows-gnu    
```

#### 3. Install the compilers and linkers

###### openSUSE

```sh
zypper install mingw64-cross-gcc
```

#### 4. Cross-compile

```sh
gradle crossCompile
```

Command runs `cargo` with the appropriate `--targets` flags on all solvers written in Rust.