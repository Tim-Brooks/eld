(ns eld.tree
  (:require [eld.node :as node]
            [eld.implementation.array :as array]))

(set! *warn-on-reflection* true)

(defn tree [node-maps]
  (let [nodes (mapv array/create-node node-maps)]
    {:nodes (to-array nodes)
     :size (count nodes)
     :root 0}))

(defn score-tree [{:keys [nodes root]} features]
  (loop [node (nth nodes root)]
    (if (node/leaf? node)
      (node/value node)
      (recur (nth nodes ((node/condition node) features))))))