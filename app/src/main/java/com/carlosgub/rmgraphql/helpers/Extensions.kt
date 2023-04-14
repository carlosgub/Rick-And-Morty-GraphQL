package com.carlosgub.rmgraphql.helpers

import com.carlosgub.rmgraphql.AllCharactersQuery
import com.carlosgub.rmgraphql.ui.model.CharacterModel

fun AllCharactersQuery.Characters?.toCharacterModel(): List<CharacterModel> =
    this?.results?.filterNotNull()?.map {
        CharacterModel(
            id = it.id.orEmpty(),
            image = it.image.orEmpty(),
            name = it.name.orEmpty(),
            species = it.species.orEmpty(),
            status = it.status.orEmpty(),
            origin = it.origin?.name.orEmpty(),
            location = it.location?.name.orEmpty()
        )
    }.orEmpty()
