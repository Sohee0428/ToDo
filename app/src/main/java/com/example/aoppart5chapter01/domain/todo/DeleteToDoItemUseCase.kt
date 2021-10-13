package com.example.aoppart5chapter01.domain.todo

import com.example.aoppart5chapter01.data.repository.ToDoRepository
import com.example.aoppart5chapter01.domain.UseCase

internal class DeleteToDoItemUseCase(
    private val toDoRepository: ToDoRepository
) : UseCase {

    suspend operator fun invoke(itemId: Long) {
        return toDoRepository.deleteToDoItem(itemId)
    }
}