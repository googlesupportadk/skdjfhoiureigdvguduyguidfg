package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalLogGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] LOG_LEVELS = {
            "VERBOSE", "DEBUG", "INFO", "WARN", "ERROR",
            "ASSERT", "FATAL", "TRACE", "OFF", "ALL"
    };

    private static final String[] LOG_TYPES = {
            "SystemLog", "FileLog", "MemoryLog", "DatabaseLog", "NetworkLog",
            "ConsoleLog", "RemoteLog", "BufferedLog", "AsyncLog", "SyncLog",
            "TimedLog", "FilteredLog", "FormattedLog", "StructuredLog", "JsonLog"
    };

    // 功能标志 - 确保所有字段和方法都会被使用
    private boolean useFileLogging;
    private boolean useMemoryLogging;
    private boolean useBufferedLogging;
    private boolean useAsyncLogging;
    private boolean useFilteredLogging;
    private boolean useTimedLogging;
    private boolean useStructuredLogging;
    private boolean useLogRotation;

    public LocalLogGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        initializeFeatureFlags();
    }

    /**
     * 初始化功能标志，确保随机性和多样性
     */
    private void initializeFeatureFlags() {
        useFileLogging = RandomUtils.randomBoolean();
        useMemoryLogging = RandomUtils.randomBoolean();
        useBufferedLogging = RandomUtils.randomBoolean();
        useAsyncLogging = RandomUtils.randomBoolean();
        useFilteredLogging = RandomUtils.randomBoolean();
        useTimedLogging = RandomUtils.randomBoolean();
        useStructuredLogging = RandomUtils.randomBoolean();
        useLogRotation = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成日志类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Log");
            generateLogClass(className, asyncHandler);
        }
    }

    private void generateLogClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("log"));

        // 生成基础导入
        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.io.File"));
        sb.append(generateImportStatement("java.io.FileWriter"));
        sb.append(generateImportStatement("java.io.IOException"));
        sb.append(generateImportStatement("java.text.SimpleDateFormat"));
        sb.append(generateImportStatement("java.util.Date"));
        sb.append(generateImportStatement("java.util.Locale"));

        // 根据功能标志添加条件导入
        if (useFileLogging) {
            sb.append(generateImportStatement("java.io.BufferedWriter"));
        }

        if (useMemoryLogging) {
            sb.append(generateImportStatement("java.util.ArrayList"));
            sb.append(generateImportStatement("java.util.List"));
        }

        if (useBufferedLogging) {
            sb.append(generateImportStatement("java.io.PrintWriter"));
            sb.append(generateImportStatement("java.io.StringWriter"));
        }

        if (useAsyncLogging) {
            if (asyncHandler.contains("coroutines")) {
                sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
                sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
            } else if (asyncHandler.contains("rxjava")) {
                sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
                sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
            } else {
                sb.append(generateImportStatement("android.os.Handler"));
                sb.append(generateImportStatement("android.os.Looper"));
            }
        }

        if (useFilteredLogging) {
            sb.append(generateImportStatement("java.util.HashSet"));
            sb.append(generateImportStatement("java.util.Set"));
        }

        if (useTimedLogging) {
            sb.append(generateImportStatement("android.os.SystemClock"));
        }

        if (useStructuredLogging) {
            sb.append(generateImportStatement("org.json.JSONObject"));
            sb.append(generateImportStatement("org.json.JSONException"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 随机选择日志类型和日志级别
        String logType = LOG_TYPES[RandomUtils.between(0, LOG_TYPES.length - 1)];
        String logLevel = LOG_LEVELS[RandomUtils.between(0, LOG_LEVELS.length - 1)];

        // 生成类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成常量字段
        String tagVarName = RandomUtils.generateWord(6);
        String logTypeVarName = RandomUtils.generateWord(6);
        String logLevelVarName = RandomUtils.generateWord(6);
        String dateFormatVarName = RandomUtils.generateWord(6);
        String maxLogSizeVarName = RandomUtils.generateWord(6);
        String maxLogFilesVarName = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVarName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(logTypeVarName).append(" = \"").append(logType).append("\";\n");
        sb.append("    private static final String ").append(logLevelVarName).append(" = \"").append(logLevel).append("\";\n");
        sb.append("    private static final String ").append(dateFormatVarName).append(" = \"yyyy-MM-dd HH:mm:ss\";\n");
        sb.append("    private static final int ").append(maxLogSizeVarName).append(" = ").append(RandomUtils.between(1024, 10240)).append(";\n");
        sb.append("    private static final int ").append(maxLogFilesVarName).append(" = ").append(RandomUtils.between(3, 10)).append(";\n\n");

        // 生成实例字段
        String contextVarName = RandomUtils.generateWord(6);
        String debugVarName = RandomUtils.generateWord(6);
        String logFileVarName = RandomUtils.generateWord(6);
        String logBufferVarName = RandomUtils.generateWord(6);
        String memoryLogVarName = RandomUtils.generateWord(6);
        String filteredTagsVarName = RandomUtils.generateWord(6);
        String logStartTimeVarName = RandomUtils.generateWord(6);
        String logCountVarName = RandomUtils.generateWord(6);
        String currentLogFileIndexVarName = RandomUtils.generateWord(6);
        String handlerVarName = RandomUtils.generateWord(6);

        sb.append("    private Context ").append(contextVarName).append(";\n");
        sb.append("    private boolean ").append(debugVarName).append(";\n");
        sb.append("    private File ").append(logFileVarName).append(";\n");
        sb.append("    private long ").append(logStartTimeVarName).append(";\n");
        sb.append("    private int ").append(logCountVarName).append(";\n");

        // 根据功能标志添加条件字段
        if (useFileLogging) {
            sb.append("    private BufferedWriter ").append(logBufferVarName).append(";\n");
        }

        if (useMemoryLogging) {
            sb.append("    private List<String> ").append(memoryLogVarName).append(";\n");
        }

        if (useFilteredLogging) {
            sb.append("    private Set<String> ").append(filteredTagsVarName).append(";\n");
        }

        if (useLogRotation) {
            sb.append("    private int ").append(currentLogFileIndexVarName).append(";\n");
        }

        if (useAsyncLogging && !asyncHandler.contains("coroutines") && !asyncHandler.contains("rxjava")) {
            sb.append("    private Handler ").append(handlerVarName).append(";\n");
        }

        sb.append("\n");

        // 生成构造函数
        generateConstructor(sb, className, contextVarName, debugVarName, logFileVarName,
                logBufferVarName, memoryLogVarName, filteredTagsVarName,
                logStartTimeVarName, logCountVarName, currentLogFileIndexVarName,
                handlerVarName, logType, asyncHandler);

        // 生成基础方法
        generateBasicMethods(sb, className, tagVarName, debugVarName, logLevelVarName, dateFormatVarName);

        // 根据功能标志添加条件方法
        if (useFileLogging) {
            generateFileLoggingMethods(sb, className, logFileVarName, logBufferVarName,
                    dateFormatVarName, maxLogSizeVarName, maxLogFilesVarName,
                    currentLogFileIndexVarName, tagVarName);
        }

        if (useMemoryLogging) {
            generateMemoryLoggingMethods(sb, className, memoryLogVarName, tagVarName);
        }

        if (useBufferedLogging) {
            generateBufferedLoggingMethods(sb, className, tagVarName);
        }

        if (useAsyncLogging) {
            generateAsyncLoggingMethods(sb, className, tagVarName, asyncHandler, handlerVarName);
        }

        if (useFilteredLogging) {
            generateFilteredLoggingMethods(sb, className, filteredTagsVarName, tagVarName);
        }

        if (useTimedLogging) {
            generateTimedLoggingMethods(sb, className, logStartTimeVarName, tagVarName);
        }

        if (useStructuredLogging) {
            generateStructuredLoggingMethods(sb, className, tagVarName);
        }

        if (useLogRotation) {
            generateLogRotationMethods(sb, className, logFileVarName, maxLogFilesVarName,
                    currentLogFileIndexVarName, tagVarName, maxLogSizeVarName);
        }

        // 生成使用所有字段和方法的示例方法
        generateExampleUsageMethod(sb, className, contextVarName, debugVarName, logFileVarName,
                logBufferVarName, memoryLogVarName, filteredTagsVarName,
                logStartTimeVarName, logCountVarName, currentLogFileIndexVarName,
                handlerVarName, tagVarName, logLevelVarName, dateFormatVarName);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "log");
    }

    /**
     * 生成构造函数
     */
    private void generateConstructor(StringBuilder sb, String className, String contextVarName,
                                     String debugVarName, String logFileVarName,
                                     String logBufferVarName, String memoryLogVarName,
                                     String filteredTagsVarName, String logStartTimeVarName,
                                     String logCountVarName, String currentLogFileIndexVarName,
                                     String handlerVarName, String logType, String asyncHandler) {
        sb.append("    public ").append(className).append("(Context ").append(contextVarName).append(") {\n");
        sb.append("        this.").append(contextVarName).append(" = ").append(contextVarName).append(".getApplicationContext();\n");
        sb.append("        this.").append(debugVarName).append(" = true;\n");
        sb.append("        this.").append(logStartTimeVarName).append(" = System.currentTimeMillis();\n");
        sb.append("        this.").append(logCountVarName).append(" = 0;\n");

        if (useFileLogging) {
            sb.append("        this.").append(logFileVarName).append(" = new File(")
                    .append(contextVarName).append(".getExternalFilesDir(null), \"log.txt\");\n");
        }

        if (useMemoryLogging) {
            sb.append("        this.").append(memoryLogVarName).append(" = new ArrayList<>();\n");
        }

        if (useFilteredLogging) {
            sb.append("        this.").append(filteredTagsVarName).append(" = new HashSet<>();\n");
        }

        if (useLogRotation) {
            sb.append("        this.").append(currentLogFileIndexVarName).append(" = 0;\n");
        }

        if (useAsyncLogging && !asyncHandler.contains("coroutines") && !asyncHandler.contains("rxjava")) {
            sb.append("        this.").append(handlerVarName).append(" = new Handler(Looper.getMainLooper());\n");
        }

        sb.append("    }\n\n");
    }

    /**
     * 生成基础方法
     */
    private void generateBasicMethods(StringBuilder sb, String className, String tagVarName,
                                      String debugVarName, String logLevelVarName,
                                      String dateFormatVarName) {
        // 生成日志方法
        String verboseMethodName = RandomUtils.generateWord(6);
        String debugMethodName = RandomUtils.generateWord(6);
        String infoMethodName = RandomUtils.generateWord(6);
        String warningMethodName = RandomUtils.generateWord(6);
        String errorMethodName = RandomUtils.generateWord(6);
        String messageParamName = RandomUtils.generateWord(6);
        String tagParamName = RandomUtils.generateWord(6);
        String throwableParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(verboseMethodName).append("(String ")
                .append(messageParamName).append(") {\n");
        sb.append("        if (").append(debugVarName).append(") {\n");
        sb.append("            Log.v(").append(tagVarName).append(", ").append(messageParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(debugMethodName).append("(String ")
                .append(messageParamName).append(") {\n");
        sb.append("        if (").append(debugVarName).append(") {\n");
        sb.append("            Log.d(").append(tagVarName).append(", ").append(messageParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(infoMethodName).append("(String ")
                .append(messageParamName).append(") {\n");
        sb.append("        if (").append(debugVarName).append(") {\n");
        sb.append("            Log.i(").append(tagVarName).append(", ").append(messageParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(warningMethodName).append("(String ")
                .append(messageParamName).append(") {\n");
        sb.append("        if (").append(debugVarName).append(") {\n");
        sb.append("            Log.w(").append(tagVarName).append(", ").append(messageParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(errorMethodName).append("(String ")
                .append(messageParamName).append(") {\n");
        sb.append("        if (").append(debugVarName).append(") {\n");
        sb.append("            Log.e(").append(tagVarName).append(", ").append(messageParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(errorMethodName).append("(String ")
                .append(messageParamName).append(", Throwable ").append(throwableParamName).append(") {\n");
        sb.append("        if (").append(debugVarName).append(") {\n");
        sb.append("            Log.e(").append(tagVarName).append(", ").append(messageParamName)
                .append(", ").append(throwableParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成设置调试模式方法
        String setDebugMethodName = RandomUtils.generateWord(6);
        String isDebugMethodName = RandomUtils.generateWord(6);
        String debugParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(setDebugMethodName).append("(boolean ")
                .append(debugParamName).append(") {\n");
        sb.append("        this.").append(debugVarName).append(" = ").append(debugParamName).append(";\n");
        sb.append("    }\n\n");

        sb.append("    public boolean ").append(isDebugMethodName).append("() {\n");
        sb.append("        return this.").append(debugVarName).append(";\n");
        sb.append("    }\n\n");

        // 生成格式化时间方法
        String formatTimeMethodName = RandomUtils.generateWord(6);
        String timeParamName = RandomUtils.generateWord(6);

        sb.append("    private String ").append(formatTimeMethodName).append("(long ")
                .append(timeParamName).append(") {\n");
        sb.append("        SimpleDateFormat sdf = new SimpleDateFormat(").append(dateFormatVarName)
                .append(", Locale.getDefault());\n");
        sb.append("        return sdf.format(new Date(").append(timeParamName).append("));\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成文件日志方法
     */
    private void generateFileLoggingMethods(StringBuilder sb, String className,
                                            String logFileVarName, String logBufferVarName,
                                            String dateFormatVarName, String maxLogSizeVarName,
                                            String maxLogFilesVarName, String currentLogFileIndexVarName,
                                            String tagVarName) {
        String initFileLoggingMethodName = RandomUtils.generateWord(6);
        String writeToFileMethodName = RandomUtils.generateWord(6);
        String closeFileLoggingMethodName = RandomUtils.generateWord(6);
        String messageParamName = RandomUtils.generateWord(6);
        String levelParamName = RandomUtils.generateWord(6);

        sb.append("    private void ").append(initFileLoggingMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(logBufferVarName).append(" != null) {\n");
        sb.append("                ").append(logBufferVarName).append(".close();\n");
        sb.append("            }\n");
        sb.append("            ").append(logBufferVarName).append(" = new BufferedWriter(new FileWriter(")
                .append(logFileVarName).append(", true));\n");
        sb.append("        } catch (IOException e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Failed to initialize file logging\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    private void ").append(writeToFileMethodName).append("(String ")
                .append(levelParamName).append(", String ").append(messageParamName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(logBufferVarName).append(" == null) {\n");
        sb.append("                ").append(initFileLoggingMethodName).append("();\n");
        sb.append("            }\n");
        sb.append("            String timestamp = formatTime(System.currentTimeMillis());\n");
        sb.append("            ").append(logBufferVarName).append(".write(\"[\" + timestamp + \"] [\" + ")
                .append(levelParamName).append(" + \"] \" + ").append(messageParamName).append(");\n");
        sb.append("            ").append(logBufferVarName).append(".newLine();\n");
        sb.append("            ").append(logBufferVarName).append(".flush();\n");
        sb.append("        } catch (IOException e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Failed to write to log file\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    private void ").append(closeFileLoggingMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(logBufferVarName).append(" != null) {\n");
        sb.append("                ").append(logBufferVarName).append(".close();\n");
        sb.append("                ").append(logBufferVarName).append(" = null;\n");
        sb.append("        } catch (IOException e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Failed to close file logging\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成内存日志方法
     */
    private void generateMemoryLoggingMethods(StringBuilder sb, String className,
                                              String memoryLogVarName, String tagVarName) {
        String addToMemoryLogMethodName = RandomUtils.generateWord(6);
        String getMemoryLogMethodName = RandomUtils.generateWord(6);
        String clearMemoryLogMethodName = RandomUtils.generateWord(6);
        String messageParamName = RandomUtils.generateWord(6);

        sb.append("    private void ").append(addToMemoryLogMethodName).append("(String ")
                .append(messageParamName).append(") {\n");
        sb.append("        String timestamp = formatTime(System.currentTimeMillis());\n");
        sb.append("        ").append(memoryLogVarName).append(".add(\"[\" + timestamp + \"] \" + ")
                .append(messageParamName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public List<String> ").append(getMemoryLogMethodName).append("() {\n");
        sb.append("        return new ArrayList<>(").append(memoryLogVarName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(clearMemoryLogMethodName).append("() {\n");
        sb.append("        ").append(memoryLogVarName).append(".clear();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成缓冲日志方法
     */
    private void generateBufferedLoggingMethods(StringBuilder sb, String className,
                                                String tagVarName) {
        String logExceptionMethodName = RandomUtils.generateWord(6);
        String throwableParamName = RandomUtils.generateWord(6);

        sb.append("    private String ").append(logExceptionMethodName).append("(Throwable ")
                .append(throwableParamName).append(") {\n");
        sb.append("        StringWriter sw = new StringWriter();\n");
        sb.append("        PrintWriter pw = new PrintWriter(sw);\n");
        sb.append("        ").append(throwableParamName).append(".printStackTrace(pw);\n");
        sb.append("        return sw.toString();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成异步日志方法
     */
    private void generateAsyncLoggingMethods(StringBuilder sb, String className,
                                             String tagVarName, String asyncHandler,
                                             String handlerVarName) {
        String asyncLogMethodName = RandomUtils.generateWord(6);
        String messageParamName = RandomUtils.generateWord(6);

        if (asyncHandler.contains("coroutines")) {
            sb.append("    private void ").append(asyncLogMethodName).append("(String ")
                    .append(messageParamName).append(") {\n");
            sb.append("        new CoroutineScope(Dispatchers.IO).launch(() -> {\n");
            sb.append("            Log.d(").append(tagVarName).append(", ").append(messageParamName).append(");\n");
            sb.append("        });\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("    private void ").append(asyncLogMethodName).append("(String ")
                    .append(messageParamName).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            Log.d(").append(tagVarName).append(", ").append(messageParamName).append(");\n");
            sb.append("            return true;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.single())\n");
            sb.append("          .subscribe(\n");
            sb.append("              success -> {},\n");
            sb.append("              error -> {}\n");
            sb.append("          );\n");
            sb.append("    }\n\n");
        } else {
            sb.append("    private void ").append(asyncLogMethodName).append("(String ")
                    .append(messageParamName).append(") {\n");
            sb.append("        ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("            Log.d(").append(tagVarName).append(", ").append(messageParamName).append(");\n");
            sb.append("        });\n");
            sb.append("    }\n\n");
        }
    }

    /**
     * 生成过滤日志方法
     */
    private void generateFilteredLoggingMethods(StringBuilder sb, String className,
                                                String filteredTagsVarName, String tagVarName) {
        String addFilteredTagMethodName = RandomUtils.generateWord(6);
        String removeFilteredTagMethodName = RandomUtils.generateWord(6);
        String isTagFilteredMethodName = RandomUtils.generateWord(6);
        String tagParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(addFilteredTagMethodName).append("(String ")
                .append(tagParamName).append(") {\n");
        sb.append("        ").append(filteredTagsVarName).append(".add(").append(tagParamName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(removeFilteredTagMethodName).append("(String ")
                .append(tagParamName).append(") {\n");
        sb.append("        ").append(filteredTagsVarName).append(".remove(").append(tagParamName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public boolean ").append(isTagFilteredMethodName).append("(String ")
                .append(tagParamName).append(") {\n");
        sb.append("        return ").append(filteredTagsVarName).append(".contains(")
                .append(tagParamName).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成计时日志方法
     */
    private void generateTimedLoggingMethods(StringBuilder sb, String className,
                                             String logStartTimeVarName, String tagVarName) {
        String startTimingMethodName = RandomUtils.generateWord(6);
        String endTimingMethodName = RandomUtils.generateWord(6);
        String messageParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(startTimingMethodName).append("() {\n");
        sb.append("        this.").append(logStartTimeVarName).append(" = SystemClock.elapsedRealtime();\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(endTimingMethodName).append("(String ")
                .append(messageParamName).append(") {\n");
        sb.append("        long elapsedTime = SystemClock.elapsedRealtime() - ")
                .append(logStartTimeVarName).append(";\n");
        sb.append("        Log.d(").append(tagVarName).append(", ")
                .append(messageParamName).append(" + \" took \" + elapsedTime + \"ms\");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成结构化日志方法
     */
    private void generateStructuredLoggingMethods(StringBuilder sb, String className,
                                                  String tagVarName) {
        String logStructuredMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);
        String valueParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(logStructuredMethodName).append("(String ")
                .append(keyParamName).append(", String ").append(valueParamName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            JSONObject json = new JSONObject();\n");
        sb.append("            json.put(").append(keyParamName).append(", ").append(valueParamName).append(");\n");
        sb.append("            Log.d(").append(tagVarName).append(", json.toString());\n");
        sb.append("        } catch (JSONException e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Failed to log structured data\", e);\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成日志轮转方法
     */
    private void generateLogRotationMethods(StringBuilder sb, String className,
                                            String logFileVarName, String maxLogFilesVarName,
                                            String currentLogFileIndexVarName, String tagVarName,
                                            String maxLogSizeVarName) {
        String rotateLogMethodName = RandomUtils.generateWord(6);

        sb.append("    private void ").append(rotateLogMethodName).append("() {\n");
        sb.append("        if (").append(logFileVarName).append(".exists() && ")
                .append(logFileVarName).append(".length() > ").append(maxLogSizeVarName).append(") {\n");
        sb.append("            File backupFile = new File(").append(logFileVarName)
                .append(".getParent(), \"log_\" + ").append(currentLogFileIndexVarName).append(" + \".txt\");\n");
        sb.append("            if (").append(logFileVarName).append(".renameTo(backupFile)) {\n");
        sb.append("                ").append(currentLogFileIndexVarName).append(" = (")
                .append(currentLogFileIndexVarName).append(" + 1) % ").append(maxLogFilesVarName).append(";\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Log rotated to \" + backupFile.getName());\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成示例使用方法
     */
    private void generateExampleUsageMethod(StringBuilder sb, String className,
                                            String contextVarName, String debugVarName,
                                            String logFileVarName, String logBufferVarName,
                                            String memoryLogVarName, String filteredTagsVarName,
                                            String logStartTimeVarName, String logCountVarName,
                                            String currentLogFileIndexVarName, String handlerVarName,
                                            String tagVarName, String logLevelVarName,
                                            String dateFormatVarName) {
        String exampleMethodName = RandomUtils.generateWord(6);
        String messageParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(exampleMethodName).append("() {\n");
        sb.append("        setDebug(true);\n");

        if (useFileLogging) {
            sb.append("        writeToFile(\"D\", \"").append(RandomUtils.generateWord(6)).append("\");\n");
        }

        if (useMemoryLogging) {
            sb.append("        addToMemoryLog(\"").append(RandomUtils.generateWord(6)).append("\");\n");
        }

        if (useFilteredLogging) {
            sb.append("        addFilteredTag(\"").append(RandomUtils.generateWord(6)).append("\");\n");
        }

        if (useTimedLogging) {
            sb.append("        startTiming();\n");
            sb.append("        endTiming(\"").append(RandomUtils.generateWord(6)).append("\");\n");
        }

        if (useStructuredLogging) {
            sb.append("        logStructured(\"").append(RandomUtils.generateWord(6)).append("\", \"")
                    .append(RandomUtils.generateWord(6)).append("\");\n");
        }

        sb.append("        String ").append(messageParamName).append(" = \"").append(RandomUtils.generateWord(6))
                .append("\";\n");
        sb.append("        Log.d(").append(tagVarName).append(", ").append(messageParamName).append(");\n");

        sb.append("    }\n\n");
    }
}