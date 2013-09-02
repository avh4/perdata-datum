package net.avh4.util.test;

import org.fest.assertions.Condition;

import java.util.Arrays;
import java.util.HashSet;

public class FestConditions {
    public static final Condition<Object[]> uniqueness = new Condition<Object[]>() {
        @Override public boolean matches(Object[] value) {
            HashSet<Object> set = new HashSet<Object>(Arrays.asList(value));
            return value.length == set.size();
        }
    }.as("all items are unique");
}
