(ns eld.core
  (:require [eld.implementation.array :as array]))

(defn array-tree [node-maps]
  (let [tree (array/create-tree node-maps)]
    {:nodes tree
     :root  0}))