;; This computes the BOW probabilities of 

(load-file "uniform-bow-prob.clj")
(in-ns 'hi.test)

(defn sample-BOW-sentence [len vocabulary probabilities]
  (if (= len 0)
    '()
    (cons (sample-categorical vocabulary probabilities)
          (sample-BOW-sentence (- len 1) vocabulary probabilities))))

;; Problem 7
(def moby-word-probabilities (normalize moby-word-frequencies))

(println (sample-BOW-sentence 3 moby-vocab moby-word-probabilities))

;; (every to involuntarily)
;; (then me little)
;; (and men knocking)
;; (but for it)
;; (me toward soul)
