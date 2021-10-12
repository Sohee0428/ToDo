package com.example.aoppart5chapter01.di

import com.example.aoppart5chapter01.data.repository.TestToDoRepository
import com.example.aoppart5chapter01.data.repository.ToDoRepository
import com.example.aoppart5chapter01.domain.todo.*
import com.example.aoppart5chapter01.presentation.list.ListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {

//    ViewModel
    viewModel { ListViewModel(get(), get(), get()) }

//    UseCase
    factory { GetToDoListUseCase(get()) }
    factory { InsertToDoListUseCase(get()) }
    factory { UpdateToDoUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }

//    Repository
    single<ToDoRepository> { TestToDoRepository() }

}