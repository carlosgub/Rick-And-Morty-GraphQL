package com.carlosgub.rmgraphql.ui.feature.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosgub.rmgraphql.core.DispatcherProvider
import com.carlosgub.rmgraphql.core.sealed.GenericState
import com.carlosgub.rmgraphql.model.usecase.GetCharactersUseCase
import com.carlosgub.rmgraphql.ui.model.CharacterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<GenericState<List<CharacterModel>>>(GenericState.Loading)
    val uiState: StateFlow<GenericState<List<CharacterModel>>> = _uiState
    var list = mutableStateListOf<CharacterModel>()

    init {
        getCharacters()
    }

    private fun getCharacters() {
        viewModelScope.launch(dispatcherProvider.main) {
            getCharactersUseCase()
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiState.value = GenericState.Error(it.message.orEmpty())
                }
                .collect {
                    list.addAll(it)
                    _uiState.value = GenericState.Success(list)
                }
        }
    }

    fun editDetailVisibility(itemClicked: CharacterModel) {
        val index = list.indexOf(itemClicked)
        list[index] = list[index].copy(showDetail = !list[index].showDetail)
        _uiState.value = GenericState.Success(list)
    }
}
