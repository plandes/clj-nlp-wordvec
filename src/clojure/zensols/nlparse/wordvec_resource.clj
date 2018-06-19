(ns zensols.nlparse.wordvec-resource
  (:require [clojure.tools.logging :as log]
            [zensols.nlparse.resource :as prs]
            [zensols.actioncli.resource :refer (resource-path) :as res]))

(defn initialize
  []
  (log/debug "initializing")
  (prs/initialize)
  (res/register-resource :glove-model-location
                         :pre-path :model :system-file "glove")
  (res/register-resource :glove-model
                         :pre-path :glove-model-location
                         :system-file "glove.6B.50d.txt"))
