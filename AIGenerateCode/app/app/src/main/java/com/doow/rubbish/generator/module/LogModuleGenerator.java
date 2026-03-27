package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.EnhancedRandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LogModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;

    // 日志级别
    private static final String[] LOG_LEVELS = {
        "verbose", "debug", "info", "warning", "error"
    };

    // 日志存储方式
    private static final String[] LOG_STORAGE_TYPES = {
        "file", "database", "memory", "remote"
    };

    // 日志格式
    private static final String[] LOG_FORMATS = {
        "simple", "detailed", "json", "xml"
    };

    public LogModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成日志模块");

        // 获取当前数据存储方式和异步处理方式
        String storageType = variationManager.getVariation("storage");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成日志模块
        generateLogModule(storageType, asyncHandler);
    }

    private void generateLogModule(String storageType, String asyncHandler) throws Exception {
        // 生成日志管理器
        generateLogManager(storageType, asyncHandler);

        // 生成日志工具类
        generateLogUtils(storageType, asyncHandler);

        // 生成日志拦截器
        generateLogInterceptor(storageType, asyncHandler);
    }

    private void generateLogManager(String storageType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Manager");
        String instanceName = EnhancedRandomUtils.generateVariableName("Instance");
        String contextName = EnhancedRandomUtils.generateObjectName();
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String logFileName = EnhancedRandomUtils.generateStringName();
        String maxLogSizeName = EnhancedRandomUtils.generateIntName();
        String dateFormatName = EnhancedRandomUtils.generateStringName();

        // 使用随机值
        int maxLogSize = EnhancedRandomUtils.generateIntRange(1, 10)[0] * 1024 * 1024; // 1-10MB
        String dateFormat = "yyyy-MM-dd HH:mm:ss";

        sb.append("package ").append(packageName).append(".log;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import java.io.File;\n");
        sb.append("import java.io.FileWriter;\n");
        sb.append("import java.text.SimpleDateFormat;\n");
        sb.append("import java.util.Date;\n");
        sb.append("import java.util.Locale;\n");

        // 根据异步处理方式添加导入
        if (asyncHandler.contains("coroutines")) {
            sb.append("import kotlinx.coroutines.CoroutineScope;\n");
            sb.append("import kotlinx.coroutines.Dispatchers;\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("import io.reactivex.rxjava3.core.Single;\n");
            sb.append("import io.reactivex.rxjava3.schedulers.Schedulers;\n");
        }

        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(logFileName).append(" = \"").append(logFileName).append(".txt\";\n");
        sb.append("    private static final int ").append(maxLogSizeName).append(" = ").append(maxLogSize).append(";\n");
        sb.append("    private static final String ").append(dateFormatName).append(" = \"").append(dateFormat).append("\";\n");
        sb.append("\n");

        // 单例
        sb.append("    private static volatile ").append(className).append(" ").append(instanceName).append(";\n");
        sb.append("    private final Context ").append(contextName).append(";\n");
        sb.append("    private final File logFile;\n");
        sb.append("\n");

        // 构造函数
        sb.append("    private ").append(className).append("(Context ").append(contextName).append(") {\n");
        sb.append("        this.").append(contextName).append(" = ").append(contextName).append(".getApplicationContext();\n");
        sb.append("        logFile = new File(").append(contextName).append(".getExternalFilesDir(null), ").append(logFileName).append(");\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取实例方法
        sb.append("    public static ").append(className).append(" getInstance(Context ").append(contextName).append(") {\n");
        sb.append("        if (").append(instanceName).append(" == null) {\n");
        sb.append("            synchronized (").append(className).append(".class) {\n");
        sb.append("                if (").append(instanceName).append(" == null) {\n");
        sb.append("                    ").append(instanceName).append(" = new ").append(className).append("(").append(contextName).append(");\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(instanceName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        // 日志方法
        generateLogMethods(sb, className, tagName, dateFormatName);

        // 清空日志方法
        generateClearLogsMethod(sb, className, tagName);

        // 获取日志内容方法
        generateGetLogsMethod(sb, className, tagName);

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("log");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateLogMethods(StringBuilder sb, String className, String tagName, String dateFormatName) {
        // Verbose日志方法
        generateVerboseMethod(sb, className, tagName, dateFormatName);

        // Debug日志方法
        generateDebugMethod(sb, className, tagName, dateFormatName);

        // Info日志方法
        generateInfoMethod(sb, className, tagName, dateFormatName);

        // Warning日志方法
        generateWarningMethod(sb, className, tagName, dateFormatName);

        // Error日志方法
        generateErrorMethod(sb, className, tagName, dateFormatName);

        // Error日志方法（带异常）
        generateErrorWithThrowableMethod(sb, className, tagName, dateFormatName);
    }

    private void generateVerboseMethod(StringBuilder sb, String className, String tagName, String dateFormatName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Verbose");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Verbose log");

        sb.append("    public void ").append(methodName).append("(String tag, String message) {\n");
        sb.append("        Log.v(tag, message);\n");
        sb.append("        writeToFile(\"V\", tag, message);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateDebugMethod(StringBuilder sb, String className, String tagName, String dateFormatName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Debug");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Debug log");

        sb.append("    public void ").append(methodName).append("(String tag, String message) {\n");
        sb.append("        Log.d(tag, message);\n");
        sb.append("        writeToFile(\"D\", tag, message);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateInfoMethod(StringBuilder sb, String className, String tagName, String dateFormatName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Info");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Info log");

        sb.append("    public void ").append(methodName).append("(String tag, String message) {\n");
        sb.append("        Log.i(tag, message);\n");
        sb.append("        writeToFile(\"I\", tag, message);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateWarningMethod(StringBuilder sb, String className, String tagName, String dateFormatName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Warning");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Warning log");

        sb.append("    public void ").append(methodName).append("(String tag, String message) {\n");
        sb.append("        Log.w(tag, message);\n");
        sb.append("        writeToFile(\"W\", tag, message);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateErrorMethod(StringBuilder sb, String className, String tagName, String dateFormatName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Error");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Error log");

        sb.append("    public void ").append(methodName).append("(String tag, String message) {\n");
        sb.append("        Log.e(tag, message);\n");
        sb.append("        writeToFile(\"E\", tag, message);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateErrorWithThrowableMethod(StringBuilder sb, String className, String tagName, String dateFormatName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Error");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Error log with exception");

        sb.append("    public void ").append(methodName).append("(String tag, String message, Throwable throwable) {\n");
        sb.append("        Log.e(tag, message, throwable);\n");
        sb.append("        writeToFile(\"E\", tag, message + \"\n\" + Log.getStackTraceString(throwable));\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateWriteToFileMethod(StringBuilder sb, String className, String tagName, String dateFormatName, String maxLogSizeName) {
        String methodName = EnhancedRandomUtils.generateMethodName("WriteToFile");
        String logEntryName = EnhancedRandomUtils.generateStringName();

        sb.append("    private void writeToFile(String level, String tag, String message) {\n");
        sb.append("        try {\n");
        sb.append("            SimpleDateFormat sdf = new SimpleDateFormat(").append(dateFormatName).append(", Locale.getDefault());\n");
        sb.append("            String timestamp = sdf.format(new Date());\n");
        sb.append("            String ").append(logEntryName).append(" = timestamp + \" \" + level + \"/\" + tag + \": \" + message + \"\n\";\n");
        sb.append("\n");
        sb.append("            // 检查日志文件大小\n");
        sb.append("            if (logFile.exists() && logFile.length() > ").append(maxLogSizeName).append(") {\n");
        sb.append("                logFile.delete();\n");
        sb.append("            }\n");
        sb.append("\n");
        sb.append("            // 写入日志\n");
        sb.append("            java.io.FileWriter writer = new java.io.FileWriter(logFile, true);\n");
        sb.append("            writer.write(").append(logEntryName).append(");\n");
        sb.append("            writer.close();\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"Error writing log to file\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateClearLogsMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ClearLogs");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Cleared log file");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        if (logFile.exists()) {\n");
        sb.append("            logFile.delete();\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetLogsMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetLogs");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Retrieved logs");

        sb.append("    public String ").append(methodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            if (logFile.exists()) {\n");
        sb.append("                java.io.FileReader reader = new java.io.FileReader(logFile);\n");
        sb.append("                char[] buffer = new char[(int) logFile.length()];\n");
        sb.append("                reader.read(buffer);\n");
        sb.append("                reader.close();\n");
        sb.append("                return new String(buffer);\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"Error reading log file\", e);\n");
        sb.append("        }\n");
        sb.append("        return \"\";\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateLogUtils(String storageType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Utils");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String debugName = EnhancedRandomUtils.generateVariableName("Debug");

        sb.append("package ").append(packageName).append(".log;\n");
        sb.append("\n");

        // 导入
        sb.append("import android.util.Log;\n");

        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static boolean ").append(debugName).append(" = true;\n");
        sb.append("\n");

        // 私有构造函数
        sb.append("    private ").append(className).append("() {\n");
        sb.append("        // 私有构造函数，防止实例化\n");
        sb.append("    }\n");
        sb.append("\n");

        // 设置调试模式方法
        generateSetDebugMethod(sb, className, debugName);

        // 日志方法
        generateLogUtilsMethods(sb, className, tagName, debugName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "log");
    }

    private void generateSetDebugMethod(StringBuilder sb, String className, String debugName) {
        String methodName = EnhancedRandomUtils.generateMethodName("SetDebug");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Debug mode changed");

        sb.append("    public static void ").append(methodName).append("(boolean debug) {\n");
        sb.append("        ").append(debugName).append(" = debug;\n");
        sb.append("        Log.d(").append(className).append(", \"").append(logMessage).append(": \" + debug);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateLogUtilsMethods(StringBuilder sb, String className, String tagName, String debugName) {
        // Verbose日志方法
        generateUtilsVerboseMethod(sb, className, tagName, debugName);

        // Debug日志方法
        generateUtilsDebugMethod(sb, className, tagName, debugName);

        // Info日志方法
        generateUtilsInfoMethod(sb, className, tagName, debugName);

        // Warning日志方法
        generateUtilsWarningMethod(sb, className, tagName, debugName);

        // Error日志方法
        generateUtilsErrorMethod(sb, className, tagName, debugName);

        // Error日志方法（带异常）
        generateUtilsErrorWithThrowableMethod(sb, className, tagName, debugName);
    }

    private void generateUtilsVerboseMethod(StringBuilder sb, String className, String tagName, String debugName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Verbose");

        sb.append("    public static void ").append(methodName).append("(String message) {\n");
        sb.append("        if (").append(debugName).append(") {\n");
        sb.append("            Log.v(").append(tagName).append(", message);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateUtilsDebugMethod(StringBuilder sb, String className, String tagName, String debugName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Debug");

        sb.append("    public static void ").append(methodName).append("(String message) {\n");
        sb.append("        if (").append(debugName).append(") {\n");
        sb.append("            Log.d(").append(tagName).append(", message);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateUtilsInfoMethod(StringBuilder sb, String className, String tagName, String debugName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Info");

        sb.append("    public static void ").append(methodName).append("(String message) {\n");
        sb.append("        if (").append(debugName).append(") {\n");
        sb.append("            Log.i(").append(tagName).append(", message);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateUtilsWarningMethod(StringBuilder sb, String className, String tagName, String debugName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Warning");

        sb.append("    public static void ").append(methodName).append("(String message) {\n");
        sb.append("        if (").append(debugName).append(") {\n");
        sb.append("            Log.w(").append(tagName).append(", message);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateUtilsErrorMethod(StringBuilder sb, String className, String tagName, String debugName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Error");

        sb.append("    public static void ").append(methodName).append("(String message) {\n");
        sb.append("        if (").append(debugName).append(") {\n");
        sb.append("            Log.e(").append(tagName).append(", message);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateUtilsErrorWithThrowableMethod(StringBuilder sb, String className, String tagName, String debugName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Error");

        sb.append("    public static void ").append(methodName).append("(String message, Throwable throwable) {\n");
        sb.append("        if (").append(debugName).append(") {\n");
        sb.append("            Log.e(").append(tagName).append(", message, throwable);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateLogInterceptor(String storageType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Interceptor");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String debugName = EnhancedRandomUtils.generateVariableName("Debug");

        sb.append("package ").append(packageName).append(".log;\n");
        sb.append("\n");

        // 导入
        sb.append("import android.util.Log;\n");
        sb.append("import java.io.IOException;\n");
        sb.append("import okhttp3.Interceptor;\n");
        sb.append("import okhttp3.Request;\n");
        sb.append("import okhttp3.Response;\n");
        sb.append("import okhttp3.ResponseBody;\n");

        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" implements Interceptor {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static boolean ").append(debugName).append(" = true;\n");
        sb.append("\n");

        // 拦截方法
        generateInterceptMethod(sb, className, tagName, debugName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "log");
    }

    private void generateInterceptMethod(StringBuilder sb, String className, String tagName, String debugName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Intercept");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Request sent");
        String responseLogMessage = EnhancedRandomUtils.generateLogMessage("Response received");

        sb.append("    @Override\n");
        sb.append("    public Response intercept(Chain chain) throws IOException {\n");
        sb.append("        Request request = chain.request();\n");
        sb.append("\n");
        sb.append("        // 记录请求\n");
        sb.append("        long startTime = System.nanoTime();\n");
        sb.append("        if (").append(debugName).append(") {\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + request.url());\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 执行请求\n");
        sb.append("        Response response = chain.proceed(request);\n");
        sb.append("\n");
        sb.append("        // 记录响应\n");
        sb.append("        long endTime = System.nanoTime();\n");
        sb.append("        long duration = (endTime - startTime) / 1_000_000;\n");
        sb.append("        if (").append(debugName).append(") {\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(responseLogMessage).append(" in \" + duration + \"ms: \" + response.code());\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        return response;\n");
        sb.append("    }\n");
        sb.append("\n");
    }
}
