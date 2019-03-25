package com.joelatdeluxe.days;

import com.joelatdeluxe.Pure;
import com.joelatdeluxe.SolutionBase;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day05 extends SolutionBase {
    @Override
    public void solve() throws IOException {
        var polymer = Pure.readResFile("day05_input.txt");

        // part 1
        // var activated = activate(polymer);
        // System.out.println("After activation, " + activated .length() + " units remain");

        //part 2
        // testPolymer(polymer, 'k');

        var chars = stringToInsensitiveSet(polymer);
        char mostOptimized = '-';
        int mostOptimizedLen = -1;

        var iter = chars.iterator();
        while (iter.hasNext()) {
            var ch = iter.next();
            int len = testPolymer(polymer, ch);
            if (mostOptimizedLen == -1 || len < mostOptimizedLen) {
                mostOptimized = ch;
                mostOptimizedLen = len;
            }
        }
        System.out.println("Best unit to remove: " + mostOptimized + " (Produces polymer of length: " + mostOptimizedLen + ")");

    }

    private int testPolymer(String polymer, char removal) {
        //var regex = "[" + String.join(new String(removals)) + "]";
        var regex = "(?i)" + removal;
        var optimized = polymer.replaceAll(regex, "");

        var activated = activate(optimized);
        return activated.length();
    }

    private HashSet<Character> stringToInsensitiveSet(String s) {
        String lowered = s.toLowerCase();
        HashSet<Character> rtn = new HashSet<>();
        for (int i = 0, len = lowered .length(); i < len; i++) {
            rtn.add(lowered.charAt(i));
        }
        return rtn;
    }

    private String activate(String polymer) {
        var lookup = prepLookupList();
        var result = new FiniteStackString(polymer.length());
        for (int i = 0, len = polymer.length(); i < len; i++) {
            var thisChar = polymer.charAt(i);
            if (result.getActualLength() == 0 || (lookup.apply(thisChar) != result.peek()) ) {
                result.pushLetter(thisChar);
            }
            else {
                result.popLetter();
            }
        }
        return result.asString();
    }

    class FiniteStackString {
        private char[] container;
        private int actualLength = 0;

        public FiniteStackString(int length) {
            container = new char[length];
        }

        public String asString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < actualLength; i++) {
                sb.append(container[i]);
            }
            return sb.toString();
        }

        public void pushLetter(char c) throws ArrayIndexOutOfBoundsException {
            container[actualLength] = c;
            actualLength++;
        }

        public void popLetter() {
            if (actualLength > 0) {
                actualLength--;
            }
        }

        public char peek() {
            return container[actualLength - 1];
        }

        public int getActualLength() {
            return actualLength;
        }

    }

    private static Function<Character, Character> prepLookupList() {
        final HashMap<Character, Character> charLookup = new HashMap<>();
        for (var i = 0; i < 26; i++) {
            char upper = (char) ('A' + i);
            char lower = (char) ('a' + i);
            charLookup.put(upper, lower);
            charLookup.put(lower, upper);
        }
        return letter -> Optional.ofNullable(charLookup.get(letter)).orElse(letter);
    }

    private String loadPuzzleData() {
        return "dabAcCaCBAcCcaDA";
    }
}
