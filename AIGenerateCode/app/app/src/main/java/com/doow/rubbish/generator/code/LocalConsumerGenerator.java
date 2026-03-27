package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class LocalConsumerGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] VALUE_TYPES = {
            "String", "Integer", "Boolean", "Long", "Float", "Double", "Object"
    };

    public LocalConsumerGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成消费者类");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Consumer");
            generateConsumerClass(className);
        }
    }

    private void generateConsumerClass(String className) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("consumer"));

        sb.append(generateImportStatement("android.os.Handler"));
        sb.append(generateImportStatement("android.os.Looper"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.HashSet"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.Set"));
        sb.append(generateImportStatement("java.util.function.Consumer"));

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 生成随机的类名后缀和类型标识符
        String consumerType = RandomUtils.generateRandomString(8);
        String dataType = VALUE_TYPES[RandomUtils.between(0, VALUE_TYPES.length - 1)];

        sb.append("public class ").append(className).append(" implements Consumer<").append(dataType).append("> {\n\n");

        // 生成所有随机常量名和值
        String constConsumerType = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDataType = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultString = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultInt = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultBoolean = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultLong = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultFloat = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultDouble = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();

        // 生成随机的默认值字符串
        String defaultStringValue = RandomUtils.generateRandomString(12);
        int defaultIntValue = RandomUtils.between(0, 100);
        boolean defaultBooleanValue = RandomUtils.randomBoolean();
        long defaultLongValue = RandomUtils.betweenLong(0L, 1000L);
        float defaultFloatValue = (float) RandomUtils.nextDouble(0.0, 100.0);
        double defaultDoubleValue = RandomUtils.nextDouble(0.0, 100.0);

        // 添加常量
        sb.append("    private static final String ").append(constConsumerType).append(" = \"" + "" + consumerType + "\";\n");
        sb.append("    private static final String ").append(constDataType).append(" = \"" + "" + dataType + "\";\n");
        sb.append("    private static final String ").append(constDefaultString).append(" = \"" + "" + defaultStringValue + "\";\n");
        sb.append("    private static final int ").append(constDefaultInt).append(" = ").append(defaultIntValue).append(";\n");
        sb.append("    private static final boolean ").append(constDefaultBoolean).append(" = ").append(defaultBooleanValue).append(";\n");
        sb.append("    private static final long ").append(constDefaultLong).append(" = ").append(defaultLongValue).append(";\n");
        sb.append("    private static final float ").append(constDefaultFloat).append(" = ").append(defaultFloatValue).append("f;\n");
        sb.append("    private static final double ").append(constDefaultDouble).append(" = ").append(defaultDoubleValue).append(";\n\n");

        // 随机生成多个字段
        int fieldCount = RandomUtils.between(3, 8);
        List<String> fieldNames = new ArrayList<>();
        Map<String, String> fieldToTypeMap = new HashMap<>();
        Map<String, String> fieldToDefaultMap = new HashMap<>();

        for (int i = 0; i < fieldCount; i++) {
            String fieldType = VALUE_TYPES[RandomUtils.between(0, VALUE_TYPES.length - 1)];
            String fieldName = RandomUtils.generateVariableName(fieldType.toLowerCase());

            fieldNames.add(fieldName);
            fieldToTypeMap.put(fieldName, fieldType);

            // 为每个字段生成随机默认值
            String defaultValue;
            switch (fieldType) {
                case "String":
                    defaultValue = RandomUtils.generateRandomString(10);
                    break;
                case "Integer":
                    defaultValue = String.valueOf(RandomUtils.between(0, 100));
                    break;
                case "Boolean":
                    defaultValue = String.valueOf(RandomUtils.randomBoolean());
                    break;
                case "Long":
                    defaultValue = String.valueOf(RandomUtils.betweenLong(0L, 1000L));
                    break;
                case "Float":
                    defaultValue = String.valueOf((float) RandomUtils.nextDouble(0.0, 100.0));
                    break;
                case "Double":
                    defaultValue = String.valueOf(RandomUtils.nextDouble(0.0, 100.0));
                    break;
                case "Object":
                    defaultValue = "null";
                    break;
                default:
                    defaultValue = "null";
                    break;
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
        String fieldEnabled = RandomUtils.generateVariableName("enabled");
        String fieldHandler = RandomUtils.generateVariableName("handler");
        String fieldLastValue = RandomUtils.generateVariableName("lastvalue");
        String fieldValueHistory = RandomUtils.generateVariableName("valuehistory");
        String fieldErrorCount = RandomUtils.generateVariableName("errorcount");
        String fieldSuccessCount = RandomUtils.generateVariableName("successcount");
        String fieldTransformers = RandomUtils.generateVariableName("transformers");
        String fieldFilters = RandomUtils.generateVariableName("filters");
        String fieldListeners = RandomUtils.generateVariableName("listeners");

        // 添加字段
        sb.append("    private boolean ").append(fieldEnabled).append(";\n");
        sb.append("    private Handler ").append(fieldHandler).append(";\n");
        sb.append("    private ").append(dataType).append(" ").append(fieldLastValue).append(";\n");
        sb.append("    private List<").append(dataType).append("> ").append(fieldValueHistory).append(";\n");
        sb.append("    private int ").append(fieldErrorCount).append(";\n");
        sb.append("    private int ").append(fieldSuccessCount).append(";\n");
        sb.append("    private List<Consumer<").append(dataType).append(">> ").append(fieldTransformers).append(";\n");
        sb.append("    private List<Consumer<").append(dataType).append(">> ").append(fieldFilters).append(";\n");
        sb.append("    private List<Consumer<").append(dataType).append(">> ").append(fieldListeners).append(";\n\n");

        // 构造函数
        String paramEnabled = RandomUtils.generateVariableName("enabled");
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this.").append(fieldEnabled).append(" = true;\n");
        sb.append("        this.").append(fieldHandler).append(" = new Handler(Looper.getMainLooper());\n");
        sb.append("        this.").append(fieldValueHistory).append(" = new ArrayList<>();\n");
        sb.append("        this.").append(fieldErrorCount).append(" = 0;\n");
        sb.append("        this.").append(fieldSuccessCount).append(" = 0;\n");
        sb.append("        this.").append(fieldTransformers).append(" = new ArrayList<>();\n");
        sb.append("        this.").append(fieldFilters).append(" = new ArrayList<>();\n");
        sb.append("        this.").append(fieldListeners).append(" = new ArrayList<>();\n");
        sb.append("    }\n\n");

        sb.append("    public ").append(className).append("(boolean ").append(paramEnabled).append(") {\n");
        sb.append("        this();\n");
        sb.append("        this.").append(fieldEnabled).append(" = ").append(paramEnabled).append(";\n");
        sb.append("    }\n\n");

        // accept方法
        String paramValue = RandomUtils.generateVariableName("value");
        String processMethodName = "process" + RandomUtils.generateRandomString(8);
        String validateMethodName = "validate" + RandomUtils.generateRandomString(8);
        String transformMethodName = "transform" + RandomUtils.generateRandomString(8);
        String filterMethodName = "filter" + RandomUtils.generateRandomString(8);
        String notifyMethodName = "notify" + RandomUtils.generateRandomString(8);

        sb.append("    @Override\n");
        sb.append("    public void accept(").append(dataType).append(" ").append(paramValue).append(") {\n");
        sb.append("        if (!").append(fieldEnabled).append(") {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        if (!").append(validateMethodName).append("(").append(paramValue).append(")) {\n");
        sb.append("            ").append(fieldErrorCount).append("++;\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        ").append(dataType).append(" ").append(transformMethodName).append(" = ").append(transformMethodName).append("(").append(paramValue).append(");\n");
        sb.append("        if (!").append(filterMethodName).append("(").append(transformMethodName).append(")) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        this.").append(fieldLastValue).append(" = ").append(transformMethodName).append(";\n");
        sb.append("        this.").append(fieldValueHistory).append(".add(").append(transformMethodName).append(");\n");
        sb.append("        this.").append(fieldSuccessCount).append("++;\n");
        sb.append("        ").append(processMethodName).append("(").append(transformMethodName).append(");\n");
        sb.append("        ").append(notifyMethodName).append("(").append(transformMethodName).append(");\n");
        sb.append("    }\n\n");

        // 验证方法
        String paramValidateValue = RandomUtils.generateVariableName("value");
        sb.append("    private boolean ").append(validateMethodName).append("(").append(dataType).append(" ").append(paramValidateValue).append(") {\n");
        sb.append("        return ").append(paramValidateValue).append(" != null;\n");
        sb.append("    }\n\n");

        // 转换方法
        String paramTransformValue = RandomUtils.generateVariableName("value");
        String localTransformed = RandomUtils.generateVariableName("transformed");
        sb.append("    private ").append(dataType).append(" ").append(transformMethodName).append("(").append(dataType).append(" ").append(paramTransformValue).append(") {\n");
        sb.append("        ").append(dataType).append(" ").append(localTransformed).append(" = ").append(paramTransformValue).append(";\n");
        sb.append("        for (Consumer<").append(dataType).append("> ").append(RandomUtils.generateVariableName("transformer")).append(" : ").append(fieldTransformers).append(") {\n");
        sb.append("            ").append(localTransformed).append(" = applyTransformer(").append(localTransformed).append(", ").append(RandomUtils.generateVariableName("transformer")).append(");\n");
        sb.append("        }\n");
        sb.append("        return ").append(localTransformed).append(";\n");
        sb.append("    }\n\n");

        // 应用转换器方法
        String paramApplyValue = RandomUtils.generateVariableName("value");
        String paramTransformer = RandomUtils.generateVariableName("transformer");
        sb.append("    private ").append(dataType).append(" applyTransformer(").append(dataType).append(" ").append(paramApplyValue).append(", Consumer<").append(dataType).append("> ").append(paramTransformer).append(") {\n");
        sb.append("        ").append(paramTransformer).append(".accept(").append(paramApplyValue).append(");\n");
        sb.append("        return ").append(paramApplyValue).append(";\n");
        sb.append("    }\n\n");

        // 过滤方法
        String paramFilterValue = RandomUtils.generateVariableName("value");
        sb.append("    private boolean ").append(filterMethodName).append("(").append(dataType).append(" ").append(paramFilterValue).append(") {\n");
        sb.append("        for (Consumer<").append(dataType).append("> ").append(RandomUtils.generateVariableName("filter")).append(" : ").append(fieldFilters).append(") {\n");
        sb.append("            if (!passesFilter(").append(paramFilterValue).append(", ").append(RandomUtils.generateVariableName("filter")).append(")) {\n");
        sb.append("                return false;\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return true;\n");
        sb.append("    }\n\n");

        // 通过过滤器方法
        String paramPassValue = RandomUtils.generateVariableName("value");
        String paramPassFilter = RandomUtils.generateVariableName("filter");
        sb.append("    private boolean passesFilter(").append(dataType).append(" ").append(paramPassValue).append(", Consumer<").append(dataType).append("> ").append(paramPassFilter).append(") {\n");
        sb.append("        try {\n");
        sb.append("            ").append(paramPassFilter).append(".accept(").append(paramPassValue).append(");\n");
        sb.append("            return true;\n");
        sb.append("        } catch (Exception ").append(RandomUtils.generateVariableName("e")).append(") {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 处理方法
        String paramProcessValue = RandomUtils.generateVariableName("value");
        sb.append("    private void ").append(processMethodName).append("(").append(dataType).append(" ").append(paramProcessValue).append(") {\n");
        sb.append("        switch (").append(constConsumerType).append(") {\n");
        sb.append("            case \"data\":\n");
        sb.append("                process").append(RandomUtils.generateRandomString(8)).append("(").append(paramProcessValue).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"event\":\n");
        sb.append("                process").append(RandomUtils.generateRandomString(8)).append("(").append(paramProcessValue).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"message\":\n");
        sb.append("                process").append(RandomUtils.generateRandomString(8)).append("(").append(paramProcessValue).append(");\n");
        sb.append("                break;\n");
        sb.append("            default:\n");
        sb.append("                process").append(RandomUtils.generateRandomString(8)).append("(").append(paramProcessValue).append(");\n");
        sb.append("                break;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 通知方法
        String paramNotifyValue = RandomUtils.generateVariableName("value");
        sb.append("    private void ").append(notifyMethodName).append("(").append(dataType).append(" ").append(paramNotifyValue).append(") {\n");
        sb.append("        for (Consumer<").append(dataType).append("> ").append(RandomUtils.generateVariableName("listener")).append(" : ").append(fieldListeners).append(") {\n");
        sb.append("            ").append(fieldHandler).append(".post(() -> ").append(RandomUtils.generateVariableName("listener")).append(".accept(").append(paramNotifyValue).append("));\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 随机生成getter和setter方法
        String getEnabledMethodName = "get" + RandomUtils.generateRandomString(8);
        String setEnabledMethodName = "set" + RandomUtils.generateRandomString(8);
        String getLastValueMethodName = "get" + RandomUtils.generateRandomString(8);
        String getValueHistoryMethodName = "get" + RandomUtils.generateRandomString(8);
        String getErrorCountMethodName = "get" + RandomUtils.generateRandomString(8);
        String getSuccessCountMethodName = "get" + RandomUtils.generateRandomString(8);
        String addTransformerMethodName = "add" + RandomUtils.generateRandomString(8);
        String addFilterMethodName = "add" + RandomUtils.generateRandomString(8);
        String addListenerMethodName = "add" + RandomUtils.generateRandomString(8);
        String clearHistoryMethodName = "clear" + RandomUtils.generateRandomString(8);
        String resetCountersMethodName = "reset" + RandomUtils.generateRandomString(8);

        // getEnabled
        if (RandomUtils.randomBoolean()) {
            sb.append("    public boolean ").append(getEnabledMethodName).append("() {\n");
            sb.append("        return ").append(fieldEnabled).append(";\n");
            sb.append("    }\n\n");
        }

        // setEnabled
        if (RandomUtils.randomBoolean()) {
            String paramNewEnabled = RandomUtils.generateVariableName("enabled");
            sb.append("    public void ").append(setEnabledMethodName).append("(boolean ").append(paramNewEnabled).append(") {\n");
            sb.append("        this.").append(fieldEnabled).append(" = ").append(paramNewEnabled).append(";\n");
            sb.append("    }\n\n");
        }

        // getLastValue
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(dataType).append(" ").append(getLastValueMethodName).append("() {\n");
            sb.append("        return ").append(fieldLastValue).append(";\n");
            sb.append("    }\n\n");
        }

        // getValueHistory
        if (RandomUtils.randomBoolean()) {
            sb.append("    public List<").append(dataType).append("> ").append(getValueHistoryMethodName).append("() {\n");
            sb.append("        return new ArrayList<>(").append(fieldValueHistory).append(");\n");
            sb.append("    }\n\n");
        }

        // getErrorCount
        if (RandomUtils.randomBoolean()) {
            sb.append("    public int ").append(getErrorCountMethodName).append("() {\n");
            sb.append("        return ").append(fieldErrorCount).append(";\n");
            sb.append("    }\n\n");
        }

        // getSuccessCount
        if (RandomUtils.randomBoolean()) {
            sb.append("    public int ").append(getSuccessCountMethodName).append("() {\n");
            sb.append("        return ").append(fieldSuccessCount).append(";\n");
            sb.append("    }\n\n");
        }

        // addTransformer
        if (RandomUtils.randomBoolean()) {
            String paramTransformers = RandomUtils.generateVariableName("transformer");
            sb.append("    public void ").append(addTransformerMethodName).append("(Consumer<").append(dataType).append("> ").append(paramTransformer).append(") {\n");
            sb.append("        if (").append(paramTransformers).append(" != null) {\n");
            sb.append("            ").append(fieldTransformers).append(".add(").append(paramTransformer).append(");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // addFilter
        if (RandomUtils.randomBoolean()) {
            String paramFilter = RandomUtils.generateVariableName("filter");
            sb.append("    public void ").append(addFilterMethodName).append("(Consumer<").append(dataType).append("> ").append(paramFilter).append(") {\n");
            sb.append("        if (").append(paramFilter).append(" != null) {\n");
            sb.append("            ").append(fieldFilters).append(".add(").append(paramFilter).append(");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // addListener
        if (RandomUtils.randomBoolean()) {
            String paramListener = RandomUtils.generateVariableName("listener");
            sb.append("    public void ").append(addListenerMethodName).append("(Consumer<").append(dataType).append("> ").append(paramListener).append(") {\n");
            sb.append("        if (").append(paramListener).append(" != null) {\n");
            sb.append("            ").append(fieldListeners).append(".add(").append(paramListener).append(");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // clearHistory
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void ").append(clearHistoryMethodName).append("() {\n");
            sb.append("        ").append(fieldValueHistory).append(".clear();\n");
            sb.append("    }\n\n");
        }

        // resetCounters
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void ").append(resetCountersMethodName).append("() {\n");
            sb.append("        ").append(fieldErrorCount).append(" = 0;\n");
            sb.append("        ").append(fieldSuccessCount).append(" = 0;\n");
            sb.append("    }\n\n");
        }

        // 生成与计算器关联的方法
        if (RandomUtils.randomBoolean()) {
            String calculateMethodName = "calculate" + RandomUtils.generateRandomString(8);
            String localTotal = RandomUtils.generateVariableName("total");
            String localValue = RandomUtils.generateVariableName("value");

            sb.append("    public double ").append(calculateMethodName).append("() {\n");
            sb.append("        double ").append(localTotal).append(" = 0.0;\n");
            sb.append("        for (").append(dataType).append(" ").append(localValue).append(" : ").append(fieldValueHistory).append(") {\n");
            sb.append("            ").append(localTotal).append(" += calculateValue(").append(localValue).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(localTotal).append(";\n");
            sb.append("    }\n\n");

            // 生成计算值方法
            String paramCalcValue = RandomUtils.generateVariableName("value");
            sb.append("    private double calculateValue(").append(dataType).append(" ").append(paramCalcValue).append(") {\n");
            sb.append("        if (").append(paramCalcValue).append(" == null) {\n");
            sb.append("            return 0.0;\n");
            sb.append("        }\n");
            sb.append("        return ").append(paramCalcValue).append(" instanceof Number ? ((Number) ").append(paramCalcValue).append(").doubleValue() : ").append(paramCalcValue).append(".toString().length();\n");
            sb.append("    }\n\n");
        }

        // 生成与图表关联的方法
        if (RandomUtils.randomBoolean()) {
            String getDataMethodName = "get" + RandomUtils.generateRandomString(8);
            String localDataPoints = RandomUtils.generateVariableName("datapoints");

            sb.append("    public double[] ").append(getDataMethodName).append("() {\n");
            sb.append("        double[] ").append(localDataPoints).append(" = new double[3];\n");
            sb.append("        ").append(localDataPoints).append("[0] = ").append(fieldErrorCount).append(";\n");
            sb.append("        ").append(localDataPoints).append("[1] = ").append(fieldSuccessCount).append(";\n");
            sb.append("        ").append(localDataPoints).append("[2] = ").append(fieldValueHistory).append(".size();\n");
            sb.append("        return ").append(localDataPoints).append(";\n");
            sb.append("    }\n\n");
        }

        // 生成与集合关联的方法
        if (RandomUtils.randomBoolean()) {
            String getUniqueMethodName = "get" + RandomUtils.generateRandomString(8);
            String localUnique = RandomUtils.generateVariableName("unique");
            String localValue = RandomUtils.generateVariableName("value");

            sb.append("    public Set<").append(dataType).append("> ").append(getUniqueMethodName).append("() {\n");
            sb.append("        Set<").append(dataType).append("> ").append(localUnique).append(" = new HashSet<>();\n");
            sb.append("        for (").append(dataType).append(" ").append(localValue).append(" : ").append(fieldValueHistory).append(") {\n");
            sb.append("            if (").append(localValue).append(" != null) {\n");
            sb.append("                ").append(localUnique).append(".add(").append(localValue).append(");\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        return ").append(localUnique).append(";\n");
            sb.append("    }\n\n");
        }

        sb.append("}\n\n");

        generateJavaFile(className, sb.toString(), "consumer");
    }
}
