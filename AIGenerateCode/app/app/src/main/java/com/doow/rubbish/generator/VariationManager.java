package com.doow.rubbish.generator;

import java.util.HashMap;
import java.util.Map;


public class VariationManager {
    private static VariationManager instance;
    private long currentSeed;
    private Map<String, Object> variations;


    private static final String[] FEATURE_MODULES = {
        "database", "cache", "file",
        "log", "performance",
        "animation", "chart", "graph", "timer",
        "calculator", "converter", "notepad", "todo",
        "flashlight", "compass", "level", "ruler", "protractor",
        "colorpicker", "drawing", "painting", "sketching",
        "puzzle", "game", "quiz", "trivia", "crossword",
        "dictionary", "encyclopedia", "reference", "education"
    };


    private static final String[] ARCHITECTURE_PATTERNS = {
        "mvvm", "mvp", "mvi", "clean_architecture", "mvvm_clean",
        "mvc", "mvvm_livedata", "mvvm_stateflow", "mvvm_rxjava",
        "mvp_retrofit", "mvi_reactive", "clean_mvvm", "clean_mvp"
    };


    private static final String[] UI_STYLES = {
        "material", "minimalist", "flat", "neumorphism", "glassmorphism",
        "skeuomorphic", "retro", "cyberpunk", "dark_mode", "light_mode",
        "material3", "ios_style", "windows_style", "macos_style",
        "brutalism", "glassmorphism_dark", "neumorphism_light",
        "solarized", "dracula", "nord", "gruvbox", "monokai"
    };


    private static final String[] STORAGE_TYPES = {
        "room", "sqlite", "shared_prefs", "file", "memory_cache",
        "datastore", "realm", "objectbox", "green_dao", "orm_lite",
        "sqlbrite", "dbflow", "cupboard", "ormlite", "activeandroid",
        "sugarorm", "snappydb", "realm_encrypted", "sqlcipher", "papercup"
    };

    // 预定义的异步处理方式（增强版）
    private static final String[] ASYNC_HANDLERS = {
        "coroutines", "rxjava", "livedata", "callback", "thread_pool",
        "rxjava2", "rxjava3", "flow", "stateflow", "sharedflow",
        "guava", "executors", "asynctask", "handler", "looper"
    };

    // 预定义的包名前缀（空字符串表示不使用前缀）
    private static final String[] PACKAGE_PREFIXES = {
        "", "app", "myapp", "demo", "sample", "test",
        "example", "tutorial", "practice", "project", "work"
    };

    // 预定义的应用名后缀（空字符串表示不使用后缀）
    private static final String[] APP_NAME_SUFFIXES = {
        "", "App", "Application", "Mobile", "Pro", "Lite",
        "Plus", "Express", "Fast", "Quick", "Smart"
    };

    private VariationManager() {
        variations = new HashMap<>();
    }

    public static synchronized VariationManager getInstance() {
        if (instance == null) {
            instance = new VariationManager();
        }
        return instance;
    }

    /**
     * 设置当前生成的种子
     * @param seed 随机种子
     */
    public void setSeed(long seed) {
        this.currentSeed = seed;
        variations.clear();
        generateVariations();
    }

    /**
     * 获取当前种子
     * @return 当前种子
     */
    public long getCurrentSeed() {
        return currentSeed;
    }

    /**
     * 生成所有变体配置
     */
    private void generateVariations() {
        EnhancedRandomUtils.setSeed(currentSeed);

        // 随机选择架构模式
        variations.put("architecture", randomChoice(ARCHITECTURE_PATTERNS));

        // 随机选择UI风格
        variations.put("ui_style", randomChoice(UI_STYLES));

        // 随机选择数据存储方式
        variations.put("storage", randomChoice(STORAGE_TYPES));

        // 随机选择异步处理方式
        variations.put("async_handler", randomChoice(ASYNC_HANDLERS));

        // 随机选择功能模块（3-8个）
        int featureCount = EnhancedRandomUtils.between(3, 8);
        String[] selectedFeatures = new String[featureCount];
        for (int i = 0; i < featureCount; i++) {
            selectedFeatures[i] = randomChoice(FEATURE_MODULES);
        }
        variations.put("features", selectedFeatures);

        // 随机生成包名前缀（可能为空）
        String packagePrefix = randomChoice(PACKAGE_PREFIXES);
        variations.put("package_prefix", packagePrefix);

        // 随机生成应用名后缀（可能为空）
        String appNameSuffix = randomChoice(APP_NAME_SUFFIXES);
        variations.put("app_name_suffix", appNameSuffix);

        // 随机生成主题色
        variations.put("primary_color", generateColor());
        variations.put("secondary_color", generateColor());
        variations.put("accent_color", generateColor());

        // 随机生成布局偏好
        variations.put("preferred_layout", generateLayoutPreference());

        // 随机生成导航模式
        variations.put("navigation_mode", generateNavigationMode());
    }

    /**
     * 获取变体值
     * @param key 变体键
     * @return 变体值
     */
    @SuppressWarnings("unchecked")
    public <T> T getVariation(String key) {
        return (T) variations.get(key);
    }

    /**
     * 从数组中随机选择一个元素
     */
    private <T> T randomChoice(T[] array) {
        return array[EnhancedRandomUtils.between(0, array.length - 1)];
    }

    /**
     * 生成颜色
     */
    private String generateColor() {
        return EnhancedRandomUtils.generateHexColor();
    }

    /**
     * 生成布局偏好
     */
    private String generateLayoutPreference() {
        String[] preferences = {
            "constraint_layout", "linear_layout", "relative_layout",
            "frame_layout", "coordinator_layout", "mixed"
        };
        return randomChoice(preferences);
    }

    /**
     * 生成导航模式（增强版）
     */
    private String generateNavigationMode() {
        String[] modes = {
            "bottom_navigation", "drawer_navigation", "tab_navigation",
            "nested_navigation", "mixed_navigation",
            "toolbar_navigation", "actionbar_navigation", "viewpager_navigation",
            "pager_tab_navigation", "bottom_sheet_navigation", "floating_navigation"
        };
        return randomChoice(modes);
    }

    /**
     * 打印当前变体配置
     */
    public void printVariations() {
        System.out.println("========================================");
        System.out.println("当前变体配置");
        System.out.println("========================================");
        System.out.println("种子: " + currentSeed);
        System.out.println("架构模式: " + getVariation("architecture"));
        System.out.println("UI风格: " + getVariation("ui_style"));
        System.out.println("数据存储: " + getVariation("storage"));
        System.out.println("异步处理: " + getVariation("async_handler"));
        System.out.println("功能模块: " + String.join(", ", getVariation("features")));
        System.out.println("========================================");
    }
}
