package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.EnhancedRandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class TodoModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;

    // 待办事项类型
    private static final String[] TODO_TYPES = {
        "task", "project", "milestone", "reminder", "habit"
    };

    // 优先级类型
    private static final String[] PRIORITY_TYPES = {
        "low", "medium", "high", "urgent"
    };

    // 状态类型
    private static final String[] STATUS_TYPES = {
        "pending", "in_progress", "completed", "cancelled"
    };

    // 排序方式
    private static final String[] SORT_TYPES = {
        "due_date", "priority", "created_date", "title"
    };

    public TodoModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成待办事项模块");

        // 获取当前UI风格和异步处理方式
        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成待办事项模块
        generateTodoModule(uiStyle, asyncHandler);
    }

    private void generateTodoModule(String uiStyle, String asyncHandler) throws Exception {
        // 生成待办事项管理器
        generateTodoManager(uiStyle, asyncHandler);

        // 生成待办事项工具类
        generateTodoUtils(uiStyle, asyncHandler);

        // 生成待办事项搜索器
        generateTodoSearcher(uiStyle, asyncHandler);
    }

    private void generateTodoManager(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Manager");
        String instanceName = EnhancedRandomUtils.generateVariableName("Instance");
        String contextName = EnhancedRandomUtils.generateObjectName();
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String callbackName = EnhancedRandomUtils.generateClassName("Callback");
        String callbackVarName = EnhancedRandomUtils.generateVariableName("Callback");
        String todosName = EnhancedRandomUtils.generateCollectionName();
        String sortBy = EnhancedRandomUtils.generateStringName();
        String sortAscending = EnhancedRandomUtils.generateBooleanName();

        // 使用随机值
        int maxTodos = EnhancedRandomUtils.generateIntRange(50, 500)[0];
        String defaultSortBy = SORT_TYPES[EnhancedRandomUtils.between(0, SORT_TYPES.length - 1)];
        boolean defaultSortAscending = EnhancedRandomUtils.nextBoolean();

        sb.append("package ").append(packageName).append(".todo;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import java.util.ArrayList;\n");
        sb.append("import java.util.Collections;\n");
        sb.append("import java.util.Comparator;\n");
        sb.append("import java.util.Date;\n");
        sb.append("import java.util.List;\n");

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
        sb.append("    private static final int MAX_TODOS = ").append(maxTodos).append(";\n");
        sb.append("\n");

        // 回调接口
        sb.append("    public interface ").append(callbackName).append(" {\n");
        sb.append("        void onTodoAdded(Todo todo);\n");
        sb.append("        void onTodoUpdated(Todo todo);\n");
        sb.append("        void onTodoDeleted(Todo todo);\n");
        sb.append("        void onTodoCompleted(Todo todo);\n");
        sb.append("        void onError(String error);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 成员变量
        sb.append("    private final Context ").append(contextName).append(";\n");
        sb.append("    private final ").append(callbackName).append(" ").append(callbackVarName).append(";\n");
        sb.append("    private final List<Todo> ").append(todosName).append(";\n");
        sb.append("    private String ").append(sortBy).append(";\n");
        sb.append("    private boolean ").append(sortAscending).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    public ").append(className).append("(Context ").append(contextName).append(", ").append(callbackName).append(" ").append(callbackVarName).append(") {\n");
        sb.append("        this.").append(contextName).append(" = ").append(contextName).append(".getApplicationContext();\n");
        sb.append("        this.").append(callbackVarName).append(" = ").append(callbackVarName).append(";\n");
        sb.append("        this.").append(todosName).append(" = new ArrayList<>();\n");
        sb.append("        this.").append(sortBy).append(" = \"").append(defaultSortBy).append("\";\n");
        sb.append("        this.").append(sortAscending).append(" = ").append(defaultSortAscending).append(";\n");
        sb.append("\n");
        sb.append("        // 加载待办事项\n");
        sb.append("        loadTodos();\n");
        sb.append("    }\n");
        sb.append("\n");

        // 添加待办事项方法
        generateAddTodoMethod(sb, className, todosName, sortBy, sortAscending, maxTodos, tagName, callbackVarName);

        // 更新待办事项方法
        generateUpdateTodoMethod(sb, className, todosName, sortBy, sortAscending, tagName, callbackVarName);

        // 删除待办事项方法
        generateDeleteTodoMethod(sb, className, todosName, sortBy, sortAscending, tagName, callbackVarName);

        // 完成待办事项方法
        generateCompleteTodoMethod(sb, className, todosName, sortBy, sortAscending, tagName, callbackVarName);

        // 获取所有待办事项方法
        generateGetAllTodosMethod(sb, className, todosName);

        // 搜索待办事项方法
        generateSearchTodosMethod(sb, className, todosName);

        // 排序待办事项方法
        generateSortTodosMethod(sb, className, todosName, sortBy, sortAscending);

        // 清空待办事项方法
        generateClearTodosMethod(sb, className, todosName, tagName);

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("todo");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateAddTodoMethod(StringBuilder sb, String className, String todosName,
                                          String sortBy, String sortAscending, int maxTodos,
                                          String tagName, String callbackVarName) {
        String methodName = EnhancedRandomUtils.generateMethodName("AddTodo");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Todo added");
        String logErrorMessage = EnhancedRandomUtils.generateLogMessage("Error adding todo");

        sb.append("    public void ").append(methodName).append("(Todo todo) {\n");
        sb.append("        if (todo == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 设置创建时间\n");
        sb.append("        todo.setCreatedAt(new Date());\n");
        sb.append("        todo.setUpdatedAt(new Date());\n");
        sb.append("\n");
        sb.append("        // 添加到列表\n");
        sb.append("        ").append(todosName).append(".add(todo);\n");
        sb.append("\n");
        sb.append("        // 限制待办事项数量\n");
        sb.append("        while (").append(todosName).append(".size() > MAX_TODOS) {\n");
        sb.append("            ").append(todosName).append(".remove(0);\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 排序待办事项\n");
        sb.append("        sortTodos();\n");
        sb.append("\n");
        sb.append("        // 保存待办事项\n");
        sb.append("        saveTodos();\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + todo.getTitle());\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onTodoAdded(todo);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateUpdateTodoMethod(StringBuilder sb, String className, String todosName,
                                            String sortBy, String sortAscending,
                                            String tagName, String callbackVarName) {
        String methodName = EnhancedRandomUtils.generateMethodName("UpdateTodo");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Todo updated");
        String logErrorMessage = EnhancedRandomUtils.generateLogMessage("Error updating todo");

        sb.append("    public void ").append(methodName).append("(Todo todo) {\n");
        sb.append("        if (todo == null || !").append(todosName).append(".contains(todo)) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 设置更新时间\n");
        sb.append("        todo.setUpdatedAt(new Date());\n");
        sb.append("\n");
        sb.append("        // 排序待办事项\n");
        sb.append("        sortTodos();\n");
        sb.append("\n");
        sb.append("        // 保存待办事项\n");
        sb.append("        saveTodos();\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + todo.getTitle());\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onTodoUpdated(todo);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateDeleteTodoMethod(StringBuilder sb, String className, String todosName,
                                            String sortBy, String sortAscending,
                                            String tagName, String callbackVarName) {
        String methodName = EnhancedRandomUtils.generateMethodName("DeleteTodo");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Todo deleted");
        String logErrorMessage = EnhancedRandomUtils.generateLogMessage("Error deleting todo");

        sb.append("    public void ").append(methodName).append("(Todo todo) {\n");
        sb.append("        if (todo == null || !").append(todosName).append(".contains(todo)) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 从列表中移除\n");
        sb.append("        ").append(todosName).append(".remove(todo);\n");
        sb.append("\n");
        sb.append("        // 保存待办事项\n");
        sb.append("        saveTodos();\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + todo.getTitle());\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onTodoDeleted(todo);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateCompleteTodoMethod(StringBuilder sb, String className, String todosName,
                                              String sortBy, String sortAscending,
                                              String tagName, String callbackVarName) {
        String methodName = EnhancedRandomUtils.generateMethodName("CompleteTodo");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Todo completed");
        String logErrorMessage = EnhancedRandomUtils.generateLogMessage("Error completing todo");

        sb.append("    public void ").append(methodName).append("(Todo todo) {\n");
        sb.append("        if (todo == null || !").append(todosName).append(".contains(todo)) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 设置完成状态\n");
        sb.append("        todo.setStatus(\"completed\");\n");
        sb.append("        todo.setCompletedAt(new Date());\n");
        sb.append("\n");
        sb.append("        // 排序待办事项\n");
        sb.append("        sortTodos();\n");
        sb.append("\n");
        sb.append("        // 保存待办事项\n");
        sb.append("        saveTodos();\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + todo.getTitle());\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onTodoCompleted(todo);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetAllTodosMethod(StringBuilder sb, String className, String todosName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetAllTodos");

        sb.append("    public List<Todo> ").append(methodName).append("() {\n");
        sb.append("        return new ArrayList<>(").append(todosName).append(");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateSearchTodosMethod(StringBuilder sb, String className, String todosName) {
        String methodName = EnhancedRandomUtils.generateMethodName("SearchTodos");
        String queryName = EnhancedRandomUtils.generateVariableName("Query");
        String resultsName = EnhancedRandomUtils.generateCollectionName();

        sb.append("    public List<Todo> ").append(methodName).append("(String ").append(queryName).append(") {\n");
        sb.append("        List<Todo> ").append(resultsName).append(" = new ArrayList<>();\n");
        sb.append("        if (").append(queryName).append(" == null || ").append(queryName).append(".isEmpty()) {\n");
        sb.append("            return ").append(resultsName).append(";\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        for (Todo todo : ").append(todosName).append(") {\n");
        sb.append("            if (todo.getTitle().toLowerCase().contains(").append(queryName).append(".toLowerCase())) {\n");
        sb.append("                ").append(resultsName).append(".add(todo);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultsName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateSortTodosMethod(StringBuilder sb, String className, String todosName, String sortBy, String sortAscending) {
        String methodName = EnhancedRandomUtils.generateMethodName("SortTodos");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Todos sorted");

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        switch (").append(sortBy).append(") {\n");
        sb.append("            case \"due_date\":\n");
        sb.append("                Collections.sort(").append(todosName).append(", Comparator.comparing(Todo::getDueDate));\n");
        sb.append("                break;\n");
        sb.append("            case \"priority\":\n");
        sb.append("                Collections.sort(").append(todosName).append(", Comparator.comparing(Todo::getPriority));\n");
        sb.append("                break;\n");
        sb.append("            case \"created_date\":\n");
        sb.append("                Collections.sort(").append(todosName).append(", Comparator.comparing(Todo::getCreatedAt));\n");
        sb.append("                break;\n");
        sb.append("            case \"title\":\n");
        sb.append("                Collections.sort(").append(todosName).append(", Comparator.comparing(Todo::getTitle));\n");
        sb.append("                break;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        if (!").append(sortAscending).append(") {\n");
        sb.append("            Collections.reverse(").append(todosName).append(");\n");
        sb.append("        }\n");
        sb.append("        Log.d(").append(className).append(".TAG, \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateClearTodosMethod(StringBuilder sb, String className, String todosName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ClearTodos");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Todos cleared");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        ").append(todosName).append(".clear();\n");
        sb.append("        saveTodos();\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateTodoUtils(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Utils");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");

        sb.append("package ").append(packageName).append(".todo;\n");
        sb.append("\n");

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
        sb.append("        // 私有构造函数，防止实例化\n");
        sb.append("    }\n");
        sb.append("\n");

        // 创建待办事项方法
        generateCreateTodoMethod(sb, className, tagName);

        // 验证待办事项方法
        generateValidateTodoMethod(sb, className, tagName);

        // 格式化优先级方法
        generateFormatPriorityMethod(sb, className, tagName);

        // 格式化状态方法
        generateFormatStatusMethod(sb, className, tagName);

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("todo");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateCreateTodoMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("CreateTodo");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Todo created");

        sb.append("    public static Todo ").append(methodName).append("(String title, String description, String priority, String dueDate) {\n");
        sb.append("        Todo todo = new Todo();\n");
        sb.append("        todo.setTitle(title);\n");
        sb.append("        todo.setDescription(description);\n");
        sb.append("        todo.setPriority(priority);\n");
        sb.append("        todo.setStatus(\"pending\");\n");
        sb.append("        todo.setCreatedAt(new Date());\n");
        sb.append("        todo.setUpdatedAt(new Date());\n");
        sb.append("        if (dueDate != null && !dueDate.isEmpty()) {\n");
        sb.append("            todo.setDueDate(new Date(Long.parseLong(dueDate)));\n");
        sb.append("        }\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + title);\n");
        sb.append("        return todo;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateValidateTodoMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ValidateTodo");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Todo validated");
        String logErrorMessage = EnhancedRandomUtils.generateLogMessage("Todo validation failed");

        sb.append("    public static boolean ").append(methodName).append("(Todo todo) {\n");
        sb.append("        if (todo == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        if (todo.getTitle() == null || todo.getTitle().isEmpty()) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"").append(logErrorMessage).append(": Title is empty\");\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        if (todo.getPriority() == null || todo.getPriority().isEmpty()) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"").append(logErrorMessage).append(": Priority is empty\");\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        return true;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateFormatPriorityMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("FormatPriority");

        sb.append("    public static String ").append(methodName).append("(String priority) {\n");
        sb.append("        if (priority == null || priority.isEmpty()) {\n");
        sb.append("            return \"medium\";\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        switch (priority.toLowerCase()) {\n");
        sb.append("            case \"low\":\n");
        sb.append("                return \"Low\";\n");
        sb.append("            case \"medium\":\n");
        sb.append("                return \"Medium\";\n");
        sb.append("            case \"high\":\n");
        sb.append("                return \"High\";\n");
        sb.append("            case \"urgent\":\n");
        sb.append("                return \"Urgent\";\n");
        sb.append("            default:\n");
        sb.append("                return \"Medium\";\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateFormatStatusMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("FormatStatus");

        sb.append("    public static String ").append(methodName).append("(String status) {\n");
        sb.append("        if (status == null || status.isEmpty()) {\n");
        sb.append("            return \"pending\";\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        switch (status.toLowerCase()) {\n");
        sb.append("            case \"pending\":\n");
        sb.append("                return \"Pending\";\n");
        sb.append("            case \"in_progress\":\n");
        sb.append("                return \"In Progress\";\n");
        sb.append("            case \"completed\":\n");
        sb.append("                return \"Completed\";\n");
        sb.append("            case \"cancelled\":\n");
        sb.append("                return \"Cancelled\";\n");
        sb.append("            default:\n");
        sb.append("                return \"Pending\";\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateTodoSearcher(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Searcher");
        String instanceName = EnhancedRandomUtils.generateVariableName("Instance");
        String contextName = EnhancedRandomUtils.generateObjectName();
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String callbackName = EnhancedRandomUtils.generateClassName("Callback");
        String callbackVarName = EnhancedRandomUtils.generateVariableName("Callback");

        sb.append("package ").append(packageName).append(".todo;\n");
        sb.append("\n");

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

        // 单例
        sb.append("    private static volatile ").append(className).append(" ").append(instanceName).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    private ").append(className).append("() {\n");
        sb.append("        // 私有构造函数，防止实例化\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取实例方法
        sb.append("    public static ").append(className).append(" getInstance() {\n");
        sb.append("        if (").append(instanceName).append(" == null) {\n");
        sb.append("            synchronized (").append(className).append(".class) {\n");
        sb.append("                if (").append(instanceName).append(" == null) {\n");
        sb.append("                    ").append(instanceName).append(" = new ").append(className).append("();\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(instanceName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        // 搜索待办事项方法
        generateSearchTodosMethodInSearcher(sb, className, tagName);

        // 过滤待办事项方法
        generateFilterTodosMethodInSearcher(sb, className, tagName);

        // 排序待办事项方法
        generateSortTodosMethodInSearcher(sb, className, tagName);

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("todo");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateSearchTodosMethodInSearcher(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("SearchTodos");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Todos searched");

        sb.append("    public List<Todo> ").append(methodName).append("(String query) {\n");
        sb.append("        if (query == null || query.isEmpty()) {\n");
        sb.append("            return new ArrayList<>();\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        List<Todo> results = new ArrayList<>();\n");
        sb.append("        for (Todo todo : TodoManager.getInstance().getAllTodos()) {\n");
        sb.append("            if (todo.getTitle().toLowerCase().contains(query.toLowerCase())) {\n");
        sb.append("                results.add(todo);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + query);\n");
        sb.append("        return results;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateFilterTodosMethodInSearcher(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("FilterTodos");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Todos filtered");

        sb.append("    public List<Todo> ").append(methodName).append("(String status) {\n");
        sb.append("        if (status == null || status.isEmpty()) {\n");
        sb.append("            return new ArrayList<>();\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        List<Todo> results = new ArrayList<>();\n");
        sb.append("        for (Todo todo : TodoManager.getInstance().getAllTodos()) {\n");
        sb.append("            if (todo.getStatus().equalsIgnoreCase(status)) {\n");
        sb.append("                results.add(todo);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + status);\n");
        sb.append("        return results;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateSortTodosMethodInSearcher(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("SortTodos");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Todos sorted");

        sb.append("    public List<Todo> ").append(methodName).append("(List<Todo> todos, String sortBy, boolean ascending) {\n");
        sb.append("        if (todos == null || todos.isEmpty()) {\n");
        sb.append("            return new ArrayList<>();\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        List<Todo> sorted = new ArrayList<>(todos);\n");
        sb.append("        switch (sortBy.toLowerCase()) {\n");
        sb.append("            case \"due_date\":\n");
        sb.append("                Collections.sort(sorted, Comparator.comparing(Todo::getDueDate));\n");
        sb.append("                break;\n");
        sb.append("            case \"priority\":\n");
        sb.append("                Collections.sort(sorted, Comparator.comparing(Todo::getPriority));\n");
        sb.append("                break;\n");
        sb.append("            case \"created_date\":\n");
        sb.append("                Collections.sort(sorted, Comparator.comparing(Todo::getCreatedAt));\n");
        sb.append("                break;\n");
        sb.append("            case \"title\":\n");
        sb.append("                Collections.sort(sorted, Comparator.comparing(Todo::getTitle));\n");
        sb.append("                break;\n");
        sb.append("            default:\n");
        sb.append("                Collections.sort(sorted, Comparator.comparing(Todo::getCreatedAt));\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        if (!ascending) {\n");
        sb.append("            Collections.reverse(sorted);\n");
        sb.append("        }\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        return sorted;\n");
        sb.append("    }\n");
        sb.append("\n");
    }
}
