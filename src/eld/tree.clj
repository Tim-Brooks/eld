(ns eld.tree
  (:require [eld.node :as node]
            [eld.implementation.array :as array]))

(set! *warn-on-reflection* true)

(defn tree [node-maps]
  (let [nodes (mapv array/create-node node-maps)]
    {:nodes (to-array nodes)
     :size (count nodes)
     :root 0}))

(defn score-tree-with-path [{:keys [nodes root]} features]
  (loop [node-index root
         path (transient [])]
    (let [modified-path (conj! path node-index)
          node (nth nodes node-index)]
      (if (node/leaf? node)
        {:value (node/value node) :path (persistent! path)}
        (recur (nth (node/children node) ((node/condition node) features))
               modified-path)))))

(defn score-tree [{:keys [nodes root]} features]
  (loop [node (nth nodes root)]
    (if (node/leaf? node)
      (node/value node)
      (recur (nth nodes (nth (node/children node)
                             ((node/condition node) features)))))))