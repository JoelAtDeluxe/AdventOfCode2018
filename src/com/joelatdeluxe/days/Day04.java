package com.joelatdeluxe.days;

import com.joelatdeluxe.Pure;
import com.joelatdeluxe.SolutionBase;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day04 extends SolutionBase {
    @Override
    public void solve() throws IOException {
        var content = loadPuzzleData("day04_sorted_input.txt", "day04_input.txt");
        var lines = content.split("\n");

        var guards = parseLog(lines);
        System.out.println("Strategy 1 product: " + strategy1(guards));
        System.out.println("Strategy 2 product: " + strategy2(guards));

    }

    private int strategy1(List<Guard> guards) {
        var guard = findSleepiestGuard(guards);

        var sleepiestData = guard.getSleepiestMinute();
        return sleepiestData.getKey() * guard.getId();
    }

    private int strategy2(List<Guard> guards) {
        var sleepiestMinuteGuard = guards.get(0);
        var sleepiestData = sleepiestMinuteGuard.getSleepiestMinute();
        for (Guard guard : guards) {
            var sleepData = guard.getSleepiestMinute();
            if (sleepData.getValue() > sleepiestData.getValue()) {
                sleepiestMinuteGuard = guard;
                sleepiestData = sleepData;
            }
        }
        return sleepiestData.getKey() * sleepiestMinuteGuard.getId();
    }

    private List<Guard> parseLog(String[] events) {
        var datePattern = "\\[\\d{4}-(\\d\\d)-(\\d\\d) (\\d\\d):(\\d\\d)]";
        var guardStarts = "Guard #(\\d+) begins shift";
        var fallsAsleep = "falls asleep";
        var wakesUp = "wakes up";
        var guardStartRegex = Pattern.compile(datePattern + " " + guardStarts);
        var fallsAsleepRegex = Pattern.compile(datePattern + " " + fallsAsleep);
        var wakesRegex = Pattern.compile(datePattern + " " + wakesUp);

        int now = 0;
        int currentGuard = 0;
        int snoozeStart = 0;
        var guardMap = new HashMap<Integer, Guard>();
        for (var event : events) {
            Matcher matcher;
            if ((matcher = guardStartRegex.matcher(event)).matches()) {
                currentGuard = Integer.parseInt(matcher.group(5));
                now = Integer.parseInt(matcher.group(1) + matcher.group(2));
            } else if ((matcher = fallsAsleepRegex.matcher(event)).matches()) {
                snoozeStart = Integer.parseInt(matcher.group(4));
            } else if ((matcher = wakesRegex.matcher(event)).matches()) {
                var snoozeEnd = Integer.parseInt(matcher.group(4));
                var snooze = new SleepInterval(now, snoozeStart, snoozeEnd);
                var guard = Optional.ofNullable(guardMap.get(currentGuard)).orElse(new Guard(currentGuard));
                guard.addSnooze(snooze);
                guardMap.put(currentGuard, guard);
            }
        }
        return new ArrayList<>(guardMap.values());
    }

    private Guard findSleepiestGuard(List<Guard> guards) {
        return guards.stream().max(Comparator.comparingInt(Guard::sumSnoozeDuration)).get();
    }

    class Guard {
        private int id;
        private List<SleepInterval> snoozes;
        private int totalSleepDuration = -1;

        public Guard(int id) {
            this.id = id;
            snoozes = new ArrayList<>();
        }

        public int getId() {
            return id;
        }

        public void addSnooze(SleepInterval snooze) {
            snoozes.add(snooze);
            this.totalSleepDuration = -1;
        }

        public int sumSnoozeDuration() {
            if (this.totalSleepDuration == -1) {
                this.totalSleepDuration = this.snoozes.stream().mapToInt(SleepInterval::calcDuration).sum();
            }
            return this.totalSleepDuration;
        }

        public Map.Entry<Integer, Integer> getSleepiestMinute() {
            var minutes = new int[60];
            Arrays.fill(minutes, 0);
            for (var snooze : snoozes) {
                for (var i = 0; i < (snooze.getEndMinute() - snooze.getStartMinute()); i++) {
                    minutes[i + snooze.getStartMinute()]++;
                }
            }
            // max help from here: https://stackoverflow.com/questions/30730861/how-to-get-the-index-and-max-value-of-an-array-in-one-shot
            // (of course, writing this out by hand isn't too difficult either)
            var sleepiestMinute = IntStream.range(0, minutes.length).reduce((a, b) -> minutes[a] < minutes[b] ? b : a).orElse(-1);
            var sleepFrequency = minutes[sleepiestMinute];
            return Map.entry(sleepiestMinute, sleepFrequency);
        }
    }

    class SleepInterval {
        private int date;
        private int startMinute;
        private int endMinute;

        public SleepInterval(int date, int startMinute, int endMinute) {
            this.date = date;
            this.startMinute = startMinute;
            this.endMinute = endMinute;
        }

        public int getDate() {
            return date;
        }

        public int getStartMinute() {
            return startMinute;
        }

        public int getEndMinute() {
            return endMinute;
        }

        public int calcDuration() {
            return endMinute - startMinute;
        }
    }

    private String loadPuzzleData(String sortedName, String unsortedName) throws IOException {
        try {
            return Pure.readResFile(sortedName);
        } catch (IOException e) {
            var unsortedContent = Pure.readResFile(unsortedName);
            var lines = unsortedContent.split("\n");
            Arrays.sort(lines);
            String sortedContent = String.join("\n", lines);
            Pure.writeResFile(sortedName, sortedContent);
            return sortedContent;
        }
    }


}
