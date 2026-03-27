package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class RepositoryGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] DATA_TYPES = {
            "String", "Integer", "Boolean", "User", "Product",
            "Data", "Content", "Entity", "Record", "Entry",
            "Value", "Instance", "Bean", "DTO", "VO",
            "Model", "Item", "Element", "Node", "Object"
    };

    private static final String[] METHOD_TYPES = {
            "get", "save", "delete", "update", "query", "search",
            "find", "fetch", "retrieve", "load", "store",
            "remove", "modify", "filter", "sort", "order"
    };

    public RepositoryGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成Repository类");

        String storageType = variationManager.getVariation("storage");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Repository");
            generateRepositoryClass(className, storageType, asyncHandler);
        }
    }

    private void generateRepositoryClass(String className, String storageType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("repository"));

        // 导入必要的类
        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));

        // 根据存储类型导入对应依赖
        if (storageType.contains("room")) {
            sb.append(generateImportStatement("androidx.room.Room"));
            sb.append(generateImportStatement("androidx.room.RoomDatabase"));
        } else if (storageType.contains("sqlite")) {
            sb.append(generateImportStatement("android.database.sqlite.SQLiteDatabase"));
            sb.append(generateImportStatement("android.database.sqlite.SQLiteOpenHelper"));
        }

        // 异步处理依赖
        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        } else if (asyncHandler.contains("rxjava")) {
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
        }

        // 业务相关导入
        sb.append(generateImportStatement(packageName + ".model.*"));
        sb.append(generateImportStatement(packageName + ".database.*"));
        sb.append(generateImportStatement("android.content.SharedPreferences"));
        sb.append("\n"); // 规范换行

        // 类定义
        sb.append("public class ").append(className).append(" {\n\n");

        // 基础常量（修复转义错误）
        String dataType = DATA_TYPES[RandomUtils.between(0, DATA_TYPES.length - 1)];
        String methodType = METHOD_TYPES[RandomUtils.between(0, METHOD_TYPES.length - 1)];
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String DATA_TYPE = \"").append(dataType).append("\";\n\n");

        // 功能开关标志
        boolean useCache = RandomUtils.randomBoolean();
        boolean useSharedPreferences = RandomUtils.randomBoolean();
        boolean useStatistics = RandomUtils.randomBoolean();
        boolean useBatch = RandomUtils.randomBoolean();
        boolean useAsync = RandomUtils.randomBoolean();

        // 生成功能字段
        if (useCache) {
            sb.append("    private Map<String, ").append(dataType).append("> cache;\n");
            sb.append("    private int maxCacheSize = 100;\n");
            sb.append("    private boolean cacheEnabled = true;\n\n"); // 补充缺失的字段定义
        }

        if (useSharedPreferences) {
            sb.append("    private SharedPreferences sharedPreferences;\n\n");
        }

        if (useStatistics) {
            sb.append("    private int readCount = 0;\n");
            sb.append("    private int writeCount = 0;\n");
            sb.append("    private long lastOperationTime = 0;\n\n");
        }

        if (useBatch) {
            sb.append("    private List<").append(dataType).append("> batchBuffer;\n");
            sb.append("    private int batchSize = 10;\n\n");
        }

        // 核心上下文字段
        sb.append("    private Context context;\n\n");

        // 构造方法（适配不同存储类型）
        if (storageType.contains("room")) {
            String databaseName = RandomUtils.generateClassName("Database");
            sb.append("    public ").append(className).append("(Context context, ").append(databaseName).append(" database) {\n");
            sb.append("        this.context = context.getApplicationContext();\n");
        } else if (storageType.contains("sqlite")) {
            sb.append("    public ").append(className).append("(Context context, SQLiteDatabase database) {\n");
            sb.append("        this.context = context.getApplicationContext();\n");
        } else {
            sb.append("    public ").append(className).append("(Context context) {\n");
            sb.append("        this.context = context.getApplicationContext();\n");
        }

        // 初始化功能字段
        if (useCache) {
            sb.append("        this.cache = new HashMap<>();\n");
        }

        if (useSharedPreferences) {
            // 修复SharedPreferences名称转义
            sb.append("        this.sharedPreferences = context.getSharedPreferences(\"").append(className).append("_prefs\", Context.MODE_PRIVATE);\n");
        }

        if (useBatch) {
            sb.append("        this.batchBuffer = new ArrayList<>();\n");
        }

        if (useStatistics) {
            sb.append("        this.lastOperationTime = System.currentTimeMillis();\n");
        }

        sb.append("    }\n\n");

        // 基础数据操作方法
        sb.append("    public ").append(dataType).append(" ").append(methodType).append("Data(String id) {\n");
        sb.append("        if (id == null || id.isEmpty()) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        ").append(dataType).append(" result = fetchData(id);\n");

        if (useStatistics) {
            sb.append("        updateReadStatistics();\n");
        }

        sb.append("        return result;\n");
        sb.append("    }\n\n");

        // 私有数据获取方法（修复Log转义、基础类型实例化问题）
        sb.append("    private ").append(dataType).append(" fetchData(String id) {\n");
        if (useCache) { // 判空cache，避免空指针
            sb.append("        if (cacheEnabled && cache != null && cache.containsKey(id)) {\n");
            sb.append("            ").append(dataType).append(" cached = cache.get(id);\n");
            sb.append("            if (cached != null) {\n");
            // 修复Log字符串转义
            sb.append("                Log.d(TAG, \"Retrieved from cache: \" + id);\n");
            sb.append("                return cached;\n");
            sb.append("            }\n");
            sb.append("        }\n");
        }

        // 适配基础类型和自定义类型的实例化
        sb.append("        ").append(dataType).append(" data = ");
        if (dataType.equals("String")) {
            sb.append("\"default_\" + id;\n");
        } else if (dataType.equals("Integer")) {
            sb.append("Integer.parseInt(id);\n");
        } else if (dataType.equals("Boolean")) {
            sb.append("id.length() % 2 == 0;\n");
        } else {
            sb.append("new ").append(dataType).append("();\n");
        }

        // 修复Log字符串转义
        sb.append("        Log.d(TAG, \"Fetched data: \" + id);\n");

        if (useCache && !dataType.equals("int")) { // 基础类型int不缓存（避免拆箱问题）
            sb.append("        if (cacheEnabled && cache != null) {\n");
            sb.append("            cache.put(id, data);\n");
            sb.append("        }\n");
        }

        sb.append("        return data;\n");
        sb.append("    }\n\n");

        // 数据保存方法
        sb.append("    public void saveData(String id, ").append(dataType).append(" data) {\n");
        sb.append("        if (id == null || id.isEmpty() || data == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");

        if (useBatch) {
            sb.append("        if (batchBuffer != null) {\n"); // 判空避免空指针
            sb.append("            batchBuffer.add(data);\n");
            sb.append("            if (batchBuffer.size() >= batchSize) {\n");
            sb.append("                flushBatch();\n");
            sb.append("            }\n");
            sb.append("        }\n");
        } else {
            sb.append("        insertData(id, data);\n");
        }

        if (useStatistics) {
            sb.append("        updateWriteStatistics();\n");
        }

        sb.append("    }\n\n");

        // 私有插入方法（修复Log转义）
        sb.append("    private void insertData(String id, ").append(dataType).append(" data) {\n");
        if (useSharedPreferences) {
            sb.append("        if (sharedPreferences != null) {\n"); // 判空避免空指针
            sb.append("            sharedPreferences.edit().putString(id, data.toString()).apply();\n");
            sb.append("        }\n");
        }
        sb.append("        Log.d(TAG, \"Saved data: \" + id);\n");
        sb.append("    }\n\n");

        // 数据删除方法
        sb.append("    public void deleteData(String id) {\n");
        sb.append("        if (id == null || id.isEmpty()) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");

        if (useCache) {
            sb.append("        if (cache != null) {\n"); // 判空避免空指针
            sb.append("            cache.remove(id);\n");
            sb.append("        }\n");
        }

        sb.append("        removeData(id);\n");

        if (useStatistics) {
            sb.append("        updateWriteStatistics();\n");
        }

        sb.append("    }\n\n");

        // 私有删除方法（修复Log转义）
        sb.append("    private void removeData(String id) {\n");
        if (useSharedPreferences) {
            sb.append("        if (sharedPreferences != null) {\n"); // 判空避免空指针
            sb.append("            sharedPreferences.edit().remove(id).apply();\n");
            sb.append("        }\n");
        }
        sb.append("        Log.d(TAG, \"Deleted data: \" + id);\n");
        sb.append("    }\n\n");

        // 生成配套功能方法
        if (useCache) {
            generateCacheMethods(sb, dataType);
        }

        if (useBatch) {
            generateBatchMethods(sb, dataType);
        }

        if (useStatistics) {
            generateStatisticsMethods(sb);
        }

        // 生成随机业务方法
        int methodCount = RandomUtils.between(3, 6);
        for (int i = 0; i < methodCount; i++) {
            String methodName = RandomUtils.generateMethodName("process");
            String returnType = getRandomType();

            sb.append("    public ").append(returnType).append(" ").append(methodName).append("(");

            // 生成方法参数
            int paramCount = RandomUtils.between(0, 3);
            for (int j = 0; j < paramCount; j++) {
                if (j > 0) sb.append(", ");
                sb.append(getRandomType()).append(" ").append(RandomUtils.generateVariableName("param"));
            }
            sb.append(") {\n");

            if (useStatistics) {
                sb.append("        updateReadStatistics();\n");
            }

            // 生成方法体（修复字符串转义）
            if (returnType.equals("void")) {
                sb.append("        Log.d(TAG, \"").append(methodName).append(" called\");\n");
            } else if (returnType.equals("boolean")) {
                sb.append("        boolean result = ").append(RandomUtils.randomBoolean()).append(";\n");
                sb.append("        Log.d(TAG, \"").append(methodName).append(" result: \" + result);\n");
                sb.append("        return result;\n");
            } else if (returnType.equals("int")) {
                sb.append("        int result = ").append(RandomUtils.between(0, 100)).append(";\n");
                sb.append("        Log.d(TAG, \"").append(methodName).append(" result: \" + result);\n");
                sb.append("        return result;\n");
            } else if (returnType.equals("String")) {
                sb.append("        String result = \"").append(RandomUtils.generateWord(RandomUtils.between(3, 8))).append("\";\n");
                sb.append("        Log.d(TAG, \"").append(methodName).append(" result: \" + result);\n");
                sb.append("        return result;\n");
            } else if (returnType.equals("List<String>")) {
                sb.append("        List<String> result = new ArrayList<>();\n");
                sb.append("        result.add(\"").append(RandomUtils.generateWord(RandomUtils.between(3, 8))).append("\");\n");
                sb.append("        Log.d(TAG, \"").append(methodName).append(" result size: \" + result.size());\n");
                sb.append("        return result;\n");
            }
            sb.append("    }\n\n");
        }

        // 闭合类
        sb.append("}\n");

        // 生成文件
        generateJavaFile(className, sb.toString(), "repository");
    }

    // 缓存相关方法（修复Log转义、判空）
    private void generateCacheMethods(StringBuilder sb, String dataType) {
        sb.append("    public void clearCache() {\n");
        sb.append("        if (cache != null) {\n");
        sb.append("            cache.clear();\n");
        sb.append("            Log.d(TAG, \"Cache cleared\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public int getCacheSize() {\n");
        sb.append("        return cache != null ? cache.size() : 0;\n");
        sb.append("    }\n\n");

        sb.append("    public void setCacheEnabled(boolean enabled) {\n");
        sb.append("        this.cacheEnabled = enabled;\n");
        sb.append("        Log.d(TAG, \"Cache: \" + (enabled ? \"enabled\" : \"disabled\"));\n");
        sb.append("    }\n\n");

        sb.append("    public void setMaxCacheSize(int size) {\n");
        sb.append("        this.maxCacheSize = size;\n");
        sb.append("    }\n\n");
    }

    // 批量操作方法（修复ID拼接转义、判空）
    private void generateBatchMethods(StringBuilder sb, String dataType) {
        sb.append("    public void flushBatch() {\n");
        sb.append("        if (batchBuffer == null || batchBuffer.isEmpty()) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        for (int i = 0; i < batchBuffer.size(); i++) {\n");
        sb.append("            ").append(dataType).append(" data = batchBuffer.get(i);\n");
        // 修复批量ID拼接转义
        sb.append("            insertData(\"batch_\" + i, data);\n");
        sb.append("        }\n");
        sb.append("        batchBuffer.clear();\n");
        sb.append("        Log.d(TAG, \"Batch flushed\");\n");
        sb.append("    }\n\n");

        sb.append("    public void setBatchSize(int size) {\n");
        sb.append("        this.batchSize = size;\n");
        sb.append("    }\n\n");
    }

    // 统计相关方法（修复Log转义）
    private void generateStatisticsMethods(StringBuilder sb) {
        sb.append("    private void updateReadStatistics() {\n");
        sb.append("        readCount++;\n");
        sb.append("        lastOperationTime = System.currentTimeMillis();\n");
        sb.append("    }\n\n");

        sb.append("    private void updateWriteStatistics() {\n");
        sb.append("        writeCount++;\n");
        sb.append("        lastOperationTime = System.currentTimeMillis();\n");
        sb.append("    }\n\n");

        sb.append("    public int getReadCount() {\n");
        sb.append("        return readCount;\n");
        sb.append("    }\n\n");

        sb.append("    public int getWriteCount() {\n");
        sb.append("        return writeCount;\n");
        sb.append("    }\n\n");

        sb.append("    public long getLastOperationTime() {\n");
        sb.append("        return lastOperationTime;\n");
        sb.append("    }\n\n");

        sb.append("    public void resetStatistics() {\n");
        sb.append("        readCount = 0;\n");
        sb.append("        writeCount = 0;\n");
        sb.append("        lastOperationTime = System.currentTimeMillis();\n");
        sb.append("        Log.d(TAG, \"Statistics reset\");\n");
        sb.append("    }\n\n");
    }

    // 获取随机返回类型
    public String getRandomType() {
        String[] types = {"void", "boolean", "int", "String", "List<String>"};
        return types[RandomUtils.between(0, types.length - 1)];
    }
}