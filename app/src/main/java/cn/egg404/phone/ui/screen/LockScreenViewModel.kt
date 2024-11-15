package cn.egg404.phone.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LockScreenViewModel @Inject constructor() : ViewModel() {
    var viewStates by mutableStateOf(LockScreenViewStates())
        private set

    fun dispatch(lockScreenAction: LockScreenAction) {
        when (lockScreenAction) {
            is LockScreenAction.UpdatePassword -> {
                updatePassword(lockScreenAction.value)
            }

            is LockScreenAction.ClearPassword -> {
                clearPassword()
            }

            is LockScreenAction.ClearAllPassword -> {
                clearAllPassword()
            }
        }
    }

    private fun updatePassword(password: String) {
        if (viewStates.password.length < 4) {
            viewStates = viewStates.copy(password = viewStates.password + password)
        }
    }

    private fun clearPassword() {
        if (viewStates.password.isNotEmpty()) {
            viewStates = viewStates.copy(
                password = viewStates.password.substring(
                    0,
                    viewStates.password.length - 1
                )
            )
        }

    }

    private fun clearAllPassword() {
        viewStates = viewStates.copy(password = "")
    }
}

sealed class LockScreenAction {
    data class UpdatePassword(val value: String) : LockScreenAction()
    object ClearPassword : LockScreenAction()
    object ClearAllPassword : LockScreenAction()
}

data class LockScreenViewStates(val password: String = "")