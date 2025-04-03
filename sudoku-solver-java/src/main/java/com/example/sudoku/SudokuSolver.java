package com.example.sudoku;

public class SudokuSolver {

    public boolean solve(int[][] board) {
        if (!isValidBoard(board)) {
            return false;
        }
        return backtrack(board);
    }

    private boolean isValidBoard(int[][] board) {
        for (int i = 0; i < 9; i++) {
            boolean[] rowCheck = new boolean[10];
            boolean[] colCheck = new boolean[10];
            boolean[] boxCheck = new boolean[10];
            for (int j = 0; j < 9; j++) {
                int rowVal = board[i][j];
                int colVal = board[j][i];
                int boxVal = board[3 * (i / 3) + j / 3][3 * (i % 3) + j % 3];

                if (rowVal != 0) {
                    if (rowCheck[rowVal]) return false;
                    rowCheck[rowVal] = true;
                }
                if (colVal != 0) {
                    if (colCheck[colVal]) return false;
                    colCheck[colVal] = true;
                }
                if (boxVal != 0) {
                    if (boxCheck[boxVal]) return false;
                    boxCheck[boxVal] = true;
                }
            }
        }
        return true;
    }

    private boolean backtrack(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num;
                            if (backtrack(board)) return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isSafe(int[][] board, int row, int col, int num) {
        for (int x = 0; x < 9; x++) {
            if (board[row][x] == num || board[x][col] == num)
                return false;
            int boxRow = 3 * (row / 3) + x / 3;
            int boxCol = 3 * (col / 3) + x % 3;
            if (board[boxRow][boxCol] == num)
                return false;
        }
        return true;
    }
}
