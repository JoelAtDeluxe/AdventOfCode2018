package com.joelatdeluxe.days;

import com.joelatdeluxe.Pure;
import com.joelatdeluxe.SolutionBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class Day06 extends SolutionBase {
    @Override
    public void solve() throws IOException {
        var rawData = loadPuzzleData(true);

        var coords = parsePuzzleData(rawData);
        var grid = mapCoordinates(coords);
        printGrid(grid);
    }

    private List<Coordinate> parsePuzzleData(String rawData) {
        var lines = rawData.split("\n");
        List<Coordinate> coords = new ArrayList<>();

        for (var i = 0; i < lines.length; i++) {
            var line = lines[i];
            coords.add(Coordinate.fromStringArray(line.split(","), i));
        }

        return coords;
    }

    private LabelDist[][] makeEmptyGrid(List<Coordinate> coords) {
        var maxX = coords.stream().mapToInt(Coordinate::X).max().orElse(0) + 1; // adding 1 to print buffer
        var maxY = coords.stream().mapToInt(Coordinate::Y).max().orElse(0) + 1; // adding 1 to print buffer
        var max = maxX + maxY - 1; // (maxX - 1) + (maxY - 1) + 1;

        var grid = new LabelDist[maxX][maxY];

        for (LabelDist[] rows : grid) {
            Arrays.fill(rows, new LabelDist('.', max));
        }
        return grid;
    }

    private void printGrid(LabelDist[][] grid) {
        Pure.printGrid(grid, i -> {
            char c = i.getLabel();
            if (i.getDistance() != 0) {
                c = Character.toLowerCase(c);
            }
            return Character.toString(c);
        }, true);
    }

    private void plotCoordinateSpread(Coordinate coord, LabelDist[][] grid) {
        List<CoordAndLabel> currentGen;
        List<CoordAndLabel> nextGen = new ArrayList<>();

        BiConsumer<CoordAndLabel, List<CoordAndLabel>> addToNextGen = (cell, list) -> {
            var x = cell.getCoordinate().X();
            var y = cell.getCoordinate().Y();
            if (!(x < 0 || y < 0 || x >= grid.length || y >= grid[x].length)) {
                if (!list.contains(cell)) { // this needs some work -- we seem to be getting overlaps
                    list.add(cell);
                }
            }
        };

        addToNextGen.accept(new CoordAndLabel(coord, new LabelDist(coord.getLabel(), 0)), nextGen);

        while (!nextGen.isEmpty()) {
            currentGen = nextGen;
            nextGen = new ArrayList<>();
            var itr = currentGen.iterator();
            while (itr.hasNext()) {
                var cell = itr.next();

                var coordChild = cell.getCoordinate();
                var target = grid[coordChild.X()][coordChild.Y()];
                var newVal = LabelDist.useClosest(target, cell.getLabelDist());
                grid[coordChild.X()][coordChild.Y()] = newVal;

                if (newVal.getLabel() == coord.getLabel() || newVal.getLabel() == '.') {
                    var direction = coord.getRelativePosition(cell.getCoordinate());

                    if ((direction & Coordinate.NORTH) > 0) {
                        addToNextGen.accept(cell.spawnFromCell(0, -1), nextGen);
                    }
                    if ((direction & Coordinate.SOUTH) == Coordinate.SOUTH) {
                        addToNextGen.accept(cell.spawnFromCell(0, 1), nextGen);
                    }
                    if ((direction & Coordinate.EAST) == Coordinate.EAST) {
                        addToNextGen.accept(cell.spawnFromCell(1, 0), nextGen);
                    }
                    if ((direction & Coordinate.WEST) == Coordinate.WEST) {
                        addToNextGen.accept(cell.spawnFromCell(-1, 0), nextGen);
                    }
                }

            }
        }
    }

    public LabelDist[][] mapCoordinates(List<Coordinate> coords) {
        var grid = makeEmptyGrid(coords);

        for (Coordinate coord : coords) {
            plotCoordinateSpread(coord, grid);
            System.out.println("-------- After " + coord.getLabel() + "----------");
            printGrid(grid);
            System.out.println();
        }

        return grid;
    }


    private String loadPuzzleData(boolean testData) throws IOException {
        if (testData) {
            return "1, 1\n" +
                    "1, 6\n" +
                    "8, 3\n" +
                    "3, 4\n" +
                    "5, 5\n" +
                    "8, 9";
        }
        return Pure.readResFile("day06_input.txt");
    }
}

