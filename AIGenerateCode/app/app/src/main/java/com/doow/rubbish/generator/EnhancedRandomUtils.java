package com.doow.rubbish.generator;

import java.util.Random;


public class EnhancedRandomUtils {
    private static Random random = new Random(System.currentTimeMillis());


    private static final String[] CLASS_PREFIXES = {
            "Data", "User", "Product", "Order", "Payment", "Cart", "Item", "Category",
            "Message", "Notification", "Comment", "Review", "Rating", "Wishlist", "Bookmark",
            "Profile", "Settings", "Preference", "Account", "Auth", "Login", "Register",
            "Search", "Filter", "Sort", "List", "Grid", "Detail", "View", "Page",
            "Home", "Dashboard", "Feed", "Timeline", "Stream", "Wall", "Board", "Panel",
            "Manager", "Handler", "Processor", "Service", "Provider", "Source", "Factory",
            "Builder", "Creator", "Maker", "Producer", "Generator", "Compiler", "Parser",
            "Validator", "Checker", "Verifier", "Tester", "Analyzer", "Inspector", "Auditor"
    };


    private static final String[] METHOD_PREFIXES = {
            "get", "set", "add", "remove", "update", "delete", "save", "load",
            "fetch", "retrieve", "query", "search", "find", "filter", "sort", "order",
            "create", "build", "make", "generate", "produce", "compile", "parse",
            "validate", "check", "verify", "test", "analyze", "inspect", "audit",
            "process", "handle", "manage", "control", "monitor", "track", "log",
            "sync", "refresh", "reload", "reset", "clear", "clean", "flush"
    };


    private static final String[] VARIABLE_PREFIXES = {
            "data", "user", "product", "order", "payment", "cart", "item", "category",
            "message", "notification", "comment", "review", "rating", "wishlist", "bookmark",
            "profile", "settings", "preference", "account", "auth", "login", "register",
            "search", "filter", "sort", "list", "grid", "detail", "view", "page",
            "home", "dashboard", "feed", "timeline", "stream", "wall", "board", "panel"
    };


    private static final String[] PACKAGE_PREFIXES = {
            "data", "model", "view", "controller", "service", "provider", "repository",
            "util", "helper", "manager", "handler", "processor", "factory", "builder",
            "adapter", "converter", "mapper", "parser", "validator", "checker", "tester",
            "analyzer", "inspector", "auditor", "monitor", "tracker", "logger", "recorder",
            "storage", "cache", "database", "network", "api", "client", "server"
    };

    // 预定义的颜色数组（增加多样性）
    private static final String[] COLORS = {
            "primary", "secondary", "accent", "background", "foreground",
            "success", "error", "warning", "info",
            "dark", "light", "medium", "transparent",
            "red", "green", "blue", "yellow", "orange", "purple", "pink", "teal",
            "indigo", "cyan", "amber", "lime", "deep_orange", "deep_purple",
            "brown", "grey", "blue_grey", "black", "white"
    };

    // 预定义的形状数组
    private static final String[] SHAPES = {
            "rectangle", "oval", "ring", "line", "arc", "path"
    };

    // 预定义的布局类型数组
    private static final String[] LAYOUT_TYPES = {
            "LinearLayout", "RelativeLayout", "ConstraintLayout",
            "FrameLayout", "TableLayout", "GridLayout", "CoordinatorLayout"
    };

    /**
     * 设置随机种子
     *
     * @param seed 随机种子
     */
    public static void setSeed(long seed) {
        random = new Random(seed);
    }

