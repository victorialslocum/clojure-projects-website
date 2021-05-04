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

(defn sample-BOW-sentence [len vocabulary probabilities]
  (if (= len 0)
    '()
    (cons (sample-categorical vocabulary probabilities)
          (sample-BOW-sentence (- len 1) vocabulary probabilities))))

(def moby-word-probabilities (normalize moby-word-frequencies))

(defn lookup-probability [w outcomes probs]
  (if (empty? outcomes)
    0
    (if (= w (first outcomes))
      (first probs)
      (lookup-probability w (rest outcomes) (rest probs)))))

(defn product [l]
  (apply * l))

(defn compute-BOW-prob [sentence vocabulary probabilities]
  (if (empty? sentence)
    1
    (product (concat (list (lookup-probability (first sentence) vocabulary probabilities))
                     (list (compute-BOW-prob (rest sentence) vocabulary probabilities))))))

(compute-BOW-prob (list 'CALL 'me 'Ishmael) moby-vocab moby-word-probabilities)