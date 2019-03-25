package com.joelatdeluxe.days;

import com.joelatdeluxe.Pure;
import com.joelatdeluxe.SolutionBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;

public class Day02 extends SolutionBase {
    public static byte HAS_TWO = 1;
    public static byte HAS_THREE = 2;

    @Override
    public void solve() throws IOException{
        var content = Pure.readResFile("day02_input.txt");
        var lines = content.split("\n");

        // Part 1
        // var sum = calculateChecksum(lines);
        // System.out.println("Checksum is: " + ());

        //Part 2
        nieveSolution(lines);
    }

    private String nieveSolution(String[] lines) {
        var exit = false;
        String rtn = null;
        for (var guessIndex = 0; guessIndex < lines.length && !exit; guessIndex++) {
            var word = lines[guessIndex];
            for (var targetIndex = guessIndex; targetIndex < lines.length; targetIndex++) {
                var target = lines[targetIndex];

                // all lines are exactly the same length, so ignoring length checks
                var diffCount = 0;
                StringBuilder sb = new StringBuilder();
                for (var charIndex = 0; charIndex < word.length(); charIndex++) {
                    if(word.charAt(charIndex) != target.charAt(charIndex)) {
                        diffCount++;
                        if (diffCount > 1) {
                            break;
                        }
                        sb.append('_');
                    }
                    else {
                        sb.append(word.charAt(charIndex));
                    }
                }
                if (diffCount == 1) {
                    rtn = sb.toString();
                    System.out.println("Boxes that match have ids (pretty): " + rtn);
                    System.out.println("Boxes that match have ids: " + rtn.replace("_", ""));
                    exit = true;
                }
            }
        }
        return rtn;
    }

    private int calculateChecksum(String[] lines) {
        var threeCount = 0;
        var twoCount = 0;

        for (var line : lines) {
            var b = checkBoxType(line);
            if ((b & HAS_TWO) > 0) {
                twoCount++;
            }
            if ((b & HAS_THREE) > 0) {
                threeCount++;
            }
        }
        return threeCount * twoCount;
    }

    private byte checkBoxType(String boxCode) {
        byte base = 0;
        var charCounts = new HashMap<Character, Integer>();

        for (var i = 0; i < boxCode.length(); i++) {
            var c = boxCode.charAt(i);
            Integer oldCount = Optional.ofNullable(charCounts.get(c)).orElse(0);
            charCounts.put(c, oldCount + 1);
        }

        for (var v : charCounts.values()) {
            if (v == 2) {
                base |= HAS_TWO;
            } else if (v == 3) {
                base |= HAS_THREE;
            }
        }
        return base;
    }
}
