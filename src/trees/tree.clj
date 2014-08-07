(ns trees.tree)

(set! *warn-on-reflection* true)

(defprotocol Node
  (leaf? [this]))

(defprotocol LeafNode
  (value [this]))

(defprotocol BranchNode
  (children [this])
  (condition [this]))

(extend-protocol Node
  (Class/forName "[Ljava.lang.Object;")
  (leaf? [this] (aget ^objects this 0)))

(extend-protocol LeafNode
  (Class/forName "[Ljava.lang.Object;")
  (value [this] (aget ^objects this 1)))

(extend-protocol BranchNode
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
