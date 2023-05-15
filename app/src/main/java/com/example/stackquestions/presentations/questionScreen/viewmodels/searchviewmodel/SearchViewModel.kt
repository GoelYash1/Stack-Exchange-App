package com.example.stackquestions.presentations.questionScreen.viewmodels.searchviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stackquestions.data.QuestionRepository
import com.example.stackquestions.data.models.Question
import com.example.stackquestions.util.Resource
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: QuestionRepository) : ViewModel() {
    var editTextValue = ""
    private val _allQuestions = MutableLiveData<Resource<List<Question>>>()

    private val _filteredQuestions = MutableLiveData<Resource<List<Question>>>()
    val filteredQuestions: LiveData<Resource<List<Question>>> = _filteredQuestions

    private val _tagsList = MutableLiveData<MutableList<String>>()
    val tagsList: LiveData<MutableList<String>> = _tagsList

    val selectedChips = MutableLiveData<MutableSet<String>>(mutableSetOf())

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    fun searchQuery(queryText: String) {
        viewModelScope.launch {
            repository.getSearchResults(queryText = queryText).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _filteredQuestions.value = Resource.Loading()
                    }

                    is Resource.Success -> {
                        val tagCountMap = mutableMapOf<String, Int>()
                        for (question in resource.data!!) {
                            for (tag in question.tags!!) {
                                tagCountMap[tag] = (tagCountMap[tag] ?: 0) + 1
                            }
                        }
                        val newList =
                            tagCountMap.entries.sortedByDescending { it.value }.map { it.key }
                                .toMutableList()
                        _tagsList.value = newList
                        _allQuestions.value = Resource.Success(resource.data)
                        _filteredQuestions.value = resource
                    }

                    is Resource.Error -> {
                        _filteredQuestions.value = resource
                        _tagsList.value = mutableListOf()
                        _allQuestions.value = Resource.Success(listOf())
                        _errorMessage.value = resource.error?.message
                    }
                }
            }
        }
    }

    fun filterQuery() {
        if (selectedChips.value == null || selectedChips.value?.isEmpty() == true) {
            if (_allQuestions.value != null) {
                _filteredQuestions.value = _allQuestions.value
            }
        } else {
            val updatedQuestions = mutableListOf<Question>()
            for (question in _allQuestions.value?.data!!) {
                for (tag in question.tags!!) {
                    if (selectedChips.value!!.contains(tag)) {
                        updatedQuestions.add(question)
                        break
                    }
                }
            }
            _filteredQuestions.value = Resource.Success(updatedQuestions)
        }
    }

}