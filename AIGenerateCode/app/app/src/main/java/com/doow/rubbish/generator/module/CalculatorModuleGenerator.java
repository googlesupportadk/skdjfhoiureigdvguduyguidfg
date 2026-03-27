package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.ModuleVariationHelper;
import com.doow.rubbish.generator.VariationManager;

public class CalculatorModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;
    private ModuleVariationHelper variationHelper;

    // 计算器类型
    private static final String[] CALCULATOR_TYPES = {
        "basic", "scientific", "programmer", "currency", "unit"
    };

    // 显示方式
    private static final String[] DISPLAY_TYPES = {
        "standard", "history", "tape", "expression"
    };

    public CalculatorModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        this.variationHelper = ModuleVariationHelper.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成计算器模块");

        // 获取当前UI风格和异步处理方式
        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成计算器模块
        generateCalculatorModule(uiStyle, asyncHandler);
    }

    private void generateCalculatorModule(String uiStyle, String asyncHandler) throws Exception {
        // 生成计算器管理器
        generateCalculatorManager(uiStyle, asyncHandler);

        // 生成计算器工具类
        generateCalculatorUtils(uiStyle, asyncHandler);

        // 生成计算器历史记录
        generateCalculatorHistory(uiStyle, asyncHandler);
    }

    private void generateCalculatorManager(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = variationHelper.generateClassName("Calculator");
        String instanceName = variationHelper.generateVariableName("Instance");
        String contextName = variationHelper.generateObjectName();
        String tagName = variationHelper.generateStringName();
        String decimalPlacesName = variationHelper.generateIntName();
        String roundingModeName = variationHelper.generateStringName();
        String callbackVarName = variationHelper.generateVariableName("Callback");

        // 使用随机值
        int decimalPlaces = variationHelper.generateIntRange(2, 6)[0];
        String roundingMode = variationHelper.generateRoundingMode();

        sb.append("package ").append(packageName).append(".calculator;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import java.math.BigDecimal;\n");
        sb.append("import java.math.RoundingMode;\n");
        sb.append("import java.util.ArrayList;\n");
        sb.append("import java.util.List;\n");

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
        sb.append("    void onHistoryChanged();\n");
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
        sb.append("    private final List<String> ").append(variationHelper.generateCollectionName()).append(" = new ArrayList<>();\n");
        sb.append("    private ").append(callbackVarName).append(" ").append(callbackVarName).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    private ").append(className).append("(").append(contextName).append(" ").append(contextName).append(") {\n");
        sb.append("        this.").append(contextName).append(" = ").append(contextName).append(".getApplicationContext();\n");
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

        // 生成输入数字方法
        generateInputDigitMethod(sb, className, tagName, callbackVarName);

        // 生成输入运算符方法
        generateInputOperatorMethod(sb, className, tagName, callbackVarName);

        // 生成计算方法
        generateCalculateMethod(sb, className, decimalPlacesName, roundingModeName, tagName, callbackVarName);

        // 生成清除方法
        generateClearMethod(sb, className, tagName, callbackVarName);

        // 生成获取历史记录方法
        generateGetHistoryMethod(sb, className, tagName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "calculator");
    }

    private void generateInputDigitMethod(StringBuilder sb, String className, String tagName, String callbackVarName) {
        String methodName = variationHelper.generateMethodName("InputDigit");
        String digitName = variationHelper.generateStringName();
        String expressionName = variationHelper.generateStringName();
        String resultName = variationHelper.generateStringName();
        String logMessage = variationHelper.generateLogMessage("Input digit");

        sb.append("    public void ").append(methodName).append("(String ").append(digitName).append(") {\n");
        sb.append("        String ").append(expressionName).append(" = getExpression();\n");
        sb.append("        String ").append(resultName).append(" = getResult();\n");
        sb.append("\n");
        sb.append("        if (").append(resultName).append(" != null && !").append(resultName).append(".isEmpty()) {\n");
        sb.append("            ").append(expressionName).append(" = \"\";\n");
        sb.append("            ").append(resultName).append(" = \"\";\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        ").append(expressionName).append(" += ").append(digitName).append(";\n");
        sb.append("        setExpression(").append(expressionName).append(");\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + ").append(digitName).append(");\n");
        sb.append("\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onResult(").append(expressionName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateInputOperatorMethod(StringBuilder sb, String className, String tagName, String callbackVarName) {
        String methodName = variationHelper.generateMethodName("InputOperator");
        String operatorName = variationHelper.generateStringName();
        String expressionName = variationHelper.generateStringName();
        String resultName = variationHelper.generateStringName();
        String logMessage = variationHelper.generateLogMessage("Input operator");

        sb.append("    public void ").append(methodName).append("(String ").append(operatorName).append(") {\n");
        sb.append("        String ").append(expressionName).append(" = getExpression();\n");
        sb.append("        String ").append(resultName).append(" = getResult();\n");
        sb.append("\n");
        sb.append("        if (").append(resultName).append(" != null && !").append(resultName).append(".isEmpty()) {\n");
        sb.append("            ").append(expressionName).append(" = ").append(resultName).append(";\n");
        sb.append("            ").append(resultName).append(" = \"\";\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        ").append(expressionName).append(" += \" \" + ").append(operatorName).append(" + \" \";\n");
        sb.append("        setExpression(").append(expressionName).append(");\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + ").append(operatorName).append(");\n");
        sb.append("\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onResult(").append(expressionName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateCalculateMethod(StringBuilder sb, String className, String decimalPlacesName,
                                         String roundingModeName, String tagName, String callbackVarName) {
        String methodName = variationHelper.generateMethodName("Calculate");
        String expressionName = variationHelper.generateStringName();
        String resultName = variationHelper.generateStringName();
        String fullExpressionName = variationHelper.generateStringName();
        String logMessage = variationHelper.generateLogMessage("Calculated");
        String logErrorMessage = variationHelper.generateLogMessage("Calculation error");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        String ").append(expressionName).append(" = getExpression();\n");
        sb.append("        String ").append(resultName).append(" = getResult();\n");
        sb.append("\n");
        sb.append("        if (").append(expressionName).append(" == null || ").append(expressionName).append(".isEmpty()) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        try {\n");
        sb.append("            String ").append(fullExpressionName).append(" = ").append(expressionName)
          .append(" + \" \" + ").append(resultName).append(";\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + ")
          .append(fullExpressionName).append(");\n");
        sb.append("\n");
        sb.append("            // 使用BigDecimal进行精确计算\n");
        sb.append("            BigDecimal result = evaluateExpression(").append(fullExpressionName).append(");\n");
        sb.append("\n");
        sb.append("            // 保存到历史记录\n");
        sb.append("            saveToHistory(").append(fullExpressionName).append(", result.toString());\n");
        sb.append("\n");
        sb.append("            // 更新当前结果\n");
        sb.append("            ").append(expressionName).append(" = \"\";\n");
        sb.append("            ").append(resultName).append(" = result.toString();\n");
        sb.append("\n");
        sb.append("            if (").append(callbackVarName).append(" != null) {\n");
        sb.append("                ").append(callbackVarName).append(".onResult(").append(resultName).append(");\n");
        sb.append("                ").append(callbackVarName).append(".onHistoryChanged();\n");
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

    private void generateEvaluateExpressionMethod(StringBuilder sb, String className,
                                                 String decimalPlacesName, String roundingModeName) {
        String methodName = variationHelper.generateMethodName("EvaluateExpression");
        String expressionName = variationHelper.generateStringName();
        String partsName = variationHelper.generateCollectionName();
        String resultName = variationHelper.generateObjectName();
        String operatorName = variationHelper.generateStringName();
        String operandName = variationHelper.generateObjectName();

        sb.append("    private BigDecimal ").append(methodName).append("(String ").append(expressionName).append(") {\n");
        sb.append("        // 简化版表达式评估\n");
        sb.append("        String[] ").append(partsName).append(" = ").append(expressionName).append(".split(\" \");\n");
        sb.append("        BigDecimal ").append(resultName).append(" = new BigDecimal(").append(partsName).append("[0]);\n");
        sb.append("\n");
        sb.append("        for (int i = 1; i < ").append(partsName).append(".length; i += 2) {\n");
        sb.append("            String ").append(operatorName).append(" = ").append(partsName).append("[i];\n");
        sb.append("            BigDecimal ").append(operandName).append(" = new BigDecimal(").append(partsName).append("[i + 1]);\n");
        sb.append("\n");
        sb.append("            switch (").append(operatorName).append(") {\n");
        sb.append("                case \"+\":\n");
        sb.append("                    ").append(resultName).append(" = ").append(resultName).append(".add(").append(operandName).append(");\n");
        sb.append("                    break;\n");
        sb.append("                case \"-\":\n");
        sb.append("                    ").append(resultName).append(" = ").append(resultName).append(".subtract(").append(operandName).append(");\n");
        sb.append("                    break;\n");
        sb.append("                case \"*\":\n");
        sb.append("                    ").append(resultName).append(" = ").append(resultName).append(".multiply(").append(operandName).append(");\n");
        sb.append("                    break;\n");
        sb.append("                case \"/\":\n");
        sb.append("                    ").append(resultName).append(" = ").append(resultName).append(".divide(").append(operandName).append(", ")
          .append(decimalPlacesName).append(", ").append(roundingModeName).append(");\n");
        sb.append("                    break;\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        return ").append(resultName).append(".setScale(").append(decimalPlacesName).append(", ")
          .append(roundingModeName).append(");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateClearMethod(StringBuilder sb, String className, String tagName, String callbackVarName) {
        String methodName = variationHelper.generateMethodName("Clear");
        String logMessage = variationHelper.generateLogMessage("Cleared");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        setExpression(\"\");\n");
        sb.append("        setResult(\"\");\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onResult(\"\");\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetHistoryMethod(StringBuilder sb, String className, String tagName) {
        String methodName = variationHelper.generateMethodName("GetHistory");
        String logMessage = variationHelper.generateLogMessage("Retrieved history");

        sb.append("    public List<String> ").append(methodName).append("() {\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        return new ArrayList<>(").append(variationHelper.generateCollectionName()).append(");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateCalculatorUtils(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名
        String className = variationHelper.generateClassName("CalculatorUtils");
        String tagName = variationHelper.generateStringName();

        sb.append("package ").append(packageName).append(".calculator;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import java.math.BigDecimal;\n");

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
        sb.append("    }\n");
        sb.append("\n");

        // 生成验证数字方法
        generateValidateNumberMethod(sb, className, tagName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "calculator");
    }

    private void generateValidateNumberMethod(StringBuilder sb, String className, String tagName) {
        String methodName = variationHelper.generateMethodName("ValidateNumber");
        String numberName = variationHelper.generateStringName();
        String logMessage = variationHelper.generateLogMessage("Validated number");

        sb.append("    public static boolean ").append(methodName).append("(String ").append(numberName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            new BigDecimal(").append(numberName).append(");\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + ").append(numberName).append(");\n");
        sb.append("            return true;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"Invalid number\", e);\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateCalculatorHistory(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名
        String className = variationHelper.generateClassName("CalculatorHistory");
        String tagName = variationHelper.generateStringName();
        String historyName = variationHelper.generateCollectionName();

        sb.append("package ").append(packageName).append(".calculator;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import java.util.ArrayList;\n");
        sb.append("import java.util.List;\n");

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
        sb.append("    }\n");
        sb.append("\n");

        // 生成添加历史记录方法
        generateAddHistoryMethod(sb, className, tagName, historyName);

        // 生成清除历史记录方法
        generateClearHistoryMethod(sb, className, tagName, historyName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "calculator");
    }

    private void generateAddHistoryMethod(StringBuilder sb, String className, String tagName, String historyName) {
        String methodName = variationHelper.generateMethodName("AddHistory");
        String expressionName = variationHelper.generateStringName();
        String resultName = variationHelper.generateStringName();
        String logMessage = variationHelper.generateLogMessage("Added history");

        sb.append("    public static void ").append(methodName).append("(String ").append(expressionName).append(", String ").append(resultName).append(") {\n");
        sb.append("        String entry = ").append(expressionName).append(" + \" = \" + ").append(resultName).append(";\n");
        sb.append("        ").append(historyName).append(".add(entry);\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + entry);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateClearHistoryMethod(StringBuilder sb, String className, String tagName, String historyName) {
        String methodName = variationHelper.generateMethodName("ClearHistory");
        String logMessage = variationHelper.generateLogMessage("Cleared history");

        sb.append("    public static void ").append(methodName).append("() {\n");
        sb.append("        ").append(historyName).append(".clear();\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }
}
