(ns eld.node)

(defprotocol Node
  (leaf? [this]))

(defprotocol LeafNode
  (value [this]))

(defprotocol BranchNode
  (children [this])
  (condition [this]))