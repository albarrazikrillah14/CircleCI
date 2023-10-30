package com.medomeckz.core.domain.usecase

import com.medomeckz.core.data.Resource
import com.medomeckz.core.domain.model.Users
import com.medomeckz.core.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow

class UsersInteractor(
    private val userRepository: IUserRepository,
) : UserUseCase {
    override fun getUsersByUsername(username: String): Flow<Resource<List<Users>>> =
        userRepository.getUsersByUsername(username)

    override fun getUserDetail(username: String): Flow<Resource<Users>> =
        userRepository.getUserDetail(username)

    override fun getUserFollowers(username: String): Flow<Resource<List<Users>>> =
        userRepository.getUserFollowers(username)

    override fun getUserFollowing(username: String): Flow<Resource<List<Users>>> =
        userRepository.getUserFollowing(username)

    override fun getAllUserFavorite(): Flow<List<Users>> =
        userRepository.getAllUserFavorite()

    override suspend fun insertUserFavorite(users: Users) =
        userRepository.insertUserFavorite(users)

    override suspend fun deleteUserFavorite(users: Users) =
        userRepository.deleteUserFavorite(users)

    override fun getFavoriteIsExists(username: String): Flow<Boolean> =
        userRepository.getFavoriteIsExists(username)

}