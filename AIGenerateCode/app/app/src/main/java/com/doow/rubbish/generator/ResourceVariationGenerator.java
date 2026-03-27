package com.doow.rubbish.generator;

import java.util.*;

/**
 * 资源变体生成器 - 第五步优化
 * 负责生成不同的资源变体，确保每个项目的资源不同
 */
public class ResourceVariationGenerator {

    private static ResourceVariationGenerator instance;
    private Random random;

    // 应用名称变体
    private static final String[] APP_NAME_PREFIXES = {
        "Smart", "Quick", "Pro", "Easy", "Fast", "Super", "Ultra",
        "Mega", "Hyper", "Power", "Master", "Expert", "Elite", "Prime"
    };

    private static final String[] APP_NAME_ROOTS = {
        "Tool", "App", "Helper", "Assistant", "Manager", "Master",
        "Wizard", "Genius", "Pro", "Plus", "Expert", "Elite"
    };

    // 应用图标风格
    private static final String[] ICON_STYLES = {
        "material", "flat", "gradient", "neumorphism", "glassmorphism",
        "skeuomorphic", "minimalist", "outline", "filled", "rounded"
    };

    // 启动画面风格
    private static final String[] SPLASH_STYLES = {
        "logo_center", "logo_top", "logo_bottom", "brand_center",
        "gradient_background", "image_background", "animation",
        "minimal", "branded", "custom"
    };

    // 字体风格
    private static final String[] FONT_STYLES = {
        "roboto", "opensans", "lato", "montserrat", "poppins",
        "raleway", "ubuntu", "nunito", "quicksand", "inter"
    };

    // 尺寸变体
    private static final Map<String, int[]> SIZE_VARIATIONS = new HashMap<>();

    static {
        // 边距变体
        SIZE_VARIATIONS.put("margin", new int[]{4, 8, 12, 16, 20, 24});

        // 内边距变体
        SIZE_VARIATIONS.put("padding", new int[]{4, 8, 12, 16, 20, 24});

        // 圆角变体
        SIZE_VARIATIONS.put("corner_radius", new int[]{4, 8, 12, 16, 20, 24});

        // 图标尺寸变体
        SIZE_VARIATIONS.put("icon_size", new int[]{16, 24, 32, 48, 64});

        // 按钮高度变体
        SIZE_VARIATIONS.put("button_height", new int[]{36, 40, 48, 56, 64});
    }

    // 动画时长变体
    private static final int[] ANIMATION_DURATIONS = {
        150, 200, 250, 300, 350, 400, 450, 500
    };

    // 字符串资源变体
    private static final Map<String, String[]> STRING_VARIATIONS = new HashMap<>();

    static {
        // 欢迎消息
        STRING_VARIATIONS.put("welcome", new String[]{
            "Welcome", "Hello", "Hi there", "Good to see you",
            "Welcome back", "Let\'s get started", "Ready to go"
        });

        // 确认按钮
        STRING_VARIATIONS.put("confirm", new String[]{
            "Confirm", "OK", "Done", "Continue", "Proceed",
            "Next", "Got it", "Apply"
        });

        // 取消按钮
        STRING_VARIATIONS.put("cancel", new String[]{
            "Cancel", "Close", "Dismiss", "Back", "Return"
        });

        // 加载消息
        STRING_VARIATIONS.put("loading", new String[]{
            "Loading...", "Please wait...", "Just a moment...",
            "Processing...", "Working on it..."
        });

        // 错误消息
        STRING_VARIATIONS.put("error", new String[]{
            "Something went wrong", "An error occurred",
            "Oops!", "Error", "Failed"
        });

        // 成功消息
        STRING_VARIATIONS.put("success", new String[]{
            "Success!", "Done!", "Completed successfully",
            "All set!", "Great!"
        });
    }

    private ResourceVariationGenerator() {
        random = new Random();
    }

    public static synchronized ResourceVariationGenerator getInstance() {
        if (instance == null) {
            instance = new ResourceVariationGenerator();
        }
        return instance;
    }

    /**
     * 设置随机种子
     */
    public void setSeed(long seed) {
        random = new Random(seed);
    }

