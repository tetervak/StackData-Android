package ca.tetervak.stackdata.ui.stack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.tetervak.stackdata.data.repository.DefaultStackItemRepository
import ca.tetervak.stackdata.data.repository.StackItemRepository
import ca.tetervak.stackdata.data.source.DefaultStackDataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StackViewModel: ViewModel() {

    private val repository: StackItemRepository =
        DefaultStackItemRepository(DefaultStackDataSource())

    val stackUiState: StateFlow<StackUiState> =
        repository.getStackItems().map { StackUiState(items = it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = StackUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun pop() = viewModelScope.launch {
        repository.pop()
    }

    fun push(value: String) = viewModelScope.launch {
        repository.push(value)
    }
}