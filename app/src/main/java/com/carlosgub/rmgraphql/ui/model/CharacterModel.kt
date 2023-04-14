package com.carlosgub.rmgraphql.ui.model

data class CharacterModel(
    val id: String,
    val image: String,
    val species: String,
    val name: String,
    val status: String,
    val origin: String,
    val location: String,
    var showDetail: Boolean = false
)