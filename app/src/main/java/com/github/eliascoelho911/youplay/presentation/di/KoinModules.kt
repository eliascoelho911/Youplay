package com.github.eliascoelho911.youplay.presentation.di

import com.github.eliascoelho911.youplay.presentation.main.MainViewModel
import com.github.eliascoelho911.youplay.presentation.screens.createroom.CreateRoomViewModel
import com.github.eliascoelho911.youplay.presentation.screens.roomdetails.RoomDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.lang.ref.WeakReference

val viewModelModule = module {
    viewModel {
        CreateRoomViewModel(get(), get(), context = WeakReference(get()), get())
    }
    viewModel {
        RoomDetailsViewModel(get(), get(), get(), get())
    }
    viewModel {
        MainViewModel(get(), get())
    }
}