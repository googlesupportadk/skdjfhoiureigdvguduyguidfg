package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalComparatorGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] COMPARATOR_TYPES = {
            "NaturalOrder", "ReverseOrder", "CaseInsensitive", "LengthBased", "Custom",
            "NullsFirst", "NullsLast", "Reverse", "Natural",
            "CaseInsensitive", "CaseSensitive", "Length", "Size", "Hash",
            "Identity", "ComparatorOrder", "Comparing", "ThenComparing"
    };

    private static final String[] DATA_TYPES = {
            "String", "Integer", "Double", "Date", "Object",
            "Long", "Float", "Boolean", "Byte", "Short",
            "Character", "StringBuilder", "StringBuffer", "LocalDate",
            "LocalDateTime", "LocalTime", "Instant", "Duration", "Period"
    };

    private static final String[] FIELD_TYPES = {
            "int", "long", "float", "double", "boolean", "String", "Object"
    };

    private static final String[] COMPARISON_STRATEGIES = {
            "Natural", "Reverse", "CaseInsensitive", "Length", "Hash",
            "Custom", "NullFirst", "NullLast", "Multi", "Chain"
    };

    private static final String[] COMPARISON_OPERATIONS = {
            "equals", "notEquals", "lessThan", "greaterThan", "lessOrEqual",
            "greaterOrEqual", "between", "contains", "startsWith", "endsWith"
    };

    private static final String[] COMPARISON_RESULT_TYPES = {
            "int", "boolean", "float", "double", "long"
    };

    public LocalComparatorGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成比较器类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Comparator");
            generateComparatorClass(className, asyncHandler);
        }
    }

    private void generateComparatorClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("comparator"));

        sb.append(generateImportStatement("java.util.Comparator"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.HashMap"));

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        String dataType = DATA_TYPES[RandomUtils.between(0, DATA_TYPES.length - 1)];
        String comparatorType = COMPARATOR_TYPES[RandomUtils.between(0, COMPARATOR_TYPES.length - 1)];
        String comparisonStrategy = COMPARISON_STRATEGIES[RandomUtils.between(0, COMPARISON_STRATEGIES.length - 1)];

        sb.append("public class ").append(className).append(" implements Comparator<").append(dataType).append("> {\n\n");

        // 添加常量
        sb.append("    private static final String COMPARATOR_TYPE = \"").append(comparatorType).append("\");\n");
        sb.append("    private static final String COMPARISON_STRATEGY = \"").append(comparisonStrategy).append("\");\n\n");

        // 随机生成多个字段
        int fieldCount = RandomUtils.between(3, 8);
        List<String> fieldNames = new ArrayList<>();
        for (int i = 0; i < fieldCount; i++) {
            String fieldType = FIELD_TYPES[RandomUtils.between(0, FIELD_TYPES.length - 1)];
            String fieldName = RandomUtils.generateVariableName(fieldType);
            fieldNames.add(fieldName);

            if (RandomUtils.randomBoolean()) {
                sb.append("    private static final ").append(fieldType).append(" ").append(fieldName);
                sb.append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            } else {
                sb.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n");
            }
        }

        // 添加比较器相关字段
        sb.append("    private boolean ascending;\n");
        sb.append("    private boolean nullsFirst;\n");
        sb.append("    private boolean caseSensitive;\n");
        sb.append("    private int comparisonCount;\n");
        sb.append("    private int matchCount;\n");
        sb.append("    private int mismatchCount;\n");
        sb.append("    private List<").append(dataType).append("> sortedList;\n");
        sb.append("    private Map<String, Integer> comparisonStats;\n\n");

        // 构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this(true);\n");
        sb.append("    }\n\n");

        sb.append("    public ").append(className).append("(boolean ascending) {\n");
        sb.append("        this.ascending = ascending;\n");
        sb.append("        this.nullsFirst = RandomUtils.randomBoolean();\n");
        sb.append("        this.caseSensitive = RandomUtils.randomBoolean();\n");
        sb.append("        this.comparisonCount = 0;\n");
        sb.append("        this.matchCount = 0;\n");
        sb.append("        this.mismatchCount = 0;\n");
        sb.append("        this.sortedList = new ArrayList<>();\n");
        sb.append("        this.comparisonStats = new HashMap<>();\n");

        for (String fieldName : fieldNames) {
            if (!isStaticField(fieldName)) {
                String fieldType = getFieldType(fieldName);
                sb.append("        this.").append(fieldName).append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            }
        }
        sb.append("    }\n\n");

        // 生成compare方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    @Override\n");
            sb.append("    public int compare(").append(dataType).append(" a, ").append(dataType).append(" b) {\n");
            sb.append("        comparisonCount++;\n");
            sb.append("        int result = 0;\n");
            sb.append("        \n");
            sb.append("        if (a == null && b == null) {\n");
            sb.append("            matchCount++;\n");
            sb.append("            return 0;\n");
            sb.append("        }\n");
            sb.append("        if (a == null) {\n");
            sb.append("            mismatchCount++;\n");
            sb.append("            return nullsFirst ? -1 : 1;\n");
            sb.append("        }\n");
            sb.append("        if (b == null) {\n");
            sb.append("            mismatchCount++;\n");
            sb.append("            return nullsFirst ? 1 : -1;\n");
            sb.append("        }\n");
            sb.append("        \n");
            sb.append("        switch (COMPARISON_STRATEGY) {\n");
            sb.append("            case \"Natural\":\n");
            sb.append("                result = compareNatural(a, b);\n");
            sb.append("                break;\n");
            sb.append("            case \"Reverse\":\n");
            sb.append("                result = compareReverse(a, b);\n");
            sb.append("                break;\n");
            sb.append("            case \"CaseInsensitive\":\n");
            sb.append("                result = compareCaseInsensitive(a, b);\n");
            sb.append("                break;\n");
            sb.append("            case \"Length\":\n");
            sb.append("                result = compareLength(a, b);\n");
            sb.append("                break;\n");
            sb.append("            case \"Hash\":\n");
            sb.append("                result = compareHash(a, b);\n");
            sb.append("                break;\n");
            sb.append("            case \"Custom\":\n");
            sb.append("                result = compareCustom(a, b);\n");
            sb.append("                break;\n");
            sb.append("            case \"NullFirst\":\n");
            sb.append("                result = compareNullFirst(a, b);\n");
            sb.append("                break;\n");
            sb.append("            case \"NullLast\":\n");
            sb.append("                result = compareNullLast(a, b);\n");
            sb.append("                break;\n");
            sb.append("            case \"Multi\":\n");
            sb.append("                result = compareMulti(a, b);\n");
            sb.append("                break;\n");
            sb.append("            case \"Chain\":\n");
            sb.append("                result = compareChain(a, b);\n");
            sb.append("                break;\n");
            sb.append("            default:\n");
            sb.append("                result = compareNatural(a, b);\n");
            sb.append("                break;\n");
            sb.append("        }\n");
            sb.append("        \n");
            sb.append("        if (result == 0) {\n");
            sb.append("            matchCount++;\n");
            sb.append("        } else {\n");
            sb.append("            mismatchCount++;\n");
            sb.append("        }\n");
            sb.append("        \n");
            sb.append("        return ascending ? result : -result;\n");
            sb.append("    }\n\n");
        }

        // 生成compareNatural方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private int compareNatural(").append(dataType).append(" a, ").append(dataType).append(" b) {\n");
            sb.append("        if (a instanceof Comparable && b instanceof Comparable) {\n");
            sb.append("            return ((Comparable<").append(dataType).append(">) a).compareTo(b);\n");
            sb.append("        }\n");
            sb.append("        return Integer.compare(a.hashCode(), b.hashCode());\n");
            sb.append("    }\n\n");
        }

        // 生成compareReverse方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private int compareReverse(").append(dataType).append(" a, ").append(dataType).append(" b) {\n");
            sb.append("        return -compareNatural(a, b);\n");
            sb.append("    }\n\n");
        }

        // 生成compareCaseInsensitive方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private int compareCaseInsensitive(").append(dataType).append(" a, ").append(dataType).append(" b) {\n");
            sb.append("        if (a instanceof String && b instanceof String) {\n");
            sb.append("            String strA = caseSensitive ? (String) a : ((String) a).toLowerCase();\n");
            sb.append("            String strB = caseSensitive ? (String) b : ((String) b).toLowerCase();\n");
            sb.append("            return strA.compareTo(strB);\n");
            sb.append("        }\n");
            sb.append("        return compareNatural(a, b);\n");
            sb.append("    }\n\n");
        }

        // 生成compareLength方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private int compareLength(").append(dataType).append(" a, ").append(dataType).append(" b) {\n");
            sb.append("        int lengthA = a != null ? a.toString().length() : 0;\n");
            sb.append("        int lengthB = b != null ? b.toString().length() : 0;\n");
            sb.append("        return Integer.compare(lengthA, lengthB);\n");
            sb.append("    }\n\n");
        }

        // 生成compareHash方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private int compareHash(").append(dataType).append(" a, ").append(dataType).append(" b) {\n");
            sb.append("        return Integer.compare(a.hashCode(), b.hashCode());\n");
            sb.append("    }\n\n");
        }

        // 生成compareCustom方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private int compareCustom(").append(dataType).append(" a, ").append(dataType).append(" b) {\n");
            sb.append("        int hashCompare = Integer.compare(a.hashCode(), b.hashCode());\n");
            sb.append("        int lengthCompare = compareLength(a, b);\n");
            sb.append("        return (hashCompare + lengthCompare) / 2;\n");
            sb.append("    }\n\n");
        }

        // 生成compareNullFirst方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private int compareNullFirst(").append(dataType).append(" a, ").append(dataType).append(" b) {\n");
            sb.append("        if (a == null && b == null) return 0;\n");
            sb.append("        if (a == null) return -1;\n");
            sb.append("        if (b == null) return 1;\n");
            sb.append("        return compareNatural(a, b);\n");
            sb.append("    }\n\n");
        }

        // 生成compareNullLast方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private int compareNullLast(").append(dataType).append(" a, ").append(dataType).append(" b) {\n");
            sb.append("        if (a == null && b == null) return 0;\n");
            sb.append("        if (a == null) return 1;\n");
            sb.append("        if (b == null) return -1;\n");
            sb.append("        return compareNatural(a, b);\n");
            sb.append("    }\n\n");
        }

        // 生成compareMulti方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private int compareMulti(").append(dataType).append(" a, ").append(dataType).append(" b) {\n");
            sb.append("        int hashCompare = compareHash(a, b);\n");
            sb.append("        int lengthCompare = compareLength(a, b);\n");
            sb.append("        int naturalCompare = compareNatural(a, b);\n");
            sb.append("        return (hashCompare + lengthCompare + naturalCompare) / 3;\n");
            sb.append("    }\n\n");
        }

        // 生成compareChain方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private int compareChain(").append(dataType).append(" a, ").append(dataType).append(" b) {\n");
            sb.append("        int result = compareHash(a, b);\n");
            sb.append("        if (result != 0) {\n");
            sb.append("            return result;\n");
            sb.append("        }\n");
            sb.append("        result = compareLength(a, b);\n");
            sb.append("        if (result != 0) {\n");
            sb.append("            return result;\n");
            sb.append("        }\n");
            sb.append("        return compareNatural(a, b);\n");
            sb.append("    }\n\n");
        }

        // 生成sort方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public List<").append(dataType).append("> sort(List<").append(dataType).append("> list) {\n");
            sb.append("        sortedList.clear();\n");
            sb.append("        sortedList.addAll(list);\n");
            sb.append("        java.util.Collections.sort(sortedList, this);\n");
            sb.append("        return sortedList;\n");
            sb.append("    }\n\n");
        }

        // 生成sortArray方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(dataType).append("[] sortArray(").append(dataType).append("[] array) {\n");
            sb.append("        List<").append(dataType).append("> list = new ArrayList<>();\n");
            sb.append("        for (").append(dataType).append(" item : array) {\n");
            sb.append("            list.add(item);\n");
            sb.append("        }\n");
            sb.append("        List<").append(dataType).append("> sorted = sort(list);\n");
            sb.append("        return sorted.toArray(new ").append(dataType).append("[0]);\n");
            sb.append("    }\n\n");
        }

        // 生成binarySearch方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public int binarySearch(List<").append(dataType).append("> list, ").append(dataType).append(" key) {\n");
            sb.append("        List<").append(dataType).append("> sorted = sort(list);\n");
            sb.append("        return java.util.Collections.binarySearch(sorted, key, this);\n");
            sb.append("    }\n\n");
        }

        // 生成min方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(dataType).append(" min(List<").append(dataType).append("> list) {\n");
            sb.append("        if (list == null || list.isEmpty()) {\n");
            sb.append("            return null;\n");
            sb.append("        }\n");
            sb.append("        ").append(dataType).append(" min = list.get(0);\n");
            sb.append("        for (").append(dataType).append(" item : list) {\n");
            sb.append("            if (compare(item, min) < 0) {\n");
            sb.append("                min = item;\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        return min;\n");
            sb.append("    }\n\n");
        }

        // 生成max方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(dataType).append(" max(List<").append(dataType).append("> list) {\n");
            sb.append("        if (list == null || list.isEmpty()) {\n");
            sb.append("            return null;\n");
            sb.append("        }\n");
            sb.append("        ").append(dataType).append(" max = list.get(0);\n");
            sb.append("        for (").append(dataType).append(" item : list) {\n");
            sb.append("            if (compare(item, max) > 0) {\n");
            sb.append("                max = item;\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        return max;\n");
            sb.append("    }\n\n");
        }

        // 生成getComparisonStats方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public Map<String, Integer> getComparisonStats() {\n");
            sb.append("        comparisonStats.clear();\n");
            sb.append("        comparisonStats.put(\"comparisonCount\", comparisonCount);\n");
            sb.append("        comparisonStats.put(\"matchCount\", matchCount);\n");
            sb.append("        comparisonStats.put(\"mismatchCount\", mismatchCount);\n");
            sb.append("        return comparisonStats;\n");
            sb.append("    }\n\n");
        }

        // 生成resetStats方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void resetStats() {\n");
            sb.append("        comparisonCount = 0;\n");
            sb.append("        matchCount = 0;\n");
            sb.append("        mismatchCount = 0;\n");
            sb.append("        comparisonStats.clear();\n");
            sb.append("    }\n\n");
        }

        // 生成与集合关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public List<").append(dataType).append("> getSortedUnique(List<").append(dataType).append("> list) {\n");
            sb.append("        List<").append(dataType).append("> sorted = sort(list);\n");
            sb.append("        List<").append(dataType).append("> unique = new ArrayList<>();\n");
            sb.append("        ").append(dataType).append(" prev = null;\n");
            sb.append("        for (").append(dataType).append(" item : sorted) {\n");
            sb.append("            if (prev == null || compare(item, prev) != 0) {\n");
            sb.append("                unique.add(item);\n");
            sb.append("                prev = item;\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        return unique;\n");
            sb.append("    }\n\n");
        }

        // 生成与计算器关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public double calculateComparisonRatio() {\n");
            sb.append("        if (comparisonCount == 0) {\n");
            sb.append("            return 0.0;\n");
            sb.append("        }\n");
            sb.append("        return (double) matchCount / comparisonCount;\n");
            sb.append("    }\n\n");
        }

        // 生成与图表关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public double[] getComparisonDataPoints() {\n");
            sb.append("        double[] dataPoints = new double[3];\n");
            sb.append("        dataPoints[0] = comparisonCount;\n");
            sb.append("        dataPoints[1] = matchCount;\n");
            sb.append("        dataPoints[2] = mismatchCount;\n");
            sb.append("        return dataPoints;\n");
            sb.append("    }\n\n");
        }

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "comparator");
    }

    private String generateInitialValue(String type) {
        switch (type) {
            case "int":
                return "RandomUtils.between(0, 100)";
            case "long":
                return "RandomUtils.between(0L, 1000L)";
            case "float":
                return "(float) RandomUtils.nextDouble(0.0, 100.0)";
            case "double":
                return "RandomUtils.nextDouble(0.0, 100.0)";
            case "boolean":
                return "RandomUtils.randomBoolean()";
            case "String":
                return "RandomUtils.generateName(\"value\")";
            case "Object":
                return "new Object()";
            default:
                return "null";
        }
    }

    private boolean isStaticField(String fieldName) {
        return fieldName.startsWith("static_");
    }

    private String getFieldType(String fieldName) {
        if (fieldName.endsWith("int")) {
            return "int";
        } else if (fieldName.endsWith("long")) {
            return "long";
        } else if (fieldName.endsWith("float")) {
            return "float";
        } else if (fieldName.endsWith("double")) {
            return "double";
        } else if (fieldName.endsWith("boolean")) {
            return "boolean";
        } else if (fieldName.toLowerCase().endsWith("string")) {
            return "String";
        } else {
            return "Object";
        }
    }
}
