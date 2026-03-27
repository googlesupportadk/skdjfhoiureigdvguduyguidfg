package com.doow.rubbish.generator;


public class CodeTemplateUtils {


    public static String generatePackageDeclaration(String packageName) {
        return "package " + packageName + ";\n\n";
    }


    public static String generateImportStatement(String importPath) {
        return "import " + importPath + ";\n";
    }


    public static String generateClassDeclaration(String className, String superClass, String... interfaces) {
        StringBuilder sb = new StringBuilder();
        sb.append("public class ").append(className);
        if (superClass != null && !superClass.isEmpty()) {
            sb.append(" extends ").append(superClass);
        }
        if (interfaces != null && interfaces.length > 0) {
            sb.append(" implements ");
            for (int i = 0; i < interfaces.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(interfaces[i]);
            }
        }
        sb.append(" {\n");
        return sb.toString();
    }


    public static String generateMethodDeclaration(String returnType, String methodName, String... parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("    public ").append(returnType).append(" ").append(methodName).append("(");
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(parameters[i]);
        }
        sb.append(") {\n");
        return sb.toString();
    }


    public static String generateFieldDeclaration(String type, String name, String defaultValue) {
        StringBuilder sb = new StringBuilder();
        sb.append("    private ").append(type).append(" ").append(name);
        if (defaultValue != null && !defaultValue.isEmpty()) {
            sb.append(" = ").append(defaultValue);
        }
        sb.append(";\n");
        return sb.toString();
    }

    // 生成构造函数
    public static String generateConstructor(String className, String... parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("    public ").append(className).append("(");
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(parameters[i]);
        }
        sb.append(") {\n");
        return sb.toString();
    }

    // 生成getter方法
    public static String generateGetter(String type, String fieldName) {
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return "    public " + type + " " + methodName + "() {\n" +
               "        return " + fieldName + ";\n" +
               "    }\n";
    }

    // 生成setter方法
    public static String generateSetter(String type, String fieldName) {
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return "    public void " + methodName + "(" + type + " " + fieldName + ") {\n" +
               "        this." + fieldName + " = " + fieldName + ";\n" +
               "    }\n";
    }

    // 生成@Override注解
    public static String generateOverrideAnnotation() {
        return "    @Override\n";
    }

    // 生成空行
    public static String generateEmptyLine() {
        return "\n";
    }

    // 生成注释
    public static String generateComment(String comment) {
        return "    // " + comment + "\n";
    }

    // 生成JavaDoc注释
    public static String generateJavaDoc(String comment) {
        return "    /**\n" +
               "     * " + comment + "\n" +
               "     */\n";
    }

    // 生成代码块结束
    public static String generateBlockEnd() {
        return "    }\n";
    }

    // 生成类结束
    public static String generateClassEnd() {
        return "}\n";
    }
}
