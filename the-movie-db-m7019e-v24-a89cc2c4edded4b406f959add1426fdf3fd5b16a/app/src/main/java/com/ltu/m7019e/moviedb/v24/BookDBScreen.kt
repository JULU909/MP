package com.ltu.m7019e.moviedb.v24

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.ltu.m7019e.moviedb.v24.viewmodel.MovieDBViewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.ltu.m7019e.moviedb.v24.ui.screens.BookListScreen
import com.ltu.m7019e.moviedb.v24.ui.screens.BooksDetailScreen
import com.ltu.m7019e.moviedb.v24.ui.screens.FavouriteMoviesScreen

//import com.ltu.m7019e.moviedb.v24.ui.screens.BookListScreenGrid


enum class MovieDBScreen(@StringRes val title: Int) {
    List(title = R.string.app_name),
    Detail(title = R.string.movie_detail),
    Favourite(title = R.string.favourite_page),
    Grid(title = R.string.Grid),
    Review(title = R.string.Review_page),
    WatchLater(title = R.string.Watch_later),
}


/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDBAppBar(
    currentScreen: MovieDBScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    toGrid: () -> Unit,
    toList: () -> Unit,
    goToFavourites: () -> Unit,



) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(currentScreen.title))

                Spacer(modifier = modifier.size(2.dp))

            Icon(
                imageVector = rememberLibraryBooks(),
                contentDescription = "Icon"
            )
        }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        } ,
        actions = {
            Button(
                onClick = {goToFavourites()},
                modifier = Modifier
                    .padding(top = 5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFc74848))
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favourite Icon"
                )
            }
            if (currentScreen == MovieDBScreen.List) {
                Button(
                    onClick = { toGrid() },
                    modifier = Modifier
                        .padding(top = 5.dp, start = 5.dp)
                    //colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Icon(
                        imageVector = rememberGridView(), //i cant find an icon for grid..
                        contentDescription = "Grid Icon"
                    )
                }
            } else if (currentScreen == MovieDBScreen.Grid) {
                Button(
                    onClick = { toList() },
                    modifier = Modifier
                        .padding(top = 5.dp, start = 5.dp)
                    // colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Icon(
                        imageVector = Icons.Filled.List,
                        contentDescription = "List Icon"
                    )
                }
            }

        })
}

@Composable
fun MovieDBApp(
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = MovieDBScreen.valueOf(
        backStackEntry?.destination?.route ?: MovieDBScreen.List.name
    )


    Scaffold(
        topBar = {
            BookDBAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() } ,
                toGrid = {
                    navController.navigate(MovieDBScreen.Grid.name)

                },
                toList = {
                    navController.navigate(MovieDBScreen.List.name)
                },
                goToFavourites = {
                    navController.navigate(MovieDBScreen.Favourite.name)
                },


            )
        }
    ) { innerPadding ->
        val movieDBViewModel: MovieDBViewModel = viewModel(factory = MovieDBViewModel.Factory)

        NavHost(
            navController = navController,
            startDestination = MovieDBScreen.List.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = MovieDBScreen.List.name) {

              BookListScreen(movieDBViewModel = movieDBViewModel, onBookListItemClicked = {
                  navController.navigate(MovieDBScreen.Detail.name)} )


            }
            composable(route = MovieDBScreen.Grid.name) {
//                BookListScreenGrid(movieDBViewModel = movieDBViewModel, onBookListItemClicked = {})
            }

            composable(route = MovieDBScreen.Detail.name) {
                BooksDetailScreen(selectedBookUIState = movieDBViewModel.selectedBookUIState, modifier =Modifier
                    .fillMaxSize()
                    .padding(16.dp) , movieDBViewModel = movieDBViewModel, selectedAuthorUiState = movieDBViewModel.selectedAuthorUiState )
            }
            composable(route = MovieDBScreen.Favourite.name) {
                FavouriteMoviesScreen(favouriteBookStateUI = movieDBViewModel.favouriteBookStateUI, movieDBViewModel = movieDBViewModel)
            }






        }
    }
}


