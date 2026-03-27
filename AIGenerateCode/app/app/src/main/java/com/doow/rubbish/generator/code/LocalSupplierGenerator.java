package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.*;
import java.util.function.*;

/**
 * 升级版提供者代码生成器 - 支持随机功能组合和多样性生成
 */
public class LocalSupplierGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    // 操作类型
    private static final String[] OPERATION_TYPES = {
            "get", "get_or_else", "get_or_null", "get_or_throw",
            "lazy", "memoize", "cache", "retry", "timeout",
            "sync", "async", "supply_async", "supply_sync", "once",
            "once_per_key", "synchronized", "blocking", "non_blocking",
            "map", "filter", "peek", "or_else", "or_else_get"
    };

    // 转换类型
    private static final String[] CONVERT_TYPES = {
            "to_function", "to_consumer", "to_predicate", "to_optional",
            "to_stream", "to_runnable", "to_callable", "to_future", "to_completable_future",
            "to_unary_operator", "to_binary_operator", "to_comparator", "to_comparing",
            "to_optional_long", "to_optional_double", "to_optional_int", "to_optional_string"
    };

    // 组合类型
    private static final String[] COMPOSE_TYPES = {
            "compose", "and_then", "sequence", "parallel", "conditional",
            "fallback", "retry", "batch", "throttle", "debounce",
            "compose_bi", "and_then_bi", "or_else_bi", "or_else_bi_get",
            "adapt", "lift", "lift_binary", "lift_to_int", "lift_to_long"
    };

    // 数据类型
    private static final String[] DATA_TYPES = {
            "int", "long", "boolean", "String", "Date",
            "List<String>", "Map<String, String>", "Supplier<T>", "Optional<T>", "Function<T, R>"
    };

    // 返回类型
    private static final String[] RETURN_TYPES = {
            "int", "long", "boolean", "String", "Date",
            "List<String>", "Map<String, String>", "Supplier<T>", "Optional<T>", "Function<T, R>"
    };

    // 异常处理类型
    private static final String[] EXCEPTION_TYPES = {
            "NullPointerException", "IllegalArgumentException", "IllegalStateException",
            "RuntimeException", "UnsupportedOperationException"
    };

    public LocalSupplierGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地提供者相关代码 - 升级版");

        // 随机生成5-15个提供者类
        int classCount = RandomUtils.between(5, 15);
        for (int i = 0; i < classCount; i++) {
            String className = RandomUtils.generateClassName("Supplier");
            String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
            generateSupplierClass(className, operationType);
        }
    }

    /**
     * 生成提供者类
     */
    private void generateSupplierClass(String className, String operationType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 包声明
        sb.append(generatePackageDeclaration("supplier"));

        // 导入语句
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Date"));
        sb.append(generateImportStatement("java.util.Optional"));
        sb.append(generateImportStatement("java.util.function.Supplier"));
        sb.append(generateImportStatement("java.util.function.Function"));
        sb.append(generateImportStatement("java.util.function.Consumer"));
        sb.append(generateImportStatement("java.util.function.Predicate"));
        sb.append(generateImportStatement("java.util.concurrent.Callable"));
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
            methodNames.add(generateGetMethod(sb, fieldNames, operationType));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateOrElseMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateMapMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateFilterMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateComposeMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateMemoizeMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateRetryMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateCacheMethod(sb, fieldNames));
        }

        // 生成调用方法 - 确保所有方法都被调用
        if (methodNames.size() > 0) {
            generateCallerMethod(sb, className, fieldNames, methodNames);
        }

        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "supplier");
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
                } else if (fieldType.equals("Date")) {
                    sb.append(" = new Date()");
                } else if (fieldType.equals("List<String>")) {
                    sb.append(" = new ArrayList<>()");
                } else if (fieldType.equals("Map<String, String>")) {
                    sb.append(" = new HashMap<>()");
                } else if (fieldType.equals("Supplier<T>")) {
                    sb.append(" = () -> null");
                } else if (fieldType.equals("Optional<T>")) {
                    sb.append(" = Optional.empty()");
                } else if (fieldType.equals("Function<T, R>")) {
                    sb.append(" = t -> null");
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
                    sb.append("        this.").append(fieldName).append(" = ").append(RandomUtils.between(0, 100)).append(";\n");
                    break;
                case 2:
                    sb.append("        this.").append(fieldName).append(" = \"" + RandomUtils.generateRandomString(6) + "\";\n");
                    break;
                case 3:
                    sb.append("        this.").append(fieldName).append(" = ").append(RandomUtils.randomBoolean()).append(";\n");
                    break;
                case 4:
                    sb.append("        this.").append(fieldName).append(" = new ArrayList<>();\n");
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
     * 生成获取方法
     */
    private String generateGetMethod(StringBuilder sb, List<String> fieldNames, String operationType) {
        // 从OPERATION_TYPES中随机选择
        String methodName = RandomUtils.generateMethodName(operationType);
        String supplierParam = RandomUtils.generateVariableName("Supplier");
        String resultVar = RandomUtils.generateVariableName("T");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Supplier<T> ").append(supplierParam).append(") {\n");
        sb.append("        T ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(supplierParam).append(" != null) {\n");
        sb.append("            try {\n");
        sb.append("                ").append(resultVar).append(" = ").append(supplierParam).append(".get();\n");
        sb.append("            } catch (Exception e) {\n");
        sb.append("                // Handle exception silently\n");
        sb.append("            }\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            try {\n");
                sb.append("                T fieldValue = ").append(usedField).append(" instanceof Supplier ? ((Supplier<T>) ").append(usedField).append(").get() : null;\n");
                sb.append("                if (fieldValue != null) {\n");
                sb.append("                    ").append(resultVar).append(" = fieldValue;\n");
                sb.append("                }\n");
                sb.append("            } catch (Exception e) {\n");
                sb.append("                // Handle exception silently\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" != null ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".toString() : null;\n");
        } else if (returnType.equals("Optional<T>")) {
            sb.append("        return Optional.ofNullable(").append(resultVar).append(");\n");
        } else if (returnType.equals("Supplier<T>")) {
            sb.append("        return () -> ").append(resultVar).append(";\n");
        } else {
            sb.append("        return ").append(resultVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成orElse方法
     */
    private String generateOrElseMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String supplierParam = RandomUtils.generateVariableName("Supplier");
        String defaultValueParam = RandomUtils.generateVariableName("T");
        String resultVar = RandomUtils.generateVariableName("T");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Supplier<T> ").append(supplierParam).append(", T ").append(defaultValueParam).append(") {\n");
        sb.append("        T ").append(resultVar).append(" = ").append(defaultValueParam).append(";\n");
        sb.append("        if (").append(supplierParam).append(" != null) {\n");
        sb.append("            try {\n");
        sb.append("                T value = ").append(supplierParam).append(".get();\n");
        sb.append("                if (value != null) {\n");
        sb.append("                    ").append(resultVar).append(" = value;\n");
        sb.append("                }\n");
        sb.append("            } catch (Exception e) {\n");
        sb.append("                // Handle exception silently\n");
        sb.append("            }\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            try {\n");
                sb.append("                T fieldValue = ").append(usedField).append(" instanceof Supplier ? ((Supplier<T>) ").append(usedField).append(").get() : null;\n");
                sb.append("                if (fieldValue != null) {\n");
                sb.append("                    ").append(resultVar).append(" = fieldValue;\n");
                sb.append("                }\n");
                sb.append("            } catch (Exception e) {\n");
                sb.append("                // Handle exception silently\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" != null ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".toString() : null;\n");
        } else if (returnType.equals("Optional<T>")) {
            sb.append("        return Optional.ofNullable(").append(resultVar).append(");\n");
        } else if (returnType.equals("Supplier<T>")) {
            sb.append("        return () -> ").append(resultVar).append(";\n");
        } else {
            sb.append("        return ").append(resultVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成映射方法
     */
    private String generateMapMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String supplierParam = RandomUtils.generateVariableName("Supplier");
        String functionParam = RandomUtils.generateVariableName("Function");
        String resultVar = RandomUtils.generateVariableName("R");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public <R> ").append(returnType).append(" ").append(methodName).append("(Supplier<T> ").append(supplierParam).append(", Function<T, R> ").append(functionParam).append(") {\n");
        sb.append("        R ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(supplierParam).append(" != null && ").append(functionParam).append(" != null) {\n");
        sb.append("            try {\n");
        sb.append("                T value = ").append(supplierParam).append(".get();\n");
        sb.append("                if (value != null) {\n");
        sb.append("                    ").append(resultVar).append(" = ").append(functionParam).append(".apply(value);\n");
        sb.append("                }\n");
        sb.append("            } catch (Exception e) {\n");
        sb.append("                // Handle exception silently\n");
        sb.append("            }\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            try {\n");
                sb.append("                T fieldValue = ").append(usedField).append(" instanceof Supplier ? ((Supplier<T>) ").append(usedField).append(").get() : null;\n");
                sb.append("                if (fieldValue != null && ").append(functionParam).append(" != null) {\n");
                sb.append("                    R mappedValue = ").append(functionParam).append(".apply(fieldValue);\n");
                sb.append("                    if (mappedValue != null) {\n");
                sb.append("                        ").append(resultVar).append(" = mappedValue;\n");
                sb.append("                    }\n");
                sb.append("                }\n");
                sb.append("            } catch (Exception e) {\n");
                sb.append("                // Handle exception silently\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" != null ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".toString() : null;\n");
        } else if (returnType.equals("Optional<R>")) {
            sb.append("        return Optional.ofNullable(").append(resultVar).append(");\n");
        } else if (returnType.equals("Supplier<R>")) {
            sb.append("        return () -> ").append(resultVar).append(";\n");
        } else {
            sb.append("        return ").append(resultVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成过滤方法
     */
    private String generateFilterMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String supplierParam = RandomUtils.generateVariableName("Supplier");
        String predicateParam = RandomUtils.generateVariableName("Predicate");
        String resultVar = RandomUtils.generateVariableName("T");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Supplier<T> ").append(supplierParam).append(", Predicate<T> ").append(predicateParam).append(") {\n");
        sb.append("        T ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(supplierParam).append(" != null && ").append(predicateParam).append(" != null) {\n");
        sb.append("            try {\n");
        sb.append("                T value = ").append(supplierParam).append(".get();\n");
        sb.append("                if (value != null && ").append(predicateParam).append(".test(value)) {\n");
        sb.append("                    ").append(resultVar).append(" = value;\n");
        sb.append("                }\n");
        sb.append("            } catch (Exception e) {\n");
        sb.append("                // Handle exception silently\n");
        sb.append("            }\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            try {\n");
                sb.append("                T fieldValue = ").append(usedField).append(" instanceof Supplier ? ((Supplier<T>) ").append(usedField).append(").get() : null;\n");
                sb.append("                if (fieldValue != null && ").append(predicateParam).append(" != null && ").append(predicateParam).append(".test(fieldValue)) {\n");
                sb.append("                    ").append(resultVar).append(" = fieldValue;\n");
                sb.append("                }\n");
                sb.append("            } catch (Exception e) {\n");
                sb.append("                // Handle exception silently\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" != null ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".toString() : null;\n");
        } else if (returnType.equals("Optional<T>")) {
            sb.append("        return Optional.ofNullable(").append(resultVar).append(");\n");
        } else if (returnType.equals("Supplier<T>")) {
            sb.append("        return () -> ").append(resultVar).append(";\n");
        } else {
            sb.append("        return ").append(resultVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成组合方法
     */
    private String generateComposeMethod(StringBuilder sb, List<String> fieldNames) {
        // 从COMPOSE_TYPES中随机选择
        String composeType = RandomUtils.randomChoice(COMPOSE_TYPES);
        String methodName = RandomUtils.generateMethodName(composeType);
        String supplierParam1 = RandomUtils.generateVariableName("Supplier");
        String supplierParam2 = RandomUtils.generateVariableName("Supplier");
        String resultVar = RandomUtils.generateVariableName("T");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Supplier<T> ").append(supplierParam1).append(", Supplier<T> ").append(supplierParam2).append(") {\n");
        sb.append("        T ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(supplierParam1).append(" != null && ").append(supplierParam2).append(" != null) {\n");
        sb.append("            try {\n");
        sb.append("                T value1 = ").append(supplierParam1).append(".get();\n");
        sb.append("                T value2 = ").append(supplierParam2).append(".get();\n");
        sb.append("                if (value1 != null && value2 != null) {\n");
        sb.append("                    ").append(resultVar).append(" = value1;\n");
        sb.append("                }\n");
        sb.append("            } catch (Exception e) {\n");
        sb.append("                // Handle exception silently\n");
        sb.append("            }\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            try {\n");
                sb.append("                T fieldValue = ").append(usedField).append(" instanceof Supplier ? ((Supplier<T>) ").append(usedField).append(").get() : null;\n");
                sb.append("                if (fieldValue != null) {\n");
                sb.append("                    ").append(resultVar).append(" = fieldValue;\n");
                sb.append("                }\n");
                sb.append("            } catch (Exception e) {\n");
                sb.append("                // Handle exception silently\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" != null ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".toString() : null;\n");
        } else if (returnType.equals("Optional<T>")) {
            sb.append("        return Optional.ofNullable(").append(resultVar).append(");\n");
        } else if (returnType.equals("Supplier<T>")) {
            sb.append("        return () -> ").append(resultVar).append(";\n");
        } else {
            sb.append("        return ").append(resultVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成记忆化方法
     */
    private String generateMemoizeMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String supplierParam = RandomUtils.generateVariableName("Supplier");
        String cacheVar = RandomUtils.generateVariableName("T");
        String resultVar = RandomUtils.generateVariableName("T");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    private T ").append(cacheVar).append(" = null;\n");
        sb.append("    private boolean ").append(cacheVar).append("Cached = false;\n\n");

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Supplier<T> ").append(supplierParam).append(") {\n");
        sb.append("        T ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(cacheVar).append("Cached) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(cacheVar).append(";\n");
        sb.append("        } else if (").append(supplierParam).append(" != null) {\n");
        sb.append("            try {\n");
        sb.append("                ").append(resultVar).append(" = ").append(supplierParam).append(".get();\n");
        sb.append("                if (").append(resultVar).append(" != null) {\n");
        sb.append("                    ").append(cacheVar).append(" = ").append(resultVar).append(";\n");
        sb.append("                    ").append(cacheVar).append("Cached = true;\n");
        sb.append("                }\n");
        sb.append("            } catch (Exception e) {\n");
        sb.append("                // Handle exception silently\n");
        sb.append("            }\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            try {\n");
                sb.append("                T fieldValue = ").append(usedField).append(" instanceof Supplier ? ((Supplier<T>) ").append(usedField).append(").get() : null;\n");
                sb.append("                if (fieldValue != null) {\n");
                sb.append("                    ").append(resultVar).append(" = fieldValue;\n");
                sb.append("                }\n");
                sb.append("            } catch (Exception e) {\n");
                sb.append("                // Handle exception silently\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" != null ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".toString() : null;\n");
        } else if (returnType.equals("Optional<T>")) {
            sb.append("        return Optional.ofNullable(").append(resultVar).append(");\n");
        } else if (returnType.equals("Supplier<T>")) {
            sb.append("        return () -> ").append(resultVar).append(";\n");
        } else {
            sb.append("        return ").append(resultVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成重试方法
     */
    private String generateRetryMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String supplierParam = RandomUtils.generateVariableName("Supplier");
        String retryCountParam = RandomUtils.generateVariableName("int");
        String resultVar = RandomUtils.generateVariableName("T");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(Supplier<T> ").append(supplierParam).append(", int ").append(retryCountParam).append(") {\n");
        sb.append("        T ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(supplierParam).append(" != null) {\n");
        sb.append("            int maxRetries = Math.max(1, ").append(retryCountParam).append(");\n");
        sb.append("            for (int i = 0; i < maxRetries; i++) {\n");
        sb.append("                try {\n");
        sb.append("                    ").append(resultVar).append(" = ").append(supplierParam).append(".get();\n");
        sb.append("                    if (").append(resultVar).append(" != null) {\n");
        sb.append("                        break;\n");
        sb.append("                    }\n");
        sb.append("                } catch (Exception e) {\n");
        sb.append("                    // Retry on exception\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            try {\n");
                sb.append("                T fieldValue = ").append(usedField).append(" instanceof Supplier ? ((Supplier<T>) ").append(usedField).append(").get() : null;\n");
                sb.append("                if (fieldValue != null) {\n");
                sb.append("                    ").append(resultVar).append(" = fieldValue;\n");
                sb.append("                }\n");
                sb.append("            } catch (Exception e) {\n");
                sb.append("                // Handle exception silently\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" != null ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".toString() : null;\n");
        } else if (returnType.equals("Optional<T>")) {
            sb.append("        return Optional.ofNullable(").append(resultVar).append(");\n");
        } else if (returnType.equals("Supplier<T>")) {
            sb.append("        return () -> ").append(resultVar).append(";\n");
        } else {
            sb.append("        return ").append(resultVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成缓存方法
     */
    private String generateCacheMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String keyParam = RandomUtils.generateVariableName("String");
        String supplierParam = RandomUtils.generateVariableName("Supplier");
        String cacheVar = RandomUtils.generateVariableName("Map");
        String resultVar = RandomUtils.generateVariableName("T");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    private Map<String, T> ").append(cacheVar).append(" = new HashMap<>();\n\n");

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(keyParam).append(", Supplier<T> ").append(supplierParam).append(") {\n");
        sb.append("        T ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(keyParam).append(" != null) {\n");
        sb.append("            if (").append(cacheVar).append(".containsKey(").append(keyParam).append(")) {\n");
        sb.append("                ").append(resultVar).append(" = ").append(cacheVar).append(".get(").append(keyParam).append(");\n");
        sb.append("            } else if (").append(supplierParam).append(" != null) {\n");
        sb.append("                try {\n");
        sb.append("                    ").append(resultVar).append(" = ").append(supplierParam).append(".get();\n");
        sb.append("                    if (").append(resultVar).append(" != null) {\n");
        sb.append("                        ").append(cacheVar).append(".put(").append(keyParam).append(", ").append(resultVar).append(");\n");
        sb.append("                    }\n");
        sb.append("                } catch (Exception e) {\n");
        sb.append("                    // Handle exception silently\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            try {\n");
                sb.append("                T fieldValue = ").append(usedField).append(" instanceof Supplier ? ((Supplier<T>) ").append(usedField).append(").get() : null;\n");
                sb.append("                if (fieldValue != null) {\n");
                sb.append("                    ").append(resultVar).append(" = fieldValue;\n");
                sb.append("                }\n");
                sb.append("            } catch (Exception e) {\n");
                sb.append("                // Handle exception silently\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(" != null;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" != null ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" != null ? ").append(resultVar).append(".toString() : null;\n");
        } else if (returnType.equals("Optional<T>")) {
            sb.append("        return Optional.ofNullable(").append(resultVar).append(");\n");
        } else if (returnType.equals("Supplier<T>")) {
            sb.append("        return () -> ").append(resultVar).append(";\n");
        } else {
            sb.append("        return ").append(resultVar).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成调用方法 - 确保所有方法都被调用
     */
    private void generateCallerMethod(StringBuilder sb, String className, List<String> fieldNames, List<String> methodNames) {
        String methodName = RandomUtils.generateMethodName("process");
        String supplierParam = RandomUtils.generateVariableName("Supplier");
        String resultVar = RandomUtils.generateVariableName("T");

        sb.append("    public void ").append(methodName).append("(Supplier<T> ").append(supplierParam).append(") {\n");
        sb.append("        T ").append(resultVar).append(" = null;\n");
        sb.append("        if (").append(supplierParam).append(" != null) {\n");
        sb.append("            try {\n");
        sb.append("                ").append(resultVar).append(" = ").append(supplierParam).append(".get();\n");
        sb.append("            } catch (Exception e) {\n");
        sb.append("                // Handle exception silently\n");
        sb.append("            }\n");
        sb.append("        }\n");

        // 调用所有方法
        for (String name : methodNames) {
            if (name.contains("get")) {
                sb.append("        ").append(name).append("(").append(supplierParam).append(");\n");
            } else if (name.contains("or_else")) {
                sb.append("        ").append(name).append("(").append(supplierParam).append(", ").append(resultVar).append(");\n");
            } else if (name.contains("map")) {
                sb.append("        Function<T, T> ").append(RandomUtils.generateVariableName("Function")).append(" = t -> t;\n");
                sb.append("        ").append(name).append("(").append(supplierParam).append(", ").append(RandomUtils.generateVariableName("Function")).append(");\n");
            } else if (name.contains("filter")) {
                sb.append("        Predicate<T> ").append(RandomUtils.generateVariableName("Predicate")).append(" = t -> t != null;\n");
                sb.append("        ").append(name).append("(").append(supplierParam).append(", ").append(RandomUtils.generateVariableName("Predicate")).append(");\n");
            } else if (name.contains("compose")) {
                sb.append("        ").append(name).append("(").append(supplierParam).append(", ").append(supplierParam).append(");\n");
            } else if (name.contains("memoize") || name.contains("retry") || name.contains("cache")) {
                sb.append("        ").append(name).append("(").append(supplierParam).append(", 3);\n");
            }
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("OPERATION_TYPE")) {
                sb.append("        if (").append(usedField).append(" != null) {\n");
                sb.append("            try {\n");
                sb.append("                T fieldValue = ").append(usedField).append(" instanceof Supplier ? ((Supplier<T>) ").append(usedField).append(").get() : null;\n");
                sb.append("                if (fieldValue != null) {\n");
                sb.append("                    ").append(resultVar).append(" = fieldValue;\n");
                sb.append("                }\n");
                sb.append("            } catch (Exception e) {\n");
                sb.append("                // Handle exception silently\n");
                sb.append("            }\n");
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
