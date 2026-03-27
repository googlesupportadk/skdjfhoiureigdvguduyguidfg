package com.doow.rubbish.generator;

import java.io.*;
import java.util.*;

/**
 * 批量项目生成器 - 第六步优化
 * 负责批量生成多个项目，每个项目具有不同的配置
 */
public class BatchProjectGenerator {

    private static BatchProjectGenerator instance;
    private Random random;

    // 项目配置
    private String baseProjectRoot;
    private String basePackageName;
    private int projectCount;
    private long baseSeed;

    // 生成器实例
    private ModuleSelector moduleSelector;
    private CodeVariationGenerator codeVariationGenerator;
    private UIVariationGenerator uiVariationGenerator;
    private ArchitectureVariationGenerator architectureVariationGenerator;
    private ResourceVariationGenerator resourceVariationGenerator;

    // 项目配置列表
    private List<ProjectConfig> projectConfigs;

    // 项目配置类
    public static class ProjectConfig {
        public String packageName;
        public String appName;
        public long seed;
        public List<String> modules;
        public Map<String, Object> codeVariations;
        public Map<String, Object> uiVariations;
        public Map<String, Object> architectureVariations;
        public Map<String, Object> resourceVariations;
        public String outputPath;

        public ProjectConfig(String packageName, String appName, long seed) {
            this.packageName = packageName;
            this.appName = appName;
            this.seed = seed;
            this.modules = new ArrayList<>();
            this.codeVariations = new HashMap<>();
            this.uiVariations = new HashMap<>();
            this.architectureVariations = new HashMap<>();
            this.resourceVariations = new HashMap<>();
        }
    }

    private BatchProjectGenerator() {
        random = new Random();
        projectConfigs = new ArrayList<>();

        // 初始化生成器
        moduleSelector = ModuleSelector.getInstance();
        codeVariationGenerator = CodeVariationGenerator.getInstance();
        uiVariationGenerator = UIVariationGenerator.getInstance();
        architectureVariationGenerator = ArchitectureVariationGenerator.getInstance();
        resourceVariationGenerator = ResourceVariationGenerator.getInstance();
    }

    public static synchronized BatchProjectGenerator getInstance() {
        if (instance == null) {
            instance = new BatchProjectGenerator();
        }
        return instance;
    }

    /**
     * 初始化批量生成器
     */
    public void initialize(String baseProjectRoot, String basePackageName, int projectCount, long baseSeed) {
        this.baseProjectRoot = baseProjectRoot;
        this.basePackageName = basePackageName;
        this.projectCount = projectCount;
        this.baseSeed = baseSeed;
        this.projectConfigs.clear();
    }

    /**
     * 生成所有项目配置
     */
    public void generateAllProjectConfigs() {
        for (int i = 0; i < projectCount; i++) {
            long projectSeed = baseSeed + i;
            generateProjectConfig(i, projectSeed);
        }
    }

    /**
     * 生成单个项目配置
     */
    private void generateProjectConfig(int index, long seed) {
        // 设置种子
        random.setSeed(seed);
        moduleSelector.setSeed(seed);
        codeVariationGenerator.setSeed(seed);
        uiVariationGenerator.setSeed(seed);
        architectureVariationGenerator.setSeed(seed);
        resourceVariationGenerator.setSeed(seed);

        // 生成包名（使用手动输入的包名）
        String packageName = basePackageName;

        // 生成应用名
        String appName = "Project_" + (index + 1);

        // 创建项目配置
        ProjectConfig config = new ProjectConfig(packageName, appName, seed);

        // 选择模块
        config.modules = moduleSelector.selectModules(3, 5);

        // 生成代码变体
        config.codeVariations = codeVariationGenerator.generateCodeVariations();

        // 生成UI变体
        config.uiVariations = uiVariationGenerator.generateUIVariations();

        // 生成架构变体
        config.architectureVariations = architectureVariationGenerator.generateArchitectureVariations();

        // 生成资源变体
        config.resourceVariations = resourceVariationGenerator.generateResourceVariations(appName);

        // 设置输出路径
        config.outputPath = baseProjectRoot + "/project_" + (index + 1);

        // 添加到配置列表
        projectConfigs.add(config);
    }

    /**
     * 生成所有项目
     */
    public void generateAllProjects() throws Exception {
        System.out.println("========================================");
        System.out.println("开始批量生成项目");
        System.out.println("========================================");
        System.out.println("项目数量: " + projectCount);
        System.out.println("基础包名: " + basePackageName);
        System.out.println("基础路径: " + baseProjectRoot);
        System.out.println("========================================\n");

        for (int i = 0; i < projectConfigs.size(); i++) {
            ProjectConfig config = projectConfigs.get(i);
            System.out.println("\n========================================");
            System.out.println("生成项目 " + (i + 1) + "/" + projectCount);
            System.out.println("========================================");

            // 打印项目配置
            printProjectConfig(config);

            // 生成项目
            generateProject(config);

            System.out.println("\n项目 " + (i + 1) + " 生成完成！");
        }

        System.out.println("\n========================================");
        System.out.println("所有项目生成完成！");
        System.out.println("========================================");
    }

