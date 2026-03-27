package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class ExceptionGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] EXCEPTION_TYPES = {
            "NetworkException", "DataException", "ValidationException", "AuthException", "BusinessException",
            "StorageException", "CacheException", "ServiceException", "UIException", "SystemException"
    };

    private static final String[] ERROR_CODES = {
            "ERROR_NETWORK", "ERROR_DATA", "ERROR_VALIDATION", "ERROR_AUTH", "ERROR_BUSINESS",
            "ERROR_STORAGE", "ERROR_CACHE", "ERROR_SERVICE", "ERROR_UI", "ERROR_SYSTEM"
    };

    private static final String[] ERROR_SEVERITY = {
            "CRITICAL", "HIGH", "MEDIUM", "LOW", "INFO"
    };

    private static final String[] ERROR_CATEGORIES = {
            "PERMANENT", "TRANSIENT", "RETRYABLE", "FATAL", "RECOVERABLE"
    };

    private static final String[] ERROR_CONTEXTS = {
            "NETWORK", "DATABASE", "FILE", "MEMORY", "UI", "BACKGROUND", "SYNC", "AUTH"
    };

    public ExceptionGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成异常类");

        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < 5; i++) {
            String className = RandomUtils.generateClassName("Exception");
            generateExceptionClass(className, uiStyle, asyncHandler);
        }
    }

    private void generateExceptionClass(String className, String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("exception"));

        // 基础导入
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.Date"));
        sb.append(generateImportStatement("java.text.SimpleDateFormat"));

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CancellationException"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 功能标志 - 确保所有字段和方法都会被使用
        boolean useTimestamp = RandomUtils.randomBoolean();
        boolean useSeverity = RandomUtils.randomBoolean();
        boolean useCategory = RandomUtils.randomBoolean();
        boolean useContext = RandomUtils.randomBoolean();
        boolean useMetadata = RandomUtils.randomBoolean();
        boolean useRecovery = RandomUtils.randomBoolean();
        boolean useStackTrace = RandomUtils.randomBoolean();
        boolean useUserMessage = RandomUtils.randomBoolean();
        boolean useErrorCode = RandomUtils.randomBoolean();

        sb.append("public class ").append(className).append(" extends Exception {\n\n");

        String exceptionType = EXCEPTION_TYPES[RandomUtils.between(0, EXCEPTION_TYPES.length - 1)];
        String errorCode = ERROR_CODES[RandomUtils.between(0, ERROR_CODES.length - 1)];

        // 基础常量
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String EXCEPTION_TYPE = \"").append(exceptionType).append("\";\n");

        if (useErrorCode) {
            sb.append("    private static final int DEFAULT_ERROR_CODE = \"").append(errorCode).append("\".hashCode();\n");
        }

        if (useSeverity) {
            String severity = ERROR_SEVERITY[RandomUtils.between(0, ERROR_SEVERITY.length - 1)];
            sb.append("    private static final String DEFAULT_SEVERITY = \"").append(severity).append("\";\n");
        }

        if (useCategory) {
            String category = ERROR_CATEGORIES[RandomUtils.between(0, ERROR_CATEGORIES.length - 1)];
            sb.append("    private static final String DEFAULT_CATEGORY = \"").append(category).append("\";\n");
        }

        if (useContext) {
            String context = ERROR_CONTEXTS[RandomUtils.between(0, ERROR_CONTEXTS.length - 1)];
            sb.append("    private static final String DEFAULT_CONTEXT = \"").append(context).append("\";\n");
        }

        sb.append("\n");

        // 基础字段
        sb.append("    private final int code;\n");
        sb.append("    private final String message;\n");
        sb.append("    private final Throwable cause;\n");

        // 功能字段
        if (useTimestamp) {
            sb.append("    private final Date timestamp;\n");
        }

        if (useSeverity) {
            sb.append("    private final String severity;\n");
        }

        if (useCategory) {
            sb.append("    private final String category;\n");
        }

        if (useContext) {
            sb.append("    private final String context;\n");
        }

        if (useMetadata) {
            sb.append("    private final Map<String, Object> metadata;\n");
        }

        if (useUserMessage) {
            sb.append("    private final String userMessage;\n");
        }

        if (useRecovery) {
            sb.append("    private final boolean recoverable;\n");
        }

        sb.append("\n");

        // 基础构造函数
        sb.append("    public ").append(className).append("(int code, String message) {\n");
        sb.append("        this(code, message, null);\n");
        sb.append("    }\n\n");

        sb.append("    public ").append(className).append("(int code, String message, Throwable cause) {\n");
        sb.append("        this(code, message, cause");

        if (useTimestamp) {
            sb.append(", new Date()");
        }

        if (useSeverity) {
            sb.append(", DEFAULT_SEVERITY");
        }

        if (useCategory) {
            sb.append(", DEFAULT_CATEGORY");
        }

        if (useContext) {
            sb.append(", DEFAULT_CONTEXT");
        }

        if (useMetadata) {
            sb.append(", null");
        }

        if (useUserMessage) {
            sb.append(", null");
        }

        if (useRecovery) {
            sb.append(", false");
        }

        sb.append(");\n");
        sb.append("    }\n\n");

        // 完整构造函数
        sb.append("    public ").append(className).append("(");
        sb.append("int code, ");
        sb.append("String message, ");
        sb.append("Throwable cause");

        if (useTimestamp) {
            sb.append(", Date timestamp");
        }

        if (useSeverity) {
            sb.append(", String severity");
        }

        if (useCategory) {
            sb.append(", String category");
        }

        if (useContext) {
            sb.append(", String context");
        }

        if (useMetadata) {
            sb.append(", Map<String, Object> metadata");
        }

        if (useUserMessage) {
            sb.append(", String userMessage");
        }

        if (useRecovery) {
            sb.append(", boolean recoverable");
        }

        sb.append(") {\n");
        sb.append("        super(message, cause);\n");
        sb.append("        this.code = code;\n");
        sb.append("        this.message = message;\n");
        sb.append("        this.cause = cause;\n");

        if (useTimestamp) {
            sb.append("        this.timestamp = timestamp != null ? timestamp : new Date();\n");
        }

        if (useSeverity) {
            sb.append("        this.severity = severity != null ? severity : DEFAULT_SEVERITY;\n");
        }

        if (useCategory) {
            sb.append("        this.category = category != null ? category : DEFAULT_CATEGORY;\n");
        }

        if (useContext) {
            sb.append("        this.context = context != null ? context : DEFAULT_CONTEXT;\n");
        }

        if (useMetadata) {
            sb.append("        this.metadata = metadata != null ? metadata : new HashMap<>();\n");
        }

        if (useUserMessage) {
            sb.append("        this.userMessage = userMessage != null ? userMessage : message;\n");
        }

        if (useRecovery) {
            sb.append("        this.recoverable = recoverable;\n");
        }

        sb.append("        logException();\n");
        sb.append("    }\n\n");

        // 日志方法
        sb.append("    private void logException() {\n");
        sb.append("        StringBuilder logBuilder = new StringBuilder();\n");
        sb.append("        logBuilder.append(\"Exception Type: \").append(EXCEPTION_TYPE);\n");
        sb.append("        logBuilder.append(\", Code: \").append(code);\n");
        sb.append("        logBuilder.append(\", Message: \").append(message);\n");

        if (useTimestamp) {
            sb.append("        logBuilder.append(\", Timestamp: \").append(new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").format(timestamp));\n");
        }

        if (useSeverity) {
            sb.append("        logBuilder.append(\", Severity: \").append(severity);\n");
        }

        if (useCategory) {
            sb.append("        logBuilder.append(\", Category: \").append(category);\n");
        }

        if (useContext) {
            sb.append("        logBuilder.append(\", Context: \").append(context);\n");
        }

        if (useRecovery) {
            sb.append("        logBuilder.append(\", Recoverable: \").append(recoverable);\n");
        }

        sb.append("        Log.e(TAG, logBuilder.toString(), cause);\n");
        sb.append("    }\n\n");

        // Getter方法
        sb.append("    public int getCode() {\n");
        sb.append("        return code;\n");
        sb.append("    }\n\n");

        sb.append("    @Override\n");
        sb.append("    public String getMessage() {\n");
        sb.append("        return message;\n");
        sb.append("    }\n\n");

        sb.append("    @Override\n");
        sb.append("    public Throwable getCause() {\n");
        sb.append("        return cause;\n");
        sb.append("    }\n\n");

        if (useTimestamp) {
            sb.append("    public Date getTimestamp() {\n");
            sb.append("        return timestamp;\n");
            sb.append("    }\n\n");
        }

        if (useSeverity) {
            sb.append("    public String getSeverity() {\n");
            sb.append("        return severity;\n");
            sb.append("    }\n\n");
        }

        if (useCategory) {
            sb.append("    public String getCategory() {\n");
            sb.append("        return category;\n");
            sb.append("    }\n\n");
        }

        if (useContext) {
            sb.append("    public String getContext() {\n");
            sb.append("        return context;\n");
            sb.append("    }\n\n");
        }

        if (useMetadata) {
            sb.append("    public Map<String, Object> getMetadata() {\n");
            sb.append("        return metadata;\n");
            sb.append("    }\n\n");

            sb.append("    public Object getMetadataValue(String key) {\n");
            sb.append("        return metadata.get(key);\n");
            sb.append("    }\n\n");

            sb.append("    public void addMetadata(String key, Object value) {\n");
            sb.append("        metadata.put(key, value);\n");
            sb.append("    }\n\n");
        }

        if (useUserMessage) {
            sb.append("    public String getUserMessage() {\n");
            sb.append("        return userMessage;\n");
            sb.append("    }\n\n");
        }

        if (useRecovery) {
            sb.append("    public boolean isRecoverable() {\n");
            sb.append("        return recoverable;\n");
            sb.append("    }\n\n");
        }

        // 工厂方法
        sb.append("    public static ").append(className).append(" fromException(Exception e) {\n");
        sb.append("        if (e instanceof ").append(className).append(") {\n");
        sb.append("            return (").append(className).append(") e;\n");
        sb.append("        }\n");

        sb.append("        int code = DEFAULT_ERROR_CODE;\n");
        sb.append("        String message = e.getMessage();\n");
        sb.append("        Throwable cause = e.getCause();\n");

        if (useMetadata) {
            sb.append("        Map<String, Object> metadata = new HashMap<>();\n");
            sb.append("        metadata.put(\"original_exception\", e.getClass().getSimpleName());\n");
            sb.append("        metadata.put(\"stack_trace\", Log.getStackTraceString(e));\n");
        }

        sb.append("        return new ").append(className).append("(");
        sb.append("code, ");
        sb.append("message, ");
        sb.append("cause");

        if (useTimestamp) {
            sb.append(", new Date()");
        }

        if (useSeverity) {
            sb.append(", DEFAULT_SEVERITY");
        }

        if (useCategory) {
            sb.append(", DEFAULT_CATEGORY");
        }

        if (useContext) {
            sb.append(", DEFAULT_CONTEXT");
        }

        if (useMetadata) {
            sb.append(", metadata");
        }

        if (useUserMessage) {
            sb.append(", message");
        }

        if (useRecovery) {
            sb.append(", false");
        }

        sb.append(");\n");
        sb.append("    }\n\n");

        // 验证方法
        if (useSeverity) {
            sb.append("    public boolean isCritical() {\n");
            sb.append("        return \"CRITICAL\".equals(severity) || \"HIGH\".equals(severity);\n");
            sb.append("    }\n\n");
        }

        if (useCategory) {
            sb.append("    public boolean isRetryable() {\n");
            sb.append("        return \"RETRYABLE\".equals(category) || \"TRANSIENT\".equals(category);\n");
            sb.append("    }\n\n");
        }

        // 格式化方法
        sb.append("    public String toDetailedString() {\n");
        sb.append("        StringBuilder builder = new StringBuilder();\n");
        sb.append("        builder.append(\"Exception: \").append(EXCEPTION_TYPE).append(\"\\n\");\n");
        sb.append("        builder.append(\"Code: \").append(code).append(\"\\n\");\n");
        sb.append("        builder.append(\"Message: \").append(message).append(\"\\n\");\n");

        if (useTimestamp) {
            sb.append("        builder.append(\"Timestamp: \").append(new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").format(timestamp)).append(\"\\n\");\n");
        }

        if (useSeverity) {
            sb.append("        builder.append(\"Severity: \").append(severity).append(\"\\n\");\n");
        }

        if (useCategory) {
            sb.append("        builder.append(\"Category: \").append(category).append(\"\\n\");\n");
        }

        if (useContext) {
            sb.append("        builder.append(\"Context: \").append(context).append(\"\\n\");\n");
        }

        if (useUserMessage) {
            sb.append("        builder.append(\"User Message: \").append(userMessage).append(\"\\n\");\n");
        }

        if (useRecovery) {
            sb.append("        builder.append(\"Recoverable: \").append(recoverable).append(\"\\n\");\n");
        }

        if (useMetadata) {
            sb.append("        builder.append(\"Metadata:\\n\");\n");
            sb.append("        for (Map.Entry<String, Object> entry : metadata.entrySet()) {\n");
            sb.append("            builder.append(\"  \").append(entry.getKey()).append(\": \").append(entry.getValue()).append(\"\\n\");\n");
            sb.append("        }\n");
        }

        sb.append("        return builder.toString();\n");
        sb.append("    }\n\n");

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "exception");
    }
}
