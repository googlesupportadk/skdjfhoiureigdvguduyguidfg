package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import android.os.Handler;
import android.os.Looper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class LocalDateGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    public LocalDateGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成日期类");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Date");
            generateDateClass(className);
        }
    }

    private void generateDateClass(String className) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("date"));

        sb.append(generateImportStatement("android.os.Handler"));
        sb.append(generateImportStatement("android.os.Looper"));
        sb.append(generateImportStatement("java.text.SimpleDateFormat"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.Calendar"));
        sb.append(generateImportStatement("java.util.Date"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.TimeZone"));

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 生成随机的日期格式和时区
        String dateFormat = RandomUtils.generateRandomString(8);
        String timezoneName = RandomUtils.generateRandomString(8);

        sb.append("public class ").append(className).append(" {\n\n");

        // 生成所有随机常量名和值
        String constDateFormat = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constTimezone = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultString = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultInt = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultLong = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();

        // 生成随机的默认值
        String defaultStringValue = RandomUtils.generateRandomString(12);
        int defaultIntValue = RandomUtils.between(0, 100);
        long defaultLongValue = RandomUtils.betweenLong(0L, 1000L);

        // 添加常量
        sb.append("    private static final String ").append(constDateFormat).append(" = \"" + "" + dateFormat + "\";\n");
        sb.append("    private static final String ").append(constTimezone).append(" = \"" + "" + timezoneName + "\";\n");
        sb.append("    private static final String ").append(constDefaultString).append(" = \"" + "" + defaultStringValue + "\";\n");
        sb.append("    private static final int ").append(constDefaultInt).append(" = ").append(defaultIntValue).append(";\n");
        sb.append("    private static final long ").append(constDefaultLong).append(" = ").append(defaultLongValue).append(";\n\n");

        // 随机生成多个字段
        int fieldCount = RandomUtils.between(3, 8);
        List<String> fieldNames = new ArrayList<>();
        Map<String, String> fieldToTypeMap = new HashMap<>();
        Map<String, String> fieldToDefaultMap = new HashMap<>();

        for (int i = 0; i < fieldCount; i++) {
            String fieldType = RandomUtils.randomBoolean() ? "String" : "long";
            String fieldName = RandomUtils.generateVariableName(fieldType.toLowerCase());

            fieldNames.add(fieldName);
            fieldToTypeMap.put(fieldName, fieldType);

            // 为每个字段生成随机默认值
            String defaultValue;
            if (fieldType.equals("String")) {
                defaultValue = RandomUtils.generateRandomString(10);
            } else {
                defaultValue = String.valueOf(RandomUtils.betweenLong(0L, 1000L));
            }
            fieldToDefaultMap.put(fieldName, defaultValue);

            if (RandomUtils.randomBoolean()) {
                sb.append("    private static final ").append(fieldType).append(" ").append(fieldName);
                sb.append(" = ").append(defaultValue).append(";\n");
            } else {
                sb.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n");
            }
        }

        // 生成随机的字段名
        String fieldFormatter = RandomUtils.generateVariableName("formatter");
        String fieldTimezone = RandomUtils.generateVariableName("timezone");
        String fieldCalendar = RandomUtils.generateVariableName("calendar");
        String fieldHandler = RandomUtils.generateVariableName("handler");
        String fieldDateHistory = RandomUtils.generateVariableName("datehistory");
        String fieldListeners = RandomUtils.generateVariableName("listeners");
        String fieldCache = RandomUtils.generateVariableName("cache");
        String fieldLastUpdate = RandomUtils.generateVariableName("lastupdate");
        String fieldFormatCache = RandomUtils.generateVariableName("formatcache");

        // 添加字段
        sb.append("    private SimpleDateFormat ").append(fieldFormatter).append(";\n");
        sb.append("    private TimeZone ").append(fieldTimezone).append(";\n");
        sb.append("    private Calendar ").append(fieldCalendar).append(";\n");
        sb.append("    private Handler ").append(fieldHandler).append(";\n");
        sb.append("    private List<Long> ").append(fieldDateHistory).append(";\n");
        sb.append("    private List<Runnable> ").append(fieldListeners).append(";\n");
        sb.append("    private Map<String, Long> ").append(fieldCache).append(";\n");
        sb.append("    private long ").append(fieldLastUpdate).append(";\n");
        sb.append("    private Map<String, String> ").append(fieldFormatCache).append(";\n\n");

        // 构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this.").append(fieldFormatter).append(" = new SimpleDateFormat(").append(constDateFormat).append(");\n");
        sb.append("        this.").append(fieldTimezone).append(" = TimeZone.getTimeZone(").append(constTimezone).append(");\n");
        sb.append("        this.").append(fieldFormatter).append(".setTimeZone(").append(fieldTimezone).append(");\n");
        sb.append("        this.").append(fieldCalendar).append(" = Calendar.getInstance(").append(fieldTimezone).append(");\n");
        sb.append("        this.").append(fieldHandler).append(" = new Handler(Looper.getMainLooper());\n");
        sb.append("        this.").append(fieldDateHistory).append(" = new ArrayList<>();\n");
        sb.append("        this.").append(fieldListeners).append(" = new ArrayList<>();\n");
        sb.append("        this.").append(fieldCache).append(" = new HashMap<>();\n");
        sb.append("        this.").append(fieldFormatCache).append(" = new HashMap<>();\n");
        sb.append("        this.").append(fieldLastUpdate).append(" = System.currentTimeMillis();\n");
        sb.append("    }\n\n");

        // 获取当前日期方法
        String getCurrentMethodName = "get" + RandomUtils.generateRandomString(8);
        String paramFormat = RandomUtils.generateVariableName("format");
        String localDate = RandomUtils.generateVariableName("date");
        String updateCacheMethodName = "update" + RandomUtils.generateRandomString(8);

        sb.append("    public String ").append(getCurrentMethodName).append("(String ").append(paramFormat).append(") {\n");
        sb.append("        long ").append(localDate).append(" = System.currentTimeMillis();\n");
        sb.append("        ").append(fieldCalendar).append(".setTimeInMillis(").append(localDate).append(");\n");
        sb.append("        String ").append(paramFormat).append("Result = ").append(fieldFormatter).append(".format(").append(fieldCalendar).append(".getTime());\n");
        sb.append("        ").append(updateCacheMethodName).append("(").append(localDate).append(", ").append(paramFormat).append("Result);\n");
        sb.append("        return ").append(paramFormat).append("Result;\n");
        sb.append("    }\n\n");

        // 更新缓存方法
        String paramCacheKey = RandomUtils.generateVariableName("key");
        String paramCacheValue = RandomUtils.generateVariableName("value");
        String notifyListenersMethodName = "notify" + RandomUtils.generateRandomString(8);

        sb.append("    private void ").append(updateCacheMethodName).append("(long ").append(paramCacheKey).append(", String ").append(paramCacheValue).append(") {\n");
        sb.append("        ").append(fieldCache).append(".put(String.valueOf(").append(paramCacheKey).append("), ").append(paramCacheKey).append(");\n");
        sb.append("        ").append(fieldFormatCache).append(".put(String.valueOf(").append(paramCacheKey).append("), ").append(paramCacheValue).append(");\n");
        sb.append("        ").append(fieldLastUpdate).append(" = System.currentTimeMillis();\n");
        sb.append("        ").append(notifyListenersMethodName).append("(").append(paramCacheKey).append(", ").append(paramCacheValue).append(");\n");
        sb.append("    }\n\n");

        // 通知监听器方法
        String paramNotifyKey = RandomUtils.generateVariableName("key");
        String paramNotifyValue = RandomUtils.generateVariableName("value");
        String localListener = RandomUtils.generateVariableName("listener");

        sb.append("    private void ").append(notifyListenersMethodName).append("(long ").append(paramNotifyKey).append(", String ").append(paramNotifyValue).append(") {\n");
        sb.append("        for (Runnable ").append(localListener).append(" : ").append(fieldListeners).append(") {\n");
        sb.append("            ").append(fieldHandler).append(".post(").append(localListener).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 格式化日期方法
        String formatMethodName = "format" + RandomUtils.generateRandomString(8);
        String paramTimestamp = RandomUtils.generateVariableName("timestamp");
        String paramFormat2 = RandomUtils.generateVariableName("format");

        sb.append("    public String ").append(formatMethodName).append("(long ").append(paramTimestamp).append(", String ").append(paramFormat2).append(") {\n");
        sb.append("        ").append(fieldCalendar).append(".setTimeInMillis(").append(paramTimestamp).append(");\n");
        sb.append("        String ").append(paramFormat2).append("Result = ").append(fieldFormatter).append(".format(").append(fieldCalendar).append(".getTime());\n");
        sb.append("        return ").append(paramFormat2).append("Result;\n");
        sb.append("    }\n\n");

        // 解析日期方法
        String parseMethodName = "parse" + RandomUtils.generateRandomString(8);
        String paramDate = RandomUtils.generateVariableName("date");
        String localParsedDate = RandomUtils.generateVariableName("parseddate");
        String localTimestamp = RandomUtils.generateVariableName("timestamp");

        sb.append("    public long ").append(parseMethodName).append("(String ").append(paramDate).append(") {\n");
        sb.append("        try {\n");
        sb.append("            Date ").append(localParsedDate).append(" = ").append(fieldFormatter).append(".parse(").append(paramDate).append(");\n");
        sb.append("            long ").append(localTimestamp).append(" = ").append(localParsedDate).append(".getTime();\n");
        sb.append("            ").append(fieldDateHistory).append(".add(").append(localTimestamp).append(");\n");
        sb.append("            return ").append(localTimestamp).append(";\n");
        sb.append("        } catch (Exception ").append(RandomUtils.generateVariableName("e")).append(") {\n");
        sb.append("            return System.currentTimeMillis();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 添加天数方法
        String addDaysMethodName = "add" + RandomUtils.generateRandomString(8);
        String paramTimestamp2 = RandomUtils.generateVariableName("timestamp");
        String paramDays = RandomUtils.generateVariableName("days");
        String localResult = RandomUtils.generateVariableName("result");

        sb.append("    public long ").append(addDaysMethodName).append("(long ").append(paramTimestamp2).append(", int ").append(paramDays).append(") {\n");
        sb.append("        ").append(fieldCalendar).append(".setTimeInMillis(").append(paramTimestamp2).append(");\n");
        sb.append("        ").append(fieldCalendar).append(".add(Calendar.DAY_OF_MONTH, ").append(paramDays).append(");\n");
        sb.append("        long ").append(localResult).append(" = ").append(fieldCalendar).append(".getTimeInMillis();\n");
        sb.append("        ").append(fieldDateHistory).append(".add(").append(localResult).append(");\n");
        sb.append("        return ").append(localResult).append(";\n");
        sb.append("    }\n\n");

        // 添加小时方法
        String addHoursMethodName = "add" + RandomUtils.generateRandomString(8);
        String paramTimestamp3 = RandomUtils.generateVariableName("timestamp");
        String paramHours = RandomUtils.generateVariableName("hours");

        sb.append("    public long ").append(addHoursMethodName).append("(long ").append(paramTimestamp3).append(", int ").append(paramHours).append(") {\n");
        sb.append("        ").append(fieldCalendar).append(".setTimeInMillis(").append(paramTimestamp3).append(");\n");
        sb.append("        ").append(fieldCalendar).append(".add(Calendar.HOUR_OF_DAY, ").append(paramHours).append(");\n");
        sb.append("        long ").append(localResult).append(" = ").append(fieldCalendar).append(".getTimeInMillis();\n");
        sb.append("        ").append(fieldDateHistory).append(".add(").append(localResult).append(");\n");
        sb.append("        return ").append(localResult).append(";\n");
        sb.append("    }\n\n");

        // 获取星期几方法
        String getDayOfWeekMethodName = "get" + RandomUtils.generateRandomString(8);
        String paramTimestamp4 = RandomUtils.generateVariableName("timestamp");
        String localDayOfWeek = RandomUtils.generateVariableName("dayofweek");

        sb.append("    public int ").append(getDayOfWeekMethodName).append("(long ").append(paramTimestamp4).append(") {\n");
        sb.append("        ").append(fieldCalendar).append(".setTimeInMillis(").append(paramTimestamp4).append(");\n");
        sb.append("        int ").append(localDayOfWeek).append(" = ").append(fieldCalendar).append(".get(Calendar.DAY_OF_WEEK);\n");
        sb.append("        return ").append(localDayOfWeek).append(";\n");
        sb.append("    }\n\n");

        // 获取月份方法
        String getMonthMethodName = "get" + RandomUtils.generateRandomString(8);
        String paramTimestamp5 = RandomUtils.generateVariableName("timestamp");
        String localMonth = RandomUtils.generateVariableName("month");

        sb.append("    public int ").append(getMonthMethodName).append("(long ").append(paramTimestamp5).append(") {\n");
        sb.append("        ").append(fieldCalendar).append(".setTimeInMillis(").append(paramTimestamp5).append(");\n");
        sb.append("        int ").append(localMonth).append(" = ").append(fieldCalendar).append(".get(Calendar.MONTH);\n");
        sb.append("        return ").append(localMonth).append(";\n");
        sb.append("    }\n\n");

        // 获取年份方法
        String getYearMethodName = "get" + RandomUtils.generateRandomString(8);
        String paramTimestamp6 = RandomUtils.generateVariableName("timestamp");
        String localYear = RandomUtils.generateVariableName("year");

        sb.append("    public int ").append(getYearMethodName).append("(long ").append(paramTimestamp6).append(") {\n");
        sb.append("        ").append(fieldCalendar).append(".setTimeInMillis(").append(paramTimestamp6).append(");\n");
        sb.append("        int ").append(localYear).append(" = ").append(fieldCalendar).append(".get(Calendar.YEAR);\n");
        sb.append("        return ").append(localYear).append(";\n");
        sb.append("    }\n\n");

        // 随机生成getter和setter方法
        String getFormatterMethodName = "get" + RandomUtils.generateRandomString(8);
        String setFormatterMethodName = "set" + RandomUtils.generateRandomString(8);
        String getTimezoneMethodName = "get" + RandomUtils.generateRandomString(8);
        String setTimezoneMethodName = "set" + RandomUtils.generateRandomString(8);
        String getDateHistoryMethodName = "get" + RandomUtils.generateRandomString(8);
        String addListenerMethodName = "add" + RandomUtils.generateRandomString(8);
        String removeListenerMethodName = "remove" + RandomUtils.generateRandomString(8);
        String clearHistoryMethodName = "clear" + RandomUtils.generateRandomString(8);
        String clearCacheMethodName = "clear" + RandomUtils.generateRandomString(8);

        // getFormatter
        if (RandomUtils.randomBoolean()) {
            sb.append("    public SimpleDateFormat ").append(getFormatterMethodName).append("() {\n");
            sb.append("        return ").append(fieldFormatter).append(";\n");
            sb.append("    }\n\n");
        }

        // setFormatter
        if (RandomUtils.randomBoolean()) {
            String paramNewFormat = RandomUtils.generateVariableName("format");
            sb.append("    public void ").append(setFormatterMethodName).append("(String ").append(paramNewFormat).append(") {\n");
            sb.append("        this.").append(fieldFormatter).append(" = new SimpleDateFormat(").append(paramNewFormat).append(");\n");
            sb.append("        this.").append(fieldFormatter).append(".setTimeZone(").append(fieldTimezone).append(");\n");
            sb.append("    }\n\n");
        }

        // getTimezone
        if (RandomUtils.randomBoolean()) {
            sb.append("    public TimeZone ").append(getTimezoneMethodName).append("() {\n");
            sb.append("        return ").append(fieldTimezone).append(";\n");
            sb.append("    }\n\n");
        }

        // setTimezone
        if (RandomUtils.randomBoolean()) {
            String paramNewTimezone = RandomUtils.generateVariableName("timezone");
            sb.append("    public void ").append(setTimezoneMethodName).append("(String ").append(paramNewTimezone).append(") {\n");
            sb.append("        this.").append(fieldTimezone).append(" = TimeZone.getTimeZone(").append(paramNewTimezone).append(");\n");
            sb.append("        this.").append(fieldFormatter).append(".setTimeZone(").append(fieldTimezone).append(");\n");
            sb.append("        this.").append(fieldCalendar).append(" = Calendar.getInstance(").append(fieldTimezone).append(");\n");
            sb.append("    }\n\n");
        }

        // getDateHistory
        if (RandomUtils.randomBoolean()) {
            sb.append("    public List<Long> ").append(getDateHistoryMethodName).append("() {\n");
            sb.append("        return new ArrayList<>(").append(fieldDateHistory).append(");\n");
            sb.append("    }\n\n");
        }

        // addListener
        if (RandomUtils.randomBoolean()) {
            String paramListener = RandomUtils.generateVariableName("listener");
            sb.append("    public void ").append(addListenerMethodName).append("(Runnable ").append(paramListener).append(") {\n");
            sb.append("        if (").append(paramListener).append(" != null) {\n");
            sb.append("            ").append(fieldListeners).append(".add(").append(paramListener).append(");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // removeListener
        if (RandomUtils.randomBoolean()) {
            String paramListener2 = RandomUtils.generateVariableName("listener");
            sb.append("    public void ").append(removeListenerMethodName).append("(Runnable ").append(paramListener2).append(") {\n");
            sb.append("        ").append(fieldListeners).append(".remove(").append(paramListener2).append(");\n");
            sb.append("    }\n\n");
        }

        // clearHistory
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void ").append(clearHistoryMethodName).append("() {\n");
            sb.append("        ").append(fieldDateHistory).append(".clear();\n");
            sb.append("    }\n\n");
        }

        // clearCache
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void ").append(clearCacheMethodName).append("() {\n");
            sb.append("        ").append(fieldCache).append(".clear();\n");
            sb.append("        ").append(fieldFormatCache).append(".clear();\n");
            sb.append("        ").append(fieldLastUpdate).append(" = System.currentTimeMillis();\n");
            sb.append("    }\n\n");
        }

        // 生成与计算器关联的方法
        if (RandomUtils.randomBoolean()) {
            String calculateMethodName = "calculate" + RandomUtils.generateRandomString(8);
            String localTotal = RandomUtils.generateVariableName("total");
            String localTimestamps = RandomUtils.generateVariableName("timestamp");
            String calculateValueMethodName = "calculate" + RandomUtils.generateRandomString(8);

            sb.append("    public double ").append(calculateMethodName).append("() {\n");
            sb.append("        double ").append(localTotal).append(" = 0.0;\n");
            sb.append("        for (long ").append(localTimestamps).append(" : ").append(fieldDateHistory).append(") {\n");
            sb.append("            ").append(localTotal).append(" += ").append(calculateValueMethodName).append("(").append(localTimestamp).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(localTotal).append(";\n");
            sb.append("    }\n\n");

            // 生成计算值方法
            String paramCalcTimestamp = RandomUtils.generateVariableName("timestamp");
            sb.append("    private double ").append(calculateValueMethodName).append("(long ").append(paramCalcTimestamp).append(") {\n");
            sb.append("        return (double) ").append(paramCalcTimestamp).append(";\n");
            sb.append("    }\n\n");
        }

        // 生成与图表关联的方法
        if (RandomUtils.randomBoolean()) {
            String getDataMethodName = "get" + RandomUtils.generateRandomString(8);
            String localDataPoints = RandomUtils.generateVariableName("datapoints");

            sb.append("    public double[] ").append(getDataMethodName).append("() {\n");
            sb.append("        double[] ").append(localDataPoints).append(" = new double[3];\n");
            sb.append("        ").append(localDataPoints).append("[0] = ").append(fieldDateHistory).append(".size();\n");
            sb.append("        ").append(localDataPoints).append("[1] = ").append(fieldCache).append(".size();\n");
            sb.append("        ").append(localDataPoints).append("[2] = ").append(fieldFormatCache).append(".size();\n");
            sb.append("        return ").append(localDataPoints).append(";\n");
            sb.append("    }\n\n");
        }

        // 生成与集合关联的方法
        if (RandomUtils.randomBoolean()) {
            String getUniqueMethodName = "get" + RandomUtils.generateRandomString(8);
            String localUnique = RandomUtils.generateVariableName("unique");
            String localTimestamp2 = RandomUtils.generateVariableName("timestamp");

            sb.append("    public List<Long> ").append(getUniqueMethodName).append("() {\n");
            sb.append("        List<Long> ").append(localUnique).append(" = new ArrayList<>();\n");
            sb.append("        for (long ").append(localTimestamp2).append(" : ").append(fieldDateHistory).append(") {\n");
            sb.append("            if (!").append(localUnique).append(".contains(").append(localTimestamp2).append(")) {\n");
            sb.append("                ").append(localUnique).append(".add(").append(localTimestamp2).append(");\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        return ").append(localUnique).append(";\n");
            sb.append("    }\n\n");
        }

        sb.append("}\n\n");

        generateJavaFile(className, sb.toString(), "date");
    }
}