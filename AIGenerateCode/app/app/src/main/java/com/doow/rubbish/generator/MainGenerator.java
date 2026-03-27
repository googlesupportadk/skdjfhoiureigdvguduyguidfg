package com.doow.rubbish.generator;

import com.doow.rubbish.generator.code.*;
import com.doow.rubbish.generator.resource.*;


public class MainGenerator {
    private String projectRoot;
    private String packageName;

    public MainGenerator(String projectRoot, String packageName) {
        this.projectRoot = projectRoot;
        this.packageName = packageName;
    }

    public void generate() throws Exception {
        System.out.println("========== 开始生成项目 ==========");
        System.out.println("包名: " + packageName);
        System.out.println("应用名: " + GeneratorConfig.appName);


        ResourceTracker.clearAll();
        ClassTracker.clearAll();


        System.out.println("\n========== 第一阶段：生成资源 ==========");
        generateResources();


        System.out.println("\n========== 第二阶段：生成Model类 ==========");
        generateModels();


        System.out.println("\n========== 第三阶段：生成Helper类 ==========");
        generateHelpers();


        System.out.println("\n========== 第四阶段：生成Manager类 ==========");
        generateManagers();


        System.out.println("\n========== 第五阶段：生成Repository类 ==========");
        generateRepositories();


        System.out.println("\n========== 第六阶段：生成ViewModel类 ==========");
        generateViewModels();

        // 7. 生成Adapter类
        System.out.println("\n========== 第七阶段：生成Adapter类 ==========");
        generateAdapters();

        // 8. 生成Fragment类
        System.out.println("\n========== 第八阶段：生成Fragment类 ==========");
        generateFragments();

        // 9. 生成Activity类
        System.out.println("\n========== 第九阶段：生成Activity类 ==========");
        generateActivities();

        // 10. 生成Manifest
        System.out.println("\n========== 第十阶段：生成Manifest ==========");
        generateManifest();

        System.out.println("\n========== 项目生成完成 ==========");
    }

    private void generateResources() throws Exception {
        DrawableGenerator drawableGenerator = new DrawableGenerator(projectRoot, packageName);
        drawableGenerator.generateAll();

        LayoutGenerator layoutGenerator = new LayoutGenerator(projectRoot, packageName);
        layoutGenerator.generateAll();

        ValuesGenerator valuesGenerator = new ValuesGenerator(projectRoot, packageName);
        valuesGenerator.generateAll();

        RawGenerator rawGenerator = new RawGenerator(projectRoot, packageName);
        rawGenerator.generateAll();
    }

    private void generateModels() throws Exception {
        ModelGenerator modelGenerator = new ModelGenerator(projectRoot, packageName);
        modelGenerator.generateAll();
    }

    private void generateHelpers() throws Exception {
        HelperGenerator helperGenerator = new HelperGenerator(projectRoot, packageName);
        helperGenerator.generateAll();
    }

    private void generateManagers() throws Exception {
        ManagerGenerator managerGenerator = new ManagerGenerator(projectRoot, packageName);
        managerGenerator.generateAll();
    }

    private void generateRepositories() throws Exception {
        RepositoryGenerator repositoryGenerator = new RepositoryGenerator(projectRoot, packageName);
        repositoryGenerator.generateAll();
    }

    private void generateViewModels() throws Exception {
        ViewModelGenerator viewModelGenerator = new ViewModelGenerator(projectRoot, packageName);
        viewModelGenerator.generateAll();
    }

    private void generateAdapters() throws Exception {
        AdapterGenerator adapterGenerator = new AdapterGenerator(projectRoot, packageName);
        adapterGenerator.generateAll();
    }

    private void generateFragments() throws Exception {
        FragmentGenerator fragmentGenerator = new FragmentGenerator(projectRoot, packageName);
        fragmentGenerator.generateAll();
    }

    private void generateActivities() throws Exception {
        ActivityGenerator activityGenerator = new ActivityGenerator(projectRoot, packageName);
        activityGenerator.generateAll();
    }

    private void generateManifest() throws Exception {
        ManifestGenerator manifestGenerator = new ManifestGenerator(projectRoot, packageName);
        manifestGenerator.generate();
    }

}
