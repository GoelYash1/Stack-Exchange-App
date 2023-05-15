package com.example.stackquestions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home

interface Destinations {
    val route: String
    val icon: Int
    val title: String
}

object Filter: Destinations{
    override val route = "Filter"
    override val icon = R.drawable.ic_filter
    override val title = "Filter"
}
object Home: Destinations{
    override val route = "Home"
    override val icon = R.drawable.ic_home
    override val title = "Home"
}
object Favourites: Destinations{
    override val route = "Favourites"
    override val icon = R.drawable.ic_favourite
    override val title = "Favourites"
}
