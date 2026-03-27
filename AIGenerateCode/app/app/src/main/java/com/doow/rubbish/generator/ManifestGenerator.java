package com.doow.rubbish.generator;

import java.io.FileWriter;
import java.io.IOException;


public class ManifestGenerator {
    private String projectRoot;
    private String packageName;

    public ManifestGenerator(String projectRoot, String packageName) {
        this.projectRoot = projectRoot;
        this.packageName = packageName;
    }

    public void generate() throws IOException {
        StringBuilder sb = new StringBuilder();


        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");


        sb.append("<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"\n");
        sb.append("    package=\"").append(packageName).append("\">\n\n");





        sb.append("    <application\n");
        sb.append("        android:allowBackup=\"true\"\n");
        sb.append("        android:icon=\"@mipmap/ic_launcher\"\n");
        sb.append("        android:label=\"@string/app_name\"\n");
        sb.append("        android:roundIcon=\"@mipmap/ic_launcher_round\"\n");
        sb.append("        android:supportsRtl=\"true\"\n");
        sb.append("        android:theme=\"@style/AppTheme\">\n\n");


        sb.append("        <!-- Activities -->\n");
        for (int i = 1; i <= GeneratorConfig.packageCount; i++) {
            String subPackage = "feature" + i;
            sb.append("        <!-- ").append(subPackage).append(" package activities -->\n");
            for (int j = 0; j < GeneratorConfig.activityCountPerPackage; j++) {
                String activityName = packageName + "." + subPackage + ".Activity" + (j + 1);
                sb.append("        <activity\n");
                sb.append("            android:name=\"").append(activityName).append("\"\n");
                if (i == 1 && j == 0) {
                    sb.append("            android:exported=\"true\">\n");
                    sb.append("            <intent-filter>\n");
                    sb.append("                <action android:name=\"android.intent.action.MAIN\"/>\n");
                    sb.append("                <category android:name=\"android.intent.category.LAUNCHER\"/>\n");
                    sb.append("            </intent-filter>\n");
                } else {
                    sb.append("            android:exported=\"false\"/>\n");
                }
                sb.append("        </activity>\n");
            }
            sb.append("\n");
        }

        // Application结束
        sb.append("    </application>\n");

        // Manifest结束
        sb.append("</manifest>");

        // 写入文件
        String manifestPath = projectRoot + "/app/src/main/AndroidManifest.xml";
        try (FileWriter writer = new FileWriter(manifestPath)) {
            writer.write(sb.toString());
        }

        System.out.println("AndroidManifest.xml 生成完成");
    }
}
