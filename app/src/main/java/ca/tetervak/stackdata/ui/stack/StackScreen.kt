package ca.tetervak.stackdata.ui.stack

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.tetervak.stackdata.R
import ca.tetervak.stackdata.domain.StackItem
import ca.tetervak.stackdata.ui.AppViewModelProvider
import ca.tetervak.stackdata.ui.common.StackDataTopBar
import ca.tetervak.stackdata.ui.theme.StackDataTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StackScreen(
    modifier: Modifier = Modifier,
    viewModel: StackViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onHelpButtonClick: () -> Unit = {}
){
    val state: State<StackUiState> = viewModel.stackUiState.collectAsState()
    val itemList: List<StackItem> = state.value.items

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            StackDataTopBar(
                onHelpButtonClick = onHelpButtonClick,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ){ innerPadding ->
        StackScreenBody(onPush = { viewModel.push(it) },
            onPop = { viewModel.pop() },
            itemList = itemList,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}


@Composable
fun StackScreenBody(
    onPush: (String) -> Unit,
    onPop: () -> Unit,
    itemList: List<StackItem>,
    modifier: Modifier = Modifier
) {
    var input: String by rememberSaveable { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(all = 32.dp)
    ) {
        StackValueInputOutput(
            value = input,
            onChange = { input = it },
            modifier = Modifier
                .sizeIn(minWidth = 256.dp)
                .padding(top = 24.dp)
        )
        StackButtonRow(onPush = {
            if (input.isNotBlank()) {
                onPush(input.trim())
                input = ""
                focusManager.clearFocus()
            }
        },
            onPop = {
                input = itemList.first().value
                onPop()
            },
            showPopButton = itemList.isNotEmpty(),
            modifier = Modifier
                .width(width = 256.dp)
                .padding(top = 24.dp)
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 24.dp),
            thickness = 2.dp, color = Color.Gray
        )
        if (itemList.isNotEmpty()) {
            StackContent(
                itemList = itemList, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.stack_empty_message),
                    fontSize = 32.sp,
                    color = colorResource(id = R.color.orange_900),
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .border(
                            width = 2.dp, color = colorResource(id = R.color.orange_900)
                        )
                        .padding(all = 16.dp)
                )
            }
        }
        HorizontalDivider(
            thickness = 2.dp,
            color = Color.Gray
        )
    }
}

@Preview
@Composable
fun StackScreenBodyPreview() {
    StackDataTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            val itemList = listOf(
                StackItem(3, "Item C"), StackItem(2, "Item B"), StackItem(1, "Item A")
            )
            StackScreenBody(onPush = {}, onPop = {}, itemList = itemList
            )
        }
    }
}

@Composable
fun StackContent(
    itemList: List<StackItem>, modifier: Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp, horizontal = 8.dp
        ), horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
    ) {
        items(itemList) { stackItem ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(width = 256.dp)
                    .border(width = 2.dp, color = Color.Gray)
                    .padding(all = 16.dp)

            ) {
                Text(
                    text = "${stackItem.count}.", fontSize = 32.sp
                )
                Text(
                    text = stackItem.value,
                    fontSize = 32.sp,
                    color = colorResource(id = R.color.blue_900)
                )
            }
        }
    }
}

@Composable
fun StackButtonRow(
    onPush: () -> Unit, onPop: () -> Unit, showPopButton: Boolean, modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier
    ) {
        StackButton(
            onClick = onPush,
            imageVector = Icons.Default.ArrowDownward,
            stringRes = R.string.push_button_label
        )
        if (showPopButton) {
            StackButton(
                onClick = onPop,
                imageVector = Icons.Default.ArrowUpward,
                stringRes = R.string.pop_button_label
            )
        }
    }
}

@Composable
fun StackButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    @StringRes stringRes: Int,
    modifier: Modifier = Modifier
) {
    Button(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = stringResource(stringRes), fontSize = 20.sp
        )
    }
}

@Composable
fun StackValueInputOutput(
    value: String, onChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    TextField(
        label = { Text(text = stringResource(R.string.input_output_label)) },
        value = value,
        onValueChange = onChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        textStyle = TextStyle.Default.copy(
            fontSize = 32.sp, color = colorResource(id = R.color.blue_900)
        ),
        modifier = modifier.border(width = 2.dp, color = Color.Gray)
    )
}
