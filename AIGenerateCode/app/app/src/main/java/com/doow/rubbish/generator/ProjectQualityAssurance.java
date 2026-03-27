package com.doow.rubbish.generator;

import java.io.*;
import java.util.*;

/**
 * 项目质量保证 - 第七步优化
 * 负责确保每个项目代码完整可编译，避免重复代码模式，确保包名唯一性
 */
public class ProjectQualityAssurance {

    private static ProjectQualityAssurance instance;
    private Set<String> usedPackageNames;
    private Set<String> usedCodePatterns;
    private Map<String, ProjectQualityReport> qualityReports;

    public static class ProjectQualityReport {
        public String packageName;
        public String projectPath;
        public boolean isCompilable;
        public boolean hasUniqueCode;
        public boolean hasValidStructure;
        public List<String> issues;
        public List<String> warnings;
        public double qualityScore;

        public ProjectQualityReport(String packageName, String projectPath) {
            this.packageName = packageName;
            this.projectPath = projectPath;
            this.isCompilable = true;
            this.hasUniqueCode = true;
            this.hasValidStructure = true;
            this.issues = new ArrayList<>();
            this.warnings = new ArrayList<>();
            this.qualityScore = 100.0;
        }
    }

    private ProjectQualityAssurance() {
        usedPackageNames = new HashSet<>();
        usedCodePatterns = new HashSet<>();
        qualityReports = new HashMap<>();
    }

    public static synchronized ProjectQualityAssurance getInstance() {
        if (instance == null) {
            instance = new ProjectQualityAssurance();
        }
        return instance;
    }

    public void reset() {
        usedPackageNames.clear();
        usedCodePatterns.clear();
        qualityReports.clear();
    }

    public boolean checkPackageNameUnique(String packageName) {
        if (usedPackageNames.contains(packageName)) {
            return false;
        }
        usedPackageNames.add(packageName);
        return true;
    }

    public boolean checkCodePatternUnique(String codePattern) {
        if (usedCodePatterns.contains(codePattern)) {
            return false;
        }
        usedCodePatterns.add(codePattern);
        return true;
    }

    public boolean validateProjectStructure(String projectPath) {
        File projectDir = new File(projectPath);
        if (!projectDir.exists() || !projectDir.isDirectory()) {
            return false;
        }

        String[] requiredDirs = {
            "app/src/main/java",
            "app/src/main/res",
            "app/src/main/AndroidManifest.xml"
        };

        for (String dir : requiredDirs) {
            File requiredFile = new File(projectPath, dir);
            if (!requiredFile.exists()) {
                return false;
            }
        }

        return true;
    }

