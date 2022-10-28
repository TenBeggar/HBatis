package com.tenbeggar.hbatis.utils;

import java.util.Arrays;

public class PropertyNamer {

    public static String captureName(String name) {
        if (!name.startsWith("get") && !name.startsWith("set")) {
            throw new IllegalStateException("Error parsing property name '" + name + "'.  Didn't start with 'get' or 'set'.");
        }
        char[] chars = Arrays.copyOfRange(name.toCharArray(), 3, name.length());
        if (chars.length >= 1 && chars[0] >= 65 && chars[0] <= 90) {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }
}
