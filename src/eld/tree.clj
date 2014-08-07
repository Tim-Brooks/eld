(ns eld.tree
  (:require [eld.node :as node]))

(set! *warn-on-reflection* true)

(extend-protocol node/Node
  (Class/forName "[Ljava.lang.Object;")
  (leaf? [this] (aget ^objects this 0)))

(extend-protocol node/LeafNode
  (Class/forName "[Ljava.lang.Object;")
  (value [this] (aget ^objects this 1)))

(extend-protocol node/BranchNode
  (Class/forName "[Ljava.lang.Object;")
  (children [this] (aget ^objects this 2))
  (condition [this] (aget ^objects this 1)))

; not leaf: [leaf? condition children]
; leaf: [leaf? value]
(defn- create-node [{:keys [condition leaf? children value] :as node-map}]
  (if leaf?
    (to-array [true value])
    (to-array [false condition (to-array children)])))

(defn tree [node-maps]
  (let [nodes (mapv create-node node-maps)]
    {:nodes (to-array nodes)
     :size (count nodes)}))

(defn score-tree [])