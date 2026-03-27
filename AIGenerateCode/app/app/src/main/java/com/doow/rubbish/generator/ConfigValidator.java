package com.doow.rubbish.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ConfigValidator {

    private List<String> errors;
    private List<String> warnings;

    public ConfigValidator() {
        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
    }


    public ValidationResult validate(String projectRoot, String packageName) {
        errors.clear();
        warnings.clear();


        validateProjectRoot(projectRoot);


        validatePackageName(packageName);


        validateGenerationCounts();


        validateResourceCounts();

        return new ValidationResult(errors, warnings);
    }


    private void validateProjectRoot(String projectRoot) {
        if (projectRoot == null || projectRoot.trim().isEmpty()) {
            errors.add("项目根路径不能为空");
            return;
        }

        File projectDir = new File(projectRoot);
        if (!projectDir.exists()) {
            errors.add("项目根路径不存在: " + projectRoot);
            return;
        }

        if (!projectDir.isDirectory()) {
            errors.add("项目根路径不是目录: " + projectRoot);
            return;
        }

        if (!projectDir.canWrite()) {
            errors.add("项目根路径不可写: " + projectRoot);
        }
    }

    /**
     * 验证包名
     */
    private void validatePackageName(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) {
            errors.add("包名不能为空");
            return;
        }

格式
        if (!packageName.matches("^[a-z][a-z0-9_]*(\\.[a-z0-9_]+)+$")) {
            errors.add("包名格式无效: " + packageName);
        }

        // 检查是否使用保留包名
        if (packageName.startsWith("android.") || 
            packageName.startsWith("java.") || 
            packageName.startsWith("javax.")) {
            errors.add("不能使用系统保留包名: " + packageName);
        }
    }

    /**
     * 验证生成数量配置
     */
    private void validateGenerationCounts() {
        if (GeneratorConfig.packageCount < 1 || GeneratorConfig.packageCount > 20) {
            warnings.add("包数量建议在1-20之间，当前: " + GeneratorConfig.packageCount);
        }

        if (GeneratorConfig.activityCountPerPackage < 1 || GeneratorConfig.activityCountPerPackage > 20) {
            warnings.add("每包Activity数量建议在1-20之间，当前: " + GeneratorConfig.activityCountPerPackage);
        }

        if (GeneratorConfig.modelCount < 1 || GeneratorConfig.modelCount > 50) {
            warnings.add("Model数量建议在1-50之间，当前: " + GeneratorConfig.modelCount);
        }

        if (GeneratorConfig.viewModelCount < 1 || GeneratorConfig.viewModelCount > 30) {
            warnings.add("ViewModel数量建议在1-30之间，当前: " + GeneratorConfig.viewModelCount);
        }

        if (GeneratorConfig.repositoryCount < 1 || GeneratorConfig.repositoryCount > 20) {
            warnings.add("Repository数量建议在1-20之间，当前: " + GeneratorConfig.repositoryCount);
        }

        if (GeneratorConfig.adapterCount < 1 || GeneratorConfig.adapterCount > 30) {
            warnings.add("Adapter数量建议在1-30之间，当前: " + GeneratorConfig.adapterCount);
        }

        if (GeneratorConfig.fragmentCount < 1 || GeneratorConfig.fragmentCount > 20) {
            warnings.add("Fragment数量建议在1-20之间，当前: " + GeneratorConfig.fragmentCount);
        }

        if (GeneratorConfig.managerCount < 1 || GeneratorConfig.managerCount > 20) {
            warnings.add("Manager数量建议在1-20之间，当前: " + GeneratorConfig.managerCount);
        }

        if (GeneratorConfig.helperCount < 1 || GeneratorConfig.helperCount > 30) {
            warnings.add("Helper数量建议在1-30之间，当前: " + GeneratorConfig.helperCount);
        }
    }

    /**
     * 验证资源数量配置
     */
    private void validateResourceCounts() {
        if (GeneratorConfig.drawableMin < 0 || GeneratorConfig.drawableMin > GeneratorConfig.drawableMax) {
            errors.add("Drawable最小值不能大于最大值");
        }

        if (GeneratorConfig.drawableMax > 100) {
            warnings.add("Drawable最大值建议不超过100，当前: " + GeneratorConfig.drawableMax);
        }

        if (GeneratorConfig.layoutMin < 0 || GeneratorConfig.layoutMin > GeneratorConfig.layoutMax) {
            errors.add("Layout最小值不能大于最大值");
        }

        if (GeneratorConfig.layoutMax > 100) {
            warnings.add("Layout最大值建议不超过100，当前: " + GeneratorConfig.layoutMax);
        }

        if (GeneratorConfig.stringMin < 0 || GeneratorConfig.stringMin > GeneratorConfig.stringMax) {
            errors.add("String最小值不能大于最大值");
        }

        if (GeneratorConfig.stringMax > 100) {
            warnings.add("String最大值建议不超过100，当前: " + GeneratorConfig.stringMax);
        }

        if (GeneratorConfig.rawMin < 0 || GeneratorConfig.rawMin > GeneratorConfig.rawMax) {
            errors.add("Raw最小值不能大于最大值");
        }

        if (GeneratorConfig.rawMax > 50) {
            warnings.add("Raw最大值建议不超过50，当前: " + GeneratorConfig.rawMax);
        }
    }

    /**
     * 验证结果类
     */
    public static class ValidationResult {
        private final List<String> errors;
        private final List<String> warnings;

        public ValidationResult(List<String> errors, List<String> warnings) {
            this.errors = errors;
            this.warnings = warnings;
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public boolean hasWarnings() {
            return !warnings.isEmpty();
        }

        public List<String> getErrors() {
            return errors;
        }

        public List<String> getWarnings() {
            return warnings;
        }

        public void print() {
            if (!errors.isEmpty()) {
                System.err.println("\n错误:");
                for (String error : errors) {
                    System.err.println("  ✗ " + error);
                }
            }

            if (!warnings.isEmpty()) {
                System.out.println("\n警告:");
                for (String warning : warnings) {
                    System.out.println("  ⚠ " + warning);
                }
            }

            if (isValid() && !hasWarnings()) {
                System.out.println("\n✓ 配置验证通过");
            }
        }
    }
}
