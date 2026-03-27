package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.EnhancedRandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class GraphicsModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;

    // 图形类型
    private static final String[] GRAPHICS_TYPES = {
        "2d", "3d", "vector", "bitmap", "svg"
    };

    // 绘制方式
    private static final String[] DRAW_TYPES = {
        "canvas", "opengl", "vulkan", "metal", "custom"
    };

    // 颜色值
    private static final int[] COLOR_VALUES = {
        0xFF000000, 0xFFFFFFFF, 0xFFFF0000, 0xFF00FF00,
        0xFF0000FF, 0xFFFFFF00, 0xFFFF00FF, 0xFF00FFFF
    };

    // 画笔样式
    private static final String[] PAINT_STYLES = {
        "FILL", "STROKE", "FILL_AND_STROKE"
    };

    // 线条端点样式
    private static final String[] CAP_STYLES = {
        "BUTT", "ROUND", "SQUARE"
    };

    // 线条连接样式
    private static final String[] JOIN_STYLES = {
        "MITER", "ROUND", "BEVEL"
    };

    // 滤镜类型
    private static final String[] FILTER_TYPES = {
        "brightness", "contrast", "saturation", "grayscale", "hue_rotation"
    };

    public GraphicsModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成图形模块");

        // 获取当前UI风格和异步处理方式
        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成图形模块
        generateGraphicsModule(uiStyle, asyncHandler);
    }

    private void generateGraphicsModule(String uiStyle, String asyncHandler) throws Exception {
        // 生成图形管理器
        generateGraphicsManager(uiStyle, asyncHandler);

        // 生成图形工具类
        generateGraphicsUtils(uiStyle, asyncHandler);

        // 生成图形处理器
        generateGraphicsProcessor(uiStyle, asyncHandler);
    }

    private void generateGraphicsManager(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Manager");
        String instanceName = EnhancedRandomUtils.generateVariableName("Instance");
        String contextName = EnhancedRandomUtils.generateObjectName();
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String callbackName = EnhancedRandomUtils.generateClassName("Callback");
        String callbackVarName = EnhancedRandomUtils.generateVariableName("Callback");
        String bitmapName = EnhancedRandomUtils.generateVariableName("Bitmap");
        String canvasName = EnhancedRandomUtils.generateVariableName("Canvas");
        String paintName = EnhancedRandomUtils.generateVariableName("Paint");
        String pathName = EnhancedRandomUtils.generateVariableName("Path");

        // 使用随机值
        int defaultBitmapWidth = EnhancedRandomUtils.generateIntRange(512, 2048)[0];
        int defaultBitmapHeight = EnhancedRandomUtils.generateIntRange(512, 2048)[0];
        int defaultColor = COLOR_VALUES[EnhancedRandomUtils.between(0, COLOR_VALUES.length - 1)];
        String paintStyle = PAINT_STYLES[EnhancedRandomUtils.between(0, PAINT_STYLES.length - 1)];
        String capStyle = CAP_STYLES[EnhancedRandomUtils.between(0, CAP_STYLES.length - 1)];
        String joinStyle = JOIN_STYLES[EnhancedRandomUtils.between(0, JOIN_STYLES.length - 1)];

        sb.append("package ").append(packageName).append(".graphics;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.graphics.Bitmap;\n");
        sb.append("import android.graphics.Canvas;\n");
        sb.append("import android.graphics.Color;\n");
        sb.append("import android.graphics.Paint;\n");
        sb.append("import android.graphics.Path;\n");
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
        sb.append("    private static final int DEFAULT_BITMAP_WIDTH = ").append(defaultBitmapWidth).append(";\n");
        sb.append("    private static final int DEFAULT_BITMAP_HEIGHT = ").append(defaultBitmapHeight).append(";\n");
        sb.append("    private static final int DEFAULT_COLOR = ").append(defaultColor).append(";\n");
        sb.append("\n");

        // 回调接口
        sb.append("    public interface ").append(callbackName).append(" {\n");
        sb.append("        void onDrawCompleted(Bitmap ").append(bitmapName).append(");\n");
        sb.append("        void onError(String error);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 成员变量
        sb.append("    private final Context ").append(contextName).append(";\n");
        sb.append("    private final ").append(callbackName).append(" ").append(callbackVarName).append(";\n");
        sb.append("    private Bitmap ").append(bitmapName).append(";\n");
        sb.append("    private Canvas ").append(canvasName).append(";\n");
        sb.append("    private Paint ").append(paintName).append(";\n");
        sb.append("    private Path ").append(pathName).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    public ").append(className).append("(Context ").append(contextName).append(", ").append(callbackName).append(" ").append(callbackVarName).append(") {\n");
        sb.append("        this.").append(contextName).append(" = ").append(contextName).append(".getApplicationContext();\n");
        sb.append("        this.").append(callbackVarName).append(" = ").append(callbackVarName).append(";\n");
        sb.append("\n");
        sb.append("        // 初始化图形对象\n");
        sb.append("        initGraphics();\n");
        sb.append("    }\n");
        sb.append("\n");

        // 初始化图形方法
        generateInitGraphicsMethod(sb, className, bitmapName, canvasName, paintName, pathName, tagName, paintStyle, capStyle, joinStyle);

        // 绘制矩形方法
        generateDrawRectMethod(sb, className, canvasName, paintName, tagName);

        // 绘制圆形方法
        generateDrawCircleMethod(sb, className, canvasName, paintName, tagName);

        // 绘制线条方法
        generateDrawLineMethod(sb, className, canvasName, paintName, tagName);

        // 绘制路径方法
        generateDrawPathMethod(sb, className, canvasName, paintName, pathName, tagName);

        // 绘制文本方法
        generateDrawTextMethod(sb, className, canvasName, paintName, tagName);

        // 清空画布方法
        generateClearCanvasMethod(sb, className, canvasName, paintName, tagName);

        // 获取Bitmap方法
        generateGetBitmapMethod(sb, className, bitmapName);

        // 通知绘制完成方法
        generateNotifyDrawCompletedMethod(sb, className, callbackVarName, bitmapName);

        // 释放资源方法
        generateReleaseMethod(sb, className, bitmapName, tagName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "graphics");
    }

    private void generateInitGraphicsMethod(StringBuilder sb, String className, String bitmapName,
                                          String canvasName, String paintName, String pathName,
                                          String tagName, String paintStyle, String capStyle, String joinStyle) {
        String methodName = EnhancedRandomUtils.generateMethodName("InitGraphics");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Graphics initialized");

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        // 创建Bitmap\n");
        sb.append("        ").append(bitmapName).append(" = Bitmap.createBitmap(DEFAULT_BITMAP_WIDTH, DEFAULT_BITMAP_HEIGHT,\n");
        sb.append("                Bitmap.Config.ARGB_8888);\n");
        sb.append("\n");
        sb.append("        // 创建Canvas\n");
        sb.append("        ").append(canvasName).append(" = new Canvas(").append(bitmapName).append(");\n");
        sb.append("\n");
        sb.append("        // 创建Paint\n");
        sb.append("        ").append(paintName).append(" = new Paint();\n");
        sb.append("        ").append(paintName).append(".setAntiAlias(true);\n");
        sb.append("        ").append(paintName).append(".setColor(DEFAULT_COLOR);\n");
        sb.append("        ").append(paintName).append(".setStyle(Paint.Style.").append(paintStyle).append(");\n");
        sb.append("        ").append(paintName).append(".setStrokeCap(Paint.Cap.").append(capStyle).append(");\n");
        sb.append("        ").append(paintName).append(".setStrokeJoin(Paint.Join.").append(joinStyle).append(");\n");
        sb.append("        ").append(paintName).append(".setStrokeWidth(2.0f);\n");
        sb.append("\n");
        sb.append("        // 创建Path\n");
        sb.append("        ").append(pathName).append(" = new Path();\n");
        sb.append("\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateDrawRectMethod(StringBuilder sb, String className, String canvasName, String paintName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("DrawRect");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Drew rectangle");

        sb.append("    public void ").append(methodName).append("(float left, float top, float right, float bottom, int color) {\n");
        sb.append("        ").append(paintName).append(".setColor(color);\n");
        sb.append("        ").append(canvasName).append(".drawRect(left, top, right, bottom, ").append(paintName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", String.format(\"Drew rect: (%.2f,%.2f)-(%.2f,%.2f)\",\n");
        sb.append("                left, top, right, bottom));\n");
        sb.append("        notifyDrawCompleted();\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateDrawCircleMethod(StringBuilder sb, String className, String canvasName, String paintName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("DrawCircle");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Drew circle");

        sb.append("    public void ").append(methodName).append("(float cx, float cy, float radius, int color) {\n");
        sb.append("        ").append(paintName).append(".setColor(color);\n");
        sb.append("        ").append(canvasName).append(".drawCircle(cx, cy, radius, ").append(paintName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", String.format(\"Drew circle: center=(%.2f,%.2f), radius=%.2f\",\n");
        sb.append("                cx, cy, radius));\n");
        sb.append("        notifyDrawCompleted();\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateDrawLineMethod(StringBuilder sb, String className, String canvasName, String paintName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("DrawLine");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Drew line");

        sb.append("    public void ").append(methodName).append("(float startX, float startY, float endX, float endY, int color) {\n");
        sb.append("        ").append(paintName).append(".setColor(color);\n");
        sb.append("        ").append(paintName).append(".setStyle(Paint.Style.STROKE);\n");
        sb.append("        ").append(canvasName).append(".drawLine(startX, startY, endX, endY, ").append(paintName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", String.format(\"Drew line: (%.2f,%.2f)-(%.2f,%.2f)\",\n");
        sb.append("                startX, startY, endX, endY));\n");
        sb.append("        notifyDrawCompleted();\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateDrawPathMethod(StringBuilder sb, String className, String canvasName, String paintName, String pathName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("DrawPath");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Drew path");

        sb.append("    public void ").append(methodName).append("(Path path, int color) {\n");
        sb.append("        ").append(paintName).append(".setColor(color);\n");
        sb.append("        ").append(paintName).append(".setStyle(Paint.Style.STROKE);\n");
        sb.append("        ").append(canvasName).append(".drawPath(path, ").append(paintName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        notifyDrawCompleted();\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateDrawTextMethod(StringBuilder sb, String className, String canvasName, String paintName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("DrawText");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Drew text");
        String textSizeName = EnhancedRandomUtils.generateVariableName("TextSize");

        sb.append("    public void ").append(methodName).append("(String text, float x, float y, int color, float ").append(textSizeName).append(") {\n");
        sb.append("        ").append(paintName).append(".setColor(color);\n");
        sb.append("        ").append(paintName).append(".setStyle(Paint.Style.FILL);\n");
        sb.append("        ").append(paintName).append(".setTextSize(").append(textSizeName).append(");\n");
        sb.append("        ").append(canvasName).append(".drawText(text, x, y, ").append(paintName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", String.format(\"Drew text: %s at (%.2f,%.2f)\", text, x, y));\n");
        sb.append("        notifyDrawCompleted();\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateClearCanvasMethod(StringBuilder sb, String className, String canvasName, String paintName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ClearCanvas");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Cleared canvas");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        ").append(canvasName).append(".drawColor(DEFAULT_COLOR);\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("        notifyDrawCompleted();\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetBitmapMethod(StringBuilder sb, String className, String bitmapName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetBitmap");

        sb.append("    public Bitmap ").append(methodName).append("() {\n");
        sb.append("        return ").append(bitmapName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateNotifyDrawCompletedMethod(StringBuilder sb, String className, String callbackVarName, String bitmapName) {
        String methodName = EnhancedRandomUtils.generateMethodName("NotifyDrawCompleted");

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        if (").append(callbackVarName).append(" != null) {\n");
        sb.append("            ").append(callbackVarName).append(".onDrawCompleted(").append(bitmapName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateReleaseMethod(StringBuilder sb, String className, String bitmapName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Release");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Released graphics resources");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        if (").append(bitmapName).append(" != null && !").append(bitmapName).append(".isRecycled()) {\n");
        sb.append("            ").append(bitmapName).append(".recycle();\n");
        sb.append("        }\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGraphicsUtils(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Utils");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String colorName = EnhancedRandomUtils.generateIntName();

        // 使用随机值
        int defaultColor = COLOR_VALUES[EnhancedRandomUtils.between(0, COLOR_VALUES.length - 1)];

        sb.append("package ").append(packageName).append(".graphics;\n");
        sb.append("\n");

        // 导入
        sb.append("import android.graphics.Bitmap;\n");
        sb.append("import android.graphics.Canvas;\n");
        sb.append("import android.graphics.Color;\n");
        sb.append("import android.graphics.Paint;\n");
        sb.append("import android.graphics.Rect;\n");
        sb.append("import android.graphics.RectF;\n");
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
        sb.append("    private static final int DEFAULT_COLOR = ").append(defaultColor).append(";\n");
        sb.append("\n");

        // 私有构造函数
        sb.append("    private ").append(className).append("() {\n");
        sb.append("        // 私有构造函数，防止实例化\n");
        sb.append("    }\n");
        sb.append("\n");

        // 创建Bitmap方法
        generateCreateBitmapMethod(sb, className, tagName);

        // 调整Bitmap大小方法
        generateResizeBitmapMethod(sb, className, tagName);

        // 裁剪Bitmap方法
        generateCropBitmapMethod(sb, className, tagName);

        // 旋转Bitmap方法
        generateRotateBitmapMethod(sb, className, tagName);

        // 翻转Bitmap方法
        generateFlipBitmapMethod(sb, className, tagName);

        // 获取颜色方法
        generateGetPixelColorMethod(sb, className, tagName, defaultColor);

        // 计算矩形中心方法
        generateGetRectCenterMethod(sb, className, tagName);

        // 计算矩形中心方法（RectF）
        generateGetRectCenterFMethod(sb, className, tagName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "graphics");
    }

    private void generateCreateBitmapMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("CreateBitmap");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Created bitmap");

        sb.append("    public static Bitmap ").append(methodName).append("(int width, int height) {\n");
        sb.append("        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateResizeBitmapMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ResizeBitmap");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Resized bitmap");

        sb.append("    public static Bitmap ").append(methodName).append("(Bitmap bitmap, int newWidth, int newHeight) {\n");
        sb.append("        if (bitmap == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateCropBitmapMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("CropBitmap");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Cropped bitmap");

        sb.append("    public static Bitmap ").append(methodName).append("(Bitmap bitmap, int x, int y, int width, int height) {\n");
        sb.append("        if (bitmap == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return Bitmap.createBitmap(bitmap, x, y, width, height);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateRotateBitmapMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("RotateBitmap");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Rotated bitmap");

        sb.append("    public static Bitmap ").append(methodName).append("(Bitmap bitmap, float degrees) {\n");
        sb.append("        if (bitmap == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        Matrix matrix = new Matrix();\n");
        sb.append("        matrix.postRotate(degrees, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);\n");
        sb.append("        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateFlipBitmapMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("FlipBitmap");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Flipped bitmap");

        sb.append("    public static Bitmap ").append(methodName).append("(Bitmap bitmap, boolean horizontal, boolean vertical) {\n");
        sb.append("        if (bitmap == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        Matrix matrix = new Matrix();\n");
        sb.append("        if (horizontal) {\n");
        sb.append("            matrix.preScale(-1, 1);\n");
        sb.append("        }\n");
        sb.append("        if (vertical) {\n");
        sb.append("            matrix.preScale(1, -1);\n");
        sb.append("        }\n");
        sb.append("        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetPixelColorMethod(StringBuilder sb, String className, String tagName, int defaultColor) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetPixelColor");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Got pixel color");

        sb.append("    public static int ").append(methodName).append("(Bitmap bitmap, int x, int y) {\n");
        sb.append("        if (bitmap == null || x < 0 || y < 0 || x >= bitmap.getWidth() || y >= bitmap.getHeight()) {\n");
        sb.append("            return ").append(defaultColor).append(";\n");
        sb.append("        }\n");
        sb.append("        return bitmap.getPixel(x, y);\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetRectCenterMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetRectCenter");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Got rect center");

        sb.append("    public static float[] ").append(methodName).append("(Rect rect) {\n");
        sb.append("        if (rect == null) {\n");
        sb.append("            return new float[]{0f, 0f};\n");
        sb.append("        }\n");
        sb.append("        return new float[]{rect.centerX(), rect.centerY()};\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGetRectCenterFMethod(StringBuilder sb, String className, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("GetRectCenterF");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Got rect center");

        sb.append("    public static float[] ").append(methodName).append("(RectF rect) {\n");
        sb.append("        if (rect == null) {\n");
        sb.append("            return new float[]{0f, 0f};\n");
        sb.append("        }\n");
        sb.append("        return new float[]{rect.centerX(), rect.centerY()};\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateGraphicsProcessor(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Processor");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String sourceBitmapName = EnhancedRandomUtils.generateVariableName("SourceBitmap");
        String processedBitmapName = EnhancedRandomUtils.generateVariableName("ProcessedBitmap");
        String colorMatrixName = EnhancedRandomUtils.generateVariableName("ColorMatrix");

        // 使用随机值
        String filterType = FILTER_TYPES[EnhancedRandomUtils.between(0, FILTER_TYPES.length - 1)];

        sb.append("package ").append(packageName).append(".graphics;\n");
        sb.append("\n");

        // 导入
        sb.append("import android.graphics.Bitmap;\n");
        sb.append("import android.graphics.Canvas;\n");
        sb.append("import android.graphics.Color;\n");
        sb.append("import android.graphics.ColorMatrix;\n");
        sb.append("import android.graphics.ColorMatrixColorFilter;\n");
        sb.append("import android.graphics.Paint;\n");
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
        sb.append("\n");

        // 成员变量
        sb.append("    private Bitmap ").append(sourceBitmapName).append(";\n");
        sb.append("    private Bitmap ").append(processedBitmapName).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    public ").append(className).append("(Bitmap ").append(sourceBitmapName).append(") {\n");
        sb.append("        this.").append(sourceBitmapName).append(" = ").append(sourceBitmapName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        // 应用亮度调整方法
        generateApplyBrightnessMethod(sb, className, sourceBitmapName, processedBitmapName, colorMatrixName, tagName);

        // 应用对比度调整方法
        generateApplyContrastMethod(sb, className, sourceBitmapName, processedBitmapName, colorMatrixName, tagName);

        // 应用饱和度调整方法
        generateApplySaturationMethod(sb, className, sourceBitmapName, processedBitmapName, colorMatrixName, tagName);

        // 应用灰度滤镜方法
        generateApplyGrayscaleMethod(sb, className, sourceBitmapName, processedBitmapName, colorMatrixName, tagName);

        // 应用色相旋转方法
        generateApplyHueRotationMethod(sb, className, sourceBitmapName, processedBitmapName, colorMatrixName, tagName);

        // 应用颜色矩阵方法
        generateApplyColorMatrixMethod(sb, className, sourceBitmapName, processedBitmapName, colorMatrixName, tagName);

        // 释放资源方法
        generateReleaseProcessorMethod(sb, className, processedBitmapName, tagName);

        sb.append("}\n");

        // 保存文件
        generateJavaFile(className, sb.toString(), "graphics");
    }

    private void generateApplyBrightnessMethod(StringBuilder sb, String className, String sourceBitmapName,
                                             String processedBitmapName, String colorMatrixName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ApplyBrightness");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Applied brightness");
        String brightnessName = EnhancedRandomUtils.generateVariableName("Brightness");

        sb.append("    public Bitmap ").append(methodName).append("(float ").append(brightnessName).append(") {\n");
        sb.append("        if (").append(sourceBitmapName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        ColorMatrix ").append(colorMatrixName).append(" = new ColorMatrix();\n");
        sb.append("        ").append(colorMatrixName).append(".setScale(").append(brightnessName).append(", ").append(brightnessName).append(", 1);\n");
        sb.append("\n");
        sb.append("        return applyColorMatrix(").append(colorMatrixName).append(");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateApplyContrastMethod(StringBuilder sb, String className, String sourceBitmapName,
                                            String processedBitmapName, String colorMatrixName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ApplyContrast");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Applied contrast");
        String contrastName = EnhancedRandomUtils.generateVariableName("Contrast");

        sb.append("    public Bitmap ").append(methodName).append("(float ").append(contrastName).append(") {\n");
        sb.append("        if (").append(sourceBitmapName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        ColorMatrix ").append(colorMatrixName).append(" = new ColorMatrix();\n");
        sb.append("        float scale = ").append(contrastName).append(" + 1.f;\n");
        sb.append("        float translate = (-.5f * scale + .5f) * 255.f;\n");
        sb.append("        ").append(colorMatrixName).append(".set(new float[]{\n");
        sb.append("                scale, 0, 0, 0, translate,\n");
        sb.append("                0, scale, 0, 0, translate,\n");
        sb.append("                0, 0, scale, 0, translate,\n");
        sb.append("                0, 0, 0, 1, 0\n");
        sb.append("        });\n");
        sb.append("\n");
        sb.append("        return applyColorMatrix(").append(colorMatrixName).append(");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateApplySaturationMethod(StringBuilder sb, String className, String sourceBitmapName,
                                              String processedBitmapName, String colorMatrixName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ApplySaturation");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Applied saturation");
        String saturationName = EnhancedRandomUtils.generateVariableName("Saturation");

        sb.append("    public Bitmap ").append(methodName).append("(float ").append(saturationName).append(") {\n");
        sb.append("        if (").append(sourceBitmapName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        ColorMatrix ").append(colorMatrixName).append(" = new ColorMatrix();\n");
        sb.append("        ").append(colorMatrixName).append(".setSaturation(").append(saturationName).append(");\n");
        sb.append("\n");
        sb.append("        return applyColorMatrix(").append(colorMatrixName).append(");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateApplyGrayscaleMethod(StringBuilder sb, String className, String sourceBitmapName,
                                              String processedBitmapName, String colorMatrixName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ApplyGrayscale");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Applied grayscale");

        sb.append("    public Bitmap ").append(methodName).append("() {\n");
        sb.append("        if (").append(sourceBitmapName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        ColorMatrix ").append(colorMatrixName).append(" = new ColorMatrix();\n");
        sb.append("        ").append(colorMatrixName).append(".setSaturation(0);\n");
        sb.append("\n");
        sb.append("        return applyColorMatrix(").append(colorMatrixName).append(");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateApplyHueRotationMethod(StringBuilder sb, String className, String sourceBitmapName,
                                              String processedBitmapName, String colorMatrixName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ApplyHueRotation");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Applied hue rotation");
        String degreesName = EnhancedRandomUtils.generateVariableName("Degrees");

        sb.append("    public Bitmap ").append(methodName).append("(float ").append(degreesName).append(") {\n");
        sb.append("        if (").append(sourceBitmapName).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        ColorMatrix ").append(colorMatrixName).append(" = new ColorMatrix();\n");
        sb.append("        ").append(colorMatrixName).append(".setRotate(0, ").append(degreesName).append(");\n");
        sb.append("\n");
        sb.append("        return applyColorMatrix(").append(colorMatrixName).append(");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateApplyColorMatrixMethod(StringBuilder sb, String className, String sourceBitmapName,
                                              String processedBitmapName, String colorMatrixName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("ApplyColorMatrix");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Applied color matrix");
        String logErrorMessage = EnhancedRandomUtils.generateLogMessage("Error applying color matrix");

        sb.append("    private Bitmap ").append(methodName).append("(ColorMatrix ").append(colorMatrixName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            Paint paint = new Paint();\n");
        sb.append("            paint.setColorFilter(new ColorMatrixColorFilter(").append(colorMatrixName).append("));\n");
        sb.append("\n");
        sb.append("            Bitmap result = Bitmap.createBitmap(").append(sourceBitmapName).append(".getWidth(),\n");
        sb.append("                    ").append(sourceBitmapName).append(".getHeight(),\n");
        sb.append("                    Bitmap.Config.ARGB_8888);\n");
        sb.append("            Canvas canvas = new Canvas(result);\n");
        sb.append("            canvas.drawBitmap(").append(sourceBitmapName).append(", 0, 0, paint);\n");
        sb.append("\n");
        sb.append("            return result;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagName).append(", \"").append(logErrorMessage).append("\", e);\n");
        sb.append("            return ").append(sourceBitmapName).append(";\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateReleaseProcessorMethod(StringBuilder sb, String className, String processedBitmapName, String tagName) {
        String methodName = EnhancedRandomUtils.generateMethodName("Release");
        String logMessage = EnhancedRandomUtils.generateLogMessage("Released graphics processor resources");

        sb.append("    public void ").append(methodName).append("() {\n");
        sb.append("        if (").append(processedBitmapName).append(" != null && !").append(processedBitmapName).append(".isRecycled()) {\n");
        sb.append("            ").append(processedBitmapName).append(".recycle();\n");
        sb.append("        }\n");
        sb.append("        Log.d(").append(tagName).append(", \"").append(logMessage).append("\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }
}
