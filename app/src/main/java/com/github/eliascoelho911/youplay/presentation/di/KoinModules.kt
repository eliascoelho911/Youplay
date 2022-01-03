package com.github.eliascoelho911.youplay.presentation.di

import com.github.eliascoelho911.youplay.presentation.main.MainViewModel
import com.github.eliascoelho911.youplay.presentation.screens.createroom.CreateRoomViewModel
import com.github.eliascoelho911.youplay.presentation.screens.roomdetails.RoomDetailsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.lang.ref.WeakReference

val viewModelModule = module {
    viewModel {
        CreateRoomViewModel(getLoggedUser = get(),
            createNewRoom = get(),
            putCurrentRoomId = get(),
            context = WeakReference(get()))
    }
    viewModel {
        RoomDetailsViewModel(
            getCurrentMusic = get(),
            getCurrentRoom = get()
        )
    }
    viewModel {
        MainViewModel(
            userIsAuthenticatedOnSpotify = get(),
            coroutineContext = Dispatchers.IO,
            authenticateUserOnSpotify = get()
        )
    }
}