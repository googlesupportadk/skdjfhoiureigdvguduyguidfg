package com.doow.rubbish.generator;

import java.util.*;

/**
 * UI变体生成器 - 第三步优化
 * 负责生成不同的UI变体，确保每个项目的UI风格不同
 */
public class UIVariationGenerator {

    private static UIVariationGenerator instance;
    private Random random;

    // UI主题变体
    private static final String[] THEME_VARIATIONS = {
        "material", "material3", "minimalist", "flat", "neumorphism",
        "glassmorphism", "skeuomorphic", "retro", "cyberpunk", "dark_mode",
        "light_mode", "ios_style", "windows_style", "macos_style", "brutalism",
        "glassmorphism_dark", "neumorphism_light", "solarized", "dracula",
        "nord", "gruvbox", "monokai", "catppuccin", "tokyo_night"
    };

    // 颜色调色板
    private static final Map<String, String[]> COLOR_PALETTES = new HashMap<>();

    static {
        // Material Design 调色板
        COLOR_PALETTES.put("material", new String[]{
            "#6200EE", "#3700B3", "#03DAC6", "#018786", "#FFFFFF", "#000000"
        });

        // Material 3 调色板
        COLOR_PALETTES.put("material3", new String[]{
            "#6750A4", "#625B71", "#7D5260", "#604D59", "#FFFBFE", "#1C1B1F"
        });

        // 暗色主题调色板
        COLOR_PALETTES.put("dark", new String[]{
            "#BB86FC", "#3700B3", "#03DAC6", "#018786", "#121212", "#FFFFFF"
        });

        // 亮色主题调色板
        COLOR_PALETTES.put("light", new String[]{
            "#6200EE", "#3700B3", "#018786", "#03DAC6", "#FFFFFF", "#000000"
        });

        // 赛博朋克调色板
        COLOR_PALETTES.put("cyberpunk", new String[]{
            "#FF00FF", "#00FFFF", "#FFFF00", "#FF0000", "#0D0D0D", "#FFFFFF"
        });

        // 复古调色板
        COLOR_PALETTES.put("retro", new String[]{
            "#FF6B6B", "#4ECDC4", "#FFE66D", "#95E1D3", "#F7FFF7", "#2C3E50"
        });

        // 极简主义调色板
        COLOR_PALETTES.put("minimalist", new String[]{
            "#2C3E50", "#3498DB", "#1ABC9C", "#16A085", "#ECF0F1", "#2C3E50"
        });

        // 扁平化设计调色板
        COLOR_PALETTES.put("flat", new String[]{
            "#E74C3C", "#3498DB", "#2ECC71", "#F39C12", "#34495E", "#ECF0F1"
        });
    }

    // 布局风格变体
    private static final String[] LAYOUT_VARIATIONS = {
        "constraint_layout", "linear_layout", "relative_layout",
        "frame_layout", "coordinator_layout", "motion_layout",
        "constraint_layout_chain", "constraint_layout_barrier",
        "constraint_layout_layer", "constraint_layout_placeholder"
    };

    // 导航模式变体
    private static final String[] NAVIGATION_VARIATIONS = {
        "bottom_navigation", "drawer_navigation", "tab_navigation",
        "nested_navigation", "mixed_navigation", "toolbar_navigation",
        "actionbar_navigation", "viewpager_navigation",
        "pager_tab_navigation", "bottom_sheet_navigation",
        "floating_navigation", "side_navigation"
    };

    // 动画效果变体
    private static final String[] ANIMATION_VARIATIONS = {
        "fade", "slide", "scale", "rotate", "flip",
        "shared_element", "transition", "motion_layout",
        "lottie", "vector_drawable", "property_animation"
    };

    // 组件风格变体
    private static final Map<String, String[]> COMPONENT_STYLES = new HashMap<>();

    static {
        // 按钮风格
        COMPONENT_STYLES.put("button", new String[]{
            "outlined", "filled", "text", "elevated", "tonal", "rounded", "square"
        });

        // 卡片风格
        COMPONENT_STYLES.put("card", new String[]{
            "elevated", "outlined", "filled", "rounded", "square"
        });

        // 输入框风格
        COMPONENT_STYLES.put("input", new String[]{
            "outlined", "filled", "underlined", "rounded", "square"
        });

        // 对话框风格
        COMPONENT_STYLES.put("dialog", new String[]{
            "alert", "bottom_sheet", "full_screen", "centered", "floating"
        });

        // 列表项风格
        COMPONENT_STYLES.put("list_item", new String[]{
            "simple", "two_line", "three_line", "icon", "avatar", "thumbnail"
        });
    }

