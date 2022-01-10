package com.github.eliascoelho911.youplay.presentation.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.eliascoelho911.youplay.R
import com.github.eliascoelho911.youplay.common.observeResource
import com.github.eliascoelho911.youplay.presentation.navigation.Destination
import com.github.eliascoelho911.youplay.presentation.screens.createroom.CreateRoomScreen
import com.github.eliascoelho911.youplay.presentation.screens.createroom.CreateRoomViewModel
import com.github.eliascoelho911.youplay.presentation.screens.roomdetails.RoomDetailsScreen
import com.github.eliascoelho911.youplay.presentation.screens.roomdetails.RoomDetailsViewModel
import com.github.eliascoelho911.youplay.presentation.theme.YouPlayTheme
import com.github.eliascoelho911.youplay.presentation.util.navigate
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()
    private val spotifyAuthorizationRequest = SpotifyAuthorizationRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showContent()
        ensureUserAuthentication()
    }

    private fun ensureUserAuthentication() {
        viewModel.userIsAuthenticatedOnSpotify.observeResource(this) {
            onSuccess { userIsAuthenticatedOnSpotify ->
                if (!userIsAuthenticatedOnSpotify) {
                    requestUserAuthentication()
                }
            }
        }
    }

    private fun showContent() {
        setContent {
            YouPlayTheme {
                val navController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = navController,
                    startDestination = Destination.CreateRoom.baseRoute
                ) {
                    createRoomScreen(navController)
                    roomDetailsScreen(navController)
                }
            }
        }
    }

    private fun NavGraphBuilder.createRoomScreen(navController: NavHostController) {
        composable(Destination.CreateRoom.baseRoute) {
            val viewModel: CreateRoomViewModel by viewModel()

            CreateRoomScreen(viewModel = viewModel,
                onClickToCreateRoom = {
                    lifecycleScope.launch {
                        runCatching {
                            viewModel.createNewRoom()
                        }.onSuccess {
                            navController.navigate(Destination.RoomDetails)
                        }.onFailure {
                            showError(getString(R.string.error_create_new_room))
                        }
                    }
                },
                onClickToEnterRoom = { /*TODO*/ })
        }
    }

    private fun showError(message: String) {
        //TODO Mostrar o erro de uma forma mais bonita
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun NavGraphBuilder.roomDetailsScreen(navController: NavHostController) {
        composable(Destination.RoomDetails.baseRoute) {
            val viewModel: RoomDetailsViewModel by viewModel()

            RoomDetailsScreen(viewModel = viewModel,
                backgroundColor = Color.Blue,
                onConfirmExitFromRoom = {
                    viewModel.userExitFromRoom()
                    navController.popBackStack()
                },
                onClickOptions = {},
                onUpdateRoomName = {},
                onClickSkipToNextMusicButton = {},
                onClickPlayOrPauseButton = {},
                onClickSkipToPreviousMusicButton = {},
                onClickShuffleButton = {},
                onClickRepeatButton = {},
                onTimeChange = {})
        }
    }

    private fun requestUserAuthentication() {
        Intent(
            Intent.ACTION_VIEW,
            spotifyAuthorizationRequest.authorizationRequestUri
        ).run { startActivity(this) }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.data?.let { data ->
            spotifyAuthorizationRequest.getResult(data)?.let { code ->
                viewModel.authenticateUserOnSpotify(code)
            }
        }
    }
}