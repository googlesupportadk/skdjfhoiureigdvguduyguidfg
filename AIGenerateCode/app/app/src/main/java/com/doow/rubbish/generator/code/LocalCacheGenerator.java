package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocalCacheGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] CACHE_TYPES = {
            "memory", "disk", "lru", "two_level",
            "fifo", "lfu", "arc", "clock",
            "random_replacement", "adaptive", "segmented", "partitioned"
    };

    private static final int[] MAX_SIZES = {
            100, 200, 500, 1000, 2000, 5000,
            10000, 20000, 50000, 100000, 200000, 500000
    };

    private static final long[] EXPIRY_TIMES = {
            60000L, 300000L, 600000L, 1800000L, 3600000L,
            7200000L, 86400000L, 604800000L, 2592000000L, 3153600000L
    };

    private static final String[] FIELD_TYPES = {
            "int", "long", "float", "double", "boolean", "String", "Object"
    };

    public LocalCacheGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成缓存类");

        String storageType = variationManager.getVariation("storage");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Cache");
            generateCacheClass(className, storageType, asyncHandler);
        }
    }

    private void generateCacheClass(String className, String storageType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("cache"));

        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("android.util.LruCache"));

        if (storageType.contains("shared_prefs")) {
            sb.append(generateImportStatement("android.content.SharedPreferences"));
        }

        sb.append(generateImportStatement(packageName + ".model.*"));
        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        int maxSize = MAX_SIZES[RandomUtils.between(0, MAX_SIZES.length - 1)];
        long expiryTime = EXPIRY_TIMES[RandomUtils.between(0, EXPIRY_TIMES.length - 1)];
        String cacheType = CACHE_TYPES[RandomUtils.between(0, CACHE_TYPES.length - 1)];

        sb.append("public class ").append(className).append(" {\n\n");

        // 添加常量
        sb.append("    private static final String TAG = \"").append(className).append("\");\n");
        sb.append("    private static final int MAX_SIZE = ").append(maxSize).append(";\n");
        sb.append("    private static final long EXPIRY_TIME = ").append(expiryTime).append(";\n");
        sb.append("    private static final String CACHE_TYPE = \"").append(cacheType).append("\");\n\n");

        // 随机生成多个字段
        int fieldCount = RandomUtils.between(3, 8);
        List<String> fieldNames = new ArrayList<>();
        for (int i = 0; i < fieldCount; i++) {
            String fieldType = FIELD_TYPES[RandomUtils.between(0, FIELD_TYPES.length - 1)];
            String fieldName = RandomUtils.generateVariableName(fieldType);
            fieldNames.add(fieldName);

            // 随机决定是否为静态字段
            if (RandomUtils.randomBoolean()) {
                sb.append("    private static final ").append(fieldType).append(" ").append(fieldName);
                // 生成初始值
                sb.append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            } else {
                sb.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n");
            }
        }

        // 添加缓存相关字段
        sb.append("    private Context context;\n");
        sb.append("    private LruCache<String, Object> memoryCache;\n");
        sb.append("    private java.util.Map<String, Long> expiryMap;\n\n");

        // 随机决定是否使用SharedPreferences
        boolean useSharedPreferences = RandomUtils.randomBoolean();
        if (useSharedPreferences) {
            sb.append("    private SharedPreferences prefs;\n");
        }

        // 构造函数
        sb.append("    public ").append(className).append("(Context context) {\n");
        sb.append("        this.context = context.getApplicationContext();\n");
        sb.append("        this.memoryCache = new LruCache<>(MAX_SIZE);\n");
        sb.append("        this.expiryMap = new java.util.HashMap<>();\n");

        if (useSharedPreferences) {
            sb.append("        this.prefs = this.context.getSharedPreferences(\"").append(cacheType).append("_cache\", Context.MODE_PRIVATE);\n");
        }

        // 初始化非静态字段
        for (String fieldName : fieldNames) {
            if (!isStaticField(fieldName)) {
                String fieldType = getFieldType(fieldName);
                sb.append("        this.").append(fieldName).append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            }
        }
        sb.append("    }\n\n");

        // 生成put方法
        sb.append("    public void put(String key, Object value) {\n");
        sb.append("        if (key == null || value == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        // 根据随机条件决定是否使用辅助方法处理值\n");
        sb.append("        if (RandomUtils.randomBoolean()) {\n");
        sb.append("            value = processValue(value);\n");
        sb.append("        }\n");
        sb.append("        memoryCache.put(key, value);\n");
        sb.append("        expiryMap.put(key, System.currentTimeMillis() + EXPIRY_TIME);\n");

        if (useSharedPreferences) {
            sb.append("        prefs.edit().putString(key, value.toString()).apply();\n");
        }

        sb.append("        Log.d(TAG, \"Cached: \" + key);\n");
        sb.append("    }\n\n");

        // 生成get方法
        sb.append("    public Object get(String key) {\n");
        sb.append("        if (key == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        if (memoryCache.containsKey(key)) {\n");
        sb.append("            Long expiry = expiryMap.get(key);\n");
        sb.append("            if (expiry != null && System.currentTimeMillis() < expiry) {\n");
        sb.append("                // 根据随机条件决定是否使用辅助方法处理值\n");
        sb.append("                if (RandomUtils.randomBoolean()) {\n");
        sb.append("                    return processValue(memoryCache.get(key));\n");
        sb.append("                }\n");
        sb.append("                return memoryCache.get(key);\n");
        sb.append("            } else {\n");
        sb.append("            remove(key);\n");
        sb.append("            }\n");
        sb.append("        }\n");

        if (useSharedPreferences) {
            sb.append("        String value = prefs.getString(key, null);\n");
            sb.append("        if (value != null) {\n");
            sb.append("            memoryCache.put(key, value);\n");
            sb.append("            expiryMap.put(key, System.currentTimeMillis() + EXPIRY_TIME);\n");
            sb.append("        }\n");
            sb.append("        return value;\n");
        } else {
            sb.append("        return null;\n");
        }

        sb.append("    }\n\n");

        // 生成remove方法
        sb.append("    public void remove(String key) {\n");
        sb.append("        if (key == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        memoryCache.remove(key);\n");
        sb.append("        expiryMap.remove(key);\n");

        if (useSharedPreferences) {
            sb.append("        prefs.edit().remove(key).apply();\n");
        }

        sb.append("    }\n\n");

        // 生成clear方法
        sb.append("    public void clear() {\n");
        sb.append("        memoryCache.evictAll();\n");
        sb.append("        expiryMap.clear();\n");

        if (useSharedPreferences) {
            sb.append("        prefs.edit().clear().apply();\n");
        }

        sb.append("    }\n\n");

        // 生成size方法
        sb.append("    public int size() {\n");
        sb.append("        return memoryCache.size();\n");
        sb.append("    }\n\n");

        // 随机生成辅助方法
        for (String fieldName : fieldNames) {
            String fieldType = getFieldType(fieldName);

            if (fieldType.equals("int") || fieldType.equals("long") || fieldType.equals("float") || fieldType.equals("double")) {
                sb.append("    private ").append(fieldType).append(" calculate").append(capitalize(fieldName)).append("() {\n");
                sb.append("        ").append(fieldType).append(" result = ").append(fieldName).append(";\n");
                sb.append("        // 随机计算逻辑\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            result += ").append(fieldName).append(";\n");
                sb.append("        } else {\n");
                sb.append("            result *= 2;\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Calculated ").append(fieldName).append(": \" + result);\n");
                sb.append("        return result;\n");
                sb.append("    }\n\n");
            } else if (fieldType.equals("boolean")) {
                sb.append("    private boolean validate").append(capitalize(fieldName)).append("() {\n");
                sb.append("        boolean isValid = ").append(fieldName).append(";\n");
                sb.append("        // 随机验证逻辑\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            isValid = !isValid;\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Validated ").append(fieldName).append(": \" + isValid);\n");
                sb.append("        return isValid;\n");
                sb.append("    }\n\n");
            } else {
                sb.append("    private void process").append(capitalize(fieldName)).append("() {\n");
                sb.append("        // 随机处理逻辑\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            ").append(fieldName).append(" = \"processed_\" + ").append(fieldName).append(";\n");
                sb.append("        } else {\n");
                sb.append("            ").append(fieldName).append(" = String.valueOf(").append(fieldName).append(".hashCode());\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Processed ").append(fieldName).append(": \" + ").append(fieldName).append(");\n");
                sb.append("    }\n\n");
            }
        }

        // 生成统计方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void logCacheStats() {\n");
            sb.append("        Log.d(TAG, \"Cache stats - Size: \" + size() + \", Max Size: \" + MAX_SIZE);\n");
            sb.append("        Log.d(TAG, \"Cache type: \" + CACHE_TYPE);\n");

            // 随机调用一些辅助方法
            int methodCallCount = RandomUtils.between(1, 3);
            for (int i = 0; i < methodCallCount; i++) {
                int methodIndex = RandomUtils.between(0, fieldNames.size() - 1);
                String fieldName = fieldNames.get(methodIndex);
                String fieldType = getFieldType(fieldName);

                if (fieldType.equals("int") || fieldType.equals("long") || fieldType.equals("float") || fieldType.equals("double")) {
                    sb.append("        calculate").append(capitalize(fieldName)).append("();\n");
                } else if (fieldType.equals("boolean")) {
                    sb.append("        validate").append(capitalize(fieldName)).append("();\n");
                } else {
                    sb.append("        process").append(capitalize(fieldName)).append("();\n");
                }
            }

            sb.append("    }\n\n");
        }

        // 生成值处理方法
        sb.append("    private Object processValue(Object value) {\n");
        sb.append("        if (value == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        // 随机处理逻辑\n");
        sb.append("        if (RandomUtils.randomBoolean()) {\n");
        sb.append("            return value.toString();\n");
        sb.append("        } else {\n");
        sb.append("            return value.hashCode();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "cache");
    }

    // 辅助方法：生成初始值
    private String generateInitialValue(String type) {
        switch (type) {
            case "int":
                return "RandomUtils.between(0, 100)";
            case "long":
                return "RandomUtils.between(0L, 1000L)";
            case "float":
                return "(float) RandomUtils.nextDouble(0.0, 100.0)";
            case "double":
                return "RandomUtils.nextDouble(0.0, 100.0)";
            case "boolean":
                return "RandomUtils.randomBoolean()";
            case "String":
                return "RandomUtils.generateName(\"value\")";
            case "Object":
                return "new Object()";
            default:
                return "null";
        }
    }

    // 辅助方法：判断是否为静态字段
    private boolean isStaticField(String fieldName) {
        return fieldName.startsWith("static_");
    }

    // 辅助方法：获取字段类型
    private String getFieldType(String fieldName) {
        if (fieldName.endsWith("int")) {
            return "int";
        } else if (fieldName.endsWith("long")) {
            return "long";
        } else if (fieldName.endsWith("float")) {
            return "float";
        } else if (fieldName.endsWith("double")) {
            return "double";
        } else if (fieldName.endsWith("boolean")) {
            return "boolean";
        } else if (fieldName.toLowerCase().endsWith("string")) {
            return "String";
        } else {
            return "Object";
        }
    }

    // 辅助方法：首字母大写
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
