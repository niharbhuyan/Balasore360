package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Text-to-Speech audio speech engine for Baleswari Odia Phrases, Fakir Mohan Literature,
 * and Heritage Narratives with automatic locale detection and fallback.
 */
class AudioPhraseHelper private constructor(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentlyPlayingId = MutableStateFlow<String?>(null)
    val currentlyPlayingId: StateFlow<String?> = _currentlyPlayingId.asStateFlow()

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("AudioPhraseHelper", "Error initializing TextToSpeech: ${e.message}")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            // Attempt to set Odia or Indian English
            val odiaLocale = Locale("or", "IN")
            val available = tts?.isLanguageAvailable(odiaLocale) ?: TextToSpeech.LANG_NOT_SUPPORTED
            if (available >= TextToSpeech.LANG_AVAILABLE) {
                tts?.language = odiaLocale
            } else {
                tts?.language = Locale("en", "IN")
            }
            tts?.setPitch(1.05f)
            tts?.setSpeechRate(0.95f)
        } else {
            isInitialized = false
            Log.w("AudioPhraseHelper", "TextToSpeech init returned code: $status")
        }
    }

    fun speak(id: String, text: String) {
        if (text.isBlank()) return
        _currentlyPlayingId.value = id
        _isPlaying.value = true

        if (isInitialized && tts != null) {
            try {
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, id)
            } catch (e: Exception) {
                Log.e("AudioPhraseHelper", "TTS speak exception: ${e.message}")
            }
        }
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (_: Exception) {}
        _isPlaying.value = false
        _currentlyPlayingId.value = null
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
        _isPlaying.value = false
        _currentlyPlayingId.value = null
    }

    companion object {
        @Volatile
        private var instance: AudioPhraseHelper? = null

        fun getInstance(context: Context): AudioPhraseHelper {
            return instance ?: synchronized(this) {
                instance ?: AudioPhraseHelper(context).also { instance = it }
            }
        }
    }
}
