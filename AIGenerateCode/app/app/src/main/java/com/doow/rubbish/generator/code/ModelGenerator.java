package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class ModelGenerator extends BaseCodeGenerator {
    private static final String[] FIELD_TYPES = {
            "String", "int", "long", "float", "double", "boolean",
            "Integer", "Long", "Float", "Double", "Boolean",
            "Byte", "Short", "Character", "StringBuilder", "StringBuffer"
    };

    protected VariationManager variationManager;

    public ModelGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成15个Model类");

        // 固定生成15个，避免循环次数错误
        for (int i = 0; i < 15; i++) {
            String className = RandomUtils.generateClassName("Model");
            generateModel(className);
        }
    }

    private void generateModel(String className) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 空值防护：给存储类型设置默认值
        String storageType = variationManager.getVariation("storage");
        storageType = storageType == null ? "room" : storageType;

        // 生成包声明
        sb.append(generatePackageDeclaration("model"));

        sb.append(generateImportStatement("java.io.Serializable"));

        // 根据存储类型导入对应注解（空值防护）
        if (storageType != null) {
            if (storageType.contains("room")) {
                sb.append(generateImportStatement("androidx.room.Entity"));
                sb.append(generateImportStatement("androidx.room.PrimaryKey"));
                sb.append(generateImportStatement("androidx.room.ColumnInfo"));
            } else if (storageType.contains("realm")) {
                sb.append(generateImportStatement("io.realm.RealmObject"));
                sb.append(generateImportStatement("io.realm.annotations.PrimaryKey"));
            } else if (storageType.contains("objectbox")) {
                sb.append(generateImportStatement("io.objectbox.annotation.Entity"));
                sb.append(generateImportStatement("io.objectbox.annotation.Id"));
            } else if (storageType.contains("green_dao") || storageType.contains("orm_lite")) {
                sb.append(generateImportStatement("androidx.room.Entity"));
                sb.append(generateImportStatement("androidx.room.PrimaryKey"));
            }
        }

        sb.append(generateImportStatement("android.os.Parcel"));
        sb.append(generateImportStatement("android.os.Parcelable"));
        sb.append("\n"); // 规范空行，替换多余的空白拼接

        // 使用标志变量确保字段和配套方法一起生成和使用
        boolean useParcelable = RandomUtils.randomBoolean();
        boolean useSerializable = RandomUtils.randomBoolean();
        boolean useValidation = RandomUtils.randomBoolean();
        boolean useConversion = RandomUtils.randomBoolean();
        boolean useComparison = RandomUtils.randomBoolean();
        boolean useToString = RandomUtils.randomBoolean();
        boolean useHashCode = RandomUtils.randomBoolean();

        // 类定义（修复缩进和接口实现格式）
        sb.append("public class ").append(className);

        // 处理接口实现（避免多余的逗号）
        boolean hasInterface = false;
        if (useSerializable) {
            sb.append(" implements Serializable");
            hasInterface = true;
        }

        if (useParcelable) {
            if (hasInterface) {
                sb.append(", Parcelable");
            } else {
                sb.append(" implements Parcelable");
            }
        }

        sb.append(" {\n\n");

        // 生成字段（边界防护：5-12个字段）
        int fieldCount = RandomUtils.between(5, 12);
        String[] fieldNames = new String[fieldCount];
        String[] fieldTypes = new String[fieldCount];
        boolean[] hasPrimaryKey = new boolean[fieldCount];

        for (int i = 0; i < fieldCount; i++) {
            // 边界防护：避免数组索引越界
            int typeIndex = RandomUtils.between(0, FIELD_TYPES.length - 1);
            fieldTypes[i] = FIELD_TYPES[typeIndex];
            String name = RandomUtils.generateVariableName(fieldTypes[i]);
            fieldNames[i] = name;

            // 第一个字段作为主键（修复注解参数转义）
            if (i == 0) {
                hasPrimaryKey[i] = true;
                if (storageType.contains("room")) {
                    sb.append("    @PrimaryKey\n");
                    sb.append("    @ColumnInfo(name = \"").append(name).append("\")\n");
                } else if (storageType.contains("realm")) {
                    sb.append("    @PrimaryKey\n");
                } else if (storageType.contains("objectbox")) {
                    sb.append("    @Id\n");
                }
            } else {
                hasPrimaryKey[i] = false;
                if (storageType.contains("room")) {
                    sb.append("    @ColumnInfo(name = \"").append(name).append("\")\n");
                }
            }

            sb.append("    private ").append(fieldTypes[i]).append(" ").append(name);

            // 根据类型设置初始值（修复字符串/字符转义）
            if (fieldTypes[i].equals("String") || fieldTypes[i].equals("StringBuilder") || fieldTypes[i].equals("StringBuffer")) {
                String randomWord = RandomUtils.generateWord(RandomUtils.between(3, 10));
                sb.append(" = \"").append(randomWord).append("\"");
            } else if (fieldTypes[i].equals("boolean") || fieldTypes[i].equals("Boolean")) {
                sb.append(" = ").append(RandomUtils.randomBoolean());
            } else if (fieldTypes[i].equals("int") || fieldTypes[i].equals("Integer") || fieldTypes[i].equals("Byte") || fieldTypes[i].equals("Short")) {
                sb.append(" = ").append(RandomUtils.between(0, 1000));
            } else if (fieldTypes[i].equals("long") || fieldTypes[i].equals("Long")) {
                sb.append(" = ").append(RandomUtils.between(0, 1000000)).append("L");
            } else if (fieldTypes[i].equals("float") || fieldTypes[i].equals("Float")) {
                sb.append(" = ").append(RandomUtils.between(0, 100) / 10.0).append("f");
            } else if (fieldTypes[i].equals("double") || fieldTypes[i].equals("Double")) {
                sb.append(" = ").append(RandomUtils.between(0, 100) / 10.0);
            } else if (fieldTypes[i].equals("Character")) {
                char randomChar = (char)('a' + RandomUtils.between(0, 25));
                sb.append(" = '").append(randomChar).append("'");
            }
            sb.append(";\n");
        }

        sb.append("\n");

        // 空构造方法
        sb.append("    public ").append(className).append("() {\n");
        sb.append("    }\n\n");

        // 全参构造方法
        sb.append("    public ").append(className).append("(");
        for (int i = 0; i < fieldCount; i++) {
            if (i > 0) sb.append(", ");
            sb.append(fieldTypes[i]).append(" ").append(fieldNames[i]);
        }
        sb.append(") {\n");
        for (int i = 0; i < fieldCount; i++) {
            sb.append("        this.").append(fieldNames[i]).append(" = ").append(fieldNames[i]).append(";\n");
        }
        sb.append("    }\n\n");

        // 生成getter和setter方法
        for (int i = 0; i < fieldCount; i++) {
            String fieldName = fieldNames[i];
            String fieldType = fieldTypes[i];
            String capitalized = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            sb.append("    public ").append(fieldType).append(" get").append(capitalized).append("() {\n");
            sb.append("        return ").append(fieldName).append(";\n");
            sb.append("    }\n\n");

            sb.append("    public void set").append(capitalized).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n");
            sb.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            sb.append("    }\n\n");
        }

        // 根据标志变量生成配套方法
        if (useValidation) {
            generateValidationMethods(sb, fieldNames, fieldTypes);
        }

        if (useConversion) {
            generateConversionMethods(sb, className, fieldNames, fieldTypes);
        }

        if (useComparison) {
            generateComparisonMethods(sb, className, fieldNames, fieldTypes);
        }

        if (useToString) {
            generateToStringMethod(sb, className, fieldNames);
        }

        if (useHashCode) {
            generateHashCodeMethod(sb, className, fieldNames,fieldTypes);
        }

        // Parcelable相关方法
        if (useParcelable) {
            generateParcelableMethods(sb, className, fieldNames, fieldTypes);
        }

        // 闭合类
        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "model");
    }

    private void generateValidationMethods(StringBuilder sb, String[] fieldNames, String[] fieldTypes) {
        sb.append("    public boolean isValid() {\n");
        // 空值防护：避免数组为空
        if (fieldNames.length > 0) {
            for (int i = 0; i < fieldNames.length; i++) {
                if (fieldTypes[i].equals("String") || fieldTypes[i].equals("StringBuilder") || fieldTypes[i].equals("StringBuffer")) {
                    sb.append("        if (").append(fieldNames[i]).append(" == null || ").append(fieldNames[i]).append(".isEmpty()) {\n");
                    sb.append("            return false;\n");
                    sb.append("        }\n");
                }
            }
        }
        sb.append("        return true;\n");
        sb.append("    }\n\n");
    }

    private void generateConversionMethods(StringBuilder sb, String className, String[] fieldNames, String[] fieldTypes) {
        sb.append("    public String toJson() {\n");
        sb.append("        StringBuilder json = new StringBuilder();\n");
        sb.append("        json.append(\"{\");\n");
        // 空值防护：避免数组为空
        if (fieldNames.length > 0) {
            for (int i = 0; i < fieldNames.length; i++) {
                if (i > 0) {
                    sb.append("        json.append(\",\");\n");
                }
                // 修复JSON键值对转义
                sb.append("        json.append(\"\\\"").append(fieldNames[i]).append("\\\":\");\n");
                if (fieldTypes[i].equals("String") || fieldTypes[i].equals("StringBuilder") || fieldTypes[i].equals("StringBuffer")) {
                    sb.append("        json.append(\"\\\"\").append(").append(fieldNames[i]).append(").append(\"\\\"\");\n");
                } else {
                    sb.append("        json.append(").append(fieldNames[i]).append(");\n");
                }
            }
        }
        sb.append("        json.append(\"}\");\n");
        sb.append("        return json.toString();\n");
        sb.append("    }\n\n");
    }

    private void generateComparisonMethods(StringBuilder sb, String className, String[] fieldNames, String[] fieldTypes) {
        sb.append("    @Override\n");
        sb.append("    public boolean equals(Object obj) {\n");
        sb.append("        if (this == obj) return true;\n");
        sb.append("        if (obj == null || getClass() != obj.getClass()) return false;\n");
        sb.append("        ").append(className).append(" other = (").append(className).append(") obj;\n");

        // 修复基本类型equals判断（避免调用equals方法）
        if (fieldNames.length > 0) {
            for (int i = 0; i < fieldNames.length; i++) {
                String fieldName = fieldNames[i];
                String fieldType = fieldTypes[i];
                if (fieldType.equals("int") || fieldType.equals("long") || fieldType.equals("float") || fieldType.equals("double") || fieldType.equals("boolean")) {
                    // 基本类型用==比较
                    sb.append("        if (").append(fieldName).append(" != other.").append(fieldName).append(") return false;\n");
                } else {
                    // 引用类型做空值判断后用equals
                    sb.append("        if (").append(fieldName).append(" == null ? other.").append(fieldName).append(" != null : !").append(fieldName).append(".equals(other.").append(fieldName).append(")) return false;\n");
                }
            }
        }
        sb.append("        return true;\n");
        sb.append("    }\n\n");
    }

    private void generateToStringMethod(StringBuilder sb, String className, String[] fieldNames) {
        sb.append("    @Override\n");
        sb.append("    public String toString() {\n");
        // 修复toString拼接转义
        sb.append("        return \"").append(className).append("{\");\n");
        if (fieldNames.length > 0) {
            for (int i = 0; i < fieldNames.length; i++) {
                if (i > 0) {
                    sb.append("        + \", \");\n");
                }
                sb.append("        + \"").append(fieldNames[i]).append("='\" + ").append(fieldNames[i]).append(" + '\\'';\n");
            }
        }
        sb.append("        + '}';\n");
        sb.append("    }\n\n");
    }

    private void generateHashCodeMethod(StringBuilder sb, String className, String[] fieldNames, String[] fieldTypes) {
        sb.append("    @Override\n");
        sb.append("    public int hashCode() {\n");
        sb.append("        int result = 17;\n");
        // 修复基本类型hashCode计算
        if (fieldNames.length > 0) {
            for (int i = 0; i < fieldNames.length; i++) {
                String fieldName = fieldNames[i];
                String fieldType = fieldTypes[i];
                if (fieldType.equals("int")) {
                    sb.append("        result = 31 * result + ").append(fieldName).append(";\n");
                } else if (fieldType.equals("long")) {
                    sb.append("        result = 31 * result + (int) (").append(fieldName).append(" ^ (").append(fieldName).append(">>> 32));\n");
                } else if (fieldType.equals("float")) {
                    sb.append("        result = 31 * result + Float.floatToIntBits(").append(fieldName).append(");\n");
                } else if (fieldType.equals("double")) {
                    sb.append("        long temp = Double.doubleToLongBits(").append(fieldName).append(");\n");
                    sb.append("        result = 31 * result + (int) (temp ^ (temp >>> 32));\n");
                } else if (fieldType.equals("boolean")) {
                    sb.append("        result = 31 * result + (").append(fieldName).append(" ? 1 : 0);\n");
                } else {
                    // 引用类型做空值判断
                    sb.append("        result = 31 * result + (").append(fieldName).append(" != null ? ").append(fieldName).append(".hashCode() : 0);\n");
                }
            }
        }
        sb.append("        return result;\n");
        sb.append("    }\n\n");
    }

    private void generateParcelableMethods(StringBuilder sb, String className, String[] fieldNames, String[] fieldTypes) {
        sb.append("    @Override\n");
        sb.append("    public int describeContents() {\n");
        sb.append("        return 0;\n");
        sb.append("    }\n\n");

        sb.append("    @Override\n");
        sb.append("    public void writeToParcel(Parcel dest, int flags) {\n");
        // 支持基本类型+包装类型
        if (fieldNames.length > 0) {
            for (int i = 0; i < fieldNames.length; i++) {
                String fieldName = fieldNames[i];
                String fieldType = fieldTypes[i];
                switch (fieldType) {
                    case "String":
                        sb.append("        dest.writeString(").append(fieldName).append(");\n");
                        break;
                    case "boolean":
                    case "Boolean":
                        sb.append("        dest.writeByte((byte) (").append(fieldName).append(" ? 1 : 0));\n");
                        break;
                    case "int":
                    case "Integer":
                        sb.append("        dest.writeInt(").append(fieldName).append(");\n");
                        break;
                    case "long":
                    case "Long":
                        sb.append("        dest.writeLong(").append(fieldName).append(");\n");
                        break;
                    case "float":
                    case "Float":
                        sb.append("        dest.writeFloat(").append(fieldName).append(");\n");
                        break;
                    case "double":
                    case "Double":
                        sb.append("        dest.writeDouble(").append(fieldName).append(");\n");
                        break;
                    case "Byte":
                        sb.append("        dest.writeByte(").append(fieldName).append(");\n");
                        break;
                    case "Short":
                        sb.append("        dest.writeShort(").append(fieldName).append(");\n");
                        break;
                    case "Character":
                        sb.append("        dest.writeChar(").append(fieldName).append(");\n");
                        break;
                }
            }
        }
        sb.append("    }\n\n");

        // Parcelable Creator（修复泛型格式）
        sb.append("    public static final Creator<").append(className).append("> CREATOR = new Creator<").append(className).append(">() {\n");
        sb.append("        @Override\n");
        sb.append("        public ").append(className).append(" createFromParcel(Parcel in) {\n");
        sb.append("            return new ").append(className).append("(in);\n");
        sb.append("        }\n\n");

        sb.append("        @Override\n");
        sb.append("        public ").append(className).append("[] newArray(int size) {\n");
        sb.append("            return new ").append(className).append("[size];\n");
        sb.append("        }\n");
        sb.append("    };\n\n");

        // 补充Parcel构造方法（修复Parcelable规范）
        sb.append("    protected ").append(className).append("(Parcel in) {\n");
        if (fieldNames.length > 0) {
            for (int i = 0; i < fieldNames.length; i++) {
                String fieldName = fieldNames[i];
                String fieldType = fieldTypes[i];
                switch (fieldType) {
                    case "String":
                        sb.append("        ").append(fieldName).append(" = in.readString();\n");
                        break;
                    case "boolean":
                    case "Boolean":
                        sb.append("        ").append(fieldName).append(" = in.readByte() != 0;\n");
                        break;
                    case "int":
                    case "Integer":
                        sb.append("        ").append(fieldName).append(" = in.readInt();\n");
                        break;
                    case "long":
                    case "Long":
                        sb.append("        ").append(fieldName).append(" = in.readLong();\n");
                        break;
                    case "float":
                    case "Float":
                        sb.append("        ").append(fieldName).append(" = in.readFloat();\n");
                        break;
                    case "double":
                    case "Double":
                        sb.append("        ").append(fieldName).append(" = in.readDouble();\n");
                        break;
                    case "Byte":
                        sb.append("        ").append(fieldName).append(" = in.readByte();\n");
                        break;
                    case "Short":
                        sb.append("        ").append(fieldName).append(" = in.readShort();\n");
                        break;
                    case "Character":
                        sb.append("        ").append(fieldName).append(" = in.readChar();\n");
                        break;
                }
            }
        }
        sb.append("    }\n\n");
    }
}