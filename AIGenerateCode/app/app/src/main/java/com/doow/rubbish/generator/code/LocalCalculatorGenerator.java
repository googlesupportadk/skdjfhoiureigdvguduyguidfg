package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.ArrayList;
import java.util.List;

public class LocalCalculatorGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] CALCULATOR_TYPES = {
            "BasicCalculator", "ScientificCalculator", "FinancialCalculator", "UnitConverter", "StatisticalCalculator",
            "MatrixCalculator", "VectorCalculator", "ComplexCalculator", "FractionCalculator", "PercentageCalculator"
    };

    private static final String[] OPERATION_TYPES = {
            "add", "subtract", "multiply", "divide", "power", "root", "log", "sin", "cos", "tan",
            "asin", "acos", "atan", "sinh", "cosh", "tanh", "exp", "ln", "log10", "sqrt", "cbrt"
    };

    private static final String[] FIELD_TYPES = {
            "int", "long", "float", "double", "boolean", "String", "Object"
    };

    private static final String[] MEMORY_OPERATIONS = {
            "clear", "add", "subtract", "recall", "store"
    };

    public LocalCalculatorGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成计算器类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Calculator");
            generateCalculatorClass(className, asyncHandler);
        }
    }

    private void generateCalculatorClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("calculator"));

        sb.append(generateImportStatement("android.util.Log"));

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        String calculatorType = CALCULATOR_TYPES[RandomUtils.between(0, CALCULATOR_TYPES.length - 1)];
        String operationType = OPERATION_TYPES[RandomUtils.between(0, OPERATION_TYPES.length - 1)];

        sb.append("public class ").append(className).append(" {\n\n");

        // 添加常量
        sb.append("    private static final String TAG = \"").append(className).append("\");\n");
        sb.append("    private static final String CALCULATOR_TYPE = \"").append(calculatorType).append("\");\n");
        sb.append("    private static final String OPERATION_TYPE = \"").append(operationType).append("\");\n\n");

        // 随机生成多个字段
        int fieldCount = RandomUtils.between(3, 8);
        List<String> fieldNames = new ArrayList<>();
        for (int i = 0; i < fieldCount; i++) {
            String fieldType = FIELD_TYPES[RandomUtils.between(0, FIELD_TYPES.length - 1)];
            String fieldName = RandomUtils.generateVariableName(fieldType);
            fieldNames.add(fieldName);

            // 随机决定是否为静态字段
            if (RandomUtils.randomBoolean()) {
                sb.append("    private static final ").append(fieldType).append(" ").append(fieldName);
                // 生成初始值
                sb.append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            } else {
                sb.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n");
            }
        }

        // 添加计算器相关字段
        sb.append("    private double result;\n");
        sb.append("    private double memory;\n");
        sb.append("    private double lastResult;\n");
        sb.append("    private int operationCount;\n\n");

        // 构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this.result = 0.0;\n");
        sb.append("        this.memory = 0.0;\n");
        sb.append("        this.lastResult = 0.0;\n");
        sb.append("        this.operationCount = 0;\n");

        // 初始化非静态字段
        for (String fieldName : fieldNames) {
            if (!isStaticField(fieldName)) {
                String fieldType = getFieldType(fieldName);
                sb.append("        this.").append(fieldName).append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            }
        }
        sb.append("    }\n\n");

        // 生成主计算方法
        sb.append("    public double ").append(operationType).append("(double a, double b) {\n");
        sb.append("        operationCount++;\n");
        sb.append("        double calcResult = 0.0;\n");
        sb.append("        switch (\"").append(operationType).append("\") {\n");

        // 根据操作类型生成不同的case
        if (RandomUtils.randomBoolean()) {
            sb.append("            case \"add\":\n");
            sb.append("                calcResult = a + b;\n");
            sb.append("                break;\n");
            sb.append("            case \"subtract\":\n");
            sb.append("                calcResult = a - b;\n");
            sb.append("                break;\n");
            sb.append("            case \"multiply\":\n");
            sb.append("                calcResult = a * b;\n");
            sb.append("                break;\n");
            sb.append("            case \"divide\":\n");
            sb.append("                if (b != 0) {\n");
            sb.append("                    calcResult = a / b;\n");
            sb.append("                } else {\n");
            sb.append("                    Log.e(TAG, \"Division by zero\");\n");
            sb.append("                }\n");
            sb.append("                break;\n");
        }

        if (RandomUtils.randomBoolean()) {
            sb.append("            case \"power\":\n");
            sb.append("                calcResult = Math.pow(a, b);\n");
            sb.append("                break;\n");
            sb.append("            case \"root\":\n");
            sb.append("                if (b != 0) {\n");
            sb.append("                    calcResult = Math.pow(a, 1.0 / b);\n");
            sb.append("                } else {\n");
            sb.append("                    Log.e(TAG, \"Root of order zero\");\n");
            sb.append("                }\n");
            sb.append("                break;\n");
        }

        if (RandomUtils.randomBoolean()) {
            sb.append("            case \"sin\":\n");
            sb.append("                calcResult = Math.sin(a);\n");
            sb.append("                break;\n");
            sb.append("            case \"cos\":\n");
            sb.append("                calcResult = Math.cos(a);\n");
            sb.append("                break;\n");
            sb.append("            case \"tan\":\n");
            sb.append("                calcResult = Math.tan(a);\n");
            sb.append("                break;\n");
        }

        sb.append("            default:\n");
        sb.append("                Log.e(TAG, \"Unknown operation: \" + OPERATION_TYPE);\n");
        sb.append("                break;\n");
        sb.append("        }\n");

        sb.append("        lastResult = result;\n");
        sb.append("        result = calcResult;\n");
        sb.append("        Log.d(TAG, \"").append(operationType).append(" result: \" + calcResult);\n");
        sb.append("        return calcResult;\n");
        sb.append("    }\n\n");

        // 生成数组计算方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public double calculate(double[] values) {\n");
            sb.append("        if (values == null || values.length == 0) {\n");
            sb.append("            Log.e(TAG, \"No values provided\");\n");
            sb.append("            return 0;\n");
            sb.append("        }\n");
            sb.append("        double sum = 0;\n");
            sb.append("        for (double value : values) {\n");
            sb.append("            sum += value;\n");
            sb.append("        }\n");
            sb.append("        Log.d(TAG, \"Sum: \" + sum);\n");
            sb.append("        return sum;\n");
            sb.append("    }\n\n");
        }

        // 生成记忆功能方法
        if (RandomUtils.randomBoolean()) {
            String memoryOp = MEMORY_OPERATIONS[RandomUtils.between(0, MEMORY_OPERATIONS.length - 1)];
            sb.append("    public void ").append(memoryOp).append("Memory(double value) {\n");
            sb.append("        switch (\"").append(memoryOp).append("\") {\n");
            sb.append("            case \"clear\":\n");
            sb.append("                memory = 0;\n");
            sb.append("                break;\n");
            sb.append("            case \"add\":\n");
            sb.append("                memory += value;\n");
            sb.append("                break;\n");
            sb.append("            case \"subtract\":\n");
            sb.append("                memory -= value;\n");
            sb.append("                break;\n");
            sb.append("            case \"recall\":\n");
            sb.append("                result = memory;\n");
            sb.append("                break;\n");
            sb.append("            case \"store\":\n");
            sb.append("                memory = result;\n");
            sb.append("                break;\n");
            sb.append("            default:\n");
            sb.append("                Log.e(TAG, \"Unknown memory operation\");\n");
            sb.append("                break;\n");
            sb.append("        }\n");
            sb.append("        Log.d(TAG, \"Memory after ").append(memoryOp).append(": \" + memory);\n");
            sb.append("    }\n\n");
        }

        // 随机生成辅助方法
        for (String fieldName : fieldNames) {
            String fieldType = getFieldType(fieldName);

            if (fieldType.equals("int") || fieldType.equals("long") || fieldType.equals("float") || fieldType.equals("double")) {
                sb.append("    private double calculate").append(capitalize(fieldName)).append("() {\n");
                sb.append("        double fieldValue = ").append(fieldName).append(";\n");
                sb.append("        // 随机计算逻辑\n");
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
                sb.append("        // 随机验证逻辑\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            isValid = !isValid;\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Validated ").append(fieldName).append(": \" + isValid);\n");
                sb.append("        return isValid;\n");
                sb.append("    }\n\n");
            } else {
                sb.append("    private void process").append(capitalize(fieldName)).append("() {\n");
                sb.append("        // 随机处理逻辑\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            ").append(fieldName).append(" = \"processed_\" + ").append(fieldName).append(";\n");
                sb.append("        } else {\n");
                sb.append("            ").append(fieldName).append(" = String.valueOf(").append(fieldName).append(".hashCode());\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Processed ").append(fieldName).append(": \" + ").append(fieldName).append(");\n");
                sb.append("    }\n\n");
            }
        }

        // 生成统计方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void logStats() {\n");
            sb.append("        Log.d(TAG, \"Calculator stats - Type: \" + CALCULATOR_TYPE);\n");
            sb.append("        Log.d(TAG, \"Operation: \" + OPERATION_TYPE);\n");
            sb.append("        Log.d(TAG, \"Operations performed: \" + operationCount);\n");
            sb.append("        Log.d(TAG, \"Last result: \" + lastResult);\n");
            sb.append("        Log.d(TAG, \"Current result: \" + result);\n");
            sb.append("        Log.d(TAG, \"Memory: \" + memory);\n");

            // 随机调用一些辅助方法
            int methodCallCount = RandomUtils.between(1, 3);
            for (int i = 0; i < methodCallCount; i++) {
                int methodIndex = RandomUtils.between(0, fieldNames.size() - 1);
                String fieldName = fieldNames.get(methodIndex);
                String fieldType = getFieldType(fieldName);

                if (fieldType.equals("int") || fieldType.equals("long") || fieldType.equals("float") || fieldType.equals("double")) {
                    sb.append("        calculate").append(capitalize(fieldName)).append("();\n");
                } else if (fieldType.equals("boolean")) {
                    sb.append("        validate").append(capitalize(fieldName)).append("();\n");
                } else {
                    sb.append("        process").append(capitalize(fieldName)).append("();\n");
                }
            }

            sb.append("    }\n\n");
        }

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "calculator");
    }

    // 辅助方法：生成初始值
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

    // 辅助方法：判断是否为静态字段
    private boolean isStaticField(String fieldName) {
        return fieldName.startsWith("static_");
    }

    // 辅助方法：获取字段类型
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

    // 辅助方法：首字母大写
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
