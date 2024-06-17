package fr.c1.chatbot.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color

inline fun <T> LazyGridScope.items(
    items: Collection<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(item: T) -> GridItemSpan)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyGridItemScope.(item: T) -> Unit
) {
    items.forEach {
        item(
            key = key?.invoke(it),
            span = span?.run { { span(it) } },
            contentType = contentType(it)
        ) {
            itemContent(it)
        }
    }
}

fun Modifier.focusRequesterIfNotNull(fr: FocusRequester?) =
    if (fr == null) this
    else focusRequester(fr)

fun Modifier.backgroundIf(color: Color, condition: Boolean) =
    if (condition) background(color)
    else this