package com.example.recursion_grid

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [GridViewModel] covering every game rule and edge case.
 */
class GridViewModelTest {

    private lateinit var vm: GridViewModel

    @Before
    fun setUp() {
        vm = GridViewModel()
    }

    // ── Initial state ────────────────────────────────────────────────

    @Test
    fun `initial state has all cells at zero`() {
        val cells = vm.cells.value
        assertEquals(9, cells.size)
        cells.forEach { assertEquals(0, it) }
    }

    // ── Basic click ──────────────────────────────────────────────────

    @Test
    fun `clicking a cell increments it by 1`() {
        vm.onCellClick(0)
        assertEquals(1, vm.cells.value[0])
    }

    // ── Rule A (divisible by 3) ──────────────────────────────────────

    @Test
    fun `rule A - divisible by 3 decrements right neighbor`() {
        // Click cell 0 three times → value becomes 3
        repeat(3) { vm.onCellClick(0) }
        assertEquals(3, vm.cells.value[0])
        // Right neighbor (cell 1) should be decremented by 1
        assertEquals(-1, vm.cells.value[1])
    }

    @Test
    fun `rule A - last column does not ripple right`() {
        // Cell 2 is in the last column (index 2, col=2)
        repeat(3) { vm.onCellClick(2) }
        assertEquals(3, vm.cells.value[2])
        // No right neighbor to affect; next cell (index 3) is in new row, untouched
        assertEquals(0, vm.cells.value[3])
    }

    // ── Rule B (divisible by 5) ──────────────────────────────────────

    @Test
    fun `rule B - divisible by 5 increments below neighbor by 2`() {
        // Click cell 0 five times → value becomes 5
        repeat(5) { vm.onCellClick(0) }
        assertEquals(5, vm.cells.value[0])
        // Below neighbor (cell 3) gets +2
        // Cell 1 was decremented once at click #3 → -1
        assertEquals(-1, vm.cells.value[1])
        assertEquals(2, vm.cells.value[3])
    }

    @Test
    fun `rule B - bottom row does not ripple below`() {
        // Cell 6 is in the bottom row
        repeat(5) { vm.onCellClick(6) }
        assertEquals(5, vm.cells.value[6])
        // No cell below index 6 in the grid; cell 7 only affected if rule A fires
        // Cell 7 should have been decremented at click 3 → -1
        assertEquals(-1, vm.cells.value[7])
    }

    // ── Lock threshold ───────────────────────────────────────────────

    @Test
    fun `cell reaching 15 or more is locked`() {
        assertFalse(vm.isLocked(14))
        assertTrue(vm.isLocked(15))
        assertTrue(vm.isLocked(100))
    }

    @Test
    fun `locked cell ignores clicks`() {
        // Rapidly click cell 4 (center) 15 times to lock it
        repeat(15) { vm.onCellClick(4) }
        val lockedValue = vm.cells.value[4]
        assertTrue(vm.isLocked(lockedValue))
        // One more click should be ignored
        vm.onCellClick(4)
        assertEquals(lockedValue, vm.cells.value[4])
    }

    @Test
    fun `ripple skips locked neighbor`() {
        // Lock cell 1 by clicking it enough times
        repeat(15) { vm.onCellClick(1) }
        val lockVal = vm.cells.value[1]
        assertTrue(vm.isLocked(lockVal))

        // Now reset cell 0 scenario: click cell 0 until value is divisible by 3.
        // Right neighbor is cell 1 (locked) → should not change.
        // Cell 0 is currently 0; click 3 times → value = 3 (div by 3)
        repeat(3) { vm.onCellClick(0) }
        assertEquals(3, vm.cells.value[0])
        // Cell 1 must remain at its locked value
        assertEquals(lockVal, vm.cells.value[1])
    }

    // ── Out-of-bounds guard ──────────────────────────────────────────

    @Test
    fun `out of bounds index is a no-op`() {
        vm.onCellClick(-1)
        vm.onCellClick(9)
        vm.cells.value.forEach { assertEquals(0, it) }
    }

    // ── Zero and negative divisibility guards ────────────────────────

    @Test
    fun `value reaching 0 does not trigger ripple`() {
        // Click cell 0 three times → cell[0]=3, cell[1]=-1 (via Rule A)
        repeat(3) { vm.onCellClick(0) }
        assertEquals(-1, vm.cells.value[1])

        // Click cell 1 once → cell[1] goes from -1 to 0
        // 0 % 3 == 0 in Kotlin, but 0 is NOT a positive multiple → NO ripple
        vm.onCellClick(1)
        assertEquals(0, vm.cells.value[1])
        // cell[2] must remain untouched (no Rule A ripple from 0)
        assertEquals(0, vm.cells.value[2])
        // cell[4] must remain untouched (no Rule B ripple from 0)
        assertEquals(0, vm.cells.value[4])
    }

    @Test
    fun `negative multiple of 3 does not trigger ripple`() {
        // Get cell 1 to -3:
        // Click cell 0 nine times. Rule A fires at clicks 3, 6, 9 → cell[1] -= 1 each time
        // Click 3: cell[0]=3 → cell[1]=-1
        // Click 5: cell[0]=5 → Rule B: cell[3]+=2
        // Click 6: cell[0]=6 → cell[1]=-2
        // Click 9: cell[0]=9 → cell[1]=-3
        repeat(9) { vm.onCellClick(0) }
        assertEquals(-3, vm.cells.value[1])

        // Click cell 1: -3 → -2. -2 is not divisible by 3, no ripple.
        vm.onCellClick(1)
        assertEquals(-2, vm.cells.value[1])
        assertEquals(0, vm.cells.value[2])
    }

    @Test
    fun `full cascade scenario - multi cell interaction`() {
        // Trace: click cell[0] 15 times and track all side effects
        // Click 3: cell[0]=3 → Rule A → cell[1]-=1 → cell[1]=-1
        // Click 5: cell[0]=5 → Rule B → cell[3]+=2 → cell[3]=2
        // Click 6: cell[0]=6 → Rule A → cell[1]-=1 → cell[1]=-2
        // Click 9: cell[0]=9 → Rule A → cell[1]-=1 → cell[1]=-3
        // Click 10: cell[0]=10 → Rule B → cell[3]+=2 → cell[3]=4
        // Click 12: cell[0]=12 → Rule A → cell[1]-=1 → cell[1]=-4
        // Click 15: cell[0]=15 → Rule A → cell[1]-=1 → cell[1]=-5
        //                       → Rule B → cell[3]+=2 → cell[3]=6
        //           cell[0] is now LOCKED (≥15)
        repeat(15) { vm.onCellClick(0) }

        assertEquals(15, vm.cells.value[0])
        assertTrue(vm.isLocked(vm.cells.value[0]))
        assertEquals(-5, vm.cells.value[1])
        assertEquals(6, vm.cells.value[3])

        // cell[0] locked → further clicks ignored
        vm.onCellClick(0)
        assertEquals(15, vm.cells.value[0])
    }
}
