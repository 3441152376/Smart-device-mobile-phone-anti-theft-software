package cn.egg404.phone.ui.page.select_audio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cn.egg404.phone.data.bean.AudioItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectAudioViewModel @Inject constructor() : ViewModel() {
    var viewStates by mutableStateOf(SelectAudioViewStates())

    init {
        viewStates = viewStates.copy(
            audioL = listOf(
                AudioItemData("默认音频", true),
                AudioItemData("搞笑女配音", false)
            )
        )
    }

}

data class SelectAudioViewStates(var audioL: List<AudioItemData> = emptyList())