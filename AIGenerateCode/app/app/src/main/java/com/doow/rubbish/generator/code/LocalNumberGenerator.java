package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalNumberGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] NUMBER_TYPES = {
            "Integer", "Long", "Float", "Double", "BigDecimal",
            "BigInteger", "Short", "Byte", "AtomicInteger", "AtomicLong",
            "AtomicReference", "Number", "Comparable", "NumberFormat", "DecimalFormat"
    };

    private static final String[] OPERATION_TYPES = {
            "add", "subtract", "multiply", "divide", "power", "root", "abs", "round", "ceil", "floor",
            "mod", "negate", "signum", "min", "max", "sum", "average",
            "median", "mode", "variance", "standard_deviation", "percentile"
    };

    // 功能标志
    private boolean useAsyncOperations;
    private boolean useValidation;
    private boolean useFormatting;
    private boolean useConversion;
    private boolean useComparison;
    private boolean useStatistics;
    private boolean useCaching;
    private boolean usePrecisionControl;

    public LocalNumberGenerator(String projectRoot, String packageName) {
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
        useFormatting = RandomUtils.randomBoolean();
        useConversion = RandomUtils.randomBoolean();
        useComparison = RandomUtils.randomBoolean();
        useStatistics = RandomUtils.randomBoolean();
        useCaching = RandomUtils.randomBoolean();
        usePrecisionControl = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成数字类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Number");
            generateNumberClass(className, asyncHandler);
        }
    }

    private void generateNumberClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("number"));

        // 生成基础导入
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.math.BigDecimal"));
        sb.append(generateImportStatement("java.math.BigInteger"));
        sb.append(generateImportStatement("java.text.DecimalFormat"));
        sb.append(generateImportStatement("java.text.NumberFormat"));
        sb.append(generateImportStatement("java.util.Locale"));

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

        // 随机选择数字类型和操作类型
        String numberType = NUMBER_TYPES[RandomUtils.between(0, NUMBER_TYPES.length - 1)];
        String operationType = OPERATION_TYPES[RandomUtils.between(0, OPERATION_TYPES.length - 1)];

        // 生成类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成常量字段
        String tagVarName = RandomUtils.generateWord(6);
        String numberTypeVarName = RandomUtils.generateWord(6);
        String operationTypeVarName = RandomUtils.generateWord(6);
        String precisionVarName = RandomUtils.generateWord(6);
        String cacheSizeVarName = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVarName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(numberTypeVarName).append(" = \"").append(numberType).append("\";\n");
        sb.append("    private static final String ").append(operationTypeVarName).append(" = \"").append(operationType).append("\";\n");

        if (usePrecisionControl) {
            sb.append("    private static final int ").append(precisionVarName).append(" = ").append(RandomUtils.between(2, 10)).append(";\n");
        }

        if (useCaching) {
            sb.append("    private static final int ").append(cacheSizeVarName).append(" = ").append(RandomUtils.between(10, 50)).append(";\n");
        }

        sb.append("\n");

        // 生成实例字段
        String valueVarName = RandomUtils.generateWord(6);
        String formatVarName = RandomUtils.generateWord(6);
        String cacheVarName = RandomUtils.generateWord(6);
        String handlerVarName = RandomUtils.generateWord(6);

        sb.append("    private double ").append(valueVarName).append(";\n");

        if (useFormatting) {
            sb.append("    private NumberFormat ").append(formatVarName).append(";\n");
        }

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
        sb.append("        this.").append(valueVarName).append(" = 0;\n");

        if (useFormatting) {
            sb.append("        this.").append(formatVarName).append(" = NumberFormat.getInstance(Locale.getDefault());\n");
            if (usePrecisionControl) {
                sb.append("        this.").append(formatVarName).append(".setMaximumFractionDigits(").append(precisionVarName).append(");\n");
            }
        }

        if (useCaching) {
            sb.append("        this.").append(cacheVarName).append(" = new LruCache<>(").append(cacheSizeVarName).append(");\n");
        }

        if (useAsyncOperations && !asyncHandler.contains("coroutines") && !asyncHandler.contains("rxjava")) {
            sb.append("        this.").append(handlerVarName).append(" = new Handler(Looper.getMainLooper());\n");
        }

        sb.append("    }\n\n");

        // 生成基础方法
        generateBasicMethods(sb, className, tagVarName, numberTypeVarName, operationTypeVarName, valueVarName);

        // 根据功能标志添加条件方法
        if (useAsyncOperations) {
            generateAsyncMethods(sb, className, valueVarName, tagVarName, asyncHandler, handlerVarName);
        }

        if (useValidation) {
            generateValidationMethods(sb, className, valueVarName, tagVarName);
        }

        if (useFormatting) {
            generateFormattingMethods(sb, className, valueVarName, formatVarName, tagVarName);
        }

        if (useConversion) {
            generateConversionMethods(sb, className, valueVarName, tagVarName);
        }

        if (useComparison) {
            generateComparisonMethods(sb, className, valueVarName, tagVarName);
        }

        if (useStatistics) {
            generateStatisticsMethods(sb, className, valueVarName, tagVarName);
        }

        if (useCaching) {
            generateCachingMethods(sb, className, valueVarName, cacheVarName, tagVarName);
        }

        if (usePrecisionControl) {
            generatePrecisionMethods(sb, className, valueVarName, precisionVarName, tagVarName);
        }

        // 生成使用所有字段和方法的示例方法
        generateExampleUsageMethod(sb, className, valueVarName, formatVarName, cacheVarName, tagVarName);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "number");
    }


    /**
     * 生成基础方法
     */
    private void generateBasicMethods(StringBuilder sb, String className, String tagVarName,
                                      String numberTypeVarName, String operationTypeVarName,
                                      String valueVarName) {
        String setValueMethodName = RandomUtils.generateWord(6);
        String getValueMethodName = RandomUtils.generateWord(6);
        String getNumberTypeMethodName = RandomUtils.generateWord(6);
        String getOperationTypeMethodName = RandomUtils.generateWord(6);
        String resetMethodName = RandomUtils.generateWord(6);
        String param1Name = RandomUtils.generateWord(6);
        String param2Name = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        // 设置值方法
        sb.append("    public void ").append(setValueMethodName).append("(double ")
                .append(param1Name).append(") {\n");
        sb.append("        this.").append(valueVarName).append(" = ").append(param1Name).append(";\n");
        sb.append("    }\n\n");

        // 获取值方法
        sb.append("    public double ").append(getValueMethodName).append("() {\n");
        sb.append("        return ").append(valueVarName).append(";\n");
        sb.append("    }\n\n");

        // 获取数字类型方法
        sb.append("    public String ").append(getNumberTypeMethodName).append("() {\n");
        sb.append("        return ").append(numberTypeVarName).append(";\n");
        sb.append("    }\n\n");

        // 获取操作类型方法
        sb.append("    public String ").append(getOperationTypeMethodName).append("() {\n");
        sb.append("        return ").append(operationTypeVarName).append(";\n");
        sb.append("    }\n\n");

        // 重置方法
        sb.append("    public void ").append(resetMethodName).append("() {\n");
        sb.append("        this.").append(valueVarName).append(" = 0;\n");
        sb.append("    }\n\n");

        // 基本操作方法
        sb.append("    public double ").append(operationTypeVarName).append("(double ")
                .append(param1Name).append(", double ").append(param2Name).append(") {\n");
        sb.append("        double ").append(resultName).append(" = 0;\n");
        sb.append("        switch (\"").append(operationTypeVarName).append("\") {\n");
        sb.append("            case \"add\":\n");
        sb.append("                ").append(resultName).append(" = ").append(param1Name).append(" + ").append(param2Name).append(";\n");
        sb.append("                break;\n");
        sb.append("            case \"subtract\":\n");
        sb.append("                ").append(resultName).append(" = ").append(param1Name).append(" - ").append(param2Name).append(";\n");
        sb.append("                break;\n");
        sb.append("            case \"multiply\":\n");
        sb.append("                ").append(resultName).append(" = ").append(param1Name).append(" * ").append(param2Name).append(";\n");
        sb.append("                break;\n");
        sb.append("            case \"divide\":\n");
        sb.append("                if (").append(param2Name).append(" != 0) {\n");
        sb.append("                    ").append(resultName).append(" = ").append(param1Name).append(" / ").append(param2Name).append(";\n");
        sb.append("                } else {\n");
        sb.append("                    Log.e(").append(tagVarName).append(", \"Division by zero\");\n");
        sb.append("                }\n");
        sb.append("                break;\n");
        sb.append("            case \"power\":\n");
        sb.append("                ").append(resultName).append(" = Math.pow(").append(param1Name).append(", ").append(param2Name).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"root\":\n");
        sb.append("                if (").append(param2Name).append(" != 0) {\n");
        sb.append("                    ").append(resultName).append(" = Math.pow(").append(param1Name).append(", 1.0 / ").append(param2Name).append(");\n");
        sb.append("                } else {\n");
        sb.append("                    Log.e(").append(tagVarName).append(", \"Root of zero\");\n");
        sb.append("                }\n");
        sb.append("                break;\n");
        sb.append("            case \"abs\":\n");
        sb.append("                ").append(resultName).append(" = Math.abs(").append(param1Name).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"round\":\n");
        sb.append("                ").append(resultName).append(" = Math.round(").append(param1Name).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"ceil\":\n");
        sb.append("                ").append(resultName).append(" = Math.ceil(").append(param1Name).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"floor\":\n");
        sb.append("                ").append(resultName).append(" = Math.floor(").append(param1Name).append(");\n");
        sb.append("                break;\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultName).append(";\n");
        sb.append("    }\n\n");

        // 数组操作方法
        sb.append("    public double ").append(operationTypeVarName).append("(double[] ")
                .append(param1Name).append(") {\n");
        sb.append("        if (").append(param1Name).append(" == null || ").append(param1Name).append(".length == 0) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Values array is null or empty\");\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        double ").append(resultName).append(" = ").append(param1Name).append("[0];\n");
        sb.append("        for (int i = 1; i < ").append(param1Name).append(".length; i++) {\n");
        sb.append("            ").append(resultName).append(" = ").append(operationTypeVarName)
                .append("(").append(resultName).append(", ").append(param1Name).append("[i]);\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成异步操作方法
     */
    /**
     * 生成异步操作方法
     */
    private void generateAsyncMethods(StringBuilder sb, String className, String valueVarName,
                                      String tagVarName, String asyncHandler, String handlerVarName) {
        String asyncOperationMethodName = RandomUtils.generateWord(6);
        String callbackMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        if (asyncHandler.contains("coroutines")) {
            // 使用协程的异步方法
            sb.append("    public void ").append(asyncOperationMethodName).append("(double ")
                    .append(paramName).append(") {\n");
            sb.append("        CoroutineScope scope = new CoroutineScope(Dispatchers.IO);\n");
            sb.append("        scope.launch(() -> {\n");
            sb.append("            double ").append(resultName).append(" = 0;\n");
            sb.append("            try {\n");
            sb.append("                // 模拟耗时操作\n");
            sb.append("                Thread.sleep(100);\n");
            sb.append("                ").append(resultName).append(" = Math.sqrt(").append(paramName).append(");\n");
            sb.append("                withContext(Dispatchers.Main) {\n");
            sb.append("                    ").append(valueVarName).append(" = ").append(resultName).append(";\n");
            sb.append("                }\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                Log.e(").append(tagVarName).append(", \"Async operation error\", e);\n");
            sb.append("            }\n");
            sb.append("        });\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            // 使用RxJava的异步方法
            sb.append("    public void ").append(asyncOperationMethodName).append("(double ")
                    .append(paramName).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            // 模拟耗时操作\n");
            sb.append("            Thread.sleep(100);\n");
            sb.append("            return Math.sqrt(").append(paramName).append(");\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.computation())\n");
            sb.append("          .subscribe(\n");
            sb.append("              result -> {\n");
            sb.append("                  ").append(valueVarName).append(" = result;\n");
            sb.append("              },\n");
            sb.append("              error -> {\n");
            sb.append("                  Log.e(").append(tagVarName).append(", \"Async operation error\", error);\n");
            sb.append("              }\n");
            sb.append("          );\n");
            sb.append("    }\n\n");
        } else {
            // 使用Handler的异步方法
            sb.append("    public void ").append(asyncOperationMethodName).append("(double ")
                    .append(paramName).append(") {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            double ").append(resultName).append(" = 0;\n");
            sb.append("            try {\n");
            sb.append("                // 模拟耗时操作\n");
            sb.append("                Thread.sleep(100);\n");
            sb.append("                ").append(resultName).append(" = Math.sqrt(").append(paramName).append(");\n");
            sb.append("                ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("                    ").append(valueVarName).append(" = ").append(resultName).append(";\n");
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
        sb.append("    public void ").append(callbackMethodName).append("(double ")
                .append(paramName).append(") {\n");
        sb.append("        ").append(asyncOperationMethodName).append("(").append(paramName).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成验证方法
     */
    private void generateValidationMethods(StringBuilder sb, String className, String valueVarName,
                                           String tagVarName) {
        String validateMethodName = RandomUtils.generateWord(6);
        String isValidMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String minName = RandomUtils.generateWord(6);
        String maxName = RandomUtils.generateWord(6);

        // 验证方法
        sb.append("    public boolean ").append(validateMethodName).append("(double ")
                .append(paramName).append(", double ").append(minName).append(", double ")
                .append(maxName).append(") {\n");
        sb.append("        if (").append(paramName).append(" < ").append(minName).append(" || ")
                .append(paramName).append(" > ").append(maxName).append(") {\n");
        sb.append("            Log.w(").append(tagVarName).append(", \"Value \" + ")
                .append(paramName).append(" + \" is out of range [\" + ")
                .append(minName).append(" + \", \" + ").append(maxName).append(" + \"]\");\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return true;\n");
        sb.append("    }\n\n");

        // 检查当前值是否有效方法
        sb.append("    public boolean ").append(isValidMethodName).append("() {\n");
        sb.append("        return !Double.isNaN(").append(valueVarName).append(") && ")
                .append(" !Double.isInfinite(").append(valueVarName).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成格式化方法
     */
    private void generateFormattingMethods(StringBuilder sb, String className, String valueVarName,
                                           String formatVarName, String tagVarName) {
        String formatMethodName = RandomUtils.generateWord(6);
        String formatWithPatternMethodName = RandomUtils.generateWord(6);
        String patternName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        // 格式化方法
        sb.append("    public String ").append(formatMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            return ").append(formatVarName).append(".format(")
                .append(valueVarName).append(");\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Formatting error\", e);\n");
        sb.append("            return String.valueOf(").append(valueVarName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 使用自定义模式格式化方法
        sb.append("    public String ").append(formatWithPatternMethodName).append("(String ")
                .append(patternName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            DecimalFormat df = new DecimalFormat(").append(patternName).append(");\n");
        sb.append("            String ").append(resultName).append(" = df.format(").append(valueVarName).append(");\n");
        sb.append("            return ").append(resultName).append(";\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Pattern formatting error\", e);\n");
        sb.append("            return String.valueOf(").append(valueVarName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成转换方法
     */
    private void generateConversionMethods(StringBuilder sb, String className, String valueVarName,
                                           String tagVarName) {
        String toIntMethodName = RandomUtils.generateWord(6);
        String toLongMethodName = RandomUtils.generateWord(6);
        String toFloatMethodName = RandomUtils.generateWord(6);
        String toBigDecimalMethodName = RandomUtils.generateWord(6);
        String toBigIntegerMethodName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        // 转换为整数方法
        sb.append("    public int ").append(toIntMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            return (int) ").append(valueVarName).append(";\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Conversion to int error\", e);\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 转换为长整数方法
        sb.append("    public long ").append(toLongMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            return (long) ").append(valueVarName).append(";\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Conversion to long error\", e);\n");
        sb.append("            return 0L;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 转换为浮点数方法
        sb.append("    public float ").append(toFloatMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            return (float) ").append(valueVarName).append(";\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Conversion to float error\", e);\n");
        sb.append("            return 0.0f;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 转换为BigDecimal方法
        sb.append("    public BigDecimal ").append(toBigDecimalMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            return BigDecimal.valueOf(").append(valueVarName).append(");\n");
        sb.append("        } catch (Exception e) {\n");
                sb.append("            Log.e(").append(tagVarName).append(", \"Conversion to BigDecimal error\", e);\n");
        sb.append("            return BigDecimal.ZERO;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 转换为BigInteger方法
        sb.append("    public BigInteger ").append(toBigIntegerMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            return BigInteger.valueOf((long) ").append(valueVarName).append(");\n");
        sb.append("        } catch (Exception e) {\n");
                sb.append("            Log.e(").append(tagVarName).append(", \"Conversion to BigInteger error\", e);\n");
        sb.append("            return BigInteger.ZERO;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成比较方法
     */
    private void generateComparisonMethods(StringBuilder sb, String className, String valueVarName,
                                           String tagVarName) {
        String isEqualToMethodName = RandomUtils.generateWord(6);
        String isGreaterThanMethodName = RandomUtils.generateWord(6);
        String isLessThanMethodName = RandomUtils.generateWord(6);
        String isBetweenMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String minName = RandomUtils.generateWord(6);
        String maxName = RandomUtils.generateWord(6);

        // 等于比较方法
        sb.append("    public boolean ").append(isEqualToMethodName).append("(double ")
                .append(paramName).append(") {\n");
        sb.append("        return Double.compare(").append(valueVarName).append(", ")
                .append(paramName).append(") == 0;\n");
        sb.append("    }\n\n");

        // 大于比较方法
        sb.append("    public boolean ").append(isGreaterThanMethodName).append("(double ")
                .append(paramName).append(") {\n");
        sb.append("        return Double.compare(").append(valueVarName).append(", ")
                .append(paramName).append(") > 0;\n");
        sb.append("    }\n\n");

        // 小于比较方法
        sb.append("    public boolean ").append(isLessThanMethodName).append("(double ")
                .append(paramName).append(") {\n");
        sb.append("        return Double.compare(").append(valueVarName).append(", ")
                .append(paramName).append(") < 0;\n");
        sb.append("    }\n\n");

        // 范围比较方法
        sb.append("    public boolean ").append(isBetweenMethodName).append("(double ")
                .append(minName).append(", double ").append(maxName).append(") {\n");
        sb.append("        return ").append(valueVarName).append(" >= ").append(minName)
                .append(" && ").append(valueVarName).append(" <= ").append(maxName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成统计方法
     */
    private void generateStatisticsMethods(StringBuilder sb, String className, String valueVarName,
                                           String tagVarName) {
        String averageMethodName = RandomUtils.generateWord(6);
        String standardDeviationMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String sumName = RandomUtils.generateWord(6);
        String meanName = RandomUtils.generateWord(6);
        String varianceName = RandomUtils.generateWord(6);
        String stdDevName = RandomUtils.generateWord(6);

        // 平均值方法
        sb.append("    public double ").append(averageMethodName).append("(double[] ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null || ").append(paramName).append(".length == 0) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Values array is null or empty\");\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        double ").append(sumName).append(" = 0;\n");
        sb.append("        for (double value : ").append(paramName).append(") {\n");
        sb.append("            ").append(sumName).append(" += value;\n");
        sb.append("        }\n");
        sb.append("        double ").append(meanName).append(" = ").append(sumName).append(" / ")
                .append(paramName).append(".length;\n");
        sb.append("        return ").append(meanName).append(";\n");
        sb.append("    }\n\n");

        // 标准差方法
        sb.append("    public double ").append(standardDeviationMethodName).append("(double[] ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null || ").append(paramName).append(".length < 2) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Values array is null or has less than 2 elements\");\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        double ").append(meanName).append(" = ").append(averageMethodName)
                .append("(").append(paramName).append(");\n");
        sb.append("        double ").append(varianceName).append(" = 0;\n");
        sb.append("        for (double value : ").append(paramName).append(") {\n");
        sb.append("            ").append(varianceName).append(" += Math.pow(value - ")
                .append(meanName).append(", 2);\n");
        sb.append("        }\n");
        sb.append("        ").append(varianceName).append(" /= ").append(paramName).append(".length;\n");
        sb.append("        double ").append(stdDevName).append(" = Math.sqrt(")
                .append(varianceName).append(");\n");
        sb.append("        return ").append(stdDevName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成缓存方法
     */
    private void generateCachingMethods(StringBuilder sb, String className, String valueVarName,
                                        String cacheVarName, String tagVarName) {
        String putInCacheMethodName = RandomUtils.generateWord(6);
        String getFromCacheMethodName = RandomUtils.generateWord(6);
        String clearCacheMethodName = RandomUtils.generateWord(6);
        String keyName = RandomUtils.generateWord(6);
        String valueName = RandomUtils.generateWord(6);

        // 放入缓存方法
        sb.append("    public void ").append(putInCacheMethodName).append("(String ")
                .append(keyName).append(", double ").append(valueName).append(") {\n");
        sb.append("        if (").append(keyName).append(" != null && !")
                .append(keyName).append(".isEmpty()) {\n");
        sb.append("            ").append(cacheVarName).append(".put(").append(keyName)
                .append(", ").append(valueName).append(");\n");
        sb.append("        } else {\n");
        sb.append("            Log.w(").append(tagVarName).append(", \"Cache key is null or empty\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 从缓存获取方法
        sb.append("    public Double ").append(getFromCacheMethodName).append("(String ")
                .append(keyName).append(") {\n");
        sb.append("        if (").append(keyName).append(" != null) {\n");
        sb.append("            Double ").append(valueName).append(" = ").append(cacheVarName)
                .append(".get(").append(keyName).append(");\n");
        sb.append("            if (").append(valueName).append(" != null) {\n");
        sb.append("                return ").append(valueName).append(";\n");
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
     * 生成精度控制方法
     */
    private void generatePrecisionMethods(StringBuilder sb, String className, String valueVarName,
                                          String precisionVarName, String tagVarName) {
        String setPrecisionMethodName = RandomUtils.generateWord(6);
        String getPrecisionMethodName = RandomUtils.generateWord(6);
        String roundWithPrecisionMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        // 设置精度方法
        sb.append("    public void ").append(setPrecisionMethodName).append("(int ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" >= 0 && ")
                .append(paramName).append(" <= 10) {\n");
        sb.append("            // 这里需要更新formatVarName的精度设置\n");
        sb.append("            // 由于formatVarName是局部变量，这里只是示例\n");
        sb.append("        } else {\n");
        sb.append("            Log.w(").append(tagVarName).append(", \"Invalid precision value: \" + ")
                .append(paramName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 获取精度方法
        sb.append("    public int ").append(getPrecisionMethodName).append("() {\n");
        sb.append("        return ").append(precisionVarName).append(";\n");
        sb.append("    }\n\n");

        // 使用指定精度四舍五入方法
        sb.append("    public double ").append(roundWithPrecisionMethodName).append("(double ")
                .append(paramName).append(") {\n");
        sb.append("        double ").append(resultName).append(" = 0;\n");
        sb.append("        try {\n");
        sb.append("            BigDecimal bd = BigDecimal.valueOf(").append(paramName).append(");\n");
        sb.append("            bd = bd.setScale(").append(precisionVarName).append(", ")
                .append("BigDecimal.ROUND_HALF_UP);\n");
        sb.append("            ").append(resultName).append(" = bd.doubleValue();\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Rounding error\", e);\n");
        sb.append("            ").append(resultName).append(" = ").append(paramName).append(";\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成示例使用方法
     */
    private void generateExampleUsageMethod(StringBuilder sb, String className,
                                            String valueVarName, String formatVarName,
                                            String cacheVarName, String tagVarName) {
        String exampleMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(exampleMethodName).append("(double ")
                .append(paramName).append(") {\n");
        sb.append("        // 设置值\n");
        sb.append("        ").append(paramName).append(" = 42.5;\n");
        sb.append("        ").append(valueVarName).append(" = ").append(paramName).append(";\n");

        if (useAsyncOperations) {
            sb.append("        // 异步操作\n");
            sb.append("        asyncOperation(").append(paramName).append(");\n");
        }

        if (useValidation) {
            sb.append("        // 验证操作\n");
            sb.append("        boolean isValid = validate(").append(paramName).append(", 0, 100);\n");
        }

        if (useFormatting) {
            sb.append("        // 格式化操作\n");
            sb.append("        String formatted = format();\n");
        }

        if (useConversion) {
            sb.append("        // 转换操作\n");
            sb.append("        int intValue = toInt();\n");
        }

        if (useComparison) {
            sb.append("        // 比较操作\n");
            sb.append("        boolean isEqual = isEqualTo(").append(paramName).append(");\n");
        }

        if (useStatistics) {
            sb.append("        // 统计操作\n");
            sb.append("        double[] values = {1.0, 2.0, 3.0, 4.0, 5.0};\n");
            sb.append("        double avg = average(values);\n");
        }

        if (useCaching) {
            sb.append("        // 缓存操作\n");
            sb.append("        putInCache(\"key1\", ").append(paramName).append(");\n");
            sb.append("        Double cached = getFromCache(\"key1\");\n");
        }

        if (usePrecisionControl) {
            sb.append("        // 精度控制操作\n");
            sb.append("        double rounded = roundWithPrecision(").append(paramName).append(");\n");
        }

        sb.append("    }\n\n");
    }
}
