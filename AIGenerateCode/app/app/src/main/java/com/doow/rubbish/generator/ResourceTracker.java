package com.doow.rubbish.generator;

import java.util.ArrayList;
import java.util.List;


public class ResourceTracker {

    private static final List<String> drawableList = new ArrayList<>();
    private static final List<String> layoutList = new ArrayList<>();
    private static final List<String> stringList = new ArrayList<>();
    private static final List<String> rawList = new ArrayList<>();
    private static final List<String> colorList = new ArrayList<>();
    private static final List<String> dimenList = new ArrayList<>();


    public static void addDrawable(String name) {
        drawableList.add(name);
    }


    public static void addLayout(String name) {
        layoutList.add(name);
    }


    public static void addString(String name) {
        stringList.add(name);
    }


    public static void addRaw(String name) {
        rawList.add(name);
    }


    public static void addColor(String name) {
        colorList.add(name);
    }


    public static void addDimen(String name) {
        dimenList.add(name);
    }


    public static String getRandomDrawable() {
        if (drawableList.isEmpty()) return null;
        return drawableList.get(RandomUtils.between(0, drawableList.size() - 1));
    }

    // 获取随机layout
    public static String getRandomLayout() {
        if (layoutList.isEmpty()) return null;
        return layoutList.get(RandomUtils.between(0, layoutList.size() - 1));
    }

    // 获取随机string
    public static String getRandomString() {
        if (stringList.isEmpty()) return null;
        return stringList.get(RandomUtils.between(0, stringList.size() - 1));
    }

    // 获取随机raw
    public static String getRandomRaw() {
        if (rawList.isEmpty()) return null;
        return rawList.get(RandomUtils.between(0, rawList.size() - 1));
    }

    // 获取随机color
    public static String getRandomColor() {
        if (colorList.isEmpty()) return null;
        return colorList.get(RandomUtils.between(0, colorList.size() - 1));
    }

    // 获取随机dimen
    public static String getRandomDimen() {
        if (dimenList.isEmpty()) return null;
        return dimenList.get(RandomUtils.between(0, dimenList.size() - 1));
    }

    // 获取所有drawable
    public static List<String> getAllDrawables() {
        return new ArrayList<>(drawableList);
    }

    // 获取所有layout
    public static List<String> getAllLayouts() {
        return new ArrayList<>(layoutList);
    }

    // 获取所有string
    public static List<String> getAllStrings() {
        return new ArrayList<>(stringList);
    }

    // 获取所有raw
    public static List<String> getAllRaws() {
        return new ArrayList<>(rawList);
    }

    // 获取所有color
    public static List<String> getAllColors() {
        return new ArrayList<>(colorList);
    }

    // 获取所有dimen
    public static List<String> getAllDimens() {
        return new ArrayList<>(dimenList);
    }

    // 清空所有资源
    public static void clearAll() {
        drawableList.clear();
        layoutList.clear();
        stringList.clear();
        rawList.clear();
        colorList.clear();
        dimenList.clear();
    }

    // 检查是否有资源
    public static boolean hasResources() {
        return !drawableList.isEmpty() || 
               !layoutList.isEmpty() || 
               !stringList.isEmpty() || 
               !rawList.isEmpty() ||
               !colorList.isEmpty() ||
               !dimenList.isEmpty();
    }
}
