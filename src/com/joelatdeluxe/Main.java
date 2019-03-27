package com.joelatdeluxe;

import com.joelatdeluxe.days.*;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            // (new Day01()).solve();
            // (new Day02()).solve();
            // (new Day03()).solve();

            // (new Day04()).solve();
            // (new Day05()).solve();
            (new Day06()).solve();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private static final void dumbListSwitchTest() {
        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<Integer> printList;
        var i = 0;
        list.add(i++);
        list.add(i++);
        list.add(i++);
        list.add(i++);
        list.add(i++);
        list.add(i++);
        list.add(i++);
        list.add(i++);

        while(!list.isEmpty()) {
            printList = list;
            list = new ArrayList<>();

            for (Integer v : printList) {
                if (v % 2 == 0) {
                    list.add(i++);
                }
                System.out.println(v);
            }
        }
    }
}