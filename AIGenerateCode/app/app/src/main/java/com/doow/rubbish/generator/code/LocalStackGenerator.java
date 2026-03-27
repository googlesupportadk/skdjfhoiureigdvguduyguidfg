package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalStackGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] STACK_TYPES = {
            "Stack", "LinkedList", "ArrayDeque", "ConcurrentLinkedBlockingDeque"
    };

    private boolean includeBasicOps;
    private boolean includeBatchOps;
    private boolean includeFilterOps;
    private boolean includeTransformOps;
    private boolean includeSearchOps;
    private boolean includeValidationOps;
    private boolean includeConversionOps;
    private boolean includeIteratorOps;
    private boolean includeStreamOps;
    private boolean includeUtilityOps;

    private String tagVar;
    private String stackVar;
    private String tempListVar;
    private String tempDequeVar;
    private String resultVar;
    private String iteratorVar;
    private String comparatorVar;
    private int logCount;

    public LocalStackGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        initializeFeatures();
    }

    private void initializeFeatures() {
        includeBasicOps = true;
        includeBatchOps = RandomUtils.randomBoolean();
        includeFilterOps = RandomUtils.randomBoolean();
        includeTransformOps = RandomUtils.randomBoolean();
        includeSearchOps = RandomUtils.randomBoolean();
        includeValidationOps = RandomUtils.randomBoolean();
        includeConversionOps = RandomUtils.randomBoolean();
        includeIteratorOps = RandomUtils.randomBoolean();
        includeStreamOps = RandomUtils.randomBoolean();
        includeUtilityOps = RandomUtils.randomBoolean();
        logCount = 0;
    }

    @Override
    public void generateAll() throws Exception {
        int classCount = RandomUtils.between(3, 8);
        for (int i = 0; i < classCount; i++) {
            initializeFeatures();
            String className = RandomUtils.generateClassName("Stack");
            String stackType = RandomUtils.randomChoice(STACK_TYPES);
            generateStackClass(className, stackType);
        }
    }

    private void generateStackClass(String className, String stackType) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("stack"));
        generateImports(sb);
        sb.append("public class ").append(className).append("<T> {\n\n");
        generateFields(sb, className, stackType);
        generateConstructors(sb, className, stackType);
        generateMethods(sb, className, stackType);
        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "stack");
    }

    private void generateImports(StringBuilder sb) {
        sb.append(generateImportStatement("java.util.Stack"));
        sb.append(generateImportStatement("java.util.LinkedList"));
        sb.append(generateImportStatement("java.util.ArrayDeque"));
        sb.append(generateImportStatement("java.util.concurrent.ConcurrentLinkedBlockingDeque"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.Vector"));
        sb.append(generateImportStatement("java.util.Queue"));
        sb.append(generateImportStatement("java.util.function.Consumer"));
        sb.append(generateImportStatement("java.util.function.Predicate"));
        sb.append(generateImportStatement("java.util.function.Function"));
        sb.append(generateImportStatement("java.util.Comparator"));
        sb.append(generateImportStatement("java.util.stream.Collectors"));
        sb.append(generateImportStatement("java.util.stream.Stream"));
        sb.append(generateImportStatement("java.util.Optional"));
        sb.append(generateImportStatement("java.util.Objects"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append("\n");
    }

    private void generateFields(StringBuilder sb, String className, String stackType) {
        tagVar = generateRandomVarName("tag");
        sb.append("    private static final String ").append(tagVar).append(" = \"").append(className).append("\";\n");

        String typeVar = generateRandomVarName("stackType");
        sb.append("    private static final String ").append(typeVar).append(" = \"").append(stackType).append("\";\n");

        stackVar = generateRandomVarName("stack");
        sb.append("    private Deque<T> ").append(stackVar).append(" = new ").append(stackType).append("<>();\n");

        if (includeSearchOps || includeFilterOps) {
            tempDequeVar = generateRandomVarName("tempDeque");
            sb.append("    private Deque<T> ").append(tempDequeVar).append(" = new ArrayDeque<>();\n");
        }

        if (includeTransformOps || includeBatchOps) {
            tempListVar = generateRandomVarName("tempList");
            sb.append("    private List<T> ").append(tempListVar).append(" = new ArrayList<>();\n");
        }

        if (includeUtilityOps) {
            comparatorVar = generateRandomVarName("comparator");
            sb.append("    private Comparator<T> ").append(comparatorVar).append(";\n");
        }

        sb.append("\n");
    }

    private void generateConstructors(StringBuilder sb, String className, String stackType) {
        sb.append("    public ").append(className).append("() {\n");
        sb.append("    }\n\n");

        String itemsParam = generateRandomParamName("items");
        sb.append("    public ").append(className).append("(Collection<T> ").append(itemsParam).append(") {\n");
        sb.append("        if (").append(itemsParam).append(" != null) {\n");
        sb.append("            ").append(stackVar).append(" = new ").append(stackType).append("<>();\n");
        sb.append("            for (T item : ").append(itemsParam).append(") {\n");
        sb.append("                ").append(stackVar).append(".push(item);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        if (includeUtilityOps) {
            String comparatorParam = generateRandomParamName("comparator");
            sb.append("    public ").append(className).append("(Collection<T> ").append(itemsParam).append(", Comparator<T> ").append(comparatorParam).append(") {\n");
            sb.append("        if (").append(itemsParam).append(" != null && ").append(comparatorParam).append(" != null) {\n");
            sb.append("            ").append(comparatorVar).append(" = ").append(comparatorParam).append(";\n");
            sb.append("            ").append(stackVar).append(" = new ").append(stackType).append("();\n");
            sb.append("            for (T item : ").append(itemsParam).append(") {\n");
            sb.append("                ").append(stackVar).append(".push(item);\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }
    }

    private void generateMethods(StringBuilder sb, String className, String stackType) {
        if (includeBasicOps) {
            generateBasicOperations(sb);
        }
        if (includeBatchOps) {
            generateBatchOperations(sb);
        }
        if (includeFilterOps) {
            generateFilterOperations(sb);
        }
        if (includeTransformOps) {
            generateTransformOperations(sb);
        }
        if (includeSearchOps) {
            generateSearchOperations(sb);
        }
        if (includeValidationOps) {
            generateValidationOperations(sb);
        }
        if (includeConversionOps) {
            generateConversionMethods(sb);
        }
        if (includeIteratorOps) {
            generateIteratorOperations(sb);
        }
        if (includeStreamOps) {
            generateStreamOperations(sb);
        }
        if (includeUtilityOps) {
            generateUtilityMethods(sb);
        }
    }

    private void generateBasicOperations(StringBuilder sb) {
        String pushMethod = generateRandomMethodName("push");
        String itemParam = generateRandomParamName("item");
        generateMethodWithDoc(sb, pushMethod, "", "void", itemParam, "", "");
        sb.append("        if (").append(itemParam).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        ").append(stackVar).append(".push(").append(itemParam).append(");\n");
        sb.append("    }\n\n");

        String popMethod = generateRandomMethodName("pop");
        generateMethodWithDoc(sb, popMethod, "", "T", "", "", "");
        sb.append("        if (").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return ").append(stackVar).append(".pop();\n");
        sb.append("    }\n\n");

        String peekMethod = generateRandomMethodName("peek");
        generateMethodWithDoc(sb, peekMethod, "", "T", "", "", "");
        sb.append("        if (").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return ").append(stackVar).append(".peek();\n");
        sb.append("    }\n\n");

        String sizeMethod = generateRandomMethodName("size");
        generateMethodWithDoc(sb, sizeMethod, "", "int", "", "", "");
        sb.append("        return ").append(stackVar).append(".size();\n");
        sb.append("    }\n\n");

        String isEmptyMethod = generateRandomMethodName("isEmpty");
        generateMethodWithDoc(sb, isEmptyMethod, "", "boolean", "", "", "");
        sb.append("        return ").append(stackVar).append(".isEmpty();\n");
        sb.append("    }\n\n");

        String clearMethod = generateRandomMethodName("clear");
        generateMethodWithDoc(sb, clearMethod, "", "void", "", "", "");
        sb.append("        ").append(stackVar).append(".clear();\n");
        sb.append("    }\n\n");
    }

    private void generateBatchOperations(StringBuilder sb) {
        resultVar = generateRandomVarName("result");

        String pushAllMethod = generateRandomMethodName("pushAll");
        String itemsParam = generateRandomParamName("items");
        generateMethodWithDoc(sb, pushAllMethod, "", "void", "Collection<T> " + itemsParam, "", "");
        sb.append("        if (").append(itemsParam).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        for (T item : ").append(itemsParam).append(") {\n");
        sb.append("            ").append(stackVar).append(".push(item);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        String popAllMethod = generateRandomMethodName("popAll");
        String countParam = generateRandomParamName("count");
        generateMethodWithDoc(sb, popAllMethod, "", "List<T>", countParam, "", "");
        sb.append("        if (").append(countParam).append(" <= 0) {\n");
        sb.append("            return new ArrayList<>();\n");
        sb.append("        }\n");
        sb.append("        List<T> ").append(resultVar).append(" = new ArrayList<>();\n");
        sb.append("        int actualCount = Math.min(").append(countParam).append(", ").append(stackVar).append(".size());\n");
        sb.append("        for (int i = 0; i < actualCount; i++) {\n");
        sb.append("            T item = ").append(stackVar).append(".pop();\n");
        sb.append("            if (item != null) {\n");
        sb.append("                ").append(resultVar).append(".add(item);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultVar).append(";\n");
        sb.append("    }\n\n");
    }

    private void generateFilterOperations(StringBuilder sb) {
        String filterMethod = generateRandomMethodName("filter");
        String predicateParam = generateRandomParamName("predicate");
        generateMethodWithDoc(sb, filterMethod, "", "Stack<T>", predicateParam, "", "");
        sb.append("        if (").append(predicateParam).append(" == null) {\n");
        sb.append("            return new Stack<>();\n");
        sb.append("        }\n");
        sb.append("        Stack<T> ").append(tempDequeVar).append(" = new Stack<>();\n");
        sb.append("        while (!").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            T item = ").append(stackVar).append(".pop();\n");
        sb.append("            if (!").append(predicateParam).append(".test(item)) {\n");
        sb.append("                ").append(tempDequeVar).append(".push(item);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        while (!").append(tempDequeVar).append(".isEmpty()) {\n");
        sb.append("            ").append(stackVar).append(".push(").append(tempDequeVar).append(".pop());\n");
        sb.append("        }\n");
        sb.append("        return ").append(stackVar).append(";\n");
        sb.append("    }\n\n");

        String removeIfMethod = generateRandomMethodName("removeIf");
        generateMethodWithDoc(sb, removeIfMethod, "", "int", predicateParam, "", "");
        sb.append("        if (").append(predicateParam).append(" == null) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        int count = 0;\n");
        sb.append("        ").append(tempDequeVar).append(".clear();\n");
        sb.append("        while (!").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            T item = ").append(stackVar).append(".pop();\n");
        sb.append("            if (").append(predicateParam).append(".test(item)) {\n");
        sb.append("                ").append(tempDequeVar).append(".push(item);\n");
        sb.append("            } else {\n");
        sb.append("                count++;\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        while (!").append(tempDequeVar).append(".isEmpty()) {\n");
        sb.append("            ").append(stackVar).append(".push(").append(tempDequeVar).append(".pop());\n");
        sb.append("        }\n");
        sb.append("        return count;\n");
        sb.append("    }\n\n");
    }

    private void generateTransformOperations(StringBuilder sb) {
        String reverseMethod = generateRandomMethodName("reverse");
        generateMethodWithDoc(sb, reverseMethod, "", "void", "", "", "");
        sb.append("        if (").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        while (!").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            ").append(tempListVar).append(".add(").append(stackVar).append(".pop());\n");
        sb.append("        }\n");
        sb.append("        while (!").append(tempListVar).append(".isEmpty()) {\n");
        sb.append("            ").append(stackVar).append(".push(").append(tempListVar).append(".remove(").append(tempListVar).append(".size() - 1));\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        String sortMethod = generateRandomMethodName("sort");
        generateMethodWithDoc(sb, sortMethod, "", "void", "", "", "");
        sb.append("        if (").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        while (!").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            ").append(tempListVar).append(".add(").append(stackVar).append(".pop());\n");
        sb.append("        }\n");
        sb.append("        ").append(tempListVar).append(".sort(").append(comparatorVar).append(");\n");
        sb.append("        while (!").append(tempListVar).append(".isEmpty()) {\n");
        sb.append("            ").append(stackVar).append(".push(").append(tempListVar).append(".remove(").append(tempListVar).append(".size() - 1));\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        String shuffleMethod = generateRandomMethodName("shuffle");
        generateMethodWithDoc(sb, shuffleMethod, "", "void", "", "", "");
        sb.append("        if (").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        while (!").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            ").append(tempListVar).append(".add(").append(stackVar).append(".pop());\n");
        sb.append("        }\n");
        sb.append("        Collections.shuffle(").append(tempListVar).append(");\n");
        sb.append("        while (!").append(tempListVar).append(".isEmpty()) {\n");
        sb.append("            ").append(stackVar).append(".push(").append(tempListVar).append(".remove(").append(tempListVar).append(".size() - 1));\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateSearchOperations(StringBuilder sb) {
        String searchMethod = generateRandomMethodName("search");
        String itemParam = generateRandomParamName("item");
        generateMethodWithDoc(sb, searchMethod, "", "int", itemParam, "", "");
        sb.append("        if (").append(itemParam).append(" == null) {\n");
        sb.append("            return -1;\n");
        sb.append("        }\n");
        sb.append("        int index = 0;\n");
        sb.append("        ").append(tempDequeVar).append(".clear();\n");
        sb.append("        while (!").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            T current = ").append(stackVar).append(".pop();\n");
        sb.append("            ").append(tempDequeVar).append(".push(current);\n");
        sb.append("            if (current.equals(").append(itemParam).append(")) {\n");
        sb.append("                return ").append(stackVar).append(".size() - ").append(tempDequeVar).append(".size();\n");
        sb.append("            }\n");
        sb.append("            index++;\n");
        sb.append("        }\n");
        sb.append("        while (!").append(tempDequeVar).append(".isEmpty()) {\n");
        sb.append("            ").append(stackVar).append(".push(").append(tempDequeVar).append(".pop());\n");
        sb.append("        }\n");
        sb.append("        return -1;\n");
        sb.append("    }\n\n");

        String containsMethod = generateRandomMethodName("contains");
        generateMethodWithDoc(sb, containsMethod, "", "boolean", itemParam, "", "");
        sb.append("        if (").append(itemParam).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        boolean found = false;\n");
        sb.append("        ").append(tempDequeVar).append(".clear();\n");
        sb.append("        while (!").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            T current = ").append(stackVar).append(".pop();\n");
        sb.append("            ").append(tempDequeVar).append(".push(current);\n");
        sb.append("            if (current.equals(").append(itemParam).append(")) {\n");
        sb.append("                found = true;\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        while (!").append(tempDequeVar).append(".isEmpty()) {\n");
        sb.append("            ").append(stackVar).append(".push(").append(tempDequeVar).append(".pop());\n");
        sb.append("        }\n");
        sb.append("        return found;\n");
        sb.append("    }\n\n");
    }

    private void generateValidationOperations(StringBuilder sb) {
        String equalsMethod = generateRandomMethodName("equals");
        String otherParam = generateRandomParamName("other");
        generateMethodWithDoc(sb, equalsMethod, "", "boolean", "Stack<T> " + otherParam, "", "");
        sb.append("        if (").append(otherParam).append(" == null) {\n");
        sb.append("            return ").append(stackVar).append(".isEmpty();\n");
        sb.append("        }\n");
        sb.append("        boolean equals = ").append(stackVar).append(".size() == ").append(otherParam).append(".size();\n");
        sb.append("        if (equals) {\n");
        sb.append("            ").append(tempDequeVar).append(".clear();\n");
        sb.append("            while (!").append(stackVar).append(".isEmpty()) {\n");
        sb.append("                T item1 = ").append(stackVar).append(".pop();\n");
        sb.append("                T item2 = ").append(otherParam).append(".pop();\n");
        sb.append("                if (!Objects.equals(item1, item2)) {\n");
        sb.append("                    equals = false;\n");
        sb.append("                    break;\n");
        sb.append("                }\n");
        sb.append("                ").append(tempDequeVar).append(".push(item1);\n");
        sb.append("                ").append(tempDequeVar).append(".push(item2);\n");
        sb.append("            }\n");
        sb.append("            while (!").append(tempDequeVar).append(".isEmpty()) {\n");
        sb.append("                ").append(stackVar).append(".push(").append(tempDequeVar).append(".pop());\n");
        sb.append("            }\n");
        sb.append("            while (!").append(tempDequeVar).append(".isEmpty()) {\n");
        sb.append("                ").append(otherParam).append(".push(").append(tempDequeVar).append(".pop());\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return equals;\n");
        sb.append("    }\n\n");

        String hashCodeMethod = generateRandomMethodName("hashCode");
        generateMethodWithDoc(sb, hashCodeMethod, "", "int", "", "", "");
        sb.append("        return ").append(stackVar).append(".hashCode();\n");
        sb.append("    }\n\n");
    }

    private void generateConversionMethods(StringBuilder sb) {
        resultVar = generateRandomVarName("result");

        String toListMethod = generateRandomMethodName("toList");
        generateMethodWithDoc(sb, toListMethod, "", "List<T>", "", "", "");
        sb.append("        List<T> ").append(resultVar).append(" = new ArrayList<>(").append(stackVar).append(");\n");
        sb.append("        return ").append(resultVar).append(";\n");
        sb.append("    }\n\n");

        String toArrayMethod = generateRandomMethodName("toArray");
        generateMethodWithDoc(sb, toArrayMethod, "", "T[]", "", "", "");
        sb.append("        return (T[]) ").append(stackVar).append(".toArray();\n");
        sb.append("    }\n\n");

        String toStringMethod = generateRandomMethodName("toCustomString");
        generateMethodWithDoc(sb, toStringMethod, "", "String", "", "", "");
        sb.append("        return ").append(stackVar).append(".toString();\n");
        sb.append("    }\n\n");
    }

    private void generateIteratorOperations(StringBuilder sb) {
        String iteratorMethod = generateRandomMethodName("iterator");
        generateMethodWithDoc(sb, iteratorMethod, "", "Iterator<T>", "", "", "");
        sb.append("        return ").append(stackVar).append(".iterator();\n");
        sb.append("    }\n\n");

        String forEachMethod = generateRandomMethodName("forEach");
        String actionParam = generateRandomParamName("action");
        generateMethodWithDoc(sb, forEachMethod, "", "void", "Consumer<T> " + actionParam, "", "");
        sb.append("        if (").append(actionParam).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        for (T item : ").append(stackVar).append(") {\n");
        sb.append("            ").append(actionParam).append(".accept(item);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateStreamOperations(StringBuilder sb) {
        String streamMethod = generateRandomMethodName("stream");
        generateMethodWithDoc(sb, streamMethod, "", "Stream<T>", "", "", "");
        sb.append("        return ").append(stackVar).append(".stream();\n");
        sb.append("    }\n\n");

        String collectMethod = generateRandomMethodName("collect");
        String collectorParam = generateRandomParamName("collector");
        generateMethodWithDoc(sb, collectMethod, "", "<R, A> R", "Collector<T, A, R> " + collectorParam, "", "");
        sb.append("        if (").append(collectorParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return ").append(stackVar).append(".stream().collect(").append(collectorParam).append(");\n");
        sb.append("    }\n\n");
    }

    private void generateUtilityMethods(StringBuilder sb) {
        String minMethod = generateRandomMethodName("min");
        generateMethodWithDoc(sb, minMethod, "", "T", "", "", "");
        sb.append("        if (").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        T min = ").append(stackVar).append(".peek();\n");
        sb.append("        while (!").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            T current = ").append(stackVar).append(".pop();\n");
        sb.append("            if (current != null && (min == null || ((Comparable<T>)current).compareTo(min) < 0)) {\n");
        sb.append("                min = current;\n");
        sb.append("            }\n");
        sb.append("            ").append(tempDequeVar).append(".push(current);\n");
        sb.append("        }\n");
        sb.append("        while (!").append(tempDequeVar).append(".isEmpty()) {\n");
        sb.append("            ").append(stackVar).append(".push(").append(tempDequeVar).append(".pop());\n");
        sb.append("        }\n");
        sb.append("        return min;\n");
        sb.append("    }\n\n");

        String maxMethod = generateRandomMethodName("max");
        generateMethodWithDoc(sb, maxMethod, "", "T", "", "", "");
        sb.append("        if (").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        T max = ").append(stackVar).append(".peek();\n");
        sb.append("        while (!").append(stackVar).append(".isEmpty()) {\n");
        sb.append("            T current = ").append(stackVar).append(".pop();\n");
        sb.append("            if (current != null && (max == null || ((Comparable<T>)current).compareTo(max) > 0)) {\n");
        sb.append("                max = current;\n");
        sb.append("            }\n");
        sb.append("            ").append(tempDequeVar).append(".push(current);\n");
        sb.append("        }\n");
        sb.append("        while (!").append(tempDequeVar).append(".isEmpty()) {\n");
        sb.append("            ").append(stackVar).append(".push(").append(tempDequeVar).append(".pop());\n");
        sb.append("        }\n");
        sb.append("        return max;\n");
        sb.append("    }\n\n");
    }

    private void generateMethodWithDoc(StringBuilder sb, String methodName, String description, String returnType, String paramType, String paramName, String returnDesc) {
        sb.append("    /**\n");
        sb.append("     */\n");
        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(");
        if (!paramType.isEmpty()) {
            sb.append(paramType).append(" ").append(paramName).append(") {\n");
        } else {
            sb.append(") {\n");
        }
    }

    private String generateRandomVarName(String prefix) {
        return RandomUtils.generateWord(3).toLowerCase() + RandomUtils.generateWord(4) + prefix;
    }

    private String generateRandomParamName(String prefix) {
        return RandomUtils.generateWord(3).toLowerCase() + RandomUtils.generateWord(4) + prefix;
    }

    private String generateRandomMethodName(String prefix) {
        return RandomUtils.generateWord(3).toLowerCase() + RandomUtils.generateWord(4) + prefix;
    }

    private String generateRandomKey() {
        return RandomUtils.generateRandomString(16);
    }
}