package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class EnumGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] ENUM_TYPES = {
            "Status", "Type", "Category", "Priority", "Level",
            "Mode", "State", "Action", "Result", "Error",
            "Direction", "Position", "Orientation", "Alignment", "Size"
    };

    private static final String[] VALUE_TYPES = {
            "ACTIVE", "INACTIVE", "PENDING", "COMPLETED", "CANCELLED",
            "SUCCESS", "FAILURE", "ERROR", "WARNING", "INFO",
            "DEBUG", "VERBOSE", "TRACE", "NONE", "UNKNOWN"
    };

    private static final String[] PROPERTY_TYPES = {
            "code", "id", "index", "priority", "weight",
            "color", "icon", "label", "description", "metadata"
    };

    private static final String[] VALIDATION_TYPES = {
            "isValid", "isEnabled", "isVisible", "isEditable", "isSelectable"
    };

    public EnumGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成枚举类");

        String uiStyle = variationManager.getVariation("ui_style");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Enum");
            generateEnumClass(className, uiStyle);
        }
    }

    private void generateEnumClass(String className, String uiStyle) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("model"));

        // 基础导入
        sb.append(generateImportStatement("android.os.Parcelable"));
        sb.append(generateImportStatement("android.os.Parcel"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 功能标志 - 确保所有字段和方法都会被使用
        boolean useCode = RandomUtils.randomBoolean();
        boolean usePriority = RandomUtils.randomBoolean();
        boolean useColor = RandomUtils.randomBoolean();
        boolean useIcon = RandomUtils.randomBoolean();
        boolean useDescription = RandomUtils.randomBoolean();
        boolean useMetadata = RandomUtils.randomBoolean();
        boolean useValidation = RandomUtils.randomBoolean();
        boolean useCache = RandomUtils.randomBoolean();
        boolean useSerialization = RandomUtils.randomBoolean();

        sb.append("public enum ").append(className).append(" implements Parcelable {\n\n");

        String enumType = ENUM_TYPES[RandomUtils.between(0, ENUM_TYPES.length - 1)];
        int valueCount = RandomUtils.between(3, 8);

        // 生成枚举常量
        for (int i = 0; i < valueCount; i++) {
            String valueType = VALUE_TYPES[i % VALUE_TYPES.length];
            sb.append("    ").append(valueType).append("(");

            // 构造函数参数
            boolean firstParam = true;
            if (useCode) {
                if (!firstParam) sb.append(", ");
                sb.append(i + 1);
                firstParam = false;
            }
            if (usePriority) {
                if (!firstParam) sb.append(", ");
                sb.append(RandomUtils.between(0, 10));
                firstParam = false;
            }
            if (useColor) {
                if (!firstParam) sb.append(", ");
                sb.append("\"").append(RandomUtils.generateColor()).append("\"");
                firstParam = false;
            }
            if (useIcon) {
                if (!firstParam) sb.append(", ");
                sb.append("\"").append(RandomUtils.generateWord(8)).append("\"");
                firstParam = false;
            }
            if (useDescription) {
                if (!firstParam) sb.append(", ");
                sb.append("\"").append(RandomUtils.generateWord(10)).append("\"");
                firstParam = false;
            }
            if (useMetadata) {
                if (!firstParam) sb.append(", ");
                sb.append("createMetadata()");
                firstParam = false;
            }

            sb.append("),\n");
        }

        sb.append("\n");

        // 生成字段
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String ENUM_TYPE = \"").append(enumType).append("\";\n");
        sb.append("    private final String value;\n");

        if (useCode) {
            sb.append("    private final int code;\n");
        }

        if (usePriority) {
            sb.append("    private final int priority;\n");
        }

        if (useColor) {
            sb.append("    private final String color;\n");
        }

        if (useIcon) {
            sb.append("    private final String icon;\n");
        }

        if (useDescription) {
            sb.append("    private final String description;\n");
        }

        if (useMetadata) {
            sb.append("    private final Map<String, Object> metadata;\n");
        }

        if (useCache) {
            sb.append("    private static final Map<String, ").append(className).append("> cache = new HashMap<>();\n");
        }

        sb.append("\n");

        // 构造函数
        sb.append("    ").append(className).append("(String value");

        if (useCode) {
            sb.append(", int code");
        }
        if (usePriority) {
            sb.append(", int priority");
        }
        if (useColor) {
            sb.append(", String color");
        }
        if (useIcon) {
            sb.append(", String icon");
        }
        if (useDescription) {
            sb.append(", String description");
        }
        if (useMetadata) {
            sb.append(", Map<String, Object> metadata");
        }

        sb.append(") {\n");
        sb.append("        this.value = value;\n");

        if (useCode) {
            sb.append("        this.code = code;\n");
        }
        if (usePriority) {
            sb.append("        this.priority = priority;\n");
        }
        if (useColor) {
            sb.append("        this.color = color;\n");
        }
        if (useIcon) {
            sb.append("        this.icon = icon;\n");
        }
        if (useDescription) {
            sb.append("        this.description = description;\n");
        }
        if (useMetadata) {
            sb.append("        this.metadata = metadata;\n");
        }

        sb.append("        Log.d(TAG, \"Created \"+").append("ENUM_TYPE").append("+\":  + \"+value);\n");
        sb.append("    }\n\n");

        // 辅助方法
        if (useMetadata) {
            sb.append("    private static Map<String, Object> createMetadata() {\n");
            sb.append("        Map<String, Object> metadata = new HashMap<>();\n");
            sb.append("        metadata.put(\"timestamp\", System.currentTimeMillis());\n");
            sb.append("        metadata.put(\"version\", 1);\n");
            sb.append("        return metadata;\n");
            sb.append("    }\n\n");
        }

        // Getter方法
        sb.append("    public String getValue() {\n");
        sb.append("        return value;\n");
        sb.append("    }\n\n");

        if (useCode) {
            sb.append("    public int getCode() {\n");
            sb.append("        return code;\n");
            sb.append("    }\n\n");
        }

        if (usePriority) {
            sb.append("    public int getPriority() {\n");
            sb.append("        return priority;\n");
            sb.append("    }\n\n");
        }

        if (useColor) {
            sb.append("    public String getColor() {\n");
            sb.append("        return color;\n");
            sb.append("    }\n\n");
        }

        if (useIcon) {
            sb.append("    public String getIcon() {\n");
            sb.append("        return icon;\n");
            sb.append("    }\n\n");
        }

        if (useDescription) {
            sb.append("    public String getDescription() {\n");
            sb.append("        return description;\n");
            sb.append("    }\n\n");
        }

        if (useMetadata) {
            sb.append("    public Map<String, Object> getMetadata() {\n");
            sb.append("        return metadata;\n");
            sb.append("    }\n\n");
        }

        // 验证方法
        if (useValidation) {
            sb.append("    public boolean isValid() {\n");
            sb.append("        boolean valid = value != null && !value.isEmpty();\n");

            if (useCode) {
                sb.append("        valid = valid && code > 0;\n");
            }

            if (usePriority) {
                sb.append("        valid = valid && priority >= 0 && priority <= 10;\n");
            }

            if (useColor) {
                sb.append("        valid = valid && color != null && color.startsWith(\"#\");\n");
            }

            sb.append("        return valid;\n");
            sb.append("    }\n\n");

            sb.append("    public boolean isEnabled() {\n");
            sb.append("        return isValid() && ");

            if (usePriority) {
                sb.append("priority > 0");
            } else {
                sb.append("true");
            }

            sb.append(";\n");
            sb.append("    }\n\n");
        }

        // 缓存方法
        if (useCache) {
            sb.append("    public ").append(className).append(" cache() {\n");
            sb.append("        cache.put(value, this);\n");
            sb.append("        return this;\n");
            sb.append("    }\n\n");

            sb.append("    public static ").append(className).append(" fromCache(String value) {\n");
            sb.append("        return cache.get(value);\n");
            sb.append("    }\n\n");

            sb.append("    public static void clearCache() {\n");
            sb.append("        cache.clear();\n");
            sb.append("    }\n\n");
        }

        // Parcelable实现
        sb.append("    @Override\n");
        sb.append("    public int describeContents() {\n");
        sb.append("        return 0;\n");
        sb.append("    }\n\n");

        sb.append("    @Override\n");
        sb.append("    public void writeToParcel(Parcel dest, int flags) {\n");
        sb.append("        dest.writeString(value);\n");

        if (useCode) {
            sb.append("        dest.writeInt(code);\n");
        }
        if (usePriority) {
            sb.append("        dest.writeInt(priority);\n");
        }
        if (useColor) {
            sb.append("        dest.writeString(color);\n");
        }
        if (useIcon) {
            sb.append("        dest.writeString(icon);\n");
        }
        if (useDescription) {
            sb.append("        dest.writeString(description);\n");
        }
        if (useMetadata) {
            sb.append("        dest.writeInt(metadata.size());\n");
            sb.append("        for (Map.Entry<String, Object> entry : metadata.entrySet()) {\n");
            sb.append("            dest.writeString(entry.getKey());\n");
            sb.append("            dest.writeString(entry.getValue() != null ? entry.getValue().toString() : null);\n");
            sb.append("        }\n");
        }

        sb.append("    }\n\n");

        sb.append("    public static final Creator<").append(className).append("> CREATOR = new Creator<").append(className).append(">() {\n");
        sb.append("        @Override\n");
        sb.append("        public ").append(className).append(" createFromParcel(Parcel in) {\n");
        sb.append("            String value = in.readString();\n");

        if (useCode) {
            sb.append("            int code = in.readInt();\n");
        }
        if (usePriority) {
            sb.append("            int priority = in.readInt();\n");
        }
        if (useColor) {
            sb.append("            String color = in.readString();\n");
        }
        if (useIcon) {
            sb.append("            String icon = in.readString();\n");
        }
        if (useDescription) {
            sb.append("            String description = in.readString();\n");
        }
        if (useMetadata) {
            sb.append("            Map<String, Object> metadata = new HashMap<>();\n");
            sb.append("            int size = in.readInt();\n");
            sb.append("            for (int i = 0; i < size; i++) {\n");
            sb.append("                String key = in.readString();\n");
            sb.append("                String val = in.readString();\n");
            sb.append("                metadata.put(key, val);\n");
            sb.append("            }\n");
        }

        sb.append("            return ").append(className).append(".valueOf(value);\n");
        sb.append("        }\n\n");

        sb.append("        @Override\n");
        sb.append("        public ").append(className).append("[] newArray(int size) {\n");
        sb.append("            return new ").append(className).append("[size];\n");
        sb.append("        }\n");
        sb.append("    };\n\n");

        // valueOf方法
        sb.append("    public static ").append(className).append(" valueOf(String value) {\n");
        sb.append("        if (useCache) {\n");
        sb.append("            ").append(className).append(" cached = fromCache(value);\n");
        sb.append("            if (cached != null) {\n");
        sb.append("                return cached;\n");
        sb.append("            }\n");
        sb.append("        }\n");

        sb.append("        for (").append(className).append(" item : values()) {\n");
        sb.append("            if (item.value.equals(value)) {\n");

        if (useCache) {
            sb.append("                item.cache();\n");
        }

        sb.append("                return item;\n");
        sb.append("            }\n");
        sb.append("        }\n");

        sb.append("        throw new IllegalArgumentException(\"Unknown \"+").append("ENUM_TYPE").append("+\" value: \" + value);\n");
        sb.append("    }\n\n");

        // toString方法
        sb.append("    @Override\n");
        sb.append("    public String toString() {\n");
        sb.append("        StringBuilder sb = new StringBuilder();\n");
        sb.append("        sb.append(\"").append(className).append("{\");\n");
        sb.append("        sb.append(\"value='\").append(value).append(\"'\");\n");

        if (useCode) {
            sb.append("        sb.append(\", code=\").append(code);\n");
        }
        if (usePriority) {
            sb.append("        sb.append(\", priority=\").append(priority);\n");
        }
        if (useColor) {
            sb.append("        sb.append(\", color='\").append(color).append(\"'\");\n");
        }
        if (useIcon) {
            sb.append("        sb.append(\", icon='\").append(icon).append(\"'\");\n");
        }
        if (useDescription) {
            sb.append("        sb.append(\", description='\").append(description).append(\"'\");\n");
        }
        if (useMetadata) {
            sb.append("        sb.append(\", metadata=\").append(metadata);\n");
        }

        sb.append("        sb.append(\"'}');\n");
        sb.append("        return sb.toString();\n");
        sb.append("    }\n");

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "model");
    }
}
