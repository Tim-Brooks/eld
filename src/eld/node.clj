(ns eld.node)

(defprotocol Node
  (branch? [this]))

(defprotocol LeafNode
  (value [this]))

(defprotocol BranchNode
  (children [this])
  (condition [this]))