package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalProbabilityGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] PROBABILITY_TYPES = {
            "basic", "conditional", "bayesian", "independent", "dependent",
            "mutually_exclusive", "complementary", "joint", "marginal", "total",
            "union", "intersection", "difference", "symmetric_difference", "cartesian_product"
    };

    private static final String[] DISTRIBUTION_TYPES = {
            "discrete", "continuous", "binomial", "poisson", "normal",
            "uniform", "exponential", "geometric",
            "hypergeometric", "negative_binomial", "multinomial", "log_normal", "weibull",
            "gamma", "beta", "chi_square", "student_t", "f_distribution"
    };

    private static final String[] CALCULATION_TYPES = {
            "probability", "odds", "likelihood", "expectation", "variance",
            "standard_deviation", "covariance", "correlation", "entropy", "information"
    };

    // 功能标志
    private boolean useAsyncOperations;
    private boolean useValidation;
    private boolean useTransformation;
    private boolean useChaining;
    private boolean useLogging;
    private boolean useCaching;
    private boolean useErrorHandling;
    private boolean useCustomDistributions;

    public LocalProbabilityGenerator(String projectRoot, String packageName) {
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
        useCustomDistributions = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地概率相关代码");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Probability");
            String probabilityType = RandomUtils.randomChoice(PROBABILITY_TYPES);
            String distributionType = RandomUtils.randomChoice(DISTRIBUTION_TYPES);
            String calculationType = RandomUtils.randomChoice(CALCULATION_TYPES);
            generateProbabilityClass(className, probabilityType, distributionType, calculationType, asyncHandler);
        }
    }

    private void generateProbabilityClass(String className, String probabilityType, String distributionType,
                                          String calculationType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("probability"));

        // 生成基础导入
        sb.append(generateImportStatement("java.util.Random"));
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
        String probabilityTypeVarName = RandomUtils.generateWord(6);
        String distributionTypeVarName = RandomUtils.generateWord(6);
        String calculationTypeVarName = RandomUtils.generateWord(6);
        String cacheSizeVarName = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVarName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(probabilityTypeVarName).append(" = \"").append(probabilityType).append("\";\n");
        sb.append("    private static final String ").append(distributionTypeVarName).append(" = \"").append(distributionType).append("\";\n");
        sb.append("    private static final String ").append(calculationTypeVarName).append(" = \"").append(calculationType).append("\";\n");

        if (useCaching) {
            sb.append("    private static final int ").append(cacheSizeVarName).append(" = ").append(RandomUtils.between(10, 50)).append(";\n");
        }

        sb.append("\n");

        // 生成实例字段
        String randomVarName = RandomUtils.generateWord(6);
        String cacheVarName = RandomUtils.generateWord(6);
        String handlerVarName = RandomUtils.generateWord(6);

        sb.append("    private Random ").append(randomVarName).append(";\n");

        if (useCaching) {
            sb.append("    private LruCache<String, Double> ").append(cacheVarName).append(";\n");
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
        sb.append("        this.").append(randomVarName).append(" = new Random();\n");

        if (useCaching) {
            sb.append("        this.").append(cacheVarName).append(" = new LruCache<>(").append(cacheSizeVarName).append(");\n");
        }

        if (useAsyncOperations && !asyncHandler.contains("coroutines") && !asyncHandler.contains("rxjava")) {
            sb.append("        this.").append(handlerVarName).append(" = new Handler(Looper.getMainLooper());\n");
        }

        sb.append("    }\n\n");

        // 生成基础方法
        generateBasicMethods(sb, className, tagVarName, probabilityTypeVarName, randomVarName);

        // 根据功能标志添加条件方法
        if (useAsyncOperations) {
            generateAsyncMethods(sb, className, randomVarName, tagVarName, asyncHandler, handlerVarName);
        }

        if (useValidation) {
            generateValidationMethods(sb, className, randomVarName, tagVarName);
        }

        if (useTransformation) {
            generateTransformationMethods(sb, className, randomVarName, tagVarName);
        }

        if (useChaining) {
            generateChainingMethods(sb, className, randomVarName, tagVarName);
        }

        if (useLogging) {
            generateLoggingMethods(sb, className, randomVarName, tagVarName);
        }

        if (useCaching) {
            generateCachingMethods(sb, className, randomVarName, cacheVarName, tagVarName);
        }

        if (useErrorHandling) {
            generateErrorHandlingMethods(sb, className, randomVarName, tagVarName);
        }

        if (useCustomDistributions) {
            generateCustomDistributionMethods(sb, className, randomVarName, tagVarName, distributionTypeVarName);
        }

        // 生成使用所有字段和方法的示例方法
        generateExampleUsageMethod(sb, className, randomVarName, cacheVarName, tagVarName, probabilityTypeVarName);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "probability");
    }

    /**
     * 生成基础方法
     */
    private void generateBasicMethods(StringBuilder sb, String className, String tagVarName,
                                      String probabilityTypeVarName, String randomVarName) {
        String calculateProbabilityMethodName = RandomUtils.generateWord(6);
        String calculateOddsMethodName = RandomUtils.generateWord(6);
        String calculateComplementMethodName = RandomUtils.generateWord(6);
        String paramName1 = RandomUtils.generateWord(6);
        String paramName2 = RandomUtils.generateWord(6);

        // 计算概率方法
        sb.append("    public double ").append(calculateProbabilityMethodName).append("(double ")
                .append(paramName1).append(", double ").append(paramName2).append(") {\n");
        sb.append("        if (").append(paramName2).append(" <= 0) {\n");
        sb.append("            throw new IllegalArgumentException(\"").append(probabilityTypeVarName)
                .append(": ").append(paramName2).append(" must be positive\");\n");
        sb.append("        }\n");
        sb.append("        return ").append(paramName1).append(" / ").append(paramName2).append(";\n");
        sb.append("    }\n\n");

        // 计算赔率方法
        sb.append("    public double ").append(calculateOddsMethodName).append("(double ")
                .append(paramName1).append(", double ").append(paramName2).append(") {\n");
        sb.append("        if (").append(paramName2).append(" <= 0) {\n");
        sb.append("            throw new IllegalArgumentException(\"").append(probabilityTypeVarName)
                .append(": ").append(paramName2).append(" must be positive\");\n");
        sb.append("        }\n");
        sb.append("        double probability = ").append(paramName1).append(" / ").append(paramName2).append(";\n");
        sb.append("        return probability / (1 - probability);\n");
        sb.append("    }\n\n");

        // 计算补集概率方法
        sb.append("    public double ").append(calculateComplementMethodName).append("(double ")
                .append(paramName1).append(", double ").append(paramName2).append(") {\n");
        sb.append("        if (").append(paramName2).append(" <= 0) {\n");
        sb.append("            throw new IllegalArgumentException(\"").append(probabilityTypeVarName)
                .append(": ").append(paramName2).append(" must be positive\");\n");
        sb.append("        }\n");
        sb.append("        return 1 - (").append(paramName1).append(" / ").append(paramName2).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成异步操作方法
     */
    private void generateAsyncMethods(StringBuilder sb, String className, String randomVarName,
                                      String tagVarName, String asyncHandler, String handlerVarName) {
        String asyncCalculateMethodName = RandomUtils.generateWord(6);
        String callbackMethodName = RandomUtils.generateWord(6);
        String paramName1 = RandomUtils.generateWord(6);
        String paramName2 = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        if (asyncHandler.contains("coroutines")) {
            // 使用协程的异步方法
            sb.append("    public void ").append(asyncCalculateMethodName).append("(double ")
                    .append(paramName1).append(", double ").append(paramName2)
                    .append(", Consumer<Double> ").append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        CoroutineScope(Dispatchers.IO).launch {\n");
            sb.append("            double ").append(resultName).append(" = 0;\n");
            sb.append("            try {\n");
            sb.append("                if (").append(paramName2).append(" > 0) {\n");
            sb.append("                    ").append(resultName).append(" = ")
                    .append(paramName1).append(" / ").append(paramName2).append(";\n");
            sb.append("                }\n");
            sb.append("                withContext(Dispatchers.Main) {\n");
            sb.append("                    ").append(RandomUtils.generateWord(6)).append(".accept(")
                    .append(resultName).append(");\n");
            sb.append("                }\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                Log.e(").append(tagVarName).append(", \"Async calculation error\", e);\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            // 使用RxJava的异步方法
            sb.append("    public void ").append(asyncCalculateMethodName).append("(double ")
                    .append(paramName1).append(", double ").append(paramName2)
                    .append(", Consumer<Double> ").append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            if (").append(paramName2).append(" > 0) {\n");
            sb.append("                return ").append(paramName1).append(" / ").append(paramName2).append(";\n");
            sb.append("            }\n");
            sb.append("            return 0.0;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.computation())\n");
            sb.append("          .subscribe(\n");
            sb.append("              result -> {\n");
            sb.append("                  ").append(RandomUtils.generateWord(6)).append(".accept(result);\n");
            sb.append("              },\n");
            sb.append("              error -> {\n");
            sb.append("                  Log.e(").append(tagVarName).append(", \"Async calculation error\", error);\n");
            sb.append("              }\n");
            sb.append("          );\n");
            sb.append("    }\n\n");
        } else {
            // 使用Handler的异步方法
            sb.append("    public void ").append(asyncCalculateMethodName).append("(double ")
                    .append(paramName1).append(", double ").append(paramName2)
                    .append(", Consumer<Double> ").append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            double ").append(resultName).append(" = 0;\n");
            sb.append("            try {\n");
            sb.append("                if (").append(paramName2).append(" > 0) {\n");
            sb.append("                    ").append(resultName).append(" = ")
                    .append(paramName1).append(" / ").append(paramName2).append(";\n");
            sb.append("                }\n");
            sb.append("                ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("                    ").append(RandomUtils.generateWord(6)).append(".accept(")
                    .append(resultName).append(");\n");
            sb.append("                });\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("                    Log.e(").append(tagVarName).append(", \"Async calculation error\", e);\n");
            sb.append("                });\n");
            sb.append("            }\n");
            sb.append("        }).start();\n");
            sb.append("    }\n\n");
        }

        // 回调方法
        sb.append("    public void ").append(callbackMethodName).append("(double ")
                .append(paramName1).append(", double ").append(paramName2)
                .append(", Consumer<Double> ").append(RandomUtils.generateWord(6)).append(") {\n");
        sb.append("        ").append(asyncCalculateMethodName).append("(").append(paramName1).append(", ")
                .append(paramName2).append(", ").append(RandomUtils.generateWord(6)).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成验证方法
     */
    private void generateValidationMethods(StringBuilder sb, String className, String randomVarName,
                                           String tagVarName) {
        String validateProbabilityMethodName = RandomUtils.generateWord(6);
        String isValidProbabilityMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 验证概率方法
        sb.append("    public boolean ").append(validateProbabilityMethodName).append("(double ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" < 0 || ")
                .append(paramName).append(" > 1) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return true;\n");
        sb.append("    }\n\n");

        // 检查是否为有效概率方法
        sb.append("    public boolean ").append(isValidProbabilityMethodName).append("(double ")
                .append(paramName).append(") {\n");
        sb.append("        return ").append(paramName).append(" >= 0 && ")
                .append(paramName).append(" <= 1;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成转换方法
     */
    private void generateTransformationMethods(StringBuilder sb, String className, String randomVarName,
                                               String tagVarName) {
        String probabilityToOddsMethodName = RandomUtils.generateWord(6);
        String oddsToProbabilityMethodName = RandomUtils.generateWord(6);
        String probabilityToLogOddsMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        // 概率转赔率方法
        sb.append("    public double ").append(probabilityToOddsMethodName).append("(double ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" <= 0 || ")
                .append(paramName).append(" >= 1) {\n");
        sb.append("            throw new IllegalArgumentException(\"Probability must be between 0 and 1 (exclusive)\");\n");
        sb.append("        }\n");
        sb.append("        return ").append(paramName).append(" / (1 - ")
                .append(paramName).append(");\n");
        sb.append("    }\n\n");

        // 赔率转概率方法
        sb.append("    public double ").append(oddsToProbabilityMethodName).append("(double ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" <= 0) {\n");
        sb.append("            throw new IllegalArgumentException(\"Odds must be positive\");\n");
        sb.append("        }\n");
        sb.append("        return ").append(paramName).append(" / (1 + ")
                .append(paramName).append(");\n");
        sb.append("    }\n\n");

        // 概率转对数赔率方法
        sb.append("    public double ").append(probabilityToLogOddsMethodName).append("(double ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" <= 0 || ")
                .append(paramName).append(" >= 1) {\n");
        sb.append("            throw new IllegalArgumentException(\"Probability must be between 0 and 1 (exclusive)\");\n");
        sb.append("        }\n");
        sb.append("        double ").append(resultName).append(" = ")
                .append(paramName).append(" / (1 - ").append(paramName).append(");\n");
        sb.append("        return Math.log(").append(resultName).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成链式调用方法
     */
    private void generateChainingMethods(StringBuilder sb, String className, String randomVarName,
                                         String tagVarName) {
        String andThenMethodName = RandomUtils.generateWord(6);
        String composeMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 链式调用方法
        sb.append("    public ").append(className).append(" ").append(andThenMethodName)
                .append("(Consumer<Double> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" != null) {\n");
        sb.append("            ").append(paramName).append(".accept(").append(randomVarName)
                .append(".nextDouble());\n");
        sb.append("        }\n");
        sb.append("        return this;\n");
        sb.append("    }\n\n");

        // 组合方法
        sb.append("    public ").append(className).append(" ").append(composeMethodName)
                .append("(Function<Double, Double> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" != null) {\n");
        sb.append("            double value = ").append(randomVarName).append(".nextDouble();\n");
        sb.append("            ").append(paramName).append(".apply(value);\n");
        sb.append("        }\n");
        sb.append("        return this;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成日志方法
     */
    private void generateLoggingMethods(StringBuilder sb, String className, String randomVarName,
                                        String tagVarName) {
        String logProbabilityMethodName = RandomUtils.generateWord(6);
        String logDistributionMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 记录概率方法
        sb.append("    public void ").append(logProbabilityMethodName).append("(double ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" >= 0 && ")
                .append(paramName).append(" <= 1) {\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"Valid probability: \"+")
                .append(paramName).append(");\n");
        sb.append("        } else {\n");
        sb.append("            Log.w(").append(tagVarName).append(", \"Invalid probability: \"+")
                .append(paramName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 记录分布方法
        sb.append("    public void ").append(logDistributionMethodName).append("(double ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" >= 0) {\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"Distribution value: \"+")
                .append(paramName).append(");\n");
        sb.append("        } else {\n");
        sb.append("            Log.w(").append(tagVarName).append(", \"Invalid distribution value: \"+")
                .append(paramName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成缓存方法
     */
    private void generateCachingMethods(StringBuilder sb, String className, String randomVarName,
                                        String cacheVarName, String tagVarName) {
        String putInCacheMethodName = RandomUtils.generateWord(6);
        String getFromCacheMethodName = RandomUtils.generateWord(6);
        String clearCacheMethodName = RandomUtils.generateWord(6);
        String keyName = RandomUtils.generateWord(6);

        // 放入缓存方法
        sb.append("    public void ").append(putInCacheMethodName).append("(String ")
                .append(keyName).append(", double ").append(RandomUtils.generateWord(6)).append(") {\n");
        sb.append("        if (").append(keyName).append(" != null && !")
                .append(keyName).append(".isEmpty()) {\n");
        sb.append("            double value = ").append(randomVarName).append(".nextDouble();\n");
        sb.append("            ").append(cacheVarName).append(".put(").append(keyName)
                .append(", value);\n");
        sb.append("        } else {\n");
        sb.append("            Log.w(").append(tagVarName).append(", \"Cache key is null or empty\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 从缓存获取方法
        sb.append("    public double ").append(getFromCacheMethodName).append("(String ")
                .append(keyName).append(") {\n");
        sb.append("        if (").append(keyName).append(" != null) {\n");
        sb.append("            Double cached = ").append(cacheVarName).append(".get(")
                .append(keyName).append(");\n");
        sb.append("            if (cached != null) {\n");
        sb.append("                return cached;\n");
        sb.append("            } else {\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Cache miss for key: \" + ")
                .append(keyName).append(");\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return 0.0;\n");
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
    private void generateErrorHandlingMethods(StringBuilder sb, String className, String randomVarName,
                                              String tagVarName) {
        String calculateOrElseMethodName = RandomUtils.generateWord(6);
        String calculateOrElseThrowMethodName = RandomUtils.generateWord(6);
        String paramName1 = RandomUtils.generateWord(6);
        String paramName2 = RandomUtils.generateWord(6);
        String supplierName = RandomUtils.generateWord(6);

        // 计算或返回默认值方法
        sb.append("    public double ").append(calculateOrElseMethodName)
                .append("(double ").append(paramName1).append(", double ")
                .append(paramName2).append(", double ").append(supplierName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(paramName2).append(" > 0) {\n");
        sb.append("                return ").append(paramName1).append(" / ")
                .append(paramName2).append(";\n");
        sb.append("            }\n");
        sb.append("            return ").append(supplierName).append(";\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Calculation error\", e);\n");
        sb.append("            return ").append(supplierName).append(";\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 计算或抛出异常方法
        sb.append("    public double ").append(calculateOrElseThrowMethodName)
                .append("(double ").append(paramName1).append(", double ")
                .append(paramName2).append(", Supplier<? extends Exception> ")
                .append(supplierName).append(") throws Exception {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(paramName2).append(" > 0) {\n");
        sb.append("                return ").append(paramName1).append(" / ")
                .append(paramName2).append(";\n");
        sb.append("            }\n");
        sb.append("            throw ").append(supplierName).append(".get();\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Calculation error\", e);\n");
        sb.append("            throw e;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成自定义分布方法
     */
    private void generateCustomDistributionMethods(StringBuilder sb, String className, String randomVarName,
                                                   String tagVarName, String distributionTypeVarName) {
        String generateNormalMethodName = RandomUtils.generateWord(6);
        String generatePoissonMethodName = RandomUtils.generateWord(6);
        String generateBinomialMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        // 生成正态分布方法
        sb.append("    public double ").append(generateNormalMethodName)
                .append("(double mean, double stddev) {\n");
        sb.append("        if (stddev <= 0) {\n");
        sb.append("            throw new IllegalArgumentException(\"Standard deviation must be positive\");\n");
        sb.append("        }\n");
        sb.append("        double ").append(paramName).append(" = ")
                .append(randomVarName).append(".nextGaussian();\n");
        sb.append("        return mean + stddev * ").append(paramName).append(";\n");
        sb.append("    }\n\n");

        // 生成泊松分布方法
        sb.append("    public int ").append(generatePoissonMethodName)
                .append("(double lambda) {\n");
        sb.append("        if (lambda <= 0) {\n");
        sb.append("            throw new IllegalArgumentException(\"Lambda must be positive\");\n");
        sb.append("        }\n");
        sb.append("        double ").append(paramName).append(" = Math.exp(-lambda);\n");
        sb.append("        int ").append(resultName).append(" = 0;\n");
        sb.append("        double product = ").append(paramName).append(";\n");
        sb.append("        while (product > 1) {\n");
        sb.append("            ").append(resultName).append("++;\n");
        sb.append("            product *= ").append(randomVarName).append(".nextDouble();\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultName).append(";\n");
        sb.append("    }\n\n");

        // 生成二项分布方法
        sb.append("    public int ").append(generateBinomialMethodName)
                .append("(int n, double p) {\n");
        sb.append("        if (n < 0 || p < 0 || p > 1) {\n");
        sb.append("            throw new IllegalArgumentException(\"Invalid parameters\");\n");
        sb.append("        }\n");
        sb.append("        int ").append(resultName).append(" = 0;\n");
        sb.append("        for (int i = 0; i < n; i++) {\n");
        sb.append("            if (").append(randomVarName).append(".nextDouble() < p) {\n");
        sb.append("                ").append(resultName).append("++;\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成示例使用方法
     */
    private void generateExampleUsageMethod(StringBuilder sb, String className,
                                            String randomVarName, String cacheVarName,
                                            String tagVarName, String probabilityTypeVarName) {
        String exampleMethodName = RandomUtils.generateWord(6);
        String paramName1 = RandomUtils.generateWord(6);
        String paramName2 = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(exampleMethodName).append("(double ")
                .append(paramName1).append(", double ").append(paramName2).append(") {\n");
        sb.append("        // 计算概率\n");
        sb.append("        double probability = calculateProbability(").append(paramName1)
                .append(", ").append(paramName2).append(");\n");

        if (useAsyncOperations) {
            sb.append("        // 异步操作\n");
            sb.append("        asyncCalculate(").append(paramName1).append(", ")
                    .append(paramName2).append(", result -> {\n");
            sb.append("            // 处理结果\n");
            sb.append("        });\n");
        }

        if (useValidation) {
            sb.append("        // 验证操作\n");
            sb.append("        boolean isValid = validateProbability(probability);\n");
        }

        if (useTransformation) {
            sb.append("        // 转换操作\n");
            sb.append("        double odds = probabilityToOdds(probability);\n");
        }

        if (useChaining) {
            sb.append("        // 链式调用\n");
            sb.append("        andThen(value -> {\n");
            sb.append("            // 处理值\n");
            sb.append("        }).compose(value -> value * 2);\n");
        }

        if (useLogging) {
            sb.append("        // 日志操作\n");
            sb.append("        logProbability(probability);\n");
        }

        if (useCaching) {
            sb.append("        // 缓存操作\n");
            sb.append("        putInCache(\"key1\", probability);\n");
            sb.append("        double cached = getFromCache(\"key1\");\n");
        }

        if (useErrorHandling) {
            sb.append("        // 错误处理\n");
            sb.append("        double result = calculateOrElse(").append(paramName1)
                    .append(", ").append(paramName2).append(", 0.0);\n");
        }

        if (useCustomDistributions) {
            sb.append("        // 自定义分布\n");
            sb.append("        double normal = generateNormal(0.0, 1.0);\n");
            sb.append("        int poisson = generatePoisson(5.0);\n");
            sb.append("        int binomial = generateBinomial(10, 0.5);\n");
        }

        sb.append("    }\n\n");
    }
}
