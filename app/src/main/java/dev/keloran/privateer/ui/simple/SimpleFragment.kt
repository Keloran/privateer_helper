package dev.keloran.privateer.ui.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.keloran.privateer.R
import dev.keloran.privateer.databinding.FragmentSimpleBinding
import dev.keloran.privateer.databinding.ItemSimpleBinding
import dev.keloran.privateer.logic.Logic

class SimpleFragment : Fragment() {
  private var _binding: FragmentSimpleBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val simpleViewModel = ViewModelProvider(this).get(SimpleViewModel::class.java)
    _binding = FragmentSimpleBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val inputBox1 = binding.playerCannons
    val inputBox2 = binding.enemyHull
    val submitButton = binding.fight
    val resetButton = binding.reset
    val resultTextView = binding.resultTextView

    submitButton?.setOnClickListener {
      val playerCannons = inputBox1?.text.toString().toIntOrNull() ?: 0
      var enemyHull = inputBox2?.text.toString().toIntOrNull() ?: 1
      enemyHull = maxOf(1, enemyHull)

      val result = Logic.SimpleCombat(playerCannons, enemyHull)
      resultTextView.text = result
    }

    resetButton?.setOnClickListener {
      inputBox1?.setText("0")
      inputBox2?.setText("1")
      resultTextView.text = ""
    }
    return root
}

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
