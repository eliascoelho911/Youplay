package com.github.eliascoelho911.youplay.domain.usecases.room

import com.github.eliascoelho911.youplay.domain.entities.ID
import com.github.eliascoelho911.youplay.domain.repositories.RoomRepository

class FetchRoomById(
    private val roomRepository: RoomRepository
) {
    fun invoke(id: ID, observe: Boolean = false) = roomRepository.fetchRoomById(id, observe)
}