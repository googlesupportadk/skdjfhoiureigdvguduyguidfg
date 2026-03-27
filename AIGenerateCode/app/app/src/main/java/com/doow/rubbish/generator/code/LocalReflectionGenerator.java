package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalReflectionGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] OPERATION_TYPES = {
            "get_class", "get_class_name", "get_super_class", "get_interfaces",
            "get_methods", "get_fields", "get_constructors", "get_annotations",
            "get_declared_methods", "get_declared_fields", "get_declared_constructors", "get_declared_annotations",
            "get_package", "get_class_loader", "get_resource", "get_signers"
    };

    private static final String[] METHOD_TYPES = {
            "invoke", "get_return_type", "get_parameter_types", "get_parameter_count",
            "is_public", "is_private", "is_protected", "is_static",
            "is_final", "is_synchronized", "is_varargs", "is_bridge", "is_synthetic"
    };

    private static final String[] FIELD_TYPES = {
            "get_value", "set_value", "get_type", "get_name",
            "is_final", "is_static", "is_volatile", "is_transient",
            "is_synthetic", "is_enum_constant", "get_declaring_class", "get_modifiers"
    };

    // 功能标志
    private boolean useAsyncOperations;
    private boolean useValidation;
    private boolean useTransformation;
    private boolean useChaining;
    private boolean useLogging;
    private boolean useCaching;
    private boolean useErrorHandling;
    private boolean useCustomReflection;

    public LocalReflectionGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        initializeFeatureFlags();
    }

    /**
     * 初始化功能标志，确保随机性和多样性
     */
    private void initializeFeatureFlags() {
        useAsyncOperations = RandomUtils.randomBoolean();
        useValidation = RandomUtils.randomBoolean();
        useTransformation = RandomUtils.randomBoolean();
        useChaining = RandomUtils.randomBoolean();
        useLogging = RandomUtils.randomBoolean();
        useCaching = RandomUtils.randomBoolean();
        useErrorHandling = RandomUtils.randomBoolean();
        useCustomReflection = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地反射相关代码");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Reflection");
            String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
            generateReflectionClass(className, operationType, asyncHandler);
        }
    }

    private void generateReflectionClass(String className, String operationType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("reflection"));

        // 生成基础导入
        sb.append(generateImportStatement("java.lang.reflect.Field"));
        sb.append(generateImportStatement("java.lang.reflect.Method"));
        sb.append(generateImportStatement("java.lang.reflect.Constructor"));
        sb.append(generateImportStatement("java.lang.annotation.Annotation"));
        sb.append(generateImportStatement("java.lang.reflect.Modifier"));
        sb.append(generateImportStatement("java.util.concurrent.*"));
        sb.append(generateImportStatement("android.util.Log"));

        // 根据功能标志添加条件导入
        if (useAsyncOperations) {
            if (asyncHandler.contains("coroutines")) {
                sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
                sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
            } else if (asyncHandler.contains("rxjava")) {
                sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
                sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
            } else {
                sb.append(generateImportStatement("android.os.Handler"));
                sb.append(generateImportStatement("android.os.Looper"));
            }
        }

        if (useCaching) {
            sb.append(generateImportStatement("android.util.LruCache"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 生成类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成常量字段
        String tagVarName = RandomUtils.generateWord(6);
        String operationTypeVarName = RandomUtils.generateWord(6);
        String cacheSizeVarName = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVarName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(operationTypeVarName).append(" = \"").append(operationType).append("\";\n");

        if (useCaching) {
            sb.append("    private static final int ").append(cacheSizeVarName).append(" = ").append(RandomUtils.between(10, 50)).append(";\n");
        }

        sb.append("\n");

        // 生成实例字段
        String cacheVarName = RandomUtils.generateWord(6);
        String handlerVarName = RandomUtils.generateWord(6);

        if (useCaching) {
            sb.append("    private LruCache<String, Object> ").append(cacheVarName).append(";\n");
        }

        if (useAsyncOperations && !asyncHandler.contains("coroutines") && !asyncHandler.contains("rxjava")) {
            sb.append("    private Handler ").append(handlerVarName).append(";\n");
        }

        sb.append("\n");

        // 生成构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        initialize").append(className).append("();\n");
        sb.append("    }\n\n");

        sb.append("    private void initialize").append(className).append("() {\n");

        if (useCaching) {
            sb.append("        this.").append(cacheVarName).append(" = new LruCache<>(").append(cacheSizeVarName).append(");\n");
        }

        if (useAsyncOperations && !asyncHandler.contains("coroutines") && !asyncHandler.contains("rxjava")) {
            sb.append("        this.").append(handlerVarName).append(" = new Handler(Looper.getMainLooper());\n");
        }

        sb.append("    }\n\n");

        // 生成基础方法
        generateBasicMethods(sb, className, tagVarName, operationTypeVarName);

        // 根据功能标志添加条件方法
        if (useAsyncOperations) {
            generateAsyncMethods(sb, className, tagVarName, asyncHandler, handlerVarName);
        }

        if (useValidation) {
            generateValidationMethods(sb, className, tagVarName);
        }

        if (useTransformation) {
            generateTransformationMethods(sb, className, tagVarName);
        }

        if (useChaining) {
            generateChainingMethods(sb, className, tagVarName);
        }

        if (useLogging) {
            generateLoggingMethods(sb, className, tagVarName);
        }

        if (useCaching) {
            generateCachingMethods(sb, className, cacheVarName, tagVarName);
        }

        if (useErrorHandling) {
            generateErrorHandlingMethods(sb, className, tagVarName);
        }

        if (useCustomReflection) {
            generateCustomReflectionMethods(sb, className, tagVarName);
        }

        // 生成使用所有字段和方法的示例方法
        generateExampleUsageMethod(sb, className, cacheVarName, tagVarName, operationTypeVarName);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "reflection");
    }

    /**
     * 生成基础方法
     */
    private void generateBasicMethods(StringBuilder sb, String className, String tagVarName,
                                      String operationTypeVarName) {
        String getClassMethodName = RandomUtils.generateWord(6);
        String getClassNameMethodName = RandomUtils.generateWord(6);
        String getSuperClassMethodName = RandomUtils.generateWord(6);
        String getInterfacesMethodName = RandomUtils.generateWord(6);
        String getMethodsMethodName = RandomUtils.generateWord(6);
        String getFieldsMethodName = RandomUtils.generateWord(6);
        String getConstructorsMethodName = RandomUtils.generateWord(6);
        String getAnnotationsMethodName = RandomUtils.generateWord(6);
        String objectParamName = RandomUtils.generateWord(6);

        // 获取类方法
        sb.append("    public Class<?> ").append(getClassMethodName).append("(Object ")
                .append(objectParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return ").append(objectParamName).append(".getClass();\n");
        sb.append("    }\n\n");

        // 获取类名方法
        sb.append("    public String ").append(getClassNameMethodName).append("(Object ")
                .append(objectParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return ").append(objectParamName).append(".getClass().getName();\n");
        sb.append("    }\n\n");

        // 获取父类方法
        sb.append("    public Class<?> ").append(getSuperClassMethodName).append("(Object ")
                .append(objectParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return ").append(objectParamName).append(".getClass().getSuperclass();\n");
        sb.append("    }\n\n");

        // 获取接口方法
        sb.append("    public Class<?>[] ").append(getInterfacesMethodName).append("(Object ")
                .append(objectParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" == null) {\n");
        sb.append("            return new Class<?>[0];\n");
        sb.append("        }\n");
        sb.append("        return ").append(objectParamName).append(".getClass().getInterfaces();\n");
        sb.append("    }\n\n");

        // 获取方法方法
        sb.append("    public Method[] ").append(getMethodsMethodName).append("(Object ")
                .append(objectParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" == null) {\n");
        sb.append("            return new Method[0];\n");
        sb.append("        }\n");
        sb.append("        return ").append(objectParamName).append(".getClass().getMethods();\n");
        sb.append("    }\n\n");

        // 获取字段方法
        sb.append("    public Field[] ").append(getFieldsMethodName).append("(Object ")
                .append(objectParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" == null) {\n");
        sb.append("            return new Field[0];\n");
        sb.append("        }\n");
        sb.append("        return ").append(objectParamName).append(".getClass().getFields();\n");
        sb.append("    }\n\n");

        // 获取构造函数方法
        sb.append("    public Constructor<?>[] ").append(getConstructorsMethodName).append("(Object ")
                .append(objectParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" == null) {\n");
        sb.append("            return new Constructor<?>[0];\n");
        sb.append("        }\n");
        sb.append("        return ").append(objectParamName).append(".getClass().getConstructors();\n");
        sb.append("    }\n\n");

        // 获取注解方法
        sb.append("    public Annotation[] ").append(getAnnotationsMethodName).append("(Object ")
                .append(objectParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" == null) {\n");
        sb.append("            return new Annotation[0];\n");
        sb.append("        }\n");
        sb.append("        return ").append(objectParamName).append(".getClass().getAnnotations();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成异步操作方法
     */
    private void generateAsyncMethods(StringBuilder sb, String className, String tagVarName,
                                      String asyncHandler, String handlerVarName) {
        String asyncGetClassMethodName = RandomUtils.generateWord(6);
        String asyncInvokeMethodName = RandomUtils.generateWord(6);
        String objectParamName = RandomUtils.generateWord(6);
        String methodParamName = RandomUtils.generateWord(6);
        String argsParamName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        if (asyncHandler.contains("coroutines")) {
            // 使用协程的异步方法
            sb.append("    public void ").append(asyncGetClassMethodName).append("(Object ")
                    .append(objectParamName).append(", Consumer<Class<?>> ")
                    .append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        CoroutineScope(Dispatchers.IO).launch {\n");
            sb.append("            Class<?> ").append(resultName).append(" = null;\n");
            sb.append("            try {\n");
            sb.append("                if (").append(objectParamName).append(" != null) {\n");
            sb.append("                    ").append(resultName).append(" = ")
                    .append(objectParamName).append(".getClass();\n");
            sb.append("                }\n");
            sb.append("                withContext(Dispatchers.Main) {\n");
            sb.append("                    ").append(RandomUtils.generateWord(6)).append(".accept(")
                    .append(resultName).append(");\n");
            sb.append("                }\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                Log.e(").append(tagVarName).append(", \"Async get class error\", e);\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncInvokeMethodName).append("(Object ")
                    .append(objectParamName).append(", Method ").append(methodParamName)
                    .append(", Object[] ").append(argsParamName).append(", Consumer<Object> ")
                    .append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        CoroutineScope(Dispatchers.IO).launch {\n");
            sb.append("            Object ").append(resultName).append(" = null;\n");
            sb.append("            try {\n");
            sb.append("                if (").append(objectParamName).append(" != null && ")
                    .append(methodParamName).append(" != null) {\n");
            sb.append("                    ").append(methodParamName).append(".setAccessible(true);\n");
            sb.append("                    ").append(resultName).append(" = ")
                    .append(methodParamName).append(".invoke(")
                    .append(objectParamName).append(", ")
                    .append(argsParamName).append(");\n");
            sb.append("                }\n");
            sb.append("                withContext(Dispatchers.Main) {\n");
            sb.append("                    ").append(RandomUtils.generateWord(6)).append(".accept(")
                    .append(resultName).append(");\n");
            sb.append("                }\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                Log.e(").append(tagVarName).append(", \"Async invoke error\", e);\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            // 使用RxJava的异步方法
            sb.append("    public void ").append(asyncGetClassMethodName).append("(Object ")
                    .append(objectParamName).append(", Consumer<Class<?>> ")
                    .append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            if (").append(objectParamName).append(" != null) {\n");
            sb.append("                return ").append(objectParamName).append(".getClass();\n");
            sb.append("            }\n");
            sb.append("            return null;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.computation())\n");
            sb.append("          .subscribe(\n");
            sb.append("              result -> {\n");
            sb.append("                  ").append(RandomUtils.generateWord(6)).append(".accept(result);\n");
            sb.append("              },\n");
            sb.append("              error -> {\n");
            sb.append("                  Log.e(").append(tagVarName).append(", \"Async get class error\", error);\n");
            sb.append("              }\n");
            sb.append("          );\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncInvokeMethodName).append("(Object ")
                    .append(objectParamName).append(", Method ").append(methodParamName)
                    .append(", Object[] ").append(argsParamName).append(", Consumer<Object> ")
                    .append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            if (").append(objectParamName).append(" != null && ")
                    .append(methodParamName).append(" != null) {\n");
            sb.append("                ").append(methodParamName).append(".setAccessible(true);\n");
            sb.append("                return ").append(methodParamName).append(".invoke(")
                    .append(objectParamName).append(", ")
                    .append(argsParamName).append(");\n");
            sb.append("            }\n");
            sb.append("            return null;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.computation())\n");
            sb.append("          .subscribe(\n");
            sb.append("              result -> {\n");
            sb.append("                  ").append(RandomUtils.generateWord(6)).append(".accept(result);\n");
            sb.append("              },\n");
            sb.append("              error -> {\n");
            sb.append("                  Log.e(").append(tagVarName).append(", \"Async invoke error\", error);\n");
            sb.append("              }\n");
            sb.append("          );\n");
            sb.append("    }\n\n");
        } else {
            // 使用Handler的异步方法
            sb.append("    public void ").append(asyncGetClassMethodName).append("(Object ")
                    .append(objectParamName).append(", Consumer<Class<?>> ")
                    .append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            Class<?> ").append(resultName).append(" = null;\n");
            sb.append("            try {\n");
            sb.append("                if (").append(objectParamName).append(" != null) {\n");
            sb.append("                    ").append(resultName).append(" = ")
                    .append(objectParamName).append(".getClass();\n");
            sb.append("                }\n");
            sb.append("                ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("                    ").append(RandomUtils.generateWord(6)).append(".accept(")
                    .append(resultName).append(");\n");
            sb.append("                });\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("                    Log.e(").append(tagVarName).append(", \"Async get class error\", e);\n");
            sb.append("                });\n");
            sb.append("            }\n");
            sb.append("        }).start();\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncInvokeMethodName).append("(Object ")
                    .append(objectParamName).append(", Method ").append(methodParamName)
                    .append(", Object[] ").append(argsParamName).append(", Consumer<Object> ")
                    .append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            Object ").append(resultName).append(" = null;\n");
            sb.append("            try {\n");
            sb.append("                if (").append(objectParamName).append(" != null && ")
                    .append(methodParamName).append(" != null) {\n");
            sb.append("                    ").append(methodParamName).append(".setAccessible(true);\n");
            sb.append("                    ").append(resultName).append(" = ")
                    .append(methodParamName).append(".invoke(")
                    .append(objectParamName).append(", ")
                    .append(argsParamName).append(");\n");
            sb.append("                }\n");
            sb.append("                ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("                    ").append(RandomUtils.generateWord(6)).append(".accept(")
                    .append(resultName).append(");\n");
            sb.append("                });\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("                    Log.e(").append(tagVarName).append(", \"Async invoke error\", e);\n");
            sb.append("                });\n");
            sb.append("            }\n");
            sb.append("        }).start();\n");
            sb.append("    }\n\n");
        }
    }

    /**
     * 生成验证方法
     */
    private void generateValidationMethods(StringBuilder sb, String className, String tagVarName) {
        String isValidClassMethodName = RandomUtils.generateWord(6);
        String isValidMethodMethodName = RandomUtils.generateWord(6);
        String isValidFieldMethodName = RandomUtils.generateWord(6);
        String objectParamName = RandomUtils.generateWord(6);
        String methodParamName = RandomUtils.generateWord(6);
        String fieldParamName = RandomUtils.generateWord(6);

        // 验证类方法
        sb.append("    public boolean ").append(isValidClassMethodName).append("(Object ")
                .append(objectParamName).append(") {\n");
        sb.append("        return ").append(objectParamName).append(" != null;\n");
        sb.append("    }\n\n");

        // 验证方法方法
        sb.append("    public boolean ").append(isValidMethodMethodName).append("(Method ")
                .append(methodParamName).append(") {\n");
        sb.append("        return ").append(methodParamName).append(" != null;\n");
        sb.append("    }\n\n");

        // 验证字段方法
        sb.append("    public boolean ").append(isValidFieldMethodName).append("(Field ")
                .append(fieldParamName).append(") {\n");
        sb.append("        return ").append(fieldParamName).append(" != null;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成转换方法
     */
    private void generateTransformationMethods(StringBuilder sb, String className, String tagVarName) {
        String classToStringMethodName = RandomUtils.generateWord(6);
        String methodToStringMethodName = RandomUtils.generateWord(6);
        String fieldToStringMethodName = RandomUtils.generateWord(6);
        String objectParamName = RandomUtils.generateWord(6);
        String methodParamName = RandomUtils.generateWord(6);
        String fieldParamName = RandomUtils.generateWord(6);

        // 类转字符串方法
        sb.append("    public String ").append(classToStringMethodName).append("(Object ")
                .append(objectParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return ").append(objectParamName).append(".getClass().toString();\n");
        sb.append("    }\n\n");

        // 方法转字符串方法
        sb.append("    public String ").append(methodToStringMethodName).append("(Method ")
                .append(methodParamName).append(") {\n");
        sb.append("        if (").append(methodParamName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return ").append(methodParamName).append(".toString();\n");
        sb.append("    }\n\n");

        // 字段转字符串方法
        sb.append("    public String ").append(fieldToStringMethodName).append("(Field ")
                .append(fieldParamName).append(") {\n");
        sb.append("        if (").append(fieldParamName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return ").append(fieldParamName).append(".toString();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成链式调用方法
     */
    private void generateChainingMethods(StringBuilder sb, String className, String tagVarName) {
        String andThenMethodName = RandomUtils.generateWord(6);
        String composeMethodName = RandomUtils.generateWord(6);
        String objectParamName = RandomUtils.generateWord(6);
        String functionParamName = RandomUtils.generateWord(6);

        // 链式调用方法
        sb.append("    public ").append(className).append(" ").append(andThenMethodName)
                .append("(Object ").append(objectParamName).append(", Consumer<Class<?>> ")
                .append(functionParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" != null && ")
                .append(functionParamName).append(" != null) {\n");
        sb.append("            Class<?> clazz = ").append(objectParamName).append(".getClass();\n");
        sb.append("            ").append(functionParamName).append(".accept(clazz);\n");
        sb.append("        }\n");
        sb.append("        return this;\n");
        sb.append("    }\n\n");

        // 组合方法
        sb.append("    public ").append(className).append(" ").append(composeMethodName)
                .append("(Object ").append(objectParamName).append(", Function<Class<?>, Class<?>> ")
                .append(functionParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" != null && ")
                .append(functionParamName).append(" != null) {\n");
        sb.append("            Class<?> clazz = ").append(objectParamName).append(".getClass();\n");
        sb.append("            Class<?> result = ").append(functionParamName).append(".apply(clazz);\n");
        sb.append("        }\n");
        sb.append("        return this;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成日志方法
     */
    private void generateLoggingMethods(StringBuilder sb, String className, String tagVarName) {
        String logClassInfoMethodName = RandomUtils.generateWord(6);
        String logMethodInfoMethodName = RandomUtils.generateWord(6);
        String logFieldInfoMethodName = RandomUtils.generateWord(6);
        String objectParamName = RandomUtils.generateWord(6);
        String methodParamName = RandomUtils.generateWord(6);
        String fieldParamName = RandomUtils.generateWord(6);

        // 记录类信息方法
        sb.append("    public void ").append(logClassInfoMethodName).append("(Object ")
                .append(objectParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" != null) {\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"Class: \" + ")
                .append(objectParamName).append(".getClass().getName());\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 记录方法信息方法
        sb.append("    public void ").append(logMethodInfoMethodName).append("(Method ")
                .append(methodParamName).append(") {\n");
        sb.append("        if (").append(methodParamName).append(" != null) {\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"Method: \" + ")
                .append(methodParamName).append(".getName());\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 记录字段信息方法
        sb.append("    public void ").append(logFieldInfoMethodName).append("(Field ")
                .append(fieldParamName).append(") {\n");
        sb.append("        if (").append(fieldParamName).append(" != null) {\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"Field: \" + ")
                .append(fieldParamName).append(".getName());\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成缓存方法
     */
    private void generateCachingMethods(StringBuilder sb, String className, String cacheVarName,
                                        String tagVarName) {
        String putInCacheMethodName = RandomUtils.generateWord(6);
        String getFromCacheMethodName = RandomUtils.generateWord(6);
        String clearCacheMethodName = RandomUtils.generateWord(6);
        String keyName = RandomUtils.generateWord(6);
        String valueName = RandomUtils.generateWord(6);

        // 放入缓存方法
        sb.append("    public void ").append(putInCacheMethodName).append("(String ")
                .append(keyName).append(", Object ").append(valueName).append(") {\n");
        sb.append("        if (").append(keyName).append(" != null && !")
                .append(keyName).append(".isEmpty() && ").append(valueName).append(" != null) {\n");
        sb.append("            ").append(cacheVarName).append(".put(").append(keyName)
                .append(", ").append(valueName).append(");\n");
        sb.append("        } else {\n");
        sb.append("            Log.w(").append(tagVarName).append(", \"Cache key or value is null or empty\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 从缓存获取方法
        sb.append("    public Object ").append(getFromCacheMethodName).append("(String ")
                .append(keyName).append(") {\n");
        sb.append("        if (").append(keyName).append(" != null) {\n");
        sb.append("            Object cached = ").append(cacheVarName).append(".get(")
                .append(keyName).append(");\n");
        sb.append("            if (cached != null) {\n");
        sb.append("                return cached;\n");
        sb.append("            } else {\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Cache miss for key: \" + ")
                .append(keyName).append(");\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return null;\n");
        sb.append("    }\n\n");

        // 清空缓存方法
        sb.append("    public void ").append(clearCacheMethodName).append("() {\n");
        sb.append("        ").append(cacheVarName).append(".evictAll();\n");
        sb.append("        Log.d(").append(tagVarName).append(", \"Cache cleared\");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成错误处理方法
     */
    private void generateErrorHandlingMethods(StringBuilder sb, String className, String tagVarName) {
        String getClassOrElseMethodName = RandomUtils.generateWord(6);
        String invokeOrElseMethodName = RandomUtils.generateWord(6);
        String objectParamName = RandomUtils.generateWord(6);
        String methodParamName = RandomUtils.generateWord(6);
        String argsParamName = RandomUtils.generateWord(6);
        String defaultValueName = RandomUtils.generateWord(6);

        // 获取类或返回默认值方法
        sb.append("    public Class<?> ").append(getClassOrElseMethodName)
                .append("(Object ").append(objectParamName).append(", Class<?> ")
                .append(defaultValueName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(objectParamName).append(" != null) {\n");
        sb.append("                return ").append(objectParamName).append(".getClass();\n");
        sb.append("            }\n");
        sb.append("            return ").append(defaultValueName).append(";\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Get class error\", e);\n");
        sb.append("            return ").append(defaultValueName).append(";\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 调用方法或返回默认值方法
        sb.append("    public Object ").append(invokeOrElseMethodName)
                .append("(Object ").append(objectParamName).append(", Method ")
                .append(methodParamName).append(", Object[] ").append(argsParamName)
                .append(", Object ").append(defaultValueName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(objectParamName).append(" != null && ")
                .append(methodParamName).append(" != null) {\n");
        sb.append("                ").append(methodParamName).append(".setAccessible(true);\n");
        sb.append("                return ").append(methodParamName).append(".invoke(")
                .append(objectParamName).append(", ")
                .append(argsParamName).append(");\n");
        sb.append("            }\n");
        sb.append("            return ").append(defaultValueName).append(";\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Invoke error\", e);\n");
        sb.append("            return ").append(defaultValueName).append(";\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成自定义反射方法
     */
    private void generateCustomReflectionMethods(StringBuilder sb, String className, String tagVarName) {
        String getFieldValueMethodName = RandomUtils.generateWord(6);
        String setFieldValueMethodName = RandomUtils.generateWord(6);
        String getMethodReturnTypeMethodName = RandomUtils.generateWord(6);
        String getMethodParameterTypesMethodName = RandomUtils.generateWord(6);
        String getMethodParameterCountMethodName = RandomUtils.generateWord(6);
        String isMethodPublicMethodName = RandomUtils.generateWord(6);
        String isMethodPrivateMethodName = RandomUtils.generateWord(6);
        String isMethodProtectedMethodName = RandomUtils.generateWord(6);
        String isMethodStaticMethodName = RandomUtils.generateWord(6);
        String isFieldFinalMethodName = RandomUtils.generateWord(6);
        String isFieldStaticMethodName = RandomUtils.generateWord(6);
        String isFieldVolatileMethodName = RandomUtils.generateWord(6);
        String isFieldTransientMethodName = RandomUtils.generateWord(6);
        String objectParamName = RandomUtils.generateWord(6);
        String methodParamName = RandomUtils.generateWord(6);
        String fieldParamName = RandomUtils.generateWord(6);
        String valueParamName = RandomUtils.generateWord(6);

        // 获取字段值方法
        sb.append("    public Object ").append(getFieldValueMethodName).append("(Object ")
                .append(objectParamName).append(", Field ").append(fieldParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" == null || ")
                .append(fieldParamName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            ").append(fieldParamName).append(".setAccessible(true);\n");
        sb.append("            return ").append(fieldParamName).append(".get(")
                .append(objectParamName).append(");\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 设置字段值方法
        sb.append("    public void ").append(setFieldValueMethodName).append("(Object ")
                .append(objectParamName).append(", Field ").append(fieldParamName)
                .append(", Object ").append(valueParamName).append(") {\n");
        sb.append("        if (").append(objectParamName).append(" == null || ")
                .append(fieldParamName).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            ").append(fieldParamName).append(".setAccessible(true);\n");
        sb.append("            ").append(fieldParamName).append(".set(")
                .append(objectParamName).append(", ").append(valueParamName).append(");\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 获取方法返回类型方法
        sb.append("    public Class<?> ").append(getMethodReturnTypeMethodName).append("(Method ")
                .append(methodParamName).append(") {\n");
        sb.append("        if (").append(methodParamName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return ").append(methodParamName).append(".getReturnType();\n");
        sb.append("    }\n\n");

        // 获取方法参数类型方法
        sb.append("    public Class<?>[] ").append(getMethodParameterTypesMethodName).append("(Method ")
                .append(methodParamName).append(") {\n");
        sb.append("        if (").append(methodParamName).append(" == null) {\n");
        sb.append("            return new Class<?>[0];\n");
        sb.append("        }\n");
        sb.append("        return ").append(methodParamName).append(".getParameterTypes();\n");
        sb.append("    }\n\n");

        // 获取方法参数数量方法
        sb.append("    public int ").append(getMethodParameterCountMethodName).append("(Method ")
                .append(methodParamName).append(") {\n");
        sb.append("        if (").append(methodParamName).append(" == null) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        return ").append(methodParamName).append(".getParameterCount();\n");
        sb.append("    }\n\n");

        // 检查方法是否为public方法
        sb.append("    public boolean ").append(isMethodPublicMethodName).append("(Method ")
                .append(methodParamName).append(") {\n");
        sb.append("        if (").append(methodParamName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return Modifier.isPublic(").append(methodParamName)
                .append(".getModifiers());\n");
        sb.append("    }\n\n");

        // 检查方法是否为private方法
        sb.append("    public boolean ").append(isMethodPrivateMethodName).append("(Method ")
                .append(methodParamName).append(") {\n");
        sb.append("        if (").append(methodParamName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return Modifier.isPrivate(").append(methodParamName)
                .append(".getModifiers());\n");
        sb.append("    }\n\n");

        // 检查方法是否为protected方法
        sb.append("    public boolean ").append(isMethodProtectedMethodName).append("(Method ")
                .append(methodParamName).append(") {\n");
        sb.append("        if (").append(methodParamName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return Modifier.isProtected(").append(methodParamName)
                .append(".getModifiers());\n");
        sb.append("    }\n\n");

        // 检查方法是否为static方法
        sb.append("    public boolean ").append(isMethodStaticMethodName).append("(Method ")
                .append(methodParamName).append(") {\n");
        sb.append("        if (").append(methodParamName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return Modifier.isStatic(").append(methodParamName)
                .append(".getModifiers());\n");
        sb.append("    }\n\n");

        // 检查字段是否为final字段
        sb.append("    public boolean ").append(isFieldFinalMethodName).append("(Field ")
                .append(fieldParamName).append(") {\n");
        sb.append("        if (").append(fieldParamName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return Modifier.isFinal(").append(fieldParamName)
                .append(".getModifiers());\n");
        sb.append("    }\n\n");

        // 检查字段是否为static字段
        sb.append("    public boolean ").append(isFieldStaticMethodName).append("(Field ")
                .append(fieldParamName).append(") {\n");
        sb.append("        if (").append(fieldParamName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return Modifier.isStatic(").append(fieldParamName)
                .append(".getModifiers());\n");
        sb.append("    }\n\n");

        // 检查字段是否为volatile字段
        sb.append("    public boolean ").append(isFieldVolatileMethodName).append("(Field ")
                .append(fieldParamName).append(") {\n");
        sb.append("        if (").append(fieldParamName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return Modifier.isVolatile(").append(fieldParamName)
                .append(".getModifiers());\n");
        sb.append("    }\n\n");

        // 检查字段是否为transient字段
        sb.append("    public boolean ").append(isFieldTransientMethodName).append("(Field ")
                .append(fieldParamName).append(") {\n");
        sb.append("        if (").append(fieldParamName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return Modifier.isTransient(").append(fieldParamName)
                .append(".getModifiers());\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成示例使用方法
     */
    private void generateExampleUsageMethod(StringBuilder sb, String className,
                                            String cacheVarName, String tagVarName,
                                            String operationTypeVarName) {
        String exampleMethodName = RandomUtils.generateWord(6);
        String objectParamName = RandomUtils.generateWord(6);
        String methodParamName = RandomUtils.generateWord(6);
        String fieldParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(exampleMethodName).append("(Object ")
                .append(objectParamName).append(") {\n");
        sb.append("        // 获取类信息\n");
        sb.append("        Class<?> clazz = getClass(").append(objectParamName).append(");\n");

        if (useAsyncOperations) {
            sb.append("        // 异步操作\n");
            sb.append("        asyncGetClass(").append(objectParamName).append(", result -> {\n");
            sb.append("            // 处理结果\n");
            sb.append("        });\n");
        }

        if (useValidation) {
            sb.append("        // 验证操作\n");
            sb.append("        boolean isValid = isValidClass(").append(objectParamName).append(");\n");
        }

        if (useTransformation) {
            sb.append("        // 转换操作\n");
            sb.append("        String classStr = classToString(").append(objectParamName).append(");\n");
        }

        if (useChaining) {
            sb.append("        // 链式调用\n");
            sb.append("        andThen(").append(objectParamName).append(", result -> {\n");
            sb.append("            // 处理结果\n");
            sb.append("        }).compose(").append(objectParamName).append(", clazz -> clazz);\n");
        }

        if (useLogging) {
            sb.append("        // 日志操作\n");
            sb.append("        logClassInfo(").append(objectParamName).append(");\n");
        }

        if (useCaching) {
            sb.append("        // 缓存操作\n");
            sb.append("        putInCache(\"key1\", ").append(objectParamName).append(");\n");
            sb.append("        Object cached = getFromCache(\"key1\");\n");
        }

        if (useErrorHandling) {
            sb.append("        // 错误处理\n");
            sb.append("        Class<?> result = getClassOrElse(").append(objectParamName).append(", null);\n");
        }

        if (useCustomReflection) {
            sb.append("        // 自定义反射\n");
            sb.append("        Field[] fields = getFields(").append(objectParamName).append(");\n");
            sb.append("        Method[] methods = getMethods(").append(objectParamName).append(");\n");
        }

        sb.append("    }\n\n");
    }
}
