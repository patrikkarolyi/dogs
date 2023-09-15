package com.example.dogs.ui.custom_view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dogs.R
import com.example.dogs.navigation.NavigationItem
import com.example.dogs.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MyNavDrawerView(
    drawerState: DrawerState,
    navController: NavController,
    content: @Composable () -> Unit
) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val items = listOf(
        NavigationItem(
            title = stringResource(R.string.all),
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            onClick = { navController.navigate(Screen.ListScreen.route) }
        ),
        NavigationItem(
            title = stringResource(R.string.dogs),
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            onClick = { navController.navigate(Screen.FavDogScreen.route) }
        ),
        NavigationItem(
            title = stringResource(R.string.images),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            onClick = { navController.navigate(Screen.FavImgScreen.route) }
        ),
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        content = content,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(text = item.title)
                        },
                        icon = {
                            Icon(
                                imageVector =
                                    if (index == selectedItemIndex) item.selectedIcon
                                    else item.unselectedIcon,
                                contentDescription = ""
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                selectedItemIndex = index
                                drawerState.close()
                                item.onClick()
                            }
                        },
                        selected = index == selectedItemIndex,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        },
    )
}