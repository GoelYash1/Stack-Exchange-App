package com.example.stackquestions

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController

@Composable
fun MainBottomNavigation(navController: NavController) {
    val destinationList = listOf(
        Filter,
        Home,
        Favourites
    )
    val selectedIndex = rememberSaveable {
        mutableStateOf(1)
    }
    NavigationBar(
        containerColor = Color.Transparent
    ) {
        destinationList.forEachIndexed { index, destination ->
            NavigationBarItem(
                label = { Text(text = destination.title) },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = destination.title
                    )
                },
                selected = index == selectedIndex.value,
                onClick = {
                    selectedIndex.value = index
                    navController.navigate(destinationList[index].route) {
                        popUpTo(Home.route)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}