package com.example.dogs.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.dogs.R

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    LIST(
        selectedIcon = Icons.Rounded.Add,
        unselectedIcon = Icons.Rounded.Add,
        iconTextId = R.string.app_name,
        titleTextId = R.string.app_name,
    ),
    FAVORITE_DOGS(
        selectedIcon = Icons.Rounded.Star,
        unselectedIcon = Icons.Rounded.Star,
        iconTextId = R.string.app_name,
        titleTextId = R.string.app_name,
    ),
    FAVORITE_IMAGES(
        selectedIcon = Icons.Rounded.Favorite,
        unselectedIcon = Icons.Rounded.Favorite,
        iconTextId = R.string.app_name,
        titleTextId = R.string.app_name,
    ),
}

fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false