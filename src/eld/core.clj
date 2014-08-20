(ns eld.core
  (:require [eld.implementation.array :as array]
            [eld.implementation.vector :as vector]))

(defn array-tree [node-maps]
  (let [tree (array/create-tree node-maps)]
    {:nodes tree
     :root  0}))

(defn vector-tree [node-maps]
  (let [tree (vector/create-tree node-maps)]
    {:nodes tree
     :root  0}))