    /**
     * 生成随机数 [min, max]
     */
    public static int between(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * 生成随机类名
     *
     * @param suffix 类名后缀
     * @return 随机类名
     */
    public static String generateClassName(String suffix) {
        String prefix = CLASS_PREFIXES[random.nextInt(CLASS_PREFIXES.length)];
        String middle = generateWord(between(2, 4));
        return prefix + middle + suffix;
    }

    /**
     * 生成随机方法名
     *
     * @param prefix 方法名前缀
     * @return 随机方法名
     */
    public static String generateMethodName(String prefix) {
        String word = generateWord(between(3, 6)).toLowerCase();
        return prefix + Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    /**
     * 生成随机变量名
     *
     * @param type 变量类型
     * @return 随机变量名
     */
    public static String generateVariableName(String type) {
        String prefix = VARIABLE_PREFIXES[random.nextInt(VARIABLE_PREFIXES.length)];
        String word = generateWord(between(2, 4)).toLowerCase();
        return prefix + word + type.toLowerCase();
    }

    /**
     * 生成随机包名
     *
     * @param basePackage 基础包名
     * @return 随机包名
     */
    public static String generatePackageName(String basePackage) {
        String prefix = PACKAGE_PREFIXES[random.nextInt(PACKAGE_PREFIXES.length)];
        String word = generateWord(between(3, 6)).toLowerCase();
        return basePackage + "." + prefix + word;
    }

    /**
     * 生成随机单词
     *
     * @param length 单词长度
     * @return 随机单词
     */
    public static String generateWord(int length) {
        char[] abc = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(abc[random.nextInt(abc.length)]));
        for (int i = 1; i < length; i++) {
            sb.append(abc[random.nextInt(abc.length)]);
        }
        return sb.toString();
    }

    /**
     * 生成随机名称
     *
     * @param prefix 名称前缀
     * @return 随机名称
     */
    public static String generateName(String prefix) {
        int length = between(5, 12);
        StringBuilder sb = new StringBuilder(prefix);
        sb.append("_");
        for (int i = 0; i < length; i++) {
            sb.append(generateWord(1).toLowerCase());
        }
        return sb.toString();
    }

    /**
     * 生成随机颜色
     *
     * @return 随机颜色
     */
    public static String generateColor() {
        return COLORS[random.nextInt(COLORS.length)];
    }

    /**
     * 生成随机形状
     *
     * @return 随机形状
     */
    public static String generateShape() {
        return SHAPES[random.nextInt(SHAPES.length)];
    }

    /**
     * 生成随机布局类型
     *
     * @return 随机布局类型
     */
    public static String generateLayoutType() {
        return LAYOUT_TYPES[random.nextInt(LAYOUT_TYPES.length)];
    }

    /**
     * 随机布尔值
     */
    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    /**
     * 从数组中随机选择一个元素
     */
    public static <T> T randomChoice(T[] array) {
        return array[random.nextInt(array.length)];
    }

    /**
     * 生成随机十六进制颜色
     *
     * @return 随机十六进制颜色
     */
    public static String generateHexColor() {
        char[] color = "0123456789abcdef".toCharArray();
        StringBuilder sb = new StringBuilder("#");
        for (int i = 0; i < 6; i++) {
            sb.append(color[random.nextInt(color.length)]);
        }
        return sb.toString();
    }

    /**
     * 生成随机尺寸值
     *
     * @return 随机尺寸值（dp）
     */
    public static int generateSize() {
        return between(8, 96);
    }

    /**
     * 生成随机边距值
     *
     * @return 随机边距值（dp）
     */
    public static int generateMargin() {
        return between(4, 32);
    }

    /**
     * 生成随机内边距值
     *
     * @return 随机内边距值（dp）
     */
    public static int generatePadding() {
        return between(4, 32);
    }

    /**
     * 生成随机文本内容
     *
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 随机文本内容
     */
    public static String generateText(int minLength, int maxLength) {
        int length = between(minLength, maxLength);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(generateWord(between(3, 8)));
            if (i < length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * 生成随机数字字符串
     *
     * @param length 长度
     * @return 随机数字字符串
     */
    public static String generateNumberString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(between(0, 9));
        }
        return sb.toString();
    }

    /**
     * 生成随机对象名
     *
     * @return 随机对象名
     */
    public static String generateObjectName() {
        String prefix = CLASS_PREFIXES[random.nextInt(CLASS_PREFIXES.length)];
        String word = generateWord(between(2, 4));
        return prefix + word;
    }

    /**
     * 生成随机整型变量名
     *
     * @return 随机整型变量名
     */
    public static String generateIntName() {
        String prefix = VARIABLE_PREFIXES[random.nextInt(VARIABLE_PREFIXES.length)];
        String word = generateWord(between(2, 4)).toLowerCase();
        return prefix + word + "Int";
    }

    /**
     * 生成随机整型数组
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机整型数组
     */
    public static int[] generateIntRange(int min, int max) {
        int count = between(1, 5);
        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i] = between(min, max);
        }
        return result;
    }

    /**
     * 生成随机集合名
     *
     * @return 随机集合名
     */
    public static String generateCollectionName() {
        String prefix = VARIABLE_PREFIXES[random.nextInt(VARIABLE_PREFIXES.length)];
        String word = generateWord(between(2, 4)).toLowerCase();
        return prefix + word + "List";
    }

    /**
     * 生成随机字符串名
     *
     * @return 随机字符串名
     */
    public static String generateStringName() {
        String prefix = VARIABLE_PREFIXES[random.nextInt(VARIABLE_PREFIXES.length)];
        String word = generateWord(between(2, 4)).toLowerCase();
        return prefix + word + "Str";
    }

    /**
     * 生成随机布尔变量名
     *
     * @return 随机布尔变量名
     */
    public static String generateBooleanName() {
        String prefix = VARIABLE_PREFIXES[random.nextInt(VARIABLE_PREFIXES.length)];
        String word = generateWord(between(2, 4)).toLowerCase();
        return prefix + word + "Bool";
    }

    /**
     * 生成随机日志消息
     *
     * @param message 日志消息模板
     * @return 随机日志消息
     */
    public static String generateLogMessage(String message) {
        return message + " " + generateNumberString(between(1, 3));
    }
}