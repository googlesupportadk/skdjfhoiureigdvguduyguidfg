package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalAlgebraGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] OPERATION_TYPES = {
            "add", "subtract", "multiply", "divide", "power", "root",
            "logarithm", "exponential", "factorial", "absolute", "modulo",
            "gcd", "lcm", "prime", "composite", "square", "cube"
    };

    private static final String[] VARIABLE_TYPES = {
            "x", "y", "z", "a", "b", "c",
            "p", "q", "r", "s", "t", "u",
            "v", "w", "m", "n", "k", "l"
    };

    public LocalAlgebraGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成代数计算类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Algebra");
            generateAlgebraClass(className, asyncHandler);
        }
    }

    private void generateAlgebraClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("math.algebra"));

        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        } else if (asyncHandler.contains("rxjava")) {
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 功能标志 - 确保所有字段和方法都会被使用
        boolean useCache = RandomUtils.randomBoolean();
        boolean useHistory = RandomUtils.randomBoolean();
        boolean useValidation = RandomUtils.randomBoolean();
        boolean useStatistics = RandomUtils.randomBoolean();
        boolean useAsync = RandomUtils.randomBoolean();
        boolean usePrecision = RandomUtils.randomBoolean();
        boolean useRange = RandomUtils.randomBoolean();
        boolean useEquations = RandomUtils.randomBoolean();

        sb.append("public class ").append(className).append(" {\n\n");

        String operationType = OPERATION_TYPES[RandomUtils.between(0, OPERATION_TYPES.length - 1)];
        String variableType = VARIABLE_TYPES[RandomUtils.between(0, VARIABLE_TYPES.length - 1)];

        // 基础常量
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String OPERATION_TYPE = \"").append(operationType).append("\";\n");
        sb.append("    private static final String VARIABLE_TYPE = \"").append(variableType).append("\";\n");

        if (usePrecision) {
            sb.append("    private static final int DEFAULT_PRECISION = ").append(RandomUtils.between(2, 10)).append(";\n");
        }

        if (useRange) {
            sb.append("    private static final double MIN_VALUE = ").append(RandomUtils.nextDouble(-1000.0, 0.0)).append(";\n");
            sb.append("    private static final double MAX_VALUE = ").append(RandomUtils.nextDouble(0.0, 1000.0)).append(";\n");
        }

        sb.append("\n");

        // 缓存字段
        if (useCache) {
            sb.append("    private Map<String, Double> calculationCache;\n");
            sb.append("    private boolean cacheEnabled = true;\n");
            sb.append("    private int maxCacheSize = 100;\n\n");
        }

        // 历史记录字段
        if (useHistory) {
            sb.append("    private Map<String, Double> operationHistory;\n");
            sb.append("    private int maxHistorySize = 50;\n");
            sb.append("    private long lastOperationTime = 0;\n\n");
        }

        // 统计字段
        if (useStatistics) {
            sb.append("    private int operationCount = 0;\n");
            sb.append("    private double totalResult = 0.0;\n");
            sb.append("    private double minResult = Double.MAX_VALUE;\n");
            sb.append("    private double maxResult = Double.MIN_VALUE;\n\n");
        }

        // 精度字段
        if (usePrecision) {
            sb.append("    private int precision = DEFAULT_PRECISION;\n\n");
        }

        // 变量字段
        sb.append("    private double ").append(variableType).append("Value;\n");
        sb.append("    private double ").append(variableType).append("Result;\n\n");

        // 构造方法
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        initialize();\n");
        sb.append("    }\n\n");

        // 初始化方法
        sb.append("    private void initialize() {\n");
        sb.append("        Log.d(TAG, \"Initializing ").append(className).append(" with operation: \").append(OPERATION_TYPE);\n");

        if (useCache) {
            sb.append("        calculationCache = new HashMap<>();\n");
        }

        if (useHistory) {
            sb.append("        operationHistory = new HashMap<>();\n");
            sb.append("        lastOperationTime = System.currentTimeMillis();\n");
        }

        if (useStatistics) {
            sb.append("        resetStatistics();\n");
        }

        sb.append("        ").append(variableType).append("Value = 0.0;\n");
        sb.append("        ").append(variableType).append("Result = 0.0;\n");
        sb.append("    }\n\n");

        // 基础操作方法
        sb.append("    public double ").append(operationType).append("(double a, double b) {\n");
        sb.append("        double result = 0.0;\n");

        if (useValidation) {
            sb.append("        if (!validateInput(a, b)) {\n");
            sb.append("            return Double.NaN;\n");
            sb.append("        }\n");
        }

        sb.append("        switch (OPERATION_TYPE) {\n");
        sb.append("            case \"add\":\n");
        sb.append("                result = a + b;\n");
        sb.append("                break;\n");
        sb.append("            case \"subtract\":\n");
        sb.append("                result = a - b;\n");
        sb.append("                break;\n");
        sb.append("            case \"multiply\":\n");
        sb.append("                result = a * b;\n");
        sb.append("                break;\n");
        sb.append("            case \"divide\":\n");
        sb.append("                if (b != 0) {\n");
        sb.append("                    result = a / b;\n");
        sb.append("                } else {\n");
        sb.append("                    Log.e(TAG, \"Division by zero\");\n");
        sb.append("                    return Double.NaN;\n");
        sb.append("                }\n");
        sb.append("                break;\n");
        sb.append("            case \"power\":\n");
        sb.append("                result = Math.pow(a, b);\n");
        sb.append("                break;\n");
        sb.append("            case \"root\":\n");
        sb.append("                result = Math.pow(a, 1.0 / b);\n");
        sb.append("                break;\n");
        sb.append("            case \"modulo\":\n");
        sb.append("                if (b != 0) {\n");
        sb.append("                    result = a % b;\n");
        sb.append("                } else {\n");
        sb.append("                    Log.e(TAG, \"Modulo by zero\");\n");
        sb.append("                    return Double.NaN;\n");
        sb.append("                }\n");
        sb.append("                break;\n");
        sb.append("            case \"absolute\":\n");
        sb.append("                result = Math.abs(a);\n");
        sb.append("                break;\n");
        sb.append("            case \"square\":\n");
        sb.append("                result = a * a;\n");
        sb.append("                break;\n");
        sb.append("            case \"cube\":\n");
        sb.append("                result = a * a * a;\n");
        sb.append("                break;\n");
        sb.append("            case \"logarithm\":\n");
        sb.append("                if (a > 0 && b > 0 && b != 1) {\n");
        sb.append("                    result = Math.log(a) / Math.log(b);\n");
        sb.append("                } else {\n");
        sb.append("                    Log.e(TAG, \"Invalid logarithm parameters\");\n");
        sb.append("                    return Double.NaN;\n");
        sb.append("                }\n");
        sb.append("                break;\n");
        sb.append("            case \"exponential\":\n");
        sb.append("                result = Math.exp(a);\n");
        sb.append("                break;\n");
        sb.append("            case \"factorial\":\n");
        sb.append("                if (a >= 0 && a == (int)a) {\n");
        sb.append("                    result = factorial((int)a);\n");
        sb.append("                } else {\n");
        sb.append("                    Log.e(TAG, \"Invalid factorial parameter\");\n");
        sb.append("                    return Double.NaN;\n");
        sb.append("                }\n");
        sb.append("                break;\n");
        sb.append("            case \"gcd\":\n");
        sb.append("                result = gcd((long)a, (long)b);\n");
        sb.append("                break;\n");
        sb.append("            case \"lcm\":\n");
        sb.append("                result = lcm((long)a, (long)b);\n");
        sb.append("                break;\n");
        sb.append("            case \"prime\":\n");
        sb.append("                result = isPrime((long)a) ? 1.0 : 0.0;\n");
        sb.append("                break;\n");
        sb.append("            case \"composite\":\n");
        sb.append("                result = isComposite((long)a) ? 1.0 : 0.0;\n");
        sb.append("                break;\n");
        sb.append("        }\n");

        if (usePrecision) {
            sb.append("        result = roundToPrecision(result, precision);\n");
        }

        if (useRange) {
            sb.append("        result = clampToRange(result, MIN_VALUE, MAX_VALUE);\n");
        }

        if (useCache) {
            sb.append("        String cacheKey = generateCacheKey(a, b);\n");
            sb.append("        cacheResult(cacheKey, result);\n");
        }

        if (useHistory) {
            sb.append("        recordOperation(OPERATION_TYPE, a, b, result);\n");
        }

        if (useStatistics) {
            sb.append("        updateStatistics(result);\n");
        }

        sb.append("        ").append(variableType).append("Value = a;\n");
        sb.append("        ").append(variableType).append("Result = result;\n");
        sb.append("        Log.d(TAG, OPERATION_TYPE + \" result: \" + result);\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        // 验证方法
        if (useValidation) {
            sb.append("    private boolean validateInput(double a, double b) {\n");
            sb.append("        if (Double.isNaN(a) || Double.isInfinite(a)) {\n");
            sb.append("            Log.e(TAG, \"Invalid input a\");\n");
            sb.append("            return false;\n");
            sb.append("        }\n");
            sb.append("        if (Double.isNaN(b) || Double.isInfinite(b)) {\n");
            sb.append("            Log.e(TAG, \"Invalid input b\");\n");
            sb.append("            return false;\n");
            sb.append("        }\n");
            sb.append("        return true;\n");
            sb.append("    }\n\n");
        }

        // 缓存方法
        if (useCache) {
            sb.append("    private String generateCacheKey(double a, double b) {\n");
            sb.append("        return OPERATION_TYPE + \"_\" + a + \"_\" + b;\n");
            sb.append("    }\n\n");

            sb.append("    private void cacheResult(String key, double result) {\n");
            sb.append("        if (!cacheEnabled) {\n");
            sb.append("            return;\n");
            sb.append("        }\n");
            sb.append("        if (calculationCache.size() >= maxCacheSize) {\n");
            sb.append("            calculationCache.clear();\n");
            sb.append("        }\n");
            sb.append("        calculationCache.put(key, result);\n");
            sb.append("    }\n\n");

            sb.append("    public Double getCachedResult(double a, double b) {\n");
            sb.append("        String key = generateCacheKey(a, b);\n");
            sb.append("        return calculationCache.get(key);\n");
            sb.append("    }\n\n");

            sb.append("    public void clearCache() {\n");
            sb.append("        calculationCache.clear();\n");
            sb.append("        Log.d(TAG, \"Cache cleared\");\n");
            sb.append("    }\n\n");

            sb.append("    public void setCacheEnabled(boolean enabled) {\n");
            sb.append("        this.cacheEnabled = enabled;\n");
            sb.append("    }\n\n");
        }

        // 历史记录方法
        if (useHistory) {
            sb.append("    private void recordOperation(String operation, double a, double b, double result) {\n");
            sb.append("        if (operationHistory.size() >= maxHistorySize) {\n");
            sb.append("            operationHistory.clear();\n");
            sb.append("        }\n");
            sb.append("        String key = operation + \"(\" + a + \", \" + b + \")\";\n");
            sb.append("        operationHistory.put(key, result);\n");
            sb.append("        lastOperationTime = System.currentTimeMillis();\n");
            sb.append("    }\n\n");

            sb.append("    public Map<String, Double> getOperationHistory() {\n");
            sb.append("        return new HashMap<>(operationHistory);\n");
            sb.append("    }\n\n");

            sb.append("    public long getLastOperationTime() {\n");
            sb.append("        return lastOperationTime;\n");
            sb.append("    }\n\n");
        }

        // 统计方法
        if (useStatistics) {
            sb.append("    private void updateStatistics(double result) {\n");
            sb.append("        operationCount++;\n");
            sb.append("        totalResult += result;\n");
            sb.append("        if (result < minResult) {\n");
            sb.append("            minResult = result;\n");
            sb.append("        }\n");
            sb.append("        if (result > maxResult) {\n");
            sb.append("            maxResult = result;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public int getOperationCount() {\n");
            sb.append("        return operationCount;\n");
            sb.append("    }\n\n");

            sb.append("    public double getAverageResult() {\n");
            sb.append("        return operationCount > 0 ? totalResult / operationCount : 0.0;\n");
            sb.append("    }\n\n");

            sb.append("    public double getMinResult() {\n");
            sb.append("        return minResult;\n");
            sb.append("    }\n\n");

            sb.append("    public double getMaxResult() {\n");
            sb.append("        return maxResult;\n");
            sb.append("    }\n\n");

            sb.append("    public void resetStatistics() {\n");
            sb.append("        operationCount = 0;\n");
            sb.append("        totalResult = 0.0;\n");
            sb.append("        minResult = Double.MAX_VALUE;\n");
            sb.append("        maxResult = Double.MIN_VALUE;\n");
            sb.append("    }\n\n");
        }

        // 精度方法
        if (usePrecision) {
            sb.append("    private double roundToPrecision(double value, int precision) {\n");
            sb.append("        double factor = Math.pow(10, precision);\n");
            sb.append("        return Math.round(value * factor) / factor;\n");
            sb.append("    }\n\n");

            sb.append("    public void setPrecision(int precision) {\n");
            sb.append("        this.precision = Math.max(0, Math.min(precision, 15));\n");
            sb.append("    }\n\n");

            sb.append("    public int getPrecision() {\n");
            sb.append("        return precision;\n");
            sb.append("    }\n\n");
        }

        // 范围方法
        if (useRange) {
            sb.append("    private double clampToRange(double value, double min, double max) {\n");
            sb.append("        return Math.max(min, Math.min(max, value));\n");
            sb.append("    }\n\n");

            sb.append("    public boolean isInRange(double value) {\n");
            sb.append("        return value >= MIN_VALUE && value <= MAX_VALUE;\n");
            sb.append("    }\n\n");
        }

        // 辅助方法
        sb.append("    private long factorial(int n) {\n");
        sb.append("        if (n <= 1) {\n");
        sb.append("            return 1;\n");
        sb.append("        }\n");
        sb.append("        long result = 1;\n");
        sb.append("        for (int i = 2; i <= n; i++) {\n");
        sb.append("            result *= i;\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        sb.append("    private long gcd(long a, long b) {\n");
        sb.append("        while (b != 0) {\n");
        sb.append("            long temp = b;\n");
        sb.append("            b = a % b;\n");
        sb.append("        a = temp;\n");
        sb.append("        }\n");
        sb.append("        return Math.abs(a);\n");
        sb.append("    }\n\n");

        sb.append("    private long lcm(long a, long b) {\n");
        sb.append("        if (a == 0 || b == 0) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        return Math.abs(a * b) / gcd(a, b);\n");
        sb.append("    }\n\n");

        sb.append("    private boolean isPrime(long n) {\n");
        sb.append("        if (n <= 1) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        if (n <= 3) {\n");
        sb.append("            return true;\n");
        sb.append("        }\n");
        sb.append("        if (n % 2 == 0 || n % 3 == 0) {\n");
        sb.append("        return false;\n");
        sb.append("        }\n");
        sb.append("        for (long i = 5; i * i <= n; i += 6) {\n");
        sb.append("            if (n % i == 0 || n % (i + 2) == 0) {\n");
        sb.append("                return false;\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return true;\n");
        sb.append("    }\n\n");

        sb.append("    private boolean isComposite(long n) {\n");
        sb.append("        return n > 1 && !isPrime(n);\n");
        sb.append("    }\n\n");

        // 方程求解方法
        if (useEquations) {
            sb.append("    public double solveEquation(double a, double b, double c) {\n");
            sb.append("        double discriminant = b * b - 4 * a * c;\n");
            sb.append("        if (discriminant >= 0) {\n");
            sb.append("            double x1 = (-b + Math.sqrt(discriminant)) / (2 * a);\n");
            sb.append("            double x2 = (-b - Math.sqrt(discriminant)) / (2 * a);\n");
            sb.append("            Log.d(TAG, \"Solutions: \" + x1 + \", \" + x2);\n");
            sb.append("            return x1;\n");
            sb.append("        } else {\n");
            sb.append("            Log.e(TAG, \"No real solutions\");\n");
            sb.append("            return Double.NaN;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // Getter和Setter方法
        sb.append("    public double get").append(Character.toUpperCase(variableType.charAt(0))).append(variableType.substring(1)).append("Value() {\n");
        sb.append("        return ").append(variableType).append("Value;\n");
        sb.append("    }\n\n");

        sb.append("    public double get").append(Character.toUpperCase(variableType.charAt(0))).append(variableType.substring(1)).append("Result() {\n");
        sb.append("        return ").append(variableType).append("Result;\n");
        sb.append("    }\n\n");

        sb.append("    public void set").append(Character.toUpperCase(variableType.charAt(0))).append(variableType.substring(1)).append("Value(double value) {\n");
        sb.append("        this.").append(variableType).append("Value = value;\n");
        sb.append("    }\n\n");

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "math.algebra");
    }
}
