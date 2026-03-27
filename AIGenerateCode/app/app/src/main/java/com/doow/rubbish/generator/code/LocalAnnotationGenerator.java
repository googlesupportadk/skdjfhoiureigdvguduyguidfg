package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalAnnotationGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] ANNOTATION_TYPES = {
            "Retention", "Target", "Documented", "Inherited", "Repeatable",
            "Native", "SuppressWarnings", "Deprecated", "Override", "SafeVarargs",
            "FunctionalInterface"
    };

    private static final String[] TARGET_TYPES = {
            "ElementType.TYPE", "ElementType.FIELD", "ElementType.METHOD", "ElementType.PARAMETER", "ElementType.CONSTRUCTOR",
            "ElementType.LOCAL_VARIABLE", "ElementType.ANNOTATION_TYPE", "ElementType.PACKAGE", "ElementType.TYPE_USE", "ElementType.TYPE_PARAMETER"
    };

    private static final String[] RETENTION_TYPES = {
            "RetentionPolicy.SOURCE", "RetentionPolicy.CLASS", "RetentionPolicy.RUNTIME"
    };

    private static final String[] DATA_TYPES = {
            "String", "int", "long", "boolean", "float", "double",
            "byte", "short", "char", "String[]", "int[]", "Class<?>"
    };

    private static final String[] PROPERTY_NAMES = {
            "id", "name", "value", "priority", "enabled", "required",
            "description", "version", "category", "tags", "metadata"
    };

    public LocalAnnotationGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成注解类");

        String uiStyle = variationManager.getVariation("ui_style");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Annotation");
            generateAnnotationClass(className, uiStyle);
        }
    }

    private void generateAnnotationClass(String className, String uiStyle) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("annotation"));

        // 功能标志 - 确保所有字段和方法都会被使用
        boolean useDocumented = RandomUtils.randomBoolean();
        boolean useInherited = RandomUtils.randomBoolean();
        boolean useRepeatable = RandomUtils.randomBoolean();
        boolean useMultipleTargets = RandomUtils.randomBoolean();
        boolean useCustomDefaults = RandomUtils.randomBoolean();
        boolean useNestedAnnotation = RandomUtils.randomBoolean();
        boolean useValidation = RandomUtils.randomBoolean();

        // 基础导入
        sb.append(generateImportStatement("java.lang.annotation.ElementType"));
        sb.append(generateImportStatement("java.lang.annotation.Retention"));
        sb.append(generateImportStatement("java.lang.annotation.RetentionPolicy"));
        sb.append(generateImportStatement("java.lang.annotation.Target"));

        // 条件导入
        if (useDocumented) {
            sb.append(generateImportStatement("java.lang.annotation.Documented"));
        }
        if (useInherited) {
            sb.append(generateImportStatement("java.lang.annotation.Inherited"));
        }
        if (useRepeatable) {
            sb.append(generateImportStatement("java.lang.annotation.Repeatable"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 生成嵌套注解
        String nestedAnnotationName = "";
        if (useNestedAnnotation) {
            nestedAnnotationName = className + "Config";
            generateNestedAnnotation(sb, nestedAnnotationName, useDocumented, useInherited);
        }

        // 生成目标类型
        String targetType = TARGET_TYPES[RandomUtils.between(0, TARGET_TYPES.length - 1)];
        String retentionType = RETENTION_TYPES[RandomUtils.between(0, RETENTION_TYPES.length - 1)];

        // 添加注解元注解
        if (useDocumented) {
            sb.append("@Documented\n");
        }
        if (useInherited) {
            sb.append("@Inherited\n");
        }
        if (useRepeatable) {
            sb.append("@Repeatable(").append(className).append(".class)\n");
        }

        if (useMultipleTargets) {
            sb.append("@Target({\n");
            int targetCount = RandomUtils.between(2, 4);
            for (int i = 0; i < targetCount; i++) {
                String t = TARGET_TYPES[RandomUtils.between(0, TARGET_TYPES.length - 1)];
                sb.append("    ").append(t);
                if (i < targetCount - 1) {
                    sb.append(",\n");
                } else {
                    sb.append("\n");
                }
            }
            sb.append("})\n");
        } else {
            sb.append("@Target({").append(targetType).append("})\n");
        }

        sb.append("@Retention(").append(retentionType).append(")\n");
        sb.append("public @interface ").append(className).append(" {\n\n");

        // 生成注解属性
        int propertyCount = RandomUtils.between(3, 8);
        for (int i = 0; i < propertyCount; i++) {
            String dataType = DATA_TYPES[RandomUtils.between(0, DATA_TYPES.length - 1)];
            String propertyName = PROPERTY_NAMES[RandomUtils.between(0, PROPERTY_NAMES.length - 1)];

            sb.append("    ").append(dataType).append(" ").append(propertyName).append("()");

            if (useCustomDefaults) {
                sb.append(" default ").append(generateDefaultValue(dataType));
            }

            sb.append(";\n");
        }

        sb.append("\n");

        // 添加常量属性
        sb.append("    String ANNOTATION_TYPE = \"").append(className).append("\";\n");
        sb.append("    String TARGET_TYPE = \"").append(targetType).append("\";\n");
        sb.append("    String RETENTION_TYPE = \"").append(retentionType).append("\";\n");

        if (useNestedAnnotation) {
            sb.append("    Class<?> CONFIG_TYPE = ").append(nestedAnnotationName).append(".class;\n");
        }

        sb.append("\n");

        // 添加验证属性（在生成的注解处理器中使用）
        if (useValidation) {
            sb.append("    boolean validate() default true;\n");
            sb.append("    String validationMessage() default \"\";\n");
            sb.append("    Class<?>[] validatorClasses() default {};\n");
            sb.append("\n");
        }

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "annotation");
    }

    private void generateNestedAnnotation(StringBuilder sb, String annotationName,
                                          boolean useDocumented, boolean useInherited) {
        sb.append("\n");

        if (useDocumented) {
            sb.append("@Documented\n");
        }
        if (useInherited) {
            sb.append("@Inherited\n");
        }

        sb.append("@Target({ElementType.TYPE, ElementType.METHOD})\n");
        sb.append("@Retention(RetentionPolicy.RUNTIME)\n");
        sb.append("public @interface ").append(annotationName).append(" {\n");
        sb.append("    String value() default \"\";\n");
        sb.append("    int priority() default 0;\n");
        sb.append("    boolean enabled() default true;\n");
        sb.append("}\n\n");
    }

    private String generateDefaultValue(String dataType) {
        switch (dataType) {
            case "String":
                return "\"" + RandomUtils.generateWord(8) + "\"";
            case "int":
                return String.valueOf(RandomUtils.between(0, 100));
            case "long":
                return String.valueOf(RandomUtils.between(0, 1000)) + "L";
            case "boolean":
                return String.valueOf(RandomUtils.randomBoolean());
            case "float":
                return String.format("%.2f", RandomUtils.nextDouble(0.0, 100.0)) + "f";
            case "double":
                return String.format("%.2f", RandomUtils.nextDouble(0.0, 100.0));
            case "byte":
                return String.valueOf((byte)RandomUtils.between(0, 127));
            case "short":
                return String.valueOf((short)RandomUtils.between(0, 32767));
            case "char":
                return "'" + RandomUtils.generateWord(1).charAt(0) + "'";
            case "String[]":
                return "{\"" + RandomUtils.generateWord(5) + "\", \"" + RandomUtils.generateWord(5) + "\"}";
            case "int[]":
                return "{" + RandomUtils.between(0, 10) + ", " + RandomUtils.between(0, 10) + "}";
            case "Class<?>":
                return "Object.class";
            default:
                return "null";
        }
    }
}
