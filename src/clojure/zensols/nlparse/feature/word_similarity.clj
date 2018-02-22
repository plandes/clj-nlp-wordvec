(ns ^{:doc "Create word vector features..
[Glove](https://nlp.stanford.edu/projects/glove/) word vectors."
      :author "Paul Landes"}
    zensols.nlparse.feature.word-similarity
  (:import [org.deeplearning4j.models.embeddings.loader WordVectorSerializer])
  (:require [clojure.string :as s]
            [clojure.java.io :as io]
            [zensols.nlparse.parse :as p]
            [zensols.actioncli.util :refer (defnlock)]
            [zensols.actioncli.resource :as res]
            [zensols.nlparse.feature.resource :as wr]))

(wr/initialize)

(defn- model-resource []
  (res/resource-path :glove-model))

(defnlock glove-model
  []
  (WordVectorSerializer/loadTxtVectors (model-resource)))

(defn- persist-model []
  (->> (:init-resource (meta #'glove-model))
       (WordVectorSerializer/writeWord2VecModel (model-resource))))

(defn similarity
  "Return the cosine similarirty of two word vectors."
  [a b]
  (-> (glove-model)
      (.similarity a b)))

(defn- similarity-tokens
  "Return the cosine similarity across string tokens and a *distribution
  word* or 0 if either is out of vocabulary."
  [dist-word text-tokens]
  (->> text-tokens
       (map (fn [tok]
              (let [sim (similarity tok dist-word)]
                (if (Double/isNaN sim) 0 sim))))))

(defn- similarity-label
  "Return the similarity from a *distribution word* and all text tokens as a
  weighted average with a partition function over the token length."
  [dist text-tokens tok-count]
  (->> dist
       (map (fn [[dist-word weight]]
              (->> (similarity-tokens dist-word text-tokens)
                   (apply +)
                   (* weight))))
       (apply +)))

(defn similarity-distribution
  "Return the similarity from all *distribution words* in **word-count-stats**
  and all tokens as a weighted average with a partition function over the token
  length."
  [tokens word-count-stats]
  (let [text-tokens (map #(-> % :text s/lower-case) tokens)
        tok-count (count text-tokens)
        word-count-stats (:word-count-dist word-count-stats)]
    (->> word-count-stats
         (map (fn [[label dist]]
                (let [p-hat (similarity-label dist text-tokens tok-count)]
                  [label (/ p-hat tok-count)])))
         (sort #(compare (second %2) (second %1))))))

(defn similarity-features
  "Create similarity features using the MLE take from
  the [[similarity-distribution]]."
  [tokens word-count-stats]
  (->> (similarity-distribution tokens word-count-stats)
       first
       (zipmap [:similarity-top-label :similarity-score])))

(defn similarity-feature-metas
  "Return the metadata for [[similarity-features]]."
  [labels]
  [[:similarity-top-label labels]
   [:similarity-score 'numeric]])
