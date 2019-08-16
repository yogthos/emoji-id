# emoji-id

A Clojure utility to convert numeric IDs into emoji strings and back (this probably doesn't need to exist)

### requirements

* [JDK](https://www.azul.com/downloads/zulu/)
* [GraalVM](https://www.graalvm.org/downloads/)
* [Leiningen](https://leiningen.org/)

### building

    lein native-image

The executable will be compiled in `target/emojify`

### Usage

    emojify :encode 12345
    🐳👄🐷

    emojify :decode 🐳👄🐷
    12345
