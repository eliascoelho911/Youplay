package com.github.eliascoelho911.youplay.domain.usecases.room

import com.github.eliascoelho911.youplay.BaseTest
import com.github.eliascoelho911.youplay.assertIsResourceFailure
import com.github.eliascoelho911.youplay.assertIsResourceSuccess
import com.github.eliascoelho911.youplay.domain.common.room.ObserveRoomById
import com.github.eliascoelho911.youplay.domain.common.session.GetCurrentRoomId
import com.github.eliascoelho911.youplay.domain.entities.Room
import com.github.eliascoelho911.youplay.util.Resource
import com.github.eliascoelho911.youplay.util.lastResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ObserveCurrentRoomTest : BaseTest() {
    @MockK
    private lateinit var observeRoomById: ObserveRoomById

    @MockK
    private lateinit var getCurrentRoomId: GetCurrentRoomId

    @InjectMockKs
    private lateinit var observeCurrentRoom: ObserveCurrentRoom

    @Test
    fun testDeveRetornarASalaAtualQuandoEncontrarOId() {
        val id = "id"
        val room = mockk<Room>()
        coEvery { getCurrentRoomId.get() } returns id
        every { observeRoomById.observe(id) } returns flowOf(Resource.success(room))

        runBlocking {
            assertIsResourceSuccess(observeCurrentRoom.observe().lastResult(), room)
        }
    }

    @Test
    fun testDeveRetornarErroQuandoNaoEncontrarOId() {
        coEvery { getCurrentRoomId.get() } returns null

        runBlocking {
            assertIsResourceFailure(observeCurrentRoom.observe().lastResult(),
                AssertionError::class.java)
        }
    }
}