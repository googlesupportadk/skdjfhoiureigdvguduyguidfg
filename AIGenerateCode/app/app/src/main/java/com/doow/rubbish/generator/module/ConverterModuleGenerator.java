package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.ModuleVariationHelper;
import com.doow.rubbish.generator.VariationManager;

public class ConverterModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;
    private ModuleVariationHelper variationHelper;

    // 转换器类型
    private static final String[] CONVERTER_TYPES = {
        "length", "weight", "temperature", "area", "volume", "time", "speed", "data"
    };

    // 显示方式
    private static final String[] DISPLAY_TYPES = {
        "dropdown", "input", "slider", "custom"
    };

    public ConverterModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        this.variationHelper = ModuleVariationHelper.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成转换器模块");

        // 获取当前UI风格和异步处理方式
        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成转换器模块
        generateConverterModule(uiStyle, asyncHandler);
    }

    private void generateConverterModule(String uiStyle, String asyncHandler) throws Exception {
        // 生成转换器管理器
        generateConverterManager(uiStyle, asyncHandler);

        // 生成转换器工具类
        generateConverterUtils(uiStyle, asyncHandler);

        // 生成转换器历史记录
        generateConverterHistory(uiStyle, asyncHandler);
    }

    private void generateConverterManager(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = variationHelper.generateClassName("Converter");
        String instanceName = variationHelper.generateVariableName("Instance");
        String contextName = variationHelper.generateObjectName();
        String tagName = variationHelper.generateStringName();
        String decimalPlacesName = variationHelper.generateIntName();
        String roundingModeName = variationHelper.generateStringName();
        String callbackVarName = variationHelper.generateVariableName("Callback");

        // 使用随机值
        int decimalPlaces = variationHelper.generateIntRange(2, 6)[0];
        String roundingMode = variationHelper.generateRoundingMode();

        sb.append("package ").append(packageName).append(".converter;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import java.math.BigDecimal;\n");
        sb.append("import java.math.RoundingMode;\n");
        sb.append("import java.util.HashMap;\n");
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

        // 回调接口
        sb.append("public interface ").append(callbackVarName).append(" {\n");
        sb.append("    void onResult(String result);\n");
        sb.append("    void onError(String error);\n");
        sb.append("}\n");
        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final int ").append(decimalPlacesName).append(" = ").append(decimalPlaces).append(";\n");
        sb.append("    private static final String ").append(roundingModeName).append(" = \"").append(roundingMode).append("\";\n");
        sb.append("\n");

        // 单例
        sb.append("    private static volatile ").append(className).append(" ").append(instanceName).append(";\n");
        sb.append("    private final ").append(contextName).append(" ").append(contextName).append(";\n");
        sb.append("    private final Map<String, BigDecimal> ").append(variationHelper.generateCollectionName()).append(" = new HashMap<>();\n");
        sb.append("    private ").append(callbackVarName).append(" ").append(callbackVarName).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    private ").append(className).append("(").append(contextName).append(" ").append(contextName).append(") {\n");
        sb.append("        this.").append(contextName).append(" = ").append(contextName).append(".getApplicationContext();\n");
        sb.append("        initializeConversionRates();\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取实例方法
        sb.append("    public static ").append(className).append(" getInstance(").append(contextName).append(" ").append(contextName).append(") {\n");
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

        // 设置回调方法
        sb.append("    public void setCallback(").append(callbackVarName).append(" ").append(callbackVarName).append(") {\n");
        sb.append("        this.").append(callbackVarName).append(" = ").append(callbackVarName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        // 初始化转换率方法
        generateInitializeConversionRatesMethod(sb, className, tagName);

        // 生成长度转换方法
        generateConvertLengthMethod(sb, className, decimalPlacesName, roundingModeName, callbackVarName, tagName);

        // 生成重量转换方法
        generateConvertWeightMethod(sb, className, decimalPlacesName, roundingModeName, callbackVarName, tagName);

        // 生成温度转换方法
        generateConvertTemperatureMethod(sb, className, decimalPlacesName, roundingModeName, callbackVarName, tagName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "converter");
    }

    private void generateInitializeConversionRatesMethod(StringBuilder sb, String className, String tagName) {
        String methodName = variationHelper.generateMethodName("InitializeConversionRates");
        String logMessage = variationHelper.generateLogMessage("Initialized conversion rates");

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        // 初始化长度转换率\n");
        sb.append("        ").append(variationHelper.generateCollectionName()).append(".put(\"meter_to_kilometer\", new BigDecimal(\"0.001\"));\n");
        sb.append("        ").append(variationHelper.generateCollectionName()).append(".put(\"meter_to_centimeter\", new BigDecimal(\"100\"));\n");
        sb.append("        ").append(variationHelper.generateCollectionName()).append(".put(\"meter_to_millimeter\", new BigDecimal(\"1000\"));\n");
        sb.append("        ").append(variationHelper.generateCollectionName()).append(".put(\"kilometer_to_meter\", new BigDecimal(\"1000\"));\n");
        sb.append("        ").append(variationHelper.generateCollectionName()).append(".put(\"centimeter_to_meter\", new BigDecimal(\"0.01\"));\n");
        sb.append("        ").append(variationHelper.generateCollectionName()).append(".put(\"millimeter_to_meter\", new BigDecimal(\"0.001\"));\n");
        sb.append("\n");
        sb.append("        // 初始化重量转换率\n");
        sb.append("        ").append(variationHelper.generateCollectionName()).append(".put(\"kilogram_to_gram\", new BigDecimal(\"1000\"));\n");
        sb.append("        ").append(variationHelper.generateCollectionName()).append(".put(\"kilogram_to_milligram\", new BigDecimal(\"1000000\"));\n");
        sb.append("        ").append(variationHelper.generateCollectionName()).append(".put(\"gram_to_kilogram\", new BigDecimal(\"0.001\"));\n");
        sb.append("        ").append(variationHelper.generateCollectionName()).append(".put(\"milligram_to_kilogram\", new BigDecimal(\"0.000001\"));\n");
        sb.append("\n");
        sb.append("        // 初始化温度转换率\n");
        sb.append("        ").append(variationHelper.generateCollectionName()).append(".put(\"celsius_to_fahrenheit\", new BigDecimal(\"1.8\"));\n");
        sb.append("        ").append(variationHelper.generateCollectionName()).append(".put(\"fahrenheit_to_celsius\", new BigDecimal(\"0.5556\"));\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateConvertLengthMethod(StringBuilder sb, String className, String decimalPlacesName,
                                             String roundingModeName, String callbackVarName, String tagName) {
        String methodName = variationHelper.generateMethodName("ConvertLength");
        String valueName = variationHelper.generateObjectName();
        String fromUnitName = variationHelper.generateStringName();
        String toUnitName = variationHelper.generateStringName();
        String logMessage = variationHelper.generateLogMessage("Converted length");
        String logErrorMessage = variationHelper.generateLogMessage("Length conversion error");

        sb.append("    public void ").append(methodName).append("(BigDecimal ").append(valueName)
          .append(", String ").append(fromUnitName).append(", String ").append(toUnitName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            BigDecimal result = ").append(valueName).append(".multiply(")
          .append(variationHelper.generateCollectionName()).append(".get(").append(fromUnitName)
          .append(" + \"_to_\" + ").append(toUnitName).append("));\n");
        sb.append("            result = result.setScale(").append(decimalPlacesName).append(", ")
          .append(roundingModeName).append(");\n");
        sb.append("\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + result);\n");
        sb.append("            if (").append(callbackVarName).append(" != null) {\n");
        sb.append("                ").append(callbackVarName).append(".onResult(\" + result.toString() + \");\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"").append(logErrorMessage).append("\", e);\n");
        sb.append("            if (").append(callbackVarName).append(" != null) {\n");
        sb.append("                ").append(callbackVarName).append(".onError(e.getMessage());\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateConvertWeightMethod(StringBuilder sb, String className, String decimalPlacesName,
                                            String roundingModeName, String callbackVarName, String tagName) {
        String methodName = variationHelper.generateMethodName("ConvertWeight");
        String valueName = variationHelper.generateObjectName();
        String fromUnitName = variationHelper.generateStringName();
        String toUnitName = variationHelper.generateStringName();
        String logMessage = variationHelper.generateLogMessage("Converted weight");
        String logErrorMessage = variationHelper.generateLogMessage("Weight conversion error");

        sb.append("    public void ").append(methodName).append("(BigDecimal ").append(valueName)
          .append(", String ").append(fromUnitName).append(", String ").append(toUnitName).append(") {\n");
        sb.append("        BigDecimal result = ").append(valueName).append(".multiply(")
          .append(variationHelper.generateCollectionName()).append(".get(").append(fromUnitName)
          .append(" + \"_to_\" + ").append(toUnitName).append("));\n");
        sb.append("        result = result.setScale(").append(decimalPlacesName).append(", ")
          .append(roundingModeName).append(");\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + result);\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("                ").append(callbackVarName).append(".onResult(\" + result.toString() + \");\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateConvertTemperatureMethod(StringBuilder sb, String className, String decimalPlacesName,
                                                 String roundingModeName, String callbackVarName, String tagName) {
        String methodName = variationHelper.generateMethodName("ConvertTemperature");
        String valueName = variationHelper.generateObjectName();
        String fromUnitName = variationHelper.generateStringName();
        String toUnitName = variationHelper.generateStringName();
        String logMessage = variationHelper.generateLogMessage("Temperature converted");

        sb.append("    public void ").append(methodName).append("(BigDecimal ").append(valueName)
          .append(", String ").append(fromUnitName).append(", String ").append(toUnitName).append(") {\n");
        sb.append("        BigDecimal result = ").append(valueName).append(".multiply(")
          .append(variationHelper.generateCollectionName()).append(".get(").append(fromUnitName)
          .append(" + \"_to_\" + ").append(toUnitName).append("));\n");
        sb.append("        result = result.setScale(").append(decimalPlacesName).append(", ")
          .append(roundingModeName).append(");\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + result);\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onResult(\" + result.toString() + \");\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateConverterUtils(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名
        String className = variationHelper.generateClassName("Utils");
        String tagName = variationHelper.generateStringName();
        String methodName = variationHelper.generateMethodName("Format");

        sb.append("package ").append(packageName).append(".converter;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import java.math.BigDecimal;\n");
        sb.append("import java.text.DecimalFormat;\n");

        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("\n");

        // 私有构造函数
        sb.append("    private ").append(className).append("() {\n");
        sb.append("    }\n");
        sb.append("\n");

        // 格式化方法
        sb.append("    public static String ").append(methodName).append("(BigDecimal value, int decimalPlaces) {\n");
        sb.append("        String pattern = \"#.").append("#".repeat(decimalPlaces)).append("\";\n");
        sb.append("        DecimalFormat df = new DecimalFormat(pattern);\n");
        sb.append("        return df.format(value);\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "converter");
    }

    private void generateConverterHistory(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名
        String className = variationHelper.generateClassName("History");
        String tagName = variationHelper.generateStringName();
        String historyName = variationHelper.generateCollectionName();
        String entryName = variationHelper.generateStringName();

        sb.append("package ").append(packageName).append(".converter;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import java.util.ArrayList;\n");
        sb.append("import java.util.List;\n");

        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("\n");

        // 历史记录
        sb.append("    private final List<String> ").append(historyName).append(" = new ArrayList<>();\n");
        sb.append("\n");

        // 添加历史记录方法
        sb.append("    public void addEntry(String ").append(entryName).append(") {\n");
        sb.append("        ").append(historyName).append(".add(").append(entryName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"Added entry: \" + ").append(entryName).append(");\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取历史记录方法
        sb.append("    public List<String> getHistory() {\n");
        sb.append("        return new ArrayList<>(").append(historyName).append(");\n");
        sb.append("    }\n");
        sb.append("\n");

        // 清空历史记录方法
        sb.append("    public void clear() {\n");
        sb.append("        ").append(historyName).append(".clear();\n");
        sb.append("        Log.d(").append(tagName).append(", \"Cleared history\");\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "converter");
    }
}
