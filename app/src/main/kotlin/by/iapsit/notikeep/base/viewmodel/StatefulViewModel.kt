package by.iapsit.notikeep.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.iapsit.notikeep.base.intents.UiAction
import by.iapsit.notikeep.base.intents.UiEffect
import by.iapsit.notikeep.base.intents.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class StatefulViewModel<Action : UiAction, State : UiState, Effect : UiEffect> : ViewModel() {

    private val initialState: State by lazy { createInitialState() }

    private val currentState: State
        get() = state.value

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _action: MutableSharedFlow<Action> = MutableSharedFlow()
    val action = _action.asSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeActions()
    }

    private fun subscribeActions() {
        viewModelScope.launch {
            action.collect {
                handleAction(it)
            }
        }
    }

    fun setAction(action: Action) {
        viewModelScope.launch { _action.emit(action) }
    }

    protected fun setState(builder: () -> State) {
        _state.value = builder()
    }

    protected fun setEffect(builder: () -> Effect) {
        viewModelScope.launch { _effect.send(builder()) }
    }

    abstract fun createInitialState(): State

    abstract suspend fun handleAction(action: Action)
}