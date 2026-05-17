package org.velohaven.kaidoku.io;

import org.velohaven.kaidoku.model.DigitSet;
import org.velohaven.kaidoku.model.DigitSetFactory;
import org.velohaven.kaidoku.model.DigitSetStorage;
import org.velohaven.kaidoku.model.Board;
import org.velohaven.kaidoku.model.Range;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.velohaven.kaidoku.io.BoardFilenameExtensions.getBoxColumnCountFromFilename;
import static org.velohaven.kaidoku.io.BoardFilenameExtensions.getBoxRowCountFromFilename;
import static org.velohaven.kaidoku.io.BoardFilenameExtensions.hasBoxSizeExtension;
import static org.velohaven.kaidoku.model.DigitConverter.digitValueToSymbol;
import static org.velohaven.kaidoku.model.DigitConverter.isValidSymbol;
import static org.velohaven.kaidoku.model.DigitConverter.symbolToDigitValue;

public class BoardSerializer {

    private String normalizeBoardRowLine(String line, int size) {
        String normalizedLine = line.stripTrailing();
        if (normalizedLine.length() > size) {
            throw new IllegalArgumentException(
                    "Line is too long (is " + normalizedLine.length() + ", limit is " + size + "): " + line
            );
        }
        // assure full row size
        return normalizedLine + " ".repeat(size - normalizedLine.length());
    }

    /**
     * Reads the lines of a Sudoku board from a Reader, ensuring that the number of lines and their lengths
     * match the expected board size.
     * @param reader
     * @param sideLength
     * @return
     * @throws IOException
     */
    private List<String> readBoardLines(Reader reader, int sideLength) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        List<String> lines = new ArrayList<>(sideLength);
        String currentLine;
        int lineNumber = 0;

        try {

            // read until end of file or expected number of lines reached
            while ((currentLine = bufferedReader.readLine()) != null && lines.size() < sideLength) {

                    lineNumber++;
                    // Comment candidate?
                    if (currentLine.contains("#")) {
                        // Illegal use of comment character?
                        if (!currentLine.startsWith("#")) {
                            throw new IllegalArgumentException(
                                    "The comment symbol (#) must appear only at the beginning of a line: " + currentLine
                            );
                        }
                    } else {
                        lines.add(normalizeBoardRowLine(currentLine, sideLength));
                    }

            }

            // assure empty lines for missing last lines (which might is omitted because of its emptiness)
            while (lines.size() < sideLength) {
                lines.add(normalizeBoardRowLine("", sideLength));
            }

        } catch (IOException | IllegalArgumentException e) {
            throw new IOException("Error reading line " + lineNumber, e);
        }

        bufferedReader.close();

        return lines;
    }


    private DigitSetStorage createDigitSetStorage(List<String> lines, int boardSideLength) {

        DigitSetStorage digitSetStorage = new DigitSetStorage(boardSideLength);
        DigitSetFactory digitSetFactory = DigitSetFactory.forMaxSize(boardSideLength);
        DigitSet digitSet;

        for (int row = 0; row < lines.size(); row++) {

            String line = lines.get(row);

            for (int column = 0; column < line.length(); column++){
                char symbol = line.charAt(column);
                if (symbol == ' ') {
                    digitSet = digitSetFactory.allDigits();
                } else {
                    if (!isValidSymbol(symbol, boardSideLength)) {
                        throw new IllegalArgumentException(
                                "Invalid symbol '" + symbol + "' at row " + row + ", column " + column
                        );
                    } else {
                        int digit = symbolToDigitValue(symbol);
                        digitSet = digitSetFactory.singleDigit(digit);
                    }
                }
                digitSetStorage.set(row, column, digitSet);
            }
        }
        return digitSetStorage;
    }

    public Board load(String filename) throws IOException {

        if (!hasBoxSizeExtension(filename)) {
            throw new IllegalArgumentException("Filename must end with a valid box size extension (e.g. .3x3)");
        }

        int boxColumnCount = getBoxColumnCountFromFilename(filename);
        int boxRowCount = getBoxRowCountFromFilename(filename);
        int boardSideLength = boxRowCount * boxColumnCount;

        if (boxColumnCount != 3 || boxRowCount != 3) {
            throw new IllegalArgumentException("Currently only 3x3 box size is supported. Filename must end with .3x3");
        }

        List<String> lines;
        try (Reader reader = new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8)) {
            lines = readBoardLines(reader, boardSideLength);
        } catch (IOException e) {
            throw new IOException("Error reading file: " + filename, e);
        }

        DigitSetStorage digitSetStorage = createDigitSetStorage(lines, boardSideLength);

        return new Board(digitSetStorage, boxRowCount, boxColumnCount);

    }

    public void save(Board board, String filename) throws IOException {

        StringBuilder sb = new StringBuilder(board.getRowCount() * board.getColumnCount());

        for (Range row : board.getRows()) {
            for (Range cell : row.getCells()) {
                if (cell.getDigitSet().isSingle()) {
                    sb.append(digitValueToSymbol(cell.getDigitSet().singleDigit()));
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }

        Files.writeString(Path.of(filename), sb.toString(), StandardCharsets.UTF_8);

    }

}
