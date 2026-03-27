package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalOptionalGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] OPERATION_TYPES = {
            "if_present", "if_present_or_else", "map", "flat_map", "filter",
            "or", "or_else", "get", "get_or_else", "is_present",
            "stream", "parallel_stream", "to_stream", "to_parallel_stream",
            "to_list", "to_set", "to_array", "to_map", "to_string"
    };

    private static final String[] CONVERT_TYPES = {
            "to_optional", "from_optional", "to_nullable", "from_nullable",
            "to_stream", "from_stream", "to_list", "from_list", "to_array", "from_array",
            "to_unmodifiable", "to_synchronized", "to_immutable", "to_empty",
            "of_nullable", "of_non_null", "empty", "of"
    };

    private static final String[] FILTER_TYPES = {
            "filter", "filter_not", "filter_null", "filter_empty",
            "filter_positive", "filter_negative", "filter_zero", "filter_non_zero", "filter_even", "filter_odd",
            "filter_match", "filter_non_match", "filter_distinct", "filter_unique",
            "filter_non_null", "filter_non_empty", "filter_true", "filter_false"
    };

    // 功能标志
    private boolean useAsyncOperations;
    private boolean useValidation;
    private boolean useTransformation;
    private boolean useChaining;
    private boolean useLogging;
    private boolean useCaching;
    private boolean useErrorHandling;
    private boolean useCustomConverters;

    public LocalOptionalGenerator(String projectRoot, String packageName) {
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
        useCustomConverters = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地Optional相关代码");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Optional");
            String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
            generateOptionalClass(className, operationType, asyncHandler);
        }
    }

    private void generateOptionalClass(String className, String operationType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("optional"));

        // 生成基础导入
        sb.append(generateImportStatement("java.util.Optional"));
        sb.append(generateImportStatement("java.util.function.*"));
        sb.append(generateImportStatement("java.util.stream.*"));
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

        // 随机选择操作类型和转换类型
        String convertType = CONVERT_TYPES[RandomUtils.between(0, CONVERT_TYPES.length - 1)];
        String filterType = FILTER_TYPES[RandomUtils.between(0, FILTER_TYPES.length - 1)];

        // 生成类声明
        sb.append("public class ").append(className).append("<T> {\n\n");

        // 生成常量字段
        String tagVarName = RandomUtils.generateWord(6);
        String operationTypeVarName = RandomUtils.generateWord(6);
        String convertTypeVarName = RandomUtils.generateWord(6);
        String filterTypeVarName = RandomUtils.generateWord(6);
        String cacheSizeVarName = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVarName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(operationTypeVarName).append(" = \"").append(operationType).append("\";\n");
        sb.append("    private static final String ").append(convertTypeVarName).append(" = \"").append(convertType).append("\";\n");
        sb.append("    private static final String ").append(filterTypeVarName).append(" = \"").append(filterType).append("\";\n");

        if (useCaching) {
            sb.append("    private static final int ").append(cacheSizeVarName).append(" = ").append(RandomUtils.between(10, 50)).append(";\n");
        }

        sb.append("\n");

        // 生成实例字段
        String optionalVarName = RandomUtils.generateWord(6);
        String cacheVarName = RandomUtils.generateWord(6);
        String handlerVarName = RandomUtils.generateWord(6);

        sb.append("    private Optional<T> ").append(optionalVarName).append(";\n");

        if (useCaching) {
            sb.append("    private LruCache<String, Optional<T>> ").append(cacheVarName).append(";\n");
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
        sb.append("        this.").append(optionalVarName).append(" = Optional.empty();\n");

        if (useCaching) {
            sb.append("        this.").append(cacheVarName).append(" = new LruCache<>(").append(cacheSizeVarName).append(");\n");
        }

        if (useAsyncOperations && !asyncHandler.contains("coroutines") && !asyncHandler.contains("rxjava")) {
            sb.append("        this.").append(handlerVarName).append(" = new Handler(Looper.getMainLooper());\n");
        }

        sb.append("    }\n\n");

        // 生成基础方法
        generateBasicMethods(sb, className, tagVarName, operationTypeVarName, optionalVarName);

        // 根据功能标志添加条件方法
        if (useAsyncOperations) {
            generateAsyncMethods(sb, className, optionalVarName, tagVarName, asyncHandler, handlerVarName);
        }

        if (useValidation) {
            generateValidationMethods(sb, className, optionalVarName, tagVarName);
        }

        if (useTransformation) {
            generateTransformationMethods(sb, className, optionalVarName, tagVarName);
        }

        if (useChaining) {
            generateChainingMethods(sb, className, optionalVarName, tagVarName);
        }

        if (useLogging) {
            generateLoggingMethods(sb, className, optionalVarName, tagVarName);
        }

        if (useCaching) {
            generateCachingMethods(sb, className, optionalVarName, cacheVarName, tagVarName);
        }

        if (useErrorHandling) {
            generateErrorHandlingMethods(sb, className, optionalVarName, tagVarName);
        }

        if (useCustomConverters) {
            generateCustomConverterMethods(sb, className, optionalVarName, tagVarName, convertTypeVarName, filterTypeVarName);
        }

        // 生成使用所有字段和方法的示例方法
        generateExampleUsageMethod(sb, className, optionalVarName, cacheVarName, tagVarName, operationTypeVarName);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "optional");
    }

    /**
     * 生成基础方法
     */
    private void generateBasicMethods(StringBuilder sb, String className, String tagVarName,
                                      String operationTypeVarName, String optionalVarName) {
        String setValueMethodName = RandomUtils.generateWord(6);
        String getValueMethodName = RandomUtils.generateWord(6);
        String isEmptyMethodName = RandomUtils.generateWord(6);
        String clearMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 设置值方法
        sb.append("    public void ").append(setValueMethodName).append("(T ")
                .append(paramName).append(") {\n");
        sb.append("        this.").append(optionalVarName).append(" = Optional.ofNullable(")
                .append(paramName).append(");\n");
        sb.append("    }\n\n");

        // 获取值方法
        sb.append("    public Optional<T> ").append(getValueMethodName).append("() {\n");
        sb.append("        return ").append(optionalVarName).append(";\n");
        sb.append("    }\n\n");

        // 检查是否为空方法
        sb.append("    public boolean ").append(isEmptyMethodName).append("() {\n");
        sb.append("        return !").append(optionalVarName).append(".isPresent();\n");
        sb.append("    }\n\n");

        // 清空方法
        sb.append("    public void ").append(clearMethodName).append("() {\n");
        sb.append("        this.").append(optionalVarName).append(" = Optional.empty();\n");
        sb.append("    }\n\n");

        // 基本操作方法
        sb.append("    public <R> R ").append(operationTypeVarName).append("(Function<T, R> ")
                .append(paramName).append(", R ").append(RandomUtils.generateWord(6)).append(") {\n");
        sb.append("        if (").append(optionalVarName).append(".isPresent()) {\n");
        sb.append("            return ").append(paramName).append(".apply(").append(optionalVarName).append(".get());\n");
        sb.append("        } else {\n");
        sb.append("            return ").append(RandomUtils.generateWord(6)).append(";\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成异步操作方法
     */
    private void generateAsyncMethods(StringBuilder sb, String className, String optionalVarName,
                                      String tagVarName, String asyncHandler, String handlerVarName) {
        String asyncOperationMethodName = RandomUtils.generateWord(6);
        String callbackMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        if (asyncHandler.contains("coroutines")) {
            // 使用协程的异步方法
            sb.append("    public <R> void ").append(asyncOperationMethodName).append("(Function<T, R> ")
                    .append(paramName).append(", Consumer<R> ").append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        CoroutineScope(Dispatchers.IO).launch {\n");
            sb.append("            R ").append(resultName).append(" = null;\n");
            sb.append("            try {\n");
            sb.append("                if (").append(optionalVarName).append(".isPresent()) {\n");
            sb.append("                    ").append(resultName).append(" = ").append(paramName).append(".apply(")
                    .append(optionalVarName).append(".get());\n");
            sb.append("                }\n");
            sb.append("                withContext(Dispatchers.Main) {\n");
            sb.append("                    ").append(RandomUtils.generateWord(6)).append(".accept(")
                    .append(resultName).append(");\n");
            sb.append("                }\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                Log.e(").append(tagVarName).append(", \"Async operation error\", e);\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            // 使用RxJava的异步方法
            sb.append("    public <R> void ").append(asyncOperationMethodName).append("(Function<T, R> ")
                    .append(paramName).append(", Consumer<R> ").append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            if (").append(optionalVarName).append(".isPresent()) {\n");
            sb.append("                return ").append(paramName).append(".apply(").append(optionalVarName).append(".get());\n");
            sb.append("            }\n");
            sb.append("            return null;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.computation())\n");
            sb.append("          .subscribe(\n");
            sb.append("              result -> {\n");
            sb.append("                  ").append(RandomUtils.generateWord(6)).append(".accept(result);\n");
            sb.append("              },\n");
            sb.append("              error -> {\n");
            sb.append("                  Log.e(").append(tagVarName).append(", \"Async operation error\", error);\n");
            sb.append("              }\n");
            sb.append("          );\n");
            sb.append("    }\n\n");
        } else {
            // 使用Handler的异步方法
            sb.append("    public <R> void ").append(asyncOperationMethodName).append("(Function<T, R> ")
                    .append(paramName).append(", Consumer<R> ").append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            R ").append(resultName).append(" = null;\n");
            sb.append("            try {\n");
            sb.append("                if (").append(optionalVarName).append(".isPresent()) {\n");
            sb.append("                    ").append(resultName).append(" = ").append(paramName).append(".apply(")
                    .append(optionalVarName).append(".get());\n");
            sb.append("                }\n");
            sb.append("                ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("                    ").append(RandomUtils.generateWord(6)).append(".accept(")
                    .append(resultName).append(");\n");
            sb.append("                });\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("                    Log.e(").append(tagVarName).append(", \"Async operation error\", e);\n");
            sb.append("                });\n");
            sb.append("            }\n");
            sb.append("        }).start();\n");
            sb.append("    }\n\n");
        }

        // 回调方法
        sb.append("    public <R> void ").append(callbackMethodName).append("(Function<T, R> ")
                .append(paramName).append(", Consumer<R> ").append(RandomUtils.generateWord(6)).append(") {\n");
        sb.append("        ").append(asyncOperationMethodName).append("(").append(paramName).append(", ")
                .append(RandomUtils.generateWord(6)).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成验证方法
     */
    private void generateValidationMethods(StringBuilder sb, String className, String optionalVarName,
                                           String tagVarName) {
        String validateMethodName = RandomUtils.generateWord(6);
        String isValidMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 验证方法
        sb.append("    public boolean ").append(validateMethodName).append("(Predicate<T> ")
                .append(paramName).append(") {\n");
        sb.append("        if (!").append(optionalVarName).append(".isPresent()) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return ").append(paramName).append(".test(").append(optionalVarName).append(".get());\n");
        sb.append("    }\n\n");

        // 检查当前值是否有效方法
        sb.append("    public boolean ").append(isValidMethodName).append("() {\n");
        sb.append("        return ").append(optionalVarName).append(".isPresent();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成转换方法
     */
    private void generateTransformationMethods(StringBuilder sb, String className, String optionalVarName,
                                               String tagVarName) {
        String mapMethodName = RandomUtils.generateWord(6);
        String flatMapMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        // 映射方法
        sb.append("    public <R> Optional<R> ").append(mapMethodName).append("(Function<T, R> ")
                .append(paramName).append(") {\n");
        sb.append("        if (!").append(optionalVarName).append(".isPresent()) {\n");
        sb.append("            return Optional.empty();\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            R ").append(resultName).append(" = ").append(paramName).append(".apply(")
                .append(optionalVarName).append(".get());\n");
        sb.append("            return Optional.ofNullable(").append(resultName).append(");\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Map operation error\", e);\n");
        sb.append("            return Optional.empty();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 平面映射方法
        sb.append("    public <R> Optional<R> ").append(flatMapMethodName).append("(Function<T, Optional<R>> ")
                .append(paramName).append(") {\n");
        sb.append("        if (!").append(optionalVarName).append(".isPresent()) {\n");
        sb.append("            return Optional.empty();\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            return ").append(paramName).append(".apply(").append(optionalVarName).append(".get());\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"FlatMap operation error\", e);\n");
        sb.append("            return Optional.empty();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成链式调用方法
     */
    private void generateChainingMethods(StringBuilder sb, String className, String optionalVarName,
                                         String tagVarName) {
        String andThenMethodName = RandomUtils.generateWord(6);
        String orElseMethodName = RandomUtils.generateWord(6);
        String orElseGetMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String supplierName = RandomUtils.generateWord(6);

        // 链式调用方法
        sb.append("    public ").append(className).append("<T> ").append(andThenMethodName)
                .append("(Consumer<T> ").append(paramName).append(") {\n");
        sb.append("        if (").append(optionalVarName).append(".isPresent()) {\n");
        sb.append("            ").append(paramName).append(".accept(").append(optionalVarName).append(".get());\n");
        sb.append("        }\n");
        sb.append("        return this;\n");
        sb.append("    }\n\n");

        // 或者返回值方法
        sb.append("    public T ").append(orElseMethodName).append("(T ")
                .append(paramName).append(") {\n");
        sb.append("        return ").append(optionalVarName).append(".orElse(").append(paramName).append(");\n");
        sb.append("    }\n\n");

        // 或者获取值方法
        sb.append("    public T ").append(orElseGetMethodName).append("(Supplier<T> ")
                .append(supplierName).append(") {\n");
        sb.append("        return ").append(optionalVarName).append(".orElseGet(").append(supplierName).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成日志方法
     */
    private void generateLoggingMethods(StringBuilder sb, String className, String optionalVarName,
                                        String tagVarName) {
        String logPresentMethodName = RandomUtils.generateWord(6);
        String logEmptyMethodName = RandomUtils.generateWord(6);
        String logValueMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 记录存在值方法
        sb.append("    public void ").append(logPresentMethodName).append("(String ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(optionalVarName).append(".isPresent()) {\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"").append(paramName).append(": \"+")
                .append(optionalVarName).append(".get());\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 记录空值方法
        sb.append("    public void ").append(logEmptyMethodName).append("(String ")
                .append(paramName).append(") {\n");
        sb.append("        if (!").append(optionalVarName).append(".isPresent()) {\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"").append(paramName).append(": Optional is empty\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 记录值方法
        sb.append("    public void ").append(logValueMethodName).append("(String ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(optionalVarName).append(".isPresent()) {\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"").append(paramName).append(": \"+")
                .append(optionalVarName).append(".get());\n");
        sb.append("        } else {\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"").append(paramName).append(": Optional is empty\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成缓存方法
     */
    private void generateCachingMethods(StringBuilder sb, String className, String optionalVarName,
                                        String cacheVarName, String tagVarName) {
        String putInCacheMethodName = RandomUtils.generateWord(6);
        String getFromCacheMethodName = RandomUtils.generateWord(6);
        String clearCacheMethodName = RandomUtils.generateWord(6);
        String keyName = RandomUtils.generateWord(6);

        // 放入缓存方法
        sb.append("    public void ").append(putInCacheMethodName).append("(String ")
                .append(keyName).append(") {\n");
        sb.append("        if (").append(keyName).append(" != null && !")
                .append(keyName).append(".isEmpty()) {\n");
        sb.append("            ").append(cacheVarName).append(".put(").append(keyName)
                .append(", ").append(optionalVarName).append(");\n");
        sb.append("        } else {\n");
        sb.append("            Log.w(").append(tagVarName).append(", \"Cache key is null or empty\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 从缓存获取方法
        sb.append("    public Optional<T> ").append(getFromCacheMethodName).append("(String ")
                .append(keyName).append(") {\n");
        sb.append("        if (").append(keyName).append(" != null) {\n");
        sb.append("            Optional<T> cached = ").append(cacheVarName).append(".get(")
                .append(keyName).append(");\n");
        sb.append("            if (cached != null) {\n");
        sb.append("                return cached;\n");
        sb.append("            } else {\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Cache miss for key: \" + ")
                .append(keyName).append(");\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return Optional.empty();\n");
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
    private void generateErrorHandlingMethods(StringBuilder sb, String className, String optionalVarName,
                                              String tagVarName) {
        String ifPresentOrElseMethodName = RandomUtils.generateWord(6);
        String ifPresentOrElseThrowMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String supplierName = RandomUtils.generateWord(6);

        // 如果存在则执行否则执行方法
        sb.append("    public void ").append(ifPresentOrElseMethodName)
                .append("(Consumer<T> ").append(paramName).append(", Runnable ")
                .append(supplierName).append(") {\n");
        sb.append("        if (").append(optionalVarName).append(".isPresent()) {\n");
        sb.append("            ").append(paramName).append(".accept(").append(optionalVarName).append(".get());\n");
        sb.append("        } else {\n");
        sb.append("            ").append(supplierName).append(".run();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 如果存在则执行否则抛出异常方法
        sb.append("    public <X extends Throwable> T ").append(ifPresentOrElseThrowMethodName)
                .append("(Supplier<? extends X> ").append(supplierName).append(") throws X {\n");
        sb.append("        if (").append(optionalVarName).append(".isPresent()) {\n");
        sb.append("            return ").append(optionalVarName).append(".get();\n");
        sb.append("        } else {\n");
        sb.append("            throw ").append(supplierName).append(".get();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成自定义转换方法
     */
    private void generateCustomConverterMethods(StringBuilder sb, String className, String optionalVarName,
                                                String tagVarName, String convertTypeVarName, String filterTypeVarName) {
        String convertMethodName = RandomUtils.generateWord(6);
        String filterMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        // 转换方法
        sb.append("    public <R> Optional<R> ").append(convertMethodName)
                .append("(Function<T, R> ").append(paramName).append(") {\n");
        sb.append("        if (!").append(optionalVarName).append(".isPresent()) {\n");
        sb.append("            return Optional.empty();\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            R ").append(resultName).append(" = ").append(paramName).append(".apply(")
                .append(optionalVarName).append(".get());\n");
        sb.append("            return Optional.ofNullable(").append(resultName).append(");\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"").append(convertTypeVarName)
                .append(" error\", e);\n");
        sb.append("            return Optional.empty();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 过滤方法
        sb.append("    public Optional<T> ").append(filterMethodName)
                .append("(Predicate<T> ").append(paramName).append(") {\n");
        sb.append("        if (!").append(optionalVarName).append(".isPresent()) {\n");
        sb.append("            return Optional.empty();\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            if (").append(paramName).append(".test(").append(optionalVarName).append(".get())) {\n");
        sb.append("                return ").append(optionalVarName).append(";\n");
        sb.append("            } else {\n");
        sb.append("                return Optional.empty();\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
                sb.append("            Log.e(").append(tagVarName).append(", \"").append(filterTypeVarName)
                        .append(" error\", e);\n");
        sb.append("            return Optional.empty();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成示例使用方法
     */
    private void generateExampleUsageMethod(StringBuilder sb, String className,
                                            String optionalVarName, String cacheVarName,
                                            String tagVarName, String operationTypeVarName) {
        String exampleMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(exampleMethodName).append("(T ")
                .append(paramName).append(") {\n");
        sb.append("        // 设置值\n");
        sb.append("        ").append(optionalVarName).append(" = Optional.ofNullable(")
                .append(paramName).append(");\n");

        if (useAsyncOperations) {
            sb.append("        // 异步操作\n");
            sb.append("        asyncOperation(value -> value.toString(), result -> {\n");
            sb.append("            // 处理结果\n");
            sb.append("        });\n");
        }

        if (useValidation) {
            sb.append("        // 验证操作\n");
            sb.append("        boolean isValid = validate(value -> value != null);\n");
        }

        if (useTransformation) {
            sb.append("        // 转换操作\n");
            sb.append("        Optional<String> mapped = map(Object::toString);\n");
        }

        if (useChaining) {
            sb.append("        // 链式调用\n");
            sb.append("        andThen(value -> {\n");
            sb.append("            // 处理值\n");
            sb.append("        }).orElse(null);\n");
        }

        if (useLogging) {
            sb.append("        // 日志操作\n");
            sb.append("        logValue(\"Optional value\");\n");
        }

        if (useCaching) {
            sb.append("        // 缓存操作\n");
            sb.append("        putInCache(\"key1\");\n");
            sb.append("        Optional<T> cached = getFromCache(\"key1\");\n");
        }

        if (useErrorHandling) {
            sb.append("        // 错误处理\n");
            sb.append("        ifPresentOrElse(value -> {\n");
            sb.append("            // 处理值\n");
            sb.append("        }, () -> {\n");
            sb.append("            // 处理空值\n");
            sb.append("        });\n");
        }

        if (useCustomConverters) {
            sb.append("        // 自定义转换\n");
            sb.append("        Optional<String> converted = convert(Object::toString);\n");
            sb.append("        Optional<T> filtered = filter(value -> value != null);\n");
        }

        sb.append("    }\n\n");
    }
}
