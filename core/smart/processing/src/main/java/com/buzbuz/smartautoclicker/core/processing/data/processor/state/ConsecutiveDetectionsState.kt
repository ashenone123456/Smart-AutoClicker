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
package com.buzbuz.smartautoclicker.core.processing.data.processor.state

private const val DEFAULT_REQUIRED_FRAMES = 2

/** Confirms a screen event over consecutive frames before it can execute. */
internal class ConsecutiveDetectionsState(
    private val requiredFrames: Int = DEFAULT_REQUIRED_FRAMES,
) {

    private val fulfilledFramesByEventId: MutableMap<Long, Int> = mutableMapOf()

    init {
        require(requiredFrames > 0)
    }

    fun shouldExecute(
        eventId: Long,
        confirmationEnabled: Boolean,
        conditionsFulfilled: Boolean,
    ): Boolean {
        if (!confirmationEnabled) {
            reset(eventId)
            return conditionsFulfilled
        }

        if (!conditionsFulfilled) {
            reset(eventId)
            return false
        }

        val fulfilledFrames = (fulfilledFramesByEventId[eventId] ?: 0) + 1
        if (fulfilledFrames < requiredFrames) {
            fulfilledFramesByEventId[eventId] = fulfilledFrames
            return false
        }

        reset(eventId)
        return true
    }

    fun reset(eventId: Long) {
        fulfilledFramesByEventId.remove(eventId)
    }
}