    public boolean validateCodeCompleteness(String projectPath) {
        File javaDir = new File(projectPath, "app/src/main/java");
        if (!javaDir.exists()) {
            return false;
        }

        File[] javaFiles = javaDir.listFiles((dir, name) -> name.endsWith(".java"));
        if (javaFiles == null || javaFiles.length == 0) {
            return false;
        }

        String[] requiredClasses = {
            "MainActivity",
            "Application"
        };

        for (String className : requiredClasses) {
            boolean found = false;
            for (File javaFile : javaFiles) {
                if (javaFile.getName().contains(className)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }

    public boolean validateResourceCompleteness(String projectPath) {
        File resDir = new File(projectPath, "app/src/main/res");
        if (!resDir.exists()) {
            return false;
        }

        String[] requiredResDirs = {
            "values",
            "layout",
            "drawable"
        };

        for (String dir : requiredResDirs) {
            File requiredDir = new File(resDir, dir);
            if (!requiredDir.exists()) {
                return false;
            }
        }

        File valuesDir = new File(resDir, "values");
        String[] requiredResFiles = {
            "strings.xml",
            "colors.xml",
            "styles.xml"
        };

        for (String fileName : requiredResFiles) {
            File requiredFile = new File(valuesDir, fileName);
            if (!requiredFile.exists()) {
                return false;
            }
        }

        return true;
    }

    public String generateCodePattern(String packageName, List<String> modules, Map<String, Object> variations) {
        StringBuilder pattern = new StringBuilder();
        pattern.append(packageName).append("|");
        pattern.append(String.join(",", modules)).append("|");
        pattern.append(variations.get("architecture")).append("|");
        pattern.append(variations.get("database_impl")).append("|");
        pattern.append(variations.get("async_impl")).append("|");
        pattern.append(variations.get("theme"));
        return pattern.toString();
    }

    public ProjectQualityReport performQualityCheck(
        String packageName,
        String projectPath,
        List<String> modules,
        Map<String, Object> variations
    ) {
        ProjectQualityReport report = new ProjectQualityReport(packageName, projectPath);

        if (!checkPackageNameUnique(packageName)) {
            report.isCompilable = false;
            report.issues.add("包名重复: " + packageName);
            report.qualityScore -= 20.0;
        }

        String codePattern = generateCodePattern(packageName, modules, variations);
        if (!checkCodePatternUnique(codePattern)) {
            report.hasUniqueCode = false;
            report.issues.add("代码模式重复");
            report.qualityScore -= 15.0;
        }

        if (!validateProjectStructure(projectPath)) {
            report.hasValidStructure = false;
            report.isCompilable = false;
            report.issues.add("项目结构无效");
            report.qualityScore -= 30.0;
        }

        if (!validateCodeCompleteness(projectPath)) {
            report.isCompilable = false;
            report.issues.add("代码不完整");
            report.qualityScore -= 25.0;
        }

        if (!validateResourceCompleteness(projectPath)) {
            report.isCompilable = false;
            report.issues.add("资源不完整");
            report.qualityScore -= 20.0;
        }

        if (report.issues.size() > 0) {
            report.qualityScore = Math.max(0.0, report.qualityScore);
        }

        qualityReports.put(packageName, report);

        return report;
    }

    public void printQualityReport(ProjectQualityReport report) {
        System.out.println("========================================");
        System.out.println("项目质量报告");
        System.out.println("========================================");
        System.out.println("包名: " + report.packageName);
        System.out.println("项目路径: " + report.projectPath);
        System.out.println("\n质量评分: " + String.format("%.1f", report.qualityScore) + "/100");
        System.out.println("\n检查结果:");
        System.out.println("  可编译: " + (report.isCompilable ? "YES" : "NO"));
        System.out.println("  代码唯一: " + (report.hasUniqueCode ? "YES" : "NO"));
        System.out.println("  结构有效: " + (report.hasValidStructure ? "YES" : "NO"));

        if (!report.issues.isEmpty()) {
            System.out.println("\n问题:");
            for (String issue : report.issues) {
                System.out.println("  [X] " + issue);
            }
        }

        if (!report.warnings.isEmpty()) {
            System.out.println("\n警告:");
            for (String warning : report.warnings) {
                System.out.println("  [!] " + warning);
            }
        }

        System.out.println("========================================");
    }

    public void generateQualityReportFile(String reportPath) throws IOException {
        File reportFile = new File(reportPath);
        File parentDir = reportFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
            writer.println("========================================");
            writer.println("批量项目质量报告");
            writer.println("========================================");
            writer.println("生成时间: " + new Date());
            writer.println("项目总数: " + qualityReports.size());
            writer.println("========================================\n");

            int passCount = 0;
            int failCount = 0;

            for (Map.Entry<String, ProjectQualityReport> entry : qualityReports.entrySet()) {
                ProjectQualityReport report = entry.getValue();
                writer.println("项目: " + report.packageName);
                writer.println("  质量评分: " + String.format("%.1f", report.qualityScore) + "/100");
                writer.println("  可编译: " + (report.isCompilable ? "是" : "否"));
                writer.println("  代码唯一: " + (report.hasUniqueCode ? "是" : "否"));
                writer.println("  结构有效: " + (report.hasValidStructure ? "是" : "否"));

                if (!report.issues.isEmpty()) {
                    writer.println("  问题:");
                    for (String issue : report.issues) {
                        writer.println("    - " + issue);
                    }
                }

                if (report.isCompilable && report.hasUniqueCode && report.hasValidStructure) {
                    passCount++;
                } else {
                    failCount++;
                }

                writer.println();
            }

            writer.println("========================================");
            writer.println("统计:");
            writer.println("  通过: " + passCount);
            writer.println("  失败: " + failCount);
            writer.println("  通过率: " + String.format("%.1f%%", 
                (double) passCount / qualityReports.size() * 100));
            writer.println("========================================");
        }

        System.out.println("\n质量报告已生成: " + reportPath);
    }

    public Map<String, ProjectQualityReport> getAllQualityReports() {
        return new HashMap<>(qualityReports);
    }

    public int getPassCount() {
        int count = 0;
        for (ProjectQualityReport report : qualityReports.values()) {
            if (report.isCompilable && report.hasUniqueCode && report.hasValidStructure) {
                count++;
            }
        }
        return count;
    }

    public int getFailCount() {
        int count = 0;
        for (ProjectQualityReport report : qualityReports.values()) {
            if (!report.isCompilable || !report.hasUniqueCode || !report.hasValidStructure) {
                count++;
            }
        }
        return count;
    }

    public double getPassRate() {
        if (qualityReports.isEmpty()) {
            return 0.0;
        }
        return (double) getPassCount() / qualityReports.size() * 100;
    }
}
