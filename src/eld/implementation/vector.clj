(ns eld.implementation.vector
  (:require [clojure.zip :as zip]
            [eld.node :as node]
            [eld.tree :as tree]))

(set! *warn-on-reflection* true)

(extend-protocol node/Node
  (Class/forName "clojure.lang.PersistentVector")
  (branch? [this] (nth this 0))
  (leaf? [this] (not (nth this 0))))

(extend-protocol node/LeafNode
  (Class/forName "clojure.lang.PersistentVector")
  (value [this] (nth this 1)))

(extend-protocol node/BranchNode
  (Class/forName "clojure.lang.PersistentVector")
  (children [this] (nth this 3))
  (condition [this] (nth this 2))
  (feature [this] (nth this 1))
  (next-node-id [this features] (nth (node/children this)
                                     ((node/condition this) features)))
  (set-children [this children] (assoc this 3 children)))

(defn create-node [condition feature branch? children value]
  (if branch?
    [true feature condition children]
    [false value]))

(defn create-node-from-map [{:keys [condition feature branch? children value]}]
  (create-node condition feature branch? children value))


(extend-protocol tree/Tree
  (Class/forName "clojure.lang.PersistentVector")
  (to-zipper [this]
    (zip/zipper node/branch?
                (fn [node] (map #(aget ^objects this %) (node/children node)))
                (fn [node children]
                  (node/set-children node children))       ;; Does mutability make sense with zippers?
                (nth ^objects this 0)))
  (get-node [this node-id] (nth this node-id)))

(defn create-tree [node-maps]
  (let [nodes (mapv create-node-from-map node-maps)]
    (to-array nodes)))