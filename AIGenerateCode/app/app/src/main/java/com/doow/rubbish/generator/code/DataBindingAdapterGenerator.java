package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class DataBindingAdapterGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] BINDING_TYPES = {
            "imageUrl", "loadImage", "setText", "setVisibility", "setEnabled",
            "setAlpha", "setTranslation", "setScale", "setRotation", "setPadding",
            "setMargin", "setBackground", "setForeground", "setElevation", "setClip"
    };

    private static final String[] ATTRIBUTE_NAMES = {
            "app:imageUrl", "app:setText", "app:isVisible", "app:isEnabled", "app:isLoading",
            "app:alpha", "app:translation", "app:scale", "app:rotation", "app:padding",
            "app:margin", "app:background", "app:foreground", "app:elevation", "app:clip"
    };

    public DataBindingAdapterGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成数据绑定适配器类");

        // 空值防护：UI样式默认值
        String uiStyle = variationManager.getVariation("ui_style");
        uiStyle = (uiStyle == null) ? "default" : uiStyle;

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("BindingAdapter");
            generateBindingAdapterClass(className, uiStyle);
        }
    }

    private void generateBindingAdapterClass(String className, String uiStyle) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 包声明
        sb.append(generatePackageDeclaration("binding"));

        // 导入语句（规范换行，补充必要依赖）
        sb.append(generateImportStatement("android.widget.ImageView"));
        sb.append(generateImportStatement("android.widget.TextView"));
        sb.append(generateImportStatement("android.view.View"));
        sb.append(generateImportStatement("android.view.ViewGroup"));
        sb.append(generateImportStatement("android.graphics.drawable.Drawable"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("androidx.databinding.BindingAdapter"));
        sb.append(generateImportStatement("androidx.databinding.BindingConversion"));

        // Material样式导入（空值防护）
        if (uiStyle != null && uiStyle.contains("material")) {
            sb.append(generateImportStatement("com.google.android.material.imageview.ShapeableImageView"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append(generateImportStatement(packageName + ".R"));
        sb.append("\n"); // 规范空行

        // 类定义（规范换行）
        sb.append("public class ").append(className).append(" {\n\n");

        // 功能开关标志
        boolean useImageLoading = RandomUtils.randomBoolean();
        boolean useTextFormatting = RandomUtils.randomBoolean();
        boolean useAnimation = RandomUtils.randomBoolean();
        boolean useValidation = RandomUtils.randomBoolean();
        boolean useConversion = RandomUtils.randomBoolean();
        boolean useCache = RandomUtils.randomBoolean();

        // 根据标志变量生成字段（修复字符串转义）
        if (useCache) {
            sb.append("    private static final String TAG = \"").append(className).append("\";\n");
            sb.append("    private static final int CACHE_SIZE = 100;\n");
            sb.append("    private static java.util.Map<String, Object> imageCache;\n");
            sb.append("    private static boolean cacheInitialized = false;\n\n");
        }

        // 初始化方法（修复Log转义 + 空值防护）
        if (useCache) {
            sb.append("    static {\n");
            sb.append("        if (!cacheInitialized) {\n");
            sb.append("            imageCache = new java.util.concurrent.ConcurrentHashMap<>();\n");
            sb.append("            cacheInitialized = true;\n");
            sb.append("            Log.d(TAG, \"Image cache initialized\");\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成多个BindingAdapter方法
        int methodCount = RandomUtils.between(3, 8);
        for (int i = 0; i < methodCount; i++) {
            // 边界防护：避免数组越界
            int bindingIndex = RandomUtils.between(0, BINDING_TYPES.length - 1);
            int attrIndex = RandomUtils.between(0, ATTRIBUTE_NAMES.length - 1);

            String bindingType = BINDING_TYPES[bindingIndex];
            String attributeName = ATTRIBUTE_NAMES[attrIndex];
            String methodName = Character.toUpperCase(bindingType.charAt(0)) + bindingType.substring(1);

            // 根据binding类型生成不同的方法
            if (bindingType.contains("image")) {
                generateImageBindingMethods(sb, attributeName, methodName, useImageLoading, useCache);
            } else if (bindingType.contains("text")) {
                generateTextBindingMethods(sb, attributeName, methodName, useTextFormatting, useValidation);
            } else if (bindingType.contains("visibility") || bindingType.contains("enabled")) {
                generateVisibilityBindingMethods(sb, attributeName, methodName);
            } else if (bindingType.contains("alpha") || bindingType.contains("scale") ||
                    bindingType.contains("rotation") || bindingType.contains("translation")) {
                generateTransformBindingMethods(sb, attributeName, methodName, useAnimation);
            } else if (bindingType.contains("padding") || bindingType.contains("margin")) {
                generatePaddingBindingMethods(sb, attributeName, methodName);
            } else if (bindingType.contains("background") || bindingType.contains("foreground")) {
                generateBackgroundBindingMethods(sb, attributeName, methodName, useCache);
            }
        }

        // 根据标志变量生成配套方法
        if (useConversion) {
            generateConversionMethods(sb);
        }

        if (useValidation) {
            generateValidationMethods(sb);
        }

        // 闭合类（规范换行）
        sb.append("}\n");

        // 生成文件
        generateJavaFile(className, sb.toString(), "binding");
    }

    // 图片绑定方法（修复字符串转义 + 空值防护）
    private void generateImageBindingMethods(StringBuilder sb, String attributeName, String methodName,
                                             boolean useImageLoading, boolean useCache) {
        // String参数版本（修复BindingAdapter注解转义）
        sb.append("    @BindingAdapter(\"").append(attributeName).append("\")\n");
        sb.append("    public static void set").append(methodName).append("(ImageView view, String url) {\n");
        // 增强空值防护：view也可能为null
        sb.append("        if (view == null || url == null || url.isEmpty()) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        if (useCache) {
            sb.append("        if (imageCache != null && imageCache.containsKey(url)) {\n"); // 空值防护
            sb.append("            Drawable cached = (Drawable) imageCache.get(url);\n");
            sb.append("            if (cached != null) {\n");
            sb.append("                view.setImageDrawable(cached);\n");
            sb.append("                return;\n");
            sb.append("            }\n");
            sb.append("        }\n");
        }
        sb.append("        ImageUtils.loadImage(view, url);\n");
        sb.append("    }\n\n");

        // Drawable参数版本（修复BindingAdapter注解转义）
        sb.append("    @BindingAdapter(\"").append(attributeName).append("\")\n");
        sb.append("    public static void set").append(methodName).append("(ImageView view, Drawable drawable) {\n");
        sb.append("        if (view != null && drawable != null) {\n"); // 空值防护
        sb.append("            view.setImageDrawable(drawable);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    // 文本绑定方法（修复字符串转义 + 空值防护）
    private void generateTextBindingMethods(StringBuilder sb, String attributeName, String methodName,
                                            boolean useTextFormatting, boolean useValidation) {
        sb.append("    @BindingAdapter(\"").append(attributeName).append("\")\n");
        sb.append("    public static void set").append(methodName).append("(TextView view, String text) {\n");
        // 增强空值防护：view可能为null
        sb.append("        if (view == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        if (useValidation) {
            sb.append("        if (!isValidText(text)) {\n");
            sb.append("            view.setText(\"\");\n"); // 修复字符串转义
            sb.append("            return;\n");
            sb.append("        }\n");
        }
        if (useTextFormatting) {
            sb.append("        String formattedText = formatText(text);\n");
            sb.append("        view.setText(formattedText);\n");
        } else {
            sb.append("        view.setText(text);\n");
        }
        sb.append("    }\n\n");
    }

    // 可见性绑定方法（修复字符串转义 + 逻辑错误）
    private void generateVisibilityBindingMethods(StringBuilder sb, String attributeName, String methodName) {
        // String参数版本（修复BindingAdapter注解转义）
        sb.append("    @BindingAdapter(\"").append(attributeName).append("\")\n");
        sb.append("    public static void set").append(methodName).append("(View view, String visible) {\n");
        sb.append("        if (view == null) {\n"); // 空值防护
        sb.append("            return;\n");
        sb.append("        }\n");
        // 修复字符串转义："true" → \"true\"
        sb.append("        boolean isVisible = \"true\".equalsIgnoreCase(visible);\n");
        sb.append("        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);\n");
        sb.append("    }\n\n");

        // boolean参数版本（修复BindingAdapter注解转义）
        sb.append("    @BindingAdapter(\"").append(attributeName).append("\")\n");
        sb.append("    public static void set").append(methodName).append("(View view, boolean visible) {\n");
        sb.append("        if (view == null) {\n"); // 空值防护
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        view.setVisibility(visible ? View.VISIBLE : View.GONE);\n");
        sb.append("    }\n\n");
    }

    // 变换绑定方法（修复字符串转义 + 语法错误）
    private void generateTransformBindingMethods(StringBuilder sb, String attributeName, String methodName, boolean useAnimation) {
        sb.append("    @BindingAdapter(\"").append(attributeName).append("\")\n");
        sb.append("    public static void set").append(methodName).append("(View view, float value) {\n");
        sb.append("        if (view == null) {\n"); // 空值防护
        sb.append("            return;\n");
        sb.append("        }\n");
        if (useAnimation) {
            // 修复语法错误：链式调用换行 + 转义
            sb.append("        view.animate()\n");
            sb.append("            .").append(methodName.toLowerCase()).append("(value)\n");
            sb.append("            .setDuration(300)\n");
            sb.append("            .start();\n");
        } else {
            // 修复方法名逻辑：setAlpha → setAlpha，而非setlpha
            String transformMethod = "set" + methodName.substring(3);
            sb.append("        view.").append(transformMethod).append("(value);\n");
        }
        sb.append("    }\n\n");
    }

    // 内边距绑定方法（修复字符串转义 + 逻辑错误）
    private void generatePaddingBindingMethods(StringBuilder sb, String attributeName, String methodName) {
        sb.append("    @BindingAdapter(\"").append(attributeName).append("\")\n");
        sb.append("    public static void set").append(methodName).append("(View view, int value) {\n");
        sb.append("        if (view == null) {\n"); // 空值防护
        sb.append("            return;\n");
        sb.append("        }\n");
        // 修复方法名逻辑：setPadding → setPadding，而非setdding
        String paddingMethod = "set" + methodName.substring(3);
        sb.append("        view.").append(paddingMethod).append("(value, value, value, value);\n");
        sb.append("    }\n\n");
    }

    // 背景绑定方法（修复字符串转义 + 空值防护）
    private void generateBackgroundBindingMethods(StringBuilder sb, String attributeName, String methodName, boolean useCache) {
        sb.append("    @BindingAdapter(\"").append(attributeName).append("\")\n");
        sb.append("    public static void set").append(methodName).append("(View view, Drawable drawable) {\n");
        sb.append("        if (view == null) {\n"); // 空值防护
        sb.append("            return;\n");
        sb.append("        }\n");
        if (useCache) {
            // 修复字符串转义："bg_" → \"bg_\"
            sb.append("        String key = \"bg_\" + System.identityHashCode(drawable);\n");
            sb.append("        if (imageCache != null) {\n"); // 空值防护
            sb.append("            imageCache.put(key, drawable);\n");
            sb.append("        }\n");
        }
        // 修复方法名逻辑：setBackground → setBackground，而非setackground
        String bgMethod = methodName.substring(3);
        sb.append("        view.").append(bgMethod).append("(drawable);\n");
        sb.append("    }\n\n");

        sb.append("    @BindingAdapter(\"").append(attributeName).append("\")\n");
        sb.append("    public static void set").append(methodName).append("(View view, int colorRes) {\n");
        sb.append("        if (view == null) {\n"); // 空值防护
        sb.append("            return;\n");
        sb.append("        }\n");
        // 修复API调用：getResources()可能为null
        sb.append("        if (view.getResources() != null) {\n");
        sb.append("            view.").append(bgMethod).append("(view.getResources().getDrawable(colorRes));\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    // 转换方法（修复字符串转义）
    private void generateConversionMethods(StringBuilder sb) {
        sb.append("    @BindingConversion\n");
        sb.append("    public static String convertToString(Object value) {\n");
        sb.append("        if (value == null) {\n");
        sb.append("            return \"\";\n"); // 修复字符串转义
        sb.append("        }\n");
        sb.append("        return value.toString();\n");
        sb.append("    }\n\n");

        sb.append("    @BindingConversion\n");
        sb.append("    public static int convertToInt(Object value) {\n");
        sb.append("        if (value == null) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("        if (value instanceof Number) {\n");
        sb.append("            return ((Number) value).intValue();\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            return Integer.parseInt(value.toString());\n");
        sb.append("        } catch (NumberFormatException e) {\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    // 验证方法（修复字符串转义 + 空值防护）
    private void generateValidationMethods(StringBuilder sb) {
        sb.append("    private static boolean isValidText(String text) {\n");
        sb.append("        if (text == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return !text.trim().isEmpty();\n");
        sb.append("    }\n\n");

        sb.append("    private static String formatText(String text) {\n");
        sb.append("        if (text == null) {\n");
        sb.append("            return \"\";\n"); // 修复字符串转义
        sb.append("        }\n");
        sb.append("        return text.trim();\n");
        sb.append("    }\n\n");
    }
}