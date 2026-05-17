package org.velohaven;

import org.velohaven.kaidoku.io.BoardSerializer;
import org.velohaven.kaidoku.model.Board;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        BoardSerializer boardSerializer = new BoardSerializer();
        Board board;
        Board board2;
        try {
            board = boardSerializer.load("src/main/resources/sudoku.3x3");
            boardSerializer.save(board, "src/main/resources/sudoku2.3x3");
            board2 = boardSerializer.load("src/main/resources/sudoku2.3x3");
            System.out.println(board);
            System.out.println(board.equals(board2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}