package com.doow.rubbish.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ConfigLoader {

    private static final String DEFAULT_CONFIG_FILE = "generator.properties";
    private static final String PROJECT_ROOT_KEY = "project.root";
    private static final String PACKAGE_NAME_KEY = "package.name";
    private static final String APP_NAME_KEY = "app.name";

    private Properties properties;
    private String configFilePath;

    public ConfigLoader() {
        this.properties = new Properties();
    }


    public void load() throws IOException {
        load(DEFAULT_CONFIG_FILE);
    }


    public void load(String configPath) throws IOException {
        this.configFilePath = configPath;
        File configFile = new File(configPath);

        if (!configFile.exists()) {
            throw new IOException("配置文件不存在: " + configPath);
        }

        if (!configFile.isFile()) {
            throw new IOException("配置路径不是文件: " + configPath);
        }

        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
            System.out.println("配置文件加载成功: " + configPath);
        } catch (IOException e) {
            throw new IOException("加载配置文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取项目根路径
     */
    public String getProjectRoot() {
        return properties.getProperty(PROJECT_ROOT_KEY, "");
    }

    /**
     * 获取包名
     */
    public String getPackageName() {
        return properties.getProperty(PACKAGE_NAME_KEY, "");
    }

    /**
     * 获取应用名
     */
    public String getAppName() {
        return properties.getProperty(APP_NAME_KEY, "");
    }

    /**
     * 获取整型配置值
     */
    public int getIntValue(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            System.err.println("配置值格式错误: " + key + " = " + value);
            return defaultValue;
        }
    }

    /**
     * 获取布尔型配置值
     */
    public boolean getBooleanValue(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        return Boolean.parseBoolean(value.trim());
    }

    /**
     * 获取字符串配置值
     */
    public String getStringValue(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 应用配置到GeneratorConfig
     */
    public void applyToGeneratorConfig() {
        // 应用基本配置
        String appName = getAppName();
        if (!appName.isEmpty()) {
            GeneratorConfig.appName = appName;
        }

        String packageName = getPackageName();
        if (!packageName.isEmpty()) {
            GeneratorConfig.packageName = packageName;
        }

        // 应用数量配置
        GeneratorConfig.packageCount = getIntValue("package.count", GeneratorConfig.packageCount);
        GeneratorConfig.activityCountPerPackage = getIntValue("activity.count.per.package", 
            GeneratorConfig.activityCountPerPackage);
        GeneratorConfig.modelCount = getIntValue("model.count", GeneratorConfig.modelCount);
        GeneratorConfig.viewModelCount = getIntValue("viewmodel.count", GeneratorConfig.viewModelCount);
        GeneratorConfig.repositoryCount = getIntValue("repository.count", GeneratorConfig.repositoryCount);
        GeneratorConfig.adapterCount = getIntValue("adapter.count", GeneratorConfig.adapterCount);
        GeneratorConfig.fragmentCount = getIntValue("fragment.count", GeneratorConfig.fragmentCount);
        GeneratorConfig.managerCount = getIntValue("manager.count", GeneratorConfig.managerCount);
        GeneratorConfig.helperCount = getIntValue("helper.count", GeneratorConfig.helperCount);

        // 应用资源配置
        GeneratorConfig.drawableMin = getIntValue("drawable.min", GeneratorConfig.drawableMin);
        GeneratorConfig.drawableMax = getIntValue("drawable.max", GeneratorConfig.drawableMax);
        GeneratorConfig.layoutMin = getIntValue("layout.min", GeneratorConfig.layoutMin);
        GeneratorConfig.layoutMax = getIntValue("layout.max", GeneratorConfig.layoutMax);
        GeneratorConfig.stringMin = getIntValue("string.min", GeneratorConfig.stringMin);
        GeneratorConfig.stringMax = getIntValue("string.max", GeneratorConfig.stringMax);
        GeneratorConfig.rawMin = getIntValue("raw.min", GeneratorConfig.rawMin);
        GeneratorConfig.rawMax = getIntValue("raw.max", GeneratorConfig.rawMax);
    }

    /**
     * 打印当前配置
     */
    public void printConfig() {
        System.out.println("\n========================================");
        System.out.println("当前配置");
        System.out.println("========================================");
        System.out.println("配置文件: " + configFilePath);
        System.out.println("----------------------------------------");
        System.out.println("基本配置:");
        System.out.println("  项目根路径: " + getProjectRoot());
        System.out.println("  包名: " + getPackageName());
        System.out.println("  应用名: " + getAppName());
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
        System.out.println("----------------------------------------");
        System.out.println("资源配置:");
        System.out.println("  Drawable: " + GeneratorConfig.drawableMin + "-" + GeneratorConfig.drawableMax);
        System.out.println("  Layout: " + GeneratorConfig.layoutMin + "-" + GeneratorConfig.layoutMax);
        System.out.println("  String: " + GeneratorConfig.stringMin + "-" + GeneratorConfig.stringMax);
        System.out.println("  Raw: " + GeneratorConfig.rawMin + "-" + GeneratorConfig.rawMax);
        System.out.println("========================================");
    }

    /**
     * 获取配置文件路径
     */
    public String getConfigFilePath() {
        return configFilePath;
    }
}
