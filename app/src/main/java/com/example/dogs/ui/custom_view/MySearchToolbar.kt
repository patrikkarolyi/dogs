package com.example.dogs.ui.custom_view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dogs.R
import com.example.dogs.ui.theme.DogsTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MySearchToolbar(
    modifier: Modifier = Modifier,
    onSearchTriggered: (String) -> Unit,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        label = {
            Text(
                text = stringResource(R.string.filter_input_hint),
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colors.secondary,
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        searchQuery = ""
                        onSearchTriggered("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colors.secondary,
                    )
                }
            }
        },
        onValueChange = {
            if (!it.contains("\n")) {
                searchQuery = it
            }
        },
        modifier = modifier
            .padding(8.dp)
            .focusRequester(focusRequester),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearchTriggered(searchQuery)
            },
        ),
        maxLines = 1,
        singleLine = true,
    )
}

@Preview
@Composable
private fun SearchToolbarPreview() {
    DogsTheme {
        MySearchToolbar(
            onSearchTriggered = {},
        )
    }
}