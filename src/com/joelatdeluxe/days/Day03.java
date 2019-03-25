package com.joelatdeluxe.days;

import com.joelatdeluxe.Pure;
import com.joelatdeluxe.SolutionBase;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day03 extends SolutionBase {


    @Override
    public void solve() throws IOException {
        var content = Pure.readResFile("day03_input.txt");
        // var content = getFakeContent();

        var lines = content.split("\n");
        var columns = 1000; // 8;
        var rows = 1000; // 8;

        var squares = Arrays.stream(lines).map(Square::fromText).collect(Collectors.toList());
        Integer[][] grid = new Integer[columns][rows];
        Pure.initGrid(grid, 0);

        //part 1
        // var overlapCount = markOverlapsPhysical(grid, squares);
        // System.out.println("Number of overlapping sq. inches: " + overlapCount);

        //part 2
        var singleOwnerID = findSingleClaimUser(grid, squares);
        System.out.println("Elf: " + singleOwnerID + " has a single claim on his region");

    }

    private int markOverlapsPhysical(Integer[][] grid, List<Square> squares) {
        var overlapCount = 0;

        for (var square : squares) {
            for (var i = 0; i < square.getWidth(); i++) {
                var cellLeftIndex = square.getLeft() + i;
                for (var j = 0; j < square.getHeight(); j++) {
                    var cellTopIndex = square.getTop() + j;
                    if (grid[cellLeftIndex][cellTopIndex] != 0) {
                        overlapCount++;
                    }
                    grid[cellLeftIndex][cellTopIndex]++;
                }
            }
        }
        return overlapCount;
    }

    private int findSingleClaimUser(Integer[][] grid, List<Square> squares) {
        var nonOverlapped = new HashSet<Integer>();

        for (var square : squares) {
            var overlapped = false;
            for (var i = 0; i < square.getWidth(); i++) {
                var cellLeft = square.getLeft() + i;
                for (var j = 0; j < square.getHeight(); j++) {
                    var cellTop = square.getTop() + j;
                    if (grid[cellLeft][cellTop] != 0) {
                        nonOverlapped.remove(grid[cellLeft][cellTop]);
                        overlapped = true;
                    }
                    else {
                        grid[cellLeft][cellTop] = square.getOwner();
                    }
                }
            }
            if (!overlapped) {
                nonOverlapped.add(square.getOwner());
            }
        }

        return nonOverlapped.iterator().next();
    }

    private static String getFakeContent() {
        return "#1 @ 1,3: 4x4\n" +
                "#2 @ 3,1: 4x4\n" +
                "#3 @ 5,5: 2x2";
    }
}

class Square {
    private static final Pattern SQUARE_REQ = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");
    private int top, left;
    private int width, height;
    private int owner;

    public Square(int top, int left, int width, int height, int owner) {
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;
        this.owner = owner;
    }

    public static Square fromText(String something) {
        var matcher = SQUARE_REQ.matcher(something);

        if (matcher.matches()) {
            var owner = Integer.parseInt(matcher.group(1));
            var left = Integer.parseInt(matcher.group(2));
            var top = Integer.parseInt(matcher.group(3));
            var width = Integer.parseInt(matcher.group(4));
            var height = Integer.parseInt(matcher.group(5));

            return new Square(top, left, width, height, owner);
        }
        return null;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOwner() {
        return owner;
    }

    public int area() {
        return width * height;
    }
}
