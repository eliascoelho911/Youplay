package com.github.eliascoelho911.youplay.domain.usecases.room

import com.github.eliascoelho911.youplay.global.Messages
import com.github.eliascoelho911.youplay.global.catchExceptions
import com.github.eliascoelho911.youplay.global.flowResource
import com.github.eliascoelho911.youplay.domain.common.room.GetRoomById
import com.github.eliascoelho911.youplay.domain.common.session.GetCurrentRoomId
import com.github.eliascoelho911.youplay.domain.entities.Room
import com.github.eliascoelho911.youplay.domain.util.runChangingExceptionMessage
import kotlinx.coroutines.flow.emitAll

class GetCurrentRoom(
    private val getRoomById: GetRoomById,
    private val getCurrentRoomId: GetCurrentRoomId,
    private val errorMessages: Messages.Error,
) {
    fun get() = flowResource<Room> {
        runChangingExceptionMessage(message = errorMessages.default) {
            val currentRoomId = getCurrentRoomId.get()

            assert(currentRoomId != null)

            emitAll(getRoomById.get(currentRoomId!!))
        }
    }.catchExceptions()
}