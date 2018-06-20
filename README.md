# Word Vector Feature Creation

[![Travis CI Build Status][travis-badge]][travis-link]

  [travis-link]: https://travis-ci.org/plandes/clj-nlp-parse
  [travis-badge]: https://travis-ci.org/plandes/clj-nlp-parse.svg?branch=master

This library creates word vector features for natural language processing
projects.  It does this using cosine similarity using across the most common
words found using the [word count calculation] function.

While this library is meant to work with the [NLP parse library] and [machine
learning library] much like the [general NLP feature creation] library, it
doesn't need to.  However, it has a dependency on the [NLP parse library].



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

See the [unit test case](test/zensols/nlparse/wordvec_test.clj).


## Building

To build from source, do the folling:

- Install [Leiningen](http://leiningen.org) (this is just a script)
- Install [GNU make](https://www.gnu.org/software/make/)
- Install [Git](https://git-scm.com)
- Download the source: `git clone --recurse-submodules https://github.com/plandes/clj-nlp-wordvec && cd clj-nlp-wordvec`
- Build the software: `make jar`
- Build the distribution binaries: `make dist`

Note that you can also build a single jar file with all the dependencies with: `make uber`


## Citation

If you use this software in your research, please cite with the following
BibTeX:

```jflex
@misc{plandes-clj-nlp-wordvec,
  author = {Paul Landes},
  title = {Word Vector Feature Creation},
  year = {2018},
  publisher = {GitHub},
  journal = {GitHub repository},
  howpublished = {\url{https://github.com/plandes/clj-nlp-wordvec}}
}
```


## References

See the [general NLP feature creation]library for additional references.

```jflex
@misc{deeplearning4j,
  author = {Eclipse Deeplearning4j Development Team},
  title = {Deeplearning4j: Open-source distributed deep learning for the JVM},
  year = {2018},
  publisher = {Apache Software Foundation License 2.0},
  journal = {GitHub repository},
  howpublished = {\url{https://deeplearning4j.org/}}
}
```


## Changelog

An extensive changelog is available [here](CHANGELOG.md).


## License

Copyright 2018 Paul Landes

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


<!-- links -->
[word count calculation](https://plandes.github.io/clj-nlp-parse/codox/zensols.nlparse.feature.word-count.html#var-calculate-feature-stats)
[NLP parse library]: https://github.com/plandes/clj-nlp-parse
[machine learning library]: https://github.com/plandes/clj-ml-model
[general NLP feature creation]: https://github.com/plandes/clj-nlp-feature
