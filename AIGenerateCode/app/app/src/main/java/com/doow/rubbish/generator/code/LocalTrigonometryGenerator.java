
package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.*;

/**
 * 升级版三角函数代码生成器 - 支持随机功能组合和多样性生成
 */
public class LocalTrigonometryGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    // 三角函数类型
    private static final String[] TRIGONOMETRY_TYPES = {
            "sine", "cosine", "tangent", "cotangent", "secant",
            "cosecant", "arcsine", "arccosine", "arctangent", "arccotangent",
            "versine", "coversine", "haversine", "exsecant", "excosecant"
    };

    // 双曲函数类型
    private static final String[] HYPERBOLIC_TYPES = {
            "sinh", "cosh", "tanh", "coth", "sech",
            "csch", "arsinh", "arcosh", "artanh", "arcoth",
            "versinh", "coverh", "haversinh", "exsech", "excosech"
    };

    // 操作类型
    private static final String[] OPERATION_TYPES = {
            "calculate", "evaluate", "simplify", "transform", "convert",
            "differentiate", "integrate", "series", "identity", "inverse",
            "compose", "decompose", "factor", "expand", "simplify_fraction"
    };

    // 返回类型
    private static final String[] RETURN_TYPES = {
            "double", "float", "Double", "Float", "int", "long"
    };

    // 数据类型
    private static final String[] DATA_TYPES = {
            "double", "float", "Double", "Float", "int", "long", "Double[]", "float[]"
    };

    public LocalTrigonometryGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地三角函数相关代码 - 升级版");

        // 随机生成5-15个三角函数处理类
        int classCount = RandomUtils.between(5, 15);
        for (int i = 0; i < classCount; i++) {
            String className = RandomUtils.generateClassName("Trigonometry");
            String trigType = RandomUtils.randomChoice(TRIGONOMETRY_TYPES);
            generateTrigonometryClass(className, trigType);
        }
    }

    /**
     * 生成三角函数处理类
     */
    private void generateTrigonometryClass(String className, String trigType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 包声明
        sb.append(generatePackageDeclaration("trigonometry"));

        // 导入语句
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.Arrays"));
        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成类成员变量
        List<String> fieldNames = generateFields(sb, trigType);

        // 生成构造方法
        generateConstructor(sb, className, fieldNames);

        // 生成核心方法
        List<String> methodNames = new ArrayList<>();

        // 根据三角函数类型生成不同的方法组合
        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateBasicTrigMethod(sb, fieldNames, trigType));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateHyperbolicMethod(sb, fieldNames, trigType));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateInverseMethod(sb, fieldNames, trigType));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateOperationMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateArrayMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateHelperMethod(sb, fieldNames));
        }

        // 生成调用方法 - 确保所有方法都被调用
        if (methodNames.size() > 0) {
            generateCallerMethod(sb, className, fieldNames, methodNames);
        }

        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "trigonometry");
    }

    /**
     * 生成类成员变量
     */
    private List<String> generateFields(StringBuilder sb, String trigType) {
        List<String> fieldNames = new ArrayList<>();

        // 生成随机数量的字段
        int fieldCount = RandomUtils.between(3, 8);

        // 常量字段
        String tagName = RandomUtils.generateWord(6);
        sb.append("    private static final String TAG = \"" + tagName + "\";\n");
        fieldNames.add("TAG");

        String trigTypeField = RandomUtils.generateWord(6);
        sb.append("    private static final String TRIG_TYPE = \"" + trigType + "\";\n");
        fieldNames.add("TRIG_TYPE");

        // 随机生成其他字段
        for (int i = 0; i < fieldCount; i++) {
            String fieldType = RandomUtils.randomChoice(DATA_TYPES);
            String fieldName = RandomUtils.generateVariableName(fieldType);

            sb.append("    private ").append(fieldType).append(" ").append(fieldName);

            // 随机初始化
            if (RandomUtils.randomBoolean()) {
                if (fieldType.equals("double") || fieldType.equals("Double")) {
                    sb.append(" = ").append(RandomUtils.nextDouble(0.0, 100.0));
                } else if (fieldType.equals("float") || fieldType.equals("Float")) {
                    sb.append(" = ").append((float) RandomUtils.nextDouble(0.0, 100.0));
                } else if (fieldType.equals("int")) {
                    sb.append(" = ").append(RandomUtils.between(0, 100));
                } else if (fieldType.equals("long")) {
                    sb.append(" = ").append(RandomUtils.betweenLong(0, 1000));
                } else if (fieldType.equals("double[]")) {
                    sb.append(" = new double[]{").append(RandomUtils.nextDouble(0.0, 100.0)).append(", ").append(RandomUtils.nextDouble(0.0, 100.0)).append("}");
                } else if (fieldType.equals("float[]")) {
                    sb.append(" = new float[]{").append((float) RandomUtils.nextDouble(0.0, 100.0)).append(", ").append((float) RandomUtils.nextDouble(0.0, 100.0)).append("}");
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
            if (fieldName.equals("TAG") || fieldName.equals("TRIG_TYPE")) {
                continue;
            }

            // 随机生成初始化代码
            int initType = RandomUtils.between(1, 4);
            switch (initType) {
                case 1:
                    sb.append("        this.").append(fieldName).append(" = ").append(RandomUtils.nextDouble(0.0, 100.0)).append(";\n");
                    break;
                case 2:
                    sb.append("        this.").append(fieldName).append(" = ").append(RandomUtils.between(0, 100)).append(";\n");
                    break;
                case 3:
                    sb.append("        this.").append(fieldName).append(" = ").append(RandomUtils.randomBoolean()).append(";\n");
                    break;
                case 4:
                    sb.append("        this.").append(fieldName).append(" = new double[]{").append(RandomUtils.nextDouble(0.0, 100.0)).append(", ").append(RandomUtils.nextDouble(0.0, 100.0)).append("};\n");
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
     * 生成基本三角函数方法
     */
    private String generateBasicTrigMethod(StringBuilder sb, List<String> fieldNames, String trigType) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String inputParam = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("double");
        String radiansVar = RandomUtils.generateVariableName("double");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(double ").append(inputParam).append(") {\n");
        sb.append("        double ").append(radiansVar).append(" = Math.toRadians(").append(inputParam).append(");\n");
        sb.append("        double ").append(resultVar).append(" = 0;\n");
        sb.append("        switch (TRIG_TYPE) {\n");

        // 使用TRIGONOMETRY_TYPES数组生成switch case
        for (String trig : TRIGONOMETRY_TYPES) {
            sb.append("            case \"").append(trig).append("\":\n");

            // 根据三角函数类型生成不同的计算
            if (trig.equals("sine")) {
                sb.append("                ").append(resultVar).append(" = Math.sin(").append(radiansVar).append(");\n");
            } else if (trig.equals("cosine")) {
                sb.append("                ").append(resultVar).append(" = Math.cos(").append(radiansVar).append(");\n");
            } else if (trig.equals("tangent")) {
                sb.append("                ").append(resultVar).append(" = Math.tan(").append(radiansVar).append(");\n");
            } else if (trig.equals("cotangent")) {
                sb.append("                ").append(resultVar).append(" = 1.0 / Math.tan(").append(radiansVar).append(");\n");
            } else if (trig.equals("secant")) {
                sb.append("                ").append(resultVar).append(" = 1.0 / Math.cos(").append(radiansVar).append(");\n");
            } else if (trig.equals("cosecant")) {
                sb.append("                ").append(resultVar).append(" = 1.0 / Math.sin(").append(radiansVar).append(");\n");
            } else if (trig.equals("arcsine")) {
                sb.append("                ").append(resultVar).append(" = Math.asin(").append(radiansVar).append(");\n");
            } else if (trig.equals("arccosine")) {
                sb.append("                ").append(resultVar).append(" = Math.acos(").append(radiansVar).append(");\n");
            } else if (trig.equals("arctangent")) {
                sb.append("                ").append(resultVar).append(" = Math.atan(").append(radiansVar).append(");\n");
            } else if (trig.equals("arccotangent")) {
                sb.append("                ").append(resultVar).append(" = Math.atan(1.0 / ").append(radiansVar).append(");\n");
            } else if (trig.equals("versine")) {
                sb.append("                ").append(resultVar).append(" = 1 - Math.cos(").append(radiansVar).append(");\n");
            } else if (trig.equals("coversine")) {
                sb.append("                ").append(resultVar).append(" = 1 - Math.sin(").append(radiansVar).append(");\n");
            } else if (trig.equals("haversine")) {
                sb.append("                ").append(resultVar).append(" = (1 - Math.cos(").append(radiansVar).append(")) / 2;\n");
            } else if (trig.equals("exsecant")) {
                sb.append("                ").append(resultVar).append(" = 1.0 / Math.cos(").append(radiansVar).append(") - 1;\n");
            } else if (trig.equals("excosecant")) {
                sb.append("                ").append(resultVar).append(" = 1.0 / Math.sin(").append(radiansVar).append(") - 1;\n");
            }

            sb.append("                break;\n");
        }

        sb.append("            default:\n");
        sb.append("                ").append(resultVar).append(" = 0;\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TRIG_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("double") || returnType.equals("Double")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("float") || returnType.equals("Float")) {
            sb.append("        return (float) ").append(resultVar).append(";\n");
        } else if (returnType.equals("int")) {
            sb.append("        return (int) ").append(resultVar).append(";\n");
        } else if (returnType.equals("long")) {
            sb.append("        return (long) ").append(resultVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成双曲函数方法
     */
    private String generateHyperbolicMethod(StringBuilder sb, List<String> fieldNames, String trigType) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String inputParam = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("double");
        String radiansVar = RandomUtils.generateVariableName("double");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(double ").append(inputParam).append(") {\n");
        sb.append("        double ").append(radiansVar).append(" = Math.toRadians(").append(inputParam).append(");\n");
        sb.append("        double ").append(resultVar).append(" = 0;\n");
        sb.append("        switch (TRIG_TYPE) {\n");

        // 使用HYPERBOLIC_TYPES数组生成switch case
        for (String hyperbolic : HYPERBOLIC_TYPES) {
            sb.append("            case \"").append(hyperbolic).append("\":\n");

            // 根据双曲函数类型生成不同的计算
            if (hyperbolic.equals("sinh")) {
                sb.append("                ").append(resultVar).append(" = Math.sinh(").append(radiansVar).append(");\n");
            } else if (hyperbolic.equals("cosh")) {
                sb.append("                ").append(resultVar).append(" = Math.cosh(").append(radiansVar).append(");\n");
            } else if (hyperbolic.equals("tanh")) {
                sb.append("                ").append(resultVar).append(" = Math.tanh(").append(radiansVar).append(");\n");
            } else if (hyperbolic.equals("coth")) {
                sb.append("                ").append(resultVar).append(" = Math.cosh(").append(radiansVar).append(") / Math.sinh(").append(radiansVar).append(");\n");
            } else if (hyperbolic.equals("sech")) {
                sb.append("                ").append(resultVar).append(" = 1.0 / Math.cosh(").append(radiansVar).append(");\n");
            } else if (hyperbolic.equals("csch")) {
                sb.append("                ").append(resultVar).append(" = 1.0 / Math.sinh(").append(radiansVar).append(");\n");
            } else if (hyperbolic.equals("arsinh")) {
                sb.append("                ").append(resultVar).append(" = Math.log(").append(radiansVar).append(" + Math.sqrt(").append(radiansVar).append(" * ").append(radiansVar).append(" + 1));\n");
            } else if (hyperbolic.equals("arcosh")) {
                sb.append("                ").append(resultVar).append(" = Math.log(").append(radiansVar).append(" + Math.sqrt(").append(radiansVar).append(" * ").append(radiansVar).append(" - 1));\n");
            } else if (hyperbolic.equals("artanh")) {
                sb.append("                ").append(resultVar).append(" = 0.5 * Math.log((1 + ").append(radiansVar).append(") / (1 - ").append(radiansVar).append("));\n");
            } else if (hyperbolic.equals("arcoth")) {
                sb.append("                ").append(resultVar).append(" = 0.5 * Math.log((").append(radiansVar).append(" + 1) / (").append(radiansVar).append(" - 1));\n");
            } else if (hyperbolic.equals("versinh")) {
                sb.append("                ").append(resultVar).append(" = 1 - Math.cosh(").append(radiansVar).append(");\n");
            } else if (hyperbolic.equals("coverh")) {
                sb.append("                ").append(resultVar).append(" = 1 - Math.sinh(").append(radiansVar).append(");\n");
            } else if (hyperbolic.equals("haversinh")) {
                sb.append("                ").append(resultVar).append(" = (1 - Math.cosh(").append(radiansVar).append(")) / 2;\n");
            } else if (hyperbolic.equals("exsech")) {
                sb.append("                ").append(resultVar).append(" = 1.0 / Math.cosh(").append(radiansVar).append(") - 1;\n");
            } else if (hyperbolic.equals("excosech")) {
                sb.append("                ").append(resultVar).append(" = 1.0 / Math.sinh(").append(radiansVar).append(") - 1;\n");
            }

            sb.append("                break;\n");
        }

        sb.append("            default:\n");
        sb.append("                ").append(resultVar).append(" = 0;\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TRIG_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("double") || returnType.equals("Double")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("float") || returnType.equals("Float")) {
            sb.append("        return (float) ").append(resultVar).append(";\n");
        } else if (returnType.equals("int")) {
            sb.append("        return (int) ").append(resultVar).append(";\n");
        } else if (returnType.equals("long")) {
            sb.append("        return (long) ").append(resultVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成反三角函数方法
     */
    private String generateInverseMethod(StringBuilder sb, List<String> fieldNames, String trigType) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String inputParam = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("double");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(double ").append(inputParam).append(") {\n");
        sb.append("        if (").append(inputParam).append(" < -1 || ").append(inputParam).append(" > 1) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        double ").append(resultVar).append(" = 0;\n");
        sb.append("        switch (TRIG_TYPE) {\n");

        // 使用TRIGONOMETRY_TYPES数组生成switch case
        for (String trig : TRIGONOMETRY_TYPES) {
            sb.append("            case \"").append(trig).append("\":\n");

            // 根据三角函数类型生成不同的反函数计算
            if (trig.equals("sine")) {
                sb.append("                ").append(resultVar).append(" = Math.asin(").append(inputParam).append(");\n");
            } else if (trig.equals("cosine")) {
                sb.append("                ").append(resultVar).append(" = Math.acos(").append(inputParam).append(");\n");
            } else if (trig.equals("tangent")) {
                sb.append("                ").append(resultVar).append(" = Math.atan(").append(inputParam).append(");\n");
            } else if (trig.equals("cotangent")) {
                sb.append("                ").append(resultVar).append(" = Math.atan(1.0 / ").append(inputParam).append(");\n");
            } else if (trig.equals("secant")) {
                sb.append("                ").append(resultVar).append(" = Math.acos(1.0 / ").append(inputParam).append(");\n");
            } else if (trig.equals("cosecant")) {
                sb.append("                ").append(resultVar).append(" = Math.asin(1.0 / ").append(inputParam).append(");\n");
            } else if (trig.equals("arcsine")) {
                sb.append("                ").append(resultVar).append(" = Math.sin(").append(inputParam).append(");\n");
            } else if (trig.equals("arccosine")) {
                sb.append("                ").append(resultVar).append(" = Math.cos(").append(inputParam).append(");\n");
            } else if (trig.equals("arctangent")) {
                sb.append("                ").append(resultVar).append(" = Math.tan(").append(inputParam).append(");\n");
            } else if (trig.equals("arccotangent")) {
                sb.append("                ").append(resultVar).append(" = 1.0 / Math.tan(").append(inputParam).append(");\n");
            } else if (trig.equals("versine")) {
                sb.append("                ").append(resultVar).append(" = Math.acos(1 - ").append(inputParam).append(");\n");
            } else if (trig.equals("coversine")) {
                sb.append("                ").append(resultVar).append(" = Math.asin(1 - ").append(inputParam).append(");\n");
            } else if (trig.equals("haversine")) {
                sb.append("                ").append(resultVar).append(" = Math.acos(1 - 2 * ").append(inputParam).append(");\n");
            } else if (trig.equals("exsecant")) {
                sb.append("                ").append(resultVar).append(" = Math.acos(1.0 / (1 + ").append(inputParam).append("));\n");
            } else if (trig.equals("excosecant")) {
                sb.append("                ").append(resultVar).append(" = Math.asin(1.0 / (1 + ").append(inputParam).append("));\n");
            }

            sb.append("                break;\n");
        }

        sb.append("            default:\n");
        sb.append("                ").append(resultVar).append(" = 0;\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TRIG_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("double") || returnType.equals("Double")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("float") || returnType.equals("Float")) {
            sb.append("        return (float) ").append(resultVar).append(";\n");
        } else if (returnType.equals("int")) {
            sb.append("        return (int) ").append(resultVar).append(";\n");
        } else if (returnType.equals("long")) {
            sb.append("        return (long) ").append(resultVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成操作方法
     */
    private String generateOperationMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String param1 = RandomUtils.generateVariableName("double");
        String param2 = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("double");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(double ").append(param1).append(", double ").append(param2).append(") {\n");
        sb.append("        double ").append(resultVar).append(" = 0;\n");

        // 随机选择不同的操作
        int operation = RandomUtils.between(1, 6);
        switch (operation) {
            case 1:
                sb.append("        ").append(resultVar).append(" = Math.").append(param1).append(" + Math.").append(param2).append(";\n");
                break;
            case 2:
                sb.append("        ").append(resultVar).append(" = Math.").append(param1).append(" - Math.").append(param2).append(";\n");
                break;
            case 3:
                sb.append("        ").append(resultVar).append(" = Math.").append(param1).append(" * Math.").append(param2).append(";\n");
                break;
            case 4:
                sb.append("        ").append(resultVar).append(" = Math.").append(param1).append(" / Math.").append(param2).append(";\n");
                break;
            case 5:
                sb.append("        double ").append(RandomUtils.generateVariableName("double")).append(" = Math.sqrt(").append(param1).append(" * ").append(param1).append(" + ").append(param2).append(" * ").append(param2).append(");\n");
                sb.append("        ").append(resultVar).append(" = Math.atan2(").append(param1).append(", ").append(param2).append(");\n");
                break;
            case 6:
                sb.append("        ").append(resultVar).append(" = Math.pow(").append(param1).append(", ").append(param2).append(");\n");
                break;
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TRIG_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("double") || returnType.equals("Double")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("float") || returnType.equals("Float")) {
            sb.append("        return (float) ").append(resultVar).append(";\n");
        } else if (returnType.equals("int")) {
            sb.append("        return (int) ").append(resultVar).append(";\n");
        } else if (returnType.equals("long")) {
            sb.append("        return (long) ").append(resultVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成数组方法
     */
    private String generateArrayMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String inputParam = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("double");
        String arrayVar = RandomUtils.generateVariableName("double[]");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(double[] ").append(inputParam).append(") {\n");
        sb.append("        if (").append(inputParam).append(" == null || ").append(inputParam).append(".length == 0) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        double[] ").append(arrayVar).append(" = new double[").append(inputParam).append(".length];\n");
        sb.append("        double ").append(resultVar).append(" = 0;\n");
        sb.append("        for (int i = 0; i < ").append(inputParam).append(".length; i++) {\n");

        // 随机选择不同的操作
        int operation = RandomUtils.between(1, 4);
        switch (operation) {
            case 1:
                sb.append("            ").append(arrayVar).append("[i] = Math.sin(").append(inputParam).append("[i]);\n");
                sb.append("            ").append(resultVar).append(" += ").append(arrayVar).append("[i];\n");
                break;
            case 2:
                sb.append("            ").append(arrayVar).append("[i] = Math.cos(").append(inputParam).append("[i]);\n");
                sb.append("            ").append(resultVar).append(" += ").append(arrayVar).append("[i];\n");
                break;
            case 3:
                sb.append("            ").append(arrayVar).append("[i] = Math.tan(").append(inputParam).append("[i]);\n");
                sb.append("            ").append(resultVar).append(" += ").append(arrayVar).append("[i];\n");
                break;
            case 4:
                sb.append("            ").append(arrayVar).append("[i] = Math.sqrt(").append(inputParam).append("[i] * ").append(inputParam).append("[i]);\n");
                sb.append("            ").append(resultVar).append(" += ").append(arrayVar).append("[i];\n");
                break;
        }

        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TRIG_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("double") || returnType.equals("Double")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("float") || returnType.equals("Float")) {
            sb.append("        return (float) ").append(resultVar).append(";\n");
        } else if (returnType.equals("int")) {
            sb.append("        return (int) ").append(resultVar).append(";\n");
        } else if (returnType.equals("long")) {
            sb.append("        return (long) ").append(resultVar).append(";\n");
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
        String resultVar = RandomUtils.generateVariableName("boolean");

        sb.append("    private boolean ").append(methodName).append("(double ").append(inputParam).append(") {\n");
        sb.append("        boolean ").append(resultVar).append(" = ").append(inputParam).append(" >= 0 && ").append(inputParam).append(" <= 360;\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TRIG_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(inputParam).append(";\n");
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
        String inputVar = RandomUtils.generateVariableName("double");
        String resultVar = RandomUtils.generateVariableName("double");

        sb.append("    public double ").append(methodName).append("(double ").append(inputVar).append(") {\n");
        sb.append("        if (").append(inputVar).append(" < 0) {\n");
        sb.append("            ").append(inputVar).append(" = Math.abs(").append(inputVar).append(");\n");
        sb.append("        }\n");

        // 调用所有方法
        for (String name : methodNames) {
            if (name.contains("calculate") || name.contains("evaluate") || name.contains("simplify")) {
                sb.append("        double ").append(resultVar).append(" = ").append(name).append("(").append(inputVar).append(");\n");
            } else if (name.contains("transform") || name.contains("convert")) {
                sb.append("        double ").append(resultVar).append(" = ").append(name).append("(").append(inputVar).append(");\n");
            } else if (name.contains("differentiate") || name.contains("integrate")) {
                sb.append("        double ").append(resultVar).append(" = ").append(name).append("(").append(inputVar).append(");\n");
            } else if (name.contains("series") || name.contains("identity")) {
                sb.append("        double ").append(resultVar).append(" = ").append(name).append("(").append(inputVar).append(");\n");
            } else if (name.contains("inverse") || name.contains("compose")) {
                sb.append("        double ").append(resultVar).append(" = ").append(name).append("(").append(inputVar).append(");\n");
            } else if (name.contains("decompose") || name.contains("factor")) {
                sb.append("        double ").append(resultVar).append(" = ").append(name).append("(").append(inputVar).append(");\n");
            } else if (name.contains("expand") || name.contains("simplify_fraction")) {
                sb.append("        double ").append(resultVar).append(" = ").append(name).append("(").append(inputVar).append(");\n");
            } else {
                sb.append("        double ").append(resultVar).append(" = ").append(name).append("(").append(inputVar).append(");\n");
            }
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TRIG_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        sb.append("        return ").append(resultVar).append(";\n");
        sb.append("    }\n\n");
    }
}
