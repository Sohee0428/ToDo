package com.example.aoppart5chapter01.data.repository

import com.example.aoppart5chapter01.data.entity.ToDoEntity
import com.example.aoppart5chapter01.data.local.db.dao.ToDoDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultToDoRepository(
    private val toDoDao: ToDoDao,
    private val ioDispatcher: CoroutineDispatcher
) : ToDoRepository {

    override suspend fun getToDoList(): List<ToDoEntity> = withContext(ioDispatcher) {
        toDoDao.getAll()
    }

    override suspend fun getToDoItem(itemId: Long): ToDoEntity? = withContext(ioDispatcher) {
        toDoDao.getById(itemId)
    }

    override suspend fun insertToDoList(toDoList: List<ToDoEntity>) = withContext(ioDispatcher) {
        toDoDao.insert(toDoList)
    }

    override suspend fun insertToDoItem(toDoItem: ToDoEntity): Long = withContext(ioDispatcher) {
        toDoDao.insert(toDoItem)
    }

    override suspend fun updateToDoItem(toDoItem: ToDoEntity) {
        toDoDao.update(toDoItem)
    }

    override suspend fun deleteAll() {
        toDoDao.deleteAll()
    }

    override suspend fun deleteToDoItem(id: Long) {
        toDoDao.delete(id)
    }
}