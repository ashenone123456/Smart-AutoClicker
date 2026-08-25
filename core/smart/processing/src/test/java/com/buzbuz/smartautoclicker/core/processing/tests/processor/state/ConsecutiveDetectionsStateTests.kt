/*
 * Copyright (C) 2026 Smart-AutoClicker contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.buzbuz.smartautoclicker.core.processing.tests.processor.state

import com.buzbuz.smartautoclicker.core.processing.data.processor.state.ConsecutiveDetectionsState

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ConsecutiveDetectionsStateTests {

    @Test
    fun confirmationDisabled_fulfilled_executesImmediately() {
        val state = ConsecutiveDetectionsState()

        assertTrue(state.shouldExecute(1L, confirmationEnabled = false, conditionsFulfilled = true))
    }

    @Test
    fun confirmationEnabled_firstFulfilledFrame_doesNotExecute() {
        val state = ConsecutiveDetectionsState()

        assertFalse(state.shouldExecute(1L, confirmationEnabled = true, conditionsFulfilled = true))
    }

    @Test
    fun confirmationEnabled_secondFulfilledFrame_executes() {
        val state = ConsecutiveDetectionsState()
        state.shouldExecute(1L, confirmationEnabled = true, conditionsFulfilled = true)

        assertTrue(state.shouldExecute(1L, confirmationEnabled = true, conditionsFulfilled = true))
    }

    @Test
    fun unfulfilledFrame_resetsConfirmation() {
        val state = ConsecutiveDetectionsState()
        state.shouldExecute(1L, confirmationEnabled = true, conditionsFulfilled = true)
        state.shouldExecute(1L, confirmationEnabled = true, conditionsFulfilled = false)

        assertFalse(state.shouldExecute(1L, confirmationEnabled = true, conditionsFulfilled = true))
    }

    @Test
    fun executedEvent_requiresTwoNewFrames() {
        val state = ConsecutiveDetectionsState()
        state.shouldExecute(1L, confirmationEnabled = true, conditionsFulfilled = true)
        state.shouldExecute(1L, confirmationEnabled = true, conditionsFulfilled = true)

        assertFalse(state.shouldExecute(1L, confirmationEnabled = true, conditionsFulfilled = true))
    }

    @Test
    fun events_areTrackedIndependently() {
        val state = ConsecutiveDetectionsState()
        state.shouldExecute(1L, confirmationEnabled = true, conditionsFulfilled = true)

        assertFalse(state.shouldExecute(2L, confirmationEnabled = true, conditionsFulfilled = true))
        assertTrue(state.shouldExecute(1L, confirmationEnabled = true, conditionsFulfilled = true))
    }
}
