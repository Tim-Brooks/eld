(ns eld.implementation.vector
  (:require [eld.node :as node]))

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
  (set-children! [this children] (assoc this 3 children)))

(defn create-node [condition feature branch? children value]
  (if branch?
    [true feature condition children]
    [false value]))

(defn create-node-from-map [{:keys [condition feature branch? children value]}]
  (create-node condition feature branch? children value))
