package dev.keloran.privateer.ui.auction

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.keloran.privateer.databinding.FragmentAuctionBinding
import dev.keloran.privateer.databinding.FragmentComplexBinding

class AuctionFragment : Fragment() {
  internal var view: View? = null
  private var _binding: FragmentAuctionBinding? = null
  private val binding get() = _binding!!
  private lateinit var viewModel: AuctionViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentAuctionBinding.inflate(inflater, container, false)
    view = binding.root
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    this.view = view

    val factory = AuctionViewModelFactory(requireActivity().applicationContext)
    viewModel = ViewModelProvider(this, factory)[AuctionViewModel::class.java]

    binding.btnRefresh.setOnClickListener {
      viewModel.refreshToken()
    }
    viewModel.currentTimeString.observe(viewLifecycleOwner) {
      binding.textViewTime.text = it
    }
    viewModel.currentTime.observe(viewLifecycleOwner) {
      binding.progressBarCircle.progress = (it / 1000).toInt()
    }
    viewModel.tokenString.observe(viewLifecycleOwner) {
      viewModel.startStop()
    }
    setProgressBarValues()
  }

  private fun setProgressBarValues() {
    binding.progressBarCircle.max = (viewModel.tokenMaxTimer / 1000).toInt()
    binding.progressBarCircle.progress = (viewModel.tokenMaxTimer / 1000).toInt()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
