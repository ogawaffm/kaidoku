package org.velohaven.kaidoku.buffer;

import java.util.ArrayList;
import java.util.List;

/**
 * A mutable text buffer that supports addressing by position (0-based)
 * as well as by line and column (both 1-based).
 *
 * <p>Line endings are represented by {@code \n}. A trailing line without
 * a {@code \n} still counts as a full line.</p>
 */
public class TextBuffer {

    private final StringBuilder buffer;

    // -----------------------------------------------------------------------
    // Construction
    // -----------------------------------------------------------------------

    /** Creates an empty TextBuffer. */
    public TextBuffer() {
        this.buffer = new StringBuilder();
    }

    /** Creates a TextBuffer pre-filled with the given text. */
    public TextBuffer(String initialText) {
        this.buffer = new StringBuilder(initialText == null ? "" : initialText);
    }

    // -----------------------------------------------------------------------
    // Basic metrics
    // -----------------------------------------------------------------------

    /** Returns the number of characters in the buffer. */
    public int length() {
        return buffer.length();
    }

    /** Returns {@code true} if the buffer contains no characters. */
    public boolean isEmpty() {
        return buffer.length() == 0;
    }

    /**
     * Returns the number of lines in the buffer.
     * <ul>
     *   <li>0 if the buffer is empty.</li>
     *   <li>A line without a trailing {@code \n} at the end of the buffer is
     *       still counted as a line.</li>
     * </ul>
     */
    public int getLineCount() {
        if (isEmpty()) return 0;
        // Count '\n' occurrences
        int count = 0;
        for (int i = 0; i < buffer.length(); i++) {
            if (buffer.charAt(i) == '\n') count++;
        }
        // If the buffer does not end with '\n', the last fragment is also a line
        if (buffer.charAt(buffer.length() - 1) != '\n') count++;
        return count;
    }

    // -----------------------------------------------------------------------
    // Internal helpers
    // -----------------------------------------------------------------------

    /**
     * Builds a list of line-start positions (0-based positions in the buffer).
     * Entry {@code i} holds the start position of line {@code i+1}.
     */
    private List<Integer> buildLineStarts() {
        List<Integer> starts = new ArrayList<>();
        if (isEmpty()) return starts;
        starts.add(0);
        for (int i = 0; i < buffer.length(); i++) {
            if (buffer.charAt(i) == '\n' && i + 1 < buffer.length()) {
                starts.add(i + 1);
            }
        }
        return starts;
    }

    /** Validates that {@code lineNo} (1-based) exists and returns its 0-based index. */
    private int requireLineIndex(int lineNo) {
        int lineCount = getLineCount();
        if (lineNo < 1 || lineNo > lineCount) {
            throw new IllegalArgumentException(
                    "Line " + lineNo + " does not exist (buffer has " + lineCount + " line(s)).");
        }
        return lineNo - 1;
    }

    /** Validates that {@code position} (0-based) exists in the buffer. */
    private void requireValidPosition(int position) {
        if (position < 0 || position >= buffer.length()) {
            throw new IllegalArgumentException(
                    "Position " + position + " does not exist (buffer length: " + buffer.length() + ").");
        }
    }

    // -----------------------------------------------------------------------
    // Position ↔ line / column conversion
    // -----------------------------------------------------------------------

    /**
     * Returns the 0-based start position of the given line.
     *
     * @param lineNo 1-based line number
     * @throws IllegalArgumentException if lineNo does not exist
     */
    public int getPosition(int lineNo) {
        requireLineIndex(lineNo); // validates
        return buildLineStarts().get(lineNo - 1);
    }

    /**
     * Returns the 0-based position corresponding to the given line and column.
     *
     * @param lineNo   1-based line number
     * @param columnNo 1-based column number
     * @throws IllegalArgumentException if lineNo or columnNo does not exist
     */
    public int getPosition(int lineNo, int columnNo) {
        requireLineIndex(lineNo);
        int lineStart = buildLineStarts().get(lineNo - 1);
        int lineLen   = getLineLength(lineNo);
        if (columnNo < 1 || columnNo > lineLen) {
            throw new IllegalArgumentException(
                    "Column " + columnNo + " does not exist in line " + lineNo +
                    " (line length: " + lineLen + ").");
        }
        return lineStart + columnNo - 1;
    }

    /**
     * Returns the 1-based line number for the given 0-based position.
     *
     * @param position 0-based character position
     * @throws IllegalArgumentException if position does not exist
     */
    public int getLineNo(int position) {
        requireValidPosition(position);
        List<Integer> starts = buildLineStarts();
        int lineIndex = 0;
        for (int i = 0; i < starts.size(); i++) {
            if (starts.get(i) <= position) lineIndex = i;
            else break;
        }
        return lineIndex + 1; // convert to 1-based
    }

    /**
     * Returns the 1-based column number for the given 0-based position.
     *
     * @param position 0-based character position
     * @throws IllegalArgumentException if position does not exist
     */
    public int getColumnNo(int position) {
        requireValidPosition(position);
        int lineStart = buildLineStarts().get(getLineNo(position) - 1);
        return position - lineStart + 1; // 1-based
    }

