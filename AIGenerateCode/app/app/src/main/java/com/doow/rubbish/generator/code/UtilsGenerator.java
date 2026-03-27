package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class UtilsGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] UTIL_TYPES = {
            "DataUtils", "DateUtils", "StringUtils", "FileUtils",
            "ColorUtils", "SizeUtils", "ValidationUtils", "LogUtils",
            "MathUtils", "NumberUtils", "CollectionUtils", "ArrayUtils",
            "MapUtils", "SetUtils", "ListUtils", "QueueUtils",
            "StackUtils", "StreamUtils", "OptionalUtils", "PredicateUtils",
            "ConsumerUtils", "SupplierUtils", "ComparatorUtils", "FormatUtils",
            "ConvertUtils", "ParseUtils", "EncodeUtils", "DecodeUtils",
            "HashUtils", "EncryptUtils", "CompressUtils", "DecompressUtils"
    };

    public UtilsGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成 " + UTIL_TYPES.length + " 个Utils工具类");

        for (String utilType : UTIL_TYPES) {
            // 修复：直接使用工具类型作为类名，避免随机生成导致命名不规范
            String className = utilType;
            generateUtils(className, utilType);
        }
    }

    private void generateUtils(String className, String utilType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 空值防护：给异步处理器/UI风格设置默认值
        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");
        asyncHandler = (asyncHandler == null) ? "thread" : asyncHandler;

        // 生成包声明
        sb.append(generatePackageDeclaration("utils"));

        // 导入核心依赖
        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.content.res.Resources"));
        sb.append(generateImportStatement("android.util.DisplayMetrics"));
        sb.append(generateImportStatement("android.util.TypedValue"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.HashMap"));
        // 补充线程安全集合导入
        sb.append(generateImportStatement("java.util.Collections"));

        // 异步处理依赖（空值防护）
        if (asyncHandler != null) {
            if (asyncHandler.contains("coroutines")) {
                sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
                sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
                sb.append(generateImportStatement("kotlinx.coroutines.launch"));
            } else if (asyncHandler.contains("rxjava")) {
                sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
                sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
            }
        }

        sb.append("\n"); // 规范空行，替换多余的空白拼接

        // 类定义（修复缩进）
        sb.append("public final class ").append(className).append(" {\n\n");

        // 常量定义（修复字符串转义）
        sb.append("    private static final String TAG = \"").append(className).append("\";\n\n");

        // 使用标志变量确保字段和配套方法一起生成和使用
        boolean useCache = RandomUtils.randomBoolean();
        boolean useConfig = RandomUtils.randomBoolean();
        boolean useValidation = RandomUtils.randomBoolean();
        boolean useConversion = RandomUtils.randomBoolean();
        boolean useAsync = RandomUtils.randomBoolean();

        // 根据标志变量生成字段（线程安全处理）
        if (useCache) {
            sb.append("    private static volatile Map<String, Object> cache;\n");
            sb.append("    private static volatile boolean cacheInitialized = false;\n\n");
        }

        if (useConfig) {
            sb.append("    private static volatile Map<String, String> config;\n");
            sb.append("    private static volatile boolean configInitialized = false;\n\n");
        }

        // 私有构造方法（禁止实例化，工具类规范）
        sb.append("    private ").append(className).append("() {\n");
        sb.append("        throw new AssertionError(\"No instance of \" + ").append(className).append(".class.getName() + \" allowed!\");\n");
        sb.append("    }\n\n");

        // 初始化方法（双重检查锁，线程安全）
        if (useCache || useConfig) {
            sb.append("    private static void initialize() {\n");
            if (useCache) {
                sb.append("        if (!cacheInitialized) {\n");
                sb.append("            synchronized (").append(className).append(".class) {\n");
                sb.append("                if (!cacheInitialized) {\n");
                sb.append("                    cache = Collections.synchronizedMap(new HashMap<>());\n");
                sb.append("                    cacheInitialized = true;\n");
                sb.append("                }\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
            if (useConfig) {
                sb.append("        if (!configInitialized) {\n");
                sb.append("            synchronized (").append(className).append(".class) {\n");
                sb.append("                if (!configInitialized) {\n");
                sb.append("                    config = Collections.synchronizedMap(new HashMap<>());\n");
                sb.append("                    configInitialized = true;\n");
                sb.append("                }\n");
                sb.append("            }\n");
                sb.append("        }\n");
            }
            sb.append("    }\n\n");
        }

        // 根据标志变量生成配套方法
        if (useCache) {
            generateCacheMethods(sb);
        }

        if (useConfig) {
            generateConfigMethods(sb);
        }

        if (useValidation) {
            generateValidationMethods(sb);
        }

        if (useConversion) {
            generateConversionMethods(sb);
        }

        if (useAsync) {
            generateAsyncMethods(sb, asyncHandler);
        }

        // 生成业务方法（3-6个，边界防护）
        int methodCount = RandomUtils.between(3, 6);
        for (int i = 0; i < methodCount; i++) {
            String methodName = RandomUtils.generateMethodName("process");
            String returnType = getRandomType();

            sb.append("    public static ").append(returnType).append(" ").append(methodName).append("(");

            int paramCount = RandomUtils.between(0, 3);
            for (int j = 0; j < paramCount; j++) {
                if (j > 0) sb.append(", ");
                String paramType = getRandomType();
                String paramName = RandomUtils.generateVariableName("param" + j);
                sb.append(paramType).append(" ").append(paramName);
            }
            sb.append(") {\n");

            if (useCache) {
                sb.append("        initialize();\n");
            }

            // 修复：字符串转义 + 空值防护
            if (returnType.equals("void")) {
                sb.append("        Log.d(TAG, \"").append(methodName).append(" called\");\n");
            } else if (returnType.equals("boolean")) {
                boolean randomBool = RandomUtils.randomBoolean();
                sb.append("        boolean result = ").append(randomBool).append(";\n");
                sb.append("        Log.d(TAG, \"").append(methodName).append(" result: \" + result);\n");
                sb.append("        return result;\n");
            } else if (returnType.equals("int")) {
                int randomInt = RandomUtils.between(0, 100);
                sb.append("        int result = ").append(randomInt).append(";\n");
                sb.append("        Log.d(TAG, \"").append(methodName).append(" result: \" + result);\n");
                sb.append("        return result;\n");
            } else if (returnType.equals("String")) {
                String randomWord = RandomUtils.generateWord(RandomUtils.between(3, 8));
                sb.append("        String result = \"").append(randomWord).append("\";\n");
                sb.append("        Log.d(TAG, \"").append(methodName).append(" result: \" + result);\n");
                sb.append("        return result;\n");
            }
            sb.append("    }\n\n");
        }

        // 闭合类
        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "utils");
    }

    /**
     * 获取随机返回类型（简化版，仅返回常用类型）
     */
    public String getRandomType() {
        String[] types = {"void", "boolean", "int", "String"};
        return types[RandomUtils.between(0, types.length - 1)];
    }

    private void generateCacheMethods(StringBuilder sb) {
        sb.append("    public static void putCache(String key, Object value) {\n");
        sb.append("        initialize();\n");
        sb.append("        if (key != null) {\n");
        sb.append("            cache.put(key, value);\n");
        sb.append("            Log.d(TAG, \"Cached: \" + key);\n");
        sb.append("        } else {\n");
        sb.append("            Log.w(TAG, \"Cache key is null, skip put\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public static Object getCache(String key) {\n");
        sb.append("        initialize();\n");
        sb.append("        if (key != null) {\n");
        sb.append("            return cache.get(key);\n");
        sb.append("        }\n");
        sb.append("        Log.w(TAG, \"Cache key is null\");\n");
        sb.append("        return null;\n");
        sb.append("    }\n\n");

        sb.append("    public static void clearCache() {\n");
        sb.append("        initialize();\n");
        sb.append("        cache.clear();\n");
        sb.append("        Log.d(TAG, \"Cache cleared\");\n");
        sb.append("    }\n\n");
    }

    private void generateConfigMethods(StringBuilder sb) {
        sb.append("    public static void setConfig(String key, String value) {\n");
        sb.append("        initialize();\n");
        sb.append("        if (key != null) {\n");
        sb.append("            config.put(key, value);\n");
        sb.append("            Log.d(TAG, \"Config set: \" + key + \" = \" + value);\n");
        sb.append("        } else {\n");
        sb.append("            Log.w(TAG, \"Config key is null, skip set\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    /**\n");
        sb.append("     * 获取配置\n");
        sb.append("     * @param key 配置键\n");
        sb.append("     * @return 配置值（null表示无配置或键为null）\n");
        sb.append("     */\n");
        sb.append("    public static String getConfig(String key) {\n");
        sb.append("        initialize();\n");
        sb.append("        if (key != null) {\n");
        sb.append("            return config.get(key);\n");
        sb.append("        }\n");
        sb.append("        Log.w(TAG, \"Config key is null\");\n");
        sb.append("        return null;\n");
        sb.append("    }\n\n");

        sb.append("    public static void clearConfig() {\n");
        sb.append("        initialize();\n");
        sb.append("        config.clear();\n");
        sb.append("        Log.d(TAG, \"Config cleared\");\n");
        sb.append("    }\n\n");
    }

    private void generateValidationMethods(StringBuilder sb) {
        sb.append("    public static boolean isEmpty(String value) {\n");
        sb.append("        return value == null || value.trim().isEmpty();\n");
        sb.append("    }\n\n");

        sb.append("    public static boolean isNotEmpty(String value) {\n");
        sb.append("        return !isEmpty(value);\n");
        sb.append("    }\n\n");

        sb.append("    public static boolean isValidEmail(String email) {\n");
        sb.append("        if (isEmpty(email)) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return email.contains(\"@\") && email.contains(\".\") && email.indexOf(\"@\") < email.lastIndexOf(\".\");\n");
        sb.append("    }\n\n");
    }

    private void generateConversionMethods(StringBuilder sb) {
        sb.append("    public static int dpToPx(Context context, float dp) {\n");
        sb.append("        if (context == null) {\n");
        sb.append("            Log.e(TAG, \"Context is null in dpToPx\");\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        Resources resources = context.getResources();\n");
        sb.append("        DisplayMetrics metrics = resources.getDisplayMetrics();\n");
        sb.append("        return (int) (dp * metrics.density + 0.5f);\n");
        sb.append("    }\n\n");

        sb.append("    public static float pxToDp(Context context, int px) {\n");
        sb.append("        if (context == null) {\n");
        sb.append("            Log.e(TAG, \"Context is null in pxToDp\");\n");
        sb.append("            return 0f;\n");
        sb.append("        }\n");
        sb.append("        Resources resources = context.getResources();\n");
        sb.append("        DisplayMetrics metrics = resources.getDisplayMetrics();\n");
        sb.append("        return px / metrics.density;\n");
        sb.append("    }\n\n");

        sb.append("    public static int getScreenWidth(Context context) {\n");
        sb.append("        if (context == null) {\n");
        sb.append("            Log.e(TAG, \"Context is null in getScreenWidth\");\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        Resources resources = context.getResources();\n");
        sb.append("        DisplayMetrics metrics = resources.getDisplayMetrics();\n");
        sb.append("        return metrics.widthPixels;\n");
        sb.append("    }\n\n");

        sb.append("    public static int getScreenHeight(Context context) {\n");
        sb.append("        if (context == null) {\n");
        sb.append("            Log.e(TAG, \"Context is null in getScreenHeight\");\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        Resources resources = context.getResources();\n");
        sb.append("        DisplayMetrics metrics = resources.getDisplayMetrics();\n");
        sb.append("        return metrics.heightPixels;\n");
        sb.append("    }\n\n");
    }

    private void generateAsyncMethods(StringBuilder sb, String asyncHandler) {
        // 空值防护：默认使用普通线程
        if (asyncHandler == null) {
            asyncHandler = "thread";
        }

        if (asyncHandler.contains("coroutines")) {
            sb.append("    public static void executeAsync(Runnable task) {\n");
            sb.append("        if (task == null) {\n");
            sb.append("            Log.w(TAG, \"Async task is null, skip execute\");\n");
            sb.append("            return;\n");
            sb.append("        }\n");
            // 修复：Kotlin协程正确使用方式（补充launch导入）
            sb.append("        CoroutineScope scope = CoroutineScope(Dispatchers.IO);\n");
            sb.append("        scope.launch {\n");
            sb.append("            try {\n");
            sb.append("                task.run();\n");
            sb.append("                Log.d(TAG, \"Coroutine task executed successfully\");\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                Log.e(TAG, \"Coroutine task execute failed\", e);\n");
            sb.append("            }\n");
            sb.append("        };\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("    public static Single<String> executeAsync(String input) {\n");
            sb.append("        return Single.fromCallable(() -> {\n");
            sb.append("            if (input == null) {\n");
            sb.append("                Log.w(TAG, \"RxJava input is null\");\n");
            sb.append("                return \"Processed: null\";\n");
            sb.append("            }\n");
            // 修复：字符串转义
            sb.append("            return \"Processed: \" + input;\n");
            // 修复：RxJava链式调用格式（换行+缩进）
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.single())\n");
            sb.append("          .onErrorReturn(e -> {\n");
            sb.append("              Log.e(TAG, \"RxJava task execute failed\", e);\n");
            sb.append("              return \"Error: \" + e.getMessage();\n");
            sb.append("          });\n");
            sb.append("    }\n\n");
        } else {
            sb.append("    public static void executeAsync(Runnable task) {\n");
            sb.append("        if (task == null) {\n");
            sb.append("            Log.w(TAG, \"Thread task is null, skip execute\");\n");
            sb.append("            return;\n");
            sb.append("        }\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            try {\n");
            sb.append("                task.run();\n");
            sb.append("                Log.d(TAG, \"Thread task executed successfully\");\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                Log.e(TAG, \"Thread task execute failed\", e);\n");
            sb.append("            }\n");
            sb.append("        }).start();\n");
            sb.append("    }\n\n");
        }
    }
}