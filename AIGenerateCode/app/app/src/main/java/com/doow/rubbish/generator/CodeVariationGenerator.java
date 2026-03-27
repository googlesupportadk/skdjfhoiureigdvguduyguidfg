package com.doow.rubbish.generator;

import java.util.*;

/**
 * 代码变体生成器 - 第二步优化
 * 负责生成不同的代码变体，确保每个项目的代码实现方式不同
 */
public class CodeVariationGenerator {

    private static CodeVariationGenerator instance;
    private Random random;

    // 代码实现变体
    private static final Map<String, String[]> IMPLEMENTATION_VARIATIONS = new HashMap<>();

    static {
        // 数据库实现变体
        IMPLEMENTATION_VARIATIONS.put("database", new String[]{
            "room", "sqlite", "datastore", "realm"
        });

        // 缓存实现变体
        IMPLEMENTATION_VARIATIONS.put("cache", new String[]{
            "memory", "disk", "memory_disk", "lru", "guava"
        });

        // 异步处理实现变体
        IMPLEMENTATION_VARIATIONS.put("async", new String[]{
            "coroutines", "rxjava", "livedata", "callback", "thread_pool"
        });

        // 依赖注入实现变体
        IMPLEMENTATION_VARIATIONS.put("di", new String[]{
            "hilt", "dagger", "manual", "koin", "kodein"
        });

        // 网络请求实现变体
        IMPLEMENTATION_VARIATIONS.put("network", new String[]{
            "retrofit", "okhttp", "volley", "asynchttp"
        });

        // 图片加载实现变体
        IMPLEMENTATION_VARIATIONS.put("image", new String[]{
            "glide", "picasso", "coil", "fresco"
        });

        // 日志实现变体
        IMPLEMENTATION_VARIATIONS.put("log", new String[]{
            "timber", "logger", "xlog", "logcat"
        });

        // JSON解析实现变体
        IMPLEMENTATION_VARIATIONS.put("json", new String[]{
            "gson", "moshi", "jackson", "kotlinx_serialization"
        });
    }

    // 代码风格变体
    private static final Map<String, String[]> STYLE_VARIATIONS = new HashMap<>();

    static {
        // 命名风格变体
        STYLE_VARIATIONS.put("naming", new String[]{
            "camelCase", "snake_case", "kebab-case", "PascalCase"
        });

        // 注释风格变体
        STYLE_VARIATIONS.put("comment", new String[]{
            "kdoc", "javadoc", "minimal", "detailed"
        });

        // 代码组织变体
        STYLE_VARIATIONS.put("organization", new String[]{
            "flat", "nested", "feature_based", "layer_based"
        });

        // 错误处理变体
        STYLE_VARIATIONS.put("error_handling", new String[]{
            "try_catch", "result", "either", "sealed_class"
        });
    }

    // 代码复杂度变体
    private static final Map<String, int[]> COMPLEXITY_VARIATIONS = new HashMap<>();

    static {
        // 方法行数范围
        COMPLEXITY_VARIATIONS.put("method_lines", new int[]{10, 50});

        // 类方法数范围
        COMPLEXITY_VARIATIONS.put("class_methods", new int[]{5, 20});

        // 嵌套深度范围
        COMPLEXITY_VARIATIONS.put("nesting_depth", new int[]{1, 5});

        // 参数数量范围
        COMPLEXITY_VARIATIONS.put("parameter_count", new int[]{1, 6});
    }

    private CodeVariationGenerator() {
        random = new Random();
    }

    public static synchronized CodeVariationGenerator getInstance() {
        if (instance == null) {
            instance = new CodeVariationGenerator();
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
     * 获取实现变体
     */
    public String getImplementationVariation(String type) {
        String[] variations = IMPLEMENTATION_VARIATIONS.get(type);
        if (variations != null && variations.length > 0) {
            return variations[random.nextInt(variations.length)];
        }
        return "default";
    }

    /**
     * 获取风格变体
     */
    public String getStyleVariation(String type) {
        String[] variations = STYLE_VARIATIONS.get(type);
        if (variations != null && variations.length > 0) {
            return variations[random.nextInt(variations.length)];
        }
        return "default";
    }

    /**
     * 获取复杂度变体
     */
    public int getComplexityVariation(String type) {
        int[] range = COMPLEXITY_VARIATIONS.get(type);
        if (range != null && range.length == 2) {
            return random.nextInt(range[1] - range[0] + 1) + range[0];
        }
        return 0;
    }

    /**
     * 生成完整的代码变体配置
     */
    public Map<String, Object> generateCodeVariations() {
        Map<String, Object> variations = new HashMap<>();

        // 实现变体
        variations.put("database_impl", getImplementationVariation("database"));
        variations.put("cache_impl", getImplementationVariation("cache"));
        variations.put("async_impl", getImplementationVariation("async"));
        variations.put("di_impl", getImplementationVariation("di"));
        variations.put("network_impl", getImplementationVariation("network"));
        variations.put("image_impl", getImplementationVariation("image"));
        variations.put("log_impl", getImplementationVariation("log"));
        variations.put("json_impl", getImplementationVariation("json"));

        // 风格变体
        variations.put("naming_style", getStyleVariation("naming"));
        variations.put("comment_style", getStyleVariation("comment"));
        variations.put("organization_style", getStyleVariation("organization"));
        variations.put("error_handling_style", getStyleVariation("error_handling"));

        // 复杂度变体
        variations.put("method_lines", getComplexityVariation("method_lines"));
        variations.put("class_methods", getComplexityVariation("class_methods"));
        variations.put("nesting_depth", getComplexityVariation("nesting_depth"));
        variations.put("parameter_count", getComplexityVariation("parameter_count"));

        return variations;
    }

    /**
     * 打印代码变体配置
     */
    public void printCodeVariations(Map<String, Object> variations) {
        System.out.println("========================================");
        System.out.println("代码变体配置");
        System.out.println("========================================");

        System.out.println("实现变体:");
        System.out.println("  数据库: " + variations.get("database_impl"));
        System.out.println("  缓存: " + variations.get("cache_impl"));
        System.out.println("  异步: " + variations.get("async_impl"));
        System.out.println("  依赖注入: " + variations.get("di_impl"));
        System.out.println("  网络: " + variations.get("network_impl"));
        System.out.println("  图片: " + variations.get("image_impl"));
        System.out.println("  日志: " + variations.get("log_impl"));
        System.out.println("  JSON: " + variations.get("json_impl"));

        System.out.println("\n风格变体:");
        System.out.println("  命名: " + variations.get("naming_style"));
        System.out.println("  注释: " + variations.get("comment_style"));
        System.out.println("  组织: " + variations.get("organization_style"));
        System.out.println("  错误处理: " + variations.get("error_handling_style"));

        System.out.println("\n复杂度变体:");
        System.out.println("  方法行数: " + variations.get("method_lines"));
        System.out.println("  类方法数: " + variations.get("class_methods"));
        System.out.println("  嵌套深度: " + variations.get("nesting_depth"));
        System.out.println("  参数数量: " + variations.get("parameter_count"));

        System.out.println("========================================");
    }
}
