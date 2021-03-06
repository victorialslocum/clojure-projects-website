;; Create a b^n a^n string
(defn sequence-to-power [x n]
  (if (= n 0)
    '()
    (concat x (sequence-to-power x (- n 1)))))

(defn generate-bn-an [k]
  (concat (sequence-to-power '(b) k) (sequence-to-power '(a) k)))

;; determine if a string is b^n a^n 
(defn remove-last-element [l]
  (reverse (rest (reverse l))))

(defn recognize-bn-an [str]
  (if (empty? str)
    true
    (if (odd? (count str))
      false
      (if (= (count str) 2)
        (if (= '(b a) str)
          true
          false)
        (if (and (= 'b (first str)) (= 'a (first (reverse str))))
          (recognize-bn-an (rest (remove-last-element str)))
          false)))))

(println (generate-bn-an 2))
(println (recognize-bn-an (list 'b 'b 'a 'a)))
