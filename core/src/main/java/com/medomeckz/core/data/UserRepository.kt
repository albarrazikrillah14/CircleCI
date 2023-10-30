package com.medomeckz.core.data

import com.medomeckz.core.data.source.local.LocalDataSource
import com.medomeckz.core.data.source.remote.RemoteDataSource
import com.medomeckz.core.data.source.remote.network.ApiResponse
import com.medomeckz.core.data.source.remote.response.UserResponse
import com.medomeckz.core.domain.model.Users
import com.medomeckz.core.domain.repository.IUserRepository
import com.medomeckz.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : IUserRepository {

    override fun getUsersByUsername(username: String): Flow<Resource<List<Users>>> =
        object :
            NetworkBoundResource<List<Users>, List<UserResponse>>() {
            override fun loadFromNetwork(data: List<UserResponse>): Flow<List<Users>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> =
                remoteDataSource.getUsersByUsername(username)

        }.asFlow()

    override fun getUserDetail(username: String): Flow<Resource<Users>> =
        object : NetworkBoundResource<Users, UserResponse>() {
            override fun loadFromNetwork(data: UserResponse): Flow<Users> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<UserResponse>> =
                remoteDataSource.getUserDetail(username)

        }.asFlow()


    override fun getUserFollowers(username: String): Flow<Resource<List<Users>>> =
        object :
            NetworkBoundResource<List<Users>, List<UserResponse>>() {
            override fun loadFromNetwork(data: List<UserResponse>): Flow<List<Users>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> =
                remoteDataSource.getUserFollowers(username)

        }.asFlow()

    override fun getUserFollowing(username: String): Flow<Resource<List<Users>>> =
        object :
            NetworkBoundResource<List<Users>, List<UserResponse>>() {
            override fun loadFromNetwork(data: List<UserResponse>): Flow<List<Users>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> =
                remoteDataSource.getUserFollowing(username)

        }.asFlow()

    override fun getAllUserFavorite(): Flow<List<Users>> {
        return localDataSource.getAllUserFavorite().map { userEntities ->
            userEntities.let {
                DataMapper.mapEntitiesToDomain(it)
            }
        }
    }

    override suspend fun insertUserFavorite(users: Users) {
        localDataSource.insertUserFavorite(DataMapper.mapDomainToEntity(users))
    }

    override suspend fun deleteUserFavorite(users: Users) {
        localDataSource.deleteUserFavorite(DataMapper.mapDomainToEntity(users))
    }

    override fun getFavoriteIsExists(username: String): Flow<Boolean> =
        localDataSource.getFavoriteIsExists(username)


}