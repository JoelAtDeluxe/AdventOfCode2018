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
            (new Day05()).solve();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}