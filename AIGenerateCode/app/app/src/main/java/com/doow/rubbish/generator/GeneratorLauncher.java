package com.doow.rubbish.generator;


public class GeneratorLauncher {

    private static final String DEFAULT_PROJECT_ROOT = System.getProperty("user.dir");
    private static final String DEFAULT_PACKAGE_NAME = "com.doow.rubbish";
    private static final String DEFAULT_CONFIG_FILE = "generator.properties";
    private static final String OUTPUT_DIR = "generated_code";

    public static void main(String[] args) {
        try {

            if (hasHelpArgument(args)) {
                printHelp();
                return;
            }


            if (hasBatchArgument(args)) {
                runBatchGeneration(args);
                return;
            }


            String projectRoot = parseProjectRoot(args);
            String packageName = parsePackageName(args);
            String configFile = parseConfigFile(args);


            ConfigLoader configLoader = new ConfigLoader();


            if (configFile != null && !configFile.isEmpty()) {
                System.out.println("加载配置文件: " + configFile);
                configLoader.load(configFile);
                configLoader.applyToGeneratorConfig();
            } else if (new java.io.File(DEFAULT_CONFIG_FILE).exists()) {
    
                System.out.println("加载默认配置文件: " + DEFAULT_CONFIG_FILE);
                configLoader.load(DEFAULT_CONFIG_FILE);
                configLoader.applyToGeneratorConfig();
            }


            if (projectRoot != null && !projectRoot.isEmpty()) {
                GeneratorConfig.packageName = packageName;
            }
            if (packageName != null && !packageName.isEmpty()) {
                GeneratorConfig.packageName = packageName;
            }

            // 验证配置
            ConfigValidator configValidator = new ConfigValidator();
            ConfigValidator.ValidationResult result = configValidator.validate(
                projectRoot != null ? projectRoot : DEFAULT_PROJECT_ROOT,
                packageName != null ? packageName : DEFAULT_PACKAGE_NAME
            );

            if (!result.isValid()) {
                System.err.println("\n配置验证失败，请检查配置！");
                result.print();
                System.exit(1);
            }

            // 显示配置信息
            printConfig(projectRoot, packageName);

            // 创建输出目录（在项目根目录下创建新文件夹）
            String baseProjectRoot = projectRoot != null ? projectRoot : DEFAULT_PROJECT_ROOT;
            String outputDir = baseProjectRoot + "/" + OUTPUT_DIR;
            java.io.File dir = new java.io.File(outputDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            System.out.println("输出目录: " + outputDir);

            // 创建并运行生成器
            MainEntry generator = new MainEntry(
                outputDir,
                packageName != null ? packageName : DEFAULT_PACKAGE_NAME
            );
            generator.generate();

            // 显示完成信息
            printCompletion();

        } catch (Exception e) {
            System.err.println("\n========================================");
            System.err.println("生成过程中发生错误");
            System.err.println("========================================");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 检查是否有帮助参数
     */
    private static boolean hasHelpArgument(String[] args) {
        for (String arg : args) {
            if ("-h".equals(arg) || "--help".equals(arg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解析项目根路径
     */
    private static String parseProjectRoot(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-p".equals(args[i]) || "--project".equals(args[i])) {
                if (i + 1 < args.length) {
                    return args[i + 1];
                }
            }
        }
        return null;
    }

    /**
     * 解析包名
     */
    private static String parsePackageName(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-pkg".equals(args[i]) || "--package".equals(args[i])) {
                if (i + 1 < args.length) {
                    return args[i + 1];
                }
            }
        }
        return null;
    }

    /**
     * 解析配置文件路径
     */
    private static String parseConfigFile(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-c".equals(args[i]) || "--config".equals(args[i])) {
                if (i + 1 < args.length) {
                    return args[i + 1];
                }
            }
        }
        return null;
    }

    /**
     * 打印配置信息
     */
    private static void printConfig(String projectRoot, String packageName) {
        System.out.println("========================================");
        System.out.println("代码生成器配置");
        System.out.println("========================================");
        System.out.println("项目根路径: " + (projectRoot != null ? projectRoot : DEFAULT_PROJECT_ROOT));
        System.out.println("包名: " + (packageName != null ? packageName : DEFAULT_PACKAGE_NAME));
        System.out.println("应用名: " + GeneratorConfig.appName);
        System.out.println("----------------------------------------");
        System.out.println("生成配置:");
        System.out.println("  包数量: " + GeneratorConfig.packageCount);
        System.out.println("  每包Activity数: " + GeneratorConfig.activityCountPerPackage);
        System.out.println("  Model数量: " + GeneratorConfig.modelCount);
        System.out.println("  ViewModel数量: " + GeneratorConfig.viewModelCount);
        System.out.println("  Repository数量: " + GeneratorConfig.repositoryCount);
        System.out.println("  Adapter数量: " + GeneratorConfig.adapterCount);
        System.out.println("  Fragment数量: " + GeneratorConfig.fragmentCount);
        System.out.println("  Manager数量: " + GeneratorConfig.managerCount);
        System.out.println("  Helper数量: " + GeneratorConfig.helperCount);
        System.out.println("========================================\n");
    }

    /**
     * 打印完成信息
     */
    private static void printCompletion() {
        System.out.println("\n========================================");
        System.out.println("生成完成！");
        System.out.println("========================================");
        System.out.println("使用说明:");
        System.out.println("1. 在Android Studio中打开项目");
        System.out.println("2. 等待Gradle同步完成");
        System.out.println("3. 运行应用");
        System.out.println("========================================");
    }

    /**
     * 显示帮助信息
     */
    public static void printHelp() {
        System.out.println("========================================");
        System.out.println("代码生成器帮助");
        System.out.println("========================================");
        System.out.println("用法: java GeneratorLauncher [选项]");
        System.out.println();
        System.out.println("选项:");
        System.out.println("  -p, --project <path>     指定项目根路径");
        System.out.println("  -pkg, --package <name>   指定包名");
        System.out.println("  -c, --config <file>      指定配置文件");
        System.out.println("  -h, --help               显示帮助信息");
        System.out.println();
        System.out.println("批量生成选项:");
        System.out.println("  -b, --batch               启用批量生成模式");
        System.out.println("  -n, --count <number>      指定生成项目数量");
        System.out.println("  -s, --seed <number>       指定随机种子");
        System.out.println();
        System.out.println("示例:");
        System.out.println("  java GeneratorLauncher");
        System.out.println("  java GeneratorLauncher -p /path/to/project -pkg com.myapp");
        System.out.println("  java GeneratorLauncher -c myconfig.properties");
        System.out.println("  java GeneratorLauncher -p /path/to/project -pkg com.myapp -c myconfig.properties");
        System.out.println();
        System.out.println("批量生成示例:");
        System.out.println("  java GeneratorLauncher -b -n 10 -p /path/to/projects -pkg com.myapp");
        System.out.println("  java GeneratorLauncher --batch --count 500 --seed 12345");
        System.out.println();
        System.out.println("配置文件:");
        System.out.println("  如果不指定配置文件，生成器将尝试加载默认的generator.properties");
        System.out.println("  配置文件中的值可以被命令行参数覆盖");
        System.out.println("========================================");
    }

    /**
     * 检查是否有批量生成参数
     */
    private static boolean hasBatchArgument(String[] args) {
        for (String arg : args) {
            if ("-b".equals(arg) || "--batch".equals(arg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 运行批量生成模式
     */
    private static void runBatchGeneration(String[] args) {
        try {
            // 解析批量生成参数
            String projectRoot = parseProjectRoot(args);
            String packageName = parsePackageName(args);
            int projectCount = parseProjectCount(args);
            long baseSeed = parseSeed(args);

            // 使用默认值
            if (projectRoot == null || projectRoot.isEmpty()) {
                projectRoot = DEFAULT_PROJECT_ROOT;
            }
            if (packageName == null || packageName.isEmpty()) {
                packageName = DEFAULT_PACKAGE_NAME;
            }
            if (baseSeed == 0) {
                baseSeed = System.currentTimeMillis();
            }

            // 初始化批量生成器
            BatchProjectGenerator batchGenerator = BatchProjectGenerator.getInstance();
            batchGenerator.initialize(projectRoot, packageName, projectCount, baseSeed);

            // 生成所有项目配置
            batchGenerator.generateAllProjectConfigs();

            // 生成所有项目
            batchGenerator.generateAllProjects();

            // 生成项目报告
            String reportPath = projectRoot + "/batch_report.txt";
            batchGenerator.generateProjectReport(reportPath);

        } catch (Exception e) {
            System.err.println("\n========================================");
            System.err.println("批量生成过程中发生错误");
            System.err.println("========================================");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 解析项目数量
     */
    private static int parseProjectCount(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-n".equals(args[i]) || "--count".equals(args[i])) {
                if (i + 1 < args.length) {
                    try {
                        return Integer.parseInt(args[i + 1]);
                    } catch (NumberFormatException e) {
                        System.err.println("无效的项目数量: " + args[i + 1]);
                        System.exit(1);
                    }
                }
            }
        }
        return 1; // 默认生成1个项目
    }

    /**
     * 解析随机种子
     */
    private static long parseSeed(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-s".equals(args[i]) || "--seed".equals(args[i])) {
                if (i + 1 < args.length) {
                    try {
                        return Long.parseLong(args[i + 1]);
                    } catch (NumberFormatException e) {
                        System.err.println("无效的种子值: " + args[i + 1]);
                        System.exit(1);
                    }
                }
            }
        }
        return 0; // 使用当前时间作为种子
    }
}
