package ca.tetervak.stackdata.domain

data class StackItem(
    val count: Int, // count from the bottom of the stack
    val value: String // content of the stack item
)
