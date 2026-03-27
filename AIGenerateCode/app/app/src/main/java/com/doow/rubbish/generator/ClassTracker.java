package com.doow.rubbish.generator;

import java.util.ArrayList;
import java.util.List;


public class ClassTracker {

    private static final List<String> activityList = new ArrayList<>();
    private static final List<String> fragmentList = new ArrayList<>();
    private static final List<String> adapterList = new ArrayList<>();
    private static final List<String> viewModelList = new ArrayList<>();
    private static final List<String> repositoryList = new ArrayList<>();
    private static final List<String> managerList = new ArrayList<>();
    private static final List<String> helperList = new ArrayList<>();
    private static final List<String> modelList = new ArrayList<>();


    public static void addActivity(String className) {
        activityList.add(className);
    }


    public static void addFragment(String className) {
        fragmentList.add(className);
    }


    public static void addAdapter(String className) {
        adapterList.add(className);
    }


    public static void addViewModel(String className) {
        viewModelList.add(className);
    }


    public static void addRepository(String className) {
        repositoryList.add(className);
    }


    public static void addManager(String className) {
        managerList.add(className);
    }

    // 添加Helper
    public static void addHelper(String className) {
        helperList.add(className);
    }

    // 添加Model
    public static void addModel(String className) {
        modelList.add(className);
    }

    // 获取随机Activity
    public static String getRandomActivity() {
        if (activityList.isEmpty()) return null;
        return activityList.get(RandomUtils.between(0, activityList.size() - 1));
    }

    // 获取随机Fragment
    public static String getRandomFragment() {
        if (fragmentList.isEmpty()) return null;
        return fragmentList.get(RandomUtils.between(0, fragmentList.size() - 1));
    }

    // 获取随机Adapter
    public static String getRandomAdapter() {
        if (adapterList.isEmpty()) return null;
        return adapterList.get(RandomUtils.between(0, adapterList.size() - 1));
    }

    // 获取随机ViewModel
    public static String getRandomViewModel() {
        if (viewModelList.isEmpty()) return null;
        return viewModelList.get(RandomUtils.between(0, viewModelList.size() - 1));
    }

    // 获取随机Repository
    public static String getRandomRepository() {
        if (repositoryList.isEmpty()) return null;
        return repositoryList.get(RandomUtils.between(0, repositoryList.size() - 1));
    }

    // 获取随机Manager
    public static String getRandomManager() {
        if (managerList.isEmpty()) return null;
        return managerList.get(RandomUtils.between(0, managerList.size() - 1));
    }

    // 获取随机Helper
    public static String getRandomHelper() {
        if (helperList.isEmpty()) return null;
        return helperList.get(RandomUtils.between(0, helperList.size() - 1));
    }

    // 获取随机Model
    public static String getRandomModel() {
        if (modelList.isEmpty()) return null;
        return modelList.get(RandomUtils.between(0, modelList.size() - 1));
    }

    // 获取所有Activity
    public static List<String> getAllActivities() {
        return new ArrayList<>(activityList);
    }

    // 获取所有Fragment
    public static List<String> getAllFragments() {
        return new ArrayList<>(fragmentList);
    }

    // 获取所有Adapter
    public static List<String> getAllAdapters() {
        return new ArrayList<>(adapterList);
    }

    // 获取所有ViewModel
    public static List<String> getAllViewModels() {
        return new ArrayList<>(viewModelList);
    }

    // 获取所有Repository
    public static List<String> getAllRepositories() {
        return new ArrayList<>(repositoryList);
    }

    // 获取所有Manager
    public static List<String> getAllManagers() {
        return new ArrayList<>(managerList);
    }

    // 获取所有Helper
    public static List<String> getAllHelpers() {
        return new ArrayList<>(helperList);
    }

    // 获取所有Model
    public static List<String> getAllModels() {
        return new ArrayList<>(modelList);
    }

    // 清空所有类
    public static void clearAll() {
        activityList.clear();
        fragmentList.clear();
        adapterList.clear();
        viewModelList.clear();
        repositoryList.clear();
        managerList.clear();
        helperList.clear();
        modelList.clear();
    }

    // 检查是否有类
    public static boolean hasClasses() {
        return !activityList.isEmpty() || 
               !fragmentList.isEmpty() || 
               !adapterList.isEmpty() || 
               !viewModelList.isEmpty() ||
               !repositoryList.isEmpty() ||
               !managerList.isEmpty() ||
               !helperList.isEmpty() ||
               !modelList.isEmpty();
    }
}
