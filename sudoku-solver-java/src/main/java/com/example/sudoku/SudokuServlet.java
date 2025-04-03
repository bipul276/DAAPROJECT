package com.example.sudoku;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import com.google.gson.*;

@WebServlet("/solve")
public class SudokuServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final SudokuSolver solver = new SudokuSolver();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (BufferedReader reader = request.getReader()) {
            JsonObject jsonRequest = gson.fromJson(reader, JsonObject.class);
            JsonArray inputBoard = jsonRequest.getAsJsonArray("board");

            int[][] board = new int[9][9];
            for (int i = 0; i < 9; i++) {
                JsonArray row = inputBoard.get(i).getAsJsonArray();
                for (int j = 0; j < 9; j++) {
                    board[i][j] = row.get(j).getAsInt();
                }
            }

            JsonObject result = new JsonObject();
            if (solver.solve(board)) {
                JsonArray solvedBoard = new JsonArray();
                for (int[] row : board) {
                    JsonArray r = new JsonArray();
                    for (int n : row) r.add(n);
                    solvedBoard.add(r);
                }
                result.add("solution", solvedBoard);
            } else {
                result.addProperty("error", "No solution exists");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            response.getWriter().write(gson.toJson(result));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
