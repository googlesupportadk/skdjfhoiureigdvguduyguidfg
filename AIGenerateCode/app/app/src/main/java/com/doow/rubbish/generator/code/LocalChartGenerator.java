package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.ArrayList;
import java.util.List;

public class LocalChartGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] CHART_TYPES = {
            "LineChart", "BarChart", "PieChart", "ScatterChart", "AreaChart",
            "BubbleChart", "CandlestickChart", "RadarChart", "PolarChart",
            "StackedBarChart", "GroupedBarChart", "HorizontalBarChart", "StackedAreaChart"
    };

    private static final String[] DATA_TYPES = {
            "float", "double", "int", "long"
    };

    private static final String[] FIELD_TYPES = {
            "int", "long", "float", "double", "boolean", "String", "Object"
    };

    private static final String[] ANIMATION_TYPES = {
            "fade_in", "slide_in", "scale_in", "rotate_in", "none"
    };

    private static final String[] COLOR_SCHEMES = {
            "default", "gradient", "solid", "pattern", "custom"
    };

    public LocalChartGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地图表类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Chart");
            generateChartClass(className, asyncHandler);
        }
    }

    private void generateChartClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("chart"));

        sb.append(generateImportStatement("android.graphics.Color"));
        sb.append(generateImportStatement("android.util.Log"));

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        String chartType = CHART_TYPES[RandomUtils.between(0, CHART_TYPES.length - 1)];
        String dataType = DATA_TYPES[RandomUtils.between(0, DATA_TYPES.length - 1)];
        String animationType = ANIMATION_TYPES[RandomUtils.between(0, ANIMATION_TYPES.length - 1)];
        String colorScheme = COLOR_SCHEMES[RandomUtils.between(0, COLOR_SCHEMES.length - 1)];

        sb.append("public class ").append(className).append(" {\n\n");

        // 添加常量
        sb.append("    private static final String TAG = \"").append(className).append("\");\n");
        sb.append("    private static final String CHART_TYPE = \"").append(chartType).append("\");\n");
        sb.append("    private static final String DATA_TYPE = \"").append(dataType).append("\");\n");
        sb.append("    private static final String ANIMATION_TYPE = \"").append(animationType).append("\");\n");
        sb.append("    private static final String COLOR_SCHEME = \"").append(colorScheme).append("\");\n\n");

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

        // 添加图表相关字段
        sb.append("    private ").append(dataType).append("[] data;\n");
        sb.append("    private int dataSize;\n");
        sb.append("    private int maxDataSize;\n");
        sb.append("    private int currentDataIndex;\n");
        sb.append("    private boolean isAnimated;\n");
        sb.append("    private int animationDuration;\n");
        sb.append("    private int primaryColor;\n");
        sb.append("    private int secondaryColor;\n\n");

        // 构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this.maxDataSize = RandomUtils.between(10, 100);\n");
        sb.append("        this.dataSize = 0;\n");
        sb.append("        this.currentDataIndex = 0;\n");
        sb.append("        this.isAnimated = RandomUtils.randomBoolean();\n");
        sb.append("        this.animationDuration = RandomUtils.between(100, 1000);\n");
        sb.append("        this.primaryColor = Color.BLUE;\n");
        sb.append("        this.secondaryColor = Color.RED;\n");
        sb.append("        this.data = new ").append(dataType).append("[maxDataSize];\n");

        for (String fieldName : fieldNames) {
            if (!isStaticField(fieldName)) {
                String fieldType = getFieldType(fieldName);
                sb.append("        this.").append(fieldName).append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            }
        }
        sb.append("    }\n\n");

        // 生成添加数据方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public boolean addData(").append(dataType).append(" value) {\n");
            sb.append("        if (dataSize < maxDataSize) {\n");
            sb.append("            data[dataSize] = value;\n");
            sb.append("            dataSize++;\n");
            sb.append("            Log.d(TAG, \"Added data at index \" + dataSize + \": \" + value);\n");
            sb.append("            return true;\n");
            sb.append("        } else {\n");
            sb.append("            Log.e(TAG, \"Cannot add data: chart is full\");\n");
            sb.append("            return false;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成移除数据方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public boolean removeData(int index) {\n");
            sb.append("        if (index >= 0 && index < dataSize) {\n");
            sb.append("            for (int i = index; i < dataSize - 1; i++) {\n");
            sb.append("                data[i] = data[i + 1];\n");
            sb.append("            }\n");
            sb.append("            dataSize--;\n");
            sb.append("            Log.d(TAG, \"Removed data at index \" + index);\n");
            sb.append("            return true;\n");
            sb.append("        } else {\n");
            sb.append("            Log.e(TAG, \"Invalid index: \" + index);\n");
            sb.append("            return false;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成更新数据方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public boolean updateData(int index, ").append(dataType).append(" value) {\n");
            sb.append("        if (index >= 0 && index < dataSize) {\n");
            sb.append("            data[index] = value;\n");
            sb.append("            Log.d(TAG, \"Updated data at index \" + index + \": \" + value);\n");
            sb.append("            return true;\n");
            sb.append("        } else {\n");
            sb.append("            Log.e(TAG, \"Invalid index: \" + index);\n");
            sb.append("            return false;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成获取数据方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(dataType).append(" getData(int index) {\n");
            sb.append("        if (index >= 0 && index < dataSize) {\n");
            sb.append("            return data[index];\n");
            sb.append("        } else {\n");
            sb.append("            Log.e(TAG, \"Invalid index: \" + index);\n");
            sb.append("            return 0;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成清空数据方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void clearData() {\n");
            sb.append("        dataSize = 0;\n");
            sb.append("        currentDataIndex = 0;\n");
            sb.append("        Log.d(TAG, \"Cleared all data\");\n");
            sb.append("    }\n\n");
        }

        // 生成辅助方法
        for (String fieldName : fieldNames) {
            String fieldType = getFieldType(fieldName);

            if (fieldType.equals("int") || fieldType.equals("long") || fieldType.equals("float") || fieldType.equals("double")) {
                sb.append("    private double calculate").append(capitalize(fieldName)).append("() {\n");
                sb.append("        double fieldValue = ").append(fieldName).append(";\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            fieldValue += ").append(fieldName).append(";\n");
                sb.append("        } else {\n");
                sb.append("            fieldValue *= 2;\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Calculated ").append(fieldName).append(": \" + fieldValue);\n");
                sb.append("        return fieldValue;\n");
                sb.append("    }\n\n");
            } else if (fieldType.equals("boolean")) {
                sb.append("    private boolean validate").append(capitalize(fieldName)).append("() {\n");
                sb.append("        boolean isValid = ").append(fieldName).append(";\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            isValid = !isValid;\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Validated ").append(fieldName).append(": \" + isValid);\n");
                sb.append("        return isValid;\n");
                sb.append("    }\n\n");
            } else {
                sb.append("    private void process").append(capitalize(fieldName)).append("() {\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            ").append(fieldName).append(" = \"processed_\" + ").append(fieldName).append(";\n");
                sb.append("        } else {\n");
                sb.append("            ").append(fieldName).append(" = String.valueOf(").append(fieldName).append(".hashCode());\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Processed ").append(fieldName).append(": \" + ").append(fieldName).append(");\n");
                sb.append("    }\n\n");
            }
        }

        // 生成统计方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(dataType).append(" calculateAverage() {\n");
            sb.append("        if (dataSize == 0) {\n");
            sb.append("            Log.e(TAG, \"No data to calculate average\");\n");
            sb.append("            return 0;\n");
            sb.append("        }\n");
            sb.append("        ").append(dataType).append(" sum = 0;\n");
            sb.append("        for (int i = 0; i < dataSize; i++) {\n");
            sb.append("            sum += data[i];\n");
            sb.append("        }\n");
            sb.append("        ").append(dataType).append(" average = sum / dataSize;\n");
            sb.append("        Log.d(TAG, \"Average: \" + average);\n");
            sb.append("        return average;\n");
            sb.append("    }\n\n");
        }

        // 生成最大值方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(dataType).append(" findMax() {\n");
            sb.append("        if (dataSize == 0) {\n");
            sb.append("            Log.e(TAG, \"No data to find max\");\n");
            sb.append("            return 0;\n");
            sb.append("        }\n");
            sb.append("        ").append(dataType).append(" max = data[0];\n");
            sb.append("        for (int i = 1; i < dataSize; i++) {\n");
            sb.append("            if (data[i] > max) {\n");
            sb.append("                max = data[i];\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("        Log.d(TAG, \"Max value: \" + max);\n");
            sb.append("        return max;\n");
            sb.append("    }\n\n");
        }

        // 生成最小值方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(dataType).append(" findMin() {\n");
            sb.append("        if (dataSize == 0) {\n");
            sb.append("            Log.e(TAG, \"No data to find min\");\n");
            sb.append("            return 0;\n");
            sb.append("        }\n");
            sb.append("        ").append(dataType).append(" min = data[0];\n");
            sb.append("        for (int i = 1; i < dataSize; i++) {\n");
            sb.append("            if (data[i] < min) {\n");
            sb.append("            min = data[i];\n");
            sb.append("        }\n");
            sb.append("        }\n");
            sb.append("        Log.d(TAG, \"Min value: \" + min);\n");
            sb.append("        return min;\n");
            sb.append("    }\n\n");
        }

        // 生成颜色设置方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void setColors(int primary, int secondary) {\n");
            sb.append("        this.primaryColor = primary;\n");
            sb.append("        this.secondaryColor = secondary;\n");
            sb.append("        Log.d(TAG, \"Colors set - Primary: \" + primary + \", Secondary: \" + secondary);\n");
            sb.append("    }\n\n");
        }

        // 生成动画设置方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void setAnimation(boolean animated, int duration) {\n");
            sb.append("        this.isAnimated = animated;\n");
            sb.append("        this.animationDuration = duration;\n");
            sb.append("        Log.d(TAG, \"Animation set - Animated: \" + animated + \", Duration: \" + duration);\n");
            sb.append("    }\n\n");
        }

        // 生成辅助方法
        for (String fieldName : fieldNames) {
            String fieldType = getFieldType(fieldName);

            if (fieldType.equals("int") || fieldType.equals("long") || fieldType.equals("float") || fieldType.equals("double")) {
                sb.append("    private ").append(fieldType).append(" calculate").append(capitalize(fieldName)).append("() {\n");
                sb.append("        ").append(fieldType).append(" fieldValue = ").append(fieldName).append(";\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            fieldValue += ").append(fieldName).append(";\n");
                sb.append("        } else {\n");
                sb.append("            fieldValue *= 2;\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Calculated ").append(fieldName).append(": \" + fieldValue);\n");
                sb.append("        return fieldValue;\n");
                sb.append("    }\n\n");
            } else if (fieldType.equals("boolean")) {
                sb.append("    private boolean validate").append(capitalize(fieldName)).append("() {\n");
                sb.append("        boolean isValid = ").append(fieldName).append(";\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            isValid = !isValid;\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Validated ").append(fieldName).append(": \" + isValid);\n");
                sb.append("        return isValid;\n");
                sb.append("    }\n\n");
            } else {
                sb.append("    private void process").append(capitalize(fieldName)).append("() {\n");
                sb.append("        if (RandomUtils.randomBoolean()) {\n");
                sb.append("            ").append(fieldName).append(" = \"processed_\" + ").append(fieldName).append(";\n");
                sb.append("        } else {\n");
                sb.append("            ").append(fieldName).append(" = String.valueOf(").append(fieldName).append(".hashCode());\n");
                sb.append("        }\n");
                sb.append("        Log.d(TAG, \"Processed ").append(fieldName).append(": \" + ").append(fieldName).append(");\n");
                sb.append("    }\n\n");
            }
        }

        // 生成统计方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void logStats() {\n");
            sb.append("        Log.d(TAG, \"Chart stats - Type: \" + CHART_TYPE);\n");
            sb.append("        Log.d(TAG, \"Data type: \" + DATA_TYPE);\n");
            sb.append("        Log.d(TAG, \"Data size: \" + dataSize);\n");
            sb.append("        Log.d(TAG, \"Max data size: \" + maxDataSize);\n");
            sb.append("        Log.d(TAG, \"Animated: \" + isAnimated);\n");
            sb.append("        Log.d(TAG, \"Animation duration: \" + animationDuration);\n");
            sb.append("        Log.d(TAG, \"Primary color: \" + primaryColor);\n");
            sb.append("        Log.d(TAG, \"Secondary color: \" + secondaryColor);\n");
            sb.append("        Log.d(TAG, \"Animation type: \" + ANIMATION_TYPE);\n");
            sb.append("        Log.d(TAG, \"Color scheme: \" + COLOR_SCHEME);\n");
            sb.append("        \n");
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

        generateJavaFile(className, sb.toString(), "chart");
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
