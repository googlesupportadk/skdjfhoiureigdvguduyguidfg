package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class ViewModelFactoryGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    public ViewModelFactoryGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成ViewModelFactory类");

        String diType = variationManager.getVariation("dependency_injection");

        for (int i = 0; i < RandomUtils.between(3, 8); i++) {
            String className = RandomUtils.generateClassName("ViewModelFactory");
            generateFactory(className, diType);
        }
    }

    private void generateFactory(String className, String diType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("factory"));

        // 导入必要的类
        sb.append(generateImportStatement("androidx.annotation.NonNull"));
        sb.append(generateImportStatement("androidx.lifecycle.ViewModel"));
        sb.append(generateImportStatement("androidx.lifecycle.ViewModelProvider"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.lang.reflect.InstantiationException"));
        sb.append(generateImportStatement("java.lang.reflect.IllegalAccessException"));
        sb.append("\n"); // 规范换行

        // 生成依赖导入（随机数量）
        int dependencyCount = RandomUtils.between(1, 6);
        for (int i = 0; i < dependencyCount; i++) {
            sb.append(generateImportStatement(packageName + ".repository.*"));
        }

        // 类定义
        sb.append("public class ").append(className).append(" extends ViewModelProvider.Factory {\n\n");

        // TAG常量（修复转义错误）
        sb.append("    private static final String TAG = \"").append(className).append("\";\n\n");

        // 功能开关标志
        boolean useCache = RandomUtils.randomBoolean();
        boolean useDependencyMap = RandomUtils.randomBoolean();
        boolean useCreationHistory = RandomUtils.randomBoolean();
        boolean useStatistics = RandomUtils.randomBoolean();
        boolean useLogging = RandomUtils.randomBoolean();

        // 生成功能字段
        if (useCache) {
            sb.append("    private Map<Class<? extends ViewModel>, ViewModel> viewModelCache;\n");
            sb.append("    private int maxCacheSize = 10;\n\n");
        }

        if (useDependencyMap) {
            sb.append("    private Map<String, Object> dependencyMap;\n\n");
        }

        if (useCreationHistory) {
            sb.append("    private List<String> creationHistory;\n");
            sb.append("    private int maxHistorySize = 50;\n\n");
        }

        if (useStatistics) {
            sb.append("    private int creationCount = 0;\n");
            sb.append("    private long lastCreationTime = 0;\n\n");
        }

        // 生成依赖字段（记录字段名，用于构造方法赋值）
        String[] depFieldNames = new String[dependencyCount];
        for (int i = 0; i < dependencyCount; i++) {
            String depName = RandomUtils.generateVariableName("repository");
            depFieldNames[i] = depName;
            sb.append("    private ").append(getRandomType()).append(" ").append(depName).append(";\n");
        }
        if (dependencyCount > 0) {
            sb.append("\n");
        }

        // 构造方法（修复参数与字段赋值不一致问题）
        sb.append("    public ").append(className).append("(");
        String[] depParamNames = new String[dependencyCount];
        for (int i = 0; i < dependencyCount; i++) {
            if (i > 0) sb.append(", ");
            String depType = getRandomType();
            String depParamName = RandomUtils.generateVariableName("repository");
            depParamNames[i] = depParamName;
            sb.append(depType).append(" ").append(depParamName);
        }
        sb.append(") {\n");

        // 初始化功能字段
        if (useCache) {
            sb.append("        viewModelCache = new HashMap<>();\n");
        }

        if (useDependencyMap) {
            sb.append("        dependencyMap = new HashMap<>();\n");
        }

        if (useCreationHistory) {
            sb.append("        creationHistory = new ArrayList<>();\n");
        }

        if (useStatistics) {
            sb.append("        lastCreationTime = System.currentTimeMillis();\n");
        }

        // 赋值依赖字段（参数名与字段名对应）
        for (int i = 0; i < dependencyCount; i++) {
            sb.append("        this.").append(depFieldNames[i]).append(" = ").append(depParamNames[i]).append(";\n");
        }
        sb.append("    }\n\n");

        // create方法（核心ViewModel创建逻辑）
        sb.append("    @NonNull\n");
        sb.append("    @Override\n");
        sb.append("    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {\n");
        sb.append("        try {\n");

        // 日志记录
        if (useLogging) {
            sb.append("            logCreation(modelClass);\n");
        }

        // 缓存读取
        if (useCache) {
            sb.append("            if (viewModelCache.containsKey(modelClass)) {\n");
            // 修复Log字符串转义
            sb.append("                Log.d(TAG, \"Retrieved from cache: \" + modelClass.getSimpleName());\n");
            sb.append("                return (T) viewModelCache.get(modelClass);\n");
            sb.append("            }\n");
        }

        // 创建ViewModel（使用getDeclaredConstructor避免newInstance过时问题）
        sb.append("            T viewModel = modelClass.getDeclaredConstructor().newInstance();\n");

        // 更新统计
        if (useStatistics) {
            sb.append("            updateStatistics();\n");
        }

        // 记录创建历史
        if (useCreationHistory) {
            sb.append("            addToHistory(modelClass);\n");
        }

        // 加入缓存
        if (useCache) {
            sb.append("            addToCache(modelClass, viewModel);\n");
        }

        sb.append("            return viewModel;\n");

        // 异常处理（修复返回null的问题，改为抛异常符合NonNull约束）
        sb.append("        } catch (InstantiationException | IllegalAccessException | ReflectiveOperationException e) {\n");
        // 修复Log字符串转义
        sb.append("            Log.e(TAG, \"Failed to create ViewModel\", e);\n");
        sb.append("            throw new RuntimeException(\"Failed to create ViewModel for \" + modelClass.getName(), e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成配套功能方法
        if (useCache) {
            generateCacheMethods(sb);
        }

        if (useDependencyMap) {
            generateDependencyMethods(sb);
        }

        if (useCreationHistory) {
            generateHistoryMethods(sb);
        }

        if (useStatistics) {
            generateStatisticsMethods(sb);
        }

        if (useLogging) {
            generateLoggingMethods(sb);
        }

        // 闭合类
        sb.append("}\n");

        // 生成文件
        generateJavaFile(className, sb.toString(), "factory");
    }

    // 缓存相关方法（修复Log字符串转义）
    private void generateCacheMethods(StringBuilder sb) {
        sb.append("    private void addToCache(Class<? extends ViewModel> modelClass, ViewModel viewModel) {\n");
        sb.append("        if (viewModelCache.size() >= maxCacheSize) {\n");
        sb.append("            viewModelCache.clear();\n");
        sb.append("            Log.d(TAG, \"Cache cleared (max size reached)\");\n");
        sb.append("        }\n");
        sb.append("        viewModelCache.put(modelClass, viewModel);\n");
        sb.append("    }\n\n");

        sb.append("    public void clearCache() {\n");
        sb.append("        viewModelCache.clear();\n");
        sb.append("        Log.d(TAG, \"Cache cleared\");\n");
        sb.append("    }\n\n");

        sb.append("    public int getCacheSize() {\n");
        sb.append("        return viewModelCache.size();\n");
        sb.append("    }\n\n");

        sb.append("    public void setMaxCacheSize(int size) {\n");
        sb.append("        this.maxCacheSize = size;\n");
        sb.append("    }\n\n");
    }

    // 依赖管理方法（修复Log字符串转义）
    private void generateDependencyMethods(StringBuilder sb) {
        sb.append("    public void addDependency(String key, Object dependency) {\n");
        sb.append("        if (key != null && dependency != null) {\n");
        sb.append("            dependencyMap.put(key, dependency);\n");
        sb.append("            Log.d(TAG, \"Dependency added: \" + key);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public Object getDependency(String key) {\n");
        sb.append("        return dependencyMap.get(key);\n");
        sb.append("    }\n\n");

        sb.append("    public void removeDependency(String key) {\n");
        sb.append("        dependencyMap.remove(key);\n");
        sb.append("        Log.d(TAG, \"Dependency removed: \" + key);\n");
        sb.append("    }\n\n");

        sb.append("    public void clearDependencies() {\n");
        sb.append("        dependencyMap.clear();\n");
        sb.append("        Log.d(TAG, \"All dependencies cleared\");\n");
        sb.append("    }\n\n");
    }

    // 创建历史方法
    private void generateHistoryMethods(StringBuilder sb) {
        sb.append("    private void addToHistory(Class<? extends ViewModel> modelClass) {\n");
        sb.append("        if (creationHistory.size() >= maxHistorySize) {\n");
        sb.append("            creationHistory.remove(0);\n");
        sb.append("        }\n");
        sb.append("        creationHistory.add(modelClass.getSimpleName());\n");
        sb.append("    }\n\n");

        sb.append("    public List<String> getCreationHistory() {\n");
        sb.append("        return new ArrayList<>(creationHistory);\n");
        sb.append("    }\n\n");

        sb.append("    public void clearHistory() {\n");
        sb.append("        creationHistory.clear();\n");
        sb.append("        Log.d(TAG, \"Creation history cleared\");\n");
        sb.append("    }\n\n");

        sb.append("    public void setMaxHistorySize(int size) {\n");
        sb.append("        this.maxHistorySize = size;\n");
        sb.append("    }\n\n");
    }

    // 统计方法（修复Log字符串转义）
    private void generateStatisticsMethods(StringBuilder sb) {
        sb.append("    private void updateStatistics() {\n");
        sb.append("        creationCount++;\n");
        sb.append("        lastCreationTime = System.currentTimeMillis();\n");
        sb.append("    }\n\n");

        sb.append("    public int getCreationCount() {\n");
        sb.append("        return creationCount;\n");
        sb.append("    }\n\n");

        sb.append("    public long getLastCreationTime() {\n");
        sb.append("        return lastCreationTime;\n");
        sb.append("    }\n\n");

        sb.append("    public void resetStatistics() {\n");
        sb.append("        creationCount = 0;\n");
        sb.append("        lastCreationTime = System.currentTimeMillis();\n");
        sb.append("        Log.d(TAG, \"Statistics reset\");\n");
        sb.append("    }\n\n");
    }

    // 日志方法（修复Log字符串转义）
    private void generateLoggingMethods(StringBuilder sb) {
        sb.append("    private void logCreation(Class<? extends ViewModel> modelClass) {\n");
        sb.append("        Log.d(TAG, \"Creating ViewModel: \" + modelClass.getSimpleName());\n");
        sb.append("    }\n\n");

        sb.append("    public void setLoggingEnabled(boolean enabled) {\n");
        sb.append("        Log.d(TAG, \"Logging: \" + (enabled ? \"enabled\" : \"disabled\"));\n");
        sb.append("    }\n\n");
    }

    // 获取随机依赖类型
    public String getRandomType() {
        String[] types = {"Repository", "DataSource", "Service", "Manager", "Helper"};
        return types[RandomUtils.between(0, types.length - 1)];
    }
}