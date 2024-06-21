package fr.c1.chatbot.composable

import fr.c1.chatbot.MainActivity
import fr.c1.chatbot.model.TypeAction
import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.ui.theme.colorSchemeExtension
import fr.c1.chatbot.utils.UnitLaunchedEffect
import fr.c1.chatbot.utils.focusRequesterIfNotNull
import fr.c1.chatbot.utils.rememberMutableStateOf
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "SearchBar"

@ExperimentalMaterial3Api
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = SearchBarDefaults.inputFieldShape,
    colors: SearchBarColors = SearchBarDefaults.colors(),
    tonalElevation: Dp = SearchBarDefaults.TonalElevation,
    shadowElevation: Dp = SearchBarDefaults.ShadowElevation,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardType: KeyboardType = KeyboardType.Text,
    focus: FocusRequester? = null
) {
    Surface(
        shape = shape,
        color = colors.containerColor,
        contentColor = Color.Red,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        modifier = modifier,
    ) {
        val inputFieldColors = colors.inputFieldColors

        val textColor = LocalTextStyle.current.color.takeOrElse {
            val state = rememberUpdatedState(
                when {
                    !enabled -> inputFieldColors.disabledTextColor
                    interactionSource.collectIsFocusedAsState().value -> inputFieldColors.focusedTextColor
                    else -> inputFieldColors.unfocusedTextColor
                }
            )

            state.value
        }

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxSize()
                .focusRequesterIfNotNull(focus),
            enabled = enabled,
            singleLine = true,
            textStyle = LocalTextStyle.current.merge(
                TextStyle(
                    color = textColor,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = keyboardType
            ),
            keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
            interactionSource = interactionSource,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = query,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon?.let { leading ->
                        {
                            Box(Modifier.offset(x = 4.dp)) { leading() }
                        }
                    },
                    trailingIcon = trailingIcon?.let { trailing ->
                        {
                            Box(Modifier.offset(x = (-4).dp)) { trailing() }
                        }
                    },
                    shape = SearchBarDefaults.inputFieldShape,
                    colors = inputFieldColors,
                    contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(),
                    container = {},
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    enabled: Boolean = true,
    action: TypeAction = TypeAction.None,
    proposals: Collection<String>? = null,
    onSearch: (String) -> Unit
) {
    var query by remember(placeholder) { mutableStateOf("") }

    val keyboardCtrl = LocalSoftwareKeyboardController.current!!
    val focusMgr = LocalFocusManager.current

    var mod = modifier
        .height(75.dp)
        .fillMaxWidth()
        .padding(horizontal = 10.dp)

    var showDp by rememberMutableStateOf(key1 = action, action == TypeAction.DateInput)

    mod = when (action) {
        TypeAction.DateInput -> mod.clickable { showDp = true }
        else -> mod
    }

    val dpState = rememberDatePickerState()
    var currentMillis by remember { mutableLongStateOf(-1L) }

    if (showDp)
        MyDatePicker(
            state = dpState,
            onDismiss = {
                showDp = false
                dpState.selectedDateMillis = if (currentMillis > 0) currentMillis else null
            },
            onConfirm = {
                showDp = false
                currentMillis = dpState.selectedDateMillis!!
                query = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(Date(currentMillis))
            }
        )

    fun search(str: String) {
        keyboardCtrl.hide()
        focusMgr.clearFocus()
        onSearch(str)
    }

    val focus =
        if (enabled && action != TypeAction.DateInput) remember { FocusRequester() }
        else null

    if (proposals != null) {
        var props by rememberMutableStateOf(proposals)
        props = proposals.filter { it.startsWith(query, true) }.take(50)
        DropDown(
            query = query,
            placeholder = placeholder,
            proposals = props,
            onValueChanged = {
                query = it
            },
            onProposalSelected = { query = it },
            onSearch = ::search,
            modifier = mod,
            focus = focus
        )

        if (focus != null)
            UnitLaunchedEffect { focus.requestFocus() }

        return
    }

    var ifColors = TextFieldDefaults.colors(
        focusedPlaceholderColor = MaterialTheme.colorSchemeExtension.placeHolder,
        unfocusedPlaceholderColor = MaterialTheme.colorSchemeExtension.placeHolder
    )

    if (action == TypeAction.DateInput)
        ifColors = ifColors.copy(
            disabledTextColor = MaterialTheme.colorScheme.onBackground,
            disabledTrailingIconColor =
            if (currentMillis > 0) MaterialTheme.colorScheme.onBackground
            else Color.Unspecified
        )

    SearchBar(
        modifier = mod,
        query = query,
        onQueryChange = { str ->
            query = when (action) {
                TypeAction.DateInput -> str.filter { it.isDigit() }
                else -> str
            }
        },
        onSearch = ::search,
        enabled = enabled && action != TypeAction.DateInput,
        colors = SearchBarDefaults.colors(inputFieldColors = ifColors),
        placeholder = { Placeholder(text = placeholder) },
        trailingIcon = if (enabled) {
            { TrailingIcon { search(query) } }
        } else null,
        keyboardType = if (action == TypeAction.DistanceInput) KeyboardType.NumberPassword else KeyboardType.Text,
        focus = focus
    )

    if (focus != null)
        UnitLaunchedEffect { focus.requestFocus() }
}

@Composable
fun Placeholder(text: String) = Text(
    text = text,
    fontSize = MaterialTheme.typography.titleMedium.fontSize
)

@Composable
fun TrailingIcon(onSearch: () -> Unit) = IconButton(
    modifier = Modifier.size(60.dp),
    onClick = onSearch
) {
    Icon(
        modifier = Modifier.fillMaxSize(),
        imageVector = Icons.AutoMirrored.Default.Send,
        contentDescription = "Search"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    state: DatePickerState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) = MaterialTheme(typography = Typography()) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = state.selectedDateMillis != null,
                onClick = onConfirm
            ) { Text(text = "OK") }
        }
    ) { DatePicker(state = state) }
}

@ExperimentalMaterial3Api
@Composable
fun DropDown(
    query: String,
    placeholder: String,
    proposals: Collection<String>,
    onValueChanged: (String) -> Unit,
    onProposalSelected: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    focus: FocusRequester? = null
) {
    var exp by rememberMutableStateOf(value = true)
    val error = query !in proposals
    val scope = rememberCoroutineScope()

    val search = fun() {
        if (error) {
            scope.launch {
                MainActivity.snackbarHostState.showSnackbar("Veuillez rentrer un nom de ville valide")
            }
            return
        }

        onSearch(query)
    }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = exp,
        onExpandedChange = { exp = it }
    ) {
        TextField(
            value = query,
            onValueChange = onValueChanged,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { search() }),
            shape = RoundedCornerShape(10.dp),
            placeholder = { Placeholder(text = placeholder) },
            trailingIcon = { TrailingIcon(onSearch = search) },
            isError = error,

            modifier = Modifier
                .fillMaxSize()
                .menuAnchor()
                .focusRequesterIfNotNull(focus)
        )

        ExposedDropdownMenu(
            expanded = exp,
            onDismissRequest = { exp = false }
        ) {
            proposals.forEach {
                DropdownMenuItem(
                    text = { Text(text = it) },
                    onClick = {
                        exp = false
                        onProposalSelected(it)
                    }
                )
            }
        }
    }
}

@Preview(device = Devices.PIXEL_TABLET)
@Composable
private fun Prev() = ChatBotPrev {
    Column(
        modifier = Modifier.align(Alignment.BottomStart),
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        MySearchBar(
            placeholder = "Écrire ici\u2026"
        ) {
            Log.i(TAG, "I have searched $it")
        }
    }
}