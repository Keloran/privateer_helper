package dev.keloran.privateer.ui.auction

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuctionViewModelFactory(private val applicationContext: Context) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(AuctionViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return AuctionViewModel(applicationContext) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
