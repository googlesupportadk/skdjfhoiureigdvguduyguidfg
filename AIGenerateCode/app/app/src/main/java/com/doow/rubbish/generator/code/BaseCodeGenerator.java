package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.FileUtils;
import com.doow.rubbish.generator.RandomUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 代码生成器基类
 * 提供所有代码生成器的基础功能
 */
public abstract class BaseCodeGenerator {
    protected String projectRoot;
    protected String packageName;

    // 跨平台换行符（替代硬编码的换行符）
    private static final String LINE_SEPARATOR = System.lineSeparator();

    public BaseCodeGenerator(String projectRoot, String packageName) {
        // 空值防护：避免传入null导致后续拼接异常
        this.projectRoot = projectRoot == null ? "" : projectRoot;
        this.packageName = packageName == null ? "" : packageName;
    }

    /**
     * 生成所有代码
     */
    public abstract void generateAll() throws Exception;

    /**
     * 生成包声明
     * @param subPackage 子包名（可为空）
     * @return 规范的包声明语句
     */
    protected String generatePackageDeclaration(String subPackage) {
        // 处理子包名为空的情况
        String fullPackage = packageName;
        if (subPackage != null && !subPackage.trim().isEmpty()) {
            fullPackage += "." + subPackage.trim();
        }
        // 使用跨平台换行符，保证不同系统下代码格式正确
        return "package " + fullPackage + ";" + LINE_SEPARATOR + LINE_SEPARATOR;
    }

    /**
     * 生成导入语句
     * @param importClass 要导入的类全限定名
     * @return 规范的导入语句
     */
    protected String generateImportStatement(String importClass) {
        // 空值防护：避免导入空字符串
        if (importClass == null || importClass.trim().isEmpty()) {
            return "";
        }
        return "import " + importClass.trim() + ";" + LINE_SEPARATOR;
    }

    /**
     * 获取随机类型
     * @return 随机的Java类型字符串，无索引越界风险
     */
    protected String getRandomType() {
        List<String> types = Arrays.asList(
                "String", "int", "long", "float", "double", "boolean",
                "Integer", "Long", "Float", "Double", "Boolean",
                "byte", "short", "Byte", "Short", "Character",
                "StringBuilder", "StringBuffer", "Object", "List<String>",
                "List<Integer>", "List<Long>", "List<Float>", "List<Double>"
        );
        // 边界防护：RandomUtils.between确保取[0, size-1]，避免索引越界
        int randomIndex = RandomUtils.between(0, types.size() - 1);
        return types.get(randomIndex);
    }

    /**
     * 生成Java文件
     * @param className 类名（不能为空）
     * @param content 类内容
     * @param subPackage 子包名（可为空）
     * @throws Exception 文件写入异常
     */
    protected void generateJavaFile(String className, String content, String subPackage) throws Exception {
        // 空值校验：核心参数不能为空
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("类名不能为空");
        }
        if (content == null) {
            content = "";
        }

        // 处理包路径：使用File.separator保证跨平台兼容性
        String packagePath = packageName.replace(".", File.separator);
        String fullPath = projectRoot;

        // 拼接主代码目录（app/src/main/java）
        if (!fullPath.endsWith(File.separator)) {
            fullPath += File.separator;
        }
        fullPath += "app" + File.separator + "src" + File.separator + "main" + File.separator + "java";

        // 拼接包路径
        if (!packagePath.isEmpty()) {
            fullPath += File.separator + packagePath;
        }

        // 拼接子包路径
        if (subPackage != null && !subPackage.trim().isEmpty()) {
            fullPath += File.separator + subPackage.trim();
        }

        // 创建目录（mkdirs会自动创建多级目录，无需手动判断）
        File dir = new File(fullPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 拼接文件完整路径
        String filePath = fullPath + File.separator + className.trim() + ".java";
        // 直接调用FileUtils，移除冗余的writeFile封装
        FileUtils.writeFile(filePath, content);
    }
}