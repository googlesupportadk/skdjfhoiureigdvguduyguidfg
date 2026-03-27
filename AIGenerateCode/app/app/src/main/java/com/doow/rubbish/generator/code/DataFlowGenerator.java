package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class DataFlowGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] FLOW_TYPES = {
            "StateFlow", "SharedFlow", "Flow", "LiveData", "Observable",
            "Single", "Maybe", "Completable", "BehaviorSubject", "PublishSubject",
            "ReplaySubject", "AsyncSubject", "UnicastSubject", "BehaviorRelay", "PublishRelay"
    };

    private static final String[] DATA_TYPES = {
            "String", "Integer", "Boolean", "Long", "Double", "Float"
    };

    private static final String[] OPERATION_TYPES = {
            "map", "filter", "transform", "convert", "process",
            "validate", "format", "parse", "encode", "decode"
    };

    private static final String[] TRANSFORM_TYPES = {
            "toList", "toSet", "toMap", "distinct", "take",
            "skip", "debounce", "throttle", "buffer", "window"
    };

    public DataFlowGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成数据流类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Flow");
            generateFlowClass(className, asyncHandler);
        }
    }

    private void generateFlowClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("flow"));

        // 基础导入
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.concurrent.ConcurrentHashMap"));

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.flow.Flow"));
            sb.append(generateImportStatement("kotlinx.coroutines.flow.MutableStateFlow"));
            sb.append(generateImportStatement("kotlinx.coroutines.flow.StateFlow"));
            sb.append(generateImportStatement("kotlinx.coroutines.flow.MutableSharedFlow"));
            sb.append(generateImportStatement("kotlinx.coroutines.flow.SharedFlow"));
            sb.append(generateImportStatement("kotlinx.coroutines.flow.map"));
            sb.append(generateImportStatement("kotlinx.coroutines.flow.filter"));
        } else if (asyncHandler.contains("rxjava")) {
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Observable"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.subjects.BehaviorSubject"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.subjects.PublishSubject"));
        } else {
            sb.append(generateImportStatement("androidx.lifecycle.LiveData"));
            sb.append(generateImportStatement("androidx.lifecycle.MutableLiveData"));
            sb.append(generateImportStatement("androidx.lifecycle.Transformations"));
        }

        sb.append("\n");

        sb.append("public class ").append(className).append(" {\n\n");

        // 随机选择数据类型
        String dataType = DATA_TYPES[RandomUtils.between(0, DATA_TYPES.length - 1)];

        // 基础常量
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String DATA_TYPE = \"").append(dataType).append("\";\n\n");

        // 功能标志 - 确保所有字段和方法都会被使用
        boolean useCache = RandomUtils.randomBoolean();
        boolean useHistory = RandomUtils.randomBoolean();
        boolean useValidation = RandomUtils.randomBoolean();
        boolean useTransformation = RandomUtils.randomBoolean();
        boolean useBatch = RandomUtils.randomBoolean();
        boolean useErrorHandling = RandomUtils.randomBoolean();
        boolean useThreading = RandomUtils.randomBoolean();

        // 根据功能标志生成字段
        if (asyncHandler.contains("coroutines")) {
            sb.append("    private final MutableStateFlow<").append(dataType).append("> _flow = new MutableStateFlow<>(getDefault());\n");
            sb.append("    public final StateFlow<").append(dataType).append("> flow = _flow;\n");

            if (useBatch) {
                sb.append("    private final MutableSharedFlow<List<").append(dataType).append(">> _batchFlow = MutableSharedFlow.create();\n");
                sb.append("    public final SharedFlow<List<").append(dataType).append(">> batchFlow = _batchFlow;\n");
            }
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("    private final BehaviorSubject<").append(dataType).append("> _flow = BehaviorSubject.createDefault(getDefault());\n");
            sb.append("    public final Observable<").append(dataType).append("> flow = _flow.hide();\n");

            if (useBatch) {
                sb.append("    private final PublishSubject<List<").append(dataType).append(">> _batchFlow = PublishSubject.create();\n");
                sb.append("    public final Observable<List<").append(dataType).append(">> batchFlow = _batchFlow;\n");
            }
        } else {
            sb.append("    private final MutableLiveData<").append(dataType).append("> _flow = new MutableLiveData<>(getDefault());\n");
            sb.append("    public final LiveData<").append(dataType).append("> flow = _flow;\n");

            if (useBatch) {
                sb.append("    private final MutableLiveData<List<").append(dataType).append(">> _batchFlow = new MutableLiveData<>();\n");
                sb.append("    public final LiveData<List<").append(dataType).append(">> batchFlow = _batchFlow;\n");
            }
        }

        if (useCache) {
            sb.append("    private final ConcurrentHashMap<String, ").append(dataType).append("> cache = new ConcurrentHashMap<>();\n");
            sb.append("    private static final int MAX_CACHE_SIZE = 100;\n");
        }

        if (useHistory) {
            sb.append("    private final List<").append(dataType).append("> history = new ArrayList<>();\n");
            sb.append("    private static final int MAX_HISTORY_SIZE = 50;\n");
        }

        if (useValidation) {
            sb.append("    private final java.util.function.Predicate<").append(dataType).append("> validator = value -> {\n");
            sb.append("        boolean isValid = value != null;\n");
            if (dataType.equals("String")) {
                sb.append("        if (isValid) isValid = !value.trim().isEmpty();\n");
            }
            sb.append("        return isValid;\n");
            sb.append("    };\n\n");
        }

        if (useErrorHandling) {
            sb.append("    private final java.util.function.Consumer<Throwable> errorHandler = error -> {\n");
            sb.append("        Log.e(TAG, \"Error in data flow\", error);\n");
            sb.append("    };\n\n");
        }

        sb.append("    public ").append(className).append("() {\n");
        sb.append("        initialize();\n");
        sb.append("    }\n\n");

        // 初始化方法 - 使用所有生成的字段
        sb.append("    private void initialize() {\n");
        sb.append("        Log.d(TAG, \"Initializing ").append(className).append(" with data type: \").append(DATA_TYPE);\n");

        if (useCache) {
            sb.append("        cache.clear();\n");
        }

        if (useHistory) {
            sb.append("        history.clear();\n");
        }

        sb.append("        ").append(dataType).append(" initialValue = getDefault();\n");
        sb.append("        setValue(initialValue);\n");

        if (useValidation) {
            sb.append("        if (!validator.test(initialValue)) {\n");
            sb.append("            Log.w(TAG, \"Initial value validation failed\");\n");
            sb.append("        }\n");
        }

        sb.append("    }\n\n");

        // 获取默认值方法
        sb.append("    private ").append(dataType).append(" getDefault() {\n");
        if (dataType.equals("String")) {
            sb.append("        return \"\";\n");
        } else if (dataType.equals("Boolean")) {
            sb.append("        return false;\n");
        } else if (dataType.equals("Integer") || dataType.equals("Long")) {
            sb.append("        return 0;\n");
        } else if (dataType.equals("Float") || dataType.equals("Double")) {
            sb.append("        return 0.0;\n");
        } else {
            sb.append("        return null;\n");
        }
        sb.append("    }\n\n");

        // 设置值方法
        sb.append("    public void setValue(").append(dataType).append(" value) {\n");
        if (useValidation) {
            sb.append("        if (!validator.test(value)) {\n");
            sb.append("            Log.w(TAG, \"Value validation failed\");\n");
            sb.append("            return;\n");
            sb.append("        }\n");
        }

        if (asyncHandler.contains("coroutines")) {
            sb.append("        _flow.setValue(value);\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("        _flow.onNext(value);\n");
        } else {
            sb.append("        _flow.setValue(value);\n");
        }

        if (useHistory) {
            sb.append("        addToHistory(value);\n");
        }

        if (useCache) {
            sb.append("        updateCache(value);\n");
        }

        sb.append("    }\n\n");

        // 获取值方法
        sb.append("    public ").append(dataType).append(" getValue() {\n");
        if (asyncHandler.contains("coroutines")) {
            sb.append("        return _flow.getValue();\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("        return _flow.getValue();\n");
        } else {
            sb.append("        return _flow.getValue();\n");
        }
        sb.append("    }\n\n");

        // 缓存相关方法
        if (useCache) {
            sb.append("    private void updateCache(").append(dataType).append(" value) {\n");
            sb.append("        String key = System.currentTimeMillis() + \"_\" + value.hashCode();\n");
            sb.append("        if (cache.size() >= MAX_CACHE_SIZE) {\n");
            sb.append("            cache.clear();\n");
            sb.append("        }\n");
            sb.append("        cache.put(key, value);\n");
            sb.append("    }\n\n");

            sb.append("    public ").append(dataType).append(" getFromCache(String key) {\n");
            sb.append("        return cache.get(key);\n");
            sb.append("    }\n\n");

            sb.append("    public void clearCache() {\n");
            sb.append("        cache.clear();\n");
            sb.append("    }\n\n");
        }

        // 历史记录相关方法
        if (useHistory) {
            sb.append("    private void addToHistory(").append(dataType).append(" value) {\n");
            sb.append("        history.add(value);\n");
            sb.append("        if (history.size() > MAX_HISTORY_SIZE) {\n");
            sb.append("            history.remove(0);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public List<").append(dataType).append("> getHistory() {\n");
            sb.append("        return new ArrayList<>(history);\n");
            sb.append("    }\n\n");

            sb.append("    public void clearHistory() {\n");
            sb.append("        history.clear();\n");
            sb.append("    }\n\n");
        }

        // 验证相关方法
        if (useValidation) {
            sb.append("    public boolean isValid(").append(dataType).append(" value) {\n");
            sb.append("        return validator.test(value);\n");
            sb.append("    }\n\n");
        }

        // 批量操作方法
        if (useBatch) {
            sb.append("    public void emitBatch(List<").append(dataType).append("> values) {\n");
            if (useValidation) {
                sb.append("        List<").append(dataType).append("> validValues = new ArrayList<>();\n");
                sb.append("        for (").append(dataType).append(" value : values) {\n");
                sb.append("            if (validator.test(value)) {\n");
                sb.append("                validValues.add(value);\n");
                sb.append("            }\n");
                sb.append("        }\n");
                sb.append("        if (!validValues.isEmpty()) {\n");
                if (asyncHandler.contains("coroutines")) {
                    sb.append("            _batchFlow.tryEmit(validValues);\n");
                } else if (asyncHandler.contains("rxjava")) {
                    sb.append("            _batchFlow.onNext(validValues);\n");
                } else {
                    sb.append("            _batchFlow.setValue(validValues);\n");
                }
                sb.append("        }\n");
            } else {
                if (asyncHandler.contains("coroutines")) {
                    sb.append("        _batchFlow.tryEmit(values);\n");
                } else if (asyncHandler.contains("rxjava")) {
                    sb.append("        _batchFlow.onNext(values);\n");
                } else {
                    sb.append("        _batchFlow.setValue(values);\n");
                }
            }
            sb.append("    }\n\n");
        }

        // 转换方法
        if (useTransformation) {
            String operationType = OPERATION_TYPES[RandomUtils.between(0, OPERATION_TYPES.length - 1)];
            sb.append("    public ").append(dataType).append(" ").append(operationType).append("(").append(dataType).append(" value) {\n");
            sb.append("        ").append(dataType).append(" result = value;\n");

            if (dataType.equals("String")) {
                sb.append("        if (result != null) {\n");
                sb.append("            switch (\"").append(operationType).append("\") {\n");
                sb.append("                case \"map\":\n");
                sb.append("                    result = result.toUpperCase();\n");
                sb.append("                    break;\n");
                sb.append("                case \"filter\":\n");
                sb.append("                    result = result.isEmpty() ? null : result;\n");
                sb.append("                    break;\n");
                sb.append("                case \"format\":\n");
                sb.append("                    result = \"[\" + result + \"]\";\n");
                sb.append("                    break;\n");
                sb.append("                default:\n");
                sb.append("                    break;\n");
                sb.append("            }\n");
                sb.append("        }\n");
            } else if (dataType.equals("Integer") || dataType.equals("Long")) {
                sb.append("        result = result + 1;\n");
            } else if (dataType.equals("Boolean")) {
                sb.append("        result = !result;\n");
            } else if (dataType.equals("Float") || dataType.equals("Double")) {
                sb.append("        result = result * 1.1;\n");
            }

            sb.append("        return result;\n");
            sb.append("    }\n\n");
        }

        // 错误处理方法
        if (useErrorHandling) {
            sb.append("    private void handleError(Throwable error) {\n");
            sb.append("        errorHandler.accept(error);\n");
            sb.append("    }\n\n");
        }

        // 清理方法
        sb.append("    public void clear() {\n");
        if (useCache) {
            sb.append("        clearCache();\n");
        }
        if (useHistory) {
            sb.append("        clearHistory();\n");
        }
        sb.append("        setValue(getDefault());\n");
        sb.append("    }\n\n");

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "flow");
    }
}
