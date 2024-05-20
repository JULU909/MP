package com.ltu.m7019e.moviedb.v24.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import com.ltu.m7019e.moviedb.v24.viewmodel.MovieDBViewModel
import com.ltu.m7019e.moviedb.v24.viewmodel.SelectedBookUIState
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore.Video
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp

import coil.compose.AsyncImage
import com.ltu.m7019e.moviedb.v24.R
import com.ltu.m7019e.moviedb.v24.model.Work
import com.ltu.m7019e.moviedb.v24.model.Works
import com.ltu.m7019e.moviedb.v24.utils.Constants
import com.ltu.m7019e.moviedb.v24.viewmodel.SelectedAuthorUiState


@Composable
fun BooksDetailScreen(
    selectedBookUIState: SelectedBookUIState,
    modifier: Modifier,
    movieDBViewModel: MovieDBViewModel,
    selectedAuthorUiState: SelectedAuthorUiState,


    ) {

    when (selectedBookUIState) {
        is SelectedBookUIState.Success -> {



            Column(Modifier.width(IntrinsicSize.Max)) {
                Row(  modifier = Modifier
                    .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.size(18.dp))
                    Box {
                        AsyncImage(
                            model = Constants.COVER_IMAGE_BASE_URL + selectedBookUIState.work.coverImage + Constants.COVER_SIZE_M,
                    //placeholder = painterResource(R.drawable.no_image_placeholder),
                            contentDescription =selectedBookUIState.work.title,
                            modifier = Modifier
                                .width(90.dp)
                                .height(136.dp)
                                .fillMaxWidth(),
                            // .padding(8.dp),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                    Spacer(modifier = Modifier.size(10.dp))

                    Column {
                        Text(
                            text = selectedBookUIState.work.title,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = selectedBookUIState.work.firstPublishYear.toString(),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        if (selectedBookUIState.work.authorName.isNotEmpty()) {
                            Text(
                                text = "by ${selectedBookUIState.work.authorName[0]}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
                //}
               // Spacer(modifier = modifier.size(5.dp))
                Text(
                    text = "Books from the same author",
                    style = MaterialTheme.typography.headlineSmall
                )

            }
        }
        is SelectedBookUIState.Loading -> {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
        is SelectedBookUIState.Error -> {
            Text(
                text = "Error...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    when (selectedAuthorUiState){



        is SelectedAuthorUiState.Success -> {
            Column() {
                Spacer(modifier = Modifier.size(150.dp))

                LazyRow(modifier = modifier
                    .fillMaxSize()
                ) {
                items(selectedAuthorUiState.works) { works ->
                    AuthorBookListItemCard(
                        work = works,
                        modifier = Modifier
                            .padding(12.dp)
                            .width(300.dp),
                        movieDBViewModel = movieDBViewModel
                    )
                }
            }
            }
        }

        SelectedAuthorUiState.Error -> {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
        SelectedAuthorUiState.Loading -> {
            Text(
                text = "Error...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorBookListItemCard(
    work: Work,
    modifier: Modifier = Modifier,
    movieDBViewModel: MovieDBViewModel,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = {

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
                        .height(136.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
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
                    text = work.firstPublishDate.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.size(8.dp))

            }
            Spacer(modifier = Modifier.size(8.dp))

            }
        }

    }



