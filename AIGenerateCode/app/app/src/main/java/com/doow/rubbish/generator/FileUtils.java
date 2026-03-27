package com.doow.rubbish.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FileUtils {

    public static void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }


    public static void createFile(String path, String content) throws IOException {
        File file = new File(path);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }


    public static void appendToFile(String path, String content) throws IOException {
        File file = new File(path);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(content);
        }
    }


    public static void deleteDirectory(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            deleteDir(dir);
        }
    }

    // 递归删除目录
    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    // 检查文件是否存在
    public static boolean exists(String path) {
        return new File(path).exists();
    }

    // 获取文件扩展名
    public static String getExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0 && lastDot < filename.length() - 1) {
            return filename.substring(lastDot + 1).toLowerCase();
        }
        return "";
    }

    // 获取不带扩展名的文件名
    public static String getBaseName(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0) {
            return filename.substring(0, lastDot);
        }
        return filename;
    }

    // 写入文件内容
    public static void writeFile(String path, String content) throws IOException {
        createFile(path, content);
    }
}
