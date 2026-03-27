package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.ArrayList;
import java.util.List;

public class LocalCalculationGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] OPERATION_TYPES = {
            "add", "subtract", "multiply", "divide", "power", "root",
            "logarithm", "exponential", "factorial", "absolute", "modulo",
            "gcd", "lcm", "prime", "composite", "square", "cube"
    };

    private static final String[] NUMBER_TYPES = {
            "int", "long", "float", "double", "short", "byte", "char", "boolean"
    };

    private static final String[] FIELD_TYPES = {
            "int", "long", "float", "double", "boolean", "String", "Object"
    };

    public LocalCalculationGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成计算类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Calculation");
            generateCalculationClass(className, asyncHandler);
        }
    }

    private void generateCalculationClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("math.calculation"));

        sb.append(generateImportStatement("android.util.Log"));

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        sb.append("public class ").append(className).append(" {\n\n");

        // 随机选择操作类型和数字类型
        String operationType = OPERATION_TYPES[RandomUtils.between(0, OPERATION_TYPES.length - 1)];
        String numberType = NUMBER_TYPES[RandomUtils.between(0, NUMBER_TYPES.length - 1)];

        // 添加常量
        sb.append("    private static final String TAG = \"").append(className).append("\");\n");
        sb.append("    private static final String OPERATION_TYPE = \"").append(operationType).append("\");\n");
        sb.append("    private static final String NUMBER_TYPE = \"").append(numberType).append("\");\n\n");

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

        // 添加计算相关字段
        sb.append("    private ").append(numberType).append(" result;\n");
        sb.append("    private ").append(numberType).append(" operand1;\n");
        sb.append("    private ").append(numberType).append(" operand2;\n\n");

        // 构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this.result = 0;\n");
        sb.append("        this.operand1 = 0;\n");
        sb.append("        this.operand2 = 0;\n");

        // 初始化非静态字段
        for (String fieldName : fieldNames) {
            if (!isStaticField(fieldName)) {
                String fieldType = getFieldType(fieldName);
                sb.append("        this.").append(fieldName).append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            }
        }
        sb.append("    }\n\n");

        // 生成主计算方法
        sb.append("    public ").append(numberType).append(" ").append(operationType).append("(").append(numberType).append(" a, ").append(numberType).append(" b) {\n");
        sb.append("        // 根据随机条件决定是否使用辅助方法处理输入\n");
        sb.append("        if (RandomUtils.randomBoolean()) {\n");
        sb.append("            a = processInput(a);\n");
        sb.append("        }\n");
        sb.append("        if (RandomUtils.randomBoolean()) {\n");
        sb.append("            b = processInput(b);\n");
        sb.append("        }\n");
        sb.append("        ").append(numberType).append(" calcResult = 0;\n");
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
            sb.append("                calcResult = (").append(numberType).append(") Math.pow(a, b);\n");
            sb.append("                break;\n");
            sb.append("            case \"root\":\n");
            sb.append("                if (b != 0) {\n");
            sb.append("                    calcResult = (").append(numberType).append(") Math.pow(a, 1.0 / b);\n");
            sb.append("                } else {\n");
            sb.append("                    Log.e(TAG, \"Root of order zero\");\n");
            sb.append("                }\n");
            sb.append("                break;\n");
        }

        if (RandomUtils.randomBoolean()) {
            sb.append("            case \"modulo\":\n");
            sb.append("                if (b != 0) {\n");
            sb.append("                    calcResult = a % b;\n");
            sb.append("                } else {\n");
            sb.append("                    Log.e(TAG, \"Modulo by zero\");\n");
            sb.append("                }\n");
            sb.append("                break;\n");
            sb.append("            case \"absolute\":\n");
            sb.append("                calcResult = Math.abs(a);\n");
            sb.append("                break;\n");
        }

        sb.append("            default:\n");
        sb.append("                Log.e(TAG, \"Unknown operation: \" + OPERATION_TYPE);\n");
        sb.append("                break;\n");
        sb.append("        }\n");

        // 根据随机条件决定是否使用辅助方法处理结果
        sb.append("        if (RandomUtils.randomBoolean()) {\n");
        sb.append("            calcResult = processResult(calcResult);\n");
        sb.append("        }\n");

        sb.append("        Log.d(TAG, \"").append(operationType).append(" result: \" + calcResult);\n");
        sb.append("        return calcResult;\n");
        sb.append("    }\n\n");

        // 生成数组计算方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(numberType).append(" calculate(").append(numberType).append("[] values) {\n");
            sb.append("        if (values == null || values.length == 0) {\n");
            sb.append("            Log.e(TAG, \"No values provided\");\n");
            sb.append("            return 0;\n");
            sb.append("        }\n");
            sb.append("        ").append(numberType).append(" sum = 0;\n");
            sb.append("        for (").append(numberType).append(" value : values) {\n");
            sb.append("            sum += value;\n");
            sb.append("        }\n");
            sb.append("        Log.d(TAG, \"Sum: \" + sum);\n");
            sb.append("        return sum;\n");
            sb.append("    }\n\n");
        }

        // 随机生成辅助方法
        for (String fieldName : fieldNames) {
            String fieldType = getFieldType(fieldName);

            if (fieldType.equals("int") || fieldType.equals("long") || fieldType.equals("float") || fieldType.equals("double")) {
                sb.append("    private ").append(fieldType).append(" calculate").append(capitalize(fieldName)).append("() {\n");
                sb.append("        ").append(fieldType).append(" result = ").append(fieldName).append(";\n");
                sb.append("        // 随机计算逻辑\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            result += ").append(fieldName).append(";\n");
                sb.append("        } else {\n");
                sb.append("            result *= 2;\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Calculated ").append(fieldName).append(": \" + result);\n");
                sb.append("        return result;\n");
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

        // 生成输入处理方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private ").append(numberType).append(" processInput(").append(numberType).append(" input) {\n");
            sb.append("        // 随机输入处理逻辑\n");
            sb.append("        if (RandomUtils.randomBoolean()) {\n");
            sb.append("            return input + 1;\n");
            sb.append("        } else {\n");
            sb.append("            return input - 1;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成结果处理方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private ").append(numberType).append(" processResult(").append(numberType).append(" result) {\n");
            sb.append("        // 随机结果处理逻辑\n");
            sb.append("        if (RandomUtils.randomBoolean()) {\n");
            sb.append("            return result * 2;\n");
            sb.append("        } else {\n");
            sb.append("            return result / 2;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成统计方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void logStats() {\n");
            sb.append("        Log.d(TAG, \"Calculation stats - Operation: \" + OPERATION_TYPE);\n");
            sb.append("        Log.d(TAG, \"Number type: \" + NUMBER_TYPE);\n");

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

        generateJavaFile(className, sb.toString(), "math.calculation");
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
