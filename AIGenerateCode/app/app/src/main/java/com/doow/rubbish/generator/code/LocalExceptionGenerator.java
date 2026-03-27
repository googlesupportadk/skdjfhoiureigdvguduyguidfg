package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalExceptionGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    // 扩展异常类型，增加更多随机性
    private static final String[] EXCEPTION_TYPES = {
            "NetworkException", "DataException", "ValidationException", "AuthException", "BusinessException",
            "StorageException", "CacheException", "CalculationException", "ConversionException", "FormatException",
            "ProcessingException", "ExecutionException", "InitializationException", "ConfigurationException", "StateException"
    };

    // 扩展错误代码，增加更多随机性
    private static final String[] ERROR_CODES = {
            "ERROR_NETWORK", "ERROR_DATA", "ERROR_VALIDATION", "ERROR_AUTH", "ERROR_BUSINESS",
            "ERROR_STORAGE", "ERROR_CACHE", "ERROR_CALCULATION", "ERROR_CONVERSION", "ERROR_FORMAT",
            "ERROR_PROCESSING", "ERROR_EXECUTION", "ERROR_INITIALIZATION", "ERROR_CONFIGURATION", "ERROR_STATE"
    };

    // 添加异常处理策略
    private static final String[] HANDLING_STRATEGIES = {
            "RETRY", "IGNORE", "LOG_AND_CONTINUE", "THROW", "RECOVER"
    };

    // 添加异常恢复方法
    private static final String[] RECOVERY_METHODS = {
            "reset", "retry", "fallback", "alternate", "default"
    };

    public LocalExceptionGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成异常类");

        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 随机生成5-10个异常类
        int exceptionCount = RandomUtils.between(5, 10);
        for (int i = 0; i < exceptionCount; i++) {
            String className = RandomUtils.generateClassName("Exception");
            generateExceptionClass(className, uiStyle, asyncHandler);
        }
    }

    private void generateExceptionClass(String className, String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("exception"));

        // 根据随机标志决定是否导入日志
        boolean useLog = RandomUtils.randomBoolean();
        if (useLog) {
            sb.append(generateImportStatement("android.util.Log"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 随机选择异常类型和错误代码
        String exceptionType = EXCEPTION_TYPES[RandomUtils.between(0, EXCEPTION_TYPES.length - 1)];
        String errorCode = ERROR_CODES[RandomUtils.between(0, ERROR_CODES.length - 1)];
        String handlingStrategy = HANDLING_STRATEGIES[RandomUtils.between(0, HANDLING_STRATEGIES.length - 1)];
        String recoveryMethod = RECOVERY_METHODS[RandomUtils.between(0, RECOVERY_METHODS.length - 1)];

        // 随机决定是否添加文档注释
        if (RandomUtils.randomBoolean()) {
            sb.append("/**\n");
            sb.append(" * ").append(exceptionType).append(" for handling specific error conditions\n");
            sb.append(" * Error code: ").append(errorCode).append("\n");
            sb.append(" * Handling strategy: ").append(handlingStrategy).append("\n");
            sb.append(" */\n");
        }

        sb.append("public class ").append(className).append(" extends Exception {\n\n");

        // 随机生成TAG，但确保只生成一次
        String tagVar = RandomUtils.generateWord(6) + "Tag";
        if (useLog) {
            sb.append("    private static final String ").append(tagVar).append(" = \"").append(className).append("\";\n");
        }

        // 随机生成常量
        int constantCount = RandomUtils.between(1, 3);
        for (int i = 0; i < constantCount; i++) {
            String constantName = RandomUtils.generateWord(6).toUpperCase();
            String constantValue = RandomUtils.generateWord(8);
            sb.append("    private static final String ").append(constantName).append(" = \"").append(constantValue).append("\";\n");
        }

        sb.append("    private static final String ").append(RandomUtils.generateWord(6).toUpperCase()).append("TYPE = \"").append(exceptionType).append("\";\n");
        sb.append("    private static final String ").append(RandomUtils.generateWord(6).toUpperCase()).append("CODE = \"").append(errorCode).append("\";\n");
        sb.append("    private static final String ").append(RandomUtils.generateWord(6).toUpperCase()).append("STRATEGY = \"").append(handlingStrategy).append("\";\n");
        sb.append("    private static final String ").append(RandomUtils.generateWord(6).toUpperCase()).append("RECOVERY = \"").append(recoveryMethod).append("\";\n\n");

        // 随机生成字段
        int fieldCount = RandomUtils.between(2, 5);
        String[] fieldNames = new String[fieldCount];
        String[] fieldTypes = new String[fieldCount];

        for (int i = 0; i < fieldCount; i++) {
            fieldTypes[i] = RandomUtils.randomChoice(new String[]{"int", "long", "String", "boolean", "float", "double"});
            fieldNames[i] = RandomUtils.generateWord(6);
            sb.append("    private final ").append(fieldTypes[i]).append(" ").append(fieldNames[i]).append(";\n");
        }
        sb.append("\n");

        // 随机生成构造函数
        boolean useSimpleConstructor = RandomUtils.randomBoolean();
        if (useSimpleConstructor) {
            sb.append("    public ").append(className).append("() {\n");
            sb.append("        this(");
            for (int i = 0; i < fieldCount; i++) {
                sb.append(generateDefaultValue(fieldTypes[i]));
                if (i < fieldCount - 1) {
                    sb.append(", ");
                }
            }
            sb.append(");\n");
            sb.append("    }\n\n");
        }

        sb.append("    public ").append(className).append("(");
        for (int i = 0; i < fieldCount; i++) {
            sb.append(fieldTypes[i]).append(" ").append(fieldNames[i]);
            if (i < fieldCount - 1) {
                sb.append(", ");
            }
        }
        sb.append(") {\n");
        sb.append("        super(");
        if (fieldCount > 0 && fieldTypes[0].equals("String")) {
            sb.append(fieldNames[0]);
        } else {
            sb.append("\"").append(exceptionType).append("\"");
        }
        sb.append(");\n");

        for (int i = 0; i < fieldCount; i++) {
            sb.append("        this.").append(fieldNames[i]).append(" = ").append(fieldNames[i]).append(";\n");
        }

        // 随机添加日志，但确保不超过5个
        if (useLog && RandomUtils.randomBoolean()) {
            sb.append("        Log.").append(RandomUtils.randomChoice(new String[]{"d", "e", "w", "i"}))
                    .append("(").append(tagVar).append(", \"").append(exceptionType).append(" created with ")
                    .append(handlingStrategy).append(" strategy\");\n");
        }

        sb.append("    }\n\n");

        // 随机生成getter方法
        for (int i = 0; i < fieldCount; i++) {
            sb.append("    public ").append(fieldTypes[i]).append(" get").append(capitalizeFirstLetter(fieldNames[i])).append("() {\n");
            sb.append("        return ").append(fieldNames[i]).append(";\n");
            sb.append("    }\n\n");
        }

        // 随机生成setter方法
        boolean generateSetters = RandomUtils.randomBoolean();
        if (generateSetters) {
            for (int i = 0; i < fieldCount; i++) {
                sb.append("    public void set").append(capitalizeFirstLetter(fieldNames[i])).append("(")
                        .append(fieldTypes[i]).append(" ").append(fieldNames[i]).append(") {\n");
                sb.append("        this.").append(fieldNames[i]).append(" = ").append(fieldNames[i]).append(";\n");

                // 随机添加日志，但确保不超过5个
                if (useLog && RandomUtils.randomBoolean()) {
                    sb.append("        Log.").append(RandomUtils.randomChoice(new String[]{"d", "e", "w", "i"}))
                            .append("(").append(tagVar).append(", \"").append(fieldNames[i]).append(" set to \" + ")
                            .append(fieldNames[i]).append(");\n");
                }

                sb.append("    }\n\n");
            }
        }

        // 随机生成toString方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    @Override\n");
            sb.append("    public String toString() {\n");
            sb.append("        return \"").append(className).append("{\" +\n");
            for (int i = 0; i < fieldCount; i++) {
                sb.append("                \"").append(fieldNames[i]).append("=\" + ").append(fieldNames[i]);
                if (i < fieldCount - 1) {
                    sb.append(" + \",\" +\n");
                } else {
                    sb.append(" +\n");
                }
            }
            sb.append("                '}';\n");
            sb.append("    }\n\n");
        }

        // 随机生成equals方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    @Override\n");
            sb.append("    public boolean equals(Object o) {\n");
            sb.append("        if (this == o) return true;\n");
            sb.append("        if (o == null || getClass() != o.getClass()) return false;\n");
            sb.append("        ").append(className).append(" that = (").append(className).append(") o;\n");
            sb.append("        return ");
            for (int i = 0; i < fieldCount; i++) {
                if (fieldTypes[i].equals("boolean")) {
                    sb.append(fieldNames[i]).append(" == that.").append(fieldNames[i]);
                } else {
                    sb.append(fieldNames[i]).append(" != null ? ").append(fieldNames[i])
                            .append(".equals(that.").append(fieldNames[i]).append(") : that.").append(fieldNames[i]).append(" == null");
                }
                if (i < fieldCount - 1) {
                    sb.append(" &&\n               ");
                }
            }
            sb.append(";\n");
            sb.append("    }\n\n");
        }

        // 随机生成hashCode方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    @Override\n");
            sb.append("    public int hashCode() {\n");
            sb.append("        int result = ");
            if (fieldCount > 0 && fieldTypes[0].equals("int")) {
                sb.append(fieldNames[0]);
            } else if (fieldCount > 0 && fieldTypes[0].equals("boolean")) {
                sb.append(fieldNames[0]).append(" ? 1 : 0");
            } else {
                sb.append(fieldNames[0]).append(" != null ? ").append(fieldNames[0]).append(".hashCode() : 0");
            }
            sb.append(";\n");
            for (int i = 1; i < fieldCount; i++) {
                sb.append("        result = 31 * result + ");
                if (fieldTypes[i].equals("int")) {
                    sb.append(fieldNames[i]);
                } else if (fieldTypes[i].equals("boolean")) {
                    sb.append(fieldNames[i]).append(" ? 1 : 0");
                } else {
                    sb.append(fieldNames[i]).append(" != null ? ").append(fieldNames[i]).append(".hashCode() : 0");
                }
                sb.append(";\n");
            }
            sb.append("        return result;\n");
            sb.append("    }\n\n");
        }

        // 随机生成fromException方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public static ").append(className).append(" fromException(Exception e) {\n");
            sb.append("        if (e instanceof ").append(className).append(") {\n");
            sb.append("            return (").append(className).append(") e;\n");
            sb.append("        }\n");
            sb.append("        return new ").append(className).append("(\n");
            for (int i = 0; i < fieldCount; i++) {
                sb.append("            ").append(generateConversionFromException(fieldTypes[i], "e"));
                if (i < fieldCount - 1) {
                    sb.append(",\n");
                } else {
                    sb.append("\n");
                }
            }
            sb.append("        );\n");
            sb.append("    }\n\n");
        }

        // 随机生成handle方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void handle() {\n");
            sb.append("        switch (").append(RandomUtils.generateWord(6).toUpperCase()).append("STRATEGY) {\n");
            sb.append("            case \"RETRY\":\n");
            sb.append("                // Retry logic would be implemented here\n");
            if (useLog && RandomUtils.randomBoolean()) {
                sb.append("                Log.").append(RandomUtils.randomChoice(new String[]{"d", "e", "w", "i"}))
                        .append("(").append(tagVar).append(", \"Retrying operation\");\n");
            }
            sb.append("                break;\n");
            sb.append("            case \"IGNORE\":\n");
            sb.append("                // Ignore the exception\n");
            if (useLog && RandomUtils.randomBoolean()) {
                sb.append("                Log.").append(RandomUtils.randomChoice(new String[]{"d", "e", "w", "i"}))
                        .append("(").append(tagVar).append(", \"Ignoring exception\");\n");
            }
            sb.append("                break;\n");
            sb.append("            case \"LOG_AND_CONTINUE\":\n");
            if (useLog) {
                sb.append("                Log.").append(RandomUtils.randomChoice(new String[]{"d", "e", "w", "i"}))
                        .append("(").append(tagVar).append(", \"Logging exception and continuing\");\n");
            }
            sb.append("                break;\n");
            sb.append("            case \"THROW\":\n");
            sb.append("                // Re-throw the exception\n");
            if (useLog && RandomUtils.randomBoolean()) {
                sb.append("                Log.").append(RandomUtils.randomChoice(new String[]{"d", "e", "w", "i"}))
                        .append("(").append(tagVar).append(", \"Re-throwing exception\");\n");
            }
            sb.append("                throw this;\n");
            sb.append("            case \"RECOVER\":\n");
            sb.append("                // Recovery logic would be implemented here\n");
            if (useLog && RandomUtils.randomBoolean()) {
                sb.append("                Log.").append(RandomUtils.randomChoice(new String[]{"d", "e", "w", "i"}))
                        .append("(").append(tagVar).append(", \"Attempting recovery\");\n");
            }
            sb.append("                break;\n");
            sb.append("            default:\n");
            if (useLog && RandomUtils.randomBoolean()) {
                sb.append("                Log.").append(RandomUtils.randomChoice(new String[]{"d", "e", "w", "i"}))
                        .append("(").append(tagVar).append(", \"Unknown handling strategy\");\n");
            }
            sb.append("                break;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 随机生成与其它生成器关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void ").append(RandomUtils.generateWord(6)).append("With").append(RandomUtils.generateClassName("Handler"))
                    .append("(Object handler) {\n");
            sb.append("        // This method would interact with handlers from other generators\n");
            if (useLog && RandomUtils.randomBoolean()) {
                sb.append("        Log.").append(RandomUtils.randomChoice(new String[]{"d", "e", "w", "i"}))
                        .append("(").append(tagVar).append(", \"Handling with external handler\");\n");
            }
            sb.append("        // Implementation would depend on the handler type\n");
            sb.append("        if (handler != null) {\n");
            sb.append("            // Process with handler\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "exception");
    }

    // 辅助方法：生成默认值
    private String generateDefaultValue(String type) {
        switch (type) {
            case "int":
                return "0";
            case "long":
                return "0L";
            case "float":
                return "0.0f";
            case "double":
                return "0.0";
            case "boolean":
                return "false";
            case "String":
                return "\"\"";
            default:
                return "null";
        }
    }

    // 辅助方法：从异常转换值
    private String generateConversionFromException(String type, String exceptionVar) {
        switch (type) {
            case "int":
                return exceptionVar + ".getMessage() != null ? " + exceptionVar + ".getMessage().hashCode() : 0";
            case "long":
                return exceptionVar + ".getMessage() != null ? (long) " + exceptionVar + ".getMessage().hashCode() : 0L";
            case "float":
                return exceptionVar + ".getMessage() != null ? (float) " + exceptionVar + ".getMessage().hashCode() : 0.0f";
            case "double":
                return exceptionVar + ".getMessage() != null ? (double) " + exceptionVar + ".getMessage().hashCode() : 0.0";
            case "boolean":
                return exceptionVar + ".getMessage() != null && " + exceptionVar + ".getMessage().length() > 0";
            case "String":
                return exceptionVar + ".getMessage() != null ? " + exceptionVar + ".getMessage() : \"\"";
            default:
                return "null";
        }
    }

    // 辅助方法：首字母大写
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
