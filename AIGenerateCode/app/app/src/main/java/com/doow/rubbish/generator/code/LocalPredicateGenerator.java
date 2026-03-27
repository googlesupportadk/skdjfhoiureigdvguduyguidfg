package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalPredicateGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] OPERATION_TYPES = {
            "test", "and", "or", "negate", "isEqual",
            "not_equal", "greater_than", "less_than", "greater_or_equal", "less_or_equal",
            "is_null", "is_not_null", "is_empty", "is_not_empty", "is_true", "is_false",
            "is_positive", "is_negative", "is_zero", "is_non_zero", "is_even", "is_odd"
    };

    private static final String[] CONVERT_TYPES = {
            "to_function", "to_consumer", "to_supplier", "to_optional",
            "to_stream", "to_filter", "to_map", "to_flat_map",
            "to_bi_predicate", "to_unary_operator", "to_binary_operator",
            "to_comparator", "to_comparing", "to_comparing_int", "to_comparing_long"
    };

    private static final String[] COMPOSE_TYPES = {
            "and", "or", "negate", "xor", "nand",
            "nor", "implication", "equivalence", "exclusive", "inclusive",
            "compose", "and_then", "or_else", "not_then", "compose_bi"
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

    public LocalPredicateGenerator(String projectRoot, String packageName) {
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
        System.out.println("生成本地谓词相关代码");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Predicate");
            String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
            generatePredicateClass(className, operationType, asyncHandler);
        }
    }

    private void generatePredicateClass(String className, String operationType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("predicate"));

        // 生成基础导入
        sb.append(generateImportStatement("java.util.function.Predicate"));
        sb.append(generateImportStatement("java.util.function.Function"));
        sb.append(generateImportStatement("java.util.function.Supplier"));
        sb.append(generateImportStatement("java.util.function.Consumer"));
        sb.append(generateImportStatement("java.util.Optional"));
        sb.append(generateImportStatement("java.util.stream.Stream"));
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
        String composeType = COMPOSE_TYPES[RandomUtils.between(0, COMPOSE_TYPES.length - 1)];

        // 生成类声明
        sb.append("public class ").append(className).append("<T> {\n\n");

        // 生成常量字段
        String tagVarName = RandomUtils.generateWord(6);
        String operationTypeVarName = RandomUtils.generateWord(6);
        String convertTypeVarName = RandomUtils.generateWord(6);
        String composeTypeVarName = RandomUtils.generateWord(6);
        String cacheSizeVarName = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVarName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(operationTypeVarName).append(" = \"").append(operationType).append("\";\n");
        sb.append("    private static final String ").append(convertTypeVarName).append(" = \"").append(convertType).append("\";\n");
        sb.append("    private static final String ").append(composeTypeVarName).append(" = \"").append(composeType).append("\";\n");

        if (useCaching) {
            sb.append("    private static final int ").append(cacheSizeVarName).append(" = ").append(RandomUtils.between(10, 50)).append(";\n");
        }

        sb.append("\n");

        // 生成实例字段
        String predicateVarName = RandomUtils.generateWord(6);
        String cacheVarName = RandomUtils.generateWord(6);
        String handlerVarName = RandomUtils.generateWord(6);

        sb.append("    private Predicate<T> ").append(predicateVarName).append(";\n");

        if (useCaching) {
            sb.append("    private LruCache<String, Boolean> ").append(cacheVarName).append(";\n");
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
        sb.append("        this.").append(predicateVarName).append(" = t -> false;\n");

        if (useCaching) {
            sb.append("        this.").append(cacheVarName).append(" = new LruCache<>(").append(cacheSizeVarName).append(");\n");
        }

        if (useAsyncOperations && !asyncHandler.contains("coroutines") && !asyncHandler.contains("rxjava")) {
            sb.append("        this.").append(handlerVarName).append(" = new Handler(Looper.getMainLooper());\n");
        }

        sb.append("    }\n\n");

        // 生成基础方法
        generateBasicMethods(sb, className, tagVarName, operationTypeVarName, predicateVarName);

        // 根据功能标志添加条件方法
        if (useAsyncOperations) {
            generateAsyncMethods(sb, className, predicateVarName, tagVarName, asyncHandler, handlerVarName);
        }

        if (useValidation) {
            generateValidationMethods(sb, className, predicateVarName, tagVarName);
        }

        if (useTransformation) {
            generateTransformationMethods(sb, className, predicateVarName, tagVarName);
        }

        if (useChaining) {
            generateChainingMethods(sb, className, predicateVarName, tagVarName);
        }

        if (useLogging) {
            generateLoggingMethods(sb, className, predicateVarName, tagVarName);
        }

        if (useCaching) {
            generateCachingMethods(sb, className, predicateVarName, cacheVarName, tagVarName);
        }

        if (useErrorHandling) {
            generateErrorHandlingMethods(sb, className, predicateVarName, tagVarName);
        }

        if (useCustomConverters) {
            generateCustomConverterMethods(sb, className, predicateVarName, tagVarName, convertTypeVarName, composeTypeVarName);
        }

        // 生成使用所有字段和方法的示例方法
        generateExampleUsageMethod(sb, className, predicateVarName, cacheVarName, tagVarName, operationTypeVarName);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "predicate");
    }

    /**
     * 生成基础方法
     */
    private void generateBasicMethods(StringBuilder sb, String className, String tagVarName,
                                      String operationTypeVarName, String predicateVarName) {
        String setPredicateMethodName = RandomUtils.generateWord(6);
        String getPredicateMethodName = RandomUtils.generateWord(6);
        String testMethodName = RandomUtils.generateWord(6);
        String negateMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 设置谓词方法
        sb.append("    public void ").append(setPredicateMethodName).append("(Predicate<T> ")
                .append(paramName).append(") {\n");
        sb.append("        this.").append(predicateVarName).append(" = ")
                .append(paramName).append(" != null ? ").append(paramName).append(" : t -> false;\n");
        sb.append("    }\n\n");

        // 获取谓词方法
        sb.append("    public Predicate<T> ").append(getPredicateMethodName).append("() {\n");
        sb.append("        return ").append(predicateVarName).append(";\n");
        sb.append("    }\n\n");

        // 测试方法
        sb.append("    public boolean ").append(testMethodName).append("(T ")
                .append(paramName).append(") {\n");
        sb.append("        return ").append(predicateVarName).append(".test(")
                .append(paramName).append(");\n");
        sb.append("    }\n\n");

        // 取反方法
        sb.append("    public void ").append(negateMethodName).append("() {\n");
        sb.append("        this.").append(predicateVarName).append(" = ")
                .append(predicateVarName).append(".negate();\n");
        sb.append("    }\n\n");

        // 基本操作方法
        sb.append("    public <R> R ").append(operationTypeVarName).append("(Predicate<T> ")
                .append(paramName).append(", Function<T, R> ").append(RandomUtils.generateWord(6))
                .append(", R ").append(RandomUtils.generateWord(6)).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return ").append(RandomUtils.generateWord(6)).append(";\n");
        sb.append("        }\n");
        sb.append("        return ").append(paramName).append(".test(").append(predicateVarName)
                .append(" != null ? ").append(predicateVarName).append(" : t -> false) ? ")
                .append(RandomUtils.generateWord(6)).append(" : ").append(RandomUtils.generateWord(6)).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成异步操作方法
     */
    private void generateAsyncMethods(StringBuilder sb, String className, String predicateVarName,
                                      String tagVarName, String asyncHandler, String handlerVarName) {
        String asyncOperationMethodName = RandomUtils.generateWord(6);
        String callbackMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        if (asyncHandler.contains("coroutines")) {
            // 使用协程的异步方法
            sb.append("    public <R> void ").append(asyncOperationMethodName).append("(T ")
                    .append(paramName).append(", Function<T, R> ").append(RandomUtils.generateWord(6))
                    .append(", Consumer<R> ").append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        CoroutineScope(Dispatchers.IO).launch {\n");
            sb.append("            R ").append(resultName).append(" = null;\n");
            sb.append("            try {\n");
            sb.append("                if (").append(predicateVarName).append(".test(")
                    .append(paramName).append(")) {\n");
            sb.append("                    ").append(resultName).append(" = ")
                    .append(RandomUtils.generateWord(6)).append(".apply(").append(paramName).append(");\n");
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
            sb.append("    public <R> void ").append(asyncOperationMethodName).append("(T ")
                    .append(paramName).append(", Function<T, R> ").append(RandomUtils.generateWord(6))
                    .append(", Consumer<R> ").append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            if (").append(predicateVarName).append(".test(")
                    .append(paramName).append(")) {\n");
            sb.append("                return ").append(RandomUtils.generateWord(6)).append(".apply(")
                    .append(paramName).append(");\n");
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
            sb.append("    public <R> void ").append(asyncOperationMethodName).append("(T ")
                    .append(paramName).append(", Function<T, R> ").append(RandomUtils.generateWord(6))
                    .append(", Consumer<R> ").append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            R ").append(resultName).append(" = null;\n");
            sb.append("            try {\n");
            sb.append("                if (").append(predicateVarName).append(".test(")
                    .append(paramName).append(")) {\n");
            sb.append("                    ").append(resultName).append(" = ")
                    .append(RandomUtils.generateWord(6)).append(".apply(").append(paramName).append(");\n");
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
        sb.append("    public <R> void ").append(callbackMethodName).append("(T ")
                .append(paramName).append(", Function<T, R> ").append(RandomUtils.generateWord(6))
                .append(", Consumer<R> ").append(RandomUtils.generateWord(6)).append(") {\n");
        sb.append("        ").append(asyncOperationMethodName).append("(").append(paramName).append(", ")
                .append(RandomUtils.generateWord(6)).append(", ").append(RandomUtils.generateWord(6)).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成验证方法
     */
    private void generateValidationMethods(StringBuilder sb, String className, String predicateVarName,
                                           String tagVarName) {
        String validateMethodName = RandomUtils.generateWord(6);
        String isValidMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 验证方法
        sb.append("    public boolean ").append(validateMethodName).append("(Predicate<T> ")
                .append(paramName).append(", T ").append(RandomUtils.generateWord(6)).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return ").append(paramName).append(".test(")
                .append(RandomUtils.generateWord(6)).append(") && ")
                .append(predicateVarName).append(".test(").append(RandomUtils.generateWord(6)).append(");\n");
        sb.append("    }\n\n");

        // 检查当前谓词是否有效方法
        sb.append("    public boolean ").append(isValidMethodName).append("() {\n");
        sb.append("        return ").append(predicateVarName).append(" != null;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成转换方法
     */
    private void generateTransformationMethods(StringBuilder sb, String className, String predicateVarName,
                                               String tagVarName) {
        String mapMethodName = RandomUtils.generateWord(6);
        String flatMapMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        // 映射方法
        sb.append("    public <R> Predicate<R> ").append(mapMethodName).append("(Function<T, R> ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return r -> false;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            return r -> {\n");
        sb.append("                R ").append(resultName).append(" = r;\n");
        sb.append("                return ").append(predicateVarName).append(".test(")
                .append(resultName).append(");\n");
        sb.append("            };\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Map operation error\", e);\n");
        sb.append("            return r -> false;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 平面映射方法
        sb.append("    public <R> Predicate<R> ").append(flatMapMethodName).append("(Function<T, Predicate<R>> ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return r -> false;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            return r -> {\n");
        sb.append("                Predicate<R> ").append(resultName).append(" = ")
                .append(paramName).append(".apply(").append(predicateVarName)
                .append(".test(r) ? r : null);\n");
        sb.append("                return ").append(resultName).append(" != null ? ")
                .append(resultName).append(".test(r) : false;\n");
        sb.append("            };\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"FlatMap operation error\", e);\n");
        sb.append("            return r -> false;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成链式调用方法
     */
    private void generateChainingMethods(StringBuilder sb, String className, String predicateVarName,
                                         String tagVarName) {
        String andMethodName = RandomUtils.generateWord(6);
        String orMethodName = RandomUtils.generateWord(6);
        String andThenMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 与操作方法
        sb.append("    public ").append(className).append("<T> ").append(andMethodName)
                .append("(Predicate<T> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return this;\n");
        sb.append("        }\n");
        sb.append("        this.").append(predicateVarName).append(" = ")
                .append(predicateVarName).append(".and(").append(paramName).append(");\n");
        sb.append("        return this;\n");
        sb.append("    }\n\n");

        // 或操作方法
        sb.append("    public ").append(className).append("<T> ").append(orMethodName)
                .append("(Predicate<T> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return this;\n");
        sb.append("        }\n");
        sb.append("        this.").append(predicateVarName).append(" = ")
                .append(predicateVarName).append(".or(").append(paramName).append(");\n");
        sb.append("        return this;\n");
        sb.append("    }\n\n");

        // 链式调用方法
        sb.append("    public ").append(className).append("<T> ").append(andThenMethodName)
                .append("(Consumer<T> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" != null) {\n");
        sb.append("            ").append(paramName).append(".accept(").append(predicateVarName)
                .append(" != null ? ").append(predicateVarName).append(" : t -> false);\n");
        sb.append("        }\n");
        sb.append("        return this;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成日志方法
     */
    private void generateLoggingMethods(StringBuilder sb, String className, String predicateVarName,
                                        String tagVarName) {
        String logTestMethodName = RandomUtils.generateWord(6);
        String logResultMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 记录测试方法
        sb.append("    public void ").append(logTestMethodName).append("(T ")
                .append(paramName).append(") {\n");
        sb.append("        boolean result = ").append(predicateVarName).append(".test(")
                .append(paramName).append(");\n");
        sb.append("        Log.d(").append(tagVarName).append(", \"Test result for \"+")
                .append(paramName).append("+\": \"+result);\n");
        sb.append("    }\n\n");

        // 记录结果方法
        sb.append("    public void ").append(logResultMethodName).append("(T ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(predicateVarName).append(".test(")
                .append(paramName).append(")) {\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"Predicate test passed for \"+")
                .append(paramName).append(");\n");
        sb.append("        } else {\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"Predicate test failed for \"+")
                .append(paramName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成缓存方法
     */
    private void generateCachingMethods(StringBuilder sb, String className, String predicateVarName,
                                        String cacheVarName, String tagVarName) {
        String putInCacheMethodName = RandomUtils.generateWord(6);
        String getFromCacheMethodName = RandomUtils.generateWord(6);
        String clearCacheMethodName = RandomUtils.generateWord(6);
        String keyName = RandomUtils.generateWord(6);

        // 放入缓存方法
        sb.append("    public void ").append(putInCacheMethodName).append("(String ")
                .append(keyName).append(", T ").append(RandomUtils.generateWord(6)).append(") {\n");
        sb.append("        if (").append(keyName).append(" != null && !")
                .append(keyName).append(".isEmpty()) {\n");
        sb.append("            boolean result = ").append(predicateVarName).append(".test(")
                .append(RandomUtils.generateWord(6)).append(");\n");
        sb.append("            ").append(cacheVarName).append(".put(").append(keyName)
                .append(", result);\n");
        sb.append("        } else {\n");
        sb.append("            Log.w(").append(tagVarName).append(", \"Cache key is null or empty\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 从缓存获取方法
        sb.append("    public boolean ").append(getFromCacheMethodName).append("(String ")
                .append(keyName).append(") {\n");
        sb.append("        if (").append(keyName).append(" != null) {\n");
        sb.append("            Boolean cached = ").append(cacheVarName).append(".get(")
                .append(keyName).append(");\n");
        sb.append("            if (cached != null) {\n");
        sb.append("                return cached;\n");
        sb.append("            } else {\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Cache miss for key: \" + ")
                .append(keyName).append(");\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return false;\n");
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
    private void generateErrorHandlingMethods(StringBuilder sb, String className, String predicateVarName,
                                              String tagVarName) {
        String testOrElseMethodName = RandomUtils.generateWord(6);
        String testOrElseThrowMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String supplierName = RandomUtils.generateWord(6);

        // 测试或返回默认值方法
        sb.append("    public boolean ").append(testOrElseMethodName)
                .append("(T ").append(paramName).append(", boolean ")
                .append(supplierName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            return ").append(predicateVarName).append(".test(")
                .append(paramName).append(");\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Predicate test error\", e);\n");
        sb.append("            return ").append(supplierName).append(";\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 测试或抛出异常方法
        sb.append("    public boolean ").append(testOrElseThrowMethodName)
                .append("(T ").append(paramName).append(", Supplier<? extends Exception> ")
                .append(supplierName).append(") throws Exception {\n");
        sb.append("        try {\n");
        sb.append("            boolean result = ").append(predicateVarName).append(".test(")
                .append(paramName).append(");\n");
        sb.append("            if (!result) {\n");
        sb.append("                throw ").append(supplierName).append(".get();\n");
        sb.append("            }\n");
        sb.append("            return result;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Predicate test error\", e);\n");
        sb.append("            throw e;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成自定义转换方法
     */
    private void generateCustomConverterMethods(StringBuilder sb, String className, String predicateVarName,
                                                String tagVarName, String convertTypeVarName, String composeTypeVarName) {
        String convertMethodName = RandomUtils.generateWord(6);
        String composeMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        // 转换方法
        sb.append("    public <R> Function<T, R> ").append(convertMethodName)
                .append("(Function<T, R> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return t -> null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            return t -> {\n");
        sb.append("                if (").append(predicateVarName).append(".test(t)) {\n");
        sb.append("                    return ").append(paramName).append(".apply(t);\n");
        sb.append("                }\n");
        sb.append("                return null;\n");
        sb.append("            };\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"").append(convertTypeVarName)
                        .append(" error\", e);\n");
        sb.append("            return t -> null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 组合方法
        sb.append("    public ").append(className).append("<T> ").append(composeMethodName)
                .append("(Predicate<T> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return this;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            this.").append(predicateVarName).append(" = ")
                .append(predicateVarName).append(".and(").append(paramName).append(");\n");
        sb.append("            return this;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"").append(composeTypeVarName)
                        .append(" error\", e);\n");
        sb.append("            return this;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成示例使用方法
     */
    private void generateExampleUsageMethod(StringBuilder sb, String className,
                                            String predicateVarName, String cacheVarName,
                                            String tagVarName, String operationTypeVarName) {
        String exampleMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(exampleMethodName).append("(T ")
                .append(paramName).append(") {\n");
        sb.append("        // 设置谓词\n");
        sb.append("        ").append(predicateVarName).append(" = t -> t != null;\n");

        if (useAsyncOperations) {
            sb.append("        // 异步操作\n");
            sb.append("        asyncOperation(").append(paramName).append(", value -> {\n");
            sb.append("            // 处理结果\n");
            sb.append("            return value;\n");
            sb.append("        }, result -> {\n");
            sb.append("            // 处理结果\n");
            sb.append("        });\n");
        }

        if (useValidation) {
            sb.append("        // 验证操作\n");
            sb.append("        boolean isValid = validate(t -> t != null, ").append(paramName).append(");\n");
        }

        if (useTransformation) {
            sb.append("        // 转换操作\n");
            sb.append("        Predicate<String> mapped = map(Object::toString);\n");
        }

        if (useChaining) {
            sb.append("        // 链式调用\n");
            sb.append("        and(t -> t != null).or(t -> t != null);\n");
        }

        if (useLogging) {
            sb.append("        // 日志操作\n");
            sb.append("        logResult(").append(paramName).append(");\n");
        }

        if (useCaching) {
            sb.append("        // 缓存操作\n");
            sb.append("        putInCache(\"key1\", ").append(paramName).append(");\n");
            sb.append("        boolean cached = getFromCache(\"key1\");\n");
        }

        if (useErrorHandling) {
            sb.append("        // 错误处理\n");
            sb.append("        boolean result = testOrElse(").append(paramName).append(", false);\n");
        }

        if (useCustomConverters) {
            sb.append("        // 自定义转换\n");
            sb.append("        Function<T, String> converter = convert(Object::toString);\n");
            sb.append("        compose(t -> t != null);\n");
        }

        sb.append("    }\n\n");
    }
}