    private UIVariationGenerator() {
        random = new Random();
    }

    public static synchronized UIVariationGenerator getInstance() {
        if (instance == null) {
            instance = new UIVariationGenerator();
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
     * 获取主题变体
     */
    public String getThemeVariation() {
        return THEME_VARIATIONS[random.nextInt(THEME_VARIATIONS.length)];
    }

    /**
     * 获取颜色调色板
     */
    public String[] getColorPalette(String theme) {
        String[] palette = COLOR_PALETTES.get(theme);
        if (palette != null) {
            return palette.clone();
        }
        // 如果主题没有对应的调色板，生成随机颜色
        return generateRandomPalette();
    }

    /**
     * 生成随机调色板
     */
    private String[] generateRandomPalette() {
        String[] palette = new String[6];
        for (int i = 0; i < palette.length; i++) {
            palette[i] = generateRandomColor();
        }
        return palette;
    }

    /**
     * 生成随机颜色
     */
    private String generateRandomColor() {
        char[] color = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("#");
        for (int i = 0; i < 6; i++) {
            sb.append(color[random.nextInt(color.length)]);
        }
        return sb.toString();
    }

    /**
     * 获取布局变体
     */
    public String getLayoutVariation() {
        return LAYOUT_VARIATIONS[random.nextInt(LAYOUT_VARIATIONS.length)];
    }

    /**
     * 获取导航变体
     */
    public String getNavigationVariation() {
        return NAVIGATION_VARIATIONS[random.nextInt(NAVIGATION_VARIATIONS.length)];
    }

    /**
     * 获取动画变体
     */
    public String getAnimationVariation() {
        return ANIMATION_VARIATIONS[random.nextInt(ANIMATION_VARIATIONS.length)];
    }

    /**
     * 获取组件风格变体
     */
    public String getComponentStyle(String componentType) {
        String[] styles = COMPONENT_STYLES.get(componentType);
        if (styles != null && styles.length > 0) {
            return styles[random.nextInt(styles.length)];
        }
        return "default";
    }

    /**
     * 生成完整的UI变体配置
     */
    public Map<String, Object> generateUIVariations() {
        Map<String, Object> variations = new HashMap<>();

        // 主题
        String theme = getThemeVariation();
        variations.put("theme", theme);

        // 颜色调色板
        String[] palette = getColorPalette(theme);
        variations.put("primary_color", palette[0]);
        variations.put("secondary_color", palette[1]);
        variations.put("accent_color", palette[2]);
        variations.put("background_color", palette[4]);
        variations.put("text_color", palette[5]);

        // 布局
        variations.put("layout_style", getLayoutVariation());

        // 导航
        variations.put("navigation_style", getNavigationVariation());

        // 动画
        variations.put("animation_style", getAnimationVariation());

        // 组件风格
        variations.put("button_style", getComponentStyle("button"));
        variations.put("card_style", getComponentStyle("card"));
        variations.put("input_style", getComponentStyle("input"));
        variations.put("dialog_style", getComponentStyle("dialog"));
        variations.put("list_item_style", getComponentStyle("list_item"));

        return variations;
    }

    /**
     * 打印UI变体配置
     */
    public void printUIVariations(Map<String, Object> variations) {
        System.out.println("========================================");
        System.out.println("UI变体配置");
        System.out.println("========================================");

        System.out.println("主题: " + variations.get("theme"));
        System.out.println("\n颜色配置:");
        System.out.println("  主色: " + variations.get("primary_color"));
        System.out.println("  副色: " + variations.get("secondary_color"));
        System.out.println("  强调色: " + variations.get("accent_color"));
        System.out.println("  背景色: " + variations.get("background_color"));
        System.out.println("  文本色: " + variations.get("text_color"));

        System.out.println("\n布局风格: " + variations.get("layout_style"));
        System.out.println("导航风格: " + variations.get("navigation_style"));
        System.out.println("动画风格: " + variations.get("animation_style"));

        System.out.println("\n组件风格:");
        System.out.println("  按钮: " + variations.get("button_style"));
        System.out.println("  卡片: " + variations.get("card_style"));
        System.out.println("  输入框: " + variations.get("input_style"));
        System.out.println("  对话框: " + variations.get("dialog_style"));
        System.out.println("  列表项: " + variations.get("list_item_style"));

        System.out.println("========================================");
    }
}
