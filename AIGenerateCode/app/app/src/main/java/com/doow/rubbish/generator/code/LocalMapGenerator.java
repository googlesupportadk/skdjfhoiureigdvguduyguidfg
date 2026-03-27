package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalMapGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] MAP_TYPES = {
            "HashMap", "LinkedHashMap", "TreeMap", "WeakHashMap", "ConcurrentHashMap",
            "IdentityHashMap", "EnumMap", "ConcurrentSkipListMap", "TreeNavigableMap",
            "ConcurrentNavigableMap", "WeakIdentityHashMap", "LinkedWeakHashMap", "ReferenceMap"
    };

    private static final String[] OPERATION_TYPES = {
            "put", "get", "remove", "contains", "clear", "size", "keys", "values",
            "put_all", "get_all", "remove_all", "contains_key", "contains_value",
            "key_set", "value_set", "entry_set", "put_if_absent", "compute_if_absent",
            "merge", "replace", "replace_all", "compute", "compute_if_present"
    };

    // 功能标志 - 确保所有字段和方法都会被使用
    private boolean useAsyncOperations;
    private boolean useBatchOperations;
    private boolean useFiltering;
    private boolean useTransformation;
    private boolean useValidation;
    private boolean useCaching;
    private boolean useStatistics;
    private boolean usePersistence;

    public LocalMapGenerator(String projectRoot, String packageName) {
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
        usePersistence = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成映射类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Map");
            generateMapClass(className, asyncHandler);
        }
    }

    private void generateMapClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("map"));

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

        if (usePersistence) {
            sb.append(generateImportStatement("android.content.SharedPreferences"));
            sb.append(generateImportStatement("android.content.Context"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 随机选择映射类型和操作类型
        String mapType = MAP_TYPES[RandomUtils.between(0, MAP_TYPES.length - 1)];
        String operationType = OPERATION_TYPES[RandomUtils.between(0, OPERATION_TYPES.length - 1)];

        // 生成类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成常量字段
        String tagVarName = RandomUtils.generateWord(6);
        String mapTypeVarName = RandomUtils.generateWord(6);
        String operationTypeVarName = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVarName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(mapTypeVarName).append(" = \"").append(mapType).append("\";\n");
        sb.append("    private static final String ").append(operationTypeVarName).append(" = \"").append(operationType).append("\";\n\n");

        // 生成实例字段
        String mapVarName = RandomUtils.generateWord(6);
        String randomVarName = RandomUtils.generateWord(6);
        String cacheVarName = RandomUtils.generateWord(6);
        String accessCountVarName = RandomUtils.generateWord(6);
        String accessTimeVarName = RandomUtils.generateWord(6);
        String contextVarName = RandomUtils.generateWord(6);
        String prefsVarName = RandomUtils.generateWord(6);
        String prefsKeyVarName = RandomUtils.generateWord(6);

        sb.append("    private Map<String, String> ").append(mapVarName).append(";\n");
        sb.append("    private Random ").append(randomVarName).append(";\n");

        // 根据功能标志添加条件字段
        if (useCaching) {
            sb.append("    private Map<String, String> ").append(cacheVarName).append(";\n");
        }

        if (useStatistics) {
            sb.append("    private AtomicInteger ").append(accessCountVarName).append(";\n");
            sb.append("    private AtomicLong ").append(accessTimeVarName).append(";\n");
        }

        if (usePersistence) {
            sb.append("    private Context ").append(contextVarName).append(";\n");
            sb.append("    private SharedPreferences ").append(prefsVarName).append(";\n");
            sb.append("    private String ").append(prefsKeyVarName).append(";\n");
        }

        sb.append("\n");

        // 生成构造函数
        generateConstructor(sb, className, mapVarName, randomVarName, cacheVarName,
                accessCountVarName, accessTimeVarName, contextVarName,
                prefsVarName, prefsKeyVarName, mapTypeVarName);

        // 生成基础方法
        generateBasicMethods(sb, className, tagVarName, mapTypeVarName, operationTypeVarName,
                mapVarName, randomVarName, operationType);

        // 根据功能标志添加条件方法
        if (useAsyncOperations) {
            generateAsyncOperationMethods(sb, className, mapVarName, tagVarName, asyncHandler);
        }

        if (useBatchOperations) {
            generateBatchOperationMethods(sb, className, mapVarName, tagVarName);
        }

        if (useFiltering) {
            generateFilteringMethods(sb, className, mapVarName, tagVarName);
        }

        if (useTransformation) {
            generateTransformationMethods(sb, className, mapVarName, tagVarName);
        }

        if (useValidation) {
            generateValidationMethods(sb, className, mapVarName, tagVarName);
        }

        if (useCaching) {
            generateCachingMethods(sb, className, mapVarName, cacheVarName, tagVarName);
        }

        if (useStatistics) {
            generateStatisticsMethods(sb, className, mapVarName, accessCountVarName,
                    accessTimeVarName, tagVarName);
        }

        if (usePersistence) {
            generatePersistenceMethods(sb, className, mapVarName, contextVarName,
                    prefsVarName, prefsKeyVarName, tagVarName);
        }

        // 生成使用所有字段和方法的示例方法
        generateExampleUsageMethod(sb, className, mapVarName, randomVarName, cacheVarName,
                accessCountVarName, accessTimeVarName, contextVarName,
                prefsVarName, prefsKeyVarName, tagVarName, mapTypeVarName,
                operationTypeVarName, operationType);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "map");
    }

    /**
     * 生成构造函数
     */
    private void generateConstructor(StringBuilder sb, String className, String mapVarName,
                                     String randomVarName, String cacheVarName,
                                     String accessCountVarName, String accessTimeVarName,
                                     String contextVarName, String prefsVarName,
                                     String prefsKeyVarName, String mapTypeVarName) {
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        initialize").append(className).append("();\n");
        sb.append("    }\n\n");

        sb.append("    private void initialize").append(className).append("() {\n");
        sb.append("        this.").append(randomVarName).append(" = new Random();\n");
        sb.append("        initializeMap();\n");

        if (useCaching) {
            sb.append("        this.").append(cacheVarName).append(" = new HashMap<>();\n");
        }

        if (useStatistics) {
            sb.append("        this.").append(accessCountVarName).append(" = new AtomicInteger(0);\n");
            sb.append("        this.").append(accessTimeVarName).append(" = new AtomicLong(0);\n");
        }

        if (usePersistence) {
            sb.append("        this.").append(contextVarName).append(" = ContextUtils.getAppContext();\n");
            sb.append("        this.").append(prefsVarName).append(" = ").append(contextVarName)
                    .append(".getSharedPreferences(\"").append(className).append("\", Context.MODE_PRIVATE);\n");
            sb.append("        this.").append(prefsKeyVarName).append(" = \"").append(className).append("_data\";\n");
        }

        sb.append("    }\n\n");

        sb.append("    private void initializeMap() {\n");
        sb.append("        switch (").append(mapTypeVarName).append(") {\n");
        sb.append("            case \"HashMap\":\n");
        sb.append("                ").append(mapVarName).append(" = new HashMap<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"LinkedHashMap\":\n");
        sb.append("                ").append(mapVarName).append(" = new LinkedHashMap<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"TreeMap\":\n");
        sb.append("                ").append(mapVarName).append(" = new TreeMap<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"WeakHashMap\":\n");
        sb.append("                ").append(mapVarName).append(" = new WeakHashMap<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"ConcurrentHashMap\":\n");
        sb.append("                ").append(mapVarName).append(" = new ConcurrentHashMap<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"IdentityHashMap\":\n");
        sb.append("                ").append(mapVarName).append(" = new IdentityHashMap<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"EnumMap\":\n");
        sb.append("                ").append(mapVarName).append(" = new EnumMap<>(String.class);\n");
        sb.append("                break;\n");
        sb.append("            case \"ConcurrentSkipListMap\":\n");
        sb.append("                ").append(mapVarName).append(" = new ConcurrentSkipListMap<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"TreeNavigableMap\":\n");
        sb.append("                ").append(mapVarName).append(" = new TreeMap<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"ConcurrentNavigableMap\":\n");
        sb.append("                ").append(mapVarName).append(" = new ConcurrentSkipListMap<>();\n");
        sb.append("                break;\n");
        sb.append("            default:\n");
        sb.append("                ").append(mapVarName).append(" = new HashMap<>();\n");
        sb.append("                break;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成基础方法
     */
    private void generateBasicMethods(StringBuilder sb, String className, String tagVarName,
                                      String mapTypeVarName, String operationTypeVarName,
                                      String mapVarName, String randomVarName, String operationType) {
        String keyParamName = RandomUtils.generateWord(6);
        String valueParamName = RandomUtils.generateWord(6);
        String resultVarName = RandomUtils.generateWord(6);

        // 生成主要操作方法
        sb.append("    public void ").append(operationType).append("(String ")
                .append(keyParamName).append(", String ").append(valueParamName).append(") {\n");
        sb.append("        if (").append(keyParamName).append(" == null) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Key is null\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        switch (").append(operationTypeVarName).append(") {\n");
        sb.append("            case \"put\":\n");
        sb.append("                ").append(mapVarName).append(".put(").append(keyParamName)
                .append(", ").append(valueParamName).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"get\":\n");
        sb.append("                String ").append(resultVarName).append(" = ").append(mapVarName)
                .append(".get(").append(keyParamName).append(");\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Get: \" + ").append(keyParamName)
                .append(" + \" = \" + ").append(resultVarName).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"remove\":\n");
        sb.append("                ").append(mapVarName).append(".remove(").append(keyParamName).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"contains\":\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Contains: \" + ").append(keyParamName)
                .append(" + \" = \" + ").append(mapVarName).append(".containsKey(").append(keyParamName).append("));\n");
        sb.append("                break;\n");
        sb.append("            case \"clear\":\n");
        sb.append("                ").append(mapVarName).append(".clear();\n");
        sb.append("                break;\n");
        sb.append("            case \"size\":\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Size: \" + ").append(mapVarName).append(".size());\n");
        sb.append("                break;\n");
        sb.append("            case \"keys\":\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Keys: \" + ").append(mapVarName).append(".keySet());\n");
        sb.append("                break;\n");
        sb.append("            case \"values\":\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Values: \" + ").append(mapVarName).append(".values());\n");
        sb.append("                break;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成单参数操作方法
        sb.append("    public String ").append(operationType).append("(String ")
                .append(keyParamName).append(") {\n");
        sb.append("        if (").append(keyParamName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        switch (").append(operationTypeVarName).append(") {\n");
        sb.append("            case \"get\":\n");
        sb.append("                return ").append(mapVarName).append(".get(").append(keyParamName).append(");\n");
        sb.append("            case \"remove\":\n");
        sb.append("                return ").append(mapVarName).append(".remove(").append(keyParamName).append(");\n");
        sb.append("            case \"contains\":\n");
        sb.append("                return String.valueOf(").append(mapVarName)
                .append(".containsKey(").append(keyParamName).append("));\n");
        sb.append("            default:\n");
        sb.append("                return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成其他基础方法
        String sizeMethodName = RandomUtils.generateWord(6);
        String isEmptyMethodName = RandomUtils.generateWord(6);
        String keySetMethodName = RandomUtils.generateWord(6);
        String valuesMethodName = RandomUtils.generateWord(6);
        String randomKeyMethodName = RandomUtils.generateWord(6);
        String randomValueMethodName = RandomUtils.generateWord(6);
        String clearMethodName = RandomUtils.generateWord(6);

        sb.append("    public int ").append(sizeMethodName).append("() {\n");
        sb.append("        return ").append(mapVarName).append(".size();\n");
        sb.append("    }\n\n");

        sb.append("    public boolean ").append(isEmptyMethodName).append("() {\n");
        sb.append("        return ").append(mapVarName).append(".isEmpty();\n");
        sb.append("    }\n\n");

        sb.append("    public Set<String> ").append(keySetMethodName).append("() {\n");
        sb.append("        return ").append(mapVarName).append(".keySet();\n");
        sb.append("    }\n\n");

        sb.append("    public Collection<String> ").append(valuesMethodName).append("() {\n");
        sb.append("        return ").append(mapVarName).append(".values();\n");
        sb.append("    }\n\n");

        sb.append("    public String ").append(randomKeyMethodName).append("() {\n");
        sb.append("        if (").append(mapVarName).append(".isEmpty()) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        int ").append(resultVarName).append(" = ").append(randomVarName)
                .append(".nextInt(").append(mapVarName).append(".size());\n");
        sb.append("        return new ArrayList<>(").append(mapVarName).append(".keySet()).get(")
                .append(resultVarName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public String ").append(randomValueMethodName).append("() {\n");
        sb.append("        if (").append(mapVarName).append(".isEmpty()) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        int ").append(resultVarName).append(" = ").append(randomVarName)
                .append(".nextInt(").append(mapVarName).append(".size());\n");
        sb.append("        return new ArrayList<>(").append(mapVarName).append(".values()).get(")
                .append(resultVarName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(clearMethodName).append("() {\n");
        sb.append("        ").append(mapVarName).append(".clear();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成异步操作方法
     */
    private void generateAsyncOperationMethods(StringBuilder sb, String className,
                                               String mapVarName, String tagVarName,
                                               String asyncHandler) {
        String asyncPutMethodName = RandomUtils.generateWord(6);
        String asyncGetMethodName = RandomUtils.generateWord(6);
        String asyncRemoveMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);
        String valueParamName = RandomUtils.generateWord(6);

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.launch"));
            sb.append("    public void ").append(asyncPutMethodName).append("(String ")
                    .append(keyParamName).append(", String ").append(valueParamName).append(") {\n");
            sb.append("        CoroutineScope(Dispatchers.IO).launch {\n");
            sb.append("            ").append(mapVarName).append(".put(").append(keyParamName)
                    .append(", ").append(valueParamName).append(");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncGetMethodName).append("(String ")
                    .append(keyParamName).append(") {\n");
            sb.append("        CoroutineScope(Dispatchers.IO).launch {\n");
            sb.append("            String result = ").append(mapVarName).append(".get(")
                    .append(keyParamName).append(");\n");
            sb.append("            Log.d(").append(tagVarName).append(", \"Async Get: \" + ").append(keyParamName)
                    .append(" + \" = \" + result);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncRemoveMethodName).append("(String ")
                    .append(keyParamName).append(") {\n");
            sb.append("        CoroutineScope(Dispatchers.IO).launch {\n");
            sb.append("            ").append(mapVarName).append(".remove(").append(keyParamName).append(");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("    public void ").append(asyncPutMethodName).append("(String ")
                    .append(keyParamName).append(", String ").append(valueParamName).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            ").append(mapVarName).append(".put(").append(keyParamName)
                    .append(", ").append(valueParamName).append(");\n");
            sb.append("            return true;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .subscribe();\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncGetMethodName).append("(String ")
                    .append(keyParamName).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            String result = ").append(mapVarName).append(".get(")
                    .append(keyParamName).append(");\n");
            sb.append("            Log.d(").append(tagVarName).append(", \"Async Get: \" + ").append(keyParamName)
                    .append(" + \" = \" + result);\n");
            sb.append("            return result;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .subscribe();\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncRemoveMethodName).append("(String ")
                    .append(keyParamName).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            ").append(mapVarName).append(".remove(").append(keyParamName).append(");\n");
            sb.append("            return true;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .subscribe();\n");
            sb.append("    }\n\n");
        } else {
            sb.append("    private Handler handler = new Handler(Looper.getMainLooper());\n\n");

            sb.append("    public void ").append(asyncPutMethodName).append("(String ")
                    .append(keyParamName).append(", String ").append(valueParamName).append(") {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            ").append(mapVarName).append(".put(").append(keyParamName)
                    .append(", ").append(valueParamName).append(");\n");
            sb.append("        }).start();\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncGetMethodName).append("(String ")
                    .append(keyParamName).append(") {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            String result = ").append(mapVarName).append(".get(")
                    .append(keyParamName).append(");\n");
            sb.append("            handler.post(() -> {\n");
            sb.append("                Log.d(").append(tagVarName).append(", \"Async Get: \" + ").append(keyParamName)
                    .append(" + \" = \" + result);\n");
            sb.append("            });\n");
            sb.append("        }).start();\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncRemoveMethodName).append("(String ")
                    .append(keyParamName).append(") {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            ").append(mapVarName).append(".remove(").append(keyParamName).append(");\n");
            sb.append("        }).start();\n");
            sb.append("    }\n\n");
        }
    }

    /**
     * 生成批量操作方法
     */
    private void generateBatchOperationMethods(StringBuilder sb, String className,
                                               String mapVarName, String tagVarName) {
        String batchPutMethodName = RandomUtils.generateWord(6);
        String batchGetMethodName = RandomUtils.generateWord(6);
        String batchRemoveMethodName = RandomUtils.generateWord(6);
        String keysParamName = RandomUtils.generateWord(6);
        String valuesParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(batchPutMethodName).append("(List<String> ")
                .append(keysParamName).append(", List<String> ").append(valuesParamName).append(") {\n");
        sb.append("        if (").append(keysParamName).append(" == null || ").append(valuesParamName)
                .append(" == null || ").append(keysParamName).append(".size() != ").append(valuesParamName)
                .append(".size()) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Invalid batch put parameters\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        for (int i = 0; i < ").append(keysParamName).append(".size(); i++) {\n");
        sb.append("            ").append(mapVarName).append(".put(").append(keysParamName)
                .append(".get(i), ").append(valuesParamName).append(".get(i));\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public List<String> ").append(batchGetMethodName).append("(List<String> ")
                .append(keysParamName).append(") {\n");
        sb.append("        if (").append(keysParamName).append(" == null) {\n");
        sb.append("            return new ArrayList<>();\n");
        sb.append("        }\n");
        sb.append("        List<String> result = new ArrayList<>();\n");
        sb.append("        for (String key : ").append(keysParamName).append(") {\n");
        sb.append("            result.add(").append(mapVarName).append(".get(key));\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(batchRemoveMethodName).append("(List<String> ")
                .append(keysParamName).append(") {\n");
        sb.append("        if (").append(keysParamName).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        for (String key : ").append(keysParamName).append(") {\n");
        sb.append("            ").append(mapVarName).append(".remove(key);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成过滤方法
     */
    private void generateFilteringMethods(StringBuilder sb, String className,
                                          String mapVarName, String tagVarName) {
        String filterKeysMethodName = RandomUtils.generateWord(6);
        String filterValuesMethodName = RandomUtils.generateWord(6);
        String filterEntriesMethodName = RandomUtils.generateWord(6);
        String predicateParamName = RandomUtils.generateWord(6);

        sb.append("    public Set<String> ").append(filterKeysMethodName)
                .append("(Predicate<String> ").append(predicateParamName).append(") {\n");
        sb.append("        Set<String> result = new HashSet<>();\n");
        sb.append("        for (String key : ").append(mapVarName).append(".keySet()) {\n");
        sb.append("            if (").append(predicateParamName).append(".test(key)) {\n");
        sb.append("                result.add(key);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        sb.append("    public List<String> ").append(filterValuesMethodName)
                .append("(Predicate<String> ").append(predicateParamName).append(") {\n");
        sb.append("        List<String> result = new ArrayList<>();\n");
        sb.append("        for (String value : ").append(mapVarName).append(".values()) {\n");
        sb.append("            if (").append(predicateParamName).append(".test(value)) {\n");
        sb.append("                result.add(value);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        sb.append("    public Map<String, String> ").append(filterEntriesMethodName)
                .append("(Predicate<Map.Entry<String, String>> ").append(predicateParamName).append(") {\n");
        sb.append("        Map<String, String> result = new HashMap<>();\n");
        sb.append("        for (Map.Entry<String, String> entry : ").append(mapVarName).append(".entrySet()) {\n");
        sb.append("            if (").append(predicateParamName).append(".test(entry)) {\n");
        sb.append("                result.put(entry.getKey(), entry.getValue());\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成转换方法
     */
    private void generateTransformationMethods(StringBuilder sb, String className,
                                               String mapVarName, String tagVarName) {
        String transformKeysMethodName = RandomUtils.generateWord(6);
        String transformValuesMethodName = RandomUtils.generateWord(6);
        String transformEntriesMethodName = RandomUtils.generateWord(6);
        String functionParamName = RandomUtils.generateWord(6);

        sb.append("    public Map<String, String> ").append(transformKeysMethodName)
                .append("(Function<String, String> ").append(functionParamName).append(") {\n");
        sb.append("        Map<String, String> result = new HashMap<>();\n");
        sb.append("        for (Map.Entry<String, String> entry : ").append(mapVarName).append(".entrySet()) {\n");
        sb.append("            String newKey = ").append(functionParamName).append(".apply(entry.getKey());\n");
        sb.append("            result.put(newKey, entry.getValue());\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        sb.append("    public Map<String, String> ").append(transformValuesMethodName)
                .append("(Function<String, String> ").append(functionParamName).append(") {\n");
        sb.append("        Map<String, String> result = new HashMap<>();\n");
        sb.append("        for (Map.Entry<String, String> entry : ").append(mapVarName).append(".entrySet()) {\n");
        sb.append("            String newValue = ").append(functionParamName).append(".apply(entry.getValue());\n");
        sb.append("            result.put(entry.getKey(), newValue);\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        sb.append("    public Map<String, String> ").append(transformEntriesMethodName)
                .append("(Function<Map.Entry<String, String>, Map.Entry<String, String>> ")
                .append(functionParamName).append(") {\n");
        sb.append("        Map<String, String> result = new HashMap<>();\n");
        sb.append("        for (Map.Entry<String, String> entry : ").append(mapVarName).append(".entrySet()) {\n");
        sb.append("            Map.Entry<String, String> newEntry = ").append(functionParamName)
                .append(".apply(entry);\n");
        sb.append("            result.put(newEntry.getKey(), newEntry.getValue());\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成验证方法
     */
    private void generateValidationMethods(StringBuilder sb, String className,
                                           String mapVarName, String tagVarName) {
        String validateKeyMethodName = RandomUtils.generateWord(6);
        String validateValueMethodName = RandomUtils.generateWord(6);
        String validateEntryMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);
        String valueParamName = RandomUtils.generateWord(6);

        sb.append("    public boolean ").append(validateKeyMethodName).append("(String ")
                .append(keyParamName).append(") {\n");
        sb.append("        if (").append(keyParamName).append(" == null || ").append(keyParamName)
                .append(".isEmpty()) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Invalid key\");\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return true;\n");
        sb.append("    }\n\n");

        sb.append("    public boolean ").append(validateValueMethodName).append("(String ")
                .append(valueParamName).append(") {\n");
        sb.append("        if (").append(valueParamName).append(" == null) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Invalid value\");\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return true;\n");
        sb.append("    }\n\n");

        sb.append("    public boolean ").append(validateEntryMethodName).append("(String ")
                .append(keyParamName).append(", String ").append(valueParamName).append(") {\n");
        sb.append("        return ").append(validateKeyMethodName).append("(").append(keyParamName)
                .append(") && ").append(validateValueMethodName).append("(").append(valueParamName).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成缓存方法
     */
    private void generateCachingMethods(StringBuilder sb, String className,
                                        String mapVarName, String cacheVarName,
                                        String tagVarName) {
        String putWithCacheMethodName = RandomUtils.generateWord(6);
        String getWithCacheMethodName = RandomUtils.generateWord(6);
        String clearCacheMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);
        String valueParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(putWithCacheMethodName).append("(String ")
                .append(keyParamName).append(", String ").append(valueParamName).append(") {\n");
        sb.append("        ").append(mapVarName).append(".put(").append(keyParamName)
                .append(", ").append(valueParamName).append(");\n");
        sb.append("        ").append(cacheVarName).append(".put(").append(keyParamName)
                .append(", ").append(valueParamName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public String ").append(getWithCacheMethodName).append("(String ")
                .append(keyParamName).append(") {\n");
        sb.append("        String result = ").append(cacheVarName).append(".get(")
                .append(keyParamName).append(");\n");
        sb.append("        if (result == null) {\n");
        sb.append("            result = ").append(mapVarName).append(".get(").append(keyParamName).append(");\n");
        sb.append("            if (result != null) {\n");
        sb.append("                ").append(cacheVarName).append(".put(").append(keyParamName)
                .append(", result);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(clearCacheMethodName).append("() {\n");
        sb.append("        ").append(cacheVarName).append(".clear();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成统计方法
     */
    private void generateStatisticsMethods(StringBuilder sb, String className,
                                           String mapVarName, String accessCountVarName,
                                           String accessTimeVarName, String tagVarName) {
        String getAccessCountMethodName = RandomUtils.generateWord(6);
        String getAccessTimeMethodName = RandomUtils.generateWord(6);
        String resetStatisticsMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);

        sb.append("    public int ").append(getAccessCountMethodName).append("() {\n");
        sb.append("        return ").append(accessCountVarName).append(".get();\n");
        sb.append("    }\n\n");

        sb.append("    public long ").append(getAccessTimeMethodName).append("() {\n");
        sb.append("        return ").append(accessTimeVarName).append(".get();\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(resetStatisticsMethodName).append("() {\n");
        sb.append("        ").append(accessCountVarName).append(".set(0);\n");
        sb.append("        ").append(accessTimeVarName).append(".set(0);\n");
        sb.append("    }\n\n");

        // 修改基础方法以包含统计功能
        sb.append("    public String getWithStatistics(String ").append(keyParamName).append(") {\n");
        sb.append("        long startTime = System.currentTimeMillis();\n");
        sb.append("        String result = ").append(mapVarName).append(".get(").append(keyParamName).append(");\n");
        sb.append("        long endTime = System.currentTimeMillis();\n");
        sb.append("        ").append(accessCountVarName).append(".incrementAndGet();\n");
        sb.append("        ").append(accessTimeVarName).append(".addAndGet(endTime - startTime);\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成持久化方法
     */
    private void generatePersistenceMethods(StringBuilder sb, String className,
                                            String mapVarName, String contextVarName,
                                            String prefsVarName, String prefsKeyVarName,
                                            String tagVarName) {
        String saveToPrefsMethodName = RandomUtils.generateWord(6);
        String loadFromPrefsMethodName = RandomUtils.generateWord(6);
        String clearPrefsMethodName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(saveToPrefsMethodName).append("() {\n");
        sb.append("        SharedPreferences.Editor editor = ").append(prefsVarName).append(".edit();\n");
        sb.append("        for (Map.Entry<String, String> entry : ").append(mapVarName).append(".entrySet()) {\n");
        sb.append("            editor.putString(entry.getKey(), entry.getValue());\n");
        sb.append("        }\n");
        sb.append("        editor.apply();\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(loadFromPrefsMethodName).append("() {\n");
        sb.append("        Map<String, ?> all = ").append(prefsVarName).append(".getAll();\n");
        sb.append("        for (Map.Entry<String, ?> entry : all.entrySet()) {\n");
        sb.append("            if (entry.getValue() instanceof String) {\n");
        sb.append("                ").append(mapVarName).append(".put(entry.getKey(), (String) entry.getValue());\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(clearPrefsMethodName).append("() {\n");
        sb.append("        SharedPreferences.Editor editor = ").append(prefsVarName).append(".edit();\n");
        sb.append("        editor.clear();\n");
        sb.append("        editor.apply();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成示例使用方法
     */
    private void generateExampleUsageMethod(StringBuilder sb, String className,
                                            String mapVarName, String randomVarName,
                                            String cacheVarName, String accessCountVarName,
                                            String accessTimeVarName, String contextVarName,
                                            String prefsVarName, String prefsKeyVarName,
                                            String tagVarName, String mapTypeVarName,
                                            String operationTypeVarName, String operationType) {
        String exampleMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);
        String valueParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(exampleMethodName).append("() {\n");
        sb.append("        // 基础操作\n");
        sb.append("        String ").append(keyParamName).append(" = \"").append(RandomUtils.generateWord(6))
                .append("\";\n");
        sb.append("        String ").append(valueParamName).append(" = \"").append(RandomUtils.generateWord(6))
                .append("\";\n");
        sb.append("        ").append(operationType).append("(").append(keyParamName).append(", ")
                .append(valueParamName).append(");\n");

        if (useAsyncOperations) {
            sb.append("        // 异步操作\n");
            sb.append("        asyncPut(\"").append(RandomUtils.generateWord(6)).append("\", \"")
                    .append(RandomUtils.generateWord(6)).append("\");\n");
            sb.append("        asyncGet(\"").append(RandomUtils.generateWord(6)).append("\");\n");
        }

        if (useBatchOperations) {
            sb.append("        // 批量操作\n");
            sb.append("        List<String> keys = Arrays.asList(\"").append(RandomUtils.generateWord(6))
                    .append("\", \"").append(RandomUtils.generateWord(6)).append("\");\n");
            sb.append("        List<String> values = Arrays.asList(\"").append(RandomUtils.generateWord(6))
                    .append("\", \"").append(RandomUtils.generateWord(6)).append("\");\n");
            sb.append("        batchPut(keys, values);\n");
        }

        if (useFiltering) {
            sb.append("        // 过滤操作\n");
            sb.append("        Set<String> filteredKeys = filterKeys(key -> key.startsWith(\"test\"));\n");
        }

        if (useTransformation) {
            sb.append("        // 转换操作\n");
            sb.append("        Map<String, String> transformedValues = transformValues(String::toUpperCase);\n");
        }

        if (useValidation) {
            sb.append("        // 验证操作\n");
            sb.append("        boolean isValidKey = validateKey(\"").append(RandomUtils.generateWord(6)).append("\");\n");
        }

        if (useCaching) {
            sb.append("        // 缓存操作\n");
            sb.append("        putWithCache(\"").append(RandomUtils.generateWord(6)).append("\", \"")
                    .append(RandomUtils.generateWord(6)).append("\");\n");
            sb.append("        getWithCache(\"").append(RandomUtils.generateWord(6)).append("\");\n");
        }

        if (useStatistics) {
            sb.append("        // 统计操作\n");
            sb.append("        getWithStatistics(\"").append(RandomUtils.generateWord(6)).append("\");\n");
            sb.append("        int count = getAccessCount();\n");
            sb.append("        long time = getAccessTime();\n");
        }

        if (usePersistence) {
            sb.append("        // 持久化操作\n");
            sb.append("        saveToPrefs();\n");
            sb.append("        loadFromPrefs();\n");
        }

        sb.append("    }\n\n");
    }
}