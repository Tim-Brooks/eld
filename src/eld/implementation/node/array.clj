(ns eld.implementation.node.array
  (:require [eld.node :as node]))

(set! *warn-on-reflection* true)

(extend-protocol node/Node
  (Class/forName "[Ljava.lang.Object;")
  (branch? [this] (aget ^objects this 0))
  (leaf? [this] (not (aget ^objects this 0))))

(extend-protocol node/LeafNode
  (Class/forName "[Ljava.lang.Object;")
  (value [this] (aget ^objects this 1)))

(extend-protocol node/BranchNode
  (Class/forName "[Ljava.lang.Object;")
  (children [this] (aget ^objects this 3))
  (condition [this] (aget ^objects this 2))
  (feature [this] (aget ^objects this 1)))

(defn create-node [condition feature branch? children value]
  (if branch?
    (to-array [true feature condition (to-array children)])
    (to-array [false value])))

(defn create-node-from-map [{:keys [condition feature branch? children value]}]
  (create-node condition feature branch? children value))