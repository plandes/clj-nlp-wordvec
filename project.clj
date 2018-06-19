(defproject com.zensols.nlp/wordvec "0.1.0-SNAPSHOT"
  :description "This creates word vector features for natural language processing projects."
  :url "https://github.com/plandes/clj-nlp-wordvec"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"
            :distribution :repo}
  :plugins [[lein-codox "0.10.3"]
            [lein-javadoc "0.3.0"]
            [org.clojars.cvillecsteele/lein-git-version "1.2.7"]]
  :codox {:metadata {:doc/format :markdown}
          :project {:name "Word Vector Feature Creation"}
          :output-path "target/doc/codox"
          :source-uri "https://github.com/plandes/clj-nlp-wordvec/blob/v{version}/{filepath}#L{line}"}
  :javadoc-opts {:package-names ["com.zensols.nlp.wordvec"]
                 :output-dir "target/doc/apidocs"}
  :git-version {:root-ns "zensols.nlparse.feature"
                :path "src/clojure/zensols/nlparse/feature"
                :version-cmd "git describe --match v*.* --abbrev=4 --dirty=-dirty"}
  :source-paths ["src/clojure"]
  :test-paths ["test" "test-resources"]
  :java-source-paths ["src/java"]
  :javac-options ["-Xlint:unchecked"]
  :jar-exclusions [#".gitignore"]
  :dependencies [[org.clojure/clojure "1.8.0"]

                 ;; logging for core
                 [org.apache.logging.log4j/log4j-core "2.7"]

                 ;; cuda
                 [org.nd4j/nd4j-native "0.9.1"]
                 [org.nd4j/nd4j-native-platform "0.9.1"]
                 ;; [org.nd4j/nd4j-cuda-8.0 "0.9.1"]
                 ;; [org.nd4j/nd4j-cuda-8.0-platform "0.9.1"]

                 ;; word2vec
                 [org.deeplearning4j/deeplearning4j-core "0.9.1"
                  :exclusions [ch.qos.logback/logback-classic
                               com.fasterxml.jackson.core/jackson-core
                               joda-time
                               log4j
                               org.apache.commons/commons-compress
                               org.apache.commons/commons-lang3
                               org.json/json
                               org.slf4j/slf4j-api
                               org.slf4j/slf4j-log4j12
                               org.yaml/snakeyaml]]
                 [org.deeplearning4j/deeplearning4j-nlp "0.9.1"]
                 [org.deeplearning4j/deeplearning4j-parallel-wrapper_2.11 "0.9.1"
                  :exclusions [ch.qos.logback/logback-classic
                               com.fasterxml.jackson.core/jackson-annotations
                               com.fasterxml.jackson.core/jackson-core
                               com.fasterxml.jackson.core/jackson-databind
                               com.fasterxml.jackson.datatype/jackson-datatype-jdk8
                               com.fasterxml.jackson.datatype/jackson-datatype-jsr310
                               com.google.guava/guava
                               log4j
                               org.apache.httpcomponents/httpcore
                               org.eclipse.collections/eclipse-collections
                               org.eclipse.collections/eclipse-collections-api
                               org.eclipse.collections/eclipse-collections-forkjoin
                               org.scala-lang/scala-reflect
                               org.slf4j/slf4j-api
                               org.slf4j/slf4j-log4j12
                               xerces/xercesImpl
                               xml-apis]]

                 ;; manage parsed annotations
                 [com.zensols.nlp/parse "0.1.6"]]
  :pom-plugins [[org.codehaus.mojo/appassembler-maven-plugin "1.6"
                 {:configuration ([:programs
                                   [:program
                                    ([:mainClass "zensols.nlparse.feature.core"]
                                     [:id "someproj"])]]
                                  [:environmentSetupFileName "setupenv"])}]]
  :profiles {:1.9 {:dependencies [[org.clojure/clojure "1.9.0"]]}
             :uberjar {:aot [zensols.nlparse.feature.core]}
             :appassem {:aot :all}
             :snapshot {:git-version {:version-cmd "echo -snapshot"}}
             :dev
             {:exclusions [org.slf4j/slf4j-log4j12
                           log4j/log4j
                           ch.qos.logback/logback-classic]
              :dependencies [[org.apache.logging.log4j/log4j-slf4j-impl "2.7"]
                             [org.apache.logging.log4j/log4j-1.2-api "2.7"]
                             [org.apache.logging.log4j/log4j-jcl "2.7"]
                             [org.apache.logging.log4j/log4j-jul "2.7"]
                             [com.zensols.tools/misc "0.0.5"]]}
             :test {:dependencies [[com.zensols.tools/misc "0.0.5"]]
                    :jvm-opts ["-Dlog4j.configurationFile=test-resources/test-log4j2.xml"
                               "-Xms4g" "-Xmx12g" "-XX:+UseConcMarkSweepGC"]}})
