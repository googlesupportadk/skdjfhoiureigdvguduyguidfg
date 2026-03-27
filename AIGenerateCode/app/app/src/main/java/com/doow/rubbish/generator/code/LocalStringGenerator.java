package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.*;

/**
 * 升级版字符串处理代码生成器 - 支持随机功能组合和多样性生成
 */
public class LocalStringGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    // 字符串操作类型
    private static final String[] OPERATION_TYPES = {
            "concat", "substring", "replace", "split", "trim",
            "lowercase", "uppercase", "reverse", "length", "empty",
            "capitalize", "repeat", "pad_left", "pad_right", "strip",
            "strip_leading", "strip_trailing", "strip_indent", "is_blank", "is_numeric"
    };

    // 字符串匹配类型
    private static final String[] MATCH_TYPES = {
            "contains", "starts_with", "ends_with", "equals", "equals_ignore_case",
            "matches", "find", "find_all", "index_of", "last_index_of",
            "first_index_of", "last_index_of_any", "contains_only", "contains_whitespace",
            "is_alpha", "is_digit", "is_alphanumeric", "is_empty", "is_blank"
    };

    // 字符串转换类型
    private static final String[] CONVERT_TYPES = {
            "to_string", "from_string", "to_bytes", "from_bytes", "to_char_array",
            "from_char_array", "to_list", "from_list", "to_integer", "from_integer",
            "to_long", "from_long", "to_float", "from_float", "to_double",
            "from_double", "to_boolean", "from_boolean", "to_uppercase", "to_lowercase"
    };

    // 数据类型
    private static final String[] DATA_TYPES = {
            "int", "long", "boolean", "String", "char[]", "byte[]",
            "List<String>", "Map<String, String>", "String[]", "int[]"
    };

    // 返回类型
    private static final String[] RETURN_TYPES = {
            "int", "long", "boolean", "String", "char[]", "byte[]",
            "List<String>", "Map<String, String>", "String[]", "int[]"
    };

    // 字符编码类型
    private static final String[] ENCODING_TYPES = {
            "UTF-8", "UTF-16", "ISO-8859-1", "ASCII", "GBK",
            "GB2312", "Big5", "Shift_JIS", "EUC-JP", "EUC-KR"
    };

    public LocalStringGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地字符串相关代码 - 升级版");

        // 随机生成5-15个字符串处理类
        int classCount = RandomUtils.between(5, 15);
        for (int i = 0; i < classCount; i++) {
            String className = RandomUtils.generateClassName("String");
            String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
            generateStringClass(className, operationType);
        }
    }

    /**
     * 生成字符串处理类
     */
    private void generateStringClass(String className, String operationType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 包声明
        sb.append(generatePackageDeclaration("string"));

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
        List<String> fieldNames = generateFields(sb, operationType);

        // 生成构造方法
        generateConstructor(sb, className, fieldNames);

        // 生成核心方法
        List<String> methodNames = new ArrayList<>();

        // 根据操作类型生成不同的方法组合
        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateConcatMethod(sb, fieldNames, operationType));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateSubstringMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateReplaceMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateSplitMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateTrimMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateCaseMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateMatchMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateConvertMethod(sb, fieldNames));
        }

        // 生成调用方法 - 确保所有方法都被调用
        if (methodNames.size() > 0) {
            generateCallerMethod(sb, className, fieldNames, methodNames);
        }

        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "string");
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
                } else if (fieldType.equals("char[]")) {
                    sb.append(" = new char[]{").append(RandomUtils.generateRandomString(1)).append("}");
                } else if (fieldType.equals("byte[]")) {
                    sb.append(" = new byte[]{").append(RandomUtils.between(0, 127)).append("}");
                } else if (fieldType.equals("List<String>")) {
                    sb.append(" = new ArrayList<>()");
                } else if (fieldType.equals("Map<String, String>")) {
                    sb.append(" = new HashMap<>()");
                } else if (fieldType.equals("String[]")) {
                    sb.append(" = new String[]{\"" + RandomUtils.generateRandomString(6) + "\"}");
                } else if (fieldType.equals("int[]")) {
                    sb.append(" = new int[]{").append(RandomUtils.between(0, 100)).append(", ").append(RandomUtils.between(0, 100)).append("}");
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
                    sb.append("        this.").append(fieldName).append(" = \"" + RandomUtils.generateRandomString(6) + "\";\n");
                    break;
                case 2:
                    sb.append("        this.").append(fieldName).append(" = ").append(RandomUtils.between(0, 100)).append(";\n");
                    break;
                case 3:
                    sb.append("        this.").append(fieldName).append(" = ").append(RandomUtils.randomBoolean()).append(";\n");
                    break;
                case 4:
                    sb.append("        this.").append(fieldName).append(" = new ArrayList<>();\n");
                    break;
                case 5:
                    sb.append("        this.").append(fieldName).append(" = new HashMap<>();\n");
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
     * 生成连接方法
     */
    private String generateConcatMethod(StringBuilder sb, List<String> fieldNames, String operationType) {
        // 从OPERATION_TYPES中随机选择
        String methodName = RandomUtils.generateMethodName(operationType);
        String str1Param = RandomUtils.generateVariableName("String");
        String str2Param = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("String");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(str1Param).append(", String ").append(str2Param).append(") {\n");
        sb.append("        String ").append(resultVar).append(" = \"\";\n");
        sb.append("        if (").append(str1Param).append(" != null && ").append(str2Param).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(str1Param).append(" + ").append(str2Param).append(";\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            ").append(usedField).append(" = ").append(resultVar).append(";\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(".length();\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return !").append(resultVar).append(".isEmpty();\n");
        } else if (returnType.equals("char[]")) {
            sb.append("        return ").append(resultVar).append(".toCharArray();\n");
        } else if (returnType.equals("byte[]")) {
            sb.append("        return ").append(resultVar).append(".getBytes();\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        ").append(RandomUtils.generateVariableName("List")).append(".add(").append(resultVar).append(");\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("String[]")) {
            sb.append("        return new String[]{").append(resultVar).append("};\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        return new int[]{").append(resultVar).append(".length()};\n");
        }

        sb.append("    }\n\n");
        return methodName;
    }

    /**
     * 生成子字符串方法
     */
    private String generateSubstringMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("substring");
        String strParam = RandomUtils.generateVariableName("String");
        String startParam = RandomUtils.generateVariableName("int");
        String endParam = RandomUtils.generateVariableName("int");
        String resultVar = RandomUtils.generateVariableName("String");

        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(strParam).append(", int ").append(startParam).append(", int ").append(endParam).append(") {\n");
        sb.append("        String ").append(resultVar).append(" = \"\";\n");
        sb.append("        if (").append(strParam).append(" != null && ").append(startParam).append(" >= 0 && ").append(endParam).append(" <= ").append(strParam).append(".length()) {\n");
        sb.append("            try {\n");
        sb.append("                ").append(resultVar).append(" = ").append(strParam).append(".substring(").append(startParam).append(", ").append(endParam).append(");\n");
        sb.append("            } catch (Exception e) {\n");
        sb.append("                // Handle exception silently\n");
        sb.append("            }\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            ").append(usedField).append(" = ").append(resultVar).append(";\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(".length();\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return !").append(resultVar).append(".isEmpty();\n");
        } else if (returnType.equals("char[]")) {
            sb.append("        return ").append(resultVar).append(".toCharArray();\n");
        } else if (returnType.equals("byte[]")) {
            sb.append("        return ").append(resultVar).append(".getBytes();\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        ").append(RandomUtils.generateVariableName("List")).append(".add(").append(resultVar).append(");\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("String[]")) {
            sb.append("        return new String[]{").append(resultVar).append("};\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        return new int[]{").append(resultVar).append(".length()};\n");
        }

        sb.append("    }\n\n");
        return methodName;
    }

    /**
     * 生成替换方法
     */
    private String generateReplaceMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("replace");
        String strParam = RandomUtils.generateVariableName("String");
        String oldParam = RandomUtils.generateVariableName("String");
        String newParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("String");

        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(strParam).append(", String ").append(oldParam).append(", String ").append(newParam).append(") {\n");
        sb.append("        String ").append(resultVar).append(" = ").append(strParam).append(";\n");
        sb.append("        if (").append(strParam).append(" != null && ").append(oldParam).append(" != null && ").append(newParam).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(strParam).append(".replace(").append(oldParam).append(", ").append(newParam).append(");\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            ").append(usedField).append(" = ").append(resultVar).append(";\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(".length();\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return !").append(resultVar).append(".isEmpty();\n");
        } else if (returnType.equals("char[]")) {
            sb.append("        return ").append(resultVar).append(".toCharArray();\n");
        } else if (returnType.equals("byte[]")) {
            sb.append("        return ").append(resultVar).append(".getBytes();\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        ").append(RandomUtils.generateVariableName("List")).append(".add(").append(resultVar).append(");\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("String[]")) {
            sb.append("        return new String[]{").append(resultVar).append("};\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        return new int[]{").append(resultVar).append(".length()};\n");
        }

        sb.append("    }\n\n");
        return methodName;
    }

    /**
     * 生成分割方法
     */
    private String generateSplitMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("split");
        String strParam = RandomUtils.generateVariableName("String");
        String delimParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("Array");

        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(strParam).append(", String ").append(delimParam).append(") {\n");
        sb.append("        String[] ").append(resultVar).append(" = new String[0];\n");
        sb.append("        if (").append(strParam).append(" != null && ").append(delimParam).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(strParam).append(".split(").append(delimParam).append(");\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            if (").append(usedField).append(" instanceof String[]) {\n");
                sb.append("                String[] tempArray = (String[]) ").append(usedField).append(";\n");
                sb.append("                ").append(usedField).append(" = ").append(resultVar).append(";\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("String[]")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        for (String s : ").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(s);\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(".length;\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(".length > 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(".length > 0 ? ").append(resultVar).append("[0] : \"\";\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        int[] ").append(RandomUtils.generateVariableName("intArray")).append(" = new int[").append(resultVar).append(".length];\n");
            sb.append("        for (int i = 0; i < ").append(resultVar).append(".length; i++) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("intArray")).append("[i] = ").append(resultVar).append("[i].length();\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("intArray")).append(";\n");
        } else {
            sb.append("        return null;\n");
        }

        sb.append("    }\n\n");
        return methodName;
    }

    /**
     * 生成修剪方法
     */
    private String generateTrimMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("trim");
        String strParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("String");

        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(strParam).append(") {\n");
        sb.append("        String ").append(resultVar).append(" = \"\";\n");
        sb.append("        if (").append(strParam).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(strParam).append(".trim();\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            ").append(usedField).append(" = ").append(resultVar).append(";\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(".length();\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return !").append(resultVar).append(".isEmpty();\n");
        } else if (returnType.equals("char[]")) {
            sb.append("        return ").append(resultVar).append(".toCharArray();\n");
        } else if (returnType.equals("byte[]")) {
            sb.append("        return ").append(resultVar).append(".getBytes();\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        ").append(RandomUtils.generateVariableName("List")).append(".add(").append(resultVar).append(");\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("String[]")) {
            sb.append("        return new String[]{").append(resultVar).append("};\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        return new int[]{").append(resultVar).append(".length()};\n");
        }

        sb.append("    }\n\n");
        return methodName;
    }

    /**
     * 生成大小写转换方法
     */
    private String generateCaseMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("case");
        String strParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("String");

        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(strParam).append(") {\n");
        sb.append("        String ").append(resultVar).append(" = \"\";\n");
        sb.append("        if (").append(strParam).append(" != null) {\n");
        sb.append("            if (Math.random() < 0.5) {\n");
        sb.append("                ").append(resultVar).append(" = ").append(strParam).append(".toUpperCase();\n");
        sb.append("            } else {\n");
        sb.append("                ").append(resultVar).append(" = ").append(strParam).append(".toLowerCase();\n");
        sb.append("            }\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            ").append(usedField).append(" = ").append(resultVar).append(";\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(".length();\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return !").append(resultVar).append(".isEmpty();\n");
        } else if (returnType.equals("char[]")) {
            sb.append("        return ").append(resultVar).append(".toCharArray();\n");
        } else if (returnType.equals("byte[]")) {
            sb.append("        return ").append(resultVar).append(".getBytes();\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        ").append(RandomUtils.generateVariableName("List")).append(".add(").append(resultVar).append(");\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", ").append(resultVar).append(");\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("String[]")) {
            sb.append("        return new String[]{").append(resultVar).append("};\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        return new int[]{").append(resultVar).append(".length()};\n");
        }

        sb.append("    }\n\n");
        return methodName;
    }

    /**
     * 生成匹配方法
     */
    private String generateMatchMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("match");
        String str1Param = RandomUtils.generateVariableName("String");
        String str2Param = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("boolean");

        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(str1Param).append(", String ").append(str2Param).append(") {\n");
        sb.append("        boolean ").append(resultVar).append(" = false;\n");
        sb.append("        if (").append(str1Param).append(" != null && ").append(str2Param).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(str1Param).append(".equals(").append(str2Param).append(");\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            ").append(usedField).append(" = ").append(resultVar).append(";\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" ? \"match\" : \"no_match\";\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        ").append(RandomUtils.generateVariableName("List")).append(".add(").append(resultVar).append(" ? \"match\" : \"no_match\");\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", ").append(resultVar).append(" ? \"match\" : \"no_match\");\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("String[]")) {
            sb.append("        return new String[]{").append(resultVar).append(" ? \"match\" : \"no_match\"};\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        return new int[]{").append(resultVar).append(" ? 1 : 0};\n");
        } else {
            sb.append("        return null;\n");
        }

        sb.append("    }\n\n");
        return methodName;
    }

    /**
     * 生成转换方法
     */
    private String generateConvertMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("convert");
        String strParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("result");

        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(strParam).append(") {\n");
        sb.append("        if (").append(strParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");

        // 根据返回类型生成不同的转换逻辑
        if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        try {\n");
            sb.append("            return ").append(strParam).append(".length();\n");
            sb.append("        } catch (Exception e) {\n");
            sb.append("            return 0;\n");
            sb.append("        }\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return !").append(strParam).append(".isEmpty();\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(strParam).append(";\n");
        } else if (returnType.equals("char[]")) {
            sb.append("        return ").append(strParam).append(".toCharArray();\n");
        } else if (returnType.equals("byte[]")) {
            sb.append("        return ").append(strParam).append(".getBytes();\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(resultVar).append(" = new ArrayList<>();\n");
            sb.append("        ").append(resultVar).append(".add(").append(strParam).append(");\n");
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(resultVar).append(" = new HashMap<>();\n");
            sb.append("        ").append(resultVar).append(".put(\"value\", ").append(strParam).append(");\n");
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("String[]")) {
            sb.append("        return new String[]{").append(strParam).append("};\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        return new int[]{").append(strParam).append(".length()};\n");
        } else {
            sb.append("        return null;\n");
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            if (").append(usedField).append(" instanceof String) {\n");
                sb.append("                ").append(usedField).append(" = ").append(strParam).append(";\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        sb.append("    }\n\n");
        return methodName;
    }

    /**
     * 生成调用方法 - 确保所有方法都被调用
     */
    private void generateCallerMethod(StringBuilder sb, String className, List<String> fieldNames, List<String> methodNames) {
        String methodName = RandomUtils.generateMethodName("process");
        String strParam = RandomUtils.generateVariableName("String");

        sb.append("    public void ").append(methodName).append("(String ").append(strParam).append(") {\n");
        sb.append("        if (").append(strParam).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");

        // 调用所有生成的方法
        for (String mName : methodNames) {
            sb.append("        ").append(mName).append("(").append(strParam).append(");\n");
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            ").append(usedField).append(" = ").append(strParam).append(";\n");
                sb.append("        }\n");
            }
        }

        // 随机添加日志
        if (RandomUtils.randomBoolean() && RandomUtils.randomBoolean()) {
            sb.append("        System.out.println(TAG + \" processed: \" + ").append(strParam).append(");\n");
        }

        sb.append("    }\n\n");
    }
}
