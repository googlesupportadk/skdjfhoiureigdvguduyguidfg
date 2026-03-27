
package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import android.util.Log;

/**
 * 增强版本地集合生成器
 * 满足以下要求：
 * 1. 确保代码功能的随机性和多样性灵活性，避免冗余代码和垃圾代码
 * 2. 在生成的项目中字段都会被使用
 * 3. 在生成的项目中方法都会被调用
 * 4. 不会产生未使用的代码
 * 5. 确保在生成的项目中代码是完整可运行的，且生成器最后生成的java代码也是可运行的
 * 6. 生成的项目中所有的变量名、方法名、类名、传参名、字符串、前缀随机、全部随机生成，防止明显语法BUG
 * 7. 每个类中只能出现0-5个Log和System.out其他都要使用在实际功能中或者在其他类或功能中使用
 * 8. 确保生成的项目中代码和其他生成器功能可以互相关联实现新功能
 * 确保功能无三方依赖，仅安卓系统库
 */
public class LocalSetGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    // 集合类型
    private static final String[] SET_TYPES = {
            "HashSet", "LinkedHashSet", "TreeSet", "ConcurrentHashMap"
    };

    // 操作类型
    private static final String[] OPERATION_TYPES = {
            "add", "remove", "contains", "size", "clear",
            "isEmpty", "union", "intersection", "difference", "subset",
            "addAll", "removeAll", "containsAll", "retainAll", "removeIf",
            "removeAllIf", "equals", "hashCode", "toArray", "toString",
            "filter", "map", "forEach", "stream", "collect",
            "groupBy", "partition", "transform", "flatten"
    };

    // 转换类型
    private static final String[] CONVERT_TYPES = {
            "toList", "toArray", "toString", "fromString",
            "toSortedSet", "toLinkedSet", "toTreeSet", "toConcurrentSet",
            "toImmutableSet", "toSynchronizedSet", "toCopyOnWriteSet", "toEnumSet"
    };

    // 功能特性标志
    private boolean includeFilterOperations;
    private boolean includeTransformOperations;
    private boolean includeGroupOperations;
    private boolean includeStreamOperations;
    private boolean includeBatchOperations;
    private boolean includeValidationOperations;
    private boolean includeConversionOperations;
    private boolean includeStatisticsOperations;

    // 用于生成随机变量名的成员变量
    private String tagVar;
    private String setVar;
    private String resultMapVar;
    private String tempListVar;

    public LocalSetGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        initializeFeatures();
    }

    /**
     * 随机初始化功能特性
     */
    private void initializeFeatures() {
        this.includeFilterOperations = RandomUtils.randomBoolean();
        this.includeTransformOperations = RandomUtils.randomBoolean();
        this.includeGroupOperations = RandomUtils.randomBoolean();
        this.includeStreamOperations = RandomUtils.randomBoolean();
        this.includeBatchOperations = RandomUtils.randomBoolean();
        this.includeValidationOperations = RandomUtils.randomBoolean();
        this.includeConversionOperations = RandomUtils.randomBoolean();
        this.includeStatisticsOperations = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地集合相关代码（增强版）");

        int classCount = RandomUtils.between(3, 8);
        for (int i = 0; i < classCount; i++) {
            // 每次生成前重新随机初始化功能特性
            initializeFeatures();

            String className = RandomUtils.generateClassName("Set");
            String setType = RandomUtils.randomChoice(SET_TYPES);
            generateSetClass(className, setType);
        }
    }

    private void generateSetClass(String className, String setType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("set"));

        // 添加必要的导入语句
        generateImports(sb);

        // 生成类声明
        sb.append("public class ").append(className).append("<T> {\n\n");

        // 生成字段
        generateFields(sb, className, setType);

        // 生成构造方法
        generateConstructors(sb, className, setType);

        // 生成方法
        generateMethods(sb, className, setType);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "set");
    }

    /**
     * 生成导入语句
     */
    private void generateImports(StringBuilder sb) {
        sb.append(generateImportStatement("java.util.Set"));
        sb.append(generateImportStatement("java.util.HashSet"));
        sb.append(generateImportStatement("java.util.LinkedHashSet"));
        sb.append(generateImportStatement("java.util.TreeSet"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.concurrent.ConcurrentHashMap"));
        sb.append(generateImportStatement("java.util.function.Consumer"));
        sb.append(generateImportStatement("java.util.function.Predicate"));
        sb.append(generateImportStatement("java.util.stream.Collectors"));
        sb.append(generateImportStatement("java.util.stream.Stream"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");
    }

    /**
     * 生成字段
     */
    private void generateFields(StringBuilder sb, String className, String setType) {
        // 生成TAG常量
        tagVar = generateRandomVarName("tag");
        sb.append("    private static final String ").append(tagVar).append(" = \"").append(className).append("\";\n");

        // 生成集合类型常量
        String typeVar = generateRandomVarName("setType");
        sb.append("    private static final String ").append(typeVar).append(" = \"").append(setType).append("\";\n");

        // 生成集合字段
        setVar = generateRandomVarName("set");
        sb.append("    private Set<T> ").append(setVar).append(" = new HashSet<>();\n");

        // 根据功能特性生成其他字段
        if (includeStatisticsOperations) {
            String statsVar = generateRandomVarName("stats");
            sb.append("    private Map<String, Integer> ").append(statsVar).append(" = new HashMap<>();\n");
        }

        if (includeTransformOperations) {
            tempListVar = generateRandomVarName("tempList");
            sb.append("    private List<T> ").append(tempListVar).append(" = new ArrayList<>();\n");
        }

        if (includeGroupOperations) {
            resultMapVar = generateRandomVarName("resultMap");
            sb.append("    private Map<String, Set<T>> ").append(resultMapVar).append(" = new HashMap<>();\n");
        }

        sb.append("\n");
    }

    /**
     * 生成构造方法
     */
    private void generateConstructors(StringBuilder sb, String className, String setType) {
        // 无参构造方法
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this(\"").append(generateRandomKey()).append("\");\n");
        sb.append("    }\n\n");

        // 带初始集合的构造方法
        String setParam = generateRandomParamName("initialSet");
        sb.append("    public ").append(className).append("(Set<T> ").append(setParam).append(") {\n");
        sb.append("        if (").append(setParam).append(" != null) {\n");
        sb.append("            ").append(setVar).append(" = new HashSet<>(").append(setParam).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成方法
     */
    private void generateMethods(StringBuilder sb, String className, String setType) {
        // 生成基本操作方法
        generateBasicOperations(sb);

        // 生成批量操作方法
        if (includeBatchOperations) {
            generateBatchOperations(sb);
        }

        // 生成过滤操作方法
        if (includeFilterOperations) {
            generateFilterOperations(sb);
        }

        // 生成转换操作方法
        if (includeTransformOperations) {
            generateTransformOperations(sb);
        }

        // 生成分组操作方法
        if (includeGroupOperations) {
            generateGroupOperations(sb);
        }

        // 生成流操作方法
        if (includeStreamOperations) {
            generateStreamOperations(sb);
        }

        // 生成验证操作方法
        if (includeValidationOperations) {
            generateValidationOperations(sb);
        }

        // 生成转换方法
        if (includeConversionOperations) {
            generateConversionMethods(sb);
        }

        // 生成统计操作方法
        if (includeStatisticsOperations) {
            generateStatisticsMethods(sb);
        }
    }

    /**
     * 生成基本操作方法
     */
    private void generateBasicOperations(StringBuilder sb) {
        // 添加方法
        String addMethodName = generateRandomMethodName("add");
        String itemParam = generateRandomParamName("item");
        sb.append("    public boolean ").append(addMethodName).append("(T ").append(itemParam).append(") {\n");
        sb.append("        if (").append(itemParam).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return ").append(setVar).append(".add(").append(itemParam).append(");\n");
        sb.append("    }\n\n");

        // 移除方法
        String removeMethodName = generateRandomMethodName("remove");
        sb.append("    public boolean ").append(removeMethodName).append("(T ").append(itemParam).append(") {\n");
        sb.append("        if (").append(itemParam).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return ").append(setVar).append(".remove(").append(itemParam).append(");\n");
        sb.append("    }\n\n");

        // 包含方法
        String containsMethodName = generateRandomMethodName("contains");
        sb.append("    public boolean ").append(containsMethodName).append("(T ").append(itemParam).append(") {\n");
        sb.append("        if (").append(itemParam).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return ").append(setVar).append(".contains(").append(itemParam).append(");\n");
        sb.append("    }\n\n");

        // 大小方法
        String sizeMethodName = generateRandomMethodName("size");
        sb.append("    public int ").append(sizeMethodName).append("() {\n");
        sb.append("        return ").append(setVar).append(".size();\n");
        sb.append("    }\n\n");

        // 判空方法
        String isEmptyMethodName = generateRandomMethodName("isEmpty");
        sb.append("    public boolean ").append(isEmptyMethodName).append("() {\n");
        sb.append("        return ").append(setVar).append(".isEmpty();\n");
        sb.append("    }\n\n");

        // 清空方法
        String clearMethodName = generateRandomMethodName("clear");
        sb.append("    public void ").append(clearMethodName).append("() {\n");
        sb.append("        ").append(setVar).append(".clear();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成批量操作方法
     */
    private void generateBatchOperations(StringBuilder sb) {
        // 批量添加方法
        String addAllMethodName = generateRandomMethodName("addAll");
        String itemsParam = generateRandomParamName("items");
        sb.append("    public boolean ").append(addAllMethodName).append("(Set<T> ").append(itemsParam).append(") {\n");
        sb.append("        if (").append(itemsParam).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return ").append(setVar).append(".addAll(").append(itemsParam).append(");\n");
        sb.append("    }\n\n");

        // 批量移除方法
        String removeAllMethodName = generateRandomMethodName("removeAll");
        sb.append("    public boolean ").append(removeAllMethodName).append("(Set<T> ").append(itemsParam).append(") {\n");
        sb.append("        if (").append(itemsParam).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return ").append(setVar).append(".removeAll(").append(itemsParam).append(");\n");
        sb.append("    }\n\n");

        // 批量包含检查方法
        String containsAllMethodName = generateRandomMethodName("containsAll");
        sb.append("    public boolean ").append(containsAllMethodName).append("(Set<T> ").append(itemsParam).append(") {\n");
        sb.append("        if (").append(itemsParam).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return ").append(setVar).append(".containsAll(").append(itemsParam).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成过滤操作方法
     */
    private void generateFilterOperations(StringBuilder sb) {
        // 过滤方法
        String filterMethodName = generateRandomMethodName("filter");
        String predicateParam = generateRandomParamName("predicate");
        sb.append("    public Set<T> ").append(filterMethodName).append("(Predicate<T> ").append(predicateParam).append(") {\n");
        sb.append("        if (").append(predicateParam).append(" == null) {\n");
        sb.append("            return new HashSet<>();\n");
        sb.append("        }\n");
        sb.append("        Set<T> result = new HashSet<>();\n");
        sb.append("        for (T item : ").append(setVar).append(") {\n");
        sb.append("            if (").append(predicateParam).append(".test(item)) {\n");
        sb.append("                result.add(item);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        // 条件移除方法
        String removeIfMethodName = generateRandomMethodName("removeIf");
        sb.append("    public int ").append(removeIfMethodName).append("(Predicate<T> ").append(predicateParam).append(") {\n");
        sb.append("        if (").append(predicateParam).append(" == null) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        int count = 0;\n");
        sb.append("        Iterator<T> iterator = ").append(setVar).append(".iterator();\n");
        sb.append("        while (iterator.hasNext()) {\n");
        sb.append("            if (").append(predicateParam).append(".test(iterator.next())) {\n");
        sb.append("                iterator.remove();\n");
        sb.append("                count++;\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return count;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成转换操作方法
     */
    private void generateTransformOperations(StringBuilder sb) {
        // 映射方法
        String mapMethodName = generateRandomMethodName("map");
        String mapperParam = generateRandomParamName("mapper");
        String resultVar = generateRandomVarName("result");
        sb.append("    public <R> Set<R> ").append(mapMethodName).append("(java.util.function.Function<T, R> ").append(mapperParam).append(") {\n");
        sb.append("        if (").append(mapperParam).append(" == null) {\n");
        sb.append("            return new HashSet<>();\n");
        sb.append("        }\n");
        sb.append("        Set<R> ").append(resultVar).append(" = new HashSet<>();\n");
        sb.append("        for (T item : ").append(setVar).append(") {\n");
        sb.append("            ").append(resultVar).append(".add(").append(mapperParam).append(".apply(item));\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultVar).append(";\n");
        sb.append("    }\n\n");

        // 扁平化方法
        String flattenMethodName = generateRandomMethodName("flatten");
        sb.append("    public List<T> ").append(flattenMethodName).append("() {\n");
        sb.append("        List<T> result = new ArrayList<>();\n");
        sb.append("        for (T item : ").append(setVar).append(") {\n");
        sb.append("            if (item instanceof Set) {\n");
        sb.append("                result.addAll((Set<T>) item);\n");
        sb.append("            } else {\n");
        sb.append("                result.add(item);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成分组操作方法
     */
    private void generateGroupOperations(StringBuilder sb) {
        // 分组方法
        String groupByMethodName = generateRandomMethodName("groupBy");
        String classifierParam = generateRandomParamName("classifier");
        sb.append("    public <K> Map<K, Set<T>> ").append(groupByMethodName).append("(java.util.function.Function<T, K> ").append(classifierParam).append(") {\n");
        sb.append("        if (").append(classifierParam).append(" == null) {\n");
        sb.append("            return new HashMap<>();\n");
        sb.append("        }\n");
        sb.append("        Map<K, Set<T>> result = new HashMap<>();\n");
        sb.append("        for (T item : ").append(setVar).append(") {\n");
        sb.append("            K key = ").append(classifierParam).append(".apply(item);\n");
        sb.append("            result.computeIfAbsent(key, k -> new HashSet<>()).add(item);\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        // 分区方法
        String partitionMethodName = generateRandomMethodName("partition");
        String predicateParam = generateRandomParamName("predicate");
        String trueSetVar = generateRandomVarName("trueSet");
        String falseSetVar = generateRandomVarName("falseSet");
        sb.append("    public Set<T>[] ").append(partitionMethodName).append("(Predicate<T> ").append(predicateParam).append(") {\n");
        sb.append("        if (").append(predicateParam).append(" == null) {\n");
        sb.append("            return new Set[]{new HashSet<>(), new HashSet<>()};\n");
        sb.append("        }\n");
        sb.append("        Set<T> ").append(trueSetVar).append(" = new HashSet<>();\n");
        sb.append("        Set<T> ").append(falseSetVar).append(" = new HashSet<>();\n");
        sb.append("        for (T item : ").append(setVar).append(") {\n");
        sb.append("            if (").append(predicateParam).append(".test(item)) {\n");
        sb.append("                ").append(trueSetVar).append(".add(item);\n");
        sb.append("            } else {\n");
        sb.append("                ").append(falseSetVar).append(".add(item);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return new Set[]{").append(trueSetVar).append(", ").append(falseSetVar).append("};\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成流操作方法
     */
    private void generateStreamOperations(StringBuilder sb) {
        // 流方法
        String streamMethodName = generateRandomMethodName("stream");
        sb.append("    public Stream<T> ").append(streamMethodName).append("() {\n");
        sb.append("        return ").append(setVar).append(".stream();\n");
        sb.append("    }\n\n");

        // forEach方法
        String forEachMethodName = generateRandomMethodName("forEach");
        String actionParam = generateRandomParamName("action");
        sb.append("    public void ").append(forEachMethodName).append("(Consumer<T> ").append(actionParam).append(") {\n");
        sb.append("        if (").append(actionParam).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        ").append(setVar).append(".forEach(").append(actionParam).append(");\n");
        sb.append("    }\n\n");

        // 收集方法
        String collectMethodName = generateRandomMethodName("collect");
        String collectorParam = generateRandomParamName("collector");
        sb.append("    public <R, A> R ").append(collectMethodName).append("(java.util.stream.Collector<T, A, R> ").append(collectorParam).append(") {\n");
        sb.append("        if (").append(collectorParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return ").append(setVar).append(".stream().collect(").append(collectorParam).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成验证操作方法
     */
    private void generateValidationOperations(StringBuilder sb) {
        // 等于方法
        String equalsMethodName = generateRandomMethodName("equals");
        String otherParam = generateRandomParamName("other");
        sb.append("    public boolean ").append(equalsMethodName).append("(Set<T> ").append(otherParam).append(") {\n");
        sb.append("        if (").append(otherParam).append(" == null) {\n");
        sb.append("            return ").append(setVar).append(".isEmpty();\n");
        sb.append("        }\n");
        sb.append("        return ").append(setVar).append(".equals(").append(otherParam).append(");\n");
        sb.append("    }\n\n");

        // 哈希码方法
        String hashCodeMethodName = generateRandomMethodName("hashCode");
        sb.append("    public int ").append(hashCodeMethodName).append("() {\n");
        sb.append("        return ").append(setVar).append(".hashCode();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成转换方法
     */
    private void generateConversionMethods(StringBuilder sb) {
        // 转列表方法
        String toListMethodName = generateRandomMethodName("toList");
        sb.append("    public List<T> ").append(toListMethodName).append("() {\n");
        sb.append("        return new ArrayList<>(").append(setVar).append(");\n");
        sb.append("    }\n\n");

        // 转数组方法
        String toArrayMethodName = generateRandomMethodName("toArray");
        sb.append("    public T[] ").append(toArrayMethodName).append("() {\n");
        sb.append("        return ").append(setVar).append(".toArray(new Object[0]);\n");
        sb.append("    }\n\n");

        // 转字符串方法
        String toStringMethodName = generateRandomMethodName("toCustomString");
        sb.append("    public String ").append(toStringMethodName).append("() {\n");
        sb.append("        return ").append(setVar).append(".toString();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成统计操作方法
     */
    private void generateStatisticsMethods(StringBuilder sb) {
        // 统计方法
        String statsMethodName = generateRandomMethodName("getStatistics");
        sb.append("    public Map<String, Object> ").append(statsMethodName).append("() {\n");
        sb.append("        Map<String, Object> stats = new HashMap<>();\n");
        sb.append("        stats.put(\"").append(generateRandomKey()).append("\", ").append(setVar).append(".size());\n");
        sb.append("        stats.put(\"").append(generateRandomKey()).append("\", ").append(setVar).append(".isEmpty());\n");
        sb.append("        return stats;\n");
        sb.append("    }\n\n");

        // 查找任意方法
        String findAnyMethodName = generateRandomMethodName("findAny");
        String predicateParam = generateRandomParamName("predicate");
        sb.append("    public T ").append(findAnyMethodName).append("(Predicate<T> ").append(predicateParam).append(") {\n");
        sb.append("        if (").append(predicateParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        for (T item : ").append(setVar).append(") {\n");
        sb.append("            if (").append(predicateParam).append(".test(item)) {\n");
        sb.append("                return item;\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return null;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成随机变量名
     */
    private String generateRandomVarName(String prefix) {
        return RandomUtils.generateWord(3).toLowerCase() + RandomUtils.generateWord(4) + prefix;
    }

    /**
     * 生成随机参数名
     */
    private String generateRandomParamName(String prefix) {
        return RandomUtils.generateWord(3).toLowerCase() + RandomUtils.generateWord(4) + prefix;
    }

    /**
     * 生成随机方法名
     */
    private String generateRandomMethodName(String prefix) {
        return RandomUtils.generateWord(3).toLowerCase() + RandomUtils.generateWord(4) + prefix;
    }

    /**
     * 生成随机密钥
     */
    private String generateRandomKey() {
        return RandomUtils.generateRandomString(16);
    }
}
