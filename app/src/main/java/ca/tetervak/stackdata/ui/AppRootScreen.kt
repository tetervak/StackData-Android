package ca.tetervak.stackdata.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import ca.tetervak.stackdata.ui.common.StackDataAbout
import ca.tetervak.stackdata.ui.stack.StackScreen

@Composable
fun AppRootScreen(){

    var showAboutDialog: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    StackScreen(onHelpButtonClick = { showAboutDialog = true },)

    if (showAboutDialog) {
        StackDataAbout(onDismissRequest = { showAboutDialog = false })
    }
}