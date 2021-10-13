package com.example.aoppart5chapter01.viewmodel.todo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aoppart5chapter01.data.entity.ToDoEntity
import com.example.aoppart5chapter01.domain.todo.InsertToDoItemUseCase
import com.example.aoppart5chapter01.presentation.detail.DetailMode
import com.example.aoppart5chapter01.presentation.detail.DetailViewModel
import com.example.aoppart5chapter01.presentation.detail.ToDoDetailState
import com.example.aoppart5chapter01.presentation.list.ListViewModel
import com.example.aoppart5chapter01.presentation.list.ToDoListState
import com.example.aoppart5chapter01.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

/**
 * [DetailViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. initData()
 * 2. test viewModel fetch
 * 3. test delete todo
 * 4. test update todo
 */
@ExperimentalCoroutinesApi
internal class DetailViewModelTest : ViewModelTest() {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val id = 1L

    private val detailViewModel by inject<DetailViewModel> { parametersOf(DetailMode.DETAIL, id) }
    private val listViewModel by inject<ListViewModel>()

    private val insertToSoItemUseCase: InsertToDoItemUseCase by inject()

    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Before
    fun init() {
        initData()
    }

    private fun initData() = runBlockingTest {
        insertToSoItemUseCase(todo)
    }

    @Test
    fun test_viewModel_fetch() = runBlockingTest {
        val testObservable = detailViewModel.todoDetailLiveData.test()

        detailViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(todo)
            )
        )
    }

    @Test
    fun test_delete_todo() = runBlockingTest {
        val detailTestObservable = detailViewModel.todoDetailLiveData.test()
        detailViewModel.deleteToDo()

        detailTestObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Delete
            )
        )

        val listTestObservable = listViewModel.todoListLiveData.test()
        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf())
            )
        )
    }

    @Test
    fun test_update_todo() = runBlockingTest {
        val testObservable = detailViewModel.todoDetailLiveData.test()

        val updateTiltle = "title 1 update"
        val updateDescription = "description 1 update"

        val updateToDo = todo.copy(
            title = updateTiltle,
            description = updateDescription
        )

        detailViewModel.writeToDo(
            title = updateTiltle,
            description = updateDescription
        )

        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(updateToDo)
            )
        )
    }
}