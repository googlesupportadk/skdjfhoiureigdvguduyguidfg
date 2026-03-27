package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class InterfaceGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] INTERFACE_TYPES = {
            "Callback", "Listener", "Handler", "Observer", "Interceptor",
            "Adapter", "Converter", "Validator", "Provider", "Factory",
            "Builder", "Strategy", "Service", "Repository", "DataSource",
            "Processor", "Transformer", "Filter", "Comparator", "Serializer"
    };

    private static final String[] DATA_TYPES = {
            "String", "int", "long", "boolean", "float", "double",
            "Object", "List<T>", "Map<K,V>", "byte[]", "T", "void"
    };

    private static final String[] PARAMETER_NAMES = {
            "data", "value", "result", "item", "element", "key", "input", "output"
    };

    public InterfaceGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成接口类");

        String asyncHandler = variationManager.getVariation("async_handler");
        String uiStyle = variationManager.getVariation("ui_style");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String interfaceType = INTERFACE_TYPES[RandomUtils.between(0, INTERFACE_TYPES.length - 1)];
            String className = RandomUtils.generateClassName(interfaceType);
            generateInterface(className, asyncHandler, uiStyle);
        }
    }

    private void generateInterface(String className, String asyncHandler, String uiStyle) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("interface"));

        // 功能标志 - 确保生成的接口具有多样性
        boolean useGeneric = RandomUtils.randomBoolean();
        boolean useDefaultMethods = RandomUtils.randomBoolean();
        boolean useStaticMethods = RandomUtils.randomBoolean();
        boolean useAsync = RandomUtils.randomBoolean();
        boolean useFunctional = RandomUtils.randomBoolean();
        boolean useConstants = RandomUtils.randomBoolean();
        boolean useNestedInterfaces = RandomUtils.randomBoolean();
        boolean useAnnotations = RandomUtils.randomBoolean();

        // 根据功能标志添加导入
        if (useAsync && asyncHandler != null && asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
        }

        if (useAnnotations) {
            sb.append(generateImportStatement("java.lang.annotation.*"));
        }

        sb.append("\n");

        // 根据功能标志添加接口注解
        if (useAnnotations) {
            sb.append("@FunctionalInterface\n");
        }

        // 生成接口声明
        sb.append("public interface ").append(className);

        if (useGeneric) {
            sb.append("<T>");
        }

        sb.append(" {\n\n");

        // 根据功能标志添加常量
        if (useConstants) {
            generateConstants(sb, className);
        }

        // 根据功能标志添加嵌套接口
        if (useNestedInterfaces) {
            generateNestedInterfaces(sb, className);
        }

        // 生成方法
        int methodCount = RandomUtils.between(3, 8);
        for (int i = 0; i < methodCount; i++) {
            generateMethod(sb, className, useGeneric, useAsync, asyncHandler, useFunctional);
        }

        // 根据功能标志添加默认方法
        if (useDefaultMethods) {
            generateDefaultMethods(sb, className, useGeneric);
        }

        // 根据功能标志添加静态方法
        if (useStaticMethods) {
            generateStaticMethods(sb, className, useGeneric);
        }

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "interface");
    }

    private void generateConstants(StringBuilder sb, String className) {
        sb.append("    // 常量定义\n");
        sb.append("    String TAG = \"").append(className).append("\";\n");
        sb.append("    int DEFAULT_TIMEOUT = 5000;\n");
        sb.append("    int MAX_RETRIES = 3;\n");
        sb.append("    boolean ENABLE_LOGGING = true;\n\n");
    }

    private void generateNestedInterfaces(StringBuilder sb, String className) {
        sb.append("    // 嵌套接口\n");
        sb.append("    interface ").append(className).append("Config {\n");
        sb.append("        int getTimeout();\n");
        sb.append("        boolean isLoggingEnabled();\n");
        sb.append("    }\n\n");

        sb.append("    interface ").append(className).append("Listener {\n");
        sb.append("        void onSuccess();\n");
        sb.append("        void onError(Throwable error);\n");
        sb.append("    }\n\n");
    }

    private void generateMethod(StringBuilder sb, String className, boolean useGeneric,
                                boolean useAsync, String asyncHandler, boolean useFunctional) {
        String methodName = RandomUtils.generateMethodName("on");
        String returnType = DATA_TYPES[RandomUtils.between(0, DATA_TYPES.length - 1)];

        // 根据功能标志调整返回类型
        if (useGeneric && returnType.contains("T")) {
            if (!returnType.equals("T")) {
                returnType = returnType.replace("T", "T");
            }
        }

        sb.append("    ").append(returnType).append(" ").append(methodName).append("(");

        // 生成参数
        int paramCount = RandomUtils.between(1, 3);
        for (int j = 0; j < paramCount; j++) {
            if (j > 0) sb.append(", ");

            String paramType = DATA_TYPES[RandomUtils.between(0, DATA_TYPES.length - 1)];
            if (useGeneric && paramType.contains("T")) {
                paramType = paramType.replace("T", "T");
            }

            String paramName = PARAMETER_NAMES[RandomUtils.between(0, PARAMETER_NAMES.length - 1)];
            sb.append(paramType).append(" ").append(paramName);
        }

        sb.append(");\n\n");
    }

    private void generateDefaultMethods(StringBuilder sb, String className, boolean useGeneric) {
        sb.append("    // 默认方法实现\n");

        String methodName1 = RandomUtils.generateMethodName("handle");
        sb.append("    default void ").append(methodName1).append("(String message) {\n");
        sb.append("        System.out.println(TAG + \": \" + message);\n");
        sb.append("    }\n\n");

        String methodName2 = RandomUtils.generateMethodName("process");
        sb.append("    default boolean ").append(methodName2).append("(Object data) {\n");
        sb.append("        return data != null;\n");
        sb.append("    }\n\n");

        if (useGeneric) {
            String methodName3 = RandomUtils.generateMethodName("transform");
            sb.append("    default <T> T ").append(methodName3).append("(T input) {\n");
            sb.append("        return input;\n");
            sb.append("    }\n\n");
        }
    }

    private void generateStaticMethods(StringBuilder sb, String className, boolean useGeneric) {
        sb.append("    // 静态工具方法\n");

        String methodName1 = RandomUtils.generateMethodName("create");
        sb.append("    static ").append(className).append(" create() {\n");
        sb.append("        return new ").append(className).append("Impl();\n");
        sb.append("    }\n\n");

        String methodName2 = RandomUtils.generateMethodName("validate");
        sb.append("    static boolean ").append(methodName2).append("(Object value) {\n");
        sb.append("        return value != null;\n");
        sb.append("    }\n\n");

        if (useGeneric) {
            String methodName3 = RandomUtils.generateMethodName("convert");
            sb.append("    static <T> T ").append(methodName3).append("(Object input, Class<T> type) {\n");
            sb.append("        if (input == null) return null;\n");
            sb.append("        if (type.isInstance(input)) {\n");
            sb.append("            return type.cast(input);\n");
            sb.append("        }\n");
            sb.append("        return null;\n");
            sb.append("    }\n\n");
        }
    }
}