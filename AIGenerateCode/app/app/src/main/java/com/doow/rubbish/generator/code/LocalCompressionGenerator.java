package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class LocalCompressionGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] COMPRESSION_TYPES = {
            "GZIP", "ZIP", "Deflate", "Inflate", "Custom",
            "LZ4", "Snappy", "Brotli", "LZMA", "XZ"
    };

    private static final String[] OPERATION_TYPES = {
            "compress", "decompress", "compressToBytes", "decompressFromBytes",
            "compressToString", "decompressFromString", "compressToFile", "decompressFromFile"
    };

    private static final String[] FIELD_TYPES = {
            "int", "long", "float", "double", "boolean", "String", "byte[]"
    };

    private static final String[] COMPRESSION_LEVELS = {
            "FASTEST", "NO_COMPRESSION", "BEST_SPEED", "DEFAULT_COMPRESSION", "BEST_COMPRESSION"
    };

    private static final String[] BUFFER_SIZES = {
            "1024", "2048", "4096", "8192", "16384"
    };

    private static final String[] ENCODING_TYPES = {
            "UTF-8", "UTF-16", "ISO-8859-1", "US-ASCII", "UTF-32"
    };

    public LocalCompressionGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成压缩类");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Compression");
            generateCompressionClass(className);
        }
    }

    private void generateCompressionClass(String className) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("compression"));

        sb.append(generateImportStatement("java.io.ByteArrayInputStream"));
        sb.append(generateImportStatement("java.io.ByteArrayOutputStream"));
        sb.append(generateImportStatement("java.io.IOException"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.zip.Deflater"));
        sb.append(generateImportStatement("java.util.zip.GZIPOutputStream"));
        sb.append(generateImportStatement("java.util.zip.Inflater"));
        sb.append(generateImportStatement("java.util.zip.ZipEntry"));
        sb.append(generateImportStatement("java.util.zip.ZipInputStream"));
        sb.append(generateImportStatement("java.util.zip.ZipOutputStream"));

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        String compressionType = COMPRESSION_TYPES[RandomUtils.between(0, COMPRESSION_TYPES.length - 1)];
        String operationType = OPERATION_TYPES[RandomUtils.between(0, OPERATION_TYPES.length - 1)];
        String compressionLevel = COMPRESSION_LEVELS[RandomUtils.between(0, COMPRESSION_LEVELS.length - 1)];
        String bufferSize = BUFFER_SIZES[RandomUtils.between(0, BUFFER_SIZES.length - 1)];
        String encodingType = ENCODING_TYPES[RandomUtils.between(0, ENCODING_TYPES.length - 1)];

        sb.append("public class ").append(className).append(" {\n\n");

        // 添加常量
        sb.append("    private static final String COMPRESSION_TYPE = \"" + "" + compressionType + "\");\n");
        sb.append("    private static final String OPERATION_TYPE = \"" + "" + operationType + "\");\n");
        sb.append("    private static final String COMPRESSION_LEVEL = \"" + "" + compressionLevel + "\");\n");
        sb.append("    private static final int BUFFER_SIZE = ").append(bufferSize).append(";\n");
        sb.append("    private static final String ENCODING_TYPE = \"" + "" + encodingType + "\");\n\n");

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

        // 添加压缩相关字段
        sb.append("    private int compressionCount;\n");
        sb.append("    private int decompressionCount;\n");
        sb.append("    private long totalCompressedSize;\n");
        sb.append("    private long totalDecompressedSize;\n");
        sb.append("    private double compressionRatio;\n");
        sb.append("    private List<byte[]> compressedDataList;\n");
        sb.append("    private Map<String, Integer> compressionStats;\n\n");

        // 构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this.compressionCount = 0;\n");
        sb.append("        this.decompressionCount = 0;\n");
        sb.append("        this.totalCompressedSize = 0;\n");
        sb.append("        this.totalDecompressedSize = 0;\n");
        sb.append("        this.compressionRatio = 0.0;\n");
        sb.append("        this.compressedDataList = new ArrayList<>();\n");
        sb.append("        this.compressionStats = new HashMap<>();\n");

        for (String fieldName : fieldNames) {
            if (!isStaticField(fieldName)) {
                String fieldType = getFieldType(fieldName);
                sb.append("        this.").append(fieldName).append(" = ").append(generateInitialValue(fieldType)).append(";\n");
            }
        }
        sb.append("    }\n\n");

        // 生成压缩方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public byte[] compress(byte[] data) {\n");
            sb.append("        if (data == null || data.length == 0) {\n");
            sb.append("            return new byte[0];\n");
            sb.append("        }\n");
            sb.append("        try {\n");
            sb.append("            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();\n");
            sb.append("            switch (COMPRESSION_TYPE) {\n");
            sb.append("                case \"GZIP\":\n");
            sb.append("                    GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);\n");
            sb.append("                    gzipOutputStream.write(data);\n");
            sb.append("                    gzipOutputStream.close();\n");
            sb.append("                    break;\n");
            sb.append("                case \"Deflate\":\n");
            sb.append("                    Deflater deflater = new Deflater();\n");
            sb.append("                    deflater.setInput(data);\n");
            sb.append("                    deflater.finish();\n");
            sb.append("                    byte[] buffer = new byte[BUFFER_SIZE];\n");
            sb.append("                    while (!deflater.finished()) {\n");
            sb.append("                        int count = deflater.deflate(buffer);\n");
            sb.append("                        outputStream.write(buffer, 0, count);\n");
            sb.append("                    }\n");
            sb.append("                    deflater.end();\n");
            sb.append("                    break;\n");
            sb.append("                case \"ZIP\":\n");
            sb.append("                    ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);\n");
            sb.append("                    ZipEntry entry = new ZipEntry(\"compressed_data\");\n");
            sb.append("                    zipOutputStream.putNextEntry(entry);\n");
            sb.append("                    zipOutputStream.write(data);\n");
            sb.append("                    zipOutputStream.closeEntry();\n");
            sb.append("                    zipOutputStream.close();\n");
            sb.append("                    break;\n");
            sb.append("                default:\n");
            sb.append("                    outputStream.write(data);\n");
            sb.append("                    break;\n");
            sb.append("            }\n");
            sb.append("            byte[] result = outputStream.toByteArray();\n");
            sb.append("            updateCompressionStats(data.length, result.length);\n");
            sb.append("            return result;\n");
            sb.append("        } catch (IOException e) {\n");
            sb.append("            return new byte[0];\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成解压缩方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public byte[] decompress(byte[] compressedData) {\n");
            sb.append("        if (compressedData == null || compressedData.length == 0) {\n");
            sb.append("            return new byte[0];\n");
            sb.append("        }\n");
            sb.append("        try {\n");
            sb.append("            ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);\n");
            sb.append("            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();\n");
            sb.append("            switch (COMPRESSION_TYPE) {\n");
            sb.append("                case \"GZIP\":\n");
            sb.append("                    java.util.zip.GZIPInputStream gzipInputStream = new java.util.zip.GZIPInputStream(inputStream);\n");
            sb.append("                    byte[] buffer = new byte[BUFFER_SIZE];\n");
            sb.append("                    int len;\n");
            sb.append("                    while ((len = gzipInputStream.read(buffer)) > 0) {\n");
            sb.append("                        outputStream.write(buffer, 0, len);\n");
            sb.append("                    }\n");
            sb.append("                    gzipInputStream.close();\n");
            sb.append("                    break;\n");
            sb.append("                case \"Inflate\":\n");
            sb.append("                    Inflater inflater = new Inflater();\n");
            sb.append("                    inflater.setInput(compressedData);\n");
            sb.append("                    byte[] buffer = new byte[BUFFER_SIZE];\n");
            sb.append("                    while (!inflater.finished()) {\n");
            sb.append("                        int count = inflater.inflate(buffer);\n");
            sb.append("                        outputStream.write(buffer, 0, count);\n");
            sb.append("                    }\n");
            sb.append("                    inflater.end();\n");
            sb.append("                    break;\n");
            sb.append("                case \"ZIP\":\n");
            sb.append("                    ZipInputStream zipInputStream = new ZipInputStream(inputStream);\n");
            sb.append("                    ZipEntry entry = zipInputStream.getNextEntry();\n");
            sb.append("                    byte[] buffer = new byte[BUFFER_SIZE];\n");
            sb.append("                    int len;\n");
            sb.append("                    while ((len = zipInputStream.read(buffer)) > 0) {\n");
            sb.append("                        outputStream.write(buffer, 0, len);\n");
            sb.append("                    }\n");
            sb.append("                    zipInputStream.closeEntry();\n");
            sb.append("                    zipInputStream.close();\n");
            sb.append("                    break;\n");
            sb.append("                default:\n");
            sb.append("                    outputStream.write(compressedData);\n");
            sb.append("                    break;\n");
            sb.append("            }\n");
            sb.append("            byte[] result = outputStream.toByteArray();\n");
            sb.append("            updateDecompressionStats(compressedData.length, result.length);\n");
            sb.append("            return result;\n");
            sb.append("        } catch (IOException e) {\n");
            sb.append("            return new byte[0];\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成字符串压缩方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public byte[] compressString(String data) {\n");
            sb.append("        if (data == null || data.isEmpty()) {\n");
            sb.append("            return new byte[0];\n");
            sb.append("        }\n");
            sb.append("        try {\n");
            sb.append("            byte[] bytes = data.getBytes(ENCODING_TYPE);\n");
            sb.append("            return compress(bytes);\n");
            sb.append("        } catch (Exception e) {\n");
            sb.append("            return new byte[0];\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成字符串解压缩方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public String decompressToString(byte[] compressedData) {\n");
            sb.append("        if (compressedData == null || compressedData.length == 0) {\n");
            sb.append("            return \"\n");
            sb.append("        }\n");
            sb.append("        try {\n");
            sb.append("            byte[] decompressed = decompress(compressedData);\n");
            sb.append("            return new String(decompressed, ENCODING_TYPE);\n");
            sb.append("        } catch (Exception e) {\n");
            sb.append("            return \"\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 生成更新压缩统计方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private void updateCompressionStats(int originalSize, int compressedSize) {\n");
            sb.append("        compressionCount++;\n");
            sb.append("        totalCompressedSize += compressedSize;\n");
            sb.append("        totalDecompressedSize += originalSize;\n");
            sb.append("        compressionRatio = (double) compressedSize / originalSize;\n");
            sb.append("        compressedDataList.add(new byte[compressedSize]);\n");
            sb.append("        compressionStats.put(\"compression_\n");
            sb.append("        compressionCount, compressedSize);\n");
            sb.append("    }\n\n");
        }

        // 生成更新解压缩统计方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    private void updateDecompressionStats(int compressedSize, int decompressedSize) {\n");
            sb.append("        decompressionCount++;\n");
            sb.append("        totalCompressedSize += compressedSize;\n");
            sb.append("        totalDecompressedSize += decompressedSize;\n");
            sb.append("        compressionRatio = (double) compressedSize / decompressedSize;\n");
            sb.append("        compressionStats.put(\"decompression_\n");
            sb.append("        decompressionCount, decompressedSize);\n");
            sb.append("    }\n\n");
        }

        // 生成与计算器关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public double calculateCompressionRatio() {\n");
            sb.append("        if (totalDecompressedSize == 0) {\n");
            sb.append("            return 0.0;\n");
            sb.append("        }\n");
            sb.append("        return (double) totalCompressedSize / totalDecompressedSize;\n");
            sb.append("    }\n\n");
        }

        // 生成与图表关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public double[] getCompressionDataPoints() {\n");
            sb.append("        double[] dataPoints = new double[5];\n");
            sb.append("        dataPoints[0] = compressionCount;\n");
            sb.append("        dataPoints[1] = decompressionCount;\n");
            sb.append("        dataPoints[2] = totalCompressedSize;\n");
            sb.append("        dataPoints[3] = totalDecompressedSize;\n");
            sb.append("        dataPoints[4] = compressionRatio;\n");
            sb.append("        return dataPoints;\n");
            sb.append("    }\n\n");
        }

        // 生成与集合关联的方法
        if (RandomUtils.randomBoolean()) {
            sb.append("    public List<byte[]> getCompressedDataList() {\n");
            sb.append("        return compressedDataList;\n");
            sb.append("    }\n\n");
        }

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "compression");
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
            case "byte[]":
                return "new byte[0]";
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
        } else if (fieldName.toLowerCase().endsWith("bytes")) {
            return "byte[]";
        } else {
            return "Object";
        }
    }
}
