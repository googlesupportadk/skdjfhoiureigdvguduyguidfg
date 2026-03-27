package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.ModuleVariationHelper;
import com.doow.rubbish.generator.VariationManager;

public class DatabaseModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;
    private ModuleVariationHelper variationHelper;

    // 数据库类型
    private static final String[] DATABASE_TYPES = {
        "room", "sqlite", "realm", "objectbox"
    };

    public DatabaseModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        this.variationHelper = ModuleVariationHelper.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成数据库模块");

        // 获取当前数据存储方式和异步处理方式
        String storageType = variationManager.getVariation("storage");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成数据库模块
        generateDatabaseModule(storageType, asyncHandler);
    }

    private void generateDatabaseModule(String storageType, String asyncHandler) throws Exception {
        // 根据数据存储方式生成不同的数据库模块
        if (storageType.contains("room")) {
            generateRoomDatabase(asyncHandler);
        } else if (storageType.contains("sqlite")) {
            generateSqliteDatabase(asyncHandler);
        } else if (storageType.contains("realm")) {
            generateRealmDatabase(asyncHandler);
        } else if (storageType.contains("objectbox")) {
            generateObjectBoxDatabase(asyncHandler);
        }
    }

    private void generateRoomDatabase(String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String databaseClassName = variationHelper.generateClassName("Database");
        String daoClassName = variationHelper.generateClassName("Dao");
        String instanceName = variationHelper.generateVariableName("Instance");
        String contextName = variationHelper.generateObjectName();
        String dbName = variationHelper.generateStringName();
        String versionName = variationHelper.generateIntName();
        String tagName = variationHelper.generateStringName();

        // 使用随机值
        int version = variationHelper.generateIntRange(1, 10)[0];

        sb.append("package ").append(packageName).append(".database;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import androidx.room.Database;\n");
        sb.append("import androidx.room.RoomDatabase;\n");
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
        sb.append("@Database(entities = {").append(generateEntityClasses()).append("}, version = ").append(version).append(")\n");
        sb.append("public abstract class ").append(databaseClassName).append(" extends RoomDatabase {\n");
        sb.append("\n");

        // DAO方法
        sb.append("    public abstract ").append(daoClassName).append(" ").append(generateDaoMethodName()).append(";\n");
        sb.append("\n");

        // 单例方法
        sb.append("    private static volatile ").append(databaseClassName).append(" ").append(instanceName).append(";\n");
        sb.append("\n");
        sb.append("    public static ").append(databaseClassName).append(" getInstance(").append(contextName).append(" ").append(contextName).append(") {\n");
        sb.append("        if (").append(instanceName).append(" == null) {\n");
        sb.append("            synchronized (").append(databaseClassName).append(".class) {\n");
        sb.append("                if (").append(instanceName).append(" == null) {\n");
        sb.append("                    ").append(instanceName).append(" = Room.databaseBuilder(\n");
        sb.append("                        ").append(contextName).append(".getApplicationContext(),\n");
        sb.append("                        ").append(databaseClassName).append(".class,\n");
        sb.append("                        \"").append(dbName).append("\").append(versionName).append("\").append("\n");
        sb.append("                    ).build();\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(instanceName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("}\n");

        // 保存文件
        generateJavaFile(databaseClassName, sb.toString(), "database");
    }

    private String generateEntityClasses() {
        String[] entityTypes = {"User", "Product", "Order", "Item", "Category"};
        String[] entities = new String[3];
        for (int i = 0; i < 3; i++) {
            entities[i] = variationHelper.generateClassName(entityTypes[variationHelper.generateIntRange(0, entityTypes.length - 1)[0]]);
        }
        return String.join(", ", entities);
    }

    private String generateDaoClassName() {
        return variationHelper.generateClassName("Dao");
    }

    private String generateDaoMethodName() {
        return variationHelper.generateVariableName("Dao");
    }

    private void generateSqliteDatabase(String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String databaseClassName = variationHelper.generateClassName("Database");
        String helperClassName = variationHelper.generateClassName("Helper");
        String instanceName = variationHelper.generateVariableName("Instance");
        String contextName = variationHelper.generateObjectName();
        String dbName = variationHelper.generateStringName();
        String versionName = variationHelper.generateIntName();
        String tagName = variationHelper.generateStringName();

        // 使用随机值
        int version = variationHelper.generateIntRange(1, 10)[0];

        sb.append("package ").append(packageName).append(".database;\n");

        // 导入
        sb.append("import android.content.ContentValues;\n");
        sb.append("import android.content.Context;\n");
        sb.append("import android.database.Cursor;\n");
        sb.append("import android.database.sqlite.SQLiteDatabase;\n");
        sb.append("import android.database.sqlite.SQLiteOpenHelper;\n");
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
        sb.append("public class ").append(databaseClassName).append(" extends ").append(helperClassName).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(databaseClassName).append("\";\n");
        sb.append("    private static final String ").append(dbName).append(" = \"").append(dbName).append("\";\n");
        sb.append("    private static final int ").append(versionName).append(" = ").append(version).append(";\n");
        sb.append("\n");

        // 单例
        sb.append("    private static ").append(databaseClassName).append(" ").append(instanceName).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    public ").append(databaseClassName).append("(").append(contextName).append(" ").append(contextName).append(") {\n");
        sb.append("        super(").append(contextName).append(", ").append(dbName).append(", null, ").append(versionName).append(");\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取实例方法
        sb.append("    public static synchronized ").append(databaseClassName).append(" getInstance(").append(contextName).append(" ").append(contextName).append(") {\n");
        sb.append("        if (").append(instanceName).append(" == null) {\n");
        sb.append("            ").append(instanceName).append(" = new ").append(databaseClassName).append("(").append(contextName).append(".getApplicationContext());\n");
        sb.append("        }\n");
        sb.append("        return ").append(instanceName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        // onCreate方法
        generateOnCreateMethod(sb, databaseClassName, tagName);

        // onUpgrade方法
        generateOnUpgradeMethod(sb, databaseClassName, tagName);

        // 插入数据方法
        generateInsertMethod(sb, databaseClassName, tagName);

        // 查询数据方法
        generateQueryMethod(sb, databaseClassName, tagName);

        // 更新数据方法
        generateUpdateMethod(sb, databaseClassName, tagName);

        // 删除数据方法
        generateDeleteMethod(sb, databaseClassName, tagName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(databaseClassName, sb.toString(), "database");
    }

    private void generateOnCreateMethod(StringBuilder sb, String className, String tagName) {
        String methodName = variationHelper.generateMethodName("Create");
        String logMessage = variationHelper.generateLogMessage("Database created");
        String tableName = variationHelper.generateClassName("Table");

        sb.append("    @Override\n");
        sb.append("    public void onCreate(SQLiteDatabase db) {\n");
        sb.append("        String ").append(methodName).append(" = \"CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (\n");
        sb.append("            id INTEGER PRIMARY KEY AUTOINCREMENT,\n");
        sb.append("            name TEXT NOT NULL,\n");
        sb.append("            value REAL,\n");
        sb.append("            created_at INTEGER\n");
        sb.append("        )\";\n");
        sb.append("        db.execSQL(").append(methodName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateOnUpgradeMethod(StringBuilder sb, String className, String tagName) {
        String methodName = variationHelper.generateMethodName("Upgrade");
        String logMessage = variationHelper.generateLogMessage("Database upgraded");
        String tableName = variationHelper.generateClassName("Table");

        sb.append("    @Override\n");
        sb.append("    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {\n");
        sb.append("        String ").append(methodName).append(" = \"DROP TABLE IF EXISTS ").append(tableName).append("\";\n");
        sb.append("        db.execSQL(").append(methodName).append(");\n");
        sb.append("        onCreate(db);\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateInsertMethod(StringBuilder sb, String className, String tagName) {
        String methodName = variationHelper.generateMethodName("Insert");
        String logMessage = variationHelper.generateLogMessage("Data inserted");
        String tableName = variationHelper.generateClassName("Table");
        String valuesName = variationHelper.generateObjectName();

        sb.append("    public long ").append(methodName).append("(String name, double value) {\n");
        sb.append("        SQLiteDatabase db = getWritableDatabase();\n");
        sb.append("        ContentValues ").append(valuesName).append(" = new ContentValues();\n");
        sb.append("        ").append(valuesName).append(".put(\"name\", name);\n");
        sb.append("        ").append(valuesName).append(".put(\"value\", value);\n");
        sb.append("        ").append(valuesName).append(".put(\"created_at\", System.currentTimeMillis());\n");
        sb.append("        long result = db.insert(").append(tableName).append(", null, ").append(valuesName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        return result;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateQueryMethod(StringBuilder sb, String className, String tagName) {
        String methodName = variationHelper.generateMethodName("Query");
        String logMessage = variationHelper.generateLogMessage("Data queried");
        String tableName = variationHelper.generateClassName("Table");
        String cursorName = variationHelper.generateObjectName();
        String listName = variationHelper.generateCollectionName();

        sb.append("    public List<Map<String, Object>> ").append(methodName).append("() {\n");
        sb.append("        List<Map<String, Object>> ").append(listName).append(" = new ArrayList<>();\n");
        sb.append("        SQLiteDatabase db = getReadableDatabase();\n");
        sb.append("        Cursor ").append(cursorName).append(" = db.query(").append(tableName).append(", null, null, null, null, null, null);\n");
        sb.append("        if (").append(cursorName).append(".moveToFirst()) {\n");
        sb.append("            do {\n");
        sb.append("                Map<String, Object> item = new HashMap<>();\n");
        sb.append("                item.put(\"id\", ").append(cursorName).append(".getInt(").append(cursorName).append(".getColumnIndex(\"id\")));\n");
        sb.append("                item.put(\"name\", ").append(cursorName).append(".getString(").append(cursorName).append(".getColumnIndex(\"name\")));\n");
        sb.append("                item.put(\"value\", ").append(cursorName).append(".getDouble(").append(cursorName).append(".getColumnIndex(\"value\")));\n");
        sb.append("                ").append(listName).append(".add(item);\n");
        sb.append("            } while (").append(cursorName).append(".moveToNext());\n");
        sb.append("        }\n");
        sb.append("        ").append(cursorName).append(".close();\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        return ").append(listName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateUpdateMethod(StringBuilder sb, String className, String tagName) {
        String methodName = variationHelper.generateMethodName("Update");
        String logMessage = variationHelper.generateLogMessage("Data updated");
        String tableName = variationHelper.generateClassName("Table");
        String valuesName = variationHelper.generateObjectName();

        sb.append("    public int ").append(methodName).append("(int id, String name, double value) {\n");
        sb.append("        SQLiteDatabase db = getWritableDatabase();\n");
        sb.append("        ContentValues ").append(valuesName).append(" = new ContentValues();\n");
        sb.append("        ").append(valuesName).append(".put(\"name\", name);\n");
        sb.append("        ").append(valuesName).append(".put(\"value\", value);\n");
        sb.append("        int result = db.update(").append(tableName).append(", ").append(valuesName).append(", \"id = ?\", new String[]{String.valueOf(id)});\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        return result;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateDeleteMethod(StringBuilder sb, String className, String tagName) {
        String methodName = variationHelper.generateMethodName("Delete");
        String logMessage = variationHelper.generateLogMessage("Data deleted");
        String tableName = variationHelper.generateClassName("Table");

        sb.append("    public int ").append(methodName).append("(int id) {\n");
        sb.append("        SQLiteDatabase db = getWritableDatabase();\n");
        sb.append("        int result = db.delete(").append(tableName).append(", \"id = ?\", new String[]{String.valueOf(id)});\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        return result;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateRealmDatabase(String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String databaseClassName = variationHelper.generateClassName("Database");
        String contextName = variationHelper.generateObjectName();
        String configName = variationHelper.generateObjectName();
        String instanceName = variationHelper.generateVariableName("Instance");
        String tagName = variationHelper.generateStringName();
        String dbName = variationHelper.generateStringName();

        sb.append("package ").append(packageName).append(".database;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import io.realm.Realm;\n");
        sb.append("import io.realm.RealmConfiguration;\n");

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
        sb.append("public class ").append(databaseClassName).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(databaseClassName).append("\";\n");
        sb.append("    private static final String ").append(dbName).append(" = \"").append(dbName).append("\";\n");
        sb.append("\n");

        // 单例
        sb.append("    private static ").append(databaseClassName).append(" ").append(instanceName).append(";\n");
        sb.append("    private final Realm ").append(configName).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    private ").append(databaseClassName).append("(").append(contextName).append(" ").append(contextName).append(") {\n");
        sb.append("        RealmConfiguration ").append(configName).append(" = new RealmConfiguration.Builder()\n");
        sb.append("            .name(").append(dbName).append(")\n");
        sb.append("            .deleteRealmIfMigrationNeeded()\n");
        sb.append("            .build();\n");
        sb.append("        Realm.setDefaultConfiguration(").append(configName).append(");\n");
        sb.append("        this.").append(configName).append(" = Realm.getDefaultInstance();\n");
        sb.append("        Log.d(").append(tagName).append(", \"Realm database initialized\");\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取实例方法
        sb.append("    public static synchronized ").append(databaseClassName).append(" getInstance(").append(contextName).append(" ").append(contextName).append(") {\n");
        sb.append("        if (").append(instanceName).append(" == null) {\n");
        sb.append("            ").append(instanceName).append(" = new ").append(databaseClassName).append("(").append(contextName).append(".getApplicationContext());\n");
        sb.append("        }\n");
        sb.append("        return ").append(instanceName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取Realm实例方法
        sb.append("    public Realm getRealm() {\n");
        sb.append("        return ").append(configName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        // 关闭数据库方法
        sb.append("    public void close() {\n");
        sb.append("        if (").append(configName).append(" != null && !").append(configName).append(".isClosed()) {\n");
        sb.append("            ").append(configName).append(".close();\n");
        sb.append("            Log.d(").append(tagName).append(", \"Realm database closed\");\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("}\n");

        // 保存文件
        generateJavaFile(databaseClassName, sb.toString(), "database");
    }

    private void generateObjectBoxDatabase(String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String databaseClassName = variationHelper.generateClassName("Database");
        String contextName = variationHelper.generateObjectName();
        String boxStoreName = variationHelper.generateObjectName();
        String instanceName = variationHelper.generateVariableName("Instance");
        String tagName = variationHelper.generateStringName();

        sb.append("package ").append(packageName).append(".database;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import io.objectbox.BoxStore;\n");

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
        sb.append("public class ").append(databaseClassName).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(databaseClassName).append("\";\n");
        sb.append("\n");

        // 单例
        sb.append("    private static ").append(databaseClassName).append(" ").append(instanceName).append(";\n");
        sb.append("    private final BoxStore ").append(boxStoreName).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    private ").append(databaseClassName).append("(").append(contextName).append(" ").append(contextName).append(") {\n");
        sb.append("        ").append(boxStoreName).append(" = MyObjectBox.builder()\n");
        sb.append("            .androidContext(").append(contextName).append(".getApplicationContext())\n");
        sb.append("            .build();\n");
        sb.append("        Log.d(").append(tagName).append(", \"ObjectBox database initialized\");\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取实例方法
        sb.append("    public static synchronized ").append(databaseClassName).append(" getInstance(").append(contextName).append(" ").append(contextName).append(") {\n");
        sb.append("        if (").append(instanceName).append(" == null) {\n");
        sb.append("            ").append(instanceName).append(" = new ").append(databaseClassName).append("(").append(contextName).append(".getApplicationContext());\n");
        sb.append("        }\n");
        sb.append("        return ").append(instanceName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取BoxStore方法
        sb.append("    public BoxStore getBoxStore() {\n");
        sb.append("        return ").append(boxStoreName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        // 关闭数据库方法
        sb.append("    public void close() {\n");
        sb.append("        if (").append(boxStoreName).append(" != null) {\n");
        sb.append("            ").append(boxStoreName).append(".close();\n");
        sb.append("            Log.d(").append(tagName).append(", \"ObjectBox database closed\");\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("}\n");

        // 保存文件
        generateJavaFile(databaseClassName, sb.toString(), "database");
    }
}
