package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.*;

/**
 * 升级版工具类代码生成器 - 支持随机功能组合和多样性生成
 */
public class LocalToolGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    // 工具类型集合
    private static final String[] TOOL_TYPES = {
            "calculator", "converter", "timer", "stopwatch", "counter",
            "ruler", "compass", "level", "protractor", "colorpicker",
            "flashlight", "magnifier", "scanner", "barcode", "qr_code",
            "calendar", "clock", "alarm", "world_clock", "weather"
    };

    // 转换类型
    private static final String[] CONVERSION_TYPES = {
            "length", "weight", "temperature", "volume", "area", "speed",
            "time", "pressure", "energy", "power", "force",
            "frequency", "angle", "data", "currency", "density"
    };

    // 操作类型
    private static final String[] OPERATION_TYPES = {
            "calculate", "convert", "measure", "compare", "validate",
            "transform", "process", "analyze", "compute", "evaluate"
    };

    // 数据类型
    private static final String[] DATA_TYPES = {
            "int", "long", "float", "double", "boolean", "String",
            "List<String>", "Map<String, Double>", "int[]", "double[]"
    };

    // 返回类型
    private static final String[] RETURN_TYPES = {
            "int", "long", "float", "double", "boolean", "String",
            "List<String>", "Map<String, Double>", "int[]", "double[]"
    };

    // 单位类型
    private static final String[] UNIT_TYPES = {
            "meter", "kilometer", "centimeter", "millimeter", "inch",
            "foot", "yard", "mile", "gram", "kilogram",
            "pound", "ounce", "celsius", "fahrenheit", "kelvin",
            "liter", "gallon", "pint", "quart", "milliliter"
    };

    public LocalToolGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地工具相关代码 - 升级版");

        // 随机生成5-15个工具类
        int classCount = RandomUtils.between(5, 15);
        for (int i = 0; i < classCount; i++) {
            String className = RandomUtils.generateClassName("Tool");
            String toolType = RandomUtils.randomChoice(TOOL_TYPES);
            generateToolClass(className, toolType);
        }
    }

    /**
     * 生成工具类
     */
    private void generateToolClass(String className, String toolType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 包声明
        sb.append(generatePackageDeclaration("tool"));

        // 导入语句
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Arrays"));
        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成类成员变量
        List<String> fieldNames = generateFields(sb, toolType);

        // 生成构造方法
        generateConstructor(sb, className, fieldNames);

        // 生成核心方法
        List<String> methodNames = new ArrayList<>();

        // 根据工具类型生成不同的方法组合
        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateCalculateMethod(sb, fieldNames, toolType));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateConvertMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateMeasureMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateCompareMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateValidateMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateTransformMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateArrayMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateHelperMethod(sb, fieldNames));
        }

        // 生成调用方法 - 确保所有方法都被调用
        if (!methodNames.isEmpty()) {
            generateCallerMethod(sb, className, fieldNames, methodNames);
        }

        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "tool");
    }

    /**
     * 生成类成员变量
     */
    private List<String> generateFields(StringBuilder sb, String toolType) {
        List<String> fieldNames = new ArrayList<>();

        // 生成随机数量的字段
        int fieldCount = RandomUtils.between(3, 8);

        // 常量字段
        String tagName = RandomUtils.generateWord(6);
        sb.append("    private static final String TAG = \"").append(tagName).append("\";\n");
        fieldNames.add("TAG");

        String toolTypeField = RandomUtils.generateWord(6);
        sb.append("    private static final String TOOL_TYPE = \"").append(toolType).append("\";\n");
        fieldNames.add("TOOL_TYPE");

        // 随机生成其他字段
        for (int i = 0; i < fieldCount; i++) {
            String fieldType = RandomUtils.randomChoice(DATA_TYPES);
            String fieldName = RandomUtils.generateVariableName(fieldType);

            sb.append("    private ").append(fieldType).append(" ").append(fieldName);

            // 随机初始化
            if (RandomUtils.randomBoolean()) {
                if (fieldType.equals("String")) {
                    sb.append(" = \"").append(RandomUtils.generateRandomString(8)).append("\"");
                } else if (fieldType.equals("int")) {
                    sb.append(" = ").append(RandomUtils.between(0, 100));
                } else if (fieldType.equals("long")) {
                    sb.append(" = ").append(RandomUtils.betweenLong(0, 1000));
                } else if (fieldType.equals("float")) {
                    sb.append(" = ").append((float) RandomUtils.nextDouble(0.0, 100.0));
                } else if (fieldType.equals("double")) {
                    sb.append(" = ").append(RandomUtils.nextDouble(0.0, 100.0));
                } else if (fieldType.equals("boolean")) {
                    sb.append(" = ").append(RandomUtils.randomBoolean());
                } else if (fieldType.equals("List<String>")) {
                    sb.append(" = new ArrayList<>()");
                } else if (fieldType.equals("Map<String, Double>")) {
                    sb.append(" = new HashMap<>()");
                } else if (fieldType.equals("int[]")) {
                    sb.append(" = new int[").append(RandomUtils.between(0, 100)).append("]");
                } else if (fieldType.equals("double[]")) {
                    sb.append(" = new double[").append(RandomUtils.nextDouble(0.0, 100.0)).append("]");
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
            if (fieldName.equals("TAG") || fieldName.equals("TOOL_TYPE")) {
                continue;
            }

            // 随机生成初始化代码
            int initType = RandomUtils.between(1, 5);
            switch (initType) {
                case 1:
                    sb.append("        this.").append(fieldName).append(" = ").append(RandomUtils.between(0, 100)).append(";\n");
                    break;
                case 2:
                    sb.append("        this.").append(fieldName).append(" = ").append(RandomUtils.nextDouble(0.0, 100.0)).append(";\n");
                    break;
                case 3:
                    sb.append("        this.").append(fieldName).append(" = \"").append(RandomUtils.generateRandomString(6)).append("\";\n");
                    break;
                case 4:
                    sb.append("        this.").append(fieldName).append(" = ").append(RandomUtils.randomBoolean()).append(";\n");
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
     * 生成计算方法
     */
    private String generateCalculateMethod(StringBuilder sb, List<String> fieldNames, String toolType) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String inputParam1 = RandomUtils.generateVariableName("double");
        String inputParam2 = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("double");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(double ").append(inputParam1).append(", double ").append(inputParam2).append(") {\n");
        sb.append("        double ").append(resultVar).append(" = 0;\n");

        // 根据工具类型生成不同的计算
        if (toolType.equals("calculator") || toolType.equals("converter")) {
            int operation = RandomUtils.between(1, 5);
            switch (operation) {
                case 1:
                    sb.append("        ").append(resultVar).append(" = ").append(inputParam1).append(" + ").append(inputParam2).append(";\n");
                    break;
                case 2:
                    sb.append("        ").append(resultVar).append(" = ").append(inputParam1).append(" - ").append(inputParam2).append(";\n");
                    break;
                case 3:
                    sb.append("        ").append(resultVar).append(" = ").append(inputParam1).append(" * ").append(inputParam2).append(";\n");
                    break;
                case 4:
                    sb.append("        if (").append(inputParam2).append(" != 0) {\n");
                    sb.append("            ").append(resultVar).append(" = ").append(inputParam1).append(" / ").append(inputParam2).append(";\n");
                    sb.append("        }\n");
                    break;
                case 5:
                    sb.append("        ").append(resultVar).append(" = Math.pow(").append(inputParam1).append(", ").append(inputParam2).append(");\n");
                    break;
            }
        } else if (toolType.equals("ruler") || toolType.equals("protractor")) {
            sb.append("        ").append(resultVar).append(" = Math.sqrt(").append(inputParam1).append(" * ").append(inputParam1).append(" + ").append(inputParam2).append(" * ").append(inputParam2).append(");\n");
        } else if (toolType.equals("timer") || toolType.equals("stopwatch")) {
            sb.append("        ").append(resultVar).append(" = Math.abs(").append(inputParam1).append(" - ").append(inputParam2).append(");\n");
        } else {
            sb.append("        ").append(resultVar).append(" = (").append(inputParam1).append(" + ").append(inputParam2).append(") / 2;\n");
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TOOL_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" > 0;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return (long) ").append(resultVar).append(";\n");
        } else if (returnType.equals("float")) {
            sb.append("        return (float) ").append(resultVar).append(";\n");
        } else if (returnType.equals("double")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("String")) {
            sb.append("        return String.valueOf(").append(resultVar).append(");\n");
        } else if (returnType.equals("List<String>")) {
            String listVar = RandomUtils.generateVariableName("List");
            sb.append("        List<String> ").append(listVar).append(" = new ArrayList<>();\n");
            sb.append("        ").append(listVar).append(".add(String.valueOf(").append(resultVar).append("));\n");
            sb.append("        return ").append(listVar).append(";\n");
        } else if (returnType.equals("Map<String, Double>")) {
            String mapVar = RandomUtils.generateVariableName("Map");
            sb.append("        Map<String, Double> ").append(mapVar).append(" = new HashMap<>();\n");
            sb.append("        ").append(mapVar).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        return ").append(mapVar).append(";\n");
        } else if (returnType.equals("int[]")) {
            String arrayVar = RandomUtils.generateVariableName("Array");
            sb.append("        int[] ").append(arrayVar).append(" = new int[]{(int) ").append(resultVar).append("};\n");
            sb.append("        return ").append(arrayVar).append(";\n");
        } else if (returnType.equals("double[]")) {
            String arrayVar = RandomUtils.generateVariableName("Array");
            sb.append("        double[] ").append(arrayVar).append(" = new double[]{").append(resultVar).append("};\n");
            sb.append("        return ").append(arrayVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成转换方法
     */
    private String generateConvertMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String inputParam = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("double");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        // 从CONVERSION_TYPES中随机选择转换类型
        String conversionType = RandomUtils.randomChoice(CONVERSION_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(double ").append(inputParam).append(") {\n");
        sb.append("        double ").append(resultVar).append(" = 0;\n");

        // 根据转换类型生成不同的转换逻辑
        if (conversionType.equals("temperature")) {
            sb.append("        ").append(resultVar).append(" = ").append(inputParam).append(" * 9.0 / 5.0 + 32;\n");
        } else if (conversionType.equals("length")) {
            sb.append("        ").append(resultVar).append(" = ").append(inputParam).append(" * 1000;\n");
        } else if (conversionType.equals("weight")) {
            sb.append("        ").append(resultVar).append(" = ").append(inputParam).append(" * 2.20462;\n");
        } else if (conversionType.equals("volume")) {
            sb.append("        ").append(resultVar).append(" = ").append(inputParam).append(" * 1000;\n");
        } else if (conversionType.equals("area")) {
            sb.append("        ").append(resultVar).append(" = ").append(inputParam).append(" * ").append(inputParam).append(";\n");
        } else if (conversionType.equals("speed")) {
            sb.append("        ").append(resultVar).append(" = ").append(inputParam).append(" * 3.6;\n");
        } else {
            sb.append("        ").append(resultVar).append(" = ").append(inputParam).append(" * 2.0;\n");
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TOOL_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" > 0;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return (long) ").append(resultVar).append(";\n");
        } else if (returnType.equals("float")) {
            sb.append("        return (float) ").append(resultVar).append(";\n");
        } else if (returnType.equals("double")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("String")) {
            sb.append("        return String.valueOf(").append(resultVar).append(");\n");
        } else if (returnType.equals("List<String>")) {
            String listVar = RandomUtils.generateVariableName("List");
            sb.append("        List<String> ").append(listVar).append(" = new ArrayList<>();\n");
            sb.append("        ").append(listVar).append(".add(String.valueOf(").append(resultVar).append("));\n");
            sb.append("        return ").append(listVar).append(";\n");
        } else if (returnType.equals("Map<String, Double>")) {
            String mapVar = RandomUtils.generateVariableName("Map");
            sb.append("        Map<String, Double> ").append(mapVar).append(" = new HashMap<>();\n");
            sb.append("        ").append(mapVar).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        return ").append(mapVar).append(";\n");
        } else if (returnType.equals("int[]")) {
            String arrayVar = RandomUtils.generateVariableName("Array");
            sb.append("        int[] ").append(arrayVar).append(" = new int[]{(int) ").append(resultVar).append("};\n");
            sb.append("        return ").append(arrayVar).append(";\n");
        } else if (returnType.equals("double[]")) {
            String arrayVar = RandomUtils.generateVariableName("Array");
            sb.append("        double[] ").append(arrayVar).append(" = new double[]{").append(resultVar).append("};\n");
            sb.append("        return ").append(arrayVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成测量方法
     */
    private String generateMeasureMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String inputParam1 = RandomUtils.generateVariableName("double");
        String inputParam2 = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("double");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(double ").append(inputParam1).append(", double ").append(inputParam2).append(") {\n");
        sb.append("        double ").append(resultVar).append(" = 0;\n");

        // 生成不同的测量逻辑
        int measureType = RandomUtils.between(1, 5);
        switch (measureType) {
            case 1:
                sb.append("        ").append(resultVar).append(" = Math.sqrt(").append(inputParam1).append(" * ").append(inputParam1).append(" + ").append(inputParam2).append(" * ").append(inputParam2).append(");\n");
                break;
            case 2:
                sb.append("        ").append(resultVar).append(" = Math.abs(").append(inputParam1).append(" - ").append(inputParam2).append(");\n");
                break;
            case 3:
                sb.append("        ").append(resultVar).append(" = Math.max(").append(inputParam1).append(", ").append(inputParam2).append(");\n");
                break;
            case 4:
                sb.append("        ").append(resultVar).append(" = Math.min(").append(inputParam1).append(", ").append(inputParam2).append(");\n");
                break;
            case 5:
                sb.append("        ").append(resultVar).append(" = (").append(inputParam1).append(" + ").append(inputParam2).append(") / 2;\n");
                break;
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TOOL_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" > 0;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return (long) ").append(resultVar).append(";\n");
        } else if (returnType.equals("float")) {
            sb.append("        return (float) ").append(resultVar).append(";\n");
        } else if (returnType.equals("double")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("String")) {
            sb.append("        return String.valueOf(").append(resultVar).append(");\n");
        } else if (returnType.equals("List<String>")) {
            String listVar = RandomUtils.generateVariableName("List");
            sb.append("        List<String> ").append(listVar).append(" = new ArrayList<>();\n");
            sb.append("        ").append(listVar).append(".add(String.valueOf(").append(resultVar).append("));\n");
            sb.append("        return ").append(listVar).append(";\n");
        } else if (returnType.equals("Map<String, Double>")) {
            String mapVar = RandomUtils.generateVariableName("Map");
            sb.append("        Map<String, Double> ").append(mapVar).append(" = new HashMap<>();\n");
            sb.append("        ").append(mapVar).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        return ").append(mapVar).append(";\n");
        } else if (returnType.equals("int[]")) {
            String arrayVar = RandomUtils.generateVariableName("Array");
            sb.append("        int[] ").append(arrayVar).append(" = new int[]{(int) ").append(resultVar).append("};\n");
            sb.append("        return ").append(arrayVar).append(";\n");
        } else if (returnType.equals("double[]")) {
            String arrayVar = RandomUtils.generateVariableName("Array");
            sb.append("        double[] ").append(arrayVar).append(" = new double[]{").append(resultVar).append("};\n");
            sb.append("        return ").append(arrayVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成比较方法
     */
    private String generateCompareMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String inputParam1 = RandomUtils.generateVariableName("double");
        String inputParam2 = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("boolean");

        sb.append("    public boolean ").append(methodName).append("(double ").append(inputParam1).append(", double ").append(inputParam2).append(") {\n");
        sb.append("        boolean ").append(resultVar).append(" = false;\n");

        // 生成不同的比较逻辑
        int compareType = RandomUtils.between(1, 6);
        switch (compareType) {
            case 1:
                sb.append("        ").append(resultVar).append(" = ").append(inputParam1).append(" > ").append(inputParam2).append(";\n");
                break;
            case 2:
                sb.append("        ").append(resultVar).append(" = ").append(inputParam1).append(" < ").append(inputParam2).append(";\n");
                break;
            case 3:
                sb.append("        ").append(resultVar).append(" = ").append(inputParam1).append(" == ").append(inputParam2).append(";\n");
                break;
            case 4:
                sb.append("        ").append(resultVar).append(" = ").append(inputParam1).append(" >= ").append(inputParam2).append(";\n");
                break;
            case 5:
                sb.append("        ").append(resultVar).append(" = ").append(inputParam1).append(" <= ").append(inputParam2).append(";\n");
                break;
            case 6:
                sb.append("        ").append(resultVar).append(" = Math.abs(").append(inputParam1).append(" - ").append(inputParam2).append(") < 0.0001;\n");
                break;
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TOOL_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(" ? 1 : 0;\n");
            }
        }

        sb.append("        return ").append(resultVar).append(";\n");
        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成验证方法
     */
    private String generateValidateMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String inputParam = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("boolean");

        sb.append("    public boolean ").append(methodName).append("(double ").append(inputParam).append(") {\n");
        sb.append("        boolean ").append(resultVar).append(" = false;\n");

        // 生成不同的验证逻辑
        int validateType = RandomUtils.between(1, 4);
        switch (validateType) {
            case 1:
                sb.append("        ").append(resultVar).append(" = ").append(inputParam).append(" >= 0;\n");
                break;
            case 2:
                sb.append("        ").append(resultVar).append(" = ").append(inputParam).append(" > 0 && ").append(inputParam).append(" < 100;\n");
                break;
            case 3:
                sb.append("        ").append(resultVar).append(" = !Double.isNaN(").append(inputParam).append(") && !Double.isInfinite(").append(inputParam).append(");\n");
                break;
            case 4:
                sb.append("        ").append(resultVar).append(" = ").append(inputParam).append(" != 0;\n");
                break;
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TOOL_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(" ? 1 : 0;\n");
            }
        }

        sb.append("        return ").append(resultVar).append(";\n");
        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成转换方法
     */
    private String generateTransformMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String inputParam = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("double");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(double ").append(inputParam).append(") {\n");
        sb.append("        double ").append(resultVar).append(" = 0;\n");

        // 生成不同的转换逻辑
        int transformType = RandomUtils.between(1, 5);
        switch (transformType) {
            case 1:
                sb.append("        ").append(resultVar).append(" = Math.toRadians(").append(inputParam).append(");\n");
                break;
            case 2:
                sb.append("        ").append(resultVar).append(" = Math.toDegrees(").append(inputParam).append(");\n");
                break;
            case 3:
                sb.append("        ").append(resultVar).append(" = Math.abs(").append(inputParam).append(");\n");
                break;
            case 4:
                sb.append("        ").append(resultVar).append(" = Math.round(").append(inputParam).append(" * 100.0) / 100.0;\n");
                break;
            case 5:
                sb.append("        ").append(resultVar).append(" = Math.pow(").append(inputParam).append(", 2);\n");
                break;
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TOOL_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" > 0;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return (long) ").append(resultVar).append(";\n");
        } else if (returnType.equals("float")) {
            sb.append("        return (float) ").append(resultVar).append(";\n");
        } else if (returnType.equals("double")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("String")) {
            sb.append("        return String.valueOf(").append(resultVar).append(");\n");
        } else if (returnType.equals("List<String>")) {
            String listVar = RandomUtils.generateVariableName("List");
            sb.append("        List<String> ").append(listVar).append(" = new ArrayList<>();\n");
            sb.append("        ").append(listVar).append(".add(String.valueOf(").append(resultVar).append("));\n");
            sb.append("        return ").append(listVar).append(";\n");
        } else if (returnType.equals("Map<String, Double>")) {
            String mapVar = RandomUtils.generateVariableName("Map");
            sb.append("        Map<String, Double> ").append(mapVar).append(" = new HashMap<>();\n");
            sb.append("        ").append(mapVar).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        return ").append(mapVar).append(";\n");
        } else if (returnType.equals("int[]")) {
            String arrayVar = RandomUtils.generateVariableName("Array");
            sb.append("        int[] ").append(arrayVar).append(" = new int[]{(int) ").append(resultVar).append("};\n");
            sb.append("        return ").append(arrayVar).append(";\n");
        } else if (returnType.equals("double[]")) {
            String arrayVar = RandomUtils.generateVariableName("Array");
            sb.append("        double[] ").append(arrayVar).append(" = new double[]{").append(resultVar).append("};\n");
            sb.append("        return ").append(arrayVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成数组处理方法
     */
    private String generateArrayMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String inputParam = RandomUtils.generateVariableName("double[]");
        String resultVar = RandomUtils.generateVariableName("double");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(double[] ").append(inputParam).append(") {\n");
        sb.append("        if (").append(inputParam).append(" == null || ").append(inputParam).append(".length == 0) {\n");
        String defaultValue = "0";
        if (returnType.equals("boolean")) defaultValue = "false";
        else if (returnType.equals("String") || returnType.equals("List<String>") || returnType.equals("Map<String, Double>") || returnType.equals("int[]") || returnType.equals("double[]")) defaultValue = "null";
        sb.append("            return ").append(defaultValue).append(";\n");
        sb.append("        }\n");
        sb.append("        double ").append(resultVar).append(" = 0;\n");

        // 生成不同的数组处理逻辑
        int arrayType = RandomUtils.between(1, 5);
        switch (arrayType) {
            case 1:
                sb.append("        for (double value : ").append(inputParam).append(") {\n");
                sb.append("            ").append(resultVar).append(" += value;\n");
                sb.append("        }\n");
                sb.append("        ").append(resultVar).append(" /= ").append(inputParam).append(".length;\n");
                break;
            case 2:
                sb.append("        ").append(resultVar).append(" = ").append(inputParam).append("[0];\n");
                sb.append("        for (double value : ").append(inputParam).append(") {\n");
                sb.append("            if (value > ").append(resultVar).append(") {\n");
                sb.append("                ").append(resultVar).append(" = value;\n");
                sb.append("            }\n");
                sb.append("        }\n");
                break;
            case 3:
                sb.append("        ").append(resultVar).append(" = ").append(inputParam).append("[0];\n");
                sb.append("        for (double value : ").append(inputParam).append(") {\n");
                sb.append("            if (value < ").append(resultVar).append(") {\n");
                sb.append("                ").append(resultVar).append(" = value;\n");
                sb.append("            }\n");
                sb.append("        }\n");
                break;
            case 4:
                sb.append("        for (double value : ").append(inputParam).append(") {\n");
                sb.append("            ").append(resultVar).append(" += value * value;\n");
                sb.append("        }\n");
                sb.append("        ").append(resultVar).append(" = Math.sqrt(").append(resultVar).append(");\n");
                break;
            case 5:
                sb.append("        for (int i = 0; i < ").append(inputParam).append(".length; i++) {\n");
                sb.append("            ").append(resultVar).append(" += ").append(inputParam).append("[i] * (i + 1);\n");
                sb.append("        }\n");
                break;
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TOOL_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" > 0;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return (long) ").append(resultVar).append(";\n");
        } else if (returnType.equals("float")) {
            sb.append("        return (float) ").append(resultVar).append(";\n");
        } else if (returnType.equals("double")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("String")) {
            sb.append("        return String.valueOf(").append(resultVar).append(");\n");
        } else if (returnType.equals("List<String>")) {
            String listVar = RandomUtils.generateVariableName("List");
            sb.append("        List<String> ").append(listVar).append(" = new ArrayList<>();\n");
            sb.append("        ").append(listVar).append(".add(String.valueOf(").append(resultVar).append("));\n");
            sb.append("        return ").append(listVar).append(";\n");
        } else if (returnType.equals("Map<String, Double>")) {
            String mapVar = RandomUtils.generateVariableName("Map");
            sb.append("        Map<String, Double> ").append(mapVar).append(" = new HashMap<>();\n");
            sb.append("        ").append(mapVar).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        return ").append(mapVar).append(";\n");
        } else if (returnType.equals("int[]")) {
            String arrayVar = RandomUtils.generateVariableName("Array");
            sb.append("        int[] ").append(arrayVar).append(" = new int[]{(int) ").append(resultVar).append("};\n");
            sb.append("        return ").append(arrayVar).append(";\n");
        } else if (returnType.equals("double[]")) {
            String arrayVar = RandomUtils.generateVariableName("Array");
            sb.append("        double[] ").append(arrayVar).append(" = new double[]{").append(resultVar).append("};\n");
            sb.append("        return ").append(arrayVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成辅助方法
     */
    private String generateHelperMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String inputParam = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("double");

        sb.append("    private double ").append(methodName).append("(double ").append(inputParam).append(") {\n");
        sb.append("        double ").append(resultVar).append(" = 0;\n");

        // 生成不同的辅助逻辑
        int helperType = RandomUtils.between(1, 5);
        switch (helperType) {
            case 1:
                sb.append("        ").append(resultVar).append(" = Math.abs(").append(inputParam).append(");\n");
                break;
            case 2:
                sb.append("        ").append(resultVar).append(" = Math.round(").append(inputParam).append(" * 100.0) / 100.0;\n");
                break;
            case 3:
                sb.append("        ").append(resultVar).append(" = Math.max(0, ").append(inputParam).append(");\n");
                break;
            case 4:
                sb.append("        ").append(resultVar).append(" = Math.min(100, ").append(inputParam).append(");\n");
                break;
            case 5:
                sb.append("        ").append(resultVar).append(" = ").append(inputParam).append(" * ").append(inputParam).append(";\n");
                break;
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TOOL_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        sb.append("        return ").append(resultVar).append(";\n");
        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成调用方法 - 确保所有方法都被调用
     */
    private void generateCallerMethod(StringBuilder sb, String className, List<String> fieldNames, List<String> methodNames) {
        String methodName = RandomUtils.generateMethodName("process");
        String inputVar1 = RandomUtils.generateVariableName("double");
        String inputVar2 = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("double");
        String arrayVar = RandomUtils.generateVariableName("double[]");

        sb.append("    public double ").append(methodName).append("(double ").append(inputVar1).append(", double ").append(inputVar2).append(") {\n");
        sb.append("        double ").append(resultVar).append(" = 0;\n");
        sb.append("        double[] ").append(arrayVar).append(" = {").append(inputVar1).append(", ").append(inputVar2).append("};\n");

        // 调用所有方法
        for (String name : methodNames) {
            if (name.contains("calculate")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(").append(inputVar1).append(", ").append(inputVar2).append(");\n");
            } else if (name.contains("convert")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(").append(inputVar1).append(");\n");
            } else if (name.contains("measure")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(").append(inputVar1).append(", ").append(inputVar2).append(");\n");
            } else if (name.contains("compare")) {
                sb.append("        boolean isGreater = ").append(name).append("(").append(inputVar1).append(", ").append(inputVar2).append(");\n");
            } else if (name.contains("validate")) {
                sb.append("        boolean isValid = ").append(name).append("(").append(inputVar1).append(");\n");
            } else if (name.contains("transform")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(").append(inputVar1).append(");\n");
            } else if (name.contains("array")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(").append(arrayVar).append(");\n");
            } else if (name.contains("helper")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(").append(inputVar1).append(");\n");
            }
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TOOL_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        System.out.println(TAG + \" processed: \" + ").append(resultVar).append(");\n");
        }

        sb.append("        return ").append(resultVar).append(";\n");
        sb.append("    }\n\n");
    }
}