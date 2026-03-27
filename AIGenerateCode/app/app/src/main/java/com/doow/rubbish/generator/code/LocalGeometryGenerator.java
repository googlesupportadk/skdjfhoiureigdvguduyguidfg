package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalGeometryGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] SHAPE_TYPES = {
            "Point", "Line", "Circle", "Rectangle", "Triangle", "Polygon",
            "Square", "Ellipse", "Parallelogram", "Trapezoid", "Rhombus",
            "Kite", "Sector", "Segment", "Ray", "Vector", "Plane"
    };

    private static final String[] OPERATION_TYPES = {
            "area", "perimeter", "distance", "intersect", "contains",
            "circumference", "diameter", "radius", "angle", "slope",
            "midpoint", "bisector", "tangent", "normal", "projection", "rotation"
    };

    private static final String[] FIELD_PREFIXES = {
            "coord", "dim", "val", "pos", "size", "dist", "angle", "ratio", "scale", "offset"
    };

    private static final String[] METHOD_PREFIXES = {
            "calc", "compute", "determine", "evaluate", "measure", "assess", "estimate", "derive"
    };

    public LocalGeometryGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成几何类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < 5; i++) {
            String className = RandomUtils.generateClassName("Geometry");
            generateGeometryClass(className, asyncHandler);
        }
    }

    private void generateGeometryClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明和导入
        sb.append(generatePackageDeclaration("geometry"));
        sb.append(generateImportStatement("android.util.Log"));

        // 根据异步处理类型添加导入
        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        } else if (asyncHandler.contains("rxjava")) {
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 随机选择形状和操作类型
        String shapeType = SHAPE_TYPES[RandomUtils.between(0, SHAPE_TYPES.length - 1)];
        String operationType = OPERATION_TYPES[RandomUtils.between(0, OPERATION_TYPES.length - 1)];

        // 生成类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成常量
        String tagVar = RandomUtils.generateWord(6);
        String shapeTypeVar = RandomUtils.generateWord(6);
        String operationTypeVar = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVar).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(shapeTypeVar).append(" = \"").append(shapeType).append("\";\n");
        sb.append("    private static final String ").append(operationTypeVar).append(" = \"").append(operationType).append("\";\n\n");

        // 随机生成字段数量 (3-6个)
        int fieldCount = RandomUtils.between(3, 6);
        String[] fieldNames = new String[fieldCount];

        for (int i = 0; i < fieldCount; i++) {
            String prefix = FIELD_PREFIXES[RandomUtils.between(0, FIELD_PREFIXES.length - 1)];
            fieldNames[i] = prefix + RandomUtils.generateWord(5);
            sb.append("    private float ").append(fieldNames[i]).append(";\n");
        }
        sb.append("\n");

        // 生成构造函数
        sb.append("    public ").append(className).append("() {\n");
        for (int i = 0; i < fieldCount; i++) {
            sb.append("        this.").append(fieldNames[i]).append(" = 0;\n");
        }
        sb.append("    }\n\n");

        // 生成参数化构造函数 (50%概率)
        if (RandomUtils.randomBoolean()) {
            sb.append("    public ").append(className).append("(");
            for (int i = 0; i < fieldCount; i++) {
                sb.append("float ").append(fieldNames[i]);
                if (i < fieldCount - 1) sb.append(", ");
            }
            sb.append(") {\n");
            for (int i = 0; i < fieldCount; i++) {
                sb.append("        this.").append(fieldNames[i]).append(" = ").append(fieldNames[i]).append(";\n");
            }
            sb.append("    }\n\n");
        }

        // 生成主要方法
        String mainMethodPrefix = METHOD_PREFIXES[RandomUtils.between(0, METHOD_PREFIXES.length - 1)];
        String mainMethodName = mainMethodPrefix + RandomUtils.capitalize(operationType);
        String resultVar = RandomUtils.generateWord(6);

        sb.append("    public float ").append(mainMethodName).append("() {\n");
        sb.append("        float ").append(resultVar).append(" = 0;\n");
        sb.append("        switch (").append(operationTypeVar).append(") {\n");

        // 为每个操作类型生成case
        for (String op : OPERATION_TYPES) {
            sb.append("            case \"").append(op).append("\":\n");
            String opMethod = RandomUtils.generateWord(6) + RandomUtils.capitalize(op);
            sb.append("                ").append(resultVar).append(" = ").append(opMethod).append("();\n");
            sb.append("                break;\n");
        }

        sb.append("        }\n");

        // 随机添加日志 (30%概率)
        if (RandomUtils.randomBoolean() && RandomUtils.randomBoolean() && RandomUtils.randomBoolean()) {
            sb.append("        Log.d(").append(tagVar).append(", \"").append(operationType).append(": \" + ").append(resultVar).append(");\n");
        }

        sb.append("        return ").append(resultVar).append(";\n");
        sb.append("    }\n\n");

        // 生成操作方法
        for (String op : OPERATION_TYPES) {
            String opMethod = RandomUtils.generateWord(6) + RandomUtils.capitalize(op);
            sb.append("    private float ").append(opMethod).append("() {\n");

            if (op.equals("area")) {
                sb.append(generateAreaMethod(shapeType, fieldNames));
            } else if (op.equals("perimeter")) {
                sb.append(generatePerimeterMethod(shapeType, fieldNames));
            } else if (op.equals("distance")) {
                sb.append(generateDistanceMethod(fieldNames));
            } else if (op.equals("intersect") || op.equals("contains")) {
                sb.append(generateBooleanMethod(op, fieldNames));
            } else {
                sb.append(generateGenericMethod(op, fieldNames));
            }

            sb.append("    }\n\n");
        }

        // 随机添加setter方法 (50%概率)
        if (RandomUtils.randomBoolean()) {
            int setterCount = RandomUtils.between(1, fieldCount);
            for (int i = 0; i < setterCount; i++) {
                int fieldIndex = RandomUtils.between(0, fieldCount - 1);
                String setterName = "set" + RandomUtils.capitalize(fieldNames[fieldIndex]);
                sb.append("    public void ").append(setterName).append("(float value) {\n");
                sb.append("        this.").append(fieldNames[fieldIndex]).append(" = value;\n");
                sb.append("    }\n\n");
            }
        }

        // 随机添加getter方法 (50%概率)
        if (RandomUtils.randomBoolean()) {
            int getterCount = RandomUtils.between(1, fieldCount);
            for (int i = 0; i < getterCount; i++) {
                int fieldIndex = RandomUtils.between(0, fieldCount - 1);
                String getterName = "get" + RandomUtils.capitalize(fieldNames[fieldIndex]);
                sb.append("    public float ").append(getterName).append("() {\n");
                sb.append("        return this.").append(fieldNames[fieldIndex]).append(";\n");
                sb.append("    }\n\n");
            }
        }

        // 随机添加验证方法 (40%概率)
        if (RandomUtils.randomBoolean() && RandomUtils.randomBoolean()) {
            String validateMethod = METHOD_PREFIXES[RandomUtils.between(0, METHOD_PREFIXES.length - 1)] + "Validity";
            sb.append("    public boolean ").append(validateMethod).append("() {\n");
            sb.append("        boolean isValid = true;\n");

            for (int i = 0; i < fieldCount; i++) {
                sb.append("        if (").append(fieldNames[i]).append(" < 0) {\n");
                sb.append("            isValid = false;\n");
                sb.append("        }\n");
            }

            sb.append("        return isValid;\n");
            sb.append("    }\n\n");
        }

        // 随机添加转换方法 (30%概率)
        if (RandomUtils.randomBoolean() && RandomUtils.randomBoolean() && RandomUtils.randomBoolean()) {
            String convertMethod = METHOD_PREFIXES[RandomUtils.between(0, METHOD_PREFIXES.length - 1)] + "To" + RandomUtils.capitalize(SHAPE_TYPES[RandomUtils.between(0, SHAPE_TYPES.length - 1)]);
            sb.append("    public String ").append(convertMethod).append("() {\n");
            sb.append("        StringBuilder sb = new StringBuilder();\n");
            sb.append("        sb.append(\"{\");\n");

            for (int i = 0; i < fieldCount; i++) {
                sb.append("        sb.append(\"\\\"").append(fieldNames[i]).append("\\\":\"").append(fieldNames[i]).append(");\n");
                if (i < fieldCount - 1) sb.append("        sb.append(\",\");\n");
            }

            sb.append("        sb.append(\"}\");\n");
            sb.append("        return sb.toString();\n");
            sb.append("    }\n\n");
        }

        // 随机添加比较方法 (30%概率)
        if (RandomUtils.randomBoolean() && RandomUtils.randomBoolean() && RandomUtils.randomBoolean()) {
            String compareMethod = METHOD_PREFIXES[RandomUtils.between(0, METHOD_PREFIXES.length - 1)] + "Difference";
            sb.append("    public float ").append(compareMethod).append("(").append(className).append(" other) {\n");
            sb.append("        float difference = 0;\n");

            for (int i = 0; i < fieldCount; i++) {
                sb.append("        difference += Math.abs(this.").append(fieldNames[i]).append(" - other.").append(fieldNames[i]).append(");\n");
            }

            sb.append("        return difference;\n");
            sb.append("    }\n\n");
        }

        // 随机添加克隆方法 (20%概率)
        if (RandomUtils.randomBoolean() && RandomUtils.randomBoolean() && RandomUtils.randomBoolean() && RandomUtils.randomBoolean()) {
            sb.append("    public ").append(className).append(" clone() {\n");
            sb.append("        return new ").append(className).append("(");

            for (int i = 0; i < fieldCount; i++) {
                sb.append("this.").append(fieldNames[i]);
                if (i < fieldCount - 1) sb.append(", ");
            }

            sb.append(");\n");
            sb.append("    }\n\n");
        }

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "geometry");
    }

    private String generateAreaMethod(String shapeType, String[] fieldNames) {
        StringBuilder sb = new StringBuilder();
        sb.append("        switch (").append(RandomUtils.generateWord(6)).append(") {\n");

        if (shapeType.equals("Circle")) {
            sb.append("            case \"Circle\":\n");
            sb.append("                return (float) (Math.PI * ").append(fieldNames[0]).append(" * ").append(fieldNames[0]).append(");\n");
        } else if (shapeType.equals("Rectangle") || shapeType.equals("Square")) {
            sb.append("            case \"Rectangle\":\n");
            sb.append("            case \"Square\":\n");
            sb.append("                return ").append(fieldNames[0]).append(" * ").append(fieldNames[1]).append(";\n");
        } else if (shapeType.equals("Triangle")) {
            sb.append("            case \"Triangle\":\n");
            sb.append("                return ").append(fieldNames[0]).append(" * ").append(fieldNames[1]).append(" / 2;\n");
        } else if (shapeType.equals("Ellipse")) {
            sb.append("            case \"Ellipse\":\n");
            sb.append("                return (float) (Math.PI * ").append(fieldNames[0]).append(" * ").append(fieldNames[1]).append(");\n");
        } else {
            sb.append("            default:\n");
            sb.append("                return 0;\n");
        }

        sb.append("        }\n");
        return sb.toString();
    }

    private String generatePerimeterMethod(String shapeType, String[] fieldNames) {
        StringBuilder sb = new StringBuilder();
        sb.append("        switch (").append(RandomUtils.generateWord(6)).append(") {\n");

        if (shapeType.equals("Circle")) {
            sb.append("            case \"Circle\":\n");
            sb.append("                return (float) (2 * Math.PI * ").append(fieldNames[0]).append(");\n");
        } else if (shapeType.equals("Rectangle") || shapeType.equals("Square")) {
            sb.append("            case \"Rectangle\":\n");
            sb.append("            case \"Square\":\n");
            sb.append("                return 2 * (").append(fieldNames[0]).append(" + ").append(fieldNames[1]).append(");\n");
        } else if (shapeType.equals("Triangle")) {
            sb.append("            case \"Triangle\":\n");
            sb.append("                return ").append(fieldNames[0]).append(" + ").append(fieldNames[1]).append(" + (float) Math.sqrt(")
                    .append(fieldNames[2]).append(" * ").append(fieldNames[2]).append(" + ").append(fieldNames[3]).append(" * ").append(fieldNames[3]).append(");\n");
        } else {
            sb.append("            default:\n");
            sb.append("                return 0;\n");
        }

        sb.append("        }\n");
        return sb.toString();
    }

    private String generateDistanceMethod(String[] fieldNames) {
        StringBuilder sb = new StringBuilder();
        sb.append("        return (float) Math.sqrt(");

        if (fieldNames.length >= 2) {
            sb.append(fieldNames[0]).append(" * ").append(fieldNames[0]);
            sb.append(" + ").append(fieldNames[1]).append(" * ").append(fieldNames[1]);
        } else {
            sb.append(fieldNames[0]).append(" * ").append(fieldNames[0]);
        }

        sb.append(");\n");
        return sb.toString();
    }

    private String generateBooleanMethod(String operation, String[] fieldNames) {
        StringBuilder sb = new StringBuilder();

        if (operation.equals("intersect")) {
            sb.append("        // Intersection logic based on shape type\n");
            sb.append("        return false;\n");
        } else if (operation.equals("contains")) {
            sb.append("        // Contains logic based on shape type\n");
            sb.append("        return false;\n");
        }

        return sb.toString();
    }

    private String generateGenericMethod(String operation, String[] fieldNames) {
        StringBuilder sb = new StringBuilder();
        sb.append("        // ").append(operation).append(" calculation\n");

        // 随机选择1-2个字段进行计算
        int usedFields = RandomUtils.between(1, Math.min(2, fieldNames.length));
        for (int i = 0; i < usedFields; i++) {
            int fieldIndex = RandomUtils.between(0, fieldNames.length - 1);
            sb.append("        float ").append(RandomUtils.generateWord(6)).append(" = ").append(fieldNames[fieldIndex]).append(";\n");
        }

        sb.append("        return 0;\n");
        return sb.toString();
    }
}
