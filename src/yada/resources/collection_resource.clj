;; Copyright © 2015, JUXT LTD.

(ns yada.resources.collection-resource
  (:require
   [clojure.tools.logging :refer :all]
   [clj-time.core :refer (now)]
   [clj-time.coerce :refer (to-date)]
   [yada.mime :refer (media-type)]
   [yada.resource :refer (Resource ResourceRepresentations ResourceConstructor platform-charsets)]
   [cheshire.core :as json]
   [json-html.core :as jh])
  (:import [clojure.lang APersistentMap]))

(defrecord MapResource [m last-modified]
  Resource
  (exists? [_ ctx] true)
  (last-modified [_ ctx] last-modified)
  (parameters [_] nil)
  (request [_ method ctx] (case method :get m))

  ResourceRepresentations
  (representations [_]
    [{:method #{:get :head}
      :content-type #{"application/edn" "application/json;q=0.9" "text/html;q=0.8"}
      :charset platform-charsets}]))

(extend-protocol ResourceConstructor
  APersistentMap
  (make-resource [m]
    (->MapResource m (to-date (now)))))