@Composable
fun rememberLibraryBooks(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "library_books",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
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
                moveTo(16.208f, 18.667f)
                horizontalLineToRelative(12.875f)
                quadToRelative(0.542f, 0f, 0.938f, -0.375f)
                quadToRelative(0.396f, -0.375f, 0.396f, -0.917f)
                quadToRelative(0f, -0.583f, -0.396f, -0.958f)
                reflectiveQuadToRelative(-0.938f, -0.375f)
                horizontalLineTo(16.208f)
                quadToRelative(-0.541f, 0f, -0.916f, 0.375f)
                reflectiveQuadToRelative(-0.375f, 0.958f)
                quadToRelative(0f, 0.542f, 0.375f, 0.917f)
                reflectiveQuadToRelative(0.916f, 0.375f)
                close()
                moveToRelative(0f, 3.958f)
                horizontalLineToRelative(6.167f)
                quadToRelative(0.5f, 0f, 0.896f, -0.375f)
                reflectiveQuadToRelative(0.396f, -0.917f)
                quadToRelative(0f, -0.583f, -0.396f, -0.958f)
                reflectiveQuadTo(22.375f, 20f)
                horizontalLineToRelative(-6.167f)
                quadToRelative(-0.541f, 0f, -0.916f, 0.396f)
                reflectiveQuadToRelative(-0.375f, 0.937f)
                quadToRelative(0f, 0.542f, 0.375f, 0.917f)
                reflectiveQuadToRelative(0.916f, 0.375f)
                close()
                moveToRelative(0f, -7.917f)
                horizontalLineToRelative(12.875f)
                quadToRelative(0.542f, 0f, 0.938f, -0.375f)
                quadToRelative(0.396f, -0.375f, 0.396f, -0.916f)
                quadToRelative(0f, -0.584f, -0.396f, -0.959f)
                reflectiveQuadToRelative(-0.938f, -0.375f)
                horizontalLineTo(16.208f)
                quadToRelative(-0.541f, 0f, -0.916f, 0.396f)
                reflectiveQuadToRelative(-0.375f, 0.938f)
                quadToRelative(0f, 0.541f, 0.375f, 0.916f)
                reflectiveQuadToRelative(0.916f, 0.375f)
                close()
                moveToRelative(-4.666f, 16.417f)
                quadToRelative(-1.084f, 0f, -1.875f, -0.792f)
                quadToRelative(-0.792f, -0.791f, -0.792f, -1.875f)
                verticalLineTo(6.25f)
                quadToRelative(0f, -1.083f, 0.792f, -1.854f)
                quadToRelative(0.791f, -0.771f, 1.875f, -0.771f)
                horizontalLineTo(33.75f)
                quadToRelative(1.083f, 0f, 1.854f, 0.771f)
                quadToRelative(0.771f, 0.771f, 0.771f, 1.854f)
                verticalLineToRelative(22.208f)
                quadToRelative(0f, 1.084f, -0.771f, 1.875f)
                quadToRelative(-0.771f, 0.792f, -1.854f, 0.792f)
                close()
                moveToRelative(0f, -2.667f)
                horizontalLineTo(33.75f)
                verticalLineTo(6.25f)
                horizontalLineTo(11.542f)
                verticalLineToRelative(22.208f)
                close()
                moveTo(6.25f, 36.375f)
                quadToRelative(-1.083f, 0f, -1.854f, -0.771f)
                quadToRelative(-0.771f, -0.771f, -0.771f, -1.854f)
                verticalLineTo(10.208f)
                quadToRelative(0f, -0.541f, 0.375f, -0.937f)
                reflectiveQuadToRelative(0.917f, -0.396f)
                quadToRelative(0.583f, 0f, 0.958f, 0.396f)
                reflectiveQuadToRelative(0.375f, 0.937f)
                verticalLineTo(33.75f)
                horizontalLineToRelative(23.542f)
                quadToRelative(0.541f, 0f, 0.937f, 0.396f)
                reflectiveQuadToRelative(0.396f, 0.937f)
                quadToRelative(0f, 0.542f, -0.396f, 0.917f)
                reflectiveQuadToRelative(-0.937f, 0.375f)
                close()
                moveTo(11.542f, 6.25f)
                verticalLineToRelative(22.208f)
                verticalLineTo(6.25f)
                close()
            }
        }.build()
    }
}