class CoordAndLabel {
    private Coordinate c;
    private LabelDist l;

    public CoordAndLabel(Coordinate c, LabelDist l) {
        this.c = c;
        this.l = l;
    }

    public CoordAndLabel spawnFromCell(int xChange, int yChange) {
        Coordinate newCoord = new Coordinate(c.X() + xChange, c.Y() + yChange, c.getLabel());
        LabelDist newLabelDist = new LabelDist(l.getLabel(), l.getDistance() + 1);

        return new CoordAndLabel(newCoord, newLabelDist);
    }

    public Coordinate getCoordinate() {
        return c;
    }

    public LabelDist getLabelDist() {
        return l;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordAndLabel that = (CoordAndLabel) o;
        return Objects.equals(c, that.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c);
    }
}

class LabelDist {
    private char label;
    private int distance;

    public LabelDist(char label, int distance) {
        this.label = label;
        this.distance = distance;
    }

    public static LabelDist useClosest(LabelDist a, LabelDist b) {
        if (a.distance < b.distance) {
            return a;
        } else if (b.distance < a.distance) {
            return b;
        } else {
            return new LabelDist('.', a.distance);
        }
    }

    public char getLabel() {
        return label;
    }

    public int getDistance() {
        return distance;
    }
}

class Coordinate {
    private int x;
    private int y;
    private char label;

    public static final int NORTH = 0b0001;
    public static final int SOUTH = 0b0010;
    public static final int NS_MASK = 0b0011;
    public static final int EAST = 0b0100;
    public static final int WEST = 0b1000;
    public static final int EW_MASK = 0b1100;

    public Coordinate(int x, int y, int labelIndex) {
        this.x = x;
        this.y = y;
        this.label = (char) ('A' + labelIndex);
    }

    public static Coordinate fromStringArray(String[] parts, int index) {
        // Puzzle data is given in column, row order, so y, then x
        int y = Integer.parseInt(parts[0].trim());
        int x = Integer.parseInt(parts[1].trim());
        return new Coordinate(x, y, index);
    }

    public int getRelativePosition(Coordinate c) {
        var movementX = c.X() - this.X();
        var movementY = c.Y() - this.Y();

        return determineRelativePosition(movementX, movementY);
    }

    public static int determineRelativePosition(int xMovement, int yMovement) {
        // int base = 0;
        //
        // // Normally I would | by 0 to keep base 0 in the case that there's no movement either direction
        // // but NORTH|SOUTH is undefined as a representation, and as such, I think using NORTH|SOUTH makes the
        // // rest of the code easier to understand, so opting for that here.
        // base |= (yMovement > 0) ? SOUTH : (yMovement < 0) ? NORTH: (NORTH | SOUTH);
        // base |= (xMovement > 0) ? EAST : (xMovement < 0) ? WEST : (EAST | WEST);

        var base = 0;
        base |= (yMovement >= 0) ? SOUTH : 0;
        base |= (yMovement <= 0) ? NORTH : 0;
        base |= (xMovement >= 0) ? EAST : 0;
        base |= (xMovement <= 0) ? WEST : 0;

        return base;
    }

    public int X() {
        return x;
    }

    public int Y() {
        return y;
    }

    public char getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x &&
                y == that.y &&
                label == that.label;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, label);
    }
}