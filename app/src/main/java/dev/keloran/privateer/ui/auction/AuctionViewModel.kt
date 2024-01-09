package dev.keloran.privateer.ui.auction

import android.content.Context
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale
import java.util.concurrent.TimeUnit

class AuctionViewModel(private val applicationContext: Context) : ViewModel() {
  private var timeCountInMilliSeconds = 1 * 60000.toLong()
  val tokenMaxTimer = 1 * 5 * 1000.toLong()
  private lateinit var textToSpeech: TextToSpeech

  private enum class TimerStatus {
    STARTED, STOPPED
  }

  private val _currentTime = MutableLiveData<Long>()
  val currentTime: LiveData<Long>
    get() = _currentTime

  private val _currentTimeString = MutableLiveData<String>()
  val currentTimeString: LiveData<String>
    get() = _currentTimeString

  private val _tokenString = MutableLiveData<String>()
  val tokenString: LiveData<String>
    get() = _tokenString

  private var timerStatus = TimerStatus.STOPPED
  private var countDownTimer: CountDownTimer? = null

  init {
    resetTime()
    initateTextToSpeech()
  }

  private fun initateTextToSpeech() {
    textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
      if (status != TextToSpeech.ERROR) {
        textToSpeech.setLanguage(Locale.UK)
      }
    })
  }

  private fun startCountDownTimer() {
    countDownTimer = object: CountDownTimer(timeCountInMilliSeconds, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        _currentTimeString.value = hmsTimeFormatter(millisUntilFinished)
        _currentTime.value = millisUntilFinished

        speakIt(String.format("%2d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)))
      }

      override fun onFinish() {
        timerStatus = TimerStatus.STOPPED
        speakIt("Show Me The MONEY")
      }
    }.start()
  }

  fun startStop() {
    if (timerStatus === TimerStatus.STOPPED) {
      setTimerValues()
      timerStatus = TimerStatus.STARTED
      startCountDownTimer()
    } else {
      timerStatus = TimerStatus.STOPPED
      stopCountDownTimer()
    }
  }

  private fun setTimerValues() {
    val time = 1
    timeCountInMilliSeconds = time * 5 * 1000.toLong()
  }

  private fun stopCountDownTimer() {
    countDownTimer!!.cancel()
  }

  init {
    resetTime()
  }

  private fun hmsTimeFormatter(milliSeconds: Long): String {
    return String.format(
      "%02d",
      TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(
        TimeUnit.MILLISECONDS.toMinutes(
          milliSeconds
        )
      )
    )
  }

  private fun resetTime() {
    _tokenString.value = (5).toString()
  }

  fun refreshToken() {
    timerStatus = TimerStatus.STOPPED
    stopCountDownTimer()
    resetTime()
  }

  fun speakIt(text: String) {
    if (text === "0") {
      return
    }

    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
  }

  override fun onCleared() {
    if (::textToSpeech.isInitialized) {
      textToSpeech.stop()
      textToSpeech.shutdown()
    }
    super.onCleared()
  }
}