@Composable
fun rememberGridView(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "grid_view",
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
                moveTo(7.875f, 18.667f)
                quadToRelative(-1.083f, 0f, -1.854f, -0.771f)
                quadToRelative(-0.771f, -0.771f, -0.771f, -1.854f)
                verticalLineTo(7.875f)
                quadToRelative(0f, -1.083f, 0.771f, -1.854f)
                quadToRelative(0.771f, -0.771f, 1.854f, -0.771f)
                horizontalLineToRelative(8.167f)
                quadToRelative(1.083f, 0f, 1.875f, 0.771f)
                quadToRelative(0.791f, 0.771f, 0.791f, 1.854f)
                verticalLineToRelative(8.167f)
                quadToRelative(0f, 1.083f, -0.791f, 1.854f)
                quadToRelative(-0.792f, 0.771f, -1.875f, 0.771f)
                close()
                moveToRelative(0f, 16.083f)
                quadToRelative(-1.083f, 0f, -1.854f, -0.771f)
                quadToRelative(-0.771f, -0.771f, -0.771f, -1.854f)
                verticalLineToRelative(-8.167f)
                quadToRelative(0f, -1.083f, 0.771f, -1.875f)
                quadToRelative(0.771f, -0.791f, 1.854f, -0.791f)
                horizontalLineToRelative(8.167f)
                quadToRelative(1.083f, 0f, 1.875f, 0.791f)
                quadToRelative(0.791f, 0.792f, 0.791f, 1.875f)
                verticalLineToRelative(8.167f)
                quadToRelative(0f, 1.083f, -0.791f, 1.854f)
                quadToRelative(-0.792f, 0.771f, -1.875f, 0.771f)
                close()
                moveToRelative(16.083f, -16.083f)
                quadToRelative(-1.083f, 0f, -1.854f, -0.771f)
                quadToRelative(-0.771f, -0.771f, -0.771f, -1.854f)
                verticalLineTo(7.875f)
                quadToRelative(0f, -1.083f, 0.771f, -1.854f)
                quadToRelative(0.771f, -0.771f, 1.854f, -0.771f)
                horizontalLineToRelative(8.167f)
                quadToRelative(1.083f, 0f, 1.854f, 0.771f)
                quadToRelative(0.771f, 0.771f, 0.771f, 1.854f)
                verticalLineToRelative(8.167f)
                quadToRelative(0f, 1.083f, -0.771f, 1.854f)
                quadToRelative(-0.771f, 0.771f, -1.854f, 0.771f)
                close()
                moveToRelative(0f, 16.083f)
                quadToRelative(-1.083f, 0f, -1.854f, -0.771f)
                quadToRelative(-0.771f, -0.771f, -0.771f, -1.854f)
                verticalLineToRelative(-8.167f)
                quadToRelative(0f, -1.083f, 0.771f, -1.875f)
                quadToRelative(0.771f, -0.791f, 1.854f, -0.791f)
                horizontalLineToRelative(8.167f)
                quadToRelative(1.083f, 0f, 1.854f, 0.791f)
                quadToRelative(0.771f, 0.792f, 0.771f, 1.875f)
                verticalLineToRelative(8.167f)
                quadToRelative(0f, 1.083f, -0.771f, 1.854f)
                quadToRelative(-0.771f, 0.771f, -1.854f, 0.771f)
                close()
                moveTo(7.875f, 16.042f)
                horizontalLineToRelative(8.167f)
                verticalLineTo(7.875f)
                horizontalLineTo(7.875f)
                close()
                moveToRelative(16.083f, 0f)
                horizontalLineToRelative(8.167f)
                verticalLineTo(7.875f)
                horizontalLineToRelative(-8.167f)
                close()
                moveToRelative(0f, 16.083f)
                horizontalLineToRelative(8.167f)
                verticalLineToRelative(-8.167f)
                horizontalLineToRelative(-8.167f)
                close()
                moveToRelative(-16.083f, 0f)
                horizontalLineToRelative(8.167f)
                verticalLineToRelative(-8.167f)
                horizontalLineTo(7.875f)
                close()
                moveToRelative(16.083f, -16.083f)
                close()
                moveToRelative(0f, 7.916f)
                close()
                moveToRelative(-7.916f, 0f)
                close()
                moveToRelative(0f, -7.916f)
                close()
            }
        }.build()
    }
}

