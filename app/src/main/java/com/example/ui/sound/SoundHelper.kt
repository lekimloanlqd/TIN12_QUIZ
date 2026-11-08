package com.example.ui.sound

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log

class SoundHelper {
    private var toneGenerator: ToneGenerator? = null
    var isSoundEnabled: Boolean = true

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (e: Exception) {
            Log.w("SoundHelper", "Could not initialize ToneGenerator", e)
        }
    }

    fun playCorrect() {
        if (!isSoundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 180)
        } catch (e: Exception) {
            Log.w("SoundHelper", "Error playing tone", e)
        }
    }

    fun playIncorrect() {
        if (!isSoundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 250)
        } catch (e: Exception) {
            Log.w("SoundHelper", "Error playing tone", e)
        }
    }

    fun playStreak() {
        if (!isSoundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 350)
        } catch (e: Exception) {
            Log.w("SoundHelper", "Error playing tone", e)
        }
    }

    fun playComplete() {
        if (!isSoundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 400)
        } catch (e: Exception) {
            Log.w("SoundHelper", "Error playing tone", e)
        }
    }

    fun release() {
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (e: Exception) {
            Log.w("SoundHelper", "Error releasing ToneGenerator", e)
        }
    }
}
