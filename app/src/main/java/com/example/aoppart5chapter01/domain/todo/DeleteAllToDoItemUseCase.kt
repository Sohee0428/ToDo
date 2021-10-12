package com.example.aoppart5chapter01.domain.todo

import com.example.aoppart5chapter01.data.repository.ToDoRepository
import com.example.aoppart5chapter01.domain.UseCase

internal class DeleteAllToDoItemUseCase(
    private val toDoRepository: ToDoRepository
) : UseCase {

    suspend operator fun invoke() {
        return toDoRepository.deleteAll()
    }
}