package com.johannesbrodwall.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SampleData {

    private static Random random = new Random();

    public static EventCategory sampleCategory() {
        return new EventCategory(randomWords(2), randomColor());
    }

    private static String randomColor() {
        return random("red", "green", "blue", "yellow", "purple");
    }

    private static String randomWords(int count) {
        List<String> words = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            words.add(randomWord());
        }
        return String.join(" ", words);
    }

    private static String randomWord() {
        return random("foo", "bar", "bar", "qux", "lorum", "ipsum", "the", "dalum");
    }

    @SafeVarargs
    private static <T> T random(T... alternatives) {
        return alternatives[random.nextInt(alternatives.length)];
    }

}
