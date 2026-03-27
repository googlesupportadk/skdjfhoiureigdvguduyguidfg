package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.*;

/**
 * 升级版时间处理代码生成器 - 支持随机功能组合和多样性生成
 */
public class LocalTimeGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    // 时间操作类型
    private static final String[] OPERATION_TYPES = {
            "add_hours", "add_minutes", "add_seconds", "add_millis", "add_nanos",
            "subtract_hours", "subtract_minutes", "subtract_seconds", "subtract_millis", "subtract_nanos",
            "plus_hours", "plus_minutes", "plus_seconds", "plus_millis", "plus_nanos",
            "minus_hours", "minus_minutes", "minus_seconds", "minus_millis", "minus_nanos"
    };

    // 时间计算类型
    private static final String[] CALCULATION_TYPES = {
            "hours_between", "minutes_between", "seconds_between", "is_before", "is_after",
            "is_same_time", "format_duration", "parse_duration", "to_timestamp", "from_timestamp",
            "to_epoch_millis", "from_epoch_millis", "to_epoch_seconds", "from_epoch_seconds",
            "to_iso_format", "from_iso_format", "to_custom_format", "from_custom_format"
    };

    // 时间格式类型
    private static final String[] FORMAT_TYPES = {
            "iso", "simple", "custom", "locale", "pattern",
            "short", "medium", "long", "full", "rfc",
            "date_time", "date_only", "time_only", "compact", "verbose",
            "debug", "trace", "info", "warn", "error"
    };

    // 数据类型
    private static final String[] DATA_TYPES = {
            "int", "long", "boolean", "String", "Date",
            "Calendar", "List<Date>", "Map<String, Date>", "long[]"
    };

    // 返回类型
    private static final String[] RETURN_TYPES = {
            "int", "long", "boolean", "String", "Date",
            "Calendar", "List<Date>", "Map<String, Date>", "long[]"
    };

    // 时区类型
    private static final String[] TIMEZONE_TYPES = {
            "UTC", "GMT", "America/New_York", "Europe/London", "Asia/Shanghai",
            "Asia/Tokyo", "Australia/Sydney", "Europe/Paris", "America/Los_Angeles", "Asia/Seoul"
    };

    public LocalTimeGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地时间相关代码 - 升级版");

        // 随机生成5-15个时间处理类
        int classCount = RandomUtils.between(5, 15);
        for (int i = 0; i < classCount; i++) {
            String className = RandomUtils.generateClassName("Time");
            String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
            generateTimeClass(className, operationType);
        }
    }

    /**
     * 生成时间处理类
     */
    private void generateTimeClass(String className, String operationType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 包声明
        sb.append(generatePackageDeclaration("time"));

        // 导入语句
        sb.append(generateImportStatement("java.util.Date"));
        sb.append(generateImportStatement("java.util.Calendar"));
        sb.append(generateImportStatement("java.util.TimeZone"));
        sb.append(generateImportStatement("java.text.SimpleDateFormat"));
        sb.append(generateImportStatement("java.text.ParseException"));
        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成类成员变量
        List<String> fieldNames = generateFields(sb, operationType);

        // 生成构造方法
        generateConstructor(sb, className, fieldNames);

        // 生成核心方法
        List<String> methodNames = new ArrayList<>();

        // 根据操作类型生成不同的方法组合
        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateAddTimeMethod(sb, fieldNames, operationType));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateSubtractTimeMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateFormatTimeMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateParseTimeMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateCompareTimeMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateCalculateDurationMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateConvertTimezoneMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateValidateTimeMethod(sb, fieldNames));
        }

        // 生成调用方法 - 确保所有方法都被调用
        if (methodNames.size() > 0) {
            generateCallerMethod(sb, className, fieldNames, methodNames);
        }

        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "time");
    }

    /**
     * 生成类成员变量
     */
    private List<String> generateFields(StringBuilder sb, String operationType) {
        List<String> fieldNames = new ArrayList<>();

        // 生成随机数量的字段
        int fieldCount = RandomUtils.between(3, 8);

        // 常量字段
        String tagName = RandomUtils.generateWord(6);
        sb.append("    private static final String TAG = \"" + tagName + "\";\n");
        fieldNames.add("TAG");

        String operationTypeField = RandomUtils.generateWord(6);
        sb.append("    private static final String OPERATION_TYPE = \"" + operationType + "\";\n");
        fieldNames.add("OPERATION_TYPE");

        // 随机生成其他字段
        for (int i = 0; i < fieldCount; i++) {
            String fieldType = RandomUtils.randomChoice(DATA_TYPES);
            String fieldName = RandomUtils.generateVariableName(fieldType);

            sb.append("    private ").append(fieldType).append(" ").append(fieldName);

            // 随机初始化
            if (RandomUtils.randomBoolean()) {
                if (fieldType.equals("String")) {
                    sb.append(" = \"" + RandomUtils.generateRandomString(8) + "\"");
                } else if (fieldType.equals("int")) {
                    sb.append(" = ").append(RandomUtils.between(0, 100));
                } else if (fieldType.equals("long")) {
                    sb.append(" = ").append(RandomUtils.betweenLong(0, 1000));
                } else if (fieldType.equals("boolean")) {
                    sb.append(" = ").append(RandomUtils.randomBoolean());
                } else if (fieldType.equals("Date")) {
                    sb.append(" = new Date()");
                } else if (fieldType.equals("Calendar")) {
                    sb.append(" = Calendar.getInstance()");
                } else if (fieldType.equals("List<Date>")) {
                    sb.append(" = new ArrayList<>()");
                } else if (fieldType.equals("Map<String, Date>")) {
                    sb.append(" = new HashMap<>()");
                } else if (fieldType.equals("long[]")) {
                    sb.append(" = new long[]{").append(RandomUtils.betweenLong(0, 1000)).append(", ").append(RandomUtils.betweenLong(0, 1000)).append("}");
                }
            }

            sb.append(";\n");
            fieldNames.add(fieldName);
        }

        sb.append("\n");
        return fieldNames;
    }

    /**
     * 生成构造方法
     */
    private void generateConstructor(StringBuilder sb, String className, List<String> fieldNames) {
        sb.append("    public ").append(className).append("() {\n");

        // 随机选择一些字段进行初始化
        int initCount = RandomUtils.between(1, fieldNames.size());
        List<String> shuffledFields = new ArrayList<>(fieldNames);
        Collections.shuffle(shuffledFields);

        for (int i = 0; i < initCount; i++) {
            String fieldName = shuffledFields.get(i);

            // 跳过常量字段
            if (fieldName.equals("TAG") || fieldName.equals("OPERATION_TYPE")) {
                continue;
            }

            // 随机生成初始化代码
            int initType = RandomUtils.between(1, 5);
            switch (initType) {
                case 1:
                    sb.append("        this.").append(fieldName).append(" = ").append(RandomUtils.between(0, 100)).append(";\n");
                    break;
                case 2:
                    sb.append("        this.").append(fieldName).append(" = ").append(RandomUtils.betweenLong(0, 1000)).append(";\n");
                    break;
                case 3:
                    sb.append("        this.").append(fieldName).append(" = new Date();\n");
                    break;
                case 4:
                    sb.append("        this.").append(fieldName).append(" = Calendar.getInstance();\n");
                    break;
                case 5:
                    sb.append("        this.").append(fieldName).append(" = new ArrayList<>();\n");
                    break;
            }
        }

        // 随机添加日志
        if (RandomUtils.randomBoolean() && RandomUtils.randomBoolean()) {
            sb.append("        System.out.println(TAG + \" initialized\");\n");
        }

        sb.append("    }\n\n");
    }

    /**
     * 生成添加时间方法
     */
    private String generateAddTimeMethod(StringBuilder sb, List<String> fieldNames, String operationType) {
        // 从OPERATION_TYPES中随机选择
        String methodName = RandomUtils.generateMethodName(operationType);
        String dateParam = RandomUtils.generateVariableName("Date");
        String amountParam = RandomUtils.generateVariableName("int");
        String resultVar = RandomUtils.generateVariableName("Date");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Date ").append(dateParam).append(", int ").append(amountParam).append(") {\n");
        sb.append("        Date ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(dateParam).append(" != null) {\n");
        sb.append("            Calendar calendar = Calendar.getInstance();\n");
        sb.append("            calendar.setTime(").append(dateParam).append(");\n");
        sb.append("            calendar.add(Calendar.HOUR, ").append(amountParam).append(");\n");
        sb.append("            ").append(resultVar).append(" = calendar.getTime();\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            Calendar fieldCalendar = Calendar.getInstance();\n");
                sb.append("            fieldCalendar.setTime(").append(usedField).append(");\n");
                sb.append("            fieldCalendar.add(Calendar.HOUR, ").append(amountParam).append(");\n");
                sb.append("            ").append(usedField).append(" = fieldCalendar.getTime();\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("Calendar")) {
            sb.append("        Calendar resultCalendar = Calendar.getInstance();\n");
            sb.append("        resultCalendar.setTime(").append(resultVar).append(");\n");
            sb.append("        return resultCalendar;\n");
        } else if (returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".getTime() : -1;\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("String")) {
            sb.append("        SimpleDateFormat format = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
            sb.append("        return ").append(resultVar).append(" != null ? format.format(").append(resultVar).append(") : null;\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(").append(resultVar).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, Date>")) {
            sb.append("        Map<String, Date> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        if (").append(resultVar).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("long[]")) {
            sb.append("        long[] ").append(RandomUtils.generateVariableName("array")).append(" = new long[1];\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[0] = ").append(resultVar).append(" != null ? ").append(resultVar).append(".getTime() : -1;\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成减去时间方法
     */
    private String generateSubtractTimeMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String dateParam = RandomUtils.generateVariableName("Date");
        String amountParam = RandomUtils.generateVariableName("int");
        String resultVar = RandomUtils.generateVariableName("Date");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Date ").append(dateParam).append(", int ").append(amountParam).append(") {\n");
        sb.append("        Date ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(dateParam).append(" != null) {\n");
        sb.append("            Calendar calendar = Calendar.getInstance();\n");
        sb.append("            calendar.setTime(").append(dateParam).append(");\n");
        sb.append("            calendar.add(Calendar.HOUR, -").append(amountParam).append(");\n");
        sb.append("            ").append(resultVar).append(" = calendar.getTime();\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            Calendar fieldCalendar = Calendar.getInstance();\n");
                sb.append("            fieldCalendar.setTime(").append(usedField).append(");\n");
                sb.append("            fieldCalendar.add(Calendar.HOUR, -").append(amountParam).append(");\n");
                sb.append("            ").append(usedField).append(" = fieldCalendar.getTime();\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("Calendar")) {
            sb.append("        Calendar resultCalendar = Calendar.getInstance();\n");
            sb.append("        resultCalendar.setTime(").append(resultVar).append(");\n");
            sb.append("        return resultCalendar;\n");
        } else if (returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".getTime() : -1;\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("String")) {
            sb.append("        SimpleDateFormat format = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
            sb.append("        return ").append(resultVar).append(" != null ? format.format(").append(resultVar).append(") : null;\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(").append(resultVar).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, Date>")) {
            sb.append("        Map<String, Date> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        if (").append(resultVar).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("long[]")) {
            sb.append("        long[] ").append(RandomUtils.generateVariableName("array")).append(" = new long[1];\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[0] = ").append(resultVar).append(" != null ? ").append(resultVar).append(".getTime() : -1;\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成格式化时间方法
     */
    private String generateFormatTimeMethod(StringBuilder sb, List<String> fieldNames) {
        // 从FORMAT_TYPES中随机选择
        String formatType = RandomUtils.randomChoice(FORMAT_TYPES);
        String methodName = RandomUtils.generateMethodName(formatType);
        String dateParam = RandomUtils.generateVariableName("Date");
        String resultVar = RandomUtils.generateVariableName("String");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Date ").append(dateParam).append(") {\n");
        sb.append("        String ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(dateParam).append(" != null) {\n");
        sb.append("            SimpleDateFormat format = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
        sb.append("            ").append(resultVar).append(" = format.format(").append(dateParam).append(");\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            SimpleDateFormat fieldFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
                sb.append("            String formatted = fieldFormat.format(").append(usedField).append(");\n");
                sb.append("            if (formatted != null) {\n");
                sb.append("                ").append(resultVar).append(" = formatted;\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null && !").append(resultVar).append(".isEmpty();\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".length() : 0;\n");
        } else if (returnType.equals("Date")) {
            sb.append("        Date resultDate = null;\n");
            sb.append("        if (").append(resultVar).append(" != null) {\n");
            sb.append("            SimpleDateFormat format = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
            sb.append("            try {\n");
            sb.append("                resultDate = format.parse(").append(resultVar).append(");\n");
            sb.append("            } catch (ParseException e) {\n");
            sb.append("                resultDate = null;\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        return resultDate;\n");
        } else if (returnType.equals("Calendar")) {
            sb.append("        Calendar resultCalendar = Calendar.getInstance();\n");
            sb.append("        if (").append(resultVar).append(" != null) {\n");
            sb.append("            SimpleDateFormat format = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
            sb.append("            try {\n");
            sb.append("                resultCalendar.setTime(format.parse(").append(resultVar).append("));\n");
            sb.append("            } catch (ParseException e) {\n");
            sb.append("                resultCalendar.setTime(new Date());\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        return resultCalendar;\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(").append(resultVar).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, Date>")) {
            sb.append("        Map<String, Date> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        if (").append(resultVar).append(" != null) {\n");
            sb.append("            SimpleDateFormat format = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
            sb.append("            try {\n");
            sb.append("                ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", format.parse(").append(resultVar).append("));\n");
            sb.append("            } catch (ParseException e) {\n");
            sb.append("                ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", new Date());\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("long[]")) {
            sb.append("        long[] ").append(RandomUtils.generateVariableName("array")).append(" = new long[1];\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[0] = ").append(resultVar).append(" != null ? ").append(resultVar).append(".length() : 0;\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成解析时间方法
     */
    private String generateParseTimeMethod(StringBuilder sb, List<String> fieldNames) {
        // 从FORMAT_TYPES中随机选择
        String formatType = RandomUtils.randomChoice(FORMAT_TYPES);
        String methodName = RandomUtils.generateMethodName(formatType);
        String timeParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("Date");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(timeParam).append(") {\n");
        sb.append("        Date ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(timeParam).append(" != null && !").append(timeParam).append(".isEmpty()) {\n");
        sb.append("            SimpleDateFormat format = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
        sb.append("            try {\n");
        sb.append("                ").append(resultVar).append(" = format.parse(").append(timeParam).append(");\n");
        sb.append("            } catch (ParseException e) {\n");
        sb.append("                ").append(resultVar).append(" = null;\n");
        sb.append("            }\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(resultVar).append(" != null && ").append(usedField).append(" != null) {\n");
                sb.append("            Calendar fieldCalendar = Calendar.getInstance();\n");
                sb.append("            fieldCalendar.setTime(").append(usedField).append(");\n");
                sb.append("            Calendar resultCalendar = Calendar.getInstance();\n");
                sb.append("            resultCalendar.setTime(").append(resultVar).append(");\n");
                sb.append("            long diff = Math.abs(fieldCalendar.getTimeInMillis() - resultCalendar.getTimeInMillis());\n");
                sb.append("            if (diff < 3600000) {\n");
                sb.append("                ").append(usedField).append(" = ").append(resultVar).append(";\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("Calendar")) {
            sb.append("        Calendar resultCalendar = Calendar.getInstance();\n");
            sb.append("        resultCalendar.setTime(").append(resultVar).append(");\n");
            sb.append("        return resultCalendar;\n");
        } else if (returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".getTime() : -1;\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("String")) {
            sb.append("        SimpleDateFormat format = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
            sb.append("        return ").append(resultVar).append(" != null ? format.format(").append(resultVar).append(") : null;\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(").append(resultVar).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, Date>")) {
            sb.append("        Map<String, Date> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        if (").append(resultVar).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("long[]")) {
            sb.append("        long[] ").append(RandomUtils.generateVariableName("array")).append(" = new long[1];\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[0] = ").append(resultVar).append(" != null ? ").append(resultVar).append(".getTime() : -1;\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成比较时间方法
     */
    private String generateCompareTimeMethod(StringBuilder sb, List<String> fieldNames) {
        // 从CALCULATION_TYPES中随机选择
        String calcType = RandomUtils.randomChoice(CALCULATION_TYPES);
        String methodName = RandomUtils.generateMethodName(calcType);
        String dateParam1 = RandomUtils.generateVariableName("Date");
        String dateParam2 = RandomUtils.generateVariableName("Date");
        String resultVar = RandomUtils.generateVariableName("boolean");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Date ").append(dateParam1).append(", Date ").append(dateParam2).append(") {\n");
        sb.append("        boolean ").append(resultVar).append(" = false;\n");
        sb.append("        if (").append(dateParam1).append(" != null && ").append(dateParam2).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(dateParam1).append(".before(").append(dateParam2).append(");\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            Calendar fieldCalendar = Calendar.getInstance();\n");
                sb.append("            fieldCalendar.setTime(").append(usedField).append(");\n");
                sb.append("            long fieldTime = fieldCalendar.getTimeInMillis();\n");
                sb.append("            Calendar param1Calendar = Calendar.getInstance();\n");
                sb.append("            param1Calendar.setTime(").append(dateParam1).append(");\n");
                sb.append("            long param1Time = param1Calendar.getTimeInMillis();\n");
                sb.append("            if (Math.abs(fieldTime - param1Time) < 3600000) {\n");
                sb.append("                ").append(usedField).append(" = ").append(dateParam2).append(";\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" ? \"before\" : \"after\";\n");
        } else if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(" ? ").append(dateParam1).append(" : ").append(dateParam2).append(";\n");
        } else if (returnType.equals("Calendar")) {
            sb.append("        Calendar resultCalendar = Calendar.getInstance();\n");
            sb.append("        resultCalendar.setTime(").append(resultVar).append(" ? ").append(dateParam1).append(" : ").append(dateParam2).append(");\n");
            sb.append("        return resultCalendar;\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(dateParam1).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(").append(dateParam1).append(");\n");
            sb.append("        }\n");
            sb.append("        if (").append(dateParam2).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(").append(dateParam2).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, Date>")) {
            sb.append("        Map<String, Date> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        if (").append(dateParam1).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"date1\", ").append(dateParam1).append(");\n");
            sb.append("        }\n");
            sb.append("        if (").append(dateParam2).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"date2\", ").append(dateParam2).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("long[]")) {
            sb.append("        long[] ").append(RandomUtils.generateVariableName("array")).append(" = new long[2];\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[0] = ").append(dateParam1).append(" != null ? ").append(dateParam1).append(".getTime() : -1;\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[1] = ").append(dateParam2).append(" != null ? ").append(dateParam2).append(".getTime() : -1;\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成计算时间差方法
     */
    private String generateCalculateDurationMethod(StringBuilder sb, List<String> fieldNames) {
        // 从CALCULATION_TYPES中随机选择
        String calcType = RandomUtils.randomChoice(CALCULATION_TYPES);
        String methodName = RandomUtils.generateMethodName(calcType);
        String dateParam1 = RandomUtils.generateVariableName("Date");
        String dateParam2 = RandomUtils.generateVariableName("Date");
        String resultVar = RandomUtils.generateVariableName("long");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Date ").append(dateParam1).append(", Date ").append(dateParam2).append(") {\n");
        sb.append("        long ").append(resultVar).append(" = 0;\n");
        sb.append("        if (").append(dateParam1).append(" != null && ").append(dateParam2).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = Math.abs(").append(dateParam1).append(".getTime() - ").append(dateParam2).append(".getTime());\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            Calendar fieldCalendar = Calendar.getInstance();\n");
                sb.append("            fieldCalendar.setTime(").append(usedField).append(");\n");
                sb.append("            long fieldTime = fieldCalendar.getTimeInMillis();\n");
                sb.append("            Calendar param1Calendar = Calendar.getInstance();\n");
                sb.append("            param1Calendar.setTime(").append(dateParam1).append(");\n");
                sb.append("            long param1Time = param1Calendar.getTimeInMillis();\n");
                sb.append("            long duration = Math.abs(fieldTime - param1Time);\n");
                sb.append("            if (duration < ").append(resultVar).append(") {\n");
                sb.append("                ").append(usedField).append(" = ").append(dateParam2).append(";\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int")) {
            sb.append("        return (int) (").append(resultVar).append(" / 1000);\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" > 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return String.valueOf(").append(resultVar).append(");\n");
        } else if (returnType.equals("Date")) {
            sb.append("        return new Date(").append(resultVar).append(");\n");
        } else if (returnType.equals("Calendar")) {
            sb.append("        Calendar resultCalendar = Calendar.getInstance();\n");
            sb.append("        resultCalendar.setTime(new Date(").append(resultVar).append("));\n");
            sb.append("        return resultCalendar;\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(dateParam1).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(").append(dateParam1).append(");\n");
            sb.append("        }\n");
            sb.append("        if (").append(dateParam2).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(").append(dateParam2).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, Date>")) {
            sb.append("        Map<String, Date> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        if (").append(dateParam1).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"date1\", ").append(dateParam1).append(");\n");
            sb.append("        }\n");
            sb.append("        if (").append(dateParam2).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"date2\", ").append(dateParam2).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("long[]")) {
            sb.append("        long[] ").append(RandomUtils.generateVariableName("array")).append(" = new long[2];\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[0] = ").append(resultVar).append(";\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[1] = ").append(resultVar).append(" / 1000;\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成时区转换方法
     */
    private String generateConvertTimezoneMethod(StringBuilder sb, List<String> fieldNames) {
        // 从TIMEZONE_TYPES中随机选择
        String timezoneType = RandomUtils.randomChoice(TIMEZONE_TYPES);
        String methodName = RandomUtils.generateMethodName(timezoneType);
        String dateParam = RandomUtils.generateVariableName("Date");
        String resultVar = RandomUtils.generateVariableName("Date");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Date ").append(dateParam).append(") {\n");
        sb.append("        Date ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(dateParam).append(" != null) {\n");
        sb.append("            Calendar calendar = Calendar.getInstance();\n");
        sb.append("            calendar.setTime(").append(dateParam).append(");\n");
        sb.append("            calendar.add(Calendar.HOUR, ").append(RandomUtils.between(1, 12)).append(");\n");
        sb.append("            ").append(resultVar).append(" = calendar.getTime();\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            Calendar fieldCalendar = Calendar.getInstance();\n");
                sb.append("            fieldCalendar.setTime(").append(usedField).append(");\n");
                sb.append("            fieldCalendar.add(Calendar.HOUR, ").append(RandomUtils.between(1, 12)).append(");\n");
                sb.append("            ").append(usedField).append(" = fieldCalendar.getTime();\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("Calendar")) {
            sb.append("        Calendar resultCalendar = Calendar.getInstance();\n");
            sb.append("        resultCalendar.setTime(").append(resultVar).append(");\n");
            sb.append("        return resultCalendar;\n");
        } else if (returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".getTime() : -1;\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("String")) {
            sb.append("        SimpleDateFormat format = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
            sb.append("        return ").append(resultVar).append(" != null ? format.format(").append(resultVar).append(") : null;\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(").append(resultVar).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, Date>")) {
            sb.append("        Map<String, Date> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        if (").append(resultVar).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("long[]")) {
            sb.append("        long[] ").append(RandomUtils.generateVariableName("array")).append(" = new long[1];\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[0] = ").append(resultVar).append(" != null ? ").append(resultVar).append(".getTime() : -1;\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成验证时间方法
     */
    private String generateValidateTimeMethod(StringBuilder sb, List<String> fieldNames) {
        // 从CALCULATION_TYPES中随机选择
        String calcType = RandomUtils.randomChoice(CALCULATION_TYPES);
        String methodName = RandomUtils.generateMethodName(calcType);
        String dateParam = RandomUtils.generateVariableName("Date");
        String resultVar = RandomUtils.generateVariableName("boolean");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Date ").append(dateParam).append(") {\n");
        sb.append("        boolean ").append(resultVar).append(" = false;\n");
        sb.append("        if (").append(dateParam).append(" != null) {\n");
        sb.append("            Calendar calendar = Calendar.getInstance();\n");
        sb.append("            calendar.setTime(").append(dateParam).append(");\n");
        sb.append("            ").append(resultVar).append(" = calendar.getTimeInMillis() > 0;\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            Calendar fieldCalendar = Calendar.getInstance();\n");
                sb.append("            fieldCalendar.setTime(").append(usedField).append(");\n");
                sb.append("            boolean fieldValid = fieldCalendar.getTimeInMillis() > 0;\n");
                sb.append("            if (fieldValid && ").append(resultVar).append(") {\n");
                sb.append("                Calendar paramCalendar = Calendar.getInstance();\n");
                sb.append("                paramCalendar.setTime(").append(dateParam).append(");\n");
                sb.append("                long diff = Math.abs(fieldCalendar.getTimeInMillis() - paramCalendar.getTimeInMillis());\n");
                sb.append("                if (diff < 86400000) {\n");
                sb.append("                    ").append(usedField).append(" = ").append(dateParam).append(";\n");
                sb.append("                }\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" ? \"valid\" : \"invalid\";\n");
        } else if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(" ? ").append(dateParam).append(" : null;\n");
        } else if (returnType.equals("Calendar")) {
            sb.append("        Calendar resultCalendar = Calendar.getInstance();\n");
            sb.append("        resultCalendar.setTime(").append(resultVar).append(" ? ").append(dateParam).append(" : new Date());\n");
            sb.append("        return resultCalendar;\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(" && ").append(dateParam).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(").append(dateParam).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, Date>")) {
            sb.append("        Map<String, Date> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        if (").append(resultVar).append(" && ").append(dateParam).append(" != null) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", ").append(dateParam).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("long[]")) {
            sb.append("        long[] ").append(RandomUtils.generateVariableName("array")).append(" = new long[1];\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[0] = ").append(resultVar).append(" ? 1 : 0;\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成调用方法 - 确保所有方法都被调用
     */
    private void generateCallerMethod(StringBuilder sb, String className, List<String> fieldNames, List<String> methodNames) {
        String methodName = RandomUtils.generateMethodName("process");
        String dateVar1 = RandomUtils.generateVariableName("Date");
        String dateVar2 = RandomUtils.generateVariableName("Date");
        String resultVar = RandomUtils.generateVariableName("boolean");

        sb.append("    public boolean ").append(methodName).append("(Date ").append(dateVar1).append(", Date ").append(dateVar2).append(") {\n");
        sb.append("        boolean ").append(resultVar).append(" = false;\n");
        sb.append("        if (").append(dateVar1).append(" != null && ").append(dateVar2).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = true;\n");
        sb.append("        }\n");

        // 调用所有方法
        for (String name : methodNames) {
            if (name.contains("add") || name.contains("plus")) {
                sb.append("        ").append(name).append("(").append(dateVar1).append(", ").append(RandomUtils.between(1, 12)).append(");\n");
            } else if (name.contains("subtract") || name.contains("minus")) {
                sb.append("        ").append(name).append("(").append(dateVar2).append(", ").append(RandomUtils.between(1, 12)).append(");\n");
            } else if (name.contains("format")) {
                sb.append("        ").append(name).append("(").append(dateVar1).append(");\n");
            } else if (name.contains("parse")) {
                sb.append("        String timeStr = \"2023-01-01 12:00:00\";\n");
                sb.append("        ").append(name).append("(timeStr);\n");
            } else if (name.contains("compare") || name.contains("duration")) {
                sb.append("        ").append(name).append("(").append(dateVar1).append(", ").append(dateVar2).append(");\n");
            } else if (name.contains("timezone") || name.contains("convert")) {
                sb.append("        ").append(name).append("(").append(dateVar1).append(");\n");
            } else if (name.contains("validate")) {
                sb.append("        ").append(name).append("(").append(dateVar1).append(");\n");
            }
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            Calendar fieldCalendar = Calendar.getInstance();\n");
                sb.append("            fieldCalendar.setTime(").append(usedField).append(");\n");
                sb.append("            long fieldTime = fieldCalendar.getTimeInMillis();\n");
                sb.append("            Calendar param1Calendar = Calendar.getInstance();\n");
                sb.append("            param1Calendar.setTime(").append(dateVar1).append(");\n");
                sb.append("            long param1Time = param1Calendar.getTimeInMillis();\n");
                sb.append("            if (Math.abs(fieldTime - param1Time) < 3600000) {\n");
                sb.append("                ").append(usedField).append(" = ").append(dateVar2).append(";\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 随机添加日志
        if (RandomUtils.randomBoolean() && RandomUtils.randomBoolean()) {
            sb.append("        System.out.println(TAG + \" processed\");\n");
        }

        sb.append("        return ").append(resultVar).append(";\n");
        sb.append("    }\n\n");
    }
}
