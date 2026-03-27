package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class CacheGenerator extends BaseCodeGenerator {

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

    public CacheGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成缓存类");

        String storageType = variationManager.getVariation("storage");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(3, 8); i++) {
            String className = RandomUtils.generateClassName("Cache");
            generateCacheClass(className, storageType, asyncHandler);
        }
    }

    private void generateCacheClass(String className, String storageType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("cache"));

        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.concurrent.ConcurrentHashMap"));
        sb.append(generateImportStatement("java.util.concurrent.TimeUnit"));

        if (storageType.contains("room") || storageType.contains("sqlite")) {
            sb.append(generateImportStatement("androidx.room.Room"));
            sb.append(generateImportStatement("androidx.room.RoomDatabase"));
        } else if (storageType.contains("shared_prefs")) {
            sb.append(generateImportStatement("android.content.SharedPreferences"));
        }

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        } else if (asyncHandler.contains("rxjava")) {
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
        }

        sb.append(generateImportStatement(packageName + ".model.*"));
        sb.append("\n");

        sb.append("public class ").append(className).append(" {\n\n");

        int maxSize = MAX_SIZES[RandomUtils.between(0, MAX_SIZES.length - 1)];
        String cacheType = CACHE_TYPES[RandomUtils.between(0, CACHE_TYPES.length - 1)];

        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final int MAX_SIZE = ").append(maxSize).append(";\n");
        sb.append("    private static final String CACHE_TYPE = \"").append(cacheType).append("\";\n\n");

        // 随机决定是否使用过期时间
        if (RandomUtils.randomBoolean()) {
            sb.append("    private static final long DEFAULT_EXPIRE_TIME = TimeUnit.MINUTES.toMillis(30);\n");
            sb.append("    private Map<String, Long> expireTimes;\n\n");
        }

        // 随机决定是否使用访问计数
        if (RandomUtils.randomBoolean()) {
            sb.append("    private Map<String, Integer> accessCounts;\n\n");
        }

        // 随机决定是否使用线程安全
        if (RandomUtils.randomBoolean()) {
            sb.append("    private ConcurrentHashMap<String, Object> threadSafeCache;\n\n");
        }

        // 随机决定是否使用日志
        if (RandomUtils.randomBoolean()) {
            sb.append("    private boolean enableLogging = true;\n\n");
        }

        sb.append("    private Context context;\n");
        sb.append("    private SharedPreferences prefs;\n");
        sb.append("    private java.util.Map<String, Object> memoryCache;\n\n");

        sb.append("    public ").append(className).append("(Context context) {\n");
        sb.append("        this.context = context.getApplicationContext();\n");
        sb.append("        this.prefs = this.context.getSharedPreferences(\"").append(cacheType).append("_cache\", Context.MODE_PRIVATE);\n");
        sb.append("        this.memoryCache = new java.util.HashMap<>();\n\n");

        // 随机决定是否初始化过期时间
        if (RandomUtils.randomBoolean()) {
            sb.append("        this.expireTimes = new HashMap<>();\n");
        }

        // 随机决定是否初始化访问计数
        if (RandomUtils.randomBoolean()) {
            sb.append("        this.accessCounts = new HashMap<>();\n");
        }

        // 随机决定是否初始化线程安全缓存
        if (RandomUtils.randomBoolean()) {
            sb.append("        this.threadSafeCache = new ConcurrentHashMap<>();\n");
        }

        sb.append("    }\n\n");

        sb.append("    public void put(String key, Object value) {\n");
        sb.append("        if (key == null || value == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        memoryCache.put(key, value);\n");
        sb.append("        prefs.edit().putString(key, value.toString()).apply();\n\n");

        // 随机决定是否设置过期时间
        if (RandomUtils.randomBoolean()) {
            sb.append("        if (expireTimes != null) {\n");
            sb.append("            expireTimes.put(key, System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);\n");
            sb.append("        }\n");
        }

        // 随机决定是否记录访问
        if (RandomUtils.randomBoolean()) {
            sb.append("        if (accessCounts != null) {\n");
            sb.append("            accessCounts.put(key, accessCounts.getOrDefault(key, 0) + 1);\n");
            sb.append("        }\n");
        }

        sb.append("    }\n\n");

        sb.append("    public Object get(String key) {\n");
        sb.append("        if (key == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n\n");

        // 随机决定是否检查过期
        if (RandomUtils.randomBoolean()) {
            sb.append("        if (expireTimes != null && expireTimes.containsKey(key)) {\n");
            sb.append("            if (System.currentTimeMillis() > expireTimes.get(key)) {\n");
            sb.append("                remove(key);\n");
            sb.append("                return null;\n");
            sb.append("            }\n");
            sb.append("        }\n");
        }

        sb.append("        if (memoryCache.containsKey(key)) {\n");
        sb.append("            return memoryCache.get(key);\n");
        sb.append("        }\n");
        sb.append("        String value = prefs.getString(key, null);\n");
        sb.append("        if (value != null) {\n");
        sb.append("            memoryCache.put(key, value);\n");
        sb.append("        }\n");
        sb.append("        return value;\n");
        sb.append("    }\n\n");

        sb.append("    public void remove(String key) {\n");
        sb.append("        if (key == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        memoryCache.remove(key);\n");
        sb.append("        prefs.edit().remove(key).apply();\n\n");

        // 随机决定是否清除过期时间
        if (RandomUtils.randomBoolean()) {
            sb.append("        if (expireTimes != null) {\n");
            sb.append("            expireTimes.remove(key);\n");
            sb.append("        }\n");
        }

        // 随机决定是否清除访问计数
        if (RandomUtils.randomBoolean()) {
            sb.append("        if (accessCounts != null) {\n");
            sb.append("            accessCounts.remove(key);\n");
            sb.append("        }\n");
        }

        sb.append("    }\n\n");

        sb.append("    public void clear() {\n");
        sb.append("        memoryCache.clear();\n");
        sb.append("        prefs.edit().clear().apply();\n\n");

        // 随机决定是否清除过期时间
        if (RandomUtils.randomBoolean()) {
            sb.append("        if (expireTimes != null) {\n");
            sb.append("            expireTimes.clear();\n");
            sb.append("        }\n");
        }

        // 随机决定是否清除访问计数
        if (RandomUtils.randomBoolean()) {
            sb.append("        if (accessCounts != null) {\n");
            sb.append("            accessCounts.clear();\n");
            sb.append("        }\n");
        }

        sb.append("    }\n\n");

        sb.append("    public int size() {\n");
        sb.append("        return memoryCache.size();\n");
        sb.append("    }\n\n");

        // 随机生成3-8个方法
        int methodCount = RandomUtils.between(3, 8);
        for (int i = 0; i < methodCount; i++) {
            int methodType = RandomUtils.between(0, 6);
            String methodName = RandomUtils.generateMethodName("handle");

            switch (methodType) {
                case 0:
                    if (RandomUtils.randomBoolean()) {
                        generateExpireMethod(sb, methodName);
                    }
                    break;
                case 1:
                    if (RandomUtils.randomBoolean()) {
                        generateAccessMethod(sb, methodName);
                    }
                    break;
                case 2:
                    if (RandomUtils.randomBoolean()) {
                        generateThreadSafeMethod(sb, methodName);
                    }
                    break;
                case 3:
                    if (RandomUtils.randomBoolean()) {
                        generateLogMethod(sb, methodName);
                    }
                    break;
                case 4:
                    if (RandomUtils.randomBoolean()) {
                        generateBatchMethod(sb, methodName);
                    }
                    break;
                case 5:
                    if (RandomUtils.randomBoolean()) {
                        generateStatsMethod(sb, methodName);
                    }
                    break;
            }
        }

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "cache");
    }

    private void generateExpireMethod(StringBuilder sb, String methodName) {
        sb.append("    public void ").append(methodName).append("WithExpire(String key, Object value, long expireTime) {\n");
        sb.append("        if (key == null || value == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        put(key, value);\n");
        sb.append("        if (expireTimes != null) {\n");
        sb.append("            expireTimes.put(key, System.currentTimeMillis() + expireTime);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(methodName).append("CheckAndClear() {\n");
        sb.append("        if (expireTimes == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        long currentTime = System.currentTimeMillis();\n");
        sb.append("        expireTimes.entrySet().removeIf(entry -> currentTime > entry.getValue());\n");
        sb.append("    }\n\n");
    }

    private void generateAccessMethod(StringBuilder sb, String methodName) {
        sb.append("    public int ").append(methodName).append("GetCount(String key) {\n");
        sb.append("        if (accessCounts == null) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        return accessCounts.getOrDefault(key, 0);\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(methodName).append("ResetCount(String key) {\n");
        sb.append("        if (accessCounts != null) {\n");
        sb.append("            accessCounts.put(key, 0);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateThreadSafeMethod(StringBuilder sb, String methodName) {
        sb.append("    public void ").append(methodName).append("Put(String key, Object value) {\n");
        sb.append("        if (threadSafeCache == null) {\n");
        sb.append("            put(key, value);\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        threadSafeCache.put(key, value);\n");
        sb.append("    }\n\n");

        sb.append("    public Object ").append(methodName).append("Get(String key) {\n");
        sb.append("        if (threadSafeCache == null) {\n");
        sb.append("            return get(key);\n");
        sb.append("        }\n");
        sb.append("        return threadSafeCache.get(key);\n");
        sb.append("    }\n\n");
    }

    private void generateLogMethod(StringBuilder sb, String methodName) {
        sb.append("    public void ").append(methodName).append("Enable(boolean enable) {\n");
        sb.append("        this.enableLogging = enable;\n");
        sb.append("    }\n\n");

        sb.append("    private void ").append(methodName).append("Operation(String operation, String key) {\n");
        sb.append("        if (enableLogging) {\n");
        sb.append("            Log.d(TAG, \"Cache operation: \" + operation + \", key: \" + key);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateBatchMethod(StringBuilder sb, String methodName) {
        sb.append("    public void ").append(methodName).append("PutAll(Map<String, Object> entries) {\n");
        sb.append("        if (entries == null || entries.isEmpty()) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        for (Map.Entry<String, Object> entry : entries.entrySet()) {\n");
        sb.append("            put(entry.getKey(), entry.getValue());\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public Map<String, Object> ").append(methodName).append("GetAll(java.util.Collection<String> keys) {\n");
        sb.append("        Map<String, Object> result = new HashMap<>();\n");
        sb.append("        if (keys == null || keys.isEmpty()) {\n");
        sb.append("            return result;\n");
        sb.append("        }\n");
        sb.append("        for (String key : keys) {\n");
        sb.append("            Object value = get(key);\n");
        sb.append("            if (value != null) {\n");
        sb.append("                result.put(key, value);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");
    }

    private void generateStatsMethod(StringBuilder sb, String methodName) {
        sb.append("    public Map<String, Object> ").append(methodName).append("GetStats() {\n");
        sb.append("        Map<String, Object> stats = new HashMap<>();\n");
        sb.append("        stats.put(\"size\", memoryCache.size());\n");
        sb.append("        stats.put(\"maxSize\", MAX_SIZE);\n");
        sb.append("        stats.put(\"cacheType\", CACHE_TYPE);\n");
        sb.append("        if (accessCounts != null) {\n");
        sb.append("            stats.put(\"totalAccess\", accessCounts.values().stream().mapToInt(Integer::intValue).sum());\n");
        sb.append("        }\n");
        sb.append("        if (expireTimes != null) {\n");
        sb.append("            stats.put(\"expireEntries\", expireTimes.size());\n");
        sb.append("        }\n");
        sb.append("        return stats;\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(methodName).append("Reset() {\n");
        sb.append("        if (accessCounts != null) {\n");
        sb.append("            accessCounts.clear();\n");
        sb.append("        }\n");
        sb.append("        if (expireTimes != null) {\n");
        sb.append("            expireTimes.clear();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }
}