    /**
     * 生成应用名称
     */
    public String generateAppName(String baseName) {
        String prefix = APP_NAME_PREFIXES[random.nextInt(APP_NAME_PREFIXES.length)];
        String root = APP_NAME_ROOTS[random.nextInt(APP_NAME_ROOTS.length)];
        return prefix + baseName + root;
    }

    /**
     * 获取图标风格
     */
    public String getIconStyle() {
        return ICON_STYLES[random.nextInt(ICON_STYLES.length)];
    }

    /**
     * 获取启动画面风格
     */
    public String getSplashStyle() {
        return SPLASH_STYLES[random.nextInt(SPLASH_STYLES.length)];
    }

    /**
     * 获取字体风格
     */
    public String getFontStyle() {
        return FONT_STYLES[random.nextInt(FONT_STYLES.length)];
    }

    /**
     * 获取尺寸变体
     */
    public int getSizeVariation(String type) {
        int[] sizes = SIZE_VARIATIONS.get(type);
        if (sizes != null && sizes.length > 0) {
            return sizes[random.nextInt(sizes.length)];
        }
        return 16; // 默认值
    }

    /**
     * 获取动画时长
     */
    public int getAnimationDuration() {
        return ANIMATION_DURATIONS[random.nextInt(ANIMATION_DURATIONS.length)];
    }

    /**
     * 获取字符串变体
     */
    public String getStringVariation(String type) {
        String[] variations = STRING_VARIATIONS.get(type);
        if (variations != null && variations.length > 0) {
            return variations[random.nextInt(variations.length)];
        }
        return "";
    }

    /**
     * 生成完整的资源变体配置
     */
    public Map<String, Object> generateResourceVariations(String baseAppName) {
        Map<String, Object> variations = new HashMap<>();

        // 应用名称
        variations.put("app_name", generateAppName(baseAppName));

        // 图标风格
        variations.put("icon_style", getIconStyle());

        // 启动画面风格
        variations.put("splash_style", getSplashStyle());

        // 字体风格
        variations.put("font_style", getFontStyle());

        // 尺寸变体
        variations.put("margin", getSizeVariation("margin"));
        variations.put("padding", getSizeVariation("padding"));
        variations.put("corner_radius", getSizeVariation("corner_radius"));
        variations.put("icon_size", getSizeVariation("icon_size"));
        variations.put("button_height", getSizeVariation("button_height"));

        // 动画时长
        variations.put("animation_duration", getAnimationDuration());

        // 字符串变体
        variations.put("welcome_message", getStringVariation("welcome"));
        variations.put("confirm_button", getStringVariation("confirm"));
        variations.put("cancel_button", getStringVariation("cancel"));
        variations.put("loading_message", getStringVariation("loading"));
        variations.put("error_message", getStringVariation("error"));
        variations.put("success_message", getStringVariation("success"));

        return variations;
    }

    /**
     * 打印资源变体配置
     */
    public void printResourceVariations(Map<String, Object> variations) {
        System.out.println("========================================");
        System.out.println("资源变体配置");
        System.out.println("========================================");

        System.out.println("应用名称: " + variations.get("app_name"));
        System.out.println("\n图标风格: " + variations.get("icon_style"));
        System.out.println("启动画面风格: " + variations.get("splash_style"));
        System.out.println("字体风格: " + variations.get("font_style"));

        System.out.println("\n尺寸配置:");
        System.out.println("  边距: " + variations.get("margin") + "dp");
        System.out.println("  内边距: " + variations.get("padding") + "dp");
        System.out.println("  圆角: " + variations.get("corner_radius") + "dp");
        System.out.println("  图标尺寸: " + variations.get("icon_size") + "dp");
        System.out.println("  按钮高度: " + variations.get("button_height") + "dp");

        System.out.println("\n动画时长: " + variations.get("animation_duration") + "ms");

        System.out.println("\n字符串资源:");
        System.out.println("  欢迎消息: " + variations.get("welcome_message"));
        System.out.println("  确认按钮: " + variations.get("confirm_button"));
        System.out.println("  取消按钮: " + variations.get("cancel_button"));
        System.out.println("  加载消息: " + variations.get("loading_message"));
        System.out.println("  错误消息: " + variations.get("error_message"));
        System.out.println("  成功消息: " + variations.get("success_message"));

        System.out.println("========================================");
    }
}