    /**
     * 生成单个项目
     */
    private void generateProject(ProjectConfig config) throws Exception {
        // 创建输出目录
        File outputDir = new File(config.outputPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // 设置生成器配置
        GeneratorConfig.packageName = config.packageName;
        GeneratorConfig.appName = config.appName;

        // 应用变体配置
        applyVariations(config);

        // 创建并运行生成器
        MainEntry generator = new MainEntry(
            config.outputPath,
            config.packageName
        );
        generator.generate();
    }

    /**
     * 应用变体配置
     */
    private void applyVariations(ProjectConfig config) {
        // 更新VariationManager
        VariationManager variationManager = VariationManager.getInstance();
        variationManager.setSeed(config.seed);

        // 应用代码变体
        for (Map.Entry<String, Object> entry : config.codeVariations.entrySet()) {
            variationManager.variations.put(entry.getKey(), entry.getValue());
        }

        // 应用UI变体
        for (Map.Entry<String, Object> entry : config.uiVariations.entrySet()) {
            variationManager.variations.put(entry.getKey(), entry.getValue());
        }

        // 应用架构变体
        for (Map.Entry<String, Object> entry : config.architectureVariations.entrySet()) {
            variationManager.variations.put(entry.getKey(), entry.getValue());
        }

        // 应用资源变体
        for (Map.Entry<String, Object> entry : config.resourceVariations.entrySet()) {
            variationManager.variations.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 打印项目配置
     */
    private void printProjectConfig(ProjectConfig config) {
        System.out.println("\n项目配置:");
        System.out.println("  包名: " + config.packageName);
        System.out.println("  应用名: " + config.appName);
        System.out.println("  种子: " + config.seed);
        System.out.println("  输出路径: " + config.outputPath);

        System.out.println("\n选中的模块:");
        for (int i = 0; i < config.modules.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + config.modules.get(i));
        }

        System.out.println("\n代码变体:");
        System.out.println("  数据库: " + config.codeVariations.get("database_impl"));
        System.out.println("  缓存: " + config.codeVariations.get("cache_impl"));
        System.out.println("  异步: " + config.codeVariations.get("async_impl"));

        System.out.println("\nUI变体:");
        System.out.println("  主题: " + config.uiVariations.get("theme"));
        System.out.println("  主色: " + config.uiVariations.get("primary_color"));
        System.out.println("  导航: " + config.uiVariations.get("navigation_style"));

        System.out.println("\n架构变体:");
        System.out.println("  架构模式: " + config.architectureVariations.get("architecture"));
        System.out.println("  依赖注入: " + config.architectureVariations.get("di_framework"));
        System.out.println("  异步框架: " + config.architectureVariations.get("async_framework"));

        System.out.println("\n资源变体:");
        System.out.println("  应用名称: " + config.resourceVariations.get("app_name"));
        System.out.println("  图标风格: " + config.resourceVariations.get("icon_style"));
        System.out.println("  字体风格: " + config.resourceVariations.get("font_style"));
    }

    /**
     * 生成项目配置报告
     */
    public void generateProjectReport(String reportPath) throws IOException {
        File reportFile = new File(reportPath);
        File parentDir = reportFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
            writer.println("========================================");
            writer.println("批量项目生成报告");
            writer.println("========================================");
            writer.println("生成时间: " + new Date());
            writer.println("项目数量: " + projectCount);
            writer.println("基础包名: " + basePackageName);
            writer.println("基础路径: " + baseProjectRoot);
            writer.println("========================================\n");

            for (int i = 0; i < projectConfigs.size(); i++) {
                ProjectConfig config = projectConfigs.get(i);
                writer.println("项目 " + (i + 1) + ":");
                writer.println("  包名: " + config.packageName);
                writer.println("  应用名: " + config.appName);
                writer.println("  种子: " + config.seed);
                writer.println("  输出路径: " + config.outputPath);
                writer.println("  模块: " + String.join(", ", config.modules));
                writer.println();
            }

            writer.println("========================================");
            writer.println("报告生成完成");
            writer.println("========================================");
        }

        System.out.println("\n项目配置报告已生成: " + reportPath);
    }

    /**
     * 获取项目配置列表
     */
    public List<ProjectConfig> getProjectConfigs() {
        return new ArrayList<>(projectConfigs);
    }
}
