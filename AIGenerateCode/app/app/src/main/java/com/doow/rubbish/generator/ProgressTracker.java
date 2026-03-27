package com.doow.rubbish.generator;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class ProgressTracker {

    private static ProgressTracker instance;

    private int totalSteps;
    private int currentStep;
    private String currentPhase;
    private Map<String, Integer> phaseProgress;
    private long startTime;
    private long phaseStartTime;

    private ProgressTracker() {
        this.totalSteps = 100;
        this.currentStep = 0;
        this.phaseProgress = new HashMap<>();
        this.startTime = System.currentTimeMillis();
    }


    public static synchronized ProgressTracker getInstance() {
        if (instance == null) {
            instance = new ProgressTracker();
        }
        return instance;
    }


    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }


    public void startPhase(String phaseName, int steps) {
        this.currentPhase = phaseName;
        this.phaseStartTime = System.currentTimeMillis();
        this.phaseProgress.put(phaseName, 0);
        System.out.println("\n>>> 开始阶段: " + phaseName + " (预计" + steps + "步)");
    }

    /**
     * 更新阶段进度
     */
    public void updatePhaseProgress(int steps) {
        if (currentPhase != null) {
            int current = phaseProgress.getOrDefault(currentPhase, 0);
            phaseProgress.put(currentPhase, current + steps);
        }
    }

    /**
     * 完成阶段
     */
    public void completePhase() {
        if (currentPhase != null) {
            long duration = System.currentTimeMillis() - phaseStartTime;
            System.out.println(">>> 阶段完成: " + currentPhase + " (耗时: " + formatDuration(duration) + ")");
            currentPhase = null;
        }
    }

    /**
     * 更新进度
     */
    public void updateProgress(int steps) {
        currentStep += steps;
        printProgress();
    }

    /**
     * 打印进度
     */
    public void printProgress() {
        double percentage = (double) currentStep / totalSteps * 100;
        DecimalFormat df = new DecimalFormat("0.00");

        StringBuilder bar = new StringBuilder("[");
        int filled = (int) (percentage / 2);
        for (int i = 0; i < 50; i++) {
            if (i < filled) {
                bar.append("=");
            } else if (i == filled) {
                bar.append(">");
            } else {
                bar.append(" ");
            }
        }
        bar.append("]");

        long elapsed = System.currentTimeMillis() - startTime;
        long estimated = (long) (elapsed * ((double) totalSteps / currentStep));
        long remaining = estimated - elapsed;

        System.out.print("\r" + bar + " " + df.format(percentage) + "% " + 
                        "已完成: " + currentStep + "/" + totalSteps + " " +
                        "已用: " + formatDuration(elapsed) + " " +
                        "剩余: " + formatDuration(remaining));
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
     * 完成所有步骤
     */
    public void complete() {
        currentStep = totalSteps;
        printProgress();
        System.out.println("\n");

        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("\n========================================");
        System.out.println("生成完成统计");
        System.out.println("========================================");
        System.out.println("总耗时: " + formatDuration(totalTime));
        System.out.println("总步骤: " + totalSteps);
        System.out.println("平均每步: " + formatDuration(totalTime / totalSteps));
        System.out.println("----------------------------------------");
        System.out.println("各阶段详情:");
        for (Map.Entry<String, Integer> entry : phaseProgress.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue() + "步");
        }
        System.out.println("========================================");
    }

    /**
     * 重置进度
     */
    public void reset() {
        this.currentStep = 0;
        this.currentPhase = null;
        this.phaseProgress.clear();
        this.startTime = System.currentTimeMillis();
    }
}
