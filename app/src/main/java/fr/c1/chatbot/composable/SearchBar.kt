package fr.c1.chatbot.composable

import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.ui.theme.colorSchemeExtension
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import android.util.Log

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
            modifier = Modifier.fillMaxSize(),
            enabled = enabled,
            singleLine = true,
            textStyle = LocalTextStyle.current.merge(
                TextStyle(
                    color = textColor,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
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
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    val keyboardCtrl = LocalSoftwareKeyboardController.current!!
    val focusMgr = LocalFocusManager.current

    fun search(str: String) {
        keyboardCtrl.hide()
        focusMgr.clearFocus()
        onSearch(str)
    }

    SearchBar(
        modifier = modifier
            .height(75.dp)
            .padding(horizontal = 10.dp),
        query = query,
        onQueryChange = { str -> query = str },
        onSearch = ::search,
        enabled = enabled,
        colors = SearchBarDefaults.colors(
            inputFieldColors = TextFieldDefaults.colors(
                focusedPlaceholderColor = MaterialTheme.colorSchemeExtension.placeHolder,
                unfocusedPlaceholderColor = MaterialTheme.colorSchemeExtension.placeHolder
            )
        ),
        placeholder = {
            Text(
                text = placeholder,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        },
        trailingIcon = if (enabled) {
            {
                IconButton(
                    modifier = Modifier.size(60.dp),
                    onClick = { search(query) }) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.AutoMirrored.Default.Send,
                        contentDescription = "Search"
                    )
                }
            }
        } else null,
    )
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

//        MySearchBar(
//            enabled = false,
//            placeholder = "Sélectionnez un des choix au dessus"
//        ) {}
    }
}