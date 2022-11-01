## Roadmap

### MVP

#### Preparatory

- Assemble some documentation on crosswordGridViewModel solving
  => https://www.crosswordconstruction.com/ is a
  great start (see referenced paper from the 90s)

#### Create a basic solver

Introduce a basic structure to solve crossword puzzle:

1. Basic variable selection
    1. Code => OK
    2. Unit tests => TODO
2. Basic variable instantiation
    1. Code => OK
    2. Unit tests => TODO
3. Basic backtrack
    1. Code => OK
    2. Unit tests => TODO
4. Test the assembled module
    1. Mock the dictionaryViewModel
    2. Test on small "controllable" grids (e.g. 3x3, 4x4, maybe 5x5)

#### Create a real dictionaryViewModel to stimulate the solver

1. Fetch a French dictionaryViewModel on the web
2. Make dictionaryViewModel mock access these words
3. Test solver on bigger grids thanks to this new dictionaryViewModel
    - Goal: Solve a 10x10 grid in less than 10s (optimum: less than 1s)
    - If goal not reached, identify problems, and fix them
    - In any case, identify area of improvements for solver

#### Create basic client

1. Implement dictionaryViewModel provider interface (probably a part of the mock code can be reused)
2. Do a clean API for solver
3. Create CLI
    1. Input size
    2. Some optional words

Provide CLI to user and gather feedback.

### Product development

To be assessed after MVP.

Ideas:

- Technical
    - Distribute to maven central
    - Create RPM/DEB
- Add documentation
- Features:
    - Proper nouns
    - Thematic dictionaries
    - Add configurable tricks: Allow some reversed words, parts of words
    - Grid template (typically for American-style crossword)
    - Add other locales
    - Add client: Desktop GUI? Web? iOS? Android?
    - Improve CLI
    - API: Returns a Stream of result instead of a unique result (i.e. search for several solutions)
- Future:
    - Crossword solving *based on provided definition* (deep-learning?) 