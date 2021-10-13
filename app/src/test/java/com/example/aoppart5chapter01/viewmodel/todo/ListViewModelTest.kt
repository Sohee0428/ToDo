package com.example.aoppart5chapter01.viewmodel.todo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aoppart5chapter01.data.entity.ToDoEntity
import com.example.aoppart5chapter01.domain.todo.GetToDoItemUseCase
import com.example.aoppart5chapter01.domain.todo.InsertToDoListUseCase
import com.example.aoppart5chapter01.presentation.list.ListViewModel
import com.example.aoppart5chapter01.presentation.list.ToDoListState
import com.example.aoppart5chapter01.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.inject

/**
 * [ListViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. initData()
 * 2. test viewModel fetch
 * 3. test Item update
 * 4. test Item Delete All
 *
 */
@ExperimentalCoroutinesApi
internal class ListViewModelTest : ViewModelTest() {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val viewModel: ListViewModel by inject()

    private val insertToDoListUseCase: InsertToDoListUseCase by inject()

    private val getToDoItemUseCase: GetToDoItemUseCase by inject()

    private val mockList = (0 until 10).map {
        ToDoEntity(
            id = it.toLong(),
            title = "title $it",
            description = "description $it",
            hasCompleted = false
        )
    }

    /**
     * 필요한 UseCase들
     *  1. InsertToDoListUseCase
     *  2. GetToDoItemUseCase
     */

    @Before
    fun init() {
        initData()
    }

    private fun initData() = runBlockingTest {
        insertToDoListUseCase(mockList)
    }

    //    Test : 입력된 데이터를 불러와서 검증한다.
    @Test
    fun test_viewModel_fetch(): Unit = runBlockingTest {
        val testObservable = viewModel.todoListLiveData.test()
        viewModel.fetchData()
        testObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(mockList)
            )
        )
    }

    //    Test : 데이터를 업데이트 했을 때 잘 반영되는가
    @Test
    fun test_Item_update(): Unit = runBlockingTest {
        val todo = ToDoEntity(
            id = 1,
            title = "title 1",
            description = "description 1",
            hasCompleted = true
        )
        viewModel.updateEntity(todo)
        assert(getToDoItemUseCase(todo.id)?.hasCompleted ?: false == todo.hasCompleted)

    }

    //    Test : 데이터를 다 날렸을 때 빈 상태로 보여지는가
    @Test
    fun test_Item_Delete_All(): Unit = runBlockingTest {
        val testObservable = viewModel.todoListLiveData.test()
        viewModel.deleteAll()
        testObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf())
            )
        )
    }

}