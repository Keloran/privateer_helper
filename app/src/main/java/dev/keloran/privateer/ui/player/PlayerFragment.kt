package dev.keloran.privateer.ui.player

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.keloran.privateer.databinding.FragmentPlayerBinding
import dev.keloran.privateer.logic.Logic
import dev.keloran.privateer.logic.ShipDetails
import java.util.Locale

class PlayerFragment : Fragment(), TextToSpeech.OnInitListener  {
  private var _binding: FragmentPlayerBinding? = null
  private val binding get() = _binding!!
  private lateinit var textToSpeech: TextToSpeech

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      textToSpeech.language = Locale.US
    }
  }

  private fun speakText(text: String) {
    if (::textToSpeech.isInitialized) {
      textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

    _binding = FragmentPlayerBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val submitButton: Button = binding.submit
    val resetButton: Button = binding.reset
    val resultTextView: TextView = binding.resultText

    submitButton.setOnClickListener {
      val shipA = ShipDetails(sails = binding.aggressorSails.text.toString().toIntOrNull() ?: 0, crew = binding.aggressorCrew.text.toString().toIntOrNull() ?: 0)
      val shipD = ShipDetails(sails = binding.defenderSails.text.toString().toIntOrNull() ?: 0, crew = binding.defenderCrew.text.toString().toIntOrNull() ?: 0)

      val result = Logic.AdvancedCombat(aggressor = shipA, defender = shipD)
      val spokenResult = when {
        result.impossible.lose -> "Defender will always lose"
        result.impossible.win -> "Aggressor will always lose"
        else -> if (!result.impossible.lose && !result.impossible.win) {
          "Aggressor needs to roll ${result.minRolls.aggressor}, Defender needs to roll ${result.minRolls.defender}"
        } else ""
      }
      resultTextView.text = spokenResult
      speakText(spokenResult)
    }

    resetButton.setOnClickListener {
      binding.aggressorSails.text.clear()
      binding.aggressorCrew.text.clear()
      binding.defenderSails.text.clear()
      binding.defenderCrew.text.clear()
      resultTextView.text = ""
    }
    return root
  }

  override fun onDestroyView() {
    if (::textToSpeech.isInitialized) {
      textToSpeech.stop()
      textToSpeech.shutdown()
    }

    super.onDestroyView()
    _binding = null
  }
}
