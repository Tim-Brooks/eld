(ns eld.implementation.array
  (:require [eld.node :as node]))

(set! *warn-on-reflection* true)

(extend-protocol node/Node
  (Class/forName "[Ljava.lang.Object;")
  (branch? [this] (aget ^objects this 0)))

(extend-protocol node/LeafNode
  (Class/forName "[Ljava.lang.Object;")
  (value [this] (aget ^objects this 1)))

(extend-protocol node/BranchNode
  (Class/forName "[Ljava.lang.Object;")
  (children [this] (aget ^objects this 2))
  (condition [this] (aget ^objects this 1)))

(defn create-node [condition branch? children value]
  (if branch?
    (to-array [true condition (to-array children)])
    (to-array [false value])))

(defn create-node-from-map [{:keys [condition branch? children value]}]
  (create-node condition branch? children value))