package com.ltu.m7019e.moviedb.v24.ui.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ltu.m7019e.moviedb.v24.utils.Constants

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

import com.ltu.m7019e.moviedb.v24.model.Works
import com.ltu.m7019e.moviedb.v24.viewmodel.BooksUIState
import com.ltu.m7019e.moviedb.v24.viewmodel.MovieDBViewModel


@Composable
fun BookListScreen(
    movieDBViewModel: MovieDBViewModel,
    onBookListItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier,

) {
    Column {
        Row {
            Spacer(modifier = Modifier.size(8.dp))
            ExoPlayerView() //Replace with Top Trending Book

        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "Top Rated Books", //change with API
            style = MaterialTheme.typography.headlineSmall
        )
            when (val bookUiState = movieDBViewModel.bookUiState) {
                is BooksUIState.Success -> {
//            Text(text = "Trending Works (this week):")
                    LazyRow(modifier = modifier) {
                        items(bookUiState.works) { work ->
                            BookListItemCard(
                                work = work,
                                onBookListItemClicked,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .width(300.dp),
                                movieDBViewModel = movieDBViewModel
                            )
                        }
                    }
                }


                BooksUIState.Error -> {
                    Text(
                        text = "Error: Something went wrong!",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                BooksUIState.Loading -> {
                    Text(
                        text = "Loading...",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "Classic Books", //change with API
            style = MaterialTheme.typography.headlineSmall
        )
        when (val bookUiState = movieDBViewModel.bookUiState) {
            is BooksUIState.Success -> {
//            Text(text = "Trending Works (this week):")
                LazyRow(modifier = modifier) {
                    items(bookUiState.classicWorks) { work ->
                        BookListItemCard(
                            work = work,
                            onBookListItemClicked,
                            modifier = Modifier
                                .padding(12.dp)
                                .width(300.dp),
                            movieDBViewModel = movieDBViewModel
                        )
                    }
                }
            }


            BooksUIState.Error -> {
                Text(
                    text = "Error: Something went wrong!",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }

            BooksUIState.Loading -> {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListItemCard(
    work: Works,
    onBookListItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    movieDBViewModel: MovieDBViewModel,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            movieDBViewModel.selectWork(work)
            println("Author : " + work.author_key[0])
            movieDBViewModel.setSelectedAuthor(work.author_key[0].toString())
            onBookListItemClicked(work.key)
        },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .wrapContentHeight()
        ) {
            Box {
                Log.d(
                    "IMAGE URL",
                    Constants.COVER_IMAGE_BASE_URL + work.coverImage + Constants.COVER_SIZE_M
                )
                AsyncImage(
                    model = Constants.COVER_IMAGE_BASE_URL + work.coverImage + Constants.COVER_SIZE_M,
//                    placeholder = painterResource(R.drawable.no_image_placeholder),
                    contentDescription = work.title,
                    modifier = Modifier
                        .width(90.dp)
                        .height(136.dp),
                    // .padding(8.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = work.title,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = work.firstPublishYear.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.size(8.dp))
                if (work.authorName.isNotEmpty()) {
                    Text(
                        text = "by ${work.authorName[0]}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                var isFavourite by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        isFavourite = !isFavourite
                        movieDBViewModel.addFavourite(work)
                        //  onMovieFavouriteItemClicked(movie)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFc74848))
                ) {
                    val icon =
                        if (isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                    Icon(
                        imageVector = icon,
                        contentDescription = "Favourite Icon"
                    )
                }


                }
            }
        }

    }



//OptIn(UnstableApi::class)
@Composable
fun ExoPlayerView() {
    // Get the current context
    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = ExoPlayer.Builder(context).build()

    // Create a MediaSource
    val mediaSource = remember(Constants.EXAMPLE_VIDEO_URI) {
        MediaItem.fromUri(Constants.EXAMPLE_VIDEO_URI)
    }

    // Set MediaSource to ExoPlayer
    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }

    // Manage lifecycle events
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Use AndroidView to embed an Android View (PlayerView) into Compose
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Set your desired height
    )

}



@Composable
fun rememberShoppingBag(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "shopping_bag",
            defaultWidth = 25.0.dp,
            defaultHeight = 25.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9.542f, 36.583f)
                quadToRelative(-1.042f, 0f, -1.834f, -0.791f)
                quadToRelative(-0.791f, -0.792f, -0.791f, -1.834f)
                verticalLineTo(13.042f)
                quadToRelative(0f, -1.042f, 0.791f, -1.854f)
                quadToRelative(0.792f, -0.813f, 1.834f, -0.813f)
                horizontalLineToRelative(4.166f)
                verticalLineToRelative(-0.25f)
                quadToRelative(0f, -2.625f, 1.834f, -4.479f)
                quadTo(17.375f, 3.792f, 20f, 3.792f)
                reflectiveQuadToRelative(4.479f, 1.854f)
                quadToRelative(1.854f, 1.854f, 1.854f, 4.479f)
                verticalLineToRelative(0.25f)
                horizontalLineToRelative(4.125f)
                quadToRelative(1.042f, 0f, 1.854f, 0.813f)
                quadToRelative(0.813f, 0.812f, 0.813f, 1.854f)
                verticalLineToRelative(20.916f)
                quadToRelative(0f, 1.042f, -0.813f, 1.834f)
                quadToRelative(-0.812f, 0.791f, -1.854f, 0.791f)
                close()
                moveToRelative(6.791f, -26.458f)
                verticalLineToRelative(0.25f)
                horizontalLineToRelative(7.375f)
                verticalLineToRelative(-0.25f)
                quadToRelative(0f, -1.542f, -1.083f, -2.604f)
                quadTo(21.542f, 6.458f, 20f, 6.458f)
                reflectiveQuadToRelative(-2.604f, 1.063f)
                quadToRelative(-1.063f, 1.062f, -1.063f, 2.604f)
                close()
                moveTo(9.542f, 33.958f)
                horizontalLineToRelative(20.916f)
                verticalLineTo(13.042f)
                horizontalLineToRelative(-4.125f)
                verticalLineToRelative(3.666f)
                quadToRelative(0f, 0.584f, -0.395f, 0.959f)
                quadToRelative(-0.396f, 0.375f, -0.938f, 0.375f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.959f)
                verticalLineToRelative(-3.666f)
                horizontalLineToRelative(-7.375f)
                verticalLineToRelative(3.666f)
                quadToRelative(0f, 0.584f, -0.395f, 0.959f)
                quadToRelative(-0.396f, 0.375f, -0.938f, 0.375f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.959f)
                verticalLineToRelative(-3.666f)
                horizontalLineTo(9.542f)
                verticalLineToRelative(20.916f)
                close()
                moveToRelative(0f, 0f)
                verticalLineTo(13.042f)
                verticalLineToRelative(20.916f)
                close()
            }
        }.build()
    }
}