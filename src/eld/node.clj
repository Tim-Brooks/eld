(ns eld.node)

(defprotocol Node
  (branch? [this])
  (leaf? [this]))

(defprotocol LeafNode
  (value [this]))

(defprotocol BranchNode
  (id [this])
  (children [this])
  (condition [this])
  (feature [this])
  (next-node-id [this features])
  (set-children [this new-children]))