package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * 升级版流处理代码生成器 - 支持随机功能组合和多样性生成
 */
public class LocalStreamGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    // 流操作类型
    private static final String[] OPERATION_TYPES = {
            "filter", "map", "reduce", "collect", "find",
            "any_match", "all_match", "none_match", "count", "limit",
            "skip", "peek", "distinct", "sorted", "sorted_comparator", "for_each",
            "iterator", "spliterator", "to_array", "to_list", "to_set", "to_map"
    };

    // 流转换类型
    private static final String[] CONVERT_TYPES = {
            "to_list", "to_set", "to_map", "to_array", "to_string",
            "group_by", "partition_by", "flat_map", "distinct", "sorted",
            "flat_map_to_int", "flat_map_to_long", "map_to_int", "map_to_long",
            "map_to_double", "map_to_obj", "map_to_string", "boxed", "unboxed"
    };

    // 流聚合类型
    private static final String[] AGGREGATE_TYPES = {
            "sum", "average", "min", "max", "count",
            "first", "last", "join", "reduce", "collect",
            "summing_int", "summing_long", "summing_double", "averaging_int",
            "averaging_long", "averaging_double", "counting", "grouping_by",
            "partitioning_by", "mapping", "reducing", "collecting_and_then"
    };

    // 数据类型
    private static final String[] DATA_TYPES = {
            "int", "long", "boolean", "String", "Stream<T>", "List<T>",
            "Set<T>", "Map<String, T>", "Optional<T>", "T[]", "int[]"
    };

    // 返回类型
    private static final String[] RETURN_TYPES = {
            "int", "long", "boolean", "String", "Stream<T>", "List<T>",
            "Set<T>", "Map<String, T>", "Optional<T>", "T[]", "int[]"
    };

    // 收集器类型
    private static final String[] COLLECTOR_TYPES = {
            "toList", "toSet", "toMap", "joining", "groupingBy",
            "partitioningBy", "mapping", "reducing", "summingInt", "summingLong",
            "summingDouble", "averagingInt", "averagingLong", "averagingDouble", "counting"
    };

    public LocalStreamGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地流相关代码 - 升级版");

        // 随机生成5-15个流处理类
        int classCount = RandomUtils.between(5, 15);
        for (int i = 0; i < classCount; i++) {
            String className = RandomUtils.generateClassName("Stream");
            String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
            generateStreamClass(className, operationType);
        }
    }

    /**
     * 生成流处理类
     */
    private void generateStreamClass(String className, String operationType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 包声明
        sb.append(generatePackageDeclaration("stream"));

        // 导入语句
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.Set"));
        sb.append(generateImportStatement("java.util.HashSet"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Optional"));
        sb.append(generateImportStatement("java.util.stream.Stream"));
        sb.append(generateImportStatement("java.util.stream.Collectors"));
        sb.append(generateImportStatement("java.util.Collections"));
        sb.append(generateImportStatement("java.util.function.Predicate"));
        sb.append(generateImportStatement("java.util.function.Function"));
        sb.append(generateImportStatement("java.util.function.BinaryOperator"));
        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append("<T> {\n\n");

        // 生成类成员变量
        List<String> fieldNames = generateFields(sb, operationType);

        // 生成构造方法
        generateConstructor(sb, className, fieldNames);

        // 生成核心方法
        List<String> methodNames = new ArrayList<>();

        // 根据操作类型生成不同的方法组合
        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateFilterMethod(sb, fieldNames, operationType));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateMapMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateReduceMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateCollectMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateFindMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateMatchMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateAggregateMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateFlatMapMethod(sb, fieldNames));
        }

        // 生成调用方法 - 确保所有方法都被调用
        if (methodNames.size() > 0) {
            generateCallerMethod(sb, className, fieldNames, methodNames);
        }

        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "stream");
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
                } else if (fieldType.equals("Stream<T>")) {
                    sb.append(" = Stream.empty()");
                } else if (fieldType.equals("List<T>")) {
                    sb.append(" = new ArrayList<>()");
                } else if (fieldType.equals("Set<T>")) {
                    sb.append(" = new HashSet<>()");
                } else if (fieldType.equals("Map<String, T>")) {
                    sb.append(" = new HashMap<>()");
                } else if (fieldType.equals("Optional<T>")) {
                    sb.append(" = Optional.empty()");
                } else if (fieldType.equals("T[]")) {
                    sb.append(" = (T[]) new Object[0]");
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
                    sb.append("        this.").append(fieldName).append(" = Stream.empty();\n");
                    break;
                case 2:
                    sb.append("        this.").append(fieldName).append(" = new ArrayList<>();\n");
                    break;
                case 3:
                    sb.append("        this.").append(fieldName).append(" = new HashSet<>();\n");
                    break;
                case 4:
                    sb.append("        this.").append(fieldName).append(" = new HashMap<>();\n");
                    break;
                case 5:
                    sb.append("        this.").append(fieldName).append(" = Optional.empty();\n");
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
     * 生成过滤方法
     */
    private String generateFilterMethod(StringBuilder sb, List<String> fieldNames, String operationType) {
        // 从OPERATION_TYPES中随机选择
        String methodName = RandomUtils.generateMethodName(operationType);
        String streamParam = RandomUtils.generateVariableName("Stream");
        String predicateParam = RandomUtils.generateVariableName("Predicate");
        String resultVar = RandomUtils.generateVariableName("Stream");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Stream<T> ").append(streamParam).append(", Predicate<T> ").append(predicateParam).append(") {\n");
        sb.append("        Stream<T> ").append(resultVar).append(" = Stream.empty();\n");
        sb.append("        if (").append(streamParam).append(" != null && ").append(predicateParam).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".filter(").append(predicateParam).append(");\n");
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
        if (returnType.equals("Stream<T>")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("List<T>")) {
            sb.append("        return ").append(resultVar).append(".collect(Collectors.toList());\n");
        } else if (returnType.equals("Set<T>")) {
            sb.append("        return ").append(resultVar).append(".collect(Collectors.toSet());\n");
        } else if (returnType.equals("Map<String, T>")) {
            sb.append("        return ").append(resultVar).append(".collect(Collectors.toMap(t -> String.valueOf(t), t -> t));\n");
        } else if (returnType.equals("Optional<T>")) {
            sb.append("        return ").append(resultVar).append(".findFirst();\n");
        } else if (returnType.equals("T[]")) {
            sb.append("        return ").append(resultVar).append(".toArray(t -> (T[]) new Object[0]);\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return (int) ").append(resultVar).append(".count();\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(".count() > 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(".map(Object::toString).collect(Collectors.joining(\",\"));\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        return ").append(resultVar).append(".mapToInt(t -> t instanceof Integer ? (Integer) t : 0).toArray();\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成映射方法
     */
    private String generateMapMethod(StringBuilder sb, List<String> fieldNames) {
        // 从CONVERT_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(CONVERT_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String streamParam = RandomUtils.generateVariableName("Stream");
        String functionParam = RandomUtils.generateVariableName("Function");
        String resultVar = RandomUtils.generateVariableName("Stream");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public <R> ").append(returnType).append(" ").append(methodName).append("(Stream<T> ").append(streamParam).append(", Function<T, R> ").append(functionParam).append(") {\n");
        sb.append("        Stream<R> ").append(resultVar).append(" = Stream.empty();\n");
        sb.append("        if (").append(streamParam).append(" != null && ").append(functionParam).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".map(").append(functionParam).append(");\n");
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
        if (returnType.equals("Stream<T>")) {
            sb.append("        return ").append(resultVar).append(".map(t -> (T) t);\n");
        } else if (returnType.equals("List<T>")) {
            sb.append("        return ").append(resultVar).append(".collect(Collectors.toList());\n");
        } else if (returnType.equals("Set<T>")) {
            sb.append("        return ").append(resultVar).append(".collect(Collectors.toSet());\n");
        } else if (returnType.equals("Map<String, T>")) {
            sb.append("        return ").append(resultVar).append(".collect(Collectors.toMap(r -> String.valueOf(r), r -> (T) r));\n");
        } else if (returnType.equals("Optional<T>")) {
            sb.append("        return ").append(resultVar).append(".findFirst().map(r -> (T) r);\n");
        } else if (returnType.equals("T[]")) {
            sb.append("        return ").append(resultVar).append(".toArray(r -> (T[]) new Object[0]);\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return (int) ").append(resultVar).append(".count();\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(".count() > 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(".map(Object::toString).collect(Collectors.joining(\",\"));\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        return ").append(resultVar).append(".mapToInt(r -> r instanceof Integer ? (Integer) r : 0).toArray();\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成归约方法
     */
    private String generateReduceMethod(StringBuilder sb, List<String> fieldNames) {
        // 从AGGREGATE_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(AGGREGATE_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String streamParam = RandomUtils.generateVariableName("Stream");
        String identityParam = RandomUtils.generateVariableName("T");
        String operatorParam = RandomUtils.generateVariableName("BinaryOperator");
        String resultVar = RandomUtils.generateVariableName("Optional");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Stream<T> ").append(streamParam).append(", T ").append(identityParam).append(", BinaryOperator<T> ").append(operatorParam).append(") {\n");
        sb.append("        Optional<T> ").append(resultVar).append(" = Optional.empty();\n");
        sb.append("        if (").append(streamParam).append(" != null && ").append(operatorParam).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".reduce(").append(operatorParam).append(");\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            ").append(usedField).append(" = ").append(resultVar).append(".orElse(").append(identityParam).append(");\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("Optional<T>")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("T")) {
            sb.append("        return ").append(resultVar).append(".orElse(").append(identityParam).append(");\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(".isPresent() ? 1 : 0;\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(".isPresent();\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(".map(Object::toString).orElse(\"\");\n");
        } else {
            sb.append("        return ").append(resultVar).append(".orElse(null);\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成收集方法
     */
    private String generateCollectMethod(StringBuilder sb, List<String> fieldNames) {
        // 从COLLECTOR_TYPES中随机选择
        String collectorType = RandomUtils.randomChoice(COLLECTOR_TYPES);
        String methodName = RandomUtils.generateMethodName(collectorType);
        String streamParam = RandomUtils.generateVariableName("Stream");
        String resultVar = RandomUtils.generateVariableName("result");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Stream<T> ").append(streamParam).append(") {\n");
        sb.append("        Object ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(streamParam).append(" != null) {\n");

        // 根据收集器类型生成不同的收集操作
        if (collectorType.equals("toList")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.toList());\n");
        } else if (collectorType.equals("toSet")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.toSet());\n");
        } else if (collectorType.equals("toMap")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.toMap(t -> String.valueOf(t), t -> t));\n");
        } else if (collectorType.equals("joining")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".map(Object::toString).collect(Collectors.joining(\",\"));\n");
        } else if (collectorType.equals("groupingBy")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.groupingBy(t -> String.valueOf(t)));\n");
        } else if (collectorType.equals("partitioningBy")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.partitioningBy(t -> t != null));\n");
        } else if (collectorType.equals("mapping")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.mapping(t -> String.valueOf(t), Collectors.toList()));\n");
        } else if (collectorType.equals("reducing")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.reducing((a, b) -> a));\n");
        } else if (collectorType.equals("summingInt")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.summingInt(t -> t instanceof Integer ? (Integer) t : 0));\n");
        } else if (collectorType.equals("summingLong")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.summingLong(t -> t instanceof Long ? (Long) t : 0L));\n");
        } else if (collectorType.equals("summingDouble")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.summingDouble(t -> t instanceof Double ? (Double) t : 0.0));\n");
        } else if (collectorType.equals("averagingInt")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.averagingInt(t -> t instanceof Integer ? (Integer) t : 0));\n");
        } else if (collectorType.equals("averagingLong")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.averagingLong(t -> t instanceof Long ? (Long) t : 0L));\n");
        } else if (collectorType.equals("averagingDouble")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.averagingDouble(t -> t instanceof Double ? (Double) t : 0.0));\n");
        } else if (collectorType.equals("counting")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.counting());\n");
        }

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
        if (returnType.equals("List<T>")) {
            sb.append("        return ").append(resultVar).append(" instanceof List ? (List<T>) ").append(resultVar).append(" : new ArrayList<>();\n");
        } else if (returnType.equals("Set<T>")) {
            sb.append("        return ").append(resultVar).append(" instanceof Set ? (Set<T>) ").append(resultVar).append(" : new HashSet<>();\n");
        } else if (returnType.equals("Map<String, T>")) {
            sb.append("        return ").append(resultVar).append(" instanceof Map ? (Map<String, T>) ").append(resultVar).append(" : new HashMap<>();\n");
        } else if (returnType.equals("Optional<T>")) {
            sb.append("        return ").append(resultVar).append(" != null ? Optional.ofNullable((T) ").append(resultVar).append(") : Optional.empty();\n");
        } else if (returnType.equals("T[]")) {
            sb.append("        return ").append(resultVar).append(" instanceof List ? ((List<T>) ").append(resultVar).append(").toArray(t -> (T[]) new Object[0]) : (T[]) new Object[0];\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" instanceof Number ? ((Number) ").append(resultVar).append(").intValue() : 0;\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".toString() : \"\";\n");
        } else {
            sb.append("        return ").append(resultVar).append(" != null ? (T) ").append(resultVar).append(" : null;\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成查找方法
     */
    private String generateFindMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String streamParam = RandomUtils.generateVariableName("Stream");
        String predicateParam = RandomUtils.generateVariableName("Predicate");
        String resultVar = RandomUtils.generateVariableName("Optional");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Stream<T> ").append(streamParam).append(", Predicate<T> ").append(predicateParam).append(") {\n");
        sb.append("        Optional<T> ").append(resultVar).append(" = Optional.empty();\n");
        sb.append("        if (").append(streamParam).append(" != null && ").append(predicateParam).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".filter(").append(predicateParam).append(").findFirst();\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            ").append(usedField).append(" = ").append(resultVar).append(".orElse(null);\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("Optional<T>")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("T")) {
            sb.append("        return ").append(resultVar).append(".orElse(null);\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(".isPresent() ? 1 : 0;\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(".isPresent();\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(".map(Object::toString).orElse(\"\");\n");
        } else {
            sb.append("        return ").append(resultVar).append(".orElse(null);\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成匹配方法
     */
    private String generateMatchMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String streamParam = RandomUtils.generateVariableName("Stream");
        String predicateParam = RandomUtils.generateVariableName("Predicate");
        String resultVar = RandomUtils.generateVariableName("boolean");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Stream<T> ").append(streamParam).append(", Predicate<T> ").append(predicateParam).append(") {\n");
        sb.append("        boolean ").append(resultVar).append(" = false;\n");
        sb.append("        if (").append(streamParam).append(" != null && ").append(predicateParam).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".anyMatch(").append(predicateParam).append(");\n");
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
        } else {
            sb.append("        return ").append(resultVar).append(" ? (T) Boolean.TRUE : (T) Boolean.FALSE;\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成聚合方法
     */
    private String generateAggregateMethod(StringBuilder sb, List<String> fieldNames) {
        // 从AGGREGATE_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(AGGREGATE_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String streamParam = RandomUtils.generateVariableName("Stream");
        String resultVar = RandomUtils.generateVariableName("result");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Stream<T> ").append(streamParam).append(") {\n");
        sb.append("        Object ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(streamParam).append(" != null) {\n");

        // 根据聚合类型生成不同的聚合操作
        if (operationType.equals("sum")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.summingInt(t -> t instanceof Integer ? (Integer) t : 0));\n");
        } else if (operationType.equals("average")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.averagingInt(t -> t instanceof Integer ? (Integer) t : 0));\n");
        } else if (operationType.equals("min")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".min(Comparator.comparing(t -> String.valueOf(t))).orElse(null);\n");
        } else if (operationType.equals("max")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".max(Comparator.comparing(t -> String.valueOf(t))).orElse(null);\n");
        } else if (operationType.equals("count")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".count();\n");
        } else if (operationType.equals("first")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".findFirst().orElse(null);\n");
        } else if (operationType.equals("last")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".reduce((a, b) -> b).orElse(null);\n");
        } else if (operationType.equals("join")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".map(Object::toString).collect(Collectors.joining(\",\"));\n");
        } else if (operationType.equals("reduce")) {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".reduce((a, b) -> a).orElse(null);\n");
        } else {
            sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".collect(Collectors.toList());\n");
        }

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
        if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" instanceof Number ? ((Number) ").append(resultVar).append(").intValue() : 0;\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".toString() : \"\";\n");
        } else {
            sb.append("        return ").append(resultVar).append(" != null ? (T) ").append(resultVar).append(" : null;\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成扁平映射方法
     */
    private String generateFlatMapMethod(StringBuilder sb, List<String> fieldNames) {
        // 从CONVERT_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(CONVERT_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String streamParam = RandomUtils.generateVariableName("Stream");
        String functionParam = RandomUtils.generateVariableName("Function");
        String resultVar = RandomUtils.generateVariableName("Stream");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public <R> ").append(returnType).append(" ").append(methodName).append("(Stream<T> ").append(streamParam).append(", Function<T, Stream<R>> ").append(functionParam).append(") {\n");
        sb.append("        Stream<R> ").append(resultVar).append(" = Stream.empty();\n");
        sb.append("        if (").append(streamParam).append(" != null && ").append(functionParam).append(" != null) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(streamParam).append(".flatMap(").append(functionParam).append(");\n");
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
        if (returnType.equals("Stream<T>")) {
            sb.append("        return ").append(resultVar).append(".map(r -> (T) r);\n");
        } else if (returnType.equals("List<T>")) {
            sb.append("        return ").append(resultVar).append(".collect(Collectors.toList());\n");
        } else if (returnType.equals("Set<T>")) {
            sb.append("        return ").append(resultVar).append(".collect(Collectors.toSet());\n");
        } else if (returnType.equals("Map<String, T>")) {
            sb.append("        return ").append(resultVar).append(".collect(Collectors.toMap(r -> String.valueOf(r), r -> (T) r));\n");
        } else if (returnType.equals("Optional<T>")) {
            sb.append("        return ").append(resultVar).append(".findFirst().map(r -> (T) r);\n");
        } else if (returnType.equals("T[]")) {
            sb.append("        return ").append(resultVar).append(".toArray(r -> (T[]) new Object[0]);\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return (int) ").append(resultVar).append(".count();\n");
        } else if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(".count() > 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(".map(Object::toString).collect(Collectors.joining(\",\"));\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        return ").append(resultVar).append(".mapToInt(r -> r instanceof Integer ? (Integer) r : 0).toArray();\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成调用方法 - 确保所有方法都被调用
     */
    private void generateCallerMethod(StringBuilder sb, String className, List<String> fieldNames, List<String> methodNames) {
        String methodName = RandomUtils.generateMethodName("process");

        sb.append("    public void ").append(methodName).append("(Stream<T> stream) {\n");
        sb.append("        if (stream == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n\n");

        // 随机调用所有方法
        Collections.shuffle(methodNames);
        for (String name : methodNames) {
            int callType = RandomUtils.between(1, 4);
            switch (callType) {
                case 1:
                    sb.append("        this.").append(name).append("(stream, t -> t != null);\n");
                    break;
                case 2:
                    sb.append("        this.").append(name).append("(stream, t -> String.valueOf(t));\n");
                    break;
                case 3:
                    sb.append("        this.").append(name).append("(stream);\n");
                    break;
                case 4:
                    sb.append("        this.").append(name).append("(stream, (a, b) -> a);\n");
                    break;
            }
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            ").append(usedField).append(" = stream;\n");
                sb.append("        }\n");
            }
        }

        // 随机添加日志
        if (RandomUtils.randomBoolean() && RandomUtils.randomBoolean()) {
            sb.append("        System.out.println(TAG + \" processed\");\n");
        }

        sb.append("    }\n\n");
    }
}
