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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.ltu.m7019e.moviedb.v24.R

import com.ltu.m7019e.moviedb.v24.model.Works
import com.ltu.m7019e.moviedb.v24.viewmodel.BooksUIState
import com.ltu.m7019e.moviedb.v24.viewmodel.MovieDBViewModel


@Composable
fun BookListScreenGrid(
    movieDBViewModel: MovieDBViewModel,
    onBookListItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier

) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }
    Column {
        Row {
            Spacer(modifier = Modifier.size(8.dp))
            ExoPlayerView() //Replace with Top Trending Book

        }
        Spacer(modifier = Modifier.size(8.dp))
        Row {
            Text(
                text = "Top Rated Books", //change with API
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More Options")
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                DropdownMenuItem(
                    onClick = {
                        // Set the selected movie list to popular
                        // movieDBViewModel.getPopularMovies()
                        // Set the menu expanded state to false
                        menuExpanded = false

                    },
                    text = {
                        Text("Top Authors")
                    }
                )
                DropdownMenuItem(
                    onClick = {
                        // Set the selected movie list to popular
                        //movieDBViewModel.getTopRatedMovies()
                        // Set the menu expanded state to false
                        menuExpanded = false

                    },
                    text = {
                        Text(stringResource(R.string.top_rated_movies))
                    }
                )
            }
        }
            when (val bookUiState = movieDBViewModel.bookUiState) {
                is BooksUIState.Success -> {
//            Text(text = "Trending Works (this week):")
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),

                        ) {
                        items(bookUiState.works) { work ->
                            BookListGridItemCard(
                                work = work,
                                onBookListItemClicked,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .width(300.dp)
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
fun BookListGridItemCard(
    work: Works,
    onBookListItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = {
            onBookListItemClicked(work.key)
        },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)

    ) {
        Column {
            //Spacer(modifier = modifier.size(10 .dp))
            Row {
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
                            .height(136.dp)
                            .padding(8.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Column {
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
            }
            Row ( horizontalArrangement = Arrangement.Center){
                var isFavourite by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        isFavourite = !isFavourite
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

                Button(
                    onClick = {
                        //onWatchLaterClicked(movie)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFc74848))
                ) {
                    Icon(
                        imageVector = rememberShoppingBag(),
                        contentDescription = "Wishlist Icon"
                    )

                }
            }
        }
    }
}