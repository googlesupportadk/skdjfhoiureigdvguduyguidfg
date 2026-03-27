package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class WorkManagerGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    public WorkManagerGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成WorkManager任务类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Worker");
            generateWorker(className, asyncHandler);
        }
    }

    private void generateWorker(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("worker"));

        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("androidx.annotation.NonNull"));
        sb.append(generateImportStatement("androidx.work.Worker"));
        sb.append(generateImportStatement("androidx.work.WorkerParameters"));
        sb.append(generateImportStatement("androidx.work.Data"));
        sb.append(generateImportStatement("androidx.work.OneTimeWorkRequest"));
        sb.append(generateImportStatement("androidx.work.PeriodicWorkRequest"));
        sb.append(generateImportStatement("androidx.work.Constraints"));
        sb.append(generateImportStatement("androidx.work.NetworkType"));
        sb.append(generateImportStatement("androidx.work.ExistingWorkPolicy"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.concurrent.TimeUnit"));

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        } else if (asyncHandler.contains("rxjava")) {
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
        }

        sb.append("\n");

        sb.append("public class ").append(className).append(" extends Worker {\n\n");

        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String KEY_RESULT = \"result\";\n");
        sb.append("    private static final String KEY_PROGRESS = \"progress\";\n\n");

        // 使用标志变量确保字段和配套方法一起生成和使用
        boolean useInputData = RandomUtils.randomBoolean();
        boolean useOutputData = RandomUtils.randomBoolean();
        boolean useProgress = RandomUtils.randomBoolean();
        boolean useRetry = RandomUtils.randomBoolean();
        boolean useConstraints = RandomUtils.randomBoolean();
        boolean useRequestBuilder = RandomUtils.randomBoolean();

        // 生成字段
        if (useInputData) {
            sb.append("    private Map<String, Object> inputData;\n\n");
        }

        if (useOutputData) {
            sb.append("    private Map<String, Object> outputData;\n\n");
        }

        if (useProgress) {
            sb.append("    private int progress = 0;\n\n");
        }

        if (useRetry) {
            sb.append("    private int retryCount = 0;\n");
            sb.append("    private static final int MAX_RETRIES = 3;\n\n");
        }

        sb.append("    public ").append(className).append("(@NonNull Context context, @NonNull WorkerParameters params) {\n");
        sb.append("        super(context, params);\n\n");

        // 在构造函数中初始化字段
        if (useInputData) {
            sb.append("        inputData = new HashMap<>();\n");
            sb.append("        if (getInputData() != null) {\n");
            sb.append("            inputData.putAll(getInputData().getKeyValueMap());\n");
            sb.append("        }\n");
        }

        if (useOutputData) {
            sb.append("        outputData = new HashMap<>();\n");
        }

        sb.append("    }\n\n");

        sb.append("    @NonNull\n");
        sb.append("    @Override\n");
        sb.append("    public Result doWork() {\n");
        sb.append("        try {\n");
        sb.append("            Log.d(TAG, \"Worker started\");\n\n");

        // 在doWork方法中使用字段
        if (useInputData) {
            sb.append("            if (inputData != null && !inputData.isEmpty()) {\n");
            sb.append("                Log.d(TAG, \"Processing input data: \" + inputData);\n");
            sb.append("            }\n");
        }

        sb.append("            Thread.sleep(1000);\n");

        if (useProgress) {
            sb.append("            updateProgress(50);\n");
        }

        sb.append("            Log.d(TAG, \"Worker completed\");\n");

        if (useOutputData) {
            sb.append("            if (outputData != null) {\n");
            sb.append("                Data result = new Data.Builder()\n");
            sb.append("                    .putString(KEY_RESULT, \"success\")\n");
            sb.append("                    .build();\n");
            sb.append("                return Result.success(result);\n");
            sb.append("            }\n");
            sb.append("            return Result.success();\n");
        } else {
            sb.append("            return Result.success();\n");
        }

        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(TAG, \"Worker failed\", e);\n\n");

        if (useRetry) {
            sb.append("            if (retryCount < MAX_RETRIES) {\n");
            sb.append("                retryCount++;\n");
            sb.append("                Log.d(TAG, \"Retrying worker, attempt: \" + retryCount);\n");
            sb.append("                return Result.retry();\n");
            sb.append("            }\n");
        }

        sb.append("            return Result.failure();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 根据标志变量生成配套方法
        if (useProgress) {
            generateProgressMethod(sb);
        }

        if (useInputData) {
            generateDataMethod(sb);
        }

        if (useRetry) {
            generateRetryMethod(sb);
        }

        if (useConstraints) {
            generateConstraintMethod(sb);
        }

        if (useRequestBuilder) {
            generateRequestMethod(sb, className);
        }

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "worker");
    }

    private void generateProgressMethod(StringBuilder sb) {
        sb.append("    public void updateProgress(int progress) {\n");
        sb.append("        this.progress = progress;\n");
        sb.append("        setProgressAsync(new Data.Builder()\n");
        sb.append("            .putInt(KEY_PROGRESS, progress)\n");
        sb.append("            .build());\n");
        sb.append("    }\n\n");

        sb.append("    public int getProgress() {\n");
        sb.append("        return progress;\n");
        sb.append("    }\n\n");
    }

    private void generateDataMethod(StringBuilder sb) {
        sb.append("    public void addInputData(String key, Object value) {\n");
        sb.append("        if (inputData != null) {\n");
        sb.append("            inputData.put(key, value);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public Object getInputDataValue(String key) {\n");
        sb.append("        if (inputData != null) {\n");
        sb.append("            return inputData.get(key);\n");
        sb.append("        }\n");
        sb.append("        return null;\n");
        sb.append("    }\n\n");
    }

    private void generateRetryMethod(StringBuilder sb) {
        sb.append("    public void resetRetry() {\n");
        sb.append("        retryCount = 0;\n");
        sb.append("    }\n\n");

        sb.append("    public int getRetryCount() {\n");
        sb.append("        return retryCount;\n");
        sb.append("    }\n\n");
    }

    private void generateConstraintMethod(StringBuilder sb) {
        sb.append("    public static Constraints buildConstraints() {\n");
        sb.append("        return new Constraints.Builder()\n");
        sb.append("            .setRequiredNetworkType(NetworkType.CONNECTED)\n");
        sb.append("            .build();\n");
        sb.append("    }\n\n");
    }

    private void generateRequestMethod(StringBuilder sb, String className) {
        sb.append("    public static OneTimeWorkRequest buildOneTimeRequest() {\n");
        sb.append("        return new OneTimeWorkRequest.Builder(").append(className).append(".class)\n");
        sb.append("            .setConstraints(buildConstraints())\n");
        sb.append("            .build();\n");
        sb.append("    }\n\n");

        sb.append("    public static PeriodicWorkRequest buildPeriodicRequest() {\n");
        sb.append("        return new PeriodicWorkRequest.Builder(").append(className).append(".class, 15, TimeUnit.MINUTES)\n");
        sb.append("            .setConstraints(buildConstraints())\n");
        sb.append("            .build();\n");
        sb.append("    }\n\n");
    }
}
