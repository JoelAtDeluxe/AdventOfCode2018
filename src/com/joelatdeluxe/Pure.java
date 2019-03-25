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
        for( T[] col : grid) {
            for( T cell : col) {
                System.out.print(mapFunc.apply(cell));
            }
            System.out.println();
        }
    }

    public static Function<Integer, String> simpleIntPrint = x -> Integer.toString(x);
}
