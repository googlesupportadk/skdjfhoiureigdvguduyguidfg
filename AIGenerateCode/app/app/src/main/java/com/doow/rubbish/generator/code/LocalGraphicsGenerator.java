package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalGraphicsGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] GRAPHICS_TYPES = {
            "Canvas", "Bitmap", "Paint", "Path", "Matrix",
            "Color", "Rect", "RectF", "Point", "PointF",
            "Shader", "Gradient", "PorterDuff", "Xfermode", "Region"
    };

    private static final String[] OPERATION_TYPES = {
            "draw", "transform", "clip", "save", "restore",
            "rotate", "scale", "translate", "skew", "concat",
            "invert", "map_points", "map_rect", "map_vectors", "post_concat"
    };

    private static final String[] PAINT_STYLES = {
            "FILL", "STROKE", "FILL_AND_STROKE"
    };

    private static final String[] CAP_STYLES = {
            "BUTT", "ROUND", "SQUARE"
    };

    private static final String[] JOIN_STYLES = {
            "MITER", "ROUND", "BEVEL"
    };

    private static final String[] SHADER_TYPES = {
            "BitmapShader", "LinearGradient", "RadialGradient", "SweepGradient", "ComposeShader"
    };

    private static final String[] XFERMODE_TYPES = {
            "CLEAR", "SRC", "DST", "SRC_OVER", "DST_OVER",
            "SRC_IN", "DST_IN", "SRC_OUT", "DST_OUT", "SRC_ATOP",
            "DST_ATOP", "XOR", "DARKEN", "LIGHTEN", "MULTIPLY", "SCREEN"
    };

    public LocalGraphicsGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成图形类");

        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Graphics");
            generateGraphicsClass(className, uiStyle, asyncHandler);
        }
    }

    private void generateGraphicsClass(String className, String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明和导入
        sb.append(generatePackageDeclaration("graphics"));
        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.graphics.*"));
        sb.append(generateImportStatement("android.util.AttributeSet"));
        sb.append(generateImportStatement("android.view.View"));

        if (uiStyle.contains("material")) {
            sb.append(generateImportStatement("com.google.android.material.card.MaterialCardView"));
        }

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        } else if (asyncHandler.contains("rxjava")) {
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 生成类声明
        String graphicsType = GRAPHICS_TYPES[RandomUtils.between(0, GRAPHICS_TYPES.length - 1)];
        String operationType = OPERATION_TYPES[RandomUtils.between(0, OPERATION_TYPES.length - 1)];

        // 生成随机变量名
        String paintVarName = RandomUtils.generateWord(6);
        String pathVarName = RandomUtils.generateWord(6);
        String matrixVarName = RandomUtils.generateWord(6);
        String canvasVarName = RandomUtils.generateWord(6);
        String bitmapVarName = RandomUtils.generateWord(6);
        String shaderVarName = RandomUtils.generateWord(6);
        String xfermodeVarName = RandomUtils.generateWord(6);
        String colorVarName = RandomUtils.generateWord(6);
        String rectVarName = RandomUtils.generateWord(6);
        String rectFVarName = RandomUtils.generateWord(6);
        String pointVarName = RandomUtils.generateWord(6);
        String pointFVarName = RandomUtils.generateWord(6);
        String regionVarName = RandomUtils.generateWord(6);
        String contextVarName = RandomUtils.generateWord(6);
        String attrsVarName = RandomUtils.generateWord(6);
        String defStyleVarName = RandomUtils.generateWord(6);
        String initMethodName = RandomUtils.generateWord(6) + "Init";
        String drawMethodName = RandomUtils.generateWord(6) + "Draw";
        String transformMethodName = RandomUtils.generateWord(6) + "Transform";
        String clipMethodName = RandomUtils.generateWord(6) + "Clip";
        String saveMethodName = RandomUtils.generateWord(6) + "Save";
        String restoreMethodName = RandomUtils.generateWord(6) + "Restore";
        String setColorMethodName = RandomUtils.generateWord(6) + "SetColor";
        String setStyleMethodName = RandomUtils.generateWord(6) + "SetStyle";
        String setWidthMethodName = RandomUtils.generateWord(6) + "SetWidth";
        String logMessage = RandomUtils.generateWord(6) + "LogMessage";

        // 随机生成颜色值
        int colorValue = RandomUtils.between(0xFF000000, 0xFFFFFFFF);
        int strokeWidth = RandomUtils.between(1, 10);

        sb.append("public class ").append(className).append(" extends View {\n\n");

        // 生成常量
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String GRAPHICS_TYPE = \"").append(graphicsType).append("\";\n");
        sb.append("    private static final String OPERATION_TYPE = \"").append(operationType).append("\";\n");
        sb.append("    private static final int DEFAULT_COLOR = 0x").append(Integer.toHexString(colorValue)).append(";\n");
        sb.append("    private static final float DEFAULT_STROKE_WIDTH = ").append(strokeWidth).append("f;\n\n");

        // 生成成员变量
        sb.append("    private Paint ").append(paintVarName).append(";\n");
        sb.append("    private Path ").append(pathVarName).append(";\n");
        sb.append("    private Matrix ").append(matrixVarName).append(";\n");
        sb.append("    private Canvas ").append(canvasVarName).append(";\n");
        sb.append("    private Bitmap ").append(bitmapVarName).append(";\n");

        // 随机添加额外成员变量
        if (RandomUtils.randomBoolean()) {
            sb.append("    private Shader ").append(shaderVarName).append(";\n");
        }
        if (RandomUtils.randomBoolean()) {
            sb.append("    private PorterDuffXfermode ").append(xfermodeVarName).append(";\n");
        }
        if (RandomUtils.randomBoolean()) {
            sb.append("    private int ").append(colorVarName).append(";\n");
        }
        if (RandomUtils.randomBoolean()) {
            sb.append("    private Rect ").append(rectVarName).append(";\n");
        }
        if (RandomUtils.randomBoolean()) {
            sb.append("    private RectF ").append(rectFVarName).append(";\n");
        }
        if (RandomUtils.randomBoolean()) {
            sb.append("    private Point ").append(pointVarName).append(";\n");
        }
        if (RandomUtils.randomBoolean()) {
            sb.append("    private PointF ").append(pointFVarName).append(";\n");
        }
        if (RandomUtils.randomBoolean()) {
            sb.append("    private Region ").append(regionVarName).append(";\n");
        }

        sb.append("\n");

        // 生成构造函数
        sb.append("    public ").append(className).append("(Context ").append(contextVarName).append(") {\n");
        sb.append("        this(").append(contextVarName).append(", null);\n");
        sb.append("    }\n\n");

        sb.append("    public ").append(className).append("(Context ").append(contextVarName).append(", AttributeSet ").append(attrsVarName).append(") {\n");
        sb.append("        this(").append(contextVarName).append(", ").append(attrsVarName).append(", 0);\n");
        sb.append("    }\n\n");

        sb.append("    public ").append(className).append("(Context ").append(contextVarName).append(", AttributeSet ").append(attrsVarName).append(", int ").append(defStyleVarName).append(") {\n");
        sb.append("        super(").append(contextVarName).append(", ").append(attrsVarName).append(", ").append(defStyleVarName).append(");\n");
        sb.append("        ").append(initMethodName).append("();\n");
        sb.append("    }\n\n");

        // 生成初始化方法
        sb.append("    private void ").append(initMethodName).append("() {\n");
        sb.append("        ").append(paintVarName).append(" = new Paint(Paint.ANTI_ALIAS_FLAG);\n");
        sb.append("        ").append(paintVarName).append(".setColor(DEFAULT_COLOR);\n");
        sb.append("        ").append(paintVarName).append(".setStrokeWidth(DEFAULT_STROKE_WIDTH);\n");

        // 随机设置Paint样式
        String paintStyle = PAINT_STYLES[RandomUtils.between(0, PAINT_STYLES.length - 1)];
        sb.append("        ").append(paintVarName).append(".setStyle(Paint.Style.").append(paintStyle).append(");\n");

        // 随机设置Cap样式
        String capStyle = CAP_STYLES[RandomUtils.between(0, CAP_STYLES.length - 1)];
        sb.append("        ").append(paintVarName).append(".setStrokeCap(Paint.Cap.").append(capStyle).append(");\n");

        // 随机设置Join样式
        String joinStyle = JOIN_STYLES[RandomUtils.between(0, JOIN_STYLES.length - 1)];
        sb.append("        ").append(paintVarName).append(".setStrokeJoin(Paint.Join.").append(joinStyle).append(");\n");

        sb.append("\n");
        sb.append("        ").append(pathVarName).append(" = new Path();\n");
        sb.append("        ").append(matrixVarName).append(" = new Matrix();\n");

        // 随机初始化额外成员变量
        if (RandomUtils.randomBoolean()) {
            sb.append("        ").append(colorVarName).append(" = DEFAULT_COLOR;\n");
        }
        if (RandomUtils.randomBoolean()) {
            sb.append("        ").append(rectVarName).append(" = new Rect();\n");
        }
        if (RandomUtils.randomBoolean()) {
            sb.append("        ").append(rectFVarName).append(" = new RectF();\n");
        }
        if (RandomUtils.randomBoolean()) {
            sb.append("        ").append(pointVarName).append(" = new Point();\n");
        }
        if (RandomUtils.randomBoolean()) {
            sb.append("        ").append(pointFVarName).append(" = new PointF();\n");
        }
        if (RandomUtils.randomBoolean()) {
            sb.append("        ").append(regionVarName).append(" = new Region();\n");
        }

        sb.append("    }\n\n");

        // 生成onDraw方法
        sb.append("    @Override\n");
        sb.append("    protected void onDraw(Canvas ").append(canvasVarName).append(") {\n");
        sb.append("        super.onDraw(").append(canvasVarName).append(");\n");
        sb.append("        this.").append(canvasVarName).append(" = ").append(canvasVarName).append(";\n");

        // 根据操作类型调用不同方法
        if (operationType.equals("draw")) {
            sb.append("        ").append(drawMethodName).append("();\n");
        } else if (operationType.equals("transform")) {
            sb.append("        ").append(transformMethodName).append("();\n");
        } else if (operationType.equals("clip")) {
            sb.append("        ").append(clipMethodName).append("();\n");
        } else if (operationType.equals("save")) {
            sb.append("        ").append(saveMethodName).append("();\n");
        } else if (operationType.equals("restore")) {
            sb.append("        ").append(restoreMethodName).append("();\n");
        } else {
            sb.append("        ").append(drawMethodName).append("();\n");
            sb.append("        ").append(transformMethodName).append("();\n");
        }

        sb.append("    }\n\n");

        // 生成draw方法
        sb.append("    private void ").append(drawMethodName).append("() {\n");
        sb.append("        ").append(canvasVarName).append(".drawColor(DEFAULT_COLOR);\n");

        // 随机绘制不同形状
        int shapeType = RandomUtils.between(0, 4);
        if (shapeType == 0) {
            sb.append("        ").append(canvasVarName).append(".drawRect(0, 0, getWidth(), getHeight(), ").append(paintVarName).append(");\n");
        } else if (shapeType == 1) {
            sb.append("        ").append(canvasVarName).append(".drawCircle(getWidth() / 2f, getHeight() / 2f, Math.min(getWidth(), getHeight()) / 4f, ").append(paintVarName).append(");\n");
        } else if (shapeType == 2) {
            sb.append("        ").append(pathVarName).append("reset();\n");
            sb.append("        ").append(pathVarName).append(".moveTo(0, getHeight() / 2f);\n");
            sb.append("        ").append(pathVarName).append(".lineTo(getWidth() / 2f, 0);\n");
            sb.append("        ").append(pathVarName).append(".lineTo(getWidth(), getHeight() / 2f);\n");
            sb.append("        ").append(pathVarName).append(".close();\n");
            sb.append("        ").append(canvasVarName).append(".drawPath(").append(pathVarName).append(", ").append(paintVarName).append(");\n");
        } else if (shapeType == 3) {
            sb.append("        ").append(canvasVarName).append(".drawOval(0, 0, getWidth(), getHeight(), ").append(paintVarName).append(");\n");
        } else {
            sb.append("        ").append(canvasVarName).append(".drawRoundRect(0, 0, getWidth(), getHeight(), 20, 20, ").append(paintVarName).append(");\n");
        }

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");

        // 生成transform方法
        sb.append("    private void ").append(transformMethodName).append("() {\n");
        sb.append("        ").append(matrixVarName).append(".reset();\n");

        // 随机应用不同的变换
        int transformType = RandomUtils.between(0, 3);
        if (transformType == 0) {
            sb.append("        ").append(matrixVarName).append(".postTranslate(getWidth() / 4f, getHeight() / 4f);\n");
        } else if (transformType == 1) {
            sb.append("        ").append(matrixVarName).append(".postScale(0.5f, 0.5f);\n");
        } else if (transformType == 2) {
            sb.append("        ").append(matrixVarName).append(".postRotate(45, getWidth() / 2f, getHeight() / 2f);\n");
        } else {
            sb.append("        ").append(matrixVarName).append(".postSkew(0.2f, 0.2f);\n");
        }

        sb.append("        ").append(canvasVarName).append(".concat(").append(matrixVarName).append(");\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");

        // 生成clip方法
        sb.append("    private void ").append(clipMethodName).append("() {\n");
        sb.append("        ").append(pathVarName).append(".reset();\n");

        // 随机裁剪不同形状
        int clipType = RandomUtils.between(0, 3);
        if (clipType == 0) {
            sb.append("        ").append(pathVarName).append(".addCircle(getWidth() / 2f, getHeight() / 2f, Math.min(getWidth(), getHeight()) / 4f, Path.Direction.CW);\n");
        } else if (clipType == 1) {
            sb.append("        ").append(pathVarName).append(".addRect(0, 0, getWidth() / 2f, getHeight() / 2f, Path.Direction.CW);\n");
        } else if (clipType == 2) {
            sb.append("        ").append(pathVarName).append(".addOval(0, 0, getWidth(), getHeight(), Path.Direction.CW);\n");
        } else {
            sb.append("        ").append(pathVarName).append(".addRoundRect(0, 0, getWidth(), getHeight(), 20, 20, Path.Direction.CW);\n");
        }

        sb.append("        ").append(canvasVarName).append(".clipPath(").append(pathVarName).append(");\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");

        // 生成save方法
        sb.append("    private void ").append(saveMethodName).append("() {\n");
        sb.append("        ").append(canvasVarName).append(".save();\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");

        // 生成restore方法
        sb.append("    private void ").append(restoreMethodName).append("() {\n");
        sb.append("        ").append(canvasVarName).append(".restore();\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");

        // 生成设置颜色方法
        sb.append("    public void ").append(setColorMethodName).append("(int ").append(colorVarName).append(") {\n");
        sb.append("        ").append(paintVarName).append(".setColor(").append(colorVarName).append(");\n");
        sb.append("        invalidate();\n");
        sb.append("    }\n\n");

        // 生成设置样式方法
        sb.append("    public void ").append(setStyleMethodName).append("(Paint.Style ").append(paintStyle).append(") {\n");
        sb.append("        ").append(paintVarName).append(".setStyle(").append(paintStyle).append(");\n");
        sb.append("        invalidate();\n");
        sb.append("    }\n\n");

        // 生成设置宽度方法
        sb.append("    public void ").append(setWidthMethodName).append("(float ").append(strokeWidth).append(") {\n");
        sb.append("        ").append(paintVarName).append(".setStrokeWidth(").append(strokeWidth).append(");\n");
        sb.append("        invalidate();\n");
        sb.append("    }\n\n");

        // 随机添加额外方法
        if (RandomUtils.randomBoolean()) {
            generateApplyShaderMethod(sb, className, shaderVarName, paintVarName, logMessage);
        }

        if (RandomUtils.randomBoolean()) {
            generateApplyXfermodeMethod(sb, className, xfermodeVarName, paintVarName, logMessage);
        }

        if (RandomUtils.randomBoolean()) {
            generateDrawTextMethod(sb, className, canvasVarName, paintVarName, logMessage);
        }

        if (RandomUtils.randomBoolean()) {
            generateDrawBitmapMethod(sb, className, canvasVarName, bitmapVarName, logMessage);
        }

        if (RandomUtils.randomBoolean()) {
            generateDrawPathMethod(sb, className, canvasVarName, pathVarName, paintVarName, logMessage);
        }

        if (RandomUtils.randomBoolean()) {
            generateDrawRegionMethod(sb, className, canvasVarName, regionVarName, paintVarName, logMessage);
        }

        if (RandomUtils.randomBoolean()) {
            generateDrawPointMethod(sb, className, canvasVarName, paintVarName, pointVarName, pointFVarName, logMessage);
        }

        if (RandomUtils.randomBoolean()) {
            generateDrawRectMethod(sb, className, canvasVarName, paintVarName, rectVarName, rectFVarName, logMessage);
        }

        if (RandomUtils.randomBoolean()) {
            generateDrawLineMethod(sb, className, canvasVarName, paintVarName, logMessage);
        }

        if (RandomUtils.randomBoolean()) {
            generateDrawArcMethod(sb, className, canvasVarName, paintVarName, logMessage);
        }

        if (RandomUtils.randomBoolean()) {
            generateDrawOvalMethod(sb, className, canvasVarName, paintVarName, logMessage);
        }

        if (RandomUtils.randomBoolean()) {
            generateDrawRoundRectMethod(sb, className, canvasVarName, paintVarName, logMessage);
        }

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "graphics");
    }

    // 生成应用着色器方法
    private void generateApplyShaderMethod(StringBuilder sb, String className, String shaderVarName, String paintVarName, String logMessage) {
        String methodName = RandomUtils.generateWord(6) + "ApplyShader";
        String shaderType = SHADER_TYPES[RandomUtils.between(0, SHADER_TYPES.length - 1)];

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        ").append(shaderVarName).append(" = new ").append(shaderType).append("(0, 0, getWidth(), getHeight(), 0xFF000000, 0xFFFFFFFF, Shader.TileMode.CLAMP);\n");
        sb.append("        ").append(paintVarName).append(".setShader(").append(shaderVarName).append(");\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");
    }

    // 生成应用混合模式方法
    private void generateApplyXfermodeMethod(StringBuilder sb, String className, String xfermodeVarName, String paintVarName, String logMessage) {
        String methodName = RandomUtils.generateWord(6) + "ApplyXfermode";
        String xfermodeType = XFERMODE_TYPES[RandomUtils.between(0, XFERMODE_TYPES.length - 1)];

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        ").append(xfermodeVarName).append(" = new PorterDuffXfermode(PorterDuff.Mode.").append(xfermodeType).append(");\n");
        sb.append("        ").append(paintVarName).append(".setXfermode(").append(xfermodeVarName).append(");\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");
    }

    // 生成绘制文本方法
    private void generateDrawTextMethod(StringBuilder sb, String className, String canvasVarName, String paintVarName, String logMessage) {
        String methodName = RandomUtils.generateWord(6) + "DrawText";
        String textVarName = RandomUtils.generateWord(6);

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        String ").append(textVarName).append(" = \"").append(RandomUtils.generateWord(6)).append("\";\n");
        sb.append("        ").append(canvasVarName).append(".drawText(").append(textVarName).append(", getWidth() / 2f, getHeight() / 2f, ").append(paintVarName).append(");\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");
    }

    // 生成绘制位图方法
    private void generateDrawBitmapMethod(StringBuilder sb, String className, String canvasVarName, String bitmapVarName, String logMessage) {
        String methodName = RandomUtils.generateWord(6) + "DrawBitmap";

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        if (").append(bitmapVarName).append(" == null) {\n");
        sb.append("            ").append(bitmapVarName).append(" = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);\n");
        sb.append("        }\n");
        sb.append("        ").append(canvasVarName).append(".drawBitmap(").append(bitmapVarName).append(", 0, 0, null);\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");
    }

    // 生成绘制路径方法
    private void generateDrawPathMethod(StringBuilder sb, String className, String canvasVarName, String pathVarName, String paintVarName, String logMessage) {
        String methodName = RandomUtils.generateWord(6) + "DrawPath";

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        ").append(pathVarName).append(".reset();\n");
        sb.append("        ").append(pathVarName).append(".moveTo(0, 0);\n");
        sb.append("        ").append(pathVarName).append(".lineTo(getWidth(), 0);\n");
        sb.append("        ").append(pathVarName).append(".lineTo(getWidth(), getHeight());\n");
        sb.append("        ").append(pathVarName).append(".lineTo(0, getHeight());\n");
        sb.append("        ").append(pathVarName).append(".close();\n");
        sb.append("        ").append(canvasVarName).append(".drawPath(").append(pathVarName).append(", ").append(paintVarName).append(");\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");
    }

    // 生成绘制区域方法
    private void generateDrawRegionMethod(StringBuilder sb, String className, String canvasVarName, String regionVarName, String paintVarName, String logMessage) {
        String methodName = RandomUtils.generateWord(6) + "DrawRegion";
        String rectVarName = RandomUtils.generateWord(6);

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        Rect ").append(rectVarName).append(" = new Rect(0, 0, getWidth() / 2, getHeight() / 2);\n");
        sb.append("        ").append(regionVarName).append(" = new Region(").append(rectVarName).append(");\n");
        sb.append("        RegionIterator ").append(regionVarName).append("Iterator = new RegionIterator(").append(regionVarName).append(");\n");
        sb.append("        Rect ").append(rectVarName).append("Temp = new Rect();\n");
        sb.append("        while (").append(regionVarName).append("Iterator.next(").append(rectVarName).append("Temp)) {\n");
        sb.append("            ").append(canvasVarName).append(".drawRect(").append(rectVarName).append("Temp, ").append(paintVarName).append(");\n");
        sb.append("        }\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");
    }

    // 生成绘制点方法
    private void generateDrawPointMethod(StringBuilder sb, String className, String canvasVarName, String paintVarName, String pointVarName, String pointFVarName, String logMessage) {
        String methodName = RandomUtils.generateWord(6) + "DrawPoint";

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        ").append(pointVarName).append(" = new Point(getWidth() / 2, getHeight() / 2);\n");
        sb.append("        ").append(pointFVarName).append(" = new PointF(getWidth() / 2f, getHeight() / 2f);\n");
        sb.append("        ").append(canvasVarName).append(".drawPoint(").append(pointFVarName).append(".x, ").append(pointFVarName).append(".y, ").append(paintVarName).append(");\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");
    }

    // 生成绘制矩形方法
    private void generateDrawRectMethod(StringBuilder sb, String className, String canvasVarName, String paintVarName, String rectVarName, String rectFVarName, String logMessage) {
        String methodName = RandomUtils.generateWord(6) + "DrawRect";

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        ").append(rectVarName).append(" = new Rect(0, 0, getWidth() / 2, getHeight() / 2);\n");
        sb.append("        ").append(rectFVarName).append(" = new RectF(0, 0, getWidth() / 2f, getHeight() / 2f);\n");
        sb.append("        ").append(canvasVarName).append(".drawRect(").append(rectFVarName).append(", ").append(paintVarName).append(");\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");
    }

    // 生成绘制线条方法
    private void generateDrawLineMethod(StringBuilder sb, String className, String canvasVarName, String paintVarName, String logMessage) {
        String methodName = RandomUtils.generateWord(6) + "DrawLine";

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        ").append(canvasVarName).append(".drawLine(0, 0, getWidth(), getHeight(), ").append(paintVarName).append(");\n");
        sb.append("        ").append(canvasVarName).append(".drawLine(getWidth(), 0, 0, getHeight(), ").append(paintVarName).append(");\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");
    }

    // 生成绘制圆弧方法
    private void generateDrawArcMethod(StringBuilder sb, String className, String canvasVarName, String paintVarName, String logMessage) {
        String methodName = RandomUtils.generateWord(6) + "DrawArc";
        String rectFVarName = RandomUtils.generateWord(6);

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        RectF ").append(rectFVarName).append(" = new RectF(0, 0, getWidth(), getHeight());\n");
        sb.append("        ").append(canvasVarName).append(".drawArc(").append(rectFVarName).append(", 0, 360, false, ").append(paintVarName).append(");\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");
    }

    // 生成绘制椭圆方法
    private void generateDrawOvalMethod(StringBuilder sb, String className, String canvasVarName, String paintVarName, String logMessage) {
        String methodName = RandomUtils.generateWord(6) + "DrawOval";
        String rectFVarName = RandomUtils.generateWord(6);

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        RectF ").append(rectFVarName).append(" = new RectF(0, 0, getWidth(), getHeight());\n");
        sb.append("        ").append(canvasVarName).append(".drawOval(").append(rectFVarName).append(", ").append(paintVarName).append(");\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");
    }

    // 生成绘制圆角矩形方法
    private void generateDrawRoundRectMethod(StringBuilder sb, String className, String canvasVarName, String paintVarName, String logMessage) {
        String methodName = RandomUtils.generateWord(6) + "DrawRoundRect";
        String rectFVarName = RandomUtils.generateWord(6);

        sb.append("    private void ").append(methodName).append("() {\n");
        sb.append("        RectF ").append(rectFVarName).append(" = new RectF(0, 0, getWidth(), getHeight());\n");
        sb.append("        ").append(canvasVarName).append(".drawRoundRect(").append(rectFVarName).append(", 20, 20, ").append(paintVarName).append(");\n");

        // 随机添加日志
        if (RandomUtils.randomBoolean()) {
            sb.append("        Log.d(TAG, \"").append(logMessage).append("\");\n");
        }

        sb.append("    }\n\n");
    }
}
