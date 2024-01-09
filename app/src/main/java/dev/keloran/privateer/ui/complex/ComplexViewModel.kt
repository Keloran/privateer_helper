package dev.keloran.privateer.ui.complex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ComplexViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is complex Fragment"
    }
    val text: LiveData<String> = _text
}
