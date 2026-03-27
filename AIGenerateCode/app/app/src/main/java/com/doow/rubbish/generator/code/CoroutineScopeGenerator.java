package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class CoroutineScopeGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] SCOPE_NAMES = {
            "AppScope", "MainScope", "IoScope", "DefaultScope", "ViewModelScope"
    };

    private static final String[] DISPATCHER_NAMES = {
            "IO", "Main", "Default", "Unconfined"
    };

    public CoroutineScopeGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成协程作用域类");

        // 空值防护：异步处理器默认值
        String asyncHandler = variationManager.getVariation("async_handler");
        asyncHandler = (asyncHandler == null) ? "coroutines" : asyncHandler;

        for (int i = 0; i < RandomUtils.between(3, 8); i++) {
            String className = RandomUtils.generateClassName("Scope");
            generateCoroutineScopeClass(className, asyncHandler);
        }
    }

    private void generateCoroutineScopeClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 包声明
        sb.append(generatePackageDeclaration("async"));

        // 导入语句（补充必要依赖，规范换行）
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
        sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        sb.append(generateImportStatement("kotlinx.coroutines.SupervisorJob"));
        sb.append(generateImportStatement("kotlinx.coroutines.Job"));
        sb.append(generateImportStatement("kotlinx.coroutines.launch"));
        sb.append(generateImportStatement("kotlinx.coroutines.withContext"));
        sb.append(generateImportStatement("kotlinx.coroutines.delay"));
        sb.append(generateImportStatement("kotlinx.coroutines.cancelChildren"));
        sb.append(generateImportStatement("java.util.concurrent.ConcurrentHashMap"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.lang.Runnable"));
        sb.append(generateImportStatement(packageName + ".ui.*"));
        sb.append("\n"); // 规范空行

        // 功能开关标志
        boolean useJobTracking = RandomUtils.randomBoolean();
        boolean useContextSwitching = RandomUtils.randomBoolean();
        boolean useTimeout = RandomUtils.randomBoolean();
        boolean useRetry = RandomUtils.randomBoolean();
        boolean useBatch = RandomUtils.randomBoolean();
        boolean useStatistics = RandomUtils.randomBoolean();

        // 随机选择调度器（边界防护）
        int dispatcherIndex = RandomUtils.between(0, DISPATCHER_NAMES.length - 1);
        String dispatcherName = DISPATCHER_NAMES[dispatcherIndex];

        // 类定义（final修饰，符合工具类规范）
        sb.append("public final class ").append(className).append(" {\n\n");

        // 修复核心问题：字符串转义错误（所有常量定义）
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String DISPATCHER_NAME = \"").append(dispatcherName).append("\";\n\n");

        // 超时常量（按需定义）
        if (useTimeout) {
            sb.append("    private static final long DEFAULT_TIMEOUT_MS = 5000;\n");
            sb.append("    private static final long MIN_TIMEOUT_MS = 1000;\n");
            sb.append("    private static final long MAX_TIMEOUT_MS = 30000;\n\n");
        }

        // 重试常量（按需定义）
        if (useRetry) {
            sb.append("    private static final int MAX_RETRY_COUNT = 3;\n");
            sb.append("    private static final long RETRY_DELAY_MS = 1000;\n\n");
        }

        // 核心字段（final + volatile保证线程安全）
        sb.append("    private final SupervisorJob supervisorJob = new SupervisorJob();\n");
        sb.append("    private final CoroutineScope scope = CoroutineScope(supervisorJob + Dispatchers.").append(dispatcherName).append(");\n\n");

        // 功能字段（按需生成，volatile保证可见性）
        if (useJobTracking) {
            sb.append("    private volatile Map<String, Job> activeJobs;\n");
            sb.append("    private boolean jobTrackingEnabled = true;\n\n");
        }

        if (useTimeout) {
            sb.append("    private long timeoutMs = DEFAULT_TIMEOUT_MS;\n\n");
        }

        if (useRetry) {
            sb.append("    private int retryCount = 0;\n\n");
        }

        if (useBatch) {
            sb.append("    private volatile List<Runnable> pendingTasks;\n");
            sb.append("    private boolean batchModeEnabled = false;\n\n");
        }

        if (useStatistics) {
            sb.append("    private int launchedJobCount = 0;\n");
            sb.append("    private int completedJobCount = 0;\n");
            sb.append("    private long totalExecutionTime = 0;\n\n");
        }

        // 构造方法（修复Log转义 + 空值初始化）
        sb.append("    public ").append(className).append("() {\n");
        if (useJobTracking) {
            sb.append("        this.activeJobs = new ConcurrentHashMap<>();\n");
            sb.append("        Log.d(TAG, \"Job tracking initialized\");\n");
        }
        if (useBatch) {
            sb.append("        this.pendingTasks = new ArrayList<>();\n");
            sb.append("        Log.d(TAG, \"Batch mode initialized\");\n");
        }
        sb.append("        Log.d(TAG, \"Scope initialized with dispatcher: \" + DISPATCHER_NAME);\n");
        sb.append("    }\n\n");

        // 基础getter方法
        sb.append("    public CoroutineScope getScope() {\n");
        sb.append("        return scope;\n");
        sb.append("    }\n\n");

        sb.append("    public SupervisorJob getSupervisorJob() {\n");
        sb.append("        return supervisorJob;\n");
        sb.append("    }\n\n");

        // 核心launch方法（修复所有Log转义 + 空值防护）
        sb.append("    public void launch(Runnable action) {\n");
        sb.append("        if (action == null) {\n");
        sb.append("            Log.w(TAG, \"Action is null, skip launch\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        Job job = scope.launch {\n");
        sb.append("            try {\n");
        sb.append("                long startTime = System.currentTimeMillis();\n");
        if (useStatistics) {
            sb.append("                launchedJobCount++;\n");
        }
        if (useTimeout) {
            sb.append("                withContext(Dispatchers.IO) {\n");
            sb.append("                    delay(timeoutMs);\n");
            sb.append("                }\n");
        }
        sb.append("                action.run();\n");
        if (useStatistics) {
            sb.append("                long executionTime = System.currentTimeMillis() - startTime;\n");
            sb.append("                totalExecutionTime += executionTime;\n");
            sb.append("                completedJobCount++;\n");
            sb.append("                Log.d(TAG, \"Job executed in \" + executionTime + \"ms\");\n");
        }
        if (useRetry) {
            sb.append("                retryCount = 0;\n");
        }
        sb.append("            } catch (Exception e) {\n");
        sb.append("                handleError(e);\n");
        if (useRetry) {
            sb.append("                if (retryCount < MAX_RETRY_COUNT) {\n");
            sb.append("                    retryCount++;\n");
            sb.append("                    Log.w(TAG, \"Retrying, attempt: \" + retryCount);\n");
            sb.append("                    withContext(Dispatchers.IO) {\n");
            sb.append("                        delay(RETRY_DELAY_MS);\n");
            sb.append("                    }\n");
            sb.append("                }\n");
        }
        sb.append("            }\n");
        sb.append("        };\n");
        if (useJobTracking) {
            sb.append("        if (jobTrackingEnabled && activeJobs != null) {\n"); // 空值防护
            sb.append("            String jobId = String.valueOf(job.hashCode());\n");
            sb.append("            activeJobs.put(jobId, job);\n");
            sb.append("            Log.d(TAG, \"Job tracked: \" + jobId);\n");
            sb.append("        }\n");
        }
        sb.append("    }\n\n");

        // 按需生成配套方法
        if (useJobTracking) {
            generateJobTrackingMethods(sb);
        }

        if (useContextSwitching) {
            generateContextSwitchingMethods(sb);
        }

        if (useTimeout) {
            generateTimeoutMethods(sb);
        }

        if (useRetry) {
            generateRetryMethods(sb);
        }

        if (useBatch) {
            generateBatchMethods(sb);
        }

        if (useStatistics) {
            generateStatisticsMethods(sb);
        }

        // 通用取消方法（修复Log转义 + 空值防护）
        sb.append("    public void cancelAll() {\n");
        if (useJobTracking) {
            sb.append("        if (activeJobs != null && !activeJobs.isEmpty()) {\n"); // 空值+空集合防护
            sb.append("            for (Job job : activeJobs.values()) {\n");
            sb.append("                if (job != null && job.isActive()) {\n"); // 空值防护
            sb.append("                    job.cancel();\n");
            sb.append("                }\n");
            sb.append("            }\n");
            sb.append("            activeJobs.clear();\n");
            sb.append("            Log.d(TAG, \"All jobs cancelled\");\n");
            sb.append("        }\n");
        }
        sb.append("        supervisorJob.cancelChildren();\n");
        sb.append("        Log.d(TAG, \"All children cancelled\");\n");
        sb.append("    }\n\n");

        // 异常处理方法（修复Log转义）
        sb.append("    private void handleError(Exception e) {\n");
        sb.append("        Log.e(TAG, \"Error in coroutine\", e);\n");
        sb.append("    }\n\n");

        // 销毁方法（补充：规范资源释放）
        sb.append("    public void destroy() {\n");
        sb.append("        cancelAll();\n");
        sb.append("        supervisorJob.cancel();\n");
        sb.append("        Log.d(TAG, \"Coroutine scope destroyed\");\n");
        sb.append("    }\n\n");

        // 闭合类
        sb.append("}\n");

        // 生成文件
        generateJavaFile(className, sb.toString(), "async");
    }

    // 任务追踪方法（修复Log转义 + 空值防护）
    private void generateJobTrackingMethods(StringBuilder sb) {
        sb.append("    public void setJobTrackingEnabled(boolean enabled) {\n");
        sb.append("        this.jobTrackingEnabled = enabled;\n");
        sb.append("        Log.d(TAG, \"Job tracking: \" + (enabled ? \"enabled\" : \"disabled\"));\n");
        sb.append("    }\n\n");

        sb.append("    public int getActiveJobCount() {\n");
        sb.append("        return activeJobs != null ? activeJobs.size() : 0;\n");
        sb.append("    }\n\n");

        sb.append("    public void cancelJob(String jobId) {\n");
        sb.append("        if (jobId == null || activeJobs == null) {\n"); // 空值防护
        sb.append("            Log.w(TAG, \"JobId or activeJobs is null, skip cancel\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        Job job = activeJobs.get(jobId);\n");
        sb.append("        if (job != null && job.isActive()) {\n");
        sb.append("            job.cancel();\n");
        sb.append("            activeJobs.remove(jobId);\n");
        sb.append("            Log.d(TAG, \"Job cancelled: \" + jobId);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    // 上下文切换方法（修复Log转义 + 空值防护）
    private void generateContextSwitchingMethods(StringBuilder sb) {
        sb.append("    public void launchOnIO(Runnable action) {\n");
        sb.append("        if (action == null) {\n"); // 空值防护
        sb.append("            Log.w(TAG, \"Action is null, skip launchOnIO\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        scope.launch(Dispatchers.IO) {\n");
        sb.append("            try {\n");
        sb.append("                action.run();\n");
        sb.append("            } catch (Exception e) {\n");
        sb.append("                handleError(e);\n");
        sb.append("            }\n");
        sb.append("        };\n");
        sb.append("    }\n\n");

        sb.append("    public void launchOnMain(Runnable action) {\n");
        sb.append("        if (action == null) {\n"); // 空值防护
        sb.append("            Log.w(TAG, \"Action is null, skip launchOnMain\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        scope.launch(Dispatchers.Main) {\n");
        sb.append("            try {\n");
        sb.append("                action.run();\n");
        sb.append("            } catch (Exception e) {\n");
        sb.append("                handleError(e);\n");
        sb.append("            }\n");
        sb.append("        };\n");
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

    // 批量任务方法（修复Log转义 + 空值防护）
    private void generateBatchMethods(StringBuilder sb) {
        sb.append("    public void setBatchModeEnabled(boolean enabled) {\n");
        sb.append("        this.batchModeEnabled = enabled;\n");
        sb.append("        Log.d(TAG, \"Batch mode: \" + (enabled ? \"enabled\" : \"disabled\"));\n");
        sb.append("    }\n\n");

        sb.append("    public void addToBatch(Runnable task) {\n");
        sb.append("        if (!batchModeEnabled || task == null || pendingTasks == null) {\n"); // 空值防护
        sb.append("            Log.w(TAG, \"Batch mode disabled or task/pendingTasks is null\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        pendingTasks.add(task);\n");
        sb.append("        Log.d(TAG, \"Task added to batch, size: \" + pendingTasks.size());\n");
        sb.append("    }\n\n");

        sb.append("    public void executeBatch() {\n");
        sb.append("        if (!batchModeEnabled || pendingTasks == null || pendingTasks.isEmpty()) {\n"); // 空值防护
        sb.append("            Log.w(TAG, \"Batch mode disabled or pendingTasks is empty\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        for (Runnable task : pendingTasks) {\n");
        sb.append("            launch(task);\n");
        sb.append("        }\n");
        sb.append("        pendingTasks.clear();\n");
        sb.append("        Log.d(TAG, \"Batch executed\");\n");
        sb.append("    }\n\n");
    }

    // 统计方法（修复Log转义）
    private void generateStatisticsMethods(StringBuilder sb) {
        sb.append("    public int getLaunchedJobCount() {\n");
        sb.append("        return launchedJobCount;\n");
        sb.append("    }\n\n");

        sb.append("    public int getCompletedJobCount() {\n");
        sb.append("        return completedJobCount;\n");
        sb.append("    }\n\n");

        sb.append("    public long getTotalExecutionTime() {\n");
        sb.append("        return totalExecutionTime;\n");
        sb.append("    }\n\n");

        sb.append("    public double getAverageExecutionTime() {\n");
        sb.append("        return completedJobCount > 0 ? (double) totalExecutionTime / completedJobCount : 0.0;\n");
        sb.append("    }\n\n");

        sb.append("    public void resetStatistics() {\n");
        sb.append("        launchedJobCount = 0;\n");
        sb.append("        completedJobCount = 0;\n");
        sb.append("        totalExecutionTime = 0;\n");
        sb.append("        Log.d(TAG, \"Statistics reset\");\n");
        sb.append("    }\n\n");
    }
}