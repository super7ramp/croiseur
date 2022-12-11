## TODO

### composer

- Trim unnecessary stuff (UI)

### jni

#### Rust part

- Something that works
    - Currently, the last conversion between Rust's `Option<Vec<char>>` and Java's
      `Optional<char[]>` is broken (see `SolverTest`)
- Lifetimes
- Consistent error handling
- Doc
- Actually understand what I'm doing and rewrite it correctly 

#### Java part

- Maybe rename `Grid` into `EmptyGrid`
- Maybe wrap returned `char[]` into `Solution` (i.e. `Optional<char[]>` -> `Optional<Solution>`)

### build

- Deployment: Currently library path is only set for tests, it would be great to set it 
  correctly when library is run from an application (e.g. via `crosswords-gui:run`)
    - Use a gradle plugin for rust?
    - Do it by hand?
- Is `src/main/rust` ok? Should we put both JNI and Composer code in it?