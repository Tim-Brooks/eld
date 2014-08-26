(ns eld.core
  (:require [eld.implementation.array :as array]
            [eld.implementation.vector :as vector]))

(defn array-tree [node-maps]
  "Returns an eld tree whose nodes are represented by object arrays."
  (let [tree (array/create-tree node-maps)]
    {:nodes tree
     :root  0}))

(defn vector-tree [node-maps]
  "Returns an eld tree whose nodes are represented by persistent vectors."
  (let [tree (vector/create-tree node-maps)]
    {:nodes tree
     :root  0}))

(defn forest [tree-vec]
  "Turns a vector of trees into a forest")