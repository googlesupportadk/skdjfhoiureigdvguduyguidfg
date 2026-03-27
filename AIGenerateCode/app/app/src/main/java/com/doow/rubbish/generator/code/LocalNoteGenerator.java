package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalNoteGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] NOTE_TYPES = {
            "TextNote", "AudioNote", "ImageNote", "VideoNote", "MixedNote",
            "ChecklistNote", "DrawingNote", "VoiceNote", "DocumentNote", "LinkNote",
            "LocationNote", "ReminderNote", "TaggedNote", "EncryptedNote", "SharedNote"
    };

    private static final String[] PRIORITY_LEVELS = {
            "LOW", "MEDIUM", "HIGH", "URGENT",
            "CRITICAL", "IMPORTANT", "NORMAL", "MINOR", "MAJOR"
    };

    // 功能标志 - 确保所有字段和方法都会被使用
    private boolean useEncryption;
    private boolean useAttachments;
    private boolean useReminders;
    private boolean useTags;
    private boolean useCollaboration;
    private boolean useVersioning;
    private boolean useSearch;
    private boolean useSync;

    public LocalNoteGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        initializeFeatureFlags();
    }

    /**
     * 初始化功能标志，确保随机性和多样性
     */
    private void initializeFeatureFlags() {
        useEncryption = RandomUtils.randomBoolean();
        useAttachments = RandomUtils.randomBoolean();
        useReminders = RandomUtils.randomBoolean();
        useTags = RandomUtils.randomBoolean();
        useCollaboration = RandomUtils.randomBoolean();
        useVersioning = RandomUtils.randomBoolean();
        useSearch = RandomUtils.randomBoolean();
        useSync = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成笔记类");

        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Note");
            generateNoteClass(className, uiStyle, asyncHandler);
        }
    }

    private void generateNoteClass(String className, String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("note"));

        // 生成基础导入
        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.*"));
        sb.append(generateImportStatement("java.text.SimpleDateFormat"));
        sb.append(generateImportStatement("java.util.concurrent.ConcurrentHashMap"));

        // 根据功能标志添加条件导入
        if (useEncryption) {
            sb.append(generateImportStatement("javax.crypto.Cipher"));
            sb.append(generateImportStatement("javax.crypto.SecretKey"));
            sb.append(generateImportStatement("javax.crypto.spec.SecretKeySpec"));
            sb.append(generateImportStatement("java.util.Base64"));
        }

        if (useAttachments) {
            sb.append(generateImportStatement("android.net.Uri"));
            sb.append(generateImportStatement("java.io.File"));
        }

        if (useReminders) {
            sb.append(generateImportStatement("android.app.AlarmManager"));
            sb.append(generateImportStatement("android.app.PendingIntent"));
            sb.append(generateImportStatement("android.content.Intent"));
        }

        if (useSync) {
            if (asyncHandler.contains("coroutines")) {
                sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
                sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
            } else if (asyncHandler.contains("rxjava")) {
                sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
                sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
            } else {
                sb.append(generateImportStatement("android.os.Handler"));
                sb.append(generateImportStatement("android.os.Looper"));
            }
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 随机选择笔记类型和优先级
        String noteType = NOTE_TYPES[RandomUtils.between(0, NOTE_TYPES.length - 1)];
        String priorityLevel = PRIORITY_LEVELS[RandomUtils.between(0, PRIORITY_LEVELS.length - 1)];

        // 生成类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成常量字段
        String tagVarName = RandomUtils.generateWord(6);
        String noteTypeVarName = RandomUtils.generateWord(6);
        String priorityLevelVarName = RandomUtils.generateWord(6);
        String dateFormatVarName = RandomUtils.generateWord(6);
        String maxVersionsVarName = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVarName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(noteTypeVarName).append(" = \"").append(noteType).append("\";\n");
        sb.append("    private static final String ").append(priorityLevelVarName).append(" = \"").append(priorityLevel).append("\";\n");
        sb.append("    private static final String ").append(dateFormatVarName).append(" = \"yyyy-MM-dd HH:mm:ss\";\n");
        sb.append("    private static final int ").append(maxVersionsVarName).append(" = ").append(RandomUtils.between(5, 10)).append(";\n\n");

        // 生成实例字段
        String contextVarName = RandomUtils.generateWord(6);
        String titleVarName = RandomUtils.generateWord(6);
        String contentVarName = RandomUtils.generateWord(6);
        String createdAtVarName = RandomUtils.generateWord(6);
        String updatedAtVarName = RandomUtils.generateWord(6);
        String pinnedVarName = RandomUtils.generateWord(6);
        String archivedVarName = RandomUtils.generateWord(6);
        String encryptionKeyVarName = RandomUtils.generateWord(6);
        String attachmentsVarName = RandomUtils.generateWord(6);
        String remindersVarName = RandomUtils.generateWord(6);
        String tagsVarName = RandomUtils.generateWord(6);
        String collaboratorsVarName = RandomUtils.generateWord(6);
        String versionsVarName = RandomUtils.generateWord(6);
        String searchIndexVarName = RandomUtils.generateWord(6);
        String syncStatusVarName = RandomUtils.generateWord(6);
        String syncHandlerVarName = RandomUtils.generateWord(6);

        sb.append("    private Context ").append(contextVarName).append(";\n");
        sb.append("    private String ").append(titleVarName).append(";\n");
        sb.append("    private String ").append(contentVarName).append(";\n");
        sb.append("    private Date ").append(createdAtVarName).append(";\n");
        sb.append("    private Date ").append(updatedAtVarName).append(";\n");
        sb.append("    private boolean ").append(pinnedVarName).append(";\n");
        sb.append("    private boolean ").append(archivedVarName).append(";\n");

        // 根据功能标志添加条件字段
        if (useEncryption) {
            sb.append("    private SecretKey ").append(encryptionKeyVarName).append(";\n");
        }

        if (useAttachments) {
            sb.append("    private List<Uri> ").append(attachmentsVarName).append(";\n");
        }

        if (useReminders) {
            sb.append("    private List<Date> ").append(remindersVarName).append(";\n");
        }

        if (useTags) {
            sb.append("    private Set<String> ").append(tagsVarName).append(";\n");
        }

        if (useCollaboration) {
            sb.append("    private Set<String> ").append(collaboratorsVarName).append(";\n");
        }

        if (useVersioning) {
            sb.append("    private List<Map<String, Object>> ").append(versionsVarName).append(";\n");
        }

        if (useSearch) {
            sb.append("    private Map<String, Integer> ").append(searchIndexVarName).append(";\n");
        }

        if (useSync) {
            sb.append("    private String ").append(syncStatusVarName).append(";\n");
            if (!asyncHandler.contains("coroutines") && !asyncHandler.contains("rxjava")) {
                sb.append("    private Handler ").append(syncHandlerVarName).append(";\n");
            }
        }

        sb.append("\n");

        // 生成构造函数
        sb.append("    public ").append(className).append("(Context ").append(contextVarName).append(") {\n");
        sb.append("        initialize").append(className).append("(").append(contextVarName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    private void initialize").append(className).append("(Context ").append(contextVarName).append(") {\n");
        sb.append("        this.").append(contextVarName).append(" = ").append(contextVarName).append(".getApplicationContext();\n");
        sb.append("        this.").append(createdAtVarName).append(" = new Date();\n");
        sb.append("        this.").append(updatedAtVarName).append(" = new Date();\n");
        sb.append("        this.").append(pinnedVarName).append(" = false;\n");
        sb.append("        this.").append(archivedVarName).append(" = false;\n");

        if (useEncryption) {
            sb.append("        this.").append(encryptionKeyVarName).append(" = generateEncryptionKey();\n");
        }

        if (useAttachments) {
            sb.append("        this.").append(attachmentsVarName).append(" = new ArrayList<>();\n");
        }

        if (useReminders) {
            sb.append("        this.").append(remindersVarName).append(" = new ArrayList<>();\n");
        }

        if (useTags) {
            sb.append("        this.").append(tagsVarName).append(" = new HashSet<>();\n");
        }

        if (useCollaboration) {
            sb.append("        this.").append(collaboratorsVarName).append(" = new HashSet<>();\n");
        }

        if (useVersioning) {
            sb.append("        this.").append(versionsVarName).append(" = new ArrayList<>();\n");
        }

        if (useSearch) {
            sb.append("        this.").append(searchIndexVarName).append(" = new ConcurrentHashMap<>();\n");
        }

        if (useSync) {
            sb.append("        this.").append(syncStatusVarName).append(" = \"synced\";\n");
            if (!asyncHandler.contains("coroutines") && !asyncHandler.contains("rxjava")) {
                sb.append("        this.").append(syncHandlerVarName).append(" = new Handler(Looper.getMainLooper());\n");
            }
        }

        sb.append("    }\n\n");

        // 生成基础方法
        generateBasicMethods(sb, className, tagVarName, noteTypeVarName, priorityLevelVarName,
                titleVarName, contentVarName, createdAtVarName, updatedAtVarName,
                pinnedVarName, archivedVarName, dateFormatVarName);

        // 根据功能标志添加条件方法
        if (useEncryption) {
            generateEncryptionMethods(sb, className, encryptionKeyVarName, tagVarName);
        }

        if (useAttachments) {
            generateAttachmentMethods(sb, className, attachmentsVarName, tagVarName);
        }

        if (useReminders) {
            generateReminderMethods(sb, className, remindersVarName, tagVarName, contextVarName);
        }

        if (useTags) {
            generateTagMethods(sb, className, tagsVarName, tagVarName);
        }

        if (useCollaboration) {
            generateCollaborationMethods(sb, className, collaboratorsVarName, tagVarName);
        }

        if (useVersioning) {
            generateVersioningMethods(sb, className, versionsVarName, maxVersionsVarName,
                    titleVarName, contentVarName, tagVarName);
        }

        if (useSearch) {
            generateSearchMethods(sb, className, searchIndexVarName, titleVarName, contentVarName, tagVarName);
        }

        if (useSync) {
            generateSyncMethods(sb, className, syncStatusVarName, syncHandlerVarName, tagVarName, asyncHandler);
        }

        // 生成使用所有字段和方法的示例方法
        generateExampleUsageMethod(sb, className, contextVarName, titleVarName, contentVarName,
                createdAtVarName, updatedAtVarName, pinnedVarName, archivedVarName,
                encryptionKeyVarName, attachmentsVarName, remindersVarName,
                tagsVarName, collaboratorsVarName, versionsVarName,
                searchIndexVarName, syncStatusVarName, tagVarName);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "note");
    }

    /**
     * 生成基础方法
     */
    private void generateBasicMethods(StringBuilder sb, String className, String tagVarName,
                                      String noteTypeVarName, String priorityLevelVarName,
                                      String titleVarName, String contentVarName,
                                      String createdAtVarName, String updatedAtVarName,
                                      String pinnedVarName, String archivedVarName,
                                      String dateFormatVarName) {
        String setTitleMethodName = RandomUtils.generateWord(6);
        String setContentMethodName = RandomUtils.generateWord(6);
        String getTitleMethodName = RandomUtils.generateWord(6);
        String getContentMethodName = RandomUtils.generateWord(6);
        String getCreatedAtMethodName = RandomUtils.generateWord(6);
        String getUpdatedAtMethodName = RandomUtils.generateWord(6);
        String setPinnedMethodName = RandomUtils.generateWord(6);
        String setArchivedMethodName = RandomUtils.generateWord(6);
        String isPinnedMethodName = RandomUtils.generateWord(6);
        String isArchivedMethodName = RandomUtils.generateWord(6);
        String clearMethodName = RandomUtils.generateWord(6);
        String formatDateMethodName = RandomUtils.generateWord(6);
        String getNoteTypeMethodName = RandomUtils.generateWord(6);
        String getPriorityLevelMethodName = RandomUtils.generateWord(6);

        // 设置标题方法
        sb.append("    public void ").append(setTitleMethodName).append("(String ")
                .append(titleVarName).append(") {\n");
        sb.append("        this.").append(titleVarName).append(" = ").append(titleVarName).append(";\n");
        sb.append("        this.").append(updatedAtVarName).append(" = new Date();\n");
        sb.append("        Log.d(").append(tagVarName).append(", \"Title set: \" + ")
                .append(titleVarName).append(");\n");
        sb.append("    }\n\n");

        // 设置内容方法
        sb.append("    public void ").append(setContentMethodName).append("(String ")
                .append(contentVarName).append(") {\n");
        sb.append("        this.").append(contentVarName).append(" = ").append(contentVarName).append(";\n");
        sb.append("        this.").append(updatedAtVarName).append(" = new Date();\n");
        sb.append("    }\n\n");

        // 获取标题方法
        sb.append("    public String ").append(getTitleMethodName).append("() {\n");
        sb.append("        return ").append(titleVarName).append(";\n");
        sb.append("    }\n\n");

        // 获取内容方法
        sb.append("    public String ").append(getContentMethodName).append("() {\n");
        sb.append("        return ").append(contentVarName).append(";\n");
        sb.append("    }\n\n");

        // 获取创建时间方法
        sb.append("    public Date ").append(getCreatedAtMethodName).append("() {\n");
        sb.append("        return ").append(createdAtVarName).append(";\n");
        sb.append("    }\n\n");

        // 获取更新时间方法
        sb.append("    public Date ").append(getUpdatedAtMethodName).append("() {\n");
        sb.append("        return ").append(updatedAtVarName).append(";\n");
        sb.append("    }\n\n");

        // 设置置顶方法
        sb.append("    public void ").append(setPinnedMethodName).append("(boolean ")
                .append(pinnedVarName).append(") {\n");
        sb.append("        this.").append(pinnedVarName).append(" = ").append(pinnedVarName).append(";\n");
        sb.append("        this.").append(updatedAtVarName).append(" = new Date();\n");
        sb.append("    }\n\n");

        // 设置归档方法
        sb.append("    public void ").append(setArchivedMethodName).append("(boolean ")
                .append(archivedVarName).append(") {\n");
        sb.append("        this.").append(archivedVarName).append(" = ").append(archivedVarName).append(";\n");
        sb.append("        this.").append(updatedAtVarName).append(" = new Date();\n");
        sb.append("    }\n\n");

        // 获取置顶状态方法
        sb.append("    public boolean ").append(isPinnedMethodName).append("() {\n");
        sb.append("        return ").append(pinnedVarName).append(";\n");
        sb.append("    }\n\n");

        // 获取归档状态方法
        sb.append("    public boolean ").append(isArchivedMethodName).append("() {\n");
        sb.append("        return ").append(archivedVarName).append(";\n");
        sb.append("    }\n\n");

        // 清空笔记方法
        sb.append("    public void ").append(clearMethodName).append("() {\n");
        sb.append("        this.").append(titleVarName).append(" = \"\";\n");
        sb.append("        this.").append(contentVarName).append(" = \"\";\n");
        sb.append("        this.").append(updatedAtVarName).append(" = new Date();\n");
        sb.append("        Log.d(").append(tagVarName).append(", \"Note cleared\");\n");
        sb.append("    }\n\n");

        // 格式化日期方法
        sb.append("    private String ").append(formatDateMethodName).append("(Date date) {\n");
        sb.append("        SimpleDateFormat sdf = new SimpleDateFormat(").append(dateFormatVarName).append(");\n");
        sb.append("        return sdf.format(date);\n");
        sb.append("    }\n\n");

        // 获取笔记类型方法
        sb.append("    public String ").append(getNoteTypeMethodName).append("() {\n");
        sb.append("        return ").append(noteTypeVarName).append(";\n");
        sb.append("    }\n\n");

        // 获取优先级方法
        sb.append("    public String ").append(getPriorityLevelMethodName).append("() {\n");
        sb.append("        return ").append(priorityLevelVarName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成加密方法
     */
    private void generateEncryptionMethods(StringBuilder sb, String className,
                                           String encryptionKeyVarName, String tagVarName) {
        String encryptMethodName = RandomUtils.generateWord(6);
        String decryptMethodName = RandomUtils.generateWord(6);
        String generateKeyMethodName = RandomUtils.generateWord(6);
        String dataParamName = RandomUtils.generateWord(6);

        // 加密方法
        sb.append("    public String ").append(encryptMethodName).append("(String ")
                .append(dataParamName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            Cipher cipher = Cipher.getInstance(\"AES/ECB/PKCS5Padding\");\n");
        sb.append("            cipher.init(Cipher.ENCRYPT_MODE, ").append(encryptionKeyVarName).append(");\n");
        sb.append("            byte[] encrypted = cipher.doFinal(").append(dataParamName).append(".getBytes());\n");
        sb.append("            return Base64.getEncoder().encodeToString(encrypted);\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Encryption error\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 解密方法
        sb.append("    public String ").append(decryptMethodName).append("(String ")
                .append(dataParamName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            Cipher cipher = Cipher.getInstance(\"AES/ECB/PKCS5Padding\");\n");
        sb.append("            cipher.init(Cipher.DECRYPT_MODE, ").append(encryptionKeyVarName).append(");\n");
        sb.append("            byte[] decoded = Base64.getDecoder().decode(").append(dataParamName).append(");\n");
        sb.append("            byte[] decrypted = cipher.doFinal(decoded);\n");
        sb.append("            return new String(decrypted);\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Decryption error\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成密钥方法
        sb.append("    private SecretKey ").append(generateKeyMethodName).append("() {\n");
        sb.append("        String key = RandomUtils.generateName(\"key\");\n");
        sb.append("        byte[] keyBytes = key.getBytes();\n");
        sb.append("        return new SecretKeySpec(keyBytes, \"AES\");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成附件方法
     */
    private void generateAttachmentMethods(StringBuilder sb, String className,
                                           String attachmentsVarName, String tagVarName) {
        String addAttachmentMethodName = RandomUtils.generateWord(6);
        String removeAttachmentMethodName = RandomUtils.generateWord(6);
        String getAttachmentsMethodName = RandomUtils.generateWord(6);
        String clearAttachmentsMethodName = RandomUtils.generateWord(6);
        String attachmentParamName = RandomUtils.generateWord(6);

        // 添加附件方法
        sb.append("    public void ").append(addAttachmentMethodName).append("(Uri ")
                .append(attachmentParamName).append(") {\n");
        sb.append("        if (").append(attachmentParamName).append(" != null) {\n");
        sb.append("            ").append(attachmentsVarName).append(".add(").append(attachmentParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 移除附件方法
        sb.append("    public void ").append(removeAttachmentMethodName).append("(Uri ")
                .append(attachmentParamName).append(") {\n");
        sb.append("        if (").append(attachmentParamName).append(" != null) {\n");
        sb.append("            ").append(attachmentsVarName).append(".remove(").append(attachmentParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 获取所有附件方法
        sb.append("    public List<Uri> ").append(getAttachmentsMethodName).append("() {\n");
        sb.append("        return new ArrayList<>(").append(attachmentsVarName).append(");\n");
        sb.append("    }\n\n");

        // 清空附件方法
        sb.append("    public void ").append(clearAttachmentsMethodName).append("() {\n");
        sb.append("        ").append(attachmentsVarName).append(".clear();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成提醒方法
     */
    private void generateReminderMethods(StringBuilder sb, String className,
                                         String remindersVarName, String tagVarName,
                                         String contextVarName) {
        String addReminderMethodName = RandomUtils.generateWord(6);
        String removeReminderMethodName = RandomUtils.generateWord(6);
        String getRemindersMethodName = RandomUtils.generateWord(6);
        String clearRemindersMethodName = RandomUtils.generateWord(6);
        String reminderParamName = RandomUtils.generateWord(6);

        // 添加提醒方法
        sb.append("    public void ").append(addReminderMethodName).append("(Date ")
                .append(reminderParamName).append(") {\n");
        sb.append("        if (").append(reminderParamName).append(" != null) {\n");
        sb.append("            ").append(remindersVarName).append(".add(").append(reminderParamName).append(");\n");
        sb.append("            scheduleReminder(").append(reminderParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 移除提醒方法
        sb.append("    public void ").append(removeReminderMethodName).append("(Date ")
                .append(reminderParamName).append(") {\n");
        sb.append("        if (").append(reminderParamName).append(" != null) {\n");
        sb.append("            ").append(remindersVarName).append(".remove(").append(reminderParamName).append(");\n");
        sb.append("            cancelReminder(").append(reminderParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 获取所有提醒方法
        sb.append("    public List<Date> ").append(getRemindersMethodName).append("() {\n");
        sb.append("        return new ArrayList<>(").append(remindersVarName).append(");\n");
        sb.append("    }\n\n");

        // 清空提醒方法
        sb.append("    public void ").append(clearRemindersMethodName).append("() {\n");
        sb.append("        for (Date reminder : ").append(remindersVarName).append(") {\n");
        sb.append("            cancelReminder(reminder);\n");
        sb.append("        }\n");
        sb.append("        ").append(remindersVarName).append(".clear();\n");
        sb.append("    }\n\n");

        // 安排提醒方法
        String scheduleReminderMethodName = RandomUtils.generateWord(6);
        sb.append("    private void ").append(scheduleReminderMethodName).append("(Date reminder) {\n");
        sb.append("        AlarmManager alarmManager = (AlarmManager) ").append(contextVarName)
                .append(".getSystemService(Context.ALARM_SERVICE);\n");
        sb.append("        Intent intent = new Intent(\"com.doow.rubbish.NOTE_REMINDER\");\n");
        sb.append("        PendingIntent pendingIntent = PendingIntent.getBroadcast(")
                .append(contextVarName).append(", 0, intent, 0);\n");
        sb.append("        alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getTime(), pendingIntent);\n");
        sb.append("    }\n\n");

        // 取消提醒方法
        String cancelReminderMethodName = RandomUtils.generateWord(6);
        sb.append("    private void ").append(cancelReminderMethodName).append("(Date reminder) {\n");
        sb.append("        AlarmManager alarmManager = (AlarmManager) ").append(contextVarName)
                .append(".getSystemService(Context.ALARM_SERVICE);\n");
        sb.append("        Intent intent = new Intent(\"com.doow.rubbish.NOTE_REMINDER\");\n");
        sb.append("        PendingIntent pendingIntent = PendingIntent.getBroadcast(")
                .append(contextVarName).append(", 0, intent, 0);\n");
        sb.append("        alarmManager.cancel(pendingIntent);\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成标签方法
     */
    private void generateTagMethods(StringBuilder sb, String className,
                                    String tagsVarName, String tagVarName) {
        String addTagMethodName = RandomUtils.generateWord(6);
        String removeTagMethodName = RandomUtils.generateWord(6);
        String getTagsMethodName = RandomUtils.generateWord(6);
        String hasTagMethodName = RandomUtils.generateWord(6);
        String clearTagsMethodName = RandomUtils.generateWord(6);
        String tagParamName = RandomUtils.generateWord(6);

        // 添加标签方法
        sb.append("    public void ").append(addTagMethodName).append("(String ")
                .append(tagParamName).append(") {\n");
        sb.append("        if (").append(tagParamName).append(" != null && !")
                .append(tagParamName).append(".isEmpty()) {\n");
        sb.append("            ").append(tagsVarName).append(".add(").append(tagParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 移除标签方法
        sb.append("    public void ").append(removeTagMethodName).append("(String ")
                .append(tagParamName).append(") {\n");
        sb.append("        if (").append(tagParamName).append(" != null) {\n");
        sb.append("            ").append(tagsVarName).append(".remove(").append(tagParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 获取所有标签方法
        sb.append("    public Set<String> ").append(getTagsMethodName).append("() {\n");
        sb.append("        return new HashSet<>(").append(tagsVarName).append(");\n");
        sb.append("    }\n\n");

        // 检查是否有标签方法
        sb.append("    public boolean ").append(hasTagMethodName).append("(String ")
                .append(tagParamName).append(") {\n");
        sb.append("        return ").append(tagsVarName).append(".contains(").append(tagParamName).append(");\n");
        sb.append("    }\n\n");

        // 清空标签方法
        sb.append("    public void ").append(clearTagsMethodName).append("() {\n");
        sb.append("        ").append(tagsVarName).append(".clear();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成协作方法
     */
    private void generateCollaborationMethods(StringBuilder sb, String className,
                                              String collaboratorsVarName, String tagVarName) {
        String addCollaboratorMethodName = RandomUtils.generateWord(6);
        String removeCollaboratorMethodName = RandomUtils.generateWord(6);
        String getCollaboratorsMethodName = RandomUtils.generateWord(6);
        String hasCollaboratorMethodName = RandomUtils.generateWord(6);
        String clearCollaboratorsMethodName = RandomUtils.generateWord(6);
        String collaboratorParamName = RandomUtils.generateWord(6);

        // 添加协作者方法
        sb.append("    public void ").append(addCollaboratorMethodName).append("(String ")
                .append(collaboratorParamName).append(") {\n");
        sb.append("        if (").append(collaboratorParamName).append(" != null && !")
                .append(collaboratorParamName).append(".isEmpty()) {\n");
        sb.append("            ").append(collaboratorsVarName).append(".add(").append(collaboratorParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 移除协作者方法
        sb.append("    public void ").append(removeCollaboratorMethodName).append("(String ")
                .append(collaboratorParamName).append(") {\n");
        sb.append("        if (").append(collaboratorParamName).append(" != null) {\n");
        sb.append("            ").append(collaboratorsVarName).append(".remove(").append(collaboratorParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 获取所有协作者方法
        sb.append("    public Set<String> ").append(getCollaboratorsMethodName).append("() {\n");
        sb.append("        return new HashSet<>(").append(collaboratorsVarName).append(");\n");
        sb.append("    }\n\n");

        // 检查是否有协作者方法
        sb.append("    public boolean ").append(hasCollaboratorMethodName).append("(String ")
                .append(collaboratorParamName).append(") {\n");
        sb.append("        return ").append(collaboratorsVarName).append(".contains(").append(collaboratorParamName).append(");\n");
        sb.append("    }\n\n");

        // 清空协作者方法
        sb.append("    public void ").append(clearCollaboratorsMethodName).append("() {\n");
        sb.append("        ").append(collaboratorsVarName).append(".clear();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成版本控制方法
     */
    private void generateVersioningMethods(StringBuilder sb, String className,
                                           String versionsVarName, String maxVersionsVarName,
                                           String titleVarName, String contentVarName, String tagVarName) {
        String createVersionMethodName = RandomUtils.generateWord(6);
        String restoreVersionMethodName = RandomUtils.generateWord(6);
        String getVersionsMethodName = RandomUtils.generateWord(6);
        String getVersionMethodName = RandomUtils.generateWord(6);
        String clearVersionsMethodName = RandomUtils.generateWord(6);
        String versionIndexParamName = RandomUtils.generateWord(6);

        // 创建版本方法
        sb.append("    private void ").append(createVersionMethodName).append("() {\n");
        sb.append("        Map<String, Object> version = new HashMap<>();\n");
        sb.append("        version.put(\"title\", ").append(titleVarName).append(");\n");
        sb.append("        version.put(\"content\", ").append(contentVarName).append(");\n");
        sb.append("        version.put(\"timestamp\", new Date());\n");
        sb.append("        ").append(versionsVarName).append(".add(version);\n");
        sb.append("        if (").append(versionsVarName).append(".size() > ")
                .append(maxVersionsVarName).append(") {\n");
        sb.append("            ").append(versionsVarName).append(".remove(0);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 恢复版本方法
        sb.append("    public void ").append(restoreVersionMethodName).append("(int ")
                .append(versionIndexParamName).append(") {\n");
        sb.append("        if (").append(versionIndexParamName).append(" >= 0 && ")
                .append(versionIndexParamName).append(" < ").append(versionsVarName).append(".size()) {\n");
        sb.append("            Map<String, Object> version = ").append(versionsVarName)
                .append(".get(").append(versionIndexParamName).append(");\n");
        sb.append("            ").append(titleVarName).append(" = (String) version.get(\"title\");\n");
        sb.append("            ").append(contentVarName).append(" = (String) version.get(\"content\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 获取所有版本方法
        sb.append("    public List<Map<String, Object>> ").append(getVersionsMethodName).append("() {\n");
        sb.append("        return new ArrayList<>(").append(versionsVarName).append(");\n");
        sb.append("    }\n\n");

        // 获取指定版本方法
        sb.append("    public Map<String, Object> ").append(getVersionMethodName).append("(int ")
                .append(versionIndexParamName).append(") {\n");
        sb.append("        if (").append(versionIndexParamName).append(" >= 0 && ")
                .append(versionIndexParamName).append(" < ").append(versionsVarName).append(".size()) {\n");
        sb.append("            return new HashMap<>(").append(versionsVarName)
                .append(".get(").append(versionIndexParamName).append("));\n");
        sb.append("        }\n");
        sb.append("        return null;\n");
        sb.append("    }\n\n");

        // 清空版本方法
        sb.append("    public void ").append(clearVersionsMethodName).append("() {\n");
        sb.append("        ").append(versionsVarName).append(".clear();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成搜索方法
     */
    private void generateSearchMethods(StringBuilder sb, String className,
                                       String searchIndexVarName, String titleVarName,
                                       String contentVarName, String tagVarName) {
        String buildSearchIndexMethodName = RandomUtils.generateWord(6);
        String searchMethodName = RandomUtils.generateWord(6);
        String queryParamName = RandomUtils.generateWord(6);
        String resultsVarName = RandomUtils.generateWord(6);

        // 构建搜索索引方法
        sb.append("    private void ").append(buildSearchIndexMethodName).append("() {\n");
        sb.append("        ").append(searchIndexVarName).append(".clear();\n");
        sb.append("        if (").append(titleVarName).append(" != null) {\n");
        sb.append("            String[] words = ").append(titleVarName).append(".split(\"\\\\s+\");\n");
        sb.append("            for (String word : words) {\n");
        sb.append("                ").append(searchIndexVarName).append(".put(word.toLowerCase(), ")
                .append(searchIndexVarName).append(".getOrDefault(word.toLowerCase(), 0) + 1);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        if (").append(contentVarName).append(" != null) {\n");
        sb.append("            String[] words = ").append(contentVarName).append(".split(\"\\\\s+\");\n");
        sb.append("            for (String word : words) {\n");
        sb.append("                ").append(searchIndexVarName).append(".put(word.toLowerCase(), ")
                .append(searchIndexVarName).append(".getOrDefault(word.toLowerCase(), 0) + 1);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 搜索方法
        sb.append("    public List<String> ").append(searchMethodName).append("(String ")
                .append(queryParamName).append(") {\n");
        sb.append("        List<String> ").append(resultsVarName).append(" = new ArrayList<>();\n");
        sb.append("        if (").append(queryParamName).append(" != null && !")
                .append(queryParamName).append(".isEmpty()) {\n");
        sb.append("            String[] words = ").append(queryParamName).append(".split(\"\\\\s+\");\n");
        sb.append("            for (String word : words) {\n");
        sb.append("                if (").append(searchIndexVarName).append(".containsKey(word.toLowerCase())) {\n");
        sb.append("                    ").append(resultsVarName).append(".add(word);\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultsVarName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成同步方法
     */
    private void generateSyncMethods(StringBuilder sb, String className,
                                     String syncStatusVarName, String syncHandlerVarName,
                                     String tagVarName, String asyncHandler) {
        String syncMethodName = RandomUtils.generateWord(6);
        String getSyncStatusMethodName = RandomUtils.generateWord(6);
        String setSyncStatusMethodName = RandomUtils.generateWord(6);

        if (asyncHandler.contains("coroutines")) {
            // 使用协程的同步方法
            sb.append("    public void ").append(syncMethodName).append("() {\n");
            sb.append("        CoroutineScope(Dispatchers.IO).launch {\n");
            sb.append("            try {\n");
            sb.append("                // 模拟同步操作\n");
            sb.append("                Thread.sleep(1000);\n");
            sb.append("                ").append(syncStatusVarName).append(" = \"synced\";\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                ").append(syncStatusVarName).append(" = \"sync_failed\";\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            // 使用RxJava的同步方法
            sb.append("    public void ").append(syncMethodName).append("() {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            // 模拟同步操作\n");
            sb.append("            Thread.sleep(1000);\n");
            sb.append("            return true;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .subscribe(\n");
            sb.append("              success -> ").append(syncStatusVarName).append(" = \"synced\",\n");
            sb.append("              error -> ").append(syncStatusVarName).append(" = \"sync_failed\"\n");
            sb.append("          );\n");
            sb.append("    }\n\n");
        } else {
            // 使用Handler的同步方法
            sb.append("    public void ").append(syncMethodName).append("() {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            try {\n");
            sb.append("                // 模拟同步操作\n");
            sb.append("                Thread.sleep(1000);\n");
            sb.append("                ").append(syncHandlerVarName).append(".post(() -> {\n");
            sb.append("                    ").append(syncStatusVarName).append(" = \"synced\";\n");
            sb.append("                });\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                ").append(syncHandlerVarName).append(".post(() -> {\n");
            sb.append("                    ").append(syncStatusVarName).append(" = \"sync_failed\";\n");
            sb.append("                });\n");
            sb.append("            }\n");
            sb.append("        }).start();\n");
            sb.append("    }\n\n");
        }

        // 获取同步状态方法
        sb.append("    public String ").append(getSyncStatusMethodName).append("() {\n");
        sb.append("        return ").append(syncStatusVarName).append(";\n");
        sb.append("    }\n\n");

        // 设置同步状态方法
        sb.append("    public void ").append(setSyncStatusMethodName).append("(String status) {\n");
        sb.append("        this.").append(syncStatusVarName).append(" = status;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成示例使用方法
     */
    private void generateExampleUsageMethod(StringBuilder sb, String className,
                                            String contextVarName, String titleVarName,
                                            String contentVarName, String createdAtVarName,
                                            String updatedAtVarName, String pinnedVarName,
                                            String archivedVarName, String encryptionKeyVarName,
                                            String attachmentsVarName, String remindersVarName,
                                            String tagsVarName, String collaboratorsVarName,
                                            String versionsVarName, String searchIndexVarName,
                                            String syncStatusVarName, String tagVarName) {
        String exampleMethodName = RandomUtils.generateWord(6);
        String exampleTitleParamName = RandomUtils.generateWord(6);
        String exampleContentParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(exampleMethodName).append("(Context ")
                .append(contextVarName).append(") {\n");
        sb.append("        ").append(exampleMethodName).append("(").append(exampleTitleParamName)
                .append(");\n");
        sb.append("        ").append(exampleMethodName).append("(").append(exampleContentParamName)
                .append(");\n");

        if (useEncryption) {
            sb.append("        String encrypted = encrypt(").append(exampleContentParamName).append(");\n");
            sb.append("        String decrypted = decrypt(encrypted);\n");
        }

        if (useAttachments) {
            sb.append("        List<Uri> attachments = getAttachments();\n");
        }

        if (useReminders) {
            sb.append("        List<Date> reminders = getReminders();\n");
        }

        if (useTags) {
            sb.append("        Set<String> tags = getTags();\n");
        }

        if (useCollaboration) {
            sb.append("        Set<String> collaborators = getCollaborators();\n");
        }

        if (useVersioning) {
            sb.append("        List<Map<String, Object>> versions = getVersions();\n");
        }

        if (useSearch) {
            sb.append("        List<String> results = search(\"test\");\n");
        }

        if (useSync) {
            sb.append("        sync();\n");
            sb.append("        String status = getSyncStatus();\n");
        }

        sb.append("    }\n\n");
    }
}
