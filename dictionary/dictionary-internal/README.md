## dictionary-internal

`dictionary-internal` is a library able to read dictionaries in a custom binary format.

The binary format is just the serialized form of
the [`InternalDictionary`](src/main/java/com/gitlab/super7ramp/crosswords/dictionary/internal/InternalDictionary.java)
class.

`dictionary-internal` can be plugged as a dictionary provider for the `crosswords` library.