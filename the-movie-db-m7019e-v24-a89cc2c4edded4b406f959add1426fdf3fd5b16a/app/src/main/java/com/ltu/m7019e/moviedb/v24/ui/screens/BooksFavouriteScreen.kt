package com.ltu.m7019e.moviedb.v24.ui.screens
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ltu.m7019e.moviedb.v24.model.Works
import com.ltu.m7019e.moviedb.v24.utils.Constants
import com.ltu.m7019e.moviedb.v24.viewmodel.FavouriteBookStateUI
import com.ltu.m7019e.moviedb.v24.viewmodel.MovieDBViewModel


@Composable
fun FavouriteMoviesScreen(movieDBViewModel: MovieDBViewModel, favouriteBookStateUI: FavouriteBookStateUI, modifier: Modifier = Modifier) {

    LazyColumn(modifier = modifier) {
        when (favouriteBookStateUI) {
            is FavouriteBookStateUI.Success -> {
                items(favouriteBookStateUI.favWork) { work ->
                    FavouriteListItemCard(
                        work = work,
                        modifier = Modifier.padding(8.dp),
                        movieDBViewModel = movieDBViewModel
                    )
                }
            }

            is FavouriteBookStateUI.Loading -> {

                item {
                    Text(
                        text = "No current favourites",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            is FavouriteBookStateUI.Error -> {
                item {
                    Text(
                        text = "Error: Something went wrong!",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteListItemCard(
        work: Works,
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
            }

        }}