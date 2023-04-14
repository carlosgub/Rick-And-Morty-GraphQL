package com.carlosgub.rmgraphql.model.usecase

import com.carlosgub.rmgraphql.data.HomeRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke() =
        flow {
            emit(homeRepository.getCharactersList())
        }
}
