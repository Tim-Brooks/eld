(ns eld.implementation.array
  (:require [clojure.zip :as zip]
            [eld.node :as node]
            [eld.tree :as tree]))

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
  (feature [this] (aget ^objects this 1))
  (next-node-id [this features] (aget ^objects (aget ^objects this 3)
                                      ((aget ^objects this 2) features)))
  (set-children [this new-children] (aset ^objects this 3 new-children)))

(defn create-node [condition feature branch? children value]
  (if branch?
    (to-array [true feature condition (to-array children)])
    (to-array [false value])))

(defn create-node-from-map [{:keys [condition feature branch? children value]}]
  (create-node condition feature branch? children value))

(extend-protocol tree/Tree
  (Class/forName "[Ljava.lang.Object;")
  (to-zipper [this]
    (zip/zipper node/branch?
                (fn [node] (map #(aget ^objects this %) (node/children node)))
                (fn [node children]
                  (node/set-children node children))       ;; Does mutability make sense with zippers?
                (aget ^objects this 0)))
  (get-node [this node-id] (aget ^objects this node-id))
  (root [this] 0))

(defn create-tree [node-maps]
  (let [nodes (mapv create-node-from-map node-maps)]
    (to-array nodes)))