(ns ^{:doc "Train a word vector model
[Example](https://github.com/deeplearning4j/dl4j-examples/blob/master/dl4j-examples/src/main/java/org/deeplearning4j/examples/nlp/glove/GloVeExample.java)."
      :author "Paul Landes"}
    zensols.nlparse.word-similarity-train
  (:import [org.deeplearning4j.models.glove Glove$Builder]
           [org.deeplearning4j.text.sentenceiterator
            BasicLineIterator SentenceIterator]
           [org.deeplearning4j.text.tokenization.tokenizer.preprocessor
            CommonPreprocessor]
           [org.deeplearning4j.text.tokenization.tokenizerfactory
            DefaultTokenizerFactory]
           [org.deeplearning4j.parallelism ParallelWrapper$Builder]
           [org.deeplearning4j.models.embeddings.loader WordVectorSerializer])
  (:require [clojure.java.io :as io]
            [zensols.actioncli.util :refer (defnlock)]))

(defn- create-model [file]
  (let [iter (BasicLineIterator. file)
        tok (DefaultTokenizerFactory.)]
    (.setTokenPreProcessor tok (CommonPreprocessor.))
    (-> (Glove$Builder.)
        (.iterate iter)
        (.tokenizerFactory tok)
        (.alpha 0.75)
        (.learningRate 0.1)

        ;; number of epochs for training
        (.epochs 10)

        ;; cutoff for weighting function
        (.xMax 100)

        ;; training is done in batches taken from training corpus
        (.batchSize 200)

        ;; if set to true, batches will be shuffled before training
        (.shuffle true)

        ;; if set to true word pairs will be built in both directions, LTR and RTL
        (.symmetric true)

        (.workers 128)
        .build)))

(defn train [fname]
  (let [file (io/file fname)
        model (create-model file)]
    (reset! model model)
    (.fit model)))
