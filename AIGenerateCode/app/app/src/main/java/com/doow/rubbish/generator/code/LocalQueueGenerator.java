package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalQueueGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] OPERATION_TYPES = {
            "offer", "poll", "peek", "element", "remove",
            "size", "is_empty", "clear", "contains", "drain_to",
            "add_all", "remove_all", "retain_all", "to_array", "to_list",
            "to_linked_list", "to_priority_queue", "to_array_deque", "to_concurrent_queue"
    };

    private static final String[] CONVERT_TYPES = {
            "to_list", "to_array", "to_string", "from_string",
            "to_priority_queue", "to_linked_list", "to_array_deque", "to_concurrent_queue",
            "to_blocking_queue", "to_linked_blocking_queue", "to_array_blocking_queue",
            "to_priority_blocking_queue", "to_delayed_queue", "to_synchronous_queue"
    };

    private static final String[] ITERATE_TYPES = {
            "for_each", "iterator", "stream", "parallel_stream", "filter",
            "map", "reduce", "collect", "find_any", "find_first",
            "remove_if", "spliterator", "parallel_for_each", "to_array",
            "to_stream", "to_parallel_stream", "any_match", "all_match", "none_match"
    };

    // 功能标志
    private boolean useAsyncOperations;
    private boolean useValidation;
    private boolean useTransformation;
    private boolean useChaining;
    private boolean useLogging;
    private boolean useCaching;
    private boolean useErrorHandling;
    private boolean useCustomIterators;

    public LocalQueueGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        initializeFeatureFlags();
    }

    /**
     * 初始化功能标志，确保随机性和多样性
     */
    private void initializeFeatureFlags() {
        useAsyncOperations = RandomUtils.randomBoolean();
        useValidation = RandomUtils.randomBoolean();
        useTransformation = RandomUtils.randomBoolean();
        useChaining = RandomUtils.randomBoolean();
        useLogging = RandomUtils.randomBoolean();
        useCaching = RandomUtils.randomBoolean();
        useErrorHandling = RandomUtils.randomBoolean();
        useCustomIterators = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地队列相关代码");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Queue");
            String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
            generateQueueClass(className, operationType, asyncHandler);
        }
    }

    private void generateQueueClass(String className, String operationType, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("queue"));

        // 生成基础导入
        sb.append(generateImportStatement("java.util.Queue"));
        sb.append(generateImportStatement("java.util.LinkedList"));
        sb.append(generateImportStatement("java.util.PriorityQueue"));
        sb.append(generateImportStatement("java.util.ArrayDeque"));
        sb.append(generateImportStatement("java.util.concurrent.ConcurrentLinkedQueue"));
        sb.append(generateImportStatement("java.util.concurrent.*"));
        sb.append(generateImportStatement("android.util.Log"));

        // 根据功能标志添加条件导入
        if (useAsyncOperations) {
            if (asyncHandler.contains("coroutines")) {
                sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
                sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
            } else if (asyncHandler.contains("rxjava")) {
                sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
                sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
            } else {
                sb.append(generateImportStatement("android.os.Handler"));
                sb.append(generateImportStatement("android.os.Looper"));
            }
        }

        if (useCaching) {
            sb.append(generateImportStatement("android.util.LruCache"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 随机选择操作类型和转换类型
        String convertType = CONVERT_TYPES[RandomUtils.between(0, CONVERT_TYPES.length - 1)];
        String iterateType = ITERATE_TYPES[RandomUtils.between(0, ITERATE_TYPES.length - 1)];

        // 生成类声明
        sb.append("public class ").append(className).append("<T> {\n\n");

        // 生成常量字段
        String tagVarName = RandomUtils.generateWord(6);
        String operationTypeVarName = RandomUtils.generateWord(6);
        String convertTypeVarName = RandomUtils.generateWord(6);
        String iterateTypeVarName = RandomUtils.generateWord(6);
        String cacheSizeVarName = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVarName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(operationTypeVarName).append(" = \"").append(operationType).append("\";\n");
        sb.append("    private static final String ").append(convertTypeVarName).append(" = \"").append(convertType).append("\";\n");
        sb.append("    private static final String ").append(iterateTypeVarName).append(" = \"").append(iterateType).append("\";\n");

        if (useCaching) {
            sb.append("    private static final int ").append(cacheSizeVarName).append(" = ").append(RandomUtils.between(10, 50)).append(";\n");
        }

        sb.append("\n");

        // 生成实例字段
        String queueVarName = RandomUtils.generateWord(6);
        String cacheVarName = RandomUtils.generateWord(6);
        String handlerVarName = RandomUtils.generateWord(6);

        sb.append("    private Queue<T> ").append(queueVarName).append(";\n");

        if (useCaching) {
            sb.append("    private LruCache<String, T> ").append(cacheVarName).append(";\n");
        }

        if (useAsyncOperations && !asyncHandler.contains("coroutines") && !asyncHandler.contains("rxjava")) {
            sb.append("    private Handler ").append(handlerVarName).append(";\n");
        }

        sb.append("\n");

        // 生成构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        initialize").append(className).append("();\n");
        sb.append("    }\n\n");

        sb.append("    private void initialize").append(className).append("() {\n");
        sb.append("        this.").append(queueVarName).append(" = new LinkedList<>();\n");

        if (useCaching) {
            sb.append("        this.").append(cacheVarName).append(" = new LruCache<>(").append(cacheSizeVarName).append(");\n");
        }

        if (useAsyncOperations && !asyncHandler.contains("coroutines") && !asyncHandler.contains("rxjava")) {
            sb.append("        this.").append(handlerVarName).append(" = new Handler(Looper.getMainLooper());\n");
        }

        sb.append("    }\n\n");

        // 生成基础方法
        generateBasicMethods(sb, className, tagVarName, operationTypeVarName, queueVarName);

        // 根据功能标志添加条件方法
        if (useAsyncOperations) {
            generateAsyncMethods(sb, className, queueVarName, tagVarName, asyncHandler, handlerVarName);
        }

        if (useValidation) {
            generateValidationMethods(sb, className, queueVarName, tagVarName);
        }

        if (useTransformation) {
            generateTransformationMethods(sb, className, queueVarName, tagVarName, convertTypeVarName);
        }

        if (useChaining) {
            generateChainingMethods(sb, className, queueVarName, tagVarName);
        }

        if (useLogging) {
            generateLoggingMethods(sb, className, queueVarName, tagVarName);
        }

        if (useCaching) {
            generateCachingMethods(sb, className, queueVarName, cacheVarName, tagVarName);
        }

        if (useErrorHandling) {
            generateErrorHandlingMethods(sb, className, queueVarName, tagVarName);
        }

        if (useCustomIterators) {
            generateCustomIteratorMethods(sb, className, queueVarName, tagVarName, iterateTypeVarName);
        }

        // 生成使用所有字段和方法的示例方法
        generateExampleUsageMethod(sb, className, queueVarName, cacheVarName, tagVarName, operationTypeVarName);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "queue");
    }

    /**
     * 生成基础方法
     */
    private void generateBasicMethods(StringBuilder sb, String className, String tagVarName,
                                      String operationTypeVarName, String queueVarName) {
        String offerMethodName = RandomUtils.generateWord(6);
        String pollMethodName = RandomUtils.generateWord(6);
        String peekMethodName = RandomUtils.generateWord(6);
        String elementMethodName = RandomUtils.generateWord(6);
        String removeMethodName = RandomUtils.generateWord(6);
        String sizeMethodName = RandomUtils.generateWord(6);
        String isEmptyMethodName = RandomUtils.generateWord(6);
        String clearMethodName = RandomUtils.generateWord(6);
        String containsMethodName = RandomUtils.generateWord(6);
        String addAllMethodName = RandomUtils.generateWord(6);
        String toArrayMethodName = RandomUtils.generateWord(6);
        String toStringMethodName = RandomUtils.generateWord(6);
        String fromStringMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 添加元素方法
        sb.append("    public boolean ").append(offerMethodName).append("(T ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return ").append(queueVarName).append(".offer(")
                .append(paramName).append(");\n");
        sb.append("    }\n\n");

        // 移除并返回队头元素方法
        sb.append("    public T ").append(pollMethodName).append("() {\n");
        sb.append("        return ").append(queueVarName).append(".poll();\n");
        sb.append("    }\n\n");

        // 查看队头元素方法
        sb.append("    public T ").append(peekMethodName).append("() {\n");
        sb.append("        return ").append(queueVarName).append(".peek();\n");
        sb.append("    }\n\n");

        // 获取队头元素方法
        sb.append("    public T ").append(elementMethodName).append("() {\n");
        sb.append("        return ").append(queueVarName).append(".element();\n");
        sb.append("    }\n\n");

        // 移除指定元素方法
        sb.append("    public boolean ").append(removeMethodName).append("(T ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return ").append(queueVarName).append(".remove(")
                .append(paramName).append(");\n");
        sb.append("    }\n\n");

        // 获取队列大小方法
        sb.append("    public int ").append(sizeMethodName).append("() {\n");
        sb.append("        return ").append(queueVarName).append(".size();\n");
        sb.append("    }\n\n");

        // 检查队列是否为空方法
        sb.append("    public boolean ").append(isEmptyMethodName).append("() {\n");
        sb.append("        return ").append(queueVarName).append(".isEmpty();\n");
        sb.append("    }\n\n");

        // 清空队列方法
        sb.append("    public void ").append(clearMethodName).append("() {\n");
        sb.append("        ").append(queueVarName).append(".clear();\n");
        sb.append("    }\n\n");

        // 检查是否包含元素方法
        sb.append("    public boolean ").append(containsMethodName).append("(T ")
                .append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return ").append(queueVarName).append(".contains(")
                .append(paramName).append(");\n");
        sb.append("    }\n\n");

        // 添加所有元素方法
        sb.append("    public boolean ").append(addAllMethodName)
                .append("(Collection<T> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return ").append(queueVarName).append(".addAll(")
                .append(paramName).append(");\n");
        sb.append("    }\n\n");

        // 转换为数组方法
        sb.append("    public T[] ").append(toArrayMethodName).append("() {\n");
        sb.append("        return ").append(queueVarName).append(".toArray(new Object[0]);\n");
        sb.append("    }\n\n");

        // 转换为字符串方法
        sb.append("    public String ").append(toStringMethodName).append("() {\n");
        sb.append("        return ").append(queueVarName).append(".toString();\n");
        sb.append("    }\n\n");

        // 从字符串创建队列方法
        sb.append("    public void ").append(fromStringMethodName)
                .append("(String ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        String[] ").append(RandomUtils.generateWord(6)).append(" = ")
                .append(paramName).append(".split(\",\");\n");
        sb.append("        for (String ").append(RandomUtils.generateWord(6)).append(" : ")
                .append(RandomUtils.generateWord(6)).append(") {\n");
        sb.append("            ").append(queueVarName).append(".offer((T) ")
                .append(RandomUtils.generateWord(6)).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成异步操作方法
     */
    private void generateAsyncMethods(StringBuilder sb, String className, String queueVarName,
                                      String tagVarName, String asyncHandler, String handlerVarName) {
        String asyncOfferMethodName = RandomUtils.generateWord(6);
        String asyncPollMethodName = RandomUtils.generateWord(6);
        String callbackMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        if (asyncHandler.contains("coroutines")) {
            // 使用协程的异步方法
            sb.append("    public void ").append(asyncOfferMethodName).append("(T ")
                    .append(paramName).append(", Consumer<Boolean> ")
                    .append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        CoroutineScope(Dispatchers.IO).launch {\n");
            sb.append("            boolean ").append(resultName).append(" = false;\n");
            sb.append("            try {\n");
            sb.append("                if (").append(paramName).append(" != null) {\n");
            sb.append("                    ").append(resultName).append(" = ")
                    .append(queueVarName).append(".offer(").append(paramName).append(");\n");
            sb.append("                }\n");
            sb.append("                withContext(Dispatchers.Main) {\n");
            sb.append("                    ").append(RandomUtils.generateWord(6)).append(".accept(")
                    .append(resultName).append(");\n");
            sb.append("                }\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                Log.e(").append(tagVarName).append(", \"Async offer error\", e);\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncPollMethodName)
                    .append("(Consumer<T> ").append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        CoroutineScope(Dispatchers.IO).launch {\n");
            sb.append("            T ").append(resultName).append(" = null;\n");
            sb.append("            try {\n");
            sb.append("                ").append(resultName).append(" = ").append(queueVarName).append(".poll();\n");
            sb.append("                withContext(Dispatchers.Main) {\n");
            sb.append("                    ").append(RandomUtils.generateWord(6)).append(".accept(")
                    .append(resultName).append(");\n");
            sb.append("                }\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                Log.e(").append(tagVarName).append(", \"Async poll error\", e);\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            // 使用RxJava的异步方法
            sb.append("    public void ").append(asyncOfferMethodName).append("(T ")
                    .append(paramName).append(", Consumer<Boolean> ")
                    .append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            if (").append(paramName).append(" != null) {\n");
            sb.append("                return ").append(queueVarName).append(".offer(")
                    .append(paramName).append(");\n");
            sb.append("            }\n");
            sb.append("            return false;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.computation())\n");
            sb.append("          .subscribe(\n");
            sb.append("              result -> {\n");
            sb.append("                  ").append(RandomUtils.generateWord(6)).append(".accept(result);\n");
            sb.append("              },\n");
            sb.append("              error -> {\n");
            sb.append("                  Log.e(").append(tagVarName).append(", \"Async offer error\", error);\n");
            sb.append("              }\n");
            sb.append("          );\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncPollMethodName)
                    .append("(Consumer<T> ").append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            return ").append(queueVarName).append(".poll();\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.computation())\n");
            sb.append("          .subscribe(\n");
            sb.append("              result -> {\n");
            sb.append("                  ").append(RandomUtils.generateWord(6)).append(".accept(result);\n");
            sb.append("              },\n");
            sb.append("              error -> {\n");
            sb.append("                  Log.e(").append(tagVarName).append(", \"Async poll error\", error);\n");
            sb.append("              }\n");
            sb.append("          );\n");
            sb.append("    }\n\n");
        } else {
            // 使用Handler的异步方法
            sb.append("    public void ").append(asyncOfferMethodName).append("(T ")
                    .append(paramName).append(", Consumer<Boolean> ")
                    .append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            boolean ").append(resultName).append(" = false;\n");
            sb.append("            try {\n");
            sb.append("                if (").append(paramName).append(" != null) {\n");
            sb.append("                    ").append(resultName).append(" = ")
                    .append(queueVarName).append(".offer(").append(paramName).append(");\n");
            sb.append("                }\n");
            sb.append("                ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("                    ").append(RandomUtils.generateWord(6)).append(".accept(")
                    .append(resultName).append(");\n");
            sb.append("                });\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("                    Log.e(").append(tagVarName).append(", \"Async offer error\", e);\n");
            sb.append("                });\n");
            sb.append("            }\n");
            sb.append("        }).start();\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncPollMethodName)
                    .append("(Consumer<T> ").append(RandomUtils.generateWord(6)).append(") {\n");
            sb.append("        new Thread(() -> {\n");
            sb.append("            T ").append(resultName).append(" = null;\n");
            sb.append("            try {\n");
            sb.append("                ").append(resultName).append(" = ").append(queueVarName).append(".poll();\n");
            sb.append("                ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("                    ").append(RandomUtils.generateWord(6)).append(".accept(")
                    .append(resultName).append(");\n");
            sb.append("                });\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                ").append(handlerVarName).append(".post(() -> {\n");
            sb.append("                    Log.e(").append(tagVarName).append(", \"Async poll error\", e);\n");
            sb.append("                });\n");
            sb.append("            }\n");
            sb.append("        }).start();\n");
            sb.append("    }\n\n");
        }

        // 回调方法
        sb.append("    public void ").append(callbackMethodName).append("(T ")
                .append(paramName).append(", Consumer<Boolean> ")
                .append(RandomUtils.generateWord(6)).append(") {\n");
        sb.append("        ").append(asyncOfferMethodName).append("(").append(paramName).append(", ")
                .append(RandomUtils.generateWord(6)).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成验证方法
     */
    private void generateValidationMethods(StringBuilder sb, String className, String queueVarName,
                                           String tagVarName) {
        String validateQueueMethodName = RandomUtils.generateWord(6);
        String isValidQueueMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 验证队列方法
        sb.append("    public boolean ").append(validateQueueMethodName)
                .append("(Collection<T> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return ").append(queueVarName).append(".addAll(")
                .append(paramName).append(");\n");
        sb.append("    }\n\n");

        // 检查队列是否有效方法
        sb.append("    public boolean ").append(isValidQueueMethodName).append("() {\n");
        sb.append("        return ").append(queueVarName).append(" != null;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成转换方法
     */
    private void generateTransformationMethods(StringBuilder sb, String className, String queueVarName,
                                               String tagVarName, String convertTypeVarName) {
        String toListMethodName = RandomUtils.generateWord(6);
        String toPriorityQueueMethodName = RandomUtils.generateWord(6);
        String toArrayDequeMethodName = RandomUtils.generateWord(6);
        String toConcurrentQueueMethodName = RandomUtils.generateWord(6);

        // 转换为列表方法
        sb.append("    public List<T> ").append(toListMethodName).append("() {\n");
        sb.append("        return new ArrayList<>(").append(queueVarName).append(");\n");
        sb.append("    }\n\n");

        // 转换为优先队列方法
        sb.append("    public PriorityQueue<T> ").append(toPriorityQueueMethodName).append("() {\n");
        sb.append("        return new PriorityQueue<>(").append(queueVarName).append(");\n");
        sb.append("    }\n\n");

        // 转换为数组双端队列方法
        sb.append("    public ArrayDeque<T> ").append(toArrayDequeMethodName).append("() {\n");
        sb.append("        return new ArrayDeque<>(").append(queueVarName).append(");\n");
        sb.append("    }\n\n");

        // 转换为并发队列方法
        sb.append("    public ConcurrentLinkedQueue<T> ").append(toConcurrentQueueMethodName).append("() {\n");
        sb.append("        return new ConcurrentLinkedQueue<>(").append(queueVarName).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成链式调用方法
     */
    private void generateChainingMethods(StringBuilder sb, String className, String queueVarName,
                                         String tagVarName) {
        String andThenMethodName = RandomUtils.generateWord(6);
        String composeMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 链式调用方法
        sb.append("    public ").append(className).append("<T> ").append(andThenMethodName)
                .append("(Consumer<T> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" != null) {\n");
        sb.append("            T ").append(RandomUtils.generateWord(6)).append(" = ")
                .append(queueVarName).append(".peek();\n");
        sb.append("            ").append(paramName).append(".accept(")
                .append(RandomUtils.generateWord(6)).append(");\n");
        sb.append("        }\n");
        sb.append("        return this;\n");
        sb.append("    }\n\n");

        // 组合方法
        sb.append("    public ").append(className).append("<T> ").append(composeMethodName)
                .append("(Function<T, T> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" != null) {\n");
        sb.append("            T ").append(RandomUtils.generateWord(6)).append(" = ")
                .append(queueVarName).append(".peek();\n");
        sb.append("            T ").append(RandomUtils.generateWord(6)).append(" = ")
                .append(paramName).append(".apply(").append(RandomUtils.generateWord(6)).append(");\n");
        sb.append("            ").append(queueVarName).append(".offer(")
                .append(RandomUtils.generateWord(6)).append(");\n");
        sb.append("        }\n");
        sb.append("        return this;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成日志方法
     */
    private void generateLoggingMethods(StringBuilder sb, String className, String queueVarName,
                                        String tagVarName) {
        String logQueueSizeMethodName = RandomUtils.generateWord(6);
        String logQueueContentMethodName = RandomUtils.generateWord(6);

        // 记录队列大小方法
        sb.append("    public void ").append(logQueueSizeMethodName).append("() {\n");
        sb.append("        Log.d(").append(tagVarName).append(", \"Queue size: \"+")
                .append(queueVarName).append(".size());\n");
        sb.append("    }\n\n");

        // 记录队列内容方法
        sb.append("    public void ").append(logQueueContentMethodName).append("() {\n");
        sb.append("        Log.d(").append(tagVarName).append(", \"Queue content: \"+")
                .append(queueVarName).append(".toString());\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成缓存方法
     */
    private void generateCachingMethods(StringBuilder sb, String className, String queueVarName,
                                        String cacheVarName, String tagVarName) {
        String putInCacheMethodName = RandomUtils.generateWord(6);
        String getFromCacheMethodName = RandomUtils.generateWord(6);
        String clearCacheMethodName = RandomUtils.generateWord(6);
        String keyName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);

        // 放入缓存方法
        sb.append("    public void ").append(putInCacheMethodName).append("(String ")
                .append(keyName).append(", T ").append(paramName).append(") {\n");
        sb.append("        if (").append(keyName).append(" != null && !")
                .append(keyName).append(".isEmpty() && ").append(paramName).append(" != null) {\n");
        sb.append("            ").append(cacheVarName).append(".put(").append(keyName)
                .append(", ").append(paramName).append(");\n");
        sb.append("        } else {\n");
        sb.append("            Log.w(").append(tagVarName).append(", \"Cache key or value is null or empty\");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 从缓存获取方法
        sb.append("    public T ").append(getFromCacheMethodName).append("(String ")
                .append(keyName).append(") {\n");
        sb.append("        if (").append(keyName).append(" != null) {\n");
        sb.append("            T cached = ").append(cacheVarName).append(".get(")
                .append(keyName).append(");\n");
        sb.append("            if (cached != null) {\n");
        sb.append("                return cached;\n");
        sb.append("            } else {\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Cache miss for key: \" + ")
                .append(keyName).append(");\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return null;\n");
        sb.append("    }\n\n");

        // 清空缓存方法
        sb.append("    public void ").append(clearCacheMethodName).append("() {\n");
        sb.append("        ").append(cacheVarName).append(".evictAll();\n");
        sb.append("        Log.d(").append(tagVarName).append(", \"Cache cleared\");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成错误处理方法
     */
    private void generateErrorHandlingMethods(StringBuilder sb, String className, String queueVarName,
                                              String tagVarName) {
        String offerOrElseMethodName = RandomUtils.generateWord(6);
        String pollOrElseMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String supplierName = RandomUtils.generateWord(6);

        // 添加或返回默认值方法
        sb.append("    public boolean ").append(offerOrElseMethodName)
                .append("(T ").append(paramName).append(", boolean ")
                .append(supplierName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(paramName).append(" != null) {\n");
        sb.append("                return ").append(queueVarName).append(".offer(")
                .append(paramName).append(");\n");
        sb.append("            }\n");
        sb.append("            return ").append(supplierName).append(";\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Offer error\", e);\n");
        sb.append("            return ").append(supplierName).append(";\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 移除或返回默认值方法
        sb.append("    public T ").append(pollOrElseMethodName)
                .append("(Supplier<T> ").append(supplierName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            T result = ").append(queueVarName).append(".poll();\n");
        sb.append("            if (result != null) {\n");
        sb.append("                return result;\n");
        sb.append("            }\n");
        sb.append("            return ").append(supplierName).append(".get();\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Poll error\", e);\n");
        sb.append("            return ").append(supplierName).append(".get();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成自定义迭代器方法
     */
    private void generateCustomIteratorMethods(StringBuilder sb, String className, String queueVarName,
                                               String tagVarName, String iterateTypeVarName) {
        String forEachMethodName = RandomUtils.generateWord(6);
        String filterMethodName = RandomUtils.generateWord(6);
        String mapMethodName = RandomUtils.generateWord(6);
        String reduceMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        // 遍历方法
        sb.append("    public void ").append(forEachMethodName)
                .append("(Consumer<T> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" != null) {\n");
        sb.append("            ").append(queueVarName).append(".forEach(")
                .append(paramName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 过滤方法
        sb.append("    public Queue<T> ").append(filterMethodName)
                .append("(Predicate<T> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return new LinkedList<>();\n");
        sb.append("        }\n");
        sb.append("        Queue<T> ").append(resultName).append(" = new LinkedList<>();\n");
        sb.append("        for (T item : ").append(queueVarName).append(") {\n");
        sb.append("            if (").append(paramName).append(".test(item)) {\n");
        sb.append("                ").append(resultName).append(".offer(item);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultName).append(";\n");
        sb.append("    }\n\n");

        // 映射方法
        sb.append("    public <R> Queue<R> ").append(mapMethodName)
                .append("(Function<T, R> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null) {\n");
        sb.append("            return new LinkedList<>();\n");
        sb.append("        }\n");
        sb.append("        Queue<R> ").append(resultName).append(" = new LinkedList<>();\n");
        sb.append("        for (T item : ").append(queueVarName).append(") {\n");
        sb.append("            ").append(resultName).append(".offer(")
                .append(paramName).append(".apply(item));\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultName).append(";\n");
        sb.append("    }\n\n");

        // 归约方法
        sb.append("    public T ").append(reduceMethodName)
                .append("(BinaryOperator<T> ").append(paramName).append(") {\n");
        sb.append("        if (").append(paramName).append(" == null || ")
                .append(queueVarName).append(".isEmpty()) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        T ").append(resultName).append(" = null;\n");
        sb.append("        for (T item : ").append(queueVarName).append(") {\n");
        sb.append("            if (").append(resultName).append(" == null) {\n");
        sb.append("                ").append(resultName).append(" = item;\n");
        sb.append("            } else {\n");
        sb.append("                ").append(resultName).append(" = ")
                .append(paramName).append(".apply(").append(resultName)
                .append(", item);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成示例使用方法
     */
    private void generateExampleUsageMethod(StringBuilder sb, String className,
                                            String queueVarName, String cacheVarName,
                                            String tagVarName, String operationTypeVarName) {
        String exampleMethodName = RandomUtils.generateWord(6);
        String paramName = RandomUtils.generateWord(6);
        String resultName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(exampleMethodName).append("(T ")
                .append(paramName).append(") {\n");
        sb.append("        // 添加元素\n");
        sb.append("        boolean added = offer(").append(paramName).append(");\n");

        if (useAsyncOperations) {
            sb.append("        // 异步操作\n");
            sb.append("        asyncOffer(").append(paramName).append(", result -> {\n");
            sb.append("            // 处理结果\n");
            sb.append("        });\n");
        }

        if (useValidation) {
            sb.append("        // 验证操作\n");
            sb.append("        boolean isValid = isValidQueue();\n");
        }

        if (useTransformation) {
            sb.append("        // 转换操作\n");
            sb.append("        List<T> list = toList();\n");
        }

        if (useChaining) {
            sb.append("        // 链式调用\n");
            sb.append("        andThen(value -> {\n");
            sb.append("            // 处理值\n");
            sb.append("        }).compose(value -> value);\n");
        }

        if (useLogging) {
            sb.append("        // 日志操作\n");
            sb.append("        logQueueSize();\n");
        }

        if (useCaching) {
            sb.append("        // 缓存操作\n");
            sb.append("        putInCache(\"key1\", ").append(paramName).append(");\n");
            sb.append("        T cached = getFromCache(\"key1\");\n");
        }

        if (useErrorHandling) {
            sb.append("        // 错误处理\n");
            sb.append("        boolean result = offerOrElse(").append(paramName).append(", false);\n");
        }

        if (useCustomIterators) {
            sb.append("        // 自定义迭代器\n");
            sb.append("        forEach(value -> {\n");
            sb.append("            // 处理值\n");
            sb.append("        });\n");
        }

        sb.append("    }\n\n");
    }
}
