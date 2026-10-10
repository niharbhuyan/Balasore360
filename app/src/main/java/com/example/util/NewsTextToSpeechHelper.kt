package com.example.util

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.data.model.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Text-to-Speech audio engine for Balasore regional news articles.
 * Enables users to listen to news stories in English (Indian/Standard)
 * or Odia (ଓଡ଼ିଆ) with playback controls, speed adjustments, and
 * utterance progress tracking.
 */
class NewsTextToSpeechHelper private constructor(context: Context) :
    TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _currentArticleId = MutableStateFlow<String?>(null)
    val currentArticleId: StateFlow<String?> = _currentArticleId.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _speechSpeed = MutableStateFlow(1.0f)
    val speechSpeed: StateFlow<Float> = _speechSpeed.asStateFlow()

    private val _isOdiaVoiceAvailable = MutableStateFlow(false)
    val isOdiaVoiceAvailable: StateFlow<Boolean> = _isOdiaVoiceAvailable.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    // Cached text for pause/resume support
    private var lastSpokenText: String = ""
    private var lastSpokenTitle: String = ""

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("NewsTTSHelper", "Failed to initialize TextToSpeech engine: ${e.message}")
            _statusMessage.value = "TTS engine not available on device"
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            setupProgressListener()
            checkOdiaAvailability()
            applyLanguageSettings(_currentLanguage.value)
            tts?.setPitch(1.0f)
            tts?.setSpeechRate(_speechSpeed.value)
            _statusMessage.value = "Audio engine ready"
            Log.i("NewsTTSHelper", "TextToSpeech engine initialized successfully")
        } else {
            isInitialized = false
            _statusMessage.value = "TextToSpeech initialization error ($status)"
            Log.w("NewsTTSHelper", "TextToSpeech init failed with status: $status")
        }
    }

    private fun checkOdiaAvailability() {
        val odiaLocale = Locale.Builder().setLanguage("or").setRegion("IN").build()
        val available = tts?.isLanguageAvailable(odiaLocale) ?: TextToSpeech.LANG_NOT_SUPPORTED
        _isOdiaVoiceAvailable.value = (available >= TextToSpeech.LANG_AVAILABLE)
        Log.i("NewsTTSHelper", "Odia TTS voice availability: ${_isOdiaVoiceAvailable.value} (code=$available)")
    }

    private fun setupProgressListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isSpeaking.value = true
                _isPaused.value = false
                _statusMessage.value = "Reading article aloud..."
            }

            override fun onDone(utteranceId: String?) {
                _isSpeaking.value = false
                _isPaused.value = false
                _statusMessage.value = "Finished reading"
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _isSpeaking.value = false
                _isPaused.value = false
                _statusMessage.value = "Audio playback completed"
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                _isSpeaking.value = false
                _isPaused.value = false
                _statusMessage.value = "Playback stopped (code: $errorCode)"
            }
        })
    }

    private fun applyLanguageSettings(language: AppLanguage) {
        if (!isInitialized || tts == null) return

        if (language == AppLanguage.ODIA) {
            val odiaLocale = Locale.Builder().setLanguage("or").setRegion("IN").build()
            val result = tts?.setLanguage(odiaLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to Indian English voice for Odia transliterated text
                val inLocale = Locale.Builder().setLanguage("en").setRegion("IN").build()
                tts?.setLanguage(inLocale)
                _statusMessage.value = "Odia voice pack recommended from Google Speech Services"
            } else {
                _statusMessage.value = "Odia voice active"
            }
        } else {
            val enLocale = Locale.Builder().setLanguage("en").setRegion("IN").build()
            val result = tts?.setLanguage(enLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.US)
            }
            _statusMessage.value = "English voice active"
        }
        tts?.setSpeechRate(_speechSpeed.value)
    }

    /**
     * Speaks the news article in the requested language (English or Odia).
     * Automatically constructs the narrative from title and body content.
     */
    fun speakArticle(
        articleId: String,
        title: String,
        content: String,
        language: AppLanguage = _currentLanguage.value
    ) {
        if (!isInitialized || tts == null) {
            _statusMessage.value = "Audio speech engine is starting..."
            return
        }

        // Clean up previous playback if speaking a different article
        if (_isSpeaking.value && _currentArticleId.value != articleId) {
            stop()
        }

        _currentArticleId.value = articleId
        _currentLanguage.value = language
        lastSpokenTitle = title
        lastSpokenText = content

        applyLanguageSettings(language)

        val textToSpeak = buildString {
            if (title.isNotBlank()) {
                append(title.trim())
                append(". ")
            }
            if (content.isNotBlank()) {
                append(content.trim())
            }
        }

        if (textToSpeak.isBlank()) {
            _statusMessage.value = "No article text available to read"
            return
        }

        val utteranceId = "article_${articleId}_${language.name}"
        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }

        _isSpeaking.value = true
        _isPaused.value = false

        try {
            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        } catch (e: Exception) {
            Log.e("NewsTTSHelper", "Error calling tts.speak: ${e.message}")
            _isSpeaking.value = false
            _statusMessage.value = "Audio error: ${e.localizedMessage}"
        }
    }

    /**
     * Pauses or stops the current playback.
     */
    fun pause() {
        if (_isSpeaking.value) {
            try {
                tts?.stop()
            } catch (_: Exception) {}
            _isSpeaking.value = false
            _isPaused.value = true
            _statusMessage.value = "Playback paused"
        }
    }

    /**
     * Resumes the paused article playback.
     */
    fun resume() {
        val articleId = _currentArticleId.value
        if (articleId != null && (lastSpokenTitle.isNotBlank() || lastSpokenText.isNotBlank())) {
            speakArticle(
                articleId = articleId,
                title = lastSpokenTitle,
                content = lastSpokenText,
                language = _currentLanguage.value
            )
        }
    }

    /**
     * Stops the audio playback completely and resets state.
     */
    fun stop() {
        try {
            tts?.stop()
        } catch (_: Exception) {}
        _isSpeaking.value = false
        _isPaused.value = false
        _statusMessage.value = "Playback stopped"
    }

    /**
     * Toggles speech language between English and Odia.
     * If currently playing, automatically restarts speech in the newly selected language.
     */
    fun setLanguage(
        newLanguage: AppLanguage,
        title: String = lastSpokenTitle,
        content: String = lastSpokenText
    ) {
        _currentLanguage.value = newLanguage
        applyLanguageSettings(newLanguage)

        // If currently speaking, re-trigger with the updated language text
        if (_isSpeaking.value && _currentArticleId.value != null) {
            speakArticle(
                articleId = _currentArticleId.value!!,
                title = title,
                content = content,
                language = newLanguage
            )
        }
    }

    /**
     * Sets playback speed: 0.8x (relaxed), 1.0x (normal), 1.25x (brisk).
     */
    fun setSpeechSpeed(speed: Float) {
        _speechSpeed.value = speed
        try {
            tts?.setSpeechRate(speed)
        } catch (_: Exception) {}
    }

    /**
     * Shuts down the engine cleanly on app termination.
     */
    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
        _isSpeaking.value = false
        _isPaused.value = false
        _currentArticleId.value = null
    }

    companion object {
        @Volatile
        private var instance: NewsTextToSpeechHelper? = null

        fun getInstance(context: Context): NewsTextToSpeechHelper {
            return instance ?: synchronized(this) {
                instance ?: NewsTextToSpeechHelper(context).also { instance = it }
            }
        }
    }
}
