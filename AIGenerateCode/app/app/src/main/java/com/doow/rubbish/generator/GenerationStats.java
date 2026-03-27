package com.doow.rubbish.generator;

import java.util.HashMap;
import java.util.Map;


public class GenerationStats {

    private static GenerationStats instance;

    private Map<String, Integer> fileCounts;
    private Map<String, Integer> lineCounts;
    private int totalFiles;
    private int totalLines;
    private long startTime;

    private GenerationStats() {
        this.fileCounts = new HashMap<>();
        this.lineCounts = new HashMap<>();
        this.totalFiles = 0;
        this.totalLines = 0;
        this.startTime = System.currentTimeMillis();
    }


    public static synchronized GenerationStats getInstance() {
        if (instance == null) {
            instance = new GenerationStats();
        }
        return instance;
    }


    public void recordFile(String fileType, int lines) {
        fileCounts.put(fileType, fileCounts.getOrDefault(fileType, 0) + 1);
        lineCounts.put(fileType, lineCounts.getOrDefault(fileType, 0) + lines);
        totalFiles++;
        totalLines += lines;
    }


    public int getFileCount(String fileType) {
        return fileCounts.getOrDefault(fileType, 0);
    }

    /**
     * 获取代码行数
     */
    public int getLineCount(String fileType) {
        return lineCounts.getOrDefault(fileType, 0);
    }

    /**
     * 获取总文件数
     */
    public int getTotalFiles() {
        return totalFiles;
    }

    /**
     * 获取总代码行数
     */
    public int getTotalLines() {
        return totalLines;
    }

    /**
     * 打印统计信息
     */
    public void printStats() {
        long duration = System.currentTimeMillis() - startTime;

        System.out.println("\n========================================");
        System.out.println("生成统计信息");
        System.out.println("========================================");
        System.out.println("总耗时: " + formatDuration(duration));
        System.out.println("总文件数: " + totalFiles);
        System.out.println("总代码行数: " + totalLines);
        System.out.println("----------------------------------------");
        System.out.println("按类型统计:");

        for (Map.Entry<String, Integer> entry : fileCounts.entrySet()) {
            String type = entry.getKey();
            int files = entry.getValue();
            int lines = lineCounts.get(type);
            double percentage = (double) files / totalFiles * 100;

            System.out.printf("  %-20s: %3d个文件 (%5.1f%%) %5d行\n", 
                type, files, percentage, lines);
        }

        System.out.println("----------------------------------------");
        System.out.printf("平均每文件: %.1f行\n", (double) totalLines / totalFiles);
        System.out.printf("平均每秒: %.1f行\n", (double) totalLines * 1000 / duration);
        System.out.println("========================================");
    }

    /**
     * 格式化持续时间
     */
    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;

        if (minutes > 0) {
            return minutes + "分" + seconds + "秒";
        } else {
            return seconds + "秒";
        }
    }

    /**
     * 重置统计
     */
    public void reset() {
        fileCounts.clear();
        lineCounts.clear();
        totalFiles = 0;
        totalLines = 0;
        startTime = System.currentTimeMillis();
    }

    /**
     * 导出统计信息为字符串
     */
    public String exportStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("生成统计信息\n");
        sb.append("========================================\n");
        sb.append("总文件数: ").append(totalFiles).append("\n");
        sb.append("总代码行数: ").append(totalLines).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("按类型统计:\n");

        for (Map.Entry<String, Integer> entry : fileCounts.entrySet()) {
            String type = entry.getKey();
            int files = entry.getValue();
            int lines = lineCounts.get(type);
            sb.append(String.format("  %-20s: %3d个文件 %5d行\n", type, files, lines));
        }

        sb.append("========================================");
        return sb.toString();
    }
}
