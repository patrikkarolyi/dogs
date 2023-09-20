package com.example.dogs.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dogs.navigation.NavigationItems.navigationItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavDrawer(
    drawerState: DrawerState,
    navController: NavController,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestionation = navBackStackEntry.value?.destination

    ModalNavigationDrawer(
        drawerState = drawerState,
        content = content,
        modifier = modifier,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(32.dp))
                navigationItems.forEach { item ->

                    val isSelected = currentDestionation?.hierarchy?.any {
                        it.route == item.route
                    } ?: false

                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(id = item.titleRes),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        },
                        icon = {
                            Icon(
                                imageVector =
                                if (isSelected) item.selectedIcon
                                else item.unselectedIcon,
                                contentDescription = ""
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            if (!isSelected) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id)
                                    launchSingleTop = true
                                }
                            }
                        },
                        selected = isSelected, modifier = Modifier.padding(8.dp)
                    )
                }
            }
        },
    )
}