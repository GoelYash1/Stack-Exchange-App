package com.example.stackquestions.presentations.filterScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.stackquestions.Home
import com.example.stackquestions.SearchBar
import com.example.stackquestions.viewmodels.searchviewmodel.SearchViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterScreen(searchViewModel: SearchViewModel,navController: NavHostController) {
    val tagsList by searchViewModel.tagsList.observeAsState(initial = mutableListOf())
    var searchQuery by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(listOf<String>()) }
    Scaffold(
        modifier = Modifier.padding(16.dp),
        topBar = {
            FilterScreenTopAppBar(
                onSearch = { query ->
                    searchQuery = query
                    searchViewModel.searchQuery(query)
                },
                selectedTags = selectedTags,
                onChipRemoved = { tag ->
                    selectedTags = selectedTags.filter { it != tag }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (tagsList.isNotEmpty()) {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxHeight(0.9f),
                        contentPadding = PaddingValues(8.dp),
                        columns = GridCells.Adaptive(100.dp),
                        content = {
                            items(tagsList) {
                                Chip(
                                    modifier = Modifier.padding(5.dp),
                                    colors = ChipDefaults.chipColors(if (selectedTags.contains(it)) Color(0xFF8BC34A) else Color.LightGray), // Set different background color for selected and unselected tags
                                    onClick = {
                                        selectedTags = if (selectedTags.contains(it)) {
                                            // Remove the tag from the selected tags list if it's already selected
                                            selectedTags.filter { tag -> tag != it }
                                        } else {
                                            // Add the tag to the selected tags list if it's not already selected
                                            selectedTags + it
                                        }
                                    }
                                ) {
                                    Text(text = it, fontWeight = FontWeight.SemiBold)
                                }

                            }
                        }
                    )
                }
                else {
                    Text(text = "Search For Tags", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                }
                Button(
                    shape = CircleShape,
                    onClick = {
                        searchViewModel.selectedChips.value?.clear()
                        selectedTags.forEach { stag->
                            searchViewModel.selectedChips.value?.add(stag)
                        }
                        navController.navigate(Home.route)
                    }
                ) {
                    Text(text = "Save and Exit to Question Screen")
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterScreenTopAppBar(
    onSearch: (String) -> Unit,
    selectedTags: List<String>,
    onChipRemoved: (String) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        SearchBar(onSearch = { onSearch(it.trim()) })
        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)) {
            items(selectedTags) { tag ->
                Chip(
                    modifier = Modifier.padding(4.dp),
                    colors = ChipDefaults.chipColors(Color(0xFF8BC34A)),
                    onClick = {
                        onChipRemoved(tag)
                    }
                ) {
                    Text(text = tag)
                }
            }
        }
    }
}