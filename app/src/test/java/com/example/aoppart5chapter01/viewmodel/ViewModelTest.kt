package com.example.aoppart5chapter01.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.aoppart5chapter01.di.appTestModule
import com.example.aoppart5chapter01.di.livedata.LiveDataTestObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
internal abstract class ViewModelTest : KoinTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var context: Application

    //    coroutine 테스트 중 mainThread <-> IoThread
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        startKoin {
            androidContext(context)
            modules(appTestModule)
        }
        Dispatchers.setMain(dispatcher)
    }

    //    test가 끝난 후 코루틴 사용 + koin 주입
    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()   // MainDispatcher를 초기화 해주어야 메모리 누수가 발생하지 않음.
    }

    protected fun <T> LiveData<T>.test(): LiveDataTestObserver<T> {
        val testObsercer = LiveDataTestObserver<T>()
        observeForever(testObsercer)
        return testObsercer
    }

}