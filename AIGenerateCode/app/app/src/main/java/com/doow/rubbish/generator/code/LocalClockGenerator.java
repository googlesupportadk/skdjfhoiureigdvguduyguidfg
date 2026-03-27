package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.ArrayList;
import java.util.List;

public class LocalClockGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] CLOCK_TYPES = {
            "DigitalClock", "AnalogClock", "StopWatch", "Timer", "AlarmClock",
            "WorldClock", "CountdownTimer", "Chronometer", "PomodoroTimer", "IntervalTimer"
    };

    private static final String[] TIME_FORMATS = {
            "HH:mm:ss", "HH:mm", "mm:ss", "ss", "HH:mm:ss.SSS", "HH:mm:ss a"
    };

    private static final String[] TIME_ZONES = {
            "UTC", "GMT", "EST", "PST", "CST", "MST", "CET", "EET", "JST"
    };

    private static final String[] FIELD_TYPES = {
            "int", "long", "float", "double", "boolean", "String", "Object"
    };

    private static final String[] ALARM_TYPES = {
            "once", "daily", "weekly", "custom"
    };

    private static final String[] TIMER_STATES = {
            "idle", "running", "paused", "stopped", "completed"
    };

    public LocalClockGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地时钟类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Clock");
            generateClockClass(className, asyncHandler);
        }
    }

    private void generateClockClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("clock"));

        sb.append(generateImportStatement("android.os.Handler"));
        sb.append(generateImportStatement("android.os.Looper"));
        sb.append(generateImportStatement("android.os.SystemClock"));
        sb.append(generateImportStatement("android.util.Log"));

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        String clockType = CLOCK_TYPES[RandomUtils.between(0, CLOCK_TYPES.length - 1)];
        String timeFormat = TIME_FORMATS[RandomUtils.between(0, TIME_FORMATS.length - 1)];
        String timeZone = TIME_ZONES[RandomUtils.between(0, TIME_ZONES.length - 1)];
        String alarmType = ALARM_TYPES[RandomUtils.between(0, ALARM_TYPES.length - 1)];

        sb.append("public class ").append(className).append(" {\n\n");

        // 添加常量
        sb.append("    private static final String TAG = \"").append(className).append("\");\n");
        sb.append("    private static final String CLOCK_TYPE = \"").append(clockType).append("\");\n");
        sb.append("    private static final String TIME_FORMAT = \"").append(timeFormat).append("\");\n");
        sb.append("    private static final String TIME_ZONE = \"").append(timeZone).append("\");\n");
        sb.append("    private static final String ALARM_TYPE = \"").append(alarmType).append("\");\n\n");

        // 随机生成多个字段
        int fieldCount = RandomUtils.between(3, 8);
        List<String> fieldNames = new ArrayList<>();
        for (int i = 0; i < fieldCount; i++) {
            String fieldType = FIELD_TYPES[RandomUtils.between(0, FIELD_TYPES.length - 1)];
            String fieldName = RandomUtils.generateVariableName(fieldType);
            fieldNames.add(fieldName);

            if (RandomUtils.randomBoolean()) {
                sb.append("    private static final ").append(fieldType).append(" ").append(fieldName);
                sb.append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            } else {
                sb.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n");
            }
        }

        // 添加时钟相关字段
        sb.append("    private long currentTime;\n");
        sb.append("    private long startTime;\n");
        sb.append("    private long elapsedTime;\n");
        sb.append("    private boolean isRunning;\n");
        sb.append("    private int tickInterval;\n");
        sb.append("    private Handler tickHandler;\n");
        sb.append("    private Runnable tickRunnable;\n");
        sb.append("    private String timerState;\n\n");

        // 构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this.currentTime = System.currentTimeMillis();\n");
        sb.append("        this.startTime = 0;\n");
        sb.append("        this.elapsedTime = 0;\n");
        sb.append("        this.isRunning = false;\n");
        sb.append("        this.tickInterval = RandomUtils.between(100, 1000);\n");
        sb.append("        this.timerState = \"idle\";\n");
        sb.append("        this.tickHandler = new Handler(Looper.getMainLooper());\n");

        for (String fieldName : fieldNames) {
            if (!isStaticField(fieldName)) {
                String fieldType = getFieldType(fieldName);
                sb.append("        this.").append(fieldName).append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            }
        }
        sb.append("        initTickRunnable();\n");
        sb.append("    }\n\n");

        // 生成初始化方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private void initTickRunnable() {\n");
            sb.append("        tickRunnable = new Runnable() {\n");
            sb.append("            @Override\n");
            sb.append("            public void run() {\n");
            sb.append("                if (isRunning) {\n");
            sb.append("                    onTick();\n");
            sb.append("                    tickHandler.postDelayed(this, tickInterval);\n");
            sb.append("                }\n");
            sb.append("            }\n");
            sb.append("        };\n");
            sb.append("    }\n\n");
        }

        // 生成启动方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void start() {\n");
            sb.append("        if (!isRunning) {\n");
            sb.append("            isRunning = true;\n");
            sb.append("            startTime = System.currentTimeMillis();\n");
            sb.append("            timerState = \"running\";\n");
            sb.append("            Log.d(TAG, \"Clock started\");\n");
            sb.append("            tickHandler.post(tickRunnable);\n");
            sb.append("        } else {\n");
            sb.append("            Log.w(TAG, \"Clock already running\");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成停止方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void stop() {\n");
            sb.append("        if (isRunning) {\n");
            sb.append("            isRunning = false;\n");
            sb.append("            elapsedTime = System.currentTimeMillis() - startTime;\n");
            sb.append("            timerState = \"stopped\";\n");
            sb.append("            tickHandler.removeCallbacks(tickRunnable);\n");
            sb.append("            Log.d(TAG, \"Clock stopped. Elapsed: \" + elapsedTime);\n");
            sb.append("        } else {\n");
            sb.append("            Log.w(TAG, \"Clock not running\");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成暂停方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void pause() {\n");
            sb.append("        if (isRunning) {\n");
            sb.append("            isRunning = false;\n");
            sb.append("            timerState = \"paused\";\n");
            sb.append("            tickHandler.removeCallbacks(tickRunnable);\n");
            sb.append("            Log.d(TAG, \"Clock paused\");\n");
            sb.append("        } else {\n");
            sb.append("            Log.w(TAG, \"Clock not running\");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成重置方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void reset() {\n");
            sb.append("        stop();\n");
            sb.append("        startTime = 0;\n");
            sb.append("        elapsedTime = 0;\n");
            sb.append("        timerState = \"idle\";\n");
            sb.append("        Log.d(TAG, \"Clock reset\");\n");
            sb.append("    }\n\n");
        }

        // 生成获取时间方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public long getCurrentTime() {\n");
            sb.append("        if (isRunning) {\n");
            sb.append("            currentTime = System.currentTimeMillis() - startTime;\n");
            sb.append("        }\n");
            sb.append("        Log.d(TAG, \"Current time: \" + currentTime);\n");
            sb.append("        return currentTime;\n");
            sb.append("    }\n\n");
        }

        // 生成获取格式化时间方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public String getFormattedTime() {\n");
            sb.append("        long time = getCurrentTime();\n");
            sb.append("        long hours = time / 3600000;\n");
            sb.append("        long minutes = (time % 3600000) / 60000;\n");
            sb.append("        long seconds = (time % 60000) / 1000;\n");
            sb.append("        String formatted = String.format(\"%02d:%02d:%02d\", hours, minutes, seconds);\n");
            sb.append("        Log.d(TAG, \"Formatted time: \" + formatted);\n");
            sb.append("        return formatted;\n");
            sb.append("    }\n\n");
        }

        // 生成设置间隔方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void setTickInterval(int interval) {\n");
            sb.append("        if (interval > 0) {\n");
            sb.append("            this.tickInterval = interval;\n");
            sb.append("            Log.d(TAG, \"Tick interval set to: \" + interval);\n");
            sb.append("        } else {\n");
            sb.append("            Log.e(TAG, \"Invalid interval: \" + interval);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成获取状态方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public String getState() {\n");
            sb.append("        Log.d(TAG, \"Current state: \" + timerState);\n");
            sb.append("        return timerState;\n");
            sb.append("    }\n\n");
        }

        // 生成Tick回调方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private void onTick() {\n");
            sb.append("        currentTime = System.currentTimeMillis() - startTime;\n");
            sb.append("        Log.d(TAG, \"Tick: \" + currentTime);\n");
            sb.append("        // 可以在这里触发回调或更新UI\n");
            sb.append("    }\n\n");
        }

        // 生成辅助方法
        for (String fieldName : fieldNames) {
            String fieldType = getFieldType(fieldName);

            if (fieldType.equals("int") || fieldType.equals("long") || fieldType.equals("float") || fieldType.equals("double")) {
                sb.append("    private double calculate").append(capitalize(fieldName)).append("() {\n");
                sb.append("        double fieldValue = ").append(fieldName).append(";\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            fieldValue += ").append(fieldName).append(";\n");
                sb.append("        } else {\n");
                sb.append("            fieldValue *= 2;\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Calculated ").append(fieldName).append(": \" + fieldValue);\n");
                sb.append("        return fieldValue;\n");
                sb.append("    }\n\n");
            } else if (fieldType.equals("boolean")) {
                sb.append("    private boolean validate").append(capitalize(fieldName)).append("() {\n");
                sb.append("        boolean isValid = ").append(fieldName).append(";\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            isValid = !isValid;\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Validated ").append(fieldName).append(": \" + isValid);\n");
                sb.append("        return isValid;\n");
                sb.append("    }\n\n");
            } else {
                sb.append("    private void process").append(capitalize(fieldName)).append("() {\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            ").append(fieldName).append(" = \"processed_\" + ").append(fieldName).append(";\n");
                sb.append("        } else {\n");
                sb.append("            ").append(fieldName).append(" = String.valueOf(").append(fieldName).append(".hashCode());\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Processed ").append(fieldName).append(": \" + ").append(fieldName).append(");\n");
                sb.append("    }\n\n");
            }
        }

        // 生成与计算器关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public long calculateTimeBasedValue() {\n");
            sb.append("        long timeValue = getCurrentTime();\n");
            sb.append("        long calculatedValue = timeValue * 1000;\n");
            sb.append("        Log.d(TAG, \"Calculated time-based value: \" + calculatedValue);\n");
            sb.append("        return calculatedValue;\n");
            sb.append("    }\n\n");
        }

        // 生成与图表关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public double[] getTimeDataPoints(int count) {\n");
            sb.append("        if (count <= 0) {\n");
            sb.append("            Log.e(TAG, \"Invalid count: \" + count);\n");
            sb.append("            return new double[0];\n");
            sb.append("        }\n");
            sb.append("        double[] dataPoints = new double[count];\n");
            sb.append("        for (int i = 0; i < count; i++) {\n");
            sb.append("            dataPoints[i] = getCurrentTime() / 1000.0;\n");
            sb.append("        }\n");
            sb.append("        Log.d(TAG, \"Generated \" + count + \" time data points\");\n");
            sb.append("        return dataPoints;\n");
            sb.append("    }\n\n");
        }

        // 生成统计方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void logStats() {\n");
            sb.append("        Log.d(TAG, \"Clock stats - Type: \" + CLOCK_TYPE);\n");
            sb.append("        Log.d(TAG, \"Time format: \" + TIME_FORMAT);\n");
            sb.append("        Log.d(TAG, \"Time zone: \" + TIME_ZONE);\n");
            sb.append("        Log.d(TAG, \"Alarm type: \" + ALARM_TYPE);\n");
            sb.append("        Log.d(TAG, \"Current time: \" + getCurrentTime());\n");
            sb.append("        Log.d(TAG, \"Elapsed time: \" + elapsedTime);\n");
            sb.append("        Log.d(TAG, \"Tick interval: \" + tickInterval);\n");
            sb.append("        Log.d(TAG, \"Timer state: \" + timerState);\n");

            sb.append("        // 随机调用辅助方法\n");
            sb.append("        int methodCallCount = RandomUtils.between(1, 3);\n");
            sb.append("        for (int i = 0; i < methodCallCount; i++) {\n");
            sb.append("            int methodIndex = RandomUtils.between(0, ").append(fieldNames.size()).append(" - 1);\n");
            sb.append("            String fieldName = fieldNames.get(methodIndex);\n");
            sb.append("            String fieldType = getFieldType(fieldName);\n");
            sb.append("            \n");
            sb.append("            if (fieldType.equals(\"int\") || fieldType.equals(\"long\") || fieldType.equals(\"float\") || fieldType.equals(\"double\")) {\n");
            sb.append("                calculate").append(capitalize("fieldName")).append("();\n");
            sb.append("            } else if (fieldType.equals(\"boolean\")) {\n");
            sb.append("                validate").append(capitalize("fieldName")).append("();\n");
            sb.append("            } else {\n");
            sb.append("                process").append(capitalize("fieldName")).append("();\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "clock");
    }

    private String generateInitialValue(String type) {
        switch (type) {
            case "int":
                return "RandomUtils.between(0, 100)";
            case "long":
                return "RandomUtils.between(0L, 1000L)";
            case "float":
                return "(float) RandomUtils.nextDouble(0.0, 100.0)";
            case "double":
                return "RandomUtils.nextDouble(0.0, 100.0)";
            case "boolean":
                return "RandomUtils.randomBoolean()";
            case "String":
                return "RandomUtils.generateName(\"value\")";
            case "Object":
                return "new Object()";
            default:
                return "null";
        }
    }

    private boolean isStaticField(String fieldName) {
        return fieldName.startsWith("static_");
    }

    private String getFieldType(String fieldName) {
        if (fieldName.endsWith("int")) {
            return "int";
        } else if (fieldName.endsWith("long")) {
            return "long";
        } else if (fieldName.endsWith("float")) {
            return "float";
        } else if (fieldName.endsWith("double")) {
            return "double";
        } else if (fieldName.endsWith("boolean")) {
            return "boolean";
        } else if (fieldName.toLowerCase().endsWith("string")) {
            return "String";
        } else {
            return "Object";
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
