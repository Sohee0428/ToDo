package com.example.aoppart5chapter01.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aoppart5chapter01.data.entity.ToDoEntity
import com.example.aoppart5chapter01.domain.todo.DeleteAllToDoItemUseCase
import com.example.aoppart5chapter01.domain.todo.GetToDoListUseCase
import com.example.aoppart5chapter01.domain.todo.UpdateToDoUseCase
import com.example.aoppart5chapter01.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 필요한 UseCase
 * 1. [GetToDoListUseCase]
 * 2. [UpdateToDoUseCase]
 * 3. [DeleteAllToDoItemUseCase]
 */

internal class ListViewModel(
    private val getToDoListUseCase: GetToDoListUseCase,
    private val updateToDoUseCase: UpdateToDoUseCase,
    private val deleteAllToDoItemUseCase: DeleteAllToDoItemUseCase
//    private val insertToDoListUseCase: InsertToDoListUseCase
) : BaseViewModel() {

    private var _toDoListLiveData = MutableLiveData<ToDoListState>(ToDoListState.UnInitialized)
    val todoListLiveData: LiveData<ToDoListState> = _toDoListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _toDoListLiveData.postValue(ToDoListState.Loading)
//        테스트용 코드
//        insertToDoListUseCase(
//            (0 until 10).map {
//                ToDoEntity(
//                    id =it.toLong(),
//                    title = "title $it",
//                    description = "description $it",
//                    hasCompleted = false
//                )
//            }
//        )
        _toDoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))
    }

    fun updateEntity(todoEntity: ToDoEntity) = viewModelScope.launch {
        updateToDoUseCase(todoEntity)
    }

    fun deleteAll() = viewModelScope.launch {
        _toDoListLiveData.postValue(ToDoListState.Loading)
        deleteAllToDoItemUseCase()
        _toDoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))
    }
}