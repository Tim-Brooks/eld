(ns eld.node)

(defprotocol Node
  (branch? [this])
  (leaf? [this]))

(defprotocol LeafNode
  (value [this]))

(defprotocol BranchNode
  (children [this])
  (condition [this])
  (feature [this])
  (next-node-id [this features])
  (set-children [this node-id]))