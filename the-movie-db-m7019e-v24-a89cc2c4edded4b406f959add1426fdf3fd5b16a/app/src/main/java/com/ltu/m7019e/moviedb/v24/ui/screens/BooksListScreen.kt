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

import com.ltu.m7019e.moviedb.v24.model.Works
import com.ltu.m7019e.moviedb.v24.viewmodel.BooksUIState
import com.ltu.m7019e.moviedb.v24.viewmodel.MovieDBViewModel


@Composable
fun BookListScreen(
    movieDBViewModel: MovieDBViewModel,
    onBookListItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when(val bookUiState = movieDBViewModel.bookUiState) {
        is BooksUIState.Success -> {
//            Text(text = "Trending Works (this week):")
            LazyColumn(modifier = modifier) {
                items(bookUiState.works) { work ->
                    BookListItemCard(
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
            )        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListItemCard(
    work: Works,
    onBookListItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = {
            onBookListItemClicked(work.key)
        }
    ) {
        Row  {
            Box {
                Log.d("IMAGE URL", Constants.COVER_IMAGE_BASE_URL + work.coverImage + Constants.COVER_SIZE_M)
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
                if (work.authorName.isNotEmpty()){
                    Text(
                        text = "by ${work.authorName[0]}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}



