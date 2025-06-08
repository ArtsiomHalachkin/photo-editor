package cz.mendelu.photoeditor.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.photoeditor.datastore.IDataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenActivityViewModel  @Inject constructor(
   // private val dataStoreRepository: IDataStoreRepository
) : ViewModel() {

    private val _splashScreenState = MutableStateFlow(SplashScreenUiState())
    val splashScreenState: StateFlow<SplashScreenUiState> = _splashScreenState

    init {
        checkAppState()
    }

    private fun checkAppState(){
        viewModelScope.launch {
            if (true){
                _splashScreenState.value = SplashScreenUiState(runForAFirstTime = true)
            }
        }
    }

}