package com.doow.rubbish.generator;

import java.util.*;

/**
 * 模块选择器 - 第一步优化
 * 负责从所有可用模块中随机选择模块组合
 * 确保每个项目的模块组合不同且合理
 */
public class ModuleSelector {

    private static ModuleSelector instance;
    private Random random;

    // 所有可用的模块
    private static final String[] ALL_MODULES = {
        "AnimationModuleGenerator",
        "CacheModuleGenerator",
        "CalculatorModuleGenerator",
        "ChartModuleGenerator",
        "ConverterModuleGenerator",
        "DatabaseModuleGenerator",
        "FileModuleGenerator",
        "GestureModuleGenerator",
        "GraphicsModuleGenerator",
        "LogModuleGenerator",
        "NotepadModuleGenerator",
        "PerformanceModuleGenerator",
        "TimerModuleGenerator",
        "TodoModuleGenerator"
    };

    // 模块分组（确保功能互补）
    private static final Map<String, String[]> MODULE_GROUPS = new HashMap<>();

    static {
        // 核心功能组（每个项目至少选择1个）
        MODULE_GROUPS.put("core", new String[]{
            "CacheModuleGenerator",
            "DatabaseModuleGenerator",
            "FileModuleGenerator"
        });

        // 工具功能组（每个项目至少选择1个）
        MODULE_GROUPS.put("utility", new String[]{
            "CalculatorModuleGenerator",
            "ConverterModuleGenerator",
            "TimerModuleGenerator"
        });

        // UI功能组（可选）
        MODULE_GROUPS.put("ui", new String[]{
            "AnimationModuleGenerator",
            "ChartModuleGenerator",
            "GraphicsModuleGenerator",
            "GestureModuleGenerator"
        });

        // 业务功能组（可选）
        MODULE_GROUPS.put("business", new String[]{
            "NotepadModuleGenerator",
            "TodoModuleGenerator",
            "LogModuleGenerator",
            "PerformanceModuleGenerator"
        });
    }

    // 已使用的模块组合（用于避免重复）
    private Set<String> usedCombinations;

    private ModuleSelector() {
        random = new Random();
        usedCombinations = new HashSet<>();
    }

    public static synchronized ModuleSelector getInstance() {
        if (instance == null) {
            instance = new ModuleSelector();
        }
        return instance;
    }

    /**
     * 设置随机种子
     */
    public void setSeed(long seed) {
        random = new Random(seed);
        usedCombinations.clear();
    }

    /**
     * 随机选择模块组合
     * @param minModules 最小模块数量
     * @param maxModules 最大模块数量
     * @return 选中的模块列表
     */
    public List<String> selectModules(int minModules, int maxModules) {
        List<String> selectedModules = new ArrayList<>();

        // 1. 从核心功能组中至少选择1个
        String[] coreModules = MODULE_GROUPS.get("core");
        selectedModules.add(coreModules[random.nextInt(coreModules.length)]);

        // 2. 从工具功能组中至少选择1个
        String[] utilityModules = MODULE_GROUPS.get("utility");
        selectedModules.add(utilityModules[random.nextInt(utilityModules.length)]);

        // 3. 计算还需要选择的模块数量
        int remainingCount = random.nextInt(maxModules - minModules + 1) + minModules - selectedModules.size();

        // 4. 从剩余的模块中随机选择
        List<String> remainingModules = getRemainingModules(selectedModules);

        while (remainingCount > 0 && !remainingModules.isEmpty()) {
            int index = random.nextInt(remainingModules.size());
            selectedModules.add(remainingModules.get(index));
            remainingModules.remove(index);
            remainingCount--;
        }

        // 5. 打乱顺序
        Collections.shuffle(selectedModules, random);

        return selectedModules;
    }

    /**
     * 获取未被选中的模块列表
     */
    private List<String> getRemainingModules(List<String> selectedModules) {
        List<String> remaining = new ArrayList<>();
        Set<String> selectedSet = new HashSet<>(selectedModules);

        for (String module : ALL_MODULES) {
            if (!selectedSet.contains(module)) {
                remaining.add(module);
            }
        }

        return remaining;
    }

    /**
     * 生成模块组合的唯一标识
     */
    private String generateCombinationId(List<String> modules) {
        List<String> sorted = new ArrayList<>(modules);
        Collections.sort(sorted);
        return String.join("|", sorted);
    }

    /**
     * 检查模块组合是否已被使用
     */
    public boolean isCombinationUsed(List<String> modules) {
        String combinationId = generateCombinationId(modules);
        return usedCombinations.contains(combinationId);
    }

    /**
     * 记录已使用的模块组合
     */
    public void markCombinationUsed(List<String> modules) {
        String combinationId = generateCombinationId(modules);
        usedCombinations.add(combinationId);
    }

    /**
     * 获取所有可用模块
     */
    public String[] getAllModules() {
        return ALL_MODULES.clone();
    }

    /**
     * 获取模块分组
     */
    public Map<String, String[]> getModuleGroups() {
        return new HashMap<>(MODULE_GROUPS);
    }

    /**
     * 打印选中的模块
     */
    public void printSelectedModules(List<String> modules) {
        System.out.println("========================================");
        System.out.println("选中的模块组合");
        System.out.println("========================================");
        System.out.println("模块数量: " + modules.size());
        System.out.println("模块列表:");
        for (int i = 0; i < modules.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + modules.get(i));
        }
        System.out.println("========================================");
    }
}
