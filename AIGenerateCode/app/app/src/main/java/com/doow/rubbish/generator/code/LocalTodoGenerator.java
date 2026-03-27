package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.*;

/**
 * 升级版待办事项代码生成器 - 支持随机功能组合和多样性生成
 */
public class LocalTodoGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    // 待办事项类型
    private static final String[] TODO_TYPES = {
            "task", "project", "goal", "habit", "routine",
            "reminder", "deadline", "milestone", "checklist", "schedule",
            "subtask", "dependency", "tag", "category", "filter",
            "search", "sort", "group", "archive", "template"
    };

    // 优先级级别
    private static final String[] PRIORITY_LEVELS = {
            "high", "medium", "low", "urgent", "normal",
            "critical", "important", "minor", "major", "none",
            "deferred", "someday", "waiting", "in_progress", "done"
    };

    // 操作类型
    private static final String[] OPERATION_TYPES = {
            "create", "update", "delete", "search", "filter",
            "sort", "group", "archive", "restore", "complete",
            "prioritize", "schedule", "remind", "sync", "export"
    };

    // 数据类型
    private static final String[] DATA_TYPES = {
            "int", "long", "boolean", "String", "Date",
            "List<String>", "Map<String, String>", "List<Date>", "int[]"
    };

    // 返回类型
    private static final String[] RETURN_TYPES = {
            "int", "long", "boolean", "String", "Date",
            "List<String>", "Map<String, String>", "List<Date>", "int[]"
    };

    // 状态类型
    private static final String[] STATUS_TYPES = {
            "pending", "in_progress", "completed", "cancelled", "deferred",
            "archived", "deleted", "draft", "published", "scheduled"
    };

    public LocalTodoGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地待办事项相关代码 - 升级版");

        // 随机生成5-15个待办事项类
        int classCount = RandomUtils.between(5, 15);
        for (int i = 0; i < classCount; i++) {
            String className = RandomUtils.generateClassName("Todo");
            String todoType = RandomUtils.randomChoice(TODO_TYPES);
            generateTodoClass(className, todoType);
        }
    }

    /**
     * 生成待办事项类
     */
    private void generateTodoClass(String className, String todoType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 包声明
        sb.append(generatePackageDeclaration("todo"));

        // 导入语句
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Date"));
        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成类成员变量
        List<String> fieldNames = generateFields(sb, todoType);

        // 生成构造方法
        generateConstructor(sb, className, fieldNames);

        // 生成核心方法
        List<String> methodNames = new ArrayList<>();

        // 根据待办事项类型生成不同的方法组合
        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateCreateMethod(sb, fieldNames, todoType));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateUpdateMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateDeleteMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateSearchMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateFilterMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateSortMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateCompleteMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateArchiveMethod(sb, fieldNames));
        }

        // 生成调用方法 - 确保所有方法都被调用
        if (methodNames.size() > 0) {
            generateCallerMethod(sb, className, fieldNames, methodNames);
        }

        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "todo");
    }

    /**
     * 生成类成员变量
     */
    private List<String> generateFields(StringBuilder sb, String todoType) {
        List<String> fieldNames = new ArrayList<>();

        // 生成随机数量的字段
        int fieldCount = RandomUtils.between(3, 8);

        // 常量字段
        String tagName = RandomUtils.generateWord(6);
        sb.append("    private static final String TAG = \"" + tagName + "\";\n");
        fieldNames.add("TAG");

        String todoTypeField = RandomUtils.generateWord(6);
        sb.append("    private static final String TODO_TYPE = \"" + todoType + "\";\n");
        fieldNames.add("TODO_TYPE");

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
                } else if (fieldType.equals("List<Date>")) {
                    sb.append(" = new ArrayList<>()");
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
            if (fieldName.equals("TAG") || fieldName.equals("TODO_TYPE")) {
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
                    sb.append("        this.").append(fieldName).append(" = new Date();\n");
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
     * 生成创建方法
     */
    private String generateCreateMethod(StringBuilder sb, List<String> fieldNames, String todoType) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String titleParam = RandomUtils.generateVariableName("String");
        String descParam = RandomUtils.generateVariableName("String");
        String priorityParam = RandomUtils.generateVariableName("String");
        String dateParam = RandomUtils.generateVariableName("Date");
        String resultVar = RandomUtils.generateVariableName("boolean");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(titleParam).append(", String ").append(descParam).append(", String ").append(priorityParam).append(", Date ").append(dateParam).append(") {\n");
        sb.append("        boolean ").append(resultVar).append(" = false;\n");
        sb.append("        if (").append(titleParam).append(" != null && !").append(titleParam).append(".isEmpty()) {\n");
        sb.append("            ").append(resultVar).append(" = true;\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TODO_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" ? \"success\" : \"failure\";\n");
        } else if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(" ? ").append(dateParam).append(" : null;\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(").append(titleParam).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"title\", ").append(titleParam).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(").append(dateParam).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        int[] ").append(RandomUtils.generateVariableName("array")).append(" = new int[1];\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[0] = ").append(resultVar).append(" ? 1 : 0;\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成更新方法
     */
    private String generateUpdateMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String idParam = RandomUtils.generateVariableName("int");
        String titleParam = RandomUtils.generateVariableName("String");
        String descParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("boolean");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(int ").append(idParam).append(", String ").append(titleParam).append(", String ").append(descParam).append(") {\n");
        sb.append("        boolean ").append(resultVar).append(" = false;\n");
        sb.append("        if (").append(idParam).append(" >= 0) {\n");
        sb.append("            ").append(resultVar).append(" = true;\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TODO_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" ? \"success\" : \"failure\";\n");
        } else if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(" ? new Date() : null;\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(").append(titleParam).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"title\", ").append(titleParam).append(");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(new Date());\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        int[] ").append(RandomUtils.generateVariableName("array")).append(" = new int[1];\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[0] = ").append(resultVar).append(" ? 1 : 0;\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成删除方法
     */
    private String generateDeleteMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String idParam = RandomUtils.generateVariableName("int");
        String resultVar = RandomUtils.generateVariableName("boolean");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(int ").append(idParam).append(") {\n");
        sb.append("        boolean ").append(resultVar).append(" = false;\n");
        sb.append("        if (").append(idParam).append(" >= 0) {\n");
        sb.append("            ").append(resultVar).append(" = true;\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TODO_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" ? \"success\" : \"failure\";\n");
        } else if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(" ? new Date() : null;\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(\"deleted\");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", \"deleted\");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(new Date());\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        int[] ").append(RandomUtils.generateVariableName("array")).append(" = new int[1];\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[0] = ").append(resultVar).append(" ? 1 : 0;\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成搜索方法
     */
    private String generateSearchMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String queryParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("List");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(queryParam).append(") {\n");
        sb.append("        List<String> ").append(resultVar).append(" = new ArrayList<>();\n");
        sb.append("        if (").append(queryParam).append(" != null && !").append(queryParam).append(".isEmpty()) {\n");
        sb.append("            ").append(resultVar).append(".add(").append(queryParam).append(");\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TODO_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(".size();\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(".size() > 0;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(".size();\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(".isEmpty() ? \"\" : ").append(resultVar).append(".get(0);\n");
        } else if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(".isEmpty() ? null : new Date();\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        for (int i = 0; i < ").append(resultVar).append(".size(); i++) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"item\" + i, ").append(resultVar).append(".get(i));\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (!").append(resultVar).append(".isEmpty()) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(new Date());\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        int[] ").append(RandomUtils.generateVariableName("array")).append(" = new int[").append(resultVar).append(".size()];\n");
            sb.append("        for (int i = 0; i < ").append(resultVar).append(".size(); i++) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("array")).append("[i] = i;\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
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
        String statusParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("List");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(statusParam).append(") {\n");
        sb.append("        List<String> ").append(resultVar).append(" = new ArrayList<>();\n");
        sb.append("        if (").append(statusParam).append(" != null && !").append(statusParam).append(".isEmpty()) {\n");
        sb.append("            ").append(resultVar).append(".add(").append(statusParam).append(");\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TODO_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(".size();\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(".size() > 0;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(".size();\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(".isEmpty() ? \"\" : ").append(resultVar).append(".get(0);\n");
        } else if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(".isEmpty() ? null : new Date();\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        for (int i = 0; i < ").append(resultVar).append(".size(); i++) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"item\" + i, ").append(resultVar).append(".get(i));\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (!").append(resultVar).append(".isEmpty()) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(new Date());\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        int[] ").append(RandomUtils.generateVariableName("array")).append(" = new int[").append(resultVar).append(".size()];\n");
            sb.append("        for (int i = 0; i < ").append(resultVar).append(".size(); i++) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("array")).append("[i] = i;\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成排序方法
     */
    private String generateSortMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String orderParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("List");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(String ").append(orderParam).append(") {\n");
        sb.append("        List<String> ").append(resultVar).append(" = new ArrayList<>();\n");
        sb.append("        if (").append(orderParam).append(" != null && !").append(orderParam).append(".isEmpty()) {\n");
        sb.append("            ").append(resultVar).append(".add(").append(orderParam).append(");\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TODO_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(".size();\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(".size() > 0;\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(".size();\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(".isEmpty() ? \"\" : ").append(resultVar).append(".get(0);\n");
        } else if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(".isEmpty() ? null : new Date();\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        for (int i = 0; i < ").append(resultVar).append(".size(); i++) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"item\" + i, ").append(resultVar).append(".get(i));\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (!").append(resultVar).append(".isEmpty()) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(new Date());\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        int[] ").append(RandomUtils.generateVariableName("array")).append(" = new int[").append(resultVar).append(".size()];\n");
            sb.append("        for (int i = 0; i < ").append(resultVar).append(".size(); i++) {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("array")).append("[i] = i;\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成完成方法
     */
    private String generateCompleteMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String idParam = RandomUtils.generateVariableName("int");
        String completedParam = RandomUtils.generateVariableName("boolean");
        String resultVar = RandomUtils.generateVariableName("boolean");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(int ").append(idParam).append(", boolean ").append(completedParam).append(") {\n");
        sb.append("        boolean ").append(resultVar).append(" = false;\n");
        sb.append("        if (").append(idParam).append(" >= 0) {\n");
        sb.append("            ").append(resultVar).append(" = ").append(completedParam).append(";\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TODO_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" ? \"success\" : \"failure\";\n");
        } else if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(" ? new Date() : null;\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(\"completed\");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", \"completed\");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(new Date());\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        int[] ").append(RandomUtils.generateVariableName("array")).append(" = new int[1];\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[0] = ").append(resultVar).append(" ? 1 : 0;\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成归档方法
     */
    private String generateArchiveMethod(StringBuilder sb, List<String> fieldNames) {
        // 从OPERATION_TYPES中随机选择
        String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
        String methodName = RandomUtils.generateMethodName(operationType);
        String idParam = RandomUtils.generateVariableName("int");
        String resultVar = RandomUtils.generateVariableName("boolean");

        // 从RETURN_TYPES中随机选择返回类型
        String returnType = RandomUtils.randomChoice(RETURN_TYPES);

        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(int ").append(idParam).append(") {\n");
        sb.append("        boolean ").append(resultVar).append(" = false;\n");
        sb.append("        if (").append(idParam).append(" >= 0) {\n");
        sb.append("            ").append(resultVar).append(" = true;\n");
        sb.append("        }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TODO_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 根据返回类型生成不同的返回语句
        if (returnType.equals("boolean")) {
            sb.append("        return ").append(resultVar).append(";\n");
        } else if (returnType.equals("int") || returnType.equals("long")) {
            sb.append("        return ").append(resultVar).append(" ? 1 : 0;\n");
        } else if (returnType.equals("String")) {
            sb.append("        return ").append(resultVar).append(" ? \"success\" : \"failure\";\n");
        } else if (returnType.equals("Date")) {
            sb.append("        return ").append(resultVar).append(" ? new Date() : null;\n");
        } else if (returnType.equals("List<String>")) {
            sb.append("        List<String> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(\"archived\");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("Map<String, String>")) {
            sb.append("        Map<String, String> ").append(RandomUtils.generateVariableName("Map")).append(" = new HashMap<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("Map")).append(".put(\"result\", \"archived\");\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("Map")).append(";\n");
        } else if (returnType.equals("List<Date>")) {
            sb.append("        List<Date> ").append(RandomUtils.generateVariableName("List")).append(" = new ArrayList<>();\n");
            sb.append("        if (").append(resultVar).append(") {\n");
            sb.append("            ").append(RandomUtils.generateVariableName("List")).append(".add(new Date());\n");
            sb.append("        }\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("List")).append(";\n");
        } else if (returnType.equals("int[]")) {
            sb.append("        int[] ").append(RandomUtils.generateVariableName("array")).append(" = new int[1];\n");
            sb.append("        ").append(RandomUtils.generateVariableName("array")).append("[0] = ").append(resultVar).append(" ? 1 : 0;\n");
            sb.append("        return ").append(RandomUtils.generateVariableName("array")).append(";\n");
        }

        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成调用方法 - 确保所有方法都被调用
     */
    private void generateCallerMethod(StringBuilder sb, String className, List<String> fieldNames, List<String> methodNames) {
        String methodName = RandomUtils.generateMethodName("process");
        String inputVar1 = RandomUtils.generateVariableName("String");
        String inputVar2 = RandomUtils.generateVariableName("int");
        String inputVar3 = RandomUtils.generateVariableName("boolean");
        String dateVar = RandomUtils.generateVariableName("Date");
        String resultVar = RandomUtils.generateVariableName("double");
        String arrayVar = RandomUtils.generateVariableName("double[]");

        sb.append("    public double ").append(methodName).append("(String ").append(inputVar1).append(", int ").append(inputVar2).append(", boolean ").append(inputVar3).append(") {\n");
        sb.append("        double ").append(resultVar).append(" = 0;\n");
        sb.append("        Date ").append(dateVar).append(" = new Date();\n");
        sb.append("        double[] ").append(arrayVar).append(" = {").append(RandomUtils.nextDouble(0.0, 100.0)).append(", ").append(RandomUtils.nextDouble(0.0, 100.0)).append("};\n");

        // 调用所有方法
        for (String name : methodNames) {
            if (name.contains("create")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(").append(inputVar1).append(", \"desc\", \"high\", ").append(dateVar).append(") ? 1.0 : 0.0;\n");
            } else if (name.contains("update")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(").append(inputVar2).append(", \"title\", \"description\") ? 1.0 : 0.0;\n");
            } else if (name.contains("delete")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(").append(inputVar2).append(") ? 1.0 : 0.0;\n");
            } else if (name.contains("search")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(").append(inputVar1).append(").size();\n");
            } else if (name.contains("filter")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(\"completed\").size();\n");
            } else if (name.contains("sort")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(\"asc\").size();\n");
            } else if (name.contains("complete")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(").append(inputVar2).append(", ").append(inputVar3).append(") ? 1.0 : 0.0;\n");
            } else if (name.contains("archive")) {
                sb.append("        ").append(resultVar).append(" = ").append(name).append("(").append(inputVar2).append(") ? 1.0 : 0.0;\n");
            }
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("TODO_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        // 随机添加日志
        if (RandomUtils.randomBoolean() && RandomUtils.randomBoolean()) {
            sb.append("        System.out.println(TAG + \" processed\");\n");
        }

        sb.append("        return ").append(resultVar).append(";\n");
        sb.append("    }\n\n");
    }
}
