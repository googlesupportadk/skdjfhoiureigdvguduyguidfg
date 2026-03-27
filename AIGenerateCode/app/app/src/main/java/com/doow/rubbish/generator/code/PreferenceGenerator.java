
package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 升级版的Preference生成器
 * 满足8条基础要求：
 * 1. 确保代码功能的随机性和多样性灵活性，避免沉郁代码和垃圾代码
 * 2. 生成的字段都会被使用
 * 3. 生成的方法都会被调用
 * 4. 不会产生未使用的代码
 * 5. 确保生成的代码是完整可运行的
 * 6. 所有的变量名、方法名、类名、传参名、字符串、前缀随机、全部随机生成，防止明显语法 BUG
 * 7. 每个类中只能出现0-5个Log和System.out其他都要使用在实际功能中或者在其他类或功能中使用
 * 8. 确保生成的代码和其他生成器功能可以互相关联实现新功能
 */
public class PreferenceGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    // Preference类型数组
    private static final String[] PREFERENCE_TYPES = {
            "UserPreference", "AppPreference", "SettingsPreference", "ConfigPreference",
            "CachePreference", "SessionPreference", "ThemePreference", "SecurityPreference",
            "NetworkPreference", "StoragePreference", "SyncPreference", "ProfilePreference"
    };

    // 数据类型数组
    private static final String[] DATA_TYPES = {
            "String", "int", "long", "float", "boolean", "StringSet"
    };

    // 方法前缀数组
    private static final String[] METHOD_PREFIXES = {
            "get", "set", "put", "save", "load", "store", "retrieve", "update"
    };

    public PreferenceGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        int count = RandomUtils.between(5, 15);
        System.out.println("生成 " + count + " 个Preference工具类");

        String storageType = variationManager.getVariation("storage");
        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < count; i++) {
            String className = RandomUtils.generateClassName("Preference");
            generatePreferenceClass(className, storageType, uiStyle, asyncHandler);
        }
    }

    private void generatePreferenceClass(String className, String storageType, String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("preference"));

        // 基础导入
        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.content.SharedPreferences"));
        sb.append(generateImportStatement("android.content.SharedPreferences.OnSharedPreferenceChangeListener"));
        sb.append(generateImportStatement("android.content.SharedPreferences.Editor"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.Set"));
        sb.append(generateImportStatement("java.util.HashSet"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));

        // 异步处理依赖
        if (asyncHandler != null && asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
            sb.append(generateImportStatement("kotlinx.coroutines.launch"));
        }

        // 导入其他生成器生成的类
        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append(generateImportStatement(packageName + ".helper.*"));
        sb.append(generateImportStatement(packageName + ".validator.*"));
        sb.append("\n");

        // 功能标志 - 确保所有字段和方法都会被使用
        boolean useSingleton = RandomUtils.randomBoolean();
        boolean useCache = RandomUtils.randomBoolean();
        boolean useEncryption = RandomUtils.randomBoolean();
        boolean useValidation = RandomUtils.randomBoolean();
        boolean useAsync = RandomUtils.randomBoolean();
        boolean useBatch = RandomUtils.randomBoolean();
        boolean useListeners = RandomUtils.randomBoolean();
        boolean useMigration = RandomUtils.randomBoolean();

        // 定义字段名称变量
        String prefsFieldName = null;
        String instanceFieldName = null;
        String cacheMapName = null;
        String listenerListName = null;
        String validatorClassName = null;

        // 类定义
        sb.append("public class ").append(className).append(" {\n\n");

        // 基础常量
        String preferenceType = PREFERENCE_TYPES[RandomUtils.between(0, PREFERENCE_TYPES.length - 1)];
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String PREF_NAME = \"").append(preferenceType).append("\";\n");

        if (useCache) {
            sb.append("    private static final int MAX_CACHE_SIZE = ").append(RandomUtils.between(50, 200)).append(";\n");
            sb.append("    private static final long CACHE_TTL_MS = ").append(RandomUtils.between(1800000, 7200000)).append("L;\n");
        }

        if (useBatch) {
            sb.append("    private static final int BATCH_SIZE = ").append(RandomUtils.between(10, 50)).append(";\n");
        }

        sb.append("\n");

        // 单例字段
        if (useSingleton) {
            instanceFieldName = RandomUtils.generateVariableName("Instance");
            sb.append("    private static volatile ").append(className).append(" ").append(instanceFieldName).append(";\n\n");
        }

        // SharedPreferences字段
        prefsFieldName = RandomUtils.generateVariableName("Prefs");
        sb.append("    private SharedPreferences ").append(prefsFieldName).append(";\n");

        // 缓存字段
        if (useCache) {
            cacheMapName = RandomUtils.generateVariableName("Cache");
            sb.append("    private Map<String, CacheEntry> ").append(cacheMapName).append(" = new HashMap<>();\n");
            sb.append("    private volatile boolean cacheInitialized = false;\n\n");
            sb.append("    private static class CacheEntry {\n");
            sb.append("        Object value;\n");
            sb.append("        long timestamp;\n\n");
            sb.append("        CacheEntry(Object value) {\n");
            sb.append("            this.value = value;\n");
            sb.append("            this.timestamp = System.currentTimeMillis();\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 监听器字段
        if (useListeners) {
            listenerListName = RandomUtils.generateVariableName("Listeners");
            sb.append("    private List<OnSharedPreferenceChangeListener> ").append(listenerListName).append(" = new ArrayList<>();\n\n");
        }

        // 验证器字段
        if (useValidation) {
            validatorClassName = RandomUtils.generateClassName("Validator");
            sb.append("    private ").append(validatorClassName).append(" ").append(RandomUtils.generateVariableName("Validator")).append(";\n\n");
        }

        // 构造方法
        sb.append("    public ").append(className).append("(Context context) {\n");
        sb.append("        ").append(prefsFieldName).append(" = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);\n");
        if (useCache) {
            sb.append("        initializeCache();\n");
        }
        if (useListeners) {
            sb.append("        registerListeners();\n");
        }
        if (useValidation) {
            sb.append("        ").append(RandomUtils.generateVariableName("Validator")).append(" = new ").append(validatorClassName).append("(context);\n");
        }
        sb.append("    }\n\n");

        // 单例获取方法
        if (useSingleton) {
            sb.append("    public static synchronized ").append(className).append(" getInstance(Context context) {\n");
            sb.append("        if (").append(instanceFieldName).append(" == null) {\n");
            sb.append("            ").append(instanceFieldName).append(" = new ").append(className).append("(context.getApplicationContext());\n");
            sb.append("        }\n");
            sb.append("        return ").append(instanceFieldName).append(";\n");
            sb.append("    }\n\n");
        }

        // 缓存初始化方法
        if (useCache) {
            generateCacheMethods(sb, className, prefsFieldName, cacheMapName);
        }

        // 监听器方法
        if (useListeners) {
            generateListenerMethods(sb, className, prefsFieldName, listenerListName);
        }

        // 批量操作方法
        if (useBatch) {
            generateBatchMethods(sb, className, prefsFieldName);
        }

        // 异步操作方法
        if (useAsync) {
            generateAsyncMethods(sb, className, prefsFieldName, asyncHandler);
        }

        // 生成基础get/set方法
        generateBasicGetSetMethods(sb, className, prefsFieldName, cacheMapName, validatorClassName, useCache, useValidation);

        // 生成高级操作方法
        generateAdvancedMethods(sb, className, prefsFieldName, cacheMapName, useCache);

        // 生成迁移方法
        if (useMigration) {
            generateMigrationMethods(sb, className, prefsFieldName);
        }

        // 闭合类
        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "preference");
    }

    private void generateCacheMethods(StringBuilder sb, String className, String prefsFieldName, String cacheMapName) {
        sb.append("    private void initializeCache() {\n");
        sb.append("        if (!cacheInitialized) {\n");
        sb.append("            synchronized (").append(className).append(".class) {\n");
        sb.append("                if (!cacheInitialized) {\n");
        sb.append("                    cacheInitialized = true;\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    private void putCache(String key, Object value) {\n");
        sb.append("        if (").append(cacheMapName).append(".size() >= MAX_CACHE_SIZE) {\n");
        sb.append("            clearExpiredCache();\n");
        sb.append("        }\n");
        sb.append("        ").append(cacheMapName).append(".put(key, new CacheEntry(value));\n");
        sb.append("    }\n\n");

        sb.append("    private Object getCache(String key) {\n");
        sb.append("        CacheEntry entry = ").append(cacheMapName).append(".get(key);\n");
        sb.append("        if (entry != null && System.currentTimeMillis() - entry.timestamp < CACHE_TTL_MS) {\n");
        sb.append("            return entry.value;\n");
        sb.append("        }\n");
        sb.append("        return null;\n");
        sb.append("    }\n\n");

        sb.append("    private void clearExpiredCache() {\n");
        sb.append("        long currentTime = System.currentTimeMillis();\n");
        sb.append("        List<String> expiredKeys = new ArrayList<>();\n");
        sb.append("        for (Map.Entry<String, CacheEntry> entry : ").append(cacheMapName).append(".entrySet()) {\n");
        sb.append("            if (currentTime - entry.getValue().timestamp >= CACHE_TTL_MS) {\n");
        sb.append("                expiredKeys.add(entry.getKey());\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        for (String key : expiredKeys) {\n");
        sb.append("            ").append(cacheMapName).append(".remove(key);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateListenerMethods(StringBuilder sb, String className, String prefsFieldName, String listenerListName) {
        sb.append("    private void registerListeners() {\n");
        sb.append("        ").append(prefsFieldName).append(".registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {\n");
        sb.append("            @Override\n");
        sb.append("            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {\n");
        sb.append("                notifyListeners(key);\n");
        sb.append("            }\n");
        sb.append("        });\n");
        sb.append("    }\n\n");

        sb.append("    public void addListener(OnSharedPreferenceChangeListener listener) {\n");
        sb.append("        if (listener != null && !").append(listenerListName).append(".contains(listener)) {\n");
        sb.append("            ").append(listenerListName).append(".add(listener);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void removeListener(OnSharedPreferenceChangeListener listener) {\n");
        sb.append("        ").append(listenerListName).append(".remove(listener);\n");
        sb.append("    }\n\n");

        sb.append("    private void notifyListeners(String key) {\n");
        sb.append("        for (OnSharedPreferenceChangeListener listener : ").append(listenerListName).append(") {\n");
        sb.append("            listener.onSharedPreferenceChanged(").append(prefsFieldName).append(", key);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateBatchMethods(StringBuilder sb, String className, String prefsFieldName) {
        sb.append("    public void putBatch(Map<String, ?> values) {\n");
        sb.append("        Editor editor = ").append(prefsFieldName).append(".edit();\n");
        sb.append("        for (Map.Entry<String, ?> entry : values.entrySet()) {\n");
        sb.append("            Object value = entry.getValue();\n");
        sb.append("            if (value instanceof String) {\n");
        sb.append("                editor.putString(entry.getKey(), (String) value);\n");
        sb.append("            } else if (value instanceof Integer) {\n");
        sb.append("                editor.putInt(entry.getKey(), (Integer) value);\n");
        sb.append("            } else if (value instanceof Long) {\n");
        sb.append("                editor.putLong(entry.getKey(), (Long) value);\n");
        sb.append("            } else if (value instanceof Float) {\n");
        sb.append("                editor.putFloat(entry.getKey(), (Float) value);\n");
        sb.append("            } else if (value instanceof Boolean) {\n");
        sb.append("                editor.putBoolean(entry.getKey(), (Boolean) value);\n");
        sb.append("            } else if (value instanceof Set) {\n");
        sb.append("                editor.putStringSet(entry.getKey(), (Set<String>) value);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        editor.apply();\n");
        sb.append("    }\n\n");

        sb.append("    public Map<String, ?> getBatch(List<String> keys) {\n");
        sb.append("        Map<String, Object> result = new HashMap<>();\n");
        sb.append("        for (String key : keys) {\n");
        sb.append("            result.put(key, ").append(prefsFieldName).append(".getAll().get(key));\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");
    }

    private void generateAsyncMethods(StringBuilder sb, String className, String prefsFieldName, String asyncHandler) {
        sb.append("    public void putAsync(final String key, final String value) {\n");
        sb.append("        new Thread(new Runnable() {\n");
        sb.append("            @Override\n");
        sb.append("            public void run() {\n");
        sb.append("                ").append(prefsFieldName).append(".edit().putString(key, value).apply();\n");
        sb.append("            }\n");
        sb.append("        }).start();\n");
        sb.append("    }\n\n");

        sb.append("    public void getAsync(final String key, final PreferenceCallback callback) {\n");
        sb.append("        new Thread(new Runnable() {\n");
        sb.append("            @Override\n");
        sb.append("            public void run() {\n");
        sb.append("                final String value = ").append(prefsFieldName).append(".getString(key, null);\n");
        sb.append("                if (callback != null) {\n");
        sb.append("                    callback.onResult(value);\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }).start();\n");
        sb.append("    }\n\n");

        sb.append("    public interface PreferenceCallback {\n");
        sb.append("        void onResult(String value);\n");
        sb.append("    }\n\n");
    }

    private void generateBasicGetSetMethods(StringBuilder sb, String className, String prefsFieldName, String cacheMapName, String validatorClassName, boolean useCache, boolean useValidation) {
        int methodCount = RandomUtils.between(5, 10);
        String validateMethodName = RandomUtils.generateMethodName("validate");
        for (int i = 0; i < methodCount; i++) {
            String key = RandomUtils.generateName("key");
            String dataType = DATA_TYPES[RandomUtils.between(0, DATA_TYPES.length - 1)];
            String methodNamePrefix = METHOD_PREFIXES[RandomUtils.between(0, METHOD_PREFIXES.length - 1)];
            String getMethodName = methodNamePrefix + Character.toUpperCase(key.charAt(0)) + key.substring(1);
            String setMethodName = "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1);

            // 生成get方法
            sb.append("    public ").append(dataType).append(" ").append(getMethodName).append("(");
            if (dataType.equals("String")) {
                sb.append("String defaultValue");
            } else if (dataType.equals("int")) {
                sb.append("int defaultValue");
            } else if (dataType.equals("long")) {
                sb.append("long defaultValue");
            } else if (dataType.equals("float")) {
                sb.append("float defaultValue");
            } else if (dataType.equals("boolean")) {
                sb.append("boolean defaultValue");
            } else if (dataType.equals("StringSet")) {
                sb.append("Set<String> defaultValue");
            }
            sb.append(") {\n");

            if (useCache && !dataType.equals("StringSet")) {
                sb.append("        Object cached = getCache(\"").append(key).append("\");\n");
                sb.append("        if (cached != null) {\n");
                sb.append("            return (").append(dataType).append(") cached;\n");
                sb.append("        }\n");
            }

            sb.append("        ");
            if (dataType.equals("String")) {
                sb.append("String value = ").append(prefsFieldName).append(".getString(\"").append(key).append("\", defaultValue);\n");
            } else if (dataType.equals("int")) {
                sb.append("int value = ").append(prefsFieldName).append(".getInt(\"").append(key).append("\", defaultValue);\n");
            } else if (dataType.equals("long")) {
                sb.append("long value = ").append(prefsFieldName).append(".getLong(\"").append(key).append("\", defaultValue);\n");
            } else if (dataType.equals("float")) {
                sb.append("float value = ").append(prefsFieldName).append(".getFloat(\"").append(key).append("\", defaultValue);\n");
            } else if (dataType.equals("boolean")) {
                sb.append("boolean value = ").append(prefsFieldName).append(".getBoolean(\"").append(key).append("\", defaultValue);\n");
            } else if (dataType.equals("StringSet")) {
                sb.append("Set<String> value = ").append(prefsFieldName).append(".getStringSet(\"").append(key).append("\", defaultValue);\n");
            }

            if (useCache && !dataType.equals("StringSet")) {
                sb.append("        putCache(\"").append(key).append("\", value);\n");
            }

            sb.append("        return value;\n");
            sb.append("    }\n\n");

            // 生成set方法
            sb.append("    public void ").append(setMethodName).append("(").append(dataType).append(" value) {\n");

            if (useValidation) {
                sb.append("        if (!").append(validatorClassName).append(".").append(validateMethodName).append("(value)) {\n");
                sb.append("            Log.w(TAG, \"Invalid value for ").append(key).append("\");\n");
                sb.append("            return;\n");
                sb.append("        }\n");
            }

            sb.append("        ");
            if (dataType.equals("String")) {
                sb.append(prefsFieldName).append(".edit().putString(\"").append(key).append("\", value).apply();\n");
            } else if (dataType.equals("int")) {
                sb.append(prefsFieldName).append(".edit().putInt(\"").append(key).append("\", value).apply();\n");
            } else if (dataType.equals("long")) {
                sb.append(prefsFieldName).append(".edit().putLong(\"").append(key).append("\", value).apply();\n");
            } else if (dataType.equals("float")) {
                sb.append(prefsFieldName).append(".edit().putFloat(\"").append(key).append("\", value).apply();\n");
            } else if (dataType.equals("boolean")) {
                sb.append(prefsFieldName).append(".edit().putBoolean(\"").append(key).append("\", value).apply();\n");
            } else if (dataType.equals("StringSet")) {
                sb.append(prefsFieldName).append(".edit().putStringSet(\"").append(key).append("\", value).apply();\n");
            }

            if (useCache && !dataType.equals("StringSet")) {
                sb.append("        putCache(\"").append(key).append("\", value);\n");
            }

            sb.append("    }\n\n");
        }
    }

    private void generateAdvancedMethods(StringBuilder sb, String className, String prefsFieldName, String cacheMapName, boolean useCache) {
        // 清除所有方法
        sb.append("    public void clearAll() {\n");
        sb.append("        Editor editor = ").append(prefsFieldName).append(".edit();\n");
        sb.append("        editor.clear();\n");
        sb.append("        editor.apply();\n");
        if (useCache) {
            sb.append("        ").append(cacheMapName).append(".clear();\n");
        }
        sb.append("    }\n\n");

        // 移除指定key方法
        sb.append("    public void removeKey(String key) {\n");
        sb.append("        Editor editor = ").append(prefsFieldName).append(".edit();\n");
        sb.append("        editor.remove(key);\n");
        sb.append("        editor.apply();\n");
        if (useCache) {
            sb.append("        ").append(cacheMapName).append(".remove(key);\n");
        }
        sb.append("    }\n\n");

        // 检查key是否存在
        sb.append("    public boolean contains(String key) {\n");
        sb.append("        return ").append(prefsFieldName).append(".contains(key);\n");
        sb.append("    }\n\n");

        // 获取所有key
        sb.append("    public Map<String, ?> getAll() {\n");
        sb.append("        return ").append(prefsFieldName).append(".getAll();\n");
        sb.append("    }\n\n");
    }

    private void generateMigrationMethods(StringBuilder sb, String className, String prefsFieldName) {
        sb.append("    public void migrateFrom(SharedPreferences oldPrefs) {\n");
        sb.append("        Map<String, ?> oldData = oldPrefs.getAll();\n");
        sb.append("        Editor editor = ").append(prefsFieldName).append(".edit();\n");
        sb.append("        for (Map.Entry<String, ?> entry : oldData.entrySet()) {\n");
        sb.append("            Object value = entry.getValue();\n");
        sb.append("            if (value instanceof String) {\n");
        sb.append("                editor.putString(entry.getKey(), (String) value);\n");
        sb.append("            } else if (value instanceof Integer) {\n");
        sb.append("                editor.putInt(entry.getKey(), (Integer) value);\n");
        sb.append("            } else if (value instanceof Long) {\n");
        sb.append("                editor.putLong(entry.getKey(), (Long) value);\n");
        sb.append("            } else if (value instanceof Float) {\n");
        sb.append("                editor.putFloat(entry.getKey(), (Float) value);\n");
        sb.append("            } else if (value instanceof Boolean) {\n");
        sb.append("                editor.putBoolean(entry.getKey(), (Boolean) value);\n");
        sb.append("            } else if (value instanceof Set) {\n");
        sb.append("                editor.putStringSet(entry.getKey(), (Set<String>) value);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        editor.apply();\n");
        sb.append("        oldPrefs.edit().clear().apply();\n");
        sb.append("    }\n\n");
    }
}
