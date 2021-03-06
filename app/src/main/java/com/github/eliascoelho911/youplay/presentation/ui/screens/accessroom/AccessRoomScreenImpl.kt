package com.github.eliascoelho911.youplay.presentation.ui.screens.accessroom

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.eliascoelho911.youplay.presentation.common.AnimationDurations
import com.github.eliascoelho911.youplay.presentation.ui.base.components.navigate
import com.github.eliascoelho911.youplay.presentation.ui.main.MainActivity
import com.github.eliascoelho911.youplay.presentation.ui.main.slideInHorizontallyTransition
import com.github.eliascoelho911.youplay.presentation.ui.main.slideOutHorizontallyTransition
import com.github.eliascoelho911.youplay.presentation.navigation.Destination
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

fun MainActivity.accessRoomScreenImpl(
    navGraphBuilder: NavGraphBuilder,
    navController: NavHostController,
    onClickAccessWithQrCode: () -> Unit,
) {
    navGraphBuilder.composable(Destination.AccessRoom.baseRoute, enterTransition = { _, _ ->
        slideInHorizontallyTransition()
    }, exitTransition = { _, target ->
        when (target.destination.route) {
            Destination.Home.baseRoute -> slideOutHorizontallyTransition()
            else -> fadeOut(animationSpec = tween(AnimationDurations.medium))
        }
    }, popEnterTransition = { _, _ ->
        slideInHorizontallyTransition()
    }, popExitTransition = { _, target ->
        when (target.destination.route) {
            Destination.Home.baseRoute -> slideOutHorizontallyTransition()
            else -> fadeOut(animationSpec = tween(AnimationDurations.medium))
        }
    }) {
        val viewModel: AccessRoomViewModel by viewModel()

        AccessRoomScreen(roomAccessIsLoading = false,
            onBackPressed = { navController.popBackStack() },
            onClickAccessWithQrCode = onClickAccessWithQrCode,
            onClickAccessWithCodeButton = { roomId ->
                lifecycleScope.launch {
                    runCatching {
                        viewModel.enterTheRoom(roomId)
                    }.onSuccess {
                        navController.navigate(Destination.RoomDetails)
                    }.onFailure {
                        showError(it)
                    }
                }
            })
    }
}