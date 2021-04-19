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

(recognize-bn-an (list 'b 'b 'a 'a))