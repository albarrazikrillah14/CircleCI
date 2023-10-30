package com.medomeckz.core.domain.usecase

import com.medomeckz.core.data.Resource
import com.medomeckz.core.domain.model.Users
import kotlinx.coroutines.flow.Flow


interface UserUseCase {

    fun getUsersByUsername(username: String): Flow<Resource<List<Users>>>

    fun getUserDetail(username: String): Flow<Resource<Users>>

    fun getUserFollowers(username: String): Flow<Resource<List<Users>>>

    fun getUserFollowing(username: String): Flow<Resource<List<Users>>>

    fun getAllUserFavorite(): Flow<List<Users>>

    suspend fun insertUserFavorite(users: Users)

    suspend fun deleteUserFavorite(users: Users)

    fun getFavoriteIsExists(username: String): Flow<Boolean>

}