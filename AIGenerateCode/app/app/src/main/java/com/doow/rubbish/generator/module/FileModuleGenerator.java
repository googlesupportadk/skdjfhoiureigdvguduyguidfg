package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.EnhancedRandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class FileModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;

    // 文件操作类型
    private static final String[] OPERATION_TYPES = {
        "read", "write", "copy", "move", "delete", "compress"
    };

    public FileModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成文件模块");

        // 获取当前数据存储方式和异步处理方式
        String storageType = variationManager.getVariation("storage");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成文件模块
        generateFileModule(storageType, asyncHandler);
    }

    private void generateFileModule(String storageType, String asyncHandler) throws Exception {
        // 生成文件管理器
        generateFileManager(storageType, asyncHandler);

        // 生成文件工具类
        generateFileUtils(storageType, asyncHandler);

        // 生成内部文件列表
        generateInternalFileList(storageType, asyncHandler);
    }

    private void generateFileManager(String storageType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Manager");
        String instanceName = EnhancedRandomUtils.generateVariableName("Instance");
        String contextName = EnhancedRandomUtils.generateObjectName();
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String bufferName = EnhancedRandomUtils.generateIntName();

        // 使用随机值
        int bufferSize = EnhancedRandomUtils.generateIntRange(1024, 8192)[0];

        sb.append("package ").append(packageName).append(".file;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import java.io.File;\n");
        sb.append("import java.io.FileInputStream;\n");
        sb.append("import java.io.FileOutputStream;\n");
        sb.append("import java.io.IOException;\n");

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
        sb.append("    private static final int ").append(bufferName).append(" = ").append(bufferSize).append(";\n");
        sb.append("\n");

        // 单例
        sb.append("    private static volatile ").append(className).append(" ").append(instanceName).append(";\n");
        sb.append("    private final Context ").append(contextName).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    private ").append(className).append("(Context ").append(contextName).append(") {\n");
        sb.append("        this.").append(contextName).append(" = ").append(contextName).append(".getApplicationContext();\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取实例方法
        sb.append("    public static ").append(className).append(" getInstance(Context context) {\n");
        sb.append("        if (").append(instanceName).append(" == null) {\n");
        sb.append("            synchronized (").append(className).append(".class) {\n");
        sb.append("                if (").append(instanceName).append(" == null) {\n");
        sb.append("                    ").append(instanceName).append(" = new ").append(className).append("(context);\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(instanceName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        // 读取文件方法
        sb.append("    public byte[] readFile(File file) throws IOException {\n");
        sb.append("        FileInputStream fis = new FileInputStream(file);\n");
        sb.append("        byte[] buffer = new byte[(int) file.length()];\n");
        sb.append("        fis.read(buffer);\n");
        sb.append("        fis.close();\n");
        sb.append("        Log.d(").append(tagName).append(", \"Read file: \" + file.getAbsolutePath());\n");
        sb.append("        return buffer;\n");
        sb.append("    }\n");
        sb.append("\n");

        // 写入文件方法
        sb.append("    public void writeFile(File file, byte[] data) throws IOException {\n");
        sb.append("        FileOutputStream fos = new FileOutputStream(file);\n");
        sb.append("        fos.write(data);\n");
        sb.append("        fos.flush();\n");
        sb.append("        fos.close();\n");
        sb.append("        Log.d(").append(tagName).append(", \"Wrote file: \" + file.getAbsolutePath());\n");
        sb.append("    }\n");
        sb.append("\n");

        // 复制文件方法
        sb.append("    public void copyFile(File source, File destination) throws IOException {\n");
        sb.append("        FileInputStream fis = new FileInputStream(source);\n");
        sb.append("        FileOutputStream fos = new FileOutputStream(destination);\n");
        sb.append("        byte[] buffer = new byte[").append(bufferName).append("];\n");
        sb.append("        int bytesRead;\n");
        sb.append("        while ((bytesRead = fis.read(buffer)) != -1) {\n");
        sb.append("            fos.write(buffer, 0, bytesRead);\n");
        sb.append("        }\n");
        sb.append("        fis.close();\n");
        sb.append("        fos.close();\n");
        sb.append("        Log.d(").append(tagName).append(", \"Copied file from \" + source.getAbsolutePath() +\n");
        sb.append("                    \" to \" + destination.getAbsolutePath());\n");
        sb.append("    }\n");
        sb.append("\n");

        // 移动文件方法
        sb.append("    public void moveFile(File source, File destination) throws IOException {\n");
        sb.append("        if (destination.exists()) {\n");
        sb.append("            destination.delete();\n");
        sb.append("        }\n");
        sb.append("        boolean success = source.renameTo(destination);\n");
        sb.append("        if (success) {\n");
        sb.append("            Log.d(").append(tagName).append(", \"Moved file from \" + source.getAbsolutePath() +\n");
        sb.append("                        \" to \" + destination.getAbsolutePath());\n");
        sb.append("        } else {\n");
        sb.append("            throw new IOException(\"Failed to move file\");\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");

        // 删除文件方法
        sb.append("    public boolean deleteFile(File file) {\n");
        sb.append("        boolean success = file.delete();\n");
        sb.append("        if (success) {\n");
        sb.append("            Log.d(").append(tagName).append(", \"Deleted file: \" + file.getAbsolutePath());\n");
        sb.append("        } else {\n");
        sb.append("            Log.e(").append(tagName).append(", \"Failed to delete file: \" + file.getAbsolutePath());\n");
        sb.append("        }\n");
        sb.append("        return success;\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取文件大小方法
        sb.append("    public long getFileSize(File file) {\n");
        sb.append("        return file.length();\n");
        sb.append("    }\n");
        sb.append("\n");

        // 检查文件是否存在方法
        sb.append("    public boolean fileExists(File file) {\n");
        sb.append("        return file.exists() && file.isFile();\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "file");
    }

    private void generateFileUtils(String storageType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(packageName).append(".file;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.net.Uri;\n");
        sb.append("import android.webkit.MimeTypeMap;\n");
        sb.append("import android.webkit.URLUtil;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import java.io.File;\n");
        sb.append("import java.text.SimpleDateFormat;\n");
        sb.append("import java.util.Date;\n");
        sb.append("import java.util.Locale;\n");

        sb.append("\n");

        // 类声明
        sb.append("public class FileUtils {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String TAG = \"FileUtils\";\n");
        sb.append("    private static final String DATE_FORMAT = \"yyyyMMdd_HHmmss\";\n");

        // 私有构造函数
        sb.append("    private FileUtils() {\n");
        sb.append("        // 私有构造函数，防止实例化\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取文件扩展名方法
        sb.append("    public static String getFileExtension(String fileName) {\n");
        sb.append("        if (fileName == null || fileName.isEmpty()) {\n");
        sb.append("            return \"\";\n");
        sb.append("        }\n");
        sb.append("        int dotIndex = fileName.lastIndexOf('.');\n");
        sb.append("        return (dotIndex == -1) ? \"\" : fileName.substring(dotIndex + 1);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取文件名方法
        sb.append("    public static String getFileName(String fileName) {\n");
        sb.append("        if (fileName == null || fileName.isEmpty()) {\n");
        sb.append("            return \"\";\n");
        sb.append("        }\n");
        sb.append("        int dotIndex = fileName.lastIndexOf('.');\n");
        sb.append("        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取MIME类型方法
        sb.append("    public static String getMimeType(String fileName) {\n");
        sb.append("        String extension = getFileExtension(fileName).toLowerCase();\n");
        sb.append("        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 格式化文件大小方法
        sb.append("    public static String formatFileSize(long size) {\n");
        sb.append("        if (size < 1024) {\n");
        sb.append("            return size + \" B\";\n");
        sb.append("        } else if (size < 1024 * 1024) {\n");
        sb.append("            return String.format(Locale.getDefault(), \"%.2f KB\", size / 1024.0);\n");
        sb.append("        } else if (size < 1024 * 1024 * 1024) {\n");
        sb.append("            return String.format(Locale.getDefault(), \"%.2f MB\", size / (1024.0 * 1024));\n");
        sb.append("        } else {\n");
        sb.append("            return String.format(Locale.getDefault(), \"%.2f GB\", size / (1024.0 * 1024 * 1024));\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");

        // 生成唯一文件名方法
        sb.append("    public static String generateUniqueFileName(String prefix, String extension) {\n");
        sb.append("        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());\n");
        sb.append("        String timestamp = sdf.format(new Date());\n");
        sb.append("        return prefix + \"_\" + timestamp + \".\" + extension;\n");
        sb.append("    }\n");
        sb.append("\n");

        // 检查是否为图片文件方法
        sb.append("    public static boolean isImageFile(String fileName) {\n");
        sb.append("        String mimeType = getMimeType(fileName);\n");
        sb.append("        return mimeType != null && mimeType.startsWith(\"image/\");\n");
        sb.append("    }\n");
        sb.append("\n");

        // 检查是否为视频文件方法
        sb.append("    public static boolean isVideoFile(String fileName) {\n");
        sb.append("        String mimeType = getMimeType(fileName);\n");
        sb.append("        return mimeType != null && mimeType.startsWith(\"video/\");\n");
        sb.append("    }\n");
        sb.append("\n");

        // 检查是否为音频文件方法
        sb.append("    public static boolean isAudioFile(String fileName) {\n");
        sb.append("        String mimeType = getMimeType(fileName);\n");
        sb.append("        return mimeType != null && mimeType.startsWith(\"audio/\");\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("}\n");

        // 保存文件
        generateJavaFile("FileUtils", sb.toString(), "file");
    }

    private void generateInternalFileList(String storageType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(packageName).append(".file;\n");

        // 导入
        sb.append("import android.app.Activity;\n");
        sb.append("import android.content.Intent;\n");
        sb.append("import android.net.Uri;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import androidx.activity.result.ActivityResultLauncher;\n");
        sb.append("import androidx.activity.result.contract.ActivityResultContracts;\n");
        sb.append("import androidx.annotation.NonNull;\n");

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
        sb.append("public class FilePicker {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String TAG = \"FilePicker\";\n");
        sb.append("    private static final int REQUEST_CODE_PICK_FILE = 1001;\n");
        sb.append("\n");

        // 单例
        sb.append("    private static volatile FilePicker INSTANCE;\n");
        sb.append("    private FilePickCallback callback;\n");
        sb.append("\n");

        // 私有构造函数
        sb.append("    private FilePicker() {\n");
        sb.append("        // 私有构造函数，防止实例化\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取实例方法
        sb.append("    public static FilePicker getInstance() {\n");
        sb.append("        if (INSTANCE == null) {\n");
        sb.append("            synchronized (FilePicker.class) {\n");
        sb.append("                if (INSTANCE == null) {\n");
        sb.append("                    INSTANCE = new FilePicker();\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return INSTANCE;\n");
        sb.append("    }\n");
        sb.append("\n");

        // 设置回调方法
        sb.append("    public void setCallback(FilePickCallback callback) {\n");
        sb.append("        this.callback = callback;\n");
        sb.append("    }\n");
        sb.append("\n");

        // 打开文件选择器方法
        sb.append("    public void openFilePicker(Activity activity) {\n");
        sb.append("        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);\n");
        sb.append("        intent.setType(\"*/*\");\n");
        sb.append("        intent.addCategory(Intent.CATEGORY_OPENABLE);\n");
        sb.append("        activity.startActivityForResult(Intent.createChooser(intent, \"选择文件\"), REQUEST_CODE_PICK_FILE);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 处理结果方法
        sb.append("    public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {\n");
        sb.append("        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK) {\n");
        sb.append("            if (data != null && data.getData() != null) {\n");
        sb.append("                Uri uri = data.getData();\n");
        sb.append("                Log.d(TAG, \"Picked file: \" + uri.toString());\n");
        sb.append("                if (callback != null) {\n");
        sb.append("                    callback.onFilePicked(uri);\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");

        // 回调接口
        sb.append("    public interface FilePickCallback {\n");
        sb.append("        void onFilePicked(Uri uri);\n");
        sb.append("        void onFilePickError(String error);\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("}\n");

        // 保存文件
        generateJavaFile("FilePicker", sb.toString(), "file");
    }
}
