package com.example.aoppart5chapter01.presentation.list

import com.example.aoppart5chapter01.data.entity.ToDoEntity

sealed class ToDoListState {

    object UnInitialised : ToDoListState()

    object Loading : ToDoListState()

    data class Success(
        val toDoList: List<ToDoEntity>
    ) : ToDoListState()

    object Error : ToDoListState()
}
