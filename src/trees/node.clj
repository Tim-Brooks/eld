(ns trees.node)

(defprotocol Node
  (leaf? [this]))

(defprotocol LeafNode
  (value [this]))

(defprotocol BranchNode
  (children [this])
  (condition [this]))

(extend-protocol Node
  (Class/forName "[Ljava.lang.Object;")
  (leaf? [this] (aget ^objects this 0)))