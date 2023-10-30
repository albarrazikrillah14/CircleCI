package com.example.githubuser.di

import com.example.githubuser.presentation.main.MainViewModel
import com.example.githubuser.presentation.detail.DetailViewModel
import com.medomeckz.core.domain.usecase.UserUseCase
import com.medomeckz.core.domain.usecase.UsersInteractor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<UserUseCase> { UsersInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}