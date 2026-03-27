package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.EnhancedRandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class NotepadModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;

    // 笔记类型
    private static final String[] NOTE_TYPES = {
        "text", "checklist", "voice", "image", "mixed"
    };

    // 排序方式
    private static final String[] SORT_TYPES = {
        "date", "title", "modified", "custom"
    };

    // 笔记优先级
    private static final String[] PRIORITIES = {
        "high", "medium", "low"
    };

    // 笔记状态
    private static final String[] NOTE_STATUSES = {
        "active", "completed", "archived", "deleted"
    };

    public NotepadModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成记事本模块");

        // 获取当前UI风格和异步处理方式
        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成记事本模块
        generateNotepadModule(uiStyle, asyncHandler);
    }

    private void generateNotepadModule(String uiStyle, String asyncHandler) throws Exception {
        // 生成笔记管理器
        generateNoteManager(uiStyle, asyncHandler);

        // 生成笔记工具类
        generateNoteUtils(uiStyle, asyncHandler);

        // 生成笔记搜索器
        generateNoteSearcher(uiStyle, asyncHandler);
    }

    private void generateNoteManager(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Manager");
        String instanceName = EnhancedRandomUtils.generateVariableName("Instance");
        String contextName = EnhancedRandomUtils.generateObjectName();
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String callbackName = EnhancedRandomUtils.generateClassName("Callback");
        String callbackVarName = EnhancedRandomUtils.generateVariableName("Callback");
        String notesName = EnhancedRandomUtils.generateCollectionName();
        String sortBy = EnhancedRandomUtils.generateStringName();
        String sortAscending = EnhancedRandomUtils.generateBooleanName();

        // 使用随机值
        int maxNotes = EnhancedRandomUtils.generateIntRange(100, 1000)[0];
        String defaultSortBy = SORT_TYPES[EnhancedRandomUtils.between(0, SORT_TYPES.length - 1)];
        boolean defaultSortAscending = EnhancedRandomUtils.nextBoolean();

        sb.append("package ").append(packageName).append(".notepad;\n");

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
        sb.append("    private static final int MAX_NOTES = ").append(maxNotes).append(";\n");
        sb.append("\n");

        // 回调接口
        sb.append("    public interface ").append(callbackName).append(" {\n");
        sb.append("        void onNoteAdded(Note note);\n");
        sb.append("        void onNoteUpdated(Note note);\n");
        sb.append("        void onNoteDeleted(Note note);\n");
        sb.append("        void onError(String error);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 成员变量
        sb.append("    private final Context ").append(contextName).append(";\n");
        sb.append("    private final ").append(callbackName).append(" ").append(callbackVarName).append(";\n");
        sb.append("    private final List<Note> ").append(notesName).append(";\n");
        sb.append("    private String ").append(sortBy).append(";\n");
        sb.append("    private boolean ").append(sortAscending).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    public ").append(className).append("(Context ").append(contextName).append(", ").append(callbackName).append(" ").append(callbackVarName).append(") {\n");
        sb.append("        this.").append(contextName).append(" = ").append(contextName).append(".getApplicationContext();\n");
        sb.append("        this.").append(callbackVarName).append(" = ").append(callbackVarName).append(";\n");
        sb.append("        this.").append(notesName).append(" = new ArrayList<>();\n");
        sb.append("        this.").append(sortBy).append(" = \"").append(defaultSortBy).append("\";\n");
        sb.append("        this.").append(sortAscending).append(" = ").append(defaultSortAscending).append(";\n");
        sb.append("\n");
        sb.append("        // 加载笔记\n");
        sb.append("        loadNotes();\n");
        sb.append("    }\n");
        sb.append("\n");

        // 添加笔记方法
        generateAddNoteMethod(sb, className, notesName, sortBy, sortAscending, maxNotes, tagName, callbackVarName);

        // 更新笔记方法
        generateUpdateNoteMethod(sb, className, notesName, sortBy, sortAscending, tagName, callbackVarName);

        // 删除笔记方法
        generateDeleteNoteMethod(sb, className, notesName, sortBy, sortAscending, tagName, callbackVarName);

        // 获取所有笔记方法
        generateGetAllNotesMethod(sb, className, notesName);

        // 搜索笔记方法
        generateSearchNotesMethod(sb, className, notesName);

        // 排序笔记方法
        generateSortNotesMethod(sb, className, notesName, sortBy, sortAscending);

        // 清空笔记方法
        generateClearNotesMethod(sb, className, notesName, tagName);

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("notepad");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateAddNoteMethod(StringBuilder sb, String className, String notesName,
                                          String sortBy, String sortAscending, int maxNotes,
                                          String tagName, String callbackVarName) {
        String methodName = EnhancedRandomUtils.generateMethodName("AddNote");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Note added");
        String logErrorMessage = EnhancedRandomUtils.generateLogMessage("Error adding note");

        sb.append("    public void ").append(methodName).append("(Note note) {\n");
        sb.append("        if (note == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 设置创建时间\n");
        sb.append("        note.setCreatedAt(new Date());\n");
        sb.append("        note.setUpdatedAt(new Date());\n");
        sb.append("\n");
        sb.append("        // 添加到列表\n");
        sb.append("        ").append(notesName).append(".add(note);\n");
        sb.append("\n");
        sb.append("        // 限制笔记数量\n");
        sb.append("        while (").append(notesName).append(".size() > MAX_NOTES) {\n");
        sb.append("            ").append(notesName).append(".remove(0);\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 排序笔记\n");
        sb.append("        sortNotes();\n");
        sb.append("\n");
        sb.append("        // 保存笔记\n");
        sb.append("        saveNotes();\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + note.getTitle());\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onNoteAdded(note);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateUpdateNoteMethod(StringBuilder sb, String className, String notesName,
                                            String sortBy, String sortAscending,
                                            String tagName, String callbackVarName) {
        String methodName = EnhancedRandomUtils.generateMethodName("UpdateNote");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Note updated");
        String logErrorMessage = EnhancedRandomUtils.generateLogMessage("Error updating note");

        sb.append("    public void ").append(methodName).append("(Note note) {\n");
        sb.append("        if (note == null || !").append(notesName).append(".contains(note)) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 设置更新时间\n");
        sb.append("        note.setUpdatedAt(new Date());\n");
        sb.append("\n");
        sb.append("        // 排序笔记\n");
        sb.append("        sortNotes();\n");
        sb.append("\n");
        sb.append("        // 保存笔记\n");
        sb.append("        saveNotes();\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + note.getTitle());\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onNoteUpdated(note);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateDeleteNoteMethod(StringBuilder sb, String className, String notesName,
                                            String sortBy, String sortAscending,
                                            String tagName, String callbackVarName) {
        String methodName = EnhancedRandomUtils.generateMethodName("DeleteNote");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Note deleted");
        String logErrorMessage = EnhancedRandomUtils.generateLogMessage("Error deleting note");

        sb.append("    public void ").append(methodName).append("(Note note) {\n");
        sb.append("        if (note == null || !").append(notesName).append(".contains(note)) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 从列表中移除\n");
        sb.append("        ").append(notesName).append(".remove(note);\n");
        sb.append("\n");
        sb.append("        // 保存笔记\n");
        sb.append("        saveNotes();\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + note.getTitle());\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onNoteDeleted(note);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetAllNotesMethod(StringBuilder sb, String className, String notesName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetAllNotes");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Retrieved all notes");

        sb.append("    public List<Note> ").append(methodName).append("() {\n");
        sb.append("        return new ArrayList<>(").append(notesName).append(");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateSearchNotesMethod(StringBuilder sb, String className, String notesName) {
        String methodName = EnhancedRandomUtils.generateMethodName("SearchNotes");
        String queryName = EnhancedRandomUtils.generateStringName();
        String resultsName = EnhancedRandomUtils.generateCollectionName();
        String logMessage = EnhancedRandomUtils.generateLogMessage("Searched notes");

        sb.append("    public List<Note> ").append(methodName).append("(String ").append(queryName).append(") {\n");
        sb.append("        List<Note> ").append(resultsName).append(" = new ArrayList<>();\n");
        sb.append("        if (").append(queryName).append(" == null || ").append(queryName).append(".isEmpty()) {\n");
        sb.append("            return ").append(resultsName).append(";\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        String ").append(queryName).append("Lower = ").append(queryName).append(".toLowerCase();\n");
        sb.append("        for (Note note : ").append(notesName).append(") {\n");
        sb.append("            if (note.getTitle().toLowerCase().contains(").append(queryName).append("Lower) ||\n");
        sb.append("                note.getContent().toLowerCase().contains(").append(queryName).append("Lower)) {\n");
        sb.append("                ").append(resultsName).append(".add(note);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + ").append(queryName).append(" - \" + ").append(resultsName).append(".size() + \" results\");\n");
        sb.append("        return ").append(resultsName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateSortNotesMethod(StringBuilder sb, String className, String notesName,
                                           String sortBy, String sortAscending) {
        String methodName = EnhancedRandomUtils.generateMethodName("SortNotes");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Sorted notes");

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        Collections.sort(").append(notesName).append(", new Comparator<Note>() {\n");
        sb.append("            @Override\n");
        sb.append("            public int compare(Note n1, Note n2) {\n");
        sb.append("                int result = 0;\n");
        sb.append("                switch (").append(sortBy).append(") {\n");
        sb.append("                    case \"date\":\n");
        sb.append("                        result = n1.getCreatedAt().compareTo(n2.getCreatedAt());\n");
        sb.append("                        break;\n");
        sb.append("                    case \"title\":\n");
        sb.append("                        result = n1.getTitle().compareTo(n2.getTitle());\n");
        sb.append("                        break;\n");
        sb.append("                    case \"modified\":\n");
        sb.append("                        result = n1.getUpdatedAt().compareTo(n2.getUpdatedAt());\n");
        sb.append("                        break;\n");
        sb.append("                    default:\n");
        sb.append("                        result = 0;\n");
        sb.append("                        break;\n");
        sb.append("                }\n");
        sb.append("                return ").append(sortAscending).append(" ? result : -result;\n");
        sb.append("            }\n");
        sb.append("        });\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateClearNotesMethod(StringBuilder sb, String className, String notesName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ClearNotes");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Cleared notes");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        ").append(notesName).append(".clear();\n");
        sb.append("        saveNotes();\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateNoteUtils(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Utils");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String noteTypeName = EnhancedRandomUtils.generateClassName("Note");
        String priorityName = EnhancedRandomUtils.generateStringName();
        String statusName = EnhancedRandomUtils.generateStringName();

        sb.append("package ").append(packageName).append(".notepad;\n");

        // 导入
        sb.append("import android.util.Log;\n");
        sb.append("import java.text.SimpleDateFormat;\n");
        sb.append("import java.util.Date;\n");
        sb.append("import java.util.Locale;\n");

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

        // 生成随机工具方法
        generateNoteUtilityMethods(sb, className, tagName, noteTypeName, priorityName, statusName);

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("notepad");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateNoteUtilityMethods(StringBuilder sb, String className, String tagName,
                                             String noteTypeName, String priorityName, String statusName) {
        // 创建笔记方法
        generateCreateNoteMethod(sb, className, noteTypeName);

        // 验证笔记方法
        generateValidateNoteMethod(sb, className, noteTypeName, priorityName, statusName);

        // 格式化笔记方法
        generateFormatNoteMethod(sb, className, noteTypeName);

        // 获取笔记摘要方法
        generateGetNoteSummaryMethod(sb, className, noteTypeName);

        // 比较笔记方法
        generateCompareNotesMethod(sb, className, noteTypeName);
    }

    private void generateCreateNoteMethod(StringBuilder sb, String className, String noteTypeName) {
        String methodName = EnhancedRandomUtils.generateMethodName("CreateNote");
        String titleName = EnhancedRandomUtils.generateStringName();
        String contentName = EnhancedRandomUtils.generateStringName();
        String logMessage = EnhancedRandomUtils.generateLogMessage("Created note");

        sb.append("    public static ").append(noteTypeName).append(" ").append(methodName).append("(String ").append(titleName).append(", String ").append(contentName).append(") {\n");
        sb.append("        ").append(noteTypeName).append(" note = new ").append(noteTypeName).append("();\n");
        sb.append("        note.setTitle(").append(titleName).append(");\n");
        sb.append("        note.setContent(").append(contentName).append(");\n");
        sb.append("        note.setCreatedAt(new Date());\n");
        sb.append("        note.setUpdatedAt(new Date());\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + ").append(titleName).append(");\n");
        sb.append("        return note;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateValidateNoteMethod(StringBuilder sb, String className, String noteTypeName,
                                               String priorityName, String statusName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ValidateNote");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Validated note");

        sb.append("    public static boolean ").append(methodName).append("(").append(noteTypeName).append(" note) {\n");
        sb.append("        if (note == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        boolean isValid = true;\n");
        sb.append("\n");
        sb.append("        // 检查标题\n");
        sb.append("        if (note.getTitle() == null || note.getTitle().isEmpty()) {\n");
        sb.append("            isValid = false;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 检查内容\n");
        sb.append("        if (note.getContent() == null || note.getContent().isEmpty()) {\n");
        sb.append("            isValid = false;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 检查优先级\n");
        sb.append("        if (note.get").append(priorityName).append("() == null) {\n");
        sb.append("            isValid = false;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // 检查状态\n");
        sb.append("        if (note.get").append(statusName).append("() == null) {\n");
        sb.append("            isValid = false;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + isValid);\n");
        sb.append("        return isValid;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateFormatNoteMethod(StringBuilder sb, String className, String noteTypeName) {
        String methodName = EnhancedRandomUtils.generateMethodName("FormatNote");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Formatted note");

        sb.append("    public static String ").append(methodName).append("(").append(noteTypeName).append(" note) {\n");
        sb.append("        if (note == null) {\n");
        sb.append("            return \"\";\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        SimpleDateFormat sdf = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\", Locale.getDefault());\n");
        sb.append("        String createdAt = sdf.format(note.getCreatedAt());\n");
        sb.append("        String updatedAt = sdf.format(note.getUpdatedAt());\n");
        sb.append("        return \"Title: \" + note.getTitle() + \"\n\" +\n");
        sb.append("               \"Content: \" + note.getContent() + \"\n\" +\n");
        sb.append("               \"Created: \" + createdAt + \"\n\" +\n");
        sb.append("               \"Updated: \" + updatedAt;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetNoteSummaryMethod(StringBuilder sb, String className, String noteTypeName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetNoteSummary");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Got note summary");

        sb.append("    public static String ").append(methodName).append("(").append(noteTypeName).append(" note) {\n");
        sb.append("        if (note == null) {\n");
        sb.append("            return \"\";\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        String title = note.getTitle();\n");
        sb.append("        String content = note.getContent();\n");
        sb.append("        int titleLength = title != null ? title.length() : 0;\n");
        sb.append("        int contentLength = content != null ? content.length() : 0;\n");
        sb.append("        String summary = \"Note: \" + (titleLength > 50 ? title.substring(0, 50) + \"...\" : title) + \"\n\";\n");
        sb.append("        summary += \"Content: \" + (contentLength > 100 ? content.substring(0, 100) + \"...\" : content);\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + summary);\n");
        sb.append("        return summary;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateCompareNotesMethod(StringBuilder sb, String className, String noteTypeName) {
        String methodName = EnhancedRandomUtils.generateMethodName("CompareNotes");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Compared notes");

        sb.append("    public static int ").append(methodName).append("(").append(noteTypeName).append(" n1, ").append(noteTypeName).append(" n2) {\n");
        sb.append("        if (n1 == null || n2 == null) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        int result = n1.getCreatedAt().compareTo(n2.getCreatedAt());\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + result);\n");
        sb.append("        return result;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateNoteSearcher(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Searcher");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String notesName = EnhancedRandomUtils.generateCollectionName();
        String resultsName = EnhancedRandomUtils.generateCollectionName();
        String queryName = EnhancedRandomUtils.generateStringName();
        String logMessage = EnhancedRandomUtils.generateLogMessage("Searched notes");

        sb.append("package ").append(packageName).append(".notepad;\n");

        // 导入
        sb.append("import android.util.Log;\n");
        sb.append("import java.util.ArrayList;\n");
        sb.append("import java.util.List;\n");

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

        // 搜索方法
        sb.append("    public static List<Note> search(List<Note> ").append(notesName).append(", String ").append(queryName).append(") {\n");
        sb.append("        List<Note> ").append(resultsName).append(" = new ArrayList<>();\n");
        sb.append("        if (").append(queryName).append(" == null || ").append(queryName).append(".isEmpty()) {\n");
        sb.append("            return ").append(resultsName).append(";\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        String ").append(queryName).append("Lower = ").append(queryName).append(".toLowerCase();\n");
        sb.append("        for (Note note : ").append(notesName).append(") {\n");
        sb.append("            if (note.getTitle().toLowerCase().contains(").append(queryName).append("Lower) ||\n");
        sb.append("                note.getContent().toLowerCase().contains(").append(queryName).append("Lower)) {\n");
        sb.append("                ").append(resultsName).append(".add(note);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + ").append(queryName).append(" - \" + ").append(resultsName).append(".size() + \" results\");\n");
        sb.append("        return ").append(resultsName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("notepad");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }
}
