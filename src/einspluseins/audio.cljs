(ns einspluseins.audio)


(defn load-audio []
  {:success (js/Audio. "media/applause7.wav")
   :failure (js/Audio. "media/Banana_Peel_Slip_Zip-SoundBible.com-803276918.wav")})

(defn play-applause
  [{:keys [success]}]
  (set! (. success -currentTime) "0")
  (.play success))

(defn play-try-again
  [{:keys [failure]}]
  (.play failure))

