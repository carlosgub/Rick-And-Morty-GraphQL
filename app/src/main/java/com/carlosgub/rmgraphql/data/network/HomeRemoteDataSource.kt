package com.carlosgub.rmgraphql.data.network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.carlosgub.rmgraphql.AllCharactersQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class HomeRemoteDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {

    suspend fun getCharacters(): Flow<AllCharactersQuery.Characters?> {
        val getCharactersResponse = apolloClient.query(AllCharactersQuery()).execute()
        val responseData: AllCharactersQuery.Data? = getCharactersResponse.data
        return flow {
            responseData?.let { response ->
                emit(response.characters)
            } ?: throw ApolloException(
                message = "${getCharactersResponse.errors?.first()?.message}"
            )
        }
    }
}