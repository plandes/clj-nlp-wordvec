# Word Vector Feature Creation

This library creates word vector features for natural language processing
projects.  It does this using cosine similarity using across the most common
words found using the [word count calculation] function.


## Obtaining

In your `project.clj` file, add:

[![Clojars Project](https://clojars.org/com.zensols.nlp/wordvec/latest-version.svg)](https://clojars.org/com.zensols.nlp/wordvec/)


## Documentation

API [documentation](https://plandes.github.io/clj-nlp-wordvec/codox/index.html).


## Usage


Use the `similarity-features` to create the features used in
the [model](https://github.com/plandes/clj-ml-model#create-features).  Again,
this piggybacks on the [word count calculation], which has to be calculated
on [two pass cross validation](https://github.com/plandes/clj-ml-model#one-pass-traintest).
For example:

```clojure
(:require [zensols.nlparse.parse :as p]
          [zensols.nlparse.feature.word-count :as wc]
          [zensols.nlparse.feature.word-similarity :as ws])

(defn create-features
  ([panon]
   (create-features panon nil))
  ([panon context]
   (let [{:keys [word-count-stats]} context
         tokens (p/tokens panon)]
     (binding [wc/*word-count-config* wc-config]
       (merge (if word-count-stats
                (wc/label-count-score-features panon word-count-stats))
              (if word-count-stats
                (ws/similarity-features tokens word-count-stats)))))))
```


## Building

To build from source, do the folling:

- Install [Leiningen](http://leiningen.org) (this is just a script)
- Install [GNU make](https://www.gnu.org/software/make/)
- Install [Git](https://git-scm.com)
- Download the source: `git clone --recurse-submodules https://github.com/plandes/clj-nlp-wordvec && cd clj-nlp-wordvec`
- Build the software: `make jar`
- Build the distribution binaries: `make dist`

Note that you can also build a single jar file with all the dependencies with: `make uber`


## Changelog

An extensive changelog is available [here](CHANGELOG.md).


## License

Copyright © 2018 Paul Landes

Apache License version 2.0

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


<!-- links -->
[word count calculation](https://plandes.github.io/clj-nlp-parse/codox/zensols.nlparse.feature.word-count.html#var-calculate-feature-stats)
