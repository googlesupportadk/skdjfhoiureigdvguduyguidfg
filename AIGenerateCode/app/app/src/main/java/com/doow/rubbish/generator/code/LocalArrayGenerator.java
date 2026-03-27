package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalArrayGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] ARRAY_TYPES = {
            "String[]", "int[]", "double[]", "boolean[]", "Object[]",
            "long[]", "float[]", "byte[]", "short[]", "char[]",
            "Integer[]", "Long[]", "Float[]", "Double[]", "Boolean[]",
            "Byte[]", "Short[]", "Character[]", "String[][]", "int[][]"
    };

    private static final String[] OPERATION_TYPES = {
            "sort", "reverse", "shuffle", "filter", "transform",
            "binary_search", "fill", "copy_of", "copy_of_range", "as_list",
            "stream", "parallel_stream", "for_each", "for_each_parallel", "set_all",
            "equals", "deep_equals", "hash_code", "deep_hash_code", "to_string",
            "to_list", "to_set", "to_map", "to_stream"
    };

    private static final String[] FIELD_TYPES = {
            "int", "long", "float", "double", "boolean", "String", "Object"
    };

    public LocalArrayGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成数组操作类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Array");
            generateArrayClass(className, asyncHandler);
        }
    }

    private void generateArrayClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("collection.array"));

        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.Arrays"));
        sb.append(generateImportStatement("java.util.Random"));

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        sb.append("public class ").append(className).append(" {\n\n");

        // 随机选择数组类型
        String arrayType = ARRAY_TYPES[RandomUtils.between(0, ARRAY_TYPES.length - 1)];

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

        // 添加数组字段
        sb.append("    private ").append(arrayType).append(" array;\n");
        sb.append("    private Random random;\n\n");

        // 构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this.random = new Random();\n");
        sb.append("        this.array = new ").append(arrayType).append("[10];\n");

        // 初始化非静态字段
        for (String fieldName : fieldNames) {
            if (!isStaticField(fieldName)) {
                String fieldType = getFieldType(fieldName);
                sb.append("        this.").append(fieldName).append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            }
        }
        sb.append("    }\n\n");

        // 生成主方法
        String mainMethodName = RandomUtils.generateMethodName("process");
        sb.append("    public void ").append(mainMethodName).append("(").append(arrayType).append(" input) {\n");
        sb.append("        if (input == null || input.length == 0) {\n");
        sb.append("            Log.e(TAG, \"Input array is null or empty\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        this.array = input.clone();\n");

        // 随机调用一些辅助方法
        int methodCallCount = RandomUtils.between(2, 5);
        for (int i = 0; i < methodCallCount; i++) {
            int methodIndex = RandomUtils.between(0, fieldNames.size() - 1);
            String fieldName = fieldNames.get(methodIndex);
            String fieldType = getFieldType(fieldName);

            // 根据字段类型生成不同的方法调用
            if (fieldType.equals("int") || fieldType.equals("long") || fieldType.equals("float") || fieldType.equals("double")) {
                sb.append("        calculate").append(capitalize(fieldName)).append("();\n");
            } else if (fieldType.equals("boolean")) {
                sb.append("        validate").append(capitalize(fieldName)).append("();\n");
            } else {
                sb.append("        process").append(capitalize(fieldName)).append("();\n");
            }
        }

        sb.append("    }\n\n");

        // 生成辅助方法
        for (String fieldName : fieldNames) {
            String fieldType = getFieldType(fieldName);

            if (fieldType.equals("int") || fieldType.equals("long") || fieldType.equals("float") || fieldType.equals("double")) {
                sb.append("    private ").append(fieldType).append(" calculate").append(capitalize(fieldName)).append("() {\n");
                sb.append("        ").append(fieldType).append(" result = ").append(fieldName).append(";\n");
                sb.append("        // 随机计算逻辑\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            result += random.").append(getRandomMethod(fieldType)).append("();\n");
                sb.append("        } else {\n");
                sb.append("            result *= random.").append(getRandomMethod(fieldType)).append("();\n");
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
                sb.append("            ").append(fieldName).append(" = String.valueOf(random.nextInt(100));\n");
                sb.append("        } else {\n");
                sb.append("            ").append(fieldName).append(" = \"processed_\"+").append(fieldName).append(";\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Processed ").append(fieldName).append(": \" + ").append(fieldName).append(");\n");
                sb.append("    }\n\n");
            }
        }

        // 生成数组操作方法
        String operationType = OPERATION_TYPES[RandomUtils.between(0, OPERATION_TYPES.length - 1)];
        sb.append("    private void perform").append(capitalize(operationType)).append("() {\n");
        sb.append("        // 随机数组操作\n");
        sb.append("        switch (\"").append(operationType).append("\") {\n");

        // 添加几个常见的数组操作
        sb.append("            case \"sort\":\n");
        sb.append("                Arrays.sort(array);\n");
        sb.append("                break;\n");
        sb.append("            case \"reverse\":\n");
        sb.append("                reverseArray();\n");
        sb.append("                break;\n");
        sb.append("            case \"shuffle\":\n");
        sb.append("                shuffleArray();\n");
        sb.append("                break;\n");
        sb.append("            case \"fill\":\n");
        sb.append("                fillArray();\n");
        sb.append("                break;\n");
        sb.append("            default:\n");
        sb.append("                Log.d(TAG, \"Unknown operation: \" + \"").append(operationType).append("\");\n");
        sb.append("                break;\n");
        sb.append("        }\n");
        sb.append("        Log.d(TAG, \"Array ").append(operationType).append(": \" + Arrays.toString(array));\n");
        sb.append("    }\n\n");

        // 生成数组辅助方法
        sb.append("    private void reverseArray() {\n");
        sb.append("        for (int i = 0; i < array.length / 2; i++) {\n");
        sb.append("            swap(array, i, array.length - 1 - i);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    private void shuffleArray() {\n");
        sb.append("        for (int i = array.length - 1; i > 0; i--) {\n");
        sb.append("            int index = random.nextInt(i + 1);\n");
        sb.append("            swap(array, i, index);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    private void fillArray() {\n");
        sb.append("        for (int i = 0; i < array.length; i++) {\n");
        sb.append("            array[i] = generateRandomElement();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    private void swap(").append(arrayType).append(" arr, int i, int j) {\n");
        sb.append("        ").append(arrayType.replace("[]", "")).append(" temp = arr[i];\n");
        sb.append("        arr[i] = arr[j];\n");
        sb.append("        arr[j] = temp;\n");
        sb.append("    }\n\n");

        sb.append("    private ").append(arrayType.replace("[]", "")).append(" generateRandomElement() {\n");
        sb.append("        // 随机元素生成逻辑\n");
        sb.append("        if (RandomUtils.randomBoolean()) {\n");
        sb.append("            return (").append(arrayType.replace("[]", "")).append(") random.nextInt(100);\n");
        sb.append("        } else {\n");
        sb.append("            return (").append(arrayType.replace("[]", "")).append(") (random.nextDouble() * 100);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "collection.array");
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
        } else if (fieldName.endsWith("string")) {
            return "String";
        } else {
            return "Object";
        }
    }

    // 辅助方法：获取随机方法
    private String getRandomMethod(String type) {
        switch (type) {
            case "int":
                return "nextInt(100)";
            case "long":
                return "nextLong()";
            case "float":
                return "nextFloat()";
            case "double":
                return "nextDouble()";
            default:
                return "nextInt(100)";
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
