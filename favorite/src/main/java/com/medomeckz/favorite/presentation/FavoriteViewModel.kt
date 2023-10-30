package com.medomeckz.favorite.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.medomeckz.core.domain.usecase.UserUseCase


class FavoriteViewModel(
    private val userUseCase: UserUseCase) : ViewModel() {

    fun getAllUserFavorite() = userUseCase.getAllUserFavorite().asLiveData()
}