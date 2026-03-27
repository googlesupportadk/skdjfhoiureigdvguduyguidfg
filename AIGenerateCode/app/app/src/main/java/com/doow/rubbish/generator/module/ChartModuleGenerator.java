package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.ModuleVariationHelper;
import com.doow.rubbish.generator.VariationManager;

public class ChartModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;
    private ModuleVariationHelper variationHelper;

    // 图表类型
    private static final String[] CHART_TYPES = {
        "line", "bar", "pie", "scatter", "bubble", "radar"
    };

    // 图表库
    private static final String[] CHART_LIBRARIES = {
        "mpandroidchart", "anychart", "achartengine", "custom"
    };

    public ChartModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        this.variationHelper = ModuleVariationHelper.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成图表模块");

        // 获取当前UI风格和异步处理方式
        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成图表模块
        generateChartModule(uiStyle, asyncHandler);
    }

    private void generateChartModule(String uiStyle, String asyncHandler) throws Exception {
        // 生成图表管理器
        generateChartManager(uiStyle, asyncHandler);

        // 生成图表工具类
        generateChartUtils(uiStyle, asyncHandler);

        // 生成图表数据处理器
        generateChartDataProcessor(uiStyle, asyncHandler);
    }

    private void generateChartManager(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = variationHelper.generateClassName("Chart");
        String instanceName = variationHelper.generateVariableName("Instance");
        String contextName = variationHelper.generateObjectName();
        String tagName = variationHelper.generateVariableName("Tag");
        String chartName = variationHelper.generateVariableName("Chart");
        String dataName = variationHelper.generateVariableName("Data");
        String maxDataPointsName = variationHelper.generateIntName();
        String lineWidthName = variationHelper.generateIntName();
        String circleRadiusName = variationHelper.generateIntName();
        String callbackName = variationHelper.generateClassName("Callback");
        String callbackVarName = variationHelper.generateVariableName("Callback");

        // 使用随机值
        int maxDataPoints = variationHelper.generateIntRange(10, 100)[0];
        int lineWidth = variationHelper.generateIntRange(1, 5)[0];
        float circleRadius = variationHelper.generateIntRange(2, 8)[0] / 10.0f;

        sb.append("package ").append(packageName).append(".chart;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.graphics.Color;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import com.github.mikephil.charting.charts.LineChart;\n");
        sb.append("import com.github.mikephil.charting.data.Entry;\n");
        sb.append("import com.github.mikephil.charting.data.LineData;\n");
        sb.append("import com.github.mikephil.charting.data.LineDataSet;\n");
        sb.append("import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;\n");
        sb.append("import java.util.ArrayList;\n");
        sb.append("import java.util.List;\n");

        // 根据异步处理方式添加导入
        if (asyncHandler.contains("coroutines")) {
            sb.append("import kotlinx.coroutines.CoroutineScope;\n");
            sb.append("import kotlinx.coroutines.Dispatchers;\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("import io.reactivex.rxjava3.core.Single;\n");
            sb.append("import io.reactivex.rxjava3.schedulers.Schedulers;\n");
        }

        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final int ").append(maxDataPointsName).append(" = ").append(maxDataPoints).append(";\n");
        sb.append("    private static final int ").append(lineWidthName).append(" = ").append(lineWidth).append(";\n");
        sb.append("    private static final float ").append(circleRadiusName).append(" = ").append(circleRadius).append("f;\n");
        sb.append("\n");

        // 回调接口
        sb.append("    public interface ").append(callbackName).append(" {\n");
        sb.append("        void onDataUpdated();\n");
        sb.append("        void onError(String error);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 成员变量
        sb.append("    private final ").append(contextName).append(" ").append(contextName).append(";\n");
        sb.append("    private final ").append(callbackName).append(" ").append(callbackVarName).append(";\n");
        sb.append("    private final ").append(chartName).append(" ").append(chartName).append(";\n");
        sb.append("    private final ").append(dataName).append(" ").append(dataName).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    public ").append(className).append("(").append(contextName).append(" ").append(contextName)
          .append(", ").append(chartName).append(" ").append(chartName)
          .append(", ").append(callbackName).append(" ").append(callbackVarName).append(") {\n");
        sb.append("        this.").append(contextName).append(" = ").append(contextName).append(".getApplicationContext();\n");
        sb.append("        this.").append(chartName).append(" = ").append(chartName).append(";\n");
        sb.append("        this.").append(callbackVarName).append(" = ").append(callbackVarName).append(";\n");
        sb.append("        this.").append(dataName).append(" = new ").append(dataName).append("();\n");
        sb.append("        initChart();\n");
        sb.append("    }\n");
        sb.append("\n");

        // 初始化图表方法
        generateInitChartMethod(sb, className, chartName, tagName);

        // 设置数据方法
        generateSetDataMethod(sb, className, dataName, chartName, callbackVarName, tagName, lineWidthName, circleRadiusName);

        // 更新数据方法
        generateUpdateDataMethod(sb, className, dataName, chartName, callbackVarName, tagName);

        // 清空数据方法
        generateClearDataMethod(sb, className, dataName, chartName, callbackVarName, tagName);

        // 添加数据点方法
        generateAddDataPointMethod(sb, className, dataName, chartName, callbackVarName, tagName, maxDataPointsName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "chart");
    }

    private void generateInitChartMethod(StringBuilder sb, String className, String chartName, String tagName) {
        String methodName = variationHelper.generateMethodName("InitChart");
        String logMessage = variationHelper.generateLogMessage("Chart initialized");

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        ").append(chartName).append(".setDescription(\"\");\n");
        sb.append("        ").append(chartName).append(".setNoDataText(\"No data available\");\n");
        sb.append("        ").append(chartName).append(".setTouchEnabled(true);\n");
        sb.append("        ").append(chartName).append(".setDragEnabled(true);\n");
        sb.append("        ").append(chartName).append(".setScaleEnabled(true);\n");
        sb.append("        ").append(chartName).append(".setPinchZoom(true);\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateSetDataMethod(StringBuilder sb, String className, String dataName,
                                       String chartName, String callbackVarName, String tagName,
                                       String lineWidthName, String circleRadiusName) {
        String methodName = variationHelper.generateMethodName("SetData");
        String entriesName = variationHelper.generateCollectionName();
        String logMessage = variationHelper.generateLogMessage("Data set");
        String logErrorMessage = variationHelper.generateLogMessage("Error setting data");

        sb.append("    public void ").append(methodName).append("(List<Entry> ").append(entriesName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            LineDataSet dataSet = new LineDataSet(").append(entriesName).append(", \"Data\");\n");
        sb.append("            dataSet.setColor(Color.BLUE);\n");
        sb.append("            dataSet.setCircleColor(Color.BLUE);\n");
        sb.append("            dataSet.setLineWidth(").append(lineWidthName).append(");\n");
        sb.append("            dataSet.setCircleRadius(").append(circleRadiusName).append(");\n");
        sb.append("            dataSet.setDrawValues(true);\n");
        sb.append("            dataSet.setDrawCircles(true);\n");
        sb.append("\n");
        sb.append("            List<ILineDataSet> dataSets = new ArrayList<>();\n");
        sb.append("            dataSets.add(dataSet);\n");
        sb.append("\n");
        sb.append("            ").append(dataName).append(" = new LineData(dataSets);\n");
        sb.append("            ").append(chartName).append(".setData(").append(dataName).append(");\n");
        sb.append("            ").append(chartName).append(".invalidate();\n");
        sb.append("\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("            if (").append(callbackVarName).append(" != null) {\n");
        sb.append("                ").append(callbackVarName).append(".onDataUpdated();\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"").append(logErrorMessage).append("\", e);\n");
        sb.append("            if (").append(callbackVarName).append(" != null) {\n");
        sb.append("                ").append(callbackVarName).append(".onError(e.getMessage());\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateUpdateDataMethod(StringBuilder sb, String className, String dataName,
                                          String chartName, String callbackVarName, String tagName) {
        String methodName = variationHelper.generateMethodName("UpdateData");
        String logMessage = variationHelper.generateLogMessage("Data updated");
        String logErrorMessage = variationHelper.generateLogMessage("Error updating data");

        sb.append("    public void ").append(methodName).append("(List<Entry> entries) {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(dataName).append(" != null && ").append(dataName).append(".getDataSetCount() > 0) {\n");
        sb.append("                ILineDataSet dataSet = ").append(dataName).append(".getDataSetByIndex(0);\n");
        sb.append("                dataSet.getValues().clear();\n");
        sb.append("                dataSet.getValues().addAll(entries);\n");
        sb.append("                ").append(dataName).append(".notifyDataChanged();\n");
        sb.append("                ").append(chartName).append(".notifyDataSetChanged();\n");
        sb.append("                ").append(chartName).append(".invalidate();\n");
        sb.append("\n");
        sb.append("                Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("                if (").append(callbackVarName).append(" != null) {\n");
        sb.append("                    ").append(callbackVarName).append(".onDataUpdated();\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"").append(logErrorMessage).append("\", e);\n");
        sb.append("            if (").append(callbackVarName).append(" != null) {\n");
        sb.append("                ").append(callbackVarName).append(".onError(e.getMessage());\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateClearDataMethod(StringBuilder sb, String className, String dataName,
                                         String chartName, String callbackVarName, String tagName) {
        String methodName = variationHelper.generateMethodName("ClearData");
        String logMessage = variationHelper.generateLogMessage("Data cleared");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        if (").append(dataName).append(" != null) {\n");
        sb.append("            ").append(dataName).append(".clearValues();\n");
        sb.append("            ").append(chartName).append(".clear();\n");
        sb.append("            ").append(chartName).append(".invalidate();\n");
        sb.append("\n");
        sb.append("            Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("            if (").append(callbackVarName).append(" != null) {\n");
        sb.append("                ").append(callbackVarName).append(".onDataUpdated();\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateAddDataPointMethod(StringBuilder sb, String className, String dataName,
                                             String chartName, String callbackVarName, String tagName,
                                             String maxDataPointsName) {
        String methodName = variationHelper.generateMethodName("AddDataPoint");
        String xName = variationHelper.generateStringName();
        String yName = variationHelper.generateStringName();
        String logMessage = variationHelper.generateLogMessage("Data point added");
        String logErrorMessage = variationHelper.generateLogMessage("Error adding data point");

        sb.append("    public void ").append(methodName).append("(float ").append(xName).append(", float ").append(yName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(dataName).append(" != null && ").append(dataName).append(".getDataSetCount() > 0) {\n");
        sb.append("                ILineDataSet dataSet = ").append(dataName).append(".getDataSetByIndex(0);\n");
        sb.append("                if (dataSet.getEntryCount() < ").append(maxDataPointsName).append(") {\n");
        sb.append("                    dataSet.addEntry(new Entry(").append(xName).append(", ").append(yName).append("));\n");
        sb.append("                    ").append(dataName).append(".notifyDataChanged();\n");
        sb.append("                    ").append(chartName).append(".notifyDataSetChanged();\n");
        sb.append("                    ").append(chartName).append(".invalidate();\n");
        sb.append("\n");
        sb.append("                    Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("                    if (").append(callbackVarName).append(" != null) {\n");
        sb.append("                        ").append(callbackVarName).append(".onDataUpdated();\n");
        sb.append("                    }\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"").append(logErrorMessage).append("\", e);\n");
        sb.append("            if (").append(callbackVarName).append(" != null) {\n");
        sb.append("                ").append(callbackVarName).append(".onError(e.getMessage());\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateChartUtils(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名
        String className = variationHelper.generateClassName("Utils");
        String tagName = variationHelper.generateVariableName("Tag");
        String colorName = variationHelper.generateIntName();

        sb.append("package ").append(packageName).append(".chart;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.graphics.Color;\n");
        sb.append("import android.util.Log;\n");

        // 根据异步处理方式添加导入
        if (asyncHandler.contains("coroutines")) {
            sb.append("import kotlinx.coroutines.CoroutineScope;\n");
            sb.append("import kotlinx.coroutines.Dispatchers;\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("import io.reactivex.rxjava3.core.Single;\n");
            sb.append("import io.reactivex.rxjava3.schedulers.Schedulers;\n");
        }

        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final int[] ").append(colorName).append(" = {\n");
        sb.append("        Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,\n");
        sb.append("        Color.CYAN, Color.MAGENTA, Color.BLACK, Color.GRAY\n");
        sb.append("    };\n");
        sb.append("\n");

        // 私有构造函数
        sb.append("    private ").append(className).append("() {\n");
        sb.append("        // 私有构造函数，防止实例化\n");
        sb.append("    }\n");
        sb.append("\n");

        // 生成随机颜色方法
        generateRandomColorMethod(sb, className, colorName, tagName);

        // 生成颜色数组方法
        generateColorArrayMethod(sb, className, colorName, tagName);

        // 生成颜色转换方法
        generateColorConvertMethod(sb, className, tagName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "chart");
    }

    private void generateRandomColorMethod(StringBuilder sb, String className, String colorName, String tagName) {
        String methodName = variationHelper.generateMethodName("RandomColor");
        String logMessage = variationHelper.generateLogMessage("Generated random color");
        String indexName = variationHelper.generateIntName();
        String colorVarName = variationHelper.generateIntName();

        sb.append("    public static int ").append(methodName).append("() {\n");
        sb.append("        int ").append(indexName).append(" = (int) (Math.random() * ").append(colorName).append(".length);\n");
        sb.append("        int ").append(colorVarName).append(" = ").append(colorName).append("[").append(indexName).append("];\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + ").append(colorVarName).append(";\n");
        sb.append("        return ").append(colorVarName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateColorArrayMethod(StringBuilder sb, String className, String colorName, String tagName) {
        String methodName = variationHelper.generateMethodName("ColorArray");
        String logMessage = variationHelper.generateLogMessage("Generated color array");
        String countName = variationHelper.generateIntName();
        String colorsName = variationHelper.generateCollectionName();
        String iName = variationHelper.generateIntName();

        sb.append("    public static int[] ").append(methodName).append("(int ").append(countName).append(") {\n");
        sb.append("        int[] ").append(colorsName).append(" = new int[").append(countName).append("];\n");
        sb.append("        for (int ").append(iName).append(" = 0; ").append(iName).append(" < ").append(countName).append("; ").append(iName).append("++) {\n");
        sb.append("            ").append(colorsName).append("[").append(iName).append("] = ").append(colorName).append("[").append(iName).append(" % ").append(colorName).append(".length];\n");
        sb.append("        }\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + ").append(countName).append(" + \" colors\");\n");
        sb.append("        return ").append(colorsName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateColorConvertMethod(StringBuilder sb, String className, String tagName) {
        String methodName = variationHelper.generateMethodName("ColorConvert");
        String logMessage = variationHelper.generateLogMessage("Converted color");
        String hexName = variationHelper.generateStringName();
        String colorName = variationHelper.generateIntName();

        sb.append("    public static int ").append(methodName).append("(String ").append(hexName).append(") {\n");
        sb.append("        int ").append(colorName).append(" = Color.parseColor(").append(hexName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append(": \" + ").append(hexName).append(" + \" -> \" + ").append(colorName).append(";\n");
        sb.append("        return ").append(colorName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateChartDataProcessor(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名
        String className = variationHelper.generateClassName("DataProcessor");
        String tagName = variationHelper.generateVariableName("Tag");
        String dataName = variationHelper.generateCollectionName();
        String resultName = variationHelper.generateCollectionName();

        sb.append("package ").append(packageName).append(".chart;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import com.github.mikephil.charting.data.Entry;\n");
        sb.append("import java.util.ArrayList;\n");
        sb.append("import java.util.List;\n");

        // 根据异步处理方式添加导入
        if (asyncHandler.contains("coroutines")) {
            sb.append("import kotlinx.coroutines.CoroutineScope;\n");
            sb.append("import kotlinx.coroutines.Dispatchers;\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("import io.reactivex.rxjava3.core.Single;\n");
            sb.append("import io.reactivex.rxjava3.schedulers.Schedulers;\n");
        }

        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("\n");

        // 私有构造函数
        sb.append("    private ").append(className).append("() {\n");
        sb.append("        // 私有构造函数，防止实例化\n");
        sb.append("    }\n");
        sb.append("\n");

        // 生成平滑数据方法
        generateSmoothDataMethod(sb, className, dataName, resultName, tagName);

        // 生成过滤数据方法
        generateFilterDataMethod(sb, className, dataName, resultName, tagName);

        // 生成排序数据方法
        generateSortDataMethod(sb, className, dataName, resultName, tagName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "chart");
    }

    private void generateSmoothDataMethod(StringBuilder sb, String className, String dataName,
                                           String resultName, String tagName) {
        String methodName = variationHelper.generateMethodName("SmoothData");
        String logMessage = variationHelper.generateLogMessage("Smoothed data");
        String iName = variationHelper.generateIntName();
        String sizeName = variationHelper.generateIntName();

        sb.append("    public static List<Entry> ").append(methodName).append("(List<Entry> ").append(dataName).append(") {\n");
        sb.append("        List<Entry> ").append(resultName).append(" = new ArrayList<>();\n");
        sb.append("        if (").append(dataName).append(" == null || ").append(dataName).append(".isEmpty()) {\n");
        sb.append("            return ").append(resultName).append(";\n");
        sb.append("        }\n");
        sb.append("        int ").append(sizeName).append(" = ").append(dataName).append(".size();\n");
        sb.append("        for (int ").append(iName).append(" = 0; ").append(iName).append(" < ").append(sizeName).append("; ").append(iName).append("++) {\n");
        sb.append("            Entry entry = ").append(dataName).append(".get(").append(iName).append(");\n");
        sb.append("            ").append(resultName).append(".add(new Entry(entry.getX(), entry.getY()));\n");
        sb.append("        }\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        return ").append(resultName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateFilterDataMethod(StringBuilder sb, String className, String dataName,
                                         String resultName, String tagName) {
        String methodName = variationHelper.generateMethodName("FilterData");
        String logMessage = variationHelper.generateLogMessage("Filtered data");
        String iName = variationHelper.generateIntName();
        String sizeName = variationHelper.generateIntName();
        String entryName = variationHelper.generateObjectName();

        sb.append("    public static List<Entry> ").append(methodName).append("(List<Entry> ").append(dataName).append(", float threshold) {\n");
        sb.append("        List<Entry> ").append(resultName).append(" = new ArrayList<>();\n");
        sb.append("        if (").append(dataName).append(" == null || ").append(dataName).append(".isEmpty()) {\n");
        sb.append("            return ").append(resultName).append(";\n");
        sb.append("        }\n");
        sb.append("        int ").append(sizeName).append(" = ").append(dataName).append(".size();\n");
        sb.append("        for (int ").append(iName).append(" = 0; ").append(iName).append(" < ").append(sizeName).append("; ").append(iName).append("++) {\n");
        sb.append("            Entry ").append(entryName).append(" = ").append(dataName).append(".get(").append(iName).append(");\n");
        sb.append("            if (").append(entryName).append(".getY() >= threshold) {\n");
        sb.append("                ").append(resultName).append(".add(").append(entryName).append(");\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        return ").append(resultName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateSortDataMethod(StringBuilder sb, String className, String dataName,
                                        String resultName, String tagName) {
        String methodName = variationHelper.generateMethodName("SortData");
        String logMessage = variationHelper.generateLogMessage("Sorted data");

        sb.append("    public static List<Entry> ").append(methodName).append("(List<Entry> ").append(dataName).append(") {\n");
        sb.append("        List<Entry> ").append(resultName).append(" = new ArrayList<>(").append(dataName).append(");\n");
        sb.append("        if (").append(dataName).append(" == null || ").append(dataName).append(".isEmpty()) {\n");
        sb.append("            return ").append(resultName).append(";\n");
        sb.append("        }\n");
        sb.append("        Collections.sort(").append(resultName).append(", (e1, e2) -> Float.compare(e1.getX(), e2.getX()));\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        return ").append(resultName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");
    }
}