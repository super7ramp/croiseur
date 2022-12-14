## TODO

### jni

#### Rust part

- Lifetimes
- Consistent error handling
- Doc
- Actually understand what I'm doing and rewrite it correctly

#### Java part (and Rust)

- Maybe rename `Grid` into `EmptyGrid`
- Maybe wrap returned `char[]` into `Solution` or `FilledGrid` (i.e. `Optional<char[]>` -> 
  `Optional<Solution>`)

### build

- Run cargo from gradle to generate the native library file.
- Create a jar for each platform.