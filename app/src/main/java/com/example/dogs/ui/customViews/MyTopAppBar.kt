package com.example.dogs.ui.customViews

import androidx.annotation.StringRes
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.dogs.R
import com.example.dogs.ui.list.NavDirection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    @StringRes titleRes: Int,
    navigationIcon: ImageVector,
    navigationIconContentDescription: String?,
    actionIcon: ImageVector,
    actionIconContentDescription: String?,
    onNavigationClick: (NavDirection) -> Unit = {},
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        navigationIcon = {
            IconButton(
                onClick = {
                    onNavigationClick(NavDirection.ToFavoriteDogs)
                }) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    )
}

@Preview("Top App Bar")
@Composable
private fun MyTopAppBarPreview() {
    MyTopAppBar(
        titleRes = R.string.app_name,
        navigationIcon = Icons.Rounded.Search,
        navigationIconContentDescription = "Search",
        actionIcon = Icons.Rounded.Star,
        actionIconContentDescription = "Favorites",
    )
}