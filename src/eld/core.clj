(ns eld.core
  (:require [eld.implementation.array :as array]
            [eld.implementation.type :as type]
            [eld.implementation.vector :as vector]))

(defn array-tree [node-maps]
  "Returns an eld tree whose nodes are represented by object arrays."
  (array/create-tree node-maps))

(defn vector-tree [node-maps]
  "Returns an eld tree whose nodes are represented by persistent vectors."
  (vector/create-tree node-maps))

(defn eld-tree [node-maps]
  "Returns an eld tree."
  (type/create-tree node-maps))

(defn forest [tree-vec]
  "Turns a vector of trees into a forest"
  )