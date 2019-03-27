package com.joelatdeluxe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

public class Pure {

    public static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public static void writeFile(String filepath, String content) throws IOException {
        Files.write(Paths.get(filepath), content.getBytes());
    }

    public static void writeResFile(String filepath, String content) throws IOException {
        writeFile("res/" + filepath, content);
    }

    public static String readResFile(String path) throws IOException {
        return readFile("res/" + path);
    }

    public static <T> void initGrid(T[][] grid, T value) {
        for (var colIndex = 0; colIndex < grid.length; colIndex++) {
            for (var rowIndex = 0; rowIndex < grid[colIndex].length; rowIndex++) {
                grid[colIndex][rowIndex] = value;
            }
        }
    }

    public static <T> void printGrid(T[][] grid, Function<T, String> mapFunc) {
        printGrid(grid, mapFunc, false);
    }

    public static <T> void printGrid(T[][] grid, Function<T, String> mapFunc, boolean withLabel) {
        if (grid.length == 0) {
            return;
        }

        int numRows = grid.length;
        int rowHeaderLength = Integer.toString(numRows).length();
        int numColumns = grid[0].length;

        if (withLabel) {
            String model = "0123456789";

            int numBatches = numColumns / model.length();
            System.out.println(" ".repeat(rowHeaderLength + 1) + model.repeat(numBatches) + model.substring(0, numColumns % model.length()));
        }

        for (int i = 0;  i < numRows; i++){
            T[] col = grid[i];
            if (withLabel) {
                System.out.print(lpad(i, rowHeaderLength, ' ') + " ");
            }
            for (T cell : col) {
                System.out.print(mapFunc.apply(cell));
            }
            System.out.println();
        }
    }

    public static String lpad(int num, int maxSize, char pad) {
        var intAsStr = Integer.toString(num);
        var padding = Character.toString(pad).repeat(maxSize);
        var fullString = (padding + intAsStr);
        var startIndex = fullString.length() - maxSize;
        return fullString.substring(startIndex);
    }

    public static Function<Integer, String> simpleIntPrint = x -> Integer.toString(x);
}
