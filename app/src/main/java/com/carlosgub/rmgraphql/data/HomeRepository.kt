package com.carlosgub.rmgraphql.data

import com.carlosgub.rmgraphql.data.network.HomeRemoteDataSource
import com.carlosgub.rmgraphql.helpers.toCharacterModel
import com.carlosgub.rmgraphql.ui.model.CharacterModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: HomeRemoteDataSource,
) {
    suspend fun getCharactersList(): List<CharacterModel> =
        withContext(Dispatchers.Default) {
            api.getCharacters().toList().flatMap {
                it.toCharacterModel()
            }
        }
}
