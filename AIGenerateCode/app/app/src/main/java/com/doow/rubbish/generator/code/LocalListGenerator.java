package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalListGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] LIST_TYPES = {
            "ArrayList", "LinkedList", "CopyOnWriteArrayList", "Vector", "Stack",
            "HashSet", "TreeSet", "LinkedHashSet", "PriorityQueue", "ArrayDeque",
            "ConcurrentLinkedQueue", "LinkedBlockingQueue", "ArrayBlockingQueue", "PriorityBlockingQueue"
    };

    private static final String[] OPERATION_TYPES = {
            "add", "remove", "contains", "get", "clear", "sort", "reverse",
            "add_all", "remove_all", "contains_all", "get_all", "retain_all",
            "remove_if", "remove_all_if", "replace_all", "sub_list", "shuffle",
            "rotate", "swap", "fill", "binary_search", "sort_comparator"
    };

    // 功能标志 - 确保所有字段和方法都会被使用
    private boolean useAsyncOperations;
    private boolean useFiltering;
    private boolean useTransformation;
    private boolean useSorting;
    private boolean useBatchOperations;
    private boolean useStatistics;
    private boolean useConversion;
    private boolean useValidation;

    public LocalListGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        initializeFeatureFlags();
    }

    /**
     * 初始化功能标志，确保随机性和多样性
     */
    private void initializeFeatureFlags() {
        useAsyncOperations = RandomUtils.randomBoolean();
        useFiltering = RandomUtils.randomBoolean();
        useTransformation = RandomUtils.randomBoolean();
        useSorting = RandomUtils.randomBoolean();
        useBatchOperations = RandomUtils.randomBoolean();
        useStatistics = RandomUtils.randomBoolean();
        useConversion = RandomUtils.randomBoolean();
        useValidation = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成列表类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("List");
            generateListClass(className, asyncHandler);
        }
    }

    private void generateListClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("list"));

        // 生成基础导入
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.*"));
        sb.append(generateImportStatement("java.util.concurrent.CopyOnWriteArrayList"));

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        } else if (asyncHandler.contains("rxjava")) {
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
        }

        if (useFiltering || useTransformation || useSorting) {
            sb.append(generateImportStatement("java.util.stream.Collectors"));
        }

        if (useStatistics) {
            sb.append(generateImportStatement("java.util.function.*"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 随机选择列表类型和操作类型
        String listType = LIST_TYPES[RandomUtils.between(0, LIST_TYPES.length - 1)];
        String operationType = OPERATION_TYPES[RandomUtils.between(0, OPERATION_TYPES.length - 1)];

        // 生成类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成常量字段
        String tagVarName = RandomUtils.generateWord(6);
        String listTypeVarName = RandomUtils.generateWord(6);
        String operationTypeVarName = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVarName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(listTypeVarName).append(" = \"").append(listType).append("\";\n");
        sb.append("    private static final String ").append(operationTypeVarName).append(" = \"").append(operationType).append("\";\n\n");

        // 生成实例字段
        String listVarName = RandomUtils.generateWord(6);
        String randomVarName = RandomUtils.generateWord(6);
        String filterPredicateVarName = RandomUtils.generateWord(6);
        String transformFunctionVarName = RandomUtils.generateWord(6);
        String comparatorVarName = RandomUtils.generateWord(6);
        String batchSizeVarName = RandomUtils.generateWord(6);
        String statisticsVarName = RandomUtils.generateWord(6);
        String validatorVarName = RandomUtils.generateWord(6);

        sb.append("    private List<String> ").append(listVarName).append(";\n");
        sb.append("    private Random ").append(randomVarName).append(";\n");

        // 根据功能标志添加条件字段
        if (useFiltering) {
            sb.append("    private Predicate<String> ").append(filterPredicateVarName).append(";\n");
        }

        if (useTransformation) {
            sb.append("    private Function<String, String> ").append(transformFunctionVarName).append(";\n");
        }

        if (useSorting) {
            sb.append("    private Comparator<String> ").append(comparatorVarName).append(";\n");
        }

        if (useBatchOperations) {
            sb.append("    private int ").append(batchSizeVarName).append(";\n");
        }

        if (useStatistics) {
            sb.append("    private LongSummaryStatistics ").append(statisticsVarName).append(";\n");
        }

        if (useValidation) {
            sb.append("    private Predicate<String> ").append(validatorVarName).append(";\n");
        }

        sb.append("\n");

        // 生成构造函数
        generateConstructor(sb, className, listVarName, randomVarName, filterPredicateVarName,
                transformFunctionVarName, comparatorVarName, batchSizeVarName,
                statisticsVarName, validatorVarName, listType);

        // 生成基础方法
        generateBasicMethods(sb, listVarName, randomVarName, tagVarName, operationType);

        // 根据功能标志添加条件方法
        if (useAsyncOperations) {
            generateAsyncMethods(sb, listVarName, tagVarName, asyncHandler);
        }

        if (useFiltering) {
            generateFilteringMethods(sb, listVarName, filterPredicateVarName, tagVarName);
        }

        if (useTransformation) {
            generateTransformationMethods(sb, listVarName, transformFunctionVarName, tagVarName);
        }

        if (useSorting) {
            generateSortingMethods(sb, listVarName, comparatorVarName, tagVarName);
        }

        if (useBatchOperations) {
            generateBatchOperationsMethods(sb, listVarName, batchSizeVarName, tagVarName);
        }

        if (useStatistics) {
            generateStatisticsMethods(sb, listVarName, statisticsVarName, tagVarName);
        }

        if (useConversion) {
            generateConversionMethods(sb, listVarName, tagVarName);
        }

        if (useValidation) {
            generateValidationMethods(sb, listVarName, validatorVarName, tagVarName);
        }

        // 生成使用所有字段和方法的示例方法
        generateExampleUsageMethod(sb, className, listVarName, randomVarName, filterPredicateVarName,
                transformFunctionVarName, comparatorVarName, batchSizeVarName,
                statisticsVarName, validatorVarName, tagVarName);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "list");
    }

    /**
     * 生成构造函数
     */
    private void generateConstructor(StringBuilder sb, String className, String listVarName,
                                     String randomVarName, String filterPredicateVarName,
                                     String transformFunctionVarName, String comparatorVarName,
                                     String batchSizeVarName, String statisticsVarName,
                                     String validatorVarName, String listType) {
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this.").append(randomVarName).append(" = new Random();\n");
        sb.append("        initialize").append(className).append("();\n");
        sb.append("    }\n\n");

        sb.append("    private void initialize").append(className).append("() {\n");
        sb.append("        switch (\"").append(listType).append("\") {\n");
        sb.append("            case \"ArrayList\":\n");
        sb.append("                ").append(listVarName).append(" = new ArrayList<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"LinkedList\":\n");
        sb.append("                ").append(listVarName).append(" = new LinkedList<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"CopyOnWriteArrayList\":\n");
        sb.append("                ").append(listVarName).append(" = new CopyOnWriteArrayList<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"Vector\":\n");
        sb.append("                ").append(listVarName).append(" = new Vector<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"Stack\":\n");
        sb.append("                ").append(listVarName).append(" = new Stack<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"HashSet\":\n");
        sb.append("                ").append(listVarName).append(" = new ArrayList<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"TreeSet\":\n");
        sb.append("                ").append(listVarName).append(" = new ArrayList<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"LinkedHashSet\":\n");
        sb.append("                ").append(listVarName).append(" = new ArrayList<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"PriorityQueue\":\n");
        sb.append("                ").append(listVarName).append(" = new ArrayList<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"ArrayDeque\":\n");
        sb.append("                ").append(listVarName).append(" = new ArrayList<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"ConcurrentLinkedQueue\":\n");
        sb.append("                ").append(listVarName).append(" = new ArrayList<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"LinkedBlockingQueue\":\n");
        sb.append("                ").append(listVarName).append(" = new ArrayList<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"ArrayBlockingQueue\":\n");
        sb.append("                ").append(listVarName).append(" = new ArrayList<>();\n");
        sb.append("                break;\n");
        sb.append("            case \"PriorityBlockingQueue\":\n");
        sb.append("                ").append(listVarName).append(" = new ArrayList<>();\n");
        sb.append("                break;\n");
        sb.append("            default:\n");
        sb.append("                ").append(listVarName).append(" = new ArrayList<>();\n");
        sb.append("                break;\n");
        sb.append("        }\n");

        if (useFiltering) {
            sb.append("        ").append(filterPredicateVarName).append(" = item -> item != null && !item.isEmpty();\n");
        }

        if (useTransformation) {
            sb.append("        ").append(transformFunctionVarName).append(" = item -> item.toUpperCase();\n");
        }

        if (useSorting) {
            sb.append("        ").append(comparatorVarName).append(" = String::compareTo;\n");
        }

        if (useBatchOperations) {
            sb.append("        ").append(batchSizeVarName).append(" = ").append(RandomUtils.between(10, 50)).append(";\n");
        }

        if (useStatistics) {
            sb.append("        ").append(statisticsVarName).append(" = new LongSummaryStatistics();\n");
        }

        if (useValidation) {
            sb.append("        ").append(validatorVarName).append(" = item -> item != null && item.length() > 0;\n");
        }

        sb.append("    }\n\n");
    }

    /**
     * 生成基础方法
     */
    private void generateBasicMethods(StringBuilder sb, String listVarName, String randomVarName,
                                      String tagVarName, String operationType) {
        // 生成操作方法
        String operationMethodName = RandomUtils.generateWord(6);
        String itemParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(operationMethodName).append("(String ")
                .append(itemParamName).append(") {\n");
        sb.append("        if (").append(itemParamName).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        switch (\"").append(operationType).append("\") {\n");
        sb.append("            case \"add\":\n");
        sb.append("                ").append(listVarName).append(".add(").append(itemParamName).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"remove\":\n");
        sb.append("                ").append(listVarName).append(".remove(").append(itemParamName).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"contains\":\n");
        sb.append("                boolean contains = ").append(listVarName).append(".contains(")
                .append(itemParamName).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"get\":\n");
        sb.append("                int index = ").append(listVarName).append(".indexOf(")
                .append(itemParamName).append(");\n");
        sb.append("                if (index >= 0) {\n");
        sb.append("                    String value = ").append(listVarName).append(".get(index);\n");
        sb.append("                }\n");
        sb.append("                break;\n");
        sb.append("            case \"clear\":\n");
        sb.append("                ").append(listVarName).append(".clear();\n");
        sb.append("                break;\n");
        sb.append("            case \"sort\":\n");
        sb.append("                Collections.sort(").append(listVarName).append(");\n");
        sb.append("                break;\n");
        sb.append("            case \"reverse\":\n");
        sb.append("                Collections.reverse(").append(listVarName).append(");\n");
        sb.append("                break;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成索引操作方法
        String indexOperationMethodName = RandomUtils.generateWord(6);
        String indexParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(indexOperationMethodName).append("(int ")
                .append(indexParamName).append(") {\n");
        sb.append("        switch (\"").append(operationType).append("\") {\n");
        sb.append("            case \"remove\":\n");
        sb.append("                if (").append(indexParamName).append(" >= 0 && ")
                .append(indexParamName).append(" < ").append(listVarName).append(".size()) {\n");
        sb.append("                    ").append(listVarName).append(".remove(").append(indexParamName).append(");\n");
        sb.append("                }\n");
        sb.append("                break;\n");
        sb.append("            case \"get\":\n");
        sb.append("                if (").append(indexParamName).append(" >= 0 && ")
                .append(indexParamName).append(" < ").append(listVarName).append(".size()) {\n");
        sb.append("                    String value = ").append(listVarName).append(".get(")
                .append(indexParamName).append(");\n");
        sb.append("                }\n");
        sb.append("                break;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成大小方法
        String sizeMethodName = RandomUtils.generateWord(6);

        sb.append("    public int ").append(sizeMethodName).append("() {\n");
        sb.append("        return ").append(listVarName).append(".size();\n");
        sb.append("    }\n\n");

        // 生成是否为空方法
        String isEmptyMethodName = RandomUtils.generateWord(6);

        sb.append("    public boolean ").append(isEmptyMethodName).append("() {\n");
        sb.append("        return ").append(listVarName).append(".isEmpty();\n");
        sb.append("    }\n\n");

        // 生成随机获取方法
        String randomGetMethodName = RandomUtils.generateWord(6);

        sb.append("    public String ").append(randomGetMethodName).append("() {\n");
        sb.append("        if (").append(listVarName).append(".isEmpty()) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        int index = ").append(randomVarName).append(".nextInt(")
                .append(listVarName).append(".size());\n");
        sb.append("        return ").append(listVarName).append(".get(index);\n");
        sb.append("    }\n\n");

        // 生成获取副本方法
        String getCopyMethodName = RandomUtils.generateWord(6);

        sb.append("    public List<String> ").append(getCopyMethodName).append("() {\n");
        sb.append("        return new ArrayList<>(").append(listVarName).append(");\n");
        sb.append("    }\n\n");

        // 生成清除方法
        String clearMethodName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(clearMethodName).append("() {\n");
        sb.append("        ").append(listVarName).append(".clear();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成异步操作方法
     */
    private void generateAsyncMethods(StringBuilder sb, String listVarName, String tagVarName,
                                      String asyncHandler) {
        String asyncAddMethodName = RandomUtils.generateWord(6);
        String asyncRemoveMethodName = RandomUtils.generateWord(6);
        String itemParamName = RandomUtils.generateWord(6);

        if (asyncHandler.contains("coroutines")) {
            // 使用协程的异步方法
            sb.append("    public void ").append(asyncAddMethodName).append("(String ")
                    .append(itemParamName).append(") {\n");
            sb.append("        new CoroutineScope(Dispatchers.IO).launch(() -> {\n");
            sb.append("            ").append(listVarName).append(".add(").append(itemParamName).append(");\n");
            sb.append("        });\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncRemoveMethodName).append("(String ")
                    .append(itemParamName).append(") {\n");
            sb.append("        new CoroutineScope(Dispatchers.IO).launch(() -> {\n");
            sb.append("            ").append(listVarName).append(".remove(").append(itemParamName).append(");\n");
            sb.append("        });\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            // 使用RxJava的异步方法
            sb.append("    public void ").append(asyncAddMethodName).append("(String ")
                    .append(itemParamName).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            ").append(listVarName).append(".add(").append(itemParamName).append(");\n");
            sb.append("            return true;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.single())\n");
            sb.append("          .subscribe(\n");
            sb.append("              success -> {},\n");
            sb.append("              error -> {}\n");
            sb.append("          );\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncRemoveMethodName).append("(String ")
                    .append(itemParamName).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            ").append(listVarName).append(".remove(").append(itemParamName).append(");\n");
            sb.append("            return true;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.single())\n");
            sb.append("          .subscribe(\n");
            sb.append("              success -> {},\n");
            sb.append("              error -> {}\n");
            sb.append("          );\n");
            sb.append("    }\n\n");
        }
    }

    /**
     * 生成过滤方法
     */
    private void generateFilteringMethods(StringBuilder sb, String listVarName,
                                          String filterPredicateVarName, String tagVarName) {
        String filterMethodName = RandomUtils.generateWord(6);
        String setFilterPredicateMethodName = RandomUtils.generateWord(6);
        String predicateParamName = RandomUtils.generateWord(6);

        sb.append("    public List<String> ").append(filterMethodName).append("() {\n");
        sb.append("        return ").append(listVarName).append(".stream()\n");
        sb.append("            .filter(").append(filterPredicateVarName).append(")\n");
        sb.append("            .collect(Collectors.toList());\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(setFilterPredicateMethodName)
                .append("(Predicate<String> ").append(predicateParamName).append(") {\n");
        sb.append("        this.").append(filterPredicateVarName).append(" = ")
                .append(predicateParamName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成转换方法
     */
    private void generateTransformationMethods(StringBuilder sb, String listVarName,
                                               String transformFunctionVarName, String tagVarName) {
        String transformMethodName = RandomUtils.generateWord(6);
        String setTransformFunctionMethodName = RandomUtils.generateWord(6);
        String functionParamName = RandomUtils.generateWord(6);

        sb.append("    public List<String> ").append(transformMethodName).append("() {\n");
        sb.append("        return ").append(listVarName).append(".stream()\n");
        sb.append("            .map(").append(transformFunctionVarName).append(")\n");
        sb.append("            .collect(Collectors.toList());\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(setTransformFunctionMethodName)
                .append("(Function<String, String> ").append(functionParamName).append(") {\n");
        sb.append("        this.").append(transformFunctionVarName).append(" = ")
                .append(functionParamName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成排序方法
     */
    private void generateSortingMethods(StringBuilder sb, String listVarName,
                                        String comparatorVarName, String tagVarName) {
        String sortMethodName = RandomUtils.generateWord(6);
        String setComparatorMethodName = RandomUtils.generateWord(6);
        String comparatorParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(sortMethodName).append("() {\n");
        sb.append("        ").append(listVarName).append(".sort(").append(comparatorVarName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(setComparatorMethodName)
                .append("(Comparator<String> ").append(comparatorParamName).append(") {\n");
        sb.append("        this.").append(comparatorVarName).append(" = ")
                .append(comparatorParamName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成批量操作方法
     */
    private void generateBatchOperationsMethods(StringBuilder sb, String listVarName,
                                                String batchSizeVarName, String tagVarName) {
        String batchAddMethodName = RandomUtils.generateWord(6);
        String batchRemoveMethodName = RandomUtils.generateWord(6);
        String itemsParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(batchAddMethodName).append("(List<String> ")
                .append(itemsParamName).append(") {\n");
        sb.append("        if (").append(itemsParamName).append(" == null || ")
                .append(itemsParamName).append(".isEmpty()) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        int start = 0;\n");
        sb.append("        while (start < ").append(itemsParamName).append(".size()) {\n");
        sb.append("            int end = Math.min(start + ").append(batchSizeVarName).append(", ")
                .append(itemsParamName).append(".size());\n");
        sb.append("            List<String> batch = ").append(itemsParamName)
                .append(".subList(start, end);\n");
        sb.append("            ").append(listVarName).append(".addAll(batch);\n");
        sb.append("            start = end;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(batchRemoveMethodName).append("(List<String> ")
                .append(itemsParamName).append(") {\n");
        sb.append("        if (").append(itemsParamName).append(" == null || ")
                .append(itemsParamName).append(".isEmpty()) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        int start = 0;\n");
        sb.append("        while (start < ").append(itemsParamName).append(".size()) {\n");
        sb.append("            int end = Math.min(start + ").append(batchSizeVarName).append(", ")
                .append(itemsParamName).append(".size());\n");
        sb.append("            List<String> batch = ").append(itemsParamName)
                .append(".subList(start, end);\n");
        sb.append("            ").append(listVarName).append(".removeAll(batch);\n");
        sb.append("            start = end;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成统计方法
     */
    private void generateStatisticsMethods(StringBuilder sb, String listVarName,
                                           String statisticsVarName, String tagVarName) {
        String calculateStatisticsMethodName = RandomUtils.generateWord(6);
        String getAverageLengthMethodName = RandomUtils.generateWord(6);
        String getMaxLengthMethodName = RandomUtils.generateWord(6);
        String getMinLengthMethodName = RandomUtils.generateWord(6);
        String getTotalLengthMethodName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(calculateStatisticsMethodName).append("() {\n");
        sb.append("        ").append(statisticsVarName).append(" = ")
                .append(listVarName).append(".stream()\n");
        sb.append("            .mapToLong(String::length)\n");
        sb.append("            .summaryStatistics();\n");
        sb.append("    }\n\n");

        sb.append("    public double ").append(getAverageLengthMethodName).append("() {\n");
        sb.append("        return ").append(statisticsVarName).append(".getAverage();\n");
        sb.append("    }\n\n");

        sb.append("    public long ").append(getMaxLengthMethodName).append("() {\n");
        sb.append("        return ").append(statisticsVarName).append(".getMax();\n");
        sb.append("    }\n\n");

        sb.append("    public long ").append(getMinLengthMethodName).append("() {\n");
        sb.append("        return ").append(statisticsVarName).append(".getMin();\n");
        sb.append("    }\n\n");

        sb.append("    public long ").append(getTotalLengthMethodName).append("() {\n");
        sb.append("        return ").append(statisticsVarName).append(".getSum();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成转换方法
     */
    private void generateConversionMethods(StringBuilder sb, String listVarName, String tagVarName) {
        String toArrayMethodName = RandomUtils.generateWord(6);
        String toSetMethodName = RandomUtils.generateWord(6);
        String toMapMethodName = RandomUtils.generateWord(6);

        sb.append("    public String[] ").append(toArrayMethodName).append("() {\n");
        sb.append("        return ").append(listVarName).append(".toArray(new String[0]);\n");
        sb.append("    }\n\n");

        sb.append("    public Set<String> ").append(toSetMethodName).append("() {\n");
        sb.append("        return new HashSet<>(").append(listVarName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public Map<String, Integer> ").append(toMapMethodName).append("() {\n");
        sb.append("        Map<String, Integer> result = new HashMap<>();\n");
        sb.append("        for (int i = 0; i < ").append(listVarName).append(".size(); i++) {\n");
        sb.append("            result.put(").append(listVarName).append(".get(i), i);\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成验证方法
     */
    private void generateValidationMethods(StringBuilder sb, String listVarName,
                                           String validatorVarName, String tagVarName) {
        String validateMethodName = RandomUtils.generateWord(6);
        String validateAllMethodName = RandomUtils.generateWord(6);
        String setValidatorMethodName = RandomUtils.generateWord(6);
        String predicateParamName = RandomUtils.generateWord(6);

        sb.append("    public boolean ").append(validateMethodName).append("(String item) {\n");
        sb.append("        return ").append(validatorVarName).append(".test(item);\n");
        sb.append("    }\n\n");

        sb.append("    public boolean ").append(validateAllMethodName).append("() {\n");
        sb.append("        return ").append(listVarName).append(".stream()\n");
        sb.append("            .allMatch(").append(validatorVarName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(setValidatorMethodName)
                .append("(Predicate<String> ").append(predicateParamName).append(") {\n");
        sb.append("        this.").append(validatorVarName).append(" = ")
                .append(predicateParamName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成示例使用方法
     */
    private void generateExampleUsageMethod(StringBuilder sb, String className, String listVarName,
                                            String randomVarName, String filterPredicateVarName,
                                            String transformFunctionVarName, String comparatorVarName,
                                            String batchSizeVarName, String statisticsVarName,
                                            String validatorVarName, String tagVarName) {
        String exampleMethodName = RandomUtils.generateWord(6);
        String itemParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(exampleMethodName).append("() {\n");
        sb.append("        String ").append(itemParamName).append(" = \"").append(RandomUtils.generateWord(6))
                .append("\";\n");
        sb.append("        ").append(listVarName).append(".add(").append(itemParamName).append(");\n");

        if (useFiltering) {
            sb.append("        List<String> filtered = ").append(listVarName).append(".stream()\n");
            sb.append("            .filter(").append(filterPredicateVarName).append(")\n");
            sb.append("            .collect(Collectors.toList());\n");
        }

        if (useTransformation) {
            sb.append("        List<String> transformed = ").append(listVarName).append(".stream()\n");
            sb.append("            .map(").append(transformFunctionVarName).append(")\n");
            sb.append("            .collect(Collectors.toList());\n");
        }

        if (useSorting) {
            sb.append("        ").append(listVarName).append(".sort(").append(comparatorVarName).append(");\n");
        }

        if (useStatistics) {
            sb.append("        ").append(statisticsVarName).append(" = ")
                    .append(listVarName).append(".stream()\n");
            sb.append("            .mapToLong(String::length)\n");
            sb.append("            .summaryStatistics();\n");
        }

        if (useValidation) {
            sb.append("        boolean isValid = ").append(validatorVarName).append(".test(")
                    .append(itemParamName).append(");\n");
        }

        sb.append("    }\n\n");
    }
}
