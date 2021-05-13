;; This code takes a vocabulary and computes a uniform probability 
;; distribution. Not very exciting, since all the probabilities 
;; end up equalling 1/2685619 because the function only relies on 
;; the uniform distribution, which has the same counts, so will 
;; be the same for every three letter sentence. 


(def moby-word-tokens '(CALL me Ishmael . Some years ago never mind
                             how long precisely having little or no money in my purse , and
                             nothing particular to interest me on shore , I thought I would
                             sail about a little and see the part of the world . It is
                             a way I have of driving off the spleen , and regulating the
                             circulation . Whenever I find myself growing grim about the mouth
                             whenever it is a damp , drizzly November in my soul whenever I
                             find myself involuntarily pausing before coffin warehouses , and
                             bringing up the rear of every funeral I meet and especially
                             whenever my hypos get such an upper hand of me , that it requires
                             a strong moral principle to prevent me from deliberately stepping
                             into the street , and methodically knocking people's hats off
                             then , I account it high time to get to sea as soon as I can .
                             This is my substitute for pistol and ball . With a philosophical
                             flourish Cato throws himself upon his sword I quietly take to the
                             ship . There is nothing surprising in this . If they but knew it
                             , almost all men in their degree , some time or other , cherish
                             very nearly the same feelings toward the ocean with me .))

(defn member-of-list? [w l]
  1
  (if (empty? l)
    false
    (if (= w (first l))
      true
      (member-of-list? w (rest l)))))

(defn get-vocabulary [word-tokens vocab]
  (if (empty? word-tokens)
    vocab
    (if (member-of-list? (first word-tokens) vocab)
      (get-vocabulary (rest word-tokens) vocab)
      (get-vocabulary (rest word-tokens) (cons (first word-tokens) vocab)))))

(def moby-vocab (reverse (get-vocabulary moby-word-tokens '())))

(defn get-count-of-word [w word-tokens count]
  (if (empty? word-tokens)
    count
    (if (= w (first word-tokens))
      (get-count-of-word w (rest word-tokens) (+ 1 count))
      (get-count-of-word w (rest word-tokens) count))))

(defn get-word-counts [vocab word-tokens]
  (let [count-word (fn [w]
                     (get-count-of-word w word-tokens 0))]
    (map count-word vocab)))

(def moby-word-frequencies (get-word-counts moby-vocab moby-word-tokens))

(defn flip [p]
  (if (< (rand 1) p)
    true
    false))

(defn normalize [params]
  (let [sum (apply + params)]
    (map (fn [x] (/ x sum)) params)))

(defn sample-categorical [outcomes params]
  (if (flip (first params))
    (first outcomes)
    (sample-categorical (rest outcomes)
                        (normalize (rest params)))))

(defn create-uniform-distribution [outcomes]
  (let [num-outcomes (count outcomes)]
    (map (fn [x] (/ 1 num-outcomes))
         outcomes)))

(defn sample-uniform-BOW-sentence [n vocab]
  (let [probabilities (create-uniform-distribution vocab)]
    (if (= n 0)
      '()
      (cons (sample-categorical vocab probabilities)
            (sample-uniform-BOW-sentence (dec n) vocab)))))

(defn compute-uniform-BOW-prob [vocab sentence]
  (if (empty? sentence)
    0
    (Math/pow (/ 1 (count vocab)) (count sentence))))

(def sample-sentence (sample-uniform-BOW-sentence 3 moby-vocab))

(println sample-sentence)
(compute-uniform-BOW-prob moby-vocab sample-sentence)