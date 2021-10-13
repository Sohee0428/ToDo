package com.example.aoppart5chapter01.di

import android.content.Context
import androidx.room.Room
import com.example.aoppart5chapter01.data.local.db.ToDoDatabase
import com.example.aoppart5chapter01.data.repository.DefaultToDoRepository
import com.example.aoppart5chapter01.data.repository.ToDoRepository
import com.example.aoppart5chapter01.domain.todo.*
import com.example.aoppart5chapter01.presentation.detail.DetailMode
import com.example.aoppart5chapter01.presentation.detail.DetailViewModel
import com.example.aoppart5chapter01.presentation.list.ListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

    //    ViewModel
    viewModel { ListViewModel(get(), get(), get()) }
    viewModel { (detailMode: DetailMode, id: Long) ->
        DetailViewModel(
            detailMode = detailMode,
            id = id,
            get(),
            get(),
            get(),
            get()
        )
    }

//    UseCase
    factory { GetToDoListUseCase(get()) }
    factory { InsertToDoItemUseCase(get()) }
    factory { InsertToDoListUseCase(get()) }
    factory { UpdateToDoUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }
    factory { DeleteToDoItemUseCase(get()) }

//    Repository
    single<ToDoRepository> { DefaultToDoRepository(get(), get()) }

    single { provideDB(androidApplication()) }
    single { providerToDoDao(get()) }

}

internal fun provideDB(context: Context): ToDoDatabase =
    Room.databaseBuilder(context, ToDoDatabase::class.java, ToDoDatabase.DB_NAME).build()

internal fun providerToDoDao(database: ToDoDatabase) = database.toDoDao()