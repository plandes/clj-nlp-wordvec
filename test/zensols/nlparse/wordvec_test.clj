(ns zensols.nlparse.wordvec-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [zensols.util.zip :as zip]
            [zensols.nlparse.parse :as p]
            [zensols.nlparse.config :as conf :refer (with-context)]
            [zensols.nlparse.stopword :as st]
            [zensols.nlparse.feature.word-count :as wc]
            [zensols.nlparse.feature.word-similarity :as ws]
            [clojure.string :as s]))

(defonce ^:private parse-context
  (->> (conf/create-parse-config
        :pipeline [(conf/tokenize)
                   (conf/sentence)
                   (conf/part-of-speech)
                   (conf/morphology)
                   (conf/stopword)])
       conf/create-context))

(def ^:private word-context
  (assoc wc/*word-count-config*
         :word-form-fn #(-> % :lemma s/lower-case)
         :words-by-label-count 25))

(defn- parse [utterance]
  (with-context parse-context
    (p/parse utterance)))

(defn clean-tokens [utterance]
  (binding [wc/*word-count-config* word-context]
    (->> utterance
         parse
         p/tokens
         (filter st/go-word?))))

(defn word-count-feature-stats [corpus-lines]
  (binding [wc/*word-count-config* word-context]
    (with-open [is (io/input-stream (io/resource "books.zip"))]
      (->> (zip/doentries [is entry-is entry]
             (when-let [label (->> entry .getName (re-find #".*\/(.+).txt") second)]
               (with-open [reader (io/reader entry-is)]
                 (->> (line-seq reader)
                      (take corpus-lines)
                      (s/join " ")
                      parse
                      (hash-map :class-label label :instance)
                      (hash-map :continue true :result)))))
           (remove nil?)
           wc/calculate-feature-stats))))

;;; tests
(deftest similarity-test
  (testing "similarity"
    (is (= 78.0
           (-> (ws/similarity "king" "queen") (* 100) Math/floor)))))

(deftest similarity-distribution-test
  (testing "similarity-distribution-test"
    (binding [wc/*word-count-config* word-context]
      (let [wc-stats (word-count-feature-stats 5000)
            labels (->> ["Some say the whale can't open his mouth, but that is a fable"
                         "Doth represent with human countenance"
                         "The hero of the poem."]
                        (map clean-tokens)
                        (map #(ws/similarity-distribution % wc-stats))
                        (map #(-> % first first)))]
        (is (= ["moby-dick" "divine-comedy" "beowulf"] labels))))))
