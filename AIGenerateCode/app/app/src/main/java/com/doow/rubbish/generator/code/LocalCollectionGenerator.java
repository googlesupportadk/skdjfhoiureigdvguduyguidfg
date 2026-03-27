package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocalCollectionGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] COLLECTION_TYPES = {
            "List", "Set", "Map", "Queue", "Stack", "Deque", "LinkedList", "ArrayList",
            "HashSet", "TreeSet", "HashMap", "TreeMap", "LinkedHashMap", "PriorityQueue"
    };

    private static final String[] ELEMENT_TYPES = {
            "String", "Integer", "Long", "Double", "Float", "Boolean", "Object"
    };

    private static final String[] DATA_OPERATIONS = {
            "add", "remove", "contains", "size", "clear", "isEmpty", "get", "set",
            "put", "getOrDefault", "keySet", "values", "entrySet", "containsKey", "containsValue"
    };

    private static final String[] FIELD_TYPES = {
            "int", "long", "float", "double", "boolean", "String", "Object"
    };

    private static final String[] ITERATION_TYPES = {
            "for-each", "iterator", "stream", "index"
    };

    private static final String[] FILTER_OPERATIONS = {
            "filter", "map", "flatMap", "distinct", "sorted", "limit", "skip"
    };

    public LocalCollectionGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地集合类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Collection");
            generateCollectionClass(className, asyncHandler);
        }
    }

    private void generateCollectionClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("collection"));

        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.HashSet"));
        sb.append(generateImportStatement("java.util.Iterator"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.Set"));

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        String collectionType = COLLECTION_TYPES[RandomUtils.between(0, COLLECTION_TYPES.length - 1)];
        String elementType = ELEMENT_TYPES[RandomUtils.between(0, ELEMENT_TYPES.length - 1)];
        String iterationType = ITERATION_TYPES[RandomUtils.between(0, ITERATION_TYPES.length - 1)];

        sb.append("public class ").append(className).append(" {\n\n");

        // 添加常量
        sb.append("    private static final String TAG = \"").append(className).append("\");\n");
        sb.append("    private static final String COLLECTION_TYPE = \"").append(collectionType).append("\");\n");
        sb.append("    private static final String ELEMENT_TYPE = \"").append(elementType).append("\");\n");
        sb.append("    private static final String ITERATION_TYPE = \"").append(iterationType).append("\");\n\n");

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

        // 添加集合相关字段
        sb.append("    private List<").append(elementType).append("> dataList;\n");
        sb.append("    private Set<").append(elementType).append("> dataSet;\n");
        sb.append("    private Map<String, ").append(elementType).append("> dataMap;\n");
        sb.append("    private int maxSize;\n");
        sb.append("    private boolean isInitialized;\n");
        sb.append("    private int operationCount;\n\n");

        // 构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this.dataList = new ArrayList<>();\n");
        sb.append("        this.dataSet = new HashSet<>();\n");
        sb.append("        this.dataMap = new HashMap<>();\n");
        sb.append("        this.maxSize = RandomUtils.between(10, 100);\n");
        sb.append("        this.isInitialized = false;\n");
        sb.append("        this.operationCount = 0;\n");

        for (String fieldName : fieldNames) {
            if (!isStaticField(fieldName)) {
                String fieldType = getFieldType(fieldName);
                sb.append("        this.").append(fieldName).append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            }
        }
        sb.append("    }\n\n");

        // 生成初始化方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void initialize() {\n");
            sb.append("        if (!isInitialized) {\n");
            sb.append("            dataList.clear();\n");
            sb.append("            dataSet.clear();\n");
            sb.append("            dataMap.clear();\n");
            sb.append("            isInitialized = true;\n");
            sb.append("            Log.d(TAG, \"Collection initialized\");\n");
            sb.append("        } else {\n");
            sb.append("            Log.w(TAG, \"Collection already initialized\");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成添加元素方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public boolean addElement(").append(elementType).append(" element) {\n");
            sb.append("        if (dataList.size() >= maxSize) {\n");
            sb.append("            Log.e(TAG, \"Collection is full, cannot add element\");\n");
            sb.append("            return false;\n");
            sb.append("        }\n");
            sb.append("        boolean added = dataList.add(element);\n");
            sb.append("        if (added) {\n");
            sb.append("            dataSet.add(element);\n");
            sb.append("            dataMap.put(\"key_\" + operationCount, element);\n");
            sb.append("            operationCount++;\n");
            sb.append("            Log.d(TAG, \"Element added: \" + element);\n");
            sb.append("        }\n");
            sb.append("        return added;\n");
            sb.append("    }\n\n");
        }

        // 生成移除元素方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public boolean removeElement(").append(elementType).append(" element) {\n");
            sb.append("        boolean removed = dataList.remove(element);\n");
            sb.append("        if (removed) {\n");
            sb.append("            dataSet.remove(element);\n");
            sb.append("            dataMap.values().remove(element);\n");
            sb.append("            operationCount++;\n");
            sb.append("            Log.d(TAG, \"Element removed: \" + element);\n");
            sb.append("        } else {\n");
            sb.append("            Log.w(TAG, \"Element not found: \" + element);\n");
            sb.append("        }\n");
            sb.append("        return removed;\n");
            sb.append("    }\n\n");
        }

        // 生成检查元素方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public boolean containsElement(").append(elementType).append(" element) {\n");
            sb.append("        boolean inList = dataList.contains(element);\n");
            sb.append("        boolean inSet = dataSet.contains(element);\n");
            sb.append("        boolean inMap = dataMap.containsValue(element);\n");
            sb.append("        Log.d(TAG, \"Element \" + element + \" - List: \" + inList + \", Set: \" + inSet + \", Map: \" + inMap);\n");
            sb.append("        return inList || inSet || inMap;\n");
            sb.append("    }\n\n");
        }

        // 生成获取大小方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public int getSize() {\n");
            sb.append("        int listSize = dataList.size();\n");
            sb.append("        int setSize = dataSet.size();\n");
            sb.append("        int mapSize = dataMap.size();\n");
            sb.append("        Log.d(TAG, \"Collection sizes - List: \" + listSize + \", Set: \" + setSize + \", Map: \" + mapSize);\n");
            sb.append("        return listSize;\n");
            sb.append("    }\n\n");
        }

        // 生成清空集合方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void clearCollection() {\n");
            sb.append("        int listSize = dataList.size();\n");
            sb.append("        int setSize = dataSet.size();\n");
            sb.append("        int mapSize = dataMap.size();\n");
            sb.append("        dataList.clear();\n");
            sb.append("        dataSet.clear();\n");
            sb.append("        dataMap.clear();\n");
            sb.append("        operationCount = 0;\n");
            sb.append("        Log.d(TAG, \"Collection cleared - Removed List: \" + listSize + \", Set: \" + setSize + \", Map: \" + mapSize);\n");
            sb.append("    }\n\n");
        }

        // 生成检查空集合方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public boolean isEmpty() {\n");
            sb.append("        boolean empty = dataList.isEmpty() && dataSet.isEmpty() && dataMap.isEmpty();\n");
            sb.append("        Log.d(TAG, \"Collection is empty: \" + empty);\n");
            sb.append("        return empty;\n");
            sb.append("    }\n\n");
        }

        // 生成获取元素方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(elementType).append(" getElement(int index) {\n");
            sb.append("        if (index < 0 || index >= dataList.size()) {\n");
            sb.append("            Log.e(TAG, \"Invalid index: \" + index);\n");
            sb.append("            return null;\n");
            sb.append("        }\n");
            sb.append("        ").append(elementType).append(" element = dataList.get(index);\n");
            sb.append("        Log.d(TAG, \"Got element at index \" + index + \": \" + element);\n");
            sb.append("        return element;\n");
            sb.append("    }\n\n");
        }

        // 生成设置元素方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(elementType).append(" setElement(int index, ").append(elementType).append(" element) {\n");
            sb.append("        if (index < 0 || index >= dataList.size()) {\n");
            sb.append("            Log.e(TAG, \"Invalid index: \" + index);\n");
            sb.append("            return null;\n");
            sb.append("        }\n");
            sb.append("        ").append(elementType).append(" oldElement = dataList.set(index, element);\n");
            sb.append("        dataSet.remove(oldElement);\n");
            sb.append("        dataSet.add(element);\n");
            sb.append("        operationCount++;\n");
            sb.append("        Log.d(TAG, \"Set element at index \" + index + \" from \" + oldElement + \" to \" + element);\n");
            sb.append("        return oldElement;\n");
            sb.append("    }\n\n");
        }

        // 生成添加映射方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(elementType).append(" putValue(String key, ").append(elementType).append(" value) {\n");
            sb.append("        ").append(elementType).append(" oldValue = dataMap.put(key, value);\n");
            sb.append("        if (oldValue != null) {\n");
            sb.append("            dataSet.remove(oldValue);\n");
            sb.append("        }\n");
            sb.append("        dataSet.add(value);\n");
            sb.append("        operationCount++;\n");
            sb.append("        Log.d(TAG, \"Put value for key \" + key + \" - Old: \" + oldValue + \", New: \" + value);\n");
            sb.append("        return oldValue;\n");
            sb.append("    }\n\n");
        }

        // 生成获取默认值方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(elementType).append(" getOrDefault(String key, ").append(elementType).append(" defaultValue) {\n");
            sb.append("        ").append(elementType).append(" value = dataMap.getOrDefault(key, defaultValue);\n");
            sb.append("        Log.d(TAG, \"Got value for key \" + key + \": \" + value);\n");
            sb.append("        return value;\n");
            sb.append("    }\n\n");
        }

        // 生成获取键集方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public Set<String> getKeySet() {\n");
            sb.append("        Set<String> keySet = dataMap.keySet();\n");
            sb.append("        Log.d(TAG, \"Got key set with size: \" + keySet.size());\n");
            sb.append("        return keySet;\n");
            sb.append("    }\n\n");
        }

        // 生成获取值集方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public Collection<").append(elementType).append("> getValueCollection() {\n");
            sb.append("        Collection<").append(elementType).append("> values = dataMap.values();\n");
            sb.append("        Log.d(TAG, \"Got value collection with size: \" + values.size());\n");
            sb.append("        return values;\n");
            sb.append("    }\n\n");
        }

        // 生成获取条目集方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public Set<Map.Entry<String, ").append(elementType).append(">> getEntrySet() {\n");
            sb.append("        Set<Map.Entry<String, ").append(elementType).append(">> entrySet = dataMap.entrySet();\n");
            sb.append("        Log.d(TAG, \"Got entry set with size: \" + entrySet.size());\n");
            sb.append("        return entrySet;\n");
            sb.append("    }\n\n");
        }

        // 生成检查键方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public boolean containsKey(String key) {\n");
            sb.append("        boolean contains = dataMap.containsKey(key);\n");
            sb.append("        Log.d(TAG, \"Map contains key \" + key + \": \" + contains);\n");
            sb.append("        return contains;\n");
            sb.append("    }\n\n");
        }

        // 生成检查值方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public boolean containsValue(").append(elementType).append(" value) {\n");
            sb.append("        boolean inList = dataList.contains(value);\n");
            sb.append("        boolean inSet = dataSet.contains(value);\n");
            sb.append("        boolean inMap = dataMap.containsValue(value);\n");
            sb.append("        Log.d(TAG, \"Contains value \" + value + \" - List: \" + inList + \", Set: \" + inSet + \", Map: \" + inMap);\n");
            sb.append("        return inList || inSet || inMap;\n");
            sb.append("    }\n\n");
        }

        // 生成迭代方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void iterateElements() {\n");
            sb.append("        Log.d(TAG, \"Iterating elements using ").append(iterationType).append("\");\n");
            sb.append("        if (ITERATION_TYPE.equals(\"for-each\")) {\n");
            sb.append("            for (").append(elementType).append(" element : dataList) {\n");
            sb.append("                Log.d(TAG, \"Element: \" + element);\n");
            sb.append("            }\n");
            sb.append("        } else if (ITERATION_TYPE.equals(\"iterator\")) {\n");
            sb.append("            Iterator<").append(elementType).append("> iterator = dataList.iterator();\n");
            sb.append("            while (iterator.hasNext()) {\n");
            sb.append("                ").append(elementType).append(" element = iterator.next();\n");
            sb.append("                Log.d(TAG, \"Element: \" + element);\n");
            sb.append("            }\n");
            sb.append("        } else if (ITERATION_TYPE.equals(\"index\")) {\n");
            sb.append("            for (int i = 0; i < dataList.size(); i++) {\n");
            sb.append("                ").append(elementType).append(" element = dataList.get(i);\n");
            sb.append("                Log.d(TAG, \"Element at index \" + i + \": \" + element);\n");
            sb.append("            }\n");
            sb.append("        } else {\n");
            sb.append("            Log.w(TAG, \"Unknown iteration type: \" + ITERATION_TYPE);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成过滤方法
        if (RandomUtils.randomBoolean()) {
            String filterOp = FILTER_OPERATIONS[RandomUtils.between(0, FILTER_OPERATIONS.length - 1)];
            sb.append("    public List<").append(elementType).append("> filterElements() {\n");
            sb.append("        List<").append(elementType).append("> filtered = new ArrayList<>();\n");
            sb.append("        for (").append(elementType).append(" element : dataList) {\n");
            sb.append("            if (RandomUtils.randomBoolean()) {\n");
            sb.append("                filtered.add(element);\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        Log.d(TAG, \"Filtered elements using ").append(filterOp).append(": \" + filtered.size());\n");
            sb.append("        return filtered;\n");
            sb.append("    }\n\n");
        }

        // 生成排序方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void sortElements() {\n");
            sb.append("        if (RandomUtils.randomBoolean()) {\n");
            sb.append("            Log.d(TAG, \"Sorting elements in ascending order\");\n");
            sb.append("        } else {\n");
            sb.append("            Log.d(TAG, \"Sorting elements in descending order\");\n");
            sb.append("        }\n");
            sb.append("        // 排序逻辑取决于元素类型\n");
            sb.append("        operationCount++;\n");
            sb.append("    }\n\n");
        }

        // 生成与计算器关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public int calculateCollectionSize() {\n");
            sb.append("        int totalSize = dataList.size() + dataSet.size() + dataMap.size();\n");
            sb.append("        Log.d(TAG, \"Calculated total collection size: \" + totalSize);\n");
            sb.append("        return totalSize;\n");
            sb.append("    }\n\n");
        }

        // 生成与图表关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public double[] getSizeDataPoints() {\n");
            sb.append("        double[] dataPoints = new double[3];\n");
            sb.append("        dataPoints[0] = dataList.size();\n");
            sb.append("        dataPoints[1] = dataSet.size();\n");
            sb.append("        dataPoints[2] = dataMap.size();\n");
            sb.append("        Log.d(TAG, \"Generated size data points\");\n");
            sb.append("        return dataPoints;\n");
            sb.append("    }\n\n");
        }

        // 生成与计算器关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public double calculateCollectionSum() {\n");
            sb.append("        double sum = 0;\n");
            sb.append("        for (").append(elementType).append(" element : dataList) {\n");
            sb.append("            if (element instanceof Number) {\n");
            sb.append("                sum += ((Number) element).doubleValue();\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        Log.d(TAG, \"Calculated collection sum: \" + sum);\n");
            sb.append("        return sum;\n");
            sb.append("    }\n\n");
        }

        // 生成与图表关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public double[] getCollectionDataPoints() {\n");
            sb.append("        int size = dataList.size();\n");
            sb.append("        if (size == 0) {\n");
            sb.append("            Log.e(TAG, \"Collection is empty\");\n");
            sb.append("            return new double[0];\n");
            sb.append("        }\n");
            sb.append("        double[] dataPoints = new double[size];\n");
            sb.append("        for (int i = 0; i < size; i++) {\n");
            sb.append("            ").append(elementType).append(" element = dataList.get(i);\n");
            sb.append("            if (element instanceof Number) {\n");
            sb.append("                dataPoints[i] = ((Number) element).doubleValue();\n");
            sb.append("            } else {\n");
            sb.append("                dataPoints[i] = 0;\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        Log.d(TAG, \"Generated \" + size + \" collection data points\");\n");
            sb.append("        return dataPoints;\n");
            sb.append("    }\n\n");
        }

        // 生成统计方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void logStats() {\n");
            sb.append("        Log.d(TAG, \"Collection stats - Type: \" + COLLECTION_TYPE);\n");
            sb.append("        Log.d(TAG, \"Element type: \" + ELEMENT_TYPE);\n");
            sb.append("        Log.d(TAG, \"Iteration type: \" + ITERATION_TYPE);\n");
            sb.append("        Log.d(TAG, \"List size: \" + dataList.size());\n");
            sb.append("        Log.d(TAG, \"Set size: \" + dataSet.size());\n");
            sb.append("        Log.d(TAG, \"Map size: \" + dataMap.size());\n");
            sb.append("        Log.d(TAG, \"Max size: \" + maxSize);\n");
            sb.append("        Log.d(TAG, \"Initialized: \" + isInitialized);\n");
            sb.append("        Log.d(TAG, \"Operation count: \" + operationCount);\n");

            sb.append("        // 随机调用辅助方法\n");
            sb.append("        int methodCallCount = RandomUtils.between(1, 3);\n");
            sb.append("        for (int i = 0; i < methodCallCount; i++) {\n");
            sb.append("            int methodIndex = RandomUtils.between(0, ").append(fieldNames.size()).append(" - 1);\n");
            sb.append("            String fieldName = fieldNames.get(methodIndex);\n");
            sb.append("            String fieldType = getFieldType(fieldName);\n");
            sb.append("            \n");
            sb.append("            if (fieldType.equals(\"int\") || fieldType.equals(\"long\") || fieldType.equals(\"float\") || fieldType.equals(\"double\")) {\n");
            sb.append("                calculate").append(capitalize("fieldName")).append("();\n");
            sb.append("            } else if (fieldType.equals(\"boolean\")) {\n");
            sb.append("                validate").append(capitalize("fieldName")).append("();\n");
            sb.append("            } else {\n");
            sb.append("                process").append(capitalize("fieldName")).append("();\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "collection");
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

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
