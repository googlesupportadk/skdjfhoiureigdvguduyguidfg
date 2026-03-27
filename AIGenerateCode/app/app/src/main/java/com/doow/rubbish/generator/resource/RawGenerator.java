package com.doow.rubbish.generator.resource;

import com.doow.rubbish.generator.GeneratorConfig;
import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.ResourceTracker;
import com.doow.rubbish.generator.VariationManager;

import java.io.IOException;

public class RawGenerator extends BaseResourceGenerator {

    protected VariationManager variationManager;

    public RawGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws IOException {
        int count = RandomUtils.between(GeneratorConfig.rawMin, GeneratorConfig.rawMax);
        System.out.println("生成 " + count + " 个raw资源");

        for (int i = 0; i < count; i++) {
            String name = RandomUtils.generateName("raw");
            ResourceTracker.addRaw(name);
            generateRawFile(name);
        }
    }

    private void generateRawFile(String name) throws IOException {
        int fileType = RandomUtils.between(0, 5);
        String content = "";
        String extension = "";

        switch (fileType) {
            case 0:
                content = generateTextContent();
                extension = ".txt";
                break;
            case 1:
                content = generateJsonContent();
                extension = ".json";
                break;
            case 2:
                content = generateXmlContent();
                extension = ".xml";
                break;
            case 3:
                content = generateHtmlContent();
                extension = ".html";
                break;
            case 4:
                content = generateFontContent();
                extension = ".ttf";
                break;
            case 5:
                content = generateFontContent();
                extension = ".otf";
                break;
        }

        generateResourceFile(name + extension, content, "raw");
    }

    private String generateTextContent() {
        StringBuilder sb = new StringBuilder();
        int lineCount = RandomUtils.between(5, 20);

        for (int i = 0; i < lineCount; i++) {
            sb.append(RandomUtils.generateWord(RandomUtils.between(3, 8)));
            sb.append(" ");
            sb.append(RandomUtils.generateWord(RandomUtils.between(3, 8)));
            sb.append("\n");
        }

        return sb.toString();
    }

    private String generateJsonContent() {
        StringBuilder sb = new StringBuilder();
        int itemCount = RandomUtils.between(3, 8);

        sb.append("{\n");
        for (int i = 0; i < itemCount; i++) {
            sb.append("    \"" + RandomUtils.generateWord(6) + "\"" + ": {\n");
            sb.append("        \"" + "id\"" + ": " + i + ",\n");
            sb.append("        \"" + "name\"" + ": \"" + RandomUtils.generateWord(RandomUtils.between(3, 8)) + "\"" + ",\n");
            sb.append("        \"" + "value\"" + ": " + RandomUtils.generateWord(RandomUtils.between(3, 8)) + "\n");
            sb.append("    }\n");
        }
        sb.append("}\n");

        return sb.toString();
    }

    private String generateXmlContent() {
        StringBuilder sb = new StringBuilder();
        int itemCount = RandomUtils.between(2, 5);

        sb.append("<?xml version=\"" + "1.0\"" + " encoding=\"" + "utf-8\"" + "?>\n");
        sb.append("<root>\n");

        for (int i = 0; i < itemCount; i++) {
            sb.append("    <item\n");
            sb.append("        id=\"" + "item_" + i + "\"" + "\n");
            sb.append("        name=\"" + RandomUtils.generateWord(RandomUtils.between(3, 8)) + "\"" + "\n");
            sb.append("        value=\"" + RandomUtils.generateWord(RandomUtils.between(3, 8)) + "\"" + "\n");
            sb.append("    />\n");
        }

        sb.append("</root>");

        return sb.toString();
    }

    private String generateHtmlContent() {
        StringBuilder sb = new StringBuilder();
        int elementCount = RandomUtils.between(3, 6);

        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("    <meta charset=\"" + "UTF-8\"" + ">\n");
        sb.append("    <title>" + RandomUtils.generateWord(RandomUtils.between(3, 8)) + "</title>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");

        for (int i = 0; i < elementCount; i++) {
            String tag = RandomUtils.nextBoolean() ? "div" : "span";
            sb.append("    <" + tag + "\n");
            sb.append("        style=\"" + "color: #" + Integer.toHexString(RandomUtils.between(0, 0xFFFFFF)) + ";\n" + "\n");
            sb.append("    >\n");
            sb.append("        " + RandomUtils.generateWord(RandomUtils.between(3, 8)) + "\n");
            sb.append("    </" + tag + ">\n");
        }

        sb.append("</body>\n");
        sb.append("</html>");

        return sb.toString();
    }

    private String generateFontContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("OTTO\n");
        sb.append(RandomUtils.generateWord(8));
        for (int i = 0; i < 100; i++) {
            sb.append(" 0 0 0 0 0\n");
        }
        return sb.toString();
    }
}
