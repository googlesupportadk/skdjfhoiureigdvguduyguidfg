package com.doow.rubbish.generator.resource;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.ResourceTracker;
import java.io.IOException;

public class DrawableGenerator extends BaseResourceGenerator {

    public DrawableGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成Drawable资源");

        generateShapes();
        generateSelectors();
        generateLayers();
        generateRipples();
    }

    private void generateShapes() throws IOException {
        int count = RandomUtils.between(10, 20);

        for (int i = 0; i < count; i++) {
            String shapeName = RandomUtils.generateDrawableName("shape");
            StringBuilder sb = new StringBuilder();

            sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
            sb.append("<shape xmlns:android="http://schemas.android.com/apk/res/android"\n");
            sb.append("    android:shape="").append(RandomUtils.generateRandomShape()).append("">\n");

            // 添加填充
            sb.append("    <solid\n");
            sb.append("        android:color="").append(RandomUtils.generateRandomColor()).append("" />\n");

            // 添加描边
            sb.append("    <stroke\n");
            sb.append("        android:width="").append(RandomUtils.generateRandomDimension()).append(""\n");
            sb.append("        android:color="").append(RandomUtils.generateRandomColor()).append("" />\n");

            // 添加圆角
            sb.append("    <corners\n");
            sb.append("        android:radius="").append(RandomUtils.generateRandomDimension()).append("" />\n");

            sb.append("</shape>");

            writeFile("drawable", shapeName + ".xml", sb.toString());
            ResourceTracker.addResource("drawable/" + shapeName + ".xml");
        }
    }

    private void generateSelectors() throws IOException {
        int count = RandomUtils.between(10, 20);

        for (int i = 0; i < count; i++) {
            String selectorName = RandomUtils.generateDrawableName("selector");
            StringBuilder sb = new StringBuilder();

            sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
            sb.append("<selector xmlns:android="http://schemas.android.com/apk/res/android">\n");

            // 按下状态
            sb.append("    <item android:state_pressed="true">\n");
            sb.append("        <shape>\n");
            sb.append("            <solid android:color="").append(RandomUtils.generateRandomColor()).append("" />\n");
            sb.append("        </shape>\n");
            sb.append("    </item>\n");

            // 聚焦状态
            sb.append("    <item android:state_focused="true">\n");
            sb.append("        <shape>\n");
            sb.append("            <solid android:color="").append(RandomUtils.generateRandomColor()).append("" />\n");
            sb.append("        </shape>\n");
            sb.append("    </item>\n");

            // 默认状态
            sb.append("    <item>\n");
            sb.append("        <shape>\n");
            sb.append("            <solid android:color="").append(RandomUtils.generateRandomColor()).append("" />\n");
            sb.append("        </shape>\n");
            sb.append("    </item>\n");

            sb.append("</selector>");

            writeFile("drawable", selectorName + ".xml", sb.toString());
            ResourceTracker.addResource("drawable/" + selectorName + ".xml");
        }
    }

    private void generateLayers() throws IOException {
        int count = RandomUtils.between(5, 10);

        for (int i = 0; i < count; i++) {
            String layerName = RandomUtils.generateDrawableName("layer");
            StringBuilder sb = new StringBuilder();

            sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
            sb.append("<layer-list xmlns:android="http://schemas.android.com/apk/res/android">\n");

            int layerCount = RandomUtils.between(2, 5);
            for (int j = 0; j < layerCount; j++) {
                sb.append("    <item>\n");
                sb.append("        <shape>\n");
                sb.append("            <solid android:color="").append(RandomUtils.generateRandomColor()).append("" />\n");
                sb.append("        </shape>\n");
                sb.append("    </item>\n");
            }

            sb.append("</layer-list>");

            writeFile("drawable", layerName + ".xml", sb.toString());
            ResourceTracker.addResource("drawable/" + layerName + ".xml");
        }
    }

    private void generateRipples() throws IOException {
        int count = RandomUtils.between(5, 10);

        for (int i = 0; i < count; i++) {
            String rippleName = RandomUtils.generateDrawableName("ripple");
            StringBuilder sb = new StringBuilder();

            sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
            sb.append("<ripple xmlns:android="http://schemas.android.com/apk/res/android"\n");
            sb.append("    android:color="").append(RandomUtils.generateRandomColor()).append("">\n");
            sb.append("    <item>\n");
            sb.append("        <shape>\n");
            sb.append("            <solid android:color="").append(RandomUtils.generateRandomColor()).append("" />\n");
            sb.append("            <corners\n");
            sb.append("                android:radius="").append(RandomUtils.generateRandomDimension()).append("" />\n");
            sb.append("        </shape>\n");
            sb.append("    </item>\n");
            sb.append("</ripple>");

            writeFile("drawable", rippleName + ".xml", sb.toString());
            ResourceTracker.addResource("drawable/" + rippleName + ".xml");
        }
    }
}
