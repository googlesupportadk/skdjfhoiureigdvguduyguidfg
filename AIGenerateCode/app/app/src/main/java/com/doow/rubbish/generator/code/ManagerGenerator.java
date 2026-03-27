package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class ManagerGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    public ManagerGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成Manager类");

        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 12); i++) {
            String className = RandomUtils.generateClassName("Manager");
            generateManager(className, uiStyle, asyncHandler);
        }
    }

    private void generateManager(String className, String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("manager"));

        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        } else if (asyncHandler.contains("rxjava")) {
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
        }

        sb.append("\n"); // 修复多余空格，保留换行

        // 修复类定义的格式和换行
        sb.append("public class ").append(className).append(" {\n\n");

        // 修复TAG常量的双引号转义
        sb.append("    private static final String TAG = \"").append(className).append("\";\n\n");

        // 使用标志变量确保字段和配套方法一起生成和使用
        boolean useSingleton = RandomUtils.randomBoolean();
        boolean useCache = RandomUtils.randomBoolean();
        boolean useThreadPool = RandomUtils.randomBoolean();
        boolean useLifecycle = RandomUtils.randomBoolean();
        boolean useStatistics = RandomUtils.randomBoolean();
        boolean useAsync = RandomUtils.randomBoolean();

        // 根据标志变量生成字段
        if (useSingleton) {
            sb.append("    private static ").append(className).append(" instance;\n\n");
        }

        if (useCache) {
            sb.append("    private Map<String, Object> cache;\n");
            sb.append("    private int cacheSize = 100;\n\n");
        }

        if (useThreadPool) {
            sb.append("    private java.util.concurrent.ExecutorService executorService;\n");
            sb.append("    private boolean isExecutorRunning = false;\n\n");
        }

        if (useLifecycle) {
            sb.append("    private boolean isInitialized = false;\n");
            sb.append("    private boolean isDestroyed = false;\n\n");
        }

        if (useStatistics) {
            sb.append("    private int operationCount = 0;\n");
            sb.append("    private long lastOperationTime = 0;\n\n");
        }

        sb.append("    private Context context;\n\n");

        // 构造方法
        sb.append("    private ").append(className).append("(Context context) {\n");
        sb.append("        this.context = context.getApplicationContext();\n");

        if (useCache) {
            sb.append("        this.cache = new HashMap<>();\n");
        }

        if (useThreadPool) {
            sb.append("        this.executorService = java.util.concurrent.Executors.newFixedThreadPool(3);\n");
            sb.append("        this.isExecutorRunning = true;\n");
        }

        if (useLifecycle) {
            sb.append("        this.isInitialized = true;\n");
        }

        if (useStatistics) {
            sb.append("        this.lastOperationTime = System.currentTimeMillis();\n");
        }

        sb.append("        init();\n");
        sb.append("    }\n\n");

        // 单例获取方法
        if (useSingleton) {
            sb.append("    public static synchronized ").append(className).append(" getInstance(Context context) {\n");
            sb.append("        if (instance == null) {\n");
            sb.append("            instance = new ").append(className).append("(context);\n");
            sb.append("        }\n");
            sb.append("        return instance;\n");
            sb.append("    }\n\n");
        }

        // 初始化方法
        sb.append("    private void init() {\n");
        // 修复Log打印的字符串引号
        sb.append("        Log.d(TAG, \"Manager initialized\");\n");

        if (useStatistics) {
            sb.append("        updateStatistics();\n");
        }

        sb.append("    }\n\n");

        // 清理方法
        sb.append("    public void clear() {\n");
        // 修复Log打印的字符串引号
        sb.append("        Log.d(TAG, \"Manager cleared\");\n");

        if (useCache) {
            sb.append("        if (cache != null) {\n");
            sb.append("            cache.clear();\n");
            sb.append("        }\n");
        }

        if (useThreadPool) {
            sb.append("        if (executorService != null && isExecutorRunning) {\n");
            sb.append("            executorService.shutdown();\n");
            sb.append("            isExecutorRunning = false;\n");
            sb.append("        }\n");
        }

        if (useLifecycle) {
            sb.append("        isDestroyed = true;\n");
        }

        sb.append("    }\n\n");

        // 根据标志变量生成配套方法
        if (useCache) {
            generateCacheMethods(sb, className);
        }

        if (useThreadPool) {
            generateThreadPoolMethods(sb);
        }

        if (useStatistics) {
            generateStatisticsMethods(sb);
        }

        if (useAsync) {
            generateAsyncMethods(sb, asyncHandler);
        }

        // 生成业务方法
        int methodCount = RandomUtils.between(3, 6);
        for (int i = 0; i < methodCount; i++) {
            String methodName = RandomUtils.generateMethodName("manage");
            String returnType = getRandomType();

            sb.append("    public ").append(returnType).append(" ").append(methodName).append("(");

            int paramCount = RandomUtils.between(0, 3);
            for (int j = 0; j < paramCount; j++) {
                if (j > 0) sb.append(", ");
                sb.append(getRandomType()).append(" ").append(RandomUtils.generateVariableName("param"));
            }
            sb.append(") {\n");

            if (useStatistics) {
                sb.append("        updateStatistics();\n");
            }

            if (returnType.equals("void")) {
                // 修复Log打印的字符串拼接
                sb.append("        Log.d(TAG, \"").append(methodName).append(" called\");\n");
            } else if (returnType.equals("boolean")) {
                sb.append("        boolean result = ").append(RandomUtils.randomBoolean()).append(";\n");
                // 修复Log打印的字符串拼接
                sb.append("        Log.d(TAG, \"").append(methodName).append(" result: \" + result);\n");
                sb.append("        return result;\n");
            } else if (returnType.equals("int")) {
                sb.append("        int result = ").append(RandomUtils.between(0, 100)).append(";\n");
                // 修复Log打印的字符串拼接
                sb.append("        Log.d(TAG, \"").append(methodName).append(" result: \" + result);\n");
                sb.append("        return result;\n");
            } else if (returnType.equals("String")) {
                // 修复字符串返回值的引号
                sb.append("        String result = \"").append(RandomUtils.generateWord(RandomUtils.between(3, 8))).append("\";\n");
                // 修复Log打印的字符串拼接
                sb.append("        Log.d(TAG, \"").append(methodName).append(" result: \" + result);\n");
                sb.append("        return result;\n");
            } else if (returnType.equals("List<String>")) {
                sb.append("        List<String> result = new ArrayList<>();\n");
                // 修复List添加元素的引号
                sb.append("        result.add(\"").append(RandomUtils.generateWord(RandomUtils.between(3, 8))).append("\");\n");
                // 修复Log打印的字符串拼接
                sb.append("        Log.d(TAG, \"").append(methodName).append(" result size: \" + result.size());\n");
                sb.append("        return result;\n");
            }
            sb.append("    }\n\n");
        }

        // 闭合类
        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "manager");
    }

    private void generateCacheMethods(StringBuilder sb, String className) {
        sb.append("    public void putCache(String key, Object value) {\n");
        sb.append("        if (cache == null || key == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        if (cache.size() >= cacheSize) {\n");
        sb.append("            cache.clear();\n");
        sb.append("        }\n");
        sb.append("        cache.put(key, value);\n");
        // 修复Log打印的字符串引号
        sb.append("        Log.d(TAG, \"Cached: \" + key);\n");
        sb.append("    }\n\n");

        sb.append("    public Object getCache(String key) {\n");
        sb.append("        if (cache == null || key == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        Object value = cache.get(key);\n");
        // 修复Log打印的字符串引号
        sb.append("        Log.d(TAG, \"Retrieved from cache: \" + key);\n");
        sb.append("        return value;\n");
        sb.append("    }\n\n");

        sb.append("    public void setCacheSize(int size) {\n");
        sb.append("        this.cacheSize = size;\n");
        // 修复Log打印的字符串引号
        sb.append("        Log.d(TAG, \"Cache size set to: \" + size);\n");
        sb.append("    }\n\n");
    }

    private void generateThreadPoolMethods(StringBuilder sb) {
        sb.append("    public void executeTask(Runnable task) {\n");
        sb.append("        if (executorService != null && isExecutorRunning && task != null) {\n");
        sb.append("            executorService.execute(task);\n");
        // 修复Log打印的字符串引号
        sb.append("            Log.d(TAG, \"Task executed\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public boolean isExecutorRunning() {\n");
        sb.append("        return isExecutorRunning;\n");
        sb.append("    }\n\n");
    }

    private void generateStatisticsMethods(StringBuilder sb) {
        sb.append("    private void updateStatistics() {\n");
        sb.append("        operationCount++;\n");
        sb.append("        lastOperationTime = System.currentTimeMillis();\n");
        sb.append("    }\n\n");

        sb.append("    public int getOperationCount() {\n");
        sb.append("        return operationCount;\n");
        sb.append("    }\n\n");

        sb.append("    public long getLastOperationTime() {\n");
        sb.append("        return lastOperationTime;\n");
        sb.append("    }\n\n");

        sb.append("    public void resetStatistics() {\n");
        sb.append("        operationCount = 0;\n");
        sb.append("        lastOperationTime = System.currentTimeMillis();\n");
        // 修复Log打印的字符串引号
        sb.append("        Log.d(TAG, \"Statistics reset\");\n");
        sb.append("    }\n\n");
    }

    private void generateAsyncMethods(StringBuilder sb, String asyncHandler) {
        if (asyncHandler.contains("coroutines")) {
            sb.append("    public void executeAsync(Runnable task) {\n");
            sb.append("        CoroutineScope scope = new CoroutineScope(Dispatchers.IO);\n");
            sb.append("        scope.launch(() -> {\n");
            sb.append("            task.run();\n");
            sb.append("        });\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("    public Single<String> executeAsync(String input) {\n");
            sb.append("        return Single.fromCallable(() -> {\n");
            // 修复字符串返回值的引号
            sb.append("            return \"Processed: \" + input;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.single());\n");
            sb.append("    }\n\n");
        }
    }

    public String getRandomType() {
        String[] types = {"void", "boolean", "int", "String", "List<String>"};
        return types[RandomUtils.between(0, types.length - 1)];
    }
}