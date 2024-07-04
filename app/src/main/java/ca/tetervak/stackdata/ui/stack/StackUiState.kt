package ca.tetervak.stackdata.ui.stack

import ca.tetervak.stackdata.domain.StackItem

data class StackUiState(val items: List<StackItem> = emptyList())