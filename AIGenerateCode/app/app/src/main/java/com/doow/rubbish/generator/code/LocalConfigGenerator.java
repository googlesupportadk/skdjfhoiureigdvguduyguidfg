package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocalConfigGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] VALUE_TYPES = {
            "String", "Integer", "Boolean", "Long", "Float", "Double", "StringSet"
    };

    public LocalConfigGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成配置类");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Config");
            generateConfigClass(className);
        }
    }

    private void generateConfigClass(String className) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("config"));

        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.content.SharedPreferences"));
        sb.append(generateImportStatement("android.os.Handler"));
        sb.append(generateImportStatement("android.os.Looper"));

        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.HashSet"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.Set"));

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 生成随机的类名后缀和类型标识符
        String typeSuffix = RandomUtils.generateRandomString(6);
        String configType = RandomUtils.generateRandomString(8);
        String storageMode = RandomUtils.generateRandomString(8);

        sb.append("public class ").append(className).append(" {\n\n");

        // 生成所有随机常量名和值
        String constConfigType = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constStorageMode = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultString = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultInt = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultBoolean = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultLong = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultFloat = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();
        String constDefaultDouble = "CONST_" + RandomUtils.generateRandomString(8).toUpperCase();

        // 生成随机的默认值字符串
        String defaultStringValue = RandomUtils.generateRandomString(12);
        int defaultIntValue = RandomUtils.between(0, 100);
        boolean defaultBooleanValue = RandomUtils.randomBoolean();
        long defaultLongValue = RandomUtils.between(0, 1000);
        float defaultFloatValue = (float) RandomUtils.nextDouble(0.0, 100.0);
        double defaultDoubleValue = RandomUtils.nextDouble(0.0, 100.0);

        // 添加常量
        sb.append("    private static final String ").append(constConfigType).append(" = \"" + "" + configType + "\";\n");
        sb.append("    private static final String ").append(constStorageMode).append(" = \"" + "" + storageMode + "\";\n");
        sb.append("    private static final String ").append(constDefaultString).append(" = \"" + "" + defaultStringValue + "\";\n");
        sb.append("    private static final int ").append(constDefaultInt).append(" = ").append(defaultIntValue).append(";\n");
        sb.append("    private static final boolean ").append(constDefaultBoolean).append(" = ").append(defaultBooleanValue).append(";\n");
        sb.append("    private static final long ").append(constDefaultLong).append(" = ").append(defaultLongValue).append(";\n");
        sb.append("    private static final float ").append(constDefaultFloat).append(" = ").append(defaultFloatValue).append("f;\n");
        sb.append("    private static final double ").append(constDefaultDouble).append(" = ").append(defaultDoubleValue).append(";\n\n");

        // 随机生成多个配置字段
        int fieldCount = RandomUtils.between(5, 12);
        List<String> fieldNames = new ArrayList<>();
        List<String> configKeys = new ArrayList<>();
        Map<String, String> fieldToKeyMap = new HashMap<>();
        Map<String, String> fieldToTypeMap = new HashMap<>();
        Map<String, String> fieldToDefaultMap = new HashMap<>();

        for (int i = 0; i < fieldCount; i++) {
            String valueType = VALUE_TYPES[RandomUtils.between(0, VALUE_TYPES.length - 1)];
            String fieldName = RandomUtils.generateVariableName(valueType.toLowerCase());
            String configKey = RandomUtils.generateRandomString(8) + "_" + RandomUtils.generateRandomString(5);

            fieldNames.add(fieldName);
            configKeys.add(configKey);
            fieldToKeyMap.put(fieldName, configKey);
            fieldToTypeMap.put(fieldName, valueType);

            // 为每个字段生成随机默认值
            String defaultValue;
            switch (valueType) {
                case "String":
                    defaultValue = RandomUtils.generateRandomString(10);
                    break;
                case "Integer":
                    defaultValue = String.valueOf(RandomUtils.between(0, 100));
                    break;
                case "Boolean":
                    defaultValue = String.valueOf(RandomUtils.randomBoolean());
                    break;
                case "Long":
                    defaultValue = String.valueOf(RandomUtils.betweenLong(0L, 1000L));
                    break;
                case "Float":
                    defaultValue = String.valueOf((float) RandomUtils.nextDouble(0.0, 100.0));
                    break;
                case "Double":
                    defaultValue = String.valueOf(RandomUtils.nextDouble(0.0, 100.0));
                    break;
                case "StringSet":
                    defaultValue = "new HashSet<>()";
                    break;
                default:
                    defaultValue = "null";
                    break;
            }
            fieldToDefaultMap.put(fieldName, defaultValue);

            if (RandomUtils.randomBoolean()) {
                sb.append("    private static final ").append(valueType).append(" ").append(fieldName);
                sb.append(" = ").append(defaultValue).append(";\n");
            } else {
                sb.append("    private ").append(valueType).append(" ").append(fieldName).append(";\n");
            }
        }

        // 生成随机的字段名
        String fieldContext = RandomUtils.generateVariableName("context");
        String fieldSharedPreferences = RandomUtils.generateVariableName("sharedpreferences");
        String fieldEditor = RandomUtils.generateVariableName("editor");
        String fieldHandler = RandomUtils.generateVariableName("handler");
        String fieldChangeListeners = RandomUtils.generateVariableName("changelisteners");
        String fieldConfigCache = RandomUtils.generateVariableName("configcache");
        String fieldConfigVersion = RandomUtils.generateVariableName("configversion");
        String fieldLastUpdateTime = RandomUtils.generateVariableName("lastupdatetime");
        String fieldIsDirty = RandomUtils.generateVariableName("isdirty");
        String fieldOperationCount = RandomUtils.generateVariableName("operationcount");
        String fieldFieldAccessCount = RandomUtils.generateVariableName("fieldaccesscount");

        // 添加配置相关字段
        sb.append("    private Context ").append(fieldContext).append(";\n");
        sb.append("    private SharedPreferences ").append(fieldSharedPreferences).append(";\n");
        sb.append("    private SharedPreferences.Editor ").append(fieldEditor).append(";\n");
        sb.append("    private Handler ").append(fieldHandler).append(";\n");
        sb.append("    private List<String> ").append(fieldChangeListeners).append(";\n");
        sb.append("    private Map<String, Object> ").append(fieldConfigCache).append(";\n");
        sb.append("    private int ").append(fieldConfigVersion).append(";\n");
        sb.append("    private long ").append(fieldLastUpdateTime).append(";\n");
        sb.append("    private boolean ").append(fieldIsDirty).append(";\n");
        sb.append("    private int ").append(fieldOperationCount).append(";\n");
        sb.append("    private Map<String, Integer> ").append(fieldFieldAccessCount).append(";\n\n");

        // 生成随机的参数名
        String paramContext = RandomUtils.generateVariableName("context");

        // 构造函数
        sb.append("    public ").append(className).append("(").append("Context ").append(paramContext).append(") {\n");
        sb.append("        this.").append(fieldContext).append(" = ").append(paramContext).append(".getApplicationContext();\n");
        sb.append("        this.").append(fieldSharedPreferences).append(" = ").append(paramContext).append(".getSharedPreferences(").append(constConfigType).append(", Context.MODE_PRIVATE);\n");
        sb.append("        this.").append(fieldEditor).append(" = ").append(fieldSharedPreferences).append(".edit();\n");
        sb.append("        this.").append(fieldHandler).append(" = new Handler(Looper.getMainLooper());\n");
        sb.append("        this.").append(fieldChangeListeners).append(" = new ArrayList<>();\n");
        sb.append("        this.").append(fieldConfigCache).append(" = new HashMap<>();\n");
        sb.append("        this.").append(fieldConfigVersion).append(" = 1;\n");
        sb.append("        this.").append(fieldLastUpdateTime).append(" = System.currentTimeMillis();\n");
        sb.append("        this.").append(fieldIsDirty).append(" = false;\n");
        sb.append("        this.").append(fieldOperationCount).append(" = 0;\n");
        sb.append("        this.").append(fieldFieldAccessCount).append(" = new HashMap<>();\n");

        for (String fieldName : fieldNames) {
            if (!isStaticField(fieldName)) {
                String configKey = fieldToKeyMap.get(fieldName);
                String loadMethodName = "load" + capitalize(fieldName);
                sb.append("        this.").append(fieldName).append(" = ").append(loadMethodName).append("(\"" + "" + configKey + "\");\n");
                sb.append("        ").append(fieldFieldAccessCount).append(".put(\"" + "" + configKey + "\", 0);\n");
            }
        }
        sb.append("    }\n\n");

        // 生成加载配置方法
        for (String fieldName : fieldNames) {
            if (RandomUtils.randomBoolean()) {
                String configKey = fieldToKeyMap.get(fieldName);
                String valueType = fieldToTypeMap.get(fieldName);
                String loadMethodName = "load" + capitalize(fieldName);
                String paramKey = RandomUtils.generateVariableName("key");
                String localValue = RandomUtils.generateVariableName("value");
                String localCache = RandomUtils.generateVariableName("cache");

                sb.append("    private ").append(valueType).append(" ").append(loadMethodName).append("(String ").append(paramKey).append(") {\n");
                sb.append("        ").append(valueType).append(" ").append(localValue).append(";\n");
                sb.append("        switch (valueType) {\n");
                sb.append("            case \"String\":\n");
                sb.append("                ").append(localValue).append(" = ").append(fieldSharedPreferences).append(".getString(").append(paramKey).append(", ").append(constDefaultString).append(");\n");
                sb.append("                break;\n");
                sb.append("            case \"Integer\":\n");
                sb.append("                ").append(localValue).append(" = ").append(fieldSharedPreferences).append(".getInt(").append(paramKey).append(", ").append(constDefaultInt).append(");\n");
                sb.append("                break;\n");
                sb.append("            case \"Boolean\":\n");
                sb.append("                ").append(localValue).append(" = ").append(fieldSharedPreferences).append(".getBoolean(").append(paramKey).append(", ").append(constDefaultBoolean).append(");\n");
                sb.append("                break;\n");
                sb.append("            case \"Long\":\n");
                sb.append("                ").append(localValue).append(" = ").append(fieldSharedPreferences).append(".getLong(").append(paramKey).append(", ").append(constDefaultLong).append(");\n");
                sb.append("                break;\n");
                sb.append("            case \"Float\":\n");
                sb.append("                ").append(localValue).append(" = ").append(fieldSharedPreferences).append(".getFloat(").append(paramKey).append(", ").append(constDefaultFloat).append(");\n");
                sb.append("                break;\n");
                sb.append("            case \"Double\":\n");
                sb.append("                ").append(localValue).append(" = Double.longBitsToDouble(").append(fieldSharedPreferences).append(".getLong(").append(paramKey).append(", Double.doubleToLongBits(").append(constDefaultDouble).append(")));\n");
                sb.append("                break;\n");
                sb.append("            case \"StringSet\":\n");
                sb.append("                ").append(localValue).append(" = ").append(fieldSharedPreferences).append(".getStringSet(").append(paramKey).append(", new HashSet<>());\n");
                sb.append("                break;\n");
                sb.append("            default:\n");
                sb.append("                ").append(localValue).append(" = null;\n");
                sb.append("                break;\n");
                sb.append("        }\n");
                sb.append("        ").append(fieldConfigCache).append(".put(").append(paramKey).append(", ").append(localValue).append(");\n");
                sb.append("        return ").append(localValue).append(";\n");
                sb.append("    }\n\n");
            }
        }

        // 生成保存配置方法
        for (String fieldName : fieldNames) {
            if (RandomUtils.randomBoolean()) {
                String configKey = fieldToKeyMap.get(fieldName);
                String valueType = fieldToTypeMap.get(fieldName);
                String saveMethodName = "save" + capitalize(fieldName);
                String paramKey = RandomUtils.generateVariableName("key");
                String paramValue = RandomUtils.generateVariableName("value");
                String localDirty = RandomUtils.generateVariableName("dirty");

                sb.append("    private void ").append(saveMethodName).append("(String ").append(paramKey).append(", ").append(valueType).append(" ").append(paramValue).append(") {\n");
                sb.append("        boolean ").append(localDirty).append(" = false;\n");
                sb.append("        switch (valueType) {\n");
                sb.append("            case \"String\":\n");
                sb.append("                ").append(fieldEditor).append(".putString(").append(paramKey).append(", (String) ").append(paramValue).append(");\n");
                sb.append("                break;\n");
                sb.append("            case \"Integer\":\n");
                sb.append("                ").append(fieldEditor).append(".putInt(").append(paramKey).append(", (Integer) ").append(paramValue).append(");\n");
                sb.append("                break;\n");
                sb.append("            case \"Boolean\":\n");
                sb.append("                ").append(fieldEditor).append(".putBoolean(").append(paramKey).append(", (Boolean) ").append(paramValue).append(");\n");
                sb.append("                break;\n");
                sb.append("            case \"Long\":\n");
                sb.append("                ").append(fieldEditor).append(".putLong(").append(paramKey).append(", (Long) ").append(paramValue).append(");\n");
                sb.append("                break;\n");
                sb.append("            case \"Float\":\n");
                sb.append("                ").append(fieldEditor).append(".putFloat(").append(paramKey).append(", (Float) ").append(paramValue).append(");\n");
                sb.append("                break;\n");
                sb.append("            case \"Double\":\n");
                sb.append("                ").append(fieldEditor).append(".putLong(").append(paramKey).append(", Double.doubleToLongBits((Double) ").append(paramValue).append("));\n");
                sb.append("                break;\n");
                sb.append("            case \"StringSet\":\n");
                sb.append("                ").append(fieldEditor).append(".putStringSet(").append(paramKey).append(", (Set<String>) ").append(paramValue).append(");\n");
                sb.append("                break;\n");
                sb.append("            default:\n");
                sb.append("                break;\n");
                sb.append("        }\n");
                sb.append("        ").append(fieldConfigCache).append(".put(").append(paramKey).append(", ").append(paramValue).append(");\n");
                sb.append("        ").append(fieldIsDirty).append(" = true;\n");
                sb.append("        ").append(fieldLastUpdateTime).append(" = System.currentTimeMillis();\n");
                sb.append("        ").append(fieldOperationCount).append("++;\n");
                sb.append("    }\n\n");
            }
        }

        // 生成getter方法
        for (String fieldName : fieldNames) {
            if (RandomUtils.randomBoolean()) {
                String configKey = fieldToKeyMap.get(fieldName);
                String valueType = fieldToTypeMap.get(fieldName);
                String getMethodName = "get" + capitalize(fieldName);
                String localAccess = RandomUtils.generateVariableName("access");

                sb.append("    public ").append(valueType).append(" ").append(getMethodName).append("() {\n");
                sb.append("        int ").append(localAccess).append(" = ").append(fieldFieldAccessCount).append(".getOrDefault(\"" + "" + configKey + "\", 0);\n");
                sb.append("        ").append(fieldFieldAccessCount).append(".put(\"" + "" + configKey + "\", ").append(localAccess).append(" + 1);\n");
                sb.append("        return ").append(fieldName).append(";\n");
                sb.append("    }\n\n");
            }
        }

        // 生成setter方法
        for (String fieldName : fieldNames) {
            if (RandomUtils.randomBoolean()) {
                String configKey = fieldToKeyMap.get(fieldName);
                String valueType = fieldToTypeMap.get(fieldName);
                String setMethodName = "set" + capitalize(fieldName);
                String paramValue = RandomUtils.generateVariableName("value");
                String saveMethodName = "save" + capitalize(fieldName);
                String notifyMethodName = "notify" + RandomUtils.generateRandomString(6);

                sb.append("    public void ").append(setMethodName).append("(").append(valueType).append(" ").append(paramValue).append(") {\n");
                sb.append("        this.").append(fieldName).append(" = ").append(paramValue).append(";\n");
                sb.append("        ").append(saveMethodName).append("(\"" + "" + configKey + "\", ").append(paramValue).append(");\n");
                sb.append("        ").append(notifyMethodName).append("(\"" + "" + configKey + "\");\n");
                sb.append("    }\n\n");
            }
        }

        // 生成批量保存方法
        if (RandomUtils.randomBoolean()) {
            String saveAllMethodName = RandomUtils.generateVariableName("saveall");
            sb.append("    public void ").append(saveAllMethodName).append("() {\n");
            sb.append("        if (").append(fieldIsDirty).append(") {\n");
            sb.append("            ").append(fieldEditor).append(".apply();\n");
            sb.append("            ").append(fieldIsDirty).append(" = false;\n");
            sb.append("            ").append(fieldLastUpdateTime).append(" = System.currentTimeMillis();\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成异步保存方法
        if (RandomUtils.randomBoolean()) {
            String saveAsyncMethodName = RandomUtils.generateVariableName("saveasync");
            String paramRunnable = RandomUtils.generateVariableName("runnable");
            sb.append("    public void ").append(saveAsyncMethodName).append("() {\n");
            sb.append("        if (").append(fieldIsDirty).append(") {\n");
            sb.append("            ").append(fieldHandler).append(".post(new Runnable() {\n");
            sb.append("                @Override\n");
            sb.append("                public void run() {\n");
            sb.append("                    ").append(fieldEditor).append(".apply();\n");
            sb.append("                    ").append(fieldIsDirty).append(" = false;\n");
            sb.append("                    ").append(fieldLastUpdateTime).append(" = System.currentTimeMillis();\n");
            sb.append("                }\n");
            sb.append("            });\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成配置变更通知方法
        if (RandomUtils.randomBoolean()) {
            String notifyMethodName = "notify" + RandomUtils.generateRandomString(6);
            String paramKey = RandomUtils.generateVariableName("key");
            String localListener = RandomUtils.generateVariableName("listener");
            String notifyListenerMethodName = "notify" + RandomUtils.generateRandomString(8);

            sb.append("    private void ").append(notifyMethodName).append("(String ").append(paramKey).append(") {\n");
            sb.append("        for (String ").append(localListener).append(" : ").append(fieldChangeListeners).append(") {\n");
            sb.append("            ").append(notifyListenerMethodName).append("(").append(localListener).append(", ").append(paramKey).append(");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成监听器通知方法
        if (RandomUtils.randomBoolean()) {
            String notifyListenerMethodName = "notify" + RandomUtils.generateRandomString(8);
            String paramListenerId = RandomUtils.generateVariableName("listenerid");
            String paramKey = RandomUtils.generateVariableName("key");
            String localValue = RandomUtils.generateVariableName("value");
            String updateMethodName = "update" + RandomUtils.generateRandomString(8);

            sb.append("    private void ").append(notifyListenerMethodName).append("(String ").append(paramListenerId).append(", String ").append(paramKey).append(") {\n");
            sb.append("        Object ").append(localValue).append(" = ").append(fieldConfigCache).append(".get(").append(paramKey).append(");\n");
            sb.append("        ").append(updateMethodName).append("(").append(paramListenerId).append(", ").append(localValue).append(");\n");
            sb.append("    }\n\n");
        }

        // 生成更新监听器值方法
        if (RandomUtils.randomBoolean()) {
            String updateMethodName = "update" + RandomUtils.generateRandomString(8);
            String paramListenerId = RandomUtils.generateVariableName("listenerid");
            String paramValue = RandomUtils.generateVariableName("value");
            String updateCacheMethodName = "update" + RandomUtils.generateRandomString(8);

            sb.append("    private void ").append(updateMethodName).append("(String ").append(paramListenerId).append(", Object ").append(paramValue).append(") {\n");
            sb.append("        if (").append(paramValue).append(" != null) {\n");
            sb.append("            ").append(updateCacheMethodName).append("(").append(paramListenerId).append(", ").append(paramValue).append(");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成更新配置缓存方法
        if (RandomUtils.randomBoolean()) {
            String updateCacheMethodName = "update" + RandomUtils.generateRandomString(8);
            String paramKey = RandomUtils.generateVariableName("key");
            String paramValue = RandomUtils.generateVariableName("value");

            sb.append("    private void ").append(updateCacheMethodName).append("(String ").append(paramKey).append(", Object ").append(paramValue).append(") {\n");
            sb.append("        ").append(fieldConfigCache).append(".put(").append(paramKey).append(", ").append(paramValue).append(");\n");
            sb.append("        ").append(fieldConfigVersion).append("++;\n");
            sb.append("    }\n\n");
        }

        // 生成添加监听器方法
        if (RandomUtils.randomBoolean()) {
            String addListenerMethodName = "add" + RandomUtils.generateRandomString(8);
            String paramListenerId = RandomUtils.generateVariableName("listenerid");

            sb.append("    public void ").append(addListenerMethodName).append("(String ").append(paramListenerId).append(") {\n");
            sb.append("        if (!").append(fieldChangeListeners).append(".contains(").append(paramListenerId).append(")) {\n");
            sb.append("            ").append(fieldChangeListeners).append(".add(").append(paramListenerId).append(");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成移除监听器方法
        if (RandomUtils.randomBoolean()) {
            String removeListenerMethodName = "remove" + RandomUtils.generateRandomString(8);
            String paramListenerId = RandomUtils.generateVariableName("listenerid");

            sb.append("    public void ").append(removeListenerMethodName).append("(String ").append(paramListenerId).append(") {\n");
            sb.append("        ").append(fieldChangeListeners).append(".remove(").append(paramListenerId).append(");\n");
            sb.append("    }\n\n");
        }

        // 生成获取配置版本方法
        if (RandomUtils.randomBoolean()) {
            String getVersionMethodName = "get" + RandomUtils.generateRandomString(8);

            sb.append("    public int ").append(getVersionMethodName).append("() {\n");
            sb.append("        return ").append(fieldConfigVersion).append(";\n");
            sb.append("    }\n\n");
        }

        // 生成获取最后更新时间方法
        if (RandomUtils.randomBoolean()) {
            String getTimeMethodName = "get" + RandomUtils.generateRandomString(8);

            sb.append("    public long ").append(getTimeMethodName).append("() {\n");
            sb.append("        return ").append(fieldLastUpdateTime).append(";\n");
            sb.append("    }\n\n");
        }

        // 生成清除配置方法
        if (RandomUtils.randomBoolean()) {
            String clearMethodName = "clear" + RandomUtils.generateRandomString(8);
            // 生成重置配置方法
            if (RandomUtils.randomBoolean()) {
                String resetMethodName = "reset" + RandomUtils.generateRandomString(8);
                String localEntry = RandomUtils.generateVariableName("entry");
                String resetValueMethodName = "reset" + RandomUtils.generateRandomString(8);

                sb.append("    public void ").append(resetMethodName).append("() {\n");
                sb.append("        ").append(clearMethodName).append("();\n");
                sb.append("        for (Map.Entry<String, Object> ").append(localEntry).append(" : ").append(fieldConfigCache).append(".entrySet()) {\n");
                sb.append("            ").append(resetValueMethodName).append("(").append(localEntry).append(".getKey());\n");
                sb.append("        }\n");
                sb.append("    }\n\n");
            }else{
                sb.append("    public void ").append(clearMethodName).append("() {\n");
                sb.append("        ").append(fieldEditor).append(".clear();\n");
                sb.append("        ").append(fieldEditor).append(".apply();\n");
                sb.append("        ").append(fieldConfigCache).append(".clear();\n");
                sb.append("        ").append(fieldConfigVersion).append("++;\n");
                sb.append("        ").append(fieldLastUpdateTime).append(" = System.currentTimeMillis();\n");
                sb.append("        ").append(fieldIsDirty).append(" = false;\n");
                sb.append("    }\n\n");
            }
        }



        // 生成重置值方法
        if (RandomUtils.randomBoolean()) {
            String resetValueMethodName = "reset" + RandomUtils.generateRandomString(8);
            String paramKey = RandomUtils.generateVariableName("key");
            String localValue = RandomUtils.generateVariableName("value");
            String saveMethodName = "save" + RandomUtils.generateRandomString(8);

            sb.append("    private void ").append(resetValueMethodName).append("(String ").append(paramKey).append(") {\n");
            sb.append("        Object ").append(localValue).append(" = ").append(fieldConfigCache).append(".get(").append(paramKey).append(");\n");
            sb.append("        if (").append(localValue).append(" != null) {\n");
            sb.append("            ").append(saveMethodName).append("(").append(paramKey).append(", ").append(localValue).append(");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成获取所有配置方法
        if (RandomUtils.randomBoolean()) {
            String getAllMethodName = "get" + RandomUtils.generateRandomString(8);
            String localAll = RandomUtils.generateVariableName("all");

            sb.append("    public Map<String, Object> ").append(getAllMethodName).append("() {\n");
            sb.append("        Map<String, Object> ").append(localAll).append(" = new HashMap<>();\n");
            sb.append("        ").append(localAll).append(".putAll(").append(fieldConfigCache).append(");\n");
            sb.append("        return ").append(localAll).append(";\n");
            sb.append("    }\n\n");
        }

        // 生成与计算器关联的方法
        if (RandomUtils.randomBoolean()) {
            String calculateMethodName = "calculate" + RandomUtils.generateRandomString(8);
            String localSize = RandomUtils.generateVariableName("size");
            String localEntry = RandomUtils.generateVariableName("entry");
            String calculateEntryMethodName = "calculate" + RandomUtils.generateRandomString(8);

            sb.append("    public double ").append(calculateMethodName).append("() {\n");
            sb.append("        double ").append(localSize).append(" = 0.0;\n");
            sb.append("        for (Map.Entry<String, Object> ").append(localEntry).append(" : ").append(fieldConfigCache).append(".entrySet()) {\n");
            sb.append("            ").append(localSize).append(" += ").append(calculateEntryMethodName).append("(").append(localEntry).append(".getKey(), ").append(localEntry).append(".getValue());\n");
            sb.append("        }\n");
            sb.append("        return ").append(localSize).append(";\n");
            sb.append("    }\n\n");
        }

        // 生成计算条目大小方法
        if (RandomUtils.randomBoolean()) {
            String calculateEntryMethodName = "calculate" + RandomUtils.generateRandomString(8);
            String paramKey = RandomUtils.generateVariableName("key");
            String paramValue = RandomUtils.generateVariableName("value");
            String localKeySize = RandomUtils.generateVariableName("keysize");
            String localValueSize = RandomUtils.generateVariableName("valuesize");

            sb.append("    private double ").append(calculateEntryMethodName).append("(String ").append(paramKey).append(", Object ").append(paramValue).append(") {\n");
            sb.append("        double ").append(localKeySize).append(" = ").append(paramKey).append(" != null ? ").append(paramKey).append(".length() : 0;\n");
            sb.append("        double ").append(localValueSize).append(" = ").append(paramValue).append(" != null ? ").append(paramValue).append(".toString().length() : 0;\n");
            sb.append("        return ").append(localKeySize).append(" + ").append(localValueSize).append(";\n");
            sb.append("    }\n\n");
        }

        // 生成与图表关联的方法
        if (RandomUtils.randomBoolean()) {
            String getDataMethodName = "get" + RandomUtils.generateRandomString(8);
            String localDataPoints = RandomUtils.generateVariableName("datapoints");
            String calculateMethodName = "calculate" + RandomUtils.generateRandomString(8);

            sb.append("    public double[] ").append(getDataMethodName).append("() {\n");
            sb.append("        double[] ").append(localDataPoints).append(" = new double[4];\n");
            sb.append("        ").append(localDataPoints).append("[0] = ").append(fieldConfigVersion).append(";\n");
            sb.append("        ").append(localDataPoints).append("[1] = ").append(fieldConfigCache).append(".size();\n");
            sb.append("        ").append(localDataPoints).append("[2] = ").append(fieldChangeListeners).append(".size();\n");
            sb.append("        ").append(localDataPoints).append("[3] = ").append(calculateMethodName).append("();\n");
            sb.append("        return ").append(localDataPoints).append(";\n");
            sb.append("    }\n\n");
        }

        // 生成与集合关联的方法
        if (RandomUtils.randomBoolean()) {
            String getCacheMethodName = "get" + RandomUtils.generateRandomString(8);

            sb.append("    public Map<String, Object> ").append(getCacheMethodName).append("() {\n");
            sb.append("        return ").append(fieldConfigCache).append(";\n");
            sb.append("    }\n\n");
        }

        sb.append("}\n\n");

        generateJavaFile(className, sb.toString(), "config");
    }

    private boolean isStaticField(String fieldName) {
        return fieldName.startsWith("static_");
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
