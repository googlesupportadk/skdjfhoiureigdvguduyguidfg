package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class CoroutineGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] SCOPE_TYPES = {
            "io", "main", "default", "unconfined"
    };

    private static final String[] DISPATCHER_TYPES = {
            "IO", "Main", "Default", "Unconfined"
    };

    public CoroutineGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成协程类");

        String asyncHandler = variationManager.getVariation("async_handler");
        asyncHandler = (asyncHandler == null) ? "coroutines" : asyncHandler;

        for (int i = 0; i < RandomUtils.between(3, 8); i++) {
            String className = RandomUtils.generateClassName("Coroutine");
            generateCoroutineClass(className, asyncHandler);
        }
    }

    private void generateCoroutineClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 包声明
        sb.append(generatePackageDeclaration("async"));

        // 导入语句（补充缺失的Kotlin相关导入，修复语法错误）
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
        sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        sb.append(generateImportStatement("kotlinx.coroutines.Job"));
        sb.append(generateImportStatement("kotlinx.coroutines.launch"));
        sb.append(generateImportStatement("kotlinx.coroutines.withContext"));
        sb.append(generateImportStatement("kotlinx.coroutines.cancelChildren"));
        sb.append(generateImportStatement("kotlinx.coroutines.delay"));
        sb.append(generateImportStatement("kotlinx.coroutines.Deferred"));
        sb.append(generateImportStatement("kotlinx.coroutines.async"));
        sb.append(generateImportStatement("kotlin.jvm.functions.Function0"));
        sb.append(generateImportStatement("java.lang.Runnable"));
        sb.append(generateImportStatement("java.util.concurrent.ConcurrentHashMap"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement(packageName + ".ui.*"));
        sb.append("\n"); // 规范空行

        // 类定义（final修饰，符合工具类规范）
        sb.append("public final class ").append(className).append(" {\n\n");

        // 标志变量（控制功能开关）
        boolean useCache = RandomUtils.randomBoolean();
        boolean useTimeout = RandomUtils.randomBoolean();
        boolean useRetry = RandomUtils.randomBoolean();
        boolean useDeferred = RandomUtils.randomBoolean();
        boolean useBatch = RandomUtils.randomBoolean();
        boolean useStatistics = RandomUtils.randomBoolean();

        // 随机选择作用域和调度器（边界防护）
        int scopeIndex = RandomUtils.between(0, SCOPE_TYPES.length - 1);
        int dispatcherIndex = RandomUtils.between(0, DISPATCHER_TYPES.length - 1);
        String scopeType = SCOPE_TYPES[scopeIndex];
        String dispatcherType = DISPATCHER_TYPES[dispatcherIndex];

        // 修复核心问题：字符串转义错误（所有常量定义）
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String SCOPE_TYPE = \"").append(scopeType).append("\";\n");
        sb.append("    private static final String DISPATCHER_TYPE = \"").append(dispatcherType).append("\";\n\n");

        // 超时相关常量（仅在useTimeout为true时定义）
        if (useTimeout) {
            sb.append("    private static final long DEFAULT_TIMEOUT_MS = 5000;\n");
            sb.append("    private static final long MIN_TIMEOUT_MS = 1000;\n");
            sb.append("    private static final long MAX_TIMEOUT_MS = 30000;\n\n");
        }

        // 重试相关常量（仅在useRetry为true时定义）
        if (useRetry) {
            sb.append("    private static final int MAX_RETRY_COUNT = 3;\n");
            sb.append("    private static final long RETRY_DELAY_MS = 1000;\n\n");
        }

        // 核心字段（volatile保证多线程可见性）
        sb.append("    private volatile CoroutineScope scope;\n");
        sb.append("    private volatile Job job;\n");

        // 功能字段（按需生成）
        if (useCache) {
            sb.append("    private Map<String, Object> resultCache;\n");
            sb.append("    private boolean cacheEnabled = true;\n\n");
        } else {
            sb.append("\n");
        }

        if (useTimeout) {
            sb.append("    private long timeoutMs = DEFAULT_TIMEOUT_MS;\n\n");
        }

        if (useRetry) {
            sb.append("    private int retryCount = 0;\n\n");
        }

        if (useDeferred) {
            sb.append("    private Deferred<?> currentDeferred;\n\n");
        }

        if (useStatistics) {
            sb.append("    private int taskCount = 0;\n");
            sb.append("    private long totalExecutionTime = 0;\n\n");
        }

        // 构造方法（修复Log转义 + 空值防护）
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        try {\n");
        sb.append("            this.scope = CoroutineScope(Dispatchers.").append(dispatcherType).append(");\n");
        sb.append("            Log.d(TAG, \"Coroutine scope initialized with dispatcher: \" + DISPATCHER_TYPE);\n");
        if (useCache) {
            sb.append("            this.resultCache = new ConcurrentHashMap<>();\n");
            sb.append("            Log.d(TAG, \"Result cache initialized\");\n");
        }
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(TAG, \"Failed to initialize coroutine scope\", e);\n");
        sb.append("            this.scope = CoroutineScope(Dispatchers.Default);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 基础launch方法（修复所有Log转义 + 空值防护）
        sb.append("    public void launch(Runnable action) {\n");
        sb.append("        if (action == null) {\n");
        sb.append("            Log.w(TAG, \"Coroutine action is null, skip launch\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        cancel();\n");
        sb.append("        job = scope.launch {\n");
        sb.append("            try {\n");
        sb.append("                long startTime = System.currentTimeMillis();\n");
        if (useStatistics) {
            sb.append("                taskCount++;\n");
        }
        if (useTimeout) {
            sb.append("                withContext(Dispatchers.IO) {\n");
            sb.append("                    delay(timeoutMs);\n");
            sb.append("                }\n");
        }
        sb.append("                Log.d(TAG, \"Coroutine started with scope: \" + SCOPE_TYPE);\n");
        sb.append("                action.run();\n");
        sb.append("                Log.d(TAG, \"Coroutine task executed successfully\");\n");
        if (useStatistics) {
            sb.append("                long executionTime = System.currentTimeMillis() - startTime;\n");
            sb.append("                totalExecutionTime += executionTime;\n");
            sb.append("                Log.d(TAG, \"Task execution time: \" + executionTime + \"ms\");\n");
        }
        if (useRetry) {
            sb.append("                retryCount = 0;\n");
        }
        sb.append("            } catch (Exception e) {\n");
        sb.append("                handleError(e);\n");
        if (useRetry) {
            sb.append("                if (retryCount < MAX_RETRY_COUNT) {\n");
            sb.append("                    retryCount++;\n");
            sb.append("                    Log.w(TAG, \"Retrying task, attempt: \" + retryCount);\n");
            sb.append("                    withContext(Dispatchers.IO) {\n");
            sb.append("                        delay(RETRY_DELAY_MS);\n");
            sb.append("                    }\n");
            sb.append("                }\n");
        }
        sb.append("            }\n");
        sb.append("        };\n");
        sb.append("    }\n\n");

        // 按需生成配套方法
        if (useCache) {
            generateCacheMethods(sb);
        }

        if (useTimeout) {
            generateTimeoutMethods(sb);
        }

        if (useRetry) {
            generateRetryMethods(sb);
        }

        if (useDeferred) {
            generateDeferredMethods(sb);
        }

        if (useBatch) {
            generateBatchMethods(sb);
        }

        if (useStatistics) {
            generateStatisticsMethods(sb);
        }

        // 通用cancel方法（修复Log转义）
        sb.append("    public void cancel() {\n");
        sb.append("        if (job != null && job.isActive()) {\n");
        sb.append("            job.cancel();\n");
        sb.append("            Log.d(TAG, \"Coroutine job cancelled\");\n");
        sb.append("        }\n");
        if (useDeferred) {
            sb.append("        if (currentDeferred != null && currentDeferred.isActive()) {\n");
            sb.append("            currentDeferred.cancel();\n");
            sb.append("        }\n");
        }
        sb.append("        if (scope != null) {\n");
        sb.append("            scope.cancelChildren();\n");
        sb.append("        }\n");
        sb.append("        job = null;\n");
        sb.append("    }\n\n");

        // destroy方法（修复Log转义）
        sb.append("    public void destroy() {\n");
        sb.append("        cancel();\n");
        if (useCache) {
            sb.append("        if (resultCache != null) {\n");
            sb.append("            resultCache.clear();\n");
            sb.append("            resultCache = null;\n");
            sb.append("        }\n");
        }
        sb.append("        if (scope != null) {\n");
        sb.append("            scope.cancel();\n");
        sb.append("            scope = null;\n");
        sb.append("            Log.d(TAG, \"Coroutine scope destroyed\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 异常处理方法（修复Log转义）
        sb.append("    private void handleError(Exception e) {\n");
        sb.append("        Log.e(TAG, \"Error in coroutine\", e);\n");
        sb.append("    }\n\n");

        // 闭合类
        sb.append("}\n");

        // 生成文件
        generateJavaFile(className, sb.toString(), "async");
    }

    // 缓存方法（修复所有Log转义）
    private void generateCacheMethods(StringBuilder sb) {
        sb.append("    public void setCacheEnabled(boolean enabled) {\n");
        sb.append("        this.cacheEnabled = enabled;\n");
        sb.append("        Log.d(TAG, \"Cache enabled: \" + enabled);\n");
        sb.append("    }\n\n");

        sb.append("    public void clearCache() {\n");
        sb.append("        if (resultCache != null) {\n");
        sb.append("            resultCache.clear();\n");
        sb.append("            Log.d(TAG, \"Cache cleared\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public int getCacheSize() {\n");
        sb.append("        return resultCache != null ? resultCache.size() : 0;\n");
        sb.append("    }\n\n");
    }

    // 超时方法（修复Log转义）
    private void generateTimeoutMethods(StringBuilder sb) {
        sb.append("    public void setTimeout(long timeoutMs) {\n");
        sb.append("        this.timeoutMs = Math.max(MIN_TIMEOUT_MS, Math.min(MAX_TIMEOUT_MS, timeoutMs));\n");
        sb.append("        Log.d(TAG, \"Timeout set to: \" + this.timeoutMs + \"ms\");\n");
        sb.append("    }\n\n");

        sb.append("    public long getTimeout() {\n");
        sb.append("        return timeoutMs;\n");
        sb.append("    }\n\n");
    }

    // 重试方法（修复Log转义）
    private void generateRetryMethods(StringBuilder sb) {
        sb.append("    public void resetRetryCount() {\n");
        sb.append("        retryCount = 0;\n");
        sb.append("        Log.d(TAG, \"Retry count reset\");\n");
        sb.append("    }\n\n");

        sb.append("    public int getRetryCount() {\n");
        sb.append("        return retryCount;\n");
        sb.append("    }\n\n");
    }

    // 异步返回方法（修复Log转义 + Kotlin语法）
    private void generateDeferredMethods(StringBuilder sb) {
        sb.append("    public <T> void launchDeferred(Function0<T> supplier) {\n");
        sb.append("        cancel();\n");
        sb.append("        currentDeferred = scope.async {\n");
        sb.append("            try {\n");
        sb.append("                T result = supplier.invoke();\n");
        sb.append("                Log.d(TAG, \"Deferred task completed successfully\");\n");
        sb.append("                return@async result;\n");
        sb.append("            } catch (Exception e) {\n");
        sb.append("                handleError(e);\n");
        sb.append("                return@async null;\n");
        sb.append("            }\n");
        sb.append("        };\n");
        sb.append("    }\n\n");

        sb.append("    public boolean isDeferredActive() {\n");
        sb.append("        return currentDeferred != null && currentDeferred.isActive();\n");
        sb.append("    }\n\n");
    }

    // 批量任务方法（修复Log转义）
    private void generateBatchMethods(StringBuilder sb) {
        sb.append("    public void launchBatch(List<Runnable> tasks) {\n");
        sb.append("        if (tasks == null || tasks.isEmpty()) {\n");
        sb.append("            Log.w(TAG, \"Batch tasks is empty, skip launch\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        Log.d(TAG, \"Launching batch of \" + tasks.size() + \" tasks\");\n");
        sb.append("        scope.launch {\n");
        sb.append("            for (Runnable task : tasks) {\n");
        sb.append("                try {\n");
        sb.append("                    task.run();\n");
        sb.append("                } catch (Exception e) {\n");
        sb.append("                    handleError(e);\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("            Log.d(TAG, \"Batch execution completed\");\n");
        sb.append("        };\n");
        sb.append("    }\n\n");
    }

    // 统计方法（修复Log转义）
    private void generateStatisticsMethods(StringBuilder sb) {
        sb.append("    public int getTaskCount() {\n");
        sb.append("        return taskCount;\n");
        sb.append("    }\n\n");

        sb.append("    public long getTotalExecutionTime() {\n");
        sb.append("        return totalExecutionTime;\n");
        sb.append("    }\n\n");

        sb.append("    public double getAverageExecutionTime() {\n");
        sb.append("        return taskCount > 0 ? (double) totalExecutionTime / taskCount : 0.0;\n");
        sb.append("    }\n\n");

        sb.append("    public void resetStatistics() {\n");
        sb.append("        taskCount = 0;\n");
        sb.append("        totalExecutionTime = 0;\n");
        sb.append("        Log.d(TAG, \"Statistics reset\");\n");
        sb.append("    }\n\n");
    }
}