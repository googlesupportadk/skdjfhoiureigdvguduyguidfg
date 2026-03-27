package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.ModuleVariationHelper;
import com.doow.rubbish.generator.VariationManager;

public class CacheModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;
    private ModuleVariationHelper variationHelper;

    // 缓存类型
    private static final String[] CACHE_TYPES = {
        "memory", "disk", "sharedpref", "database", "network"
    };

    public CacheModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        this.variationHelper = ModuleVariationHelper.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成缓存模块");

        // 获取当前UI风格和异步处理方式
        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成缓存模块
        generateCacheModule(uiStyle, asyncHandler);
    }

    private void generateCacheModule(String uiStyle, String asyncHandler) throws Exception {
        // 生成缓存管理器
        generateCacheManager(uiStyle, asyncHandler);

        // 生成缓存工具类
        generateCacheUtils(uiStyle, asyncHandler);
    }

    private void generateCacheManager(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = variationHelper.generateClassName("Cache");
        String instanceName = variationHelper.generateVariableName("Instance");
        String contextName = variationHelper.generateObjectName();
        String cacheName = variationHelper.generateCollectionName();
        String keyName = variationHelper.generateStringName();
        String valueName = variationHelper.generateObjectName();
        String tagName = variationHelper.generateStringName();
        String maxSizeName = variationHelper.generateIntName();

        // 使用随机值
        int maxCacheSize = variationHelper.generateIntRange(50, 200)[0];

        sb.append("package ").append(packageName).append(".cache;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import java.util.HashMap;\n");
        sb.append("import java.util.Map;\n");

        // 根据异步处理方式添加导入
        if (asyncHandler.contains("coroutines")) {
            sb.append("import kotlinx.coroutines.CoroutineScope;\n");
            sb.append("import kotlinx.coroutines.Dispatchers;\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("import io.reactivex.rxjava3.core.Single;\n");
            sb.append("import io.reactivex.rxjava3.schedulers.Schedulers;\n");
        }

        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final int ").append(maxSizeName).append(" = ").append(maxCacheSize).append(";\n");
        sb.append("\n");

        // 单例
        sb.append("    private static volatile ").append(className).append(" ").append(instanceName).append(";\n");
        sb.append("    private final ").append(contextName).append(" ").append(contextName).append(";\n");
        sb.append("    private final Map<String, Object> ").append(cacheName).append(" = new HashMap<>();\n");
        sb.append("\n");

        // 构造函数
        sb.append("    private ").append(className).append("(").append(contextName).append(" ").append(contextName).append(") {\n");
        sb.append("        this.").append(contextName).append(" = ").append(contextName).append(".getApplicationContext();\n");
        sb.append("        this.").append(cacheName).append(" = new HashMap<>();\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取实例方法
        sb.append("    public static ").append(className).append(" getInstance(").append(contextName).append(" ").append(contextName).append(") {\n");
        sb.append("        if (").append(instanceName).append(" == null) {\n");
        sb.append("            synchronized (").append(className).append(".class) {\n");
        sb.append("                if (").append(instanceName).append(" == null) {\n");
        sb.append("                    ").append(instanceName).append(" = new ").append(className).append("(").append(contextName).append(");\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(instanceName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        // 存储数据方法
        generatePutMethod(sb, className, cacheName, keyName, valueName, tagName, maxSizeName);

        // 获取数据方法
        generateGetMethod(sb, className, cacheName, keyName, tagName);

        // 删除数据方法
        generateRemoveMethod(sb, className, cacheName, keyName, tagName);

        // 清空缓存方法
        generateClearMethod(sb, className, cacheName, tagName);

        // 获取缓存大小方法
        generateSizeMethod(sb, className, cacheName, tagName);

        // 检查缓存是否存在方法
        generateContainsKeyMethod(sb, className, cacheName, keyName, tagName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "cache");
    }

    private void generatePutMethod(StringBuilder sb, String className, String cacheName,
                                   String keyName, String valueName, String tagName, String maxSizeName) {
        String methodName = variationHelper.generateMethodName("Put");
        String logMessage = variationHelper.generateLogMessage("Cached data");
        String condition = variationHelper.generateCondition(keyName);
        String valueType = "Object";

        sb.append("    public void ").append(methodName).append("(String ").append(keyName)
          .append(", ").append(valueType).append(" ").append(valueName).append(") {\n");
        sb.append("        ").append(condition).append(" {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        if (").append(cacheName).append(".size() >= ").append(maxSizeName).append(") {\n");
        sb.append("            ").append(cacheName).append(".clear();\n");
        sb.append("        }\n");
        sb.append("        ").append(cacheName).append(".put(").append(keyName).append(", ").append(valueName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetMethod(StringBuilder sb, String className, String cacheName,
                                 String keyName, String tagName) {
        String methodName = variationHelper.generateMethodName("Get");
        String logMessage = variationHelper.generateLogMessage("Retrieved data");
        String valueName = "Object";

        sb.append("    public ").append(valueName).append(" ").append(methodName).append("(String ").append(keyName).append(") {\n");
        sb.append("        ").append(variationHelper.generateCondition(keyName)).append(" {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        ").append(valueName).append(" result = ").append(cacheName).append(".get(").append(keyName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        return result;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateRemoveMethod(StringBuilder sb, String className, String cacheName,
                                   String keyName, String tagName) {
        String methodName = variationHelper.generateMethodName("Remove");
        String logMessage = variationHelper.generateLogMessage("Removed data");

        sb.append("    public void ").append(methodName).append("(String ").append(keyName).append(") {\n");
        sb.append("        ").append(variationHelper.generateCondition(keyName)).append(" {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        ").append(cacheName).append(".remove(").append(keyName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateClearMethod(StringBuilder sb, String className, String cacheName, String tagName) {
        String methodName = variationHelper.generateMethodName("Clear");
        String logMessage = variationHelper.generateLogMessage("Cleared cache");
        String sizeName = variationHelper.generateIntName();

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        int ").append(sizeName).append(" = ").append(cacheName).append(".size();\n");
        sb.append("        ").append(cacheName).append(".clear();\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + ").append(sizeName).append(" + \" items\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateSizeMethod(StringBuilder sb, String className, String cacheName, String tagName) {
        String methodName = variationHelper.generateMethodName("Size");
        String logMessage = variationHelper.generateLogMessage("Cache size");

        sb.append("    public int ").append(methodName).append("() {\n");
        sb.append("        int size = ").append(cacheName).append(".size();\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + size);\n");
        sb.append("        return size;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateContainsKeyMethod(StringBuilder sb, String className, String cacheName, String keyName, String tagName) {
        String methodName = variationHelper.generateMethodName("ContainsKey");
        String logMessage = variationHelper.generateLogMessage("Check key existence");

        sb.append("    public boolean ").append(methodName).append("(String ").append(keyName).append(") {\n");
        sb.append("        boolean exists = ").append(keyName).append(" != null && ").append(cacheName).append(".containsKey(").append(keyName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + ").append(keyName).append(");\n");
        sb.append("        return exists;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateCacheUtils(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名
        String className = variationHelper.generateClassName("Utils");
        String tagName = variationHelper.generateStringName();
        String keyName = variationHelper.generateStringName();
        String prefixName = variationHelper.generateStringName();
        String paramsName = variationHelper.generateCollectionName();

        sb.append("package ").append(packageName).append(".cache;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");

        // 根据异步处理方式添加导入
        if (asyncHandler.contains("coroutines")) {
            sb.append("import kotlinx.coroutines.CoroutineScope;\n");
            sb.append("import kotlinx.coroutines.Dispatchers;\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("import io.reactivex.rxjava3.core.Single;\n");
            sb.append("import io.reactivex.rxjava3.schedulers.Schedulers;\n");
        }

        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("\n");

        // 私有构造函数
        sb.append("    private ").append(className).append("() {\n");
        sb.append("        // ").append(variationHelper.generateComment("Private constructor to prevent instantiation")).append("\n");
        sb.append("    }\n");
        sb.append("\n");

        // 生成缓存键方法
        generateGenerateKeyMethod(sb, className, keyName, prefixName, paramsName, tagName);

        // 验证缓存键方法
        generateIsValidKeyMethod(sb, className, keyName, tagName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "cache");
    }

    private void generateGenerateKeyMethod(StringBuilder sb, String className, String keyName,
                                          String prefixName, String paramsName, String tagName) {
        String methodName = variationHelper.generateMethodName("GenerateKey");
        String logMessage = variationHelper.generateLogMessage("Generated cache key");

        sb.append("    public static String ").append(methodName).append("(String ").append(prefixName)
          .append(", String... ").append(paramsName).append(") {\n");
        sb.append("        StringBuilder ").append(keyName).append(" = new StringBuilder(").append(prefixName).append(");\n");
        sb.append("        for (String param : ").append(paramsName).append(") {\n");
        sb.append("            ").append(keyName).append(".append(\"_\").append(param);\n");
        sb.append("        }\n");
        sb.append("        String result = ").append(keyName).append(".toString();\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + result);\n");
        sb.append("        return result;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateIsValidKeyMethod(StringBuilder sb, String className, String keyName, String tagName) {
        String methodName = variationHelper.generateMethodName("IsValidKey");
        String logMessage = variationHelper.generateLogMessage("Validate cache key");
        String condition = variationHelper.generateCondition(keyName);

        sb.append("    public static boolean ").append(methodName).append("(String ").append(keyName).append(") {\n");
        sb.append("        boolean isValid = ").append(condition).append(" && !").append(keyName).append(".isEmpty();\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + ").append(keyName).append(" + \" - \" + isValid);\n");
        sb.append("        return isValid;\n");
        sb.append("    }\n");
        sb.append("\n");
    }
}
