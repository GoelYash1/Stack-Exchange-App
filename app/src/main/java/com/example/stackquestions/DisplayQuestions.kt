package com.example.stackquestions

import android.util.Log
import android.view.ViewTreeObserver
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.stackquestions.util.Resource
import com.example.stackquestions.viewmodels.QuestionViewModel

@Composable
fun DisplayQuestions(
    viewModel: QuestionViewModel
)
{
    val questionList = viewModel.questions
    questionList.observe(LocalLifecycleOwner.current, Observer { response ->
        when(response){
            is Resource.Success -> {
                response.data?.let { questionResponse ->
                    Log.d("The data is not empty",questionResponse.items.toString().take(1))
                }
            }
            else -> Log.d("No data is there","Hello ")
        }

    })
    Text(text = "Hello")
}