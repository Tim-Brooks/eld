(ns eld.implementation.type
  (:require [clojure.zip :as zip]
            [eld.node :as node]
            [eld.tree :as tree]))

(set! *warn-on-reflection* true)

(deftype PersistentNode [leaf? feature value ^ints children condition]
  node/Node
  (branch? [this] (not leaf?))
  (leaf? [this]  leaf?)
  node/LeafNode
  (value [this] value)
  node/BranchNode
  (children [this] children)
  (condition [this] condition)
  (feature [this] feature)
  (next-node-id [this features]
    (aget children (condition features)))
  (set-children [this new-children]
    (PersistentNode. leaf? feature value new-children condition)))

(deftype PersistentTree [^int root ^objects nodes]
  tree/Tree
  (to-zipper [this]
    (zip/zipper node/branch?
                (fn [node] (map #(aget ^objects this %) (node/children node)))
                (fn [node children]
                  (node/set-children node children))       ;; Does mutability make sense with zippers?
                (aget ^objects this 0)))
  (get-node [_ node-id] (aget ^objects nodes node-id)))

(defn test-thing []
  (PersistentNode. false "feature" nil (int-array [1 2 3]) (fn [features] (get features "feature" 0))))