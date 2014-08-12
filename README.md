# eld

A Clojure library to create fast, reducible, debuggable trees.

## Usage

Tree schema:

* Leaf Node:   {:leaf? true :value value}
* Branch Node: {:leaf? false :condition (fn [features] ...) :children []}

## License

Copyright © 2014 Tim Brooks

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