    // -----------------------------------------------------------------------
    // Line content / length
    // -----------------------------------------------------------------------

    /**
     * Returns the content of the given line, including the trailing {@code \n}
     * (if present).
     *
     * @param lineNo 1-based line number
     * @throws IllegalArgumentException if lineNo does not exist
     */
    public String getLine(int lineNo) {
        requireLineIndex(lineNo);
        List<Integer> starts = buildLineStarts();
        int start = starts.get(lineNo - 1);
        int end;
        if (lineNo < starts.size()) {
            // next line starts right after the '\n' of the current line
            end = starts.get(lineNo); // this position is after the '\n'
        } else {
            end = buffer.length();
        }
        return buffer.substring(start, end);
    }

    /**
     * Returns the length (in characters) of the given line, including the
     * trailing {@code \n} (if present).
     *
     * @param lineNo 1-based line number
     * @throws IllegalArgumentException if lineNo does not exist
     */
    public int getLineLength(int lineNo) {
        return getLine(lineNo).length();
    }

    // -----------------------------------------------------------------------
    // Mutation – insert
    // -----------------------------------------------------------------------

    /**
     * Inserts {@code object.toString()} (or {@code "null"} if object is
     * {@code null}) at the given 0-based position.
     *
     * @param position 0-based insert position (0 … length, inclusive)
     * @param object   object whose string representation is inserted
     * @return this
     * @throws IllegalArgumentException if position is out of range
     */
    public TextBuffer insertAt(int position, Object object) {
        if (position < 0 || position > buffer.length()) {
            throw new IllegalArgumentException(
                    "Insert position " + position + " is out of range (buffer length: " + buffer.length() + ").");
        }
        buffer.insert(position, object == null ? "null" : object.toString());
        return this;
    }

    /**
     * Inserts {@code object.toString()} (or {@code "null"} if object is
     * {@code null}) at the given 1-based line and column.
     *
     * @param lineNo   1-based line number
     * @param columnNo 1-based column number
     * @param object   object whose string representation is inserted
     * @return this
     * @throws IllegalArgumentException if lineNo or columnNo does not exist
     */
    public TextBuffer insertAt(int lineNo, int columnNo, Object object) {
        return insertAt(getPosition(lineNo, columnNo), object);
    }

    // -----------------------------------------------------------------------
    // Mutation – insert as block
    // -----------------------------------------------------------------------

    /**
     * Inserts {@code object.toString()} (or {@code "null"}) at the given
     * 0-based position.  If the string to insert contains {@code \n},
     * each subsequent line is indented by the same column offset as the
     * insert position within its line.
     *
     * @param position 0-based insert position
     * @param object   object whose string representation is inserted
     * @return this
     * @throws IllegalArgumentException if position is out of range
     */
    public TextBuffer insertAsBlockAt(int position, Object object) {
        if (position < 0 || position > buffer.length()) {
            throw new IllegalArgumentException(
                    "Insert position " + position + " is out of range (buffer length: " + buffer.length() + ").");
        }
        String text   = object == null ? "null" : object.toString();
        int    indent = computeIndent(position);
        String indentStr = " ".repeat(indent);

        // Replace every '\n' (except the last character if it is '\n') with
        // '\n' + indentStr so that subsequent lines start at the same column.
        String expanded = text.replace("\n", "\n" + indentStr);
        buffer.insert(position, expanded);
        return this;
    }

    /**
     * Inserts {@code object.toString()} (or {@code "null"}) at the given
     * 1-based line and column as a block (see
     * {@link #insertAsBlockAt(int, Object)}).
     *
     * @param lineNo   1-based line number
     * @param columnNo 1-based column number
     * @param object   object whose string representation is inserted
     * @return this
     * @throws IllegalArgumentException if lineNo or columnNo does not exist
     */
    public TextBuffer insertAsBlockAt(int lineNo, int columnNo, Object object) {
        return insertAsBlockAt(getPosition(lineNo, columnNo), object);
    }

    /**
     * Computes the column offset (0-based) of a given 0-based buffer position
     * relative to the start of its line.
     */
    private int computeIndent(int position) {
        // Walk backwards to find the preceding '\n'
        for (int i = position - 1; i >= 0; i--) {
            if (buffer.charAt(i) == '\n') {
                return position - i - 1;
            }
        }
        return position; // on the first line
    }

    // -----------------------------------------------------------------------
    // Mutation – append
    // -----------------------------------------------------------------------

    /**
     * Appends {@code object.toString()} (or {@code "null"} if object is
     * {@code null}) to the end of the buffer.
     *
     * @param object object whose string representation is appended
     * @return this
     */
    public TextBuffer append(Object object) {
        buffer.append(object == null ? "null" : object.toString());
        return this;
    }

    // -----------------------------------------------------------------------
    // Object overrides
    // -----------------------------------------------------------------------

    /**
     * Returns the current content of the buffer, or {@code ""} if the buffer
     * has never been written to.
     */
    @Override
    public String toString() {
        return buffer.toString();
    }
}

