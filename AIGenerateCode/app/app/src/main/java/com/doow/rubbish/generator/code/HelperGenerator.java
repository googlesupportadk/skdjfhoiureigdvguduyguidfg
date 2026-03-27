package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class HelperGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] HELPER_TYPES = {
            "DataHelper", "ViewHelper", "NetworkHelper", "StorageHelper", "ValidationHelper",
            "CacheHelper", "DatabaseHelper", "FileHelper", "PreferenceHelper", "SecurityHelper",
            "EncryptionHelper", "CompressionHelper", "ImageHelper", "StringHelper", "DateHelper",
            "FormatHelper", "ConvertHelper", "ParseHelper", "SerializeHelper", "JsonHelper"
    };

    private static final String[] METHOD_TYPES = {
            "get", "set", "validate", "convert", "transform",
            "parse", "format", "serialize", "deserialize", "encode", "decode",
            "compress", "decompress", "encrypt", "decrypt", "hash", "verify"
    };

    private static final String[] DATA_TYPES = {
            "String", "Integer", "Boolean", "Long", "Double", "Float", "byte[]"
    };

    private static final String[] ENCODING_TYPES = {
            "UTF-8", "UTF-16", "ISO-8859-1", "ASCII", "GBK"
    };

    private static final String[] HASH_ALGORITHMS = {
            "MD5", "SHA-1", "SHA-256", "SHA-512"
    };

    public HelperGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成Helper类");

        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 12); i++) {
            String className = RandomUtils.generateClassName("Helper");
            generateHelperClass(className, uiStyle, asyncHandler);
        }
    }

    private void generateHelperClass(String className, String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("helper"));

        // 基础导入
        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("android.content.SharedPreferences"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.concurrent.ConcurrentHashMap"));

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        } else if (asyncHandler.contains("rxjava")) {
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 功能标志 - 确保所有字段和方法都会被使用
        boolean useSingleton = RandomUtils.randomBoolean();
        boolean useCache = RandomUtils.randomBoolean();
        boolean useValidation = RandomUtils.randomBoolean();
        boolean useTransform = RandomUtils.randomBoolean();
        boolean useAsync = RandomUtils.randomBoolean();
        boolean useStatistics = RandomUtils.randomBoolean();
        boolean useEncoding = RandomUtils.randomBoolean();
        boolean useHashing = RandomUtils.randomBoolean();
        boolean useCompression = RandomUtils.randomBoolean();
        boolean useBatch = RandomUtils.randomBoolean();
        boolean useQueue = RandomUtils.randomBoolean();

        sb.append("public class ").append(className).append(" {\n\n");

        String helperType = HELPER_TYPES[RandomUtils.between(0, HELPER_TYPES.length - 1)];
        String methodType = METHOD_TYPES[RandomUtils.between(0, METHOD_TYPES.length - 1)];
        String dataType = DATA_TYPES[RandomUtils.between(0, DATA_TYPES.length - 1)];
        String encodingType = useEncoding ? ENCODING_TYPES[RandomUtils.between(0, ENCODING_TYPES.length - 1)] : "";
        String hashAlgorithm = useHashing ? HASH_ALGORITHMS[RandomUtils.between(0, HASH_ALGORITHMS.length - 1)] : "";

        // 基础常量
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String HELPER_TYPE = \"").append(helperType).append("\";\n");
        sb.append("    private static final String METHOD_TYPE = \"").append(methodType).append("\";\n");
        sb.append("    private static final String DATA_TYPE = \"").append(dataType).append("\";\n");

        if (useEncoding) {
            sb.append("    private static final String ENCODING_TYPE = \"").append(encodingType).append("\";\n");
        }

        if (useHashing) {
            sb.append("    private static final String HASH_ALGORITHM = \"").append(hashAlgorithm).append("\";\n");
        }

        if (useCache) {
            sb.append("    private static final int MAX_CACHE_SIZE = 100;\n");
            sb.append("    private static final long CACHE_TTL_MS = 3600000;\n");
        }

        if (useBatch) {
            sb.append("    private static final int BATCH_SIZE = 50;\n");
        }

        if (useQueue) {
            sb.append("    private static final int MAX_QUEUE_SIZE = 1000;\n");
        }

        sb.append("\n");

        // 单例字段
        if (useSingleton) {
            sb.append("    private static volatile ").append(className).append(" instance;\n\n");
        }

        // 缓存字段
        if (useCache) {
            sb.append("    private SharedPreferences sharedPreferences;\n");
            sb.append("    private Map<String, CacheEntry> memoryCache;\n");
            sb.append("    private boolean cacheEnabled = true;\n");
            sb.append("\n");
            sb.append("    private static class CacheEntry {\n");
            sb.append("        Object value;\n");
            sb.append("        long timestamp;\n");
            sb.append("\n");
            sb.append("        CacheEntry(Object value) {\n");
            sb.append("            this.value = value;\n");
            sb.append("            this.timestamp = System.currentTimeMillis();\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 统计字段
        if (useStatistics) {
            sb.append("    private int operationCount = 0;\n");
            sb.append("    private long lastOperationTime = 0;\n");
            sb.append("    private Map<String, Integer> operationTypeCount = new ConcurrentHashMap<>();\n\n");
        }

        // 队列字段
        if (useQueue) {
            sb.append("    private List<Runnable> operationQueue = new ArrayList<>();\n");
            sb.append("    private boolean queueProcessing = false;\n\n");
        }

        // 编码字段
        if (useEncoding) {
            sb.append("    private java.nio.charset.Charset charset;\n\n");
        }

        // 上下文字段
        sb.append("    private Context context;\n\n");

        // 构造方法
        sb.append("    public ").append(className).append("(Context context) {\n");
        sb.append("        this.context = context.getApplicationContext();\n");
        sb.append("        initialize();\n");
        sb.append("    }\n\n");

        // 初始化方法
        sb.append("    private void initialize() {\n");
        sb.append("        Log.d(TAG, \"Initializing ").append(className).append(" with type: \").append(HELPER_TYPE);\n");

        if (useCache) {
            sb.append("        initializeCache();\n");
        }

        if (useStatistics) {
            sb.append("        initializeStatistics();\n");
        }

        if (useEncoding) {
            sb.append("        initializeEncoding();\n");
        }

        if (useQueue) {
            sb.append("        initializeQueue();\n");
        }

        sb.append("    }\n\n");

        // 缓存初始化
        if (useCache) {
            sb.append("    private void initializeCache() {\n");
            sb.append("        sharedPreferences = context.getSharedPreferences(\"").append(className).append("_cache\", Context.MODE_PRIVATE);\n");
            sb.append("        memoryCache = new ConcurrentHashMap<>();\n");
            sb.append("        Log.d(TAG, \"Cache initialized\");\n");
            sb.append("    }\n\n");
        }

        // 统计初始化
        if (useStatistics) {
            sb.append("    private void initializeStatistics() {\n");
            sb.append("        operationCount = 0;\n");
            sb.append("        lastOperationTime = System.currentTimeMillis();\n");
            sb.append("        operationTypeCount.clear();\n");
            sb.append("        Log.d(TAG, \"Statistics initialized\");\n");
            sb.append("    }\n\n");
        }

        // 编码初始化
        if (useEncoding) {
            sb.append("    private void initializeEncoding() {\n");
            sb.append("        try {\n");
            sb.append("            charset = java.nio.charset.Charset.forName(ENCODING_TYPE);\n");
            sb.append("            Log.d(TAG, \"Encoding initialized: \" + ENCODING_TYPE);\n");
            sb.append("        } catch (Exception e) {\n");
            sb.append("            Log.e(TAG, \"Failed to initialize encoding\", e);\n");
            sb.append("            charset = java.nio.charset.StandardCharsets.UTF_8;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 队列初始化
        if (useQueue) {
            sb.append("    private void initializeQueue() {\n");
            sb.append("        operationQueue.clear();\n");
            sb.append("        queueProcessing = false;\n");
            sb.append("        Log.d(TAG, \"Queue initialized\");\n");
            sb.append("    }\n\n");
        }

        // 单例获取方法
        if (useSingleton) {
            sb.append("    public static ").append(className).append(" getInstance(Context context) {\n");
            sb.append("        if (instance == null) {\n");
            sb.append("            synchronized (").append(className).append(".class) {\n");
            sb.append("                if (instance == null) {\n");
            sb.append("                    instance = new ").append(className).append("(context);\n");
            sb.append("                }\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        return instance;\n");
            sb.append("    }\n\n");
        }

        // 基础方法
        sb.append("    public ").append(dataType).append(" ").append(methodType).append("Data(String input) {\n");
        sb.append("        if (input == null || input.isEmpty()) {\n");
        sb.append("            return getDefault").append(dataType).append("();\n");
        sb.append("        }\n");
        sb.append("        ").append(dataType).append(" result = ").append(methodType).append("Internal(input);\n");
        sb.append("        Log.d(TAG, \"").append(methodType).append(" data: \" + result);\n");

        if (useStatistics) {
            sb.append("        updateStatistics(\"").append(methodType).append("Data\");\n");
        }

        if (useCache) {
            sb.append("        cacheResult(input, result);\n");
        }

        sb.append("        return result;\n");
        sb.append("    }\n\n");

        // 内部处理方法
        sb.append("    private ").append(dataType).append(" ").append(methodType).append("Internal(String input) {\n");
        sb.append("        String processed = input.trim();\n");

        if (useEncoding) {
            sb.append("        processed = encodeString(processed);\n");
        }

        sb.append("        return convertTo").append(dataType).append("(processed);\n");
        sb.append("    }\n\n");

        // 默认值方法
        sb.append("    private ").append(dataType).append(" getDefault").append(dataType).append("() {\n");
        sb.append("        switch (DATA_TYPE) {\n");
        sb.append("            case \"String\":\n");
        sb.append("                return \"\";\n");
        sb.append("            case \"Integer\":\n");
        sb.append("                return 0;\n");
        sb.append("            case \"Boolean\":\n");
        sb.append("                return false;\n");
        sb.append("            case \"Long\":\n");
        sb.append("                return 0L;\n");
        sb.append("            case \"Double\":\n");
        sb.append("                return 0.0;\n");
        sb.append("            case \"Float\":\n");
        sb.append("                return 0.0f;\n");
        sb.append("            case \"byte[]\":\n");
        sb.append("                return new byte[0];\n");
        sb.append("            default:\n");
        sb.append("                return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 转换方法
        sb.append("    private ").append(dataType).append(" convertTo").append(dataType).append("(String input) {\n");
        sb.append("        try {\n");
        sb.append("            switch (DATA_TYPE) {\n");
        sb.append("                case \"String\":\n");
        sb.append("                    return (").append(dataType).append(") input;\n");
        sb.append("                case \"Integer\":\n");
        sb.append("                    return Integer.parseInt(input);\n");
        sb.append("                case \"Boolean\":\n");
        sb.append("                    return Boolean.parseBoolean(input);\n");
        sb.append("                case \"Long\":\n");
        sb.append("                    return Long.parseLong(input);\n");
        sb.append("                case \"Double\":\n");
        sb.append("                    return Double.parseDouble(input);\n");
        sb.append("                case \"Float\":\n");
        sb.append("                    return Float.parseFloat(input);\n");
        sb.append("                case \"byte[]\":\n");
        sb.append("                    return input.getBytes(java.nio.charset.StandardCharsets.UTF_8);\n");
        sb.append("                default:\n");
        sb.append("                    return null;\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(TAG, \"Conversion failed\", e);\n");
        sb.append("            return getDefault").append(dataType).append("();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 编码方法
        if (useEncoding) {
            sb.append("    private String encodeString(String input) {\n");
            sb.append("        try {\n");
            sb.append("            byte[] bytes = input.getBytes(charset);\n");
            sb.append("            return new String(bytes, charset);\n");
            sb.append("        } catch (Exception e) {\n");
            sb.append("            Log.e(TAG, \"Encoding failed\", e);\n");
            sb.append("            return input;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 验证方法
        if (useValidation) {
            generateValidationMethods(sb, methodType);
        }

        // 转换方法
        if (useTransform) {
            generateTransformMethods(sb, methodType);
        }

        // 缓存方法
        if (useCache) {
            generateCacheMethods(sb, className);
        }

        // 统计方法
        if (useStatistics) {
            generateStatisticsMethods(sb);
        }

        // 异步方法
        if (useAsync) {
            generateAsyncMethods(sb, asyncHandler, methodType);
        }

        // 批处理方法
        if (useBatch) {
            generateBatchMethods(sb, methodType);
        }

        // 队列方法
        if (useQueue) {
            generateQueueMethods(sb);
        }

        // 闭合类
        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "helper");
    }

    private void generateValidationMethods(StringBuilder sb, String methodType) {
        sb.append("    public boolean ").append(methodType).append("Valid(String input) {\n");
        sb.append("        if (input == null || input.isEmpty()) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        boolean result = !input.trim().isEmpty();\n");
        sb.append("        Log.d(TAG, \"").append(methodType).append(" valid: \" + result);\n");

        if (RandomUtils.randomBoolean()) {
            sb.append("        updateStatistics(\"").append(methodType).append("Valid\");\n");
        }

        sb.append("        return result;\n");
        sb.append("    }\n\n");

        sb.append("    public boolean ").append(methodType).append("NotEmpty(String input) {\n");
        sb.append("        return input != null && !input.trim().isEmpty();\n");
        sb.append("    }\n\n");
    }

    private void generateTransformMethods(StringBuilder sb, String methodType) {
        sb.append("    public String ").append(methodType).append("ToUpperCase(String input) {\n");
        sb.append("        if (input == null) {\n");
        sb.append("            return \"\";\n");
        sb.append("        }\n");
        sb.append("        return input.toUpperCase();\n");
        sb.append("    }\n\n");

        sb.append("    public String ").append(methodType).append("ToLowerCase(String input) {\n");
        sb.append("        if (input == null) {\n");
        sb.append("            return \"\";\n");
        sb.append("        }\n");
        sb.append("        return input.toLowerCase();\n");
        sb.append("    }\n\n");
    }

    private void generateCacheMethods(StringBuilder sb, String className) {
        sb.append("    private void cacheResult(String key, Object value) {\n");
        sb.append("        if (!cacheEnabled || key == null || key.isEmpty()) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        if (memoryCache.size() >= MAX_CACHE_SIZE) {\n");
        sb.append("            clearExpiredCache();\n");
        sb.append("        }\n");
        sb.append("        memoryCache.put(key, new CacheEntry(value));\n");
        sb.append("        Log.d(TAG, \"Cached: \" + key);\n");
        sb.append("    }\n\n");

        sb.append("    public Object getCachedResult(String key) {\n");
        sb.append("        if (key == null || !cacheEnabled) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        CacheEntry entry = memoryCache.get(key);\n");
        sb.append("        if (entry == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        if (System.currentTimeMillis() - entry.timestamp > CACHE_TTL_MS) {\n");
        sb.append("            memoryCache.remove(key);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return entry.value;\n");
        sb.append("    }\n\n");

        sb.append("    private void clearExpiredCache() {\n");
        sb.append("        long currentTime = System.currentTimeMillis();\n");
        sb.append("        memoryCache.entrySet().removeIf(entry -> \n");
        sb.append("            currentTime - entry.getValue().timestamp > CACHE_TTL_MS\n");
        sb.append("        );\n");
        sb.append("        Log.d(TAG, \"Expired cache cleared\");\n");
        sb.append("    }\n\n");

        sb.append("    public void clearCache() {\n");
        sb.append("        memoryCache.clear();\n");
        sb.append("        sharedPreferences.edit().clear().apply();\n");
        sb.append("        Log.d(TAG, \"Cache cleared\");\n");
        sb.append("    }\n\n");

        sb.append("    public void setCacheEnabled(boolean enabled) {\n");
        sb.append("        this.cacheEnabled = enabled;\n");
        sb.append("        Log.d(TAG, \"Cache enabled: \" + enabled);\n");
        sb.append("    }\n\n");
    }

    private void generateStatisticsMethods(StringBuilder sb) {
        sb.append("    private void updateStatistics(String operationType) {\n");
        sb.append("        operationCount++;\n");
        sb.append("        lastOperationTime = System.currentTimeMillis();\n");
        sb.append("        operationTypeCount.merge(operationType, 1, Integer::sum);\n");
        sb.append("    }\n\n");

        sb.append("    public int getOperationCount() {\n");
        sb.append("        return operationCount;\n");
        sb.append("    }\n\n");

        sb.append("    public long getLastOperationTime() {\n");
        sb.append("        return lastOperationTime;\n");
        sb.append("    }\n\n");

        sb.append("    public Map<String, Integer> getOperationTypeCount() {\n");
        sb.append("        return new HashMap<>(operationTypeCount);\n");
        sb.append("    }\n\n");

        sb.append("    public void resetStatistics() {\n");
        sb.append("        operationCount = 0;\n");
        sb.append("        lastOperationTime = System.currentTimeMillis();\n");
        sb.append("        operationTypeCount.clear();\n");
        sb.append("        Log.d(TAG, \"Statistics reset\");\n");
        sb.append("    }\n\n");
    }

    private void generateAsyncMethods(StringBuilder sb, String asyncHandler, String methodType) {
        if (asyncHandler.contains("coroutines")) {
            sb.append("    public void ").append(methodType).append("Async(String input, CoroutineScope scope, kotlinx.coroutines.Continuation<String> continuation) {\n");
            sb.append("        scope.launch(Dispatchers.IO, continuation, () -> {\n");
            sb.append("            return ").append(methodType).append("Data(input);\n");
            sb.append("        });\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("    public Single<String> ").append(methodType).append("Async(String input) {\n");
            sb.append("        return Single.fromCallable(() -> ").append(methodType).append("Data(input))\n");
            sb.append("            .subscribeOn(Schedulers.io())\n");
            sb.append("            .observeOn(Schedulers.mainThread());\n");
            sb.append("    }\n\n");
        } else {
            sb.append("    public void ").append(methodType).append("Async(String input, OnResultListener listener) {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            String result = ").append(methodType).append("Data(input);\n");
            sb.append("            if (listener != null) {\n");
            sb.append("                listener.onResult(result);\n");
            sb.append("            }\n");
            sb.append("        }).start();\n");
            sb.append("    }\n\n");

            sb.append("    public interface OnResultListener {\n");
            sb.append("        void onResult(String result);\n");
            sb.append("    }\n\n");
        }
    }

    private void generateBatchMethods(StringBuilder sb, String methodType) {
        sb.append("    public List<String> ").append(methodType).append("Batch(List<String> inputs) {\n");
        sb.append("        if (inputs == null || inputs.isEmpty()) {\n");
        sb.append("            return new ArrayList<>();\n");
        sb.append("        }\n");
        sb.append("        List<String> results = new ArrayList<>();\n");
        sb.append("        for (int i = 0; i < inputs.size(); i++) {\n");
        sb.append("            results.add(").append(methodType).append("Data(inputs.get(i)));\n");
        sb.append("            if ((i + 1) % BATCH_SIZE == 0) {\n");
        sb.append("                Log.d(TAG, \"Processed batch: \" + (i + 1));\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return results;\n");
        sb.append("    }\n\n");
    }

    private void generateQueueMethods(StringBuilder sb) {
        sb.append("    public void enqueueOperation(Runnable operation) {\n");
        sb.append("        if (operation == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        synchronized (operationQueue) {\n");
        sb.append("            if (operationQueue.size() >= MAX_QUEUE_SIZE) {\n");
        sb.append("                operationQueue.remove(0);\n");
        sb.append("            }\n");
        sb.append("            operationQueue.add(operation);\n");
        sb.append("        }\n");
        sb.append("        processQueue();\n");
        sb.append("    }\n\n");

        sb.append("    private void processQueue() {\n");
        sb.append("        if (queueProcessing) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        queueProcessing = true;\n");
        sb.append("        new Thread(() -> {\n");
        sb.append("            while (true) {\n");
        sb.append("                Runnable operation;\n");
        sb.append("                synchronized (operationQueue) {\n");
        sb.append("                    if (operationQueue.isEmpty()) {\n");
        sb.append("                        queueProcessing = false;\n");
        sb.append("                        break;\n");
        sb.append("                    }\n");
        sb.append("                    operation = operationQueue.remove(0);\n");
        sb.append("                }\n");
        sb.append("                try {\n");
        sb.append("                    operation.run();\n");
        sb.append("                } catch (Exception e) {\n");
        sb.append("                    Log.e(TAG, \"Operation failed\", e);\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }).start();\n");
        sb.append("    }\n\n");
    }
}
