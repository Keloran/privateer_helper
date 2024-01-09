package dev.keloran.privateer.ui.complex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.keloran.privateer.databinding.FragmentComplexBinding
import dev.keloran.privateer.logic.Logic
import dev.keloran.privateer.logic.ShipDetails

class ComplexFragment : Fragment() {
  private var _binding: FragmentComplexBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val complexViewModel = ViewModelProvider(this).get(ComplexViewModel::class.java)

    _binding = FragmentComplexBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val submitButton: Button = binding.submit
    val resetButton: Button = binding.reset
    val resultTextView: TextView = binding.resultText

    submitButton.setOnClickListener {
      val shipA = ShipDetails(sails = binding.aggressorSails.text.toString().toIntOrNull() ?: 0, crew = binding.aggressorCrew.text.toString().toIntOrNull() ?: 0)
      val shipD = ShipDetails(sails = binding.defenderSails.text.toString().toIntOrNull() ?: 0, crew = binding.defenderCrew.text.toString().toIntOrNull() ?: 0)

      val result = Logic.AdvancedCombat(aggressor = shipA, defender = shipD)
      val spokenResult = when {
        result.impossible?.win == true -> "You will always lose"
        result.impossible?.lose == true -> "Merchant will always lose"
        result.other != null -> {
          when {
            result.other.aggressor == true -> "You needs to just roll, Merchant needs ${result.minRolls?.defender}"
            result.other.defender == true -> "Merchant needs to just roll, You need ${result.minRolls?.aggressor}"
            else -> "Special condition not recognized"
          }
        }
        else -> {
          val aggressorRoll = result.minRolls?.aggressor?.let { "You needs to roll $it" } ?: ""
          val defenderRoll = result.minRolls?.defender?.let { "Merchant needs to roll $it" } ?: ""
          "$defenderRoll, $aggressorRoll".trim().replaceFirst(",", "")
        }
      }
      resultTextView.text = spokenResult
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
    super.onDestroyView()
    _binding = null
  }
}
