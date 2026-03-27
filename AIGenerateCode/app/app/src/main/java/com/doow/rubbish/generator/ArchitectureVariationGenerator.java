package com.doow.rubbish.generator;

import java.util.*;


public class ArchitectureVariationGenerator {

    private static ArchitectureVariationGenerator instance;
    private Random random;


    private static final String[] ARCHITECTURE_PATTERNS = {
        "mvvm", "mvp", "mvi", "clean_architecture", "mvvm_clean",
        "mvc", "mvvm_livedata", "mvvm_stateflow", "mvvm_rxjava",
        "mvp_retrofit", "mvi_reactive", "clean_mvvm", "clean_mvp",
        "mvvm_hilt", "mvp_dagger", "mvi_koin", "clean_mvvm_hilt"
    };


    private static final String[] DI_FRAMEWORKS = {
        "hilt", "dagger", "koin", "kodein", "manual"
    };


    private static final String[] ASYNC_FRAMEWORKS = {
        "coroutines", "rxjava", "livedata", "callback", "thread_pool"
    };


    private static final String[] NETWORK_LIBRARIES = {
        "retrofit", "okhttp", "volley", "asynchttp", "ktor"
    };


    private static final String[] DATABASE_LIBRARIES = {
        "room", "sqlite", "datastore", "realm", "objectbox"
    };


    private static final String[] IMAGE_LOADING_LIBRARIES = {
        "glide", "picasso", "coil", "fresco"
    };


    private static final String[] LOGGING_LIBRARIES = {
        "timber", "logger", "xlog", "logcat"
    };

    // JSON解析库变体
    private static final String[] JSON_LIBRARIES = {
        "gson", "moshi", "jackson", "kotlinx_serialization"
    };

    // 架构层级配置
    private static final Map<String, String[]> LAYER_CONFIGURATIONS = new HashMap<>();

    static {
        // MVVM 层级配置
        LAYER_CONFIGURATIONS.put("mvvm", new String[]{
            "presentation", "domain", "data"
        });

        // MVP 层级配置
        LAYER_CONFIGURATIONS.put("mvp", new String[]{
            "presentation", "domain", "data"
        });

        // MVI 层级配置
        LAYER_CONFIGURATIONS.put("mvi", new String[]{
            "presentation", "domain", "data"
        });

        // Clean Architecture 层级配置
        LAYER_CONFIGURATIONS.put("clean_architecture", new String[]{
            "presentation", "domain", "data", "di"
        });
    }

    // 包结构配置
    private static final Map<String, String[]> PACKAGE_STRUCTURES = new HashMap<>();

    static {
        // 功能包结构
        PACKAGE_STRUCTURES.put("feature", new String[]{
            "feature_home", "feature_profile", "feature_settings"
        });

        // 层级包结构
        PACKAGE_STRUCTURES.put("layer", new String[]{
            "presentation", "domain", "data"
        });

        // 混合包结构
        PACKAGE_STRUCTURES.put("mixed", new String[]{
            "presentation_home", "presentation_profile",
            "domain_home", "domain_profile",
            "data_home", "data_profile"
        });
    }

    private ArchitectureVariationGenerator() {
        random = new Random();
    }

    public static synchronized ArchitectureVariationGenerator getInstance() {
        if (instance == null) {
            instance = new ArchitectureVariationGenerator();
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
     * 获取架构模式
     */
    public String getArchitecturePattern() {
        return ARCHITECTURE_PATTERNS[random.nextInt(ARCHITECTURE_PATTERNS.length)];
    }

    /**
     * 获取依赖注入框架
     */
    public String getDIFramework() {
        return DI_FRAMEWORKS[random.nextInt(DI_FRAMEWORKS.length)];
    }

    /**
     * 获取异步处理框架
     */
    public String getAsyncFramework() {
        return ASYNC_FRAMEWORKS[random.nextInt(ASYNC_FRAMEWORKS.length)];
    }

    /**
     * 获取网络库
     */
    public String getNetworkLibrary() {
        return NETWORK_LIBRARIES[random.nextInt(NETWORK_LIBRARIES.length)];
    }

    /**
     * 获取数据库库
     */
    public String getDatabaseLibrary() {
        return DATABASE_LIBRARIES[random.nextInt(DATABASE_LIBRARIES.length)];
    }

    /**
     * 获取图片加载库
     */
    public String getImageLoadingLibrary() {
        return IMAGE_LOADING_LIBRARIES[random.nextInt(IMAGE_LOADING_LIBRARIES.length)];
    }

    /**
     * 获取日志库
     */
    public String getLoggingLibrary() {
        return LOGGING_LIBRARIES[random.nextInt(LOGGING_LIBRARIES.length)];
    }

    /**
     * 获取JSON解析库
     */
    public String getJSONLibrary() {
        return JSON_LIBRARIES[random.nextInt(JSON_LIBRARIES.length)];
    }

    /**
     * 获取层级配置
     */
    public String[] getLayerConfiguration(String architecture) {
        String[] layers = LAYER_CONFIGURATIONS.get(architecture);
        if (layers != null) {
            return layers.clone();
        }
        return new String[]{"presentation", "domain", "data"};
    }

    /**
     * 获取包结构
     */
    public String[] getPackageStructure() {
        String[] structures = {
            "feature", "layer", "mixed"
        };
        String structureType = structures[random.nextInt(structures.length)];
        return PACKAGE_STRUCTURES.get(structureType);
    }

    /**
     * 生成完整的架构变体配置
     */
    public Map<String, Object> generateArchitectureVariations() {
        Map<String, Object> variations = new HashMap<>();

        // 架构模式
        String architecture = getArchitecturePattern();
        variations.put("architecture", architecture);

        // 依赖注入
        variations.put("di_framework", getDIFramework());

        // 异步处理
        variations.put("async_framework", getAsyncFramework());

        // 网络库
        variations.put("network_library", getNetworkLibrary());

        // 数据库库
        variations.put("database_library", getDatabaseLibrary());

        // 图片加载库
        variations.put("image_library", getImageLoadingLibrary());

        // 日志库
        variations.put("logging_library", getLoggingLibrary());

        // JSON解析库
        variations.put("json_library", getJSONLibrary());

        // 层级配置
        variations.put("layers", getLayerConfiguration(architecture));

        // 包结构
        variations.put("package_structure", getPackageStructure());

        return variations;
    }

    /**
     * 打印架构变体配置
     */
    public void printArchitectureVariations(Map<String, Object> variations) {
        System.out.println("========================================");
        System.out.println("架构变体配置");
        System.out.println("========================================");

        System.out.println("架构模式: " + variations.get("architecture"));
        System.out.println("\n依赖库:");
        System.out.println("  依赖注入: " + variations.get("di_framework"));
        System.out.println("  异步处理: " + variations.get("async_framework"));
        System.out.println("  网络: " + variations.get("network_library"));
        System.out.println("  数据库: " + variations.get("database_library"));
        System.out.println("  图片加载: " + variations.get("image_library"));
        System.out.println("  日志: " + variations.get("logging_library"));
        System.out.println("  JSON: " + variations.get("json_library"));

        System.out.println("\n层级配置:");
        String[] layers = (String[]) variations.get("layers");
        System.out.println("  " + String.join(" -> ", layers));

        System.out.println("\n包结构:");
        String[] packages = (String[]) variations.get("package_structure");
        System.out.println("  " + String.join(", ", packages));

        System.out.println("========================================");
    }
}
