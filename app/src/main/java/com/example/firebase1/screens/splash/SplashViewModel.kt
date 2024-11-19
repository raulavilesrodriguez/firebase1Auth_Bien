package com.example.firebase1.screens.splash

import androidx.compose.runtime.mutableStateOf
import com.example.firebase1.SETTINGS_SCREEN
import com.example.firebase1.SPLASH_SCREEN
import com.example.firebase1.TASKS_SCREEN
import com.example.firebase1.model.service.AccountService
import com.example.firebase1.model.service.ConfigurationService
import com.example.firebase1.model.service.LogService
import com.example.firebase1.screens.FireViewModel
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService
) : FireViewModel(logService) {
    val showError = mutableStateOf(false)

    init {
        launchCatching { configurationService.fetchConfiguration() }
    }

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {

        showError.value = false
        if (accountService.hasUser) openAndPopUp(SETTINGS_SCREEN, SPLASH_SCREEN)
        else createAnonymousAccount(openAndPopUp)
    }

    private fun createAnonymousAccount(openAndPopUp: (String, String) -> Unit) {
        launchCatching(snackbar = false) {
            try {
                accountService.createAnonymousAccount()
            } catch (ex: FirebaseAuthException) {
                showError.value = true
                throw ex
            }
            openAndPopUp(SETTINGS_SCREEN, SPLASH_SCREEN)
        }
    }
}