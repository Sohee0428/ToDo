package com.example.aoppart5chapter01.domain.todo

import com.example.aoppart5chapter01.data.entity.ToDoEntity
import com.example.aoppart5chapter01.data.repository.ToDoRepository
import com.example.aoppart5chapter01.domain.UseCase

internal class InsertToDoItemUseCase(
    private val toDoRepository: ToDoRepository
) : UseCase {

    suspend operator fun invoke(toDoItem: ToDoEntity): Long {
        return toDoRepository.insertToDoItem(toDoItem)
    }
}