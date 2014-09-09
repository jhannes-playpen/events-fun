package com.johannesbrodwall.infrastructure;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SampleData {

    private static Random random = new Random();

    protected static String randomColor() {
        return random("red", "green", "blue", "yellow", "purple");
    }

    protected static String randomWords(int count) {
        List<String> words = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            words.add(randomWord());
        }
        return String.join(" ", words);
    }

    protected static String randomWord() {
        return random("foo", "bar", "bar", "qux", "lorum", "ipsum", "the", "dalum");
    }

    @SafeVarargs
    private static <T> T random(T... alternatives) {
        return alternatives[random.nextInt(alternatives.length)];
    }

    protected static LocalDate randomDate() {
        return LocalDate.of(2014, 1, 1).plusDays(randomInt(720));
    }

    protected static int randomInt(int max) {
        return random.nextInt(max);
    }

}
