package com.github.eliascoelho911.youplay.domain.util.room

import com.github.eliascoelho911.youplay.util.Resource
import com.github.eliascoelho911.youplay.util.lastResult
import com.github.eliascoelho911.youplay.domain.common.room.GetRoomById
import com.github.eliascoelho911.youplay.domain.entities.ID

class CheckIfRoomExistsById(
    private val getRoomById: GetRoomById,
) {
    suspend fun check(id: ID) = getRoomById.get(id).lastResult() is Resource.Success
}

