package com.medomeckz.core.data.source.remote

import com.medomeckz.core.data.source.remote.network.ApiResponse
import com.medomeckz.core.data.source.remote.network.ApiService
import com.medomeckz.core.data.source.remote.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(
    private val apiService: ApiService,
) {

    suspend fun getUsersByUsername(username: String): Flow<ApiResponse<List<UserResponse>>> {
        return flow {
            try {
                val response = apiService.getUserByUsername(username)
                val dataArray = response.items
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.items))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserDetail(username: String): Flow<ApiResponse<UserResponse>> {
        return flow {
            try {
                val response = apiService.getUserDetail(username)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserFollowers(username: String): Flow<ApiResponse<List<UserResponse>>> {
        return flow {
            try {
                val response = apiService.getUserFollowers(username)
                val dataArray = arrayOf(response)
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserFollowing(username: String): Flow<ApiResponse<List<UserResponse>>> {
        return flow {
            try {
                val response = apiService.getUserFollowing(username)
                val dataArray = arrayOf(response)
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}