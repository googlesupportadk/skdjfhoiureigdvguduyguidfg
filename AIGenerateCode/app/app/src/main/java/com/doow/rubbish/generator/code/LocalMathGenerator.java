package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalMathGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] MATH_TYPES = {
            "Arithmetic", "Geometry", "Trigonometry", "Statistics", "Algebra",
            "Calculus", "LinearAlgebra", "Probability", "Combinatorics", "NumberTheory",
            "Matrix", "Vector", "Complex", "Logic", "SetTheory"
    };

    private static final String[] OPERATION_TYPES = {
            "add", "subtract", "multiply", "divide", "power", "root", "log", "sin", "cos", "tan",
            "asin", "acos", "atan", "sinh", "cosh", "tanh", "exp", "ln", "log10",
            "abs", "ceil", "floor", "round", "max", "min", "mod", "factorial",
            "gcd", "lcm", "sqrt", "cbrt", "hypot", "atan2", "pow", "signum"
    };

    // 功能标志 - 确保所有字段和方法都会被使用
    private boolean useAsyncOperations;
    private boolean useBatchOperations;
    private boolean useFiltering;
    private boolean useTransformation;
    private boolean useValidation;
    private boolean useCaching;
    private boolean useStatistics;
    private boolean usePrecisionControl;

    public LocalMathGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        initializeFeatureFlags();
    }

    /**
     * 初始化功能标志，确保随机性和多样性
     */
    private void initializeFeatureFlags() {
        useAsyncOperations = RandomUtils.randomBoolean();
        useBatchOperations = RandomUtils.randomBoolean();
        useFiltering = RandomUtils.randomBoolean();
        useTransformation = RandomUtils.randomBoolean();
        useValidation = RandomUtils.randomBoolean();
        useCaching = RandomUtils.randomBoolean();
        useStatistics = RandomUtils.randomBoolean();
        usePrecisionControl = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成数学类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Math");
            generateMathClass(className, asyncHandler);
        }
    }

    private void generateMathClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("math"));

        // 生成基础导入
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.*"));
        sb.append(generateImportStatement("java.util.concurrent.ConcurrentHashMap"));

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

        if (useBatchOperations) {
            sb.append(generateImportStatement("java.util.List"));
            sb.append(generateImportStatement("java.util.ArrayList"));
        }

        if (useFiltering || useTransformation) {
            sb.append(generateImportStatement("java.util.function.Predicate"));
            sb.append(generateImportStatement("java.util.function.Function"));
        }

        if (useStatistics) {
            sb.append(generateImportStatement("java.util.concurrent.atomic.AtomicInteger"));
            sb.append(generateImportStatement("java.util.concurrent.atomic.AtomicLong"));
        }

        if (usePrecisionControl) {
            sb.append(generateImportStatement("java.math.BigDecimal"));
            sb.append(generateImportStatement("java.math.RoundingMode"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 随机选择数学类型和操作类型
        String mathType = MATH_TYPES[RandomUtils.between(0, MATH_TYPES.length - 1)];
        String operationType = OPERATION_TYPES[RandomUtils.between(0, OPERATION_TYPES.length - 1)];

        // 生成类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成常量字段
        String tagVarName = RandomUtils.generateWord(6);
        String mathTypeVarName = RandomUtils.generateWord(6);
        String operationTypeVarName = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVarName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(mathTypeVarName).append(" = \"").append(mathType).append("\";\n");
        sb.append("    private static final String ").append(operationTypeVarName).append(" = \"").append(operationType).append("\";\n\n");

        // 生成实例字段
        String randomVarName = RandomUtils.generateWord(6);
        String cacheVarName = RandomUtils.generateWord(6);
        String accessCountVarName = RandomUtils.generateWord(6);
        String accessTimeVarName = RandomUtils.generateWord(6);
        String precisionVarName = RandomUtils.generateWord(6);

        sb.append("    private Random ").append(randomVarName).append(";\n");

        // 根据功能标志添加条件字段
        if (useCaching) {
            sb.append("    private Map<String, Double> ").append(cacheVarName).append(";\n");
        }

        if (useStatistics) {
            sb.append("    private AtomicInteger ").append(accessCountVarName).append(";\n");
            sb.append("    private AtomicLong ").append(accessTimeVarName).append(";\n");
        }

        if (usePrecisionControl) {
            sb.append("    private int ").append(precisionVarName).append(";\n");
        }

        sb.append("\n");

        // 生成构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        initialize").append(className).append("();\n");
        sb.append("    }\n\n");

        sb.append("    private void initialize").append(className).append("() {\n");
        sb.append("        this.").append(randomVarName).append(" = new Random();\n");

        if (useCaching) {
            sb.append("        this.").append(cacheVarName).append(" = new HashMap<>();\n");
        }

        if (useStatistics) {
            sb.append("        this.").append(accessCountVarName).append(" = new AtomicInteger(0);\n");
            sb.append("        this.").append(accessTimeVarName).append(" = new AtomicLong(0);\n");
        }

        if (usePrecisionControl) {
            sb.append("        this.").append(precisionVarName).append(" = RandomUtils.between(2, 10);\n");
        }

        sb.append("    }\n\n");

        // 生成基础方法
        generateBasicMethods(sb, className, tagVarName, mathTypeVarName, operationTypeVarName,
                randomVarName, operationType);

        // 根据功能标志添加条件方法
        if (useAsyncOperations) {
            generateAsyncOperationMethods(sb, className, tagVarName, asyncHandler);
        }

        if (useBatchOperations) {
            generateBatchOperationMethods(sb, className, tagVarName);
        }

        if (useFiltering) {
            generateFilteringMethods(sb, className, tagVarName);
        }

        if (useTransformation) {
            generateTransformationMethods(sb, className, tagVarName);
        }

        if (useValidation) {
            generateValidationMethods(sb, className, tagVarName);
        }

        if (useCaching) {
            generateCachingMethods(sb, className, cacheVarName, tagVarName);
        }

        if (useStatistics) {
            generateStatisticsMethods(sb, className, accessCountVarName, accessTimeVarName, tagVarName);
        }

        if (usePrecisionControl) {
            generatePrecisionControlMethods(sb, className, precisionVarName, tagVarName);
        }

        // 生成使用所有字段和方法的示例方法
        generateExampleUsageMethod(sb, className, randomVarName, cacheVarName, accessCountVarName,
                accessTimeVarName, precisionVarName, tagVarName, mathTypeVarName,
                operationTypeVarName, operationType);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "math");
    }

    /**
     * 生成基础方法
     */
    private void generateBasicMethods(StringBuilder sb, String className, String tagVarName,
                                      String mathTypeVarName, String operationTypeVarName,
                                      String randomVarName, String operationType) {
        String param1VarName = RandomUtils.generateWord(6);
        String param2VarName = RandomUtils.generateWord(6);
        String resultVarName = RandomUtils.generateWord(6);

        // 生成主要操作方法
        sb.append("    public double ").append(operationType).append("(double ")
                .append(param1VarName).append(", double ").append(param2VarName).append(") {\n");
        sb.append("        double ").append(resultVarName).append(" = 0;\n");
        sb.append("        switch (").append(operationTypeVarName).append(") {\n");
        sb.append("            case \"add\":\n");
        sb.append("                ").append(resultVarName).append(" = ").append(param1VarName)
                .append(" + ").append(param2VarName).append(";\n");
        sb.append("                break;\n");
        sb.append("            case \"subtract\":\n");
        sb.append("                ").append(resultVarName).append(" = ").append(param1VarName)
                .append(" - ").append(param2VarName).append(";\n");
        sb.append("                break;\n");
        sb.append("            case \"multiply\":\n");
        sb.append("                ").append(resultVarName).append(" = ").append(param1VarName)
                .append(" * ").append(param2VarName).append(";\n");
        sb.append("                break;\n");
        sb.append("            case \"divide\":\n");
        sb.append("                if (").append(param2VarName).append(" != 0) {\n");
        sb.append("                    ").append(resultVarName).append(" = ").append(param1VarName)
                .append(" / ").append(param2VarName).append(";\n");
        sb.append("                } else {\n");
        sb.append("                    Log.e(").append(tagVarName).append(", \"Division by zero\");\n");
        sb.append("                }\n");
        sb.append("                break;\n");
        sb.append("            case \"power\":\n");
        sb.append("                ").append(resultVarName).append(" = Math.pow(").append(param1VarName)
                .append(", ").append(param2VarName).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"root\":\n");
        sb.append("                if (").append(param2VarName).append(" != 0) {\n");
        sb.append("                    ").append(resultVarName).append(" = Math.pow(").append(param1VarName)
                .append(", 1.0 / ").append(param2VarName).append(");\n");
        sb.append("                } else {\n");
        sb.append("                    Log.e(").append(tagVarName).append(", \"Root of zero\");\n");
        sb.append("                }\n");
        sb.append("                break;\n");
        sb.append("            case \"log\":\n");
        sb.append("                if (").append(param1VarName).append(" > 0) {\n");
        sb.append("                    ").append(resultVarName).append(" = Math.log(").append(param1VarName)
                .append(") / Math.log(").append(param2VarName).append(");\n");
        sb.append("                } else {\n");
        sb.append("                    Log.e(").append(tagVarName).append(", \"Log of non-positive\");\n");
        sb.append("                }\n");
        sb.append("                break;\n");
        sb.append("            case \"sin\":\n");
        sb.append("                ").append(resultVarName).append(" = Math.sin(").append(param1VarName)
                .append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"cos\":\n");
        sb.append("                ").append(resultVarName).append(" = Math.cos(").append(param1VarName)
                .append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"tan\":\n");
        sb.append("                ").append(resultVarName).append(" = Math.tan(").append(param1VarName)
                .append(");\n");
        sb.append("                break;\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultVarName).append(";\n");
        sb.append("    }\n\n");

        // 生成数组操作方法
        String arrayParamName = RandomUtils.generateWord(6);
        sb.append("    public double ").append(operationType).append("(double[] ")
                .append(arrayParamName).append(") {\n");
        sb.append("        if (").append(arrayParamName).append(" == null || ")
                .append(arrayParamName).append(".length == 0) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        double ").append(resultVarName).append(" = ").append(arrayParamName)
                .append("[0];\n");
        sb.append("        for (int i = 1; i < ").append(arrayParamName).append(".length; i++) {\n");
        sb.append("            ").append(resultVarName).append(" = ").append(operationType)
                .append("(").append(resultVarName).append(", ").append(arrayParamName).append("[i]);\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultVarName).append(";\n");
        sb.append("    }\n\n");

        // 生成统计方法
        String avgMethodName = RandomUtils.generateWord(6);
        String stdDevMethodName = RandomUtils.generateWord(6);
        String sumMethodName = RandomUtils.generateWord(6);
        String minMethodName = RandomUtils.generateWord(6);
        String maxMethodName = RandomUtils.generateWord(6);

        sb.append("    public double ").append(avgMethodName).append("(double[] ")
                .append(arrayParamName).append(") {\n");
        sb.append("        if (").append(arrayParamName).append(" == null || ")
                .append(arrayParamName).append(".length == 0) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        double ").append(resultVarName).append(" = 0;\n");
        sb.append("        for (double value : ").append(arrayParamName).append(") {\n");
        sb.append("            ").append(resultVarName).append(" += value;\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultVarName).append(" / ")
                .append(arrayParamName).append(".length;\n");
        sb.append("    }\n\n");

        sb.append("    public double ").append(stdDevMethodName).append("(double[] ")
                .append(arrayParamName).append(") {\n");
        sb.append("        if (").append(arrayParamName).append(" == null || ")
                .append(arrayParamName).append(".length < 2) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        double avg = ").append(avgMethodName).append("(").append(arrayParamName)
                .append(");\n");
        sb.append("        double ").append(resultVarName).append(" = 0;\n");
        sb.append("        for (double value : ").append(arrayParamName).append(") {\n");
        sb.append("            ").append(resultVarName).append(" += Math.pow(value - avg, 2);\n");
        sb.append("        }\n");
        sb.append("        return Math.sqrt(").append(resultVarName).append(" / ")
                .append(arrayParamName).append(".length);\n");
        sb.append("    }\n\n");

        sb.append("    public double ").append(sumMethodName).append("(double[] ")
                .append(arrayParamName).append(") {\n");
        sb.append("        if (").append(arrayParamName).append(" == null || ")
                .append(arrayParamName).append(".length == 0) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        double ").append(resultVarName).append(" = 0;\n");
        sb.append("        for (double value : ").append(arrayParamName).append(") {\n");
        sb.append("            ").append(resultVarName).append(" += value;\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultVarName).append(";\n");
        sb.append("    }\n\n");

        sb.append("    public double ").append(minMethodName).append("(double[] ")
                .append(arrayParamName).append(") {\n");
        sb.append("        if (").append(arrayParamName).append(" == null || ")
                .append(arrayParamName).append(".length == 0) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        double ").append(resultVarName).append(" = ").append(arrayParamName)
                .append("[0];\n");
        sb.append("        for (double value : ").append(arrayParamName).append(") {\n");
        sb.append("            if (value < ").append(resultVarName).append(") {\n");
        sb.append("                ").append(resultVarName).append(" = value;\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultVarName).append(";\n");
        sb.append("    }\n\n");

        sb.append("    public double ").append(maxMethodName).append("(double[] ")
                .append(arrayParamName).append(") {\n");
        sb.append("        if (").append(arrayParamName).append(" == null || ")
                .append(arrayParamName).append(".length == 0) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        double ").append(resultVarName).append(" = ").append(arrayParamName)
                .append("[0];\n");
        sb.append("        for (double value : ").append(arrayParamName).append(") {\n");
        sb.append("            if (value > ").append(resultVarName).append(") {\n");
        sb.append("                ").append(resultVarName).append(" = value;\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultVarName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成异步操作方法
     */
    private void generateAsyncOperationMethods(StringBuilder sb, String className,
                                               String tagVarName, String asyncHandler) {
        String asyncOperationMethodName = RandomUtils.generateWord(6);
        String param1VarName = RandomUtils.generateWord(6);
        String param2VarName = RandomUtils.generateWord(6);

        if (asyncHandler.contains("coroutines")) {
            sb.append("    public void ").append(asyncOperationMethodName).append("(double ")
                    .append(param1VarName).append(", double ").append(param2VarName)
                    .append(", Function<Double, Void> callback) {\n");
            sb.append("        CoroutineScope(Dispatchers.IO).launch {\n");
            sb.append("            double result = ").append(param1VarName).append(" + ")
                    .append(param2VarName).append(";\n");
            sb.append("            callback.apply(result);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("    public void ").append(asyncOperationMethodName).append("(double ")
                    .append(param1VarName).append(", double ").append(param2VarName)
                    .append(", Consumer<Double> callback) {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            return ").append(param1VarName).append(" + ")
                    .append(param2VarName).append(";\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .subscribe(callback);\n");
            sb.append("    }\n\n");
        } else {
            sb.append("    private Handler handler = new Handler(Looper.getMainLooper());\n\n");
            sb.append("    public void ").append(asyncOperationMethodName).append("(double ")
                    .append(param1VarName).append(", double ").append(param2VarName)
                    .append(", Consumer<Double> callback) {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            double result = ").append(param1VarName).append(" + ")
                    .append(param2VarName).append(";\n");
            sb.append("            handler.post(() -> {\n");
            sb.append("                callback.accept(result);\n");
            sb.append("            });\n");
            sb.append("        }).start();\n");
            sb.append("    }\n\n");
        }
    }

    /**
     * 生成批量操作方法
     */
    private void generateBatchOperationMethods(StringBuilder sb, String className,
                                               String tagVarName) {
        String batchOperationMethodName = RandomUtils.generateWord(6);
        String valuesParamName = RandomUtils.generateWord(6);

        sb.append("    public double[] ").append(batchOperationMethodName)
                .append("(List<Double> ").append(valuesParamName).append(") {\n");
        sb.append("        if (").append(valuesParamName).append(" == null || ")
                .append(valuesParamName).append(".isEmpty()) {\n");
        sb.append("            return new double[0];\n");
        sb.append("        }\n");
        sb.append("        double[] result = new double[").append(valuesParamName)
                .append(".size()];\n");
        sb.append("        for (int i = 0; i < ").append(valuesParamName).append(".size(); i++) {\n");
        sb.append("            result[i] = ").append(valuesParamName).append(".get(i) * 2;\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成过滤方法
     */
    private void generateFilteringMethods(StringBuilder sb, String className,
                                          String tagVarName) {
        String filterMethodName = RandomUtils.generateWord(6);
        String valuesParamName = RandomUtils.generateWord(6);
        String predicateParamName = RandomUtils.generateWord(6);

        sb.append("    public List<Double> ").append(filterMethodName)
                .append("(List<Double> ").append(valuesParamName).append(", Predicate<Double> ")
                .append(predicateParamName).append(") {\n");
        sb.append("        if (").append(valuesParamName).append(" == null) {\n");
        sb.append("            return new ArrayList<>();\n");
        sb.append("        }\n");
        sb.append("        List<Double> result = new ArrayList<>();\n");
        sb.append("        for (Double value : ").append(valuesParamName).append(") {\n");
        sb.append("            if (").append(predicateParamName).append(".test(value)) {\n");
        sb.append("                result.add(value);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成转换方法
     */
    private void generateTransformationMethods(StringBuilder sb, String className,
                                               String tagVarName) {
        String transformMethodName = RandomUtils.generateWord(6);
        String valuesParamName = RandomUtils.generateWord(6);
        String functionParamName = RandomUtils.generateWord(6);

        sb.append("    public List<Double> ").append(transformMethodName)
                .append("(List<Double> ").append(valuesParamName).append(", Function<Double, Double> ")
                .append(functionParamName).append(") {\n");
        sb.append("        if (").append(valuesParamName).append(" == null) {\n");
        sb.append("            return new ArrayList<>();\n");
        sb.append("        }\n");
        sb.append("        List<Double> result = new ArrayList<>();\n");
        sb.append("        for (Double value : ").append(valuesParamName).append(") {\n");
        sb.append("            result.add(").append(functionParamName).append(".apply(value));\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成验证方法
     */
    private void generateValidationMethods(StringBuilder sb, String className,
                                           String tagVarName) {
        String validateMethodName = RandomUtils.generateWord(6);
        String valueParamName = RandomUtils.generateWord(6);

        sb.append("    public boolean ").append(validateMethodName).append("(double ")
                .append(valueParamName).append(") {\n");
        sb.append("        if (Double.isNaN(").append(valueParamName).append(") || ")
                .append("Double.isInfinite(").append(valueParamName).append(")) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Invalid value\");\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return true;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成缓存方法
     */
    private void generateCachingMethods(StringBuilder sb, String className,
                                        String cacheVarName, String tagVarName) {
        String cacheOperationMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);
        String valueParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(cacheOperationMethodName).append("(String ")
                .append(keyParamName).append(", double ").append(valueParamName).append(") {\n");
        sb.append("        ").append(cacheVarName).append(".put(").append(keyParamName)
                .append(", ").append(valueParamName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public double ").append(cacheOperationMethodName).append("(String ")
                .append(keyParamName).append(") {\n");
        sb.append("        return ").append(cacheVarName).append(".getOrDefault(")
                .append(keyParamName).append(", 0.0);\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成统计方法
     */
    private void generateStatisticsMethods(StringBuilder sb, String className,
                                           String accessCountVarName, String accessTimeVarName,
                                           String tagVarName) {
        String getAccessCountMethodName = RandomUtils.generateWord(6);
        String getAccessTimeMethodName = RandomUtils.generateWord(6);
        String resetStatisticsMethodName = RandomUtils.generateWord(6);

        sb.append("    public int ").append(getAccessCountMethodName).append("() {\n");
        sb.append("        return ").append(accessCountVarName).append(".get();\n");
        sb.append("    }\n\n");

        sb.append("    public long ").append(getAccessTimeMethodName).append("() {\n");
        sb.append("        return ").append(accessTimeVarName).append(".get();\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(resetStatisticsMethodName).append("() {\n");
        sb.append("        ").append(accessCountVarName).append(".set(0);\n");
        sb.append("        ").append(accessTimeVarName).append("set(0);\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成精度控制方法
     */
    private void generatePrecisionControlMethods(StringBuilder sb, String className,
                                                 String precisionVarName, String tagVarName) {
        String setPrecisionMethodName = RandomUtils.generateWord(6);
        String getPrecisionMethodName = RandomUtils.generateWord(6);
        String roundMethodName = RandomUtils.generateWord(6);
        String valueParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(setPrecisionMethodName).append("(int ")
                .append(valueParamName).append(") {\n");
        sb.append("        if (").append(valueParamName).append(" >= 0 && ")
                .append(valueParamName).append(" <= 10) {\n");
        sb.append("            ").append(precisionVarName).append(" = ").append(valueParamName).append(";\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public int ").append(getPrecisionMethodName).append("() {\n");
        sb.append("        return ").append(precisionVarName).append(";\n");
        sb.append("    }\n\n");

        sb.append("    public double ").append(roundMethodName).append("(double ")
                .append(valueParamName).append(") {\n");
        sb.append("        BigDecimal bd = new BigDecimal(").append(valueParamName).append(");\n");
        sb.append("        bd = bd.setScale(").append(precisionVarName).append(", RoundingMode.HALF_UP);\n");
        sb.append("        return bd.doubleValue();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成示例使用方法
     */
    private void generateExampleUsageMethod(StringBuilder sb, String className,
                                            String randomVarName, String cacheVarName,
                                            String accessCountVarName, String accessTimeVarName,
                                            String precisionVarName, String tagVarName,
                                            String mathTypeVarName, String operationTypeVarName,
                                            String operationType) {
        String exampleMethodName = RandomUtils.generateWord(6);
        String param1VarName = RandomUtils.generateWord(6);
        String param2VarName = RandomUtils.generateWord(6);
        String randomHelperName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(exampleMethodName).append("() {\n");
        sb.append("        Random ").append(randomHelperName).append(" = new Random();\n");
        sb.append("        double ").append(param1VarName).append(" = ").append(randomHelperName).append(".nextDouble() * 100.0;\n");
        sb.append("        double ").append(param2VarName).append(" = ").append(randomHelperName).append(".nextDouble() * 100.0;\n");
        sb.append("        double result = ").append(operationType).append("(").append(param1VarName)
                .append(", ").append(param2VarName).append(");\n");

        if (useAsyncOperations) {
            sb.append("        asyncOperation(").append(param1VarName).append(", ")
                    .append(param2VarName).append(", value -> {\n");
            sb.append("            Log.d(").append(tagVarName).append(", \"Async result: \" + value);\n");
            sb.append("        });\n");
        }

        if (useBatchOperations) {
            sb.append("        List<Double> values = Arrays.asList(").append(param1VarName)
                    .append(", ").append(param2VarName).append(");\n");
            sb.append("        double[] batchResults = batchOperation(values);\n");
        }

        if (useFiltering) {
            sb.append("        List<Double> filteredValues = filter(values, value -> value > 50);\n");
        }

        if (useTransformation) {
            sb.append("        List<Double> transformedValues = transform(values, value -> value * 2);\n");
        }

        if (useValidation) {
            sb.append("        boolean isValid = validate(").append(param1VarName).append(");\n");
        }

        if (useCaching) {
            sb.append("        cacheOperation(\"key1\", ").append(param1VarName).append(");\n");
            sb.append("        double cachedValue = cacheOperation(\"key1\");\n");
        }

        if (useStatistics) {
            sb.append("        int count = getAccessCount();\n");
            sb.append("        long time = getAccessTime();\n");
        }

        if (usePrecisionControl) {
            sb.append("        setPrecision(4);\n");
            sb.append("        double roundedValue = round(").append(param1VarName).append(");\n");
        }

        sb.append("    }\n\n");
    }
}
