package com.carlosgub.rmgraphql.ui.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.carlosgub.rmgraphql.core.sealed.GenericState
import com.carlosgub.rmgraphql.helpers.getDataFromUiState
import com.carlosgub.rmgraphql.ui.model.CharacterModel
import com.carlosgub.rmgraphql.ui.theme.BackgroundColor
import com.carlosgub.rmgraphql.ui.theme.TextTitleColor

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<GenericState<List<CharacterModel>>>(
        initialValue = GenericState.Loading,
        key1 = lifecycle,
        key2 = viewModel,
        producer = {
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    value = it
                }
            }
        }
    )
    val list = getDataFromUiState(uiState) ?: listOf()
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (rv) = createRefs()

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 12.dp),
            state = rememberLazyGridState(),
            modifier = modifier
                .padding(horizontal = 12.dp)
                .constrainAs(rv) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                }
        ) {
            items(list, key = {
                it.id
            }) { characterModel ->
                MovieBookedItem(characterModel = characterModel,
                    onclick = { itemClicked ->
                        viewModel.editDetailVisibility(itemClicked)
                    }
                )
            }
        }

    }
}

@Composable
fun MovieBookedItem(
    characterModel: CharacterModel,
    onclick: (CharacterModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        backgroundColor = BackgroundColor,
        modifier = modifier
            .padding(all = 12.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (movieCard, detail) = createRefs()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(characterModel.image)
                    .crossfade(true)
                    .build(),
                contentDescription = "movie poster",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clickable {
                        onclick(characterModel)
                    }
                    .aspectRatio(1.0f)
                    .clip(RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp))
                    .constrainAs(movieCard) {
                        linkTo(start = parent.start, end = parent.end)
                        top.linkTo(parent.top)
                        width = Dimension.wrapContent
                    }
            )
            AnimatedVisibility(
                characterModel.showDetail,
                modifier = Modifier
                    .constrainAs(detail) {
                        top.linkTo(movieCard.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
            ) {
                CharacterDetail(
                    characterModel
                )
            }

        }
    }
}

@Composable
fun CharacterDetail(
    characterModel: CharacterModel,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier.fillMaxWidth()) {
        val (text, bulletStatus, status, locationTitle, location) = createRefs()
        Text(
            text = characterModel.name,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .constrainAs(text) {
                    linkTo(
                        start = parent.start,
                        startMargin = 8.dp,
                        end = parent.end,
                        endMargin = 8.dp
                    )
                    top.linkTo(
                        parent.top,
                        8.dp
                    )
                    width = Dimension.fillToConstraints
                }
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(
                    if (characterModel.status == "Alive") {
                        Color.Green
                    } else {
                        Color.Red
                    }
                )
                .constrainAs(bulletStatus) {
                    start.linkTo(
                        parent.start,
                        8.dp
                    )
                    top.linkTo(
                        status.top,
                        4.dp
                    )
                    width = Dimension.fillToConstraints
                }
        )
        Text(
            text = "${characterModel.status} - ${characterModel.species}",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier
                .constrainAs(status) {
                    start.linkTo(
                        bulletStatus.end,
                        8.dp
                    )
                    end.linkTo(
                        parent.end,
                        8.dp
                    )
                    top.linkTo(
                        text.bottom,
                        2.dp
                    )
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }
        )
        Text(
            text = "Last known location:",
            color = TextTitleColor,
            fontSize = 14.sp,
            modifier = Modifier
                .constrainAs(locationTitle) {
                    start.linkTo(
                        parent.start,
                        8.dp
                    )
                    end.linkTo(
                        parent.end,
                        8.dp
                    )
                    top.linkTo(
                        status.bottom,
                        4.dp
                    )
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }
        )
        Text(
            text = characterModel.location,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier
                .constrainAs(location) {
                    start.linkTo(
                        parent.start,
                        8.dp
                    )
                    end.linkTo(
                        parent.end,
                        8.dp
                    )
                    top.linkTo(
                        locationTitle.bottom,
                        2.dp
                    )
                    bottom.linkTo(
                        parent.bottom,
                        8.dp
                    )
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }
        )

    }
}