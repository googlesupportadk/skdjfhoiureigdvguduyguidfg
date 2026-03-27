package com.doow.rubbish.generator.resource;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.ResourceTracker;
import java.io.FileWriter;
import java.io.IOException;

public class ValuesGenerator extends BaseResourceGenerator {

    public ValuesGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成Values资源");

        generateStrings();
        generateColors();
        generateDimensions();
        generateStyles();
        generateIntegers();
        generateArrays();
    }

    private void generateStrings() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
        sb.append("<resources>\n");

        // 生成应用名称
        sb.append("    <string name="app_name">").append(GeneratorConfig.appName).append("</string>\n");

        // 生成常用字符串
        String[] commonStrings = {
            "ok", "cancel", "yes", "no", "save", "delete", "edit", "add",
            "search", "filter", "sort", "refresh", "loading", "error", "success", "warning"
        };

        for (String str : commonStrings) {
            sb.append("    <string name="").append(str.toLowerCase()).append("">").append(str).append("</string>\n");
        }

        // 生成随机字符串
        for (int i = 0; i < RandomUtils.between(20, 50); i++) {
            String name = RandomUtils.generateStringName();
            String value = RandomUtils.generateRandomString(10, 50);
            sb.append("    <string name="").append(name).append("">").append(value).append("</string>\n");
        }

        sb.append("</resources>");

        writeFile("values", "strings.xml", sb.toString());
        ResourceTracker.addResource("values/strings.xml");
    }

    private void generateColors() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
        sb.append("<resources>\n");

        // 生成常用颜色
        String[] commonColors = {
            "primary", "secondary", "accent", "background", "surface",
            "error", "on_primary", "on_secondary", "on_background", "on_surface", "on_error"
        };

        for (String color : commonColors) {
            sb.append("    <color name="").append(color).append("">")
              .append(RandomUtils.generateRandomColor()).append("</color>\n");
        }

        // 生成随机颜色
        for (int i = 0; i < RandomUtils.between(20, 50); i++) {
            String name = RandomUtils.generateColorName();
            sb.append("    <color name="").append(name).append("">")
              .append(RandomUtils.generateRandomColor()).append("</color>\n");
        }

        sb.append("</resources>");

        writeFile("values", "colors.xml", sb.toString());
        ResourceTracker.addResource("values/colors.xml");
    }

    private void generateDimensions() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
        sb.append("<resources>\n");

        // 生成常用尺寸
        String[] commonDimensions = {
            "margin_small", "margin_medium", "margin_large",
            "padding_small", "padding_medium", "padding_large",
            "text_size_small", "text_size_medium", "text_size_large",
            "icon_size_small", "icon_size_medium", "icon_size_large"
        };

        for (String dim : commonDimensions) {
            sb.append("    <dimen name="").append(dim).append("">")
              .append(RandomUtils.generateRandomDimension()).append("</dimen>\n");
        }

        // 生成随机尺寸
        for (int i = 0; i < RandomUtils.between(10, 30); i++) {
            String name = RandomUtils.generateDimensionName();
            sb.append("    <dimen name="").append(name).append("">")
              .append(RandomUtils.generateRandomDimension()).append("</dimen>\n");
        }

        sb.append("</resources>");

        writeFile("values", "dimens.xml", sb.toString());
        ResourceTracker.addResource("values/dimens.xml");
    }

    private void generateStyles() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
        sb.append("<resources>\n");

        // 生成基础主题
        sb.append("    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">\n");
        sb.append("        <item name="colorPrimary">@color/primary</item>\n");
        sb.append("        <item name="colorPrimaryDark">@color/primary</item>\n");
        sb.append("        <item name="colorAccent">@color/accent</item>\n");
        sb.append("    </style>\n\n");

        // 生成随机样式
        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String styleName = RandomUtils.generateStyleName();
            sb.append("    <style name="").append(styleName).append("">\n");

            int itemCount = RandomUtils.between(2, 5);
            for (int j = 0; j < itemCount; j++) {
                String item = RandomUtils.generateStyleItem();
                sb.append("        <item name="").append(item).append("">")
                  .append(RandomUtils.generateRandomStyleValue()).append("</item>\n");
            }

            sb.append("    </style>\n\n");
        }

        sb.append("</resources>");

        writeFile("values", "styles.xml", sb.toString());
        ResourceTracker.addResource("values/styles.xml");
    }

    private void generateIntegers() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
        sb.append("<resources>\n");

        // 生成随机整数
        for (int i = 0; i < RandomUtils.between(10, 30); i++) {
            String name = RandomUtils.generateIntegerName();
            int value = RandomUtils.generateRandomInteger(0, 100);
            sb.append("    <integer name="").append(name).append("">").append(value).append("</integer>\n");
        }

        sb.append("</resources>");

        writeFile("values", "integers.xml", sb.toString());
        ResourceTracker.addResource("values/integers.xml");
    }

    private void generateArrays() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
        sb.append("<resources>\n");

        // 生成字符串数组
        for (int i = 0; i < RandomUtils.between(5, 10); i++) {
            String arrayName = RandomUtils.generateArrayName();
            sb.append("    <string-array name="").append(arrayName).append("">\n");

            int itemCount = RandomUtils.between(3, 10);
            for (int j = 0; j < itemCount; j++) {
                sb.append("        <item>").append(RandomUtils.generateRandomString(5, 20)).append("</item>\n");
            }

            sb.append("    </string-array>\n\n");
        }

        // 生成整数数组
        for (int i = 0; i < RandomUtils.between(3, 8); i++) {
            String arrayName = RandomUtils.generateIntegerArrayName();
            sb.append("    <integer-array name="").append(arrayName).append("">\n");

            int itemCount = RandomUtils.between(3, 10);
            for (int j = 0; j < itemCount; j++) {
                sb.append("        <item>").append(RandomUtils.generateRandomInteger(0, 100)).append("</item>\n");
            }

            sb.append("    </integer-array>\n\n");
        }

        sb.append("</resources>");

        writeFile("values", "arrays.xml", sb.toString());
        ResourceTracker.addResource("values/arrays.xml");
    }
}
