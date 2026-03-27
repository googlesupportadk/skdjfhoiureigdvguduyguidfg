package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class CustomViewGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] VIEW_TYPES = {
            "Button", "TextView", "ImageView", "CardView", "LinearLayout",
            "RelativeLayout", "ConstraintLayout", "FrameLayout", "GridLayout", "TableLayout",
            "ScrollView", "HorizontalScrollView", "NestedScrollView", "CoordinatorLayout", "AppBarLayout"
    };

    private static final String[] ATTRIBUTE_NAMES = {
            "text", "color", "size", "padding", "margin", "radius",
            "elevation", "alpha", "rotation", "scale", "translation",
            "shadow", "border", "background", "foreground", "visibility"
    };

    public CustomViewGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成自定义视图类");

        // 空值防护：UI样式默认值
        String uiStyle = variationManager.getVariation("ui_style");
        uiStyle = (uiStyle == null) ? "default" : uiStyle;

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("View");
            generateCustomViewClass(className, uiStyle);
        }
    }

    private void generateCustomViewClass(String className, String uiStyle) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 包声明
        sb.append(generatePackageDeclaration("ui"));

        // 导入语句（规范换行，补充必要依赖）
        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.graphics.Canvas"));
        sb.append(generateImportStatement("android.graphics.Paint"));
        sb.append(generateImportStatement("android.graphics.RectF"));
        sb.append(generateImportStatement("android.graphics.Path"));
        sb.append(generateImportStatement("android.graphics.drawable.Drawable"));
        sb.append(generateImportStatement("android.util.AttributeSet"));
        sb.append(generateImportStatement("android.view.View"));
        sb.append(generateImportStatement("android.view.animation.Animation"));
        sb.append(generateImportStatement("android.view.animation.AnimationUtils"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.List"));

        // Material样式导入（空值防护）
        if (uiStyle != null && uiStyle.contains("material")) {
            sb.append(generateImportStatement("com.google.android.material.card.MaterialCardView"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n"); // 规范空行

        // 类定义（继承View，规范换行）
        sb.append("public class ").append(className).append(" extends View {\n\n");

        // 随机选择属性名（边界防护）
        int attrIndex = RandomUtils.between(0, ATTRIBUTE_NAMES.length - 1);
        String attributeName = ATTRIBUTE_NAMES[attrIndex];

        // 修复核心问题：字符串转义错误（常量定义）
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(attributeName.toUpperCase()).append(" = \"").append(attributeName).append("\";\n\n");

        // 核心字段
        sb.append("    private Paint paint;\n");
        sb.append("    private float ").append(attributeName).append("Value;\n\n");

        // 随机决定是否使用路径
        if (RandomUtils.randomBoolean()) {
            sb.append("    private Path customPath;\n");
        }

        // 随机决定是否使用矩形
        if (RandomUtils.randomBoolean()) {
            sb.append("    private RectF boundsRect;\n");
        }

        // 随机决定是否使用Drawable
        if (RandomUtils.randomBoolean()) {
            sb.append("    private Drawable backgroundDrawable;\n");
        }

        // 随机决定是否使用动画
        if (RandomUtils.randomBoolean()) {
            sb.append("    private Animation viewAnimation;\n");
        }

        // 随机决定是否使用多个Paint（修复语法错误：多余的\n\n;）
        if (RandomUtils.randomBoolean()) {
            sb.append("    private List<Paint> paintList;\n");
        }
        sb.append("\n"); // 规范空行

        // 构造方法重载
        sb.append("    public ").append(className).append("(Context context) {\n");
        sb.append("        this(context, null);\n");
        sb.append("    }\n\n");

        sb.append("    public ").append(className).append("(Context context, AttributeSet attrs) {\n");
        sb.append("        this(context, attrs, 0);\n");
        sb.append("    }\n\n");

        sb.append("    public ").append(className).append("(Context context, AttributeSet attrs, int defStyleAttr) {\n");
        sb.append("        super(context, attrs, defStyleAttr);\n");
        sb.append("        init();\n");
        sb.append("    }\n\n");

        // 初始化方法
        sb.append("    private void init() {\n");
        sb.append("        paint = new Paint(Paint.ANTI_ALIAS_FLAG);\n");
        sb.append("        paint.setColor(0xFF000000);\n");
        sb.append("        paint.setStyle(Paint.Style.FILL);\n");
        sb.append("        paint.setTextSize(48f);\n\n");

        // 随机初始化路径
        if (RandomUtils.randomBoolean()) {
            sb.append("        customPath = new Path();\n");
        }

        // 随机初始化矩形
        if (RandomUtils.randomBoolean()) {
            sb.append("        boundsRect = new RectF();\n");
        }

        // 随机初始化动画（空值防护：上下文可能为null）
        if (RandomUtils.randomBoolean()) {
            sb.append("        if (getContext() != null) {\n");
            sb.append("            viewAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.view_animation);\n");
            sb.append("        }\n");
        }

        // 随机初始化Paint列表
        if (RandomUtils.randomBoolean()) {
            sb.append("        paintList = new ArrayList<>();\n");
            sb.append("        for (int i = 0; i < 3; i++) {\n");
            sb.append("            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);\n");
            sb.append("            p.setColor(0xFF000000 + i * 0x333333);\n");
            sb.append("            paintList.add(p);\n");
            sb.append("        }\n");
        }

        sb.append("    }\n\n");

        // onDraw方法（修复字符串转义 + 绘制文本语法）
        sb.append("    @Override\n");
        sb.append("    protected void onDraw(Canvas canvas) {\n");
        sb.append("        super.onDraw(canvas);\n");
        // 空值防护：canvas可能为null
        sb.append("        if (canvas == null || paint == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        // 修复核心问题：绘制文本的字符串转义 + 语法错误
        String drawText = RandomUtils.generateWord(RandomUtils.between(3, 8));
        sb.append("        canvas.drawText(\"").append(drawText).append("\", getWidth() / 2f, getHeight() / 2f, paint);\n");
        sb.append("    }\n\n");

        // onMeasure方法
        sb.append("    @Override\n");
        sb.append("    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {\n");
        sb.append("        int width = MeasureSpec.getSize(widthMeasureSpec);\n");
        sb.append("        int height = MeasureSpec.getSize(heightMeasureSpec);\n");
        sb.append("        setMeasuredDimension(width, height);\n");
        sb.append("    }\n\n");

        // Setter方法（首字母大写）
        String attrCamelCase = Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);
        sb.append("    public void set").append(attrCamelCase).append("(float value) {\n");
        sb.append("        this.").append(attributeName).append("Value = value;\n");
        sb.append("        invalidate();\n");
        sb.append("    }\n\n");

        // Getter方法
        sb.append("    public float get").append(attrCamelCase).append("() {\n");
        sb.append("        return ").append(attributeName).append("Value;\n");
        sb.append("    }\n\n");

        // 随机生成3-8个方法
        int methodCount = RandomUtils.between(3, 8);
        for (int i = 0; i < methodCount; i++) {
            int methodType = RandomUtils.between(0, 6);
            String methodName = RandomUtils.generateMethodName("draw");

            switch (methodType) {
                case 0:
                    if (RandomUtils.randomBoolean()) {
                        generatePathMethod(sb, methodName);
                    }
                    break;
                case 1:
                    if (RandomUtils.randomBoolean()) {
                        generateRectMethod(sb, methodName);
                    }
                    break;
                case 2:
                    if (RandomUtils.randomBoolean()) {
                        generateDrawableMethod(sb, methodName);
                    }
                    break;
                case 3:
                    if (RandomUtils.randomBoolean()) {
                        generateAnimationMethod(sb, methodName);
                    }
                    break;
                case 4:
                    if (RandomUtils.randomBoolean()) {
                        generateMultiPaintMethod(sb, methodName);
                    }
                    break;
                case 5:
                    if (RandomUtils.randomBoolean()) {
                        generateColorMethod(sb, methodName);
                    }
                    break;
            }
        }

        // 闭合类
        sb.append("}\n");

        // 生成文件
        generateJavaFile(className, sb.toString(), "ui");
    }

    // 路径操作方法（空值防护）
    private void generatePathMethod(StringBuilder sb, String methodName) {
        sb.append("    public void ").append(methodName).append("Path(Path path) {\n");
        sb.append("        if (customPath != null && path != null) {\n"); // 空值防护
        sb.append("            customPath.set(path);\n");
        sb.append("            invalidate();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    // 矩形操作方法（空值防护）
    private void generateRectMethod(StringBuilder sb, String methodName) {
        sb.append("    public void ").append(methodName).append("Rect(float left, float top, float right, float bottom) {\n");
        sb.append("        if (boundsRect != null) {\n");
        sb.append("            boundsRect.set(left, top, right, bottom);\n");
        sb.append("            invalidate();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    // Drawable操作方法（空值防护）
    private void generateDrawableMethod(StringBuilder sb, String methodName) {
        sb.append("    public void ").append(methodName).append("Drawable(Drawable drawable) {\n");
        sb.append("        if (drawable != null) {\n"); // 空值防护（修复逻辑错误：不应判断backgroundDrawable）
        sb.append("            this.backgroundDrawable = drawable;\n");
        sb.append("            setBackground(drawable);\n");
        sb.append("            invalidate();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    // 动画操作方法（空值防护）
    private void generateAnimationMethod(StringBuilder sb, String methodName) {
        sb.append("    public void ").append(methodName).append("Animation() {\n");
        sb.append("        if (viewAnimation != null) {\n");
        sb.append("            startAnimation(viewAnimation);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    // 多Paint操作方法（空值防护）
    private void generateMultiPaintMethod(StringBuilder sb, String methodName) {
        sb.append("    public void ").append(methodName).append("WithPaint(int index, Paint newPaint) {\n");
        sb.append("        if (paintList != null && newPaint != null && index >= 0 && index < paintList.size()) {\n"); // 空值+边界防护
        sb.append("            paintList.set(index, newPaint);\n");
        sb.append("            invalidate();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    // 颜色操作方法（修复语法错误 + 空值防护）
    private void generateColorMethod(StringBuilder sb, String methodName) {
        // 修复核心问题：多余的sb.append()嵌套语法错误
        sb.append("    public void ").append(methodName).append("Color(int color) {\n");
        sb.append("        if (paint != null) {\n"); // 空值防护
        sb.append("            paint.setColor(color);\n");
        sb.append("            invalidate();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(methodName).append("ColorRandom() {\n");
        sb.append("        if (paint != null) {\n"); // 空值防护
        sb.append("            paint.setColor(0xFF000000 + RandomUtils.between(0, 0xFFFFFF));\n");
        sb.append("            invalidate();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }
}