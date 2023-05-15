package com.example.stackquestions

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stackquestions.data.QuestionDatabase
import com.example.stackquestions.data.QuestionRepository
import com.example.stackquestions.presentations.questionScreen.MainScreen
import com.example.stackquestions.viewmodels.questionviewmodel.QuestionViewModel
import com.example.stackquestions.viewmodels.questionviewmodel.QuestionViewModelProviderFactory
import com.example.stackquestions.viewmodels.searchviewmodel.SearchViewModel
import com.example.stackquestions.viewmodels.searchviewmodel.SearchViewModelFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val questionRepository = QuestionRepository(QuestionDatabase(this))
            val questionViewModelProviderFactory = QuestionViewModelProviderFactory(questionRepository)
            val questionViewModel = ViewModelProvider(this,questionViewModelProviderFactory)[QuestionViewModel::class.java]
            val searchViewModelProviderFactory = SearchViewModelFactory(questionRepository)
            val searchViewModel = ViewModelProvider(this,searchViewModelProviderFactory)[SearchViewModel::class.java]
            var mainNavController = rememberNavController()
            Scaffold(
                bottomBar = {
                    MainBottomNavigation(mainNavController)
                }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    MainNavigation(mainNavController,questionViewModel,searchViewModel)
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(
    mainNavController: NavHostController,
    questionViewModel: QuestionViewModel,
    searchViewModel: SearchViewModel
)
{
    NavHost(
        navController = mainNavController,
        startDestination = Home.route
    ){
        composable(Home.route)
        {
            MainScreen(questionViewModel,searchViewModel)
        }
        composable(Filter.route)
        {

        }
        composable(Favourites.route)
        {

        }
    }
}