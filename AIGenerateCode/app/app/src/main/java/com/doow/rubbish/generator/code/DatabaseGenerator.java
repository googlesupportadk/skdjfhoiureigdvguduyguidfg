package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DatabaseGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] DATABASE_TYPES = {
            "room", "sqlite", "realm", "objectbox",
            "green_dao", "orm_lite", "active_android", "sugarorm",
            "snappydb", "realm_encrypted", "sqlcipher", "sql_delight",
            "requery", "dbflow", "cupboard", "ormlite", "papercup"
    };

    private static final int[] DATABASE_VERSIONS = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    };

    public DatabaseGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成数据库类");

        // 空值防护：避免variation返回null
        String storageType = variationManager.getVariation("storage");
        String asyncHandler = variationManager.getVariation("async_handler");
        storageType = storageType == null ? "room" : storageType;
        asyncHandler = asyncHandler == null ? "coroutines" : asyncHandler;

        // 边界防护：生成数量限定在合理范围
        int generateCount = RandomUtils.between(3, 8);
        for (int i = 0; i < generateCount; i++) {
            String className = RandomUtils.generateClassName("Database");
            generateDatabaseClass(className, storageType, asyncHandler);
        }
    }

    private void generateDatabaseClass(String className, String storageType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("database"));

        // 导入核心依赖
        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.concurrent.ExecutorService"));
        sb.append(generateImportStatement("java.util.concurrent.Executors"));
        sb.append(generateImportStatement("java.io.File"));
        sb.append(generateImportStatement("java.io.FileInputStream"));
        sb.append(generateImportStatement("java.io.FileOutputStream"));
        sb.append(generateImportStatement("java.io.IOException"));

        // 根据存储类型导入对应数据库依赖
        if (storageType.contains("room")) {
            sb.append(generateImportStatement("androidx.room.Database"));
            sb.append(generateImportStatement("androidx.room.RoomDatabase"));
            sb.append(generateImportStatement("androidx.room.Room"));
        } else if (storageType.contains("sqlite")) {
            sb.append(generateImportStatement("android.database.sqlite.SQLiteDatabase"));
            sb.append(generateImportStatement("android.database.sqlite.SQLiteOpenHelper"));
            sb.append(generateImportStatement("android.database.Cursor"));
            sb.append(generateImportStatement("android.content.ContentValues"));
        } else if (storageType.contains("realm")) {
            sb.append(generateImportStatement("io.realm.Realm"));
            sb.append(generateImportStatement("io.realm.RealmConfiguration"));
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
        sb.append(generateImportStatement(packageName + ".dao.*"));
        sb.append(generateImportStatement(packageName + ".model.*"));
        sb.append("\n"); // 规范空行，替换多余的空白拼接

        // 数据库基础配置（边界防护：避免数组索引越界）
        int versionIndex = RandomUtils.between(0, DATABASE_VERSIONS.length - 1);
        int version = DATABASE_VERSIONS[versionIndex];
        int dbTypeIndex = RandomUtils.between(0, DATABASE_TYPES.length - 1);
        String databaseType = DATABASE_TYPES[dbTypeIndex];

        // Room数据库生成逻辑
        if (storageType.contains("room")) {
            // Room注解 + 类定义（修复转义和缩进）
            sb.append("@Database(\n");
            sb.append("    entities = {").append(RandomUtils.generateClassName("Entity")).append(".class},\n");
            sb.append("    version = ").append(version).append(",\n");
            sb.append("    exportSchema = false\n");
            sb.append(")\n");
            sb.append("public abstract class ").append(className).append(" extends RoomDatabase {\n\n");

            // 常量定义（修复字符串转义）
            sb.append("    private static final String TAG = \"").append(className).append("\";\n");
            sb.append("    private static final int DATABASE_VERSION = ").append(version).append(";\n");
            sb.append("    private static final String DATABASE_TYPE = \"").append(databaseType).append("\";\n\n");

            // 使用标志变量确保字段和配套方法一起生成和使用
            boolean useCache = RandomUtils.randomBoolean();
            boolean useLogging = RandomUtils.randomBoolean();
            boolean useExecutor = RandomUtils.randomBoolean();
            boolean useTransaction = RandomUtils.randomBoolean();
            boolean useBackup = RandomUtils.randomBoolean();

            // 根据标志变量生成字段
            if (useCache) {
                sb.append("    private static Map<String, Object> queryCache;\n\n");
            }

            if (useLogging) {
                sb.append("    private static boolean enableLogging = true;\n\n");
            }

            if (useExecutor) {
                sb.append("    private static ExecutorService databaseExecutor;\n\n");
            }

            // DAO声明
            String daoClassName = RandomUtils.generateClassName("Dao");
            String daoVarName = RandomUtils.generateVariableName("dao");
            sb.append("    public abstract ").append(daoClassName).append(" ").append(daoVarName).append("();\n\n");

            // 单例实例
            sb.append("    private static volatile ").append(className).append(" instance;\n\n");

            // 初始化缓存
            if (useCache) {
                sb.append("    private static void initCache() {\n");
                sb.append("        if (queryCache == null) {\n");
                sb.append("            queryCache = new HashMap<>();\n");
                sb.append("        }\n");
                sb.append("    }\n\n");
            }

            // 初始化线程池
            if (useExecutor) {
                sb.append("    private static void initExecutor() {\n");
                sb.append("        if (databaseExecutor == null) {\n");
                sb.append("            databaseExecutor = Executors.newFixedThreadPool(4);\n");
                sb.append("        }\n");
                sb.append("    }\n\n");
            }

            // 单例获取方法（修复数据库名转义）
            sb.append("    public static ").append(className).append(" getInstance(Context context) {\n");
            sb.append("        if (instance == null) {\n");
            sb.append("            synchronized (").append(className).append(".class) {\n");
            sb.append("                if (instance == null) {\n");
            sb.append("                    instance = Room.databaseBuilder(\n");
            sb.append("                        context.getApplicationContext(),\n");
            sb.append("                        ").append(className).append(".class,\n");
            // 修复数据库名字符串转义
            sb.append("                        \"").append(className.toLowerCase()).append(".db\"\n");
            sb.append("                    ).build();\n");
            if (useCache) {
                sb.append("                    initCache();\n");
            }
            if (useExecutor) {
                sb.append("                    initExecutor();\n");
            }
            sb.append("                }\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        return instance;\n");
            sb.append("    }\n\n");

            // 根据标志变量生成配套方法
            if (useCache) {
                generateCacheMethods(sb, className, useLogging);
            }

            if (useLogging) {
                generateLoggingMethods(sb);
            }

            if (useExecutor) {
                generateExecutorMethods(sb);
            }

            if (useTransaction) {
                generateTransactionMethods(sb, useLogging);
            }

            if (useBackup) {
                generateBackupMethods(sb, className, useLogging);
            }

        } else if (storageType.contains("sqlite")) {
            // SQLite数据库生成逻辑
            sb.append("public class ").append(className).append(" extends SQLiteOpenHelper {\n\n");

            // 常量定义（修复字符串转义）
            sb.append("    private static final String TAG = \"").append(className).append("\";\n");
            sb.append("    private static final int DATABASE_VERSION = ").append(version).append(";\n");
            sb.append("    private static final String DATABASE_TYPE = \"").append(databaseType).append("\";\n");
            sb.append("    private static final String DATABASE_NAME = \"").append(className.toLowerCase()).append(".db\";\n\n");

            // 使用标志变量确保字段和配套方法一起生成和使用
            boolean useCache = RandomUtils.randomBoolean();
            boolean useLogging = RandomUtils.randomBoolean();
            boolean useTransaction = RandomUtils.randomBoolean();

            // 根据标志变量生成字段
            if (useCache) {
                sb.append("    private static Map<String, Object> queryCache;\n\n");
            }

            if (useLogging) {
                sb.append("    private static boolean enableLogging = true;\n\n");
            }

            // 构造方法
            sb.append("    public ").append(className).append("(Context context) {\n");
            sb.append("        super(context, DATABASE_NAME, null, DATABASE_VERSION);\n");
            sb.append("    }\n\n");

            // 创建表（修复建表语句转义和拼接）
            sb.append("    @Override\n");
            sb.append("    public void onCreate(SQLiteDatabase db) {\n");
            String entityName = RandomUtils.generateClassName("Entity");
            // 修复建表语句的字符串转义
            sb.append("        String createTable = \"CREATE TABLE IF NOT EXISTS ").append(entityName).append(" (\" +\n");
            sb.append("                \"id INTEGER PRIMARY KEY AUTOINCREMENT, \" +\n");
            sb.append("                \"name TEXT NOT NULL, \" +\n");
            sb.append("                \"value TEXT)\";\n");
            sb.append("        db.execSQL(createTable);\n");
            sb.append("    }\n\n");

            // 升级数据库（修复DROP语句转义）
            sb.append("    @Override\n");
            sb.append("    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {\n");
            sb.append("        db.execSQL(\"DROP TABLE IF EXISTS ").append(entityName).append("\");\n");
            sb.append("        onCreate(db);\n");
            sb.append("    }\n\n");

            // 根据标志变量生成配套方法
            if (useCache) {
                generateCacheMethods(sb, className, useLogging);
            }

            if (useLogging) {
                generateLoggingMethods(sb);
            }

            if (useTransaction) {
                generateTransactionMethods(sb, useLogging);
            }

            // 生成SQLite专属方法
            int methodCount = RandomUtils.between(3, 6);
            for (int i = 0; i < methodCount; i++) {
                int methodType = RandomUtils.between(0, 4); // 0-4，避免switch越界
                String methodName = RandomUtils.generateMethodName("handle");

                switch (methodType) {
                    case 0:
                        generateQueryMethod(sb, methodName, useLogging);
                        break;
                    case 1:
                        generateInsertMethod(sb, methodName, useLogging);
                        break;
                    case 2:
                        generateUpdateMethod(sb, methodName, useLogging);
                        break;
                    case 3:
                        generateDeleteMethod(sb, methodName, useLogging);
                        break;
                    case 4:
                        generateClearMethod(sb, methodName, useLogging);
                        break;
                }
            }
        }

        // 通用工具方法：文件复制（修复缩进和异常处理）
        sb.append("    private void copyFile(File source, File dest) throws IOException {\n");
        sb.append("        if (source == null || !source.exists()) {\n"); // 空值+存在性校验
        sb.append("            throw new IOException(\"Source file does not exist\");\n");
        sb.append("        }\n");
        sb.append("        try (FileInputStream in = new FileInputStream(source);\n");
        sb.append("             FileOutputStream out = new FileOutputStream(dest)) {\n");
        sb.append("            byte[] buffer = new byte[1024];\n");
        sb.append("            int length;\n");
        sb.append("            while ((length = in.read(buffer)) > 0) {\n");
        sb.append("                out.write(buffer, 0, length);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 闭合类
        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "database");
    }

    private void generateCacheMethods(StringBuilder sb, String className, boolean useLogging) {
        sb.append("    public void cacheQuery(String key, Object value) {\n");
        sb.append("        if (queryCache != null && key != null) {\n"); // 空值防护
        sb.append("            queryCache.put(key, value);\n");
        if (useLogging) {
            // 修复Log字符串转义
            sb.append("            Log.d(TAG, \"Cached: \" + key);\n");
        }
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public Object getCachedQuery(String key) {\n");
        sb.append("        if (queryCache != null && key != null) {\n"); // 空值防护
        sb.append("            Object value = queryCache.get(key);\n");
        if (useLogging) {
            sb.append("            Log.d(TAG, \"Retrieved from cache: \" + key);\n");
        }
        sb.append("            return value;\n");
        sb.append("        }\n");
        sb.append("        return null;\n");
        sb.append("    }\n\n");

        sb.append("    public void clearCache() {\n");
        sb.append("        if (queryCache != null) {\n");
        sb.append("            queryCache.clear();\n");
        if (useLogging) {
            sb.append("            Log.d(TAG, \"Cache cleared\");\n");
        }
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateLoggingMethods(StringBuilder sb) {
        sb.append("    public void enableLogging(boolean enable) {\n");
        sb.append("        enableLogging = enable;\n");
        sb.append("    }\n\n");

        sb.append("    public boolean isLoggingEnabled() {\n");
        sb.append("        return enableLogging;\n");
        sb.append("    }\n\n");
    }

    private void generateExecutorMethods(StringBuilder sb) {
        sb.append("    public void executeTask(Runnable task) {\n");
        sb.append("        if (task == null) {\n"); // 空值防护
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        if (databaseExecutor != null) {\n");
        sb.append("            databaseExecutor.execute(task);\n");
        sb.append("        } else {\n");
        sb.append("            task.run();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void shutdownExecutor() {\n");
        sb.append("        if (databaseExecutor != null) {\n");
        sb.append("            databaseExecutor.shutdownNow();\n");
        sb.append("            databaseExecutor = null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateTransactionMethods(StringBuilder sb, boolean useLogging) {
        sb.append("    public void executeTransaction(Runnable operation) {\n");
        sb.append("        if (operation == null) {\n"); // 空值防护
        sb.append("            return;\n");
        sb.append("        }\n");
        // 修复Room的runInTransaction调用方式（需要实例调用）
        sb.append("        this.runInTransaction(operation);\n");
        if (useLogging) {
            sb.append("        Log.d(TAG, \"Transaction completed successfully\");\n");
        }
        sb.append("    }\n\n");
    }

    private void generateBackupMethods(StringBuilder sb, String className, boolean useLogging) {
        sb.append("    public void backupDatabase(String path) {\n");
        sb.append("        if (path == null || path.isEmpty()) {\n"); // 空值防护
        sb.append("            Log.e(TAG, \"Backup path is empty\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        // 修复数据库路径字符串转义
        sb.append("            File dbFile = getDatabasePath(\"").append(className.toLowerCase()).append(".db\");\n");
        sb.append("            File backupFile = new File(path);\n");
        // 确保备份目录存在
        sb.append("            if (backupFile.getParentFile() != null && !backupFile.getParentFile().exists()) {\n");
        sb.append("                     backupFile.getParentFile().mkdirs();\n");
        sb.append("            }\n");
        sb.append("            copyFile(dbFile, backupFile);\n");
        if (useLogging) {
            sb.append("            Log.d(TAG, \"Database backed up to: \" + path);\n");
        }
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(TAG, \"Backup failed\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateQueryMethod(StringBuilder sb, String methodName, boolean useLogging) {
        sb.append("    public List<Map<String, Object>> ").append(methodName).append("Query(String query) {\n");
        sb.append("        List<Map<String, Object>> results = new ArrayList<>();\n");
        sb.append("        if (query == null || query.isEmpty()) {\n"); // 空值防护
        sb.append("            return results;\n");
        sb.append("        }\n");
        sb.append("        SQLiteDatabase db = getReadableDatabase();\n");
        sb.append("        Cursor cursor = null;\n");
        sb.append("        try {\n");
        sb.append("            cursor = db.rawQuery(query, null);\n");
        sb.append("            while (cursor.moveToNext()) {\n");
        sb.append("                Map<String, Object> row = new HashMap<>();\n");
        sb.append("                for (int i = 0; i < cursor.getColumnCount(); i++) {\n");
        sb.append("                    row.put(cursor.getColumnName(i), cursor.getString(i));\n");
        sb.append("                }\n");
        sb.append("                results.add(row);\n");
        sb.append("            }\n");
        if (useLogging) {
            sb.append("            Log.d(TAG, \"Query executed: \" + query);\n");
        }
        sb.append("        } catch (Exception e) {\n"); // 捕获异常，避免崩溃
        sb.append("            Log.e(TAG, \"Query failed: \" + query, e);\n");
        sb.append("        } finally {\n"); // 确保Cursor关闭
        sb.append("            if (cursor != null) {\n");
        sb.append("                cursor.close();\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return results;\n");
        sb.append("    }\n\n");
    }

    private void generateInsertMethod(StringBuilder sb, String methodName, boolean useLogging) {
        sb.append("    public long ").append(methodName).append("Insert(String table, ContentValues values) {\n");
        sb.append("        if (table == null || table.isEmpty() || values == null) {\n"); // 空值防护
        sb.append("            return -1;\n");
        sb.append("        }\n");
        sb.append("        SQLiteDatabase db = getWritableDatabase();\n");
        sb.append("        long id = -1;\n");
        sb.append("        try {\n");
        sb.append("            id = db.insert(table, null, values);\n");
        if (useLogging) {
            sb.append("            Log.d(TAG, \"Inserted into \" + table + \": \" + id);\n");
        }
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(TAG, \"Insert failed for table: \" + table, e);\n");
        sb.append("        }\n");
        sb.append("        return id;\n");
        sb.append("    }\n\n");
    }

    private void generateUpdateMethod(StringBuilder sb, String methodName, boolean useLogging) {
        sb.append("    public int ").append(methodName).append("Update(String table, ContentValues values, String whereClause, String[] whereArgs) {\n");
        sb.append("        if (table == null || table.isEmpty() || values == null) {\n"); // 空值防护
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        SQLiteDatabase db = getWritableDatabase();\n");
        sb.append("        int count = 0;\n");
        sb.append("        try {\n");
        sb.append("            count = db.update(table, values, whereClause, whereArgs);\n");
        if (useLogging) {
            sb.append("            Log.d(TAG, \"Updated in \" + table + \": \" + count + \" rows\");\n");
        }
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(TAG, \"Update failed for table: \" + table, e);\n");
        sb.append("        }\n");
        sb.append("        return count;\n");
        sb.append("    }\n\n");
    }

    private void generateDeleteMethod(StringBuilder sb, String methodName, boolean useLogging) {
        sb.append("    public int ").append(methodName).append("Delete(String table, String whereClause, String[] whereArgs) {\n");
        sb.append("        if (table == null || table.isEmpty()) {\n"); // 空值防护
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        SQLiteDatabase db = getWritableDatabase();\n");
        sb.append("        int count = 0;\n");
        sb.append("        try {\n");
        sb.append("            count = db.delete(table, whereClause, whereArgs);\n");
        if (useLogging) {
            sb.append("            Log.d(TAG, \"Deleted from \" + table + \": \" + count + \" rows\");\n");
        }
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(TAG, \"Delete failed for table: \" + table, e);\n");
        sb.append("        }\n");
        sb.append("        return count;\n");
        sb.append("    }\n\n");
    }

    private void generateClearMethod(StringBuilder sb, String methodName, boolean useLogging) {
        sb.append("    public void ").append(methodName).append("ClearAll() {\n");
        sb.append("        if (queryCache != null) {\n");
        sb.append("            queryCache.clear();\n");
        sb.append("        }\n");
        if (useLogging) {
            sb.append("        Log.d(TAG, \"All resources cleared\");\n");
        }
        sb.append("    }\n\n");
    }
}