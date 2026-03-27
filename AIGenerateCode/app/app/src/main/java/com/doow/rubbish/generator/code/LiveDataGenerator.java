package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LiveDataGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    public LiveDataGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成LiveData包装类");

        // 空值防护：给异步处理器设置默认值
        String asyncHandler = variationManager.getVariation("async_handler");
        asyncHandler = asyncHandler == null ? "coroutines" : asyncHandler;

        // 边界防护：确保生成数量在合理范围（5-12）
        int generateCount = RandomUtils.between(5, 12);
        for (int i = 0; i < generateCount; i++) {
            String className = RandomUtils.generateClassName("LiveData");
            generateLiveDataClass(className, asyncHandler);
        }
    }

    private void generateLiveDataClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("livedata"));

        // 导入核心依赖（补充缺失的MediatorLiveData/Transformations）
        sb.append(generateImportStatement("androidx.lifecycle.MutableLiveData"));
        sb.append(generateImportStatement("androidx.lifecycle.LiveData"));
        sb.append(generateImportStatement("androidx.lifecycle.Observer"));
        sb.append(generateImportStatement("androidx.lifecycle.LifecycleOwner"));
        sb.append(generateImportStatement("androidx.lifecycle.Transformations")); // 补充Transformations导入
        sb.append(generateImportStatement("androidx.lifecycle.MediatorLiveData")); // 补充MediatorLiveData导入
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("android.os.Handler"));
        sb.append(generateImportStatement("android.os.Looper"));

        // 异步处理依赖（空值防护）
        if (asyncHandler != null) {
            if (asyncHandler.contains("coroutines")) {
                sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
                sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
                sb.append(generateImportStatement("kotlinx.coroutines.launch"));
            } else if (asyncHandler.contains("rxjava")) {
                sb.append(generateImportStatement("io.reactivex.rxjava3.core.Observable"));
                sb.append(generateImportStatement("io.reactivex.rxjava3.subjects.PublishSubject"));
            }
        }

        sb.append("\n"); // 规范空行，替换多余的空白拼接

        // 类定义（修复缩进）
        sb.append("public class ").append(className).append(" {\n\n");

        // 常量定义（修复字符串转义）
        sb.append("    private static final String TAG = \"").append(className).append("\";\n\n");

        // 使用标志变量确保字段和配套方法一起生成和使用
        boolean useSingleData = RandomUtils.randomBoolean();
        boolean useMultipleData = RandomUtils.randomBoolean();
        boolean useTransformations = RandomUtils.randomBoolean();
        boolean useMediator = RandomUtils.randomBoolean();
        boolean useCache = RandomUtils.randomBoolean();
        boolean useThrottle = RandomUtils.randomBoolean();

        // 节流延迟常量（规范常量定义）
        if (useThrottle) {
            sb.append("    private static final long THROTTLE_DELAY_MS = 500L;\n\n");
        }

        // 生成字段
        if (useSingleData) {
            sb.append("    private final MutableLiveData<String> singleData = new MutableLiveData<>();\n");
            sb.append("    private final LiveData<String> publicSingleData;\n\n");
        }

        if (useMultipleData) {
            sb.append("    private final MutableLiveData<String> primaryData = new MutableLiveData<>();\n");
            sb.append("    private final MutableLiveData<String> secondaryData = new MutableLiveData<>();\n");
            sb.append("    private final LiveData<String> publicPrimaryData;\n");
            sb.append("    private final LiveData<String> publicSecondaryData;\n\n");
        }

        if (useCache) {
            sb.append("    private String cachedValue;\n");
            sb.append("    private final MutableLiveData<String> cachedData = new MutableLiveData<>();\n");
            sb.append("    private final LiveData<String> publicCachedData;\n\n");
        }

        if (useThrottle) {
            sb.append("    private final Handler throttleHandler = new Handler(Looper.getMainLooper());\n");
            sb.append("    private Runnable throttleRunnable;\n\n");
        }

        if (useTransformations) {
            sb.append("    private final MutableLiveData<String> sourceData = new MutableLiveData<>();\n");
            sb.append("    private final LiveData<String> transformedData;\n\n");
        }

        if (useMediator) {
            sb.append("    private final MutableLiveData<String> mediatorSource1 = new MutableLiveData<>();\n");
            sb.append("    private final MutableLiveData<String> mediatorSource2 = new MutableLiveData<>();\n");
            sb.append("    private final MediatorLiveData<String> mediatorData = new MediatorLiveData<>();\n");
            sb.append("    private final LiveData<String> publicMediatorData;\n\n");
        }

        // 构造方法
        sb.append("    public ").append(className).append("() {\n\n");

        if (useSingleData) {
            sb.append("        publicSingleData = singleData;\n\n");
        }

        if (useMultipleData) {
            sb.append("        publicPrimaryData = primaryData;\n");
            sb.append("        publicSecondaryData = secondaryData;\n\n");
        }

        if (useCache) {
            sb.append("        publicCachedData = cachedData;\n\n");
        }

        // Transformations映射（修复字符串转义）
        if (useTransformations) {
            sb.append("        transformedData = Transformations.map(sourceData, value -> {\n");
            sb.append("            return \"Transformed: \" + (value != null ? value : \"null\");\n");
            sb.append("        });\n\n");
        }

        // MediatorLiveData配置（修复字符串转义）
        if (useMediator) {
            sb.append("        publicMediatorData = mediatorData;\n");
            sb.append("        mediatorData.addSource(mediatorSource1, value -> {\n");
            sb.append("            mediatorData.setValue(\"Source1: \" + (value != null ? value : \"null\"));\n");
            sb.append("        });\n");
            sb.append("        mediatorData.addSource(mediatorSource2, value -> {\n");
            sb.append("            mediatorData.setValue(\"Source2: \" + (value != null ? value : \"null\"));\n");
            sb.append("        });\n\n");
        }

        sb.append("    }\n\n");

        // 生成getter方法
        if (useSingleData) {
            sb.append("    public LiveData<String> getSingleData() {\n");
            sb.append("        return publicSingleData;\n");
            sb.append("    }\n\n");
        }

        if (useMultipleData) {
            sb.append("    public LiveData<String> getPrimaryData() {\n");
            sb.append("        return publicPrimaryData;\n");
            sb.append("    }\n\n");

            sb.append("    public LiveData<String> getSecondaryData() {\n");
            sb.append("        return publicSecondaryData;\n");
            sb.append("    }\n\n");
        }

        if (useCache) {
            sb.append("    public LiveData<String> getCachedData() {\n");
            sb.append("        return publicCachedData;\n");
            sb.append("    }\n\n");
        }

        if (useTransformations) {
            sb.append("    public LiveData<String> getTransformedData() {\n");
            sb.append("        return transformedData;\n");
            sb.append("    }\n\n");
        }

        if (useMediator) {
            sb.append("    public LiveData<String> getMediatorData() {\n");
            sb.append("        return publicMediatorData;\n");
            sb.append("    }\n\n");
        }

        // 生成setter方法（修复Log转义和空值防护）
        if (useSingleData) {
            sb.append("    public void setSingleData(String value) {\n");
            sb.append("        Log.d(TAG, \"Setting single data: \" + value);\n");
            if (useThrottle) {
                sb.append("        throttleUpdate(() -> singleData.setValue(value));\n");
            } else {
                // 补充postValue，适配非主线程场景
                sb.append("        if (Looper.myLooper() == Looper.getMainLooper()) {\n");
                sb.append("            singleData.setValue(value);\n");
                sb.append("        } else {\n");
                sb.append("            singleData.postValue(value);\n");
                sb.append("        }\n");
            }
            sb.append("    }\n\n");
        }

        if (useMultipleData) {
            sb.append("    public void setPrimaryData(String value) {\n");
            sb.append("        Log.d(TAG, \"Setting primary data: \" + value);\n");
            if (useThrottle) {
                sb.append("        throttleUpdate(() -> primaryData.setValue(value));\n");
            } else {
                sb.append("        if (Looper.myLooper() == Looper.getMainLooper()) {\n");
                sb.append("            primaryData.setValue(value);\n");
                sb.append("        } else {\n");
                sb.append("            primaryData.postValue(value);\n");
                sb.append("        }\n");
            }
            sb.append("    }\n\n");

            sb.append("    public void setSecondaryData(String value) {\n");
            sb.append("        Log.d(TAG, \"Setting secondary data: \" + value);\n");
            if (useThrottle) {
                sb.append("        throttleUpdate(() -> secondaryData.setValue(value));\n");
            } else {
                sb.append("        if (Looper.myLooper() == Looper.getMainLooper()) {\n");
                sb.append("            secondaryData.setValue(value);\n");
                sb.append("        } else {\n");
                sb.append("            secondaryData.postValue(value);\n");
                sb.append("        }\n");
            }
            sb.append("    }\n\n");
        }

        if (useCache) {
            sb.append("    public void setCachedData(String value) {\n");
            sb.append("        Log.d(TAG, \"Setting cached data: \" + value);\n");
            sb.append("        cachedValue = value;\n");
            if (useThrottle) {
                sb.append("        throttleUpdate(() -> cachedData.setValue(value));\n");
            } else {
                sb.append("        if (Looper.myLooper() == Looper.getMainLooper()) {\n");
                sb.append("            cachedData.setValue(value);\n");
                sb.append("        } else {\n");
                sb.append("            cachedData.postValue(value);\n");
                sb.append("        }\n");
            }
            sb.append("    }\n\n");
        }

        if (useTransformations) {
            sb.append("    public void setSourceData(String value) {\n");
            sb.append("        Log.d(TAG, \"Setting source data: \" + value);\n");
            sb.append("        if (Looper.myLooper() == Looper.getMainLooper()) {\n");
            sb.append("            sourceData.setValue(value);\n");
            sb.append("        } else {\n");
            sb.append("            sourceData.postValue(value);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        if (useMediator) {
            sb.append("    public void setMediatorSource1(String value) {\n");
            sb.append("        Log.d(TAG, \"Setting mediator source1: \" + value);\n");
            sb.append("        if (Looper.myLooper() == Looper.getMainLooper()) {\n");
            sb.append("            mediatorSource1.setValue(value);\n");
            sb.append("        } else {\n");
            sb.append("            mediatorSource1.postValue(value);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public void setMediatorSource2(String value) {\n");
            sb.append("        Log.d(TAG, \"Setting mediator source2: \" + value);\n");
            sb.append("        if (Looper.myLooper() == Looper.getMainLooper()) {\n");
            sb.append("            mediatorSource2.setValue(value);\n");
            sb.append("        } else {\n");
            sb.append("            mediatorSource2.postValue(value);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成节流辅助方法（增加空值防护）
        if (useThrottle) {
            sb.append("    private void throttleUpdate(Runnable update) {\n");
            sb.append("        if (update == null) { // 空值防护\n");
            sb.append("            return;\n");
            sb.append("        }\n");
            sb.append("        if (throttleRunnable != null) {\n");
            sb.append("            throttleHandler.removeCallbacks(throttleRunnable);\n");
            sb.append("        }\n");
            sb.append("        throttleRunnable = () -> {\n");
            sb.append("            update.run();\n");
            sb.append("            throttleRunnable = null;\n");
            sb.append("        };\n");
            sb.append("        throttleHandler.postDelayed(throttleRunnable, THROTTLE_DELAY_MS);\n");
            sb.append("    }\n\n");
        }

        // 缓存相关方法
        if (useCache) {
            sb.append("    public String getCachedValue() {\n");
            sb.append("        return cachedValue;\n");
            sb.append("    }\n\n");

            sb.append("    public boolean hasCachedValue() {\n");
            sb.append("        return cachedValue != null;\n");
            sb.append("    }\n\n");
        }

        // 生成观察者管理方法
        if (useSingleData) {
            sb.append("    public void observeSingleData(LifecycleOwner owner, Observer<String> observer) {\n");
            sb.append("        if (owner == null || observer == null) { // 空值防护\n");
            sb.append("            return;\n");
            sb.append("        }\n");
            sb.append("        publicSingleData.observe(owner, observer);\n");
            sb.append("    }\n\n");
        }

        if (useMultipleData) {
            sb.append("    public void observePrimaryData(LifecycleOwner owner, Observer<String> observer) {\n");
            sb.append("        if (owner == null || observer == null) {\n");
            sb.append("            return;\n");
            sb.append("        }\n");
            sb.append("        publicPrimaryData.observe(owner, observer);\n");
            sb.append("    }\n\n");

            sb.append("    public void observeSecondaryData(LifecycleOwner owner, Observer<String> observer) {\n");
            sb.append("        if (owner == null || observer == null) {\n");
            sb.append("            return;\n");
            sb.append("        }\n");
            sb.append("        publicSecondaryData.observe(owner, observer);\n");
            sb.append("    }\n\n");
        }

        if (useCache) {
            sb.append("    public void observeCachedData(LifecycleOwner owner, Observer<String> observer) {\n");
            sb.append("        if (owner == null || observer == null) {\n");
            sb.append("            return;\n");
            sb.append("        }\n");
            sb.append("        publicCachedData.observe(owner, observer);\n");
            sb.append("    }\n\n");
        }

        if (useTransformations) {
            sb.append("    public void observeTransformedData(LifecycleOwner owner, Observer<String> observer) {\n");
            sb.append("        if (owner == null || observer == null) {\n");
            sb.append("            return;\n");
            sb.append("        }\n");
            sb.append("        transformedData.observe(owner, observer);\n");
            sb.append("    }\n\n");
        }

        if (useMediator) {
            sb.append("    public void observeMediatorData(LifecycleOwner owner, Observer<String> observer) {\n");
            sb.append("        if (owner == null || observer == null) {\n");
            sb.append("            return;\n");
            sb.append("        }\n");
            sb.append("        publicMediatorData.observe(owner, observer);\n");
            sb.append("    }\n\n");
        }

        // 闭合类
        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "livedata");
    }
}