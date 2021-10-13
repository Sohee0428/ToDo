package com.example.aoppart5chapter01.viewmodel.todo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aoppart5chapter01.data.entity.ToDoEntity
import com.example.aoppart5chapter01.presentation.detail.DetailMode
import com.example.aoppart5chapter01.presentation.detail.DetailViewModel
import com.example.aoppart5chapter01.presentation.detail.ToDoDetailState
import com.example.aoppart5chapter01.presentation.list.ListViewModel
import com.example.aoppart5chapter01.presentation.list.ToDoListState
import com.example.aoppart5chapter01.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

/**
 * [DetailViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. test viewModel fetch
 * 2. test insert todo
 */
@ExperimentalCoroutinesApi
internal class DetailViewModelForWriteTest : ViewModelTest() {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val id = 0L

    private val detailViewModel by inject<DetailViewModel> { parametersOf(DetailMode.WRITE, id) }
    private val listViewModel by inject<ListViewModel>()

    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Test
    fun test_viewModel_fetch() = runBlockingTest {
        val testObservable = detailViewModel.todoDetailLiveData.test()

        detailViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Write
            )
        )
    }

    @Test
    fun test_insert_todo() = runBlockingTest {
        val detailTestObservable = detailViewModel.todoDetailLiveData.test()
        val listTestObservable = listViewModel.todoListLiveData.test()

        detailViewModel.writeToDo(
            title = todo.title,
            description = todo.description
        )

        detailTestObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(todo)
            )
        )
        assert(detailViewModel.detailMode == DetailMode.DETAIL)
        assert(detailViewModel.id == id)

//        뒤로 나가서 리스트 확인
        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf(todo))
            )

        )

    }
}