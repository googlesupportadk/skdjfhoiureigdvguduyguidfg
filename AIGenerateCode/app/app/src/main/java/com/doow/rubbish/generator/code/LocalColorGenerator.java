package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.ArrayList;
import java.util.List;

public class LocalColorGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] COLOR_TYPES = {
            "RGB", "ARGB", "HSV", "HSL", "CMYK",
            "XYZ", "LAB", "LCH", "YUV", "YIQ",
            "YCbCr", "YPbPr", "HCL", "HPL", "HSLuv"
    };

    private static final String[] COLOR_NAMES = {
            "primary", "secondary", "accent", "background", "foreground",
            "red", "green", "blue", "yellow", "orange", "purple", "pink", "teal",
            "cyan", "magenta", "lime", "indigo", "violet",
            "amber", "brown", "gray", "black", "white"
    };

    private static final String[] FIELD_TYPES = {
            "int", "long", "float", "double", "boolean", "String", "Object"
    };

    private static final String[] COLOR_OPERATIONS = {
            "blend", "mix", "invert", "grayscale", "sepia", "saturate",
            "desaturate", "lighten", "darken", "rotate", "complement"
    };

    private static final String[] COLOR_SPACES = {
            "RGB", "HSV", "HSL", "CMYK", "LAB", "XYZ"
    };

    public LocalColorGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成颜色类");

        String uiStyle = variationManager.getVariation("ui_style");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Color");
            generateColorClass(className, uiStyle);
        }
    }

    private void generateColorClass(String className, String uiStyle) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("ui.color"));

        sb.append(generateImportStatement("android.graphics.Color"));
        sb.append(generateImportStatement("android.graphics.PorterDuff"));
        sb.append(generateImportStatement("android.graphics.PorterDuffColorFilter"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.List"));

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        String colorType = COLOR_TYPES[RandomUtils.between(0, COLOR_TYPES.length - 1)];
        String colorName = COLOR_NAMES[RandomUtils.between(0, COLOR_NAMES.length - 1)];
        String colorSpace = COLOR_SPACES[RandomUtils.between(0, COLOR_SPACES.length - 1)];

        sb.append("public class ").append(className).append(" {\n\n");

        // 添加常量

        sb.append("    private static final String COLOR_TYPE = \"").append(colorType).append("\");\n");
        sb.append("    private static final String COLOR_NAME = \"").append(colorName).append("\");\n");
        sb.append("    private static final String COLOR_SPACE = \"").append(colorSpace).append("\");\n\n");

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

        // 添加颜色相关字段
        sb.append("    private int color;\n");
        sb.append("    private float alpha;\n");
        sb.append("    private float red;\n");
        sb.append("    private float green;\n");
        sb.append("    private float blue;\n");
        sb.append("    private float hue;\n");
        sb.append("    private float saturation;\n");
        sb.append("    private float brightness;\n");
        sb.append("    private boolean isDark;\n");
        sb.append("    private boolean isLight;\n");
        sb.append("    private float contrastRatio;\n");
        sb.append("    private int operationCount;\n\n");

        // 构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this.color = Color.BLACK;\n");
        sb.append("        this.alpha = 1.0f;\n");
        sb.append("        this.red = 0.0f;\n");
        sb.append("        this.green = 0.0f;\n");
        sb.append("        this.blue = 0.0f;\n");
        sb.append("        this.hue = 0.0f;\n");
        sb.append("        this.saturation = 0.0f;\n");
        sb.append("        this.brightness = 0.0f;\n");
        sb.append("        this.isDark = true;\n");
        sb.append("        this.isLight = false;\n");
        sb.append("        this.contrastRatio = 1.0f;\n");
        sb.append("        this.operationCount = 0;\n");

        for (String fieldName : fieldNames) {
            if (!isStaticField(fieldName)) {
                String fieldType = getFieldType(fieldName);
                sb.append("        this.").append(fieldName).append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            }
        }
        sb.append("    }\n\n");

        // 带颜色参数的构造函数
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(className).append("(int color) {\n");
            sb.append("        this();\n");
            sb.append("        setColor(color);\n");
            sb.append("    }\n\n");
        }

        // 带RGB参数的构造函数
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(className).append("(float red, float green, float blue) {\n");
            sb.append("        this();\n");
            sb.append("        setRgb(red, green, blue);\n");
            sb.append("    }\n\n");
        }

        // 带ARGB参数的构造函数
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(className).append("(float alpha, float red, float green, float blue) {\n");
            sb.append("        this();\n");
            sb.append("        setArgb(alpha, red, green, blue);\n");
            sb.append("    }\n\n");
        }

        // 生成设置颜色方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void setColor(int color) {\n");
            sb.append("        this.color = color;\n");
            sb.append("        this.alpha = Color.alpha(color) / 255.0f;\n");
            sb.append("        this.red = Color.red(color) / 255.0f;\n");
            sb.append("        this.green = Color.green(color) / 255.0f;\n");
            sb.append("        this.blue = Color.blue(color) / 255.0f;\n");
            sb.append("        updateHsv();\n");
            sb.append("        updateColorProperties();\n");
            sb.append("        operationCount++;\n");
            sb.append("    }\n\n");
        }

        // 生成获取颜色方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public int getColor() {\n");
            sb.append("        return color;\n");
            sb.append("    }\n\n");
        }

        // 生成设置RGB方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void setRgb(float red, float green, float blue) {\n");
            sb.append("        this.red = Math.max(0.0f, Math.min(1.0f, red));\n");
            sb.append("        this.green = Math.max(0.0f, Math.min(1.0f, green));\n");
            sb.append("        this.blue = Math.max(0.0f, Math.min(1.0f, blue));\n");
            sb.append("        updateColor();\n");
            sb.append("        updateHsv();\n");
            sb.append("        updateColorProperties();\n");
            sb.append("        operationCount++;\n");
            sb.append("    }\n\n");
        }

        // 生成设置ARGB方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void setArgb(float alpha, float red, float green, float blue) {\n");
            sb.append("        this.alpha = Math.max(0.0f, Math.min(1.0f, alpha));\n");
            sb.append("        this.red = Math.max(0.0f, Math.min(1.0f, red));\n");
            sb.append("        this.green = Math.max(0.0f, Math.min(1.0f, green));\n");
            sb.append("        this.blue = Math.max(0.0f, Math.min(1.0f, blue));\n");
            sb.append("        updateColor();\n");
            sb.append("        updateHsv();\n");
            sb.append("        updateColorProperties();\n");
            sb.append("        operationCount++;\n");
            sb.append("    }\n\n");
        }

        // 生成设置HSV方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void setHsv(float hue, float saturation, float value) {\n");
            sb.append("        this.hue = hue;\n");
            sb.append("        this.saturation = saturation;\n");
            sb.append("        this.brightness = value;\n");
            sb.append("        float[] hsv = {hue, saturation, value};\n");
            sb.append("        this.color = Color.HSVToColor((int) (alpha * 255), hsv);\n");
            sb.append("        this.red = Color.red(color) / 255.0f;\n");
            sb.append("        this.green = Color.green(color) / 255.0f;\n");
            sb.append("        this.blue = Color.blue(color) / 255.0f;\n");
            sb.append("        updateColorProperties();\n");
            sb.append("        operationCount++;\n");
            sb.append("    }\n\n");
        }

        // 生成混合颜色方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public int blendColor(int color1, int color2, float ratio) {\n");
            sb.append("        float a1 = Color.alpha(color1) / 255.0f;\n");
            sb.append("        float r1 = Color.red(color1) / 255.0f;\n");
            sb.append("        float g1 = Color.green(color1) / 255.0f;\n");
            sb.append("        float b1 = Color.blue(color1) / 255.0f;\n");
            sb.append("        float a2 = Color.alpha(color2) / 255.0f;\n");
            sb.append("        float r2 = Color.red(color2) / 255.0f;\n");
            sb.append("        float g2 = Color.green(color2) / 255.0f;\n");
            sb.append("        float b2 = Color.blue(color2) / 255.0f;\n");
            sb.append("        float a = a1 * (1 - ratio) + a2 * ratio;\n");
            sb.append("        float r = r1 * (1 - ratio) + r2 * ratio;\n");
            sb.append("        float g = g1 * (1 - ratio) + g2 * ratio;\n");
            sb.append("        float b = b1 * (1 - ratio) + b2 * ratio;\n");
            sb.append("        int blended = Color.argb((int) (a * 255), (int) (r * 255), (int) (g * 255), (int) (b * 255));\n");
            sb.append("        operationCount++;\n");
            sb.append("        return blended;\n");
            sb.append("    }\n\n");
        }

        // 生成反转颜色方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public int invertColor(int color) {\n");
            sb.append("        int a = Color.alpha(color);\n");
            sb.append("        int r = 255 - Color.red(color);\n");
            sb.append("        int g = 255 - Color.green(color);\n");
            sb.append("        int b = 255 - Color.blue(color);\n");
            sb.append("        int inverted = Color.argb(a, r, g, b);\n");
            sb.append("        operationCount++;\n");
            sb.append("        return inverted;\n");
            sb.append("    }\n\n");
        }

        // 生成灰度方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public int toGrayscale(int color) {\n");
            sb.append("        int a = Color.alpha(color);\n");
            sb.append("        int r = Color.red(color);\n");
            sb.append("        int g = Color.green(color);\n");
            sb.append("        int b = Color.blue(color);\n");
            sb.append("        int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);\n");
            sb.append("        int grayscale = Color.argb(a, gray, gray, gray);\n");
            sb.append("        operationCount++;\n");
            sb.append("        return grayscale;\n");
            sb.append("    }\n\n");
        }

        // 生成调整亮度方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public int adjustBrightness(int color, float factor) {\n");
            sb.append("        int a = Color.alpha(color);\n");
            sb.append("        int r = (int) (Color.red(color) * factor);\n");
            sb.append("        int g = (int) (Color.green(color) * factor);\n");
            sb.append("        int b = (int) (Color.blue(color) * factor);\n");
            sb.append("        r = Math.max(0, Math.min(255, r));\n");
            sb.append("        g = Math.max(0, Math.min(255, g));\n");
            sb.append("        b = Math.max(0, Math.min(255, b));\n");
            sb.append("        int adjusted = Color.argb(a, r, g, b);\n");
            sb.append("        operationCount++;\n");
            sb.append("        return adjusted;\n");
            sb.append("    }\n\n");
        }

        // 生成调整饱和度方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public int adjustSaturation(int color, float factor) {\n");
            sb.append("        int a = Color.alpha(color);\n");
            sb.append("        float[] hsv = new float[3];\n");
            sb.append("        Color.colorToHSV(color, hsv);\n");
            sb.append("        hsv[1] = Math.max(0.0f, Math.min(1.0f, hsv[1] * factor));\n");
            sb.append("        int adjusted = Color.HSVToColor(a, hsv);\n");
            sb.append("        operationCount++;\n");
            sb.append("        return adjusted;\n");
            sb.append("    }\n\n");
        }

        // 生成旋转色相方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public int rotateHue(int color, float degrees) {\n");
            sb.append("        int a = Color.alpha(color);\n");
            sb.append("        float[] hsv = new float[3];\n");
            sb.append("        Color.colorToHSV(color, hsv);\n");
            sb.append("        hsv[0] = (hsv[0] + degrees) % 360;\n");
            sb.append("        if (hsv[0] < 0) {\n");
            sb.append("            hsv[0] += 360;\n");
            sb.append("        }\n");
            sb.append("        int rotated = Color.HSVToColor(a, hsv);\n");
            sb.append("        operationCount++;\n");
            sb.append("        return rotated;\n");
            sb.append("    }\n\n");
        }

        // 生成获取对比度方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public float getContrastRatio(int color1, int color2) {\n");
            sb.append("        float luminance1 = calculateLuminance(color1);\n");
            sb.append("        float luminance2 = calculateLuminance(color2);\n");
            sb.append("        float lighter = Math.max(luminance1, luminance2);\n");
            sb.append("        float darker = Math.min(luminance1, luminance2);\n");
            sb.append("        float contrast = (lighter + 0.05f) / (darker + 0.05f);\n");
            sb.append("        return contrast;\n");
            sb.append("    }\n\n");
        }

        // 生成计算亮度方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private float calculateLuminance(int color) {\n");
            sb.append("        float r = Color.red(color) / 255.0f;\n");
            sb.append("        float g = Color.green(color) / 255.0f;\n");
            sb.append("        float b = Color.blue(color) / 255.0f;\n");
            sb.append("        r = r <= 0.03928f ? r / 12.92f : (float) Math.pow((r + 0.055f) / 1.055f, 2.4);\n");
            sb.append("        g = g <= 0.03928f ? g / 12.92f : (float) Math.pow((g + 0.055f) / 1.055f, 2.4);\n");
            sb.append("        b = b <= 0.03928f ? b / 12.92f : (float) Math.pow((b + 0.055f) / 1.055f, 2.4);\n");
            sb.append("        return 0.2126f * r + 0.7152f * g + 0.0722f * b;\n");
            sb.append("    }\n\n");
        }

        // 生成更新颜色方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private void updateColor() {\n");
            sb.append("        this.color = Color.argb(\n");
            sb.append("            (int) (alpha * 255),\n");
            sb.append("            (int) (red * 255),\n");
            sb.append("            (int) (green * 255),\n");
            sb.append("            (int) (blue * 255)\n");
            sb.append("        );\n");
            sb.append("    }\n\n");
        }

        // 生成更新HSV方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private void updateHsv() {\n");
            sb.append("        float[] hsv = new float[3];\n");
            sb.append("        Color.RGBToHSV(\n");
            sb.append("            (int) (red * 255),\n");
            sb.append("            (int) (green * 255),\n");
            sb.append("            (int) (blue * 255),\n");
            sb.append("            hsv\n");
            sb.append("        );\n");
            sb.append("        this.hue = hsv[0];\n");
            sb.append("        this.saturation = hsv[1];\n");
            sb.append("        this.brightness = hsv[2];\n");
            sb.append("    }\n\n");
        }

        // 生成更新颜色属性方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private void updateColorProperties() {\n");
            sb.append("        float luminance = calculateLuminance(color);\n");
            sb.append("        this.isDark = luminance < 0.5f;\n");
            sb.append("        this.isLight = luminance >= 0.5f;\n");
            sb.append("        this.contrastRatio = (luminance + 0.05f) / (0.05f);\n");
            sb.append("    }\n\n");
        }

        // 生成转十六进制字符串方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public String toHexString() {\n");
            sb.append("        return \"#\" + Integer.toHexString(color).substring(2);\n");
            sb.append("    }\n\n");
        }

        // 生成转RGB字符串方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public String toRgbString() {\n");
            sb.append("        return String.format(\"rgb(%d, %d, %d)\",\n");
            sb.append("            (int) (red * 255), (int) (green * 255), (int) (blue * 255));\n");
            sb.append("    }\n\n");
        }

        // 生成转HSV字符串方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public String toHsvString() {\n");
            sb.append("        return String.format(\"hsv(%.2f, %.2f, %.2f)\", hue, saturation, brightness);\n");
            sb.append("    }\n\n");
        }

        // 生成获取颜色过滤器方法（与UI关联）
        if (RandomUtils.randomBoolean()) {
            sb.append("    public PorterDuffColorFilter getColorFilter() {\n");
            sb.append("        return new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);\n");
            sb.append("    }\n\n");
        }

        // 生成获取颜色过滤器方法（带模式）
        if (RandomUtils.randomBoolean()) {
            sb.append("    public PorterDuffColorFilter getColorFilter(PorterDuff.Mode mode) {\n");
            sb.append("        return new PorterDuffColorFilter(color, mode);\n");
            sb.append("    }\n\n");
        }

        // 生成与计算器关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public double calculateColorDistance(int color1, int color2) {\n");
            sb.append("        int r1 = Color.red(color1);\n");
            sb.append("        int g1 = Color.green(color1);\n");
            sb.append("        int b1 = Color.blue(color1);\n");
            sb.append("        int r2 = Color.red(color2);\n");
            sb.append("        int g2 = Color.green(color2);\n");
            sb.append("        int b2 = Color.blue(color2);\n");
            sb.append("        double distance = Math.sqrt(\n");
            sb.append("            Math.pow(r1 - r2, 2) +\n");
            sb.append("            Math.pow(g1 - g2, 2) +\n");
            sb.append("            Math.pow(b1 - b2, 2)\n");
            sb.append("        );\n");
            sb.append("        operationCount++;\n");
            sb.append("        Log.d(TAG, \"Color distance: \" + distance);\n");
            sb.append("        return distance;\n");
            sb.append("    }\n\n");
        }

        // 生成与图表关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public double[] getColorDataPoints() {\n");
            sb.append("        double[] dataPoints = new double[4];\n");
            sb.append("        dataPoints[0] = red * 255;\n");
            sb.append("        dataPoints[1] = green * 255;\n");
            sb.append("        dataPoints[2] = blue * 255;\n");
            sb.append("        dataPoints[3] = alpha * 255;\n");
            sb.append("        Log.d(TAG, \"Generated color data points\");\n");
            sb.append("        return dataPoints;\n");
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

        // 生成与计算器关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public double calculateColorValue() {\n");
            sb.append("        double value = red * 0.299 + green * 0.587 + blue * 0.114;\n");
            sb.append("        value = value * alpha;\n");
            sb.append("        return value;\n");
            sb.append("    }\n\n");
        }

        // 生成与图表关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public double[] getColorDataPoints() {\n");
            sb.append("        double[] dataPoints = new double[4];\n");
            sb.append("        dataPoints[0] = alpha;\n");
            sb.append("        dataPoints[1] = red;\n");
            sb.append("        dataPoints[2] = green;\n");
            sb.append("        dataPoints[3] = blue;\n");
            sb.append("        return dataPoints;\n");
            sb.append("    }\n\n");
        }

        // 生成与集合关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public List<Integer> getColorComponents() {\n");
            sb.append("        List<Integer> components = new ArrayList<>();\n");
            sb.append("        components.add((int) (alpha * 255));\n");
            sb.append("        components.add((int) (red * 255));\n");
            sb.append("        components.add((int) (green * 255));\n");
            sb.append("        components.add((int) (blue * 255));\n");
            sb.append("        return components;\n");
            sb.append("    }\n\n");
        }

        // 生成统计方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public void logStats() {\n");
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

        generateJavaFile(className, sb.toString(), "ui.color");
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
