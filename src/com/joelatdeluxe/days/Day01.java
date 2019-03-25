package com.joelatdeluxe.days;

import com.joelatdeluxe.Pure;
import com.joelatdeluxe.SolutionBase;

import java.io.IOException;
import java.util.HashSet;

public class Day01 extends SolutionBase {
    @Override
    public void solve() throws IOException{
        var content = Pure.readResFile("day01_input.txt");
        var lines = content.split("\n");
        var encounteredFrequenices = new HashSet<>();
        var firstFreqEncountered = false;
        var sum = 0;
        encounteredFrequenices.add(0);

        while (!firstFreqEncountered) {
            for (var line : lines) {
                var number = Integer.parseInt(line.substring(1));
                sum += (line.charAt(0) == '-') ? -1 * number : number;
                if (!firstFreqEncountered && encounteredFrequenices.contains(sum)) {
                    System.out.println("First frequency is: " + sum);
                    firstFreqEncountered = true;
                } else {
                    encounteredFrequenices.add(sum);
                }
            }
        }
        System.out.println("Sum is: " + sum);

    }
}
