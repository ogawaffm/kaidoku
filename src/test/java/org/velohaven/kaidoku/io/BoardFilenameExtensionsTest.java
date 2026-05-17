package org.velohaven.kaidoku.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BoardFilenameExtensionsTest {

    @Test
    void getExtensionShouldReturnEmptyForNull() {
        assertEquals("", BoardFilenameExtensions.getExtension(null));
    }

    @Test
    void getExtensionShouldReturnEmptyForEmptyString() {
        assertEquals("", BoardFilenameExtensions.getExtension(""));
    }

    @Test
    void getExtensionShouldReturnEmptyWhenNoDotExists() {
        assertEquals("", BoardFilenameExtensions.getExtension("board"));
    }

    @Test
    void getExtensionShouldReturnEmptyForHiddenFile() {
        assertEquals("", BoardFilenameExtensions.getExtension(".gitignore"));
    }

    @Test
    void getExtensionShouldReturnEmptyForTrailingDot() {
        assertEquals("", BoardFilenameExtensions.getExtension("board."));
    }

    @Test
    void getExtensionShouldReturnExtension() {
        assertEquals("9x9", BoardFilenameExtensions.getExtension("board.9x9"));
    }

    @Test
    void getExtensionShouldUseOnlyLastPathSegmentUnix() {
        assertEquals("", BoardFilenameExtensions.getExtension("/tmp/dir.with.dots/board"));
    }

    @Test
    void getExtensionShouldUseOnlyLastPathSegmentWindows() {
        assertEquals("3x4", BoardFilenameExtensions.getExtension("C:\\temp\\folder.name\\board.3x4"));
    }

    @Test
    void hasBoxSizeExtensionShouldReturnTrueForLowercaseX() {
        assertTrue(BoardFilenameExtensions.hasBoxSizeExtension("board.3x4"));
    }

    @Test
    void hasBoxSizeExtensionShouldReturnTrueForUppercaseX() {
        assertTrue(BoardFilenameExtensions.hasBoxSizeExtension("board.3X4"));
    }

    @Test
    void hasBoxSizeExtensionShouldReturnFalseWithoutExtension() {
        assertFalse(BoardFilenameExtensions.hasBoxSizeExtension("board"));
    }

    @Test
    void hasBoxSizeExtensionShouldReturnFalseForNonNumericExtension() {
        assertFalse(BoardFilenameExtensions.hasBoxSizeExtension("board.txt"));
    }

    @Test
    void hasBoxSizeExtensionShouldReturnFalseForMissingLeftSide() {
        assertFalse(BoardFilenameExtensions.hasBoxSizeExtension("board.x4"));
    }

    @Test
    void hasBoxSizeExtensionShouldReturnFalseForMissingRightSide() {
        assertFalse(BoardFilenameExtensions.hasBoxSizeExtension("board.4x"));
    }

    @Test
    void hasBoxSizeExtensionShouldReturnFalseForZeroLeftSide() {
        assertFalse(BoardFilenameExtensions.hasBoxSizeExtension("board.0x4"));
    }

    @Test
    void hasBoxSizeExtensionShouldReturnFalseForZeroRightSide() {
        assertFalse(BoardFilenameExtensions.hasBoxSizeExtension("board.4x0"));
    }

    @Test
    void hasBoxSizeExtensionShouldReturnFalseForNegativeLeftSide() {
        assertFalse(BoardFilenameExtensions.hasBoxSizeExtension("board.-3x4"));
    }

    @Test
    void hasBoxSizeExtensionShouldReturnFalseForNegativeRightSide() {
        assertFalse(BoardFilenameExtensions.hasBoxSizeExtension("board.3x-4"));
    }

    @Test
    void hasBoxSizeExtensionShouldReturnFalseForBothSeparators() {
        assertFalse(BoardFilenameExtensions.hasBoxSizeExtension("board.3x4X5"));
    }

    @Test
    void getXSizeByFilenameShouldReturnLeftDimensionForLowercaseBoxX() {
        assertEquals(3, BoardFilenameExtensions.getBoxColumnCountFromFilename("board.3x4"));
    }

    @Test
    void getXSizeByFilenameShouldReturnLeftDimensionForUppercaseBoxX() {
        assertEquals(9, BoardFilenameExtensions.getBoxColumnCountFromFilename("board.9X9"));
    }

    @Test
    void getBoxRowCountFromFilenameShouldReturnRightDimensionForLowercaseX() {
        assertEquals(4, BoardFilenameExtensions.getBoxRowCountFromFilename("board.3x4"));
    }

    @Test
    void getBoxRowCountFromFilenameShouldReturnRightDimensionForUppercaseX() {
        assertEquals(9, BoardFilenameExtensions.getBoxRowCountFromFilename("board.9X9"));
    }

    @Test
    void getBoxColumnCountFromFilenameShouldThrowForMissingExtension() {
        assertThrows(IllegalArgumentException.class,
                () -> BoardFilenameExtensions.getBoxColumnCountFromFilename("board"));
    }

    @Test
    void getBoxRowCountFromFilenameShouldThrowForMissingExtension() {
        assertThrows(IllegalArgumentException.class,
                () -> BoardFilenameExtensions.getBoxRowCountFromFilename("board"));
    }

    @Test
    void getBoxColumnCountFromFilenameShouldThrowForInvalidExtension() {
        assertThrows(IllegalArgumentException.class,
                () -> BoardFilenameExtensions.getBoxColumnCountFromFilename("board.txt"));
    }

    @Test
    void getBoxRowCountFromFilenameShouldThrowForInvalidExtension() {
        assertThrows(IllegalArgumentException.class,
                () -> BoardFilenameExtensions.getBoxRowCountFromFilename("board.txt"));
    }

    @Test
    void getBoxColumnCountFromFilenameShouldThrowForZeroDimension() {
        assertThrows(IllegalArgumentException.class,
                () -> BoardFilenameExtensions.getBoxColumnCountFromFilename("board.0x9"));
    }

    @Test
    void getBoxRowCountFromFilenameShouldThrowForZeroDimension() {
        assertThrows(IllegalArgumentException.class,
                () -> BoardFilenameExtensions.getBoxRowCountFromFilename("board.9x0"));
    }

    @Test
    void getBoxColumnCountFromFilenameShouldThrowForBothSeparators() {
        assertThrows(IllegalArgumentException.class,
                () -> BoardFilenameExtensions.getBoxColumnCountFromFilename("board.3x4X5"));
    }
}