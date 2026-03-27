package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.EnhancedRandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class TimerModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;

    // 定时器类型
    private static final String[] TIMER_TYPES = {
        "countdown", "stopwatch", "interval", "cron", "schedule"
    };

    // 计时器精度
    private static final String[] PRECISION_TYPES = {
        "milliseconds", "seconds", "minutes", "hours"
    };

    // 通知方式
    private static final String[] NOTIFICATION_TYPES = {
        "sound", "vibration", "visual", "none"
    };

    // 暂停方式
    private static final String[] PAUSE_TYPES = {
        "pause", "stop", "resume"
    };

    // 重置方式
    private static final String[] RESET_TYPES = {
        "reset", "restart", "continue"
    };

    public TimerModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成定时器模块");

        // 获取当前UI风格和异步处理方式
        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成定时器模块
        generateTimerModule(uiStyle, asyncHandler);
    }

    private void generateTimerModule(String uiStyle, String asyncHandler) throws Exception {
        // 生成定时器管理器
        generateTimerManager(uiStyle, asyncHandler);

        // 生成定时器工具类
        generateTimerUtils(uiStyle, asyncHandler);

        // 生成定时器监听器
        generateTimerListener(uiStyle, asyncHandler);
    }

    private void generateTimerManager(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Timer");
        String instanceName = EnhancedRandomUtils.generateVariableName("Instance");
        String contextName = EnhancedRandomUtils.generateObjectName();
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String callbackName = EnhancedRandomUtils.generateClassName("Callback");
        String callbackVarName = EnhancedRandomUtils.generateVariableName("Callback");
        String timerName = EnhancedRandomUtils.generateVariableName("Timer");
        String remainingTimeName = EnhancedRandomUtils.generateVariableName("RemainingTime");
        String totalTimeName = EnhancedRandomUtils.generateVariableName("TotalTime");
        String isRunningName = EnhancedRandomUtils.generateBooleanName();

        // 使用随机值
        int defaultInterval = EnhancedRandomUtils.generateIntRange(100, 1000)[0];
        int defaultDuration = EnhancedRandomUtils.generateIntRange(1000, 60000)[0];

        sb.append("package ").append(packageName).append(".timer;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.os.CountDownTimer;\n");
        sb.append("import android.os.Handler;\n");
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
        sb.append("    private static final long DEFAULT_INTERVAL = ").append(defaultInterval).append(";\n");
        sb.append("    private static final long DEFAULT_DURATION = ").append(defaultDuration).append(";\n");
        sb.append("\n");

        // 回调接口
        sb.append("    public interface ").append(callbackName).append(" {\n");
        sb.append("        void onTick(long remainingTime);\n");
        sb.append("        void onFinish();\n");
        sb.append("        void onError(String error);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 成员变量
        sb.append("    private final Context ").append(contextName).append(";\n");
        sb.append("    private final ").append(callbackName).append(" ").append(callbackVarName).append(";\n");
        sb.append("    private CountDownTimer ").append(timerName).append(";\n");
        sb.append("    private long ").append(remainingTimeName).append(";\n");
        sb.append("    private long ").append(totalTimeName).append(";\n");
        sb.append("    private boolean ").append(isRunningName).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    public ").append(className).append("(Context ").append(contextName).append(", ").append(callbackName).append(" ").append(callbackVarName).append(") {\n");
        sb.append("        this.").append(contextName).append(" = ").append(contextName).append(".getApplicationContext();\n");
        sb.append("        this.").append(callbackVarName).append(" = ").append(callbackVarName).append(";\n");
        sb.append("        this.").append(isRunningName).append(" = false;\n");
        sb.append("        this.").append(remainingTimeName).append(" = 0;\n");
        sb.append("        this.").append(totalTimeName).append(" = 0;\n");
        sb.append("        initTimer();\n");
        sb.append("    }\n");
        sb.append("\n");

        // 初始化定时器方法
        generateInitTimerMethod(sb, className, timerName, defaultInterval, defaultDuration, tagName);

        // 开始计时方法
        generateStartMethod(sb, className, timerName, remainingTimeName, totalTimeName, isRunningName, tagName);

        // 暂停计时方法
        generatePauseMethod(sb, className, timerName, isRunningName, tagName);

        // 继续计时方法
        generateResumeMethod(sb, className, timerName, isRunningName, tagName);

        // 停止计时方法
        generateStopMethod(sb, className, timerName, isRunningName, remainingTimeName, callbackVarName, tagName);

        // 重置计时方法
        generateResetMethod(sb, className, timerName, remainingTimeName, totalTimeName, isRunningName, tagName);

        // 获取剩余时间方法
        generateGetRemainingTimeMethod(sb, className, remainingTimeName);

        // 获取总时间方法
        generateGetTotalTimeMethod(sb, className, totalTimeName);

        // 获取运行状态方法
        generateIsRunningMethod(sb, className, isRunningName);

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("timer");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateInitTimerMethod(StringBuilder sb, String className, String timerName,
                                          int defaultInterval, int defaultDuration, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("InitTimer");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Timer initialized");

        sb.append("    private void initTimer() {\n");
        sb.append("        ").append(timerName).append(" = new CountDownTimer(DEFAULT_DURATION, DEFAULT_INTERVAL) {\n");
        sb.append("            @Override\n");
        sb.append("            public void onTick(long millisUntilFinished) {\n");
        sb.append("                ").append(remainingTimeName).append(" = millisUntilFinished;\n");
        sb.append("                Log.d(").append(tagName).append(", \"Tick: \" + ").append(remainingTimeName).append(" + \" ms\");\n");
        sb.append("                if (").append(callbackVarName).append(" != null) {\n");
        sb.append("                    ").append(callbackVarName).append(".onTick(").append(remainingTimeName).append(");\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("\n");
        sb.append("            @Override\n");
        sb.append("            public void onFinish() {\n");
        sb.append("                ").append(remainingTimeName).append(" = 0;\n");
        sb.append("                ").append(isRunningName).append(" = false;\n");
        sb.append("                Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("                if (").append(callbackVarName).append(" != null) {\n");
        sb.append("                    ").append(callbackVarName).append(".onFinish();\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        };\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateStartMethod(StringBuilder sb, String className, String timerName,
                                     String remainingTimeName, String totalTimeName,
                                     String isRunningName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Start");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Timer started");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        if (").append(isRunningName).append(") {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        ").append(timerName).append(".start();\n");
        sb.append("        ").append(isRunningName).append(" = true;\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generatePauseMethod(StringBuilder sb, String className, String timerName, String isRunningName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Pause");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Timer paused");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        if (!").append(isRunningName).append(") {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        ").append(timerName).append(".cancel();\n");
        sb.append("        ").append(isRunningName).append(" = false;\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateResumeMethod(StringBuilder sb, String className, String timerName, String isRunningName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Resume");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Timer resumed");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        if (").append(isRunningName).append(") {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        ").append(timerName).append(".start();\n");
        sb.append("        ").append(isRunningName).append(" = true;\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateStopMethod(StringBuilder sb, String className, String timerName, String isRunningName,
                                     String remainingTimeName, String callbackVarName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Stop");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Timer stopped");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        if (!").append(isRunningName).append(") {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        ").append(timerName).append(".cancel();\n");
        sb.append("        ").append(isRunningName).append(" = false;\n");
        sb.append("        ").append(remainingTimeName).append(" = 0;\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onFinish();\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateResetMethod(StringBuilder sb, String className, String timerName,
                                     String remainingTimeName, String totalTimeName,
                                     String isRunningName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Reset");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Timer reset");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        ").append(timerName).append(".cancel();\n");
        sb.append("        ").append(remainingTimeName).append(" = 0;\n");
        sb.append("        ").append(totalTimeName).append(" = 0;\n");
        sb.append("        ").append(isRunningName).append(" = false;\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetRemainingTimeMethod(StringBuilder sb, String className, String remainingTimeName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetRemainingTime");

        sb.append("    public long ").append(methodName).append("() {\n");
        sb.append("        return ").append(remainingTimeName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetTotalTimeMethod(StringBuilder sb, String className, String totalTimeName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetTotalTime");

        sb.append("    public long ").append(methodName).append("() {\n");
        sb.append("        return ").append(totalTimeName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateIsRunningMethod(StringBuilder sb, String className, String isRunningName) {
        String methodName = EnhancedRandomUtils.generateMethodName("IsRunning");

        sb.append("    public boolean ").append(methodName).append("() {\n");
        sb.append("        return ").append(isRunningName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateTimerUtils(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Utils");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");

        sb.append("package ").append(packageName).append(".timer;\n");
        sb.append("\n");

        // 导入
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

        // 格式化时间方法
        generateFormatTimeMethod(sb, className, tagName);

        // 解析时间方法
        generateParseTimeMethod(sb, className, tagName);

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("timer");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateFormatTimeMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("FormatTime");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Time formatted");

        sb.append("    public static String ").append(methodName).append("(long milliseconds) {\n");
        sb.append("        long seconds = milliseconds / 1000;\n");
        sb.append("        long minutes = seconds / 60;\n");
        sb.append("        long hours = minutes / 60;\n");
        sb.append("        seconds = seconds % 60;\n");
        sb.append("        minutes = minutes % 60;\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        return String.format(\"%02d:%02d:%02d\", hours, minutes, seconds);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateParseTimeMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ParseTime");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Time parsed");

        sb.append("    public static long ").append(methodName).append("(String timeString) {\n");
        sb.append("        try {\n");
        sb.append("            String[] parts = timeString.split(\"\");\n");
        sb.append("            long hours = Long.parseLong(parts[0]);\n");
        sb.append("            long minutes = Long.parseLong(parts[1]);\n");
        sb.append("            long seconds = Long.parseLong(parts[2]);\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("            return (hours * 3600 + minutes * 60 + seconds) * 1000;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"Error parsing time\", e);\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateTimerListener(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Listener");
        String contextName = EnhancedRandomUtils.generateObjectName();
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String callbackName = EnhancedRandomUtils.generateClassName("Callback");
        String callbackVarName = EnhancedRandomUtils.generateVariableName("Callback");
        String timerName = EnhancedRandomUtils.generateVariableName("Timer");

        sb.append("package ").append(packageName).append(".timer;\n");
        sb.append("\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.os.CountDownTimer;\n");
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
        sb.append("public class ").append(className).append(" extends CountDownTimer {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("\n");

        // 成员变量
        sb.append("    private final Context ").append(contextName).append(";\n");
        sb.append("    private final ").append(callbackName).append(" ").append(callbackVarName).append(";\n");
        sb.append("    private CountDownTimer ").append(timerName).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    public ").append(className).append("(Context ").append(contextName).append(", long millisInFuture, long countDownInterval, ").append(callbackName).append(" ").append(callbackVarName).append(") {\n");
        sb.append("        super(millisInFuture, countDownInterval);\n");
        sb.append("        this.").append(contextName).append(" = ").append(contextName).append(".getApplicationContext();\n");
        sb.append("        this.").append(callbackVarName).append(" = ").append(callbackVarName).append(";\n");
        sb.append("        this.").append(timerName).append(" = this;\n");
        sb.append("    }\n");
        sb.append("\n");

        // onTick方法
        sb.append("    @Override\n");
        sb.append("    public void onTick(long millisUntilFinished) {\n");
        sb.append("        Log.d(").append(tagName).append(", \"Tick: \" + millisUntilFinished + \" ms\");\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onTick(millisUntilFinished);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");

        // onFinish方法
        sb.append("    @Override\n");
        sb.append("    public void onFinish() {\n");
        sb.append("        Log.d(").append(tagName).append(", \"Timer finished\");\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onFinish();\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("timer");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }
}
