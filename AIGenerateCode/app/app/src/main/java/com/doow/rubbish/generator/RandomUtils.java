package com.doow.rubbish.generator;

import java.util.Random;


public class RandomUtils {
    private static final Random random = new Random(System.currentTimeMillis());
    private static final char[] abc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] color = "0123456789abcdef".toCharArray();


    public static int between(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static long betweenLong(long min, long max) {
        if (min >= max) {
            return min;
        }
        long range = max - min + 1;
        return min + (long) (random.nextDouble() * range);
    }



    public static String generateName(String prefix) {
        int length = between(5, 12);
        StringBuilder sb = new StringBuilder(prefix);
        sb.append("_");
        for (int i = 0; i < length; i++) {
            sb.append(abc[random.nextInt(abc.length)]);
        }
        return sb.toString();
    }


    public static String generateColor() {
        StringBuilder sb = new StringBuilder("#");
        for (int i = 0; i < 6; i++) {
            sb.append(color[random.nextInt(color.length)]);
        }
        return sb.toString();
    }


    public static String generateClassName(String suffix) {
        String prefix = generateWord(between(2, 4));
        return prefix + suffix;
    }


    public static String generateWord(int length) {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(abc[random.nextInt(abc.length)]));
        for (int i = 1; i < length; i++) {
            sb.append(abc[random.nextInt(abc.length)]);
        }
        return sb.toString();
    }

    // 生成随机包名
    public static String generatePackageName(String basePackage) {
        return basePackage + "." + generateWord(between(3, 6)).toLowerCase();
    }

    // 生成随机方法名
    public static String generateMethodName(String prefix) {
        String word = generateWord(between(3, 6)).toLowerCase();
        return prefix + Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    // 生成随机变量名
    public static String generateVariableName(String type) {
        String word = generateWord(between(3, 6)).toLowerCase();
        return word + type.toLowerCase();
    }

    // 随机布尔值
    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    // 从数组中随机选择一个元素
    public static <T> T randomChoice(T[] array) {
        return array[random.nextInt(array.length)];
    }

    // 生成随机double值
    public static double nextDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    // 生成随机字符串
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(abc[random.nextInt(abc.length)]);
        }
        return sb.toString();
    }

    // 生成随机字符串（默认长度8）
    public static String generateRandomString() {
        return generateRandomString(8);
    }
}
