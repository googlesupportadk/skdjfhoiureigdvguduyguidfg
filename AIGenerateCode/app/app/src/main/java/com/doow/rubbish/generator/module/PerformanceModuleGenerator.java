package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.EnhancedRandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class PerformanceModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;

    // 性能指标类型
    private static final String[] METRIC_TYPES = {
        "cpu", "memory", "network", "disk", "battery"
    };

    // 监控方式
    private static final String[] MONITORING_TYPES = {
        "realtime", "periodic", "event_driven", "manual"
    };

    // 报告方式
    private static final String[] REPORT_TYPES = {
        "log", "file", "database", "remote", "dashboard"
    };

    // 采样间隔
    private static final int[] SAMPLING_INTERVALS = {
        100, 200, 500, 1000, 2000, 5000
    };

    // 缓冲区大小
    private static final int[] BUFFER_SIZES = {
        10, 20, 50, 100, 200, 500
    };

    public PerformanceModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成性能模块");

        // 获取当前UI风格和异步处理方式
        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成性能模块
        generatePerformanceModule(uiStyle, asyncHandler);
    }

    private void generatePerformanceModule(String uiStyle, String asyncHandler) throws Exception {
        // 生成性能监控器
        generatePerformanceMonitor(uiStyle, asyncHandler);

        // 生成性能工具类
        generatePerformanceUtils(uiStyle, asyncHandler);

        // 生成性能报告器
        generatePerformanceReporter(uiStyle, asyncHandler);
    }

    private void generatePerformanceMonitor(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Monitor");
        String instanceName = EnhancedRandomUtils.generateVariableName("Instance");
        String contextName = EnhancedRandomUtils.generateObjectName();
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String callbackName = EnhancedRandomUtils.generateClassName("Callback");
        String callbackVarName = EnhancedRandomUtils.generateVariableName("Callback");
        String metricsName = EnhancedRandomUtils.generateCollectionName();

        // 使用随机值
        int samplingInterval = SAMPLING_INTERVALS[EnhancedRandomUtils.between(0, SAMPLING_INTERVALS.length - 1)];
        int bufferSize = BUFFER_SIZES[EnhancedRandomUtils.between(0, BUFFER_SIZES.length - 1)];

        sb.append("package ").append(packageName).append(".performance;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import java.util.ArrayList;\n");
        sb.append("import java.util.HashMap;\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Map;\n");

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
        sb.append("    private static final int SAMPLING_INTERVAL = ").append(samplingInterval).append(";\n");
        sb.append("    private static final int BUFFER_SIZE = ").append(bufferSize).append(";\n");
        sb.append("\n");

        // 回调接口
        sb.append("    public interface ").append(callbackName).append(" {\n");
        sb.append("        void onMetricsUpdated(Map<String, Object> metrics);\n");
        sb.append("        void onError(String error);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 成员变量
        sb.append("    private final Context ").append(contextName).append(";\n");
        sb.append("    private final ").append(callbackName).append(" ").append(callbackVarName).append(";\n");
        sb.append("    private final List<Map<String, Object>> ").append(metricsName).append(";\n");
        sb.append("    private volatile boolean isMonitoring;\n");
        sb.append("\n");

        // 构造函数
        sb.append("    public ").append(className).append("(Context ").append(contextName).append(", ").append(callbackName).append(" ").append(callbackVarName).append(") {\n");
        sb.append("        this.").append(contextName).append(" = ").append(contextName).append(".getApplicationContext();\n");
        sb.append("        this.").append(callbackVarName).append(" = ").append(callbackVarName).append(";\n");
        sb.append("        this.").append(metricsName).append(" = new ArrayList<>();\n");
        sb.append("        this.isMonitoring = false;\n");
        sb.append("    }\n");
        sb.append("\n");

        // 开始监控方法
        generateStartMonitoringMethod(sb, className, tagName, metricsName);

        // 停止监控方法
        generateStopMonitoringMethod(sb, className, tagName, metricsName, callbackVarName);

        // 收集指标方法
        generateCollectMetricsMethod(sb, className, metricsName, tagName);

        // 获取指标方法
        generateGetMetricsMethod(sb, className, metricsName);

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("performance");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateStartMonitoringMethod(StringBuilder sb, String className, String tagName, String metricsName) {
        String methodName = EnhancedRandomUtils.generateMethodName("StartMonitoring");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Monitoring started");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        if (isMonitoring) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        isMonitoring = true;\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateStopMonitoringMethod(StringBuilder sb, String className, String tagName, String metricsName, String callbackVarName) {
        String methodName = EnhancedRandomUtils.generateMethodName("StopMonitoring");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Monitoring stopped");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        if (!isMonitoring) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        isMonitoring = false;\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onMetricsUpdated(new HashMap<>(").append(metricsName).append("));\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateCollectMetricsMethod(StringBuilder sb, String className, String metricsName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("CollectMetrics");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Metrics collected");
        String cpuName = EnhancedRandomUtils.generateStringName();
        String memoryName = EnhancedRandomUtils.generateStringName();
        String networkName = EnhancedRandomUtils.generateStringName();

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        if (!isMonitoring) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 收集CPU指标\n");
        sb.append("        float ").append(cpuName).append(" = getCpuUsage();\n");
        sb.append("\n");
        sb.append("        // 收集内存指标\n");
        sb.append("        long ").append(memoryName).append(" = getMemoryUsage();\n");
        sb.append("\n");
        sb.append("        // 收集网络指标\n");
        sb.append("        long ").append(networkName).append(" = getNetworkUsage();\n");
        sb.append("\n");
        sb.append("        // 创建指标映射\n");
        sb.append("        Map<String, Object> metrics = new HashMap<>();\n");
        sb.append("        metrics.put(\"cpu\", ").append(cpuName).append(");\n");
        sb.append("        metrics.put(\"memory\", ").append(memoryName).append(");\n");
        sb.append("        metrics.put(\"network\", ").append(networkName).append(");\n");
        sb.append("\n");
        sb.append("        // 添加到缓冲区\n");
        sb.append("        ").append(metricsName).append(".add(metrics);\n");
        sb.append("\n");
        sb.append("        // 限制缓冲区大小\n");
        sb.append("        while (").append(metricsName).append(".size() > BUFFER_SIZE) {\n");
        sb.append("            ").append(metricsName).append(".remove(0);\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetMetricsMethod(StringBuilder sb, String className, String metricsName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetMetrics");

        sb.append("    public List<Map<String, Object>> ").append(methodName).append("() {\n");
        sb.append("        return new ArrayList<>(").append(metricsName).append(");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generatePerformanceUtils(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Utils");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");

        sb.append("package ").append(packageName).append(".performance;\n");
        sb.append("\n");

        // 导入
        sb.append("import android.app.ActivityManager;\n");
        sb.append("import android.content.Context;\n");
        sb.append("import android.os.Debug;\n");
        sb.append("import android.util.Log;\n");

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
        sb.append("\n");

        // 私有构造函数
        sb.append("    private ").append(className).append("() {\n");
        sb.append("        // 私有构造函数，防止实例化\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取CPU使用率方法
        generateGetCpuUsageMethod(sb, className, tagName);

        // 获取内存使用方法
        generateGetMemoryUsageMethod(sb, className, tagName);

        // 获取网络使用方法
        generateGetNetworkUsageMethod(sb, className, tagName);

        // 获取磁盘使用方法
        generateGetDiskUsageMethod(sb, className, tagName);

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("performance");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateGetCpuUsageMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetCpuUsage");
        String logMessage = EnhancedRandomUtils.generateLogMessage("CPU usage retrieved");

        sb.append("    public static float ").append(methodName).append("(Context context) {\n");
        sb.append("        try {\n");
        sb.append("            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);\n");
        sb.append("            Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();\n");
        sb.append("            Debug.getMemoryInfo(memoryInfo);\n");
        sb.append("            float cpuUsage = memoryInfo.totalPrivateDirty * 100.0f / memoryInfo.totalPrivate;\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + cpuUsage + \"%\");\n");
        sb.append("            return cpuUsage;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"Error getting CPU usage\", e);\n");
        sb.append("            return 0.0f;\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetMemoryUsageMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetMemoryUsage");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Memory usage retrieved");

        sb.append("    public static long ").append(methodName).append("(Context context) {\n");
        sb.append("        try {\n");
        sb.append("            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);\n");
        sb.append("            Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();\n");
        sb.append("            Debug.getMemoryInfo(memoryInfo);\n");
        sb.append("            long memoryUsage = memoryInfo.totalPrivateDirty;\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + memoryUsage + \" bytes\");\n");
        sb.append("            return memoryUsage;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"Error getting memory usage\", e);\n");
        sb.append("            return 0L;\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetNetworkUsageMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetNetworkUsage");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Network usage retrieved");

        sb.append("    public static long ").append(methodName).append("(Context context) {\n");
        sb.append("        try {\n");
        sb.append("            android.net.ConnectivityManager cm = (android.net.ConnectivityManager)\n");
        sb.append("                context.getSystemService(Context.CONNECTIVITY_SERVICE);\n");
        sb.append("            android.net.NetworkInfo activeNetwork = cm.getActiveNetworkInfo();\n");
        sb.append("            if (activeNetwork != null) {\n");
        sb.append("                long networkUsage = System.currentTimeMillis();\n");
        sb.append("                Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + networkUsage);\n");
        sb.append("                return networkUsage;\n");
        sb.append("            }\n");
        sb.append("            return 0L;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"Error getting network usage\", e);\n");
        sb.append("            return 0L;\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetDiskUsageMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetDiskUsage");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Disk usage retrieved");

        sb.append("    public static long ").append(methodName).append("(Context context) {\n");
        sb.append("        try {\n");
        sb.append("            java.io.File path = context.getFilesDir();\n");
        sb.append("            long diskUsage = 0L;\n");
        sb.append("            if (path.exists()) {\n");
        sb.append("                diskUsage = getFolderSize(path);\n");
        sb.append("            }\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + diskUsage + \" bytes\");\n");
        sb.append("            return diskUsage;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"Error getting disk usage\", e);\n");
        sb.append("            return 0L;\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取文件夹大小方法
        sb.append("    private static long getFolderSize(java.io.File folder) {\n");
        sb.append("        long size = 0L;\n");
        sb.append("        java.io.File[] files = folder.listFiles();\n");
        sb.append("        if (files != null) {\n");
        sb.append("            for (java.io.File file : files) {\n");
        sb.append("                if (file.isFile()) {\n");
        sb.append("                    size += file.length();\n");
        sb.append("                } else if (file.isDirectory()) {\n");
        sb.append("                    size += getFolderSize(file);\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return size;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generatePerformanceReporter(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Reporter");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String metricsName = EnhancedRandomUtils.generateCollectionName();

        sb.append("package ").append(packageName).append(".performance;\n");
        sb.append("\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import java.io.FileWriter;\n");
        sb.append("import java.io.IOException;\n");
        sb.append("import java.text.SimpleDateFormat;\n");
        sb.append("import java.util.Date;\n");
        sb.append("import java.util.Locale;\n");
        sb.append("import java.util.Map;\n");

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
        sb.append("    private static final String REPORT_FILE = \"performance_report.txt\";\n");
        sb.append("    private static final String DATE_FORMAT = \"yyyy-MM-dd HH:mm:ss\";\n");
        sb.append("\n");

        // 私有构造函数
        sb.append("    private ").append(className).append("() {\n");
        sb.append("        // 私有构造函数，防止实例化\n");
        sb.append("    }\n");
        sb.append("\n");

        // 生成报告方法
        generateGenerateReportMethod(sb, className, tagName, metricsName);

        // 保存报告方法
        generateSaveReportMethod(sb, className, tagName);

        // 发送报告方法
        generateSendReportMethod(sb, className, tagName);

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("performance");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateGenerateReportMethod(StringBuilder sb, String className, String tagName, String metricsName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GenerateReport");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Report generated");
        String reportName = EnhancedRandomUtils.generateStringName();

        sb.append("    public static String ").append(methodName).append("(Map<String, Object> ").append(metricsName).append(") {\n");
        sb.append("        StringBuilder ").append(reportName).append(" = new StringBuilder();\n");
        sb.append("        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());\n");
        sb.append("        String timestamp = sdf.format(new Date());\n");
        sb.append("        ").append(reportName).append(".append(\"Performance Report - \").append(timestamp).append(\"\n\n");\n");
        sb.append("        ").append(reportName).append(".append(\"====================================\n\n");\n");
        sb.append("\n");
        sb.append("        // 添加所有指标\n");
        sb.append("        for (Map.Entry<String, Object> entry : ").append(metricsName).append(".entrySet()) {\n");
        sb.append("            ").append(reportName).append(".append(entry.getKey()).append(\" : \").append(entry.getValue()).append(\"\n");\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        ").append(reportName).append(".append(\"====================================\n");\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        return ").append(reportName).append(".toString();\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateSaveReportMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("SaveReport");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Report saved");
        String reportName = EnhancedRandomUtils.generateStringName();

        sb.append("    public static void ").append(methodName).append("(Context context, String report) {\n");
        sb.append("        try {\n");
        sb.append("            java.io.File file = new java.io.File(context.getExternalFilesDir(null), REPORT_FILE);\n");
        sb.append("            java.io.FileWriter writer = new java.io.FileWriter(file);\n");
        sb.append("            writer.write(report);\n");
        sb.append("            writer.close();\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + file.getAbsolutePath());\n");
        sb.append("        } catch (IOException e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"Error saving report\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateSendReportMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("SendReport");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Report sent");
        String reportName = EnhancedRandomUtils.generateStringName();

        sb.append("    public static void ").append(methodName).append("(Context context, String report) {\n");
        sb.append("        try {\n");
        sb.append("            android.content.Intent intent = new android.content.Intent();\n");
        sb.append("            intent.setAction(android.content.Intent.ACTION_SEND);\n");
        sb.append("            intent.setType(\"text/plain\");\n");
        sb.append("            intent.putExtra(android.content.Intent.EXTRA_TEXT, report);\n");
        sb.append("            context.startActivity(android.content.Intent.createChooser(intent, \"Share Report\"));\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"Error sending report\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }
}
